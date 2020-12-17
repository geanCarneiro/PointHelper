package persistencia;

import java.util.Arrays;
import java.util.List;

public class JornadaDAO extends GenericDAO<Jornada> {

    public static Jornada saveByDate(Jornada jornada){

        List<Jornada> jornadas = queryByFilter(Jornada.class, Arrays.asList("JOR_DATA = '" + jornada.getData() + "'"));
        if(jornadas.size() > 0) jornada.setId(jornadas.get(0).getId());

        try{
            if(jornada.getId() == -1L) return insert(jornada);
            return update(jornada);
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static Jornada getByDate(String date){
        List<Jornada> jornadas = queryByFilter(Jornada.class, Arrays.asList("JOR_DATA = '" + date + "'"));

        return (jornadas.size() > 0) ? jornadas.get(0) : null;
    }

}
