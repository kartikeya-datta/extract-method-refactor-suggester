error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5518.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5518.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5518.java
text:
```scala
n@@ew String[] { "classpath:org/springframework/test/context/support/AbstractContextConfigurationUtilsTests$BareAnnotations-context.xml" },

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

package org.springframework.test.context.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.web.WebDelegatingSmartContextLoader;
import org.springframework.test.context.web.WebMergedContextConfiguration;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link BootstrapTestUtils} involving {@link MergedContextConfiguration}.
 *
 * @author Sam Brannen
 * @since 3.1
 */
public class BootstrapTestUtilsMergedConfigTests extends AbstractContextConfigurationUtilsTests {

	@Test
	public void buildMergedConfigWithoutAnnotation() {
		Class<?> testClass = Enigma.class;
		MergedContextConfiguration mergedConfig = buildMergedContextConfiguration(testClass);

		assertMergedConfig(mergedConfig, testClass, EMPTY_STRING_ARRAY, EMPTY_CLASS_ARRAY, null);
	}

	@Test
	public void buildMergedConfigWithBareAnnotations() {
		Class<?> testClass = BareAnnotations.class;
		MergedContextConfiguration mergedConfig = buildMergedContextConfiguration(testClass);

		assertMergedConfig(
			mergedConfig,
			testClass,
			new String[] { "classpath:org/springframework/test/context/support/AbstractContextLoaderUtilsTests$BareAnnotations-context.xml" },
			EMPTY_CLASS_ARRAY, DelegatingSmartContextLoader.class);
	}

	@Test
	public void buildMergedConfigWithLocalAnnotationAndLocations() {
		Class<?> testClass = LocationsFoo.class;
		MergedContextConfiguration mergedConfig = buildMergedContextConfiguration(testClass);

		assertMergedConfig(mergedConfig, testClass, new String[] { "classpath:/foo.xml" }, EMPTY_CLASS_ARRAY,
			DelegatingSmartContextLoader.class);
	}

	@Test
	public void buildMergedConfigWithMetaAnnotationAndLocations() {
		Class<?> testClass = MetaLocationsFoo.class;
		MergedContextConfiguration mergedConfig = buildMergedContextConfiguration(testClass);

		assertMergedConfig(mergedConfig, testClass, new String[] { "classpath:/foo.xml" }, EMPTY_CLASS_ARRAY,
			DelegatingSmartContextLoader.class);
	}

	@Test
	public void buildMergedConfigWithMetaAnnotationAndClasses() {
		buildMergedConfigWithMetaAnnotationAndClasses(Dog.class);
		buildMergedConfigWithMetaAnnotationAndClasses(WorkingDog.class);
		buildMergedConfigWithMetaAnnotationAndClasses(GermanShepherd.class);
	}

	private void buildMergedConfigWithMetaAnnotationAndClasses(Class<?> testClass) {
		MergedContextConfiguration mergedConfig = buildMergedContextConfiguration(testClass);
		assertMergedConfig(mergedConfig, testClass, EMPTY_STRING_ARRAY, new Class<?>[] { FooConfig.class,
			BarConfig.class }, DelegatingSmartContextLoader.class);
	}

	@Test
	public void buildMergedConfigWithLocalAnnotationAndClasses() {
		Class<?> testClass = ClassesFoo.class;
		MergedContextConfiguration mergedConfig = buildMergedContextConfiguration(testClass);

		assertMergedConfig(mergedConfig, testClass, EMPTY_STRING_ARRAY, new Class<?>[] { FooConfig.class },
			DelegatingSmartContextLoader.class);
	}

	/**
	 * Introduced to investigate claims made in a discussion on
	 * <a href="http://stackoverflow.com/questions/24725438/what-could-cause-a-class-implementing-applicationlistenercontextrefreshedevent">Stack Overflow</a>.
	 */
	@Test
	public void buildMergedConfigWithAtWebAppConfigurationWithAnnotationAndClassesOnSuperclass() {
		Class<?> webTestClass = WebClassesFoo.class;
		Class<?> standardTestClass = ClassesFoo.class;
		WebMergedContextConfiguration webMergedConfig = (WebMergedContextConfiguration) buildMergedContextConfiguration(webTestClass);
		MergedContextConfiguration standardMergedConfig = buildMergedContextConfiguration(standardTestClass);

		assertEquals(webMergedConfig, webMergedConfig);
		assertEquals(standardMergedConfig, standardMergedConfig);
		assertNotEquals(standardMergedConfig, webMergedConfig);
		assertNotEquals(webMergedConfig, standardMergedConfig);

		assertMergedConfig(webMergedConfig, webTestClass, EMPTY_STRING_ARRAY, new Class<?>[] { FooConfig.class },
			WebDelegatingSmartContextLoader.class);
		assertMergedConfig(standardMergedConfig, standardTestClass, EMPTY_STRING_ARRAY,
			new Class<?>[] { FooConfig.class }, DelegatingSmartContextLoader.class);
	}

	@Test
	public void buildMergedConfigWithLocalAnnotationAndOverriddenContextLoaderAndLocations() {
		Class<?> testClass = PropertiesLocationsFoo.class;
		Class<? extends ContextLoader> expectedContextLoaderClass = GenericPropertiesContextLoader.class;
		MergedContextConfiguration mergedConfig = buildMergedContextConfiguration(testClass);

		assertMergedConfig(mergedConfig, testClass, new String[] { "classpath:/foo.properties" }, EMPTY_CLASS_ARRAY,
			expectedContextLoaderClass);
	}

	@Test
	public void buildMergedConfigWithLocalAnnotationAndOverriddenContextLoaderAndClasses() {
		Class<?> testClass = PropertiesClassesFoo.class;
		Class<? extends ContextLoader> expectedContextLoaderClass = GenericPropertiesContextLoader.class;
		MergedContextConfiguration mergedConfig = buildMergedContextConfiguration(testClass);

		assertMergedConfig(mergedConfig, testClass, EMPTY_STRING_ARRAY, new Class<?>[] { FooConfig.class },
			expectedContextLoaderClass);
	}

	@Test
	public void buildMergedConfigWithLocalAndInheritedAnnotationsAndLocations() {
		Class<?> testClass = LocationsBar.class;
		String[] expectedLocations = new String[] { "/foo.xml", "/bar.xml" };
		MergedContextConfiguration mergedConfig = buildMergedContextConfiguration(testClass);

		assertMergedConfig(mergedConfig, testClass, expectedLocations, EMPTY_CLASS_ARRAY,
			AnnotationConfigContextLoader.class);
	}

	@Test
	public void buildMergedConfigWithLocalAndInheritedAnnotationsAndClasses() {
		Class<?> testClass = ClassesBar.class;
		Class<?>[] expectedClasses = new Class<?>[] { FooConfig.class, BarConfig.class };
		MergedContextConfiguration mergedConfig = buildMergedContextConfiguration(testClass);

		assertMergedConfig(mergedConfig, testClass, EMPTY_STRING_ARRAY, expectedClasses,
			AnnotationConfigContextLoader.class);
	}

	@Test
	public void buildMergedConfigWithAnnotationsAndOverriddenLocations() {
		Class<?> testClass = OverriddenLocationsBar.class;
		String[] expectedLocations = new String[] { "/bar.xml" };
		MergedContextConfiguration mergedConfig = buildMergedContextConfiguration(testClass);

		assertMergedConfig(mergedConfig, testClass, expectedLocations, EMPTY_CLASS_ARRAY,
			AnnotationConfigContextLoader.class);
	}

	@Test
	public void buildMergedConfigWithAnnotationsAndOverriddenClasses() {
		Class<?> testClass = OverriddenClassesBar.class;
		Class<?>[] expectedClasses = new Class<?>[] { BarConfig.class };
		MergedContextConfiguration mergedConfig = buildMergedContextConfiguration(testClass);

		assertMergedConfig(mergedConfig, testClass, EMPTY_STRING_ARRAY, expectedClasses,
			AnnotationConfigContextLoader.class);
	}


	@ContextConfiguration
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface SpringAppConfig {

		Class<?>[] classes() default {};
	}

	@SpringAppConfig(classes = { FooConfig.class, BarConfig.class })
	public static abstract class Dog {
	}

	public static abstract class WorkingDog extends Dog {
	}

	public static class GermanShepherd extends WorkingDog {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5518.java