error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9196.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9196.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9196.java
text:
```scala
protected C@@lass<?> getClassForLogging(Object target) {

/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.aop.interceptor;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.aop.support.AopUtils;

/**
 * Base {@code MethodInterceptor} implementation for tracing.
 *
 * <p>By default, log messages are written to the log for the interceptor class,
 * not the class which is being intercepted. Setting the {@code useDynamicLogger}
 * bean property to {@code true} causes all log messages to be written to
 * the {@code Log} for the target class being intercepted.
 *
 * <p>Subclasses must implement the {@code invokeUnderTrace} method, which
 * is invoked by this class ONLY when a particular invocation SHOULD be traced.
 * Subclasses should write to the {@code Log} instance provided.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.2
 * @see #setUseDynamicLogger
 * @see #invokeUnderTrace(org.aopalliance.intercept.MethodInvocation, org.apache.commons.logging.Log)
 */
@SuppressWarnings("serial")
public abstract class AbstractTraceInterceptor implements MethodInterceptor, Serializable {

	/**
	 * The default {@code Log} instance used to write trace messages.
	 * This instance is mapped to the implementing {@code Class}.
	 */
	protected transient Log defaultLogger = LogFactory.getLog(getClass());

	/**
	 * Indicates whether or not proxy class names should be hidden when using dynamic loggers.
	 * @see #setUseDynamicLogger
	 */
	private boolean hideProxyClassNames = false;


	/**
	 * Set whether to use a dynamic logger or a static logger.
	 * Default is a static logger for this trace interceptor.
	 * <p>Used to determine which {@code Log} instance should be used to write
	 * log messages for a particular method invocation: a dynamic one for the
	 * {@code Class} getting called, or a static one for the {@code Class}
	 * of the trace interceptor.
	 * <p><b>NOTE:</b> Specify either this property or "loggerName", not both.
	 * @see #getLoggerForInvocation(org.aopalliance.intercept.MethodInvocation)
	 */
	public void setUseDynamicLogger(boolean useDynamicLogger) {
		// Release default logger if it is not being used.
		this.defaultLogger = (useDynamicLogger ? null : LogFactory.getLog(getClass()));
	}

	/**
	 * Set the name of the logger to use. The name will be passed to the
	 * underlying logger implementation through Commons Logging, getting
	 * interpreted as log category according to the logger's configuration.
	 * <p>This can be specified to not log into the category of a class
	 * (whether this interceptor's class or the class getting called)
	 * but rather into a specific named category.
	 * <p><b>NOTE:</b> Specify either this property or "useDynamicLogger", not both.
	 * @see org.apache.commons.logging.LogFactory#getLog(String)
	 * @see org.apache.log4j.Logger#getLogger(String)
	 * @see java.util.logging.Logger#getLogger(String)
	 */
	public void setLoggerName(String loggerName) {
		this.defaultLogger = LogFactory.getLog(loggerName);
	}

	/**
	 * Set to "true" to have {@link #setUseDynamicLogger dynamic loggers} hide
	 * proxy class names wherever possible. Default is "false".
	 */
	public void setHideProxyClassNames(boolean hideProxyClassNames) {
		this.hideProxyClassNames = hideProxyClassNames;
	}


	/**
	 * Determines whether or not logging is enabled for the particular {@code MethodInvocation}.
	 * If not, the method invocation proceeds as normal, otherwise the method invocation is passed
	 * to the {@code invokeUnderTrace} method for handling.
	 * @see #invokeUnderTrace(org.aopalliance.intercept.MethodInvocation, org.apache.commons.logging.Log)
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Log logger = getLoggerForInvocation(invocation);
		if (isInterceptorEnabled(invocation, logger)) {
			return invokeUnderTrace(invocation, logger);
		}
		else {
			return invocation.proceed();
		}
	}

	/**
	 * Return the appropriate {@code Log} instance to use for the given
	 * {@code MethodInvocation}. If the {@code useDynamicLogger} flag
	 * is set, the {@code Log} instance will be for the target class of the
	 * {@code MethodInvocation}, otherwise the {@code Log} will be the
	 * default static logger.
	 * @param invocation the {@code MethodInvocation} being traced
	 * @return the {@code Log} instance to use
	 * @see #setUseDynamicLogger
	 */
	protected Log getLoggerForInvocation(MethodInvocation invocation) {
		if (this.defaultLogger != null) {
			return this.defaultLogger;
		}
		else {
			Object target = invocation.getThis();
			return LogFactory.getLog(getClassForLogging(target));
		}
	}

	/**
	 * Determine the class to use for logging purposes.
	 * @param target the target object to introspect
	 * @return the target class for the given object
	 * @see #setHideProxyClassNames
	 */
	protected Class getClassForLogging(Object target) {
		return (this.hideProxyClassNames ? AopUtils.getTargetClass(target) : target.getClass());
	}

	/**
	 * Determine whether the interceptor should kick in, that is,
	 * whether the {@code invokeUnderTrace} method should be called.
	 * <p>Default behavior is to check whether the given {@code Log}
	 * instance is enabled. Subclasses can override this to apply the
	 * interceptor in other cases as well.
	 * @param invocation the {@code MethodInvocation} being traced
	 * @param logger the {@code Log} instance to check
	 * @see #invokeUnderTrace
	 * @see #isLogEnabled
	 */
	protected boolean isInterceptorEnabled(MethodInvocation invocation, Log logger) {
		return isLogEnabled(logger);
	}

	/**
	 * Determine whether the given {@link Log} instance is enabled.
	 * <p>Default is {@code true} when the "trace" level is enabled.
	 * Subclasses can override this to change the level under which 'tracing' occurs.
	 * @param logger the {@code Log} instance to check
	 */
	protected boolean isLogEnabled(Log logger) {
		return logger.isTraceEnabled();
	}


	/**
	 * Subclasses must override this method to perform any tracing around the
	 * supplied {@code MethodInvocation}. Subclasses are responsible for
	 * ensuring that the {@code MethodInvocation} actually executes by
	 * calling {@code MethodInvocation.proceed()}.
	 * <p>By default, the passed-in {@code Log} instance will have log level
	 * "trace" enabled. Subclasses do not have to check for this again, unless
	 * they overwrite the {@code isInterceptorEnabled} method to modify
	 * the default behavior.
	 * @param logger the {@code Log} to write trace messages to
	 * @return the result of the call to {@code MethodInvocation.proceed()}
	 * @throws Throwable if the call to {@code MethodInvocation.proceed()}
	 * encountered any errors
	 * @see #isInterceptorEnabled
	 * @see #isLogEnabled
	 */
	protected abstract Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable;

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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9196.java