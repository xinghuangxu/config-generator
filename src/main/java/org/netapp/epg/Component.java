package org.netapp.epg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.netapp.epg.duplication.Deduplicator;

public class Component {
	
	private static Map<String, Component> dic = new HashMap<String, Component>(); //easy look up
	private String name;
	private List<Folder> folders;
	private List<String> srcs=new ArrayList<String>();
	private CCG parent;
	private Deduplicator dp;
	
	public static Map<String, Component> getMap(){
		return dic;
	}
	
	public static Component find(String name){
		Component comp=dic.get(name);
		if(comp!=null){
			return comp;
		}
		return null;
	}

	
	public Component(String name) {
		this.name=name;
		this.folders=new ArrayList<Folder>();
		dic.put(name, this);
		//System.out.println("Create Component:"+name);
	}
	
	public void setFolders(List<Folder> folders){
		this.folders=folders;
		if(folders!=null){
			for(Folder f:folders){
				f.setParent(this);
			}
		}
	}
	
	public void addFolder(Folder f){
		folders.add(f);
		f.setParent(this);
	}
	
	public void setParent(CCG ccg){
		this.parent=ccg;
	}
	
	public CCG getParent(){
		return parent;
	}
	
	public String getName(){
		return name;
	}

	public boolean isEmpty() {
		if(folders==null||folders.size()==0)
			return true;
		return false;
	}
	
	
	public boolean isEmptyAfterMerge(){
		if(srcs==null||srcs.size()==0){
			return true;
		}
		return false;
	}

	/*
	 * Get component sources 
	 * like: 26x0/Application/RAID1/cmd,2701/Application/RAID1/cmd,5468/Application/RAID1/cmd,5501/Application/RAID1/cmd
	 */
	public String getSources() {
		StringBuilder sb=new StringBuilder();
		for(String s: srcs){
			sb.append(s+",");
		}
		if(sb.length()>0){
			return sb.toString().substring(0,sb.length()-1);
		}
		return "";
	}

	public List<Folder> getFolders() {
		return folders;
	}
	
	public void deduplicate(){
		this.dp=new Deduplicator(folders);
		dp.deduplicate();
	}

	public void generateSource(String base) {
		this.srcs=this.dp.generateSource(base,this.name);
	}

	public static String getSource(String cmpname, String key) {
		File f=(new File(key)).getParentFile();
		while(f!=null&&!f.getName().equals(cmpname)){
			f=f.getParentFile();
		}
		if(f==null){
			return "";
		}
		return f.getPath();
	}

}
