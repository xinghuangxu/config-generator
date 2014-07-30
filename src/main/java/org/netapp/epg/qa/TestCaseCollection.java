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

public class TestCaseCollection extends QaGeneric{
	
	private HashMap<String, TestCase> testcaseMap=new HashMap<String, TestCase>();

	public void add(ResultSet words) throws SQLException {
		String testcase=words.getString("Test Case");
		if(!testcaseMap.containsKey(testcase)){
			testcaseMap.put(testcase, new TestCase(words));
		}else{
			LOG.info("Duplication!"+testcase);
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

	public void generateTestReport(String classname, Writer writer) throws IOException {
		Iterator<Entry<String, TestCase>> it = testcaseMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,TestCase> pairs = (Map.Entry<String,TestCase>)it.next();
	        pairs.getValue().generateTestReport(classname,writer);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}

}
