package org.netapp.epg.qa;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TestSetCollection {
	
	private HashMap<String,TestSet> testsetMap=new HashMap<String, TestSet>();

	public void add(ResultSet words) throws SQLException {
		String testset=words.getString("Test Set");
		if(!testsetMap.containsKey(testset)){
			testsetMap.put(testset, new TestSet(testset));
		}
		testsetMap.get(testset).add(words);
	}

	public void makeFolder(String basePath) {
		Iterator<Entry<String, TestSet>> it = testsetMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,TestSet> pairs = (Map.Entry<String,TestSet>)it.next();
	        pairs.getValue().makeFolder(basePath);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}

	public void generateTestReport(String classname, Writer writer) throws IOException {
		Iterator<Entry<String, TestSet>> it = testsetMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,TestSet> pairs = (Map.Entry<String,TestSet>)it.next();
	        pairs.getValue().generateTestReport(classname,writer);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}

}
