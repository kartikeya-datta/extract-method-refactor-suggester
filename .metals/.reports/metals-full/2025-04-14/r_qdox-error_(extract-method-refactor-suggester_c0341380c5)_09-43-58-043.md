error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2473.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2473.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2473.java
text:
```scala
I@@mageDescriptor desc = ((WorkbenchPreferenceNode) node).getImageDescriptor();

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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.GenericListItem;

/**
 * The PreferencesCategoryItem is the item for displaying 
 * preferences in the WorkbenchPreferencesDialog.
 */
public class PreferencesTreeItem extends GenericListItem {

	private Composite control;

	private CLabel imageLabel;

	private CLabel textLabel;
	
	private Color gradientColor;
	
	//Keep a reference for the life of the instance.
	private Image cachedImage;

	/**
	 * Create a new instance of the receiver for displaying
	 * wrapped element.
	 * @param wrappedElement
	 */
	public PreferencesTreeItem(Object wrappedElement) {
		super(wrappedElement);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.GenericListItem#dispose()
	 */
	public void dispose() {
		cachedImage.dispose();

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.GenericListItem#createControl(org.eclipse.swt.widgets.Composite, org.eclipse.swt.graphics.Color)
	 */
	public void createControl(Composite parent, Color color) {

		IPreferenceNode node = (IPreferenceNode) getElement();

		gradientColor = color;
		
		control = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		control.setLayout(layout);
		control.setBackground(color);

		Image image;
		ImageDescriptor desc = ((WorkbenchPreferenceNode) node).getDescriptor();
		if (desc == null)
			image = JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_INFO);
		else{
			cachedImage = desc.createImage(true);
			image = cachedImage;
		}
		
		imageLabel = new CLabel(control, SWT.CENTER);
		imageLabel.setImage(image);
		imageLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		textLabel = new CLabel(control, SWT.CENTER);
		textLabel.setText(node.getLabelText());
		textLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		setColors(false);
		

	}

	/**
	 * Set the colors of the receiver based on whether or
	 * not it is selected.
	 * @param selected whether or not the receiver is selected
	 */
	private void setColors(boolean selected) {
		
		if(selected){
			Color selection = imageLabel.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION);
			imageLabel.setBackground(selection);
			textLabel.setBackground(selection);
			return;
		}
		
		Color[] gradientColors = new Color[] { gradientColor,
				imageLabel.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND),
				imageLabel.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND),

		};
		Color background = imageLabel.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		
		imageLabel.setBackground(background);
		textLabel.setBackground(background);
		
		imageLabel.setBackground(gradientColors,new int[] {50,50});
		textLabel.setBackground(gradientColors,new int[] {50,50});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.GenericListItem#getControl()
	 */
	public Control getControl() {
		return control;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.GenericListItem#addMouseListener(org.eclipse.swt.events.MouseListener)
	 */
	public void addMouseListener(MouseListener listener) {
		imageLabel.addMouseListener(listener);
		textLabel.addMouseListener(listener);

	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.GenericListItem#clearHighlight()
	 */
	public void clearHighlight() {
		setColors(false);

	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.GenericListItem#highlightForSelection()
	 */
	public void highlightForSelection() {
		setColors(true);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2473.java