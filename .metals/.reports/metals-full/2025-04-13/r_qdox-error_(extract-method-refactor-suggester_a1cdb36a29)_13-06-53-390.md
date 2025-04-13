error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17217.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17217.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17217.java
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

class StencilTab extends OpenGLTab {
	private abstract class Shape {
		private String name;
		/**
		 * Constructor
		 * 
		 * @param name the shape's name
		 */
		Shape(String name) {
			super();
			this.name = name;
		}
		/**
		 * Draws this shape.
		 */
		abstract void draw();
		/**
		 * Returns the name.
		 * 
		 * @return String
		 */
		String getName() {
			return name;
		}
	}

	private Shape[] shapes = new Shape[5];
	private Shape currentShape;
	private float xPos = 0, yPos = 0;
	private float size = 0.45f;
	private int texture;
	private int quadratic;
	private final static String IMAGE = "images/splash.bmp";
	private final static int SLEEP_LENGTH = 50;

	/**
	 * @see OpenGLTab#createControls(Composite)
	 */
	void createControls(Composite composite) {
		Composite controls = new Composite(composite, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		controls.setLayout(layout);
		new Label(controls, SWT.NONE).setText("Object:");
		final Combo shapeCombo = new Combo(controls, SWT.READ_ONLY);
		for (int i = 0; i < shapes.length; i++) {
			shapeCombo.add(shapes[i].getName());
		}
		shapeCombo.select(0);
		shapeCombo.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				currentShape = shapes[shapeCombo.getSelectionIndex()];
			}
		});

		new Label(composite, SWT.NONE).setText("Size:");
		final Slider sizeSlider = new Slider(composite, SWT.HORIZONTAL);
		sizeSlider.setValues(0, 15, 75, 5, 5, 10);
		sizeSlider.setSelection(45);
		sizeSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				size = ((float) sizeSlider.getSelection()) / 100;
			}
		});
	}

	/**
	 * @see OpenGLTab#dispose()
	 */
	void dispose() {
		super.dispose();
		GLU.gluDeleteQuadric(quadratic);
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
		return "Stencil";
	}

	/**
	 * @see OpenGLTab#init()
	 */
	void init() {
		if (!hasStencilSupport()) return;
		
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glClearDepth(1.0f);
		int[] textureOut = new int[1];
		GL.glGenTextures(1, textureOut);
		loadTexture(getContext(), IMAGE, 0, textureOut);
		this.texture = textureOut[0];
		quadratic = GLU.gluNewQuadric();
		GLU.gluQuadricNormals(quadratic, GLU.GLU_SMOOTH);
		GL.glDepthFunc(GL.GL_LEQUAL);
		GL.glEnable(GL.GL_STENCIL_TEST);
		GL.glEnable(GL.GL_TEXTURE_2D);
		GL.glEnable(GL.GL_DEPTH_TEST);
		// create shapes
		shapes[0] = new Shape("Triangle") {
			public void draw() {
				GL.glBegin(GL.GL_TRIANGLES);
				GL.glVertex3f(0.0f, size, 0.0f);
				GL.glVertex3f(-size, -size, 0.0f);
				GL.glVertex3f(size, -size, 0.0f);
				GL.glEnd();
			}
		};

		shapes[1] = new Shape("Disk") {
			public void draw() {
				GLU.gluDisk(quadratic, 0.0f, size, 32, 32);
			}
		};

		shapes[2] = new Shape("Square") {
			public void draw() {
				GL.glRectf(-size, -size, size, size);
			}
		};

		shapes[3] = new Shape("Hour Glass") {
			public void draw() {
				GL.glBegin(GL.GL_TRIANGLES);
				GL.glVertex3f(0.0f, size, 0.0f);
				GL.glVertex3f(-size, -size, 0.0f);
				GL.glVertex3f(size, -size, 0.0f);
				GL.glVertex3f(0.0f, -size, 0.0f);
				GL.glVertex3f(size, size, 0.0f);
				GL.glVertex3f(-size, size, 0.0f);
				GL.glEnd();
			}
		};

		shapes[4] = new Shape("Star") {
			public void draw() {
				GL.glBegin(GL.GL_TRIANGLES);
				GL.glVertex3f(-0.3f, 0, 0.0f);
				GL.glVertex3f(2 * size - 0.3f, 0, 0.0f);
				GL.glVertex3f(size - 0.3f, 2 * size * 0.85f, 0.0f);
				GL.glVertex3f(2 * size - 0.3f, size, 0.0f);
				GL.glVertex3f(0 - 0.3f, size, 0.0f);
				GL.glVertex3f(size - 0.3f, -size * 0.85f, 0.0f);
				GL.glEnd();
			}
		};

		currentShape = shapes[0];
	}

	/**
	 * @see OpenGLTab#isStencilSupportNeeded
	 */
	boolean isStencilSupportNeeded() {
		return true;
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
		GL.glTranslatef(
			(float) (1.5 * Math.cos(xPos)),
			(float) (1.0 * Math.sin(yPos)),
			-3.0f);
		xPos += 0.15f;
		yPos += 0.3f;

		GL.glColorMask(false, false, false, false);
		GL.glStencilFunc(GL.GL_ALWAYS, 1, 1);
		GL.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_REPLACE);
		GL.glDisable(GL.GL_DEPTH_TEST);
		GL.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		currentShape.draw();

		GL.glEnable(GL.GL_DEPTH_TEST);
		GL.glColorMask(true, true, true, true);
		GL.glStencilFunc(GL.GL_EQUAL, 1, 1);
		GL.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_KEEP);

		GL.glLoadIdentity();
		// load the texture behind the stencil object
		GL.glTranslatef(0.0f, 0.0f, -3.1f);
		GL.glBindTexture(GL.GL_TEXTURE_2D, texture);
		GL.glBegin(GL.GL_QUADS);
		GL.glTexCoord2f(0.0f, 0.0f);
		GL.glVertex3f(-0.85f, -0.85f, 1.0f);
		GL.glTexCoord2f(1.0f, 0.0f);
		GL.glVertex3f(0.85f, -0.85f, 1.0f);
		GL.glTexCoord2f(1.0f, 1.0f);
		GL.glVertex3f(0.85f, 0.85f, 1.0f);
		GL.glTexCoord2f(0.0f, 1.0f);
		GL.glVertex3f(-0.85f, 0.85f, 1.0f);
		GL.glEnd();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17217.java