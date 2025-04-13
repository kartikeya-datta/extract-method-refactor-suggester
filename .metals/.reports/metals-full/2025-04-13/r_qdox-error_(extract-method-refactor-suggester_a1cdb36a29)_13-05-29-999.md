error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5937.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5937.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5937.java
text:
```scala
S@@ystem.out.println("XSD adapter [" + Thread.currentThread().getName()

/*******************************************************************************
 * Copyright (c) 2005 - 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package org.eclipse.xtend.typesystem.xsd.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtend.shared.ui.MetamodelContributor;
import org.eclipse.xtend.shared.ui.core.metamodel.MetamodelContributorRegistry;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class XSDToolsPlugin extends AbstractUIPlugin {

//	public static class XsdLogger extends SimpleLog {
//
//		private static final long serialVersionUID = 1L;
//
//		public XsdLogger(String name) {
//			super(name);
//			setLevel(LOG_LEVEL_ALL);
//		}
//
//		protected void write(StringBuffer buffer) {
//			traceLog(buffer.toString());
//		}
//
//	}

	// The shared instance
	private static XSDToolsPlugin plugin;

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.xtend.typesystem.xsd.ui";
	public static boolean trace = false;

//	static {
//		String value = Platform.getDebugOption(PLUGIN_ID + "/trace");
//		if (value != null && value.equals("true")) {
//			trace = true;
//		}
//		XSDLog.setLogFactory(new XSDLogFactory() {
//			public Log getLog(Class<?> clazz) {
//				return new XsdLogger(clazz.getName());
//			}
//		});
//	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static XSDToolsPlugin getDefault() {
		if (plugin == null)
			traceLog("Access to XSDToolsPlugin before the plugin has been initialized!");
		return plugin;
	}

	public static boolean isXSDProject(IProject proj) {
		IJavaProject jp = JavaCore.create(proj);
		if (jp == null)
			return false;
		for (MetamodelContributor c : MetamodelContributorRegistry
				.getActiveMetamodelContributors(jp))
			if (c instanceof XSDMetamodelContributor)
				return true;
		return false;
	}

	public static void traceLog(String msg) {
		if (trace)
			System.out.println("oAW-XSD[" + Thread.currentThread().getName()
					+ "]: " + msg);
	}

	private XSDBuilderConfigurator builderConfigurator = new XSDBuilderConfigurator();

	private XSDMetamodelStore store = new XSDMetamodelStore();

	public XSDToolsPlugin() {
		plugin = this;
	}

	public XSDMetamodelStore getXSDStore() {
		return store;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		traceLog(getClass().getName() + " started");
		builderConfigurator = new XSDBuilderConfigurator();
		store = new XSDMetamodelStore();
		builderConfigurator.start();
	}

	public void stop(BundleContext context) throws Exception {
		builderConfigurator.stop();
		plugin = null;
		super.stop(context);
		traceLog(getClass().getName() + " stopped");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5937.java