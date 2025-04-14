error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2041.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2041.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2041.java
text:
```scala
h@@andler = PluginManager.getInstance().getExtensionHandler(

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.core.scripting.service;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.columba.api.exception.PluginException;
import org.columba.api.exception.PluginHandlerNotFoundException;
import org.columba.api.plugin.IExtensionHandler;
import org.columba.api.plugin.IExtensionHandlerKeys;
import org.columba.api.plugin.IExtensionInterface;
import org.columba.core.logging.Logging;
import org.columba.core.plugin.Extension;
import org.columba.core.plugin.PluginManager;

public class ServiceManager {

	private static final Logger LOG = Logger
			.getLogger("org.columba.core.scripting.service.ServiceManager");

	private static ServiceManager instance = new ServiceManager();

	private IExtensionHandler handler;

	private ServiceManager() {
		try {
			handler = PluginManager.getInstance().getHandler(
					IExtensionHandlerKeys.ORG_COLUMBA_CORE_SERVICE);
		} catch (PluginHandlerNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static ServiceManager getInstance() {
		return instance;
	}

	/**
	 * Retrieve service instance. <code>ExtensionHandler</code> automatically
	 * handles singleton extensions. We don't need to cache instances.
	 * 
	 * @param extension
	 *            extension metadata
	 * @return instance of extension interface
	 */
	private IColumbaService getServiceInstance(Extension extension) {

		IExtensionInterface service = null;
		try {
			service = (IExtensionInterface) extension
					.instanciateExtension(new Object[] {});
		} catch (PluginException e1) {
			LOG.severe("Failed to load service: " + e1.getMessage());

			if (Logging.DEBUG)
				e1.printStackTrace();

			return null;
		}

		if (!(service instanceof IColumbaService)) {
			LOG.log(Level.WARNING,
					"Service plugin doesn't explicitly declare an "
							+ "IColumbaService interface. Service ignored...");
			return null;
		}

		return (IColumbaService) service;

	}

	/**
	 * Instanciate all services.
	 * 
	 */
	public void initServices() {
		Enumeration e = handler.getExtensionEnumeration();
		while (e.hasMoreElements()) {
			Extension extension = (Extension) e.nextElement();

			// retrieving the instance for the first time
			// creates an instance in ExtensionHandler subclass
			// 
			// instance reference is kept in hashmap automatically
			IColumbaService service = getServiceInstance(extension);
			service.initService();

		}

	}

	public void disposeServices() {
		Enumeration e = handler.getExtensionEnumeration();
		while (e.hasMoreElements()) {
			Extension extension = (Extension) e.nextElement();
			IColumbaService service = getServiceInstance(extension);
			service.disposeService();
		}
	}

	public void startServices() {
		Enumeration e = handler.getExtensionEnumeration();
		while (e.hasMoreElements()) {
			Extension extension = (Extension) e.nextElement();
			IColumbaService service = getServiceInstance(extension);
			service.startService();
		}

	}

	public void stopServices() {
		Enumeration e = handler.getExtensionEnumeration();
		while (e.hasMoreElements()) {
			Extension extension = (Extension) e.nextElement();
			IColumbaService service = getServiceInstance(extension);
			service.stopService();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2041.java