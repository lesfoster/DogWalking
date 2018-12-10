/*
 * Created on Jan 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package html;

import java.util.Calendar;

import org.omg.CORBA.TypeCode;

import servlet.ProcessHTTPinput;
import time.CalCustom;
import time.TimeLength;
import util.StringProc;

/**
 * @author whitteng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TimeElement
{
    static String[] HoursList = new String[13];
    static String[] MinutesList = new String[60];
    static String[] AmPmList = new String[2];
    
    ComboListElement hoursSelectEl = null;
    ComboListElement minutesSelectEl = null;
    ComboListElement AmPmSelectEl = null;
    
    Calendar cal = null;
    
    TimeLength timeLength = null;
    
    String baseName = null;
    
    static String stringVal = null;
    
    int am = 0; // AM -> 0 ; PM -> 1
   
    static final int PointType = 0;   // e.g. - time of day
    static final int LengthType = 1;  // time length
   
    int type = PointType; // default - time of day

    static
    {
        for (int k=0; k<=12; k++)
        {
            HoursList[k] = String.valueOf(k);
        }
        
        for (int k=0; k<60; k++)
        {
            MinutesList[k] = formatMinute (k);
        }
        
        AmPmList [0] = "am";
        AmPmList [1] = "pm";
    }
    
    public TimeElement 
    	(String baseName, Calendar cal)
    {
      this.type = PointType;
      this.baseName = baseName;
      if (cal != null)
      { this.cal = cal; }
      else
      { this.cal = new CalCustom(); }

      initPoint();
    }
    
    public TimeElement 
    	(String baseName, TimeLength timeLength)
    {
      this.type = LengthType;
      this.baseName = baseName;
      if (timeLength != null)
      {  this.timeLength = timeLength; }
      else
      { this.timeLength = new TimeLength(); }
      
      initLength();
    }
    
    static public CalCustom retrieveCalCustom 
    	(String baseName, ProcessHTTPinput processInput)
    {
        String hoursString = (String)
        	processInput.getParameter(baseName + ":Hours");
        
        int hoursInt = StringProc.stringToInt(hoursString);
        
        String minutesString = (String)
    		processInput.getParameter(baseName + ":Minutes");
        
        int minutesInt = StringProc.stringToInt(minutesString);
        
        String AmPmString = (String)
        	processInput.getParameter(baseName + ":AmPm");
        int AmPm = -1;
        if (AmPmString.equalsIgnoreCase("am"))
        { // am
            AmPm = Calendar.AM;
        }
        else
        {
            AmPm = Calendar.PM;
        }
        
        CalCustom cal = new CalCustom ();
        cal.set(Calendar.HOUR, hoursInt);
        cal.set(Calendar.MINUTE, minutesInt);
        cal.set(Calendar.AM_PM, AmPm);
        cal.set(Calendar.SECOND, 0);
        
        return (cal);
    }
    
    static public TimeLength retrieveTimeLength 
		(String baseName, ProcessHTTPinput processInput)
{
    String hoursString = (String)
    	processInput.getParameter(baseName + ":Hours");
    
    int hoursInt = StringProc.stringToInt(hoursString);
    
    String minutesString = (String)
		processInput.getParameter(baseName + ":Minutes");
    
    int minutesInt = StringProc.stringToInt(minutesString);
    
    TimeLength timeLength =
        new TimeLength (0,
                	    0,
                	    hoursInt,
                	    minutesInt,
                	    0,
                	    0);
    
    return (timeLength);
  }

    
    public void initPoint()
    {
        hoursSelectEl = new ComboListElement
        	(baseName + ":Hours", HoursList);
        
        hoursSelectEl.setMultiple(false);
        
        int hour = cal.get(Calendar.HOUR);
        hoursSelectEl.setSelectedEntry(String.valueOf(hour));
        
        minutesSelectEl = new ComboListElement
    	(baseName + ":Minutes", MinutesList);
        
        minutesSelectEl.setMultiple(false);
        
        int minute = cal.get(Calendar.MINUTE);
        minutesSelectEl.setSelectedEntry(formatMinute(minute));
        
        AmPmSelectEl = new ComboListElement
        		(baseName + ":AmPm", AmPmList);
    
        AmPmSelectEl.setMultiple(false);
    
        int amInt = cal.get(Calendar.AM_PM);
        if (amInt == Calendar.AM )
        { am = 0; }
        else
        { am = 1; }
    
        AmPmSelectEl.setSelectedEntry(AmPmList[am]);
    }
    
    public void initLength()
    {
        hoursSelectEl = new ComboListElement
        	(baseName + ":Hours", HoursList);
        
        hoursSelectEl.setMultiple(false);
        
        int hour = (int) timeLength.getHours();
        hoursSelectEl.setSelectedEntry(String.valueOf(hour));
        
        minutesSelectEl = new ComboListElement
    	(baseName + ":Minutes", MinutesList);
        
        minutesSelectEl.setMultiple(false);
        
        int minute = (int) timeLength.getMinutes();
        minutesSelectEl.setSelectedEntry(formatMinute(minute));
    }
    
    
    public String outputHTML ()
    {
        StringBuffer buf = new StringBuffer ();
        
        buf.append("<TABLE Border=\"1\">\n<TR>\n <TD>\n");
        
        buf.append("<TABLE>\n<TR>\n");
        
        buf.append("<TD>");
        buf.append(hoursSelectEl.outputHTML());
        buf.append("</TD>");
        
        buf.append("<TD>");
        buf.append(" : ");
        buf.append("</TD>");
        
        buf.append("<TD>");
        buf.append(minutesSelectEl.outputHTML());
        buf.append("</TD>");
        
        if (type == PointType)
        {
            buf.append("<TD>");
            buf.append(AmPmSelectEl.outputHTML());
            buf.append("</TD>");
        }
        
        buf.append("\n</TR>\n</TABLE>");
        
        buf.append("\n </TD>\n</TR>\n</TABLE>\n");
        
        return (buf.toString());    
    }
    
    private static String formatMinute (int minuteInt)
    {
        String minuteString = String.valueOf(minuteInt);
        if (minuteString.length() < 2)
        { minuteString = "0" + minuteString; }
        
        return (minuteString);
    }
    
    /**
     * @return Returns the timeLength.
     */
    public TimeLength getTimeLength()
    {
        return timeLength;
    }
    /**
     * @param timeLength The timeLength to set.
     */
    public void setTimeLength(TimeLength timeLength)
    {
        this.timeLength = timeLength;
    }
}
