error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11356.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11356.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11356.java
text:
```scala
o@@bject.value = component.getDefaultModelObjectAsString();

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
package org.apache.wicket.util.tester;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.apache.wicket.Component;
import org.apache.wicket.IClusterable;
import org.apache.wicket.Page;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.util.string.Strings;

/**
 * A <code>WicketTester</code>-specific helper class.
 * 
 * @author Ingram Chen
 * @since 1.2.6
 */
public class WicketTesterHelper
{
	/**
	 * <code>ComponentData</code> class.
	 * 
	 * @author Ingram Chen
	 * @since 1.2.6
	 */
	public static class ComponentData implements IClusterable
	{
		private static final long serialVersionUID = 1L;

		/** Component path. */
		public String path;

		/** Component type. */
		public String type;

		/** Component value. */
		public String value;
	}

	/**
	 * Gets recursively all <code>Component</code>s of a given <code>Page</code>, extracts the
	 * information relevant to us, and adds them to a <code>List</code>.
	 * 
	 * @param page
	 *            the <code>Page</code> to analyze
	 * @return a <code>List</code> of <code>Component</code> data objects
	 */
	public static List<WicketTesterHelper.ComponentData> getComponentData(final Page page)
	{
		final List data = new ArrayList();

		if (page != null)
		{
			page.visitChildren(new IVisitor()
			{
				public Object component(final Component component)
				{
					final ComponentData object = new ComponentData();

					// anonymous class? Get the parent's class name
					String name = component.getClass().getName();
					if (name.indexOf("$") > 0)
					{
						name = component.getClass().getSuperclass().getName();
					}

					// remove the path component
					name = Strings.lastPathComponent(name, Component.PATH_SEPARATOR);

					object.path = component.getPageRelativePath();
					object.type = name;
					try
					{
						object.value = component.getModelObjectAsString();
					}
					catch (Exception e)
					{
						object.value = e.getMessage();
					}

					data.add(object);
					return IVisitor.CONTINUE_TRAVERSAL;
				}
			});
		}
		return data;
	}

	/**
	 * Asserts that both <code>Collection</code>s contain the same elements.
	 * 
	 * @param expects
	 *            a <code>Collection</code> object
	 * @param actuals
	 *            a <code>Collection</code> object
	 */
	public static void assertEquals(final Collection expects, final Collection actuals)
	{
		if (!expects.containsAll(actuals) || !actuals.containsAll(expects))
		{
			failWithVerboseMessage(expects, actuals);
		}
	}

	/**
	 * Fails with a verbose error message.
	 * 
	 * @param expects
	 *            a <code>Collection</code> object
	 * @param actuals
	 *            a <code>Collection</code> object
	 */
	public static void failWithVerboseMessage(final Collection expects, final Collection actuals)
	{
		Assert.fail("\nexpect (" + expects.size() + "):\n" + asLined(expects) + "\nbut was (" +
			actuals.size() + "):\n" + asLined(actuals));
	}

	/**
	 * A <code>toString</code> method for the given <code>Collection</code>.
	 * 
	 * @param objects
	 *            a <code>Collection</code> object
	 * @return a <code>String</code> representation of the <code>Collection</code>
	 */
	public static String asLined(final Collection objects)
	{
		StringBuffer lined = new StringBuffer();
		for (Iterator iter = objects.iterator(); iter.hasNext();)
		{
			String objectString = iter.next().toString();
			lined.append("   ");
			lined.append(objectString);
			if (iter.hasNext())
			{
				lined.append("\n");
			}
		}
		return lined.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11356.java