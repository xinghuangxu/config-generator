package org.netapp.epg.qa;

import java.io.File;
import java.io.PrintWriter;

import org.netapp.epg.CCG;

public class TestCase extends QaGeneric{
	
	public static String key="Test Case";
	
	private String name;
	
	private double duration; //in second
	
	private String status;
	
	private String lastestExecutionDate;
	
	private boolean run;
	
	private boolean passed;
	
	private boolean failed;
	
	public TestCase(String[] words, int i) {
		this.name=words[i++].replace("&", "&amp;");
		this.duration=Double.parseDouble(words[i++]);
		this.status=words[i++];
		this.lastestExecutionDate=words[i++];
		this.run="1".equals(words[i++]);
		this.passed="1".equals(words[i++]);
		this.failed="1".equals(words[i]);;
	}

	public void makeFolder(String basePath) {
		File testcase=new File(basePath+"/"+name+".cc");
		testcase.getParentFile().mkdirs();
		try{
			PrintWriter writer=new PrintWriter(testcase);
			writer.println(name);
			writer.println("Lastest Execution Date: "+this.lastestExecutionDate);
			writer.println("Status: "+this.status);
			writer.println("Run: "+this.run);
			writer.println("Passed: "+this.passed);
			writer.println("Failed: "+this.failed);
			writer.close();
		}catch(Exception ex){
			LOG.warn(ex.getMessage());
		}
	}

	public void generateTestReport(String classname, PrintWriter writer) {
		writer.println("<testcase classname=\""+classname+"/"+this.name+".cc\" name=\""+this.name+"\" time=\""+this.duration+"\">");
		if(this.failed){
			writer.println("<failure message=\"This test has failed.\"></failure>");
		}else if(!this.passed){
			writer.println("<skipped message=\"This test has been skipped.\"></skipped>");
		}
		writer.println("</testcase>");
	}

}
