package org.netapp.epg.qa;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class QaReportCollection {
	
	private List<QaReport> qaReports=new ArrayList<QaReport>();
	
	private static String folderName="sonar-qa-tests";
	
	
	public QaReportCollection(String path){
		File directory = new File(path);
		File[] fList = directory.listFiles();
		if (fList != null) {
			for (File file : fList) {
				if (file.isFile()) {
					qaReports.add(new QaReport(file));
				}
			}
		}
	}

	public void makeFolder(String basePath) {
		for(int i=0;i<qaReports.size();i++){
			qaReports.get(i).makeFolder(basePath+"/"+folderName);
		}
	}

	public void generateTestReport(String basePath) {
		for(int i=0;i<qaReports.size();i++){
			qaReports.get(i).generateTestReport(basePath+"/"+folderName);
		}
	}
	
	
}
