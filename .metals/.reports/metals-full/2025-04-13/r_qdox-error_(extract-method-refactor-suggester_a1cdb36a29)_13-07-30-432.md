error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6713.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6713.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6713.java
text:
```scala
s@@ashFormGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class SashFormTab extends Tab {
	/* Example widgets and groups that contain them */
	Group sashFormGroup;
	SashForm form;
	List list1, list2;
	Text text;
	
	/* Style widgets added to the "Style" group */
	Button horizontalButton, verticalButton, smoothButton;

	static String [] ListData0 = {ControlExample.getResourceString("ListData0_0"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData0_1"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData0_2"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData0_3"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData0_4"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData0_5"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData0_6"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData0_7")}; //$NON-NLS-1$
								  
	static String [] ListData1 = {ControlExample.getResourceString("ListData1_0"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData1_1"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData1_2"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData1_3"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData1_4"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData1_5"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData1_6"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData1_7")}; //$NON-NLS-1$


	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	SashFormTab(ControlExample instance) {
		super(instance);
	}
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the sashform widget */
		sashFormGroup = new Group (exampleGroup, SWT.NONE);
		sashFormGroup.setLayout (new GridLayout ());
		sashFormGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		sashFormGroup.setText ("SashForm");
	}
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (horizontalButton.getSelection ()) style |= SWT.H_SCROLL;
		if (verticalButton.getSelection ()) style |= SWT.V_SCROLL;
		if (smoothButton.getSelection ()) style |= SWT.SMOOTH;
		
		/* Create the example widgets */
		form = new SashForm (sashFormGroup, style);
		list1 = new List (form, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		list1.setItems (ListData0);
		list2 = new List (form, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		list2.setItems (ListData1);
		text = new Text (form, SWT.MULTI | SWT.BORDER);
		text.setText (ControlExample.getResourceString("Multi_line")); //$NON-NLS-1$
		form.setWeights(new int[] {1, 1, 1});
	}
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup();
	
		/* Create the extra widgets */
		horizontalButton = new Button (styleGroup, SWT.RADIO);
		horizontalButton.setText ("SWT.HORIZONTAL");
		horizontalButton.setSelection(true);
		verticalButton = new Button (styleGroup, SWT.RADIO);
		verticalButton.setText ("SWT.VERTICAL");
		verticalButton.setSelection(false);
		smoothButton = new Button (styleGroup, SWT.CHECK);
		smoothButton.setText ("SWT.SMOOTH");
		smoothButton.setSelection(false);
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {form};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "SashForm"; //$NON-NLS-1$
	}
	
		/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		horizontalButton.setSelection ((form.getStyle () & SWT.H_SCROLL) != 0);
		verticalButton.setSelection ((form.getStyle () & SWT.V_SCROLL) != 0);
		smoothButton.setSelection ((form.getStyle () & SWT.SMOOTH) != 0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6713.java