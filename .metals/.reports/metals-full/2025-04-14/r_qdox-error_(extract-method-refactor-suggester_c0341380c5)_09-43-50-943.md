error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/819.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/819.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/819.java
text:
```scala
protected b@@oolean invalidated = true;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Layout;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;

/** Base class for all UI widgets. A widget implements the {@link Layout} interface which has a couple of features.</p>
 * 
 * A widget has a preferred width and height which it will use if possible, e.g. if it is not in a {@link Table} ({@link Pane},
 * {@link Window}) or a {@link SplitPane} or a {@link ScrollPane}. In case it is contained in one of the aforementioned
 * containers, the preferred width and height will be used to guide the layouting mechanism employed by those containers.</p>
 * 
 * A widget can be invalidated, e.g. by a Container changing its available space in the layout, in which case it will layout
 * itself at the next oportunity to do so.</p>
 * 
 * Invalidation can also be triggered manually via a call to {@link #invalidate()} or {@link #invalidateHierarchy()}. The former
 * will tell the Widget to only invalidate itself. The later will also invalidate all the widget's parents. The later mechanism is
 * used in case the widget was modified and the container it is contained in must relayout itself due to this modification as
 * well.
 * @author mzechner */
public abstract class Widget extends Actor implements Layout {
	public float prefWidth;
	public float prefHeight;
	protected boolean invalidated = false;

	/** Creates a new widget without a name or preferred size. */
	public Widget () {
		super(null);
	}

	/** Creates a new widget with the preferred width and height
	 * @param name the name
	 * @param prefWidth the preferred width
	 * @param prefHeight the preferred height */
	public Widget (String name, float prefWidth, float prefHeight) {
		super(name);
		this.prefWidth = prefWidth;
		this.prefHeight = prefHeight;
	}

	@Override
	public float getPrefWidth () {
		return prefWidth;
	}

	@Override
	public float getPrefHeight () {
		return prefHeight;
	}

	/** Invalidates this widget, causing it to relayout itself at the next oportunity. */
	public void invalidate () {
		this.invalidated = true;
	}

	/** Invalidates this widget and all its parents, causing all involved widgets to relayout themselves at the next oportunity. */
	public void invalidateHierarchy () {
		invalidate();
		Group parent = this.parent;
		while (parent != null) {
			if (parent instanceof Layout) ((Layout)parent).invalidate();
			parent = parent.parent;
		}
	}

	@Override
	public Actor hit (float x, float y) {
		return x > 0 && x < width && y > 0 && y < height ? this : null;
	}

	/** Sets the preferred width and height of this widget. Invalidates all parents.
	 * @param prefWidth the preferred width
	 * @param prefHeight the preferred height */
	public void setPrefSize (int prefWidth, int prefHeight) {
		this.prefWidth = width = prefWidth;
		this.prefHeight = height = prefHeight;
		invalidateHierarchy();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/819.java