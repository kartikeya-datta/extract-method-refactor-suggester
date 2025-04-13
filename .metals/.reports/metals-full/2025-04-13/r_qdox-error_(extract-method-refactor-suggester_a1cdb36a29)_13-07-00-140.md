error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17905.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17905.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17905.java
text:
```scala
S@@tring getShortTabText() {

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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

class ExpandBarTab extends Tab {
	/* Example widgets and groups that contain them */
	ExpandBar expandBar1;
	Group expandBarGroup;
	
	/* Style widgets added to the "Style" group */	
	Button verticalButton;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	ExpandBarTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the list */
		expandBarGroup = new Group (exampleGroup, SWT.NONE);
		expandBarGroup.setLayout (new GridLayout ());
		expandBarGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		expandBarGroup.setText ("ExpandBar");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (borderButton.getSelection ()) style |= SWT.BORDER;
		if (verticalButton.getSelection()) style |= SWT.V_SCROLL;
	
		/* Create the example widgets */		
		expandBar1 = new ExpandBar (expandBarGroup, style);
		
		// First item
		Composite composite = new Composite (expandBar1, SWT.NONE);
		composite.setLayout(new GridLayout ());
		new Button (composite, SWT.PUSH).setText("SWT.PUSH");
		new Button (composite, SWT.RADIO).setText("SWT.RADIO");
		new Button (composite, SWT.CHECK).setText("SWT.CHECK");
		new Button (composite, SWT.TOGGLE).setText("SWT.TOGGLE");
		ExpandItem item = new ExpandItem (expandBar1, SWT.NONE, 0);
		item.setText(ControlExample.getResourceString("Item1_Text"));
		item.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item.setControl(composite);
		item.setImage(instance.images[ControlExample.ciClosedFolder]);
		
		// Second item
		composite = new Composite (expandBar1, SWT.NONE);
		composite.setLayout(new GridLayout (2, false));	
		new Label (composite, SWT.NONE).setImage(display.getSystemImage(SWT.ICON_ERROR));
		new Label (composite, SWT.NONE).setText("SWT.ICON_ERROR");
		new Label (composite, SWT.NONE).setImage(display.getSystemImage(SWT.ICON_INFORMATION));
		new Label (composite, SWT.NONE).setText("SWT.ICON_INFORMATION");
		new Label (composite, SWT.NONE).setImage(display.getSystemImage(SWT.ICON_WARNING));
		new Label (composite, SWT.NONE).setText("SWT.ICON_WARNING");
		new Label (composite, SWT.NONE).setImage(display.getSystemImage(SWT.ICON_QUESTION));
		new Label (composite, SWT.NONE).setText("SWT.ICON_QUESTION");
		item = new ExpandItem (expandBar1, SWT.NONE, 1);
		item.setText(ControlExample.getResourceString("Item2_Text"));
		item.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item.setControl(composite);
		item.setImage(instance.images[ControlExample.ciOpenFolder]);
		item.setExpanded(true);
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup ();
		
		/* Create the extra widgets */
		verticalButton = new Button (styleGroup, SWT.CHECK);
		verticalButton.setText ("SWT.V_SCROLL");
		verticalButton.setSelection(true);
		borderButton = new Button(styleGroup, SWT.CHECK);
		borderButton.setText("SWT.BORDER");
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {expandBar1};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Spacing"};
	}
	
	/**
	 * Gets the short text for the tab folder item.
	 */
	public String getShortTabText() {
		return "EB";
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "ExpandBar";
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		Widget [] widgets = getExampleWidgets ();
		if (widgets.length != 0){
			verticalButton.setSelection ((widgets [0].getStyle () & SWT.V_SCROLL) != 0);
			borderButton.setSelection ((widgets [0].getStyle () & SWT.BORDER) != 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17905.java