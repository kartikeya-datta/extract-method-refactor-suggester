error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11798.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11798.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11798.java
text:
```scala
r@@eturn !ObjectUtils.isEmpty(mergedConfig.getLocations()) && ObjectUtils.isEmpty(mergedConfig.getClasses());

/*
 * Copyright 2002-2011 the original author or authors.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.SmartContextLoader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

/**
 * Abstract application context loader, which provides a basis for all concrete
 * implementations of the {@link ContextLoader} SPI. Provides a
 * <em>Template Method</em> based approach for {@link #processLocations processing}
 * resource locations.
 * 
 * <p>As of Spring 3.1, <code>AbstractContextLoader</code> also provides a basis
 * for all concrete implementations of the {@link SmartContextLoader} SPI. For
 * backwards compatibility with the {@code ContextLoader} SPI, 
 * {@link #processContextConfiguration()} delegates to
 * {@link #processLocations()}, and {@link #generatesDefaults()} delegates to
 * {@link #isGenerateDefaultLocations()}. 
 *
 * @author Sam Brannen
 * @author Juergen Hoeller
 * @since 2.5
 * @see #generateDefaultLocations
 * @see #modifyLocations
 */
public abstract class AbstractContextLoader implements SmartContextLoader {

	private static final Log logger = LogFactory.getLog(AbstractContextLoader.class);

	private static final String[] EMPTY_STRING_ARRAY = new String[] {};
	private static final String SLASH = "/";


	// --- SmartContextLoader -----------------------------------------------

	/**
	 * For backwards compatibility with the {@link ContextLoader} SPI, the
	 * default implementation simply delegates to
	 * {@link #isGenerateDefaultLocations()}.
	 * @see org.springframework.test.context.SmartContextLoader#generatesDefaults()
	 * @see #isGenerateDefaultLocations()
	 */
	public boolean generatesDefaults() {
		return isGenerateDefaultLocations();
	}

	/**
	 * For backwards compatibility with the {@link ContextLoader} SPI, the
	 * default implementation simply delegates to {@link #processLocations()},
	 * passing it the {@link ContextConfigurationAttributes#getDeclaringClass()
	 * declaring class} and {@link ContextConfigurationAttributes#getLocations()
	 * resource locations} retrieved from the supplied
	 * {@link ContextConfigurationAttributes configuration attributes}. The
	 * processed locations are then
	 * {@link ContextConfigurationAttributes#setLocations(String[]) set} in
	 * the supplied configuration attributes.
	 * <p>Can be overridden in subclasses &mdash; for example, to process
	 * configuration classes instead of resource locations.
	 * @see #processLocations()
	 */
	public void processContextConfiguration(ContextConfigurationAttributes configAttributes) {
		String[] processedLocations = processLocations(configAttributes.getDeclaringClass(),
			configAttributes.getLocations());
		configAttributes.setLocations(processedLocations);
	}

	/**
	 * TODO Document default supports(MergedContextConfiguration) implementation.
	 */
	public boolean supports(MergedContextConfiguration mergedConfig) {
		return !ObjectUtils.isEmpty(mergedConfig.getLocations());
	}

	// --- ContextLoader -------------------------------------------------------

	/**
	 * If the supplied <code>locations</code> are <code>null</code> or
	 * <em>empty</em> and {@link #isGenerateDefaultLocations()} returns
	 * <code>true</code>, default locations will be
	 * {@link #generateDefaultLocations(Class) generated} for the specified
	 * {@link Class class} and the configured
	 * {@link #getResourceSuffix() resource suffix}; otherwise, the supplied
	 * <code>locations</code> will be {@link #modifyLocations modified} if
	 * necessary and returned.
	 * @param clazz the class with which the locations are associated: to be
	 * used when generating default locations
	 * @param locations the unmodified locations to use for loading the
	 * application context (can be <code>null</code> or empty)
	 * @return a processed array of application context resource locations
	 * @see #isGenerateDefaultLocations()
	 * @see #generateDefaultLocations()
	 * @see #modifyLocations()
	 * @see org.springframework.test.context.ContextLoader#processLocations()
	 * @see #processContextConfiguration()
	 */
	public final String[] processLocations(Class<?> clazz, String... locations) {
		return (ObjectUtils.isEmpty(locations) && isGenerateDefaultLocations()) ? generateDefaultLocations(clazz)
				: modifyLocations(clazz, locations);
	}

	/**
	 * Generate the default classpath resource locations array based on the
	 * supplied class.
	 * <p>For example, if the supplied class is <code>com.example.MyTest</code>,
	 * the generated locations will contain a single string with a value of
	 * &quot;classpath:/com/example/MyTest<code>&lt;suffix&gt;</code>&quot;,
	 * where <code>&lt;suffix&gt;</code> is the value of the
	 * {@link #getResourceSuffix() resource suffix} string.
	 * <p>As of Spring 3.1, the implementation of this method adheres to the
	 * contract defined in the {@link SmartContextLoader} SPI. Specifically, 
	 * this method will <em>preemptively</em> verify that the generated default
	 * location actually exists. If it does not exist, this method will log a
	 * warning and return an empty array.
	 * <p>Subclasses can override this method to implement a different
	 * <em>default location generation</em> strategy.
	 * @param clazz the class for which the default locations are to be generated
	 * @return an array of default application context resource locations
	 * @see #getResourceSuffix()
	 */
	protected String[] generateDefaultLocations(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		String suffix = getResourceSuffix();
		Assert.hasText(suffix, "Resource suffix must not be empty");
		String resourcePath = SLASH + ClassUtils.convertClassNameToResourcePath(clazz.getName()) + suffix;

		if (!new ClassPathResource(resourcePath, clazz).exists()) {
			if (logger.isInfoEnabled()) {
				logger.info(String.format("Cannot generate default resource locations for test class [%s]: "
						+ "classpath resource [%s] does not exist.", clazz.getName(), resourcePath));
			}
			return EMPTY_STRING_ARRAY;
		}

		// else
		return new String[] { ResourceUtils.CLASSPATH_URL_PREFIX + resourcePath };
	}

	/**
	 * Generate a modified version of the supplied locations array and return it.
	 * <p>A plain path &mdash; for example, &quot;context.xml&quot; &mdash;
	 * will be treated as a classpath resource from the same package in which
	 * the specified class is defined. A path starting with a slash is treated
	 * as a fully qualified classpath location, for example:
	 * &quot;/org/springframework/whatever/foo.xml&quot;. A path which
	 * references a URL (e.g., a path prefixed with
	 * {@link ResourceUtils#CLASSPATH_URL_PREFIX classpath:},
	 * {@link ResourceUtils#FILE_URL_PREFIX file:}, <code>http:</code>,
	 * etc.) will be added to the results unchanged.
	 * <p>Subclasses can override this method to implement a different
	 * <em>location modification</em> strategy.
	 * @param clazz the class with which the locations are associated
	 * @param locations the resource locations to be modified
	 * @return an array of modified application context resource locations
	 */
	protected String[] modifyLocations(Class<?> clazz, String... locations) {
		String[] modifiedLocations = new String[locations.length];
		for (int i = 0; i < locations.length; i++) {
			String path = locations[i];
			if (path.startsWith(SLASH)) {
				modifiedLocations[i] = ResourceUtils.CLASSPATH_URL_PREFIX + path;
			}
			else if (!ResourcePatternUtils.isUrl(path)) {
				modifiedLocations[i] = ResourceUtils.CLASSPATH_URL_PREFIX + SLASH
						+ StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(clazz) + SLASH + path);
			}
			else {
				modifiedLocations[i] = StringUtils.cleanPath(path);
			}
		}
		return modifiedLocations;
	}

	/**
	 * Determine whether or not <em>default</em> resource locations should be
	 * generated if the <code>locations</code> provided to
	 * {@link #processLocations()} are <code>null</code> or empty.
	 * <p>Can be overridden by subclasses to change the default behavior.
	 * @return always <code>true</code> by default
	 */
	protected boolean isGenerateDefaultLocations() {
		return true;
	}

	/**
	 * Get the suffix to append to {@link ApplicationContext} resource
	 * locations when generating default locations.
	 * <p>Must be implemented by subclasses.
	 * @return the resource suffix; should not be <code>null</code> or empty
	 * @see #generateDefaultLocations()
	 */
	protected abstract String getResourceSuffix();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11798.java