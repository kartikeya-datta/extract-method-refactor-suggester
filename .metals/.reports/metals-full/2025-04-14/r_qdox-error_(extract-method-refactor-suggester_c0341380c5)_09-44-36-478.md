error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9025.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9025.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9025.java
text:
```scala
v@@iewer = new ProgressViewer(region, SWT.NO_FOCUS, 1,36);

/**********************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved.   This
 * program and the accompanying materials are made available under the terms of
 * the Common Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: 
 * IBM - Initial API and implementation
 **********************************************************************/
package org.eclipse.ui.internal.progress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import org.eclipse.jface.viewers.IContentProvider;

import org.eclipse.ui.internal.WorkbenchWindow;
/**
 * The ProgressRegion is class for the region of the workbench where the
 * progress line and the animation item are shown.
 */
public class ProgressRegion {
	ProgressViewer viewer;
	AnimationItem item;
	Composite region;
	WorkbenchWindow workbenchWindow;
	
	/**
	 * Create a new instance of the receiver.
	 */
	public ProgressRegion() {
		//No default behavior.
	}
	/**
	 * Create the contents of the receiver in the parent. Use the
	 * window for the animation item. 
	 * @param parent The parent widget of the composite.
	 * @param window The WorkbenchWindow this is in.
	 * @return
	 */
	public Control createContents(Composite parent, WorkbenchWindow window) {
		
		workbenchWindow = window;
		
		region = new Composite(parent, SWT.NONE);
		FormLayout regionLayout = new FormLayout();
		region.setLayout(regionLayout);
		
		Label seperator = new Label(region, SWT.SEPARATOR);
		
		item = new ProgressAnimationItem(window);
		item.createControl(region);
		Control itemControl = item.getControl();
		
		viewer = new ProgressViewer(region, SWT.NONE, 1,36);
		viewer.setUseHashlookup(true);
		Control viewerControl = viewer.getControl();
		
		int widthPreference = AnimationManager.getInstance().getPreferredWidth();
		Point preferredSize = viewer.getSizeHints();
		int margin = 2;
		
		FormData labelData = new FormData();
		labelData.left = new FormAttachment(0, margin);
		labelData.top = new FormAttachment(0);
		seperator.setLayoutData(labelData);
		
		FormData itemData = new FormData();
		itemData.right = new FormAttachment(100, (-1 * margin));
		itemData.top = new FormAttachment(viewerControl,margin,SWT.TOP);
		itemData.width = widthPreference + (margin * 2);
		itemData.height = preferredSize.y;
		itemControl.setLayoutData(itemData);

		
		FormData viewerData = new FormData();
		viewerData.left = new FormAttachment(seperator,margin);
		viewerData.right = new FormAttachment(itemControl, margin);
		viewerData.top = new FormAttachment(0);
		viewerData.bottom = new FormAttachment(100);
		
		viewerData.width = preferredSize.x + margin;
		viewerData.height = preferredSize.y;
		viewerControl.setLayoutData(viewerData);
		
		viewerControl.addMouseListener(new MouseAdapter(){
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.MouseAdapter#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
			 */
			public void mouseDoubleClick(MouseEvent e) {
				ProgressManagerUtil.openProgressView(workbenchWindow);
			}
		});
		
		viewerControl.addMouseTrackListener(new MouseTrackListener(){
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
			 */
			public void mouseEnter(MouseEvent e) {
				//Do nothing

			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
			 */
			public void mouseExit(MouseEvent e) {
				//Do nothing

			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
			 */
			public void mouseHover(MouseEvent e) {
				item.openFloatingWindow();
			}
			
		});
		
		IContentProvider provider = new ProgressViewerContentProvider(viewer);
		viewer.setContentProvider(provider);
		viewer.setInput(provider);
		viewer.setLabelProvider(new ProgressViewerLabelProvider(viewerControl));
		viewer.setSorter(ProgressManagerUtil.getProgressViewerSorter());
		return region;
	}
	
	
	/**
	 * Return the animationItem for the receiver.
	 * @return
	 */
	public AnimationItem getAnimationItem(){
		return item;
	}
	
	/**
	 * Return the control for the receiver.
	 * @return
	 */
	public Control getControl(){
		return region;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9025.java