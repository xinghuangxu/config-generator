package org.netapp.epg;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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

	public void generateSonarProperty(String projectKey,String fileName, String level, String codeCoverageReportPath) {
		File file=new File(fileName);
		file.getParentFile().mkdirs();
		try{
			PrintWriter writer=new PrintWriter(file);
			writer.println("sonar.projectKey=epg.netapp.com:"+projectKey+"_"+this.getName());
			writer.println("sonar.projectName="+this.getName());
			writer.println("sonar.projectVersion="+projectKey.substring(projectKey.length()-5));
			writer.println("sonar.language=c++");
			writer.println("sonar.sourceEncoding=UTF-8");
			
			writer.println("sonar.testes="+level+"Application/IMT");
			writer.println("sonar.cxx.indluceDirectories="+level+"Application/RAIDLib");
			writer.println("sonar.cxx.suffixes.sources=.cpp,.cc,.c");
			writer.println("sonar.cxx.suffixes.headers=.h,.hh,.hpp");
			writer.println("sonar.cxx.coverage.reportPath="+codeCoverageReportPath+".xml");
			
			StringBuilder modules=new StringBuilder();
			StringBuilder moduleInfos=new StringBuilder();
			for(Component c: components){
				if(!c.isEmpty()){
					modules.append(c.getName()+",");
					moduleInfos.append(c.getName()+".sonar.projectBaseDir="+level+"Application"+"\n");
					moduleInfos.append(c.getName()+".sonar.sources="+c.getSources()+"\n");
				}
			}
			writer.println("sonar.modules="+modules.toString().substring(0,modules.length()-1));
			writer.println(moduleInfos.toString());
			System.out.println("Generate Property File for CCG: "+this.getName());
			writer.close();
		}catch(Exception ex){
			System.out.println("Generate Property File "+this.getName()+" Failed."+ex.getMessage());
		}
	}
}
