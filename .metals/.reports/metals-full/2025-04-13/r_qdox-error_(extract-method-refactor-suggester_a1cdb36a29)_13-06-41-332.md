error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1329.java
text:
```scala
v@@oid moveTo(AbstractFolder child);

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
package org.columba.mail.folder;

import javax.swing.tree.TreePath;

import org.columba.mail.config.FolderItem;
import org.columba.mail.folder.event.FolderListener;

/**
 * @author fdietz
 *
 */
public interface IFolder {
	/**
	 * Adds a listener.
	 */
	void addFolderListener(FolderListener l);

	/**
	 * Removes a previously registered listener.
	 */
	void removeFolderListener(FolderListener l);

	/**
	 * Method getSelectionTreePath.
	 * 
	 * @return TreePath
	 */
	TreePath getSelectionTreePath();

	/**
	 * Returns the folder's UID.
	 */
	int getUid();

	/**
	 * Returns the folder's configuration.
	 */
	FolderItem getConfiguration();

	/**
	 * Sets the folder's configuration.
	 */
	void setConfiguration(FolderItem node);

	/**
	 * Returns the folder's name.
	 */
	String getName();

	/**
	 * Sets the folder's name. This method notifies registered FolderListeners.
	 */
	void setName(String newName) throws Exception;

	/*
	 public FolderCommandReference getCommandReference(FolderCommandReference r) {
	 return r;
	 }
	 */boolean tryToGetLock(Object locker);

	void releaseLock(Object locker);

	/**
	 * ************************** treenode management
	 * ******************************
	 */
	void insert(AbstractFolder newFolder, int newIndex);

	/**
	 * Removes this folder from its parent. This method will notify registered
	 * FolderListeners.
	 */
	void removeFolder() throws Exception;

	/**
	 * Adds a child folder to this folder. This method will notify registered
	 * FolderListeners.
	 */
	void addSubfolder(AbstractFolder child) throws Exception;

	/**
	 * 
	 * AbstractFolder wraps XmlElement
	 * 
	 * all treenode manipulation is passed to the corresponding XmlElement
	 */
	void append(AbstractFolder child);

	/** ******************* capabilities ************************************* */
	boolean supportsAddMessage();

	/**
	 * Returns true if this folder can have sub folders of the specified type;
	 * false otherwise.
	 * 
	 * @param newFolder
	 *            the folder that is going to be inserted as a child.
	 * @return true if this folder can have sub folders; false otherwise.
	 */
	boolean supportsAddFolder(AbstractFolder newFolder);

	/**
	 * Returns true if this folder type can be moved around in the folder tree.
	 * 
	 * @return true if this folder type can be moved around in the folder tree.
	 */
	boolean supportsMove();

	/**
	 * Return the root folder of this folder.
	 * <p>
	 * This is especially useful when using IMAP. IMAP has a root folder which
	 * is labelled with the account name.
	 * 
	 * @return root parent folder of this folder
	 */
	AbstractFolder getRootFolder();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1329.java