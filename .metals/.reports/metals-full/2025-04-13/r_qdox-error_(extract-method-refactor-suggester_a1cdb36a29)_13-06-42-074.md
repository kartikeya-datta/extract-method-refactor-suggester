error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4684.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4684.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4684.java
text:
```scala
i@@f (offset >= actionRanges[i][0] && offset < actionRanges[i][0] + actionRanges[i][1]) {

package org.eclipse.ui.internal.dialogs;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.ui.internal.*;
import org.eclipse.help.*;
import org.eclipse.ui.help.*;
import org.eclipse.jface.action.*;
import org.eclipse.core.runtime.*;
import org.eclipse.swt.custom.BusyIndicator;
/**
 * Holds the information for an item appearing in the welcome editor
 */
public class WelcomeItem {
	private String text;
	private int[][] boldRanges;
	private int[][] helpRanges;
	private String[] helpIds;
	private String[] helpHrefs;
	private int[][] actionRanges;
	private String[] actionPluginIds;
	private String[] actionClasses;
/**
 * Creates a new welcome item
 */
public WelcomeItem(
	String text,
	int[][] boldRanges,
	int[][] actionRanges,
	String[] actionPluginIds,
	String[] actionClasses,
	int[][] helpRanges,
	String[] helpIds,
	String[] helpHrefs) {
	    
	this.text = text;
	this.boldRanges = boldRanges;
	this.actionRanges = actionRanges;
	this.actionPluginIds = actionPluginIds;
	this.actionClasses = actionClasses;
	this.helpRanges = helpRanges;
	this.helpIds = helpIds;
	this.helpHrefs = helpHrefs;
}
/**
 * Returns the action ranges (character locations)
 */
public int[][] getActionRanges() {
	return actionRanges;
}
/**
 * Returns the bold ranges (character locations)
 */
public int[][] getBoldRanges() {
	return boldRanges;
}
/**
 * Returns the help ranges (character locations)
 */
public int[][] getHelpRanges() {
	return helpRanges;
}
/**
 * Returns the text to display
 */
public String getText() {
	return text;
}
/**
 * Returns true is a link (action or help) is present at the given character location
 */
public boolean isLinkAt(int offset) {
	// Check if there is a link at the offset
	for (int i = 0; i < helpRanges.length; i++){
		if (offset >= helpRanges[i][0] && offset < helpRanges[i][0] + helpRanges[i][1]) {
			return true;
		}
	}

	// Check if there is an action link at the offset
	for (int i = 0; i < actionRanges.length; i++){
		if (offset >= actionRanges[i][0] && offset < actionRanges[i][0] + actionRanges[i][1]) {
			return true;
		}
	}
	return false;
}
/**
 * Logs a error to the workbench log
 */
public void logActionLinkError(String actionPluginId, String actionClass) {
	WorkbenchPlugin.log(WorkbenchMessages.getString("WelcomeItem.unableToLoadClass") + actionPluginId + " " + actionClass); //$NON-NLS-1$	//$NON-NLS-2$
}
/**
 * Open a help topic
 */
private void openHelpTopic(String topic, String href) {
	IHelp helpSupport = WorkbenchHelp.getHelpSupport();
	if (helpSupport != null) {
		if (href != null) 
			helpSupport.displayHelpResource(href);
		else
			helpSupport.displayHelpResource(topic);
	}
}
/**
 * Run an action
 */
private void runAction(String pluginId, String className) {
	IPluginDescriptor desc = Platform.getPluginRegistry().getPluginDescriptor(pluginId);
	if (desc == null) {
		logActionLinkError(pluginId, className);
		return;
	}		
	Class actionClass;
	IAction action;
	try {
		actionClass = desc.getPluginClassLoader().loadClass(className);
	} catch (ClassNotFoundException e) {
		logActionLinkError(pluginId, className);
		return;
	}
	try {
		action = (IAction)actionClass.newInstance();
	} catch (InstantiationException e) {
		logActionLinkError(pluginId, className);
		return;
	} catch (IllegalAccessException e) {
		logActionLinkError(pluginId, className);
		return;
	} catch (ClassCastException e) {
		logActionLinkError(pluginId, className);
		return;
	}
	action.run();
}
/**
 * Triggers the link at the given offset (if there is one)
 */
public void triggerLinkAt(int offset) {
	// Check if there is a help link at the offset
	for (int i = 0; i < helpRanges.length; i++){
		if (offset >= helpRanges[i][0] && offset < helpRanges[i][0] + helpRanges[i][1]) {
			// trigger the link
			openHelpTopic(helpIds[i], helpHrefs[i]);
			return;
		}
	}

	// Check if there is an action link at the offset
	for (int i = 0; i < actionRanges.length; i++){
		if (offset > actionRanges[i][0] && offset <= actionRanges[i][0] + actionRanges[i][1]) {
			// trigger the link
			runAction(actionPluginIds[i], actionClasses[i]);
			return;
		}
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4684.java