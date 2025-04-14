error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15132.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15132.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15132.java
text:
```scala
c@@ontainerFactory.createListenerContainer(endpoint);

/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.jms.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Before;
import org.junit.Test;

import org.springframework.context.support.StaticApplicationContext;
import org.springframework.jms.StubTextMessage;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author Stephane Nicoll
 */
public class JmsListenerContainerFactoryIntegrationTests {

	private final DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();

	private final DefaultJmsHandlerMethodFactory factory = new DefaultJmsHandlerMethodFactory();

	private final JmsEndpointSampleBean sample = new JmsEndpointSampleBean();

	@Before
	public void setup() {
		initializeFactory(factory);
	}

	@Test
	public void messageConverterUsedIfSet() throws JMSException {
		containerFactory.setMessageConverter(new UpperCaseMessageConverter());

		MethodJmsListenerEndpoint endpoint = createDefaultMethodJmsEndpoint("expectFooBarUpperCase", String.class);
		Message message = new StubTextMessage("foo-bar");

		invokeListener(endpoint, message);
		assertListenerMethodInvocation("expectFooBarUpperCase");
	}

	static class JmsEndpointSampleBean {

		private final Map<String, Boolean> invocations = new HashMap<String, Boolean>();

		public void expectFooBarUpperCase(@Payload String msg) {
			invocations.put("expectFooBarUpperCase", true);
			assertEquals("Unexpected payload message", "FOO-BAR", msg);
		}
	}

	@SuppressWarnings("unchecked")
	private void invokeListener(JmsListenerEndpoint endpoint, Message message) throws JMSException {
		DefaultMessageListenerContainer messageListenerContainer =
				containerFactory.createMessageListenerContainer(endpoint);
		Object listener = messageListenerContainer.getMessageListener();
		if (listener instanceof SessionAwareMessageListener) {
			((SessionAwareMessageListener<Message>) listener).onMessage(message, mock(Session.class));
		}
		else {
			((MessageListener) listener).onMessage(message);
		}
	}

	private void assertListenerMethodInvocation(String methodName) {
		assertTrue("Method " + methodName + " should have been invoked", sample.invocations.get(methodName));
	}


	private MethodJmsListenerEndpoint createMethodJmsEndpoint(
			DefaultJmsHandlerMethodFactory factory, Method method) {
		MethodJmsListenerEndpoint endpoint = new MethodJmsListenerEndpoint();
		endpoint.setBean(sample);
		endpoint.setMethod(method);
		endpoint.setJmsHandlerMethodFactory(factory);
		return endpoint;
	}

	private MethodJmsListenerEndpoint createDefaultMethodJmsEndpoint(String methodName, Class<?>... parameterTypes) {
		return createMethodJmsEndpoint(this.factory, getListenerMethod(methodName, parameterTypes));
	}

	private Method getListenerMethod(String methodName, Class<?>... parameterTypes) {
		Method method = ReflectionUtils.findMethod(JmsEndpointSampleBean.class,
				methodName, parameterTypes);
		assertNotNull("no method found with name " + methodName
				+ " and parameters " + Arrays.toString(parameterTypes));
		return method;
	}


	private void initializeFactory(DefaultJmsHandlerMethodFactory factory) {
		factory.setApplicationContext(new StaticApplicationContext());
		factory.afterPropertiesSet();
	}


	private static class UpperCaseMessageConverter implements MessageConverter {
		@Override
		public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
			return new StubTextMessage(object.toString().toUpperCase());
		}

		@Override
		public Object fromMessage(Message message) throws JMSException, MessageConversionException {
			String content = ((TextMessage) message).getText();
			return content.toUpperCase();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15132.java