error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8902.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8902.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8902.java
text:
```scala
a@@ssertFalse(testFlag1);

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

import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.RequestHandlerStack;
import org.apache.wicket.request.Response;

/**
 * 
 * @author Matej Knopp
 */
public class RequestHandlerStackTest extends BaseRequestHandlerStackTest
{

	/**
	 * Construct.
	 */
	public RequestHandlerStackTest()
	{
	}

	private boolean testFlag1;
	private boolean testFlag2;
	private boolean testFlag3;
	private boolean testFlag4;

	private boolean detachedFlag1;
	private boolean detachedFlag2;
	private boolean detachedFlag3;
	private boolean detachedFlag4;

	private void initFlags()
	{
		testFlag1 = true;
		testFlag2 = true;
		testFlag3 = true;
		testFlag4 = true;

		detachedFlag1 = false;
		detachedFlag2 = false;
		detachedFlag3 = false;
		detachedFlag4 = false;
	}

	/**
	 * 
	 */
	public void test1()
	{
		initFlags();

		final Response originalResponse = newResponse();

		final RequestHandlerStack stack = newStack(originalResponse);

		final IRequestHandler handler3 = new IRequestHandler()
		{
			public void respond(IRequestCycle requestCycle)
			{
				testFlag3 = false;
			}

			public void detach(IRequestCycle requestCycle)
			{
				detachedFlag3 = true;
			}
		};

		final IRequestHandler handler2 = new IRequestHandler()
		{
			public void respond(IRequestCycle requestCycle)
			{
				testFlag2 = false;

				stack.replaceAllRequestHandlers(handler3);

				// this code must not be executed
				testFlag2 = true;
			}

			public void detach(IRequestCycle requestCycle)
			{
				detachedFlag2 = true;
			}
		};

		final IRequestHandler handler1 = new IRequestHandler()
		{
			public void respond(IRequestCycle requestCycle)
			{
				testFlag1 = false;

				Response resp = newResponse();
				stack.setResponse(resp);
				stack.executeRequestHandler(handler2);
				assertEquals(stack.getResponse(), resp);

				// this code must be executed
				testFlag1 = true;
			}

			public void detach(IRequestCycle requestCycle)
			{
				detachedFlag1 = true;
			}
		};

		stack.executeRequestHandler(handler1);

		assertEquals(stack.getResponse(), originalResponse);

		stack.detach();

		assertTrue(testFlag1);
		assertFalse(testFlag2);
		assertFalse(testFlag3);

		assertTrue(detachedFlag1);
		assertTrue(detachedFlag2);
		assertTrue(detachedFlag3);
	}

	/**
	 * 
	 */
	public void test2()
	{
		initFlags();

		final Response originalResponse = newResponse();
		final RequestHandlerStack stack = newStack(originalResponse);

		final IRequestHandler handler4 = new IRequestHandler()
		{
			public void respond(IRequestCycle requestCycle)
			{
				testFlag4 = false;

				assertEquals(stack.getResponse(), originalResponse);

				stack.setResponse(newResponse());
			}

			public void detach(IRequestCycle requestCycle)
			{
				detachedFlag4 = true;
			}
		};

		final IRequestHandler handler3 = new IRequestHandler()
		{
			public void respond(IRequestCycle requestCycle)
			{
				testFlag3 = false;
				stack.setResponse(newResponse());
				stack.replaceAllRequestHandlers(handler4);
				// code must not be reached
				testFlag3 = true;
			}

			public void detach(IRequestCycle requestCycle)
			{
				detachedFlag3 = true;
			}
		};

		final IRequestHandler handler2 = new IRequestHandler()
		{
			public void respond(IRequestCycle requestCycle)
			{
				testFlag2 = false;
				stack.setResponse(newResponse());
				stack.executeRequestHandler(handler3);
				// code must not be reached
				testFlag2 = true;
			}

			public void detach(IRequestCycle requestCycle)
			{
				detachedFlag2 = true;
			}
		};

		IRequestHandler handler1 = new IRequestHandler()
		{
			public void respond(IRequestCycle requestCycle)
			{
				testFlag1 = false;
				stack.setResponse(newResponse());
				stack.executeRequestHandler(handler2);

				// code must not be reached
				testFlag1 = true;
			}

			public void detach(IRequestCycle requestCycle)
			{
				detachedFlag1 = true;
			}
		};

		stack.executeRequestHandler(handler1);

		assertEquals(stack.getResponse(), originalResponse);

		stack.detach();

		assertFalse(testFlag1);
		assertFalse(testFlag2);
		assertFalse(testFlag3);
		assertFalse(testFlag4);

		assertTrue(detachedFlag1);
		assertTrue(detachedFlag2);
		assertTrue(detachedFlag3);
		assertTrue(detachedFlag4);
	}

	/**
	 * 
	 */
	public void test3()
	{
		initFlags();

		final Response originalResponse = newResponse();
		final RequestHandlerStack stack = newStack(originalResponse);

		final IRequestHandler handler4 = new IRequestHandler()
		{
			public void respond(IRequestCycle requestCycle)
			{
				testFlag4 = true;

				stack.setResponse(newResponse());
			}

			public void detach(IRequestCycle requestCycle)
			{
				detachedFlag4 = true;
			}
		};

		final IRequestHandler handler3 = new IRequestHandler()
		{
			public void respond(IRequestCycle requestCycle)
			{
				testFlag3 = false;
				stack.scheduleRequestHandlerAfterCurrent(handler4);

				// make sure that handler4's respond method is fired after this
				// one ends
				testFlag4 = false;

				// code must be be reached
				testFlag3 = true;
			}

			public void detach(IRequestCycle requestCycle)
			{
				detachedFlag3 = true;
			}
		};

		final IRequestHandler handler2 = new IRequestHandler()
		{
			public void respond(IRequestCycle requestCycle)
			{
				testFlag2 = false;
				stack.executeRequestHandler(handler3);
				// code must be reached
				testFlag2 = true;
			}

			public void detach(IRequestCycle requestCycle)
			{
				detachedFlag2 = true;
			}
		};

		IRequestHandler handler1 = new IRequestHandler()
		{
			public void respond(IRequestCycle requestCycle)
			{
				testFlag1 = false;
				stack.executeRequestHandler(handler2);

				// code must be reached
				testFlag1 = true;
			}

			public void detach(IRequestCycle requestCycle)
			{
				detachedFlag1 = true;
			}
		};

		stack.executeRequestHandler(handler1);

		assertEquals(stack.getResponse(), originalResponse);

		stack.detach();

		assertTrue(testFlag1);
		assertTrue(testFlag2);
		assertTrue(testFlag3);
		assertTrue(testFlag4);

		assertTrue(detachedFlag1);
		assertTrue(detachedFlag2);
		assertTrue(detachedFlag3);
		assertTrue(detachedFlag4);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8902.java