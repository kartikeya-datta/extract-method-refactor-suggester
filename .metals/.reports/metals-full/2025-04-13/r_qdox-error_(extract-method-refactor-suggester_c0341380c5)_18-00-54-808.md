error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4555.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4555.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4555.java
text:
```scala
f@@or (int j = 0; j < serviceNames.length; j++) {

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

package org.eclipse.ui.internal.services;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.services.AbstractServiceFactory;
import org.eclipse.ui.services.IServiceLocator;
import org.eclipse.ui.statushandlers.StatusManager;

/**
 * This class will create a service from the matching factory. If the factory
 * doesn't exist, it will try and load it from the registry.
 * 
 * @since 3.4
 */
public class WorkbenchServiceRegistry {
	private static final String EXT_ID_SERVICES = "org.eclipse.ui.services"; //$NON-NLS-1$

	private static WorkbenchServiceRegistry registry = null;

	public static WorkbenchServiceRegistry getRegistry() {
		if (registry == null) {
			registry = new WorkbenchServiceRegistry();
		}
		return registry;
	}

	/**
	 * Used as the global service locator's parent.
	 */
	public static final IServiceLocator GLOBAL_PARENT = new IServiceLocator() {
		public Object getService(Class api) {
			return null;
		}

		public boolean hasService(Class api) {
			return false;
		}
	};

	private Map factories = new HashMap();

	public Object getService(Class key, IServiceLocator parentLocator,
			IServiceLocator locator) {
		Object f = factories.get(key.getName());
		if (f instanceof AbstractServiceFactory) {
			AbstractServiceFactory factory = (AbstractServiceFactory) f;
			return factory.create(key, parentLocator, locator);
		}
		return loadFromRegistry(key, parentLocator, locator);
	}

	private Object loadFromRegistry(Class key, IServiceLocator parentLocator,
			IServiceLocator locator) {
		Object service = null;
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IExtensionPoint ep = reg.getExtensionPoint(EXT_ID_SERVICES);
		IConfigurationElement[] serviceFactories = ep
				.getConfigurationElements();
		try {
			final String requestedName = key.getName();
			boolean done = false;
			for (int i = 0; i < serviceFactories.length && !done; i++) {
				final IConfigurationElement[] serviceNames = serviceFactories[i]
						.getChildren(IWorkbenchRegistryConstants.TAG_SERVICE);
				for (int j = 0; j < serviceNames.length && !done; j++) {
					String serviceName = serviceNames[j]
							.getAttribute(IWorkbenchRegistryConstants.ATTR_SERVICE_CLASS);
					if (requestedName.equals(serviceName)) {
						done = true;
					}
				}
				if (done) {
					final AbstractServiceFactory f = (AbstractServiceFactory) serviceFactories[i]
							.createExecutableExtension(IWorkbenchRegistryConstants.ATTR_FACTORY_CLASS);
					for (int j = 0; j < serviceNames.length && !done; j++) {
						String serviceName = serviceNames[j]
								.getAttribute(IWorkbenchRegistryConstants.ATTR_SERVICE_CLASS);
						if (factories.containsKey(serviceName)) {
							WorkbenchPlugin.log("Factory already exists for " //$NON-NLS-1$
									+ serviceName);
						} else {
							factories.put(serviceName, f);
						}
					}
					service = f.create(key, parentLocator, locator);
				}
			}
		} catch (CoreException e) {
			StatusManager.getManager().handle(e.getStatus());
		}
		return service;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4555.java