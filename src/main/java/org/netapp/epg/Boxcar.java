package org.netapp.epg;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.netapp.epg.qa.QaReportCollection;

public class Boxcar {

	private String CQID;
	private String boxcarName;
	private String almPrefix;

	public String getName() {
		return this.boxcarName;
	}
	
	public String getAlmPrefix(){
		return this.almPrefix;
	}

	private Map<String, Component> components;
	private Map<String, CCG> ccgs;

	public Boxcar(String CQID, String boxcarName,String almPrefix) {
		this.CQID = CQID;
		this.almPrefix=almPrefix;
		this.boxcarName = boxcarName.replace(" ", "_");
		components = new HashMap<String, Component>();
		ccgs = new HashMap<String, CCG>();
	}

	// relate component to boxcar
	public boolean addComp(String name) {
		Component comp = Component.find(name);
		if (comp != null) {
			if (components.get(comp.getName()) == null) {
				components.put(name,  new Component(comp.getName()));
				CCG ccg = comp.getParent();
				if (ccgs.get(ccg.getName()) == null) {
					ccgs.put(ccg.getName(), new CCG(ccg.getName()));
				}
				ccgs.get(ccg.getName()).addComponent(components.get(comp.getName()));
				components.get(comp.getName()).setFolders(comp.getFolders());
				return true;
			}
		}
		return false;
	}

	// relate source to component and ccg
	public void addSource(String source) {
		Folder f = Folder.find(source);
		if (f != null) {
			// locate the component
			Component c = f.getParent();
			if (c != null) {
				if (components.get(c.getName()) == null) {
					components.put(c.getName(), new Component(c.getName()));
					CCG ccg = c.getParent();
					if (ccgs.get(ccg.getName()) == null) {
						ccgs.put(ccg.getName(), new CCG(ccg.getName()));
					}

					ccgs.get(ccg.getName()).addComponent(
							components.get(c.getName()));
				}
				components.get(c.getName()).setFolders(c.getFolders());
			}
		}
	}

	public void generateSonarProperty(String projectKey, String fileName,
			String level) {
		File file = new File(fileName);
		file.getParentFile().mkdirs();
		try {
			PrintWriter writer = new PrintWriter(file);
			writer.println("sonar.projectKey=epg.netapp.com:" + projectKey
					+ "_bc_" + this.getName());
			writer.println("sonar.projectName=Kingston_" + this.getName());
			writer.println("sonar.projectVersion="
					+ Config.getRoot().getName());
			writer.println("sonar.language=c++");
			writer.println("sonar.sourceEncoding=UTF-8");

			// writer.println("sonar.projectBaseDir=../../../Application");
			StringBuilder modules = new StringBuilder();
			StringBuilder sb = new StringBuilder();
			Iterator<Entry<String, CCG>> it = ccgs.entrySet().iterator();
			boolean once = true;
			while (it.hasNext()) {
				CCG c = it.next().getValue();
				it.remove();
				sb.append(c.getName() + ".sonar.projectBaseDir=" + c.getName()
						+ "\n");
				if(
				c.generateSonarProperty(projectKey,
						file.getParent() + "/" + c.getName()
								+ "/sonar-project.properties", "../../../../",
						this.getName(), once, this.getName())){
					modules.append(c.getName() + ",");
					if (once)
						once = !once;
				}
				
				
			}
			writer.println("sonar.modules="
					+ modules.toString().substring(0, modules.length() - 1));
			writer.println(sb.toString());
			writer.close();
		} catch (Exception ex) {
			System.out.println("Generate Property File " + this.getName()
					+ " Failed." + ex.getMessage());
		}
	}

}
