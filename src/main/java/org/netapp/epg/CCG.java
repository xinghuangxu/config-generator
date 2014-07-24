package org.netapp.epg;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.netapp.epg.qa.QaReportCollection;

public class CCG {
	private String name;
	private List<Component> components;
	
	public CCG(String name){
		this.name=name;
		components=new ArrayList<Component>();
	}
	
	public String getName(){
		return this.name;
	}
	
	public void addComponent(Component c){
		components.add(c);
		c.setParent(this);
	}

	public boolean isEmpty() {
		if(components.size()==0)
			return true;
		for(Component c : components){
			if(!c.isEmpty())
				return false;
		}
		return true;
	}

	public boolean generateSonarProperty(String projectKey,String fileName, String level, String codeCoverageReportPath, boolean isWithTest,String name) {
		File file=new File(fileName);
		file.getParentFile().mkdirs();
		try{
			PrintWriter writer=new PrintWriter(file);
			writer.println("sonar.projectKey="+this.getName());
			writer.println("sonar.projectName="+this.getName());
			//writer.println("sonar.projectVersion="+projectKey.substring(projectKey.length()));
			writer.println("sonar.language=c++");
			writer.println("sonar.sourceEncoding=UTF-8");
			
			
			StringBuilder modules=new StringBuilder();
			StringBuilder moduleInfos=new StringBuilder();
			boolean once=true;
			for(Component c: components){
				if(!c.isEmpty()){
					if(isWithTest&&once){
						//test folder config
//						writer.print(c.getName()+".sonar.tests=IMT");
//						String qaTestName=QaReportCollection.hasTestFor(name);
//						if(qaTestName!=""){
//							writer.println(","+QaReportCollection.getTestBaseFolder()+"/"+qaTestName);
//							writer.println(c.getName()+".sonar.qa.reportPath="+QaReportCollection.getTestBaseFolder()+"/"+qaTestName+".xml");
//							System.out.println("Test Config output to: "+this.getName()+": " + c.getName());
//						}else{
//							writer.println();
//						}
						//end test folder config
						once=false;
					}
					c.deduplicate();
					modules.append(c.getName()+",");
					moduleInfos.append(c.getName()+".sonar.projectBaseDir="+level+"\n");
					moduleInfos.append(c.getName()+".sonar.sources="+c.getSources()+"\n");
				}
			}
			if(modules.length()==0){
				System.out.println("Empty CCG: Generate Property File for CCG: "+this.getName());
				writer.close();
				return false;
			}
			writer.println("sonar.modules="+modules.toString().substring(0,modules.length()-1));
			writer.println(moduleInfos.toString());
			System.out.println("Generate Property File for CCG: "+this.getName());
			writer.close();
		}catch(Exception ex){
			System.out.println("Generate Property File "+this.getName()+" Failed."+ex.getMessage());
		}
		return true;
	}
}
