package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connectionpool.ConnectionPool;

public class ExecuteResults 
{
	ResultSet rs = null;
	Statement statement = null;
	Connection con = null;
	ConnectionPool conPool = null;	
	
	public ExecuteResults
		(ResultSet rs, Statement statement, Connection con, ConnectionPool conPool)
	{
		super();
	
		this.rs = rs;
		this.statement = statement;
		this.con = con;
		this.conPool = conPool;
	}
	
	public void cleanUp ()
	{
		try
		{
			if (rs != null)
			{ rs.close(); }
			
			if (statement != null)
			{ statement.close(); }
			
			if ((con != null) && (conPool != null))
			{ conPool.free(con); }
			
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public Connection getCon() {
		return con;
	}
	public void setCon(Connection con) {
		this.con = con;
	}
	public ResultSet getRs() {
		return rs;
	}
	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
	public Statement getStatement() {
		return statement;
	}
	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public ConnectionPool getConPool() {
		return conPool;
	}

	public void setConPool(ConnectionPool conPool) {
		this.conPool = conPool;
	}

	

}
