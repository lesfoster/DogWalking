package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import time.CalCustom;
import time.Duration;

import db.DBAccess;
import db.ExecuteResults;
import db.QueryConfig;

public class UpdateDogsDBwithArkDB_TEST 
{
	DBAccess dbAccessDogs = null;
	DBAccess dbAccessArk = null;
	
	public void updateData ()
	{
		Vector cols = new Vector ();
		String col1 = "id_num";
		String col2 = "name";
		
		cols.addElement(col1);
		cols.addElement(col2);
		
		// Read from Ark data:
		QueryConfig arkQueryConfig = 
			new QueryConfig ("ANIMALS.DBF", cols);
		
		ExecuteResults execResults =
			dbAccessArk.queryData(arkQueryConfig);
		
		ResultSet queryResults = execResults.getRs();
		
		String id = null;
		String name = null;
		
		Connection conDogs = dbAccessDogs.getCon();
		
		try
	   {
		Statement updateStatement =
			conDogs.createStatement();
		
		int nRows = -1;
		
		while (queryResults.next())
		{
			id = queryResults.getString(col1);
			name = queryResults.getString(col2);
			
			String sqlString = 
				("UPDATE DogsMain SET ID = \"" + id + 
						 "\", NAME = \"" + name + "\" WHERE ID = \"" + id + "\";");

			nRows = updateStatement.executeUpdate (sqlString);
		}
		
	   }
	   catch (Exception sqlException)
	   {
		System.out.println(sqlException);
		execResults.cleanUp();
		dbAccessDogs.returnCon(conDogs);
	   }
		
	   execResults.cleanUp();
	   dbAccessDogs.returnCon(conDogs);
	}

	/**
	 * - clear "current" column in Dogs DB
	 * - update Dog DB with "current" field from Ark DB
	 * - if update does not occur, add new row
	 *
	 */
	public void applyArkDBtoDogsDB ()
	{
		Vector cols = new Vector ();
		String col1 = "id_num";
		String col2 = "name";
		String col3	= "current";
		String col4 = "remarks";
		
		
		//TEST:
		String dateDispo = "date_dispo";
		String dateSurr = "date_surr";
		String adoptID = "adpt_id";
		
		cols.addElement(col1);
		cols.addElement(col2);
		cols.addElement(col4);
		//cols.addElement(col3);
		
		//TEST:
		cols.addElement(dateDispo);
		cols.addElement(dateSurr);
		cols.addElement("adpt_id");
		cols.addElement("a_rcpt_no");
		cols.addElement("a_referenc");
		
		cols.addElement("animal_txt");
		cols.addElement("species");
		cols.addElement("adoptable");
		cols.addElement("breed");
		cols.addElement("age");
		cols.addElement("sex");
		cols.addElement("color");
		cols.addElement("surr_code");
		cols.addElement("location");
		cols.addElement("hair");
		cols.addElement("adpt_refnd");
		cols.addElement("contrib");
		cols.addElement("adpt_cost");
		cols.addElement("date_fecal");
		cols.addElement("date_hrtwr");
		cols.addElement("fecal");
		cols.addElement("health");
		cols.addElement("heartworm");
		cols.addElement("neuter_vet");
		cols.addElement("rabies");
		cols.addElement("rab_cert");
		cols.addElement("euth_usd");
		cols.addElement("reclaimed");
		cols.addElement("pu_loc");
		cols.addElement("disp_code");
		cols.addElement("sn_refund");
		cols.addElement("surr_id");
		cols.addElement("treatment");
		cols.addElement("vac1");
		cols.addElement("vac2");
		cols.addElement("vet");
		cols.addElement("worm1");
		cols.addElement("worm2");
		cols.addElement("worm3");
		cols.addElement("neuter_dat");
		cols.addElement("neuter_tag");
		cols.addElement("license");
		cols.addElement("cert_no");
		cols.addElement("s_pay_code");
		cols.addElement("a_pay_code");
		cols.addElement("s_rcpt_no");
		cols.addElement("a_referenc");
		cols.addElement("screening1");
		cols.addElement("screening2");
		cols.addElement("screening3");
		cols.addElement("parvo");
		cols.addElement("parvo_date");
		cols.addElement("last_user");
		
		Hashtable whereHT = new Hashtable();
		
		whereHT.put("species", " = \"C\"");
		//whereHT.put("adpt_id", " = \"\"");
		
		
		
		whereHT.put("euth_usd", " <> 0");
		//whereHT.put("a_referenc", " = \"\"");
				
		/* Date Filter:
		String startDateString = "{12/31/2004}";
     	whereHT.put("date_surr", " > " + startDateString);
        */
		
		//whereHT.put("adpt_id", " IS NULL");
		//whereHT.put("date_dispo", " = \"\"");
		//whereHT.put("date_surr", " IS NULL");
		
		//whereHT.put("date_dispo", " IS NULL");

		//whereHT.put("date_surr", " > \"31-DEC-2004\"");
		
		// Read from Ark data:
		QueryConfig arkQueryConfig = 
			new QueryConfig ("ANIMALS.DBF", cols);
		arkQueryConfig.setWhereFields(whereHT);
		
		System.out.println(" ** Query: " + arkQueryConfig.toString());
		
		ExecuteResults execResults =
			dbAccessArk.queryData(arkQueryConfig);
		
		ResultSet queryResults = execResults.getRs();
		
		String id = null;
		String name = null;
		String adoptable = null;
		String notes = null;
		
		String sqlString = null;
		
		int nRows = -1;
		int nCurrentDogs = 0;
		
		Connection conDogs = dbAccessDogs.getCon();
	
	 try {
		Statement updateStatement =
			conDogs.createStatement();
		
		while (queryResults.next())
		{
			id = queryResults.getString(col1);
			name = queryResults.getString(col2);
			notes = queryResults.getString(col4);
			//adoptable = queryResults.getString(col3);

			//TEST:
			String dateDispoVal = queryResults.getString (dateDispo);
			String dateSurrVal = queryResults.getString (dateSurr);
			
			
			nCurrentDogs++;
			System.out.println(" ***********> Current Dog:" + nCurrentDogs + "  " + name);
			for (int kCol=0; kCol<cols.size();kCol++)
			{
				String colName = (String) cols.elementAt(kCol);
				System.out.println(colName + ": " + queryResults.getString(colName));
			}
			
			sqlString = 
				("UPDATE DogsMain SET ID = \"" + id + 
				 "\", NAME = \"" + name + "\", notes = \"" + notes + 
				 "\" WHERE ID = \"" + id + "\";");
		  try
		  {
			nRows = updateStatement.executeUpdate (sqlString);
			
			if (nRows <= 0)
			{ // NO update, do insert
				sqlString = 
					("INSERT into DogsMain (ID, NAME, NOTES) VALUES(\"" +
					  id + "\", \"" + name + "\", \"" + notes + "\");"); 
				
				updateStatement.executeUpdate (sqlString);
			}
		  }
		  catch (Exception sqlException)
		  {
			System.out.println(sqlException);
		  }
		}
   	  }
	  catch (Exception sqlException)
	  {
		System.out.println(sqlException);
	  }
	  
	  execResults.cleanUp();
	  dbAccessDogs.returnCon(conDogs);
	}
	
	public Dogs updateDogsLocalWithDogsDB()
	{
		Vector cols = new Vector ();
		String col1 = "id";
		String col2 = "name";
		String col3	= "current";
		String col4 = "notes";
		String col5 = "dogsmainID";
		//String col5 = "species";
		Dogs dogs = null;
		
		cols.addElement(col1);
		cols.addElement(col2);
		cols.addElement(col4);
		//cols.addElement(col3);
		
		// Read from Dogs DB:
		QueryConfig dogsQueryConfig = 
			new QueryConfig ("dogsMain", cols);
		
		dogsQueryConfig.setWhereString
			("current = \"T\"");
//			("dogsMain.id equals walks.idDogsMain AND walks.startWalk > **begin today**");
		
		ExecuteResults execResults =
			dbAccessDogs.queryData(dogsQueryConfig);
		ResultSet queryResults = execResults.getRs();
		
		String id = null;
		String name = null;
		String adoptable = null;
		String notes = null;
		
		String sqlString = null;
		
		int nRows = -1;
	
		dogs = new Dogs();
		Dog dog = null;
		
	 try 
	 {
		while (queryResults.next())
		{
			id = queryResults.getString(col1);
			name = queryResults.getString(col2);
			notes = queryResults.getString(col4);
			//adoptable = queryResults.getString(col3);
			
			dog = new Dog (id, name, "1.0", "0", notes);
			dogs.addDog(dog);
		}
	 }
	 catch (Exception e) 
	 {
		e.printStackTrace();
		return (null);
	 }
	
	 execResults.cleanUp();
	 
	 cols = new Vector ();
	 
	 col1 = "dogsmainID";
	 col2 = "walkstartTime";
     col3 = "walkendTime";
     col4 = "id";
	 cols.addElement(col1);
	 cols.addElement(col2);
	 cols.addElement(col3);
	 cols.addElement(col4);
	 
	 GregorianCalendar beginToday = new GregorianCalendar();
	 beginToday.set (Calendar.HOUR_OF_DAY, 0);
	 beginToday.set (Calendar.MINUTE, 0);
	 beginToday.set (Calendar.SECOND, 0);
	 beginToday.set (Calendar.MILLISECOND, 0);
	 
	 long beginTodayMillis = beginToday.getTimeInMillis();
	 
	 Timestamp beginTodayTimeStamp = 
		 new Timestamp (beginTodayMillis);
	 
	 String beginTodayString = beginTodayTimeStamp.toString();
		
	// Read from walks table DB:
	QueryConfig walksQueryConfig = 
		new QueryConfig ("walks", cols);
		
	walksQueryConfig.setWhereString
		("walkStartTime > \"" + beginTodayString + "\"");
		
	execResults =
			dbAccessDogs.queryData(walksQueryConfig);
	
	queryResults = execResults.getRs();
	
	Timestamp beginWalkTimeStamp = null;
	Timestamp endWalkTimeStamp = null;
	
	int walkID = -1;
	
	Walk walk = null;
	
	try
	{
		while (queryResults.next())
		{
			id = queryResults.getString(col1);
			beginWalkTimeStamp = queryResults.getTimestamp(col2);
			endWalkTimeStamp = queryResults.getTimestamp(col3);
			walkID = queryResults.getInt(col4);
			
			walk = 
				new Walk (beginWalkTimeStamp, endWalkTimeStamp, walkID);
			
			dog = dogs.getDog(id);
			dog.addWalk (walk);
		}
	} 
	catch (SQLException e) 
	{
		e.printStackTrace();
	}
	
	execResults.cleanUp();
	 
	 return (dogs);
	}

	public DBAccess getDbAccessArk() {
		return dbAccessArk;
	}

	public void setDbAccessArk(DBAccess dbAccessArk) {
		this.dbAccessArk = dbAccessArk;
	}

	public DBAccess getDbAccessDogs() {
		return dbAccessDogs;
	}

	public void setDbAccessDogs(DBAccess dbAccessDogs) {
		this.dbAccessDogs = dbAccessDogs;
	}
}
