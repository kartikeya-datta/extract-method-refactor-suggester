error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6120.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6120.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6120.java
text:
```scala
public v@@oid dragEnter(DropTargetEvent event) {

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

package org.eclipse.ui.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Display;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.misc.Assert;
import org.eclipse.ui.part.EditorInputTransfer;
import org.eclipse.ui.part.MarkerTransfer;
import org.eclipse.ui.part.ResourceTransfer;

/* package */ class EditorAreaDropAdapter extends DropTargetAdapter {
	
	private WorkbenchPage page;
	
	/**
	 * Constructs a new EditorAreaDropAdapter.
	 * @param page the workbench page
	 */
	public EditorAreaDropAdapter(WorkbenchPage page) {
		this.page = page;
	}


	public void drop(final DropTargetEvent event) {
		Display d = page.getWorkbenchWindow().getShell().getDisplay();
		d.asyncExec(new Runnable() {
			public void run() {
				asyncDrop(event);
			}
		});
	}

	private void asyncDrop(DropTargetEvent event) {

		/* Open Editor for generic IEditorInput */
		if (EditorInputTransfer.getInstance().isSupportedType(event.currentDataType)) {
			/* event.data is an array of EditorInputData, which contains an IEditorInput and 
			 * the corresponding editorId */
			Assert.isTrue(event.data instanceof EditorInputTransfer.EditorInputData[]);
			EditorInputTransfer.EditorInputData[] editorInputs = (EditorInputTransfer.EditorInputData []) event.data;

			try { //open all the markers
				for (int i = 0; i < editorInputs.length; i++) {
					String editorId = editorInputs[i].editorId;
					IEditorInput editorInput = editorInputs[i].input;
					page.openInternalEditor(editorInput, editorId);	
				}
			} catch (PartInitException e) {
				//do nothing, user may have been trying to drag a marker with no associated file
			}
		}
			
		/* Open Editor for Marker (e.g. Tasks, Bookmarks, etc) */
		else if (MarkerTransfer.getInstance().isSupportedType(event.currentDataType)) {
			Assert.isTrue(event.data instanceof IMarker[]);
			IMarker[] markers = (IMarker[]) event.data;
			try { //open all the markers
				for (int i = 0; i < markers.length; i++) {
					page.openInternalEditor(markers[i]);	
				}
			} catch (PartInitException e) {
				//do nothing, user may have been trying to drag a marker with no associated file
			}
		}

		/* Open Editor for resource */
		else if (ResourceTransfer.getInstance().isSupportedType(event.currentDataType)) {
			Assert.isTrue(event.data instanceof IResource[]);
			IResource[] files = (IResource[]) event.data;
			try { //open all the files
				for (int i = 0; i < files.length; i++) {
					if (files[i] instanceof IFile)
						page.openInternalEditor((IFile)files[i]);	
				}
			} catch (PartInitException e) {
				//do nothing, user may have been trying to drag a folder
			}
		}
			
	}
	
	public void dragOver(DropTargetEvent event) {				
		//make sure the file is never moved; always do a copy
		event.detail = DND.DROP_COPY;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6120.java