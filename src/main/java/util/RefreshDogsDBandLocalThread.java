package util;

import main.DogsDBproc;

public class RefreshDogsDBandLocalThread
  implements Runnable
 
 {
	DogsDBproc dogsDBproc = null;

	public RefreshDogsDBandLocalThread () 
	{
		super();
		
	}
	
	public RefreshDogsDBandLocalThread (DogsDBproc dogsDBprocLoc) 
	{
		this();
		
		this.dogsDBproc = dogsDBprocLoc;
	}

	public void run() 
	{
		boolean cont = true;
		long waitMins = 10;
		long waitMillis = waitMins * 60 * 1000;
		
		while (cont)
		{
			dogsDBproc.refreshDogsDBandLocal();
			
			try
			{
				Thread.sleep(waitMillis);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

}
