error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3423.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3423.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3423.java
text:
```scala
private static R@@esourceBundle resourceBundle = ResourceBundle.getBundle("examples_hoverhelp");

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.hoverhelp;


import java.io.*;
import java.text.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
/**
 * This example demonstrates how to implement hover help feedback
 * using the MouseTrackListener.
 */
public class HoverHelp {
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("examples_hoverhelp");;
	
	static final int
		hhiInformation = 0,
		hhiWarning = 1;
	static final String[] imageLocations = {
		"information.gif",
		"warning.gif"
	};
	Image images[];

	/**
	 * Runs main program.
	 */
	public static void main (String [] args) {
		Display display = new Display();
		Shell shell = new HoverHelp().open(display);
		// Event loop
		while (shell != null && ! shell.isDisposed()) {
			if (! display.readAndDispatch()) display.sleep();
		}
		// Cleanup
		display.dispose();

	}
	
	/**
	 * Opens the main program.
	 */
	public Shell open(Display display) {		
		// Load the images
		Class clazz = HoverHelp.class;
		try {
			if (images == null) {
				images = new Image[imageLocations.length];
				
				for (int i = 0; i < imageLocations.length; ++i) {
					InputStream stream = clazz.getResourceAsStream(imageLocations[i]);
					ImageData source = new ImageData(stream);
					ImageData mask = source.getTransparencyMask();
					images[i] = new Image(display, source, mask);
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}	
		} catch (Exception ex) {
			System.err.println(getResourceString("error.CouldNotLoadResources",
				new Object[] { ex.getMessage() }));
			return null;
		}

		// Create the window
		Shell shell = new Shell();
		createPartControl(shell);
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				/* Free resources */
				if (images != null) {
					for (int i = 0; i < images.length; i++) {
						final Image image = images[i];
						if (image != null) image.dispose();
					}
					images = null;
				}
			}
		});
		shell.pack();
		shell.open();
		return shell;
	}

	/**
	 * Gets a string from the resource bundle.
	 * We don't want to crash because of a missing String.
	 * Returns the key if not found.
	 */
	public String getResourceString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}			
	}

	/**
	 * Gets a string from the resource bundle and binds it
	 * with the given arguments. If the key is not found,
	 * return the key.
	 */
	public String getResourceString(String key, Object[] args) {
		try {
			return MessageFormat.format(getResourceString(key), args);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}
	}

	/**
	 * Creates the example
	 */
	public void createPartControl(Composite frame) {
		final ToolTipHandler tooltip = new ToolTipHandler(frame.getShell());
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		frame.setLayout(layout);
	
		ToolBar bar = new ToolBar (frame, SWT.BORDER);
		for (int i=0; i<5; i++) {
			ToolItem item = new ToolItem (bar, SWT.PUSH);
			item.setText (getResourceString("ToolItem.text", new Object[] { new Integer(i) }));
			item.setData ("TIP_TEXT", getResourceString("ToolItem.tooltip",
				new Object[] { item.getText() }));
			item.setData ("TIP_HELPTEXTHANDLER", new ToolTipHelpTextHandler() {
				public String getHelpText(Widget widget) {
					Item item = (Item) widget;
					return getResourceString("ToolItem.help", new Object[] { item.getText() });
				}
			});
		}
		GridData gridData = new GridData();
		gridData.horizontalSpan = 3;
		bar.setLayoutData(gridData);
		tooltip.activateHoverHelp(bar);

		Table table = new Table (frame, SWT.BORDER);
		for (int i=0; i<4; i++) {
			TableItem item = new TableItem (table, SWT.PUSH);
			item.setText (getResourceString("Item", new Object[] { new Integer(i) }));
			item.setData ("TIP_IMAGE", images[hhiInformation]);
			item.setText (getResourceString("TableItem.text", new Object[] { new Integer(i) }));
			item.setData ("TIP_TEXT", getResourceString("TableItem.tooltip",
				new Object[] { item.getText() }));
			item.setData ("TIP_HELPTEXTHANDLER", new ToolTipHelpTextHandler() {
				public String getHelpText(Widget widget) {
					Item item = (Item) widget;
					return getResourceString("TableItem.help", new Object[] { item.getText() });
				}
			});
		}
		table.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));
		tooltip.activateHoverHelp(table);
	
		Tree tree = new Tree (frame, SWT.BORDER);
		for (int i=0; i<4; i++) {
			TreeItem item = new TreeItem (tree, SWT.PUSH);
			item.setText (getResourceString("Item", new Object[] { new Integer(i) }));
			item.setData ("TIP_IMAGE", images[hhiWarning]);
			item.setText (getResourceString("TreeItem.text", new Object[] { new Integer(i) }));
			item.setData ("TIP_TEXT", getResourceString("TreeItem.tooltip",
				new Object[] { item.getText() }));
			item.setData ("TIP_HELPTEXTHANDLER", new ToolTipHelpTextHandler() {
				public String getHelpText(Widget widget) {
					Item item = (Item) widget;
					return getResourceString("TreeItem.help", new Object[] { item.getText() });
				}
			});
		}
		tree.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL));
		tooltip.activateHoverHelp(tree);
	
		Button button = new Button (frame, SWT.PUSH);
		button.setText (getResourceString("Hello.text"));
		button.setData ("TIP_TEXT", getResourceString("Hello.tooltip"));
		tooltip.activateHoverHelp(button);
	}

	/**
	 * Emulated tooltip handler
	 * Notice that we could display anything in a tooltip besides text and images.
	 * For instance, it might make sense to embed large tables of data or buttons linking
	 * data under inspection to material elsewhere, or perform dynamic lookup for creating
	 * tooltip text on the fly.
	 */
	protected static class ToolTipHandler {
		private Shell  parentShell;
		private Shell  tipShell;
		private Label  tipLabelImage, tipLabelText;
		private Widget tipWidget; // widget this tooltip is hovering over
		private Point  tipPosition; // the position being hovered over
	
		/**
		 * Creates a new tooltip handler
		 *
		 * @param parent the parent Shell
		 */	
		public ToolTipHandler(Shell parent) {
			final Display display = parent.getDisplay();
			this.parentShell = parent;
	
			tipShell = new Shell(parent, SWT.ON_TOP | SWT.TOOL);
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			gridLayout.marginWidth = 2;
			gridLayout.marginHeight = 2;
			tipShell.setLayout(gridLayout);
	
			tipShell.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			
			tipLabelImage = new Label(tipShell, SWT.NONE);
			tipLabelImage.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
			tipLabelImage.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			tipLabelImage.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
				GridData.VERTICAL_ALIGN_CENTER));
	
			tipLabelText = new Label(tipShell, SWT.NONE);
			tipLabelText.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
			tipLabelText.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			tipLabelText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
				GridData.VERTICAL_ALIGN_CENTER));
		}
	
		/**
		 * Enables customized hover help for a specified control
		 * 
		 * @control the control on which to enable hoverhelp
		 */
		public void activateHoverHelp(final Control control) {
			/*
			 * Get out of the way if we attempt to activate the control underneath the tooltip
			 */
			control.addMouseListener(new MouseAdapter () {
				public void mouseDown (MouseEvent e) {
					if (tipShell.isVisible()) tipShell.setVisible(false);
				}	
			});
	
			/*
			 * Trap hover events to pop-up tooltip
			 */
			control.addMouseTrackListener(new MouseTrackAdapter () {
				public void mouseExit(MouseEvent e) {
					if (tipShell.isVisible()) tipShell.setVisible(false);
					tipWidget = null;
				}
				public void mouseHover (MouseEvent event) {
					Point pt = new Point (event.x, event.y);
					Widget widget = event.widget;
					if (widget instanceof ToolBar) {
						ToolBar w = (ToolBar) widget;
						widget = w.getItem (pt);
					}
					if (widget instanceof Table) {
						Table w = (Table) widget;
						widget = w.getItem (pt);
					}
					if (widget instanceof Tree) {
						Tree w = (Tree) widget;
						widget = w.getItem (pt);
					}
					if (widget == null) {
						tipShell.setVisible(false);
						tipWidget = null;
						return;
					}
					if (widget == tipWidget) return;
					tipWidget = widget;
					tipPosition = control.toDisplay(pt);
					String text = (String) widget.getData("TIP_TEXT");
					Image image = (Image) widget.getData("TIP_IMAGE");
					tipLabelText.setText(text != null ? text : "");
					tipLabelImage.setImage(image); // accepts null
					tipShell.pack();
					setHoverLocation(tipShell, tipPosition);
					tipShell.setVisible(true);
				}
			});
	
			/*
			 * Trap F1 Help to pop up a custom help box
			 */
			control.addHelpListener(new HelpListener () {
				public void helpRequested(HelpEvent event) {
					if (tipWidget == null) return;
					ToolTipHelpTextHandler handler = (ToolTipHelpTextHandler)
						tipWidget.getData("TIP_HELPTEXTHANDLER");
					if (handler == null) return;
					String text = handler.getHelpText(tipWidget);
					if (text == null) return;
					
					if (tipShell.isVisible()) {
						tipShell.setVisible(false);
						Shell helpShell = new Shell(parentShell, SWT.SHELL_TRIM);
						helpShell.setLayout(new FillLayout());
						Label label = new Label(helpShell, SWT.NONE);
						label.setText(text);
						helpShell.pack();
						setHoverLocation(helpShell, tipPosition);
						helpShell.open();
					}
				}
			});
		}
		
		/**
		 * Sets the location for a hovering shell
		 * @param shell the object that is to hover
		 * @param position the position of a widget to hover over
		 * @return the top-left location for a hovering box
		 */
		private void setHoverLocation(Shell shell, Point position) {
			Rectangle displayBounds = shell.getDisplay().getBounds();
			Rectangle shellBounds = shell.getBounds();
			shellBounds.x = Math.max(Math.min(position.x, displayBounds.width - shellBounds.width), 0);
			shellBounds.y = Math.max(Math.min(position.y + 16, displayBounds.height - shellBounds.height), 0);
			shell.setBounds(shellBounds);
		}
	}
	
	/**
	 * ToolTip help handler
	 */
	protected interface ToolTipHelpTextHandler {
		/**
		 * Get help text
		 * @param widget the widget that is under help
		 * @return a help text string
		 */
		public String getHelpText(Widget widget);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3423.java