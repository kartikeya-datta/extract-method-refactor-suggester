error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2120.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2120.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2120.java
text:
```scala
s@@crolled = new ScrolledComposite(parent, SWT.V_SCROLL

package org.eclipse.ui.internal.progress;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * The DetailedProgressViewer is a viewer that shows the details of all in
 * progress job or jobs that are finished awaiting user input.
 * 
 * @since 3.2
 * 
 */
public class DetailedProgressViewer extends AbstractProgressViewer {

	Composite control;

	private ScrolledComposite scrolled;

	/**
	 * Create a new instance of the receiver with a control that is a child of
	 * parent with style style.
	 * 
	 * @param parent
	 * @param style
	 */
	public DetailedProgressViewer(Composite parent, int style) {
		scrolled = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL
 style);
		int height = JFaceResources.getDefaultFont().getFontData()[0]
				.getHeight();
		scrolled.getVerticalBar().setIncrement(height * 2);
		scrolled.setExpandHorizontal(true);
		scrolled.setExpandVertical(true);

		control = new Composite(scrolled, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		control.setLayout(layout);
		control.setBackground(parent.getDisplay().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND));

		scrolled.setContent(control);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.progress.AbstractProgressViewer#add(java.lang.Object[])
	 */
	public void add(Object[] elements) {
		ViewerSorter sorter = getSorter();
		ArrayList newItems = new ArrayList(control.getChildren().length
				+ elements.length);

		Control[] existingChildren = control.getChildren();
		for (int i = 0; i < existingChildren.length; i++) {
			newItems.add(existingChildren[i].getData());
		}

		for (int i = 0; i < elements.length; i++) {
			newItems.add(elements[i]);
		}

		JobTreeElement[] infos = new JobTreeElement[newItems.size()];
		newItems.toArray(infos);

		if (sorter != null) {
			sorter.sort(this, infos);
		}

		// Update with the new elements to prevent flash
		for (int i = 0; i < existingChildren.length; i++) {
			((ProgressInfoItem) existingChildren[i]).dispose();
		}

		for (int i = 0; i < newItems.size(); i++) {
			ProgressInfoItem item = createNewItem(infos[i]);
			item.setColor(i);
		}

		control.layout(true);
	}

	/**
	 * Create a new item for info.
	 * 
	 * @param info
	 * @return ProgressInfoItem
	 */
	private ProgressInfoItem createNewItem(JobTreeElement info) {
		final ProgressInfoItem item = new ProgressInfoItem(control, SWT.NONE, info);

		
		item.setIndexListener(new ProgressInfoItem.IndexListener(){
			/* (non-Javadoc)
			 * @see org.eclipse.ui.internal.progress.ProgressInfoItem.IndexListener#selectNext()
			 */
			public void selectNext() {
				DetailedProgressViewer.this.selectNext(item);
				
			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.ui.internal.progress.ProgressInfoItem.IndexListener#selectPrevious()
			 */
			public void selectPrevious() {
				DetailedProgressViewer.this.selectPrevious(item);
				
			}
			
			/* (non-Javadoc)
			 * @see org.eclipse.ui.internal.progress.ProgressInfoItem.IndexListener#select()
			 */
			public void select() {
				
				Control[] children = control.getChildren();
				for (int i = 0; i < children.length; i++) {
					ProgressInfoItem child = (ProgressInfoItem) children[i];
					if(!item.equals(child))//Deselect the others
						child.selectWidgets(false);					
				}	
				item.selectWidgets(true);
				
			}
		});
		
		return item;
	}

	/**
	 * Select the previous item in the receiver.
	 * @param item
	 */
	protected void selectPrevious(ProgressInfoItem item) {
		Control[] children = control.getChildren();
		for (int i = 0; i < children.length; i++) {
			ProgressInfoItem child = (ProgressInfoItem) children[i];
			if(item.equals(child)){
				ProgressInfoItem previous;
				if(i == 0)
					previous = (ProgressInfoItem) children[children.length - 1];
				else
					previous = (ProgressInfoItem) children[i - 1];
				
				item.selectWidgets(false);
				previous.selectWidgets(true);
				return;
			}			
		}		
	}

	/**
	 * Select the next item in the receiver.
	 * @param item
	 */
	protected void selectNext(ProgressInfoItem item) {
		Control[] children = control.getChildren();
		for (int i = 0; i < children.length; i++) {
			ProgressInfoItem child = (ProgressInfoItem) children[i];
			if(item.equals(child)){
				ProgressInfoItem next;
				if(i == children.length - 1)
					next = (ProgressInfoItem) children[0];
				else
					next = (ProgressInfoItem) children[i + 1];
				item.selectWidgets(false);
				next.selectWidgets(true);
				
				return;
			}			
		}	
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindInputItem(java.lang.Object)
	 */
	protected Widget doFindInputItem(Object element) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doFindItem(java.lang.Object)
	 */
	protected Widget doFindItem(Object element) {
		Control[] existingChildren = control.getChildren();
		for (int i = 0; i < existingChildren.length; i++) {
			if (existingChildren[i].isDisposed()
 existingChildren[i].getData() == null)
				continue;
			if (existingChildren[i].getData().equals(element))
				return existingChildren[i];
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#doUpdateItem(org.eclipse.swt.widgets.Widget,
	 *      java.lang.Object, boolean)
	 */
	protected void doUpdateItem(Widget item, Object element, boolean fullMap) {
		if (usingElementMap()) {
			unmapElement(item);
		}
		item.dispose();
		add(new Object[] {element});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.Viewer#getControl()
	 */
	public Control getControl() {
		return scrolled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#getSelectionFromWidget()
	 */
	protected List getSelectionFromWidget() {
		return new ArrayList(0);
	}

	protected void inputChanged(Object input, Object oldInput) {
		super.inputChanged(input, oldInput);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#internalRefresh(java.lang.Object)
	 */
	protected void internalRefresh(Object element) {
		if (element == null)
			return;

		if (element.equals(getRoot())){
			refreshAll();
			return;
		}
		Widget widget = findItem(element);
		if (widget == null){
			add(new Object[] {element});
			return;
		}
		((ProgressInfoItem) widget).refresh();

		Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		control.setSize(size);
		scrolled.setMinSize(size);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.progress.AbstractProgressViewer#remove(java.lang.Object[])
	 */
	public void remove(Object[] elements) {

		for (int i = 0; i < elements.length; i++) {

			// Make sure we are not keeping this one
			if (((JobTreeElement) elements[i]).isJobInfo()
					&& FinishedJobs.getInstance().isFinished(
							(JobInfo) elements[i])) {
				Widget item = doFindItem(elements[i]);
				if (item != null)
					((ProgressInfoItem) item).refresh();

			} else {
				Widget item = doFindItem(elements[i]);
				if (item != null) {
					unmapElement(elements[i]);
					item.dispose();
				}
			}
		}

		control.layout(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#reveal(java.lang.Object)
	 */
	public void reveal(Object element) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.StructuredViewer#setSelectionToWidget(java.util.List,
	 *      boolean)
	 */
	protected void setSelectionToWidget(List l, boolean reveal) {

	}

	/**
	 * Cancel the current selection
	 * 
	 */
	public void cancelSelection() {

	}

	/**
	 * Set focus on the current selection.
	 * 
	 */
	public void setFocus() {
		Control [] children = control.getChildren();
		for (int i = 0; i < children.length; i++) {
			ProgressInfoItem item = (ProgressInfoItem) children[i];
			item.setButtonFocus();
			return;			
		}
	}

	/**
	 * Refresh everything as the root is being refreshed.
	 */
	private void refreshAll() {

		Object[] infos = getSortedChildren(getRoot());
		Control[] existingChildren = control.getChildren();

		for (int i = 0; i < existingChildren.length; i++) {
			existingChildren[i].dispose();
			
		}
		// Create new ones if required
		for (int i = 0; i < infos.length; i++) {
			ProgressInfoItem item = createNewItem((JobTreeElement) infos[i]);
			item.setColor(i);
		}


		control.layout(true);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2120.java