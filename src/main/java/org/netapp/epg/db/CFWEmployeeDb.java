package org.netapp.epg.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.netapp.epg.Boxcar;
import org.netapp.epg.CCG;
import org.netapp.epg.Component;
import org.netapp.epg.Folder;

public class CFWEmployeeDb extends Database{

	
	public CFWEmployeeDb(){
		this.driveclass="com.mysql.jdbc.Driver";
	}

	public void readDataBase() throws Exception {
		try {
			String url="jdbc:mysql://ictmysql01.eng.netapp.com/CFW-Employee";
			String userName="cfwreadonly";
			String password="cfwreadonly";
			this.createConnection(url,userName,password);
			
			// statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// resultSet gets the result of the SQL query
			resultSet = statement
					.executeQuery("SELECT CQID,ALMPrefix, boxcarname,compname FROM `CFW-Employee`.Boxcar_Component as bcc left join Boxcars as bc on bcc.boxcarCQID=bc.CQID left join components as c on bcc.componentid=c.compid ");
			writeResultSet(resultSet);
		} catch (Exception e) {
			throw e;
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// resultSet is initialised before the first data set
		while (resultSet.next()) {
			// it is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g., resultSet.getSTring(2);
			String user = resultSet.getString("CQID");
			String website = resultSet.getString("boxcarname");
			String summary = resultSet.getString("compname");

			System.out.println("CQID: " + user);
			System.out.println("boxcarname: " + website);
			System.out.println("compname: " + summary);
		}
	}

	public List<Boxcar> readBoxcarCompData() throws Exception {
		try {

			// this will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager
					.getConnection("jdbc:mysql://ictmysql01.eng.netapp.com/CFW-Employee?"
							+ "user=cfwreadonly&password=cfwreadonly");
			// statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// resultSet gets the result of the SQL query
			resultSet = statement
					.executeQuery("SELECT CQID,ALMPrefix,boxcarname,compname FROM `CFW-Employee`.Boxcar_Component as bcc left join Boxcars as bc on bcc.boxcarCQID=bc.CQID left join components as c on bcc.componentid=c.compid ORDER BY CQID");
			// writeResultSet(resultSet);

			return recordBoxcarCompData();
		} catch (Exception e) {
			throw e;
		}
	}

	private List<Boxcar> recordBoxcarCompData() throws SQLException {
		List<Boxcar> bcs = new ArrayList<Boxcar>();
		System.out
				.println("Reading Boxcar Mapping Info from Employee Database.");
		String tempId = "";
		Boxcar bc = null;
		while (resultSet.next()) {
			String cqId = resultSet.getString("CQID");
			String boxcarname = resultSet.getString("boxcarname");
			String almPrefix=resultSet.getString("ALMPrefix");
			String compname = resultSet.getString("compname");
			if (!tempId.equals(cqId)) {
				tempId = cqId;
				bc = new Boxcar(cqId, boxcarname,almPrefix);
				bcs.add(bc);
			}
			bc.addComp(compname);

		}
		System.out.println("Successfully Read Boxcar Mapping Info.");
		return bcs;
	}

	public List<CCG> readCcgCompData() {
		List<CCG> ccgs = new ArrayList<CCG>();
		try {

			// this will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// setup the connection with the DB.
			connect = DriverManager
					.getConnection("jdbc:mysql://ictmysql01.eng.netapp.com/CFW-Employee?"
							+ "user=cfwreadonly&password=cfwreadonly");
			// statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// resultSet gets the result of the SQL query
			resultSet = statement
					.executeQuery("SELECT assetabbr , c.compname FROM assets as a, components as c WHERE a.assetid=c.assetid AND assetabbr IS NOT NULL  ORDER BY assetabbr, compname");
			// writeResultSet(resultSet);
			
			System.out.println("Read Config Info from CFW-Employee Database.");
			System.out.println("Read ccg-map.");
			
			resultSet.next();
			String assetabbr = resultSet.getString("assetabbr");
			String compname = resultSet.getString("compname");
			CCG nccg = new CCG(assetabbr);
			Component ncomponent = new Component(compname);
			ncomponent.setFolders(Folder.getMap().get(ncomponent.getName()));
			nccg.addComponent(ncomponent);
			while (resultSet.next()) {
				assetabbr = resultSet.getString("assetabbr");
				compname = resultSet.getString("compname");
				if (!nccg.getName().equals(assetabbr)) {
					ccgs.add(nccg);
					nccg = new CCG(assetabbr);
				}
				ncomponent = new Component(compname);
				ncomponent
						.setFolders(Folder.getMap().get(ncomponent.getName()));
				nccg.addComponent(ncomponent);
			}
			ccgs.add(nccg);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return null;
		}
		return ccgs;
	}

}
