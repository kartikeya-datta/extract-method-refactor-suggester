error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8746.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8746.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8746.java
text:
```scala
i@@f (page.isPageZoomed()) {

/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.internal.dnd.AbstractDropTarget;
import org.eclipse.ui.internal.dnd.DragUtil;
import org.eclipse.ui.internal.dnd.IDragOverListener;
import org.eclipse.ui.internal.dnd.IDropTarget;
import org.eclipse.ui.presentations.PresentationUtil;

/**
 * @since 3.3
 *
 */
public class FastViewDnDHandler implements IDragOverListener {
	private String id;
	private ToolBarManager tbm;
	private WorkbenchWindow wbw;
    private ViewDropTarget dropTarget = null;

    private Listener dragListener = new Listener() {
        public void handleEvent(Event event) {
            Point position = DragUtil.getEventLoc(event);
            
            ToolBar toolbar = tbm.getControl();
            Point local = toolbar.toControl(position);
            ToolItem item = toolbar.getItem(local);
            IViewReference ref = (IViewReference) item
            .getData(ShowFastViewContribution.FAST_VIEW);

            if (ref != null) {
                startDraggingFastView(ref, position, false);
            }
        }
    };

    class ViewDropTarget extends AbstractDropTarget {
        List panes;
        ToolItem curItem;

        /**
         * @param panesToDrop the list of ViewPanes to drop at the given position
         */
        public ViewDropTarget(List panesToDrop, ToolItem position) {
            setTarget(panesToDrop, position);
        }
        
        public void setTarget(List panesToDrop, ToolItem position) {
            panes = panesToDrop;
            this.curItem = position;            
        }

        /* (non-Javadoc)
         * @see org.eclipse.ui.internal.dnd.IDropTarget#drop()
         */
        public void drop() {
            Perspective persp = wbw.getActiveWorkbenchPage().getActivePerspective();
            FastViewManager fvm = persp.getFastViewManager();

            int insertIndex = tbm.getControl().indexOf(curItem);
            Iterator iter = panes.iterator();
            while (iter.hasNext()) {
                ViewPane pane = (ViewPane) iter.next();
                IViewReference ref = pane.getViewReference();
        		adoptRef(ref);
                fvm.addViewReference(id, insertIndex++, ref, !iter.hasNext());
            }
        }

        private void adoptRef(IViewReference ref) {
			Perspective persp = wbw.getActiveWorkbenchPage()
					.getActivePerspective();
			PerspectiveHelper helper = persp.getPresentation();
			ContainerPlaceholder ourContainerPlaceholder = (ContainerPlaceholder) helper
					.findPart(id, null);
			LayoutPart refPart = helper.findPart(ref.getId(), ref
					.getSecondaryId());
			ILayoutContainer refContainer = refPart.container;
			if (refContainer != ourContainerPlaceholder) {
				// remove the old part... if it's represented by a
				// placeholder then just remove it...
				if (refPart instanceof PartPlaceholder) {
					if (refContainer instanceof ContainerPlaceholder) {
						// just remove the placeholder
						ViewStack realContainer = (ViewStack) ((ContainerPlaceholder) refContainer)
								.getRealContainer();
						realContainer.remove(refPart);
					}
					else if (refContainer instanceof ViewStack) {
						refContainer.remove(refPart);
					}
				}
				else {
					// If its a real view ref then defref it...
					helper.derefPart(refPart);
				}
				PartPlaceholder newPlaceholder = new PartPlaceholder(ref
						.getId());
				ourContainerPlaceholder.add(newPlaceholder);
			}
		}
        
        /*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.internal.dnd.IDropTarget#getCursor()
		 */
        public Cursor getCursor() {
            return DragCursors.getCursor(DragCursors.FASTVIEW);
        }

        public Rectangle getSnapRectangle() {
            if (curItem == null) {
                // As long as the toolbar is not empty, highlight the place
                // where this view will appear (we
                // may have compressed it to save space when empty, so the actual
                // icon location may not be over the toolbar when it is empty)
                if (tbm.getControl().getItemCount() > 0) {
                    return getLocationOfNextIcon();
                }
                // If the toolbar is empty, highlight the entire toolbar 
                return DragUtil.getDisplayBounds(tbm.getControl());
			}

			return Geometry.toDisplay(tbm.getControl(), curItem.getBounds());
        }
    }
    
	/**
	 * 
	 */
	public FastViewDnDHandler(String id, final ToolBarManager tbm, WorkbenchWindow wbw) {
		this.id = id;
		this.tbm = tbm;
		this.wbw = wbw;
		
		// Hook the 'drop' listener to the control
		DragUtil.addDragTarget(tbm.getControl(), this);
        PresentationUtil.addDragListener(tbm.getControl(), dragListener);
		
		// Clean up on dispose
		tbm.getControl().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				DragUtil.removeDragTarget((Control)(e.widget), FastViewDnDHandler.this);
		        PresentationUtil.removeDragListener(tbm.getControl(), dragListener);
			}
		});
	}

    /**
     * Returns the toolbar item at the given position, in display coordinates
     * @param position
     */
    private ToolItem getToolItem(Point position) {
        ToolBar toolbar = tbm.getControl();
        Point local = toolbar.toControl(position);
        return toolbar.getItem(local);
    }
    
	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.dnd.IDragOverListener#drag(org.eclipse.swt.widgets.Control, java.lang.Object, org.eclipse.swt.graphics.Point, org.eclipse.swt.graphics.Rectangle)
	 */
	public IDropTarget drag(Control currentControl, Object draggedObject,
			Point position, Rectangle dragRectangle) {
		// If we're trying to drop onto a 'standalone' stack, don't...
		if (isStandaloneStack())
			return null;
		
        ToolItem targetItem = getToolItem(position);
        if (draggedObject instanceof ViewPane) {
            ViewPane pane = (ViewPane) draggedObject;

            // Can't drag views between windows
            if (pane.getWorkbenchWindow() != wbw) {
                return null;
            }

            List newList = new ArrayList(1);
            newList.add(draggedObject);

            return createDropTarget(newList, targetItem);
        }
        if (draggedObject instanceof ViewStack) {
            ViewStack folder = (ViewStack) draggedObject;

            if (folder.getWorkbenchWindow() != wbw) {
                return null;
            }

            List viewList = new ArrayList(folder.getItemCount());
            LayoutPart[] children = folder.getChildren();

            for (int idx = 0; idx < children.length; idx++) {
                if (!(children[idx] instanceof PartPlaceholder)) {
                    viewList.add(children[idx]);
                }
            }

            return createDropTarget(viewList, targetItem);
        }

        return null;
	}
    /**
     * Tests the view references associated with the stack and
     * returns <code>true</code> if any view is a stand-alone view
     * 
	 * @return <code>true</code> is any view is stand-alone
	 */
	private boolean isStandaloneStack() {
		Perspective persp = wbw.getActiveWorkbenchPage().getActivePerspective();
		List fvs = persp.getFastViewManager().getFastViews(id);
		for (Iterator iterator = fvs.iterator(); iterator.hasNext();) {
			IViewReference ref = (IViewReference) iterator.next();
			if (persp.isStandaloneView(ref))
				return true;
		}
		
		return false;
	}

	private IDropTarget createDropTarget(List viewList, ToolItem targetItem) {
        if (dropTarget == null) {
            dropTarget = new ViewDropTarget(viewList, targetItem);
        } else {
            dropTarget.setTarget(viewList, targetItem);
        }
        return dropTarget;
    }

    /**
     * Returns the approximate location where the next fastview icon
     * will be drawn (display coordinates)
     */
    public Rectangle getLocationOfNextIcon() {
        ToolBar control = tbm.getControl();

        Rectangle result = control.getBounds();
        Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
        result.height = size.y;
        result.width = size.x;
        
        boolean horizontal = (control.getStyle() & SWT.VERTICAL) == 0;
        if (control.getItemCount() == 0) {
        	Geometry.setDimension(result, horizontal, 0);
        }
        
        int hoverSide = horizontal ? SWT.RIGHT : SWT.BOTTOM;

        result = Geometry.getExtrudedEdge(result, -Geometry.getDimension(
                result, !horizontal), hoverSide);

        return Geometry.toDisplay(control.getParent(), result);
    }

    /**
     * Returns the index of the ToolItem fronting the view ref
     * @param toFind the view reference to find the index of
     * @return the index or -1 if not found
     */
    private int getIndex(IViewReference toFind) {
        ToolItem[] items = tbm.getControl().getItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].getData(ShowFastViewContribution.FAST_VIEW) == toFind) {
                return i;
            }
        }

        return -1;
    }
    
    /**
     * Begins dragging a particular fast view
     * 
     * @param ref
     * @param position
     */
    protected void startDraggingFastView(IViewReference ref, Point position,
            boolean usingKeyboard) {
        int index = getIndex(ref);
        if (index == -1)
        	return;
        
        ToolItem item = tbm.getControl().getItem(index);
        Rectangle dragRect = Geometry.toDisplay(tbm.getControl(), item.getBounds());
        startDrag(((WorkbenchPartReference) ref).getPane(), dragRect, position,
				usingKeyboard);
    }

    private void startDrag(Object toDrag, Rectangle dragRect, Point position,
            boolean usingKeyboard) {
        WorkbenchPage page = wbw.getActiveWorkbenchPage();
        Perspective persp = page.getActivePerspective();

        // Prevent dragging non-movable refs out of a minimized stack  
        if (toDrag instanceof ViewPane) {
        	ViewPane pane = (ViewPane) toDrag;
        	if (!persp.isMoveable(pane.getViewReference()))
        		return;
        }
        
        IViewReference oldFastView = null;
        if (persp != null) {
            oldFastView = persp.getActiveFastView();

            if (page != null) {
                page.hideFastView();
            }
        }

        if (page.isZoomed()) {
            page.zoomOut();
        }

        boolean success = DragUtil.performDrag(toDrag, dragRect, position,
                !usingKeyboard);

        // If the drag was cancelled, reopen the old fast view
        if (!success && oldFastView != null && page != null) {
            page.toggleFastView(oldFastView);
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8746.java