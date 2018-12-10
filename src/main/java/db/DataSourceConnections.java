package db;

import java.sql.Connection;

import connectionpool.ConnectionPool;

public class DataSourceConnections 
{
	Connection con = null;
	ConnectionPool conPool = null;
	boolean isConPool = false;
	boolean isProblem = false;
	
	public DataSourceConnections(Connection con)
	{
		super();
		isConPool = false;
		this.con = con;
	}
	
	public DataSourceConnections(ConnectionPool conPool)
	{
		super();
		isConPool = true;
		this.conPool = conPool;
	}

	public java.sql.Connection getCon() 
	{
		if (isConPool)
		{
			Connection conLocal = null;
			try
			{
		 		conLocal = conPool.getConnection();
			} 
			catch(Exception e) 
			{ System.out.println
				("**ERROR (DBWaccess.getCon) No con. from Pool: \n"+ e.getMessage());
				
			  e.printStackTrace();
			}
			return (conLocal);
		}
		else
		{
			return (con);
		}
	}

	public ConnectionPool getConPool() {
		return conPool;
	}

	public void setConPool(ConnectionPool conPool) 
	{
		this.conPool = conPool;
	}

	public boolean isConPool() 
	{
		return isConPool;
	}

	public void setConPool(boolean isConPool) 
	{
		this.isConPool = isConPool;
	}

	public void setCon(Connection con)
	{
		this.con = con;
	}

	public boolean isProblem() {
		return isProblem;
	}

	public void setProblem(boolean isProblem) {
		this.isProblem = isProblem;
	}
	
}
