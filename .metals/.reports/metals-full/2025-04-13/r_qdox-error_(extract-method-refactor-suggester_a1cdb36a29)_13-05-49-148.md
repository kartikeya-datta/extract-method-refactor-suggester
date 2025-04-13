error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9696.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9696.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9696.java
text:
```scala
A@@cceleratorScope.resetMode(service);

package org.eclipse.ui.internal;
/*
 * (c) Copyright IBM Corp. 2000, 2002.
 * All Rights Reserved.
 */
import java.util.Arrays;

import org.eclipse.jface.action.*;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.internal.registry.AcceleratorScope;

/**
 * A submenu with an item for each accelerator in the AcceleratorScope table.
 */
public class KeyBindingMenu extends ContributionItem {

	private WorkbenchWindow workbenchWindow;
	private Menu acceleratorsMenu;
	private MenuItem cascade;
	private Control focusControl;

	private ResetModeListener resetModeListener = new ResetModeListener();

	private static class ResetModeListener implements Listener {
		private AcceleratorScope scope;
		private KeyBindingService service;
		public void handleEvent (Event event) {
			if(event.type == SWT.Verify)
				event.doit = false;
			scope.resetMode(service);
		}
	};

	/**
	 * Initializes this contribution item with its window.
	 */
	public KeyBindingMenu(WorkbenchWindow window) {
		super("Key binding menu"); //$NON-NLS-1$
		this.workbenchWindow = window;
	}
	/** 
	 * Creates the cascade menu which will be hidden from the user.
	 */
	public void fill(final Menu parent, int index) {
		cascade = new MenuItem(parent, SWT.CASCADE,index);
		cascade.setText("Key binding menu"); //$NON-NLS-1$
		cascade.setMenu (acceleratorsMenu = new Menu (cascade));
		workbenchWindow.getKeyBindingService().setAcceleratorsMenu(this);
		parent.addListener (SWT.Show, new Listener () {
			public void handleEvent (Event event) {
				cascade.setMenu (null);
				cascade.dispose ();
			}
		});
		parent.addListener (SWT.Hide, new Listener () {
			public void handleEvent (Event event) {
				cascade = new MenuItem (parent, SWT.CASCADE, 0);
				cascade.setText("Key binding menu"); //$NON-NLS-1$
				cascade.setMenu(acceleratorsMenu);
			}
		});
	}
	/** 
	 * Disposes the current menu and create a new one with items for
	 * the specified accelerators.
	 */
	public void setAccelerators(final int accs[],final AcceleratorScope scope,final KeyBindingService activeService,boolean defaultMode) {
		if(acceleratorsMenu != null) {
			acceleratorsMenu.dispose();
			acceleratorsMenu = null;
		}
		acceleratorsMenu = new Menu (cascade);
		cascade.setMenu(acceleratorsMenu);
//		Arrays.sort(accs);
		for (int i = 0; i < accs.length; i++) {
			final int acc = accs[i];
			String accId = scope.getActionDefinitionId(acc);
			if(accId == null || activeService.getAction(accId) == null)
				continue;
			MenuItem item = new MenuItem(acceleratorsMenu,SWT.PUSH);
//			item.setText(Action.convertAccelerator(acc));
			item.setAccelerator(acc);
			item.addListener(SWT.Selection, new Listener() {
				public void handleEvent (Event event) {
					scope.processKey(activeService,event,acc);
				}
			});
		}
		resetModeListener.scope = scope;
		resetModeListener.service = activeService;
		updateCancelListener(defaultMode);
	}
	/**
	 * Add/remove the reset mode listener to/from the focus control.
	 * If the control loses focus, is disposed, or any key (which will not
	 * be an accelerator) gets to the control, the mode is reset.
	 */
	private void updateCancelListener(boolean defaultMode) {
		if(defaultMode) {
			if (focusControl != null && !focusControl.isDisposed ()) {
				focusControl.removeListener (SWT.KeyDown, resetModeListener);
				focusControl.removeListener (SWT.Verify, resetModeListener);
				focusControl.removeListener (SWT.FocusOut, resetModeListener);
				focusControl.removeListener (SWT.Dispose, resetModeListener);
			}
		} else {
			Display display = workbenchWindow.getShell().getDisplay ();
			focusControl = display.getFocusControl ();
			//BAD - what about null?
			if (focusControl != null) {
				focusControl.addListener (SWT.KeyDown, resetModeListener);
				focusControl.addListener (SWT.Verify, resetModeListener);				
				focusControl.addListener (SWT.FocusOut, resetModeListener);
				focusControl.addListener (SWT.Dispose, resetModeListener);
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9696.java