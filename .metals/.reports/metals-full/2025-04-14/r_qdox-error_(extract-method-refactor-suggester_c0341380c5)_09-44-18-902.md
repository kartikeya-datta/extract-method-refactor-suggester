error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12336.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12336.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 680
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12336.java
text:
```scala
public class RequiredAnnotationBeanPostProcessorTests {

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

p@@ackage org.springframework.beans.factory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import static org.junit.Assert.*;

/**
 * @author Rob Harrop
 * @author Chris Beams
 * @since 2.0
 */
public final class RequiredAnnotationBeanPostProcessorTests {

	@Test
	public void testWithRequiredPropertyOmitted() {
		try {
			DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
			BeanDefinition beanDef = BeanDefinitionBuilder
				.genericBeanDefinition(RequiredTestBean.class)
				.addPropertyValue("name", "Rob Harrop")
				.addPropertyValue("favouriteColour", "Blue")
				.addPropertyValue("jobTitle", "Grand Poobah")
				.getBeanDefinition();
			factory.registerBeanDefinition("testBean", beanDef);
			factory.addBeanPostProcessor(new RequiredAnnotationBeanPostProcessor());
			factory.preInstantiateSingletons();
			fail("Should have thrown BeanCreationException");
		}
		catch (BeanCreationException ex) {
			String message = ex.getCause().getMessage();
			assertTrue(message.contains("Property"));
			assertTrue(message.contains("age"));
			assertTrue(message.contains("testBean"));
		}
	}

	@Test
	public void testWithThreeRequiredPropertiesOmitted() {
		try {
			DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
			BeanDefinition beanDef = BeanDefinitionBuilder
				.genericBeanDefinition(RequiredTestBean.class)
				.addPropertyValue("name", "Rob Harrop")
				.getBeanDefinition();
			factory.registerBeanDefinition("testBean", beanDef);
			factory.addBeanPostProcessor(new RequiredAnnotationBeanPostProcessor());
			factory.preInstantiateSingletons();
			fail("Should have thrown BeanCreationException");
		}
		catch (BeanCreationException ex) {
			String message = ex.getCause().getMessage();
			assertTrue(message.contains("Properties"));
			assertTrue(message.contains("age"));
			assertTrue(message.contains("favouriteColour"));
			assertTrue(message.contains("jobTitle"));
			assertTrue(message.contains("testBean"));
		}
	}

	@Test
	public void testWithAllRequiredPropertiesSpecified() {
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		BeanDefinition beanDef = BeanDefinitionBuilder
			.genericBeanDefinition(RequiredTestBean.class)
			.addPropertyValue("age", "24")
			.addPropertyValue("favouriteColour", "Blue")
			.addPropertyValue("jobTitle", "Grand Poobah")
			.getBeanDefinition();
		factory.registerBeanDefinition("testBean", beanDef);
		factory.addBeanPostProcessor(new RequiredAnnotationBeanPostProcessor());
		factory.preInstantiateSingletons();
		RequiredTestBean bean = (RequiredTestBean) factory.getBean("testBean");
		assertEquals(24, bean.getAge());
		assertEquals("Blue", bean.getFavouriteColour());
	}

	@Test
	public void testWithCustomAnnotation() {
		try {
			DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
			BeanDefinition beanDef = BeanDefinitionBuilder
				.genericBeanDefinition(RequiredTestBean.class)
				.getBeanDefinition();
			factory.registerBeanDefinition("testBean", beanDef);
			RequiredAnnotationBeanPostProcessor rabpp = new RequiredAnnotationBeanPostProcessor();
			rabpp.setRequiredAnnotationType(MyRequired.class);
			factory.addBeanPostProcessor(rabpp);
			factory.preInstantiateSingletons();
			fail("Should have thrown BeanCreationException");
		}
		catch (BeanCreationException ex) {
			String message = ex.getCause().getMessage();
			assertTrue(message.contains("Property"));
			assertTrue(message.contains("name"));
			assertTrue(message.contains("testBean"));
		}
	}

	@Test
	public void testWithStaticFactoryMethod() {
		try {
			DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
			BeanDefinition beanDef = BeanDefinitionBuilder
					.genericBeanDefinition(RequiredTestBean.class)
					.setFactoryMethod("create")
					.addPropertyValue("name", "Rob Harrop")
					.addPropertyValue("favouriteColour", "Blue")
					.addPropertyValue("jobTitle", "Grand Poobah")
					.getBeanDefinition();
			factory.registerBeanDefinition("testBean", beanDef);
			factory.addBeanPostProcessor(new RequiredAnnotationBeanPostProcessor());
			factory.preInstantiateSingletons();
			fail("Should have thrown BeanCreationException");
		}
		catch (BeanCreationException ex) {
			String message = ex.getCause().getMessage();
			assertTrue(message.contains("Property"));
			assertTrue(message.contains("age"));
			assertTrue(message.contains("testBean"));
		}
	}

	@Test
	public void testWithStaticFactoryMethodAndRequiredPropertiesSpecified() {
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		BeanDefinition beanDef = BeanDefinitionBuilder
				.genericBeanDefinition(RequiredTestBean.class)
				.setFactoryMethod("create")
				.addPropertyValue("age", "24")
				.addPropertyValue("favouriteColour", "Blue")
				.addPropertyValue("jobTitle", "Grand Poobah")
				.getBeanDefinition();
		factory.registerBeanDefinition("testBean", beanDef);
		factory.addBeanPostProcessor(new RequiredAnnotationBeanPostProcessor());
		factory.preInstantiateSingletons();
		RequiredTestBean bean = (RequiredTestBean) factory.getBean("testBean");
		assertEquals(24, bean.getAge());
		assertEquals("Blue", bean.getFavouriteColour());
	}

	@Test
	public void testWithFactoryBean() {
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		RootBeanDefinition beanDef = new RootBeanDefinition(RequiredTestBean.class);
		beanDef.setFactoryBeanName("testBeanFactory");
		beanDef.setFactoryMethodName("create");
		factory.registerBeanDefinition("testBean", beanDef);
		factory.registerBeanDefinition("testBeanFactory", new RootBeanDefinition(RequiredTestBeanFactory.class));
		RequiredAnnotationBeanPostProcessor bpp = new RequiredAnnotationBeanPostProcessor();
		bpp.setBeanFactory(factory);
		factory.addBeanPostProcessor(bpp);
		factory.preInstantiateSingletons();
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface MyRequired {
	}


	public static class RequiredTestBean implements BeanNameAware, BeanFactoryAware {

		private String name;

		private int age;

		private String favouriteColour;

		private String jobTitle;


		public int getAge() {
			return age;
		}

		@Required
		public void setAge(int age) {
			this.age = age;
		}

		public String getName() {
			return name;
		}

		@MyRequired
		public void setName(String name) {
			this.name = name;
		}

		public String getFavouriteColour() {
			return favouriteColour;
		}

		@Required
		public void setFavouriteColour(String favouriteColour) {
			this.favouriteColour = favouriteColour;
		}

		public String getJobTitle() {
			return jobTitle;
		}

		@Required
		public void setJobTitle(String jobTitle) {
			this.jobTitle = jobTitle;
		}

		@Override
		@Required
		public void setBeanName(String name) {
		}

		@Override
		@Required
		public void setBeanFactory(BeanFactory beanFactory) {
		}

		public static RequiredTestBean create() {
			return new RequiredTestBean();
		}
	}


	public static class RequiredTestBeanFactory {

		public RequiredTestBean create() {
			return new RequiredTestBean();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12336.java