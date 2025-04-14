error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15.java
text:
```scala
f@@or (BeanMethod method : configClass.getBeanMethods())

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.config.java.Bean;
import org.springframework.config.java.Configuration;
import org.springframework.core.io.Resource;


/**
 * Reads a given fully-populated {@link ConfigurationModel}, registering bean definitions
 * with the given {@link BeanDefinitionRegistry} based on its contents.
 * <p>
 * This class was modeled after the {@link BeanDefinitionReader} hierarchy, but does not
 * implement/extend any of its artifacts as {@link ConfigurationModel} is not a
 * {@link Resource}.
 * 
 * @author Chris Beams
 */
class ConfigurationModelBeanDefinitionReader {

	private static final Log log = LogFactory.getLog(ConfigurationModelBeanDefinitionReader.class);

	private BeanDefinitionRegistry registry;


	/**
	 * Creates a new {@link ConfigurationModelBeanDefinitionReader} instance.
	 */
	public ConfigurationModelBeanDefinitionReader() {
	}

	/**
	 * Reads {@code model}, registering bean definitions with {@link #registry} based on
	 * its contents.
	 * 
	 * @return number of bean definitions generated
	 */
	public BeanDefinitionRegistry loadBeanDefinitions(ConfigurationModel model) {
		registry = new SimpleBeanDefinitionRegistry();
		
		for (ConfigurationClass configClass : model.getAllConfigurationClasses())
			loadBeanDefinitionsForConfigurationClass(configClass);

		return registry;
	}

	/**
	 * Reads a particular {@link ConfigurationClass}, registering bean definitions for the
	 * class itself, all its {@link Bean} methods
	 */
	private void loadBeanDefinitionsForConfigurationClass(ConfigurationClass configClass) {
		doLoadBeanDefinitionForConfigurationClass(configClass);

		for (BeanMethod method : configClass.getMethods())
			loadBeanDefinitionsForModelMethod(method);

//		Annotation[] pluginAnnotations = configClass.getPluginAnnotations();
//		Arrays.sort(pluginAnnotations, new PluginComparator());
//		for (Annotation annotation : pluginAnnotations)
//			loadBeanDefinitionsForExtensionAnnotation(beanDefs, annotation);
	}

	/**
	 * Registers the {@link Configuration} class itself as a bean definition.
	 * @param beanDefs 
	 */
	private void doLoadBeanDefinitionForConfigurationClass(ConfigurationClass configClass) {

		GenericBeanDefinition configBeanDef = new GenericBeanDefinition();
		configBeanDef.setBeanClassName(configClass.getName());

		String configBeanName = configClass.getBeanName();

		// consider the case where it's already been defined (probably in XML)
		// and potentially has PropertyValues and ConstructorArgs)
		if (registry.containsBeanDefinition(configBeanName)) {
			if (log.isInfoEnabled())
				log.info(format("Copying property and constructor arg values from existing bean definition for "
				              + "@Configuration class %s to new bean definition", configBeanName));
			AbstractBeanDefinition existing = (AbstractBeanDefinition) registry.getBeanDefinition(configBeanName);
			configBeanDef.setPropertyValues(existing.getPropertyValues());
			configBeanDef.setConstructorArgumentValues(existing.getConstructorArgumentValues());
			configBeanDef.setResource(existing.getResource());
		}

		if (log.isInfoEnabled())
			log.info(format("Registering bean definition for @Configuration class %s", configBeanName));

		registry.registerBeanDefinition(configBeanName, configBeanDef);
	}

	/**
	 * Reads a particular {@link BeanMethod}, registering bean definitions with
	 * {@link #registry} based on its contents.
	 */
	private void loadBeanDefinitionsForModelMethod(BeanMethod method) {
		new BeanRegistrar().register(method, registry);
	}

//	@SuppressWarnings("unchecked")
//	private void loadBeanDefinitionsForExtensionAnnotation(Map<String, BeanDefinition> beanDefs, Annotation anno) {
//		// ExtensionAnnotationUtils.getRegistrarFor(anno).registerBeanDefinitionsWith(beanFactory);
//		// there is a fixed assumption that in order for this annotation to have
//		// been registered in the first place, it must be meta-annotated with @Plugin
//		// assert this as an invariant now
//		Class<?> annoClass = anno.getClass();
//		Extension extensionAnno = AnnotationUtils.findAnnotation(annoClass, Extension.class);
//		Assert.isTrue(extensionAnno != null, format("%s annotation is not annotated as a @%s", annoClass,
//		        Extension.class.getSimpleName()));
//
//		Class<? extends ExtensionAnnotationBeanDefinitionRegistrar> extHandlerClass = extensionAnno.handler();
//
//		ExtensionAnnotationBeanDefinitionRegistrar extHandler = getInstance(extHandlerClass);
//		extHandler.handle(anno, beanFactory);
//	}
//
//	private static class PluginComparator implements Comparator<Annotation> {
//		public int compare(Annotation a1, Annotation a2) {
//			Integer i1 = getOrder(a1);
//			Integer i2 = getOrder(a2);
//			return i1.compareTo(i2);
//		}
//
//		private Integer getOrder(Annotation a) {
//			Extension plugin = a.annotationType().getAnnotation(Extension.class);
//			if (plugin == null)
//				throw new IllegalArgumentException("annotation was not annotated with @Plugin: "
//				        + a.annotationType());
//			return plugin.order();
//		}
//	}

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15.java