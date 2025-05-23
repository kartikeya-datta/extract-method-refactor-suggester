error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1890.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1890.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1890.java
text:
```scala
i@@f (bar == null || bar.isDisposed() || dragListener == null)

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

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CBanner;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.internal.dnd.AbstractDropTarget;
import org.eclipse.ui.internal.dnd.DragUtil;
import org.eclipse.ui.internal.dnd.IDragOverListener;
import org.eclipse.ui.internal.dnd.IDropTarget;
import org.eclipse.ui.internal.layout.CacheWrapper;
import org.eclipse.ui.internal.layout.CellLayout;
import org.eclipse.ui.internal.layout.LayoutUtil;
import org.eclipse.ui.internal.layout.Row;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.presentations.PresentationUtil;

/**
 * A utility class to manage the perspective switcher.  At some point, it might be nice to
 * move all this into PerspectiveViewBar.
 * 
 * @since 3.0
 */
public class PerspectiveSwitcher {

    private WorkbenchWindow window;

    private CBanner topBar;

    private int style;

    private Composite parent;

    private Composite trimControl;

    private Label trimSeparator;

    private GridData trimLayoutData;

    private boolean trimVisible = false;

    private int trimOldLength = 0;

    private PerspectiveBarManager perspectiveBar;

    private CoolBar perspectiveCoolBar;

    private CacheWrapper perspectiveCoolBarWrapper;

    private CoolItem coolItem;

    private CacheWrapper toolbarWrapper;

    // The menus are cached, so the radio buttons should not be disposed until
    // the switcher is disposed.
    private Menu popupMenu;

    private Menu genericMenu;

    private static final int INITIAL = -1;

    private static final int TOP_RIGHT = 1;

    private static final int TOP_LEFT = 2;

    private static final int LEFT = 3;

    private static final int DEFAULT_RIGHT_X = 160;

    private int currentLocation = INITIAL;

    private static final int SEPARATOR_LENGTH = 20;

    private IPreferenceStore apiPreferenceStore = PrefUtil
            .getAPIPreferenceStore();

    private IPropertyChangeListener propertyChangeListener;

    private Listener popupListener = new Listener() {
        public void handleEvent(Event event) {
            if (event.type == SWT.MenuDetect) {
                showPerspectiveBarPopup(new Point(event.x, event.y));
            }
        }
    };

	private Listener dragListener;

	private IDragOverListener dragTarget;

	private IDragOverListener externalDragTarget;

	private DisposeListener toolBarListener;

	public PerspectiveSwitcher(WorkbenchWindow window, CBanner topBar, int style) {
		this.window = window;
		this.topBar = topBar;
		this.style = style;
		setPropertyChangeListener();
		// this listener will only be run when the Shell is being disposed
		// and each WorkbenchWindow has its own PerspectiveSwitcher
		toolBarListener = new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				dispose();
			}
		};
	}

	private static int convertLocation(String preference) {
		if (IWorkbenchPreferenceConstants.TOP_RIGHT.equals(preference))
			return TOP_RIGHT;
		if (IWorkbenchPreferenceConstants.TOP_LEFT.equals(preference))
			return TOP_LEFT;
		if (IWorkbenchPreferenceConstants.LEFT.equals(preference))
			return LEFT;

		// TODO log the unknown preference
		return TOP_RIGHT;
	}

	public void createControl(Composite parent) {
		Assert.isTrue(this.parent == null);
		this.parent = parent;
		// set the initial location read from the preference
		setPerspectiveBarLocation(PrefUtil.getAPIPreferenceStore().getString(
				IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR));
	}

	public void addPerspectiveShortcut(IPerspectiveDescriptor perspective,
			WorkbenchPage workbenchPage) {
		if (perspectiveBar == null)
			return;

		PerspectiveBarContributionItem item = new PerspectiveBarContributionItem(
				perspective, workbenchPage);
		perspectiveBar.addItem(item);
		setCoolItemSize(coolItem);
		// This is need to update the vertical size of the tool bar on GTK+ when
		// using large fonts.
		if (perspectiveBar != null)
			perspectiveBar.update(true);
	}

	public IContributionItem findPerspectiveShortcut(
			IPerspectiveDescriptor perspective, WorkbenchPage page) {
		if (perspectiveBar == null)
			return null;

		IContributionItem[] items = perspectiveBar.getItems();
		int length = items.length;
		for (int i = 0; i < length; i++) {
			IContributionItem item = items[i];
			if (item instanceof PerspectiveBarContributionItem
					&& ((PerspectiveBarContributionItem) item).handles(
							perspective, page))
				return item;
		}
		return null;
	}

	public void removePerspectiveShortcut(IPerspectiveDescriptor perspective,
			WorkbenchPage page) {
		if (perspectiveBar == null)
			return;

		IContributionItem item = findPerspectiveShortcut(perspective, page);
		if (item != null) {
			if (item instanceof PerspectiveBarContributionItem)
				perspectiveBar
						.removeItem((PerspectiveBarContributionItem) item);
			item.dispose();
			perspectiveBar.update(false);
			setCoolItemSize(coolItem);
		}
	}

	public void setPerspectiveBarLocation(String preference) {
		// return if the control has not been created. createControl(...) will
		// handle updating the state in that case
		if (parent == null)
			return;
		int newLocation = convertLocation(preference);
		if (newLocation == currentLocation)
			return;
		createControlForLocation(newLocation);
		currentLocation = newLocation;
		showPerspectiveBar();
		if (newLocation == TOP_LEFT || newLocation == TOP_RIGHT) {
			updatePerspectiveBar();
			updateBarParent();
		}
	}

	/**
	 * Make the perspective bar visible in its current location. This method
	 * should not be used unless the control has been successfully created.
	 */
	private void showPerspectiveBar() {
		switch (currentLocation) {
		case TOP_LEFT:
			topBar.setRight(null);
			topBar.setBottom(perspectiveCoolBarWrapper.getControl());
			break;
		case TOP_RIGHT:
			topBar.setBottom(null);
			topBar.setRight(perspectiveCoolBarWrapper.getControl());
			topBar.setRightWidth(DEFAULT_RIGHT_X);
			break;
		case LEFT:
			topBar.setBottom(null);
			topBar.setRight(null);
			LayoutUtil.resize(topBar);
			window.addPerspectiveBarToTrim(trimControl, SWT.LEFT);
			break;
		default:
			return;
		}

		LayoutUtil.resize(perspectiveBar.getControl());
	}

	public void update(boolean force) {
		if (perspectiveBar == null)
			return;

		perspectiveBar.update(force);

		if (currentLocation == LEFT) {
			ToolItem[] items = perspectiveBar.getControl().getItems();
			boolean shouldExpand = items.length > 0;
			if (shouldExpand != trimVisible) {
				perspectiveBar.getControl().setVisible(true);
				trimVisible = shouldExpand;
			}

			if (items.length != trimOldLength) {
				LayoutUtil.resize(trimControl);
				trimOldLength = items.length;
			}
		}
	}

	public void selectPerspectiveShortcut(IPerspectiveDescriptor perspective,
			WorkbenchPage page, boolean selected) {
		IContributionItem item = findPerspectiveShortcut(perspective, page);
		if (item != null && (item instanceof PerspectiveBarContributionItem)) {
			if (selected) {
				// check if not visible and ensure visible
				PerspectiveBarContributionItem contribItem = (PerspectiveBarContributionItem) item;
				perspectiveBar.select(contribItem);
			}
			// select or de-select
			((PerspectiveBarContributionItem) item).setSelection(selected);
		}
	}

	public void updatePerspectiveShortcut(IPerspectiveDescriptor oldDesc,
			IPerspectiveDescriptor newDesc, WorkbenchPage page) {
		IContributionItem item = findPerspectiveShortcut(oldDesc, page);
		if (item != null && (item instanceof PerspectiveBarContributionItem))
			((PerspectiveBarContributionItem) item).update(newDesc);
	}

	public PerspectiveBarManager getPerspectiveBar() {
		return perspectiveBar;
	}

	public void dispose() {
		if (propertyChangeListener != null) {
			apiPreferenceStore
					.removePropertyChangeListener(propertyChangeListener);
			propertyChangeListener = null;
		}
		unhookDragSupport();
		toolBarListener = null;
	}

	private void disposeChildControls() {

		if (trimControl != null) {
			trimControl.dispose();
			trimControl = null;
		}

		if (trimSeparator != null) {
			trimSeparator.dispose();
			trimSeparator = null;
		}

		if (perspectiveCoolBar != null) {
			perspectiveCoolBar.dispose();
			perspectiveCoolBar = null;
		}

		if (toolbarWrapper != null) {
			toolbarWrapper.dispose();
			toolbarWrapper = null;
		}

		if (perspectiveBar != null) {
			perspectiveBar.dispose();
			perspectiveBar = null;
		}

		perspectiveCoolBarWrapper = null;
	}

	/**
	 * Ensures the control has been set for the argument location. If the
	 * control already exists and can be used the argument location, nothing
	 * happens. Updates the location attribute.
	 * 
	 * @param newLocation
	 */
	private void createControlForLocation(int newLocation) {
		// if there is a control, then perhaps it can be reused
		if (perspectiveBar != null && perspectiveBar.getControl() != null
				&& !perspectiveBar.getControl().isDisposed()) {
			if (newLocation == LEFT && currentLocation == LEFT)
				return;
			if ((newLocation == TOP_LEFT || newLocation == TOP_RIGHT)
					&& (currentLocation == TOP_LEFT || currentLocation == TOP_RIGHT))
				return;
		}

		if (perspectiveBar != null) {
			perspectiveBar.getControl().removeDisposeListener(toolBarListener);
			unhookDragSupport();
		}
		// otherwise dispose the current controls and make new ones
		disposeChildControls();
		if (newLocation == LEFT)
			createControlForLeft();
		else
			createControlForTop();
		hookDragSupport();

		perspectiveBar.getControl().addDisposeListener(toolBarListener);
	}

	/**
	 * Remove any drag and drop support and associated listeners hooked for the
	 * perspective switcher.
	 */
	private void unhookDragSupport() {
		ToolBar bar = perspectiveBar.getControl();

		if (bar != null || !bar.isDisposed() || dragListener == null)
			return;
		PresentationUtil.removeDragListener(bar, dragListener);
		DragUtil.removeDragTarget(perspectiveBar.getControl(), dragTarget);
		DragUtil.removeDragTarget(null, externalDragTarget);
		dragListener = null;
		dragTarget = null;
		externalDragTarget = null;
	}

    /**
	 * Attach drag and drop support and associated listeners hooked for 
	 * the perspective switcher.
	 */
	 private void hookDragSupport() {
        dragListener = new Listener() {
        	/* (non-Javadoc)
			 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
			 */
			public void handleEvent(Event event) {
				ToolBar toolbar = perspectiveBar.getControl();
                ToolItem item = toolbar.getItem(new Point(event.x, event.y));
                
                if (item != null) {
                	//ignore the first item, which remains in position Zero
                    if (item.getData() instanceof PerspectiveBarNewContributionItem)
                    	return;
                    
                	Rectangle bounds = item.getBounds();
                	Rectangle parentBounds = toolbar.getBounds();
                	bounds.x += parentBounds.x;
                	bounds.y += parentBounds.y;
                	startDragging((PerspectiveBarContributionItem)item.getData(), toolbar.getDisplay().map(toolbar, null, bounds));
                } else { 
                    //startDragging(toolbar, toolbar.getDisplay().map(toolbar, null, toolbar.getBounds()));
                }
            }
			
			private void startDragging(Object widget, Rectangle bounds) {
				boolean success = DragUtil.performDrag(widget, bounds, new Point(bounds.x, bounds.y), true);
		    }
        };

		dragTarget = new IDragOverListener() {
			protected PerspectiveDropTarget perspectiveDropTarget;

			class PerspectiveDropTarget extends AbstractDropTarget {

				private PerspectiveBarContributionItem perspective;

				private Point location;

				/**
				 * @param location
				 * @param draggedObject
				 * @param dragRectangle
				 */
				public PerspectiveDropTarget(Object draggedObject,
						Point location, Rectangle dragRectangle) {
					update(draggedObject, location, dragRectangle);
				}

				/**
				 * 
				 * @param draggedObject
				 * @param location
				 * @param dragRectangle
				 */
				private void update(Object draggedObject, Point location,
						Rectangle dragRectangle) {
					this.location = location;
					this.perspective = (PerspectiveBarContributionItem) draggedObject;
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.ui.internal.dnd.IDropTarget#drop()
				 */
				public void drop() {
					ToolBar toolBar = perspectiveBar.getControl();
					ToolItem item = toolBar.getItem(toolBar.getDisplay().map(
							null, toolBar, location));
					if (toolBar.getItem(0) == item)
						return;
					ToolItem[] items = toolBar.getItems();
					ToolItem oldItem = null;
					int index = -1;
					for (int i = 0; i < items.length; i++) {
						if (item == items[i])
							index = i;
						if (items[i].getData() == perspective)
							oldItem = items[i];
					}
					if (index != -1 && oldItem != null && (oldItem != item)) {
						perspectiveBar.relocate(
								(PerspectiveBarContributionItem) oldItem
										.getData(), index);
					}
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.ui.internal.dnd.IDropTarget#getCursor()
				 */
				public Cursor getCursor() {
					return DragCursors.getCursor(DragCursors.CENTER);
				}

				boolean sameShell() {
					return perspective.getToolItem().getParent().getShell().equals(perspectiveBar.getControl().getShell());
				}
				
				public Rectangle getSnapRectangle() {
					ToolBar toolBar = perspectiveBar.getControl();
					ToolItem item = toolBar.getItem(toolBar.getDisplay().map(
							null, toolBar, location));
					Rectangle bounds;
					if (item != null && item != toolBar.getItem(0)) {
						bounds = item.getBounds();
					} else {
						// it should not be possible to start a drag with item 0
						return null;
					}
					return toolBar.getDisplay().map(toolBar, null, bounds);
				}
			}

			public IDropTarget drag(Control currentControl,
					Object draggedObject, Point position,
					Rectangle dragRectangle) {
				if (draggedObject instanceof PerspectiveBarContributionItem) {
					if (perspectiveDropTarget == null) {
						perspectiveDropTarget = new PerspectiveDropTarget(
								draggedObject, position, dragRectangle);
					} else {
						perspectiveDropTarget.update(draggedObject, position,
								dragRectangle);
					}
					// do not support drag to perspective bars between shells.
					if (!perspectiveDropTarget.sameShell())
							return null;
					
					return (IDropTarget) perspectiveDropTarget;
				}// else if (draggedObject instanceof IPerspectiveBar) {
				//	return new PerspectiveBarDropTarget();
				//}

				return null;
			}

		};

		externalDragTarget = new IDragOverListener() {
			protected ExternalPerspectiveDropTarget externalPerspectiveDropTarget;

			class ExternalPerspectiveDropTarget extends AbstractDropTarget {

				private PerspectiveBarContributionItem perspective;
				private Rectangle rect; 
				private Point location;

				/**
				 * @param location
				 * @param draggedObject
				 * @param dragRectangle
				 */
				public ExternalPerspectiveDropTarget(Object draggedObject,
						Point location, Rectangle dragRectangle) {
					update(draggedObject, location, dragRectangle);
				}

				/**
				 * 
				 * @param draggedObject
				 * @param location
				 * @param dragRectangle
				 */
				private void update(Object draggedObject, Point location,
						Rectangle dragRectangle) {
					this.location = location;
					this.perspective = (PerspectiveBarContributionItem) draggedObject;
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.ui.internal.dnd.IDropTarget#drop()
				 */
				public void drop() {
					IWorkbenchWindow workbenchWindow = Workbench.getInstance()
							.getActiveWorkbenchWindow();
					IWorkbenchPage page = workbenchWindow.getActivePage();
					
			        // Open the page.
			        try {
			        	workbenchWindow.getWorkbench().openWorkbenchWindow(perspective.getId(),
								page.getInput());
			        } catch (WorkbenchException e) {
			            MessageDialog.openError(workbenchWindow.getShell(), WorkbenchMessages
			                    .getString("OpenNewWindowMenu.dialogTitle"), //$NON-NLS-1$
			                    e.getMessage());
			            return;
			        }
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.ui.internal.dnd.IDropTarget#getCursor()
				 */
				public Cursor getCursor() {
					return DragCursors.getCursor(DragCursors.OFFSCREEN);
				}

				public Rectangle getSnapRectangle() {
					/*
					 * ToolBar toolBar = perspectiveBar.getControl(); ToolItem
					 * item = toolBar.getItem(toolBar.getDisplay().map(null,
					 * toolBar, location)); Rectangle bounds; if (item != null &&
					 * item != toolBar.getItem(0)) { bounds = item.getBounds(); }
					 * else { // it should not be possible to start a drag with
					 * item 0 return null; }
					 */
					if (rect == null) {
						IWorkbenchWindow workbenchWindow = Workbench.getInstance()
						.getActiveWorkbenchWindow();
						rect = workbenchWindow.getShell().getBounds();
					}
					rect.x = location.x;
					rect.y = location.y;					
					return rect;
				}
			}

			public IDropTarget drag(Control currentControl,
					Object draggedObject, Point position,
					Rectangle dragRectangle) {
				if (draggedObject instanceof PerspectiveBarContributionItem) {
					if (externalPerspectiveDropTarget == null) {
						externalPerspectiveDropTarget = new ExternalPerspectiveDropTarget(
								draggedObject, position, dragRectangle);
					} else {
						externalPerspectiveDropTarget.update(draggedObject,
								position, dragRectangle);
					}
					return (IDropTarget) externalPerspectiveDropTarget;
				}// else if (draggedObject instanceof IPerspectiveBar) {
				//	return new PerspectiveBarDropTarget();
				//}

				return null;
			}

		};

		PresentationUtil.addDragListener(perspectiveBar.getControl(),
				dragListener);
		DragUtil.addDragTarget(perspectiveBar.getControl(), dragTarget);
		DragUtil.addDragTarget(null, externalDragTarget);
	}

	private void setPropertyChangeListener() {
        propertyChangeListener = new IPropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if (IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR
                        .equals(propertyChangeEvent.getProperty())) {
                    if (perspectiveBar == null)
                        return;
                    updatePerspectiveBar();
                    updateBarParent();
                }
            }
        };
        apiPreferenceStore.addPropertyChangeListener(propertyChangeListener);
    }

    private void createControlForLeft() {
        trimControl = new Composite(parent, SWT.NONE);

        trimControl.setLayout(new CellLayout(1).setMargins(0, 0).setSpacing(3,
                3).setDefaultRow(Row.fixed()).setDefaultColumn(Row.growing()));

        perspectiveBar = createBarManager(SWT.VERTICAL);

        perspectiveBar.createControl(trimControl);
        perspectiveBar.getControl().addListener(SWT.MenuDetect, popupListener);

        trimSeparator = new Label(trimControl, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridData sepData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
 GridData.HORIZONTAL_ALIGN_CENTER);
        sepData.widthHint = SEPARATOR_LENGTH;
        trimSeparator.setLayoutData(sepData);

        trimLayoutData = new GridData(GridData.FILL_BOTH);
        trimVisible = false;
        perspectiveBar.getControl().setLayoutData(trimLayoutData);
    }

    private void createControlForTop() {
        perspectiveBar = createBarManager(SWT.HORIZONTAL);

        perspectiveCoolBarWrapper = new CacheWrapper(topBar);
        perspectiveCoolBar = new CoolBar(
                perspectiveCoolBarWrapper.getControl(), SWT.FLAT);
        coolItem = new CoolItem(perspectiveCoolBar, SWT.DROP_DOWN);
        toolbarWrapper = new CacheWrapper(perspectiveCoolBar);
        perspectiveBar.createControl(toolbarWrapper.getControl());
        coolItem.setControl(toolbarWrapper.getControl());
        perspectiveCoolBar.setLocked(true);
        perspectiveBar.setParent(perspectiveCoolBar);
        perspectiveBar.update(true);

        // adjust the toolbar size to display as many items as possible
        perspectiveCoolBar.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
                setCoolItemSize(coolItem);
            }
        });

        coolItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (e.detail == SWT.ARROW) {
                    if (perspectiveBar != null) {
                        perspectiveBar.handleChevron(e);
                    }
                }
            }
        });
        coolItem.setMinimumSize(0, 0);
        perspectiveBar.getControl().addListener(SWT.MenuDetect, popupListener);
    }

    /**
     * @param coolItem
     * @param toolbarWrapper
     */
    private void setCoolItemSize(final CoolItem coolItem) {
        // there is no coolItem when the bar is on the left
        if (currentLocation == LEFT)
            return;

        ToolBar toolbar = perspectiveBar.getControl();
        if (toolbar == null)
            return;

        int rowHeight = 0;
        ToolItem[] toolItems = toolbar.getItems();
        for (int i = 0; i < toolItems.length; i++) {
            rowHeight = Math.max(rowHeight, toolItems[i].getBounds().height);
        }

        Rectangle area = perspectiveCoolBar.getClientArea();
        int rows = rowHeight <= 0 ? 1 : (int) Math.max(1, Math
                .floor(area.height / rowHeight));
        if (rows == 1 || (toolbar.getStyle() & SWT.WRAP) == 0
 currentLocation == TOP_LEFT) {
            Point p = toolbar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            coolItem.setSize(coolItem.computeSize(p.x, p.y));
            return;
        }
        Point offset = coolItem.computeSize(0, 0);
        Point wrappedSize = toolbar.computeSize(area.width - offset.x,
                SWT.DEFAULT);
        int h = rows * rowHeight;
        int w = wrappedSize.y <= h ? wrappedSize.x : wrappedSize.x + 1;
        coolItem.setSize(coolItem.computeSize(w, h));
    }

    private void showPerspectiveBarPopup(Point pt) {
        if (perspectiveBar == null)
            return;

        // Get the tool item under the mouse.
        ToolBar toolBar = perspectiveBar.getControl();
        ToolItem toolItem = toolBar.getItem(toolBar.toControl(pt));

        // Get the action for the tool item.
        Object data = null;
        if (toolItem != null)
            data = toolItem.getData();

        if (toolItem == null
 !(data instanceof PerspectiveBarContributionItem)) {
            if (genericMenu == null) {
                Menu menu = new Menu(toolBar);
                addDockOnSubMenu(menu);
                addShowTextItem(menu);
                genericMenu = menu;
            }

            // set the state of the menu items to match the preferences
            genericMenu
                    .getItem(1)
                    .setSelection(
                            PrefUtil
                                    .getAPIPreferenceStore()
                                    .getBoolean(
                                            IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR));
            updateLocationItems(genericMenu.getItem(0).getMenu(),
                    currentLocation);

            // Show popup menu.
            genericMenu.setLocation(pt.x, pt.y);
            genericMenu.setVisible(true);
            return;
        }

        if (data == null || !(data instanceof PerspectiveBarContributionItem))
            return;

        // The perspective bar menu is created lazily here.
        // Its data is set (each time) to the tool item, which refers to the SetPagePerspectiveAction
        // which in turn refers to the page and perspective.
        // It is important not to refer to the action, the page or the perspective directly
        // since otherwise the menu hangs on to them after they are closed.
        // By hanging onto the tool item instead, these references are cleared when the
        // corresponding page or perspective is closed.
        // See bug 11282 for more details on why it is done this way.
        if (popupMenu == null) {
            Menu menu = new Menu(toolBar);
            MenuItem menuItem = new MenuItem(menu, SWT.NONE);
            menuItem.setText(WorkbenchMessages
                    .getString("WorkbenchWindow.close")); //$NON-NLS-1$
            menuItem.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    ToolItem perspectiveToolItem = (ToolItem) popupMenu
                            .getData();
                    if (perspectiveToolItem != null
                            && !perspectiveToolItem.isDisposed()) {
                        PerspectiveBarContributionItem item = (PerspectiveBarContributionItem) perspectiveToolItem
                                .getData();
                        item.getPage().closePerspective(item.getPerspective(),
                                true);
                    }
                }
            });
            menuItem = new MenuItem(menu, SWT.NONE);
            menuItem.setText(WorkbenchMessages
                    .getString("WorkbenchWindow.closeAll")); //$NON-NLS-1$
            menuItem.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    ToolItem perspectiveToolItem = (ToolItem) popupMenu
                            .getData();
                    if (perspectiveToolItem != null
                            && !perspectiveToolItem.isDisposed()) {
                        PerspectiveBarContributionItem item = (PerspectiveBarContributionItem) perspectiveToolItem
                                .getData();
                        item.getPage().closeAllPerspectives();
                    }
                }
            });

            new MenuItem(menu, SWT.SEPARATOR);
            addDockOnSubMenu(menu);
            addShowTextItem(menu);
            popupMenu = menu;
        }
        popupMenu.setData(toolItem);

        // set the state of the menu items to match the preferences
        popupMenu
                .getItem(4)
                .setSelection(
                        PrefUtil
                                .getAPIPreferenceStore()
                                .getBoolean(
                                        IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR));
        updateLocationItems(popupMenu.getItem(3).getMenu(), currentLocation);

        // Show popup menu.
        popupMenu.setLocation(pt.x, pt.y);
        popupMenu.setVisible(true);
    }

    /**
     * @param direction one of <code>SWT.HORIZONTAL</code> or <code>SWT.VERTICAL</code>
     */
    private PerspectiveBarManager createBarManager(int direction) {
        PerspectiveBarManager barManager = new PerspectiveBarManager(style
 direction);
        barManager.add(new PerspectiveBarNewContributionItem(window));

        // add an item for all open perspectives
        WorkbenchPage page = (WorkbenchPage) window.getActivePage();
        if (page != null) {
            // these are returned with the most recently opened one first
            IPerspectiveDescriptor[] perspectives = page
                    .getOpenedPerspectives();
            for (int i = 0; i < perspectives.length; ++i)
                barManager.add(new PerspectiveBarContributionItem(
                        perspectives[i], page));
        }

        return barManager;
    }

    private void updateLocationItems(Menu parent, int newLocation) {
        MenuItem left;
        MenuItem topLeft;
        MenuItem topRight;

        topRight = parent.getItem(0);
        topLeft = parent.getItem(1);
        left = parent.getItem(2);

        if (newLocation == LEFT) {
            left.setSelection(true);
            topRight.setSelection(false);
            topLeft.setSelection(false);
        } else if (newLocation == TOP_LEFT) {
            topLeft.setSelection(true);
            left.setSelection(false);
            topRight.setSelection(false);
        } else {
            topRight.setSelection(true);
            left.setSelection(false);
            topLeft.setSelection(false);
        }
    }

    private void addDockOnSubMenu(Menu menu) {
        MenuItem item = new MenuItem(menu, SWT.CASCADE);
        item.setText(WorkbenchMessages.getString("PerspectiveSwitcher.dockOn")); //$NON-NLS-1$

        final Menu subMenu = new Menu(item);

        final MenuItem menuItemTopRight = new MenuItem(subMenu, SWT.RADIO);
        menuItemTopRight.setText(WorkbenchMessages
                .getString("PerspectiveSwitcher.topRight")); //$NON-NLS-1$

        final MenuItem menuItemTopLeft = new MenuItem(subMenu, SWT.RADIO);
        menuItemTopLeft.setText(WorkbenchMessages
                .getString("PerspectiveSwitcher.topLeft")); //$NON-NLS-1$

        final MenuItem menuItemLeft = new MenuItem(subMenu, SWT.RADIO);
        menuItemLeft.setText(WorkbenchMessages
                .getString("PerspectiveSwitcher.left")); //$NON-NLS-1$

        SelectionListener listener = new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                MenuItem item = (MenuItem) e.widget;
                String pref = null;
                if (item.equals(menuItemLeft)) {
                    updateLocationItems(subMenu, LEFT);
                    pref = IWorkbenchPreferenceConstants.LEFT;
                } else if (item.equals(menuItemTopLeft)) {
                    updateLocationItems(subMenu, TOP_LEFT);
                    pref = IWorkbenchPreferenceConstants.TOP_LEFT;
                } else {
                    updateLocationItems(subMenu, TOP_RIGHT);
                    pref = IWorkbenchPreferenceConstants.TOP_RIGHT;
                }
                IPreferenceStore apiStore = PrefUtil.getAPIPreferenceStore();
                apiStore.setValue(
                        IWorkbenchPreferenceConstants.DOCK_PERSPECTIVE_BAR,
                        pref);
            }
        };

        menuItemTopRight.addSelectionListener(listener);
        menuItemTopLeft.addSelectionListener(listener);
        menuItemLeft.addSelectionListener(listener);
        item.setMenu(subMenu);
    }

    private void addShowTextItem(Menu menu) {
        final MenuItem showtextMenuItem = new MenuItem(menu, SWT.CHECK);
        showtextMenuItem.setText(WorkbenchMessages
                .getString("PerspectiveBar.showText")); //$NON-NLS-1$
        showtextMenuItem.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (perspectiveBar == null)
                    return;

                boolean preference = showtextMenuItem.getSelection();
                PrefUtil
                        .getAPIPreferenceStore()
                        .setValue(
                                IWorkbenchPreferenceConstants.SHOW_TEXT_ON_PERSPECTIVE_BAR,
                                preference);
            }
        });
    }

    /**
     * Method to save the width of the perspective bar in the 
     */
    public void saveState(IMemento persBarMem) {
        // save the width of the perspective bar
        IMemento childMem = persBarMem
                .createChild(IWorkbenchConstants.TAG_ITEM_SIZE);

        int x;
        if (currentLocation == TOP_RIGHT && topBar != null)
            x = topBar.getRightWidth();
        else
            x = DEFAULT_RIGHT_X;

        childMem.putString(IWorkbenchConstants.TAG_X, Integer.toString(x));
    }

    /**
     * Method to restore the width of the perspective bar
     */
    public void restoreState(IMemento memento) {
        if (memento == null)
            return;
        // restore the width of the perspective bar
        IMemento attributes = memento
                .getChild(IWorkbenchConstants.TAG_PERSPECTIVE_BAR);
        IMemento size = null;
        if (attributes != null)
            size = attributes.getChild(IWorkbenchConstants.TAG_ITEM_SIZE);
        if (size != null && currentLocation == TOP_RIGHT && topBar != null) {
            Integer x = size.getInteger(IWorkbenchConstants.TAG_X);
            if (x != null)
                topBar.setRightWidth(x.intValue());
            else
                topBar.setRightWidth(DEFAULT_RIGHT_X);
        }
    }

    /**
     * Method to rebuild and update the toolbar when necessary
     */
    void updatePerspectiveBar() {
        // Update each item as the text may have to be shortened.
        IContributionItem[] items = perspectiveBar.getItems();
        for (int i = 0; i < items.length; i++)
            items[i].update();
        // make sure the selected item is visible
        perspectiveBar.arrangeToolbar();
        setCoolItemSize(coolItem);
        perspectiveBar.getControl().redraw();
    }

    /**
     * Updates the height of the CBanner if the perspective bar
     * is docked on the top right
     */
    public void updateBarParent() {
        if (perspectiveBar == null || perspectiveBar.getControl() == null)
            return;

        // TOP_LEFT and LEFT need only relayout in this case, however TOP_RIGHT
        // will need to set the minimum height of the CBanner as it might have changed.
        if (currentLocation == TOP_RIGHT && topBar != null) {
            // This gets the height of the tallest tool item.
            int maxRowHeight = 0;
            ToolItem[] toolItems = perspectiveBar.getControl().getItems();
            for (int i = 0; i < toolItems.length; i++) {
                maxRowHeight = Math.max(maxRowHeight,
                        toolItems[i].getBounds().height);
            }
            // This sets the CBanner's minimum height to support large fonts
            topBar.setRightMinimumSize(new Point(SWT.DEFAULT, maxRowHeight));
        }

        LayoutUtil.resize(perspectiveBar.getControl());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1890.java