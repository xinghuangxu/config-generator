package org.netapp.epg.qa;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Type {
	
	public static String key="Type";

	private String name;
	
	private LevelCollection lc=new LevelCollection();
	
	public Type(String name) {
		this.name=name;
	}

	public void add(ResultSet rs) throws SQLException {
		lc.add(rs);
	}

	public void makeFolder(String basePath) {
		lc.makeFolder(basePath+"/"+name);
	}

	public void generateTestReport(Writer writer, String reportName) throws IOException {
		lc.generateTestReport(name,writer);
	}

}
