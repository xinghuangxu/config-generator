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

public class LevelCollection {
	
	public HashMap<String,Level> levelMap=new HashMap<String, Level>();

	public void add(ResultSet rs) throws SQLException {
		String level=rs.getString("Level");
		if(!levelMap.containsKey(level)){
			levelMap.put(level, new Level(level));
		}
		levelMap.get(level).add(rs);
	}

	public void makeFolder(String basePath) {
		Iterator<Entry<String, Level>> it = levelMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Level> pairs = (Map.Entry<String,Level>)it.next();
	        pairs.getValue().makeFolder(basePath);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}

	public void generateTestReport(String classname, Writer writer) throws IOException {
		Iterator<Entry<String, Level>> it = levelMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Level> pairs = (Map.Entry<String,Level>)it.next();
	        pairs.getValue().generateTestReport(classname,writer);
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}

}
