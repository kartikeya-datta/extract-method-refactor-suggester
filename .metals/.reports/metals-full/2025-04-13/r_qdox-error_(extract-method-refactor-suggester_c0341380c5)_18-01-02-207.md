error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6887.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6887.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[18,1]

error in qdox parser
file content:
```java
offset: 675
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6887.java
text:
```scala
public class ConfigurationClassPostProcessorTests {

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
p@@ackage org.springframework.context.annotation;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.*;

import java.lang.reflect.Field;
import java.util.Vector;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.util.ClassUtils;


/**
 * Unit tests for {@link ConfigurationClassPostProcessor}.
 *
 * @author Chris Beams
 */
public class ConfigurationPostProcessorTests {

	private static final String ORIG_CGLIB_TEST_CLASS = ConfigurationClassPostProcessor.CGLIB_TEST_CLASS;
	private static final String BOGUS_CGLIB_TEST_CLASS = "a.bogus.class";

	/**
	 * CGLIB is an optional dependency for Spring.  If users attempt
	 * to use {@link Configuration} classes, they'll need it on the classpath;
	 * if Configuration classes are present in the bean factory and CGLIB
	 * is not present, an instructive exception should be thrown.
	 */
	@Test
	public void testFailFastIfCglibNotPresent() {
		@Configuration class Config { }

		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		factory.registerBeanDefinition("config", rootBeanDefinition(Config.class).getBeanDefinition());
		ConfigurationClassPostProcessor cpp = new ConfigurationClassPostProcessor();

		// temporarily set the cglib test class to something bogus
		ConfigurationClassPostProcessor.CGLIB_TEST_CLASS = BOGUS_CGLIB_TEST_CLASS;

		try {
			cpp.postProcessBeanFactory(factory);
		} catch (RuntimeException ex) {
			assertTrue(ex.getMessage().contains("CGLIB is required to process @Configuration classes"));
		} finally {
			ConfigurationClassPostProcessor.CGLIB_TEST_CLASS = ORIG_CGLIB_TEST_CLASS;
		}
	}

	/**
	 * In order to keep Spring's footprint as small as possible, CGLIB must
	 * not be required on the classpath unless the user is taking advantage
	 * of {@link Configuration} classes.
	 * 
	 * This test will fail if any CGLIB classes are classloaded before the call
	 * to {@link ConfigurationClassPostProcessor#enhanceConfigurationClasses}
	 */
	@Ignore @Test // because classloader hacking below causes extremely hard to
	              // debug downstream side effects. Re-enable at will to verify
	              // CGLIB is not prematurely classloaded, but this technique is
	              // not stable enough to leave enabled.
	public void testCglibClassesAreLoadedJustInTimeForEnhancement() throws Exception {
		ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		Field classesField = ClassLoader.class.getDeclaredField("classes");
		classesField.setAccessible(true);

		// first, remove any CGLIB classes that may have been loaded by other tests
		@SuppressWarnings("unchecked")
		Vector<Class<?>> classes = (Vector<Class<?>>) classesField.get(classLoader);

		Vector<Class<?>> cglibClassesAlreadyLoaded = new Vector<Class<?>>();
		for(Class<?> loadedClass : classes)
			if(loadedClass.getName().startsWith("net.sf.cglib"))
				cglibClassesAlreadyLoaded.add(loadedClass);

		for(Class<?> cglibClass : cglibClassesAlreadyLoaded)
			classes.remove(cglibClass);

		// now, execute a scenario where everything except enhancement occurs
		// -- no CGLIB classes should get loaded!
		testFailFastIfCglibNotPresent();

		// test to ensure that indeed no CGLIB classes have been loaded
		for(Class<?> loadedClass : classes)
			if(loadedClass.getName().startsWith("net.sf.cglib"))
				fail("CGLIB class should not have been eagerly loaded: " + loadedClass.getName());
	}

	/**
	 * Enhanced {@link Configuration} classes are only necessary for respecting
	 * certain bean semantics, like singleton-scoping, scoped proxies, etc.
	 * 
	 * Technically, {@link ConfigurationClassPostProcessor} could fail to enhance the
	 * registered Configuration classes and many use cases would still work.
	 * Certain cases, however, like inter-bean singleton references would not.
	 * We test for such a case below, and in doing so prove that enhancement is
	 * working.
	 */
	@Test
	public void testEnhancementIsPresentBecauseSingletonSemanticsAreRespected() {
		DefaultListableBeanFactory beanFactory = new  DefaultListableBeanFactory();
		beanFactory.registerBeanDefinition("config",
				rootBeanDefinition(SingletonBeanConfig.class).getBeanDefinition());
		new ConfigurationClassPostProcessor().postProcessBeanFactory(beanFactory);
		Foo foo = (Foo) beanFactory.getBean("foo");
		Bar bar = (Bar) beanFactory.getBean("bar");
		assertThat(foo, sameInstance(bar.foo));
	}

	@Configuration
	static class SingletonBeanConfig {
		public @Bean Foo foo() {
			return new Foo();
		}

		public @Bean Bar bar() {
			return new Bar(foo());
		}
	}

	static class Foo { }
	static class Bar {
		final Foo foo;
		public Bar(Foo foo) { this.foo = foo; }
	}

	/**
	 * Tests the fix for SPR-5655, a special workaround that prefers reflection
	 * over ASM if a bean class is already loaded.
	 */
	@Test
	public void testAlreadyLoadedConfigurationClasses() {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.registerBeanDefinition("unloadedConfig",
				rootBeanDefinition(UnloadedConfig.class.getName()).getBeanDefinition());
		beanFactory.registerBeanDefinition("loadedConfig",
				rootBeanDefinition(LoadedConfig.class).getBeanDefinition());
		new ConfigurationClassPostProcessor() .postProcessBeanFactory(beanFactory);
		beanFactory.getBean("foo");
		beanFactory.getBean("bar");
	}

	@Configuration
	static class UnloadedConfig {
		public @Bean Foo foo() {
			return new Foo();
		}
	}

	@Configuration
	static class LoadedConfig {
		public @Bean Bar bar() {
			return new Bar(new Foo());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6887.java