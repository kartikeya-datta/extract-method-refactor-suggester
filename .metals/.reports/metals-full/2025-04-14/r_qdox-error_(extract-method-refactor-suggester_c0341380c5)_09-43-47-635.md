error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5736.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5736.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5736.java
text:
```scala
i@@f (!isCloseable(part)) {

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

import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.ui.internal.dnd.DragUtil;
import org.eclipse.ui.internal.presentations.PartTabFolderPresentation;
import org.eclipse.ui.internal.presentations.StandardSystemContribution;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.IStackPresentationSite;
import org.eclipse.ui.presentations.StackPresentation;

/**
 * Handles the presentation of an active fastview. A fast view pane docks to one side of a
 * parent composite, and is capable of displaying a single view. The view may be resized.
 * Displaying a new view will hide any view currently being displayed in the pane. 
 * 
 * Currently, the fast view pane does not own or contain the view. It only controls the view's 
 * position and visibility.  
 * 
 * @see org.ecliplse.ui.internal.FastViewBar
 */
class FastViewPane {
	private int side = SWT.LEFT;

	private Sash fastViewSash;
	private ViewPane currentPane;
	private Composite clientComposite;
	private static final int SASH_SIZE = 5;
	private static final int MIN_FASTVIEW_SIZE = 10;
	private int size;
	private WidgetHider hider;
	
	// Counts how many times we've scheduled a redraw... use this to avoid resizing
	// the widgetry when we're getting resize requests faster than we can process them.
	// This is needed for GTK, which resizes slowly (bug 54517)
	private int redrawCounter = 0;
	
	private DefaultStackPresentationSite site = new DefaultStackPresentationSite() {
		/* (non-Javadoc)
		 * @see org.eclipse.ui.internal.skins.IPresentationSite#setState(int)
		 */
		public void setState(int newState) {
			super.setState(newState);
			ViewPane pane = currentPane;
			switch(newState) {
				case IStackPresentationSite.STATE_MINIMIZED: 
					currentPane.getPage().toggleFastView(currentPane.getViewReference());
					break;
				case IStackPresentationSite.STATE_MAXIMIZED:
					hider.setEnabled(false);
					fastViewSash.setVisible(false);
					getPresentation().setBounds(getBounds());
					break;
				case IStackPresentationSite.STATE_RESTORED:
					hider.setEnabled(true);
					fastViewSash.setVisible(false);
					getPresentation().setBounds(getBounds());
					break;
				default:
			}
		}
		
		public void close(IPresentablePart part) {
			if (!isClosable(part)) {
				return;
			}
			currentPane.getPage().hideView(currentPane.getViewReference());
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.ui.internal.skins.IPresentationSite#dragStart(org.eclipse.ui.internal.skins.IPresentablePart, boolean)
		 */
		public void dragStart(IPresentablePart beingDragged, Point initialPosition, boolean keyboard) {
			dragStart(initialPosition, keyboard);
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.ui.internal.skins.IPresentationSite#dragStart(boolean)
		 */
		public void dragStart(Point initialPosition, boolean keyboard) {
			ViewPane pane = currentPane;
			
			Control control = getPresentation().getControl(); 
			
			Rectangle bounds = Geometry.toDisplay(clientComposite,
                    control.getBounds());
			
			Perspective persp = currentPane.getPage().getActivePerspective();
			
			persp.setActiveFastView(null, 0);
			
            DragUtil.performDrag(pane, bounds,
                    initialPosition, !keyboard);
		}
		
	};
	
	private StandardSystemContribution systemContribution = new StandardSystemContribution(site) {
		
		protected void addSizeMenuItem (Menu menu) {
			//Add size menu
			MenuItem item = new MenuItem(menu, SWT.NONE);
			item.setText(WorkbenchMessages.getString("PartPane.size")); //$NON-NLS-1$
			item.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					moveSash();
				}
			});		
			item.setEnabled(currentPane != null);
		}
	};
	
	public void moveSash() {
		hider.setEnabled(false);
		fastViewSash.setVisible(true);
		fastViewSash.setFocus();		
	}
	
	private Listener resizeListener = new Listener() {
		public void handleEvent(Event event) {
			if (event.type == SWT.Resize && currentPane != null) {
				setSize(size);
			}
		}
	};
		
	private SelectionAdapter selectionListener = new SelectionAdapter () {
		public void widgetSelected(SelectionEvent e) {
			hider.setEnabled(false);
			
			if (currentPane != null) {
				Rectangle bounds = clientComposite.getClientArea();
				Point location = new Point(e.x, e.y);
				int distanceFromEdge = Geometry.getDistanceFromEdge(bounds, location, side);
				if (distanceFromEdge < MIN_FASTVIEW_SIZE) {
					distanceFromEdge = MIN_FASTVIEW_SIZE;
				}
				
				if (side == SWT.TOP || side == SWT.LEFT) {
					distanceFromEdge += SASH_SIZE;
				}
				
				setSize(distanceFromEdge);
				
				if (e.detail != SWT.DRAG) {
					getPresentation().getControl().moveAbove(null);
					currentPane.moveAbove(null); 
					fastViewSash.moveAbove(null);
					hider.setEnabled(true);
				}
			}
		}
	};

	private void setSize(int size) {		
		this.size = size; 
		
		// Do the rest of this method inside an asyncExec. This allows the method 
		// to return quickly (without resizing). This way, if we recieve a lot of 
		// resize requests in a row, we only need to process the last one.
		// This is needed for GTK, which resizes slowly (bug 54517)
		redrawCounter++;
		getPresentation().getControl().getDisplay().asyncExec(new Runnable() {
			public void run() {
				--redrawCounter;
				StackPresentation presentation = getPresentation();
				if (presentation == null || presentation.getControl().isDisposed()) {
					return;
				}
				if (redrawCounter == 0) {
					getPresentation().setBounds(getBounds());
				}
			}
		});
		
		updateFastViewSashBounds();
	}
	
	/**
	 * Returns the current fastview size ratio. Returns 0.0 if there is no fastview visible.
	 * 
	 * @return
	 */
	public float getCurrentRatio() {
		if (currentPane == null) {
			return 0.0f;
		}
		
		boolean isVertical = !Geometry.isHorizontal(side);
		Rectangle clientArea = clientComposite.getClientArea();

		int clientSize = Geometry.getDimension(clientArea, isVertical);
		
		return (float)size / (float)clientSize;
	}

	private Rectangle getClientArea() {
		return clientComposite.getClientArea();
	}
	
	private Rectangle getBounds() {
		Rectangle bounds = getClientArea();
		
		if (site.getState() == IStackPresentationSite.STATE_MAXIMIZED) {
			return bounds;
		}
		
		boolean horizontal = Geometry.isHorizontal(side);
		
		int available = Geometry.getDimension(bounds, !horizontal);
		
		return Geometry.getExtrudedEdge(bounds, Math.min(FastViewPane.this.size, available), side);		
	}
	
	/**
	 * Displays the given view as a fastview. The view will be docked to the edge of the
	 * given composite until it is subsequently hidden by a call to hideFastView. 
	 * 
	 * @param newClientComposite
	 * @param pane
	 * @param newSide
	 */
	public void showView(Composite newClientComposite, ViewPane pane, int newSide, float sizeRatio) {
		side = newSide;
		
		if (currentPane != null) {
			hideView();
		}
	
		currentPane = pane;
		
		clientComposite = newClientComposite;
	
		clientComposite.addListener(SWT.Resize, resizeListener);
		
		// Create the control first
		Control ctrl = pane.getControl();
		if (ctrl == null) {
			pane.createControl(clientComposite);
			ctrl = pane.getControl();			
		}
		
		// TODO this should go through the factory
//		AbstractPresentationFactory factory = ((WorkbenchWindow) page.getWorkbenchWindow())
//        .getWindowConfigurer().getPresentationFactory();
//		site.setPresentation(factory.createPresentation(newClientComposite,
//        site, AbstractPresentationFactory.ROLE_FAST_VIEW,
//        flags, page.getPerspective().getId(), getID()));

		StackPresentation presentation = new PartTabFolderPresentation(newClientComposite, site, SWT.MIN | SWT.MAX);
		
		site.setPresentation(presentation);
		site.setPresentationState(IStackPresentationSite.STATE_RESTORED);
		systemContribution.setPart(pane.getPresentablePart());
		presentation.addPart(pane.getPresentablePart(), null);
		presentation.selectPart(pane.getPresentablePart());
		presentation.setActive(true);
		presentation.setVisible(true);
		presentation.getSystemMenuManager().add(systemContribution);

		// Show pane fast.
		ctrl.setEnabled(true); // Add focus support.
		Composite parent = ctrl.getParent();

		pane.setVisible(true);
		pane.setFocus();
		
		boolean horizontal = Geometry.isHorizontal(side);
		fastViewSash = new Sash(parent, Geometry.getSwtHorizontalOrVerticalConstant(horizontal));
		fastViewSash.setVisible(false);
		final KeyListener listener = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.ESC || e.character == '\r') {
					currentPane.setFocus();
				}
			}
		};
		fastViewSash.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				hider.setEnabled(false);
				fastViewSash.setBackground(fastViewSash.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
				fastViewSash.addKeyListener(listener);
			}
			public void focusLost(FocusEvent e) {
				fastViewSash.setBackground(null);
				fastViewSash.removeKeyListener(listener);
				hider.setEnabled(true);
			}
		});

		fastViewSash.addSelectionListener(selectionListener);

		Rectangle clientArea = newClientComposite.getClientArea();
		
		getPresentation().getControl().moveAbove(null);
		currentPane.moveAbove(null); 
		fastViewSash.moveAbove(null);

		hider = new WidgetHider(fastViewSash);
		
		setSize((int)(Geometry.getDimension(clientArea, !horizontal) * sizeRatio));

	}
	
	/**
	 * Updates the position of the resize sash.
	 * 
	 * @param bounds
	 */
	private void updateFastViewSashBounds() {
		Rectangle bounds = getBounds();
		
		int oppositeSide = Geometry.getOppositeSide(side);
		Rectangle newBounds = Geometry.getExtrudedEdge(bounds, SASH_SIZE, oppositeSide);
		
		Rectangle oldBounds = fastViewSash.getBounds();
		
		if (!newBounds.equals(oldBounds)) {
			fastViewSash.setBounds(newBounds);
		}
	}
	
	/**
	 * Disposes of any active widgetry being used for the fast view pane. Does not dispose
	 * of the view itself.
	 */
	public void dispose() {
		if (hider != null) {
			hider.dispose();
			hider = null;
		}
		
		// Dispose of the sash too...
		if (fastViewSash != null) {
			fastViewSash.dispose();
			fastViewSash = null;
		}
				
		StackPresentation presentation = getPresentation();
		if (presentation != null) {
			presentation.getSystemMenuManager().remove(systemContribution);
		}
		site.dispose();
	}

	/**
	 * Returns the bounding rectangle for the currently visible fastview, given the rectangle
	 * in which the fastview can dock. 
	 * 
	 * @param clientArea
	 * @param ratio
	 * @param orientation
	 * @return
	 */
	private Rectangle getFastViewBounds() {
		Rectangle clientArea = clientComposite.getClientArea();

		boolean isVertical = !Geometry.isHorizontal(side);
		int clientSize = Geometry.getDimension(clientArea, isVertical);
		int viewSize = Math.min(Geometry.getDimension(getBounds(), isVertical),
				clientSize - MIN_FASTVIEW_SIZE);
		
		return Geometry.getExtrudedEdge(clientArea, viewSize, side);
	}
	
	/**
	 * @return
	 */
	private StackPresentation getPresentation() {
		return site.getPresentation();
	}

	/**
	 * Hides the sash for the fastview if it is currently visible. This method may not be
	 * required anymore, and might be removed from the public interface.
	 */
	public void hideFastViewSash() {
		if (fastViewSash != null) {
			hider.setEnabled(false);
			fastViewSash.setBounds(new Rectangle(0, 0, 0, 0));
		}
	}
	
	/**
	 * Hides the currently visible fastview.
	 */
	public void hideView() {
		
		if (currentPane == null) {
			return;
		}
		
		hider.setEnabled(false);
		
		clientComposite.removeListener(SWT.Resize, resizeListener);
		
		// Get pane.
		// Hide the right side sash first
		hideFastViewSash();
		Control ctrl = currentPane.getControl();
		
		// Hide it completely.
		getPresentation().setVisible(false);
		site.dispose();
		//currentPane.setFastViewSash(null);
		ctrl.setEnabled(false); // Remove focus support.
		
		currentPane = null;
	}
	
	/**
	 * @return Returns the currently visible fastview or null if none
	 */
	public ViewPane getCurrentPane() {
		return currentPane;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5736.java