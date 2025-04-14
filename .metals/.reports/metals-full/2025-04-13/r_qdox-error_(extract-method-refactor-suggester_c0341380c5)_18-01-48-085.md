error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2234.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2234.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2234.java
text:
```scala
public O@@bject run(IProgressMonitor monitor) throws Exception {

/*******************************************************************************
  * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.remoteservice.generic;

import java.lang.reflect.*;
import java.lang.reflect.Proxy;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ecf.core.util.*;
import org.eclipse.ecf.internal.provider.remoteservice.Messages;
import org.eclipse.ecf.remoteservice.*;
import org.eclipse.osgi.util.NLS;

public class RemoteServiceImpl implements IRemoteService, InvocationHandler {

	protected static final long DEFAULT_TIMEOUT = 30000;

	protected RemoteServiceRegistrationImpl registration = null;

	protected RegistrySharedObject sharedObject = null;

	public RemoteServiceImpl(RegistrySharedObject sharedObject, RemoteServiceRegistrationImpl registration) {
		this.sharedObject = sharedObject;
		this.registration = registration;
	}

	public void callAsynch(IRemoteCall call, IRemoteCallListener listener) {
		sharedObject.sendCallRequestWithListener(registration, call, listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.remoteservice.IRemoteService#callAsynch(org.eclipse.ecf.remoteservice.IRemoteCall)
	 */
	public IFuture callAsynch(final IRemoteCall call) {
		JobsExecutor executor = new JobsExecutor(NLS.bind("callAsynch({0}", call.getMethod())); //$NON-NLS-1$
		return executor.execute(new IProgressRunnable() {
			public Object run(IProgressMonitor monitor) throws Throwable {
				return callSynch(call);
			}
		}, null);
	}

	public Object callSynch(IRemoteCall call) throws ECFException {
		return sharedObject.callSynch(registration, call);
	}

	public void fireAsynch(IRemoteCall call) throws ECFException {
		sharedObject.sendFireRequest(registration, call);
	}

	public Object getProxy() throws ECFException {
		Object proxy;
		try {
			// Get clazz from reference
			final String[] clazzes = registration.getClasses();
			final Class[] cs = new Class[clazzes.length];
			for (int i = 0; i < clazzes.length; i++)
				cs[i] = Class.forName(clazzes[i]);
			proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), cs, this);
		} catch (final Exception e) {
			throw new ECFException(Messages.RemoteServiceImpl_EXCEPTION_CREATING_PROXY, e);
		}
		return proxy;
	}

	public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
		return this.callSynch(new IRemoteCall() {

			public String getMethod() {
				return method.getName();
			}

			public Object[] getParameters() {
				return args;
			}

			public long getTimeout() {
				return DEFAULT_TIMEOUT;
			}
		});
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2234.java