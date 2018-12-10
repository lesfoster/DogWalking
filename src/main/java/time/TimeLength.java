/*
 * Created on Jan 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package time;

import java.util.Calendar;



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
public class TimeLength
{
    long weeks = 0;
    long days = 0;
    long hours = 0;
    long minutes = 0;
    long seconds = 0;
    long milliseconds = 0;
    long TotalMilliseconds = 0;
    
    static private final long SecondsToMillis = 1000;
    static private final long MinutesToMillis = 60 * SecondsToMillis;
    static private final long HoursToMillis = 60 * MinutesToMillis;
    static private final long DaysToMillis = 24 * HoursToMillis;
    static private final long WeeksToMillis = 7 * DaysToMillis;  
    
    public TimeLength ()
    {
        
    }
    
    /**
     * @param weeks
     * @param days
     * @param hours
     * @param minutes
     * @param seconds
     * @param milliseconds
     */
    public TimeLength
    	(long weeks,
    	 long days,
    	 long hours,
    	 long minutes,
    	 long seconds,
         long milliseconds)
    {
        this.weeks = weeks;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.milliseconds = milliseconds;
    }
    
    private void CalcTotalMillisFromUnits ()
    {
        TotalMilliseconds = milliseconds +
		(SecondsToMillis * seconds) +
		(MinutesToMillis * minutes) +
		(HoursToMillis * hours) +
		(DaysToMillis * days) +
		(WeeksToMillis * weeks); 
    }
    
    public TimeLength (long totalMilliseconds)
    {
        this.TotalMilliseconds = totalMilliseconds;
        
        CalcUnitsFromTotalMillis ();
    }
    
    private void CalcUnitsFromTotalMillis ()
    {
        long rem = TotalMilliseconds;
        
        weeks = rem / WeeksToMillis;
        rem =  rem % WeeksToMillis;
        
        days = rem / DaysToMillis;
        rem = rem % DaysToMillis;
        
        hours = rem / HoursToMillis;
        rem = rem % HoursToMillis;
        
        minutes = rem / MinutesToMillis;
        rem = rem % MinutesToMillis;
        
        seconds = rem / SecondsToMillis;
        rem = rem % SecondsToMillis;
        
        milliseconds = rem;
    }
    
    public long add (Calendar calAdd)    
    {
    	if (calAdd == null)
    	{ return -1; }
    	
       long timeMillisAdd = calAdd.getTimeInMillis();
       TotalMilliseconds = TotalMilliseconds + timeMillisAdd;
       
       CalcUnitsFromTotalMillis ();
       
       return (TotalMilliseconds);
    }
    
    public long add (TimeLength timeLength)    
    {
       long timeMillisAdd = timeLength.getTotalMilliseconds();
       TotalMilliseconds = TotalMilliseconds + timeMillisAdd;
       
       CalcUnitsFromTotalMillis ();
       
       return (TotalMilliseconds);
    }
    
    public String outputTimeLength()
    {
        return (outputTimeLength(true));
    }
    
    public String outputTimeLength (boolean showSeconds)
    {
        StringBuffer buf = new StringBuffer();
        
        if (weeks > 0)
        { buf.append (weeks + " weeks, "); }
        
        if (days > 0)
        { buf.append (days + " days, "); }
        
        if (hours > 0)
        { buf.append (hours + " hours, "); }
        
        long showMinutes = minutes;
        
        if (!showSeconds)
        { 
            if (seconds >= 30)
            {
                showMinutes = showMinutes + 1;
            }
            
            buf.append (showMinutes + " minutes "); 
        }
        else
        {
            buf.append (showMinutes + " minutes, ");  
        }
        

        if (showSeconds)
        { buf.append ("and " + seconds + " seconds "); }
        
        return (buf.toString());
    }
    
    /**
     * @return Returns the days.
     */
    public long getDays()
    {
        return days;
    }
    /**
     * @param days The days to set.
     */
    public void setDays(long days)
    {
        this.days = days;
    }
    /**
     * @return Returns the hours.
     */
    public long getHours()
    {
        return hours;
    }
    /**
     * @param hours The hours to set.
     */
    public void setHours(long hours)
    {
        this.hours = hours;
    }
    /**
     * @return Returns the milliseconds.
     */
    public long getMilliseconds()
    {
        return milliseconds;
    }
    /**
     * @param milliseconds The milliseconds to set.
     */
    public void setMilliseconds(long milliseconds)
    {
        this.milliseconds = milliseconds;
    }
    /**
     * @return Returns the minutes.
     */
    public long getMinutes()
    {
        return minutes;
    }
    /**
     * @param minutes The minutes to set.
     */
    public void setMinutes(long minutes)
    {
        this.minutes = minutes;
    }
    /**
     * @return Returns the seconds.
     */
    public long getSeconds()
    {
        return seconds;
    }
    /**
     * @param seconds The seconds to set.
     */
    public void setSeconds(long seconds)
    {
        this.seconds = seconds;
    }
    /**
     * @return Returns the totalMilliseconds.
     */
    public long getTotalMilliseconds()
    {
        CalcTotalMillisFromUnits();
        return TotalMilliseconds;
    }
    /**
     * @param totalMilliseconds The totalMilliseconds to set.
     */
    public void setTotalMilliseconds(long totalMilliseconds)
    {
        TotalMilliseconds = totalMilliseconds;
    }
    /**
     * @return Returns the weeks.
     */
    public long getWeeks()
    {
        return weeks;
    }
    /**
     * @param weeks The weeks to set.
     */
    public void setWeeks(long weeks)
    {
        this.weeks = weeks;
    }
}
