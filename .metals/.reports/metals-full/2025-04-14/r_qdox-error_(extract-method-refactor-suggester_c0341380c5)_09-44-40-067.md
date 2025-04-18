error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9362.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9362.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9362.java
text:
```scala
public I@@Future callAsynch(final IRemoteCall call) {

/*******************************************************************************
 * Copyright (c) 2008 Jan S. Rellermeyer, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Jan S. Rellermeyer - initial API and implementation
 ******************************************************************************/

package org.eclipse.ecf.internal.provider.r_osgi;

import ch.ethz.iks.r_osgi.RemoteOSGiException;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.ecf.core.util.*;
import org.eclipse.ecf.remoteservice.*;
import org.eclipse.ecf.remoteservice.events.*;

/**
 * The R-OSGi adapter implementation of the IRemoteService interface.
 * 
 * @author Jan S. Rellermeyer, ETH Zurich
 */
final class RemoteServiceImpl implements IRemoteService {

	// the ECF remote refImpl
	RemoteServiceReferenceImpl refImpl;

	// the service object.
	private Object service;

	// the next free service ID.
	private long nextID;

	/**
	 * constructor.
	 * 
	 * @param service
	 *            the service (proxy) object.
	 */
	public RemoteServiceImpl(final RemoteServiceReferenceImpl refImpl, final Object service) {
		this.refImpl = refImpl;
		this.service = service;
	}

	/**
	 * call the service asynchronously.
	 * 
	 * @param call
	 *            the call object.
	 * @param listener
	 *            the callback listener.
	 * @see org.eclipse.ecf.remoteservice.IRemoteService#callAsynch(org.eclipse.ecf.remoteservice.IRemoteCall,
	 *      org.eclipse.ecf.remoteservice.IRemoteCallListener)
	 */
	public void callAsynch(final IRemoteCall call, final IRemoteCallListener listener) {
		new AsyncResult(call, listener).start();
	}

	/**
	 * call the service asynchronously.
	 * 
	 * @param call
	 *            the call object.
	 * @return the result proxy.
	 * @see org.eclipse.ecf.remoteservice.IRemoteService#callAsynch(org.eclipse.ecf.remoteservice.IRemoteCall)
	 */
	public IFutureStatus callAsynch(final IRemoteCall call) {
		final FutureStatus result = new FutureStatus();
		final IRemoteCallListener listener = new IRemoteCallListener() {
			public void handleEvent(IRemoteCallEvent event) {
				if (event instanceof IRemoteCallCompleteEvent) {
					IRemoteCallCompleteEvent cce = (IRemoteCallCompleteEvent) event;
					if (cce.hadException())
						result.setException(cce.getException());
					else
						result.set(cce.getResponse());
				}
			}
		};
		callAsynch(call, listener);
		return result;
	}

	/**
	 * call the service synchronously.
	 * 
	 * @param call
	 *            the call object.
	 * @return the result or <code>null</code>
	 * @see org.eclipse.ecf.remoteservice.IRemoteService#callSynch(org.eclipse.ecf.remoteservice.IRemoteCall)
	 */
	public Object callSynch(final IRemoteCall call) throws ECFException {
		final Class[] formalParams = new Class[call.getParameters().length];
		for (int i = 0; i < formalParams.length; i++) {
			formalParams[i] = call.getParameters()[i].getClass();
		}
		try {
			return service.getClass().getMethod(call.getMethod(), formalParams).invoke(service, call.getParameters());
		} catch (Throwable t) {
			throw new ECFException(t);
		}
	}

	/**
	 * fire an asynchronous call without getting the result returned.
	 * 
	 * @param call
	 *            the call object.
	 * @see org.eclipse.ecf.remoteservice.IRemoteService#fireAsynch(org.eclipse.ecf.remoteservice.IRemoteCall)
	 */
	public void fireAsynch(final IRemoteCall call) throws ECFException {
		try {
			callAsynch(call);
		} catch (RemoteOSGiException r) {
			throw new ECFException(r);
		} catch (Throwable t) {
			// do not rethrow
		}
	}

	/**
	 * get the service proxy object.
	 * 
	 * @return the service proxy object.
	 * @see org.eclipse.ecf.remoteservice.IRemoteService#getProxy()
	 */
	public Object getProxy() throws ECFException {
		if (!refImpl.getR_OSGiServiceReference().isActive()) {
			throw new ECFException("Container currently not connected"); //$NON-NLS-1$
		}
		return service;
	}

	/**
	 * get the next call id.
	 * 
	 * @return the next call id.
	 */
	synchronized long getNextID() {
		return nextID++;
	}

	/**
	 * inner class implementing the asynchronous result object. This
	 * implementation also provides the calling infrastructure.
	 */
	private class AsyncResult extends Thread implements IAsyncResult {

		// the result of the call.
		Object result;

		// the exception, if any happened during the call.
		Throwable exception;

		// the remote call object.
		IRemoteCall call;

		// the callback listener, if provided.
		private IRemoteCallListener listener;

		// constructor
		AsyncResult(final IRemoteCall call, final IRemoteCallListener listener) {
			this.call = call;
			this.listener = listener;
		}

		/**
		 * clear the effects of the last remote call.
		 * 
		 * @see org.eclipse.ecf.core.util.IAsyncResult#clear()
		 */
		public synchronized void clear() {
			result = null;
			exception = null;
		}

		/**
		 * get the result, block until available.
		 * 
		 * @return the result.
		 * 
		 * @see org.eclipse.ecf.core.util.IAsyncResult#get()
		 */
		public Object get() throws InterruptedException, InvocationTargetException {
			return get(0);
		}

		/**
		 * get the result. If not yet available, wait at most <code>msecs</code>
		 * milliseconds.
		 * 
		 * @param msecs
		 *            the maximum amount of milliseconds to wait.
		 * @return the result.
		 * @see org.eclipse.ecf.core.util.IAsyncResult#get(long)
		 */
		public synchronized Object get(final long msecs) throws InterruptedException, InvocationTargetException {
			if (exception != null) {
				throw getException();
			}
			if (result != null) {
				return result;
			}
			wait(msecs);
			return result;
		}

		/**
		 * get the exception of the last call, if any happened.
		 * 
		 * @return the exception, or <code>null</code>.
		 * @see org.eclipse.ecf.core.util.IAsyncResult#getException()
		 */
		public synchronized InvocationTargetException getException() {
			return exception == null ? null : new InvocationTargetException(exception);
		}

		/**
		 * tests, if the result is ready and available.
		 * 
		 * @return <code>true</code>, if the result is available.
		 * @see org.eclipse.ecf.core.util.IAsyncResult#isReady()
		 */
		public synchronized boolean isReady() {
			return result != null || exception != null;
		}

		/**
		 * peek, if the result is already available.
		 * 
		 * @return the result, or <code>null</code>, if not yet available.
		 * @see org.eclipse.ecf.core.util.IAsyncResult#peek()
		 */
		public synchronized Object peek() {
			return result;
		}

		// the call happens here.
		public void run() {
			Object r = null;
			Throwable e = null;

			final long reqID = getNextID();

			if (listener != null) {
				listener.handleEvent(new IRemoteCallStartEvent() {
					public IRemoteCall getCall() {
						return call;
					}

					public IRemoteServiceReference getReference() {
						return refImpl;
					}

					public long getRequestId() {
						return reqID;
					}
				});
			}

			try {
				r = callSynch(call);
			} catch (Throwable t) {
				e = t;
			}

			synchronized (AsyncResult.this) {
				result = r;
				exception = e;
				AsyncResult.this.notify();
			}

			if (listener != null) {
				listener.handleEvent(new IRemoteCallCompleteEvent() {

					public Throwable getException() {
						return exception;
					}

					public Object getResponse() {
						return result;
					}

					public boolean hadException() {
						return exception != null;
					}

					public long getRequestId() {
						return reqID;
					}
				});
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9362.java