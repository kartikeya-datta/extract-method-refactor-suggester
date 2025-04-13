error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6067.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6067.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6067.java
text:
```scala
I@@ExtensionRegistry registry = Platform.getExtensionRegistry();

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
package org.eclipse.ui.part;

import org.eclipse.core.runtime.*;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IWorkbenchConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.*;

/**
 * Adapter for adding handling of the <code>PluginTransfer</code> drag and drop
 * transfer type to a drop action.
 * <p>
 * This class may be instantiated or subclassed.
 * </p>
 */
public class PluginDropAdapter extends ViewerDropAdapter  {
	/**
	 * The extension point attribute that defines the drop action class.
	 */
	public static final String ATT_CLASS = "class";//$NON-NLS-1$

	/**
	 * The current transfer data, or <code>null</code> if none.
	 */
	private TransferData currentTransfer;
/** 
 * Creates a plug-in drop adapter for the given viewer.
 *
 * @param viewer the viewer
 */
public PluginDropAdapter(StructuredViewer viewer) {
	super(viewer);
}
/* (non-Javadoc)
 * Method declared on DropTargetAdapter.
 * The user has dropped something on the desktop viewer.
 */
public void drop(DropTargetEvent event) {
	try {
		if (PluginTransfer.getInstance().isSupportedType(event.currentDataType)) {
			PluginTransferData pluginData = (PluginTransferData) event.data;
			IDropActionDelegate delegate = getPluginAdapter(pluginData);
			if (!delegate.run(pluginData.getData(), getCurrentTarget())) {
				event.detail = DND.DROP_NONE;
			}
		} else {
			super.drop(event);
		}
	} catch (CoreException e) {
		WorkbenchPlugin.log("Drop Failed", e.getStatus());//$NON-NLS-1$
	}
}
/**
 * Returns the current transfer.
 */
protected TransferData getCurrentTransfer() {
	return currentTransfer;
}
/**
 * Loads the class that will perform the action associated with the given drop
 * data.
 *
 * @param data the drop data
 * @return the viewer drop adapter
 */
protected static IDropActionDelegate getPluginAdapter(PluginTransferData data) throws CoreException {

	IPluginRegistry registry = Platform.getPluginRegistry();
	String adapterName = data.getExtensionId();
	IExtensionPoint xpt =
		registry.getExtensionPoint(PlatformUI.PLUGIN_ID, IWorkbenchConstants.PL_DROP_ACTIONS);
	IExtension[] extensions = xpt.getExtensions();
	for (int i = 0; i < extensions.length; i++) {
		IConfigurationElement[] configs = extensions[i].getConfigurationElements();
		if (configs != null && configs.length > 0) {
			String id = configs[0].getAttribute("id");//$NON-NLS-1$
			if (id != null && id.equals(adapterName)) {
				return (IDropActionDelegate)WorkbenchPlugin.createExtension(
					configs[0], ATT_CLASS);
			}
		}
	}
	return null;
}
/**
 * @see ViewerDropAdapter#performDrop
 */
public boolean performDrop(Object data) {
	//should never be called, since we override the drop() method.
	return false;
}
/**
 * The <code>PluginDropAdapter</code> implementation of this
 * <code>ViewerDropAdapter</code> method is used to notify the action that some
 * aspect of the drop operation has changed. Subclasses may override.
 */
public boolean validateDrop(Object target, int operation, TransferData transferType) {
	currentTransfer = transferType;
	if (currentTransfer != null && PluginTransfer.getInstance().isSupportedType(currentTransfer)) {
		//plugin cannot be loaded without the plugin data
		return true;
	}
	return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6067.java