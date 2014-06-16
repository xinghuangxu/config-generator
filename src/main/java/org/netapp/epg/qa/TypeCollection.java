package org.netapp.epg.qa;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TypeCollection {
	
	private HashMap<String, Type> typeMap=new HashMap<String, Type>();

	public void add(String[] words, int i) {
		if(!typeMap.containsKey(words[i])){
			typeMap.put(words[i], new Type(words[i]));
		}
		typeMap.get(words[i]).add(words,i+1);
	}

	public void makeFolder(String basePath) {
		Iterator<Entry<String, Type>> it = typeMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Type> pairs = (Map.Entry<String,Type>)it.next();
	        pairs.getValue().makeFolder(basePath);
	       // it.remove(); // avoids a ConcurrentModificationException
	    }
	}

	public void generateTestReport(PrintWriter writer) {
		Iterator<Entry<String, Type>> it = typeMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Type> pairs = (Map.Entry<String,Type>)it.next();
	        pairs.getValue().generateTestReport(writer);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}

}
