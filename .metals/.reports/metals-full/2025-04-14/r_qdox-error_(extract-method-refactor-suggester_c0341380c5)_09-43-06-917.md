error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17218.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17218.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17218.java
text:
```scala
l@@oadTexture(getGlCanvas(), IMAGE, 0, textureOut);

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl.examples;


import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.opengl.*;
import org.eclipse.swt.widgets.*;

class TextureTab extends OpenGLTab {
	private float yrot = 0, zrot = 0;
	private float xPos = 0.0f, yPos = 0.0f, zPos = -20;
	private int texture = 0;
	private float[][][] points = new float[45][45][3];
	private final static String IMAGE = "images/splash.bmp";
	private final static int SLEEP_LENGTH = 50;

	/**
	 * @see OpenGLTab#createControls(Composite)
	 */
	void createControls(Composite composite) {
		Group movementGroup = new Group(composite, SWT.NONE);
		movementGroup.setText("Translation");
		movementGroup.setLayout(new GridLayout(2, false));

		new Label(movementGroup, SWT.NONE).setText("X:");
		final Slider xMove = new Slider(movementGroup, SWT.NONE);
		xMove.setIncrement(1);
		xMove.setMaximum(12);
		xMove.setMinimum(0);
		xMove.setThumb(2);
		xMove.setPageIncrement(2);
		xMove.setSelection(5);
		xMove.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				xPos = xMove.getSelection() - 5;
			}
		});

		new Label(movementGroup, SWT.NONE).setText("Y:");
		final Slider yMove = new Slider(movementGroup, SWT.NONE);
		yMove.setIncrement(1);
		yMove.setMaximum(12);
		yMove.setMinimum(0);
		yMove.setThumb(2);
		yMove.setPageIncrement(2);
		yMove.setSelection(5);
		yMove.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				yPos = yMove.getSelection() - 5;
			}
		});

		new Label(movementGroup, SWT.NONE).setText("Z:");
		final Slider zMove = new Slider(movementGroup, SWT.NONE);
		zMove.setIncrement(1);
		zMove.setMaximum(24);
		zMove.setMinimum(0);
		zMove.setThumb(4);
		zMove.setPageIncrement(2);
		zMove.setSelection(10);
		zMove.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				zPos = zMove.getSelection() - 30;
			}
		});
	}

	/**
	 * @see OpenGLTab#getSleepLength()
	 */
	int getSleepLength() {
		return SLEEP_LENGTH;
	}
	
	/**
	 * @see OpenGLTab#getTabText()
	 */
	String getTabText() {
		return "Texture";
	}

	/**
	 * @see OpenGLTab#init()
	 */
	void init() {
		//GL.glClearColor(0.0f, 0.0f, 0.3f, 1.0f); 
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glClearDepth(1.0f);
		int[] textureOut = new int[1];
		GL.glGenTextures(1, textureOut);
		loadTexture(getContext(), IMAGE, 0, textureOut);
		texture = textureOut[0];
		GL.glDepthFunc(GL.GL_LEQUAL);
		GL.glPolygonMode(GL.GL_BACK, GL.GL_FILL);
		GL.glPolygonMode(GL.GL_FRONT, GL.GL_LINE);
		GL.glEnable(GL.GL_LIST_MODE);
		GL.glEnable(GL.GL_TEXTURE_2D);
		GL.glEnable(GL.GL_DEPTH_TEST);
		// initialize points	
		for (int x = 0; x < 45; x++) {
			for (int y = 0; y < 45; y++) {
				points[x][y][0] = x / 5.0f - 4.5f;
				points[x][y][1] = y / 5.0f - 4.5f;
				points[x][y][2] =
					(float) Math.sin(
						(((x / 5.0f) * 40.0f) / 360.0f) * Math.PI * 2);
			}
		}
	}

	/**
	 * @see OpenGLTab#renderScene()
	 */
	void renderScene() {
		GL.glClear(
			GL.GL_COLOR_BUFFER_BIT
 GL.GL_DEPTH_BUFFER_BIT
 GL.GL_STENCIL_BUFFER_BIT);
		GL.glLoadIdentity();
		GL.glTranslatef(xPos, yPos, zPos);
		GL.glRotatef(yrot, 0.0f, 1.0f, 0.0f);
		GL.glRotatef(zrot, 0.0f, 0.0f, 1.0f);
		GL.glBindTexture(GL.GL_TEXTURE_2D, texture);

		GL.glBegin(GL.GL_QUADS);
		for (int x = 0; x < 44; x++) {
			for (int y = 0; y < 44; y++) {
				float fx = x / 44.0f;
				float fy = y / 44.0f;
				float fxb = (x + 1) / 44.0f;
				float fyb = (y + 1) / 44.0f;
				GL.glTexCoord2f(fx, fy);
				GL.glVertex3f(
					points[x][y][0],
					points[x][y][1],
					points[x][y][2]);
				GL.glTexCoord2f(fx, fyb);
				GL.glVertex3f(
					points[x][y + 1][0],
					points[x][y + 1][1],
					points[x][y + 1][2]);
				GL.glTexCoord2f(fxb, fyb);
				GL.glVertex3f(
					points[x + 1][y + 1][0],
					points[x + 1][y + 1][1],
					points[x + 1][y + 1][2]);
				GL.glTexCoord2f(fxb, fy);
				GL.glVertex3f(
					points[x + 1][y][0],
					points[x + 1][y][1],
					points[x + 1][y][2]);
			}
		}
		GL.glEnd();
		// perform wave motion
		for (int y = 0; y < 45; y++) {
			float hold = points[0][y][2];
			for (int x = 0; x < 44; x++) {
				points[x][y][2] = points[x + 1][y][2];
			}
			points[44][y][2] = hold;
		}
		yrot += 0.9f;
		zrot += 0.6f;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17218.java