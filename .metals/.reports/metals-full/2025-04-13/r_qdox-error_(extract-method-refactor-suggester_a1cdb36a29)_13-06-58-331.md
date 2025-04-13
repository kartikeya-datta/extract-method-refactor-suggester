error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5716.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5716.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5716.java
text:
```scala
i@@f (className == null || configElement == null)

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


import org.eclipse.core.runtime.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.*;
import org.eclipse.ui.internal.*;

/**
 * PerspectiveDescriptor.
 * <p>
 * A PerspectiveDesciptor has 3 states:
 * </p>
 * <ol>
 * <li>It <code>isPredefined()</code>, in which case it was defined from an
 * extension point.</li>
 * <li>It <code>isPredefined()</code> and <code>hasCustomFile</code>, in which 
 * case the user has customized a predefined perspective.</li>
 * <li>It <code>hasCustomFile</code>, in which case the user created a
 * new perspective.</li>
 * </ol>
 */
public class PerspectiveDescriptor implements IPerspectiveDescriptor {
	private String id;
	private String originalId;
	private String label;
	private String className;
	private String description;
	private boolean singleton;
	private ImageDescriptor image;
	private IConfigurationElement configElement;
	
	private static final String ATT_ID="id";//$NON-NLS-1$
	private static final String ATT_DEFAULT = "default";//$NON-NLS-1$
	private static final String ATT_DESC="description";//$NON-NLS-1$
	private static final String ATT_NAME="name";//$NON-NLS-1$
	private static final String ATT_ICON="icon";//$NON-NLS-1$
	private static final String ATT_CLASS="class";//$NON-NLS-1$
	private static final String ATT_SINGLETON="singleton";//$NON-NLS-1$
	
/**
 * Create a new empty descriptor.
 */
public PerspectiveDescriptor(String id, String label,PerspectiveDescriptor originalDescriptor) {
	super();
	this.id = id;
	this.label = label;
	if(originalDescriptor != null) {
		this.originalId = originalDescriptor.getOriginalId();
		this.image = originalDescriptor.image;
	}
}
/**
 * Create a descriptor from a config element.
 */
public PerspectiveDescriptor(IConfigurationElement configElement)
	throws CoreException
{
	super();
	this.configElement = configElement;
	id = configElement.getAttribute(ATT_ID);
	label = configElement.getAttribute(ATT_NAME);
	className = configElement.getAttribute(ATT_CLASS);
	singleton = (configElement.getAttributeAsIs(ATT_SINGLETON) != null);
	description = configElement.getAttribute(ATT_DESC);

	// Sanity check.
	if ((label == null) || (className == null)) {
		throw new CoreException(new Status(IStatus.ERROR,
			WorkbenchPlugin.PI_WORKBENCH, 0,
			"Invalid extension (missing label or class name): " + id,//$NON-NLS-1$
			null));
	}

	// Load icon.
	String icon = configElement.getAttribute(ATT_ICON);
	if (icon != null) {
		image = WorkbenchImages.getImageDescriptorFromExtension(
			configElement.getDeclaringExtension(), icon);
	}
}
/**
 * Creates a factory for a predefined perspective.  If the perspective
 * is not predefined return null.
 *
 * @throws a CoreException if the object could not be instantiated.
 */
public IPerspectiveFactory createFactory() throws CoreException {
	if (className == null)
		return null;
	Object obj = WorkbenchPlugin.createExtension(configElement, ATT_CLASS);
	return (IPerspectiveFactory) obj;
}
/**
 * Deletes the custom definition for a perspective..
 */
public void deleteCustomDefinition() {
	((PerspectiveRegistry)WorkbenchPlugin.getDefault().
		getPerspectiveRegistry()).deleteCustomDefinition(this);
}
/**
 * Returns this perspective's description. 
 * This is the value of its <code>"description"</code> attribute.
 *
 * @return the description
 */
public String getDescription() {
	return description;
}
/**
 * Returns the ID.
 */
public String getId() {
	return id;
}
/**
 * Returns the descriptor of the image for this perspective.
 *
 * @return the descriptor of the image to display next to this perspective
 */
public ImageDescriptor getImageDescriptor() {
	return image;
}
/**
 * Returns the label.
 */
public String getLabel() {
	return label;
}
public String getOriginalId() {
	if(originalId == null)
		return id;
	return originalId;
}
/**
 * Returns true if this perspective has a custom file.
 */
public boolean hasCustomDefinition() {
	return ((PerspectiveRegistry)WorkbenchPlugin.getDefault().
		getPerspectiveRegistry()).hasCustomDefinition(this);
}
/**
 * Returns true if this perspective wants to be default.
 */
public boolean hasDefaultFlag() {
	if (configElement == null)
		return false;
	String str = configElement.getAttribute(ATT_DEFAULT);
	if (str == null)
		return false;
	return str.equals("true");//$NON-NLS-1$
}
/**
 * Returns true if this perspective is predefined by an extension.
 */
public boolean isPredefined() {
	return (className != null);
}
/**
 * Returns true if this perspective is a singleton.
 */
public boolean isSingleton() {
	return singleton;
}
/**
 * @see IPersistable
 */
public IStatus restoreState(IMemento memento) {
	IMemento childMem = memento.getChild(IWorkbenchConstants.TAG_DESCRIPTOR);
	if(childMem != null) {
		id = childMem.getString(IWorkbenchConstants.TAG_ID);
		originalId = childMem.getString(IWorkbenchConstants.TAG_DESCRIPTOR);
		label = childMem.getString(IWorkbenchConstants.TAG_LABEL);
		className = childMem.getString(IWorkbenchConstants.TAG_CLASS);
		singleton = (childMem.getInteger(IWorkbenchConstants.TAG_SINGLETON) != null);
	
		//Find a descriptor in the registry.
		PerspectiveDescriptor descriptor = (PerspectiveDescriptor)WorkbenchPlugin.getDefault().
			getPerspectiveRegistry().findPerspectiveWithId(getOriginalId());
	
		if(descriptor != null)
			//Copy the state from the registred descriptor.	
			image = descriptor.image;
	}
	return new Status(IStatus.OK,PlatformUI.PLUGIN_ID,0,"",null);		 //$NON-NLS-1$
}
/**
 * Revert to the predefined extension template.
 * Does nothing if this descriptor is user defined.
 */
public void revertToPredefined() {
	if (isPredefined())
		deleteCustomDefinition();
}
/**
 * @see IPersistable
 */
public IStatus saveState(IMemento memento) {
	IMemento childMem = memento.createChild(IWorkbenchConstants.TAG_DESCRIPTOR);
	childMem.putString(IWorkbenchConstants.TAG_ID,id);
	if(originalId != null)
		childMem.putString(IWorkbenchConstants.TAG_DESCRIPTOR,originalId);
	childMem.putString(IWorkbenchConstants.TAG_LABEL,label);
	childMem.putString(IWorkbenchConstants.TAG_CLASS,className);
	if (singleton)
		childMem.putInteger(IWorkbenchConstants.TAG_SINGLETON, 1);
	return new Status(IStatus.OK,PlatformUI.PLUGIN_ID,0,"",null); //$NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5716.java