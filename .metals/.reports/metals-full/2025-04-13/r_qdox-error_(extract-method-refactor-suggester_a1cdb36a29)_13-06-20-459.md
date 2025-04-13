error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14031.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14031.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14031.java
text:
```scala
r@@eturn new String[] {"Selection", "SelectionIndex", "ToolTipText"};

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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class TabFolderTab extends Tab {
	/* Example widgets and groups that contain them */
	TabFolder tabFolder1;
	Group tabFolderGroup;
	
	/* Style widgets added to the "Style" group */
	Button topButton, bottomButton;

	static String [] TabItems1 = {ControlExample.getResourceString("TabItem1_0"),
								  ControlExample.getResourceString("TabItem1_1"),
								  ControlExample.getResourceString("TabItem1_2")};

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	TabFolderTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the TabFolder */
		tabFolderGroup = new Group (exampleGroup, SWT.NONE);
		tabFolderGroup.setLayout (new GridLayout ());
		tabFolderGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		tabFolderGroup.setText ("TabFolder");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (topButton.getSelection ()) style |= SWT.TOP;
		if (bottomButton.getSelection ()) style |= SWT.BOTTOM;
		if (borderButton.getSelection ()) style |= SWT.BORDER;

		/* Create the example widgets */
		tabFolder1 = new TabFolder (tabFolderGroup, style);
		for (int i = 0; i < TabItems1.length; i++) {
			TabItem item = new TabItem(tabFolder1, SWT.NONE);
			item.setText(TabItems1[i]);
			item.setToolTipText(ControlExample.getResourceString("Tooltip", new String [] {TabItems1[i]}));
			Text content = new Text(tabFolder1, SWT.WRAP | SWT.MULTI);
			content.setText(ControlExample.getResourceString("TabItem_content") + ": " + i);
			item.setControl(content);
		}
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup ();
		
		/* Create the extra widgets */
		topButton = new Button (styleGroup, SWT.RADIO);
		topButton.setText ("SWT.TOP");
		topButton.setSelection(true);
		bottomButton = new Button (styleGroup, SWT.RADIO);
		bottomButton.setText ("SWT.BOTTOM");
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
	}
	
	/**
	 * Gets the "Example" widget children's items, if any.
	 *
	 * @return an array containing the example widget children's items
	 */
	Item [] getExampleWidgetItems () {
		return tabFolder1.getItems();
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {tabFolder1};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Selection", "SelectionIndex"};
	}

	String setMethodName(String methodRoot) {
		/* Override to handle special case of int getSelectionIndex()/setSelection(int) */
		return (methodRoot.equals("SelectionIndex")) ? "setSelection" : "set" + methodRoot;
	}

	Object[] parameterForType(String typeName, String value, Widget widget) {
		if (value.equals("")) return new Object[] {new TabItem[0]};
		if (typeName.equals("org.eclipse.swt.widgets.TabItem")) {
			TabItem item = findItem(value, ((TabFolder) widget).getItems());
			if (item != null) return new Object[] {item};
		}
		if (typeName.equals("[Lorg.eclipse.swt.widgets.TabItem;")) {
			String[] values = split(value, ',');
			TabItem[] items = new TabItem[values.length];
			for (int i = 0; i < values.length; i++) {
				items[i] = findItem(values[i], ((TabFolder) widget).getItems());
			}
			return new Object[] {items};
		}
		return super.parameterForType(typeName, value, widget);
	}

	TabItem findItem(String value, TabItem[] items) {
		for (int i = 0; i < items.length; i++) {
			TabItem item = items[i];
			if (item.getText().equals(value)) return item;
		}
		return null;
	}

	/**
	 * Gets the short text for the tab folder item.
	 */
	String getShortTabText() {
		return "TF";
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "TabFolder";
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		topButton.setSelection ((tabFolder1.getStyle () & SWT.TOP) != 0);
		bottomButton.setSelection ((tabFolder1.getStyle () & SWT.BOTTOM) != 0);
		borderButton.setSelection ((tabFolder1.getStyle () & SWT.BORDER) != 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14031.java