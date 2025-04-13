error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9680.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9680.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9680.java
text:
```scala
r@@esource = ResourceBundle.getBundle(location + ".options", loc); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2000, 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.internal.compiler;

/**
 * Generic option description, which can be modified independently from the
 * component it belongs to.
 * 
 * @deprecated backport 1.0 internal functionality
 */

import java.util.*;

public class ConfigurableOption {
	private String componentName;
	private String optionName;
	private int id;

	private String category;
	private String name;
	private String description;
	private int currentValueIndex;
	private int defaultValueIndex;
	private String[] possibleValues;

	// special value for <possibleValues> indicating that 
	// the <currentValueIndex> is the actual value
	public final static String[] NoDiscreteValue = {}; 
/**
 * INTERNAL USE ONLY
 *
 * Initialize an instance of this class according to a specific locale
 *
 * @param loc java.util.Locale
 */
public ConfigurableOption(
	String componentName, 
	String optionName, 
	Locale loc, 
	int currentValueIndex) {

	this.componentName = componentName;
	this.optionName = optionName;
	this.currentValueIndex = currentValueIndex;
		
	ResourceBundle resource = null;
	try {
		String location = componentName.substring(0, componentName.lastIndexOf('.'));
		resource = ResourceBundle.getBundle(location + ".Options", loc); //$NON-NLS-1$
	} catch (MissingResourceException e) {
		category = "Missing ressources entries for" + componentName + " options"; //$NON-NLS-1$ //$NON-NLS-2$
		name = "Missing ressources entries for"+ componentName + " options"; //$NON-NLS-1$ //$NON-NLS-2$
		description = "Missing ressources entries for" + componentName + " options"; //$NON-NLS-1$ //$NON-NLS-2$
		possibleValues = new String[0];
		id = -1;
	}
	if (resource == null) return;
	try {
		id = Integer.parseInt(resource.getString(optionName + ".number")); //$NON-NLS-1$
	} catch (MissingResourceException e) {
		id = -1;
	} catch (NumberFormatException e) {
		id = -1;
	}
	try {
		category = resource.getString(optionName + ".category"); //$NON-NLS-1$
	} catch (MissingResourceException e) {
		category = "Missing ressources entries for" + componentName + " options"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	try {
		name = resource.getString(optionName + ".name"); //$NON-NLS-1$
	} catch (MissingResourceException e) {
		name = "Missing ressources entries for"+ componentName + " options"; //$NON-NLS-1$ //$NON-NLS-2$
	}
	try {
		StringTokenizer tokenizer = new StringTokenizer(resource.getString(optionName + ".possibleValues"), "|"); //$NON-NLS-1$ //$NON-NLS-2$
		int numberOfValues = Integer.parseInt(tokenizer.nextToken());
		if(numberOfValues == -1){
			possibleValues = NoDiscreteValue;
		} else {
			possibleValues = new String[numberOfValues];
			int index = 0;
			while (tokenizer.hasMoreTokens()) {
				possibleValues[index] = tokenizer.nextToken();
				index++;
			}
		}
	} catch (MissingResourceException e) {
		possibleValues = new String[0];
	} catch (NoSuchElementException e) {
		possibleValues = new String[0];
	} catch (NumberFormatException e) {
		possibleValues = new String[0];
	}
	try {
		description = resource.getString(optionName + ".description");  //$NON-NLS-1$
	} catch (MissingResourceException e) {
		description = "Missing ressources entries for"+ componentName + " options"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
/**
 * Return a String that represents the localized category of the receiver.
 * @return java.lang.String
 */
public String getCategory() {
	return category;
}
/**
 * Return a String that identifies the component owner (typically the qualified
 *	type name of the class which it corresponds to).
 *
 * e.g. "org.eclipse.jdt.internal.compiler.api.Compiler"
 *
 * @return java.lang.String
 */
public String getComponentName() {
	return componentName;
}
/**
 * Answer the index (in possibleValues array) of the current setting for this
 * particular option.
 *
 * In case the set of possibleValues is NoDiscreteValue, then this index is the
 * actual value (e.g. max line lenght set to 80).
 *
 * @return int
 */
public int getCurrentValueIndex() {
	return currentValueIndex;
}
/**
 * Answer the index (in possibleValues array) of the default setting for this
 * particular option.
 *
 * In case the set of possibleValues is NoDiscreteValue, then this index is the
 * actual value (e.g. max line lenght set to 80).
 *
 * @return int
 */
public int getDefaultValueIndex() {
	return defaultValueIndex;
}
/**
 * Return an String that represents the localized description of the receiver.
 *
 * @return java.lang.String
 */
public String getDescription() {
	return description;
}
/**
 * Internal ID which allows the configurable component to identify this particular option.
 *
 * @return int
 */
public int getID() {
	return id;
}
/**
 * Return a String that represents the localized name of the receiver.
 * @return java.lang.String
 */
public String getName() {
	return name;
}
/**
 * Return an array of String that represents the localized possible values of the receiver.
 * @return java.lang.String[]
 */
public String[] getPossibleValues() {
	return possibleValues;
}
/**
 * Change the index (in possibleValues array) of the current setting for this
 * particular option.
 *
 * In case the set of possibleValues is NoDiscreteValue, then this index is the
 * actual value (e.g. max line lenght set to 80).
 *
 * @return int
 */
public void setValueIndex(int newIndex) {
	currentValueIndex = newIndex;
}
public String toString() {
	StringBuffer buffer = new StringBuffer();
	buffer.append("Configurable option for "); //$NON-NLS-1$ 
	buffer.append(this.componentName).append("\n"); //$NON-NLS-1$ 
	buffer.append("- category:			").append(this.category).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
	buffer.append("- name:				").append(this.name).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
	/* display current value */
	buffer.append("- current value:	"); //$NON-NLS-1$ 
	if (possibleValues == NoDiscreteValue){
		buffer.append(this.currentValueIndex);
	} else {
		buffer.append(this.possibleValues[this.currentValueIndex]);
	}
	buffer.append("\n"); //$NON-NLS-1$ 
	
	/* display possible values */
	if (possibleValues != NoDiscreteValue){
		buffer.append("- possible values:	["); //$NON-NLS-1$ 
		for (int i = 0, max = possibleValues.length; i < max; i++) {
			if (i != 0)
				buffer.append(", "); //$NON-NLS-1$ 
			buffer.append(possibleValues[i]);
		}
		buffer.append("]\n"); //$NON-NLS-1$ 
		buffer.append("- curr. val. index:	").append(currentValueIndex).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	buffer.append("- description:		").append(description).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
	return buffer.toString();
}
	/**
	 * Gets the optionName.
	 * @return Returns a String
	 */
	public String getOptionName() {
		return optionName;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9680.java