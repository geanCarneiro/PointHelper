package persistencia;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BaseEntity {

    @Coluna(tipo = "INT")
    @ChavePrimaria
    private long id = -1L;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BaseEntity{\n");
        for (Field field : GenericDAO.getAllFields(this.getClass())) {
            String value;
            try {
                Method getMethod = GenericDAO.getGetMethodByField(field);
                value = String.valueOf(getMethod.invoke(this));
            } catch (Exception ex) {
                ex.printStackTrace();
                value = "ERROR";
            }

            sb.append("  ").append(field.getName()).append("=").append(value).append(";\n");
        }
        sb.append('}');
        return sb.toString();
    }
}
