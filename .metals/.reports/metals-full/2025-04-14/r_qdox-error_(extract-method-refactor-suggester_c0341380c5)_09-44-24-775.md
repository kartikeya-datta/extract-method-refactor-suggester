error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1461.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1461.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1461.java
text:
```scala
A@@ssert.notNull(taskExecutor, "TaskExecutor must not be null");

/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.socket.client.endpoint;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.Assert;
import org.springframework.web.socket.client.ConnectionManagerSupport;
import org.springframework.web.socket.support.BeanCreatingHandlerProvider;

/**
 * A WebSocket connection manager that is given a URI, a {@link ServerEndpoint}-annotated
 * endpoint, connects to a WebSocket server through the {@link #start()} and
 * {@link #stop()} methods. If {@link #setAutoStartup(boolean)} is set to {@code true}
 * this will be done automatically when the Spring ApplicationContext is refreshed.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class AnnotatedEndpointConnectionManager extends ConnectionManagerSupport implements BeanFactoryAware {

	private final Object endpoint;

	private final BeanCreatingHandlerProvider<Object> endpointProvider;

	private WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();

	private Session session;

	private TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("AnnotatedEndpointConnectionManager-");


	public AnnotatedEndpointConnectionManager(Object endpoint, String uriTemplate, Object... uriVariables) {
		super(uriTemplate, uriVariables);
		this.endpointProvider = null;
		this.endpoint = endpoint;
	}

	public AnnotatedEndpointConnectionManager(Class<?> endpointClass, String uriTemplate, Object... uriVariables) {
		super(uriTemplate, uriVariables);
		this.endpointProvider = new BeanCreatingHandlerProvider<Object>(endpointClass);
		this.endpoint = null;
	}


	public void setWebSocketContainer(WebSocketContainer webSocketContainer) {
		this.webSocketContainer = webSocketContainer;
	}

	public WebSocketContainer getWebSocketContainer() {
		return this.webSocketContainer;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (this.endpointProvider != null) {
			this.endpointProvider.setBeanFactory(beanFactory);
		}
	}

	/**
	 * Set a {@link TaskExecutor} to use to open the connection.
	 * By default {@link SimpleAsyncTaskExecutor} is used.
	 */
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		Assert.notNull(taskExecutor, "taskExecutor is required");
		this.taskExecutor = taskExecutor;
	}

	/**
	 * Return the configured {@link TaskExecutor}.
	 */
	public TaskExecutor getTaskExecutor() {
		return this.taskExecutor;
	}

	@Override
	protected void openConnection() {
		this.taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					logger.info("Connecting to WebSocket at " + getUri());
					Object endpointToUse = (endpoint != null) ? endpoint : endpointProvider.getHandler();
					session = webSocketContainer.connectToServer(endpointToUse, getUri());
					logger.info("Successfully connected");
				}
				catch (Throwable ex) {
					logger.error("Failed to connect", ex);
				}
			}
		});
	}

	@Override
	protected void closeConnection() throws Exception {
		try {
			if (isConnected()) {
				this.session.close();
			}
		}
		finally {
			this.session = null;
		}
	}

	@Override
	protected boolean isConnected() {
		return ((this.session != null) && this.session.isOpen());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1461.java