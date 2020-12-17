package persistencia;

import main.Main;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericDAO<T extends BaseEntity> {

    public static Connection connection = null;

    public static void openConnection() throws Exception {
        if(connection == null){
            Class.forName(Main.DRIVERPATH);
            connection = DriverManager.getConnection(Main.URL, Main.USER, Main.PASS);
            connection.setAutoCommit(false);
        }
    }

    public static void closeConnection(){
        if (connection == null) return;
        try {
            connection.close();
        } catch (SQLException e) {}
    }

    public static void commit(){
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback(){
        try {
            connection.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static ResultSet query(String sql){
        try {
            return connection.createStatement().executeQuery(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static <T extends BaseEntity> T save(T entity){
        try{
            if(entity.getId() == -1L) return insert(entity);
            return update(entity);
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static <T extends BaseEntity> List<T> query(Class<T> entity){
        ArrayList<T> out = new ArrayList<>();
        String tableName = getTableName(entity);
        List<String> columnList = getAllFields(entity).stream()
                        .map(field -> {
                            return tableName + "_" + getColumnName(field);
                        }).collect(Collectors.toList());

        String sql = "SELECT " + String.join(", ", columnList) + " FROM " + tableName;
        System.out.println(sql);

        ResultSet rs = query(sql);
        T newEntity;
        if(rs != null){
            try {
                while (rs.next()) {
                    newEntity = entity.getDeclaredConstructor().newInstance();

                    for(Field field : getAllFields(entity)) {
                        getSetMethodByField(field).invoke(newEntity, rs.getObject(tableName + "_"+ getColumnName(field)));
                    }

                    out.add(newEntity);
                }
                return out;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return out;
    }

    public static <T extends BaseEntity> List<T> queryByFilter(Class<T> entity, List<String> filtro){
        ArrayList<T> out = new ArrayList<>();
        String tableName = getTableName(entity);
        List<String> columnList = getAllFields(entity).stream()
                .map(field -> {
                    return tableName + "_" + getColumnName(field);
                }).collect(Collectors.toList());

        String sql = "SELECT " + String.join(", ", columnList) + " FROM " + tableName
                        + " WHERE " + String.join(" AND ", filtro);

        System.out.println(sql);

        ResultSet rs = query(sql);
        T newEntity;
        if(rs != null){
            try {
                while (rs.next()) {
                    newEntity = entity.getDeclaredConstructor().newInstance();

                    for(Field field : getAllFields(entity)) {
                        getSetMethodByField(field).invoke(newEntity, rs.getObject(tableName + "_"+ getColumnName(field)));
                    }

                    out.add(newEntity);
                }
                return out;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return out;
    }

    public static <T extends BaseEntity> T get(Class<T> entity, long id){
        try {
            T out = entity.getDeclaredConstructor().newInstance();
            String tableName = getTableName(entity);
            List<String> columnList = getAllFields(entity).stream()
                    .map(field -> {
                        return tableName + "_" + getColumnName(field);
                    }).collect(Collectors.toList());

            Field pkField = getPkField(entity);

            String sql = "SELECT " + String.join(", ", columnList) + " FROM " + tableName + " WHERE " + tableName + "_" + getColumnName(pkField) + "=" + id;
            System.out.println(sql);

            ResultSet rs = query(sql);
            if (rs != null && rs.next()) {

                for (Field field : getAllFields(entity)) {
                    getSetMethodByField(field).invoke(out, rs.getObject(tableName + "_" + getColumnName(field)));
                }

                return out;
            }

            return null;
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    public static <T extends BaseEntity> T insert(T entity){
        List<Field> fields = getAllFields(entity.getClass()).stream()
                .filter(field -> field.getDeclaredAnnotation(ChavePrimaria.class) == null)
                .collect(Collectors.toList());
        ArrayList<Object> values = new ArrayList<>(fields.stream().map(field -> getValueByField(entity, field)).collect(Collectors.toList()));
        String tableName = getTableName(entity.getClass());

        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ").append(tableName);
        builder.append("(").append(String.join(", ", fields.stream().map(field -> tableName + "_" + getColumnName(field)).collect(Collectors.toList()))).append(") ");
        builder.append("VALUES (").append(String.join(", ", fields.stream().map(field -> "?").collect(Collectors.toList())));
        builder.append(")");
        try {
            PreparedStatement ps = connection.prepareStatement(builder.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
            for(int i = 0; i < values.size(); i++)
                ps.setObject(i+1, values.get(i));

            ps.execute();
            System.out.println(ps.toString());
            ResultSet generatedKeyRs = ps.getGeneratedKeys();
            if(generatedKeyRs.next())
                entity.setId(generatedKeyRs.getLong(1));

            return entity;
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static <T extends BaseEntity> T update(T entity){
        if(get(entity.getClass(), entity.getId()) != null) {
            try {
                List<Field> fields = getAllFields(entity.getClass()).stream()
                        .filter(field -> field.getDeclaredAnnotation(ChavePrimaria.class) == null).collect(Collectors.toList());
                ArrayList<Object> values = new ArrayList<>(fields.stream().map(field -> getValueByField(entity, field)).collect(Collectors.toList()));
                values.add(getValueByField(entity, getPkField(entity.getClass())));
                String tableName = getTableName(entity.getClass());

                StringBuilder builder = new StringBuilder();
                builder.append("UPDATE ").append(tableName).append(" SET ");
                builder.append(String.join(", ", fields.stream().map(field -> tableName + "_" + getColumnName(field) + " = ?").collect(Collectors.toList())));
                builder.append(" WHERE ").append(tableName + "_" + getColumnName(getPkField(entity.getClass()))).append(" = ?");

                PreparedStatement ps = connection.prepareStatement(builder.toString());
                for(int i = 0; i < values.size(); i++)
                    ps.setObject(i+1, values.get(i));

                ps.execute();
                System.out.println(ps.toString());

                return entity;
            } catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static <T extends BaseEntity> T delete(T entity){
        T out = (T) get(entity.getClass(), entity.getId());

        if(out != null){
            String tableName = getTableName(entity.getClass());
            Field pkField = getPkField(entity.getClass());

            StringBuilder builder = new StringBuilder();
            builder.append("DELETE FROM ").append(tableName);
            builder.append(" WHERE ").append(tableName + "_" + getColumnName(pkField)).append(" = ?");
            System.out.println(builder.toString());

            try {
                PreparedStatement ps = connection.prepareStatement(builder.toString());

                ps.setLong(1, entity.getId());
                ps.execute();

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        return out;
    }

    public static <T extends BaseEntity> boolean createTableFromEntity(Class<T> entity){
        String tableName = getTableName(entity);

        List<String> campos = getAllFields(entity).stream()
                .map(field -> {
                    Coluna coluna = field.getAnnotation(Coluna.class);
                    Nullable nullable = field.getAnnotation(Nullable.class);
                    ChavePrimaria chavePrimaria = field.getAnnotation(ChavePrimaria.class);

                    StringBuilder builder = new StringBuilder();
                    builder.append(tableName + "_" + getColumnName(field) + " ");
                    builder.append(((chavePrimaria != null) ? "IDENTITY" : coluna.tipo()) + " ");
                    builder.append((chavePrimaria != null) ? "PRIMARY KEY" : "NULL");
                    return builder.toString();
                }).collect(Collectors.toList());
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + String.join(", ", campos) + ")";

        System.out.println(sql);
        try {
            connection.createStatement().executeUpdate(sql);
            return true;
        } catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    private static <T extends BaseEntity> Method getSetMethodByField(Field field){
        try {
            return field.getDeclaringClass().getDeclaredMethod("set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), field.getType());
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static <T extends BaseEntity> Object getValueByField(T entity, Field field){
        try {
            return getGetMethodByField(field).invoke(entity);
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static Method getGetMethodByField(Field field){
        try {
            return field.getDeclaringClass().getDeclaredMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static <T extends BaseEntity> Field getPkField(Class<T> entity){
        for(Field field : getAllFields(entity)){
            if(field.getDeclaredAnnotation(ChavePrimaria.class) != null){
                return field;
            }
        }
        return null;
    }

    private static String getColumnName(Field field){
        Coluna coluna = field.getDeclaredAnnotation(Coluna.class);

        return ((coluna == null || coluna.nome().isEmpty()) ? field.getName() : coluna.nome()).toUpperCase();
    }

    private static <T extends BaseEntity> String getTableName(Class<T> entity){
        Tabela tabela = entity.getDeclaredAnnotation(Tabela.class);

        return ((tabela == null) ? entity.getSimpleName() : tabela.nome()).toUpperCase();
    }

    public static <T extends BaseEntity> List<Field> getAllFields(Class<T> classe){
        ArrayList<Field> fields = new ArrayList<>();
        for(Class c = classe; c != Object.class; c = c.getSuperclass())
            fields.addAll(0, Arrays.asList(c.getDeclaredFields()));

        return fields.stream().filter(field ->  field.getAnnotation(Coluna.class) != null).collect(Collectors.toList());
    }

    public static boolean isConnectionOpen(){
        return connection != null;
    }

}
