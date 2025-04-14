error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7010.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7010.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7010.java
text:
```scala
i@@f (logger != null && logger.isDebugEnabled()) {

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

package org.springframework.core.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.Assert;
import org.springframework.util.CachingMapDecorator;
import org.springframework.util.ClassUtils;

/**
 * Abstract base class for {@link LabeledEnumResolver} implementations,
 * caching all retrieved {@link LabeledEnum} instances.
 *
 * <p>Subclasses need to implement the template method
 * {@link #findLabeledEnums(Class)}.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 1.2.2
 * @see #findLabeledEnums(Class)
 * @deprecated as of Spring 3.0, in favor of Java 5 enums.
 */
@Deprecated
public abstract class AbstractCachingLabeledEnumResolver implements LabeledEnumResolver {

	protected transient final Log logger = LogFactory.getLog(getClass());

	private final LabeledEnumCache labeledEnumCache = new LabeledEnumCache();


	public Set<LabeledEnum> getLabeledEnumSet(Class type) throws IllegalArgumentException {
		return new TreeSet<LabeledEnum>(getLabeledEnumMap(type).values());
	}

	public Map<Comparable, LabeledEnum> getLabeledEnumMap(Class type) throws IllegalArgumentException {
		Assert.notNull(type, "No type specified");
		return this.labeledEnumCache.get(type);
	}

	public LabeledEnum getLabeledEnumByCode(Class type, Comparable code) throws IllegalArgumentException {
		Assert.notNull(code, "No enum code specified");
		Map<Comparable, LabeledEnum> typeEnums = getLabeledEnumMap(type);
		LabeledEnum codedEnum = typeEnums.get(code);
		if (codedEnum == null) {
			throw new IllegalArgumentException(
					"No enumeration with code '" + code + "'" + " of type [" + type.getName() +
					"] exists: this is likely a configuration error. " +
					"Make sure the code value matches a valid instance's code property!");
		}
		return codedEnum;
	}

	public LabeledEnum getLabeledEnumByLabel(Class type, String label) throws IllegalArgumentException {
		Map<Comparable, LabeledEnum> typeEnums = getLabeledEnumMap(type);
		for (LabeledEnum value : typeEnums.values()) {
			if (value.getLabel().equalsIgnoreCase(label)) {
				return value;
			}
		}
		throw new IllegalArgumentException(
				"No enumeration with label '" + label + "' of type [" + type +
				"] exists: this is likely a configuration error. " +
				"Make sure the label string matches a valid instance's label property!");
	}


	/**
	 * Template method to be implemented by subclasses.
	 * Supposed to find all LabeledEnum instances for the given type.
	 * @param type the enum type
	 * @return the Set of LabeledEnum instances
	 * @see org.springframework.core.enums.LabeledEnum
	 */
	protected abstract Set<LabeledEnum> findLabeledEnums(Class type);


	/**
	 * Inner cache class that implements lazy building of LabeledEnum Maps.
	 */
	private class LabeledEnumCache extends CachingMapDecorator<Class, Map<Comparable, LabeledEnum>> {

		public LabeledEnumCache() {
			super(true);
		}

		@Override
		protected Map<Comparable, LabeledEnum> create(Class key) {
			Set<LabeledEnum> typeEnums = findLabeledEnums(key);
			if (typeEnums == null || typeEnums.isEmpty()) {
				throw new IllegalArgumentException(
						"Unsupported labeled enumeration type '" + key + "': " +
						"make sure you've properly defined this enumeration! " +
						"If it is static, are the class and its fields public/static/final?");
			}
			Map<Comparable, LabeledEnum> typeEnumMap = new HashMap<Comparable, LabeledEnum>(typeEnums.size());
			for (LabeledEnum labeledEnum : typeEnums) {
				typeEnumMap.put(labeledEnum.getCode(), labeledEnum);
			}
			return Collections.unmodifiableMap(typeEnumMap);
		}

		@Override
		protected boolean useWeakValue(Class key, Map<Comparable, LabeledEnum> value) {
			if (!ClassUtils.isCacheSafe(key, AbstractCachingLabeledEnumResolver.this.getClass().getClassLoader())) {
				if (logger.isDebugEnabled()) {
					logger.debug("Not strongly caching class [" + key.getName() + "] because it is not cache-safe");
				}
				return true;
			}
			else {
				return false;
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7010.java