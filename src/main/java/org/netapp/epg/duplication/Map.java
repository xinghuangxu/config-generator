package org.netapp.epg.duplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.netapp.epg.Config;

public class Map {

	public static final String mapFilRelPath = "/records/full/map/CCM2REP.file";

	private static int mapCount = 0;

	private List<MapEntry> entries;

	private Product prod;

	public Map(Product prod) {
		Map.mapCount++;
		this.prod = prod;
		this.entries = new ArrayList<MapEntry>();
		this.readMapFile();
	}

	private void readMapFile() {
		try {
			String mapFilePath = prod.getPath() + Map.mapFilRelPath;
			System.out.println("Read Map File:" + mapFilePath);
			BufferedReader br = new BufferedReader(new FileReader(mapFilePath));
			String line;
			while ((line = br.readLine()) != null) {
				this.entries.add(MapEntry.getEntry(line));
				; // add each line of reading into entries
			}
			br.close();
		} catch (Exception ex) {
			Config.LOG.warn(ex.getMessage());
		}
	}

	public static void consolidate() {
		// TODO Auto-generated method stub

	}

	public int getUniqueCount(int prodCount) {
		int count = 0;
		for (int i = 0; i < entries.size(); i++) {
			if (!entries.get(i).isCommon()) {
				count++;
			}
		}
		return count;
	}

	

}
