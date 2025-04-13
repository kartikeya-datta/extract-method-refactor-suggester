error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1791.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1791.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1791.java
text:
```scala
r@@eturn new ConfigurationParser(this.getProblemReporter(), beanFactory.getBeanClassLoader());

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
package org.springframework.config.java.support;

import static java.lang.String.*;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.config.java.Bean;
import org.springframework.config.java.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


/**
 * {@link BeanFactoryPostProcessor} used for bootstrapping processing of
 * {@link Configuration @Configuration} classes.
 * <p>
 * Registered by default when using {@literal <context:annotation-config/>} or
 * {@literal <context:component-scan/>}. Otherwise, may be declared manually as
 * with any other BeanFactoryPostProcessor.
 * <p>
 * This post processor is {@link Ordered#HIGHEST_PRECEDENCE} as it's important
 * that any {@link Bean} methods declared in Configuration classes have their
 * respective bean definitions registered before any other BeanFactoryPostProcessor
 * executes.
 * 
 * @author Chris Beams
 * @since 3.0
 */
public class ConfigurationClassPostProcessor extends AbstractConfigurationClassProcessor
                                             implements Ordered, BeanFactoryPostProcessor {

	private static final Log logger = LogFactory.getLog(ConfigurationClassPostProcessor.class);

	/**
	 * A well-known class in the CGLIB API used when testing to see if CGLIB
	 * is present on the classpath. Package-private visibility allows for
	 * manipulation by tests.
	 * @see #assertCglibIsPresent(BeanDefinitionRegistry)
	 */
	static String CGLIB_TEST_CLASS = "net.sf.cglib.proxy.Callback";

	/**
	 * Holder for the calling BeanFactory
	 * @see #postProcessBeanFactory(ConfigurableListableBeanFactory)
	 */
	private DefaultListableBeanFactory beanFactory;


	/**
	 * @return {@link Ordered#HIGHEST_PRECEDENCE}.
	 */
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	/**
	 * Finds {@link Configuration} bean definitions within <var>clBeanFactory</var>
	 * and processes them in order to register bean definitions for each Bean method
	 * found within; also prepares the the Configuration classes for servicing
	 * bean requests at runtime by replacing them with CGLIB-enhanced subclasses.
	 */
	public void postProcessBeanFactory(ConfigurableListableBeanFactory clBeanFactory) throws BeansException {
		Assert.isInstanceOf(DefaultListableBeanFactory.class, clBeanFactory);
		beanFactory = (DefaultListableBeanFactory) clBeanFactory;

		BeanDefinitionRegistry factoryBeanDefs = processConfigBeanDefinitions();

		for(String beanName : factoryBeanDefs.getBeanDefinitionNames())
			beanFactory.registerBeanDefinition(beanName, factoryBeanDefs.getBeanDefinition(beanName));

		enhanceConfigurationClasses();
	}

	/**
	 * @return a ConfigurationParser that uses the enclosing BeanFactory's
	 * ClassLoader to load all Configuration class artifacts.
	 */
	@Override
	protected ConfigurationParser createConfigurationParser() {
		return new ConfigurationParser(beanFactory.getBeanClassLoader());
	}

	/**
	 * @return map of all non-abstract {@link BeanDefinition}s in the
	 * enclosing {@link #beanFactory}
	 */
	@Override
	protected BeanDefinitionRegistry getConfigurationBeanDefinitions(boolean includeAbstractBeanDefs) {

		BeanDefinitionRegistry configBeanDefs = new DefaultListableBeanFactory();

		for (String beanName : beanFactory.getBeanDefinitionNames()) {
			BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);

			if (beanDef.isAbstract() && !includeAbstractBeanDefs)
				continue;

			if (isConfigClass(beanDef))
				configBeanDefs.registerBeanDefinition(beanName, beanDef);
		}

		return configBeanDefs;
	}

	/**
	 * Validates the given <var>model</var>. Any problems found are delegated
	 * to {@link #getProblemReporter()}.
	 */
	@Override
	protected void validateModel(ConfigurationModel model) {
		model.validate(this.getProblemReporter());
	}

	/**
	 * Post-processes a BeanFactory in search of Configuration class BeanDefinitions; any
	 * candidates are then enhanced by a {@link ConfigurationEnhancer}. Candidate status is
	 * determined by BeanDefinition attribute metadata.
	 * 
	 * @author Chris Beams
	 * @see ConfigurationEnhancer
	 * @see BeanFactoryPostProcessor
	 */
	private void enhanceConfigurationClasses() {

		BeanDefinitionRegistry configBeanDefs = getConfigurationBeanDefinitions(true);

		assertCglibIsPresent(configBeanDefs);

		ConfigurationEnhancer enhancer = new ConfigurationEnhancer(beanFactory);

		for(String beanName : configBeanDefs.getBeanDefinitionNames()) {
			BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
			String configClassName = beanDef.getBeanClassName();
			String enhancedClassName = enhancer.enhance(configClassName);

			if (logger.isDebugEnabled())
				logger.debug(format("Replacing bean definition '%s' existing class name '%s' "
				                  + "with enhanced class name '%s'", beanName, configClassName, enhancedClassName));

			beanDef.setBeanClassName(enhancedClassName);
		}
	}

	/**
	 * Tests for the presence of CGLIB on the classpath by trying to
	 * classload {@link #CGLIB_TEST_CLASS}.
	 */
	private void assertCglibIsPresent(BeanDefinitionRegistry configBeanDefs) {
		try {
			Class.forName(CGLIB_TEST_CLASS);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("CGLIB is required to process @Configuration classes. " +
					"Either add CGLIB v2.2.3 to the classpath or remove the following " +
					"@Configuration bean definitions: ["
					+ StringUtils.arrayToCommaDelimitedString(configBeanDefs.getBeanDefinitionNames()) + "]");
		}
	}

	/**
	 * @return whether the BeanDefinition's beanClass is Configuration-annotated,
	 * false if no beanClass is specified.
	 */
	private static boolean isConfigClass(BeanDefinition beanDef) {

		String className = beanDef.getBeanClassName();

		if(className == null)
			return false;

		try {
			MetadataReader metadataReader = new SimpleMetadataReaderFactory().getMetadataReader(className);
			AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
			return annotationMetadata.hasAnnotation(Configuration.class.getName());
		} catch (IOException ex) {
			throw new RuntimeException(ex); 
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1791.java