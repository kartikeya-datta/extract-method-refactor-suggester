error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3309.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3309.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[12,1]

error in qdox parser
file content:
```java
offset: 591
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3309.java
text:
```scala
public class DistributionProviderImpl implements DistributionProvider {

/*******************************************************************************
* Copyright (c) 2009 EclipseSource and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   EclipseSource - initial API and implementation
******************************************************************************/
p@@ackage org.eclipse.ecf.internal.osgi.services.distribution;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.distribution.DistributionProvider;

public class ECFRSDistributionProvider implements DistributionProvider {

	Map exposedServices = Collections.synchronizedMap(new HashMap());
	Map publishedServices = Collections.synchronizedMap(new HashMap());
	Map remoteServices = Collections.synchronizedMap(new HashMap());
	
	Long getServiceId(ServiceReference sr) {
		return (Long) sr.getProperty(Constants.SERVICE_ID);
	}
	
	ServiceReference addExposedService(ServiceReference sr) {
		if (sr == null) return null;
		return (ServiceReference) exposedServices.put(getServiceId(sr),sr);
	}
	
	ServiceReference addPublishedService(ServiceReference sr) {
		if (sr == null) return null;
		return (ServiceReference) publishedServices.put(getServiceId(sr),sr);
	}
	
	ServiceReference addRemoteService(ServiceReference sr) {
		if (sr == null) return null;
		return (ServiceReference) remoteServices.put(getServiceId(sr),sr);
	}
		
	ServiceReference removeExposedService(Long sid) {
		if (sid == null) return null;
		return (ServiceReference) exposedServices.remove(sid);
	}
	
	ServiceReference removePublishedService(Long sid) {
		if (sid == null) return null;
		return (ServiceReference) publishedServices.remove(sid);
	}
	
	ServiceReference removeRemoteService(Long sid) {
		if (sid == null) return null;
		return (ServiceReference) remoteServices.remove(sid);
	}

	boolean containsExposedService(Long sid) {
		if (sid == null) return false;
		return exposedServices.containsKey(sid);
	}
	
	boolean containsPublishedService(Long sid) {
		if (sid == null) return false;
		return publishedServices.containsKey(sid);
	}
	
	boolean containsRemoteService(Long sid) {
		if (sid == null) return false;
		return remoteServices.containsKey(sid);
	}

	ServiceReference getExposedService(Long sid) {
		if (sid == null) return null;
		return (ServiceReference) exposedServices.get(sid);
	}
	
	ServiceReference getPublishedService(Long sid) {
		if (sid == null) return null;
		return (ServiceReference) publishedServices.get(sid);
	}

	ServiceReference getRemoteService(Long sid) {
		if (sid == null) return null;
		return (ServiceReference) remoteServices.get(sid);
	}

	public ServiceReference[] getExposedServices() {
		return (ServiceReference[]) exposedServices.entrySet().toArray(new ServiceReference[] {});
	}

	public Map getPublicationProperties(ServiceReference sr) {
		// the spec or javadocs don't say what should happen if given sr is null or
		// the given sr is not found in those published...
		Map result = new HashMap();
		if (sr == null) return result;
		ServiceReference publishedService = getPublishedService(getServiceId(sr));
		if (publishedService == null) return result;
		return getPropertyMap(result,publishedService);
	}

	private Map getPropertyMap(Map result, ServiceReference sr) {
		String [] propKeys = sr.getPropertyKeys();
		if (propKeys != null) {
			for(int i=0; i < propKeys.length; i++) {
				result.put(propKeys[i], sr.getProperty(propKeys[i]));
			}
		}
		return result;
	}
	
	public ServiceReference[] getPublishedServices() {
		return (ServiceReference[]) publishedServices.entrySet().toArray(new ServiceReference[] {});
	}

	public ServiceReference[] getRemoteServices() {
		return (ServiceReference[]) remoteServices.entrySet().toArray(new ServiceReference[] {});
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3309.java