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

public class TypeCollection {
	
	private HashMap<String, Type> typeMap=new HashMap<String, Type>();

	public void add(ResultSet rs) throws SQLException {
		String type=rs.getString("Type");
		if(!typeMap.containsKey(type)){
			typeMap.put(type, new Type(type));
		}
		typeMap.get(type).add(rs);
	}

	public void makeFolder(String basePath) {
		Iterator<Entry<String, Type>> it = typeMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Type> pairs = (Map.Entry<String,Type>)it.next();
	        pairs.getValue().makeFolder(basePath);
	       // it.remove(); // avoids a ConcurrentModificationException
	    }
	}

	public void generateTestReport(Writer writer,String reportName) throws IOException {
		Iterator<Entry<String, Type>> it = typeMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Type> pairs = (Map.Entry<String,Type>)it.next();
	        pairs.getValue().generateTestReport(writer,reportName);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}

}
