error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5138.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5138.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5138.java
text:
```scala
f@@ontDialog.setFontList(new FontData[] {fontData});

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.opengl.examples;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.opengl.*;
import org.eclipse.swt.widgets.*;

class OutlineTextTab extends OpenGLTab {
	private Text messageText;
	private FontData fontData;
	private float[] textColor = { 1.0f, 0.0f, 0.0f };
	private boolean fill = true;
	private float extrude = 0.0f;
	private float xPos = 0.0f, yPos = 0.0f, zPos = -10.0f;
	private float xRot = 0.0f;
	private int listIndexBase;
	private final static int LIST_INDEX_SIZE = 256;
	private final static int DEFAULT_FONT_SIZE = 24;
	private final static String DEFAULT_FONT_NAME = "Arial";
	private final static int SLEEP_LENGTH = 50;

	/**
	 * @see OpenGLTab#createControls(Composite)
	 */
	void createControls(final Composite composite) {
		if (!SWT.getPlatform().startsWith("win32")) {
			new Label(composite, SWT.NONE).setText("This tab requires win32.");
			return;
		}
		
		Group movementGroup = new Group(composite, SWT.NONE);
		movementGroup.setText("Translation");
		movementGroup.setLayout(new GridLayout(2, false));

		new Label(movementGroup, SWT.NONE).setText("X:");
		final Slider xMove = new Slider(movementGroup, SWT.NONE);
		xMove.setIncrement(1);
		xMove.setMaximum(22);
		xMove.setMinimum(0);
		xMove.setThumb(2);
		xMove.setPageIncrement(2);
		xMove.setSelection(10);
		xMove.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				xPos = xMove.getSelection() - 10;
			}
		});

		new Label(movementGroup, SWT.NONE).setText("Y:");
		final Slider yMove = new Slider(movementGroup, SWT.NONE);
		yMove.setIncrement(1);
		yMove.setMaximum(22);
		yMove.setMinimum(0);
		yMove.setThumb(2);
		yMove.setPageIncrement(2);
		yMove.setSelection(10);
		yMove.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				yPos = yMove.getSelection() - 10;
			}
		});

		new Label(movementGroup, SWT.NONE).setText("Z:");
		final Slider zMove = new Slider(movementGroup, SWT.NONE);
		zMove.setIncrement(1);
		zMove.setMaximum(22);
		zMove.setMinimum(0);
		zMove.setThumb(2);
		zMove.setPageIncrement(2);
		zMove.setSelection(10);
		zMove.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				zPos = zMove.getSelection() - 20;
			}
		});

		Composite textGroup = new Composite(composite,SWT.NONE);
		GridLayout layout = new GridLayout(2,false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		textGroup.setLayout(layout);
		textGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		
		new Label(textGroup, SWT.NONE).setText("Text:");
		messageText = new Text(textGroup, SWT.BORDER);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		data.grabExcessHorizontalSpace = true;
		messageText.setLayoutData(data);
		messageText.setText("OpenGL - SWT");
		
		new Label(composite, SWT.NONE).setText("Extrude:");
		final Slider extrudeSlider = new Slider(composite, SWT.NONE);
		extrudeSlider.setIncrement(1);
		extrudeSlider.setMaximum(22);
		extrudeSlider.setMinimum(0);
		extrudeSlider.setThumb(2);
		extrudeSlider.setPageIncrement(2);
		extrudeSlider.setSelection(0);
		extrudeSlider.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				extrude = (float) extrudeSlider.getSelection() / 10;
				getContext().loadOutlineFont(
					fontData, extrudeSlider.getDisplay(),
					listIndexBase, 0, 255, 0.0f, extrude,
					fill ? GL.GL_POLYGON : GL.GL_LINE,
					null);
			}
		});

		final Button fontSelectButton = new Button(composite, SWT.NONE);
		fontSelectButton.setText("Set Font");
		final ColorSelectionGroup colorGroup =
			new ColorSelectionGroup(composite, SWT.NONE);
		colorGroup.setText("Text color");
		colorGroup.addColorSelectionListener(new IColorSelectionListener() {
			public void handleColorSelection(RGB rgb) {
				GL.glColor3ub((byte) rgb.red, (byte) rgb.green, (byte) rgb.blue);
			}
		});
		
		fontSelectButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				FontDialog fontDialog = new FontDialog(fontSelectButton.getShell());
				fontDialog.setText("Choose Font Options");
				double[] currentColor = new double[4];
				GL.glGetDoublev(GL.GL_CURRENT_COLOR, currentColor);
				fontDialog.setRGB(
					new RGB(
						(int) currentColor[0] * 255,
						(int) currentColor[1] * 255,
						(int) currentColor[2] * 255));
				fontDialog.setFontData(fontData);
				FontData result = fontDialog.open();
				if (result != null) {
					fontData = result;
					RGB rgb = fontDialog.getRGB();
					GL.glColor3ub((byte) rgb.red, (byte) rgb.green, (byte) rgb.blue);
					colorGroup.setRGB(rgb);
					getContext().loadOutlineFont(
						fontData, fontSelectButton.getDisplay(),
						listIndexBase, 0, 255, 0.0f, extrude,
						fill ? GL.GL_POLYGON : GL.GL_LINE,
						null);
				}
			}
		});

		final Button fillButton = new Button(composite, SWT.CHECK);
		fillButton.setText("Fill");
		fillButton.setSelection(true);
		fillButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				fill = fillButton.getSelection();
				getContext().loadOutlineFont(
					fontData, fillButton.getDisplay(),
					listIndexBase, 0, 255, 0.0f, extrude,
					fill ? GL.GL_POLYGON : GL.GL_LINE,
					null);
			}
		});
	}

	/**
	 * @see OpenGLTab#dispose()
	 */
	void dispose() {
		super.dispose();
		GL.glDeleteLists(listIndexBase, LIST_INDEX_SIZE);
	}

	/**
	 * Draws the text to the screen
	 * 
	 * @param string the text to draw
	 */
	void drawText(String string) {
		char[] stringChars = string.toCharArray();
		int[] text = new int[stringChars.length];
		for (int i = 0; i < text.length; i++) {
			text[i] = (int) stringChars[i];
		}
		GL.glPushAttrib(GL.GL_LIST_BIT);
		GL.glListBase(listIndexBase);
		GL.glCallLists(text.length, GL.GL_UNSIGNED_INT, text);
		GL.glPopAttrib();
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
		return "Outline Text";
	}

	/**
	 * @see OpenGLTab#init()
	 */
	void init() {
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glColor3fv(textColor);
		GL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		GL.glEnable(GL.GL_DEPTH_TEST);
		GL.glEnable(GL.GL_BLEND);
		// build the initial font
		listIndexBase = GL.glGenLists(LIST_INDEX_SIZE);
		fontData = new FontData();
		fontData.setHeight(DEFAULT_FONT_SIZE);
		fontData.setName(DEFAULT_FONT_NAME);
		getContext().loadOutlineFont(
			fontData, getGlCanvas().getDisplay(),
			listIndexBase, 0, 255, 0f, extrude,
			fill ? GL.GL_POLYGON : GL.GL_LINE,
			null);
	}

	/**
	 * @see OpenGLTab#renderScene()
	 */
	void renderScene() {
		GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		GL.glLoadIdentity();
		GL.glTranslatef(xPos, yPos, zPos);
		GL.glRotatef(xRot, 1.0f, 0.0f, 0.0f); // rotate on X axis
		// use GL.glScalef to change size since outline fonts
		// don't support size
		float size = (float) fontData.getHeight() / 24;
		GL.glScalef(size, size, 1.0f);
		// draw the text, assuming that we're running on win32
		if (messageText != null) drawText(messageText.getText());
		xRot += 1.2f;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5138.java