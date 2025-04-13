error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7933.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7933.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7933.java
text:
```scala
public final static S@@tring ANY_POPUP = "popup:org.eclipse.ui.popup.any"; //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.menus;

/**
 * Provides utilities and constants for use with the
 * new menus API.
 *  
 * @since 3.3
 *
 */
public class MenuUtil {
	/** Main Menu */
	public final static String MAIN_MENU = "menu:org.eclipse.ui.main.menu"; //$NON-NLS-1$
	/** Main ToolBar (CoolBar) */
	public final static String MAIN_TOOLBAR = "toolbar:org.eclipse.ui.main.toolbar"; //$NON-NLS-1$

	/** -Any- Popup Menu */
	public final static String ANY_POPUP = "popup:org.eclipse.ui.any.popup"; //$NON-NLS-1$
	
	/** Top Left Trim Area */
	public final static String TRIM_COMMAND1 = "toolbar:org.eclipse.ui.trim.command1"; //$NON-NLS-1$
	/** Top Right Trim Area */
	public final static String TRIM_COMMAND2 = "toolbar:org.eclipse.ui.trim.command2"; //$NON-NLS-1$
	/** Left Vertical Trim Area */
	public final static String TRIM_VERTICAL1 = "toolbar:org.eclipse.ui.trim.vertical1"; //$NON-NLS-1$
	/** Right Vertical Trim Area */
	public final static String TRIM_VERTICAL2 = "toolbar:org.eclipse.ui.trim.vertical2"; //$NON-NLS-1$
	/** Bottom (Status) Trim Area */
	public final static String TRIM_STATUS = "toolbar:org.eclipse.ui.trim.status"; //$NON-NLS-1$
	
	/**
	 * @param id The menu's id
	 * @return
	 *      The lcoation URI for a menu with the given id 
	 */
	public static String menuUri(String id) {
		return "menu:" + id; //$NON-NLS-1$
	}
	
	/**
	 * @param id The id of the menu
	 * @param location The relative location specifier
	 * @param refId The id of the menu element to be relative to
	 * @return
	 *     A location URI formatted with the given parameters 
	 */
	public static String menuAddition(String id, String location, String refId) {
		return menuUri(id) + '?' + location + '=' + refId;
	}
	
	/**
	 * Convenience method to create a standard menu addition
	 * The resulting string has the format:
	 * "menu:[id]?after=additions"
	 * @param id The id of the root element to contribute to
	 * @return The formatted string
	 */
	public static String menuAddition(String id) {
		return menuAddition(id, "after", "additions");   //$NON-NLS-1$//$NON-NLS-2$
	}
	
	/**
	 * @param id The toolbar's id
	 * @return
	 *      The lcoation URI for a toolbar with the given id 
	 */
	public static String toolbarUri(String id) {
		return "toolbar:" + id; //$NON-NLS-1$
	}
	
	/**
	 * @param id The id of the toolbar
	 * @param location The relative location specifier
	 * @param refId The id of the toolbar element to be relative to
	 * @return
	 *     A location URI formatted with the given parameters 
	 */
	public static String toolbarAddition(String id, String location, String refId) {
		return toolbarUri(id) + '?' + location + '=' + refId;
	}
	
	/**
	 * Convenience method to create a standard toolbar addition
	 * The resulting string has the format:
	 * "toolbar:[id]?after=additions"
	 * @param id The id of the root element to contribute to
	 * @return The formatted string
	 */
	public static String toolbarAddition(String id) {
		return toolbarAddition(id, "after", "additions");   //$NON-NLS-1$//$NON-NLS-2$
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7933.java