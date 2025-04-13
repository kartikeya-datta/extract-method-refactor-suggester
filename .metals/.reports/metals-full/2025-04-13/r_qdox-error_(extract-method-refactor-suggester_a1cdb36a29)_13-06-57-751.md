error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3565.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3565.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3565.java
text:
```scala
static b@@oolean WITH_TEXT = false;

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.
package org.columba.core.gui.util;

import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import org.columba.core.action.BasicAction;
import org.columba.core.config.Config;
import org.columba.core.config.GuiItem;

public class ToolbarButton extends JButton {
	String buttonText;

	static boolean WITH_ICON = true;
	static boolean WITH_TEXT = true;
	static boolean ALIGNMENT = true;

	public ToolbarButton()
	{
		
		super();
		setRequestFocusEnabled(false);
	}
	
	public ToolbarButton(Icon icon)
	{
		super(icon);
		setRequestFocusEnabled(false);
	}
	
	public ToolbarButton(BasicAction a) {
		super(a);

		setRequestFocusEnabled(false);
		setMargin(new Insets(1, 1, 1, 1));

		GuiItem item = Config.getOptionsConfig().getGuiItem();
		/*
		WindowItem item =
			MailConfig.getMainFrameOptionsConfig().getWindowItem();
		*/
		
		if (item.getBoolean("toolbar","enable_icon") == true)
			WITH_ICON = true;
		else
			WITH_ICON = false;

		if (item.getBoolean("toolbar","enable_text") == true)
			WITH_TEXT = true;
		else
			WITH_TEXT = false;

		if (item.getBoolean("toolbar","text_position") == true)
			ALIGNMENT = true;
		else
			ALIGNMENT = false;

		if ((WITH_ICON == true)
			&& (WITH_TEXT == true)
			&& (ALIGNMENT == true)) {

			setVerticalTextPosition(SwingConstants.BOTTOM);
			setHorizontalTextPosition(SwingConstants.CENTER);
			setIcon(a.getLargeIcon());

			setText(a.getToolbarName());

		} else if (
			(WITH_ICON == true)
				&& (WITH_TEXT == true)
				&& (ALIGNMENT == false)) {
			setVerticalTextPosition(SwingConstants.CENTER);
			setHorizontalTextPosition(SwingConstants.RIGHT);
			setIcon(a.getLargeIcon());

			setText(a.getName());

		} else if ((WITH_ICON == true) && (WITH_TEXT == false)) {

			setIcon(a.getLargeIcon());
			setText(null);

		} else if ((WITH_ICON == false) && (WITH_TEXT == true)) {

			setIcon(null);
			setText(a.getName());

		}
	}

	public boolean isFocusTraversable() {
		return isRequestFocusEnabled();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3565.java