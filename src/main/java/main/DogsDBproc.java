package main;

import db.DBAccess;
import db.ExecuteResults;
import db.QueryConfig;
import util.StringProc;

import java.io.BufferedReader;
import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

public class DogsDBproc
		extends Thread
{
	DBAccess dbAccessDogs = null;
	DBAccess dbAccessArk = null;
	servlet.Action parent = null;

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

		cols.addElement(col1);
		cols.addElement(col2);
		cols.addElement(col4);
		//cols.addElement(col3);

		// Adopted:
		cols.addElement("adpt_id");
		// Euthanized
		cols.addElement("disp_code");
		
		
/*
		//TEST:
		String dateDispo = "date_dispo";
		String dateSurr = "date_surr";
		String adoptID = "adpt_id";
		
		
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
*/

		//		 Canine (dog not cat):
		//       NOT adopted:
		//		 NOT euthanized:

		String whereString =
				"species = \"C\" AND (adpt_id  = \"\")" +
						" AND (disp_code = \"\")";

		/*
		 * MAY NEED TO HANDLE NULL ??? - BUT FOXPRO COMPLAINS
		 */
		/*
		String whereString = 
			"species = \"C\" AND ( (adpt_id  = \"\") OR (adpt_id = NULL) )" +
			" AND ( (disp_code = \"\") OR (disp_code = NULL) )";
	   */

		/*
		String whereString = 
			"species = \"C\" AND adpt_id = \"\"";// +
			//" AND disp_code <> 0";
		*/
		
		/* Date Filter:
		String startDateString = "{12/31/2004}";
     	whereHT.put("date_surr", " > " + startDateString);
        */

		//whereHT.put("euth_usd", " <> 0");
		//whereHT.put("a_referenc", " = \"\"");
		//whereHT.put("adpt_id", " IS NULL");
		//whereHT.put("date_dispo", " = \"\"");
		//whereHT.put("date_surr", " IS NULL");
		//whereHT.put("date_dispo", " IS NULL");
		//whereHT.put("date_surr", " > \"31-DEC-2004\"");

		// Read from Ark data:
		QueryConfig arkQueryConfig =
				new QueryConfig ("ANIMALS.DBF", cols);
		arkQueryConfig.setWhereString (whereString);

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

			// Explicitly set ALL dogs to NOT current:
			Statement updateStatement =
					conDogs.createStatement();

			sqlString =
					("UPDATE DogsMain SET CURRENT = \"F\";");

			nRows = updateStatement.executeUpdate (sqlString);

			while (queryResults.next())
			{
				id = queryResults.getString(col1);
				id = id.trim();
				name = queryResults.getString(col2);
				name = name.trim();
				notes = queryResults.getString(col4);
				notes = notes.trim();
				//adoptable = queryResults.getString(col3);

/*
			//TEST:
			String dateDispoVal = queryResults.getString (dateDispo);
			String dateSurrVal = queryResults.getString (dateSurr);
*/

				nCurrentDogs++;
			/* DISPLAY DOGS:
			System.out.println(" ***********> Current Dog:" + nCurrentDogs + "  " + name);
			for (int kCol=0; kCol<cols.size();kCol++)
			{
				String colName = (String) cols.elementAt(kCol);
				System.out.println(colName + ": " + queryResults.getString(colName));
			}
			*/

				sqlString =
						("UPDATE DogsMain SET ID = \"" + id +
								"\", NAME = \"" + name + "\", notes = \"" + notes + "\"" +
								" , CURRENT = \"T\""  +
								" WHERE ID = \"" + id + "\";");
				try
				{
					nRows = updateStatement.executeUpdate (sqlString);

					if (nRows <= 0)
					{ // NO update, do insert
				/* WAS WORKING???
				sqlString = 
					("INSERT into DogsMain (ID, NAME, NOTES) VALUES(\"" +
					  id + "\", \"" + name + "\", \"" + notes + "\"" +
					  " , CURRENT = \"T\""  +  
					  ");"); 
				*/

						sqlString =
								("INSERT into DogsMain (ID, NAME, NOTES, CURRENT) VALUES(\"" +
										id + "\", \"" + name + "\", \"" + notes + "\"" +
										" , \"T\""  +
										");");
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

	public void updateDogDynamic (String dogID, String notes)
	{
		Connection conDogs = dbAccessDogs.getCon();
		String sqlString = null;
		int nRows = -1;

		try {

			Statement updateStatement =
					conDogs.createStatement();

			sqlString =
					("UPDATE DogsMain SET notes = \"" + notes + "\"" +
							" WHERE ID = \"" + dogID + "\";");

			nRows = updateStatement.executeUpdate (sqlString);

			if (nRows <= 0)
			{
				System.out.println(" *** PROBLEM UPDATING: " + dogID);
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}

		dbAccessDogs.returnCon(conDogs);
	}


	/**
	 * - clear "current" column in Dogs DB
	 * - update Dog DB with "current" field from Ark DB
	 * - if update does not occur, add new row
	 *
	 */
	public void applyOrigWHSdataToDogsDB ()
	{
		Vector cols = new Vector ();
		String col1 = "id_num";
		String col2 = "name";
		String col3	= "current";
		String col4 = "remarks";

		cols.addElement(col1);
		cols.addElement(col2);
		cols.addElement(col4);

		String id = null;
		String name = null;
		String adoptable = null;
		String notes = null;

		String sqlString = null;

		int nRows = -1;
		int nCurrentDogs = 0;

		Connection conDogs = dbAccessDogs.getCon();

		try {

			// Explicitly set ALL dogs to NOT current:
			Statement updateStatement =
					conDogs.createStatement();

			// Read from Live WHS Available Dogs website:
			String WHSdogsURL = "" +
					"http://www.petango.com/webservices/adoptablesearch/wsAdoptableAnimals.aspx?species=Dog&sex=A&agegroup=All&orderby=ID&authkey=uopok4cnn4h4m336hlncl322nd6ibjars633i31p78nof017if&recAmount=&colnum=3&onhold=N&detailsInPopup=Yes&css=https://secure3.convio.net/whsdc/assets/css/pettango.css";

			BufferedReader bufReader =
					StringProc.openWebFileForReading(WHSdogsURL);

			boolean cont = true;
			Dog dogFromSource = null;

			if (bufReader != null) {
				// Determine what dogs are "current" based on their presence on the web site.
				sqlString =
						("UPDATE DogsMain SET CURRENT = \"F\";");

				nRows = updateStatement.executeUpdate (sqlString);

				while (cont)
				{
					dogFromSource = getNextDogFromWHSwebPage(bufReader);

					if (dogFromSource == null)
					{
						cont = false;
						break;
					}

					nCurrentDogs++;
			/* DISPLAY DOGS:
			System.out.println(" ***********> Current Dog:" + nCurrentDogs + "  " + name);
			for (int kCol=0; kCol<cols.size();kCol++)
			{
				String colName = (String) cols.elementAt(kCol);
				System.out.println(colName + ": " + queryResults.getString(colName));
			}
			*/

					sqlString =
							("UPDATE DogsMain SET ID = \"" + dogFromSource.getId() + "\" ," +
									"Name = \"" + dogFromSource.getName() + "\" , "  +
									"Breed = \"" + dogFromSource.getBreed() + "\" , " +
									"Sex = \"" + dogFromSource.getSex() + "\" , " +
									"Age = \"" + dogFromSource.getAge() + "\" , " +
									"SpayNeuter = \"" + dogFromSource.getSN() + "\" , " +
									"SpecialAttentionFactor = \"" + dogFromSource.getSpecialAttentionFactorString() + "\" , " +
									"Access = \"" + dogFromSource.getAccess() + "\" , " +
									"Current = \"T\" "  +
									"WHERE ID = \"" + dogFromSource.getId() + "\";");
					try
					{
						nRows = updateStatement.executeUpdate (sqlString);

						if (nRows <= 0)
						{ // NO update, do insert
				  /* WAS WORKING???
				  sqlString = 
					("INSERT into DogsMain (ID, NAME, NOTES) VALUES(\"" +
					  id + "\", \"" + name + "\", \"" + notes + "\"" +
					  " , CURRENT = \"T\""  +  
					  ");"); 
				   */

							sqlString =
									("INSERT into DogsMain (ID, Name, Breed, Sex, Age, SpayNeuter, SpecialAttentionFactor, Access, Notes, Current) VALUES(" +
											"\"" + dogFromSource.getId() + "\", " +
											"\"" + dogFromSource.getName() + "\", " +
											"\"" + dogFromSource.getBreed() + "\" , " +
											"\"" + dogFromSource.getSex() + "\" , " +
											"\"" + dogFromSource.getAge() + "\" , " +
											"\"" + dogFromSource.getSN() + "\" , " +
											"\"" + dogFromSource.getSpecialAttentionFactorString() + "\" , " +
											"\"" + dogFromSource.getAccess() + "\" , " +
											"\"" + dogFromSource.getNotes() + "\" , " +
											"Current = \"T\" "  +
											");");

							updateStatement.executeUpdate (sqlString);
						}
					}
					catch (Exception sqlException)
					{
						System.out.println(sqlException);
					}

				}

				bufReader.close();

			}

		}
		catch (SQLException sqlE)
		{
			System.out.println(sqlE);
			System.out.println(sqlE.getErrorCode());
		}
		catch (Exception E)
		{
			System.out.println(E);
		}

		dbAccessDogs.returnCon(conDogs);
	}

	Dog getNextDogFromWHSwebPage (BufferedReader bufReader)
	{
		String foundString = null;
		Vector <String> stringsVec = null;
		int nStrings = -1;

		Dog newDog = new Dog();

		// Find next block:
		foundString = StringProc.findLineThatContains
				(bufReader, "list-animal-info-block");

		if (foundString == null)
		{ return (null); }

		//	foundString = StringProc.findLineThatContains
		//	(bufReader, "list-animal-name");

		//foundString = StringProc.findBeginEndInString(foundString, ">", "</a></div>");

		/**
		 Pattern pattern = Pattern.compile( ".*>" + "(.*?)" + "</a></div>");
		 Matcher matcher = pattern.matcher(foundString);
		 boolean matched = matcher.find();
		 foundString = matcher.group(1);
		 **/

		foundString = StringProc.findLineWithBeginEnd
				(bufReader, "list-animal-name.*>", "</a></div>");

		newDog.setName(foundString);

		foundString = StringProc.findLineWithBeginEnd
				(bufReader, "list-animal-id\">", "</div>");

		newDog.setId(foundString);

		foundString = StringProc.findLineWithBeginEnd
				(bufReader, "list-animal-sexSN\">", "</div>");

		stringsVec = StringProc.getAllSubstrings(foundString, '/');

		nStrings = stringsVec.size();
		if (nStrings > 0)
		{ newDog.setSex(stringsVec.elementAt(0)); }
		if (nStrings > 1)
		{newDog.setSN(stringsVec.elementAt(1));}

		foundString = StringProc.findLineWithBeginEnd
				(bufReader, "list-animal-breed\">", "</div>");

		newDog.setBreed(foundString);

		foundString = StringProc.findLineWithBeginEnd
				(bufReader, "list-animal-age\">", "</div>");

		newDog.setAge(foundString);

		return (newDog);
	}

	public Dogs updateDogsLocalWithDogsDB()
	{
		Vector cols = new Vector ();

		Dogs dogs = null;

		cols.addElement("ID");
		cols.addElement("Name");
		cols.addElement("Notes");
		cols.addElement("Sex");
		cols.addElement("SpayNeuter");
		cols.addElement("Breed");
		cols.addElement("Age");

		// Read from Dogs DB:
		QueryConfig dogsQueryConfig =
				new QueryConfig ("DogsMain", cols);

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
				//dog = new Dog (id, name, "1.0", "0", notes);
				dog = new Dog (queryResults);
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

		String col1;
		String col2;
		String col3;
		String col4;

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

				walk = new Walk (beginWalkTimeStamp, endWalkTimeStamp, walkID);

				dog = dogs.getDog(id);
				if (dog == null) {
					System.out.println("Failed to find dog by id /" + id + "/");
				}
				else {
					dog.addWalk (walk);
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		execResults.cleanUp();

		return (dogs);
	}

	public void createDogsMainTable (Connection con)
	{
		try
		{
			Statement createTable =
					con.createStatement();

			String sqlString =
					"CREATE TABLE `dogs`.`DogsMain` (" +
							"`ID` VARCHAR(255) NOT NULL, " +
							" `Name` VARCHAR(255), " +
							" `Sex` VARCHAR(45), " +
							" `SpayNeuter` VARCHAR(45), " +
							" `Breed` VARCHAR(255), " +
							" `Age` VARCHAR(255), " +
							" `SpecialAttentionFactor` VARCHAR(255), " +
							" `Access` VARCHAR(255), " +
							" `Current` VARCHAR(45), " +
							" `Notes` TEXT, " +
							" PRIMARY KEY (`id`) " +
							")";

			createTable.executeUpdate (sqlString);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void createWalksTable (Connection con)
	{
		try
		{
			Statement createTable =
					con.createStatement();

			String sqlString =
					"CREATE TABLE `dogs`.`walks` (" +
							"`id` INTEGER NOT NULL, " +
							" `dogsmainID` VARCHAR(255), " +
							" `walkstartTime` TIMESTAMP, " +
							" `walkendTime` TIMESTAMP, " +
							" PRIMARY KEY (`id`) " +
							")";

			createTable.executeUpdate (sqlString);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}


	public synchronized void refreshDogsDBandLocal ()
	{
		try
		{
			// NOTE: as of 12/25/2018, this URL does not work.  Petango.com is NOT defunct, however. LLF
			applyOrigWHSdataToDogsDB();

			Dogs dogs = updateDogsLocalWithDogsDB();

			dogs.sort();

			servlet.Action.setDogs(dogs);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run()
	{

		try
		{
			if (parent.isArkAvailable())
			{ // ARK (WARL): applyArkDBtoDogsDB(); 
				applyOrigWHSdataToDogsDB();
			}

			Dogs dogs = updateDogsLocalWithDogsDB();

			dogs.sort();

			servlet.Action.setDogs(dogs);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public servlet.Action getParent()
	{
		return parent;
	}

	public void setParent(servlet.Action parent)
	{
		this.parent = parent;
	}
}
