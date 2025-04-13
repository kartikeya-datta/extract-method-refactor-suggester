error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4868.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4868.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4868.java
text:
```scala
r@@eturn Util.ZERO_LENGTH_STRING;

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
package org.eclipse.ui.internal;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.internal.util.Util;
import org.eclipse.ui.presentations.IPartMenu;
import org.eclipse.ui.presentations.IPresentablePart;

/**
 * This is a lightweight wrapper around the ViewPane. It allows a ViewPane to be used as an IPresentablePart.
 * All methods here should either redirect directly to ViewPane or do trivial conversions.
 */
public class PresentableViewPart implements IPresentablePart {

    private final List listeners = new ArrayList();
 
    private ViewPane pane;
    
    private IPartMenu viewMenu = new IPartMenu() {
		public void showMenu(Point location) {
			pane.showViewMenu(location);
		}
    };

    private final IPropertyListener propertyListenerProxy = new IPropertyListener() {

        public void propertyChanged(Object source, int propId) {
            for (int i = 0; i < listeners.size(); i++)
                ((IPropertyListener) listeners.get(i)).propertyChanged(
                        PresentableViewPart.this, propId);
        }
    };

    public PresentableViewPart(ViewPane pane) {
        this.pane = pane;
    }

    public void firePropertyChange(int propertyId) {
		 for (int i = 0; i < listeners.size(); i++)
            ((IPropertyListener) listeners.get(i)).propertyChanged(
                    this, propertyId);    	
    }
    
    public void addPropertyListener(final IPropertyListener listener) {
        if (listeners.isEmpty())
                getViewReference().addPropertyListener(propertyListenerProxy);

        listeners.add(listener);
    }

    public String getName() {
        WorkbenchPartReference ref = (WorkbenchPartReference) pane
                .getPartReference();
        return Util.safeString(ref.getRegisteredName());
    }

    public String getTitle() {
        return Util.safeString(getViewReference().getTitle());
    }

    public Image getTitleImage() {
        return getViewReference().getTitleImage();
    }

    public String getTitleToolTip() {
        return Util.safeString(getViewReference().getTitleToolTip());
    }

    private IViewReference getViewReference() {
        return pane.getViewReference();
    }

    public boolean isDirty() {
        return false;
    }

    public void removePropertyListener(final IPropertyListener listener) {
        listeners.remove(listener);

        if (listeners.isEmpty())
                getViewReference()
                        .removePropertyListener(propertyListenerProxy);
    }

    public void setBounds(Rectangle bounds) {
        pane.setBounds(bounds);
    }

    public void setFocus() {
        pane.setFocus();
    }

    public void setVisible(boolean isVisible) {
        pane.setVisible(isVisible);
    }
    
    /* (non-Javadoc)
	 * @see org.eclipse.ui.presentations.IPresentablePart#isBusy()
	 */
	public boolean isBusy() {
		return pane.isBusy();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.presentations.IPresentablePart#getToolBar()
	 */
	public Control getToolBar() {
		
		if (!pane.toolbarIsVisible()) {
			return null;
		}
		
		ToolBarManager toolbarManager = pane.getToolBarManager();
		
		if (toolbarManager == null) {
			return null;
		}
		
		ToolBar control = toolbarManager.getControl();
		
		if (control == null || control.isDisposed() ) {
			return null;
		}		
		
		return control;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.presentations.IPresentablePart#getPartMenu()
	 */
	public IPartMenu getMenu() {
		if (pane.hasViewMenu()) {
			return viewMenu;
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.presentations.IPresentablePart#getControl()
	 */
	public Control getControl() {
		return pane.getControl();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.presentations.IPresentablePart#getTitleStatus()
	 */
	public String getTitleStatus() {
		String title = getTitle();
		
		String name = getName();
		
		// Return the empty string if the title is unmodified
		if (title.equals(name)) {
			return new String();
		} 

		if (title.startsWith(name)) {
			String substr = title.substring(name.length(), title.length());
			
			substr = substr.trim();
			
			if (substr.startsWith("(")) { //$NON-NLS-1$
				int end = substr.indexOf(")", 1); //$NON-NLS-1$
				
				if (end != -1) {
					return substr.substring(1, end);
				}
			}			
		}
		
		return title;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4868.java