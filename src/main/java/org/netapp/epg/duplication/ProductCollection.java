package org.netapp.epg.duplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.netapp.epg.Config;

public class ProductCollection {

	private File baseDir;
	private List<Product> products;
	private static int prodCount=0;

	
	private ProductCollection(String baseDir) {
		this.baseDir=new File(baseDir);
		this.products=new ArrayList<Product>();
	}
	
	
	public static ProductCollection deduplicate(){
		ProductCollection pc=new ProductCollection(Config.getBaseDir());
		pc.scanProducts();
		pc.reduceCode();
		return pc;
	}
	
	private void scanProducts() {
		//get all the sub directories
		File[] subs=this.baseDir.listFiles();
		for(int i=0;i<subs.length;i++){
			if(subs[i].isDirectory()&&!subs[i].getName().equals("sonar")){
				products.add(new Product(subs[i])); //create product
				ProductCollection.prodCount++;
			}
		}
	}


	private void reduceCode() {
		MapEntry.reverseCount(prodCount);
		Map.consolidate();
	}


	
}
