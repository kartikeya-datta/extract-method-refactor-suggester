error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8205.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8205.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8205.java
text:
```scala
n@@ew File(configDirectory, "options.xml");

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

package org.columba.addressbook.config;

import java.io.File;

import org.columba.core.config.DefaultConfig;
import org.columba.core.config.DefaultXmlConfig;
import org.columba.core.xml.XmlElement;

/**
 * @version 	1.0
 * @author
 */
public class AddressbookConfig extends DefaultConfig {

	public static final String MODULE_NAME = "addressbook";

	private static File addressbookFile;
	private static File addressbookOptionsFile;
	private static File folderFile;

	/**
	 * @see java.lang.Object#Object()
	 */
	public AddressbookConfig() {

		File configDirectory = createConfigDir(MODULE_NAME);

		addressbookFile = new File(configDirectory, "addressbook.xml");
		registerPlugin(
			addressbookFile.getName(),
			new DefaultXmlConfig(addressbookFile));

		addressbookOptionsFile =
			new File(configDirectory, "addressbookoptions.xml");
		registerPlugin(
			addressbookOptionsFile.getName(),
			new DefaultXmlConfig(addressbookOptionsFile));

		folderFile = new File(configDirectory, "tree.xml");
		registerPlugin(folderFile.getName(), new DefaultXmlConfig(folderFile));

	}
	
	public static XmlElement get(String name) {
			DefaultXmlConfig xml = getPlugin(name + ".xml");
			return xml.getRoot();
		}

	/**
	 * Method registerPlugin.
	 * @param id
	 * @param plugin
	 */
	protected static void registerPlugin(String id, DefaultXmlConfig plugin) {
		DefaultConfig.registerPlugin(MODULE_NAME, id, plugin);
	}

	/**
	 * Method getPlugin.
	 * @param id
	 * @return DefaultXmlConfig
	 */
	protected static DefaultXmlConfig getPlugin(String id) {
		return DefaultConfig.getPlugin(MODULE_NAME, id);
	}

	/**
	 * Method getAddressbookConfig.
	 * @return AddressbookXmlConfig
	 */
	/*
	public static AddressbookXmlConfig getAddressbookConfig() {
		//return addressbookConfig;
		return (AddressbookXmlConfig) getPlugin(addressbookFile.getName());
	}
	*/

	/**
	 * Method getAddressbookOptionsConfig.
	 * @return AddressbookOptionsXmlConfig
	 */
	/*
	public static AddressbookOptionsXmlConfig getAddressbookOptionsConfig() {
		//return addressbookOptionsConfig;
		return (AddressbookOptionsXmlConfig) getPlugin(
			addressbookOptionsFile.getName());
	}
	*/
	/**
	 * Method getTreeConfig.
	 * @return TreeXmlConfig
	 */

	/*
	public static TreeXmlConfig getTreeConfig() {
		//return treeConfig;
		return (TreeXmlConfig) getPlugin(folderFile.getName());
	}
	*/

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8205.java