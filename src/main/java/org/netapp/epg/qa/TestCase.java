package org.netapp.epg.qa;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	
	
	public TestCase(ResultSet words) throws SQLException {
		String name=words.getString("Test Case");
		if(name==null){
			this.name="Unknown";
		}else{
			this.name=words.getString("Test Case").replace("&", "&amp;");
		}
		String duration=words.getString("Plan Dur");
		if(duration==null){
			this.duration=0;
		}else{
			String time=words.getString("Plan Dur");
			String[] timeSplit=time.split(":");
			Double min=Double.parseDouble(timeSplit[1])/60;
			Double hr=Double.parseDouble(timeSplit[0]);
			this.duration=hr+min;
		}
		this.status=words.getString("Status");
		this.lastestExecutionDate=words.getString("Ex Date");
		if(this.status.equals("Passed")){
			this.run=true;
			this.passed=true;
			this.failed=false;
		}else if(this.status.equals("No Run")){
			this.run=false;
			this.passed=false;
			this.failed=false;
		}else if (this.status.equals("Failed")){
			this.run=true;
			this.passed=false;
			this.failed=true;
		}else{
			this.run=false;
			this.passed=false;
			this.failed=false;
		}
	}

	public void makeFolder(String basePath) {
		File testcase=new File(basePath+"/"+name+".cc");
		testcase.getParentFile().mkdirs();
		try{
			Writer write = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream(testcase), "UTF-8"));
			write.write("/*");
			write.write("\n");
			write.write(name);
			write.write("\n");
			write.write("Lastest Execution Date: "+this.lastestExecutionDate);
			write.write("\n");
			write.write("Status: "+this.status);
			write.write("\n");
			write.write("Run: "+this.run);
			write.write("\n");
			write.write("Passed: "+this.passed);
			write.write("\n");
			write.write("Failed: "+this.failed);
			write.write("\n");
			write.write("*/");
			write.close();
		}catch(Exception ex){
			LOG.warn(ex.getMessage());
		}
	}

	public void generateTestReport(String classname, Writer write) throws IOException {
		write.write("<testcase classname=\""+classname+"/"+this.name+".cc\" name=\""+this.name+"\" time=\""+this.duration+"\">");
		if(this.failed){
			write.write("<failure message=\"This test has failed.\"></failure>");
		}else if(!this.passed){
			write.write("<skipped message=\"This test has been skipped.\"></skipped>");
		}
		write.write("</testcase>");
	}

}
