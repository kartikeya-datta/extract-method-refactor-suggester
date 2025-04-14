error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13369.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13369.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13369.java
text:
```scala
V@@isits.visitPostOrder(cursor, new ComponentEventVisitor(event));

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
package org.apache.wicket;

import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEventSink;
import org.apache.wicket.event.IEventSource;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.util.visit.Visits;

/**
 * Implements event {@link Broadcast}ing
 * 
 * @author Igor Vaynberg (ivaynberg)
 */
final class ComponentEventSender implements IEventSource
{
	private final Component source;

	/**
	 * Constructor
	 * 
	 * @param source
	 *            component that originated the event
	 */
	public ComponentEventSender(Component source)
	{
		this.source = source;
	}

	/** {@inheritDoc} */
	public <T> void send(IEventSink sink, Broadcast type, T payload)
	{
		ComponentEvent<?> event = new ComponentEvent<T>(sink, source, type, payload);
		Args.notNull(type, "type");
		switch (type)
		{
			case BUBBLE :
				bubble(event);
				break;
			case BREADTH :
				breadth(event);
				break;
			case DEPTH :
				depth(event);
				break;
			case EXACT :
				event.getSink().onEvent(event);
				break;
		}
	}

	/**
	 * Breadth broadcast
	 * 
	 * @param event
	 */
	private void breadth(final ComponentEvent<?> event)
	{
		IEventSink sink = event.getSink();

		boolean targetsApplication = sink instanceof Application;
		boolean targetsSession = targetsApplication || sink instanceof Session;
		boolean targetsCycle = targetsSession || sink instanceof RequestCycle;
		boolean targetsComponent = sink instanceof Component;

		if (!targetsComponent && !targetsCycle)
		{
			sink.onEvent(event);
			return;
		}

		if (targetsApplication)
		{
			source.getApplication().onEvent(event);
		}
		if (event.isStop())
		{
			return;
		}
		if (targetsSession)
		{
			source.getSession().onEvent(event);
		}
		if (event.isStop())
		{
			return;
		}
		if (targetsCycle)
		{
			source.getRequestCycle().onEvent(event);
		}
		if (event.isStop())
		{
			return;
		}

		Component cursor = (targetsCycle) ? cursor = source.getPage() : (Component)sink;

		cursor.onEvent(event);

		if (event.isStop())
		{
			return;
		}

		event.resetShallow(); // reset shallow flag

		if (cursor instanceof MarkupContainer)
		{
			((MarkupContainer)cursor).visitChildren(new ComponentEventVisitor(event));
		}
	}

	/**
	 * Depth broadcast
	 * 
	 * @param event
	 *            event
	 */
	private void depth(final ComponentEvent<?> event)
	{
		IEventSink sink = event.getSink();

		boolean targetsApplication = sink instanceof Application;
		boolean targetsSession = targetsApplication || sink instanceof Session;
		boolean targetsCycle = targetsSession || sink instanceof RequestCycle;
		boolean targetsComponnet = sink instanceof Component;

		if (!targetsComponnet && !targetsCycle)
		{
			sink.onEvent(event);
			return;
		}

		Component cursor = (targetsCycle) ? source.getPage() : (Component)sink;

		if (cursor instanceof MarkupContainer)
		{
			Visits.visitComponentsPostOrder(cursor, new ComponentEventVisitor(event));
		}
		if (event.isStop())
		{
			return;
		}
		if (targetsCycle)
		{
			source.getRequestCycle().onEvent(event);
		}
		if (event.isStop())
		{
			return;
		}
		if (targetsSession)
		{
			source.getSession().onEvent(event);
		}
		if (event.isStop())
		{
			return;
		}
		if (targetsApplication)
		{
			source.getApplication().onEvent(event);
		}
	}

	/**
	 * Bubbles the event
	 * 
	 * @param event
	 *            event
	 */
	private void bubble(ComponentEvent<?> event)
	{
		IEventSink sink = event.getSink();

		boolean targetsComponent = sink instanceof Component;
		boolean targetsCycle = targetsComponent || sink instanceof RequestCycle;
		boolean targetsSession = targetsCycle || sink instanceof Session;
		boolean targetsApplication = targetsSession || sink instanceof Application;

		if (!targetsApplication && !targetsComponent)
		{
			sink.onEvent(event);
			return;
		}

		if (targetsComponent)
		{
			Component cursor = (Component)sink;
			cursor.onEvent(event);
			if (event.isStop())
			{
				return;
			}
			cursor.visitParents(Component.class, new ComponentEventVisitor(event));
		}

		if (event.isStop())
		{
			return;
		}
		if (targetsCycle)
		{
			source.getRequestCycle().onEvent(event);
		}
		if (event.isStop())
		{
			return;
		}
		if (targetsSession)
		{
			source.getSession().onEvent(event);
		}
		if (event.isStop())
		{
			return;
		}
		if (targetsApplication)
		{
			source.getApplication().onEvent(event);
		}
	}

	/**
	 * Visitor used to broadcast events to components
	 * 
	 * @author igor
	 */
	private static class ComponentEventVisitor implements IVisitor<Component, Void>
	{
		private final ComponentEvent<?> e;

		/**
		 * Constructor
		 * 
		 * @param event
		 *            event to send
		 */
		private ComponentEventVisitor(ComponentEvent<?> event)
		{
			e = event;
		}

		/** {@inheritDoc} */
		public void component(Component object, IVisit<Void> visit)
		{
			object.onEvent(e);

			if (e.isStop())
			{
				visit.stop();
			}

			if (e.isShallow())
			{
				visit.dontGoDeeper();
			}

			e.resetShallow(); // reset shallow bit
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13369.java