package org.netapp.epg;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.netapp.epg.db.CFWEmployeeDb;

public class BoxcarMap {

	private List<Boxcar> boxcars;

	public BoxcarMap(String filePath) throws Exception {
		boxcars = new ArrayList<Boxcar>();
		//Database connect test
		CFWEmployeeDb dao = new CFWEmployeeDb();
		boxcars=dao.readBoxcarCompData();
//		try {
//			System.out.println("Read Boxcar Mapping File.");
//			BufferedReader br = new BufferedReader(new FileReader(filePath));
//			String line = br.readLine();
//			while ((line = br.readLine()) != null) {
//				String[] cells = line.split(",");
//				if (cells.length > 2) {
//					Boxcar bc = new Boxcar(cells[0], cells[1]);
//					for (int i = 2; i < cells.length; i++) {
//						bc.addSource(cells[i].replace("\"", ""));
//					}
//					boxcars.add(bc);
//				}
//			}
//			br.close();
//			System.out.println("Successfully Parsed Boxcar Mapping File.");
//		} catch (Exception ex) {
//			System.out.println(ex.getMessage());
//		}
	}

	public void generate(Folder root) {
		System.out.println("Generate Boxcar level sonar properties.");
		File baseDir = new File(root.getPath());
		String projectFilePath = baseDir.getPath()
				+ "/sonar/boxcar/sonar-project.properties";
		String sonarRunnerScript=baseDir.getPath()+"/sonar/boxcar-run.sh";
		File projectFile = new File(projectFilePath);
		projectFile.getParentFile().mkdirs();
		try {
			PrintWriter writer = new PrintWriter(projectFile);
			String projectKey = baseDir.getParentFile().getName();
			writer.println("sonar.projectKey=epg.netapp.com:" + projectKey
					+ ".Boxcar");
			writer.println("sonar.projectName=RAIDCore_Kingston_Boxcar_"
					+ projectKey);
			writer.println("sonar.projectVersion="
					+ projectKey.substring(projectKey.length() - 5));
			writer.println("sonar.language=c++");
			writer.println("sonar.sourceEncoding=UTF-8");
			StringBuilder modules = new StringBuilder();
			StringBuilder sb = new StringBuilder();
			for (Boxcar bc : boxcars) {
				modules.append(bc.getName() + ",");
				sb.append(bc.getName() + ".sonar.projectBaseDir="
						+ bc.getName() + "\n");
				bc.generateSonarProperty(projectKey, projectFile.getParent()
						+ "/" + bc.getName() + "/sonar-project.properties",
						"../../../");
			}
			writer.println("sonar.modules="
					+ modules.toString().substring(0, modules.length() - 1));
			writer.println(sb.toString());
			writer.println("sonar.coverity.source.path=" + baseDir.getParent());
			writer.close();
			generateScript(sonarRunnerScript,baseDir.getParent().toString());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void generateScript(String sonarRunnerScript,String basePath) throws FileNotFoundException {
		File scriptFile=new File(sonarRunnerScript);
		PrintWriter writer=new PrintWriter(scriptFile);
		writer.print("for dir in boxcar/*; do (cd \"$dir\" && sh "+Config.getSonnarRunnerPath()+"); done");
		writer.close();
		
		File localScript =new File("boxcar-run.sh");
		writer=new PrintWriter(localScript);
		writer.println("cd "+basePath+"/sonar && sh boxcar-run.sh");
		writer.close();
	}

}
