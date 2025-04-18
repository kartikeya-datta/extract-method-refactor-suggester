error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11903.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11903.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11903.java
text:
```scala
g@@lCanvas = new GLCanvas(tabFolderPage, SWT.NO_BACKGROUND, data);

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


import org.eclipse.opengl.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.opengl.*;

/**
 * <code>OpenGLTab</code> is the abstract superclass of every page
 * in the example's tab folder.  Each page in the tab folder
 * displays a different example.
 *
 * An OpenGLTab itself is not a control but instead provides a hierarchy
 * with which to share code that is common to every page in the folder.
 */
abstract class OpenGLTab {
	private GLCanvas glCanvas;
	private Composite tabFolderPage;
	private boolean stencilSupport;
	private final static int DEFAULT_SLEEP_LENGTH = 100;

	/**
	 * Creates this tab's controls.  Subclasses must override.
	 *
	 * @param composite the parent composite
	 */
	abstract void createControls(Composite composite);

	/**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder the parent tab folder
	 * @return the new page
	 */
	Composite createTabFolderPage(TabFolder tabFolder) {
		tabFolderPage = new Composite(tabFolder, SWT.NONE);
		tabFolderPage.setLayout(new GridLayout(2, false));

		GridData gridData = new GridData();
		gridData.heightHint = 400;
		gridData.widthHint = 400;
		gridData.verticalAlignment = GridData.BEGINNING;
		GLData data = new GLData();
		data.doubleBuffer = true;
		data.stencilSize = 8;
		glCanvas = new GLCanvas(tabFolderPage, SWT.NONE, data);
		glCanvas.setLayout(new GridLayout());
		glCanvas.setLayoutData(gridData);
		glCanvas.setSize(400, 400);		// needed for windows

		gridData = new GridData();
		gridData.verticalAlignment = GridData.BEGINNING;
		Composite controlComposite = new Composite(tabFolderPage, SWT.NONE);
		controlComposite.setLayout(new GridLayout());
		controlComposite.setLayoutData(gridData);

		// create the OpenGL Screen and controls
		setCurrent();
		setupViewingArea();

		// determine if native stencil support is available
		int[] param = new int[1];
		GL.glGetIntegerv(GL.GL_STENCIL_BITS, param);
		stencilSupport = param[0] != 0;

		init();

		if (!isStencilSupportNeeded() || hasStencilSupport()) {
			createControls(controlComposite);
		} else {
			Label label = new Label(controlComposite, SWT.NONE);
			label.setText("This tab requires native stencil support.");
		}

		return tabFolderPage;
	}

	/**
	 * Disposes all resources allocated by this tab.
	 */
	void dispose() {
	}

	/**
	 * Returns the glCanvas for this tab.
	 * 
	 * @return Canvas
	 */
	GLCanvas getGlCanvas() {
		return glCanvas;
	}

	/**
	 * Returns the length of time in milliseconds that the example
	 * should sleep between animation redraws.  As this length
	 * increases, user responsiveness increases and the frequency of
	 * animation redraws decreases.  Subclasses with moving animations
	 * may wish to override this default implementation to return a
	 * smaller value if their animations do not occur frequently enough. 
	 *
	 * @return the length of time in milliseconds to sleep between redraws
	 */
	int getSleepLength() {
		return DEFAULT_SLEEP_LENGTH;	
	}

	/**
	 * Returns the text for this tab.  Subclasses must override.
	 *
	 * @return the text for the tab item
	 */
	abstract String getTabText();

	/**
	 * Returns whether this machine has native stencils support.
	 * 
	 * @return boolean
	 */
	boolean hasStencilSupport() {
		return stencilSupport;
	}

	/**
	 * Initialize OpenGL resources for this tab.  Subclasses must override.
	 */
	abstract void init();

	/**
	 * Loads a texture.
	 * 
	 * @param context
	 * @param fileName
	 * @param index
	 * @param texture[]
	 */
	static void loadTexture(GLCanvas context, String fileName, int index, int[] texture) {
		GL.glBindTexture(GL.GL_TEXTURE_2D, texture[index]);
		ImageData source =
			new ImageData(OpenGLTab.class.getResourceAsStream(fileName));
		Image image = new Image(Display.getCurrent(), source);
		Image newImage = new Image(Display.getCurrent(), 256, 256);
		GC gc = new GC(newImage);
		gc.drawImage(image, 0, 0, source.width, source.height, 0, 0, 256, 256);
		source = newImage.getImageData();
		gc.dispose();
		source = ImageDataUtil.convertImageData(source);
		newImage.dispose();
		image.dispose();
		GL.glTexImage2D(
			GL.GL_TEXTURE_2D, 0, 3, 
			source.width, source.height, 0,
			GL.GL_RGB, GL.GL_UNSIGNED_BYTE, source.data);
		GL.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		GL.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
	}

	/**
	 * Renders this tab.
	 */
	void render() {
		setCurrent();
		if (!isStencilSupportNeeded() || hasStencilSupport()) {
			renderScene();
		} else {
			GL.glClear(GL.GL_COLOR_BUFFER_BIT);
		}
	}

	/**
	 * Renders the scene for this tab.  Subclasses must override.
	 */
	abstract void renderScene();

	/**
	 * Returns whether this tab requires stencil support in order to display
	 * properly. Subclasses may wish to override this method.
	 * 
	 * @return boolean
	 */
	boolean isStencilSupportNeeded() {
		return false;
	}

	/**
	 * Sets this rendering context to be current.
	 */
	void setCurrent() {
		glCanvas.setCurrent();
	}
	
	/**
	 * Sets up the viewing area for the OpenGL screen.  The default
	 * behavior is to use a perspective view, but there also exist frustrum
	 * and ortho views.  Subclasses may wish to override this method.
	 */
	void setupViewingArea() {
		Rectangle rect = glCanvas.getClientArea();
		int width = rect.width;
		int height = rect.height;
		height = Math.max(height, 1);
		GL.glViewport(0, 0, width, height);
		GL.glMatrixMode(GL.GL_PROJECTION);	// select the projection matrix
		GL.glLoadIdentity();				// reset the projection matrix
		float fAspect = (float) width / (float) height;
		GLU.gluPerspective(45.0f, fAspect, 0.5f, 400.0f);
		GL.glMatrixMode(GL.GL_MODELVIEW);	// select the modelview matrix
		GL.glLoadIdentity();
	}

	/**
	 * Swaps the buffers.
	 */
	void swap() {
		glCanvas.swapBuffers();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11903.java