package customClass;


import main.Main;
import org.jdesktop.swingx.calendar.DateSelectionModel;
import persistencia.Jornada;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JXDatePicker extends org.jdesktop.swingx.JXDatePicker {

    public JXDatePicker(PropertyChangeListener listener){
        super();

        this.addPropertyChangeListener(listener);

        this.setFormats("dd/MM/yyyy");

        this.setDate(new Date());

        this.getMonthView().setFlaggedDayForeground(Color.GREEN);
        this.getMonthView().setUpperBound(new Date());


    }

    public void setFlaggedDates(String... inDates){
        List<Date> dates = Stream.of(inDates).map(date -> {
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
        this.getMonthView().setFlaggedDates(dates.toArray(new Date[dates.size()]));
    }

    public void setFlaggedDatesByJornadas(List<Jornada> jornadas){

        final ArrayList<String> dates = new ArrayList<>();
        jornadas = jornadas.stream().filter(Jornada::isJornadaValida).collect(Collectors.toList());
        jornadas.forEach(jornada1 -> dates.add(jornada1.getData()));

        this.setFlaggedDates(dates.toArray(new String[dates.size()]));
    }

    public String getStringDate(){
        return Main.SIMPLE_DATE_FORMAT.format(super.getDate());
    }

}
