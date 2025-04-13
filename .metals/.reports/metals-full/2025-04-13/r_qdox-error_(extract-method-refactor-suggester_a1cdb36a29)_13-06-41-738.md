error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/105.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/105.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/105.java
text:
```scala
I@@WorkbenchAdapter2.class);

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
package org.eclipse.ui.model;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * Provides basic labels for adaptable objects that have the
 * <code>IWorkbenchAdapter</code> adapter associated with them.  All dispensed
 * images are cached until the label provider is explicitly disposed.
 * This class provides a facility for subclasses to define annotations
 * on the labels and icons of adaptable objects.
 */
public class WorkbenchLabelProvider extends LabelProvider implements IColorProvider {
	/**
	 * The cache of images that have been dispensed by this provider.
	 * Maps ImageDescriptor->Image.
	 */
	private Map imageTable;
	
	/**
	 * The cache of colors that have been dispensed by this provider.
	 * Maps RGB->Color.
	 */
	private Map colorTable;

	/**
	 * Returns a workbench label provider that is hooked up to the decorator
	 * mechanism.
	 * 
	 * @return a new <code>DecoratingLabelProvider</code> which wraps a <code>
	 *   new <code>WorkbenchLabelProvider</code>
	 */
	public static ILabelProvider getDecoratingWorkbenchLabelProvider() {
		return new DecoratingLabelProvider(
			new WorkbenchLabelProvider(),
			PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator());
	}
	/**
	 * Creates a new workbench label provider.
	 */
	public WorkbenchLabelProvider() {
	    // no-op
	}

	/**
	 * Returns an image descriptor that is based on the given descriptor,
	 * but decorated with additional information relating to the state
	 * of the provided object.
	 *
	 * Subclasses may reimplement this method to decorate an object's
	 * image.
	 * @see org.eclipse.jface.resource.CompositeImageDescriptor
	 */
	protected ImageDescriptor decorateImage(
		ImageDescriptor input,
		Object element) {
		return input;
	}
	/**
	 * Returns a label that is based on the given label,
	 * but decorated with additional information relating to the state
	 * of the provided object.
	 *
	 * Subclasses may implement this method to decorate an object's
	 * label.
	 */
	protected String decorateText(String input, Object element) {
		return input;
	}
	/* (non-Javadoc)
	 * Method declared on IBaseLabelProvider
	 */
	/**
	 * Disposes of all allocated images.
	 */
	public final void dispose() {
		if (imageTable != null) {
			for (Iterator i = imageTable.values().iterator(); i.hasNext();) {
				((Image) i.next()).dispose();
			}
			imageTable = null;
		}
		if (colorTable != null) {
			for (Iterator i = colorTable.values().iterator(); i.hasNext();) {
				((Color) i.next()).dispose();
			}
			colorTable = null;		    
		}
	}
	/**
	 * Returns the implementation of IWorkbenchAdapter for the given
	 * object.  Returns <code>null</code> if the adapter is not defined or the
	 * object is not adaptable.
	 */
	protected final IWorkbenchAdapter getAdapter(Object o) {
		if (!(o instanceof IAdaptable)) {
			return null;
		}
		return (IWorkbenchAdapter) ((IAdaptable) o).getAdapter(
			IWorkbenchAdapter.class);
	}
	
	/**
	 * Returns the implementation of IWorkbenchAdapter2 for the given
	 * object.  Returns <code>null</code> if the adapter is not defined or the
	 * object is not adaptable.
	 */
	protected final IWorkbenchAdapter2 getAdapter2(Object o) {
		if (!(o instanceof IAdaptable)) {
			return null;
		}
		return (IWorkbenchAdapter2) ((IAdaptable) o).getAdapter(
			IWorkbenchAdapter.class);
	}
	
	/* (non-Javadoc)
	 * Method declared on ILabelProvider
	 */
	public final Image getImage(Object element) {
		//obtain the base image by querying the element
		IWorkbenchAdapter adapter = getAdapter(element);
		if (adapter == null) {
			return null;
		}
		ImageDescriptor descriptor = adapter.getImageDescriptor(element);
		if (descriptor == null) {
			return null;
		}

		//add any annotations to the image descriptor
		descriptor = decorateImage(descriptor, element);

		//obtain the cached image corresponding to the descriptor
		if (imageTable == null) {
			imageTable = new Hashtable(40);
		}
		Image image = (Image) imageTable.get(descriptor);
		if (image == null) {
			image = descriptor.createImage();
			imageTable.put(descriptor, image);
		}
		return image;
	}
	/* (non-Javadoc)
	 * Method declared on ILabelProvider
	 */
	public final String getText(Object element) {
		//query the element for its label
		IWorkbenchAdapter adapter = getAdapter(element);
		if (adapter == null) {
			return ""; //$NON-NLS-1$
		}
		String label = adapter.getLabel(element);

		//return the decorated label
		return decorateText(label, element);
	}
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
     */
    public Color getForeground(Object element) {
        return getColor(element, true);
    }
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
     */
    public Color getBackground(Object element) {
        return getColor(element, false);
    }
    
    private Color getColor(Object element, boolean forground) {
		IWorkbenchAdapter2 adapter = getAdapter2(element);
		if (adapter == null) {
			return null;
		}
		RGB descriptor = 
		    forground ? 
		            adapter.getForeground(element) 
		            : adapter.getBackground(element);
		if (descriptor == null) {
			return null;
		}

		//obtain the cached color corresponding to the descriptor
		if (colorTable == null) {
			colorTable = new Hashtable(7);
		}
		Color color = (Color) colorTable.get(descriptor);
		if (color == null) {
			color = new Color(Display.getCurrent(), descriptor);
			colorTable.put(descriptor, color);
		}
		return color;
        
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/105.java