error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8761.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8761.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8761.java
text:
```scala
a@@dapter.setResourceProcessor(createRestResource());

/******************************************************************************* 
 * Copyright (c) 2009 EclipseSource and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   EclipseSource - initial API and implementation
 *******************************************************************************/
package org.eclipse.ecf.tests.remoteservice.rest.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.core.security.ConnectContextFactory;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.remoteservice.IRemoteCall;
import org.eclipse.ecf.remoteservice.IRemoteCallListener;
import org.eclipse.ecf.remoteservice.IRemoteService;
import org.eclipse.ecf.remoteservice.IRemoteServiceRegistration;
import org.eclipse.ecf.remoteservice.events.IRemoteCallCompleteEvent;
import org.eclipse.ecf.remoteservice.events.IRemoteCallEvent;
import org.eclipse.ecf.remoteservice.rest.IRestCallable;
import org.eclipse.ecf.remoteservice.rest.RestCallFactory;
import org.eclipse.ecf.remoteservice.rest.RestCallable;
import org.eclipse.ecf.remoteservice.rest.client.IRestClientContainerAdapter;
import org.eclipse.ecf.remoteservice.rest.resource.IRestResourceProcessor;
import org.eclipse.ecf.tests.remoteservice.rest.AbstractRestTestCase;
import org.eclipse.ecf.tests.remoteservice.rest.RestConstants;
import org.eclipse.equinox.concurrent.future.IFuture;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TwitterRemoteServiceTest extends AbstractRestTestCase {

	private String username = System.getProperty("username","eclipsedummy");
	private String password = System.getProperty("password","eclipse");
	
	IContainer container;
	IRemoteServiceRegistration registration;
	
	protected void setUp() throws Exception {
		// Create container
		container = createRestContainer(RestConstants.TEST_TWITTER_TARGET);	
		// Get adapter
		IRestClientContainerAdapter adapter = (IRestClientContainerAdapter) getRestClientContainerAdapter(container);
		// Setup authentication info
		adapter.setConnectContextForAuthentication(ConnectContextFactory.createUsernamePasswordConnectContext(username, password));
		
		// Setup resource handler
		adapter.setRestResource(createRestResource());

		// Create and register callable to register service
		List callables = new ArrayList();
		callables.add(new RestCallable("getUserStatuses","/statuses/user_timeline.json",null,IRestCallable.RequestType.GET));
		// Setup callable
		registration = adapter.registerCallable(new String[] { IUserTimeline.class.getName() }, callables, null);
}

	protected void tearDown() throws Exception {
		registration.unregister();
		container.disconnect();
	}

	private IRestResourceProcessor createRestResource() {
		return new IRestResourceProcessor() {

			public Object createResponseRepresentation(IRemoteCall call, IRestCallable callable, Map responseHeaders, String responseBody)
					throws ECFException {
				try {
					JSONArray timeline = new JSONArray(responseBody);
					List statuses = new ArrayList();
					for (int i = 0; i < timeline.length(); i++) {
						try {
							JSONObject jsonObject = timeline.getJSONObject(i);
							String source = jsonObject.getString("source");
							String text = jsonObject.getString("text");
							String createdString = jsonObject.getString("created_at");
							IUserStatus status = new UserStatus(createdString, source, text);
							statuses.add(status);
						} catch (JSONException e) {
							throw new ECFException("Cannot process response representation",e);
						}
					}
					return (IUserStatus[]) statuses.toArray(new IUserStatus[statuses.size()]);
				} catch (JSONException e) {
					throw new ECFException("JSON array parse exception",e);
				}
			}};
	}

	public void testSyncCall() {
		IRemoteService restClientService = getRestClientContainerAdapter(container).getRemoteService(registration.getReference());
		try {
			Object result = restClientService.callSync(RestCallFactory.createRestCall(IUserTimeline.class.getName() + ".getUserStatuses"));
			assertNotNull(result);
		} catch (ECFException e) {
			fail("Could not contact the service");
		}
	}

	public void testGetProxy() {
		IRemoteService restClientService = getRestClientContainerAdapter(container).getRemoteService(registration.getReference());
		try {
			IUserTimeline userTimeline = (IUserTimeline) restClientService.getProxy();
			assertNotNull(userTimeline);
		} catch (ECFException e) {
			fail("Could not contact the service");
		}
	}

	public void testAsyncCall() {
		IRemoteService restClientService = getRestClientContainerAdapter(container).getRemoteService(registration.getReference());
		IFuture future = restClientService.callAsync(RestCallFactory.createRestCall(IUserTimeline.class.getName() + ".getUserStatuses"));
		try {
			Object response = future.get();
			assertTrue(response instanceof IUserStatus[]);
		} catch (OperationCanceledException e) {
			fail(e.getMessage());
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
	}

	public void testAsyncCallWithListener() throws Exception {
		IRemoteService restClientService = getRestClientContainerAdapter(container).getRemoteService(registration.getReference());
		restClientService.callAsync(RestCallFactory.createRestCall(IUserTimeline.class.getName() + ".getUserStatuses"), new IRemoteCallListener() {
			public void handleEvent(IRemoteCallEvent event) {
				if (event instanceof IRemoteCallCompleteEvent) {
					IRemoteCallCompleteEvent cce = (IRemoteCallCompleteEvent) event;
					Object response = cce.getResponse();
					assertTrue(response instanceof IUserStatus[]);
					syncNotify();
				}
			}
		});
		syncWaitForNotify(10000);
	}

	public void testProxyCall() {
		IRemoteService restClientService = getRestClientContainerAdapter(container).getRemoteService(registration.getReference());
		try {
			IUserTimeline userTimeline = (IUserTimeline) restClientService.getProxy();
			IUserStatus[] statuses = userTimeline.getUserStatuses();
			assertNotNull(statuses);
		} catch (ECFException e) {
			fail("Could not contact the service");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8761.java