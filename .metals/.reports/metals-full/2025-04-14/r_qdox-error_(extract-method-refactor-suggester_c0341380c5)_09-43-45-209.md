error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4281.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4281.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4281.java
text:
```scala
i@@f (button instanceof TextButton && text.equals(((TextButton)button).getText())) {

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.utils.Array;

/** Manages a group of buttons to enforce a minimum and maximum number of checked buttons. This enables "radio button"
 * functionality and more. A button may only be in one group at a time.
 * @author Nathan Sweet */
public class ButtonGroup {
	private final Array<Button> buttons = new Array();
	private Array<Button> checkedButtons = new Array(1);
	private int minCheckCount, maxCheckCount = 1;
	private boolean uncheckLast = true;
	private Button lastChecked;

	public ButtonGroup () {
		minCheckCount = 1;
	}

	public ButtonGroup (Button... buttons) {
		minCheckCount = 0;
		add(buttons);
		minCheckCount = 1;
	}

	public void add (Button button) {
		if (button == null) throw new IllegalArgumentException("button cannot be null.");
		button.buttonGroup = null;
		boolean shouldCheck = button.isChecked() || buttons.size < minCheckCount;
		button.setChecked(false);
		button.buttonGroup = this;
		buttons.add(button);
		if (shouldCheck) button.setChecked(true);
	}

	public void add (Button... buttons) {
		if (buttons == null) throw new IllegalArgumentException("buttons cannot be null.");
		for (int i = 0, n = buttons.length; i < n; i++)
			add(buttons[i]);
	}

	public void remove (Button button) {
		if (button == null) throw new IllegalArgumentException("button cannot be null.");
		button.buttonGroup = null;
		buttons.removeValue(button, true);
	}

	public void remove (Button... buttons) {
		if (buttons == null) throw new IllegalArgumentException("buttons cannot be null.");
		for (int i = 0, n = buttons.length; i < n; i++)
			remove(buttons[i]);
	}

	/** Sets the first {@link TextButton} with the specified text to checked. */
	public void setChecked (String text) {
		if (text == null) throw new IllegalArgumentException("text cannot be null.");
		for (int i = 0, n = buttons.size; i < n; i++) {
			Button button = buttons.get(i);
			if (button instanceof TextButton && text.contentEquals(((TextButton)button).getText())) {
				button.setChecked(true);
				return;
			}
		}
	}

	/** Called when a button is checked or unchecked.
	 * @return true if the new state should be allowed. */
	protected boolean canCheck (Button button, boolean newState) {
		if (button.isChecked == newState) return false;

		if (!newState) {
			// Keep button checked to enforce minCheckCount.
			if (checkedButtons.size <= minCheckCount) return false;
			checkedButtons.removeValue(button, true);
		} else {
			// Keep button unchecked to enforce maxCheckCount.
			if (maxCheckCount != -1 && checkedButtons.size >= maxCheckCount) {
				if (uncheckLast) {
					int old = minCheckCount;
					minCheckCount = 0;
					lastChecked.setChecked(false);
					minCheckCount = old;
				} else
					return false;
			}
			checkedButtons.add(button);
			lastChecked = button;
		}

		return true;
	}

	/** Sets all buttons' {@link Button#isChecked()} to false, regardless of {@link #setMinCheckCount(int)}. */
	public void uncheckAll () {
		int old = minCheckCount;
		minCheckCount = 0;
		for (int i = 0, n = buttons.size; i < n; i++) {
			Button button = buttons.get(i);
			button.setChecked(false);
		}
		minCheckCount = old;
	}

	/** @return the first checked button, or null. */
	public Button getChecked () {
		if (checkedButtons.size > 0) return checkedButtons.get(0);
		return null;
	}

	public Array<Button> getAllChecked () {
		return checkedButtons;
	}

	public Array<Button> getButtons () {
		return buttons;
	}

	/** Sets the minimum number of buttons that must be checked. Default is 1. */
	public void setMinCheckCount (int minCheckCount) {
		this.minCheckCount = minCheckCount;
	}

	/** Sets the maximum number of buttons that can be checked. Set to -1 for no maximum. Default is 1. */
	public void setMaxCheckCount (int maxCheckCount) {
		this.maxCheckCount = maxCheckCount;
	}

	/** If true, when the maximum number of buttons are checked and an additional button is checked, the last button to be checked
	 * is unchecked so that the maximum is not exceeded. If false, additional buttons beyond the maximum are not allowed to be
	 * checked. Default is true. */
	public void setUncheckLast (boolean uncheckLast) {
		this.uncheckLast = uncheckLast;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4281.java