error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12808.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12808.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12808.java
text:
```scala
r@@egistrySharedObject.removeRemoteServiceListener(listener);

package org.eclipse.ecf.provider.remoteservice.generic;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.eclipse.ecf.core.ISharedObjectContainerConfig;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.provider.generic.TCPClientSOContainer;
import org.eclipse.ecf.remoteservice.IRemoteService;
import org.eclipse.ecf.remoteservice.IRemoteServiceContainer;
import org.eclipse.ecf.remoteservice.IRemoteServiceListener;
import org.eclipse.ecf.remoteservice.IRemoteServiceReference;
import org.eclipse.ecf.remoteservice.IRemoteServiceRegistration;

public class RemoteServiceContainer extends TCPClientSOContainer
		implements IRemoteServiceContainer {

	private static final String REGISTRY_ID = RemoteServiceContainer.class.getName() + ".registry";
	protected List serviceListener = new ArrayList();
	protected RegistrySharedObject registrySharedObject;
	
	protected RegistrySharedObject createAndAddRegistry() {
		RegistrySharedObject rso = new RegistrySharedObject();
		try {
			getSharedObjectManager().addSharedObject(
					IDFactory.getDefault().createStringID(REGISTRY_ID), rso,
					null);
		} catch (Exception e) {
			// Should not happen...if does throw NullPointerException
			throw new NullPointerException();
			// TODO: handle exception
		}	
		return rso;
	}

	public RemoteServiceContainer(ISharedObjectContainerConfig config) {
		super(config);
		registrySharedObject = createAndAddRegistry();
	}

	public RemoteServiceContainer(ISharedObjectContainerConfig config,
			int ka) {
		super(config, ka);
		registrySharedObject = createAndAddRegistry();
	}

	public void addRemoteServiceListener(IRemoteServiceListener listener) {
		registrySharedObject.addRemoteServiceListener(listener);

	}

	public IRemoteService getRemoteService(IRemoteServiceReference ref)
			throws ECFException {
		return registrySharedObject.getRemoteService(ref);
	}

	public IRemoteServiceReference[] getRemoteServiceReferences(ID[] idFilter,
			String clazz, String filter) throws ECFException {
		return registrySharedObject.getRemoteServiceReferences(idFilter,clazz,filter);
	}

	public IRemoteServiceRegistration registerRemoteService(String[] clazzes,
			Object service, Dictionary properties) throws ECFException {
		return registrySharedObject.registerRemoteService(clazzes,service,properties);
	}

	public void removeRemoteServiceListener(IRemoteServiceListener listener) {
		registrySharedObject.remoteRemoteServiceListener(listener);
	}

	public boolean ungetRemoteService(IRemoteServiceReference ref) {
		return registrySharedObject.ungetRemoteService(ref);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12808.java