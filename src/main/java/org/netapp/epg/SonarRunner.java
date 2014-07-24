package org.netapp.epg;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;

public class SonarRunner {

	public static void runCcg(String root) {
//		File pathToExecutable = new File("sonar-runner/bin/sonar-runner.bat");
//		String command=pathToExecutable.getAbsolutePath();
//		//"cd "+root+"/sonar/ccg & "+
//		Process p;
		try {
			String ccgConfigPath = root + "/sonar/ccg";
			File pathToExecutable = new File("sonar-runner/bin/sonar-runner.bat");
			ProcessBuilder builder = new ProcessBuilder(
					pathToExecutable.getAbsolutePath());
			builder.directory(new File(ccgConfigPath).getAbsoluteFile());
			builder.redirectErrorStream(true);
			Process process = builder.start();
			

			Scanner s = new Scanner(process.getInputStream());
			while (s.hasNextLine()) {
				Config.LOG.info(s.nextLine());
			}
			s.close();

			int result = process.waitFor();

			Config.LOG.info("Process exited with result"+
					result);
//			p = Runtime.getRuntime().exec(command);
//			p.waitFor();
//			BufferedReader reader = 
//                            new BufferedReader(new InputStreamReader(p.getInputStream()));
// 
//                        String line = "";			
//			while ((line = reader.readLine())!= null) {
//				Config.LOG.info(line);
//			}
			
		} catch (Exception e) {
			Config.LOG.info("Sonar-runner CCG run failed!");
			e.printStackTrace();
		}

	}

	public static void runBoxcar() {

	}
}
