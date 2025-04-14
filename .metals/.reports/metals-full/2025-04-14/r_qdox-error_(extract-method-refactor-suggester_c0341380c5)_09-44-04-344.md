error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6662.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6662.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6662.java
text:
```scala
r@@eturn new String[] {"Text", "ToolTipText"};

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


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

class LabelTab extends AlignableTab {
	/* Example widgets and groups that contain them */
	Label label1, label2, label3, label4, label5, label6;
	Group textLabelGroup, imageLabelGroup;

	/* Style widgets added to the "Style" group */
	Button wrapButton, separatorButton, horizontalButton, verticalButton, shadowInButton, shadowOutButton, shadowNoneButton;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	LabelTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the text labels */
		textLabelGroup = new Group(exampleGroup, SWT.NONE);
		GridLayout gridLayout = new GridLayout ();
		textLabelGroup.setLayout (gridLayout);
		gridLayout.numColumns = 3;
		textLabelGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		textLabelGroup.setText (ControlExample.getResourceString("Text_Labels"));
	
		/* Create a group for the image labels */
		imageLabelGroup = new Group (exampleGroup, SWT.SHADOW_NONE);
		gridLayout = new GridLayout ();
		imageLabelGroup.setLayout (gridLayout);
		gridLayout.numColumns = 3;
		imageLabelGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		imageLabelGroup.setText (ControlExample.getResourceString("Image_Labels"));
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (wrapButton.getSelection ()) style |= SWT.WRAP;
		if (separatorButton.getSelection ()) style |= SWT.SEPARATOR;
		if (horizontalButton.getSelection ()) style |= SWT.HORIZONTAL;
		if (verticalButton.getSelection ()) style |= SWT.VERTICAL;
		if (shadowInButton.getSelection ()) style |= SWT.SHADOW_IN;
		if (shadowOutButton.getSelection ()) style |= SWT.SHADOW_OUT;
		if (shadowNoneButton.getSelection ()) style |= SWT.SHADOW_NONE;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
		if (leftButton.getSelection ()) style |= SWT.LEFT;
		if (centerButton.getSelection ()) style |= SWT.CENTER;
		if (rightButton.getSelection ()) style |= SWT.RIGHT;
	
		/* Create the example widgets */
		label1 = new Label (textLabelGroup, style);
		label1.setText(ControlExample.getResourceString("One"));
		label2 = new Label (textLabelGroup, style);
		label2.setText(ControlExample.getResourceString("Two"));
		label3 = new Label (textLabelGroup, style);
		if (wrapButton.getSelection ()) {
			label3.setText (ControlExample.getResourceString("Wrap_Text"));
		} else {
			label3.setText (ControlExample.getResourceString("Three"));
		}
		label4 = new Label (imageLabelGroup, style);
		label4.setImage (instance.images[ControlExample.ciClosedFolder]);
		label5 = new Label (imageLabelGroup, style);
		label5.setImage (instance.images[ControlExample.ciOpenFolder]);
		label6 = new Label(imageLabelGroup, style);
		label6.setImage (instance.images[ControlExample.ciTarget]);
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup ();
		
		/* Create the extra widgets */
		wrapButton = new Button (styleGroup, SWT.CHECK);
		wrapButton.setText ("SWT.WRAP");
		separatorButton = new Button (styleGroup, SWT.CHECK);
		separatorButton.setText ("SWT.SEPARATOR");
		horizontalButton = new Button (styleGroup, SWT.RADIO);
		horizontalButton.setText ("SWT.HORIZONTAL");
		verticalButton = new Button (styleGroup, SWT.RADIO);
		verticalButton.setText ("SWT.VERTICAL");
		Group styleSubGroup = new Group (styleGroup, SWT.NONE);
		styleSubGroup.setLayout (new GridLayout ());
		shadowInButton = new Button (styleSubGroup, SWT.RADIO);
		shadowInButton.setText ("SWT.SHADOW_IN");
		shadowOutButton = new Button (styleSubGroup, SWT.RADIO);
		shadowOutButton.setText ("SWT.SHADOW_OUT");
		shadowNoneButton = new Button (styleSubGroup, SWT.RADIO);
		shadowNoneButton.setText ("SWT.SHADOW_NONE");
		borderButton = new Button(styleGroup, SWT.CHECK);
		borderButton.setText("SWT.BORDER");
	
		/* Add the listeners */
		SelectionListener selectionListener = new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				if ((event.widget.getStyle() & SWT.RADIO) != 0) {
					if (!((Button) event.widget).getSelection ()) return;
				}
				recreateExampleWidgets ();
			}
		};
		shadowInButton.addSelectionListener (selectionListener);
		shadowOutButton.addSelectionListener (selectionListener);
		shadowNoneButton.addSelectionListener (selectionListener);
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {label1, label2, label3, label4, label5, label6};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Text"};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Label";
	}
	
	/**
	 * Sets the alignment of the "Example" widgets.
	 */
	void setExampleWidgetAlignment () {
		int alignment = 0;
		if (leftButton.getSelection ()) alignment = SWT.LEFT;
		if (centerButton.getSelection ()) alignment = SWT.CENTER;
		if (rightButton.getSelection ()) alignment = SWT.RIGHT;
		label1.setAlignment (alignment);
		label2.setAlignment (alignment);
		label3.setAlignment (alignment);
		label4.setAlignment (alignment);
		label5.setAlignment (alignment);
		label6.setAlignment (alignment);
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		boolean isSeparator = (label1.getStyle () & SWT.SEPARATOR) != 0;
		wrapButton.setSelection (!isSeparator && (label1.getStyle () & SWT.WRAP) != 0);
		leftButton.setSelection (!isSeparator && (label1.getStyle () & SWT.LEFT) != 0);
		centerButton.setSelection (!isSeparator && (label1.getStyle () & SWT.CENTER) != 0);
		rightButton.setSelection (!isSeparator && (label1.getStyle () & SWT.RIGHT) != 0);
		shadowInButton.setSelection (isSeparator && (label1.getStyle () & SWT.SHADOW_IN) != 0);
		shadowOutButton.setSelection (isSeparator && (label1.getStyle () & SWT.SHADOW_OUT) != 0);
		shadowNoneButton.setSelection (isSeparator && (label1.getStyle () & SWT.SHADOW_NONE) != 0);
		horizontalButton.setSelection (isSeparator && (label1.getStyle () & SWT.HORIZONTAL) != 0);
		verticalButton.setSelection (isSeparator && (label1.getStyle () & SWT.VERTICAL) != 0);		
		wrapButton.setEnabled (!isSeparator);
		leftButton.setEnabled (!isSeparator);
		centerButton.setEnabled (!isSeparator);
		rightButton.setEnabled (!isSeparator);
		shadowInButton.setEnabled (isSeparator);
		shadowOutButton.setEnabled (isSeparator);
		shadowNoneButton.setEnabled (isSeparator);
		horizontalButton.setEnabled (isSeparator);
		verticalButton.setEnabled (isSeparator);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6662.java