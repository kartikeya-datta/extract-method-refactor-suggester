error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7138.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7138.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7138.java
text:
```scala
g@@c.drawString(text, (width-textWidth)/2, (height-textHeight)/2, true);

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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This tab demonstrates various text fonts. It allows the user to specify font
 * parameters such as face, style and size.
 */
public class CustomFontTab extends GraphicsTab {

	String text = GraphicsExample.getResourceString("SWT");
	GraphicsBackground fontForeground;
	Combo fontFaceCb, fontStyleCb;
	Spinner fontPointSpinner;
	Button colorButton;
	ArrayList fontNames;
	int [] styleValues;
	String [] fontStyles;
	Menu menu;
	
public CustomFontTab(GraphicsExample example) {
	super(example);
	
	// create list of fonts for this platform
	FontData [] fontData = Display.getCurrent().getFontList(null, true);
	fontNames = new ArrayList();
	for (int i=0; i < fontData.length; i++) {
		// remove duplicates and sort
		String nextName = fontData[i].getName();
		if (!fontNames.contains(nextName)) {
			int j = 0;
			while(j < fontNames.size() && nextName.compareTo((String)fontNames.get(j)) > 0) {
				j++;
			}
			fontNames.add(j, nextName);
		}
	}
	fontStyles = new String [] {
			GraphicsExample.getResourceString("Regular"), //$NON-NLS-1$
			GraphicsExample.getResourceString("Italic"), //$NON-NLS-1$
			GraphicsExample.getResourceString("Bold"), //$NON-NLS-1$
			GraphicsExample.getResourceString("BoldItalic") //$NON-NLS-1$
	};
	styleValues = new int [] {SWT.NORMAL, SWT.ITALIC, SWT.BOLD, SWT.BOLD | SWT.ITALIC};
}

public String getCategory() {
	return GraphicsExample.getResourceString("Font"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("CustomFont"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("CustomFontDescription"); //$NON-NLS-1$
}

public void dispose() {
	if (menu != null) {
		menu.dispose();
		menu = null;
	}
}

public void createControlPanel(Composite parent) {

	Composite mainComp = new Composite(parent, SWT.NONE);
	mainComp.setLayout(new RowLayout());
	
	// create combo for font face
	Composite comp = new Composite(mainComp, SWT.NONE);
	comp.setLayout(new GridLayout(2, false));
	
	new Label(comp, SWT.LEFT).setText(GraphicsExample.getResourceString("FontFace")); //$NON-NLS-1$
	fontFaceCb = new Combo(comp, SWT.DROP_DOWN);
	for (int i=0; i < fontNames.size(); i++) {
		String name = (String)fontNames.get(i);
		fontFaceCb.add(name);
	}
	fontFaceCb.select(0);
	fontFaceCb.addListener(SWT.Selection, new Listener() {
		public void handleEvent (Event event) {
			example.redraw();
		}
	});
	
	// create combo for font style
	comp = new Composite(mainComp, SWT.NONE);
	comp.setLayout(new GridLayout(2, false));
	
	new Label(comp, SWT.LEFT).setText(GraphicsExample.getResourceString("FontStyle")); //$NON-NLS-1$
	fontStyleCb = new Combo(comp, SWT.DROP_DOWN);
	for (int i=0; i < fontStyles.length; i++) {
		fontStyleCb.add(fontStyles[i]);
	}
	fontStyleCb.select(0);
	fontStyleCb.addListener(SWT.Selection, new Listener() {
		public void handleEvent (Event event) {
			example.redraw();
		}
	});
	
	// create spinner for font size (points)
	comp = new Composite(mainComp, SWT.NONE);
	comp.setLayout(new GridLayout(2, false));
	
	new Label(comp, SWT.LEFT).setText(GraphicsExample.getResourceString("FontSize")); //$NON-NLS-1$
	fontPointSpinner = new Spinner(comp, SWT.BORDER | SWT.WRAP);
	fontPointSpinner.setMinimum(1);
	fontPointSpinner.setMaximum(1000);
	fontPointSpinner.setSelection(200);
	fontPointSpinner.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
				example.redraw();
		}
	});

	ColorMenu cm = new ColorMenu();
	cm.setColorItems(true);
	cm.setPatternItems(example.checkAdvancedGraphics());
	menu = cm.createMenu(parent.getParent(), new ColorListener() {
		public void setColor(GraphicsBackground gb) {
			fontForeground = gb;
			colorButton.setImage(gb.getThumbNail());
			example.redraw();
		}
	});
	
	// initialize the background to the 2nd item in the menu (black)
	fontForeground = (GraphicsBackground)menu.getItem(1).getData();
	
	// create color button
	comp = new Composite(parent, SWT.NONE);
	comp.setLayout(new GridLayout());
		
	colorButton = new Button(comp, SWT.PUSH);
	colorButton.setText(GraphicsExample.getResourceString("Color")); //$NON-NLS-1$
	colorButton.setImage(fontForeground.getThumbNail());
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

/* (non-Javadoc)
 * @see org.eclipse.swt.examples.graphics.GraphicsTab#paint(org.eclipse.swt.graphics.GC, int, int)
 */
public void paint(GC gc, int width, int height) {
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();
	
	String fontFace = (String)fontNames.get(fontFaceCb.getSelectionIndex());
	int points = fontPointSpinner.getSelection();
	int style = styleValues[fontStyleCb.getSelectionIndex()];
	
	Font font = new Font(device, fontFace, points, style);
	gc.setFont(font);
	gc.setTextAntialias(SWT.ON);

	Point size = gc.stringExtent(text);
	int textWidth = size.x;
	int textHeight = size.y;

	Pattern pattern = null;
	if (fontForeground.getBgColor1() != null) {
		gc.setForeground(fontForeground.getBgColor1());
	} else if (fontForeground.getBgImage() != null) {
		pattern = new Pattern(device, fontForeground.getBgImage());
		gc.setForegroundPattern(pattern);
	}
	
	gc.drawString(text, (width-textWidth)/2, (height-textHeight)/2);

	font.dispose();
	if (pattern != null) pattern.dispose();	
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7138.java