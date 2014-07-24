package org.netapp.epg;

//Import log4j classes.
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import org.netapp.epg.db.CFWEmployeeDb;
import org.netapp.epg.duplication.ProductCollection;

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
		
		//Config.LOG.info("Start Reducing the code base.");
		//ProductCollection.deduplicate();
		
		// String source = "config";
		// if (args.length>0&&args[0] != "") {
		// source = args[0];
		// }
		//
		// String level="boxcar";
		// if(args.length>1&&args[1]!=""){
		// level=args[1];
		// }
		config.generateProperties();
		SonarRunner.runCcg(Config.getBaseDir());
		System.exit(0);
	}
}
