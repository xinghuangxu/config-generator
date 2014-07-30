package org.netapp.epg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.netapp.epg.qa.QaReportCollection;

public class Config {
	public static final Logger LOG = Logger
			.getLogger("NetApp.Epg.Sonar.ConfigGenerator");

	private List<String> baseDirectories;

	private static List<String> sourceDirectories;

	private String ccgMapFilePath = "config/ccg-map.csv";
	private CCGMap ccgMap;

	private String boxcarMapFilePath = "config/boxcar-mapping.csv";
	private BoxcarMap boxcarMap;

	private String qaTestsFolder = "config/qa-test-reports";
	private QaReportCollection qrc;

	private static String sonarHostUrl;
	private static String sonarJdbcUrl;

	private static Config config = null;

	private String baseDir;

	public static String getBaseDir() {
		Config config = Config.getInstance();
		return config.baseDir;
	}

	public static Config getInstance() {
		return config;
	}

	public static Config getInstance(String baseDir) {
		if (config == null) {
			config = new Config(baseDir);
		}
		return config;
	}

	/*
	 * Create a config object from reading in the 'config.sonar' file at the
	 * root directory of the application
	 */
	private Config(String basedir) {
		baseDirectories = new ArrayList<String>();
		sourceDirectories = new ArrayList<String>();
		try {
			System.out.println("Read Config File.");
			BufferedReader br = new BufferedReader(new FileReader(
					"config.sonar"));

			// pass the base directories, if pass by the command line parameter
			// then use the command line one
			// otherwise use the one in the config.sonar file
			System.out.println("Read Base Directory Config.");
			String line = br.readLine();
			String par;
			String src = "";
			String[] dirs = null;
			if (basedir != "") {
				this.baseDir = basedir;
			} else {
				par = line.substring(line.indexOf('=') + 1);
				this.baseDir = par;
			}
			if (basedir != null) {
				src = basedir;
				File file = new File(src);
				dirs = file.list(new FilenameFilter() {
					@Override
					public boolean accept(File current, String name) {
						if (name.equals("sonar"))
							return false;
						return new File(current, name).isDirectory();
					}
				});
			}
			for (int i = 0; i < dirs.length; i++) {
				baseDirectories.add(src + "/" + dirs[i] + "/Application");
			}

			// read in the source
			String[] r;
			System.out.println("Read Source Directories.");
			line = br.readLine();
			r = line.split("=");
			dirs = r[1].split(",");
			for (int i = 0; i < dirs.length; i++) {
				sourceDirectories.add(dirs[i]);
			}

			line = br.readLine();
			r = line.split("=");
			this.sonarHostUrl = r[1];

			line = br.readLine();
			r = line.split("=");
			this.sonarJdbcUrl = r[1];

			this.writeToSonarConfig();

			br.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	private void writeToSonarConfig() {
		try {
			File confFile = new File(
					"sonar-runner/conf/sonar-runner.properties");
			PrintWriter writer = new PrintWriter(confFile);
			writer.println("sonar.host.url= " + Config.getSonarHostUrl());
			writer.println("sonar.jdbc.url= " + Config.getSonarJdbcUrl());
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static String getSonarJdbcUrl() {
		return sonarJdbcUrl;
	}

	public static String getSonarHostUrl() {
		return sonarHostUrl;
	}

	public static List<String> getSourceDirectories() {
		return sourceDirectories;
	}

	public void generateProperties() throws Exception {

		// generate the folder structure
		Folder root = null;
		root = new Folder(); // generate the folder trees
		root.setPath(new File(baseDirectories.get(0)).getParentFile()
				.getParent());
		for (int i = 0; i < baseDirectories.size(); i++) {
			Folder prod = new Folder();
			prod.setPath(baseDirectories.get(i));
			root.addFolder(prod);
			for (int j = 0; j < sourceDirectories.size(); j++) {
				Folder nf = new Folder(baseDirectories.get(i) + "/"
						+ sourceDirectories.get(j));
				prod.addFolder(nf);
			}
		}
		System.out.println("Folder Initialized!");

		// read in ccg mapping
		ccgMap = new CCGMap(this.ccgMapFilePath);
		

		// read in boxcar mapping
		boxcarMap = new BoxcarMap(this.boxcarMapFilePath);
		

		// read in the qa tests report
		qrc = new QaReportCollection(this.qaTestsFolder);
		qrc.makeFolder(Config.getBaseDir());
		qrc.generateTestReport(Config.getBaseDir());
		
		ccgMap.generate(root);
		boxcarMap.generate(root);

		generateShellScript();
	}

	private void generateShellScript() {
		// ccg script

		// boxcar script
	}

	public static File getRoot() {
		return new File(Config.getBaseDir());
	}

}
