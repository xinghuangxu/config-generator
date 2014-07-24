package org.netapp.epg.duplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.netapp.epg.Folder;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

public class Deduplicator {

	Map<String, List<Integer>> filechk;
	File base;

	public Deduplicator(List<Folder> folders) {
		this.filechk = new HashMap<String, List<Integer>>();
		for (int i = 0; i < folders.size(); i++) {
			
			this.base = new File(folders.get(i).getPath());
			this.addFolder(this.base);
		}
	}

	private void addFolder(File f) {
		File[] files = f.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()&&!this.isIgnore(files[i])) {
				this.addFile(files[i]);
			} else if (files[i].isDirectory()) {
				this.addFolder(files[i]);
			}
		}
	}

	private boolean isIgnore(File file) {
		String name=file.getName();
		String extension = "";

		int i = name.lastIndexOf('.');
		if (i > 0) {
		    extension = name.substring(i+1);
		}
		if(extension.equals("h")||extension.equals("cc")||extension.equals("c")){
			return false;
		}
//		if(extension.equals("ignore"))
//			return true;
		return true;
	}

	private void addFile(File file) {
		try {
			HashCode crc32 = Files.hash(file, Hashing.crc32());
			int crc32Int = crc32.asInt();
			String key=this.base.toURI().relativize(file.toURI()).getPath();
			if (!this.filechk.containsKey(key)) {
				List<Integer> chks = new ArrayList<Integer>();
				this.filechk.put(key, chks);
			}
			List<Integer> chks = this.filechk.get(key);
			if(chks.contains(crc32Int)){
				file.renameTo(new File(file.getPath()+".ignore"));
			}else{
				chks.add(crc32Int);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void deduplicate() {
		// TODO Auto-generated method stub

	}

}
