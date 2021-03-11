import java.text.*;
import java.util.* ;

public class DateStamp {

    private static Calendar cal = new GregorianCalendar();
    static DateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    //DateFormat: Oberklasse & SimpleDateFormat:Unterklasse

    public static String printTime (){          //change to private???
        return (dateformatter.format(cal.getTime()));
    }
}
