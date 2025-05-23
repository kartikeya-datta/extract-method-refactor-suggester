error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9426.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9426.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9426.java
text:
```scala
s@@ = "true";

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.core.gui.profiles;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.columba.core.base.OSInfo;
import org.columba.core.io.DiskIO;
import org.columba.core.xml.XmlElement;
import org.columba.core.xml.XmlIO;

/**
 * Manages profiles consisting of configuration folders.
 * <p>
 * Every profile has a name and a loation pointing to the configuration folder.
 * <p>
 * A profiles.xml configuration file is saved in the default config directory,
 * storing all profiles information.
 * 
 * @author fdietz
 */
public class ProfileManager implements IProfileManager {

	/**
	 * location of configuration directory
	 */
	private File location;

	/**
	 * profiles.xml file
	 */
	private File profilesConfig;

	/**
	 * top-level xml node
	 */
	private XmlElement profiles;

	/**
	 * using singleton pattern to instanciate class
	 */
	private static ProfileManager instance = new ProfileManager();

	/**
	 * Comment for <code>xml</code>
	 */
	private XmlIO xml;

	/**
	 * Currently running profile.
	 */
	private Profile currentProfile;

	/**
	 * default constructor
	 */
	public ProfileManager() {
		super();

		if (OSInfo.isWindowsPlatform()) {
			location = new File("config");
		} else {
			location = new File(System.getProperty("user.home"), ".columba");
		}

		// create directory, if it doesn't already exist
		DiskIO.ensureDirectory(location);

		profilesConfig = new File(location, "profiles.xml");

	}

	/**
	 * Get instance of class
	 * 
	 * @return instance
	 */
	public static ProfileManager getInstance() {

		return instance;
	}

	/* (non-Javadoc)
	 * @see org.columba.core.profiles.IProfileManager#getProfileForName(java.lang.String)
	 */
	public Profile getProfileForName(String name) {
		if (name.equalsIgnoreCase("default")) {
			return new Profile("Default", location);
		}

		XmlElement profile = getXmlElementForName(name);
		if (profile == null)
			return null;

		String n = profile.getAttribute("name");

		location = new File(profile.getAttribute("location"));
		return new Profile(n, location);

	}

	/**
	 * Remove profile xml-element from "profiles.xml"
	 * 
	 * @param name
	 *            name of profile
	 */
	protected void removeProfileXmlElement(String name) {
		XmlElement child = getXmlElementForName(name);
		child.removeFromParent();

	}

	protected XmlElement getXmlElementForName(String name) {
		for (int i = 0; i < profiles.count(); i++) {

			XmlElement profile = profiles.getElement(i);
			String n = profile.getAttribute("name");
			if (name.equalsIgnoreCase(n)) {

				return profile;
			}
		}
		return null;
	}

	/**
	 * Get profile with location.
	 * 
	 * @param path
	 *            location of configuration directory
	 * @return profile if available. Otherwise, return null.
	 */
	protected Profile getProfileForLocation(String path) {
		for (int i = 0; i < profiles.count(); i++) {
			XmlElement profile = profiles.getElement(i);
			String location = profile.getAttribute("location");
			if (path.equals(location)) {
				String n = profile.getAttribute("name");
				return new Profile(n, new File(location));
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.columba.core.profiles.IProfileManager#getProfile(java.lang.String)
	 */
	public Profile getProfile(String location) {
		// load profiles.xml
		loadProfilesConfiguration();

		currentProfile = null;
		if (location == null) {
			// prompt user for profile
			currentProfile = promptForProfile();
		} else {
			// use commandline-specified location

			// try name first
			currentProfile = getProfileForName(location);

			if (currentProfile == null) {
				// try directory
				currentProfile = getProfileForLocation(location);
			}

			if (currentProfile == null) {
				// create profile
				XmlElement profileElement = new XmlElement("profile");
				profileElement.addAttribute("name", location);
				profileElement.addAttribute("location", location);
				profiles.addElement(profileElement);

				// save to profiles.xml
				try {
					xml.save();
				} catch (Exception e) {
					e.printStackTrace();
				}

				currentProfile = getProfileForLocation(location);
			}
		}

		return currentProfile;
	}

	/* (non-Javadoc)
	 * @see org.columba.core.profiles.IProfileManager#getSelectedProfile()
	 */
	public String getSelectedProfile() {
		String selected = null;
		selected = profiles.getAttribute("selected");

		if (selected == null)
			selected = "Default";

		return selected;
	}

	/**
	 * Set always ask parameter.
	 * 
	 * @param alwaysAsk
	 *            true, if user should be always asked which profile to use.
	 *            False, otherwise.
	 */
	public void setAlwaysAsk(boolean alwaysAsk) {
		if (alwaysAsk) {
			profiles.addAttribute("dont_ask", "false");
		} else {
			profiles.addAttribute("dont_ask", "true");
		}

		//		 save to profiles.xml
		try {
			xml.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check if user should always be prompted for a profile on startup.
	 * 
	 * @return true, if user should be always asked. False, otherwise.
	 */
	public boolean isAlwaysAsk() {
		String s = profiles.getAttribute("dont_ask");
		if (s == null)
			s = "false";

		if (s.equals("true"))
			return false;

		return true;
	}

	/**
	 * Open dialog and prompt user for profile
	 * 
	 * @return profile
	 */
	protected Profile promptForProfile() {
		String s = profiles.getAttribute("dont_ask");
		if (s == null)
			s = "false";

		boolean dontAsk = Boolean.valueOf(s).booleanValue();

		// use preselected profile
		if (dontAsk) {
			String selected = profiles.getAttribute("selected");
			Profile p = getProfileForName(selected);

			if (p == null) {
				// fall back to default profile

				p = new Profile("Default", location);
			}

			return p;
		}

		// show profile choosing dialog
		ProfileChooserDialog d = new ProfileChooserDialog();
		String profileName = d.getSelection();

		if (profileName.equals("Default")) {
			profiles.addAttribute("selected", "Default");
			profiles.addAttribute("dont_ask", new Boolean(d
					.isDontAskedSelected()).toString());

			// save to profiles.xml
			try {
				xml.save();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return new Profile("Default", location);
		} else {
			profiles.addAttribute("selected", profileName);
			profiles.addAttribute("dont_ask", new Boolean(d
					.isDontAskedSelected()).toString());

			// save to profiles.xml
			try {
				xml.save();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return getProfileForName(profileName);
		}
	}

	/* (non-Javadoc)
	 * @see org.columba.core.profiles.IProfileManager#getProfiles()
	 */
	public XmlElement getProfiles() {
		return profiles;
	}

	/**
	 * Add new profile.
	 * 
	 * @param p
	 *            new profile
	 */
	public void addProfile(Profile p) {
		XmlElement profile = new XmlElement("profile");
		profile.addAttribute("name", p.getName());
		profile.addAttribute("location", p.getLocation().getPath());

		profiles.addElement(profile);

		// save to profiles.xml
		try {
			xml.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void renameProfile(String oldName, String newName) {
		XmlElement element = getXmlElementForName(oldName);
		element.addAttribute("name", newName);

		// save to profiles.xml
		try {
			xml.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load profile configuration.
	 */
	protected void loadProfilesConfiguration() {
		if (!profilesConfig.exists()) {
			// create profile config file
			String hstr = "org/columba/core/config/profiles.xml";
			try {
				DiskIO.copyResource(hstr, profilesConfig);
			} catch (IOException e) {
			}
		}

		// load profile config file
		try {
			URL url = profilesConfig.toURL();
			xml = new XmlIO(url);
			xml.load();
			profiles = xml.getRoot().getElement("profiles");

		} catch (MalformedURLException mue) {
		}
	}

	/* (non-Javadoc)
	 * @see org.columba.core.profiles.IProfileManager#getCurrentProfile()
	 */
	public Profile getCurrentProfile() {
		return currentProfile;
	}

	public void setCurrentProfile(String path) {
		currentProfile = getProfile(path);
	}

	/**
	 * Remove all Columba-config files and skip everything else
	 * <p>
	 * This way a user could use his Windows directory as config-folder and
	 * wouldn't risk to loose files when deleting his profile.
	 * 
	 * @param profile
	 *            name of profile
	 */
	public void removeProfile(String profile) {
		Profile p = getProfileForName(profile);

		String[] folders = new String[] { "mail", "addressbook", "chat", "log" };
		String[] files = new String[] { "options.xml", "external_tools.xml" };

		File location = p.getLocation();

		// Is the location still existing?
		if (location.exists()) {

			// delete all directories
			for (int i = 0; i < folders.length; i++) {
				//	delete directory recursivly
				DiskIO.deleteDirectory(new File(location, folders[i]));
			}

			// delete all files
			for (int i = 0; i < files.length; i++) {
				new File(location, files[i]).delete();
			}

			// if config-folder is really empty
			// -> delete folder
			if (location.listFiles().length == 0)
				location.delete();
		}

		// remove profile xml-element
		removeProfileXmlElement(profile);
	}
}

```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9426.java