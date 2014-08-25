package org.netapp.epg.duplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.netapp.epg.Component;
import org.netapp.epg.Config;
import org.netapp.epg.Folder;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;


public class Deduplicator {

	Map<String, Map<Integer,List<String>>> filechk;
	File base;

	public Deduplicator(List<Folder> folders) {
		this.filechk = new HashMap<String, Map<Integer,List<String>>>();
		for (int i = 0; i < folders.size(); i++) {
			
			this.base = new File(folders.get(i).getPath());
			this.addFolder(this.base,folders.get(i).getProdName());
		}
	}

	private void addFolder(File f, String prodname) {
		File[] files = f.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()&&!this.isIgnore(files[i])) {
				this.addFile(files[i],prodname);
			} else if (files[i].isDirectory()) {
				this.addFolder(files[i],prodname);
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

	private void addFile(File file, String prodname) {
		try {
			Map<Integer,List<String>> versionMap=null;
			List<String> versions=null;
			HashCode crc32 = Files.hash(file, Hashing.crc32());
			int crc32Int = crc32.asInt(); //(int) file.length();
			String key=file.getPath().substring(file.getPath().indexOf("Application"));;//this.base.toURI().relativize(file.toURI()).getPath();
			if (!this.filechk.containsKey(key)) {
				versionMap=new HashMap<Integer, List<String>>();
				//List<Integer> chks = new ArrayList<Integer>();
				
				this.filechk.put(key, versionMap);
			}
			versionMap=this.filechk.get(key);
			if(!versionMap.containsKey(crc32Int)){
				versionMap.put(crc32Int,new ArrayList<String>());
			}
			versions=versionMap.get(crc32Int);
			versions.add(prodname);
			//List<Integer> chks = this.filechk.get(key);
//			if(chks.contains(crc32Int)){
//				//file.renameTo(new File(file.getPath()+".ignore"));
//			}else{
//				chks.add(crc32Int);
//			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void deduplicate() {
		// TODO Auto-generated method stub

	}

	public List<String> generateSource(String base,String cmpname) {
		List<String> sources=new ArrayList<String>();
		String compSource=null;
		int prodNum=Config.getProdNum();
		Iterator it=filechk.entrySet().iterator();
		Iterator vit=null;
		while(it.hasNext()){
			Map.Entry paris=(Map.Entry)it.next();
			String key=(String)paris.getKey();  //also the file name
			Map<Integer,List<String>> value=(Map<Integer, List<String>>) paris.getValue();
			vit=value.entrySet().iterator();
			while(vit.hasNext()){
				Map.Entry vparis=(Map.Entry)vit.next();
				List<String> vvalue=(List<String>) vparis.getValue();
				if(vvalue.size()==prodNum){
					try {
						/*crete hard link
						Path target = Paths.get(this.base.getPath()+"\\"+key);
						Path link = Paths.get(base+"\\src\\common\\"+key);
						if(java.nio.file.Files.exists(target)&&java.nio.file.Files.exists(link)){
							java.nio.file.Files.createLink(link,target);
						}
						 */
						
						/*copy file approch*/
						File src=new File(Config.getBaseDir()+"\\"+vvalue.get(0)+"\\"+key);
						File dest=new File(base+"\\src\\common\\"+key);
						dest.getParentFile().mkdirs();
						compSource="common\\"+Component.getSource(cmpname,key);
						this.addSource(compSource,sources);
						Files.copy(src,dest);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					try {
					String configBasePath = Config.getBaseDir();
					String origin=vvalue.get(0);
					String replicate=null;
					/*copy file approch*/
					File src=new File(configBasePath+"\\"+origin+"\\"+key);
					File dest=new File(base+"\\src\\"+origin+"\\"+key);
					dest.getParentFile().mkdirs();
					Files.copy(src,dest);
					compSource=origin+"\\"+Component.getSource(cmpname,key);;
					this.addSource(compSource,sources);
					
					for(int i=1;vvalue.size()>i;i++){
						replicate=vvalue.get(i);
						dest=new File(base+"\\src\\"+replicate+"\\"+key);
						dest.getParentFile().mkdirs();
						dest.createNewFile();
						this.writeOrigin(dest,origin);
						compSource=replicate+"\\"+Component.getSource(cmpname,key);;
						this.addSource(compSource,sources);
					}
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				vit.remove();
			}
			it.remove();
		}
		return sources;
	}

	private void addSource(String compSource,List<String> compsrcs) {
		compSource=compSource.replace('\\', '/');
		if(compsrcs.contains(compSource)){
			return;
		}
		compsrcs.add(compSource);
	}

	private void writeOrigin(File dest, String origin) {
		// TODO Auto-generated method stub
		try {
			PrintWriter writer=new PrintWriter(dest);
			writer.println("//please see file at "+origin);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
