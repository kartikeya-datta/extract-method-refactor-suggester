error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6665.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6665.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6665.java
text:
```scala
r@@eturn new String[] {"CaretOffset", "DoubleClickEnabled", "Editable", "HorizontalIndex", "HorizontalPixel", "Orientation", "Selection", "Tabs", "Text", "TextLimit", "ToolTipText", "TopIndex", "TopPixel", "WordWrap"};

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
package org.eclipse.swt.examples.controlexample;


import java.io.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.custom.*;

class StyledTextTab extends ScrollableTab {
	/* Example widgets and groups that contain them */
	StyledText styledText;
	Group styledTextGroup, styledTextStyleGroup;

	/* Style widgets added to the "Style" group */
	Button wrapButton, readOnlyButton, fullSelectionButton;
	
	/* Buttons for adding StyleRanges to StyledText */
	Button boldButton, italicButton, redButton, yellowButton, underlineButton, strikeoutButton;
	Image boldImage, italicImage, redImage, yellowImage, underlineImage, strikeoutImage;
	
	/* Variables for saving state. */
	StyleRange[] styleRanges;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	StyledTextTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates a bitmap image. 
	 */
	Image createBitmapImage (Display display, String name) {
		InputStream sourceStream = ControlExample.class.getResourceAsStream (name + ".bmp");
		InputStream maskStream = ControlExample.class.getResourceAsStream (name + "_mask.bmp");
		ImageData source = new ImageData (sourceStream);
		ImageData mask = new ImageData (maskStream);
		Image result = new Image (display, source, mask);
		try {
			sourceStream.close ();
			maskStream.close ();
		} catch (IOException e) {
			e.printStackTrace ();
		}
		return result;
	}
	
	/**
	 * Creates the "Control" widget children.
	 */
	void createControlWidgets () {
		super.createControlWidgets ();
		
		/* Add a group for modifying the StyledText widget */
		createStyledTextStyleGroup ();
	}

	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the styled text widget */
		styledTextGroup = new Group (exampleGroup, SWT.NONE);
		styledTextGroup.setLayout (new GridLayout ());
		styledTextGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		styledTextGroup.setText ("StyledText");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (singleButton.getSelection ()) style |= SWT.SINGLE;
		if (multiButton.getSelection ()) style |= SWT.MULTI;
		if (horizontalButton.getSelection ()) style |= SWT.H_SCROLL;
		if (verticalButton.getSelection ()) style |= SWT.V_SCROLL;
		if (wrapButton.getSelection ()) style |= SWT.WRAP;
		if (readOnlyButton.getSelection ()) style |= SWT.READ_ONLY;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
		if (fullSelectionButton.getSelection ()) style |= SWT.FULL_SELECTION;
	
		/* Create the example widgets */
		styledText = new StyledText (styledTextGroup, style);
		styledText.setText (ControlExample.getResourceString("Example_string"));
		styledText.append ("\n");
		styledText.append (ControlExample.getResourceString("One_Two_Three"));
		
		if (styleRanges != null) {
			styledText.setStyleRanges(styleRanges);
			styleRanges = null;
		}
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup();
	
		/* Create the extra widgets */
		wrapButton = new Button (styleGroup, SWT.CHECK);
		wrapButton.setText ("SWT.WRAP");
		readOnlyButton = new Button (styleGroup, SWT.CHECK);
		readOnlyButton.setText ("SWT.READ_ONLY");
		fullSelectionButton = new Button (styleGroup, SWT.CHECK);
		fullSelectionButton.setText ("SWT.FULL_SELECTION");
	}
	
	/**
	 * Creates the "StyledText Style" group.
	 */
	void createStyledTextStyleGroup () {
		final Display display = controlGroup.getDisplay ();
		styledTextStyleGroup = new Group (controlGroup, SWT.NONE);
		styledTextStyleGroup.setText (ControlExample.getResourceString ("StyledText_Styles"));
		styledTextStyleGroup.setLayout (new GridLayout(7, false));
		GridData data = new GridData (GridData.HORIZONTAL_ALIGN_FILL);
		data.horizontalSpan = 2;
		styledTextStyleGroup.setLayoutData (data);
		
		/* Get images */
		boldImage = createBitmapImage (display, "bold");
		italicImage = createBitmapImage (display, "italic");
		redImage = createBitmapImage (display, "red");
		yellowImage = createBitmapImage (display, "yellow");
		underlineImage = createBitmapImage (display, "underline");
		strikeoutImage = createBitmapImage (display, "strikeout");
		
		/* Create controls to modify the StyledText */
		Label label = new Label (styledTextStyleGroup, SWT.NONE);
		label.setText (ControlExample.getResourceString ("StyledText_Style_Instructions"));
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 7;
		label.setLayoutData(data);
		new Label (styledTextStyleGroup, SWT.NONE).setText (ControlExample.getResourceString ("Bold"));
		boldButton = new Button (styledTextStyleGroup, SWT.PUSH);
		boldButton.setImage (boldImage);
		new Label (styledTextStyleGroup, SWT.NONE).setText (ControlExample.getResourceString ("Underline"));
		underlineButton = new Button (styledTextStyleGroup, SWT.PUSH);
		underlineButton.setImage (underlineImage);
		new Label (styledTextStyleGroup, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label (styledTextStyleGroup, SWT.NONE).setText (ControlExample.getResourceString ("Foreground_Style"));
		redButton = new Button (styledTextStyleGroup, SWT.PUSH);
		redButton.setImage (redImage);
		new Label (styledTextStyleGroup, SWT.NONE).setText (ControlExample.getResourceString ("Italic"));
		italicButton = new Button (styledTextStyleGroup, SWT.PUSH);
		italicButton.setImage (italicImage);
		new Label (styledTextStyleGroup, SWT.NONE).setText (ControlExample.getResourceString ("Strikeout"));
		strikeoutButton = new Button (styledTextStyleGroup, SWT.PUSH);
		strikeoutButton.setImage (strikeoutImage);
		new Label (styledTextStyleGroup, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label (styledTextStyleGroup, SWT.NONE).setText (ControlExample.getResourceString ("Background_Style"));
		yellowButton = new Button (styledTextStyleGroup, SWT.PUSH);
		yellowButton.setImage (yellowImage);
		SelectionListener styleListener = new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				Point sel = styledText.getSelectionRange();
				if ((sel == null) || (sel.y == 0)) return;
				StyleRange style;
				for (int i = sel.x; i<sel.x+sel.y; i++) {
					StyleRange range = styledText.getStyleRangeAtOffset(i);
					if (range != null) {
						style = (StyleRange)range.clone();
						style.start = i;
						style.length = 1;
					} else {
						style = new StyleRange(i, 1, null, null, SWT.NORMAL);
					}
					if (e.widget == boldButton) {
						style.fontStyle ^= SWT.BOLD;
					} else if (e.widget == italicButton) {
						style.fontStyle ^= SWT.ITALIC;						
					} else if (e.widget == underlineButton) {
						style.underline = !style.underline;
					} else if (e.widget == strikeoutButton) {
						style.strikeout = !style.strikeout;
					}
					styledText.setStyleRange(style);
				}
				styledText.setSelectionRange(sel.x + sel.y, 0);			
			}
		};
		SelectionListener colorListener = new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				Point sel = styledText.getSelectionRange();
				if ((sel == null) || (sel.y == 0)) return;
				Color fg = null, bg = null;
				if (e.widget == redButton) {
					fg = display.getSystemColor (SWT.COLOR_RED);
				} else if (e.widget == yellowButton) {
					bg = display.getSystemColor (SWT.COLOR_YELLOW);
				}
				StyleRange style;
				for (int i = sel.x; i<sel.x+sel.y; i++) {
					StyleRange range = styledText.getStyleRangeAtOffset(i);
					if (range != null) {
						style = (StyleRange)range.clone();
						style.start = i;
						style.length = 1;
						style.foreground = style.foreground != null ? null : fg;
						style.background = style.background != null ? null : bg;
					} else {
						style = new StyleRange (i, 1, fg, bg, SWT.NORMAL);
					}
					styledText.setStyleRange(style);
				}
				styledText.setSelectionRange(sel.x + sel.y, 0);
			}
		};
		boldButton.addSelectionListener(styleListener);
		italicButton.addSelectionListener(styleListener);
		underlineButton.addSelectionListener(styleListener);
		strikeoutButton.addSelectionListener(styleListener);
		redButton.addSelectionListener(colorListener);
		yellowButton.addSelectionListener(colorListener);
		yellowButton.addDisposeListener(new DisposeListener () {
			public void widgetDisposed (DisposeEvent e) {
				boldImage.dispose();
				italicImage.dispose();
				redImage.dispose();
				yellowImage.dispose();
				underlineImage.dispose();
				strikeoutImage.dispose();
			}
		});
	}
	
	/**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder org.eclipse.swt.widgets.TabFolder
	 * @return the new page for the tab folder
	 */
	Composite createTabFolderPage (TabFolder tabFolder) {
		super.createTabFolderPage (tabFolder);

		/*
		 * Add a resize listener to the tabFolderPage so that
		 * if the user types into the example widget to change
		 * its preferred size, and then resizes the shell, we
		 * recalculate the preferred size correctly.
		 */
		tabFolderPage.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				setExampleWidgetSize ();
			}
		});
		
		return tabFolderPage;
	}

	/**
	 * Disposes the "Example" widgets.
	 */
	void disposeExampleWidgets () {
		/* store the state of the styledText if applicable */
		if (styledText != null) {
			styleRanges = styledText.getStyleRanges();
		}
		super.disposeExampleWidgets();	
	}

	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {styledText};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"CaretOffset", "DoubleClickEnabled", "Editable", "HorizontalIndex", "HorizontalPixel", "Orientation", "Selection", "Tabs", "Text", "TextLimit", "TopIndex", "TopPixel", "WordWrap"};
	}

	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "StyledText";
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		wrapButton.setSelection ((styledText.getStyle () & SWT.WRAP) != 0);
		readOnlyButton.setSelection ((styledText.getStyle () & SWT.READ_ONLY) != 0);
		fullSelectionButton.setSelection ((styledText.getStyle () & SWT.FULL_SELECTION) != 0);
		horizontalButton.setEnabled ((styledText.getStyle () & SWT.MULTI) != 0);
		verticalButton.setEnabled ((styledText.getStyle () & SWT.MULTI) != 0);
		wrapButton.setEnabled ((styledText.getStyle () & SWT.MULTI) != 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6665.java