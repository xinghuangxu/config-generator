package org.netapp.epg.qa;

import java.io.PrintWriter;

public class TestSet {
	
	public static String key="Test Set";
	
	private String name;
	
	private TestCaseCollection tcc=new TestCaseCollection();

	public TestSet(String name) {
		this.name=name.replace("&", "&amp;");
	}

	public void add(String[] words, int i) {
		tcc.add(words,i);
	}


	public void makeFolder(String basePath) {
		tcc.makeFolder(basePath+"/"+name);
	}

	public void generateTestReport(String classname, PrintWriter writer) {
		tcc.generateTestReport(classname+"/"+name,writer);
	}

}
