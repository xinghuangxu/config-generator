package org.netapp.epg.qa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import org.netapp.epg.utils.CSVParser;


public class QaReport extends QaGeneric{
	
	public static String key="Boxcar";
	
	private String name;
	
	private TypeCollection tc=new TypeCollection();

	public QaReport(File file) {
		
		this.name=file.getName().substring(0, file.getName().lastIndexOf('.'));
		try {
			LOG.info("Read Qa Test Report "+file.getName()+" File.");
			BufferedReader br = new BufferedReader(new FileReader(file));

			String line=br.readLine();
			String[] words;
			CSVParser myCSV = new CSVParser();
			while((line=br.readLine())!=null){
				words=myCSV.parse(line);
				tc.add(words,1);
			}
			br.close();
		} catch (Exception ex) {
			LOG.warn(ex.getMessage());
		}
	}

	public void makeFolder(String basePath) {
		tc.makeFolder(basePath+"/"+name);
	}

	public void generateTestReport(String basePath) {
		File xml=new File(basePath+"/"+name+".xml");
		try{
			PrintWriter writer=new PrintWriter(xml);
			writer.println("<?xml version=\"1.0\"?>");
			writer.println("<testsuite name=\""+name+"\">");
			tc.generateTestReport(writer);
			writer.println("</testsuite>");
			writer.close();
		}catch(Exception ex){
			LOG.warn(ex);
		}
	}

}
