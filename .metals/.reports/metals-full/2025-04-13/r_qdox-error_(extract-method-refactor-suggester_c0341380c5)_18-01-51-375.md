error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9409.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9409.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9409.java
text:
```scala
i@@f (!(child instanceof Element) || !child.getLocalName().equals("scheduled")) {

/*
 * Copyright 2002-2009 the original author or authors.
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

package org.springframework.scheduling.config;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parser for the 'scheduled-tasks' element of the scheduling namespace.
 * 
 * @author Mark Fisher
 * @since 3.0
 */
public class ScheduledTasksBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	@Override
	protected boolean shouldGenerateId() {
		return true;
	}

	@Override
	protected String getBeanClassName(Element element) {
		return "org.springframework.scheduling.config.ScheduledTaskRegistrar";
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		ManagedMap<RuntimeBeanReference, String> cronTaskMap = new ManagedMap<RuntimeBeanReference, String>();
		ManagedMap<RuntimeBeanReference, String> fixedDelayTaskMap = new ManagedMap<RuntimeBeanReference, String>();
		ManagedMap<RuntimeBeanReference, String> fixedRateTaskMap = new ManagedMap<RuntimeBeanReference, String>();
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (!(child instanceof Element) || !child.getLocalName().equals("task")) {
				continue;
			}
			Element taskElement = (Element) child;
			String ref = taskElement.getAttribute("ref");
			String method = taskElement.getAttribute("method");
			
			// Check that 'ref' and 'method' are specified
			if (!StringUtils.hasText(ref) || !StringUtils.hasText(method)) {
				parserContext.getReaderContext().error("Both 'ref' and 'method' are required", taskElement);
				// Continue with the possible next task element
				continue;
			}
			
			RuntimeBeanReference runnableBeanRef = new RuntimeBeanReference(
					this.createRunnableBean(ref, method, taskElement, parserContext));
			String cronAttribute = taskElement.getAttribute("cron");
			if (StringUtils.hasText(cronAttribute)) {
				cronTaskMap.put(runnableBeanRef, cronAttribute);
			}
			else {
				String fixedDelayAttribute = taskElement.getAttribute("fixed-delay");
				if (StringUtils.hasText(fixedDelayAttribute)) {
					fixedDelayTaskMap.put(runnableBeanRef, fixedDelayAttribute);
				}
				else {
					String fixedRateAttribute = taskElement.getAttribute("fixed-rate");
					if (!StringUtils.hasText(fixedRateAttribute)) {
						parserContext.getReaderContext().error(
								"One of 'cron', 'fixed-delay', or 'fixed-rate' is required", taskElement);
						// Continue with the possible next task element
						continue;
					}
					fixedRateTaskMap.put(runnableBeanRef, fixedRateAttribute);
				}
			}
		}
		String schedulerRef = element.getAttribute("scheduler");
		if (StringUtils.hasText(schedulerRef)) {
			builder.addPropertyReference("taskScheduler", schedulerRef);
		}
		builder.addPropertyValue("cronTasks", cronTaskMap);
		builder.addPropertyValue("fixedDelayTasks", fixedDelayTaskMap);
		builder.addPropertyValue("fixedRateTasks", fixedRateTaskMap);
	}

	private String createRunnableBean(String ref, String method, Element taskElement, ParserContext parserContext) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(
				"org.springframework.scheduling.support.MethodInvokingRunnable");
		builder.addPropertyReference("targetObject", ref);
		builder.addPropertyValue("targetMethod", method);
		// Extract the source of the current task
		builder.getRawBeanDefinition().setSource(parserContext.extractSource(taskElement));
		String generatedName = parserContext.getReaderContext().generateBeanName(builder.getRawBeanDefinition());
		parserContext.registerBeanComponent(new BeanComponentDefinition(builder.getBeanDefinition(), generatedName));
		return generatedName;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9409.java