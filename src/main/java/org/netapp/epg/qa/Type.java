package org.netapp.epg.qa;

import java.io.PrintWriter;

public class Type {
	
	public static String key="Type";

	private String name;
	
	private LevelCollection lc=new LevelCollection();
	
	public Type(String name) {
		this.name=name;
	}

	public void add(String[] words, int i) {
		lc.add(words,i);
	}

	public void makeFolder(String basePath) {
		lc.makeFolder(basePath+"/"+name);
	}

	public void generateTestReport(PrintWriter writer) {
		lc.generateTestReport(name,writer);
	}

}
