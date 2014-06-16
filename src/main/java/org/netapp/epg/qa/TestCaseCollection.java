package org.netapp.epg.qa;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TestCaseCollection {
	
	private HashMap<String, TestCase> testcaseMap=new HashMap<String, TestCase>();

	public void add(String[] words, int i) {
		if(!testcaseMap.containsKey(words[i])){
			testcaseMap.put(words[i], new TestCase(words,i));
		}
	}

	public void makeFolder(String basePath) {
		Iterator<Entry<String, TestCase>> it = testcaseMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,TestCase> pairs = (Map.Entry<String,TestCase>)it.next();
	        pairs.getValue().makeFolder(basePath);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}

	public void generateTestReport(String classname, PrintWriter writer) {
		Iterator<Entry<String, TestCase>> it = testcaseMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,TestCase> pairs = (Map.Entry<String,TestCase>)it.next();
	        pairs.getValue().generateTestReport(classname,writer);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}

}
