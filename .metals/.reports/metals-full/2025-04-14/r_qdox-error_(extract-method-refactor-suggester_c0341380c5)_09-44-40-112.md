error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13045.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13045.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13045.java
text:
```scala
r@@eturn new String[] {"Text", "ToolTipText"};

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
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class GroupTab extends Tab {
	Button titleButton;
	
	/* Example widgets and groups that contain them */
	Group group1;
	Group groupGroup;
	
	/* Style widgets added to the "Style" group */
	Button shadowEtchedInButton, shadowEtchedOutButton, shadowInButton, shadowOutButton, shadowNoneButton;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	GroupTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Other" group.
	 */
	void createOtherGroup () {
		super.createOtherGroup ();
	
		/* Create display controls specific to this example */
		titleButton = new Button (otherGroup, SWT.CHECK);
		titleButton.setText (ControlExample.getResourceString("Title_Text"));
	
		/* Add the listeners */
		titleButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setTitleText ();
			}
		});
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the Group */
		groupGroup = new Group (exampleGroup, SWT.NONE);
		groupGroup.setLayout (new GridLayout ());
		groupGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		groupGroup.setText ("Group");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (shadowEtchedInButton.getSelection ()) style |= SWT.SHADOW_ETCHED_IN;
		if (shadowEtchedOutButton.getSelection ()) style |= SWT.SHADOW_ETCHED_OUT;
		if (shadowInButton.getSelection ()) style |= SWT.SHADOW_IN;
		if (shadowOutButton.getSelection ()) style |= SWT.SHADOW_OUT;
		if (shadowNoneButton.getSelection ()) style |= SWT.SHADOW_NONE;
		if (borderButton.getSelection ()) style |= SWT.BORDER;

		/* Create the example widgets */
		group1 = new Group (groupGroup, style);
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup ();
		
		/* Create the extra widgets */
		shadowEtchedInButton = new Button (styleGroup, SWT.RADIO);
		shadowEtchedInButton.setText ("SWT.SHADOW_ETCHED_IN");
		shadowEtchedInButton.setSelection(true);
		shadowEtchedOutButton = new Button (styleGroup, SWT.RADIO);
		shadowEtchedOutButton.setText ("SWT.SHADOW_ETCHED_OUT");
		shadowInButton = new Button (styleGroup, SWT.RADIO);
		shadowInButton.setText ("SWT.SHADOW_IN");
		shadowOutButton = new Button (styleGroup, SWT.RADIO);
		shadowOutButton.setText ("SWT.SHADOW_OUT");
		shadowNoneButton = new Button (styleGroup, SWT.RADIO);
		shadowNoneButton.setText ("SWT.SHADOW_NONE");
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {group1};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"ToolTipText"};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Group";
	}

	/**
	 * Sets the title text of the "Example" widgets.
	 */
	void setTitleText () {
		if (titleButton.getSelection ()) {
			group1.setText (ControlExample.getResourceString("Title_Text"));
		} else {
			group1.setText ("");
		}
		setExampleWidgetSize ();
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		shadowEtchedInButton.setSelection ((group1.getStyle () & SWT.SHADOW_ETCHED_IN) != 0);
		shadowEtchedOutButton.setSelection ((group1.getStyle () & SWT.SHADOW_ETCHED_OUT) != 0);
		shadowInButton.setSelection ((group1.getStyle () & SWT.SHADOW_IN) != 0);
		shadowOutButton.setSelection ((group1.getStyle () & SWT.SHADOW_OUT) != 0);
		shadowNoneButton.setSelection ((group1.getStyle () & SWT.SHADOW_NONE) != 0);
		borderButton.setSelection ((group1.getStyle () & SWT.BORDER) != 0);
		if (!instance.startup) setTitleText ();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13045.java