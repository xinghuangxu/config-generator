package org.netapp.epg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.netapp.epg.qa.QaReportCollection;


public class Config {
	public static final Logger LOG= Logger.getLogger("NetApp.Epg.Sonar.ConfigGenerator");
	
	private List<String> baseDirectories;
	private static List<String> sourceDirectories;
	
	private String ccgMapFilePath="config/ccg-map.csv";
	private CCGMap ccgMap;
	
	private String boxcarMapFilePath="config/boxcar-mapping.csv";
	private BoxcarMap boxcarMap;
	
	private String qaTestsFolder="config/qa-test-reports";
	private QaReportCollection qrc;
	
	
	private static String sonarRunnerPath;

	public Config(String sources) {
		baseDirectories = new ArrayList<String>();
		sourceDirectories=new ArrayList<String>();
		try {
			System.out.println("Read Config File.");
			BufferedReader br = new BufferedReader(new FileReader(
					"config.sonar"));
			
			//pass the base directories, if pass by the command line parameter then use the command line one
			//otherwise use the one in the config.sonar file
			System.out.println("Read Base Directories.");
			String line = br.readLine();
			String[] dirs;
			String par;
			if(sources!=""){
				dirs=sources.split(",");
			}else{
				par=line.substring(line.indexOf(':')+1);
				dirs=par.split(",");
			}
			for(int i=0;i<dirs.length;i++){
				baseDirectories.add(dirs[i]);
			}
			
			
			//read in the source
			String[] r;
			System.out.println("Read Source Directories.");
			line = br.readLine();
			r=line.split(":");
			dirs=r[1].split(",");
			for(int i=0;i<dirs.length;i++){
				sourceDirectories.add(dirs[i]);
			}
			
			line = br.readLine();
			r=line.split(":");
			this.sonarRunnerPath=r[1];
			
			br.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
	}
	
	public static String getSonnarRunnerPath(){
		return sonarRunnerPath;
	}
	
	public static List<String> getSourceDirectories(){
		return sourceDirectories;
	}


	public void generateProperties() {
		//read in the qa tests report
		qrc=new QaReportCollection(this.qaTestsFolder);
		qrc.makeFolder(baseDirectories.get(0));
		qrc.generateTestReport(baseDirectories.get(0));
		
		//generate the folder structure
		Folder root=null;
		for(int i=0;i<baseDirectories.size();i++){
			root=new Folder(); //generate the folder trees
			root.setPath(baseDirectories.get(i));
			for(int j=0;j<sourceDirectories.size();j++){
				Folder nf=new Folder(baseDirectories.get(i)+"/"+sourceDirectories.get(j));
				root.addFolder(nf);
			}
		}
		System.out.println("Folder Initialized!");
		
		
		//read in ccg mapping
		ccgMap=new CCGMap(this.ccgMapFilePath);
		ccgMap.generate(root);
		
		//read in boxcar mapping
		boxcarMap=new BoxcarMap(this.boxcarMapFilePath);
		boxcarMap.generate(root);
		
		generateShellScript();
	}


	private void generateShellScript() {
		//ccg script
		
		
		//boxcar script
	}
}
