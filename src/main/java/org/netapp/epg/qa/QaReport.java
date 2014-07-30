package org.netapp.epg.qa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;

import org.netapp.epg.db.AlmDb;
import org.netapp.epg.utils.CSVParser;

public class QaReport extends QaGeneric {

	public static String key = "Boxcar";

	private String name;

	private TypeCollection tc = new TypeCollection();

	public QaReport(String boxcarName, String almPrefix) {
		this.name=boxcarName;
		try {
			AlmDb almdb = new AlmDb();
			ResultSet rs = almdb.readBoxcarTest(almPrefix);
			while (rs.next()) {
				tc.add(rs);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// this.name=file.getName().substring(0,
		// file.getName().lastIndexOf('.'));
		// try {
		// LOG.info("Read Qa Test Report "+file.getName()+" File.");
		// BufferedReader br = new BufferedReader(new FileReader(file));
		//
		// String line=br.readLine();
		// String[] words;
		// CSVParser myCSV = new CSVParser();
		// while((line=br.readLine())!=null){
		// words=myCSV.parse(line);
		// tc.add(words,1);
		// }
		// br.close();
		// } catch (Exception ex) {
		// LOG.warn(ex.getMessage());
		// }
	}

	public void makeFolder(String basePath) {
		tc.makeFolder(basePath + "/" + name);
	}

	public void generateTestReport(String basePath) throws FileNotFoundException {
		FileOutputStream xml = new FileOutputStream(basePath + "/" + name + ".xml");
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(xml, "UTF-8"));
			writer.write("<?xml version=\"1.0\"?>");
			writer.write("<testsuite name=\"" + name + "\">");
			tc.generateTestReport(writer,this.name);
			writer.write("</testsuite>");
			writer.close();
		} catch (Exception ex) {
			LOG.warn(ex);
		}
	}

	public String getName() {
		return name;
	}

}
