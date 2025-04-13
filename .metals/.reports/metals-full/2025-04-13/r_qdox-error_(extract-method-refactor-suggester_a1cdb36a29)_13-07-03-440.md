error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5507.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5507.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5507.java
text:
```scala
w@@indowShell.setActive();

package org.eclipse.ui.internal;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import org.eclipse.jface.action.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

/**
 * A dynamic menu item to switch to other opened workbench windows.
 */
public class SwitchToWindowMenu extends ContributionItem {
	private static final int MAX_TEXT_LENGTH = 40;
	
	private WorkbenchWindow workbenchWindow;
	private boolean showSeparator;
	private boolean dirty = true;
	private IMenuListener menuListener = new IMenuListener() {
		public void menuAboutToShow(IMenuManager manager) {
			manager.markDirty();
			dirty = true;
		}
	};
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param window the workbench window this action applies to
	 * @param showSeparator whether to add a separator in the menu
	 */
	public SwitchToWindowMenu(WorkbenchWindow window, boolean showSeparator) {
		super("Switch To Window"); //$NON-NLS-1$
		this.workbenchWindow = window;
		this.showSeparator = showSeparator;
	}
	
	/**
	 * Returns the text for a window. This may be truncated to fit
	 * within the MAX_TEXT_LENGTH.
	 */
	private String calcText(int number, IWorkbenchWindow window) {
		String suffix = window.getShell().getText();
		if (suffix == null)
			return null;
			
		StringBuffer sb = new StringBuffer();
		if (number < 10)
			sb.append('&');
		sb.append(number);
		sb.append(' ');
		if (suffix.length() <= MAX_TEXT_LENGTH) {
			sb.append(suffix);
		} else {
			sb.append(suffix.substring(0, MAX_TEXT_LENGTH));
			sb.append("..."); //$NON-NLS-1$
		}
		return sb.toString();
	}
	
	/**
	 * Fills the given menu with menu items for all
	 * opened workbench windows.
	 */
	public void fill(Menu menu, int index) {
		
		// Get workbench windows.
		IWorkbench workbench = workbenchWindow.getWorkbench();
		IWorkbenchWindow[] array = workbench.getWorkbenchWindows();
		// avoid showing the separator and list for 0 or 1 items
		if (array.length < 2)
			return;

		if(getParent() instanceof MenuManager)
			((MenuManager)getParent()).addMenuListener(menuListener);
		
		if(!dirty)
			return;
						
		// Add separator.
		if (showSeparator) {
			new MenuItem(menu, SWT.SEPARATOR, index);
			++index;
		}

		// Add one item for each window.
		int count = 1;
		for (int i = 0; i < array.length; i++) {
			final IWorkbenchWindow window = array[i];
			// can encounter disposed shells if this update is in response to a shell closing
			if (!window.getShell().isDisposed()) {
				String name = calcText(count, window);
				if (name != null) {
					MenuItem mi = new MenuItem(menu, SWT.RADIO, index);
					index++;
					count++;
					mi.setText(name);
					mi.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent e) {
							Shell windowShell = window.getShell();
							if (windowShell.getMinimized())
								windowShell.setMinimized(false);
							windowShell.forceFocus();
							windowShell.moveAbove(null);
						}
					});
					mi.setSelection(window == workbenchWindow);
				}
			}
		}
		dirty = false;
	}
	/**
	 * Overridden to always return true and force dynamic menu building.
	 */
	public boolean isDirty() {
		return dirty;
	}	
	/**
	 * Overridden to always return true and force dynamic menu building.
	 */
	public boolean isDynamic() {
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5507.java