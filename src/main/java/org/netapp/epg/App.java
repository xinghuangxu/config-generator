package org.netapp.epg;


import org.apache.log4j.BasicConfigurator;

public class App {

	public static void main(String[] args) throws Exception {
		

		//Initialize all the configuration
		// Set up a simple configuration that logs on the console.
		BasicConfigurator.configure();
		
		String baseDir = "";
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-b")) {
				baseDir = args[i + 1];
			}
		}
		Config config = Config.getInstance(baseDir);
		

		config.generateProperties();
		//SonarRunner.runCcg(Config.getBaseDir());
		SonarRunner.runBoxcar(Config.getBaseDir());
		System.exit(0);
		
	}
}
