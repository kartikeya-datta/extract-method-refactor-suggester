error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11650.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11650.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11650.java
text:
```scala
p@@ublic class BodyFrame extends WebPage<Void>

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
package org.apache.wicket.examples.frames;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageMap;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


/**
 * Body frame page for the frames example.
 * 
 * @author Eelco Hillenius
 */
public class BodyFrame extends WebPage
{
	/**
	 * Model that returns the url to the bookmarkable page that is set in the current frame target.
	 */
	private final class FrameModel implements IModel<CharSequence>
	{
		/**
		 * @see org.apache.wicket.model.Model#getObject()
		 */
		public CharSequence getObject()
		{
			return RequestCycle.get().urlFor(PageMap.forName(RIGHT_FRAME_NAME),
				frameTarget.getFrameClass(), null);
		}

		/**
		 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
		 */
		public void setObject(CharSequence object)
		{
		}

		/**
		 * @see org.apache.wicket.model.IDetachable#detach()
		 */
		public void detach()
		{
		}
	}

	/** name for page map etc. */
	public static final String RIGHT_FRAME_NAME = "right";

	private final FrameTarget frameTarget = new FrameTarget(Page1.class);

	/**
	 * Constructor
	 */
	public BodyFrame()
	{
		// create a new page instance, passing this 'master page' as an argument
		LeftFrame leftFrame = new LeftFrame(this);
		// get the url to that page
		CharSequence leftFrameSrc = RequestCycle.get().urlFor(leftFrame);
		// and create a simple component that modifies it's src attribute to
		// hold the url to that frame
		WebComponent leftFrameTag = new WebComponent("leftFrame");
		leftFrameTag.add(new AttributeModifier("src", new Model((Serializable)leftFrameSrc)));
		add(leftFrameTag);

		// make a simple component for the right frame tag
		WebComponent rightFrameTag = new WebComponent("rightFrame");
		// and this time, set a model which retrieves the url to the currently
		// set frame class in the frame target
		rightFrameTag.add(new AttributeModifier("src", new FrameModel()));
		add(rightFrameTag);
	}

	/**
	 * Gets frameTarget.
	 * 
	 * @return frameTarget
	 */
	public FrameTarget getFrameTarget()
	{
		return frameTarget;
	}

	/**
	 * @see org.apache.wicket.Component#isVersioned()
	 */
	@Override
	public boolean isVersioned()
	{
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11650.java