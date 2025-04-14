error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2142.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2142.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 733
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2142.java
text:
```scala
public class ContextLoaderUtilsConfigurationAttributesTests extends AbstractContextConfigurationUtilsTests {

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

p@@ackage org.springframework.test.context.support;

import java.util.List;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.ContextLoaderUtils;

import static org.springframework.test.context.support.ContextLoaderUtils.*;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link ContextLoaderUtils} involving {@link ContextConfigurationAttributes}.
 *
 * @author Sam Brannen
 * @since 3.1
 */
public class ContextLoaderUtilsConfigurationAttributesTests extends AbstractContextLoaderUtilsTests {

	private void assertLocationsFooAttributes(ContextConfigurationAttributes attributes) {
		assertAttributes(attributes, LocationsFoo.class, new String[] { "/foo.xml" }, EMPTY_CLASS_ARRAY,
			ContextLoader.class, false);
	}

	private void assertClassesFooAttributes(ContextConfigurationAttributes attributes) {
		assertAttributes(attributes, ClassesFoo.class, EMPTY_STRING_ARRAY, new Class<?>[] { FooConfig.class },
			ContextLoader.class, false);
	}

	private void assertLocationsBarAttributes(ContextConfigurationAttributes attributes) {
		assertAttributes(attributes, LocationsBar.class, new String[] { "/bar.xml" }, EMPTY_CLASS_ARRAY,
			AnnotationConfigContextLoader.class, true);
	}

	private void assertClassesBarAttributes(ContextConfigurationAttributes attributes) {
		assertAttributes(attributes, ClassesBar.class, EMPTY_STRING_ARRAY, new Class<?>[] { BarConfig.class },
			AnnotationConfigContextLoader.class, true);
	}

	@Test(expected = IllegalStateException.class)
	public void resolveConfigAttributesWithConflictingLocations() {
		resolveContextConfigurationAttributes(ConflictingLocations.class);
	}

	@Test
	public void resolveConfigAttributesWithBareAnnotations() {
		Class<BareAnnotations> testClass = BareAnnotations.class;
		List<ContextConfigurationAttributes> attributesList = resolveContextConfigurationAttributes(testClass);
		assertNotNull(attributesList);
		assertEquals(1, attributesList.size());
		assertAttributes(attributesList.get(0), testClass, EMPTY_STRING_ARRAY, EMPTY_CLASS_ARRAY, ContextLoader.class,
			true);
	}

	@Test
	public void resolveConfigAttributesWithLocalAnnotationAndLocations() {
		List<ContextConfigurationAttributes> attributesList = resolveContextConfigurationAttributes(LocationsFoo.class);
		assertNotNull(attributesList);
		assertEquals(1, attributesList.size());
		assertLocationsFooAttributes(attributesList.get(0));
	}

	@Test
	public void resolveConfigAttributesWithMetaAnnotationAndLocations() {
		Class<MetaLocationsFoo> testClass = MetaLocationsFoo.class;
		List<ContextConfigurationAttributes> attributesList = resolveContextConfigurationAttributes(testClass);
		assertNotNull(attributesList);
		assertEquals(1, attributesList.size());
		assertAttributes(attributesList.get(0), testClass, new String[] { "/foo.xml" }, EMPTY_CLASS_ARRAY,
			ContextLoader.class, true);
	}

	@Test
	public void resolveConfigAttributesWithMetaAnnotationAndLocationsAndOverrides() {
		Class<MetaLocationsFooWithOverrides> testClass = MetaLocationsFooWithOverrides.class;
		List<ContextConfigurationAttributes> attributesList = resolveContextConfigurationAttributes(testClass);
		assertNotNull(attributesList);
		assertEquals(1, attributesList.size());
		assertAttributes(attributesList.get(0), testClass, new String[] { "/foo.xml" }, EMPTY_CLASS_ARRAY,
			ContextLoader.class, true);
	}

	@Test
	public void resolveConfigAttributesWithMetaAnnotationAndLocationsAndOverriddenAttributes() {
		Class<MetaLocationsFooWithOverriddenAttributes> testClass = MetaLocationsFooWithOverriddenAttributes.class;
		List<ContextConfigurationAttributes> attributesList = resolveContextConfigurationAttributes(testClass);
		assertNotNull(attributesList);
		assertEquals(1, attributesList.size());
		assertAttributes(attributesList.get(0), testClass, new String[] { "foo1.xml", "foo2.xml" }, EMPTY_CLASS_ARRAY,
			ContextLoader.class, true);
	}

	@Test
	public void resolveConfigAttributesWithMetaAnnotationAndLocationsInClassHierarchy() {
		Class<MetaLocationsBar> testClass = MetaLocationsBar.class;
		List<ContextConfigurationAttributes> attributesList = resolveContextConfigurationAttributes(testClass);
		assertNotNull(attributesList);
		assertEquals(2, attributesList.size());
		assertAttributes(attributesList.get(0), testClass, new String[] { "/bar.xml" }, EMPTY_CLASS_ARRAY,
			ContextLoader.class, true);
		assertAttributes(attributesList.get(1), MetaLocationsFoo.class, new String[] { "/foo.xml" },
			EMPTY_CLASS_ARRAY, ContextLoader.class, true);
	}

	@Test
	public void resolveConfigAttributesWithLocalAnnotationAndClasses() {
		List<ContextConfigurationAttributes> attributesList = resolveContextConfigurationAttributes(ClassesFoo.class);
		assertNotNull(attributesList);
		assertEquals(1, attributesList.size());
		assertClassesFooAttributes(attributesList.get(0));
	}

	@Test
	public void resolveConfigAttributesWithLocalAndInheritedAnnotationsAndLocations() {
		List<ContextConfigurationAttributes> attributesList = resolveContextConfigurationAttributes(LocationsBar.class);
		assertNotNull(attributesList);
		assertEquals(2, attributesList.size());
		assertLocationsBarAttributes(attributesList.get(0));
		assertLocationsFooAttributes(attributesList.get(1));
	}

	@Test
	public void resolveConfigAttributesWithLocalAndInheritedAnnotationsAndClasses() {
		List<ContextConfigurationAttributes> attributesList = resolveContextConfigurationAttributes(ClassesBar.class);
		assertNotNull(attributesList);
		assertEquals(2, attributesList.size());
		assertClassesBarAttributes(attributesList.get(0));
		assertClassesFooAttributes(attributesList.get(1));
	}

	/**
	 * Verifies change requested in <a href="https://jira.spring.io/browse/SPR-11634">SPR-11634</a>.
	 * @since 4.0.4
	 */
	@Test
	public void resolveConfigAttributesWithLocationsAndClasses() {
		List<ContextConfigurationAttributes> attributesList = resolveContextConfigurationAttributes(LocationsAndClasses.class);
		assertNotNull(attributesList);
		assertEquals(1, attributesList.size());
	}


	// -------------------------------------------------------------------------

	@ContextConfiguration(value = "x", locations = "y")
	private static class ConflictingLocations {
	}

	@ContextConfiguration(locations = "x", classes = Object.class)
	private static class LocationsAndClasses {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2142.java