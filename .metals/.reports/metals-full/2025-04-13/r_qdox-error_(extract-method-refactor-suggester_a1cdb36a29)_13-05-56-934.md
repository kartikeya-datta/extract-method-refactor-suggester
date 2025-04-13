error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12448.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12448.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12448.java
text:
```scala
a@@ssertEquals(0, incrementingListener.schedules);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.request.cycle;

import org.apache.wicket.Application;
import org.apache.wicket.ThreadContext;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.mock.MockWebRequest;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.apache.wicket.request.IExceptionMapper;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.Url;
import org.apache.wicket.resource.DummyApplication;

/**
 * @author Jeremy Thomerson
 */
public class RequestCycleListenerTest extends BaseRequestHandlerStackTest
{

	private IRequestHandler handler;

	private int errorCode;

	private int responses;

	private int detaches;

	private int exceptionsMapped;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		DummyApplication application = new DummyApplication();
		application.setName("dummyTestApplication");
		ThreadContext.setApplication(application);
		application.setServletContext(new MockServletContext(application, "/"));
		application.initApplication();
		errorCode = 0;
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		ThreadContext.getApplication().internalDestroy();
		ThreadContext.detach();
	}


	private RequestCycle newRequestCycle(final boolean throwExceptionInRespond)
	{
		final Response originalResponse = newResponse();
		Request request = new MockWebRequest(Url.parse("http://wicket.apache.org"));
		handler = new IRequestHandler()
		{
			public void respond(IRequestCycle requestCycle)
			{
				if (throwExceptionInRespond)
				{
					throw new RuntimeException("testing purposes only");
				}
				responses++;
			}

			public void detach(IRequestCycle requestCycle)
			{
				detaches++;
			}
		};
		IRequestMapper requestMapper = new IRequestMapper()
		{
			public IRequestHandler mapRequest(Request request)
			{
				return handler;
			}

			public Url mapHandler(IRequestHandler requestHandler)
			{
				throw new UnsupportedOperationException();
			}

			public int getCompatibilityScore(Request request)
			{
				throw new UnsupportedOperationException();
			}
		};
		IExceptionMapper exceptionMapper = new IExceptionMapper()
		{
			public IRequestHandler map(Exception e)
			{
				exceptionsMapped++;
				return null;
			}
		};
		RequestCycleContext context = new RequestCycleContext(request, originalResponse,
			requestMapper, exceptionMapper);

		RequestCycle cycle = new RequestCycle(context);

		if (Application.exists())
		{
			cycle.getListeners().add(Application.get().getRequestCycleListeners());
		}

		return cycle;
	}

	/**
	 * @throws Exception
	 */
	public void testBasicOperations() throws Exception
	{
		IncrementingListener incrementingListener = new IncrementingListener();
		Application.get().getRequestCycleListeners().add(incrementingListener);

		RequestCycle cycle = newRequestCycle(false);

		incrementingListener.assertValues(0, 0, 0, 0);
		assertValues(0, 0, 0);
		/*
		 * begins, ends, responses and detaches should increment by one because we have one listener
		 * 
		 * exceptions should not increment because none are thrown
		 */
		cycle.processRequestAndDetach();

		incrementingListener.assertValues(1, 1, 0, 1);
		assertValues(0, 1, 1);

		// TEST WITH TWO LISTENERS
		cycle = newRequestCycle(false);
		cycle.getListeners().add(incrementingListener);
		/*
		 * we now have two listeners (app and cycle)
		 * 
		 * begins and ends should increment by two (once for each listener)
		 * 
		 * exceptions should not increment because none are thrown
		 * 
		 * responses and detaches should increment by one
		 */
		cycle.processRequestAndDetach();
		incrementingListener.assertValues(3, 3, 0, 3);
		assertValues(0, 2, 2);

		// TEST WITH TWO LISTENERS AND AN EXCEPTION DURING RESPONSE
		cycle = newRequestCycle(true);
		cycle.getListeners().add(incrementingListener);
		/*
		 * begins and ends should increment by two (once for each listener)
		 * 
		 * exceptions should increment by two (once for each listener)
		 * 
		 * exceptionsMapped should increment by one
		 * 
		 * responses should not increment because of the error
		 * 
		 * detaches should increment by one
		 */
		cycle.processRequestAndDetach();
		incrementingListener.assertValues(5, 5, 2, 5);
		assertValues(1, 2, 3);
	}

	/**
	 * @throws Exception
	 */
	public void testExceptionRequestHandlers() throws Exception
	{
		IncrementingListener incrementingListener = new IncrementingListener();
		Application.get().getRequestCycleListeners().add(incrementingListener);
		Application.get().getRequestCycleListeners().add(new ErrorCodeListener(401));

		RequestCycle cycle = newRequestCycle(true);
		cycle.processRequestAndDetach();

		assertEquals(401, errorCode);
		assertEquals(1, incrementingListener.exceptionResolutions);
		assertEquals(1, incrementingListener.schedules);

		// two listeners that return a request handler should cause an exception
		Application.get().getRequestCycleListeners().add(new ErrorCodeListener(401));
		cycle = newRequestCycle(true);
		try
		{
			cycle.processRequestAndDetach();
			fail("expected an exception because two request cycle listeners returned a request handler");
		}
		catch (WicketRuntimeException e)
		{
			/*
			 * expected, the second handler was resolved but thrown an exception handling the
			 * request
			 */
			assertEquals(2, incrementingListener.resolutions);
		}
	}

	/**
	 * @throws Exception
	 */
	public void testExceptionHandingInOnDetach() throws Exception
	{
		// this test is a little flaky because it depends on the ordering of listeners which is not
		// guaranteed
		RequestCycle cycle = newRequestCycle(false);
		IncrementingListener incrementingListener = new IncrementingListener()
		{
			@Override
			public void onDetach(final RequestCycle cycle)
			{
				super.onDetach(cycle);
				throw new RuntimeException();
			}
		};
		cycle.getListeners().add(incrementingListener);
		cycle.getListeners().add(incrementingListener);
		cycle.getListeners().add(incrementingListener);
		cycle.processRequestAndDetach();

		incrementingListener.assertValues(3, 3, 0, 3);
		assertValues(0, 1, 1);
		assertEquals(3, incrementingListener.resolutions);
	}

	private void assertValues(int exceptionsMapped, int responses, int detaches)
	{
		assertEquals(exceptionsMapped, this.exceptionsMapped);
		assertEquals(responses, this.responses);
		assertEquals(detaches, this.detaches);
	}

	private class ErrorCodeListener extends AbstractRequestCycleListener
	{
		private final int code;

		public ErrorCodeListener(int code)
		{
			this.code = code;
		}

		@Override
		public IRequestHandler onException(final RequestCycle cycle, Exception ex)
		{
			return new IRequestHandler()
			{
				public void respond(IRequestCycle requestCycle)
				{
					errorCode = code;
				}

				public void detach(IRequestCycle requestCycle)
				{
				}

			};
		}
	}


	private class IncrementingListener implements IRequestCycleListener
	{

		private int begins, ends, exceptions, detachesnotified, resolutions, exceptionResolutions,
			schedules = 0;

		public IRequestHandler onException(final RequestCycle cycle, Exception ex)
		{
			exceptions++;
			return null;
		}

		public void onEndRequest(final RequestCycle cycle)
		{
			ends++;
		}

		public void onBeginRequest(final RequestCycle cycle)
		{
			assertNotNull(RequestCycle.get());
			begins++;
		}

		public void onDetach(final RequestCycle cycle)
		{
			detachesnotified++;
		}

		public void onRequestHandlerResolved(IRequestHandler handler)
		{
			resolutions++;
		}

		public void onExceptionRequestHandlerResolved(IRequestHandler handler, Exception exception)
		{
			exceptionResolutions++;
		}

		public void onRequestHandlerScheduled(IRequestHandler handler)
		{
			schedules++;
		}

		private void assertValues(int begins, int ends, int exceptions, int detachesnotified)
		{
			assertEquals(begins, this.begins);
			assertEquals(ends, this.ends);
			assertEquals(exceptions, this.exceptions);
			assertEquals(detachesnotified, this.detachesnotified);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12448.java