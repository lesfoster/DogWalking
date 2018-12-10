/*
 * Created on Jan 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package time;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author whitteng
 *
 * CalCustome extends GregorianCalendar to <br>
 * provide adding a time based on another Calendar time <br>
 * NOT just elements (e.g. Day, Year, Hour..)
 * 
 * see http://www.javaworld.com/javaworld/jw-03-2001/jw-0330-time_p.html <br>
 *   and <br>
 * http://www.javaworld.com/javaworld/jw-03-2001/jw-0330-time_p.html
 * 
 */
public class CalCustom extends GregorianCalendar
{
    /**
     * @param year
     * @param month
     * @param date
     */
    public CalCustom(int year, int month, int date)
    {
        super(year, month, date);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     */
    public CalCustom(int year, int month, int date, int hour, int minute)
    {
        super(year, month, date, hour, minute);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param second
     */
    public CalCustom(int year, int month, int date, int hour, int minute,
            int second)
    {
        super(year, month, date, hour, minute, second);
        // TODO Auto-generated constructor stub
    }
    
    public CalCustom ()
    {
        
    }
    
    static public CalCustom createZeroCalCustom ()
    {
        CalCustom calCustom = new CalCustom (0, 0, 0, 0, 0, 0);
        //calCustom.clear();
        calCustom.setTimeInMillis((long) 0);
        
        return (calCustom);
    }
    
    public long add (Calendar calAdd)    
    {
       long timeMillisAdd = calAdd.getTimeInMillis();
       long timeMillisThis = getTimeInMillis();
       long timeMillisNew = timeMillisThis + timeMillisAdd;

       setTimeInMillis(timeMillisNew);
       
       return (timeMillisNew);
    }
    
    public String outputTime ()
    {
        Date thisDate = getTime();
        DateFormat dfTime = DateFormat.getTimeInstance(DateFormat.SHORT);
        String timeString = dfTime.format(thisDate);
        
        return (timeString);
    }
    
    public String outputTimeSubYear ()
    {
        return (outputTimeSubYear(false));
    }
    
    public String outputTimeSubYear (boolean showSeconds)
    {
        int dayOfYear = get (Calendar.DAY_OF_YEAR);
        int weeks = dayOfYear / 7;
        int days = dayOfYear % 7;
        int hours = get (Calendar.HOUR_OF_DAY);
        int minutes = get (Calendar.MINUTE);
        int seconds = get (Calendar.SECOND);
        
        StringBuffer buf = new StringBuffer();
        
        if (weeks > 0)
        { buf.append (weeks + " weeks, "); }
        
        if (days > 0)
        { buf.append (days + " days, "); }
        
        if (hours > 0)
        { buf.append (hours + " hours, "); }
        
        if (minutes > 0)
        { buf.append (minutes + " minutes "); }

        if (showSeconds && (days > 0))
        { buf.append ("and " + seconds + " seconds "); }
        
        return (buf.toString());
    }
    
    
}
