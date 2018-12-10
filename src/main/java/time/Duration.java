/*
 * Created on Jan 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package time;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import sort.ContainsSortable;

/**
 * @author whitteng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Duration
	implements ContainsSortable
{
    public CalCustom beginTime = null;
    public CalCustom endTime = null;
    public TimeLength elapsedTime = null;
    
    public Duration ()
    {
        beginTime = new CalCustom();
    }
    
    public Duration (java.util.Date beginDate, java.util.Date endDate)
    {
    	if (beginDate != null)
    	{
    		beginTime = new CalCustom ();
    		beginTime.setTime (beginDate);
    	}
    	
    	if (endDate != null)
    	{
    		endTime = new CalCustom ();
    		endTime.setTime (endDate);
    	}
    }
    
    public Duration (CalCustom beginTime, CalCustom endTime)
    {
    	if (beginTime != null)
    	{
    		setBeginTime (beginTime);
    	}
    	
    	if (endTime != null)
    	{
    		setEndTime (endTime);
    	}
    }
    
    /**
     * stops timing <br> 
     *  constructor starts timing
     */
    public void stopTiming ()
    {
        endTime = new CalCustom();
        
        calcElapsedTime();        
    }
    
    public TimeLength calcElapsedTime ()
    {
    	if ((endTime == null) || (beginTime == null))
    	{ return (null); }
    	
    	long endTimeMillis = endTime.getTimeInMillis();
        long beginTimeMilis = beginTime.getTimeInMillis();
        long elapsedTimeMillis = endTimeMillis - beginTimeMilis;
        
        elapsedTime = new TimeLength (elapsedTimeMillis);
        
        return (elapsedTime);
    }
    
    public String outputBeginTime ()
    {
        return (beginTime.outputTime());
    }
    
    public String outputBeginDateString ()
    {
    	if (beginTime == null)
    	return (null);
    	
    	long timeMillis = beginTime.getTimeInMillis();
    	Timestamp timeStamp = new Timestamp (timeMillis);
        return (timeStamp.toString());
    }
    
    public String outputEndTime ()
    {
        if (endTime != null)
        {
            return (endTime.outputTime());
        }
        else
        {
            return ("NONE");
        }
    }
    
    public String outputEndDateString ()
    {
    	if (endTime == null)
    	return (null);
    	
    	long timeMillis = endTime.getTimeInMillis();
    	Timestamp timeStamp = new Timestamp (timeMillis);
        return (timeStamp.toString());
    }
    
    public Comparable getSortableElement ()
    {
    	long timeInMillis = beginTime.getTimeInMillis();
    	
    	return (new Long (timeInMillis));
    }
        
    public String outputElapsedTime ()
    {
        return (endTime.outputTimeSubYear());
    }
    
    /**
     * @return Returns the beginTime.
     */
    public CalCustom getBeginTime()
    {
        return beginTime;
    }
    /**
     * @param beginTime The beginTime to set.
     */
    public void setBeginTime(CalCustom beginTime)
    {
        this.beginTime = beginTime;
    }
    /**
     * @return Returns the elapsedTime.
     */
    public TimeLength getElapsedTime()
    {
    	if (elapsedTime != null)
    	{ return elapsedTime; }
    	
    	if ( (beginTime != null) && (endTime != null) )
    	{ calcElapsedTime(); }
    	
        return elapsedTime;
    }
    /**
     * @param elapsedTime The elapsedTime to set.
     */
    public void TimeLength (TimeLength elapsedTime)
    {
        this.elapsedTime = elapsedTime;
    }
    /**
     * @return Returns the endTime.
     */
    public CalCustom getEndTime()
    {
        return endTime;
    }
    /**
     * @param endTime The endTime to set.
     */
    public void setEndTime(CalCustom endTime)
    {
        this.endTime = endTime;
    }
}
