error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13880.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13880.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13880.java
text:
```scala
c@@m.setPatternItems(example.checkAdvancedGraphics());

/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This tab demonstrates line styles applicable to a line.  It shows the use of the
 * <code>GC.setLineStyle()</code> method.
 */
public class LineStyleTab extends GraphicsTab {

	Button colorButton;
	GraphicsBackground lineColor;
	Menu menu;
	Spinner lineWidthSpinner;

public LineStyleTab(GraphicsExample example) {
	super(example);
}

public String getCategory() {
	return GraphicsExample.getResourceString("Lines"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("LineStyles"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("LineStylesDescription"); //$NON-NLS-1$
}

public void dispose() {
	if (menu != null) {
		menu.dispose();
		menu = null;
	}
}

public void createControlPanel(Composite parent) {
	
	Composite comp;
	
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout(2, false));
	
	new Label(comp, SWT.CENTER).setText(GraphicsExample
			.getResourceString("LineWidth")); //$NON-NLS-1$
	lineWidthSpinner = new Spinner(comp, SWT.BORDER | SWT.WRAP);
	lineWidthSpinner.setSelection(10);
	lineWidthSpinner.setMinimum(1);
	lineWidthSpinner.setMaximum(30);
	lineWidthSpinner.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
				example.redraw();
		}
	});
		
	ColorMenu cm = new ColorMenu();
	cm.setPatternItems(true);
	menu = cm.createMenu(parent.getParent(), new ColorListener() {
		public void setColor(GraphicsBackground gb) {
			lineColor = gb;		
			colorButton.setImage(gb.getThumbNail());
			example.redraw();
		}
	});

	// create color button
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout());
	
	// initialize the foreground to the 5th item in the menu (blue)
	lineColor = (GraphicsBackground)menu.getItem(4).getData();
	
	// color button
	colorButton = new Button(comp, SWT.PUSH);
	colorButton.setText(GraphicsExample
			.getResourceString("Color")); //$NON-NLS-1$
	colorButton.setImage(lineColor.getThumbNail());
	colorButton.addListener(SWT.Selection, new Listener() { 
		public void handleEvent(Event event) {
			final Button button = (Button) event.widget;
			final Composite parent = button.getParent(); 
			Rectangle bounds = button.getBounds();
			Point point = parent.toDisplay(new Point(bounds.x, bounds.y));
			menu.setLocation(point.x, point.y + bounds.height);
			menu.setVisible(true);
		}
	});
}

public void paint(GC gc, int width, int height) {
	Device device = gc.getDevice();

	Pattern pattern = null;
	if (lineColor.getBgColor1() != null) {
		gc.setForeground(lineColor.getBgColor1());
	} else if (lineColor.getBgImage() != null) {
		pattern = new Pattern(device, lineColor.getBgImage());
		gc.setForegroundPattern(pattern);
	}
	
	// set line width
	gc.setLineWidth(lineWidthSpinner.getSelection());
	
	// draw lines with caps
	gc.drawLine(3*width/16, 1*height/6, 13*width/16, 1*height/6);
	gc.setLineStyle(SWT.LINE_DASH);
	gc.drawLine(3*width/16, 2*height/6, 13*width/16, 2*height/6);
	gc.setLineStyle(SWT.LINE_DOT);
	gc.drawLine(3*width/16, 3*height/6, 13*width/16, 3*height/6);
	gc.setLineStyle(SWT.LINE_DASHDOT);
	gc.drawLine(3*width/16, 4*height/6, 13*width/16, 4*height/6);
	gc.setLineStyle(SWT.LINE_DASHDOTDOT);
	gc.drawLine(3*width/16, 5*height/6, 13*width/16, 5*height/6);
	
	if (pattern != null) pattern.dispose();
	
	// draw labels
	Font font = new Font(device, getPlatformFont(), 20, SWT.NORMAL);
	gc.setFont(font);

	gc.setForeground(device.getSystemColor(SWT.COLOR_BLACK));
	
	String text = GraphicsExample.getResourceString("Solid"); //$NON-NLS-1$
	Point size = gc.stringExtent(text);
	gc.drawString(text, (width-size.x)/2, 1*height/12);
	text = GraphicsExample.getResourceString("Dash"); //$NON-NLS-1$
	size = gc.stringExtent(text);	
	gc.drawString(text, (width-size.x)/2, 3*height/12);
	text = GraphicsExample.getResourceString("Dot"); //$NON-NLS-1$
	size = gc.stringExtent(text);
	gc.drawString(text, (width-size.x)/2, 5*height/12);
	text = GraphicsExample.getResourceString("DashDot"); //$NON-NLS-1$
	size = gc.stringExtent(text);
	gc.drawString(text, (width-size.x)/2, 7*height/12);
	text = GraphicsExample.getResourceString("DashDotDot"); //$NON-NLS-1$
	size = gc.stringExtent(text);
	gc.drawString(text, (width-size.x)/2, 9*height/12);
	font.dispose();
}

/**
 * Returns the name of a valid font for the resident platform.
 */
static String getPlatformFont() {
	if(SWT.getPlatform() == "win32") {
		return "Arial";	
	} else if (SWT.getPlatform() == "motif") {
		return "Helvetica";		
	} else if (SWT.getPlatform() == "gtk") {
		return "Baekmuk Batang";		
	} else if (SWT.getPlatform() == "carbon") {
		return "Arial";
	} else { // photon, etc ...
		return "Verdana";
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13880.java