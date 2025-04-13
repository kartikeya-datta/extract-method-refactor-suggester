error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10079.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10079.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10079.java
text:
```scala
k@@eyboard = new WorkbenchKeyboard(workbench, workbench.getActivitySupport().getActivityManager(), getCommandManager());

package org.eclipse.ui.internal.commands.ws;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.commands.CommandHandlerServiceFactory;
import org.eclipse.ui.commands.CommandManagerFactory;
import org.eclipse.ui.commands.ICommandManager;
import org.eclipse.ui.commands.ICompoundCommandHandlerService;
import org.eclipse.ui.commands.IWorkbenchCommandSupport;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.keys.WorkbenchKeyboard;
import org.eclipse.ui.keys.KeyFormatterFactory;
import org.eclipse.ui.keys.SWTKeySupport;

public class WorkbenchCommandSupport implements IWorkbenchCommandSupport {

	private ICompoundCommandHandlerService compoundCommandHandlerService;
	/**
	 * The keyboard interface for commands. This handles this dispatching of
	 * key events to the command architecture.
	 */
	private WorkbenchKeyboard keyboard;

	/**
	 * Whether the key binding architecture is currently disabled.  If it is 
	 * disabled, then this means it will not be trapping any key events and 
	 * commands cannot be activated by key events.
	 */
	private volatile boolean keyFilterDisabled;

	private /* TODO IMutableCommandManager */
	ICommandManager mutableCommandManager;

	/**
	 * Constructs a new instance of <code>WorkbenchCommandSupport</code> with
	 * the workbench it should support. The workbench must already have a
	 * command manager defined, and must have a display. This initializes the
	 * key binding support.
	 * 
	 * @param workbench
	 *            The workbench which needs command support; must not be <code>null</code>.
	 */
	public WorkbenchCommandSupport(Workbench workbench) {
		mutableCommandManager = CommandManagerFactory.getCommandManager();
		/* TODO getMutableCommandManager */
		compoundCommandHandlerService =
			CommandHandlerServiceFactory.getCompoundCommandHandlerService();
		KeyFormatterFactory.setDefault(SWTKeySupport.getKeyFormatterForPlatform());		
		keyboard = new WorkbenchKeyboard(workbench, getCommandManager());
		enableKeyFilter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.IWorkbenchCommandSupport#deregisterFromKeyBindings(org.eclipse.swt.widgets.Shell)
	 */
	public void deregisterFromKeyBindings(Shell shell) {
		if (keyboard != null) {
			keyboard.deregister(shell);
		} else {
			String message = "deregisterFromKeyBindings: Global key bindings are not available."; //$NON-NLS-1$
			WorkbenchPlugin.log(
				message,
				new Status(
					IStatus.ERROR,
					WorkbenchPlugin.PI_WORKBENCH,
					0,
					message,
					new Exception()));

		}

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.IWorkbenchCommandSupport#disableKeyFilter()
	 */
	public final void disableKeyFilter() {
		synchronized (keyboard) {
			Display currentDisplay = Display.getCurrent();
			Listener keyFilter = keyboard.getKeyDownFilter();
			currentDisplay.removeFilter(SWT.KeyDown, keyFilter);
			currentDisplay.removeFilter(SWT.Traverse, keyFilter);
			keyFilterDisabled = true;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.IWorkbenchCommandSupport#enableKeyFilter()
	 */
	public final void enableKeyFilter() {
		synchronized (keyboard) {
			Display currentDisplay = Display.getCurrent();
			Listener keyFilter = keyboard.getKeyDownFilter();
			currentDisplay.addFilter(SWT.KeyDown, keyFilter);
			currentDisplay.addFilter(SWT.Traverse, keyFilter);
			keyFilterDisabled = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.IWorkbenchCommandSupport#getCommandManager()
	 */
	public ICommandManager getCommandManager() {
		// TODO need to proxy this to prevent casts to IMutableCommandManager
		return mutableCommandManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.IWorkbenchCommandSupport#getCompoundCommandHandlerService()
	 */
	public ICompoundCommandHandlerService getCompoundCommandHandlerService() {
		return compoundCommandHandlerService;
	}

	/**
	 * <p>
	 * An accessor for the keyboard interface this workbench is using. This can
	 * be used by external class to get a reference with which they can
	 * simulate key presses in the key binding architecture. This is used for
	 * testing purposes currently.
	 * </p>
	 * 
	 * @return A reference to the workbench keyboard interface; never <code>null</code>.
	 */
	public WorkbenchKeyboard getKeyboard() {
		return keyboard;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.IWorkbenchCommandSupport#isKeyFilterEnabled()
	 */
	public final boolean isKeyFilterEnabled() {
		synchronized (keyboard) {
			return !keyFilterDisabled;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.commands.IWorkbenchCommandSupport#registerForKeyBindings(org.eclipse.swt.widgets.Shell,
	 *      boolean)
	 */
	public void registerForKeyBindings(Shell shell, boolean dialogOnly) {
		if (keyboard != null) {
			keyboard.register(shell, dialogOnly);
		} else {
			String message = "registerForKeyBindings: Global key bindings are not available."; //$NON-NLS-1$
			WorkbenchPlugin.log(
				message,
				new Status(
					IStatus.ERROR,
					WorkbenchPlugin.PI_WORKBENCH,
					0,
					message,
					new Exception()));

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10079.java