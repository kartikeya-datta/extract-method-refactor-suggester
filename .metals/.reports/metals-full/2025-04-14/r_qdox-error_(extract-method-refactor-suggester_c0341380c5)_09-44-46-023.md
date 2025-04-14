error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9292.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9292.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9292.java
text:
```scala
protected C@@lass<?> determineActivationSpecClass(ResourceAdapter adapter) {

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

package org.springframework.jms.listener.endpoint;

import javax.jms.Session;
import javax.resource.spi.ResourceAdapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.BeanWrapper;

/**
 * Default implementation of the {@link JmsActivationSpecFactory} interface.
 * Supports the standard JMS properties as defined by the JMS 1.5 specification,
 * as well as Spring's extended "maxConcurrency" and "prefetchSize" settings
 * through autodetection of well-known vendor-specific provider properties.
 *
 * <p>An ActivationSpec factory is effectively dependent on the concrete
 * JMS provider, e.g. on ActiveMQ. This default implementation simply
 * guesses the ActivationSpec class name from the provider's class name
 * ("ActiveMQResourceAdapter" -> "ActiveMQActivationSpec" in the same package,
 * or "ActivationSpecImpl" in the same package as the ResourceAdapter class),
 * and populates the ActivationSpec properties as suggested by the
 * JCA 1.5 specification (Appendix B). Specify the 'activationSpecClass'
 * property explicitly if these default naming rules do not apply.
 *
 * <p>Note: ActiveMQ, JORAM and WebSphere are supported in terms of extended
 * settings (through the detection of their bean property naming conventions).
 * The default ActivationSpec class detection rules may apply to other
 * JMS providers as well.
 *
 * <p>Thanks to Agim Emruli and Laurie Chan for pointing out WebSphere MQ
 * settings and contributing corresponding tests!
 *
 * @author Juergen Hoeller
 * @since 2.5
 * @see #setActivationSpecClass
 */
public class DefaultJmsActivationSpecFactory extends StandardJmsActivationSpecFactory {

	private static final String RESOURCE_ADAPTER_SUFFIX = "ResourceAdapter";

	private static final String RESOURCE_ADAPTER_IMPL_SUFFIX = "ResourceAdapterImpl";

	private static final String ACTIVATION_SPEC_SUFFIX = "ActivationSpec";

	private static final String ACTIVATION_SPEC_IMPL_SUFFIX = "ActivationSpecImpl";


	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());


	/**
	 * This implementation guesses the ActivationSpec class name from the
	 * provider's class name: e.g. "ActiveMQResourceAdapter" ->
	 * "ActiveMQActivationSpec" in the same package, or a class named
	 * "ActivationSpecImpl" in the same package as the ResourceAdapter class.
	 */
	@Override
	protected Class determineActivationSpecClass(ResourceAdapter adapter) {
		String adapterClassName = adapter.getClass().getName();

		if (adapterClassName.endsWith(RESOURCE_ADAPTER_SUFFIX)) {
			// e.g. ActiveMQ
			String providerName =
					adapterClassName.substring(0, adapterClassName.length() - RESOURCE_ADAPTER_SUFFIX.length());
			String specClassName = providerName + ACTIVATION_SPEC_SUFFIX;
			try {
				return adapter.getClass().getClassLoader().loadClass(specClassName);
			}
			catch (ClassNotFoundException ex) {
				logger.debug("No default <Provider>ActivationSpec class found: " + specClassName);
			}
		}

		else if (adapterClassName.endsWith(RESOURCE_ADAPTER_IMPL_SUFFIX)){
			//e.g. WebSphere
			String providerName =
					adapterClassName.substring(0, adapterClassName.length() - RESOURCE_ADAPTER_IMPL_SUFFIX.length());
			String specClassName = providerName + ACTIVATION_SPEC_IMPL_SUFFIX;
			try {
				return adapter.getClass().getClassLoader().loadClass(specClassName);
			}
			catch (ClassNotFoundException ex) {
				logger.debug("No default <Provider>ActivationSpecImpl class found: " + specClassName);
			}
		}

		// e.g. JORAM
		String providerPackage = adapterClassName.substring(0, adapterClassName.lastIndexOf('.') + 1);
		String specClassName = providerPackage + ACTIVATION_SPEC_IMPL_SUFFIX;
		try {
			return adapter.getClass().getClassLoader().loadClass(specClassName);
		}
		catch (ClassNotFoundException ex) {
			logger.debug("No default ActivationSpecImpl class found in provider package: " + specClassName);
		}

		// ActivationSpecImpl class in "inbound" subpackage (WebSphere MQ 6.0.2.1)
		specClassName = providerPackage + "inbound." + ACTIVATION_SPEC_IMPL_SUFFIX;
		try {
			return adapter.getClass().getClassLoader().loadClass(specClassName);
		}
		catch (ClassNotFoundException ex) {
			logger.debug("No default ActivationSpecImpl class found in inbound subpackage: " + specClassName);
		}

		throw new IllegalStateException("No ActivationSpec class defined - " +
				"specify the 'activationSpecClass' property or override the 'determineActivationSpecClass' method");
	}

	/**
	 * This implementation supports Spring's extended "maxConcurrency"
	 * and "prefetchSize" settings through detecting corresponding
	 * ActivationSpec properties: "maxSessions"/"maxNumberOfWorks" and
	 * "maxMessagesPerSessions"/"maxMessages", respectively
	 * (following ActiveMQ's and JORAM's naming conventions).
	 */
	@Override
	protected void populateActivationSpecProperties(BeanWrapper bw, JmsActivationSpecConfig config) {
		super.populateActivationSpecProperties(bw, config);
		if (config.getMaxConcurrency() > 0) {
			if (bw.isWritableProperty("maxSessions")) {
				// ActiveMQ
				bw.setPropertyValue("maxSessions", Integer.toString(config.getMaxConcurrency()));
			}
			else if (bw.isWritableProperty("maxNumberOfWorks")) {
				// JORAM
				bw.setPropertyValue("maxNumberOfWorks", Integer.toString(config.getMaxConcurrency()));
			}
			else if (bw.isWritableProperty("maxConcurrency")){
				// WebSphere
				bw.setPropertyValue("maxConcurrency", Integer.toString(config.getMaxConcurrency()));
			}
		}
		if (config.getPrefetchSize() > 0) {
			if (bw.isWritableProperty("maxMessagesPerSessions")) {
				// ActiveMQ
				bw.setPropertyValue("maxMessagesPerSessions", Integer.toString(config.getPrefetchSize()));
			}
			else if (bw.isWritableProperty("maxMessages")) {
				// JORAM
				bw.setPropertyValue("maxMessages", Integer.toString(config.getPrefetchSize()));
			}
			else if(bw.isWritableProperty("maxBatchSize")){
				// WebSphere
				bw.setPropertyValue("maxBatchSize", Integer.toString(config.getPrefetchSize()));
			}
		}
	}

	/**
	 * This implementation maps {@code SESSION_TRANSACTED} onto an
	 * ActivationSpec property named "useRAManagedTransaction", if available
	 * (following ActiveMQ's naming conventions).
	 */
	@Override
	protected void applyAcknowledgeMode(BeanWrapper bw, int ackMode) {
		if (ackMode == Session.SESSION_TRANSACTED && bw.isWritableProperty("useRAManagedTransaction")) {
			// ActiveMQ
			bw.setPropertyValue("useRAManagedTransaction", "true");
		}
		else {
			super.applyAcknowledgeMode(bw, ackMode);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9292.java