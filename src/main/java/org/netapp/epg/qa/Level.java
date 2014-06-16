package org.netapp.epg.qa;

import java.io.PrintWriter;

public class Level {
	
	public static String key="Level";
	
	private String name;
	
	private TestSetCollection tsc=new TestSetCollection();

	public Level(String name) {
		this.name=name;
	}

	public void add(String[] words, int i) {
		tsc.add(words,i);
	}

	public void makeFolder(String basePath) {
		tsc.makeFolder(basePath+"/"+name);
	}

	public void generateTestReport(String classname, PrintWriter writer) {
		tsc.generateTestReport(classname+"/"+name,writer);
	}

}
