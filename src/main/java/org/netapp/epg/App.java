package org.netapp.epg;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
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
