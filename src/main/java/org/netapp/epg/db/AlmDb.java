package org.netapp.epg.db;


import java.sql.ResultSet;

public class AlmDb extends Database {
	
	public AlmDb(){
		this.driveclass="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	}

	public ResultSet readBoxcarTest(String prefix) throws Exception {
		try {
			String url="jdbc:sqlserver://apg-eng-db-prod01.eng.netapp.com";
			String userName = "td_readonly";
			String password = "td_readonly";
			this.createConnection(url, userName, password);
			// statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// resultSet gets the result of the SQL query
			resultSet = statement
					.executeQuery("SELECT ts.TS_USER_05 as 'Type','Unknown' as 'Level', cyc.CY_CYCLE as 'Test Set', ts.TS_NAME as 'Test Case', ts.TS_USER_02 as 'Plan Dur', ts.TS_USER_09 as 'TC Type',"
							+ " tscyc.TC_STATUS as 'Status', tscyc.TC_EXEC_DATE as 'Ex Date', tscyc.TC_USER_04 as 'Cur Status',"
							+ " tscyc.TC_USER_02 as 'Ex Status'"
							+ " FROM [pct_producttest_db].[td].[CYCLE] as cyc"
							+ " left join [pct_producttest_db].[td].[TESTCYCL] as tscyc on cyc.[CY_CYCLE_ID]=tscyc.[TC_CYCLE_ID]"
							+ " left join [pct_producttest_db].[td].[TEST] as ts on ts.[TS_TEST_ID]=tscyc.[TC_TEST_ID]"
							+ " WHERE cyc.[CY_CYCLE] like '%"+prefix+"%' AND ts.TS_NAME IS NOT NULL");
			System.out.println("Read Boxcar:"+prefix+" Mapping Info.");
			return resultSet;
			// writeResultSet(resultSet);
//			ResultSetMetaData rsmd = resultSet.getMetaData();
//			int columnsNumber = rsmd.getColumnCount();
//			while (resultSet.next()) {
//				for (int i = 1; i < columnsNumber; i++) {
//					if (i > 1)
//						System.out.print(",  ");
//					String columnValue = resultSet.getString(i);
//					System.out.print(columnValue + " " + rsmd.getColumnName(i));
//				}
//
//			}
			
		} catch (Exception e) {
			throw e;
		}
	}
}
