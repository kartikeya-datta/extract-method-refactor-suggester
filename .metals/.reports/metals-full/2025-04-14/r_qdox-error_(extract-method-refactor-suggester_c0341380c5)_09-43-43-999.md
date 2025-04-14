error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4709.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4709.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4709.java
text:
```scala
public v@@oid readRegistry(IPluginRegistry registry, String pluginId, String extensionPoint) {

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.registry;

import java.util.Hashtable;

import org.eclipse.core.runtime.*;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.misc.Sorter;

/**
 *	Template implementation of a registry reader that creates objects
 *	representing registry contents. Typically, an extension
 * contains one element, but this reader handles multiple
 * elements per extension.
 *
 * To start reading the extensions from the registry for an
 * extension point, call the method <code>readRegistry</code>.
 *
 * To read children of an IConfigurationElement, call the
 * method <code>readElementChildren</code> from your implementation
 * of the method <code>readElement</code>, as it will not be
 * done by default.
 */
public abstract class RegistryReader {
	protected static final String TAG_DESCRIPTION = "description";	//$NON-NLS-1$
	protected static Hashtable extensionPoints = new Hashtable();
/**
 * The constructor.
 */
protected RegistryReader() {
}
/**
 * This method extracts description as a subelement of
 * the given element.
 * @return description string if defined, or empty string
 * if not.
 */
protected String getDescription(IConfigurationElement config) {
	IConfigurationElement [] children = config.getChildren(TAG_DESCRIPTION);
	if (children.length>=1) {
		return children[0].getValue();
	}
	return "";//$NON-NLS-1$
}
/**
 * Logs the error in the workbench log using the provided
 * text and the information in the configuration element.
 */
protected void logError(IConfigurationElement element, String text) {
	IExtension extension = element.getDeclaringExtension();
	IPluginDescriptor descriptor = extension.getDeclaringPluginDescriptor();
	StringBuffer buf = new StringBuffer();
	buf.append("Plugin " + descriptor.getUniqueIdentifier() + ", extension " + extension.getExtensionPointUniqueIdentifier());//$NON-NLS-2$//$NON-NLS-1$
	buf.append("\n"+text);//$NON-NLS-1$
	WorkbenchPlugin.log(buf.toString());
}
/**
 * Logs a very common registry error when a required attribute is missing.
 */
protected void logMissingAttribute(IConfigurationElement element, String attributeName) {
	logError(element, "Required attribute '"+attributeName+"' not defined");//$NON-NLS-2$//$NON-NLS-1$
}

/**
 * Logs a very common registry error when a required child is missing.
 */
protected void logMissingElement(IConfigurationElement element, String elementName) {
	logError(element, "Required sub element '"+elementName+"' not defined");//$NON-NLS-2$//$NON-NLS-1$
}

/**
 * Logs a registry error when the configuration element is unknown.
 */
protected void logUnknownElement(IConfigurationElement element) {
	logError(element, "Unknown extension tag found: " + element.getName());//$NON-NLS-1$
}
/**
 *	Apply a reproducable order to the list of extensions
 * provided, such that the order will not change as
 * extensions are added or removed.
 */
protected IExtension[] orderExtensions(IExtension[] extensions) {
	// By default, the order is based on plugin id sorted
	// in ascending order. The order for a plugin providing
	// more than one extension for an extension point is
	// dependent in the order listed in the XML file.
	Sorter sorter = new Sorter() {
		public boolean compare(Object extension1, Object extension2) {
			String s1 = ((IExtension)extension1).getDeclaringPluginDescriptor().getUniqueIdentifier();
			String s2 = ((IExtension)extension2).getDeclaringPluginDescriptor().getUniqueIdentifier();
			//Return true if elementTwo is 'greater than' elementOne
			return s2.compareToIgnoreCase(s1) > 0;
		}
	};

	Object[] sorted = sorter.sort(extensions);
	IExtension[] sortedExtension = new IExtension[sorted.length];
	System.arraycopy(sorted, 0, sortedExtension, 0, sorted.length);
	return sortedExtension;
}
/**
 * Implement this method to read element's attributes.
 * If children should also be read, then implementor
 * is responsible for calling <code>readElementChildren</code>.
 * Implementor is also responsible for logging missing 
 * attributes.
 *
 * @return true if element was recognized, false if not.
 */
protected abstract boolean readElement(IConfigurationElement element);
/**
 * Read the element's children. This is called by
 * the subclass' readElement method when it wants
 * to read the children of the element.
 */
protected void readElementChildren(IConfigurationElement element) {
	readElements(element.getChildren());
}
/**
 * Read each element one at a time by calling the
 * subclass implementation of <code>readElement</code>.
 *
 * Logs an error if the element was not recognized.
 */
protected void readElements(IConfigurationElement[] elements) {
	for (int i = 0; i < elements.length; i++) {
		if (!readElement(elements[i]))
			logUnknownElement(elements[i]);
	}
}
/**
 * Read one extension by looping through its
 * configuration elements.
 */
protected void readExtension(IExtension extension) {
	readElements(extension.getConfigurationElements());
}
/**
 *	Start the registry reading process using the
 * supplied plugin ID and extension point.
 */
protected void readRegistry(IPluginRegistry registry, String pluginId, String extensionPoint) {
	String pointId = pluginId + "-" + extensionPoint; //$NON-NLS-1$
	IExtension[] extensions = (IExtension[])extensionPoints.get(pointId); 
	if (extensions == null) {
		IExtensionPoint point = registry.getExtensionPoint(pluginId, extensionPoint);
		if (point == null) return;
		extensions = point.getExtensions();
		extensions = orderExtensions(extensions);
		extensionPoints.put(pointId, extensions);
	} 
	for (int i = 0; i < extensions.length; i++)
		readExtension(extensions[i]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4709.java