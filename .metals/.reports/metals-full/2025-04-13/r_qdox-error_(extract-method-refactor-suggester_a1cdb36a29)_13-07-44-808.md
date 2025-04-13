error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9739.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9739.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9739.java
text:
```scala
S@@tring str = new ToStringCreator(this).append("myMethod", this.getClass().getMethod("testMethod"))

/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springframework.core.style;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.springframework.core.CollectionFactory;
import org.springframework.util.ObjectUtils;

/**
 * @author Keith Donald
 */
public class ToStringCreatorTests extends TestCase {

	private SomeObject s1, s2, s3;

	protected void setUp() throws Exception {
		s1 = new SomeObject() {
			public String toString() {
				return "A";
			}
		};
		s2 = new SomeObject() {
			public String toString() {
				return "B";
			}
		};
		s3 = new SomeObject() {
			public String toString() {
				return "C";
			}
		};
	}

	public void testDefaultStyleMap() {
		final Map map = getMap();
		Object stringy = new Object() {
			public String toString() {
				return new ToStringCreator(this).append("familyFavoriteSport", map).toString();
			}
		};
		assertEquals("[ToStringCreatorTests.4@" + ObjectUtils.getIdentityHexString(stringy)
				+ " familyFavoriteSport = map['Keri' -> 'Softball', 'Scot' -> 'Fishing', 'Keith' -> 'Flag Football']]",
				stringy.toString());
	}

	private Map getMap() {
		Map map = CollectionFactory.createLinkedMapIfPossible(3);
		map.put("Keri", "Softball");
		map.put("Scot", "Fishing");
		map.put("Keith", "Flag Football");
		return map;
	}

	public void testDefaultStyleArray() {
		SomeObject[] array = new SomeObject[] { s1, s2, s3 };
		String str = new ToStringCreator(array).toString();
		assertEquals("[@" + ObjectUtils.getIdentityHexString(array)
				+ " array<ToStringCreatorTests.SomeObject>[A, B, C]]", str);
	}

	public void testPrimitiveArrays() {
		int[] integers = new int[] { 0, 1, 2, 3, 4 };
		String str = new ToStringCreator(integers).toString();
		assertEquals("[@" + ObjectUtils.getIdentityHexString(integers) + " array<Integer>[0, 1, 2, 3, 4]]", str);
	}

	public void testList() {
		List list = new ArrayList();
		list.add(s1);
		list.add(s2);
		list.add(s3);
		String str = new ToStringCreator(this).append("myLetters", list).toString();
		assertEquals("[ToStringCreatorTests@" + ObjectUtils.getIdentityHexString(this) + " myLetters = list[A, B, C]]",
				str);
	}

	public void testSet() {
		Set set = CollectionFactory.createLinkedSetIfPossible(3);
		set.add(s1);
		set.add(s2);
		set.add(s3);
		String str = new ToStringCreator(this).append("myLetters", set).toString();
		assertEquals("[ToStringCreatorTests@" + ObjectUtils.getIdentityHexString(this) + " myLetters = set[A, B, C]]",
				str);
	}

	public void testClass() {
		String str = new ToStringCreator(this).append("myClass", this.getClass()).toString();
		assertEquals("[ToStringCreatorTests@" + ObjectUtils.getIdentityHexString(this)
				+ " myClass = ToStringCreatorTests]", str);
	}

	public void testMethod() throws Exception {
		String str = new ToStringCreator(this).append("myMethod", this.getClass().getMethod("testMethod", null))
				.toString();
		assertEquals("[ToStringCreatorTests@" + ObjectUtils.getIdentityHexString(this)
				+ " myMethod = testMethod@ToStringCreatorTests]", str);
	}


	public static class SomeObject {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9739.java