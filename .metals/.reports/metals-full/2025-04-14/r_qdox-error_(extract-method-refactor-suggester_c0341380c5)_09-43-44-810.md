error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4613.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4613.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4613.java
text:
```scala
(@@(JoglInput)((JoglApplication)Gdx.app).getInput()).processEvents();

/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.badlogic.gdx.backends.jogl;

import java.awt.Toolkit;
import java.util.List;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.openal.OpenALAudio;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Implements the {@link Graphics} interface with Jogl.
 * 
 * @author mzechner
 * 
 */
public class JoglGraphics extends JoglGraphicsBase implements GLEventListener {
	ApplicationListener listener = null;
	boolean useGL2;
	boolean created = false;	

	public JoglGraphics (ApplicationListener listener, String title, int width, int height, boolean useGL2) {
		initialize(title, width, height, useGL2);
		if (listener == null) throw new GdxRuntimeException("RenderListener must not be null");
		this.listener = listener;
	}

	public void create () {
		super.create();
	}

	public void pause () {
		super.pause();
		canvas.getContext().makeCurrent();
		listener.pause();
	}

	public void resume () {
		canvas.getContext().makeCurrent();
		listener.resume();
		super.resume();
	}

	@Override public void init (GLAutoDrawable drawable) {
		initializeGLInstances(drawable);

		if (!created) {
			listener.create();
			synchronized (this) {
				paused = false;
			}
			created = true;
		}
	}

	@Override public void reshape (GLAutoDrawable drawable, int x, int y, int width, int height) {
		listener.resize(width, height);
	}

	@Override public void display (GLAutoDrawable arg0) {
		synchronized (this) {
			if (!paused) {
				updateTimes();
				synchronized (((JoglApplication)Gdx.app).runnables) {
					List<Runnable> runnables = ((JoglApplication)Gdx.app).runnables;
					for(int i = 0; i < runnables.size(); i++) {
						runnables.get(i).run();
					}
					runnables.clear();
				}
				((JoglInput)Gdx.input).processEvents();
				listener.render();
				((OpenALAudio)Gdx.audio).update();
			}
		}
	}

	@Override public void displayChanged (GLAutoDrawable arg0, boolean arg1, boolean arg2) {

	}

	public void destroy () {
		canvas.getContext().makeCurrent();
		listener.dispose();
	}

	@Override public float getPpiX () {
		return Toolkit.getDefaultToolkit().getScreenResolution();
	}

	@Override public float getPpiY () {
		return Toolkit.getDefaultToolkit().getScreenResolution();
	}

	@Override public float getPpcX () {
		return (Toolkit.getDefaultToolkit().getScreenResolution() / 2.54f);
	}

	@Override public float getPpcY () {
		return (Toolkit.getDefaultToolkit().getScreenResolution() / 2.54f);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4613.java