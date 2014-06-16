package org.netapp.epg;

import java.util.ArrayList;
import java.util.List;

public class Component {
	
	private String name;
	private List<Folder> folders;
	private CCG parent;
	
	public Component(String name) {
		this.name=name;
		this.folders=new ArrayList<Folder>();
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

	public String getSources() {
		StringBuilder sb=new StringBuilder();
		for(Folder f: folders){
			sb.append(f.getRelativePath("Application")+",");
		}
		return sb.toString().substring(0,sb.length()-1);
	}
	
	

}
