error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3830.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3830.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3830.java
text:
```scala
protected static S@@tring nullSafeToString(ContextLoader contextLoader) {

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

package org.springframework.test.context;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.style.ToStringCreator;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * {@code MergedContextConfiguration} encapsulates the <em>merged</em>
 * context configuration declared on a test class and all of its superclasses
 * via {@link ContextConfiguration @ContextConfiguration} and
 * {@link ActiveProfiles @ActiveProfiles}.
 *
 * <p>Merged resource locations, annotated classes, and active profiles
 * represent all declared values in the test class hierarchy taking into
 * consideration the semantics of the
 * {@link ContextConfiguration#inheritLocations inheritLocations} and
 * {@link ActiveProfiles#inheritProfiles inheritProfiles} flags in
 * {@code @ContextConfiguration} and {@code @ActiveProfiles}, respectively.
 * 
 * <p>A {@link SmartContextLoader} uses {@code MergedContextConfiguration}
 * to load an {@link org.springframework.context.ApplicationContext ApplicationContext}.
 * 
 * <p>{@code MergedContextConfiguration} is also used by the {@link TestContext}
 * as the context cache key for caching an
 * {@link org.springframework.context.ApplicationContext ApplicationContext}
 * that was loaded using properties of this {@code MergedContextConfiguration}.
 * 
 * @author Sam Brannen
 * @since 3.1
 * @see ContextConfiguration
 * @see ActiveProfiles
 * @see ContextConfigurationAttributes
 * @see SmartContextLoader#loadContext(MergedContextConfiguration)
 */
public class MergedContextConfiguration implements Serializable {

	private static final long serialVersionUID = -3290560718464957422L;

	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];
	private static final Set<Class<? extends ApplicationContextInitializer<? extends ConfigurableApplicationContext>>> EMPTY_INITIALIZER_CLASSES = //
	Collections.<Class<? extends ApplicationContextInitializer<? extends ConfigurableApplicationContext>>> emptySet();

	private final Class<?> testClass;
	private final String[] locations;
	private final Class<?>[] classes;
	private final Set<Class<? extends ApplicationContextInitializer<? extends ConfigurableApplicationContext>>> contextInitializerClasses;
	private final String[] activeProfiles;
	private final ContextLoader contextLoader;


	private static String[] processLocations(String[] locations) {
		return locations == null ? EMPTY_STRING_ARRAY : locations;
	}

	private static Class<?>[] processClasses(Class<?>[] classes) {
		return classes == null ? EMPTY_CLASS_ARRAY : classes;
	}

	private static Set<Class<? extends ApplicationContextInitializer<? extends ConfigurableApplicationContext>>> processContextInitializerClasses(
			Set<Class<? extends ApplicationContextInitializer<? extends ConfigurableApplicationContext>>> contextInitializerClasses) {
		return contextInitializerClasses == null ? EMPTY_INITIALIZER_CLASSES
				: Collections.unmodifiableSet(contextInitializerClasses);
	}

	private static String[] processActiveProfiles(String[] activeProfiles) {
		if (activeProfiles == null) {
			return EMPTY_STRING_ARRAY;
		}

		// Active profiles must be unique and sorted in order to support proper
		// cache key generation. Specifically, profile sets {foo,bar} and
		// {bar,foo} must both result in the same array (e.g., [bar,foo]).
		SortedSet<String> sortedProfilesSet = new TreeSet<String>(Arrays.asList(activeProfiles));
		return StringUtils.toStringArray(sortedProfilesSet);
	}

	/**
	 * Generate a null-safe {@link String} representation of the supplied
	 * {@link ContextLoader} based solely on the fully qualified name of the
	 * loader or &quot;null&quot; if the supplied loaded is <code>null</code>.
	 */
	private static String nullSafeToString(ContextLoader contextLoader) {
		return contextLoader == null ? "null" : contextLoader.getClass().getName();
	}

	/**
	 * Create a new {@code MergedContextConfiguration} instance for the
	 * supplied test class, resource locations, annotated classes, active
	 * profiles, and {@code ContextLoader}.
	 *
	 * <p>If a <code>null</code> value is supplied for <code>locations</code>,
	 * <code>classes</code>, or <code>activeProfiles</code> an empty array will
	 * be stored instead. Furthermore, active profiles will be sorted, and duplicate
	 * profiles will be removed.
	 * 
	 * @param testClass the test class for which the configuration was merged
	 * @param locations the merged resource locations
	 * @param classes the merged annotated classes
	 * @param activeProfiles the merged active bean definition profiles
	 * @param contextLoader the resolved <code>ContextLoader</code>
	 * @see #MergedContextConfiguration(Class, String[], Class[], Set, String[], ContextLoader)
	 */
	public MergedContextConfiguration(Class<?> testClass, String[] locations, Class<?>[] classes,
			String[] activeProfiles, ContextLoader contextLoader) {
		this(testClass, locations, classes, null, activeProfiles, contextLoader);
	}

	/**
	 * Create a new {@code MergedContextConfiguration} instance for the
	 * supplied test class, resource locations, annotated classes, context
	 * initializers, active profiles, and {@code ContextLoader}.
	 *
	 * <p>If a <code>null</code> value is supplied for <code>locations</code>,
	 * <code>classes</code>, or <code>activeProfiles</code> an empty array will
	 * be stored instead. If a <code>null</code> value is supplied for the
	 * <code>contextInitializerClasses</code> an empty set will be stored instead.
	 * Furthermore, active profiles will be sorted, and duplicate profiles will
	 * be removed.
	 *
	 * @param testClass the test class for which the configuration was merged
	 * @param locations the merged resource locations
	 * @param classes the merged annotated classes
	 * @param contextInitializerClasses the merged context initializer classes
	 * @param activeProfiles the merged active bean definition profiles
	 * @param contextLoader the resolved <code>ContextLoader</code>
	 */
	public MergedContextConfiguration(
			Class<?> testClass,
			String[] locations,
			Class<?>[] classes,
			Set<Class<? extends ApplicationContextInitializer<? extends ConfigurableApplicationContext>>> contextInitializerClasses,
			String[] activeProfiles, ContextLoader contextLoader) {
		this.testClass = testClass;
		this.locations = processLocations(locations);
		this.classes = processClasses(classes);
		this.contextInitializerClasses = processContextInitializerClasses(contextInitializerClasses);
		this.activeProfiles = processActiveProfiles(activeProfiles);
		this.contextLoader = contextLoader;
	}

	/**
	 * Get the {@linkplain Class test class} associated with this {@code MergedContextConfiguration}.
	 */
	public Class<?> getTestClass() {
		return testClass;
	}

	/**
	 * Get the merged resource locations for the {@linkplain #getTestClass() test class}.
	 */
	public String[] getLocations() {
		return locations;
	}

	/**
	 * Get the merged annotated classes for the {@linkplain #getTestClass() test class}.
	 */
	public Class<?>[] getClasses() {
		return classes;
	}

	/**
	 * Get the merged {@code ApplicationContextInitializer} classes for the
	 * {@linkplain #getTestClass() test class}.
	 */
	public Set<Class<? extends ApplicationContextInitializer<? extends ConfigurableApplicationContext>>> getContextInitializerClasses() {
		return contextInitializerClasses;
	}

	/**
	 * Get the merged active bean definition profiles for the {@linkplain #getTestClass() test class}.
	 */
	public String[] getActiveProfiles() {
		return activeProfiles;
	}

	/**
	 * Get the resolved {@link ContextLoader} for the {@linkplain #getTestClass() test class}.
	 */
	public ContextLoader getContextLoader() {
		return contextLoader;
	}

	/**
	 * Generate a unique hash code for all properties of this
	 * {@code MergedContextConfiguration} excluding the
	 * {@linkplain #getTestClass() test class}.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(locations);
		result = prime * result + Arrays.hashCode(classes);
		result = prime * result + contextInitializerClasses.hashCode();
		result = prime * result + Arrays.hashCode(activeProfiles);
		result = prime * result + nullSafeToString(contextLoader).hashCode();
		return result;
	}

	/**
	 * Determine if the supplied object is equal to this {@code MergedContextConfiguration}
	 * instance by comparing both object's {@linkplain #getLocations() locations},
	 * {@linkplain #getClasses() annotated classes},
	 * {@linkplain #getContextInitializerClasses() context initializer classes},
	 * {@linkplain #getActiveProfiles() active profiles}, and the fully qualified
	 * names of their {@link #getContextLoader() ContextLoaders}.
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MergedContextConfiguration)) {
			return false;
		}

		final MergedContextConfiguration that = (MergedContextConfiguration) obj;

		if (!Arrays.equals(this.locations, that.locations)) {
			return false;
		}
		if (!Arrays.equals(this.classes, that.classes)) {
			return false;
		}
		if (!this.contextInitializerClasses.equals(that.contextInitializerClasses)) {
			return false;
		}
		if (!Arrays.equals(this.activeProfiles, that.activeProfiles)) {
			return false;
		}
		if (!nullSafeToString(this.contextLoader).equals(nullSafeToString(that.contextLoader))) {
			return false;
		}

		return true;
	}

	/**
	 * Provide a String representation of the {@linkplain #getTestClass() test class},
	 * {@linkplain #getLocations() locations}, {@linkplain #getClasses() annotated classes},
	 * {@linkplain #getContextInitializerClasses() context initializer classes},
	 * {@linkplain #getActiveProfiles() active profiles}, and the name of the
	 * {@link #getContextLoader() ContextLoader}.
	 */
	@Override
	public String toString() {
		return new ToStringCreator(this)//
		.append("testClass", testClass)//
		.append("locations", ObjectUtils.nullSafeToString(locations))//
		.append("classes", ObjectUtils.nullSafeToString(classes))//
		.append("contextInitializerClasses", ObjectUtils.nullSafeToString(contextInitializerClasses))//
		.append("activeProfiles", ObjectUtils.nullSafeToString(activeProfiles))//
		.append("contextLoader", nullSafeToString(contextLoader))//
		.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3830.java