package org.netapp.epg.qa;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class LevelCollection {
	
	public HashMap<String,Level> levelMap=new HashMap<String, Level>();

	public void add(String[] words, int i) {
		if(!levelMap.containsKey(words[i])){
			levelMap.put(words[i], new Level(words[i]));
		}
		levelMap.get(words[i]).add(words,i+1);
	}

	public void makeFolder(String basePath) {
		Iterator<Entry<String, Level>> it = levelMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Level> pairs = (Map.Entry<String,Level>)it.next();
	        pairs.getValue().makeFolder(basePath);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}

	public void generateTestReport(String classname, PrintWriter writer) {
		Iterator<Entry<String, Level>> it = levelMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Level> pairs = (Map.Entry<String,Level>)it.next();
	        pairs.getValue().generateTestReport(classname,writer);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}

}
