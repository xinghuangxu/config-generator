package org.netapp.epg;

import java.io.File;
import java.util.Scanner;

public class SonarRunner {

	public static void runCcg(String root) {
		String ccgConfigPath = root + "/sonar/ccg";
		SonarRunner.run(ccgConfigPath);
	}

	private static void run(String dirPath) {
		try {
			String system = System.getProperty("os.name");
			String shellExe = "sonar-runner";
			if (system.contains("Windows")) {
				shellExe = "sonar-runner.bat";
			}
			File pathToExecutable = new File("sonar-runner/bin/" + shellExe);
			ProcessBuilder builder = new ProcessBuilder(
					pathToExecutable.getAbsolutePath());
			builder.directory(new File(dirPath).getAbsoluteFile());
			builder.redirectErrorStream(true);
			Process process = builder.start();

			Scanner s = new Scanner(process.getInputStream());
			while (s.hasNextLine()) {
				Config.LOG.info(s.nextLine());
			}
			s.close();

			int result = process.waitFor();

			Config.LOG.info("Process exited with result" + result);
		} catch (Exception e) {
			Config.LOG.info("Sonar-runner CCG run failed!");
			e.printStackTrace();
		}
	}

	public static void runBoxcar(String root) {
		String bcConfigPath = root + "/sonar/boxcar";
		File bcFolder = new File(bcConfigPath);
		File[] allBoxcars = bcFolder.listFiles();
		for (int i = 0; i < allBoxcars.length; i++) {
			if (allBoxcars[i].isDirectory()) {
				SonarRunner.run(allBoxcars[i].getPath());
			}
		}
	}
}
