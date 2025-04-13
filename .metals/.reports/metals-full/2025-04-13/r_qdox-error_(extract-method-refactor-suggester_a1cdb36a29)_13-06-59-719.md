error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5010.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5010.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5010.java
text:
```scala
r@@eturn super.get(index);

/*
 * $Id: TestList.java 5394 2006-04-16 13:36:52 +0000 (Sun, 16 Apr 2006)
 * jdonnerstag $ $Revision$ $Date: 2004-12-19 18:21:51 +0100 (So, 19 Dez
 * 2004) $
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.examples.displaytag.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Just a utility class for testing out the table and column tags. When this
 * class is created, it loads itself with a number of ListObjects that are shown
 * throughout the various example pages that exercise the table object. If
 * created via the default constructor, this loads itself with 60 ListObjects.
 * 
 * @author epesh (wicket.examples.wicket.examples.displaytag)
 */
public class TestList extends ArrayList<ListObject> implements Serializable
{

	/**
	 * Creats a TestList that is filled with 60 ListObjects suitable for
	 * testing.
	 */
	public TestList()
	{
		super();

		for (int j = 0; j < 60; j++)
		{
			add(new ListObject());
		}
	}

	/**
	 * Creates a TestList that is filled with [size] ListObjects suitable for
	 * testing.
	 * 
	 * @param size
	 *            int size of the list
	 * @param duplicates
	 *            boolean put duplicates in the list
	 */
	public TestList(int size, boolean duplicates)
	{
		if (duplicates)
		{
			// generate a random number between 1 and 3 and duplicate that many
			// number of times.
			for (int j = 0; j < size; j++)
			{
				ListObject object1 = new ListObject();
				ListObject object2 = new ListObject();
				ListObject object3 = new ListObject();

				int random = new Random().nextInt(3);
				for (int k = 0; k <= random; k++)
				{
					add(object1);
				}

				object1.setId(object2.getId());

				random = new Random().nextInt(3);
				for (int k = 0; k <= random; k++)
				{
					add(object1);
					add(object2);
				}

				object1.setEmail(object3.getEmail());

				random = new java.util.Random().nextInt(3);
				for (int k = 0; k <= random; k++)
				{
					add(object1);
				}
			}
		}
		else
		{
			for (int j = 0; j < size; j++)
			{
				add(new ListObject());
			}
		}
	}

	/**
	 * Returns a ListObject using get(index) from the Array.
	 * 
	 * @param index
	 *            int index of the List object into the array
	 * @return ListObject
	 */
	public ListObject getItem(int index)
	{
		return (ListObject)super.get(index);
	}

}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5010.java