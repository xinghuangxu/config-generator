package org.netapp.epg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import sun.security.krb5.internal.LocalSeqNumber;

public class CCGMap {
	private List<CCG> ccgs;
	
	public CCGMap(String filePath) {
		ccgs=new ArrayList<CCG>();
		try {
			System.out.println("Read Config File.");
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			System.out.println("Read ccg-map.");
			br.readLine();
			String line = br.readLine();
			String[] row=line.split(",");
			CCG nccg=new CCG(row[0]);
			Component ncomponent=new Component(row[1]);
			ncomponent.setFolders(Folder.getMap().get(ncomponent.getName()));
			nccg.addComponent(ncomponent);
			while((line=br.readLine())!=null){
				row=line.split(",");
				if(!nccg.getName().equals(row[0])){
					ccgs.add(nccg);
					nccg=new CCG(row[0]);
				}
				ncomponent=new Component(row[1]);
				ncomponent.setFolders(Folder.getMap().get(ncomponent.getName()));
				nccg.addComponent(ncomponent);
			}
			ccgs.add(nccg);
			br.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void generate(Folder root) {
		System.out.println("Generate ccg level sonar properties.");
		File baseDir=new File(root.getPath());
		String projectFilePath=baseDir.getPath()+"/../sonar/ccg/sonar-project.properties";
		String sonarRunnerScript=baseDir.getPath()+"/../sonar/ccg-run.sh";
		File projectFile=new File(projectFilePath);
		projectFile.getParentFile().mkdirs();
		try{
			PrintWriter writer=new PrintWriter(projectFile);
			String projectKey=baseDir.getParentFile().getName();
			writer.println("sonar.projectKey=epg.netapp.com:"+projectKey+".CCG");
			writer.println("sonar.projectName=RAIDCore_Kingston");//_CCG_"+projectKey);
			writer.println("sonar.projectVersion="+projectKey.substring(projectKey.length()-5));
			writer.println("sonar.language=c++");
			writer.println("sonar.sourceEncoding=UTF-8");
			StringBuilder modules=new StringBuilder();
			StringBuilder sb=new StringBuilder();
			boolean once=true;
			for(CCG c : ccgs){
				if(!c.isEmpty()){
					modules.append(c.getName()+",");
					sb.append(c.getName()+".sonar.projectBaseDir="+c.getName()+"\n");
					c.generateSonarProperty(projectKey,projectFile.getParent()+"/"+c.getName()+"/sonar-project.properties","../../../","coverage",once,"RAIDCore_Kingston");
					once=false;
				}
			}
			writer.println("sonar.modules="+modules.toString().substring(0,modules.length()-1));
			writer.println(sb.toString());
			writer.println("sonar.coverity.source.path="+baseDir.getParent());
			writer.close();
			generateScript(sonarRunnerScript,baseDir.getParent().toString());
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	}

	private void generateScript(String sonarRunnerScript,String basePath) throws FileNotFoundException {
		File scriptFile=new File(sonarRunnerScript);
		PrintWriter writer=new PrintWriter(scriptFile);
		writer.print("cd ccg && sh "+Config.getSonnarRunnerPath());
		writer.close();
		File localScript =new File("ccg-run.sh");
		writer=new PrintWriter(localScript);
		writer.println("cd "+basePath+"/sonar && sh ccg-run.sh");
		writer.close();
	}
}
