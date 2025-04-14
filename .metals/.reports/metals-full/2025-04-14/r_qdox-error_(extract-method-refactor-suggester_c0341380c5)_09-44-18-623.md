error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6229.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6229.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6229.java
text:
```scala
s@@ettings.configure("development", "src/eelco/java");

/*
 * $Id$
 * $Revision$
 * $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package navmenu;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import wicket.ApplicationSettings;
import wicket.protocol.http.WebApplication;

/**
 * WicketServlet class for nested structure example.
 *
 * @author Eelco Hillenius
 */
public class MenuApplication extends WebApplication
{
    /**
     * Constructor.
     */
    public MenuApplication()
    {
        getPages().setHomePage(Home.class);
		ApplicationSettings settings = getSettings();
		settings.configure("development", "src/java");
		settings.setStripWicketTags(true);
    }

    /**
     * @return the menu as a tree model
     */
    public static TreeModel getMenu()
    {
		// create tree
		TreeModel model = null;
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT"); // dummy
		DefaultMutableTreeNode a0 = new DefaultMutableTreeNode(
				new MenuItem("Home", Home.class, null));
		root.add(a0);
		DefaultMutableTreeNode a1 = new DefaultMutableTreeNode(
				new MenuItem("Images", Page1.class, null));
		root.add(a1);
		DefaultMutableTreeNode a2 = new DefaultMutableTreeNode(
				new MenuItem("Templates", Page2.class, null));
//		DefaultMutableTreeNode a2a = new DefaultMutableTreeNode(
//				new MenuItem("Ognl", Page2a.class, null));
//		a2.add(a2a);
//		DefaultMutableTreeNode a2b = new DefaultMutableTreeNode(
//				new MenuItem("Velocity", Page2b.class, null));
//		a2.add(a2b);
		root.add(a2);
		DefaultMutableTreeNode a3 = new DefaultMutableTreeNode(
				new MenuItem("Users", Page3.class, null));
//		DefaultMutableTreeNode a3a = new DefaultMutableTreeNode(
//				new MenuItem("Truus", Page2a.class, null));
//		a3.add(a3a);
//		DefaultMutableTreeNode a3b = new DefaultMutableTreeNode(
//				new MenuItem("Mien", Page2b.class, null));
//		a3.add(a3b);
		root.add(a3);
		DefaultMutableTreeNode a4 = new DefaultMutableTreeNode(
				new MenuItem("Preferences", Page4.class, null));
		root.add(a4);
		// TODO this is just one level. We should be able to handle more than that
		model = new DefaultTreeModel(root);
		return model;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6229.java