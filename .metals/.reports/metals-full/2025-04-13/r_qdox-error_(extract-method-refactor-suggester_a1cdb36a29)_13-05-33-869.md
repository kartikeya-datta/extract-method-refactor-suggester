error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10604.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10604.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10604.java
text:
```scala
S@@tring localName = parserContext.getDelegate().getLocalName(child);

/*
 * Copyright 2002-2008 the original author or authors.
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

import javax.jms.Session;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Abstract parser for JMS listener container elements, providing support for
 * common properties that are identical for all listener container variants.
 *
 * @author Juergen Hoeller
 * @since 2.5
 */
abstract class AbstractListenerContainerParser implements BeanDefinitionParser {

	protected static final String LISTENER_ELEMENT = "listener";

	protected static final String ID_ATTRIBUTE = "id";

	protected static final String DESTINATION_ATTRIBUTE = "destination";

	protected static final String SUBSCRIPTION_ATTRIBUTE = "subscription";

	protected static final String SELECTOR_ATTRIBUTE = "selector";

	protected static final String REF_ATTRIBUTE = "ref";

	protected static final String METHOD_ATTRIBUTE = "method";

	protected static final String DESTINATION_RESOLVER_ATTRIBUTE = "destination-resolver";

	protected static final String MESSAGE_CONVERTER_ATTRIBUTE = "message-converter";

	protected static final String RESPONSE_DESTINATION_ATTRIBUTE = "response-destination";

	protected static final String DESTINATION_TYPE_ATTRIBUTE = "destination-type";

	protected static final String DESTINATION_TYPE_QUEUE = "queue";

	protected static final String DESTINATION_TYPE_TOPIC = "topic";

	protected static final String DESTINATION_TYPE_DURABLE_TOPIC = "durableTopic";

	protected static final String CLIENT_ID_ATTRIBUTE = "client-id";

	protected static final String ACKNOWLEDGE_ATTRIBUTE = "acknowledge";

	protected static final String ACKNOWLEDGE_AUTO = "auto";

	protected static final String ACKNOWLEDGE_CLIENT = "client";

	protected static final String ACKNOWLEDGE_DUPS_OK = "dups-ok";

	protected static final String ACKNOWLEDGE_TRANSACTED = "transacted";

	protected static final String TRANSACTION_MANAGER_ATTRIBUTE = "transaction-manager";

	protected static final String CONCURRENCY_ATTRIBUTE = "concurrency";

	protected static final String PREFETCH_ATTRIBUTE = "prefetch";


	public BeanDefinition parse(Element element, ParserContext parserContext) {
		CompositeComponentDefinition compositeDef =
			new CompositeComponentDefinition(element.getTagName(), parserContext.extractSource(element));
		parserContext.pushContainingComponent(compositeDef);

		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				String localName = child.getLocalName();
				if (LISTENER_ELEMENT.equals(localName)) {
					parseListener((Element) child, element, parserContext);
				}
			}
		}
		
		parserContext.popAndRegisterContainingComponent();
		return null;
	}

	private void parseListener(Element listenerEle, Element containerEle, ParserContext parserContext) {
		RootBeanDefinition listenerDef = new RootBeanDefinition();
		listenerDef.setSource(parserContext.extractSource(listenerEle));
		
		String ref = listenerEle.getAttribute(REF_ATTRIBUTE);
		if (!StringUtils.hasText(ref)) {
			parserContext.getReaderContext().error(
					"Listener 'ref' attribute contains empty value.", listenerEle);
		}
		else {
			listenerDef.getPropertyValues().addPropertyValue("delegate", new RuntimeBeanReference(ref));
		}

		String method = null;
		if (listenerEle.hasAttribute(METHOD_ATTRIBUTE)) {
			method = listenerEle.getAttribute(METHOD_ATTRIBUTE);
			if (!StringUtils.hasText(method)) {
				parserContext.getReaderContext().error(
						"Listener 'method' attribute contains empty value.", listenerEle);
			}
		}
		listenerDef.getPropertyValues().addPropertyValue("defaultListenerMethod", method);

		if (containerEle.hasAttribute(MESSAGE_CONVERTER_ATTRIBUTE)) {
			String messageConverter = containerEle.getAttribute(MESSAGE_CONVERTER_ATTRIBUTE);
			if (!StringUtils.hasText(messageConverter)) {
				parserContext.getReaderContext().error(
						"Listener container 'message-converter' attribute contains empty value.", containerEle);
			}
			else {
				listenerDef.getPropertyValues().addPropertyValue("messageConverter", 
						new RuntimeBeanReference(messageConverter));
			}
		}

		BeanDefinition containerDef = parseContainer(listenerEle, containerEle, parserContext);

		if (listenerEle.hasAttribute(RESPONSE_DESTINATION_ATTRIBUTE)) {
			String responseDestination = listenerEle.getAttribute(RESPONSE_DESTINATION_ATTRIBUTE);
			boolean pubSubDomain = indicatesPubSub(containerDef);
			listenerDef.getPropertyValues().addPropertyValue(
					pubSubDomain ? "defaultResponseTopicName" : "defaultResponseQueueName", responseDestination);
			if (containerDef.getPropertyValues().contains("destinationResolver")) {
				listenerDef.getPropertyValues().addPropertyValue("destinationResolver",
						containerDef.getPropertyValues().getPropertyValue("destinationResolver").getValue());
			}
		}

		// Remain JMS 1.0.2 compatible for the adapter if the container class indicates this.
		boolean jms102 = indicatesJms102(containerDef);
		listenerDef.setBeanClassName(
				"org.springframework.jms.listener.adapter.MessageListenerAdapter" + (jms102 ? "102" : ""));

		containerDef.getPropertyValues().addPropertyValue("messageListener", listenerDef);

		String containerBeanName = listenerEle.getAttribute(ID_ATTRIBUTE);
		// If no bean id is given auto generate one using the ReaderContext's BeanNameGenerator 
		if (!StringUtils.hasText(containerBeanName)) {
			containerBeanName = parserContext.getReaderContext().generateBeanName(containerDef);
		}
		
		// Register the listener and fire event
		parserContext.registerBeanComponent(new BeanComponentDefinition(containerDef, containerBeanName));
	}

	protected abstract BeanDefinition parseContainer(
			Element listenerEle, Element containerEle, ParserContext parserContext);

	protected boolean indicatesPubSub(BeanDefinition containerDef) {
		return false;
	}

	protected boolean indicatesJms102(BeanDefinition containerDef) {
		return false;
	}

	protected void parseListenerConfiguration(Element ele, ParserContext parserContext, BeanDefinition configDef) {
		String destination = ele.getAttribute(DESTINATION_ATTRIBUTE);
		if (!StringUtils.hasText(destination)) {
			parserContext.getReaderContext().error(
					"Listener 'destination' attribute contains empty value.", ele);
		}
		configDef.getPropertyValues().addPropertyValue("destinationName", destination);

		if (ele.hasAttribute(SUBSCRIPTION_ATTRIBUTE)) {
			String subscription = ele.getAttribute(SUBSCRIPTION_ATTRIBUTE);
			if (!StringUtils.hasText(subscription)) {
				parserContext.getReaderContext().error(
						"Listener 'subscription' attribute contains empty value.", ele);
			}
			configDef.getPropertyValues().addPropertyValue("durableSubscriptionName", subscription);
		}

		if (ele.hasAttribute(SELECTOR_ATTRIBUTE)) {
			String selector = ele.getAttribute(SELECTOR_ATTRIBUTE);
			if (!StringUtils.hasText(selector)) {
				parserContext.getReaderContext().error(
						"Listener 'selector' attribute contains empty value.", ele);
			}
			configDef.getPropertyValues().addPropertyValue("messageSelector", selector);
		}
	}

	protected void parseContainerConfiguration(Element ele, ParserContext parserContext, BeanDefinition configDef) {
		String destinationType = ele.getAttribute(DESTINATION_TYPE_ATTRIBUTE);
		boolean pubSubDomain = false;
		boolean subscriptionDurable = false;
		if (DESTINATION_TYPE_DURABLE_TOPIC.equals(destinationType)) {
			pubSubDomain = true;
			subscriptionDurable = true;
		}
		else if (DESTINATION_TYPE_TOPIC.equals(destinationType)) {
			pubSubDomain = true;
		}
		else if ("".equals(destinationType) || DESTINATION_TYPE_QUEUE.equals(destinationType)) {
			// the default: queue
		}
		else {
			parserContext.getReaderContext().error("Invalid listener container 'destination-type': " +
					"only \"queue\", \"topic\" and \"durableTopic\" supported.", ele);
		}
		configDef.getPropertyValues().addPropertyValue("pubSubDomain", pubSubDomain);
		configDef.getPropertyValues().addPropertyValue("subscriptionDurable", subscriptionDurable);

		if (ele.hasAttribute(CLIENT_ID_ATTRIBUTE)) {
			String clientId = ele.getAttribute(CLIENT_ID_ATTRIBUTE);
			if (!StringUtils.hasText(clientId)) {
				parserContext.getReaderContext().error(
						"Listener 'client-id' attribute contains empty value.", ele);
			}
			configDef.getPropertyValues().addPropertyValue("clientId", clientId);
		}
	}

	protected Integer parseAcknowledgeMode(Element ele, ParserContext parserContext) {
		String acknowledge = ele.getAttribute(ACKNOWLEDGE_ATTRIBUTE);
		if (StringUtils.hasText(acknowledge)) {
			int acknowledgeMode = Session.AUTO_ACKNOWLEDGE;
			if (ACKNOWLEDGE_TRANSACTED.equals(acknowledge)) {
				acknowledgeMode = Session.SESSION_TRANSACTED;
			}
			else if (ACKNOWLEDGE_DUPS_OK.equals(acknowledge)) {
				acknowledgeMode = Session.DUPS_OK_ACKNOWLEDGE;
			}
			else if (ACKNOWLEDGE_CLIENT.equals(acknowledge)) {
				acknowledgeMode = Session.CLIENT_ACKNOWLEDGE;
			}
			else if (!ACKNOWLEDGE_AUTO.equals(acknowledge)) {
				parserContext.getReaderContext().error("Invalid listener container 'acknowledge' setting [" +
						acknowledge + "]: only \"auto\", \"client\", \"dups-ok\" and \"transacted\" supported.", ele);
			}
			return acknowledgeMode;
		}
		else {
			return null;
		}
	}

	protected boolean indicatesPubSubConfig(BeanDefinition configDef) {
		return (Boolean) configDef.getPropertyValues().getPropertyValue("pubSubDomain").getValue();
	}

	protected int[] parseConcurrency(Element ele, ParserContext parserContext) {
		String concurrency = ele.getAttribute(CONCURRENCY_ATTRIBUTE);
		if (!StringUtils.hasText(concurrency)) {
			return null;
		}
		try {
			int separatorIndex = concurrency.indexOf('-');
			if (separatorIndex != -1) {
				int[] result = new int[2];
				result[0] = Integer.parseInt(concurrency.substring(0, separatorIndex));
				result[1] = Integer.parseInt(concurrency.substring(separatorIndex + 1, concurrency.length()));
				return result;
			}
			else {
				return new int[] {1, Integer.parseInt(concurrency)};
			}
		}
		catch (NumberFormatException ex) {
			parserContext.getReaderContext().error("Invalid concurrency value [" + concurrency + "]: only " +
					"single maximum integer (e.g. \"5\") and minimum-maximum combo (e.g. \"3-5\") supported.", ele, ex);
			return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10604.java