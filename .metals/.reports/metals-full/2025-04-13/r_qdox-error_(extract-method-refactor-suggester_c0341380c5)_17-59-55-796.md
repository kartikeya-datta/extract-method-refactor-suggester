error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2141.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2141.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 711
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2141.java
text:
```scala
public class ActiveProfilesUtilsTests extends AbstractContextConfigurationUtilsTests {

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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ActiveProfilesResolver;

import static org.junit.Assert.*;
import static org.springframework.test.context.support.ActiveProfilesUtils.*;

/**
 * Unit tests for {@link ActiveProfilesUtils} involving resolution of active bean
 * definition profiles.
 *
 * @author Sam Brannen
 * @author Michail Nikolaev
 * @since 3.1
 */
public class ActiveProfilesUtilsTests extends AbstractContextLoaderUtilsTests {

	private void assertResolvedProfiles(Class<?> testClass, String... expected) {
		assertNotNull(testClass);
		assertNotNull(expected);
		String[] actual = resolveActiveProfiles(testClass);
		Set<String> expectedSet = new HashSet<String>(Arrays.asList(expected));
		Set<String> actualSet = new HashSet<String>(Arrays.asList(actual));
		assertEquals(expectedSet, actualSet);
	}

	@Test
	public void resolveActiveProfilesWithoutAnnotation() {
		assertArrayEquals(EMPTY_STRING_ARRAY, resolveActiveProfiles(Enigma.class));
	}

	@Test
	public void resolveActiveProfilesWithNoProfilesDeclared() {
		assertArrayEquals(EMPTY_STRING_ARRAY, resolveActiveProfiles(BareAnnotations.class));
	}

	@Test
	public void resolveActiveProfilesWithEmptyProfiles() {
		assertArrayEquals(EMPTY_STRING_ARRAY, resolveActiveProfiles(EmptyProfiles.class));
	}

	@Test
	public void resolveActiveProfilesWithDuplicatedProfiles() {
		assertResolvedProfiles(DuplicatedProfiles.class, "foo", "bar", "baz");
	}

	@Test
	public void resolveActiveProfilesWithLocalAnnotation() {
		assertResolvedProfiles(LocationsFoo.class, "foo");
	}

	@Test
	public void resolveActiveProfilesWithInheritedAnnotationAndLocations() {
		assertResolvedProfiles(InheritedLocationsFoo.class, "foo");
	}

	@Test
	public void resolveActiveProfilesWithInheritedAnnotationAndClasses() {
		assertResolvedProfiles(InheritedClassesFoo.class, "foo");
	}

	@Test
	public void resolveActiveProfilesWithLocalAndInheritedAnnotations() {
		assertResolvedProfiles(LocationsBar.class, "foo", "bar");
	}

	@Test
	public void resolveActiveProfilesWithOverriddenAnnotation() {
		assertResolvedProfiles(Animals.class, "dog", "cat");
	}

	/**
	 * @since 4.0
	 */
	@Test
	public void resolveActiveProfilesWithMetaAnnotation() {
		assertResolvedProfiles(MetaLocationsFoo.class, "foo");
	}

	/**
	 * @since 4.0
	 */
	@Test
	public void resolveActiveProfilesWithMetaAnnotationAndOverrides() {
		assertResolvedProfiles(MetaLocationsFooWithOverrides.class, "foo");
	}

	/**
	 * @since 4.0
	 */
	@Test
	public void resolveActiveProfilesWithMetaAnnotationAndOverriddenAttributes() {
		assertResolvedProfiles(MetaLocationsFooWithOverriddenAttributes.class, "foo1", "foo2");
	}

	/**
	 * @since 4.0
	 */
	@Test
	public void resolveActiveProfilesWithLocalAndInheritedMetaAnnotations() {
		assertResolvedProfiles(MetaLocationsBar.class, "foo", "bar");
	}

	/**
	 * @since 4.0
	 */
	@Test
	public void resolveActiveProfilesWithOverriddenMetaAnnotation() {
		assertResolvedProfiles(MetaAnimals.class, "dog", "cat");
	}

	/**
	 * @since 4.0
	 */
	@Test
	public void resolveActiveProfilesWithResolver() {
		assertResolvedProfiles(FooActiveProfilesResolverTestCase.class, "foo");
	}

	/**
	 * @since 4.0
	 */
	@Test
	public void resolveActiveProfilesWithInheritedResolver() {
		assertResolvedProfiles(InheritedFooActiveProfilesResolverTestCase.class, "foo");
	}

	/**
	 * @since 4.0
	 */
	@Test
	public void resolveActiveProfilesWithMergedInheritedResolver() {
		assertResolvedProfiles(MergedInheritedFooActiveProfilesResolverTestCase.class, "foo", "bar");
	}

	/**
	 * @since 4.0
	 */
	@Test
	public void resolveActiveProfilesWithOverridenInheritedResolver() {
		assertResolvedProfiles(OverridenInheritedFooActiveProfilesResolverTestCase.class, "bar");
	}

	/**
	 * @since 4.0
	 */
	@Test
	public void resolveActiveProfilesWithResolverAndProfiles() {
		assertResolvedProfiles(ResolverAndProfilesTestCase.class, "bar");
	}

	/**
	 * @since 4.0
	 */
	@Test
	public void resolveActiveProfilesWithResolverAndValue() {
		assertResolvedProfiles(ResolverAndValueTestCase.class, "bar");
	}

	/**
	 * @since 4.0
	 */
	@Test(expected = IllegalStateException.class)
	public void resolveActiveProfilesWithConflictingProfilesAndValue() {
		resolveActiveProfiles(ConflictingProfilesAndValueTestCase.class);
	}

	/**
	 * @since 4.0
	 */
	@Test(expected = IllegalStateException.class)
	public void resolveActiveProfilesWithResolverWithoutDefaultConstructor() {
		resolveActiveProfiles(NoDefaultConstructorActiveProfilesResolverTestCase.class);
	}

	/**
	 * @since 4.0
	 */
	@Test(expected = IllegalStateException.class)
	public void resolveActiveProfilesWithResolverThatReturnsNull() {
		resolveActiveProfiles(NullActiveProfilesResolverTestCase.class);
	}

	/**
	 * This test verifies that the actual test class, not the composed annotation,
	 * is passed to the resolver.
	 *
	 * @since 4.0.3
	 */
	@Test
	public void resolveActiveProfilesWithMetaAnnotationAndTestClassVerifyingResolver() {
		Class<TestClassVerifyingActiveProfilesResolverTestCase> testClass = TestClassVerifyingActiveProfilesResolverTestCase.class;
		assertResolvedProfiles(testClass, testClass.getSimpleName());
	}


	// -------------------------------------------------------------------------

	@ActiveProfiles({ "    ", "\t" })
	private static class EmptyProfiles {
	}

	@ActiveProfiles({ "foo", "bar", "  foo", "bar  ", "baz" })
	private static class DuplicatedProfiles {
	}

	@ActiveProfiles(profiles = { "dog", "cat" }, inheritProfiles = false)
	private static class Animals extends LocationsBar {
	}

	@ActiveProfiles(profiles = { "dog", "cat" }, inheritProfiles = false)
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	private static @interface MetaAnimalsConfig {
	}

	@ActiveProfiles(resolver = TestClassVerifyingActiveProfilesResolver.class)
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	private static @interface MetaResolverConfig {
	}

	@MetaAnimalsConfig
	private static class MetaAnimals extends MetaLocationsBar {
	}

	private static class InheritedLocationsFoo extends LocationsFoo {
	}

	private static class InheritedClassesFoo extends ClassesFoo {
	}

	@ActiveProfiles(resolver = NullActiveProfilesResolver.class)
	private static class NullActiveProfilesResolverTestCase {
	}

	@ActiveProfiles(resolver = NoDefaultConstructorActiveProfilesResolver.class)
	private static class NoDefaultConstructorActiveProfilesResolverTestCase {
	}

	@ActiveProfiles(resolver = FooActiveProfilesResolver.class)
	private static class FooActiveProfilesResolverTestCase {
	}

	private static class InheritedFooActiveProfilesResolverTestCase extends FooActiveProfilesResolverTestCase {
	}

	@ActiveProfiles(resolver = BarActiveProfilesResolver.class)
	private static class MergedInheritedFooActiveProfilesResolverTestCase extends
			InheritedFooActiveProfilesResolverTestCase {
	}

	@ActiveProfiles(resolver = BarActiveProfilesResolver.class, inheritProfiles = false)
	private static class OverridenInheritedFooActiveProfilesResolverTestCase extends
			InheritedFooActiveProfilesResolverTestCase {
	}

	@ActiveProfiles(resolver = BarActiveProfilesResolver.class, profiles = "ignored by custom resolver")
	private static class ResolverAndProfilesTestCase {
	}

	@ActiveProfiles(resolver = BarActiveProfilesResolver.class, value = "ignored by custom resolver")
	private static class ResolverAndValueTestCase {
	}

	@MetaResolverConfig
	private static class TestClassVerifyingActiveProfilesResolverTestCase {
	}

	@ActiveProfiles(profiles = "conflict 1", value = "conflict 2")
	private static class ConflictingProfilesAndValueTestCase {
	}

	private static class FooActiveProfilesResolver implements ActiveProfilesResolver {

		@Override
		public String[] resolve(Class<?> testClass) {
			return new String[] { "foo" };
		}
	}

	private static class BarActiveProfilesResolver implements ActiveProfilesResolver {

		@Override
		public String[] resolve(Class<?> testClass) {
			return new String[] { "bar" };
		}
	}

	private static class NullActiveProfilesResolver implements ActiveProfilesResolver {

		@Override
		public String[] resolve(Class<?> testClass) {
			return null;
		}
	}

	private static class NoDefaultConstructorActiveProfilesResolver implements ActiveProfilesResolver {

		@SuppressWarnings("unused")
		NoDefaultConstructorActiveProfilesResolver(Object agument) {
		}

		@Override
		public String[] resolve(Class<?> testClass) {
			return null;
		}
	}

	private static class TestClassVerifyingActiveProfilesResolver implements ActiveProfilesResolver {

		@Override
		public String[] resolve(Class<?> testClass) {
			return testClass.isAnnotation() ? new String[] { "@" + testClass.getSimpleName() }
					: new String[] { testClass.getSimpleName() };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2141.java