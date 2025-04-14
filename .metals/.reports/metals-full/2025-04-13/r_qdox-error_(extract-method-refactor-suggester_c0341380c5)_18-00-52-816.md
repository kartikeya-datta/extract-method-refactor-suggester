error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4992.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4992.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4992.java
text:
```scala
m@@enu.add(new Separator("Xpand"));

/*******************************************************************************
 * Copyright (c) 2005 - 2008 committers of openArchitectureWare and others. All
 * rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: committers of openArchitectureWare - initial API and
 * implementation
 ******************************************************************************/
package org.eclipse.xtend.shared.ui.editor;

import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ui.actions.IJavaEditorActionDefinitionIds;
import org.eclipse.jdt.ui.actions.JdtActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.xtend.shared.ui.editor.navigation.OpenAction;
import org.eclipse.xtend.shared.ui.editor.outlineview.AbstractExtXptContentOutlinePage;
import org.eclipse.xtend.shared.ui.editor.search.actions.SearchActionGroup;

public abstract class AbstractXtendXpandEditor extends TextEditor {

	private AbstractExtXptContentOutlinePage outlinePage = null;

	private SearchActionGroup searchActionGroup;

	private BreakpointActionGroup bpActionGroup;

	@Override
	public void doRevertToSaved() {
		super.doRevertToSaved();
		if (outlinePage != null) {
			this.outlinePage.refresh();
		}
	}

	@Override
	public void doSave(final IProgressMonitor aMonitor) {
		super.doSave(aMonitor);
		if (outlinePage != null) {
			this.outlinePage.refresh();
		}
	}

	@Override
	public void doSaveAs() {
		super.doSaveAs();
		if (outlinePage != null) {
			this.outlinePage.refresh();
		}
	}

	@Override
	protected void editorContextMenuAboutToShow(final IMenuManager menu) {
		menu.add(new Separator("mwe"));
		super.editorContextMenuAboutToShow(menu);

		final ActionContext context = new ActionContext(getSelectionProvider().getSelection());
		searchActionGroup.setContext(context);
		searchActionGroup.fillContextMenu(menu);
		searchActionGroup.setContext(null);

		bpActionGroup.fillContextMenu(menu);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(final Class aRequired) {
		if (IContentOutlinePage.class.equals(aRequired)) {
			if (this.outlinePage == null) {
				outlinePage = createOutlinePage();
				if (getEditorInput() != null) {
					outlinePage.setInput(getEditorInput());
				}
			}
			return outlinePage;
		}
		return super.getAdapter(aRequired);
	}

	protected abstract AbstractExtXptContentOutlinePage createOutlinePage();

	@Override
	protected void createActions() {
		super.createActions();
		final ResourceBundle rb = new ResourceBundle() {

			@SuppressWarnings("unchecked")
			@Override
			public Enumeration getKeys() {
				return new Vector().elements();
			}

			@Override
			protected Object handleGetObject(final String key) {
				return null;
			}
		};

		// content assist
		IAction a = new TextOperationAction(rb, "ContentAssistProposal.", this, ISourceViewer.CONTENTASSIST_PROPOSALS);
		a.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		setAction("ContentAssistProposal", a);

		a = new TextOperationAction(rb, "ContentAssistTip.", this, ISourceViewer.CONTENTASSIST_CONTEXT_INFORMATION);
		a.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_CONTEXT_INFORMATION);
		setAction("ContentAssistTip", a);

		// hyperlinking and F3 support
		final OpenAction openAction = new OpenAction(this);
		openAction.setActionDefinitionId(IJavaEditorActionDefinitionIds.OPEN_EDITOR);
		setAction(JdtActionConstants.OPEN, openAction);

		// search
		searchActionGroup = new SearchActionGroup(this);

		// debug
		bpActionGroup = new BreakpointActionGroup(this);
	}

	@Override
	protected void rulerContextMenuAboutToShow(final IMenuManager menu) {
		menu.add(new Separator("Xpand")); //$NON-NLS-1$
		super.rulerContextMenuAboutToShow(menu);

		bpActionGroup.fillContextMenu(menu);
	}

	public ISourceViewer internalGetSourceViewer() {
		return getSourceViewer();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4992.java