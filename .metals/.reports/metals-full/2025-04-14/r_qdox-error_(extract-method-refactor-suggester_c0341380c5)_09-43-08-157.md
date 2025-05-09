error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/469.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/469.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/469.java
text:
```scala
c@@reateEntry(node, "General", 0);  //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * The PreferencesPageContainer is the container object for 
 * the preference pages in a node.
 */
public class PreferencesPageContainer implements IPreferencePageContainer{

	private Composite control;
	private ScrolledComposite scrolled;

	private class PreferenceEntry {

		Composite composite;

		IPreferenceNode node;

		String title;

		int offset;

		/**
		 * Create a new instance of the receiver.
		 * @param displayedNode
		 * @param pageTitle
		 */
		PreferenceEntry(IPreferenceNode displayedNode, String pageTitle) {
			node = displayedNode;
			title = pageTitle;
		}

		/**
		 * Create the contents of the entry in parent.
		 * When laying this out indent the composite
		 * ident units.
		 * @param indent
		 */
		void createContents(int indent) {
			
			composite = new Composite(control, SWT.NULL);
			
			// Create the title area which will contain
			// a title, message, and image.

			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalIndent = IDialogConstants.SMALL_INDENT * indent;
			composite.setLayoutData(gridData);
			
			FormLayout layout = new FormLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			composite.setLayout(layout);
	
			Font titleFont = JFaceResources.getBannerFont();
			
			Label expandImage = new Label(composite,SWT.RIGHT);
			expandImage.setText("+");//$NON-NLS-1$
			expandImage.setFont(titleFont);
			
			FormData imageData = new FormData();
			imageData.top = new FormAttachment(0);
			imageData.left = new FormAttachment(0,IDialogConstants.HORIZONTAL_SPACING);
			expandImage.setLayoutData(imageData);

			// Title image
			final Label titleLabel = new Label(composite, SWT.LEFT);
			titleLabel.setText(title);
			titleLabel.setFont(titleFont);
			
			FormData titleData = new FormData();
			titleData.right = new FormAttachment(100);
			titleData.top = new FormAttachment(0);
			titleData.left = new FormAttachment(expandImage,IDialogConstants.HORIZONTAL_SPACING);
			titleLabel.setLayoutData(titleData);
			
			titleLabel.addMouseListener(new MouseAdapter(){
				/* (non-Javadoc)
				 * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.MouseEvent)
				 */
				public void mouseDown(MouseEvent e) {
					
					Composite pageContainer = new Composite(composite,SWT.BORDER);
	
					FormData containerData = new FormData();
					containerData.top = new FormAttachment(titleLabel,0);
					containerData.left = new FormAttachment(0);
					containerData.right = new FormAttachment(100);
					pageContainer.setLayoutData(containerData);
					
					pageContainer.setLayout(new GridLayout());
					
					node.createPage();
					node.getPage().createControl(pageContainer);
					node.getPage().setContainer(PreferencesPageContainer.this);	
					node.getPage().getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
					Point contentSize = node.getPage().computeSize();	
					Rectangle totalArea = composite.getClientArea();
					
					if(contentSize.x < totalArea.width)
						contentSize.x = totalArea.width;
					
					node.getPage().setSize(contentSize);
					
					adjustScrollbars(contentSize);

					
					control.layout(true);
				}
			});
			offset = indent;
		}

	}

	/**
	 * Create a new instance of the receiver.
	 */
	public PreferencesPageContainer() {
		super();
	}

	/**
	 * Create the contents area of the composite.
	 * @param parent
	 * @param style
	 */
	void createContents(Composite parent, int style) {
		scrolled = new ScrolledComposite(parent,SWT.V_SCROLL | SWT.H_SCROLL);

		GridData newPageData = new GridData(GridData.FILL_BOTH);
		scrolled.setLayoutData(newPageData);
		
		control = new Composite(scrolled, style);

		scrolled.setContent(control);
        scrolled.setExpandVertical(true);
        scrolled.setExpandHorizontal(true);
		GridData controlData = new GridData(GridData.FILL_BOTH);
		control.setLayoutData(controlData);
		
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 1;
		control.setLayout(layout);
	

	}

	/**
	 * Return the top level control
	 * @return Control
	 */
	Control getControl() {
		return control;
	}

	/**
	 * Show the selected node. Return whether or
	 * not this succeeded.
	 * @param node
	 * @return <code>true</code> if the page selection was sucessful
	 *         <code>false</code> is unsuccessful
	 * @see org.eclipse.jface.preference.PreferenceDialog#showPage(IPreferenceNode)
	 */
	boolean show(IPreferenceNode node) {
		createGeneralEntry(node);		
		control.layout(true);
		return true;
	}

	/**
	 * Create an entry for the receiver with
	 * the general tag. Do not recurse through the
	 * children as this is the implied top node.
	 * @param node
	 */
	private void createGeneralEntry(IPreferenceNode node) {
		createEntry(node, "General", 0);
		IPreferenceNode[] subnodes = node.getSubNodes();
		for (int i = 0; i < subnodes.length; i++) {
			createEntry(subnodes[i], subnodes[i].getLabelText(), 1);

		}

	}

	/**
	 * Create an entry with the given title for the
	 * IPreferenceNode with an indent i.
	 * @param node 
	 * @param name
	 * @param indent
	 */
	private void createEntry(IPreferenceNode node, String name, int indent) {
		PreferenceEntry entry = new PreferenceEntry(node, name);
		entry.createContents(indent);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#getPreferenceStore()
	 */
	public IPreferenceStore getPreferenceStore() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateButtons()
	 */
	public void updateButtons() {
		// TODO Auto-generated method stub

	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateMessage()
	 */
	public void updateMessage() {
		// TODO Auto-generated method stub

	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.IPreferencePageContainer#updateTitle()
	 */
	public void updateTitle() {
		// TODO Auto-generated method stub

	}

	/**
	 * Adjust the scrollbars for the content that just
	 * got added.
	 * @param contentSize
	 */
	private void adjustScrollbars(Point contentSize) {
		
		Point size = control.getSize();
		scrolled.setMinHeight(size.y + contentSize.y);
		scrolled.setMinWidth(size.x + contentSize.x);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/469.java