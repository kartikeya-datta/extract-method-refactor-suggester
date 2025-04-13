error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5672.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5672.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5672.java
text:
```scala
L@@ist<IBreadCrumbParticipant> allBreadCrumbParticipants();

/*
 * $Id: org.eclipse.jdt.ui.prefs 5004 2006-03-17 20:47:08 -0800 (Fri, 17 Mar
 * 2006) eelco12 $ $Revision: 5004 $ $Date: 2006-03-17 20:47:08 -0800 (Fri, 17
 * Mar 2006) $
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
package wicket.extensions.breadcrumb;

import java.io.Serializable;
import java.util.List;

import wicket.extensions.breadcrumb.panel.BreadCrumbPanel;

/**
 * Bread crumbs provide a means to track certain history of client actions.
 * Bread crumbs are typically rendered as a list of links, and are useful when
 * users 'dig deeper' into the site structure so that they can find their way
 * back again and have a notion of where they currently are.
 * <p>
 * Bread crumbs in the original sense just represent where people are in a site
 * hierarchy. For example, when browsing a product site, bread crumbs could look
 * like this:
 * 
 * <pre>
 *            Home &gt; Products &amp; Solutions &gt; Hardware &gt; Desktop Systems
 * </pre>
 * 
 * or
 * 
 * <pre>
 *            World &gt; Europe &gt; The Netherlands &gt; Utrecht
 * </pre>
 * 
 * These items would be rendered as links to the corresponding site location.
 * </p>
 * Classes that implement this interface are responsible for managing such a
 * bread crumb structure. A {@link BreadCrumbBar typical implementation} regards
 * bread crumbs as a stack. When
 * {@link #setActive(IBreadCrumbParticipant) a bread crumb is activated} that
 * was not in the stack yet, it would add it to the stack, or when a bread crumb
 * is activated that is already on the stack, it would roll back to the
 * corresponding depth.
 * <p>
 * This model does not make any presumptions on how it should interact with
 * components. Just that there is a list of
 * {@link IBreadCrumbParticipant bread crumb participants}, and the notion of a
 * currently active bread crumb participant.
 * </p>
 * <p>
 * A {@link IBreadCrumbParticipant bread crumb participant} is not an actual
 * bread crump, but rather a proxy to components that represent a certain
 * location relative to other bread crumbs in this model, and a means to get the
 * bread crumb title, which is typically rendered as a link label of the actual
 * bread crumb. The actual bread crumbs are supposed to be rendered by a
 * component that works together with this model. I choose this model as this
 * would suit what I think is one of the nicest patterns:
 * {@link BreadCrumbPanel bread crumb aware panels}.
 * </p>
 * 
 * @author Eelco Hillenius
 */
public interface IBreadCrumbModel extends Serializable
{
	/**
	 * Adds a bread crumb model listener.
	 * 
	 * @param listener
	 *            The listener to add
	 */
	void addListener(IBreadCrumbModelListener listener);

	/**
	 * Lists the bread crumb participants in this model.
	 * 
	 * @return The bread crumbs particpants, as list with
	 *         {@link IBreadCrumbParticipant bread crumb participants}.
	 */
	List allBreadCrumbParticipants();

	/**
	 * Gets the currently active participant, if any.
	 * 
	 * @return The currently active participant, may be null
	 */
	IBreadCrumbParticipant getActive();

	/**
	 * Removes a bread crumb model listener.
	 * 
	 * @param listener
	 *            The listener to remove
	 */
	void removeListener(IBreadCrumbModelListener listener);

	/**
	 * Sets the {@link IBreadCrumbParticipant bread crumb} as the active one.
	 * Implementations should call
	 * {@link IBreadCrumbModelListener#breadCrumbAdded(IBreadCrumbParticipant) bread crumb added}
	 * when the bread crumb was not yet part of the model, and
	 * {@link IBreadCrumbModelListener#breadCrumbRemoved(IBreadCrumbParticipant) bread crumb removed}
	 * for every crumb that was removed as the result of this call.
	 * 
	 * @param breadCrumbParticipant
	 *            The bread crump that should be set as the currently active
	 */
	void setActive(IBreadCrumbParticipant breadCrumbParticipant);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5672.java