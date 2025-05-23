error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1156.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1156.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1156.java
text:
```scala
i@@f (container != null && container instanceof PartStack) {

package org.eclipse.ui.internal;

/******************************************************************************* 
 * Copyright (c) 2000, 2004 IBM Corporation and others. 
 * All rights reserved. This program and the accompanying materials! 
 * are made available under the terms of the Common Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 * 
 * Contributors: 
 *    IBM Corporation - initial API and implementation 
 *    Cagatay Kavukcuoglu <cagatayk@acm.org>
 *      - Fix for bug 10025 - Resizing views should not use height ratios
**********************************************************************/

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.internal.dnd.DragUtil;
import org.eclipse.ui.internal.presentations.PresentableViewPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.StackPresentation;

/**
 * Provides support for a title bar where the
 * title and icon of the view can be displayed.
 * Along with an X icon to close the view, and
 * a pin icon to attach the view back to the
 * main layout.
 *
 * Also provides support to add tool icons and menu
 * icon on the system bar if required by the view
 * part.
 */
public class ViewPane extends PartPane implements IPropertyListener {
	private PresentableViewPart presentableAdapter = new PresentableViewPart(this);

	private boolean busy = false;

	private boolean fast = false;
	private boolean showFocus = false;
	// toolbars can be locked (i.e. part of the view form and 
	// positioned inside it, or not locked (embedded in a floating
	// toolbar that is not part of the view form
	private boolean locked = true;

	ToolBar isvToolBar;
	// create initially toolbarless bar manager so that actions may be added in the 
	// init method of the view.
	private ToolBarManager isvToolBarMgr = new PaneToolBarManager(SWT.FLAT | SWT.WRAP);
	private MenuManager isvMenuMgr;
	boolean hasFocus;
	
	/**
	 * Indicates whether a toolbar button is shown for the view local menu.
	 */
	private boolean hadViewMenu = false;

	/**
	 * Toolbar manager for the ISV toolbar.
	 */
	class PaneToolBarManager extends ToolBarManager {
		public PaneToolBarManager(int style) {
			super(style);
		}

		protected void relayout(ToolBar toolBar, int oldCount, int newCount) {
			toolBarResized(toolBar, newCount);
			
			toolBar.layout();
		}
	}

	/**
	 * Menu manager for view local menu.
	 */
	class PaneMenuManager extends MenuManager {
		public PaneMenuManager() {
			super("View Local Menu"); //$NON-NLS-1$
		}
		protected void update(boolean force, boolean recursive) {
			super.update(force, recursive);
			
			boolean hasMenu = !isEmpty();
			if (hasMenu != hadViewMenu) {
				hadViewMenu = hasMenu;
				presentableAdapter.firePropertyChange(IPresentablePart.PROP_PANE_MENU);
			}
		}
	}

	/**
	 * Constructs a view pane for a view part.
	 */
	public ViewPane(IViewReference ref, WorkbenchPage page) {
		super(ref, page);
		fast = ref.isFastView();
	}

	/**
	 * Create control. Add the title bar.
	 */
	public void createControl(Composite parent) {
		// Only do this once.
		if (getControl() != null && !getControl().isDisposed())
			return;

		super.createControl(parent);
	}

	
	protected void createChildControl() {
		final IWorkbenchPart part[] = new IWorkbenchPart[] { partReference.getPart(false)};
		if (part[0] == null)
			return;

		if (control == null)
			return;

		super.createChildControl();

		Platform.run(new SafeRunnable() {
			public void run() {
				// Install the part's tools and menu
				ViewActionBuilder builder = new ViewActionBuilder();
				IViewPart part = (IViewPart) getViewReference().getPart(true);
				if (part != null) {
					builder.readActionExtensions(part);
					ActionDescriptor[] actionDescriptors = builder.getExtendedActions();
					KeyBindingService keyBindingService =
						(KeyBindingService) part.getSite().getKeyBindingService();

					if (actionDescriptors != null) {
						for (int i = 0; i < actionDescriptors.length; i++) {
							ActionDescriptor actionDescriptor = actionDescriptors[i];

							if (actionDescriptor != null) {
								IAction action = actionDescriptors[i].getAction();

								if (action != null && action.getActionDefinitionId() != null)
									keyBindingService.registerAction(action);
							}
						}
					}
				}
				updateActionBars();
			}
			public void handleException(Throwable e) {
				//Just have it logged.
			}
		});
	}

	private void recreateToolbars() {
		// create new toolbars based on the locked vs !locked state
		createToolBars();
		// create new toolbars
		updateActionBars();
		
	}	
	
	protected IWorkbenchPart createErrorPart(IWorkbenchPart oldPart) {
		class ErrorViewPart extends ViewPart {
			private Text text;
			public void createPartControl(Composite parent) {
				text = new Text(parent, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
				text.setForeground(JFaceColors.getErrorText(text.getDisplay()));
				text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				text.setText(WorkbenchMessages.getString("ViewPane.errorMessage")); //$NON-NLS-1$
			}
			public void setFocus() {
				if (text != null)
					text.setFocus();
			}
			public void setSite(IWorkbenchPartSite site) {
				super.setSite(site);
			}
			public void setTitle(String title) {
				super.setTitle(title);
			}
		}
		ErrorViewPart newPart = new ErrorViewPart();
		PartSite site = (PartSite) oldPart.getSite();
		newPart.setSite(site);
		newPart.setTitle(site.getRegisteredName());
		site.setPart(newPart);
		return newPart;
	}
	
	/**
	 * Create a title bar for the pane.
	 * 	- the view icon and title to the far left
	 *	- the view toolbar appears in the middle.
	 * 	- the view pulldown menu, pin button, and close button to the far right.
	 */
	protected void createTitleBar() {
		// Only do this once.

		updateTitles();

		// Listen to title changes.
		getPartReference().addPropertyListener(this);

		createToolBars();
	
	}
	
	private void toolBarResized(ToolBar toolBar, int newSize) {
		if (isvToolBar != null) {
			Control ctrl = getControl();
			
			boolean visible = ctrl != null && ctrl.isVisible() && toolbarIsVisible();
			
			isvToolBar.setVisible(visible);
		}
		
		presentableAdapter.firePropertyChange(IPresentablePart.PROP_TOOLBAR);
	}
	
	/**
	 * 
	 */
	private void createToolBars() {
		Composite parentControl = control;
		// ISV toolbar.
		//			// 1GD0ISU: ITPUI:ALL - Dbl click on view tool cause zoom
		isvToolBar = isvToolBarMgr.createControl(parentControl.getParent());
		
		if (locked) {
			//((ViewForm2)control).setTopCenter(isvToolBar);	
			isvToolBar.addMouseListener(new MouseAdapter(){
				public void mouseDoubleClick(MouseEvent event) {
					if (isvToolBar.getItem(new Point(event.x, event.y)) == null)
						doZoom();
				}
			});
		} else {
			//isvToolBar.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
 	}
	
    public void dispose() {
		super.dispose();

		/* Bug 42684.  The ViewPane instance has been disposed, but an attempt is
		 * then made to remove focus from it.  This happens because the ViewPane is
		 * still viewed as the active part.  In general, when disposed, the control
		 * containing the titleLabel will also disappear (disposing of the 
		 * titleLabel).  As a result, the reference to titleLabel should be dropped. 
		 */
		if (isvMenuMgr != null)
			isvMenuMgr.dispose();
		if (isvToolBarMgr != null)
			isvToolBarMgr.dispose();
	}
	/**
	 * @see PartPane#doHide
	 */
	public void doHide() {
		getPage().hideView(getViewReference());
	}

	private Rectangle getParentBounds() {
		Control ctrl = getControl();
		
		if (getContainer() != null && getContainer() instanceof LayoutPart) {
			LayoutPart part = (LayoutPart)getContainer();
			
			if (part.getControl() != null) {
				ctrl = part.getControl();
			}
		}
		
		return DragUtil.getDisplayBounds(ctrl);
	}
	
	/**
	 * Make this view pane a fast view
	 */
	public void doMakeFast() {
	    FastViewBar fastViewBar = ((WorkbenchWindow)getPage().getWorkbenchWindow()).getFastViewBar();
	    if (fastViewBar == null) {
	        return;
	    }
		Shell shell = getControl().getShell();
		
		RectangleAnimation animation = new RectangleAnimation(shell,  
				getParentBounds(), 
				fastViewBar.getLocationOfNextIcon(), 
				250);
		
		animation.schedule();
		
		getPage().addFastView(getViewReference());
	}

	public void doRemoveFast() {

		Shell shell = getControl().getShell();
		
		Rectangle initialBounds = getParentBounds();
				
		getPage().removeFastView(getViewReference());

		Rectangle finalBounds = getParentBounds();
		
		RectangleAnimation animation = new RectangleAnimation(shell,  
				initialBounds, 
				finalBounds, 
				250);
		
		animation.schedule();
	}
	
	/**
	 * Pin the view.
	 */
	protected void doDock() {
		getPage().removeFastView(getViewReference());
	}

	/**
	 * Returns the drag control.
	 */
	public Control getDragHandle() {
		return control;
	}
	/**
	 * @see ViewActionBars
	 */
	public MenuManager getMenuManager() {
		if (isvMenuMgr == null)
			isvMenuMgr = new PaneMenuManager();
		return isvMenuMgr;
	}

	/**
	 * Returns the tab list to use when this part is active.
	 * Includes the view and its tab (if applicable), in the appropriate order.
	 */
	public Control[] getTabList() {
		Control c = getControl();
		if (getContainer() instanceof ViewStack) {
			ViewStack tf = (ViewStack) getContainer();
			return tf.getTabList(this);
		}
		return new Control[] { c };
	}

	/**
	 * @see ViewActionBars
	 */
	public ToolBarManager getToolBarManager() {
		return isvToolBarMgr;
	}
	/**
	 * Answer the view part child.
	 */
	public IViewReference getViewReference() {
		return (IViewReference) getPartReference();
	}
	/**
	 * Indicates that a property has changed.
	 *
	 * @param source the object whose property has changed
	 * @param propId the id of the property which has changed; property ids
	 *   are generally defined as constants on the source class
	 */
	public void propertyChanged(Object source, int propId) {
		if (propId == IWorkbenchPart.PROP_TITLE)
			updateTitles();
	}
	/**
	 * Sets the fast view state.  If this view is a fast view then
	 * various controls like pin and minimize are added to the
	 * system bar.
	 */
	public void setFast(boolean b) {
		fast = b;
	}

	/* (non-Javadoc)
	 * Method declared on PartPane.
	 */
	/* package */
	void shellActivated() {
		//	drawGradient();
	}

	/* (non-Javadoc)
	 * Method declared on PartPane.
	 */
	/* package */
	void shellDeactivated() {
		//hideToolBarShell();
		//	drawGradient();
	}
	
	/**
	 * Set the active border.
	 * @param active
	 */
	void setActive(boolean active){
		hasFocus = active;
		
		if(getContainer() instanceof PartStack){
			((PartStack) getContainer()).setActive(active ? StackPresentation.AS_ACTIVE_FOCUS : StackPresentation.AS_INACTIVE);
		}
	}
	
	/**
	 * Indicate focus in part.
	 */
	public void showFocus(boolean inFocus) {
		setActive(inFocus);
	}

	/**
	 * Return true if this view is a fast view.
	 */
	private boolean isFastView() {
		return page.isFastView(getViewReference());
	}
	
	/**
	 * Return true if the view may be moved.
	 */
	boolean isMoveable() {
		return !page.isFixedLayout();
	}

	/**
	 * Return if there should be a view menu at all.
	 * There is no view menu if there is no menu manager,
	 * no pull down button or if the receiver is an
	 * inactive fast view.
	 */
	public boolean hasViewMenu() {
		
		if (isvMenuMgr != null) {
			return !isvMenuMgr.isEmpty();
		}
		
		return false;		
	}
	
	public void showViewMenu(Point location) {
		if (!hasViewMenu())
			return;
	
		// If this is a fast view, it may have been minimized. Do nothing in this case.
		if (isFastView() && (page.getActiveFastView() != getViewReference()))
			return;
	
		Menu aMenu = isvMenuMgr.createContextMenu(getControl().getParent());
		aMenu.setLocation(location.x, location.y);
		aMenu.setVisible(true);
	}
	
	public String toString() {

		return getClass().getName() + "@" + Integer.toHexString(hashCode()); //$NON-NLS-1$
	}
	/**
	 * @see ViewActionBars
	 */
	public void updateActionBars() {
		if (isvMenuMgr != null)
			isvMenuMgr.updateAll(false);
		if (isvToolBarMgr != null)
			isvToolBarMgr.update(false);

	}
	/**
	 * Update the title attributes.
	 */
	public void updateTitles() {
		presentableAdapter.firePropertyChange(IPresentablePart.PROP_TITLE);
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.PartPane#addSizeMenuItem(org.eclipse.swt.widgets.Menu)
	 */
	public void addSizeMenuItem(Menu menu, int index) {
		if(isMoveable())
			super.addSizeMenuItem(menu, index);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.PartPane#doZoom()
	 */
	protected void doZoom() {
		if (isMoveable())
			super.doZoom();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.LayoutPart#setContainer(org.eclipse.ui.internal.ILayoutContainer)
	 */
	public void setContainer(ILayoutContainer container) {
		ILayoutContainer oldContainer = getContainer();
		if (hasFocus) {
			if (oldContainer != null && oldContainer instanceof PartStack) {
				((PartStack)oldContainer).setActive(StackPresentation.AS_INACTIVE);
			}
			
			if (container != null && container instanceof ViewStack) {
				((PartStack)container).setActive(StackPresentation.AS_ACTIVE_FOCUS);
			}
		}

		super.setContainer(container);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.LayoutPart#getPresentablePart()
	 */
	public IPresentablePart getPresentablePart() {
		return presentableAdapter;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.LayoutPart#reparent(org.eclipse.swt.widgets.Composite)
	 */
	public void reparent(Composite newParent) {
		super.reparent(newParent);
		
		if (isvToolBar != null) {
			isvToolBar.setParent(newParent);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.LayoutPart#moveAbove(org.eclipse.swt.widgets.Control)
	 */
	public void moveAbove(Control refControl) {
		super.moveAbove(refControl);
		
		isvToolBar.moveAbove(control);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.internal.LayoutPart#setVisible(boolean)
	 */
	public void setVisible(boolean makeVisible) {
		super.setVisible(makeVisible);
		
		if (isvToolBar != null) {
			isvToolBar.setVisible(makeVisible && toolbarIsVisible());
		}
	}

	public boolean toolbarIsVisible() {
		ToolBarManager toolbarManager = getToolBarManager();
		
		if (toolbarManager == null) {
			return false;
		}
		
		ToolBar control = toolbarManager.getControl();
		
		if (control == null || control.isDisposed() ) {
			return false;
		}
		
		return control.getItemCount() > 0;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ui.internal.PartPane#setBusy(boolean)
	 */
	public void setBusy(boolean isBusy) {
		if (isBusy != busy) {			
			busy = isBusy;
			presentableAdapter.firePropertyChange(IPresentablePart.PROP_BUSY);
		}
	}

	/**
	 * Return the busy state of the receiver.
	 * @return boolean
	 */
	public boolean isBusy() {
		return busy;
	}
	
	
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.PartPane#showHighlight()
     */
    public void showHighlight() {
        presentableAdapter.firePropertyChange(IPresentablePart.PROP_HIGHLIGHT_IF_BACK);
    }
	
    
    /* (non-Javadoc)
     * @see org.eclipse.ui.internal.LayoutPart#getPlaceHolderId()
     */
    public String getPlaceHolderId() {
        return ViewFactory.getKey(getViewReference());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1156.java