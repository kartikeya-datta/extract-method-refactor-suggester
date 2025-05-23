error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3893.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3893.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,37]

error in qdox parser
file content:
```java
offset: 37
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3893.java
text:
```scala
"org/columba/mail/config/convert.py",@@

// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

package org.columba.core.config;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.columba.core.io.DiskIO;
import org.columba.core.scripting.Python;

public class Config {

	public static File inboxDirectory;
	public static File sentDirectory;
	public static File headerDirectory;
	public static File pop3Directory;
	public static String userDir;

	private static OptionsXmlConfig optionsConfig;

	public static File loggerPropertyFile;

	private static Hashtable pluginList;

	private static File optionsFile;

	/**
	 * @see java.lang.Object#Object()
	 */
	public Config() {

		pluginList = new Hashtable();

		optionsFile = new File(ConfigPath.getConfigDirectory(), "options.xml");

		DefaultConfig.registerPlugin(
			"core",
			optionsFile.getName(),
			new OptionsXmlConfig(optionsFile));

		File file = new File(ConfigPath.getConfigDirectory(), "mail");

		if (file.exists() == false) {
			// convert to new config-schema
			
			Python.runResource(
				"org/columba/modules/mail/config/convert.py",
				ConfigPath.getConfigDirectory().getPath());
			

			/*
			StringBuffer buf = new StringBuffer();
			try {
				InputStream in =
					DiskIO.getResourceStream(
						"org/columba/modules/mail/config/convert.py");
				InputStreamReader in_read = new InputStreamReader(in);

				BufferedReader buf_read = new BufferedReader(in_read);

				String line = null;
				while ((line = buf_read.readLine()) != null)
					buf.append(line);
				buf_read.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
				System.exit(1);
			}

			Python.execute(
				buf.toString(),
				ConfigPath.getConfigDirectory().getPath());
			*/
		}

	}

	/**
	 * Method init.
	 */
	public static void init() {

		File configDirectory = ConfigPath.getConfigDirectory();

		load();

		pop3Directory = new File(configDirectory, "mail/pop3server");
		if (!pop3Directory.exists()) {
			pop3Directory.mkdir();
		}

	}

	/**
	 * Method registerPlugin.
	 * @param moduleName
	 * @param id
	 * @param configPlugin
	 */
	public static void registerPlugin(
		String moduleName,
		String id,
		DefaultXmlConfig configPlugin) {

		if (!pluginList.containsKey(moduleName)) {
			Hashtable table = new Hashtable();
			pluginList.put(moduleName, table);
		}

		addPlugin(moduleName, id, configPlugin);
	}

	/**
	 * Method getPlugin.
	 * @param moduleName
	 * @param id
	 * @return DefaultXmlConfig
	 */
	public static DefaultXmlConfig getPlugin(String moduleName, String id) {

		if (pluginList.containsKey(moduleName)) {
			Hashtable table = (Hashtable) pluginList.get(moduleName);

			if (table.containsKey(id)) {
				DefaultXmlConfig plugin = (DefaultXmlConfig) table.get(id);
				return plugin;
			}
		}

		return null;
	}

	/**
	 * Method addPlugin.
	 * @param moduleName
	 * @param id
	 * @param configPlugin
	 */
	public static void addPlugin(
		String moduleName,
		String id,
		DefaultXmlConfig configPlugin) {
		Hashtable table = (Hashtable) pluginList.get(moduleName);

		if (table != null) {
			table.put(id, configPlugin);
		}
	}

	/**
	 * Method getPluginList.
	 * @return Vector
	 */
	public static Vector getPluginList() {
		Vector v = new Vector();

		for (Enumeration keys = pluginList.keys(); keys.hasMoreElements();) {
			String key = (String) keys.nextElement();
			Hashtable table = (Hashtable) pluginList.get(key);

			if (table != null) {
				for (Enumeration keys2 = table.keys();
					keys2.hasMoreElements();
					) {
					String key2 = (String) keys2.nextElement();
					DefaultXmlConfig plugin =
						(DefaultXmlConfig) table.get(key2);

					v.add(plugin);
				}
			}

		}

		return v;
	}

	/**
	 * Method save.
	 */
	public static void save() {

		Vector v = getPluginList();
		for (int i = 0; i < v.size(); i++) {
			DefaultXmlConfig plugin = (DefaultXmlConfig) v.get(i);
			if (plugin == null)
				continue;

			plugin.save();

		}

	}

	/**
	 * Method load.
	 */
	public static void load() {

		Vector v = getPluginList();
		for (int i = 0; i < v.size(); i++) {
			DefaultXmlConfig plugin = (DefaultXmlConfig) v.get(i);
			if (plugin == null)
				continue;

			plugin.load();

		}

	}

	/**
	 * Method getOptionsConfig.
	 * @return OptionsXmlConfig
	 */
	public static OptionsXmlConfig getOptionsConfig() {

		return (OptionsXmlConfig) DefaultConfig.getPlugin(
			"core",
			optionsFile.getName());
	}

	/**
	 * Method getLoggingPropertyFile.
	 * @return File
	 */
	public static File getLoggingPropertyFile() {
		File configDirectory = ConfigPath.getConfigDirectory();

		loggerPropertyFile = new File(configDirectory, "log4j.properties");

		File loggingDirectory = new File( configDirectory, "log");
		DiskIO.ensureDirectory(loggingDirectory);
		
		if (loggerPropertyFile.exists() == false) {
			String str = LogProperty.createPropertyString( loggingDirectory );
			
			//DefaultConfig.copy("core", "log4j.properties", configDirectory);
			loggerPropertyFile = new File(configDirectory, "log4j.properties");
			
			try
			{
				DiskIO.saveStringInFile(loggerPropertyFile, str );
			}
			catch ( IOException ex )
			{
				ex.printStackTrace();
			}
		}

		return loggerPropertyFile;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3893.java