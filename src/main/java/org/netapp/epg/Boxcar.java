package org.netapp.epg;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Boxcar {
	
	private String CQID;
	private String boxcarName;
	
	public String getName(){
		return this.boxcarName;
	}
	
	private Map<String,Component> components;
	private Map<String,CCG> ccgs;
	
	public Boxcar(String CQID,String boxcarName){
		this.CQID=CQID;
		this.boxcarName=boxcarName.replace(" ", "_");
		components=new HashMap<String, Component>();
		ccgs=new HashMap<String, CCG>();
	}

	
	public void addSource(String source) {
		Folder f=Folder.find(source);
		if(f!=null){
			Component c=f.getParent();
			if(c!=null){
				if(components.get(c.getName())==null){
					components.put(c.getName(), new Component(c.getName()));
					CCG ccg=c.getParent();
					if(ccgs.get(ccg.getName())==null){
						ccgs.put(ccg.getName(), new CCG(ccg.getName()));
					}
					ccgs.get(ccg.getName()).addComponent(components.get(c.getName()));
				}
				components.get(c.getName()).addFolder(f.clone());
			}
		}
	}

	public void generateSonarProperty(String projectKey, String fileName,
			String level) {
		File file=new File(fileName);
		file.getParentFile().mkdirs();
		try{
			PrintWriter writer=new PrintWriter(file);
			writer.println("sonar.projectKey=epg.netapp.com:"+projectKey+"_bc_"+this.getName());
			writer.println("sonar.projectName=Kingston_"+this.getName());
			writer.println("sonar.projectVersion="+projectKey.substring(projectKey.length()-5));
			writer.println("sonar.language=c++");
			writer.println("sonar.sourceEncoding=UTF-8");
			
			
			StringBuilder modules=new StringBuilder();
			StringBuilder sb=new StringBuilder();
			Iterator<Entry<String, CCG>> it=ccgs.entrySet().iterator();
			while(it.hasNext()){
				CCG c=it.next().getValue();
				it.remove();
				modules.append(c.getName()+",");
				sb.append(c.getName()+".sonar.projectBaseDir="+c.getName()+"\n");
				c.generateSonarProperty(projectKey,file.getParent()+"/"+c.getName()+"/sonar-project.properties","../../../../",this.getName());
			}
			writer.println("sonar.modules="+modules.toString().substring(0,modules.length()-1));
			writer.println(sb.toString());
			writer.close();
		}catch(Exception ex){
			System.out.println("Generate Property File "+this.getName()+" Failed."+ex.getMessage());
		}
	}
	
}
