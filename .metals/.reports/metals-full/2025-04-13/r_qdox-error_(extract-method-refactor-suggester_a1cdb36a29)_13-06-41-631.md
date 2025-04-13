error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3204.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3204.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3204.java
text:
```scala
s@@etPartName(input.getName());

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.part;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.EditorSite;
import org.eclipse.ui.internal.IWorkbenchThemeConstants;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.eclipse.ui.themes.ITheme;

/**
 * A MultiEditor is a composite of editors.
 * 
 * This class is intended to be subclassed.
 * 		
 */
public abstract class MultiEditor extends EditorPart {

	private int activeEditorIndex;
	private IEditorPart innerEditors[];

	/**
	 * Constructor for TileEditor.
	 */
	public MultiEditor() {
		super();
	}
	/*
	 * @see IEditorPart#doSave(IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		for (int i = 0; i < innerEditors.length; i++) {
			IEditorPart e = innerEditors[i];
			e.doSave(monitor);
		}
	}
	/**
	 * Create the control of the inner editor.
	 * 
	 * Must be called by subclass.
	 */
	public Composite createInnerPartControl(Composite parent,final IEditorPart e) {
		Composite content = new Composite(parent, SWT.NONE);
		content.setLayout(new FillLayout());
		e.createPartControl(content);
		parent.addListener(SWT.Activate, new Listener() {
			public void handleEvent(Event event) {
				if (event.type == SWT.Activate)
					activateEditor(e);
			}
		});
		return content;
	}
			
	/**
	 * The <code>MultiEditor</code> implementation of this 
	 * method extends the <code>EditorPart</code> implementation,
	 * and disposes any inner editors.  Subclasses may extend.
	 * 
	 * @since 3.0
	 */
	public void dispose() {
		super.dispose();
		IEditorPart[] editors = getInnerEditors();
		for (int i = 0; i < editors.length; i++) {
			editors[i].dispose();
		}
	}
	
	/*
	 * @see IEditorPart#doSaveAs()
	 */
	public void doSaveAs() {
	    //no-op
	}

	/*
	 * @see IEditorPart#init(IEditorSite, IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		init(site, (MultiEditorInput) input);
	}
	/*
	 * @see IEditorPart#init(IEditorSite, IEditorInput)
	 */
	public void init(IEditorSite site, MultiEditorInput input) throws PartInitException {
		setInput(input);
		setSite(site);
		setTitle(input.getName());
		setTitleToolTip(input.getToolTipText());
	}
	/*
	 * @see IEditorPart#isDirty()
	 */
	public boolean isDirty() {
		for (int i = 0; i < innerEditors.length; i++) {
			IEditorPart e = innerEditors[i];
			if (e.isDirty())
				return true;
		}
		return false;
	}

	/*
	 * @see IEditorPart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}

	/*
	 * @see IWorkbenchPart#setFocus()
	 */
	public void setFocus() {
		innerEditors[activeEditorIndex].setFocus();
		updateGradient(innerEditors[activeEditorIndex]);
	}
	
	/**
	 * Returns the active inner editor.
	 */
	public final IEditorPart getActiveEditor() {
		return innerEditors[activeEditorIndex];
	}
	
	/**
	 * Returns an array with all inner editors.
	 */
	public final IEditorPart[] getInnerEditors() {
		return innerEditors;
	}
	
	/**
	 * Set the inner editors.
	 * 
	 * Should not be called by clients.
	 */
	public final void setChildren(IEditorPart[] children) {
		innerEditors = children;
		activeEditorIndex = 0;
	}
	
	/**
	 * Activates the given nested editor.
	 * 
	 * @param part the nested editor
	 * @since 3.0
	 */
	protected void activateEditor(IEditorPart part) {
		IEditorPart oldEditor = getActiveEditor();
		activeEditorIndex = getIndex(part);
		IEditorPart e = getActiveEditor();
		EditorSite innerSite = (EditorSite) e.getEditorSite();
		((WorkbenchPage) innerSite.getPage()).requestActivation(e);
		updateGradient(oldEditor);
	}
	
	/**
	 * Returns the index of the given nested editor.
	 * 
	 * @return the index of the nested editor
	 * @since 3.0
	 */
	protected int getIndex(IEditorPart editor) {
		for (int i = 0; i < innerEditors.length; i++) {
			if (innerEditors[i] == editor)
				return i;
		}
		return -1;
	}
	
	/**
	 * Updates the gradient in the title bar.
	 */
	public void updateGradient(IEditorPart editor) {
		boolean activeEditor = editor == getSite().getPage().getActiveEditor();
		boolean activePart = editor == getSite().getPage().getActivePart();

		ITheme theme = editor.getEditorSite().getWorkbenchWindow().getWorkbench().getThemeManager().getCurrentTheme();
		Gradient g = new Gradient();
				       
		ColorRegistry colorRegistry = theme.getColorRegistry();
        if (activePart) {
			g.fgColor = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_TEXT_COLOR);
			g.bgColors = new Color [2];
			g.bgColors[0] = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_START);
			g.bgColors[1] = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_END);
		} else {
			if (activeEditor) {			    
				g.fgColor = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_TEXT_COLOR);							    
				g.bgColors = new Color [2];
				g.bgColors[0] = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_START);
				g.bgColors[1] = colorRegistry.get(IWorkbenchThemeConstants.ACTIVE_TAB_BG_END);
			}
			else {
				g.fgColor = colorRegistry.get(IWorkbenchThemeConstants.INACTIVE_TAB_TEXT_COLOR);
				g.bgColors = new Color [2];
				g.bgColors[0] = colorRegistry.get(IWorkbenchThemeConstants.INACTIVE_TAB_BG_START);
				g.bgColors[1] = colorRegistry.get(IWorkbenchThemeConstants.INACTIVE_TAB_BG_END);
			}
		}
	    g.bgPercents = new int [] {theme.getInt(IWorkbenchThemeConstants.ACTIVE_TAB_PERCENT)};
		
		drawGradient(editor, g);
	}
	/**
	 * Draw the gradient in the title bar.
	 */
	protected abstract void drawGradient(IEditorPart innerEditor,Gradient g);
		
	/**
	 * Return true if the shell is activated.
	 */
	protected boolean getShellActivated() {
		WorkbenchWindow window = (WorkbenchWindow) getSite().getPage().getWorkbenchWindow();
		return window.getShellActivated();
	}
	
	/**
	 * The colors used to draw the title bar of the inner editors
	 */
	public static class Gradient {
		public Color fgColor;
		public Color[] bgColors;
		public int[] bgPercents;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3204.java