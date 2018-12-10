package main;

import java.util.Date;

import time.CalCustom;
import time.Duration;

public class Walk extends Duration
{
	int walkID = -1;
	
	public Walk()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Walk(int walkID)
	{
		super();
		this.walkID = walkID;
	}

	public Walk(Date beginDate, Date endDate)
	{
		super(beginDate, endDate);
		// TODO Auto-generated constructor stub
	}
	
	public Walk(Date beginDate, Date endDate, int walkID)
	{
		super(beginDate, endDate);
		// TODO Auto-generated constructor stub
		this.walkID = walkID;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

	public int getWalkID()
	{
		return walkID;
	}

	public void setWalkID(int walkID)
	{
		this.walkID = walkID;
	}

	public Walk(CalCustom beginTime, CalCustom endTime)
	{
		super(beginTime, endTime);
		// TODO Auto-generated constructor stub
	}

}
