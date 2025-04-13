error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2272.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2272.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2272.java
text:
```scala
protected v@@oid propertyChanged(int propId) {

/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.part;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistable;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPart2;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.internal.components.framework.Components;
import org.eclipse.ui.internal.components.framework.IDisposable;
import org.eclipse.ui.internal.part.components.interfaces.IFocusable;
import org.eclipse.ui.internal.part.components.services.INameable;
import org.eclipse.ui.internal.part.components.services.IStatusFactory;

/**
 * @since 3.1
 */
public abstract class OldPartToNewWrapper implements IFocusable, IAdaptable, IDisposable, IPersistable, IPartPropertyProvider {
	private INameable nameable;
	private IWorkbenchPart part;
	private IWorkbenchPart2 wb2;
    private IStatusFactory errorContext;
    private Image lastImage = null;
    private IMemento savedState = null;
    private Composite control;
    
	private IPropertyListener propertyListener = new IPropertyListener() {
		public void propertyChanged(Object source, int propId) {
			OldPartToNewWrapper.this.propertyChanged(propId);
		}
	};

    private Map listenerMap = new HashMap();
    
    /**
     * IPropertyListener that changes the property source and forwards events
     * to another listener.
     */
    private static final class ListenerWrapper implements IPropertyListener {
        IWorkbenchPart part;
        IPropertyListener listener;
        
        public ListenerWrapper(IWorkbenchPart part, IPropertyListener listener) {
            this.part = part;
            this.listener = listener;
        }
        
        /* (non-Javadoc)
         * @see org.eclipse.ui.IPropertyListener#propertyChanged(java.lang.Object, int)
         */
        public void propertyChanged(Object source, int propId) {
            listener.propertyChanged(source, propId);
        }
    };
    
    public OldPartToNewWrapper(StandardWorkbenchServices services) {
        this.control = services.getParentComposite();
        this.nameable = services.getNameable();
        this.savedState = services.getState();
        this.errorContext = services.getStatusFactory();
    }

    protected void setPart(IWorkbenchPart newPart) {
        part = newPart;
        
		if (part instanceof IWorkbenchPart2) {
			wb2 = (IWorkbenchPart2)part;
		}
		
		part.addPropertyListener(propertyListener);
        savedState = null;
    }
    
    protected IStatusFactory getErrorContext() {
        return errorContext;
    }
    
    protected IWorkbenchPart getPart() {
        return part;
    }
    
    /**
     * @since 3.1 
     *
     * @param propId
     */
    private void propertyChanged(int propId) {
        switch(propId) {
        	case IWorkbenchPartConstants.PROP_TITLE:
        	    if (part.getTitleImage() != lastImage ) {
        	        lastImage = part.getTitleImage();
        	        nameable.setImage(ImageDescriptor.createFromImage(part.getTitleImage(), Display.getCurrent()));
        	    }
        		nameable.setTooltip(part.getTitleToolTip());
        		if (wb2 == null) {
        			nameable.setContentDescription(part.getTitle());
        		}
        		break;
        	case IWorkbenchPartConstants.PROP_PART_NAME:
        		if (wb2 != null) {
        			nameable.setName(wb2.getPartName());
        		}
        		break;
        	case IWorkbenchPartConstants.PROP_CONTENT_DESCRIPTION:
        		if (wb2 != null) {
        			nameable.setContentDescription(wb2.getContentDescription());
        		}
        		break;
        }
    }
    
	public void dispose() {
	    if (part != null) {
	        part.dispose();
            for (Iterator iter = listenerMap.values().iterator(); iter.hasNext();) {
                IPropertyListener listener = (IPropertyListener) iter.next();
                
                part.removePropertyListener(listener);
            }
            listenerMap.clear();
	    }
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.part.components.interfaces.IFocusable#setFocus()
	 */
	public boolean setFocus() {
	    if (part != null) {
	        part.setFocus();
            return true;
	    }
        
	    return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
        if (adapter.isInstance(this)) {
            return this;
        }
        
		return Components.getAdapter(part, adapter);
	}

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.part.components.interfaces.IPersistable#saveState(org.eclipse.ui.IMemento)
     */
    public void saveState(IMemento memento) {
        if (part != null) {
            if (part instanceof IPersistable) {
                IPersistable persistable = (IPersistable) part;
                persistable.saveState(memento);
            }
        } else if (savedState != null) {
            memento.putMemento(savedState);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.part.IPartPropertyProvider#addPropertyListener(org.eclipse.ui.IWorkbenchPart, org.eclipse.ui.IPropertyListener)
     */
    public void addPropertyListener(IWorkbenchPart part, IPropertyListener l) {
        if (part != null) {
            IPropertyListener wrapper = new ListenerWrapper(part, l); 
            part.addPropertyListener(wrapper);
            
            listenerMap.put(l, wrapper);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.part.IPartPropertyProvider#removePropertyListener(org.eclipse.ui.IWorkbenchPart, org.eclipse.ui.IPropertyListener)
     */
    public void removePropertyListener(IWorkbenchPart part, IPropertyListener l) {
        if (part != null) {
            IPropertyListener wrapper = (IPropertyListener)listenerMap.get(l);
            
            if (wrapper != null) {
                part.removePropertyListener(wrapper);
                listenerMap.remove(l);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.part.IPartPropertyProvider#getTitleToolTip()
     */
    public String getTitleToolTip() {
        return part.getTitleToolTip();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.part.IPartPropertyProvider#getTitleImage()
     */
    public Image getTitleImage() {
        return part.getTitleImage();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.part.IPartPropertyProvider#getTitle()
     */
    public String getTitle() {
        return part.getTitle();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2272.java