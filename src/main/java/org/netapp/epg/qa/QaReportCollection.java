package org.netapp.epg.qa;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.netapp.epg.Boxcar;
import org.netapp.epg.BoxcarMap;



public class QaReportCollection {
	
	private static List<QaReport> qaReports=new ArrayList<QaReport>();
	
	private static String folderName="qa-tests";
	
	
	public QaReportCollection(String path){
//		File directory = new File(path);
//		File[] fList = directory.listFiles();
//		if (fList != null) {
//			for (File file : fList) {
//				if (file.isFile()) {
//					qaReports.add(new QaReport(file));
//				}
//			}
//		}
		
		//loop through all the boxcars to get their current test cases
		List<Boxcar> bcs=BoxcarMap.getAllBoxcars();
		for(int i=0;i<bcs.size();i++){
			qaReports.add(new QaReport(bcs.get(i).getName(),bcs.get(i).getAlmPrefix()));
		}
		
	}

	public void makeFolder(String basePath) {
		for(int i=0;i<qaReports.size();i++){
			qaReports.get(i).makeFolder(basePath+"/"+folderName);
		}
	}

	public void generateTestReport(String basePath) throws FileNotFoundException {
		for(int i=0;i<qaReports.size();i++){
			qaReports.get(i).generateTestReport(basePath+"/"+folderName);
		}
	}

	public static String getTestBaseFolder() {
		return folderName;
	}

	public static String hasTestFor(String name) {
		for(int i=0;i<qaReports.size();i++){
			if(name.indexOf(qaReports.get(i).getName())!=-1){
				return qaReports.get(i).getName();
			}
		}
		return "";
	}
	
	
}
