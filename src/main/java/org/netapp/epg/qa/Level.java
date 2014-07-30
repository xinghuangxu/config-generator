package org.netapp.epg.qa;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Level {
	
	public static String key="Level";
	
	private String name;
	
	private TestSetCollection tsc=new TestSetCollection();

	public Level(String name) {
		this.name=name;
	}

	public void add(ResultSet rs) throws SQLException {
		tsc.add(rs);
	}

	public void makeFolder(String basePath) {
		tsc.makeFolder(basePath+"/"+name);
	}

	public void generateTestReport(String classname, Writer writer) throws IOException {
		tsc.generateTestReport(classname+"/"+name,writer);
	}

}
