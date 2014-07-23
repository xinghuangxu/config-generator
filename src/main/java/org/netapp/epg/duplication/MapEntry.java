package org.netapp.epg.duplication;

import java.util.HashMap;
import java.util.Map;

public class MapEntry {
	
	private static Map<String,MapEntry> dic=new HashMap<String,MapEntry>();
	
	private int tagnum;
	
	private int getTagnum() {
		return this.tagnum;
	}

	public void setTagnum(int num){
		this.tagnum=num;
	}

	
	private String src;
	private String dest;
	
	public MapEntry(String key) {
		int index=key.lastIndexOf(':');
		this.src=key.substring(0,index-1);
		this.dest=key.substring(index+1);
		tagnum=0;
	}

	public MapEntry tag(){
		tagnum++;
		return this;
	}
	
	public static MapEntry getEntry(String key){
		if(!dic.containsKey(key)){
			dic.put(key, new MapEntry(key));
		}
		return dic.get(key).tag();
	}

	public boolean isCommon() {
		if(this.tagnum==0)
			return true;
		return false;
	}

	public static void reverseCount(int prodCount) {
		MapEntry me=null;
		for(String key : dic.keySet()){
			me=dic.get(key);
			me.setTagnum(prodCount-me.getTagnum());
		}
	}
	
	
}
