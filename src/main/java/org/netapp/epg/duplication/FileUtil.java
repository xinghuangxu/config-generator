package org.netapp.epg.duplication;

import java.io.File;

public class FileUtil {

	
	public static File[] getFiles(File dir){
		return dir.listFiles();
	}
	
	
}
