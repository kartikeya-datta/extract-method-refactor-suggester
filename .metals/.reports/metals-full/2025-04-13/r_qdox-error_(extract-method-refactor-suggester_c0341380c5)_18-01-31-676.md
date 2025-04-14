error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4130.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4130.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4130.java
text:
```scala
public b@@oolean readElement(IConfigurationElement element) {

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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.dialogs.WizardCollectionElement;
import org.eclipse.ui.internal.dialogs.WorkbenchWizardElement;
import org.eclipse.ui.model.AdaptableList;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 *  Instances access the registry that is provided at creation time
 *  in order to determine the contained Wizards
 */
public class WizardsRegistryReader extends RegistryReader {
	private AdaptableList wizards;
	private String pluginPoint;

	protected final static String TAG_WIZARD = "wizard";//$NON-NLS-1$

	protected final static String ATT_NAME = "name";//$NON-NLS-1$
	// @issue we should have an IExtensionConstants class with all these attribute names, element names, attribute values (like true, false, etc).
	public final static String ATT_CLASS = "class";//$NON-NLS-1$
	protected final static String ATT_ICON = "icon";//$NON-NLS-1$
	protected final static String ATT_ID = "id";//$NON-NLS-1$
	protected final static String trueString = "TRUE";//$NON-NLS-1$
/**
 *	Create an instance of this class.
 *
 *	@param pluginPointId java.lang.String
 */
public WizardsRegistryReader(String pluginPointId) {
	pluginPoint = pluginPointId;
}
/**
 * Adds new wizard to the provided collection. Override to
 * provide more logic.
 */
protected void addNewElementToResult(WorkbenchWizardElement wizard, IConfigurationElement config) {
	wizards.add(wizard);
}
/**
 * Creates empty element collection. Overrider to fill
 * initial elements, if needed.
 */
protected void createEmptyWizardCollection() {
	wizards = new AdaptableList();
}
/**
 * Returns a new WorkbenchWizardElement configured according to the parameters
 * contained in the passed Registry.  
 *
 * May answer null if there was not enough information in the Extension to create 
 * an adequate wizard
 */
protected WorkbenchWizardElement createWizardElement(IConfigurationElement element) {
	// WizardElements must have a name attribute
	String nameString = element.getAttribute(ATT_NAME);
	if (nameString == null) {
		logMissingAttribute(element, ATT_NAME);
		return null;
	}
	WorkbenchWizardElement result = new WorkbenchWizardElement(nameString);
	if (initializeWizard(result, element))
		return result;	// ie.- initialization was successful

	return null;
}
/**
 *	Returns the first wizard with a given id.
 */
public WorkbenchWizardElement findWizard(String id) {
	Object [] wizards = getWizardCollectionElements();
	for (int nX = 0; nX < wizards.length; nX ++) {
		WizardCollectionElement collection = (WizardCollectionElement)wizards[nX];
		WorkbenchWizardElement element = collection.findWizard(id,true);
		if (element != null)
			return element;
	}
	return null;
}
/**
 * Returns a list of wizards, project and not.
 *
 * The return value for this method is cached since computing its value
 * requires non-trivial work.  
 */
public AdaptableList getWizards() {
	if (!areWizardsRead()) {
		readWizards();
	}
	return wizards;
}
/**
 *	Initialize the passed element's properties based on the contents of
 *	the passed registry.  Answer a boolean indicating whether the element
 *	was able to be adequately initialized.
 *
 *	@return boolean
 *	@param element WorkbenchWizardElement
 *	@param extension Extension
 */
protected boolean initializeWizard(WorkbenchWizardElement element, IConfigurationElement config) {
	element.setID(config.getAttribute(ATT_ID));
	element.setDescription(getDescription(config));

	// apply CLASS and ICON properties	
	element.setConfigurationElement(config);
	String iconName = config.getAttribute(ATT_ICON);
	if (iconName != null) {
		IExtension extension = config.getDeclaringExtension();
		String extendingPluginId =
			extension.getDeclaringPluginDescriptor().getUniqueIdentifier();
		ImageDescriptor image =
			AbstractUIPlugin.imageDescriptorFromPlugin(extendingPluginId, iconName);
		element.setImageDescriptor(image);
	}
	// ensure that a class was specified
	if (element.getConfigurationElement() == null) {
		logMissingAttribute(config, ATT_CLASS);
		return false;
	}
	return true;	
}
/**
 * Implement this method to read element attributes.
 */
protected boolean readElement(IConfigurationElement element) {
	if (!element.getName().equals(TAG_WIZARD))
		return false;
	WorkbenchWizardElement wizard = createWizardElement(element);
	if (wizard != null)
	   addNewElementToResult(wizard, element);
	return true;
}
/**
 * Reads the wizards in a registry.  
 */
protected void readWizards() {
	if (!areWizardsRead()) {
		createEmptyWizardCollection();
		IPluginRegistry pregistry = Platform.getPluginRegistry();
		readRegistry(pregistry, WorkbenchPlugin.PI_WORKBENCH, pluginPoint);
	}
}
/**
 * Returns whether the wizards have been read already
 */
protected boolean areWizardsRead() {
	return wizards != null;
}
protected Object[] getWizardCollectionElements() {
	if (!areWizardsRead()) {
		readWizards();
	}
	return wizards.getChildren();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4130.java