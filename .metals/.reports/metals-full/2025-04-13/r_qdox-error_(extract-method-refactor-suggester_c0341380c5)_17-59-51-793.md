error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9233.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9233.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,14]

error in qdox parser
file content:
```java
offset: 14
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9233.java
text:
```scala
public final v@@oid registerScope(Class<?> annotationType, String scopeName) {

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

package org.springframework.context.annotation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * Simple {@link ScopeMetadataResolver} implementation that follows JSR-330 scoping rules:
 * defaulting to prototype scope unless {@link javax.inject.Singleton} is present.
 *
 * <p>This scope resolver can be used with {@link ClassPathBeanDefinitionScanner} and
 * {@link AnnotatedBeanDefinitionReader} for standard JSR-330 compliance. However,
 * in practice, you will typically use Spring's rich default scoping instead - or extend
 * this resolver with custom scoping annotations that point to extended Spring scopes.
 *
 * @author Juergen Hoeller
 * @since 3.0
 * @see #registerScope
 * @see #resolveScopeName
 * @see ClassPathBeanDefinitionScanner#setScopeMetadataResolver
 * @see AnnotatedBeanDefinitionReader#setScopeMetadataResolver
 */
public class Jsr330ScopeMetadataResolver implements ScopeMetadataResolver {

	private final Map<String, String> scopeMap = new HashMap<String, String>();


	public Jsr330ScopeMetadataResolver() {
		registerScope("javax.inject.Singleton", BeanDefinition.SCOPE_SINGLETON);
	}


	/**
	 * Register an extended JSR-330 scope annotation, mapping it onto a
	 * specific Spring scope by name.
	 * @param annotationType the JSR-330 annotation type as a Class
	 * @param scopeName the Spring scope name
	 */
	public final void registerScope(Class annotationType, String scopeName) {
		this.scopeMap.put(annotationType.getName(), scopeName);
	}

	/**
	 * Register an extended JSR-330 scope annotation, mapping it onto a
	 * specific Spring scope by name.
	 * @param annotationType the JSR-330 annotation type by name
	 * @param scopeName the Spring scope name
	 */
	public final void registerScope(String annotationType, String scopeName) {
		this.scopeMap.put(annotationType, scopeName);
	}

	/**
	 * Resolve the given annotation type into a named Spring scope.
	 * <p>The default implementation simply checks against registered scopes.
	 * Can be overridden for custom mapping rules, e.g. naming conventions.
	 * @param annotationType the JSR-330 annotation type
	 * @return the Spring scope name
	 */
	protected String resolveScopeName(String annotationType) {
		return this.scopeMap.get(annotationType);
	}


	@Override
	public ScopeMetadata resolveScopeMetadata(BeanDefinition definition) {
		ScopeMetadata metadata = new ScopeMetadata();
		metadata.setScopeName(BeanDefinition.SCOPE_PROTOTYPE);
		if (definition instanceof AnnotatedBeanDefinition) {
			AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition) definition;
			Set<String> annTypes = annDef.getMetadata().getAnnotationTypes();
			String found = null;
			for (String annType : annTypes) {
				Set<String> metaAnns = annDef.getMetadata().getMetaAnnotationTypes(annType);
				if (metaAnns.contains("javax.inject.Scope")) {
					if (found != null) {
						throw new IllegalStateException("Found ambiguous scope annotations on bean class [" +
								definition.getBeanClassName() + "]: " + found + ", " + annType);
					}
					found = annType;
					String scopeName = resolveScopeName(annType);
					if (scopeName == null) {
						throw new IllegalStateException(
								"Unsupported scope annotation - not mapped onto Spring scope name: " + annType);
					}
					metadata.setScopeName(scopeName);
				}
			}
		}
		return metadata;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9233.java