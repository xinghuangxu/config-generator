package org.netapp.epg.duplication;

import java.io.File;

public class Product {
	
	private File baseDir;
	private Map map;
	private String name;
	
	public Product(File baseDir){
		this.name=baseDir.getName();
		this.baseDir=baseDir;
		this.map=new Map(this);
	}

	public String getPath() {
		// TODO Auto-generated method stub
		return this.baseDir.getAbsolutePath();
	}
	
	public int getUniqueFileCount(int prodCount){
		return map.getUniqueCount(prodCount);
	}

	

}
