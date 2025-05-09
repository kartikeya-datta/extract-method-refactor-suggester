error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3733.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3733.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3733.java
text:
```scala
R@@owLayout layout = new RowLayout();

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
 * This dialog is used for prompting the user to select two colors for the
 * creation of a gradient.
 */
public class GradientDialog extends Dialog {

	Canvas canvas;

	Button colorButton1, colorButton2;		// color buttons

	Button okButton, cancelButton;

	Menu menu1, menu2;
	
	RGB rgb1, rgb2;			// first and second color used in gradient
	int returnVal; 			// value to be returned by open(), set to SWT.OK 
							// if the ok button has been pressed		
	ArrayList resources;
			
	public GradientDialog(Shell parent) {		
		this (parent, SWT.PRIMARY_MODAL);
	}
	
	public GradientDialog(Shell parent, int style) {
		super(parent, style);
		rgb1 = rgb2 = null;
		returnVal = SWT.CANCEL;
		resources = new ArrayList();
	}
	
	/**
	 * Sets up the dialog and opens it.
	 * */
	public int open() {
		final Shell dialog = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE | getStyle());
		dialog.setText(GraphicsExample.getResourceString("Gradient")); //$NON-NLS-1$
				
		GridLayout gridLayout = new GridLayout();
	    gridLayout.numColumns = 2;
	    gridLayout.marginHeight = 10;
	    gridLayout.marginWidth = 10;
	    dialog.setLayout(gridLayout);
	    
	    // create the controls in the dialog
	    createDialogControls(dialog);
		
		dialog.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				for (int i = 0; i < resources.size(); i++) {
					Object obj = resources.get(i);
					if (obj != null && obj instanceof Resource) {
						((Resource) obj).dispose();
					}
				}
				dialog.dispose();
			}
		});	

		dialog.setDefaultButton (okButton);
		dialog.pack ();
		Rectangle rect = getParent().getMonitor().getBounds();
		Rectangle bounds = dialog.getBounds();
		dialog.setLocation(rect.x + (rect.width - bounds.width) / 2, rect.y + (rect.height - bounds.height) / 2);
		dialog.setMinimumSize(bounds.width, bounds.height);
		
		dialog.open ();
		
		Display display = getParent().getDisplay();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		
		if (menu1 != null) {
			menu1.dispose();
			menu1 = null;
		}
		
		if (menu2 != null) {
			menu2.dispose();
			menu2 = null;
		}

		return returnVal;
	}
	
	/**
	 * Creates the controls of the dialog.
	 * */
	public void createDialogControls(final Shell parent) {
		final Display display = parent.getDisplay();
		
		// message
		Label message = new Label(parent, SWT.NONE); 
		message.setText(GraphicsExample.getResourceString("GradientDlgMsg"));
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.horizontalSpan = 2;
		message.setLayoutData(gridData);
		
		// default colors are white and black
		if (rgb1 == null || rgb2 == null) {
			rgb1 = display.getSystemColor(SWT.COLOR_WHITE).getRGB();
			rgb2 = display.getSystemColor(SWT.COLOR_BLACK).getRGB();
		}			

		// canvas
		canvas = new Canvas(parent, SWT.NONE);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 200;
		gridData.heightHint = 100;
		canvas.setLayoutData(gridData);
		canvas.addListener (SWT.Paint, new Listener () {
			public void handleEvent (Event e) {
				Image preview = null;
				Point size = canvas.getSize();
				Color color1 = new Color(display, rgb1);
				Color color2 = new Color(display, rgb2);
				preview = GraphicsExample.createImage(display, color1, color2, size.x, size.y);
				if (preview != null) {
					e.gc.drawImage (preview, 0, 0);
				}
				preview.dispose();
				color1.dispose();
				color2.dispose();
			}
		});
		
		// composite used for both color buttons
		Composite colorButtonComp = new Composite(parent, SWT.NONE);
		
		// layout buttons
		RowLayout layout = new RowLayout();;
		layout.type = SWT.VERTICAL;
		layout.pack = false;
		colorButtonComp.setLayout(layout);

		// position composite
		gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
	    colorButtonComp.setLayoutData(gridData);
	    
		ColorMenu colorMenu = new ColorMenu();
		
		// color controls: first color
		colorButton1 = new Button(colorButtonComp, SWT.PUSH);
		colorButton1.setText(GraphicsExample.getResourceString("GradientDlgButton1"));
		Color color1 = new Color(display, rgb1);
		Image img1 = GraphicsExample.createImage(display, color1);
		color1.dispose();
		colorButton1.setImage(img1);
		resources.add(img1);
		menu1 = colorMenu.createMenu(parent.getParent(), new ColorListener() {
			public void setColor(GraphicsBackground gb) {
				rgb1 = gb.getBgColor1().getRGB();
				colorButton1.setImage(gb.getThumbNail());
				if (canvas != null) canvas.redraw();
			}
		});
		colorButton1.addListener(SWT.Selection, new Listener() { 
			public void handleEvent(Event event) {
				final Button button = (Button) event.widget;
				final Composite parent = button.getParent(); 
				Rectangle bounds = button.getBounds();
				Point point = parent.toDisplay(new Point(bounds.x, bounds.y));
				menu1.setLocation(point.x, point.y + bounds.height);
				menu1.setVisible(true);
			}
		});
		
		// color controls: second color 
		colorButton2 = new Button(colorButtonComp, SWT.PUSH);
		colorButton2.setText(GraphicsExample.getResourceString("GradientDlgButton2"));
		Color color2 = new Color(display, rgb2);
		Image img2 = GraphicsExample.createImage(display, color2);
		color2.dispose();
		colorButton2.setImage(img2);
		resources.add(img2);
		menu2 = colorMenu.createMenu(parent.getParent(), new ColorListener() {
			public void setColor(GraphicsBackground gb) {
				rgb2 = gb.getBgColor1().getRGB();
				colorButton2.setImage(gb.getThumbNail());
				if (canvas != null) canvas.redraw();
			}
		});
		colorButton2.addListener(SWT.Selection, new Listener() { 
			public void handleEvent(Event event) {
				final Button button = (Button) event.widget;
				final Composite parent = button.getParent(); 
				Rectangle bounds = button.getBounds();
				Point point = parent.toDisplay(new Point(bounds.x, bounds.y));
				menu2.setLocation(point.x, point.y + bounds.height);
				menu2.setVisible(true);
			}
		});
		
		// composite used for ok and cancel buttons
		Composite okCancelComp = new Composite(parent, SWT.NONE);

		// layout buttons
		RowLayout rowLayout = new RowLayout();
		rowLayout.pack = false;
		rowLayout.marginTop = 5;
		okCancelComp.setLayout(rowLayout);

		// position composite
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
	    gridData.horizontalSpan = 2;
	    okCancelComp.setLayoutData(gridData);
		
	    // OK button
		okButton = new Button (okCancelComp, SWT.PUSH);
		okButton.setText("&OK");
		okButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				returnVal = SWT.OK;
				parent.close();
			}
		});
		
		// cancel button
		cancelButton = new Button (okCancelComp, SWT.PUSH);
		cancelButton.setText("&Cancel");
		cancelButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				parent.close();
			}
		});
	}
	
	/**
	 * Returns the first RGB selected by the user.
	 * */
	public RGB getFirstRGB() {
		return rgb1;		
	}
	
	/**
	 * Sets the first RGB.
	 * @param rgb
	 */
	public void setFirstRGB(RGB rgb) {
		this.rgb1 = rgb;
	}
	
	/**
	 * Returns the second RGB selected by the user.
	 * */
	public RGB getSecondRGB() {
		return rgb2;
	}

	/**
	 * Sets the second RGB.
	 * @param rgb
	 */
	public void setSecondRGB(RGB rgb) {
		this.rgb2 = rgb;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3733.java