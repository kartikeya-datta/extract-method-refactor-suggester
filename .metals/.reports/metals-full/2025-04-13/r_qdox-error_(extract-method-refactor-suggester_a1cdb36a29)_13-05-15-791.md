error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8737.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8737.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8737.java
text:
```scala
n@@ew DefaultResponseProcessor(), exceptionResponseStrategy);

/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.request.compound;

import wicket.request.IRequestEncoder;

/**
 * Default implementation of {@link
 * wicket.request.compound.AbstractCompoundRequestCycleProcessor} that expects
 * the delegate strategies to be set once at construction time.
 * 
 * @author Eelco Hillenius
 */
public class CompoundRequestCycleProcessor extends AbstractCompoundRequestCycleProcessor
{
	/** the strategy for constructing request parameters. */
	private final IRequestEncoder requestEncoder;

	/** the strategy for the target resolver method. */
	private final IRequestTargetResolverStrategy requestTargetResolverStrategy;

	/** the strategy for the event processor method. */
	private final IEventProcessorStrategy eventProcessorStrategy;

	/** the strategy for the response method. */
	private final IResponseStrategy responseStrategy;

	/** the strategy for the exception response method. */
	private final IExceptionResponseStrategy exceptionResponseStrategy;

	/**
	 * Construct using the given strategies and
	 * {@link DefaultRequestTargetResolver}, {@link DefaultResponseProcessor}
	 * and {@link DefaultExceptionResponseProcessor}.
	 * 
	 * @param requestEncoder
	 *            the strategy for constructing request parameters
	 * @param eventProcessorStrategy
	 *            the strategy for the event processor method
	 */
	public CompoundRequestCycleProcessor(IRequestEncoder requestEncoder,
			IEventProcessorStrategy eventProcessorStrategy)
	{
		this(requestEncoder, eventProcessorStrategy, new DefaultExceptionResponseProcessor());
	}

	/**
	 * Construct using the given strategies and
	 * {@link DefaultRequestTargetResolver} and {@link DefaultResponseProcessor}.
	 * 
	 * @param requestEncoder
	 *            the strategy for constructing request parameters
	 * @param eventProcessorStrategy
	 *            the strategy for the event processor method
	 * @param exceptionResponseStrategy
	 */
	public CompoundRequestCycleProcessor(IRequestEncoder requestEncoder,
			IEventProcessorStrategy eventProcessorStrategy,
			IExceptionResponseStrategy exceptionResponseStrategy)
	{
		this(requestEncoder, new DefaultRequestTargetResolver(), eventProcessorStrategy,
				new DefaultResponseProcessor(), new DefaultExceptionResponseProcessor());
	}

	/**
	 * Construct using the given strategies.
	 * 
	 * @param requestParameterFactory
	 *            the strategy for constructing request parameters
	 * @param requestTargetResolverStrategy
	 *            the strategy for the target resolver method
	 * @param eventProcessorStrategy
	 *            the strategy for the event processor method
	 * @param responseStrategy
	 *            the strategy for the response method
	 * @param exceptionResponseStrategy
	 *            the strategy for the exception response method
	 */
	public CompoundRequestCycleProcessor(IRequestEncoder requestParameterFactory,
			IRequestTargetResolverStrategy requestTargetResolverStrategy,
			IEventProcessorStrategy eventProcessorStrategy, IResponseStrategy responseStrategy,
			IExceptionResponseStrategy exceptionResponseStrategy)
	{
		this.requestEncoder = requestParameterFactory;
		this.requestTargetResolverStrategy = requestTargetResolverStrategy;
		this.eventProcessorStrategy = eventProcessorStrategy;
		this.responseStrategy = responseStrategy;
		this.exceptionResponseStrategy = exceptionResponseStrategy;
	}

	/**
	 * @see wicket.request.IRequestCycleProcessor#getRequestEncoder()
	 */
	public final IRequestEncoder getRequestEncoder()
	{
		return requestEncoder;
	}

	/**
	 * @see wicket.request.compound.AbstractCompoundRequestCycleProcessor#getRequestTargetResolverStrategy()
	 */
	protected final IRequestTargetResolverStrategy getRequestTargetResolverStrategy()
	{
		return requestTargetResolverStrategy;
	}

	/**
	 * @see wicket.request.compound.AbstractCompoundRequestCycleProcessor#getEventProcessorStrategy()
	 */
	protected final IEventProcessorStrategy getEventProcessorStrategy()
	{
		return eventProcessorStrategy;
	}

	/**
	 * @see wicket.request.compound.AbstractCompoundRequestCycleProcessor#getResponseStrategy()
	 */
	protected final IResponseStrategy getResponseStrategy()
	{
		return responseStrategy;
	}

	/**
	 * @see wicket.request.compound.AbstractCompoundRequestCycleProcessor#getExceptionResponseStrategy()
	 */
	protected final IExceptionResponseStrategy getExceptionResponseStrategy()
	{
		return exceptionResponseStrategy;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8737.java