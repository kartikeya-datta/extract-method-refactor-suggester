error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9335.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9335.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9335.java
text:
```scala
I@@mage image = display.getSystemImage(SWT.ICON_QUESTION);

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;
/* 
 * example snippet: ExpandBar example
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

public class Snippet223 {

public static void main (String [] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	shell.setText("ExpandBar Example");
	ExpandBar bar = new ExpandBar (shell, SWT.V_SCROLL);
	Image image = new Image(display, Snippet223.class.getResourceAsStream("eclipse.png")); 
	
	// First item
	Composite composite = new Composite (bar, SWT.NONE);
	GridLayout layout = new GridLayout ();
	layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
	layout.verticalSpacing = 10;
	composite.setLayout(layout);
	Button button = new Button (composite, SWT.PUSH);
	button.setText("SWT.PUSH");
	button = new Button (composite, SWT.RADIO);
	button.setText("SWT.RADIO");
	button = new Button (composite, SWT.CHECK);
	button.setText("SWT.CHECK");
	button = new Button (composite, SWT.TOGGLE);
	button.setText("SWT.TOGGLE");
	ExpandItem item0 = new ExpandItem (bar, SWT.NONE, 0);
	item0.setText("What is your favorite button");
	item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
	item0.setControl(composite);
	item0.setImage(image);
	
	// Second item
	composite = new Composite (bar, SWT.NONE);
	layout = new GridLayout (2, false);
	layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
	layout.verticalSpacing = 10;
	composite.setLayout(layout);	
	Label label = new Label (composite, SWT.NONE);
	label.setImage(display.getSystemImage(SWT.ICON_ERROR));
	label = new Label (composite, SWT.NONE);
	label.setText("SWT.ICON_ERROR");
	label = new Label (composite, SWT.NONE);
	label.setImage(display.getSystemImage(SWT.ICON_INFORMATION));
	label = new Label (composite, SWT.NONE);
	label.setText("SWT.ICON_INFORMATION");
	label = new Label (composite, SWT.NONE);
	label.setImage(display.getSystemImage(SWT.ICON_WARNING));
	label = new Label (composite, SWT.NONE);
	label.setText("SWT.ICON_WARNING");
	label = new Label (composite, SWT.NONE);
	label.setImage(display.getSystemImage(SWT.ICON_QUESTION));
	label = new Label (composite, SWT.NONE);
	label.setText("SWT.ICON_QUESTION");
	ExpandItem item1 = new ExpandItem (bar, SWT.NONE, 1);
	item1.setText("What is your favorite icon");
	item1.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
	item1.setControl(composite);
	item1.setImage(image);
	
	// Third item
	composite = new Composite (bar, SWT.NONE);
	layout = new GridLayout (2, true);
	layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
	layout.verticalSpacing = 10;
	composite.setLayout(layout);
	label = new Label (composite, SWT.NONE);
	label.setText("Scale");	
	new Scale (composite, SWT.NONE);
	label = new Label (composite, SWT.NONE);
	label.setText("Spinner");	
	new Spinner (composite, SWT.BORDER);
	label = new Label (composite, SWT.NONE);
	label.setText("Slider");	
	new Slider (composite, SWT.NONE);
	ExpandItem item2 = new ExpandItem (bar, SWT.NONE, 2);
	item2.setText("What is your favorite range widget");
	item2.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
	item2.setControl(composite);
	item2.setImage(image);
	
	item1.setExpanded(true);
	bar.setSpacing(8);
	shell.setSize(400, 350);
	shell.open();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) {
			display.sleep ();
		}
	}
	image.dispose();
	display.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9335.java