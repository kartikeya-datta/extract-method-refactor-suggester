error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3144.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3144.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3144.java
text:
```scala
.@@addPostSelectionChangedListener(new ISelectionChangedListener() {

/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.statushandlers;

import com.ibm.icu.text.DateFormat;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogTray;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ErrorSupportProvider;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.progress.ProgressManager;
import org.eclipse.ui.internal.progress.ProgressManagerUtil;
import org.eclipse.ui.internal.progress.ProgressMessages;
import org.eclipse.ui.internal.statushandlers.DefaultDetailsArea;
import org.eclipse.ui.internal.statushandlers.IStatusDialogConstants;
import org.eclipse.ui.internal.statushandlers.StackTraceSupportArea;
import org.eclipse.ui.progress.IProgressConstants;
import org.eclipse.ui.statushandlers.StatusManager.INotificationTypes;
import org.eclipse.ui.views.IViewDescriptor;

/**
 * <p>
 * The {@link WorkbenchStatusDialogManager} is a utility class for displaying
 * one or more messages (errors, warnings or infos) to the user. The dialog
 * supplied has a Details button that opens/closes the details area. The default
 * {@link AbstractStatusAreaProvider} displays a tree of {@link StatusAdapter}s
 * related to the selected item on the messages list. The dialog also hasa
 * Support button that opens/closes the support area which contains the provided
 * {@link AbstractStatusAreaProvider}. The Support button is disabled and not
 * visible unless
 * {@link WorkbenchStatusDialogManager#enableDefaultSupportArea(boolean)} is
 * invoked.
 * </p>
 * 
 * <p>
 * The default details area can be replaced using
 * {@link WorkbenchStatusDialogManager#setDetailsAreaProvider(AbstractStatusAreaProvider)}
 * </p>
 * 
 * <p>
 * The default support area can be replaced using
 * {@link WorkbenchStatusDialogManager#setSupportAreaProvider(AbstractStatusAreaProvider)}
 * or {@link Policy#setErrorSupportProvider(ErrorSupportProvider)}.
 * </p>
 * 
 * <p>
 * The manager can switch from a non-modal dialog to a modal dialog. See
 * {@link #addStatusAdapter(StatusAdapter, boolean)}
 * </p>
 * 
 * @see Policy#setErrorSupportProvider(ErrorSupportProvider)
 * @see ErrorSupportProvider
 * @see AbstractStatusAreaProvider
 * 
 * @since 3.4
 * @noextend This class is not intended to be subclassed by clients.
 */
public class WorkbenchStatusDialogManager {
	
	private static final String LOG_VIEW_ID = "org.eclipse.pde.runtime.LogView"; //$NON-NLS-1$
	static final QualifiedName HINT = new QualifiedName(
			IStatusAdapterConstants.PROPERTY_PREFIX, "hint"); //$NON-NLS-1$
	
	/**
	 * The default status label provider.
	 */
	private class DefaultLabelProvider implements ITableLabelProvider {
		ResourceManager manager = new LocalResourceManager(JFaceResources
				.getResources());

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener)
		 */
		public void addListener(ILabelProviderListener listener) {
			// Do nothing
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
		 */
		public void dispose() {
			manager.dispose();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java
		 * .lang.Object, int)
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			Image result = null;
			if (element != null) {
				StatusAdapter statusAdapter = ((StatusAdapter) element);
				Job job = (Job) (statusAdapter.getAdapter(Job.class));
				if (job != null) {
					result = getIcon(job);
				}
			}
			// if somehow disposed image was received (should not happen)
			if (result != null && result.isDisposed()) {
				result = null;
			}
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.
		 * lang.Object, int)
		 */
		public String getColumnText(Object element, int columnIndex) {
			StatusAdapter statusAdapter = (StatusAdapter) element;
			String text = WorkbenchMessages.WorkbenchStatusDialog_ProblemOccurred;
			if (getStatusAdapters().size() == 1) {
				Job job = (Job) (statusAdapter.getAdapter(Job.class));
				if (job != null) {
					text = getPrimaryMessage(statusAdapter);
				} else {
					text = getSecondaryMessage(statusAdapter);
				}
			} else {
				Job job = (Job) (statusAdapter.getAdapter(Job.class));
				if (job != null) {
					text = job.getName();
				} else {
					text = getPrimaryMessage(statusAdapter);
				}
			}
			Long timestamp = (Long) statusAdapter
					.getProperty(IStatusAdapterConstants.TIMESTAMP_PROPERTY);

			if (timestamp != null && getStatusAdapters().size() > 1) {
				String date = DateFormat.getDateTimeInstance(DateFormat.LONG,
						DateFormat.LONG)
						.format(new Date(timestamp.longValue()));
				return NLS.bind(ProgressMessages.JobInfo_Error, (new Object[] {
						text, date }));
			}
			return text;
		}

		/*
		 * Get the icon for the job.
		 */
		private Image getIcon(Job job) {
			if (job != null) {
				Object property = job
						.getProperty(IProgressConstants.ICON_PROPERTY);

				// Create an image from the job's icon property or family
				if (property instanceof ImageDescriptor) {
					return manager.createImage((ImageDescriptor) property);
				} else if (property instanceof URL) {
					return manager.createImage(ImageDescriptor
							.createFromURL((URL) property));
				} else {
					// Let the progress manager handle the resource management
					return ProgressManager.getInstance().getIconFor(job);
				}
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java
		 * .lang.Object, java.lang.String)
		 */
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(ILabelProviderListener listener) {
			// Do nothing
		}
	}

	/**
	 * A wrapper for the status label provider that prevents the label provider
	 * from being disposed when modality is switched.
	 */
	private class LabelProviderWrapper implements ITableLabelProvider {

		private ITableLabelProvider labelProvider = new DefaultLabelProvider();

		public void setLabelProvider(ITableLabelProvider provider) {
			this.labelProvider = provider;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java
		 * .lang.Object, int)
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			return labelProvider.getColumnImage(element, columnIndex);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.
		 * lang.Object, int)
		 */
		public String getColumnText(Object element, int columnIndex) {
			return labelProvider.getColumnText(element, columnIndex);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener)
		 */
		public void addListener(ILabelProviderListener listener) {
			labelProvider.addListener(listener);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
		 */
		public void dispose() {
			if (!modalitySwitch) {
				labelProvider.dispose();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java
		 * .lang.Object, java.lang.String)
		 */
		public boolean isLabelProperty(Object element, String property) {
			return labelProvider.isLabelProperty(element, property);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(ILabelProviderListener listener) {
			labelProvider.removeListener(listener);
		}

	}

	/**
	 * This class is responsible for managing details area.
	 * 
	 * @since 3.4
	 */
	private final class DetailsAreaManager {
		private AbstractStatusAreaProvider provider = null;
		private Control control = null;

		/**
		 * Closes the details area
		 */
		public void close() {
			if (control != null && !control.isDisposed()) {
				control.dispose();
			}
		}

		/**
		 * This method is responsible for creating details area on the specified
		 * Composite and displaying specified StatusAdapter
		 * 
		 * @param parent
		 *            A composite on which should be the details area created.
		 * @param statusAdapter
		 *            StatusAdapter for which should be the details area
		 *            created.
		 */
		public void createDetailsArea(Composite parent,
				StatusAdapter statusAdapter) {
			Composite container = new Composite(parent, SWT.NONE);
			container.setLayout(GridLayoutFactory.fillDefaults().create());
			container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			getProvider().createSupportArea(container, statusAdapter);
			control = container;
		}

		/**
		 * Returns current detail area provider.
		 * 
		 * @return current detail area provider.
		 */
		private AbstractStatusAreaProvider getProvider() {
			if (provider == null) {
				provider = new DefaultDetailsArea(
						WorkbenchStatusDialogManager.this);
			}
			return provider;
		}

		/**
		 * This method allows to check if the details area is open (physically
		 * constructed).
		 * 
		 * @return true if the area is open, false otherwise
		 */
		public boolean isOpen() {
			if (control == null || control.isDisposed()) {
				return false;
			}
			return true;
		}

		/**
		 * This method sets the details area provider. If null is set, the
		 * default area provider (status tree) will be used.
		 * 
		 * @param provider
		 *            A provider that will create contents for details area.
		 */
		public void setDetailsAreaProvider(AbstractStatusAreaProvider provider) {
			this.provider = provider;
		}
	}

	/**
	 * Parent window actually does not use its Shell to build dialog on. The
	 * window passes the shell to the InternalDialog, and it can do switching
	 * modality and recreate the window silently.
	 * 
	 * @since 3.4
	 */
	private class InternalDialog extends TrayDialog {

		private WorkbenchStatusDialogManager statusDialog;

		/**
		 * Instantiates the internal dialog on the given shell. Created dialog
		 * uses statusDialog methods to create its contents.
		 * 
		 * @param parentShell -
		 *            a parent shell for the dialog
		 * @param statusDialog -
		 *            a dialog from which methods should be used to create
		 *            contents
		 * @param modal -
		 *            true if created dialog should be modal, false otherwise.
		 */
		public InternalDialog(Shell parentShell,
				WorkbenchStatusDialogManager statusDialog, boolean modal) {
			super(parentShell);

			this.statusDialog = statusDialog;
			setShellStyle(SWT.RESIZE | SWT.MAX | SWT.MIN | getShellStyle());
			setBlockOnOpen(false);

			if (!modal) {
				setShellStyle(~SWT.APPLICATION_MODAL & getShellStyle());
			}
		}

		protected void buttonPressed(int id) {
			if (id == GOTO_ACTION_ID) {
				IAction gotoAction = getGotoAction();
				if (gotoAction != null) {
					if (isPromptToClose()) {
						okPressed(); // close the dialog
						gotoAction.run(); // run the goto action
					}
				}
			}
			if (id == IDialogConstants.DETAILS_ID) {
				// was the details button pressed?
				detailsOpened = toggleDetailsArea();
			} else {
				super.buttonPressed(id);
			}
		}

		/*
		 * (non-Javadoc) Method declared in Window.
		 */
		final protected void configureShell(Shell shell) {
			super.configureShell(shell);
			if (title != null) {
				shell.setText(title);
			}
		}

		public int convertHeightInCharsToPixels(int chars) {
			return super.convertHeightInCharsToPixels(chars);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.dialogs.Dialog#convertHorizontalDLUsToPixels(int)
		 */
		public int convertHorizontalDLUsToPixels(int dlus) {
			return super.convertHorizontalDLUsToPixels(dlus);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.dialogs.Dialog#convertVerticalDLUsToPixels(int)
		 */
		public int convertVerticalDLUsToPixels(int dlus) {
			return super.convertVerticalDLUsToPixels(dlus);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.dialogs.Dialog#convertWidthInCharsToPixels(int)
		 */
		public int convertWidthInCharsToPixels(int chars) {
			return super.convertWidthInCharsToPixels(chars);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.dialogs.Dialog#createButton(org.eclipse.swt.widgets.Composite,
		 *      int, java.lang.String, boolean)
		 */
		public Button createButton(Composite parent, int id, String label,
				boolean defaultButton) {
			return super.createButton(parent, id, label, defaultButton);
		}
		
		/**
		 * Status dialog button should be aligned SWT.END. 
		 */
		protected void setButtonLayoutData(Button button) {
			GridData data = new GridData(SWT.END, SWT.CENTER, false, false);
			int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
			Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
			data.widthHint = Math.max(widthHint, minSize.x);
			button.setLayoutData(data);
		}

		protected Control createButtonBar(Composite parent) {
			return statusDialog.createButtonBar(parent);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
		 */
		protected Control createDialogArea(Composite parent) {
			return statusDialog.createDialogArea(parent);
		}

		public Button getButton(int id) {
			return super.getButton(id);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.dialogs.Dialog#initializeBounds()
		 */
		protected void initializeBounds() {
			super.initializeBounds();
			this.statusDialog.initializeBounds();
		}

		/**
		 * This function checks if the dialog is modal.
		 * 
		 * @return true if the dialog is modal, false otherwise
		 * 
		 */
		public boolean isModal() {
			return ((getShellStyle() & SWT.APPLICATION_MODAL) == SWT.APPLICATION_MODAL);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
		 */
		protected boolean isResizable() {
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.window.Window#open()
		 */
		public int open() {
			int result = super.open();
			if (modalitySwitch) {
				if (detailsOpened) {
					showDetailsArea();
				}
				if (trayOpened) {
					openTray(supportTray);
				}
			}
			return result;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.dialogs.TrayDialog#closeTray()
		 */
		public void closeTray() throws IllegalStateException {
			super.closeTray();
			//preserve state during modality switch
			if(!modalitySwitch){
				trayOpened = false;
			}
			if (launchTrayLink != null && !launchTrayLink.isDisposed()) {
				launchTrayLink.setEnabled(supportTray.providesSupport() && !trayOpened);
			}
		}

		public Point getInitialLocation(Point initialSize) {
			return super.getInitialLocation(initialSize);
		}
		
	}

	/**
	 * This class is responsible for disposing dialog elements when the dialog
	 * is closed.
	 * 
	 * @since 3.4
	 * 
	 */
	private final class StatusDialogDisposeListener implements DisposeListener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
		 */
		public void widgetDisposed(org.eclipse.swt.events.DisposeEvent e) {
			cleanUp();
		}
	}

	/**
	 * This class is responsible for displaying the support area on the right
	 * side of the status dialog.
	 * 
	 * @since 3.4
	 * 
	 */
	private class SupportTray extends DialogTray implements
			ISelectionChangedListener {

		private IContributionItem closeAction;
		private Image normal;
		private Image hover;

		private Composite supportArea;
		private Composite supportAreaContent;

		private StatusAdapter lastSelectedStatus;
		private boolean defaultSupportAreaEnabled;

		/**
		 * Creates any actions needed by the tray.
		 */
		private void createActions() {
			createImages();
			closeAction = new ContributionItem() {
				public void fill(ToolBar parent, int index) {
					final ToolItem item = new ToolItem(parent, SWT.PUSH);
					item.setImage(normal);
					item.setHotImage(hover);
					item.setToolTipText(JFaceResources.getString("close")); //$NON-NLS-1$
					item.addListener(SWT.Selection, new Listener() {
						public void handleEvent(Event event) {
							trayOpened = false;

							// close the tray
							closeTray();

							// set focus back to shell
							getShell().setFocus();
						}
					});
				}
			};

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.dialogs.DialogTray#createContents(org.eclipse.swt.widgets.Composite)
		 */
		protected Control createContents(Composite parent) {
			Composite container = new Composite(parent, SWT.NONE);

			// nothing to display. Should never happen, cause button is disabled
			// when nothing to display.

			if (!providesSupport()) {
				return container;
			}

			GridLayout layout = new GridLayout();
			layout.marginWidth = layout.marginHeight = 0;
			layout.verticalSpacing = 0;
			container.setLayout(layout);
			GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
			container.setLayoutData(layoutData);

			container.addListener(SWT.Dispose, new Listener() {
				public void handleEvent(Event event) {
					destroyImages();
				}
			});

			if(!hideSupportButton){
				ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
				toolBarManager.createControl(container);
				GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
				gd.grabExcessHorizontalSpace = true;
				toolBarManager.getControl().setLayoutData(gd);
				Label separator = new Label(container, SWT.SEPARATOR
 SWT.HORIZONTAL);
				gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
				gd.heightHint = 1;
				separator.setLayoutData(gd);

				createActions();
				toolBarManager.add(closeAction);

				toolBarManager.update(true);
			}

			supportArea = new Composite(container, SWT.NONE);
			layout = new GridLayout();
			layout.marginWidth = layout.marginHeight = 0;
			layout.verticalSpacing = 0;
			supportArea.setLayout(layout);
			GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL
 GridData.VERTICAL_ALIGN_FILL);
			gd.horizontalSpan = 1;
			gd.grabExcessHorizontalSpace = true;
			gd.grabExcessVerticalSpace = true;
			supportArea.setLayoutData(gd);
			
			// if only one status adapter is displayed,
			// selection listener does not work, so it is necessary to
			// set the last selected status manually
			if (getStatusAdapters().size() == 1) {
				lastSelectedStatus = statusAdapter;
			}

			if (lastSelectedStatus != null)
				createSupportArea(supportArea, lastSelectedStatus);

			Point shellSize = getShell().getSize();
			Point desiredSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
			
			if(desiredSize.y > shellSize.y){
				getShell().setSize(shellSize.x, Math.min(desiredSize.y,500));
			}

			return container;
		}

		/**
		 * Creates any custom needed by the tray, such as the close button.
		 */
		private void createImages() {
			Display display = Display.getCurrent();
			int[] shape = new int[] { 3, 3, 5, 3, 7, 5, 8, 5, 10, 3, 12, 3, 12,
					5, 10, 7, 10, 8, 12, 10, 12, 12, 10, 12, 8, 10, 7, 10, 5,
					12, 3, 12, 3, 10, 5, 8, 5, 7, 3, 5 };

			/*
			 * Use magenta as transparency color since it is used infrequently.
			 */
			Color border = display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);
			Color background = display
					.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
			Color backgroundHot = new Color(display, new RGB(252, 160, 160));
			Color transparent = display.getSystemColor(SWT.COLOR_MAGENTA);

			PaletteData palette = new PaletteData(new RGB[] {
					transparent.getRGB(), border.getRGB(), background.getRGB(),
					backgroundHot.getRGB() });
			ImageData data = new ImageData(16, 16, 8, palette);
			data.transparentPixel = 0;

			normal = new Image(display, data);
			normal.setBackground(transparent);
			GC gc = new GC(normal);
			gc.setBackground(background);
			gc.fillPolygon(shape);
			gc.setForeground(border);
			gc.drawPolygon(shape);
			gc.dispose();

			hover = new Image(display, data);
			hover.setBackground(transparent);
			gc = new GC(hover);
			gc.setBackground(backgroundHot);
			gc.fillPolygon(shape);
			gc.setForeground(border);
			gc.drawPolygon(shape);
			gc.dispose();

			backgroundHot.dispose();
		}
		
		private void destroyImages() {
			if (normal != null) normal.dispose();
			if (hover != null) hover.dispose();
		}

		/**
		 * Create the area for extra error support information.
		 * 
		 * @param parent
		 *            A composite on which should be the support area created.
		 * @param statusAdapter
		 *            StatusAdapter for which should be the support area
		 *            created.
		 */
		private void createSupportArea(Composite parent,
				StatusAdapter statusAdapter) {

			ErrorSupportProvider provider = Policy.getErrorSupportProvider();

			if (userSupportProvider != null) {
				provider = userSupportProvider;
			}

			if (defaultSupportAreaEnabled && provider == null) {
				provider = new StackTraceSupportArea();
			}

			// default support area was disabled
			if (provider == null)
				return;

			if (supportAreaContent != null)
				supportAreaContent.dispose();

			supportAreaContent = new Composite(parent, SWT.FILL);

			GridData supportData = new GridData(SWT.FILL, SWT.FILL, true, true);
			supportAreaContent.setLayoutData(supportData);
			if (supportAreaContent.getLayout() == null) {
				GridLayout layout = new GridLayout();
				layout.marginWidth = 0;
				layout.marginHeight = 0;
				supportAreaContent.setLayout(layout); // Give it a default
				// layout
			}

			if (provider instanceof AbstractStatusAreaProvider) {
				((AbstractStatusAreaProvider) provider).createSupportArea(
						supportAreaContent, statusAdapter);
			} else {
				provider.createSupportArea(supportAreaContent, statusAdapter
						.getStatus());
			}
		}

		/**
		 * This method manages the enablement of the default support area.
		 * 
		 * @param enable
		 *            true enables, false disables.
		 */
		public void enableDefaultSupportArea(boolean enable) {
			this.defaultSupportAreaEnabled = enable;
			Policy.setErrorSupportProvider(new StackTraceSupportArea());
		}

		private StatusAdapter getStatusAdapterFromEvent(
				SelectionChangedEvent event) {

			ISelection selection = event.getSelection();

			if (selection instanceof StructuredSelection) {
				StructuredSelection structuredSelection = (StructuredSelection) selection;
				Object element = structuredSelection.getFirstElement();
				if (element instanceof StatusAdapter) {
					return (StatusAdapter) element;
				}
			}
			return null;
		}

		/**
		 * Checks if the support dialog has any support areas.
		 * 
		 * @return true if support dialog has any support areas to display,
		 *         false otherwise
		 * 
		 */
		private boolean providesSupport() {
			if (Policy.getErrorSupportProvider() != null) {
				return true;
			}
			if (userSupportProvider != null) {
				return true;
			}
			return defaultSupportAreaEnabled;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
		 */
		public void selectionChanged(SelectionChangedEvent event) {
			lastSelectedStatus = getStatusAdapterFromEvent(event);
			if (supportArea != null && !supportArea.isDisposed()) {
				if (lastSelectedStatus != null) {
					createSupportArea(supportArea, lastSelectedStatus);
					supportArea.layout(true);
				}
			}
		}
	}

	/**
	 * Preference used to indicate whether the user should be prompted to
	 * confirm the execution of the job's goto action
	 */
	private static final String PREF_SKIP_GOTO_ACTION_PROMPT = "pref_skip_goto_action_prompt"; //$NON-NLS-1$

	/**
	 * The id of the goto action button
	 */
	private static final int GOTO_ACTION_ID = IDialogConstants.CLIENT_ID + 1;

	/**
	 * Returns whether the given StatusAdapter object should be displayed.
	 * 
	 * @param statusAdapter
	 *            a status object
	 * @param mask
	 *            a mask as per <code>IStatus.matches</code>
	 * @return <code>true</code> if the given status should be displayed, and
	 *         <code>false</code> otherwise
	 * @see org.eclipse.core.runtime.IStatus#matches(int)
	 */
	private boolean shouldAccept(StatusAdapter statusAdapter, int mask) {
		IStatus status = statusAdapter.getStatus();
		IStatus[] children = status.getChildren();
		if (children == null || children.length == 0) {
			return status.matches(mask) || (handleOKStatuses && status.isOK());
		}
		for (int i = 0; i < children.length; i++) {
			if (children[i].matches(mask)) {
				return true;
			}
		}
		if (handleOKStatuses && status.isOK()) {
			return true;
		}
		return false;
	}

	/**
	 * Stores statuses.
	 */
	private Collection errors = Collections.synchronizedSet(new HashSet());

	/**
	 * Stores information, which statuses should be displayed in modal window.
	 */
	private HashMap modals = new HashMap();

	/**
	 * This field stores the real dialog that appears to the user.
	 */
	private InternalDialog dialog;

	/**
	 * This composite holds all components of the dialog.
	 */
	private Composite dialogArea;

	/**
	 * This composite is initially scrolled to the 0 x 0 size. When more than
	 * one status arrives, listArea is resized and a list is created on it to
	 * present statuses to the user.
	 */
	private Composite listArea;

	/**
	 * On this composite are presented additional elements for displaying single
	 * status. Currently it is the second label that displays the second most
	 * important message to the user.
	 */
	private Composite singleStatusDisplayArea;

	/**
	 * This label is used to display the second most important message to the
	 * user. It is placed on singleStatusDisplayArea.
	 */
	private Label singleStatusLabel;

	/**
	 * A list from which the user selects statuses. The list is placed on
	 * listArea.
	 */
	private TableViewer statusListViewer;

	/**
	 * A list label provider
	 */
	private LabelProviderWrapper labelProviderWrapper = new LabelProviderWrapper();

	/**
	 * This variable holds current details area provider.
	 */
	private DetailsAreaManager detailsManager = new DetailsAreaManager();

	private DisposeListener disposeListener = new StatusDialogDisposeListener();

	/**
	 * The title of the dialog.
	 */
	private String title;

	/**
	 * Filter mask for determining which status items to display. Default allows
	 * for displaying all statuses.
	 */
	private int displayMask = 0xFFFF;

	/**
	 * Currently selected status adapter.
	 */
	private StatusAdapter statusAdapter;

	/**
	 * In this support tray status support providers are displayed.
	 */
	private SupportTray supportTray = new WorkbenchStatusDialogManager.SupportTray();

	/**
	 * Composite on the left bottom corner. Allows for opening support tray &
	 * Error Log.
	 */
	private Composite linkComposite;
	
	/**
	 * 
	 */
	private boolean showSupport = false;
	
	/**
	 * This item is used to launch support tray
	 */
	private Link launchTrayLink;
	
	/**
	 * This fields contains indicator if link to ErrorLog view should be
	 * present.
	 */
	private Link showErrorLogLink;

	/**
	 * This flag indicates if the dialog is switching modality. For now it is
	 * possible only to change from non-modal to modal.
	 */
	private boolean modalitySwitch = false;

	/**
	 * This fields holds the information about dialog position and size when
	 * switching the modality.
	 */
	private Rectangle shellBounds;

	/**
	 * This flag indicates if the details area was opened before switching the
	 * modality or not.
	 */
	private boolean detailsOpened = false;

	/**
	 * This flag indicates if the support area was opened before switching the
	 * modality or not.
	 */
	private boolean trayOpened = false;

	/**
	 * This field contains the support provider set by the user.
	 */
	private AbstractStatusAreaProvider userSupportProvider;

	/**
	 * Main dialog image holder.
	 */
	private Label titleImageLabel;

	/**
	 * Message in the header.
	 */
	private Label mainMessageLabel;

	/**
	 * Header area.
	 */
	private Composite titleArea;
	
	/**
	 * The decorator that is used to modify a message extracted from the
	 * StatusAdapter.
	 */
	private ILabelDecorator messageDecorator;

	/**
	 * This fields indicates if OK statuses should be handled or ignored.
	 */
	private boolean handleOKStatuses = false;

	private boolean supportForErrorLog = false;

	private boolean hideSupportButton = false;

	/**
	 * Creates workbench status dialog.
	 * 
	 * @param displayMask
	 *            the mask used to filter the handled <code>StatusAdapter</code>
	 *            objects, the mask is a logical sum of status severities
	 * @param dialogTitle
	 *            the title of the dialog. If null, than default will be used.
	 */
	
	public WorkbenchStatusDialogManager(int displayMask, String dialogTitle) {
		
		Assert.isNotNull(Display.getCurrent(),
						"WorkbenchStatusDialogManager must be instantiated in UI thread"); //$NON-NLS-1$

		this.displayMask = displayMask;
		this.title = dialogTitle == null ? JFaceResources
				.getString("Problem_Occurred") : //$NON-NLS-1$
				dialogTitle;
	}
	
	/**
	 * Creates workbench status dialog.
	 * 
	 * @param parentShell
	 *            the parent shell for the dialog. It may be null.
	 * @param displayMask
	 *            the mask used to filter the handled <code>StatusAdapter</code>
	 *            objects, the mask is a logical sum of status severities
	 * @param dialogTitle
	 *            the title of the dialog. If null, than default will be used.
	 * @deprecated As of 3.4 the <code>parentShell<code> is ignored
	 * @see #WorkbenchStatusDialogManager(int, String)
	 */
	public WorkbenchStatusDialogManager(Shell parentShell, int displayMask,
			String dialogTitle) {

		this(displayMask, dialogTitle);
	}

	/**
	 * Creates workbench status dialog.
	 * 
	 * @param dialogTitle
	 *            the title of the dialog. If null, than default will be used.
	 */
	public WorkbenchStatusDialogManager(String dialogTitle) {
		this(IStatus.INFO | IStatus.WARNING | IStatus.ERROR | IStatus.CANCEL,
				dialogTitle);
	}

	/**
	 * Creates workbench status dialog.
	 * 
	 * @param parentShell
	 *            the parent shell for the dialog. It may be null.
	 * @param dialogTitle
	 *            the title of the dialog. If null, than default will be used.
	 * @deprecated As of 3.4 the <code>parentShell<code> is ignored
	 * @see #WorkbenchStatusDialogManager(String)
	 */
	public WorkbenchStatusDialogManager(Shell parentShell, String dialogTitle) {
		this(dialogTitle);
	}

	/**
	 * <p>
	 * Adds a new {@link StatusAdapter} to the status adapters list in the
	 * dialog.
	 * </p>
	 * <p>
	 * If the dialog is already visible, the status adapter will be shown
	 * immediately. Otherwise, the dialog with the added status adapter will
	 * show up, if all conditions below are false.
	 * <ul>
	 * <li>the status adapter has
	 * {@link IProgressConstants#NO_IMMEDIATE_ERROR_PROMPT_PROPERTY} set to true</li>
	 * </ul>
	 * </p>
	 * <p>
	 * All not shown status adapters will be displayed as soon as the dialog
	 * shows up.
	 * </p>
	 * 
	 * @param modal
	 *            <code>true</code> if the dialog should be modal,
	 *            <code>false</code> otherwise
	 * @param statusAdapter
	 *            the status adapter
	 */
	public void addStatusAdapter(final StatusAdapter statusAdapter,
			final boolean modal) {
		try {
			doAddStatusAdapter(statusAdapter, modal);
		} catch (Exception e) {
			// an exception occurred, so reset modality switch
			modalitySwitch = false;
			// if dialog is open, dispose it (and all child controls)
			if (!isDialogClosed()) {
				dialog.getShell().dispose();
			}
			// reset the state
			cleanUp();
			// log original problem
			// TODO: check if is it possible to discover duplicates
			WorkbenchPlugin.log(statusAdapter.getStatus());
			// log the problem with status handling
			WorkbenchPlugin.log(e);
		}
	}

	private boolean isDialogClosed() {
		return dialog == null || dialog.getShell() == null
 dialog.getShell().isDisposed();
	}

	private void cleanUp() {
		dialog = null;
		statusListViewer = null;
		statusAdapter = null;
		errors.clear();
		modals.clear();
	}

	private void doAddStatusAdapter(final StatusAdapter statusAdapter,
			final boolean modal) {

		if (ErrorDialog.AUTOMATED_MODE == true) {
			return;
		}
		
		Assert.isNotNull(Display.getCurrent(),
						"WorkbenchStatusDialogManager#addStatusAdapter must be called from UI thread"); //$NON-NLS-1$

		if (!PlatformUI.isWorkbenchRunning()) {
			// we are shutting down, so just log
			WorkbenchPlugin.log(statusAdapter.getStatus());
			return;
		}
		
		// if statusAdapter does not match the mask, ignore it
		if (!shouldAccept(statusAdapter, displayMask)) {
			return;
		}

		// Add the error in the UI thread to ensure thread safety in the
		// dialog
		if (isDialogClosed()) {

			errors.add(statusAdapter);
			modals.put(statusAdapter, new Boolean(modal));
			// Delay prompting if the status adapter property is set
			if (shouldPrompt(statusAdapter)) {
				// notify all interested parties that status adapters will be
				// handled
				StatusManager.getManager().fireNotification(
						INotificationTypes.HANDLED,
						(StatusAdapter[]) errors
								.toArray(new StatusAdapter[] {}));
				
				if (dialog == null) {
					dialog = new InternalDialog(getParentShell(),
							WorkbenchStatusDialogManager.this, shouldBeModal());
					setSelectedStatusAdapter(statusAdapter);
					dialog.create();
					dialog.getShell().addDisposeListener(disposeListener);
					if (supportTray.providesSupport() && showSupport) {
						openTray(supportTray);
						dialog.getShell().setLocation(
								dialog.getInitialLocation(dialog.getShell()
										.getSize()));
					}
					dialog.open();
				}
				refresh();
				refreshDialogSize();
			}

		} else {
			StatusManager.getManager().fireNotification(
					INotificationTypes.HANDLED,
					new StatusAdapter[] { statusAdapter });
			if (statusAdapter
					.getProperty(IProgressConstants.NO_IMMEDIATE_ERROR_PROMPT_PROPERTY) != null) {
				statusAdapter.setProperty(
						IProgressConstants.NO_IMMEDIATE_ERROR_PROMPT_PROPERTY,
						Boolean.FALSE);
			}
			openStatusDialog(modal, statusAdapter);
		}
	}

	/**
	 * This method closes the dialog.
	 */
	private boolean close() {
		if (detailsOpened) {
			toggleDetailsArea();
		}
		if (trayOpened) {
			closeTray();
		}
		shellBounds = getShell().getBounds();
		statusListViewer = null;
		boolean result = this.dialog.close();
		if (!modalitySwitch) {
			ProgressManagerUtil.animateDown(shellBounds);
		}
		return result;
	}

	/**
	 * Closes the dialog tray (it is support area at the right side of the
	 * dialog)
	 * 
	 * @throws IllegalStateException
	 */
	private void closeTray() throws IllegalStateException {
		this.dialog.closeTray();
	}

	/**
	 * This method creates button bar that is available on the bottom of the
	 * dialog.
	 */
	private Control createButtonBar(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = dialog
				.convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginHeight = dialog
				.convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		linkComposite = createLinkComposite(composite);

		// Add the buttons to the button bar.
		createButtonsForButtonBar(composite);

		composite.layout();
		return composite;
	}

	/**
	 * This method creates buttons that are placed on button bar.
	 */
	private void createButtonsForButtonBar(Composite parent) {
		IAction gotoAction = getGotoAction();
		String text = null;
		if (gotoAction != null) {
			text = gotoAction.getText();
		}
		Button button = dialog.createButton(parent, GOTO_ACTION_ID,
				text == null ? "" : text, //$NON-NLS-1$
				false);
		if (text == null)
			hideButton(button, true);

		dialog.createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL, true);

		dialog.createButton(parent, IDialogConstants.DETAILS_ID,
				IDialogConstants.SHOW_DETAILS_LABEL, false);
	}

	/**
	 * This method creates dialog area.
	 */
	private Control createDialogArea(Composite parent) {
		createTitleArea(parent);
		createListArea(parent);
		dialogArea = parent;
		Dialog.applyDialogFont(dialogArea);
		return parent;
	}

	/**
	 * Create an area which allows the user to view the status if only one is
	 * created or to select one of reported statuses when there are many.
	 * 
	 * @param parent
	 *            the parent composite on which all components should be placed.
	 */
	private void createListArea(Composite parent) {
		listArea = new Composite(parent, SWT.NONE);
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		layoutData.heightHint = 0;
		layoutData.widthHint = 0;
		listArea.setLayoutData(layoutData);
		GridLayout layout = new GridLayout();
		listArea.setLayout(layout);
		if(getStatusAdapters().size() > 1){
			fillListArea(listArea);
		}
	}

	/**
	 * This method creates additional display area for {@link StatusAdapter}
	 * when only one is available.
	 * 
	 * It creates one label on a composite currently for secondary message.
	 * 
	 * @param parent
	 *            A parent composite on which all components should be placed.
	 * @return composite the composite on which are all components for
	 *         displaying status when only one is available.
	 */
	private Composite createSingleStatusDisplayArea(Composite parent) {
		// secondary message is displayed on separate composite with no margins
		Composite singleStatusParent = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		singleStatusParent.setLayout(gridLayout);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		singleStatusParent.setLayoutData(gd);

		// label that wraps
		singleStatusLabel = new Label(singleStatusParent, SWT.WRAP);
		GridData labelLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
		labelLayoutData.widthHint = dialog.convertWidthInCharsToPixels(50);
		singleStatusLabel.setLayoutData(labelLayoutData);
		// main message set up early, to address bug 222391
		singleStatusLabel.setText(labelProviderWrapper.getColumnText(
				statusAdapter, 0));

		singleStatusLabel.addMouseListener(new MouseListener() {	
			public void mouseDoubleClick(MouseEvent e) {
			}

			public void mouseDown(MouseEvent e) {
				showDetailsArea();
			}

			public void mouseUp(MouseEvent e) {
			}
		});
		return singleStatusParent;
	}

	/**
	 * Creates a new control that provides access to support providers.
	 * <p>
	 * The <code>WorkbenchStatusDialog</code> implementation of this method
	 * creates the control, registers it for selection events including
	 * selection, Note that the parent's layout is assumed to be a
	 * <code>GridLayout</code> and the number of columns in this layout is
	 * incremented. Subclasses may override.
	 * </p>
	 * 
	 * @param parent
	 *            A parent composite on which all components should be placed.
	 * @return the report control
	 */
	private Composite createLinkComposite(Composite parent) {
		Composite linkArea = new Composite(parent, SWT.NONE) {

			// the composite should be as small as possible when there is no
			// additional controls on it
			public Point computeSize(int wHint, int hHint, boolean changed) {
				Point newSize = super.computeSize(wHint, hHint, changed); 
				if (getChildren().length == 0) {
					newSize.x = 0;
					newSize.y = 0;
				}
				return newSize;
			}

		};
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		linkArea.setLayout(layout);

		((GridLayout) parent.getLayout()).numColumns++;

		GridData layoutData = new GridData(SWT.BEGINNING, SWT.CENTER, true,
				false);
		linkArea.setLayoutData(layoutData);
		return linkArea;
	}

	/**
	 * Creates a button with a report image. This is only used if there is an
	 * image available.
	 */
	private Link createGetSupportLink() {
		// no support
		if (!supportTray.providesSupport() || hideSupportButton) {
			return null;
		}

		Link link = new Link(linkComposite, SWT.NONE);
		link.setText(WorkbenchMessages.WorkbenchStatusDialog_SupportHyperlink);
		link.setToolTipText(WorkbenchMessages.WorkbenchStatusDialog_SupportTooltip);
		link.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openTray(supportTray);
			}
		});
		Dialog.applyDialogFont(link);
		return link;
	}

	private Link createShowErrorLogLink() {
		Link link = new Link(linkComposite, SWT.NONE);
		link.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				try {
					Workbench.getInstance().getActiveWorkbenchWindow()
							.getActivePage().showView(LOG_VIEW_ID);
				} catch (CoreException ce) {
					StatusManager.getManager().handle(ce,
							WorkbenchPlugin.PI_WORKBENCH);
				}
			}
		});
		link.setText(WorkbenchMessages.ErrorLogUtil_ShowErrorLogHyperlink);
		link.setToolTipText(WorkbenchMessages.ErrorLogUtil_ShowErrorLogTooltip);
		Dialog.applyDialogFont(link);
		return link;
	}

	/**
	 * Creates title area.
	 * 
	 * @param parent
	 *            A composite on which the title area should be created.
	 */
	private void createTitleArea(Composite parent) {
		titleArea = new Composite(parent, SWT.NONE);
		titleArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 10;
		layout.marginLeft = 10;
		layout.marginTop = 10;
		layout.marginBottom = 0;
		titleArea.setLayout(layout);

		titleImageLabel = new Label(titleArea, SWT.NONE);
		titleImageLabel.setImage(getImage());
		GridData layoutData = new GridData();
		layoutData.verticalSpan = 2;
		layoutData.verticalAlignment = SWT.TOP;
		titleImageLabel.setLayoutData(layoutData);

		GridData messageData = new GridData(SWT.FILL, SWT.FILL, true, true);
		messageData.widthHint = dialog.convertWidthInCharsToPixels(50);
		mainMessageLabel = new Label(titleArea, SWT.WRAP);
		mainMessageLabel.setLayoutData(messageData);
		// main message set up early, to address bug 222391
		mainMessageLabel.setText(getMainMessage(statusAdapter));
		if (getStatusAdapters().size() == 1) {
			singleStatusDisplayArea = createSingleStatusDisplayArea(titleArea);
		}
	}

	/**
	 * Enables the default support area that shows stack trace of the exception
	 * contained in the selected status.
	 * 
	 * @param enable
	 *            true enables, false disables default support
	 */
	public void enableDefaultSupportArea(boolean enable) {
		supportTray.enableDefaultSupportArea(enable);
	}

	/**
	 * This method creates display area for {@link StatusAdapter}s when more is
	 * available.
	 * 
	 * @param parent
	 *            A parent composite on which all components should be placed.
	 */
	private void fillListArea(Composite parent) {
		// it is necessary to make list parent composite taller
		GridData listAreaGD = (GridData) parent.getLayoutData();
		listAreaGD.grabExcessHorizontalSpace = true;
		if (!detailsManager.isOpen()) {
			listAreaGD.grabExcessVerticalSpace = true;
		}
		listAreaGD.heightHint = SWT.DEFAULT;

		// create list viewer
		statusListViewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL
 SWT.V_SCROLL | SWT.BORDER);
		statusListViewer.setComparator(getViewerComparator());
		Control control = statusListViewer.getControl();
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = dialog.convertHeightInCharsToPixels(5);
		statusListViewer.addSelectionChangedListener(supportTray);
		control.setLayoutData(data);
		initContentProvider();
		initLabelProvider();
		statusListViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						handleSelectionChange();
					}
				});
		Dialog.applyDialogFont(parent);
	}

	/**
	 * Returns {@link IAction} associated with selected StatusAdapter.
	 * 
	 * @return {@link IAction} that is set as {@link StatusAdapter} property
	 *         with Job.class key.
	 */
	private IAction getGotoAction() {
		Object property = null;

		Job job = (Job) (statusAdapter.getAdapter(Job.class));
		if (job != null) {
			property = job.getProperty(IProgressConstants.ACTION_PROPERTY);
		}

		if (property instanceof IAction) {
			return (IAction) property;
		}
		return null;
	}

	/**
	 * Gets {@link Image} associated with current {@link StatusAdapter}
	 * severity.
	 * 
	 * @return {@link Image} associated with current {@link StatusAdapter}
	 *         severity.
	 */
	private Image getImage() {
		if (statusAdapter != null) {
			int severity = statusAdapter.getStatus().getSeverity();
			switch (severity) {
			case IStatus.OK:
			case IStatus.INFO:
			case IStatus.CANCEL:
				return getSWTImage(SWT.ICON_INFORMATION);
			case IStatus.WARNING:
				return getSWTImage(SWT.ICON_WARNING);
			default: /* IStatus.ERROR */
				return getSWTImage(SWT.ICON_ERROR);
			}
		}
		// should not never happen but if we do not know what is going on, then
		// return error image.
		return getSWTImage(SWT.ICON_ERROR);
	}

	/**
	 * Get an <code>Image</code> from the provide SWT image constant.
	 * 
	 * @param imageID
	 *            the SWT image constant
	 * @return image the image
	 */
	private Image getSWTImage(final int imageID) {
		return getShell().getDisplay().getSystemImage(imageID);
	}

	/**
	 * This method computes the dialog main message.
	 * 
	 * If there is only one reported status adapter, main message should be:
	 * <ul>
	 * <li>information about job that reported an error.</li>
	 * <li>primary message, if the statusAdapter was not reported by job</li>
	 * </ul>
	 * 
	 * If there is more reported statusAdapters, main message should be:
	 * <ul>
	 * <li>primary message for job reported statusAdapters</li>
	 * <li>secondary message for statuses not reported by jobs</li>
	 * </ul>
	 * 
	 * If nothing can be found, some general information should be displayed.
	 * 
	 * @param statusAdapter
	 *            A status adapter which is used as the base for computation.
	 * @return main message of the dialog.
	 * 
	 * @see WorkbenchStatusDialogManager#getPrimaryMessage(StatusAdapter)
	 * @see WorkbenchStatusDialogManager#getSecondaryMessage(StatusAdapter)
	 * @see WorkbenchStatusDialogManager#setStatusListLabelProvider(ITableLabelProvider)
	 */
	private String getMainMessage(StatusAdapter statusAdapter) {
		if (errors.size() == 1) {
			Job job = (Job) (statusAdapter.getAdapter(Job.class));
			// job
			if (job != null) {
				return NLS
						.bind(
								WorkbenchMessages.WorkbenchStatusDialog_ProblemOccurredInJob,
								job.getName());
			}
			// we are not handling job
			return getPrimaryMessage(statusAdapter);
		}
		// we have a list. primary message or job name or on the list name (both
		// with timestamp if available).
		// we display secondary message or status
		if (errors.size() > 1) {
			Job job = (Job) (statusAdapter.getAdapter(Job.class));
			// job
			if (job != null) {
				return getPrimaryMessage(statusAdapter);
			}

			// plain status
			return getSecondaryMessage(statusAdapter);
		}
		return WorkbenchMessages.WorkbenchStatusDialog_ProblemOccurred;
	}

	/**
	 * Return the parent shell.
	 * 
	 * @return the parent shell of the dialog.
	 */
	private Shell getParentShell() {
		return ProgressManagerUtil.getDefaultParent();
	}

	/**
	 * Retrieves primary message from passed statusAdapter. Primary message
	 * should be (from the most important):
	 * <ul>
	 * <li>statusAdapter title</li>
	 * <li>IStatus message</li>
	 * <li>pointing to child statuses if IStatus has them.</li>
	 * <li>exception message</li>
	 * <li>exception class</li>
	 * <li>general message informing about error (no details at all)</li>
	 * </ul>
	 * 
	 * @param statusAdapter
	 *            an status adapter to retrieve primary message from
	 * @return String containing primary message
	 * 
	 * @see WorkbenchStatusDialogManager#getMainMessage(StatusAdapter)
	 * @see WorkbenchStatusDialogManager#getSecondaryMessage(StatusAdapter)
	 */
	private String getPrimaryMessage(StatusAdapter statusAdapter) {
		// if there was nonempty title set, display the title
		Object property = statusAdapter
				.getProperty(IStatusAdapterConstants.TITLE_PROPERTY);
		if (property instanceof String) {
			String header = (String) property;
			if (header.trim().length() > 0) {
				return decorate(header, statusAdapter);
			}
		}
		// if there was message set in the status
		IStatus status = statusAdapter.getStatus();
		if (status.getMessage() != null
				&& status.getMessage().trim().length() > 0) {
			return decorate(status.getMessage(), statusAdapter);
		}

		// if status has children
		if (status.getChildren().length > 0) {
			return WorkbenchMessages.WorkbenchStatusDialog_StatusWithChildren;
		}

		// check the exception
		Throwable t = status.getException();
		if (t != null) {
			if (t.getMessage() != null && t.getMessage().trim().length() > 0) {
				return decorate(t.getMessage(), statusAdapter);
			}
			return t.getClass().getName();
		}
		return WorkbenchMessages.WorkbenchStatusDialog_ProblemOccurred;
	}

	/**
	 * Retrieves secondary message from the passed statusAdapter. Secondary
	 * message is one level lower than primary. Secondary message should be
	 * (from the most important):
	 * <ul>
	 * <li>IStatus message</li>
	 * <li>pointing to child statuses if IStatus has them.</li>
	 * <li>exception message</li>
	 * <li>exception class</li>
	 * </ul>
	 * Secondary message should not be the same as primary one. If no secondary
	 * message can be extracted, details should be pointed.
	 * 
	 * @param statusAdapter
	 *            an status adapter to retrieve secondary message from
	 * @return String containing secondary message
	 * 
	 * @see WorkbenchStatusDialogManager#getMainMessage(StatusAdapter)
	 * @see WorkbenchStatusDialogManager#getPrimaryMessage(StatusAdapter)
	 */
	private String getSecondaryMessage(StatusAdapter statusAdapter) {
		String primary = getPrimaryMessage(statusAdapter);
		// we can skip the title, it is always displayed as primary message

		// if there was message set in the status
		IStatus status = statusAdapter.getStatus();
		String message = status.getMessage();
		String decoratedMessage = message == null ? null : decorate(message,
				statusAdapter);
		if (message != null && message.trim().length() > 0
				&& !primary.equals(decoratedMessage)) { 
			/* we have not displayed it yet */
			return decoratedMessage;
		}
		// if status has children
		if (status.getChildren().length > 0
				&& !primary.equals(decoratedMessage)) {
			return WorkbenchMessages.WorkbenchStatusDialog_StatusWithChildren;
		}

		// check the exception
		Throwable t = status.getException();
		if (t != null) {
			if (t.getMessage() != null) {
				String decoratedThrowable = decorate(t.getMessage(),
						statusAdapter);
				if (t.getMessage().trim().length() > 0
						&& !primary.equals(decoratedThrowable)) {
					return decoratedThrowable;
				}
			}
			String throwableName = t.getClass().getName();
			if (!primary.equals(throwableName)) {
				return throwableName;
			}
		}
		return WorkbenchMessages.WorkbenchStatusDialog_SeeDetails;
	}

	/**
	 * Get the single selection. Return null if the selection is not just one
	 * element.
	 * 
	 * @return StatusAdapter or <code>null</code>.
	 */
	private StatusAdapter getSingleSelection() {
		ISelection rawSelection = statusListViewer.getSelection();
		if (rawSelection != null
				&& rawSelection instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) rawSelection;
			if (selection.size() == 1) {
				return (StatusAdapter) selection.getFirstElement();
			}
		}
		return null;
	}

	/**
	 * Gets a collection of status adapters that were passed to the dialog.
	 * 
	 * @return collection of {@link StatusAdapter} objects
	 */
	public Collection getStatusAdapters() {
		return Collections.unmodifiableCollection(errors);
	}

	/**
	 * Return a viewer sorter for looking at the jobs.
	 * 
	 * @return ViewerSorter
	 */
	private ViewerComparator getViewerComparator() {
		return new ViewerComparator() {
			private int compare(StatusAdapter s1, StatusAdapter s2) {
				Long timestamp1 = ((Long) s1
						.getProperty(IStatusAdapterConstants.TIMESTAMP_PROPERTY));
				Long timestamp2 = ((Long) s2
						.getProperty(IStatusAdapterConstants.TIMESTAMP_PROPERTY));
				if (timestamp1 == null || timestamp2 == null
 (timestamp1.equals(timestamp2))) {
					String text1 = labelProviderWrapper.getColumnText(s1, 0);
					String text2 = labelProviderWrapper.getColumnText(s2, 0);
					return text1.compareTo(text2);
				}

				if (timestamp1.longValue() < timestamp2.longValue()) {
					return -1;
				}
				if (timestamp1.longValue() > timestamp2.longValue()) {
					return 1;
				}
				// should be never called
				return 0;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer,
			 *      java.lang.Object, java.lang.Object)
			 */
			public int compare(Viewer testViewer, Object o1, Object o2) {
				if (o1 instanceof StatusAdapter && o2 instanceof StatusAdapter) {
					return compare((StatusAdapter) o1, (StatusAdapter) o2);
				}
				// should not happen
				if (o1.hashCode() < o2.hashCode()) {
					return -1;
				}
				if (o2.hashCode() > o2.hashCode()) {
					return 1;
				}
				return 0;
			}
		};
	}

	/**
	 * The selection in the multiple job list has changed. Update widget
	 * enablements, repopulate the list and show details.
	 */
	private void handleSelectionChange() {
		StatusAdapter newSelection = getSingleSelection();
		if (newSelection != null) {
			setSelectedStatusAdapter(newSelection);
			showDetailsArea();
			refresh();
		}
	}

	/**
	 * Sets the content provider for the viewer.
	 */
	private void initContentProvider() {
		IContentProvider provider = new IStructuredContentProvider() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
			 */
			public void dispose() {
				// Nothing of interest here
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
			 */
			public Object[] getElements(Object inputElement) {
				return getStatusAdapters().toArray();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
			 *      java.lang.Object, java.lang.Object)
			 */
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				if (newInput != null) {
					refreshStatusListArea();
				}
			}
		};
		statusListViewer.setContentProvider(provider);
		statusListViewer.setInput(this);
		statusListViewer.setSelection(new StructuredSelection(statusAdapter));
	}

	/**
	 * This method should initialize the dialog bounds.
	 */
	private void initializeBounds() {
		refreshDialogSize();
		if (!modalitySwitch) {
			Rectangle shellPosition = getShell().getBounds();
			ProgressManagerUtil.animateUp(shellPosition);
		} else {
			getShell().setBounds(shellBounds);
		}
	}

	/**
	 * Sets initial label provider.
	 */
	private void initLabelProvider() {
		statusListViewer.setLabelProvider(labelProviderWrapper);
	}

	/*
	 * Prompt to inform the user that the dialog will close and the errors
	 * cleared.
	 */
	private boolean isPromptToClose() {
		IPreferenceStore store = WorkbenchPlugin.getDefault()
				.getPreferenceStore();
		if (!store.contains(PREF_SKIP_GOTO_ACTION_PROMPT)
 !store.getString(PREF_SKIP_GOTO_ACTION_PROMPT).equals(
						MessageDialogWithToggle.ALWAYS)) {
			MessageDialogWithToggle dialog = MessageDialogWithToggle.open(
					MessageDialog.CONFIRM, getShell(),
					ProgressMessages.JobErrorDialog_CloseDialogTitle,
					ProgressMessages.JobErrorDialog_CloseDialogMessage,
					ProgressMessages.JobErrorDialog_DoNotShowAgainMessage,
					false, store, PREF_SKIP_GOTO_ACTION_PROMPT, SWT.SHEET);
			return dialog.getReturnCode() == Window.OK;
		}
		return true;
	}

	/**
	 * Opens status dialog with particular statusAdapter selected.
	 * 
	 * @param modal
	 *            decides if window is modal or not.
	 * @param statusAdapter
	 *            status adapter to be selected.
	 */
	private void openStatusDialog(final boolean modal,
			final StatusAdapter statusAdapter) {
		errors.add(statusAdapter);
		modals.put(statusAdapter, new Boolean(modal));
		boolean shouldBeModal = shouldBeModal();
		if (shouldBeModal ^ dialog.isModal()) {
			dialog.getShell().removeDisposeListener(disposeListener);
			modalitySwitch = true;
			close();
			dialog = new InternalDialog(getParentShell(), this, modal);
			dialog.open();
			dialog.getShell().addDisposeListener(disposeListener);
			modalitySwitch = false;
		}
		refresh();
	}

	/**
	 * Opens the dialog tray (support area at the right side of the dialog)
	 */
	private void openTray(DialogTray tray) throws IllegalStateException,
			UnsupportedOperationException {
		if (launchTrayLink != null && !launchTrayLink.isDisposed()) {
			launchTrayLink.setEnabled(false);
		}
		this.dialog.openTray(tray);
		trayOpened = true;
	}

	/**
	 * Method which should be invoked when new errors become available for
	 * display.
	 */
	private void refresh() {
		if (dialog == null) {
			return;
		}
		if (dialogArea == null || dialogArea.isDisposed()) {
			return;
		}
		updateTitleArea();
		updateListArea();
		updateEnablements();
		// adjust width if necessary
		Point currentSize = getShell().getSize();
		Point desiredSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		if(currentSize.x < desiredSize.x){
			getShell().setSize(desiredSize.x, currentSize.y);
		} else {
			getShell().layout();
		}
	}

	private void refreshDialogSize() {
		Point newSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		getShell().setSize(newSize);
	}

	/**
	 * Refreshes the single status area. Is called only when there is one and
	 * only one error.
	 */
	private void refreshSingleStatusArea() {
		String description = labelProviderWrapper.getColumnText(
				statusAdapter, 0);
		if (description.equals(singleStatusLabel.getText()))
			singleStatusLabel.setText(" "); //$NON-NLS-1$
		singleStatusLabel.setText(description);
		singleStatusDisplayArea.layout();
		getShell().setText(title);
	}

	/**
	 * Refresh the contents of the viewer.
	 */
	private void refreshStatusListArea() {
		if (statusListViewer != null
				&& !statusListViewer.getControl().isDisposed()) {
			statusListViewer.refresh();
			if (statusListViewer.getTable().getItemCount() > 1) {
				getShell()
						.setText(
								WorkbenchMessages.WorkbenchStatusDialog_MultipleProblemsHaveOccured);
			} else {
				getShell().setText(this.title);
			}
		}
	}

	/**
	 * Sets the details area provider. If null is set, the default area provider
	 * will be used.
	 * 
	 * @param provider
	 *            A details area provider to be set.
	 */
	public void setDetailsAreaProvider(AbstractStatusAreaProvider provider) {
		this.detailsManager.setDetailsAreaProvider(provider);
	}

	/**
	 * Sets current status adapter.
	 * 
	 * @param statusAdapter
	 *            The statusAdapter to set.
	 */
	private void setSelectedStatusAdapter(StatusAdapter statusAdapter) {
		if (this.statusAdapter != statusAdapter) {
			this.statusAdapter = statusAdapter;
		}
	}

	/**
	 * Sets new label provider for the status list. This label provider is used
	 * also to display the second message on the dialog if only one status is
	 * available.
	 * 
	 * <p>
	 * This method is no longer recommended to use as it is impossible to
	 * achieve consistent behavior after changing only one label provider.
	 * </p>
	 * 
	 * @deprecated As of 3.5 {@link #setMessageDecorator} is recommended. 
	 * 
	 * @param labelProvider
	 *            a label provider to be used when displaying status adapters.
	 */
	public void setStatusListLabelProvider(ITableLabelProvider labelProvider) {
		Assert.isLegal(labelProvider != null, "Label Provider cannot be null"); //$NON-NLS-1$
		labelProviderWrapper.setLabelProvider(labelProvider);
	}

	/**
	 * Sets the support provider.
	 * 
	 * The policy for choosing support provider is:
	 * <ol>
	 * <li>use the support provider set by this method, if set</li>
	 * <li>use the support provider set in JFace Policy, if set</li>
	 * <li>use the default support area, if enabled</li>
	 * </ol>
	 * 
	 * @param provider
	 *            Support provider to be set.
	 */
	public void setSupportAreaProvider(AbstractStatusAreaProvider provider) {
		userSupportProvider = provider;
	}

	/**
	 * Decides if dialog should be modal. Dialog will be modal if any of the
	 * statuses contained by StatusAdapters had been reported with
	 * {@link StatusManager#BLOCK} flag.
	 * 
	 * @return true if any StatusHandler should be displayed in modal window
	 */
	private boolean shouldBeModal() {
		for (Iterator it = modals.keySet().iterator(); it.hasNext();) {
			Object o = it.next();
			Object value = modals.get(o);
			if (value instanceof Boolean) {
				Boolean b = (Boolean) value;
				if (b.booleanValue()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if the user should be prompted immediately about
	 * {@link StatusAdapter}
	 * 
	 * @param statusAdapter
	 *            to be checked.
	 * @return true if the statusAdapter should be prompted, false otherwise.
	 */
	private boolean shouldPrompt(final StatusAdapter statusAdapter) {
		Object noPromptProperty = statusAdapter
				.getProperty(IProgressConstants.NO_IMMEDIATE_ERROR_PROMPT_PROPERTY);

		boolean prompt = true;
		if (noPromptProperty instanceof Boolean) {
			prompt = !((Boolean) noPromptProperty).booleanValue();
		}
		return prompt;
	}

	/**
	 * Show the details portion of the dialog if it is not already visible. This
	 * method will only work when it is invoked after the control of the dialog
	 * has been set. In other words, after the <code>createContents</code>
	 * method has been invoked and has returned the control for the content area
	 * of the dialog. Invoking the method before the content area has been set
	 * or after the dialog has been disposed will have no effect.
	 */
	private void showDetailsArea() {
		if (dialogArea != null && !dialogArea.isDisposed()) {
			if (detailsManager.isOpen()) {
				detailsManager.close();
				detailsManager.createDetailsArea(dialogArea, statusAdapter);
			} else {
				toggleDetailsArea();
				detailsOpened = true;
			}
			dialogArea.layout();
		}
	}

	/**
	 * Toggles the unfolding of the details area. This is triggered by the user
	 * pressing the details button.
	 * 
	 */
	private boolean toggleDetailsArea() {
		boolean opened = false;
		Point windowSize = getShell().getSize();
		if (detailsManager.isOpen()) {
			detailsManager.close();
			dialog.getButton(IDialogConstants.DETAILS_ID).setText(
					IDialogConstants.SHOW_DETAILS_LABEL);
			opened = false;
		} else {
			detailsManager.createDetailsArea(dialogArea, statusAdapter);
			dialog.getButton(IDialogConstants.DETAILS_ID).setText(
					IDialogConstants.HIDE_DETAILS_LABEL);
			opened = true;
		}

		GridData listAreaGridData = (GridData) listArea.getLayoutData();
		// if there is only one status to display,
		// make sure that the list area is as small as possible
		if (getStatusAdapters().size() == 1) {
			listAreaGridData.heightHint = 0;
		}
		// allow listArea to grab space depending if details
		// are opened or not
		if (opened) {
			listAreaGridData.grabExcessVerticalSpace = false;
		} else {
			listAreaGridData.grabExcessVerticalSpace = true;
		}
		listArea.setLayoutData(listAreaGridData);

		Point newSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int diffY = newSize.y - windowSize.y;
		// increase the dialog height if details were opened and such increase is necessary
		// decrease the dialog height if details were closed and empty space appeared
		if ((opened && diffY > 0) || (!opened && diffY < 0)) {
			getShell().setSize(new Point(windowSize.x, windowSize.y + (diffY)));
		}
		dialogArea.layout();
		return opened;
	}

	/**
	 * Update the button enablements
	 */
	private void updateEnablements() {
		Button details = dialog.getButton(IDialogConstants.DETAILS_ID);
		if (details != null) {
			details.setEnabled(true);
		}
		Button gotoButton = dialog.getButton(GOTO_ACTION_ID);
		if (gotoButton != null) {
			IAction gotoAction = getGotoAction();
			boolean hasValidGotoAction = (gotoAction != null)
					&& (gotoAction.getText() != null);
			if (hasValidGotoAction) {
				hideButton(gotoButton, false);
				gotoButton.setText(gotoAction.getText());

				((GridData) gotoButton.getLayoutData()).widthHint = gotoButton
						.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
				gotoButton.getParent().layout();
			} else
				hideButton(gotoButton, true);
		}
		// and tray enablement button
		if (supportTray.providesSupport() && !hideSupportButton) {
			if(launchTrayLink == null || launchTrayLink.isDisposed()){
				launchTrayLink = createGetSupportLink();
			}
			launchTrayLink.setEnabled(!trayOpened);
		} else {
			if(launchTrayLink != null && !launchTrayLink.isDisposed()){
				launchTrayLink.dispose();
				launchTrayLink = null;
			}
		}
		IViewDescriptor descriptor = shouldDisplayLinkToErrorLog();
		if (descriptor != null) {
			if (showErrorLogLink == null || showErrorLogLink.isDisposed()) {
				showErrorLogLink = createShowErrorLogLink();
			}
		} else {
			if (showErrorLogLink != null && !showErrorLogLink.isDisposed()) {
				showErrorLogLink.dispose();
			}
		}
		linkComposite.getParent().layout();
	}

	private IViewDescriptor shouldDisplayLinkToErrorLog(){
		/* no support for error log */
		if(!supportForErrorLog) {
			return null;
		}
		/* check handling hint and display link if it is expected */
		boolean shouldDisplay = false;
		Iterator it = getStatusAdapters().iterator();
		while (it.hasNext()) {
			StatusAdapter adapter = (StatusAdapter) it.next();
			Integer hint = (Integer) adapter.getProperty(HINT); 
			if (hint != null && ((hint.intValue() & StatusManager.LOG) != 0)) {
				shouldDisplay |= true;
				break;
			}
		}
		if (!shouldDisplay) {
			return null;
		}
		/* view description */
		return Workbench.getInstance().getViewRegistry().find(LOG_VIEW_ID);
	}

	/**
	 * Hide the button if hide is <code>true</code>.
	 * 
	 * @param button
	 * @param hide
	 */
	private void hideButton(Button button, boolean hide) {
		((GridData) button.getLayoutData()).exclude = hide;
		button.setVisible(!hide);
		button.setEnabled(!hide);
	}

	/**
	 * This methods switches StatusAdapters presentation depending if there is
	 * one status or more.
	 */
	private void updateListArea() {
		// take care about list area if there is more than one status
		if (errors.size() > 1) {
			if (singleStatusDisplayArea != null) {
				singleStatusDisplayArea.dispose();
			}
			if (statusListViewer == null
 statusListViewer.getControl().isDisposed()) {
				fillListArea(listArea);
				listArea.layout();
				listArea.getParent().layout();
				getShell().setSize(
						getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
			refreshStatusListArea();
		}
	}

	/**
	 * Updated title area. Adjust title, title message and title image according
	 * to selected {@link StatusAdapter}.
	 */
	private void updateTitleArea() {
		Image image = getImage();
		titleImageLabel.setImage(image);
		if (statusAdapter != null) {
			mainMessageLabel.setText(getMainMessage(statusAdapter));
		}
		if (singleStatusDisplayArea != null) {
			if (getStatusAdapters().size() > 1) {
				singleStatusDisplayArea.dispose();
			} else {
				refreshSingleStatusArea();
			}
		}
		titleArea.layout();
	}

	/**
	 * Gets the shell of the managed dialog.
	 * 
	 * @see org.eclipse.jface.window.IShellProvider#getShell()
	 */
	private Shell getShell() {
		if (this.dialog == null)
			return null;
		return this.dialog.getShell();
	}

	/**
	 * <p>
	 * This methods sets up the decorator, which is used to modify displayed
	 * strings extracted from StatusAdapter. The decorator should be used to
	 * remove technical codes from the dialog, f.e. following message
	 * "<i>ERR2008 Invalid password</i>" can be translated into
	 * "<i>Invalid password</i>".
	 * </p>
	 * <p>
	 * The decorator will be applied only to messages extracted from
	 * StatusAdapter (predefined messages like
	 * "This status has children statuses. See 'Details' for more information."
	 * are not affected.
	 * </p>
	 * <p>
	 * This method should not be used together with
	 * {@link #setStatusListLabelProvider(ITableLabelProvider)}.
	 * </p>
	 * 
	 * @param decorator
	 *            - the decorator to be set. Only
	 *            {@link ILabelDecorator#decorateText(String, Object)} method
	 *            will be used. This method should return <code>null</code> if
	 *            and only if the first argument is null. StatusAdapter is
	 *            passed as second parameter. Other methods should have default
	 *            behavior as they may be used in future versions of the dialog.
	 * @since 3.5
	 */
	public void setMessageDecorator(ILabelDecorator decorator){
		this.messageDecorator = decorator;
	}
	
	private String decorate(String string, StatusAdapter adapter) {
		if(messageDecorator != null){
			string = messageDecorator.decorateText(string, adapter);
		}
		return string;
	}

	/**
	 * This method sets various properties on the manager.
	 * 
	 * @param key
	 *            a key of the property to be set.
	 * @param value
	 *            a value of the property to be set. The value must be of type
	 *            specified by the property key. <code>null</code> should never
	 *            be passed unless the property key javadoc allows for that.
	 * @since 3.5
	 */
	public void setProperty(Object key, Object value){
		if(key == IStatusDialogConstants.SHOW_SUPPORT){
			showSupport = ((Boolean)value).booleanValue();
		}
		if(key == IStatusDialogConstants.ERRORLOG_LINK){
			supportForErrorLog = ((Boolean)value).booleanValue();
		}
		if(key == IStatusDialogConstants.HANDLE_OK_STATUSES){
			handleOKStatuses   = ((Boolean)value).booleanValue();
		}
	}

	/**
	 * This method gets various dialog properties.
	 * 
	 * @param key
	 *            a key of the property to be get.
	 * @return a value of the property. The value will be of type specified by
	 *         the property key. <code>null</code> can be returned.
	 * @since 3.5
	 */
	public Object getProperty(Object key){
		if(key == IStatusDialogConstants.SHELL){
			return getShell();
		}
		if(key == IStatusDialogConstants.SHOW_SUPPORT){
			return Boolean.valueOf(showSupport);
		}
		if(key == IStatusDialogConstants.ERRORLOG_LINK){
			return Boolean.valueOf(supportForErrorLog);
		}
		if(key == IStatusDialogConstants.HANDLE_OK_STATUSES){
			return Boolean.valueOf(handleOKStatuses);
		}
		return null;
	}
	
	
	/**
	 * This method makes the dialog to be similar to the JFace ErrorDialog. The
	 * dialog handles {@link StatusAdapter}s wrapping {@link IStatus} with
	 * severity {@link IStatus#OK}, does not display the link to the error log,
	 * does not display the link to the support area but always opens it.
	 * 
	 * @see ErrorDialog
	 * @since 3.5
	 */
	public void enableErrorDialogCompatibility(){
		setProperty(IStatusDialogConstants.ERRORLOG_LINK, Boolean.FALSE);
		setProperty(IStatusDialogConstants.HANDLE_OK_STATUSES, Boolean.TRUE);
		setProperty(IStatusDialogConstants.SHOW_SUPPORT, Boolean.TRUE);
		setHideShowSupportButton(true);
	}
	
	private void setHideShowSupportButton(boolean enabled) {
		this.hideSupportButton  = enabled;
		
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3144.java