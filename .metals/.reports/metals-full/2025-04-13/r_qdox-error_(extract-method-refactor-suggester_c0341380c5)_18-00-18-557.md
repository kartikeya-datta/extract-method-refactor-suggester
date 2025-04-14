error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16808.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16808.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16808.java
text:
```scala
c@@ollabsharedobject.sendOpenAndSelectForFile(null, project.getName() + "/" + file.getProjectRelativePath().toString(), textSelection.getOffset(), textSelection.getLength()); //$NON-NLS-1$

package org.eclipse.ecf.internal.example.collab;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ecf.example.collab.share.EclipseCollabSharedObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.part.FileEditorInput;

public class EditorCompoundContributionItem extends CompoundContributionItem {

	private static final IContributionItem[] EMPTY = new IContributionItem[] {};

	public EditorCompoundContributionItem() {
	}

	public EditorCompoundContributionItem(String id) {
		super(id);
	}

	protected IFile getFileForPart(IEditorPart editorPart) {
		final IEditorInput input = editorPart.getEditorInput();
		if (input instanceof FileEditorInput) {
			final FileEditorInput fei = (FileEditorInput) input;
			return fei.getFile();
		}
		return null;
	}

	protected ClientEntry isConnected(IResource res) {
		if (res == null)
			return null;
		final CollabClient client = CollabClient.getDefault();
		final ClientEntry entry = client.isConnected(res, CollabClient.GENERIC_CONTAINER_CLIENT_NAME);
		return entry;
	}

	protected IEditorPart getEditorPart() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return null;
		final IWorkbenchWindow ww = workbench.getActiveWorkbenchWindow();
		if (ww == null)
			return null;
		final IWorkbenchPage wp = ww.getActivePage();
		if (wp == null)
			return null;
		return wp.getActiveEditor();
	}

	protected ITextSelection getSelection() {
		final IEditorPart ep = getEditorPart();
		if (ep == null)
			return null;
		final IWorkbenchPartSite ws = ep.getEditorSite();
		if (ws == null)
			return null;
		final ISelectionProvider sp = ws.getSelectionProvider();
		if (sp == null)
			return null;
		final ISelection sel = sp.getSelection();
		if (sel == null || !(sel instanceof ITextSelection))
			return null;
		return (ITextSelection) sel;
	}

	protected IContributionItem[] getContributionItems() {
		final ITextSelection textSelection = getSelection();
		if (textSelection == null)
			return EMPTY;
		final IEditorPart editorPart = getEditorPart();
		if (editorPart == null)
			return EMPTY;
		final IFile file = getFileForPart(editorPart);
		if (file == null)
			return EMPTY;
		final IProject project = file.getProject();
		if (isConnected(project.getWorkspace().getRoot()) == null)
			return EMPTY;

		final IAction action = new Action() {
			public void run() {
				final ClientEntry entry = isConnected(project.getWorkspace().getRoot());
				if (entry == null) {
					MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), Messages.EditorCompoundContributionItem_EXCEPTION_NOT_CONNECTED_TITLE, Messages.EditorCompoundContributionItem_EXCEPTION_NOT_CONNECTED_MESSAGE);
					return;
				}
				final EclipseCollabSharedObject collabsharedobject = entry.getSharedObject();
				if (collabsharedobject != null) {
					collabsharedobject.sendOpenAndSelectForFile(null, project.getName() + "/" + file.getProjectRelativePath().toString(), textSelection.getOffset(), textSelection.getLength());
				}
			}
		};

		action.setText(Messages.EditorCompoundContributionItem_SHARE_SELECTION_MENU_ITEM_NAME);
		//action.setAccelerator(SWT.CTRL | SWT.SHIFT | '1');
		return new IContributionItem[] {new Separator(), new ActionContributionItem(action)};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16808.java