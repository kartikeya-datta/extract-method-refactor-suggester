error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13905.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13905.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13905.java
text:
```scala
private static final S@@tring PLUGIN_ID = "ID: org.eclipse.m2t.common.recipe";

/*******************************************************************************
 * Copyright (c) 2005, 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

package org.eclipse.m2t.common.recipe.ui;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.m2t.common.recipe.ui.shared.iface.ITraceLog;
import org.eclipse.m2t.common.recipe.ui.shared.messages.DialogMessages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
/**
 * 
 * This class manages the initialization for a generator plugin. If the plugin
 * is run in debug mode (see plugin.properties) then a default logger is
 * initialized with the settings read from the plugin.properties. It also
 * manages the reading of images.
 *  
 */
public class RecipePlugin extends AbstractUIPlugin implements ITraceLog {
    
    
    private static final String PLUGIN_ID = "ID: org.openarchitectureware.recipe";
	/**
	 * The shared instance
	 */
	private static RecipePlugin cvGeneratorPlugin;
	
	/**
	 * Creates new Generator plugin. Reads logging information from descriptor
	 * and adds a LogListener if needed
	 * 
	 * @see Plugin.properties
	 */
	public RecipePlugin() {
	    super();
		cvGeneratorPlugin = this;
	}
	/**
	 * Returns the shared instance of GeneratorPlugin
	 */
	public static RecipePlugin getDefault() {
	    if (cvGeneratorPlugin==null) {
	        IStatus status = new Status(Status.ERROR,getPluginId(),ITraceLog.CRITICAL , "singleton not initialized",new NullPointerException());
	        Platform.getLog(Platform.getBundle(getPluginId())).log(status);
	    }
		return cvGeneratorPlugin;
	}
	/**
	 * Returns an ImageDescriptor for a given name
	 * 
	 * @param aName
	 *            a name of an image
	 */
	public ImageDescriptor getImageDescriptor(String aName) {
		ImageDescriptor imageDesc = null;
		try {
			URL url = new URL(getBundle().getEntry("/icons/"), aName);
			imageDesc = ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException e) {
			imageDesc = ImageDescriptor.getMissingImageDescriptor();
		}
		return imageDesc;
	}
	/**
	 * Returns the unique id associated with the plugin
	 */
	public static String getPluginId() {
		return PLUGIN_ID;
	}
	/**
	 * Returns a message for a given key
	 * 
	 * @param aKey
	 *            a key associated with a message
	 */
	public static String getMessage(String aKey) {
		return DialogMessages.getMessage(aKey);
	}
	/**
	 * Logs a status
	 * 
	 * @param aStatus
	 *            a status to be logged
	 * @see Plugin.properties
	 */
	public static void log(IStatus aStatus) {
		getDefault().getLog().log(aStatus);
	}
	
	/**
	 * Logs a message
	 * 
	 * @param aMessage
	 *            a message to be logged
	 * @see Plugin.properties
	 */
	public void log(String aMessage) {
		log(new Status(IStatus.INFO, getPluginId(), INFO, aMessage, null));
	}
	/**
	 * Logs a message for a given level
	 * 
	 * @param aLevel
	 *            a log level for a message
	 * @param aMessage
	 *            a message to be logged
	 * @see Plugin.properties
	 */
	public void log(int aLevel, String aMessage) {
		log(new Status(IStatus.INFO, getPluginId(), aLevel, aMessage, null));
	}
	/**
	 * Logs an Exception
	 * 
	 * @param anException
	 *            an exception to be logged
	 * @see Plugin.properties
	 */
	public static void log(Exception anException) {
		log(new Status(IStatus.ERROR, getPluginId(), EXCEPTION, "EXCEPTION",
				anException));
	}
	
	
	public IWorkbenchPage getActivePage() {
		IWorkbenchWindow window= getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return null;
		return getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}
	
	public void start(BundleContext context) throws Exception {
		super.start(context);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13905.java