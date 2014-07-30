package org.netapp.epg.qa;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestSet {
	
	public static String key="Test Set";
	
	private String name;
	
	private TestCaseCollection tcc=new TestCaseCollection();

	public TestSet(String name) {
		this.name=name.replace("&", "&amp;");
	}

	public void add(ResultSet words) throws SQLException {
		tcc.add(words);
	}


	public void makeFolder(String basePath) {
		tcc.makeFolder(basePath+"/"+name);
	}

	public void generateTestReport(String classname, Writer writer) throws IOException {
		tcc.generateTestReport(classname+"/"+name,writer);
	}


}
