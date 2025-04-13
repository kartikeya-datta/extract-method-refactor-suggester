error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7285.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7285.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7285.java
text:
```scala
i@@f (res == null && createIfNotFound)

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.ng.resource;

import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.wicket.util.lang.Checks;
import org.apache.wicket.util.lang.Objects;

/**
 * Allows to register and lookup {@link ResourceReference}s.
 * 
 * @author Matej Knopp
 */
public class ResourceReferenceRegistry
{
	private static class Key
	{
		private final String scope;
		private final String name;
		private final Locale locale;
		private final String style;
		private final String variation;

		public Key(String scope, String name, Locale locale, String style, String variation)
		{
			Checks.argumentNotNull(scope, "scope");
			Checks.argumentNotNull(name, "name");

			this.scope = scope.intern();
			this.name = name.intern();
			this.locale = locale;
			this.style = style != null ? style.intern() : null;
			this.variation = variation != null ? variation.intern() : null;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (obj instanceof Key == false)
			{
				return false;
			}
			Key that = (Key)obj;
			return Objects.equal(scope, that.scope) && //
				Objects.equal(name, that.name) && //
				Objects.equal(locale, that.locale) && //
				Objects.equal(style, that.style) && //
				Objects.equal(variation, that.variation);
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			return Objects.hashCode(scope, name, locale, style, variation);
		}
	};

	private final Map<Key, ResourceReference> map = new ConcurrentHashMap<Key, ResourceReference>();
	private final Queue<Key> autoAddedQueue = new ConcurrentLinkedQueue<Key>();

	private int autoAddedCapacity = 1000;

	/**
	 * Construct.
	 */
	public ResourceReferenceRegistry()
	{
	}

	/**
	 * Registers the given {@link ResourceReference}.
	 * 
	 * @param reference
	 */
	public void registerResourceReference(ResourceReference reference)
	{
		Checks.argumentNotNull(reference, "reference");

		if (reference.canBeRegistered())
		{

			Key key = new Key(reference.getScope().getName(), reference.getName(),
				reference.getLocale(), reference.getStyle(), reference.getVariation());

			if (map.containsKey(key) == false)
			{
				map.put(key, reference);
			}
		}
	}

	/**
	 * Unregisters the given {@link ResourceReference}.
	 * 
	 * @param reference
	 */
	public void unregisterResourceReference(ResourceReference reference)
	{
		Checks.argumentNotNull(reference, "reference");

		Key key = new Key(reference.getScope().getName(), reference.getName(),
			reference.getLocale(), reference.getStyle(), reference.getVariation());
		map.remove(key);
	}

	/**
	 * 
	 * @param scope
	 * @param name
	 * @param locale
	 * @param style
	 * @param variation
	 * @param strict
	 * @param createIfNotFound
	 * @return
	 */
	protected ResourceReference getResourceReference(Class<?> scope, String name, Locale locale,
		String style, String variation, boolean strict, boolean createIfNotFound)
	{
		Key key = new Key(scope.getName(), name, locale, style, variation);
		ResourceReference res = map.get(key);
		if (strict)
		{
			if (res == null)
			{
				res = addDefaultResourceReference(scope, name, locale, style, variation);
			}
		}
		if (strict || res != null)
		{
			return res;
		}
		else
		{
			res = getResourceReference(scope, name, locale, style, null, true, false);
			if (res == null)
			{
				res = getResourceReference(scope, name, locale, null, variation, true, false);
			}
			if (res == null)
			{
				res = getResourceReference(scope, name, locale, null, null, true, false);
			}
			if (res == null)
			{
				res = getResourceReference(scope, name, null, style, variation, true, false);
			}
			if (res == null)
			{
				res = getResourceReference(scope, name, null, style, null, true, false);
			}
			if (res == null)
			{
				res = getResourceReference(scope, name, null, null, variation, true, false);
			}
			if (res == null)
			{
				res = getResourceReference(scope, name, null, null, null, true, false);
			}
			if (res == null && createIfNotFound)
			{
				res = addDefaultResourceReference(scope, name, locale, style, variation);
			}
			return res;
		}
	}

	private final ClassScanner scanner = new ClassScanner()
	{
		@Override
		void foundResourceReference(ResourceReference reference)
		{
			registerResourceReference(reference);
		}
	};

	/**
	 * Looks up resource reference with specified attributes. If the reference is not found and
	 * <code>strict</code> is set to <code>false</code>, result of
	 * {@link #createDefaultResourceReference(Class, String, Locale, String)} is returned.
	 * 
	 * @param scope
	 *            mandatory parameter
	 * @param name
	 *            mandatory parameter
	 * @param locale
	 * @param style
	 * @param variation
	 * @param strict
	 *            if <code>strict</code> is <code>true</code> only resources that match exactly are
	 *            returned. Otherwise if there is no resource registered that is an exact match,
	 *            also resources with <code>null</code> style and locale are tried. If still no
	 *            resource is found, result of
	 *            {@link #createDefaultResourceReference(Class, String, Locale, String)} is
	 *            returned.
	 * @return {@link ResourceReference} or <code>null</code>
	 */
	public ResourceReference getResourceReference(Class<?> scope, String name, Locale locale,
		String style, String variation, boolean strict)
	{
		ResourceReference reference = getResourceReference(scope, name, locale, style, variation,
			strict, false);

		if (reference == null)
		{
			scanner.scanClass(scope);
		}

		// try again, it might have been registered by scanner; create default one if necessary
		reference = getResourceReference(scope, name, locale, style, variation, strict, true);

		return reference;
	}


	private ResourceReference addDefaultResourceReference(Class<?> scope, String name,
		Locale locale, String style, String variation)
	{
		ResourceReference reference = createDefaultResourceReference(scope, name, locale, style,
			variation);

		if (reference != null)
		{
			Key key = new Key(scope.getName(), name, locale, style, variation);
			if (autoAddedQueue.size() > getAutoAddedCapacity())
			{
				Key first = autoAddedQueue.remove();
				map.remove(first);
			}
			autoAddedQueue.add(key);

			registerResourceReference(reference);
		}
		return reference;
	}

	protected ResourceReference createDefaultResourceReference(Class<?> scope, String name,
		Locale locale, String style, String variation)
	{
		if (PackageResource.exists(scope, name, locale, style, variation))
		{
			return new PackageResourceReference(scope, name, locale, style, variation);
		}
		else
		{
			return null;
		}
	}


	public void setAutoAddedCapacity(int autoAddedCapacity)
	{
		this.autoAddedCapacity = autoAddedCapacity;
	}

	public int getAutoAddedCapacity()
	{
		return autoAddedCapacity;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7285.java