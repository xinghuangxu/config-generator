package org.netapp.epg;

//Import log4j classes.
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class App {
	
	// Define a static logger variable so that it references the
	// Logger instance named "MyApp".
   static Logger logger = Logger.getLogger(App.class);
	public static void main(String[] args) {
		
		 // Set up a simple configuration that logs on the console.
	     BasicConfigurator.configure();
	     
	     
		String baseDirs="";
		for(int i=0;i<args.length;i++){
			if(args[0]=="-b"){
				baseDirs=args[1];
			}
		}
		Config config = new Config(baseDirs);
//		String source = "config";
//		if (args.length>0&&args[0] != "") {
//			source = args[0];
//		}
//		
//		String level="boxcar";
//		if(args.length>1&&args[1]!=""){
//			level=args[1];
//		}
		config.generateProperties();
	}
}
