package org.netapp.epg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Folder {

	private static Map<String, List<Folder>> dic = new HashMap<String, List<Folder>>();
	private String path;
	private String name;
	private Component parent;
	private List<Folder> subFolders;
	
	public static Map<String, List<Folder>> getMap(){
		return dic;
	}
	
	public void setParent(Component component){
		this.parent=component;
	}
	
	public Component getParent(){
		return parent;
	}
	
	public String getPath(){
		return this.path;
	}
	
	public String getName(){
		return this.name;
	}
	

	public void setPath(String path) {
		this.path = path;
		this.name = new File(path).getName();//path.substring(path.lastIndexOf('\\') + 1);
		if(dic.get(this.name)==null){
			dic.put(this.name, new ArrayList<Folder>());
		}
		List<Folder> val=dic.get(this.name);
		val.add(this);
	}

	public Folder() {
		subFolders = new ArrayList<Folder>();
		this.parent=null;
	}

	public Folder(String path) { //loop through all the sub folders if you don't want it to do that please use the constructor above
		this();
		this.setPath(path);
		File directory = new File(path);
		File[] fList = directory.listFiles();
		if (fList != null) {
			for (File file : fList) {
				if (file.isDirectory()) {
					subFolders.add(new Folder(path + "/" + file.getName()));
				}
			}
		}
	}

	public void addFolder(Folder f) {
		subFolders.add(f);
	}

	public String getRelativePath(String dirname) {
		int index=this.path.indexOf(dirname)+dirname.length();
		return this.path.substring(index+1);
	}
	
	
	public static Folder find(String source){
		source=source.trim();
		int i=source.lastIndexOf('/');
		String name=source.substring(i+1);
		List<Folder> folders=dic.get(name);
		if(folders!=null){
			for(Folder f: folders){
				if(f.getPath().indexOf(source)!=-1){
					return f;
				}
			}
		}
		return null;
	}

	
	public Folder clone(){
		Folder c=new Folder();
		c.name=this.name;
		c.path=this.path;
		return c;
	}
}
