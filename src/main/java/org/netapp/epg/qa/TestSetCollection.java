package org.netapp.epg.qa;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TestSetCollection {
	
	private HashMap<String,TestSet> testsetMap=new HashMap<String, TestSet>();

	public void add(String[] words, int i) {
		if(!testsetMap.containsKey(words[i])){
			testsetMap.put(words[i], new TestSet(words[i]));
		}
		testsetMap.get(words[i]).add(words,i+1);
	}

	public void makeFolder(String basePath) {
		Iterator<Entry<String, TestSet>> it = testsetMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,TestSet> pairs = (Map.Entry<String,TestSet>)it.next();
	        pairs.getValue().makeFolder(basePath);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}

	public void generateTestReport(String classname, PrintWriter writer) {
		Iterator<Entry<String, TestSet>> it = testsetMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,TestSet> pairs = (Map.Entry<String,TestSet>)it.next();
	        pairs.getValue().generateTestReport(classname,writer);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}

}
