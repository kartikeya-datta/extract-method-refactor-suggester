error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1428.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1428.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1428.java
text:
```scala
E@@ditorStack workbook = (EditorStack) iter.next();

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

package org.eclipse.ui.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.internal.intro.IIntroConstants;
import org.eclipse.ui.internal.registry.ActionSetRegistry;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;
import org.eclipse.ui.internal.registry.IStickyViewDescriptor;
import org.eclipse.ui.internal.registry.IViewRegistry;
import org.eclipse.ui.internal.registry.PerspectiveDescriptor;
import org.eclipse.ui.internal.registry.PerspectiveExtensionReader;
import org.eclipse.ui.internal.registry.PerspectiveRegistry;

/**
 * The ViewManager is a factory for workbench views.  
 */
public class Perspective
{
	private PerspectiveDescriptor descriptor;
	protected WorkbenchPage page;
	protected LayoutPart editorArea;
	private PartPlaceholder editorHolder;
	private ViewFactory viewFactory;
	private ArrayList visibleActionSets;
	private ArrayList alwaysOnActionSets;
	private ArrayList alwaysOffActionSets;
	private ArrayList newWizardActionIds;
	private ArrayList showViewActionIds;
	private ArrayList perspectiveActionIds;
	private ArrayList fastViews;
	private Map mapIDtoViewLayoutRec;
	private boolean fixed;
	private ArrayList showInPartIds;
	private HashMap showInTimes = new HashMap();
	private IViewReference activeFastView;
	private IMemento memento;
	protected PerspectivePresentation presentation;
	final static private String VERSION_STRING = "0.016";//$NON-NLS-1$
	private FastViewPane fastViewPane = new FastViewPane();
	
	// fields used by fast view resizing via a sash
	private static final int FASTVIEW_HIDE_STEPS = 5;

	private String oldPartID = null;
	private boolean shouldHideEditorsOnActivate = false;

/**
 * ViewManager constructor comment.
 */
public Perspective(PerspectiveDescriptor desc, WorkbenchPage page)
	throws WorkbenchException
{
	this(page);
	descriptor = desc;
	if(desc != null)
		createPresentation(desc);
}
/**
 * ViewManager constructor comment.
 */
protected Perspective(WorkbenchPage page) throws WorkbenchException {
	this.page = page;
	this.editorArea = page.getEditorPresentation().getLayoutPart();
	this.viewFactory = page.getViewFactory();
	visibleActionSets = new ArrayList(2);
	alwaysOnActionSets = new ArrayList(2);
	alwaysOffActionSets = new ArrayList(2);
	fastViews = new ArrayList(2);
	mapIDtoViewLayoutRec = new HashMap();
}

/**
 * Sets the fast view attribute.
 * Note: The page is expected to update action bars.
 */
public void addFastView(IViewReference ref) {
	ViewPane pane = (ViewPane)((WorkbenchPartReference)ref).getPane();
	if (!isFastView(ref)) {
		// Only remove the part from the presentation if it
		// is actually in the presentation.
		if (presentation.hasPlaceholder(ref.getId(), ref.getSecondaryId()) ||
			pane.getContainer() != null)
				presentation.removePart(pane);
		// We are drag-enabling the pane because it has been disabled
		// when it was removed from the perspective presentation.
		fastViews.add(ref);
		pane.setFast(true);
		Control ctrl = pane.getControl();
		if (ctrl != null)
			ctrl.setEnabled(false); // Remove focus support.
	}
}
/**
 * Moves a part forward in the Z order of a perspective so it is visible.
 *
 * @param part the part to bring to move forward
 * @return true if the part was brought to top, false if not.
 */
public boolean bringToTop(IViewReference ref) {
	if (isFastView(ref)) {
		setActiveFastView(ref);
		return true;
	} else {
		return presentation.bringPartToTop(getPane(ref));
	}
}
/**
 * Returns true if a view can close.
 */
public boolean canCloseView(IViewPart view) {
	return true;
}


/**
 * Returns whether a view exists within the perspective.
 */
public boolean containsView(IViewPart view) {
    IViewSite site = view.getViewSite();
    IViewReference ref = findView(site.getId(), site.getSecondaryId());
	if(ref == null)
		return false;
	return (view == ref.getPart(false));
}
/**
 * Create the initial list of action sets.
 */
private void createInitialActionSets(List stringList) {
	ActionSetRegistry reg = WorkbenchPlugin.getDefault().getActionSetRegistry();
	Iterator iter = stringList.iterator();
	while (iter.hasNext()) {
		String id = (String)iter.next();
		IActionSetDescriptor desc = reg.findActionSet(id);
		if (desc != null)
			visibleActionSets.add(desc);
		else
			WorkbenchPlugin.log("Unable to find Action Set: " + id);//$NON-NLS-1$
	}
}
/**
 * Create a presentation for a perspective.
 */
private void createPresentation(PerspectiveDescriptor persp)
	throws WorkbenchException
{
	if (persp.hasCustomDefinition()) {
		loadCustomPersp(persp);
	} else {
		loadPredefinedPersp(persp);
	}
}
/**
 * Dispose the perspective and all views contained within.
 */
public void dispose() {
	// Get rid of presentation.
	if(presentation == null)
		return;
		
	presentation.deactivate();
	presentation.disposeSashes();
	

	// Release each view.
	IViewReference refs[] = getViewReferences();
	for (int i = 0,length = refs.length; i < length; i ++) {
		getViewFactory().releaseView(refs[i]);
	}

	fastViewPane.dispose();

	mapIDtoViewLayoutRec.clear();
}
/**
 * Finds the view with the given ID that is open in this page, or <code>null</code>
 * if not found.
 * 
 * @param viewId the view ID
 */
public IViewReference findView(String viewId) {
	return findView(viewId, null);
}

/**
 * Finds the view with the given id and secondary id that is open in this page, 
 * or <code>null</code> if not found.
 * 
 * @param viewId the view ID
 * @param secondaryId the secondary ID
 */
public IViewReference findView(String id, String secondaryId) {
	IViewReference refs[] = getViewReferences();
	for (int i = 0; i < refs.length; i ++) {
		IViewReference ref = refs[i];
			if (id.equals(ref.getId())
					&& (secondaryId == null ? ref.getSecondaryId() == null : secondaryId.equals(ref
							.getSecondaryId())))
			return ref;
	}
	return null;
}
/**
 * Returns an array of the visible action sets. 
 */
public IActionSetDescriptor[] getActionSets() {
	int size = visibleActionSets.size();
	IActionSetDescriptor [] array = new IActionSetDescriptor[size];
	for (int i = 0; i < size; i ++) {
		array[i] = (IActionSetDescriptor)visibleActionSets.get(i);
	}
	return array;
}
/**
 * Returns the window's client composite widget
 * which views and editor area will be parented.
 */
private Composite getClientComposite() {
	return page.getClientComposite();
}
/**
 * Returns the perspective.
 */
public IPerspectiveDescriptor getDesc() {
	return descriptor;
}

/**
 * Returns the bounds of the given fast view.
 */
/*package*/ Rectangle getFastViewBounds(IViewReference ref) {
	// Copy the bounds of the page composite
	Rectangle bounds = page.getClientComposite().getBounds();
	// get the width ratio of the fast view
	float ratio = getFastViewWidthRatio(ref.getId());
	// Compute the actual width of the fast view.
	bounds.width = (int)(ratio*getClientComposite().getSize().x);
	return bounds;
}
/**
 * Returns the docked views.
 */
public IViewReference [] getFastViews() {
	IViewReference array[] = new IViewReference[fastViews.size()];
	fastViews.toArray(array);
	return array;
}
/**
 * Returns the new wizard actions the page.
 * This is List of Strings.
 */
public ArrayList getNewWizardActionIds() {
	return newWizardActionIds;
}
/**
 * Returns the pane for a view reference.
 */
private ViewPane getPane(IViewReference ref) {
	return (ViewPane)((WorkbenchPartReference)ref).getPane();
}
/**
 * Returns the perspective actions for this page.
 * This is List of Strings.
 */
public ArrayList getPerspectiveActionIds() {
	return perspectiveActionIds;
}
/**
 * Returns the presentation.
 */
public PerspectivePresentation getPresentation() {
	return presentation;
}
/**
 * Retrieves the ratio for the fast view with the given ID. If
 * the ratio is not known, the default ratio for the view is returned.
 */
private float getFastViewWidthRatio(String id) {
	ViewLayoutRec rec = getViewLayoutRec(id, true);
	if (rec.fastViewWidthRatio == IPageLayout.INVALID_RATIO) {
	    IViewRegistry reg = WorkbenchPlugin.getDefault().getViewRegistry();	
	    rec.fastViewWidthRatio = reg.find(id).getFastViewWidthRatio();
	}
	return rec.fastViewWidthRatio;
}

/**
 * Returns the ids of the parts to list in the Show In... dialog.
 * This is a List of Strings.
 */
public ArrayList getShowInPartIds() {
	return showInPartIds;
}

/**
 * Returns the time at which the last Show In was performed
 * for the given target part, or 0 if unknown.
 */
public long getShowInTime(String partId) {
	Long t = (Long) showInTimes.get(partId);
	return t == null ? 0L : t.longValue();
}
/**
 * Returns the ids of the views to list in the Show View shortcuts.
 * This is a List of Strings.
 */
public ArrayList getShowViewActionIds() {
	return showViewActionIds;
}

/**
 * Returns the view factory.
 */
private ViewFactory getViewFactory() {
	return viewFactory;
}
/**
 * Open the tracker to allow the user to move
 * the specified part using keyboard.
 */
public void openTracker(ViewPane pane) {
	presentation.openTracker(pane);
}
/**
 * See IWorkbenchPage.
 */
public IViewReference [] getViewReferences() {
	// Get normal views.
	if(presentation == null)
		return new IViewReference[0];
	
	List panes = new ArrayList(5);
	presentation.collectViewPanes(panes);
	
	IViewReference [] resultArray = new IViewReference[panes.size() + fastViews.size()];

	// Copy fast views.
	int nView = 0;
	for (int i = 0; i < fastViews.size(); i++) {
		resultArray[nView] = (IViewReference)fastViews.get(i);
		++ nView;
	}
	
	// Copy normal views.
	for (int i = 0; i < panes.size(); i ++) {
		ViewPane pane = (ViewPane)panes.get(i);
		resultArray[nView] = pane.getViewReference();
		++ nView;
	}
	
	return resultArray;
}
/**
 * @see IWorkbenchPage
 * Note: The page is expected to update action bars.
 */
public void hideActionSet(String id) {
	ActionSetRegistry reg = WorkbenchPlugin.getDefault().getActionSetRegistry();
	IActionSetDescriptor desc = reg.findActionSet(id);
	if (alwaysOnActionSets.contains(desc))
		return;
	if (desc != null)
		visibleActionSets.remove(desc);
}
/**
 * Hide the editor area if visible
 */
protected void hideEditorArea() {
	if (!isEditorAreaVisible())
		return;

	// Replace the editor area with a placeholder so we
	// know where to put it back on show editor area request.
	editorHolder = new PartPlaceholder(editorArea.getID());
	presentation.getLayout().replace(editorArea, editorHolder);

	// Disable the entire editor area so if an editor had
	// keyboard focus it will let it go.
	if (editorArea.getControl() != null)
		editorArea.getControl().setEnabled(false);
		
	setEditorAreaVisible(false);	
}
/**
 * Hides a fast view. The view shrinks equally <code>steps</code> times
 * before disappearing completely.
 */
private void hideFastView(IViewReference ref, int steps) {
	setFastViewIconSelection(ref, false);

		// Note: We always do at least one step of the animation.
				// Note: This doesn't take into account the overhead of doing
	if (ref == activeFastView) {
		saveFastViewWidthRatio();
		fastViewPane.hideView();
	}	
}
/**
 * Hides the fast view sash for zooming in a fast view.
 */
void hideFastViewSash() {
	fastViewPane.hideFastViewSash();
}
public boolean hideView(IViewReference ref) {
	// If the view is locked just return.
	ViewPane pane = getPane(ref);
	
	// Remove the view from the current presentation.
	if (isFastView(ref)) {
		fastViews.remove(ref);
		if(pane != null)
			pane.setFast(false);		//force an update of the toolbar
		if (activeFastView == ref)
			setActiveFastView(null);
		if(pane != null)
			pane.getControl().setEnabled(true);
	} else { 
		presentation.removePart(pane);
	}
	
	// Dispose view if ref count == 0.
	getViewFactory().releaseView(ref);
	return true;
}
/*
 * Return whether the editor area is visible or not.
 */
protected boolean isEditorAreaVisible() {
	return editorHolder == null;
}
/**
 * Returns true if a view is fast.
 */
public boolean isFastView(IViewReference ref) {
	return fastViews.contains(ref);
}
/**
 * Returns the view layout rec for the given view reference,
 * or null if not found.  If create is true, it creates the record
 * if not already created.
 */
private ViewLayoutRec getViewLayoutRec(IViewReference ref, boolean create) {
    return getViewLayoutRec(ViewFactory.getKey(ref), create);
}
/**
 * Returns the view layout record for the given view id
 * or null if not found.  If create is true, it creates the record
 * if not already created.
 */
private ViewLayoutRec getViewLayoutRec(String viewId, boolean create) {
    ViewLayoutRec rec = (ViewLayoutRec) mapIDtoViewLayoutRec.get(viewId);
    if (rec == null && create) {
        rec = new ViewLayoutRec();
        mapIDtoViewLayoutRec.put(viewId, rec);
    }
    return rec;
}
/**
 * Returns true if a layout or perspective is fixed.
 */
public boolean isFixedLayout() {
	//@issue is there a difference between a fixed
	//layout and a fixed perspective?? If not the API
	//may need some polish, WorkbenchPage, PageLayout
	//and Perspective all have isFixed methods.  
	//PageLayout and Perspective have their own fixed
	//attribute, we are assuming they are always in sync.
	//WorkbenchPage delegates to the perspective.
	return fixed;
}
/**
 * Returns true if a view is standalone.
 * 
 * @since 3.0
 */
public boolean isStandaloneView(IViewReference ref) {
    ViewLayoutRec rec = getViewLayoutRec(ref, false);
    return rec != null && rec.isStandalone;
}
/**
 * Returns whether the title for a view should
 * be shown.  This applies only to standalone views.
 * 
 * @since 3.0
 */
public boolean getShowTitleView(IViewReference ref) {
    ViewLayoutRec rec = getViewLayoutRec(ref, false);
    return rec != null && rec.showTitle;
}

/**
 * Creates a new presentation from a persistence file.
 * Note: This method should not modify the current state of the perspective.
 */
private void loadCustomPersp(PerspectiveDescriptor persp)
{
	//get the layout from the registry	
	PerspectiveRegistry perspRegistry = (PerspectiveRegistry) WorkbenchPlugin.getDefault().getPerspectiveRegistry();
	try {
		IMemento memento = perspRegistry.getCustomPersp(persp.getId());
		// Restore the layout state.
		MultiStatus status = new MultiStatus(
			PlatformUI.PLUGIN_ID,IStatus.OK,
			WorkbenchMessages.format("Perspective.unableToRestorePerspective",new String[]{persp.getLabel()}), //$NON-NLS-1$
			null);
		status.merge(restoreState(memento));
		status.merge(restoreState());
		if(status.getSeverity() != IStatus.OK) {
			unableToOpenPerspective(persp,status);
		}
	} catch (IOException e) {
		unableToOpenPerspective(persp,null);
	} catch (WorkbenchException e) {
		unableToOpenPerspective(persp,e.getStatus());
	}
}
private void unableToOpenPerspective(PerspectiveDescriptor persp,IStatus status) {
	PerspectiveRegistry perspRegistry = (PerspectiveRegistry) WorkbenchPlugin.getDefault().getPerspectiveRegistry();
	perspRegistry.deletePerspective(persp);
	// If this is a predefined perspective, we will not be able to delete
	// the perspective (we wouldn't want to).  But make sure to delete the
	// customized portion.
	persp.deleteCustomDefinition();
	String title = WorkbenchMessages.getString("Perspective.problemRestoringTitle");  //$NON-NLS-1$
	String msg = WorkbenchMessages.getString("Perspective.errorReadingState"); //$NON-NLS-1$
	if(status == null) {
		MessageDialog.openError((Shell)null,title,msg); 
	} else {
		ErrorDialog.openError((Shell)null,title,msg,status); 
	}
}

/**
 * Create a presentation for a perspective.
 * Note: This method should not modify the current state of the perspective.
 */
private void loadPredefinedPersp(
	PerspectiveDescriptor persp)
	throws WorkbenchException
{
	// Create layout engine.
	IPerspectiveFactory factory = null;
	try {
		factory = persp.createFactory();
	} catch (CoreException e) {
		throw new WorkbenchException(WorkbenchMessages.format("Perspective.unableToLoad", new Object[] {persp.getId()})); //$NON-NLS-1$
	}

	// Create layout factory.
	RootLayoutContainer container = new RootLayoutContainer(page);
	PageLayout layout = new PageLayout(container, getViewFactory(), editorArea, descriptor);
	layout.setFixed(descriptor.getFixed());

	// add the placeholders for the sticky folders and their contents	
	IPlaceholderFolderLayout stickyFolderRight = null, 
		stickyFolderLeft = null, 
		stickyFolderTop = null, 
		stickyFolderBottom = null;
	
	IStickyViewDescriptor [] descs = WorkbenchPlugin
										.getDefault()
										.getViewRegistry()
										.getStickyViews();
	for (int i = 0; i < descs.length; i++) {	    
	    String id = descs[i].getId();
        switch(descs[i].getLocation()) {
	    	case IPageLayout.RIGHT:
	    	    if (stickyFolderRight == null)
	    	        stickyFolderRight = layout.createPlaceholderFolder(
	    	                IStickyViewDescriptor.STICKY_FOLDER_RIGHT, 
	    	                IPageLayout.RIGHT, 
	    	                .75f, 
	    	                IPageLayout.ID_EDITOR_AREA);
	    	    stickyFolderRight.addPlaceholder(id);
	    		break;
	    	case IPageLayout.LEFT:
	    	    if (stickyFolderLeft == null)
	    	        stickyFolderLeft = layout.createPlaceholderFolder(
	    	                IStickyViewDescriptor.STICKY_FOLDER_LEFT, 
	    	                IPageLayout.LEFT, 
	    	                .25f, 
	    	                IPageLayout.ID_EDITOR_AREA);
	    	    stickyFolderLeft.addPlaceholder(id);
	    		break;
	    	case IPageLayout.TOP:
	    	    if (stickyFolderTop == null)
	    	        stickyFolderTop = layout.createPlaceholderFolder(
	    	                IStickyViewDescriptor.STICKY_FOLDER_TOP, 
	    	                IPageLayout.TOP, 
	    	                .25f, 
	    	                IPageLayout.ID_EDITOR_AREA);
	    	    stickyFolderTop.addPlaceholder(id);
	    		break;
	    	case IPageLayout.BOTTOM:
	    	    if (stickyFolderBottom == null)
	    	        stickyFolderBottom = layout.createPlaceholderFolder(
	    	                IStickyViewDescriptor.STICKY_FOLDER_BOTTOM, 
	    	                IPageLayout.BOTTOM, 
	    	                .75f, 
	    	                IPageLayout.ID_EDITOR_AREA);
	    	    stickyFolderBottom.addPlaceholder(id);
	    		break;
	    }    	
    }

	// Run layout engine.
	factory.createInitialLayout(layout);
	PerspectiveExtensionReader extender = new PerspectiveExtensionReader();
	extender.extendLayout(descriptor.getId(), layout);

	// Retrieve view layout info stored in the page layout.
	mapIDtoViewLayoutRec.putAll(layout.getIDtoViewLayoutRecMap());

	// Create action sets.
	createInitialActionSets(layout.getActionSets());
	alwaysOnActionSets.addAll(visibleActionSets);
	newWizardActionIds = layout.getNewWizardActionIds();
	showViewActionIds = layout.getShowViewActionIds();
	perspectiveActionIds = layout.getPerspectiveActionIds();
	showInPartIds = layout.getShowInPartIds();
	
	// Retrieve fast views
	fastViews = layout.getFastViews();
		
	// Is the layout fixed
	fixed = layout.isFixed();
	
	// Create presentation.	
	presentation = new PerspectivePresentation(page, container);

	// Hide editor area if requested by factory
	if (!layout.isEditorAreaVisible())
		hideEditorArea();
		
}
/**
 * activate.
 */
protected void onActivate() {
	// Update editor area state.
	if (editorArea.getControl() != null) {
		if (isEditorAreaVisible()) {
			// Enable the editor area now that it will be made
			// visible and can accept keyboard focus again.
			editorArea.getControl().setEnabled(true);
			setEditorAreaVisible(true);
		} else {
			// Disable the entire editor area so if an editor had
			// keyboard focus it will let it go.
			editorArea.getControl().setEnabled(false);
			setEditorAreaVisible(false);
		}
	}

	// Update fast views.
	// Make sure the control for the fastviews are create so they can
	// be activated.
	for (int i = 0; i < fastViews.size(); i++){
		ViewPane pane = getPane((IViewReference)fastViews.get(i));
		if(pane != null) {
			Control ctrl = pane.getControl();
			if (ctrl == null) {
				pane.createControl(getClientComposite());
				ctrl = pane.getControl();
			}	
			ctrl.setEnabled(false); // Remove focus support.
		}
	}
	
	setAllPinsVisible(true);
	presentation.activate(getClientComposite());
	
	if (shouldHideEditorsOnActivate) {
		// We do this here to ensure that createPartControl is called on the top editor
		// before it is hidden. See bug 20166.
		hideEditorArea();
		shouldHideEditorsOnActivate = false;
	}
}
/**
 * deactivate.
 */
protected void onDeactivate() {
	presentation.deactivate();
	setActiveFastView(null);
	setAllPinsVisible(false);

	// Update fast views.
	for (int i = 0; i < fastViews.size(); i++){
		ViewPane pane = getPane((IViewReference)fastViews.get(i));
		if(pane != null) {
			Control ctrl = pane.getControl();
			if (ctrl != null)
				ctrl.setEnabled(true); // Add focus support.
		}
	}
}
/**
 * Notifies that a part has been activated.
 */
public void partActivated(IWorkbenchPart activePart) {
	// If a fastview is open close it.
	if (activeFastView != null && activeFastView.getPart(false) != activePart)
		setActiveFastView(null);
}

/**
 * The user successfully performed a Show In... action on the specified part.
 * Update the history.
 */
public void performedShowIn(String partId) {
	showInTimes.put(partId, new Long(System.currentTimeMillis()));
}

/**
 * Sets the fast view attribute.
 * Note: The page is expected to update action bars.
 */
public void removeFastView(IViewReference ref) {
	ViewPane pane = getPane(ref);
	if (isFastView(ref)) {
		if (activeFastView == ref)
			setActiveFastView(null);
		fastViews.remove(ref);
		pane.setFast(false);
		Control ctrl = pane.getControl();
		if (ctrl != null)
			ctrl.setEnabled(true); // Modify focus support.
		// We are disabling the pane because it will be enabled when it
		// is added to the presentation. When a pane is enabled a drop
		// listener is added to it, and we do not want to have multiple
		// listeners for a pane
		presentation.addPart(pane);
	}
}
/**
 * Fills a presentation with layout data.
 * Note: This method should not modify the current state of the perspective.
 */
public IStatus restoreState(IMemento memento) {
	MultiStatus result = new MultiStatus(
		PlatformUI.PLUGIN_ID,IStatus.OK,
		WorkbenchMessages.getString("Perspective.problemsRestoringPerspective"),null); //$NON-NLS-1$

	// Create persp descriptor.
	descriptor = new PerspectiveDescriptor(null,null,null);
	result.add(descriptor.restoreState(memento));
	PerspectiveDescriptor desc = (PerspectiveDescriptor)WorkbenchPlugin
		.getDefault().getPerspectiveRegistry().findPerspectiveWithId(descriptor.getId());
	if (desc != null)
		descriptor = desc;
		
	this.memento = memento;
	// Add the visible views.
	IMemento views[] = memento.getChildren(IWorkbenchConstants.TAG_VIEW);
	result.merge(createReferences(views));
	
	memento = memento.getChild(IWorkbenchConstants.TAG_FAST_VIEWS);
	if(memento != null) {
		views = memento.getChildren(IWorkbenchConstants.TAG_VIEW);
		result.merge(createReferences(views));	
	}
	return result;
}
private IStatus createReferences(IMemento views[]) {
	MultiStatus result = new MultiStatus(
		PlatformUI.PLUGIN_ID,IStatus.OK,
		WorkbenchMessages.getString("Perspective.problemsRestoringViews"),null); //$NON-NLS-1$
	
	for (int x = 0; x < views.length; x ++) {
		// Get the view details.
		IMemento childMem = views[x];
		String primaryId = childMem.getString(IWorkbenchConstants.TAG_ID);
		String secondaryId = childMem.getString(IWorkbenchConstants.TAG_SECONDARY_ID);
		// Create and open the view.
		try {
			if(!"true".equals(childMem.getString(IWorkbenchConstants.TAG_REMOVED))) { //$NON-NLS-1$
				viewFactory.createView(primaryId, secondaryId);
			}
		} catch (PartInitException e) {
			childMem.putString(IWorkbenchConstants.TAG_REMOVED,"true"); //$NON-NLS-1$
			result.add(new Status(IStatus.ERROR,PlatformUI.PLUGIN_ID,0,e.getMessage(),e));
		}
	}
	return result;
}

/**
 * Fills a presentation with layout data.
 * Note: This method should not modify the current state of the perspective.
 */
public IStatus restoreState() {
	if(this.memento == null)
		return new Status(IStatus.OK,PlatformUI.PLUGIN_ID,0,"",null); //$NON-NLS-1$

	MultiStatus result = new MultiStatus(
		PlatformUI.PLUGIN_ID,IStatus.OK,
		WorkbenchMessages.getString("Perspective.problemsRestoringPerspective"),null); //$NON-NLS-1$
				
	IMemento memento = this.memento;
	this.memento = null;
	
	IMemento boundsMem = memento.getChild(IWorkbenchConstants.TAG_WINDOW);
	if(boundsMem != null) {
		Rectangle r = new Rectangle(0,0,0,0);
		r.x = boundsMem.getInteger(IWorkbenchConstants.TAG_X).intValue();
		r.y = boundsMem.getInteger(IWorkbenchConstants.TAG_Y).intValue();
		r.height = boundsMem.getInteger(IWorkbenchConstants.TAG_HEIGHT).intValue();
		r.width = boundsMem.getInteger(IWorkbenchConstants.TAG_WIDTH).intValue();
		if(page.getWorkbenchWindow().getPages().length == 0) {
			page.getWorkbenchWindow().getShell().setBounds(r);
		}
	}
	
	// Create an empty presentation..
	RootLayoutContainer mainLayout = new RootLayoutContainer(page);
	PerspectivePresentation pres = new PerspectivePresentation(page, mainLayout);

	// Read the layout.
	result.merge(pres.restoreState(memento.getChild(IWorkbenchConstants.TAG_LAYOUT)));

	// Add the editor workbook. Do not hide it now.
	pres.replacePlaceholderWithPart(editorArea);

	// Add the visible views.
	IMemento [] views = memento.getChildren(IWorkbenchConstants.TAG_VIEW);

	for (int x = 0; x < views.length; x ++) {
		// Get the view details.
		IMemento childMem = views[x];
		String primaryId = childMem.getString(IWorkbenchConstants.TAG_ID);
		String secondaryId = childMem.getString(IWorkbenchConstants.TAG_SECONDARY_ID);
		
		// skip the intro as it is restored higher up in workbench.
		if (primaryId.equals(IIntroConstants.INTRO_VIEW_ID))
			continue;

		// Create and open the view.
		IViewReference viewRef = viewFactory.getView(primaryId, secondaryId);
		WorkbenchPartReference ref = (WorkbenchPartReference) viewRef;
		
		// report error
		if(ref == null) {
		    String key = ViewFactory.getKey(primaryId, secondaryId);
			result.add(new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, 0,
                        WorkbenchMessages.format("Perspective.couldNotFind", //$NON-NLS-1$
                                new String[] { key }),
                        null));
			continue;
		}
		if(ref.getPane() == null) {
			ViewPane vp = new ViewPane(viewRef,page);
			ref.setPane(vp);
		}
		page.addPart(ref);
		boolean willPartBeVisible = pres.willPartBeVisible(ref.getId(), secondaryId);
		if(willPartBeVisible) {
			IStatus restoreStatus = viewFactory.restoreView(viewRef);
			result.add(restoreStatus);
			if(restoreStatus.getSeverity() == IStatus.OK) {
				IViewPart view = (IViewPart)ref.getPart(true);
				if(view != null) {
					ViewSite site = (ViewSite)view.getSite();
					ViewPane pane = (ViewPane)site.getPane();			
					pres.replacePlaceholderWithPart(pane);
				}
			} else {
				page.removePart(ref);
			}
		} else {
			pres.replacePlaceholderWithPart(ref.getPane());			
		}
		
		restoreViewLayoutRec(childMem, viewRef);
	}

	// Load the fast views
	IMemento fastViewsMem = memento.getChild(IWorkbenchConstants.TAG_FAST_VIEWS);
	if(fastViewsMem != null) {
		views = fastViewsMem.getChildren(IWorkbenchConstants.TAG_VIEW);
		for (int x = 0; x < views.length; x ++) {
			// Get the view details.
			IMemento childMem = views[x];
			String viewID = childMem.getString(IWorkbenchConstants.TAG_ID);
			String secondaryId = childMem.getString(IWorkbenchConstants.TAG_SECONDARY_ID);
				
			IViewReference viewRef = viewFactory.getView(viewID, secondaryId);
			WorkbenchPartReference ref = (WorkbenchPartReference) viewRef;
			if(ref == null) {
				String key = ViewFactory.getKey(viewID, secondaryId);
				WorkbenchPlugin.log("Could not create view: '" + key + "'."); //$NON-NLS-1$ //$NON-NLS-2$
				result.add(new Status(
					IStatus.ERROR,PlatformUI.PLUGIN_ID,0,
					WorkbenchMessages.format("Perspective.couldNotFind", new String[]{key}), //$NON-NLS-1$
					null));
				continue;
			}
			
			// Restore fast view width ratio
			Float ratio = childMem.getFloat(IWorkbenchConstants.TAG_RATIO);
			if (ratio == null) {
				Integer viewWidth = childMem.getInteger(IWorkbenchConstants.TAG_WIDTH);
				if (viewWidth == null)
					ratio = new Float(IPageLayout.DEFAULT_FASTVIEW_RATIO);
				else
					ratio = new Float((float)viewWidth.intValue() / (float)getClientComposite().getSize().x);
			}
			ViewLayoutRec rec = getViewLayoutRec(viewRef, true);
			rec.fastViewWidthRatio = ratio.floatValue();

			// Add to fast view list because creating a view pane
			// will come back to check if its a fast view. We really
			// need to clean up this code.		
			fastViews.add(ref);
			if(ref.getPane() == null) {
				ref.setPane(new ViewPane(viewRef,page));
			}
			page.addPart(ref);

			restoreViewLayoutRec(childMem, viewRef);
		}
	}
		
	// Load the action sets.
	IMemento [] actions = memento.getChildren(IWorkbenchConstants.TAG_ACTION_SET);
	ArrayList actionsArray = new ArrayList(actions.length);
	for (int x = 0; x < actions.length; x ++) {
		String actionSetID = actions[x].getString(IWorkbenchConstants.TAG_ID);
		actionsArray.add(actionSetID);
	}
	createInitialActionSets(actionsArray);

	// Load the always on action sets.
	actions = memento.getChildren(IWorkbenchConstants.TAG_ALWAYS_ON_ACTION_SET);
	for (int x = 0; x < actions.length; x ++) {
		String actionSetID = actions[x].getString(IWorkbenchConstants.TAG_ID);
		IActionSetDescriptor d = 
			WorkbenchPlugin.getDefault().getActionSetRegistry().findActionSet(actionSetID);
		if (d != null) 
			alwaysOnActionSets.add(d);	
	}

	// Load the always off action sets.
	actions = memento.getChildren(IWorkbenchConstants.TAG_ALWAYS_OFF_ACTION_SET);
	for (int x = 0; x < actions.length; x ++) {
		String actionSetID = actions[x].getString(IWorkbenchConstants.TAG_ID);
		IActionSetDescriptor d = 
			WorkbenchPlugin.getDefault().getActionSetRegistry().findActionSet(actionSetID);
		if (d != null) 
			alwaysOffActionSets.add(d);	
	}

	// Load "show view actions".
	actions = memento.getChildren(IWorkbenchConstants.TAG_SHOW_VIEW_ACTION);
	showViewActionIds = new ArrayList(actions.length);
	for (int x = 0; x < actions.length; x ++) {
		String id = actions[x].getString(IWorkbenchConstants.TAG_ID);
		showViewActionIds.add(id);
	}
	
	// Load "show in times".
	actions = memento.getChildren(IWorkbenchConstants.TAG_SHOW_IN_TIME);
	for (int x = 0; x < actions.length; x ++) {
		String id = actions[x].getString(IWorkbenchConstants.TAG_ID);
		String timeStr = actions[x].getString(IWorkbenchConstants.TAG_TIME);
		if (id != null && timeStr != null) {
			try {
				long time = Long.parseLong(timeStr);
				showInTimes.put(id, new Long(time));
			}
			catch (NumberFormatException e) {
				// skip this one
			}
		}
	}
	
	// Load "show in parts" from registry, not memento
	showInPartIds = getShowInIdsFromRegistry();
	
	// Load "new wizard actions".
	actions = memento.getChildren(IWorkbenchConstants.TAG_NEW_WIZARD_ACTION);
	newWizardActionIds = new ArrayList(actions.length);
	for (int x = 0; x < actions.length; x ++) {
		String id = actions[x].getString(IWorkbenchConstants.TAG_ID);
		newWizardActionIds.add(id);
	}
	
	// Load "perspective actions".
	actions = memento.getChildren(IWorkbenchConstants.TAG_PERSPECTIVE_ACTION);
	perspectiveActionIds = new ArrayList(actions.length);
	for (int x = 0; x < actions.length; x ++) {
		String id = actions[x].getString(IWorkbenchConstants.TAG_ID);
		perspectiveActionIds.add(id);
	}
	
	// Save presentation.	
	presentation = pres;

	// Hide the editor area if needed. Need to wait for the
	// presentation to be fully setup first.
	Integer areaVisible = memento.getInteger(IWorkbenchConstants.TAG_AREA_VISIBLE);
	// Rather than hiding the editors now we must wait until after their controls
	// are created. This ensures that if an editor is instantiated, createPartControl
	// is also called. See bug 20166.
	shouldHideEditorsOnActivate = (areaVisible != null && areaVisible.intValue() == 0);
	
	// restore the fixed state
	Integer isFixed = memento.getInteger(IWorkbenchConstants.TAG_FIXED);
	fixed = (isFixed != null && isFixed.intValue() == 1);
	
	return result;
}

/**
 * Restores the layout rec for the given view.
 */
private void restoreViewLayoutRec(IMemento childMem, IViewReference viewRef) {
    ViewLayoutRec rec = getViewLayoutRec(viewRef, true);
    if (IWorkbenchConstants.FALSE.equals(childMem.getString(IWorkbenchConstants.TAG_CLOSEABLE)))
        rec.isCloseable = false;
    if (IWorkbenchConstants.FALSE.equals(childMem.getString(IWorkbenchConstants.TAG_MOVEABLE)))
        rec.isMoveable = false;
    if (IWorkbenchConstants.TRUE.equals(childMem.getString(IWorkbenchConstants.TAG_STANDALONE)))
        rec.isStandalone = true;
    if (IWorkbenchConstants.FALSE.equals(childMem.getString(IWorkbenchConstants.TAG_SHOW_TITLE)))
        rec.showTitle = false;
}
/**
 * Returns the Show In... part ids read from the registry.  
 */
private ArrayList getShowInIdsFromRegistry() {
	PerspectiveExtensionReader reader = new PerspectiveExtensionReader();
	reader.setIncludeOnlyTags(new String[] { PerspectiveExtensionReader.TAG_SHOW_IN_PART });
	PageLayout layout = new PageLayout();
	reader.extendLayout(descriptor.getOriginalId(), layout);
	return layout.getShowInPartIds();
}

/**
 * Save the layout.
 */
public void saveDesc() {
	saveDescAs(descriptor);
}
/**
 * Save the layout.
 */
public void saveDescAs(IPerspectiveDescriptor desc) {		
	PerspectiveDescriptor realDesc = (PerspectiveDescriptor)desc;
	//get the layout from the registry	
	PerspectiveRegistry perspRegistry = (PerspectiveRegistry) WorkbenchPlugin.getDefault().getPerspectiveRegistry();
	// Capture the layout state.	
	XMLMemento memento = XMLMemento.createWriteRoot("perspective");//$NON-NLS-1$
	IStatus status = saveState(memento, realDesc, false);
	if(status.getSeverity() == IStatus.ERROR) {
		ErrorDialog.openError((Shell)null, 
			WorkbenchMessages.getString("Perspective.problemSavingTitle"),  //$NON-NLS-1$
			WorkbenchMessages.getString("Perspective.problemSavingMessage"), //$NON-NLS-1$
			status);
		return;
	}
	//save it to the preference store
	try {
		perspRegistry.saveCustomPersp(realDesc, memento);
		descriptor = realDesc;
	} catch (IOException e) {
		perspRegistry.deletePerspective(realDesc);
		MessageDialog.openError((Shell)null, 
			WorkbenchMessages.getString("Perspective.problemSavingTitle"),  //$NON-NLS-1$
			WorkbenchMessages.getString("Perspective.problemSavingMessage")); //$NON-NLS-1$
	}
}
/**
 * Save the layout.
 */
public IStatus saveState(IMemento memento) {
	MultiStatus result = new MultiStatus(
		PlatformUI.PLUGIN_ID,IStatus.OK,
		WorkbenchMessages.getString("Perspective.problemsSavingPerspective"),null); //$NON-NLS-1$
		
	result.merge(saveState(memento, descriptor, true));

	return result;
}
/**
 * Save the layout.
 */
private IStatus saveState(IMemento memento, PerspectiveDescriptor p,
	boolean saveInnerViewState)
{
	MultiStatus result = new MultiStatus(
		PlatformUI.PLUGIN_ID,IStatus.OK,
		WorkbenchMessages.getString("Perspective.problemsSavingPerspective"),null); //$NON-NLS-1$

	if(this.memento != null) {
		memento.putMemento(this.memento);
		return result;
	}
			
	// Save the version number.
	memento.putString(IWorkbenchConstants.TAG_VERSION, VERSION_STRING);
	result.add(p.saveState(memento));
	if(!saveInnerViewState) {
		Rectangle bounds = page.getWorkbenchWindow().getShell().getBounds();
		IMemento boundsMem = memento.createChild(IWorkbenchConstants.TAG_WINDOW);
		boundsMem.putInteger(IWorkbenchConstants.TAG_X,bounds.x);
		boundsMem.putInteger(IWorkbenchConstants.TAG_Y,bounds.y);
		boundsMem.putInteger(IWorkbenchConstants.TAG_HEIGHT,bounds.height);
		boundsMem.putInteger(IWorkbenchConstants.TAG_WIDTH,bounds.width);
	}
	
	// Save the visible action sets.
	Iterator enum = visibleActionSets.iterator();
	while (enum.hasNext()) {
		IActionSetDescriptor desc = (IActionSetDescriptor)enum.next();
		IMemento child = memento.createChild(IWorkbenchConstants.TAG_ACTION_SET);
		child.putString(IWorkbenchConstants.TAG_ID, desc.getId());
	}

	// Save the "always on" action sets.
	enum = alwaysOnActionSets.iterator();
	while (enum.hasNext()) {
		IActionSetDescriptor desc = (IActionSetDescriptor)enum.next();
		IMemento child = memento.createChild(IWorkbenchConstants.TAG_ALWAYS_ON_ACTION_SET);
		child.putString(IWorkbenchConstants.TAG_ID, desc.getId());
	}

	// Save the "always off" action sets.
	enum = alwaysOffActionSets.iterator();
	while (enum.hasNext()) {
		IActionSetDescriptor desc = (IActionSetDescriptor)enum.next();
		IMemento child = memento.createChild(IWorkbenchConstants.TAG_ALWAYS_OFF_ACTION_SET);
		child.putString(IWorkbenchConstants.TAG_ID, desc.getId());
	}

	// Save "show view actions"
	enum = showViewActionIds.iterator();
	while (enum.hasNext()) {
		String str = (String)enum.next();
		IMemento child = memento.createChild(IWorkbenchConstants.TAG_SHOW_VIEW_ACTION);
		child.putString(IWorkbenchConstants.TAG_ID, str);
	}

	// Save "show in times"
	enum = showInTimes.keySet().iterator();
	while (enum.hasNext()) {
		String id = (String) enum.next();
		Long time = (Long) showInTimes.get(id);
		IMemento child = memento.createChild(IWorkbenchConstants.TAG_SHOW_IN_TIME);
		child.putString(IWorkbenchConstants.TAG_ID, id);
		child.putString(IWorkbenchConstants.TAG_TIME, time.toString());
	}

	// Save "new wizard actions".
	enum = newWizardActionIds.iterator();
	while (enum.hasNext()) {
		String str = (String)enum.next();
		IMemento child = memento.createChild(IWorkbenchConstants.TAG_NEW_WIZARD_ACTION);
		child.putString(IWorkbenchConstants.TAG_ID, str);
	}
	
	// Save "perspective actions".
	enum = perspectiveActionIds.iterator();
	while (enum.hasNext()) {
		String str = (String)enum.next();
		IMemento child = memento.createChild(IWorkbenchConstants.TAG_PERSPECTIVE_ACTION);
		child.putString(IWorkbenchConstants.TAG_ID, str);
	}
	
	// Get visible views.
	List viewPanes = new ArrayList(5);
	presentation.collectViewPanes(viewPanes);

	// Save the views.
	enum = viewPanes.iterator();
	int errors = 0;
	while (enum.hasNext()) {
		ViewPane pane = (ViewPane)enum.next();
		IViewReference ref = pane.getViewReference();
		IMemento viewMemento = memento.createChild(IWorkbenchConstants.TAG_VIEW);
		viewMemento.putString(IWorkbenchConstants.TAG_ID, ref.getId());
		if (ref.getSecondaryId() != null) {
			viewMemento.putString(IWorkbenchConstants.TAG_SECONDARY_ID, ref.getSecondaryId());
		}
		saveViewLayoutRec(ref, viewMemento);
	}

	if(fastViews.size() > 0) {
		IMemento childMem = memento.createChild(IWorkbenchConstants.TAG_FAST_VIEWS);
		enum = fastViews.iterator();
		while (enum.hasNext()) {
			IViewReference ref = (IViewReference)enum.next();
			IMemento viewMemento = childMem.createChild(IWorkbenchConstants.TAG_VIEW);
			String id = ref.getId();
			viewMemento.putString(IWorkbenchConstants.TAG_ID, id);
			float ratio = getFastViewWidthRatio(id);
			viewMemento.putFloat(IWorkbenchConstants.TAG_RATIO, ratio);
			saveViewLayoutRec(ref, viewMemento);
		}
	}
	if(errors > 0) {
		String message = WorkbenchMessages.getString("Perspective.multipleErrors"); //$NON-NLS-1$
		if(errors == 1)
			message = WorkbenchMessages.getString("Perspective.oneError"); //$NON-NLS-1$
		MessageDialog.openError(null, WorkbenchMessages.getString("Error"), message); //$NON-NLS-1$
	}
	
	// Save the layout.
	IMemento childMem = memento.createChild(IWorkbenchConstants.TAG_LAYOUT);
	result.add(presentation.saveState(childMem));

	// Save the editor visibility state
	if (isEditorAreaVisible())
		memento.putInteger(IWorkbenchConstants.TAG_AREA_VISIBLE, 1);
	else
		memento.putInteger(IWorkbenchConstants.TAG_AREA_VISIBLE, 0);
	
	// Save the fixed state
	if (fixed)
		memento.putInteger(IWorkbenchConstants.TAG_FIXED, 1);
	else
		memento.putInteger(IWorkbenchConstants.TAG_FIXED, 0);
	
	return result;
}
/**
 * @param ref
 * @param viewMemento
 */
private void saveViewLayoutRec(IViewReference ref, IMemento viewMemento) {
    ViewLayoutRec rec = getViewLayoutRec(ref, false);
    if (rec != null) {
        if (!rec.isCloseable) {
            viewMemento.putString(IWorkbenchConstants.TAG_CLOSEABLE, IWorkbenchConstants.FALSE);
        }
        if (!rec.isMoveable) {
            viewMemento.putString(IWorkbenchConstants.TAG_MOVEABLE, IWorkbenchConstants.FALSE);
        }
        if (rec.isStandalone) {
            viewMemento.putString(IWorkbenchConstants.TAG_STANDALONE, IWorkbenchConstants.TRUE);
   	        viewMemento.putString(IWorkbenchConstants.TAG_SHOW_TITLE, Boolean.toString(rec.showTitle));
        }
    }
}
/**
 * Sets the visible action sets. 
 * Note: The page is expected to update action bars.
 */
public void setActionSets(IActionSetDescriptor[] newArray) {
	// We assume that changes to action set visibilty should be remembered
	// and not reversed as parts are activated.
	ArrayList turnedOff = (ArrayList)visibleActionSets.clone();
	for (int i = 0; i < newArray.length; i++) {
		IActionSetDescriptor desc = newArray[i];
		turnedOff.remove(desc);
		if (!visibleActionSets.contains(desc)) {
			// make sure this always stays visible
			alwaysOnActionSets.add(desc);
			alwaysOffActionSets.remove(desc);
		}
	}
	for (int i = 0; i < turnedOff.size(); i++) {
		IActionSetDescriptor desc = (IActionSetDescriptor)turnedOff.get(i);
		// make sure this always stays hidden
		alwaysOnActionSets.remove(desc);
		alwaysOffActionSets.add(desc);
	}
	
	visibleActionSets.clear();
	int newSize = newArray.length;
	for (int i = 0; i < newSize; i ++) {
		visibleActionSets.add(newArray[i]);
	}
}
/**
 * Return the active fast view or null if there are no
 * fast views or if there are all minimized.
 */
public IViewReference getActiveFastView() {
	return activeFastView;
}
/**
 * Sets the active fast view. If a different fast view is already open,
 * it shrinks equally <code>steps</code> times before disappearing
 * completely. Then, <code>view</code> becomes active and is shown.
 */
/*package*/ void setActiveFastView(IViewReference ref, int steps) {
	if (activeFastView == ref)
		return;
		
	if (activeFastView != null) {
		ViewPane pane = getPane(activeFastView);
		if (pane != null) {
			if (pane.isZoomed()) {
				presentation.zoomOut();
			}
			hideFastView(activeFastView, steps);
		}
	}
	activeFastView = ref;
	try {
		if (activeFastView != null) {
			if (!showFastView(activeFastView)) {
			    activeFastView = null;
			}
		}
	}
	catch (RuntimeException e) {
	    activeFastView = null;
	}
}
/**
 * Sets the active fast view.
 */
/*package*/ void setActiveFastView(IViewReference ref) {
	setActiveFastView(ref, FASTVIEW_HIDE_STEPS);
}
/**
 * Sets the visibility of all fast view pins.
 */
private void setAllPinsVisible(boolean visible) {
	Iterator iter = fastViews.iterator();
	while (iter.hasNext()) {
		ViewPane pane = getPane((IViewReference)iter.next());
		if(pane != null)
			pane.setFast(visible);
	}
}
/**
 * Sets the selection for the shortcut bar icon representing the givevn fast view.
 */
private void setFastViewIconSelection(IViewReference ref, boolean selected) {
	WorkbenchWindow window = (WorkbenchWindow) page.getWorkbenchWindow();
	FastViewBar bar = window.getFastViewBar();
	if (bar != null) {
		if (selected) {
			bar.setSelection(ref);
		} else {
			if (ref == bar.getSelection()) {
				bar.setSelection(null);
			}
		}
	}
}
/**
 * Sets the new wizard actions for the page.
 * This is List of Strings.
 */
public void setNewWizardActionIds(ArrayList newList ) {
	newWizardActionIds = newList;
}
/**
 * Sets the perspective actions for this page.
 * This is List of Strings.
 */
public void setPerspectiveActionIds(ArrayList list) {
	perspectiveActionIds = list;
}
/**
 * Sets the ids of the parts to list in the Show In... prompter.
 * This is a List of Strings.
 */
public void setShowInPartIds(ArrayList list) {
	showInPartIds = list;
}
/**
 * Sets the ids of the views to list in the Show View shortcuts.
 * This is a List of Strings.
 */
public void setShowViewActionIds(ArrayList list) {
	showViewActionIds = list;
}
/**
 * @see IWorkbenchPage
 * Note: The page is expected to update action bars.
 */
public void showActionSet(String id) {
	ActionSetRegistry reg = WorkbenchPlugin.getDefault().getActionSetRegistry();
	IActionSetDescriptor desc = reg.findActionSet(id);
	if (alwaysOffActionSets.contains(desc))
		return;
	if (desc != null && !visibleActionSets.contains(desc))
		visibleActionSets.add(desc);
}
/**
 * Show the editor area if not visible
 */
protected void showEditorArea() {
	if (isEditorAreaVisible())
		return;

	// Enable the editor area now that it will be made
	// visible and can accept keyboard focus again.
	if (editorArea.getControl() != null)
		editorArea.getControl().setEnabled(true);

	setEditorAreaVisible(true);

	// Replace the part holder with the editor area.
	presentation.getLayout().replace(editorHolder, editorArea);
	editorHolder = null;
}

private void setEditorAreaVisible(boolean visible) {
	ArrayList workbooks = ((EditorArea)editorArea).getEditorWorkbooks();
	for (Iterator iter = workbooks.iterator(); iter.hasNext();) {
		EditorWorkbook workbook = (EditorWorkbook) iter.next();
		workbook.setVisible(visible);
		EditorPane pane = workbook.getVisibleEditor();
		if(pane != null)
			pane.setVisible(visible);
	}
	editorArea.setVisible(visible);
}
/**
 * Shows a fast view.
 * @return whether the view was successfully shown
 */
boolean showFastView(IViewReference ref) {
	// Make sure the part is restored.
	if(ref.getPart(true) == null)
		return false;
	
	ViewPane pane = getPane(ref);	
	if (pane == null)
		return false;

	saveFastViewWidthRatio();
	
	WorkbenchWindow window = (WorkbenchWindow) page.getWorkbenchWindow();
	FastViewBar bar = window.getFastViewBar();
	if (bar == null) {
	    return false;
	}
	int side = bar.getViewSide(ref);
	
	fastViewPane.showView(getClientComposite(), pane, side, getFastViewWidthRatio(ref.getId()));	
	
	setFastViewIconSelection(ref, true);
	
	return true;
}

private void saveFastViewWidthRatio() {
	ViewPane pane = fastViewPane.getCurrentPane();
	if (pane != null) {
	    ViewLayoutRec rec = getViewLayoutRec(pane.getViewReference(), true);
	    rec.fastViewWidthRatio = fastViewPane.getCurrentRatio();
	}
}

/**
 * Shows the view with the given id and secondary id.
 */
public IViewPart showView(String viewId, String secondaryId) 
	throws PartInitException 
{
	ViewFactory factory = getViewFactory();
	IViewReference ref = factory.createView(viewId, secondaryId);
	IViewPart part = (IViewPart)ref.getPart(false);
	if(part == null) {
		IStatus status = factory.restoreView(ref);
		if(status.getSeverity() == IStatus.ERROR) {
			if(status.getException() instanceof PartInitException)
				throw (PartInitException)status.getException();
			else
				throw new PartInitException(status);
		} else { //No error so the part has been created
			part = (IViewPart)ref.getPart(false);
		}
	}
	ViewSite site = (ViewSite)part.getSite();
	ViewPane pane = (ViewPane)site.getPane();
	
	IPreferenceStore store = WorkbenchPlugin.getDefault().getPreferenceStore();
	int openViewMode = store.getInt(IPreferenceConstants.OPEN_VIEW_MODE);
	if (presentation.hasPlaceholder(viewId, secondaryId)) {
		presentation.addPart(pane);
	} else if (openViewMode == IPreferenceConstants.OVM_EMBED) {
		presentation.addPart(pane);
	
	} else if (openViewMode == IPreferenceConstants.OVM_FLOAT && presentation.canDetach()) {
		presentation.addDetachedPart(pane);
	} else {
		showFastView(ref);
		addFastView(ref);
		//Refresh the part as there might have been an error when showing
	}
	return part;
}
/**
 * Toggles the visibility of a fast view.  If the view is active it
 * is deactivated.  Otherwise, it is activated.
 */
public void toggleFastView(IViewReference ref) {
	if (ref == activeFastView) {
		setActiveFastView(null);
	} else {
		setActiveFastView(ref);
	}
}
/**
 * Returns the oldPartID.
 * @return String
 */
public String getOldPartID() {
	return oldPartID;
}

/**
 * Sets the oldPartID.
 * @param oldPartID The oldPartID to set
 */
public void setOldPartID(String oldPartID) {
	this.oldPartID = oldPartID;
}
/**
 * Method moveFastView.  Moves draggedView to the position above
 * destinationView.  If placeAtEnd is true, add view to the end. Otherwise,
 * either place above destination view, or at the beginning if the
 * destinationView is null
 * @param draggedView
 * @param destinationView
 * @param useDestination
 */

/*package*/ void moveFastView(IViewReference draggedView, IViewReference destinationView) {
	//PR 6988
	
	//do nothing if views are the same
	if (draggedView == destinationView)
		return;
		
	int insertIdx = fastViews.indexOf(destinationView);
	
	//move the view
	fastViews.remove(draggedView);
	
	if (insertIdx < 0 || insertIdx >= fastViews.size()) {
		fastViews.add(draggedView);
	} else {
		fastViews.add(insertIdx, draggedView);
	}	
}

//for dynamic UI
/* package */ void addActionSet(IActionSetDescriptor newDesc) {
	for (int i = 0; i < visibleActionSets.size(); i++) {
		IActionSetDescriptor desc = (IActionSetDescriptor)visibleActionSets.get(i);
		if (desc.getId().equals(newDesc.getId())) {
			visibleActionSets.remove(desc);
			alwaysOnActionSets.remove(desc);
			alwaysOffActionSets.remove(desc);
			break;
		}
	}
	visibleActionSets.add(newDesc);
	alwaysOnActionSets.add(newDesc);
}

//for dynamic UI
/* package */ void removeActionSet(String id) {
	for (int i = 0; i < visibleActionSets.size(); i++) {
		IActionSetDescriptor desc = (IActionSetDescriptor)visibleActionSets.get(i);
		if (desc.getId().equals(id)) {
			visibleActionSets.remove(desc);
			alwaysOnActionSets.remove(desc);
			alwaysOffActionSets.remove(desc);
			break;
		}
	}
	
}

public void toggleFastViewZoom() {
	fastViewPane.toggleZoom();
}

/**
 * Returns whether the given view is closeable in this perspective.
 * 
 * @since 3.0
 */
public boolean isCloseable(IViewReference reference) {
    ViewLayoutRec rec = getViewLayoutRec(reference, false);
    if (rec != null)
        return rec.isCloseable;
    return true;
}

/**
 * Returns whether the given view is moveable in this perspective.
 * 
 * @since 3.0
 */
public boolean isMoveable(IViewReference reference) {
    ViewLayoutRec rec = getViewLayoutRec(reference, false);
    if (rec != null)
        return rec.isMoveable;
    return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1428.java