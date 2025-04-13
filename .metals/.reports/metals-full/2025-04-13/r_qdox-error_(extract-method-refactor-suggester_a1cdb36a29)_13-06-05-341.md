error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8151.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8151.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8151.java
text:
```scala
r@@estoreView(this);

package org.eclipse.ui.internal;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.*;
import org.eclipse.ui.internal.registry.IViewDescriptor;
import org.eclipse.ui.internal.registry.IViewRegistry;

/**
 * The ViewFactory is used to control the creation and disposal of views.  
 * It implements a reference counting strategy so that one view can be shared
 * by more than one client.
 */
public class ViewFactory
{
	static boolean DEBUG = false;
	private WorkbenchPage page;
	private IViewRegistry viewReg;
	private ReferenceCounter counter;
	private HashMap mementoTable = new HashMap();

/**
 * ViewManager constructor comment.
 */
public ViewFactory(WorkbenchPage page, IViewRegistry reg) {
	super();
	this.page = page;
	this.viewReg = reg;
	counter = new ReferenceCounter();
}
/**
 * Creates an instance of a view defined by id.
 * 
 * This factory implements reference counting.  The first call to this
 * method will return a new view.  Subsequent calls will return the
 * first view with an additional reference count.  The view is
 * disposed when releaseView is called an equal number of times
 * to getView.
 */
public IViewReference createView(final String id) throws PartInitException {
	IViewDescriptor desc = viewReg.find(id);
	if(desc == null)
		throw new PartInitException(WorkbenchMessages.format("ViewFactory.couldNotCreate", new Object[] {id})); //$NON-NLS-1$
	IViewReference ref = (IViewReference)counter.get(desc);
	if (ref == null) {
		ref = new ViewReference(id);
		counter.put(desc,ref);
	} else {
		counter.addRef(desc);
	}
	return ref;
}
/**
 * Creates an instance of a view defined by id.
 * 
 * This factory implements reference counting.  The first call to this
 * method will return a new view.  Subsequent calls will return the
 * first view with an additional reference count.  The view is
 * disposed when releaseView is called an equal number of times
 * to getView.
 */
public IStatus restoreView(final IViewReference ref) {
	if(ref.getPart(false) != null)
		return new Status(IStatus.OK,PlatformUI.PLUGIN_ID,0,"",null);
		
	final String viewID = ref.getId();
	final IMemento stateMem = (IMemento)mementoTable.get(viewID);
	mementoTable.remove(viewID);
	
	final IStatus result[] = new IStatus[]{new Status(IStatus.OK,PlatformUI.PLUGIN_ID,0,"",null)};
	Platform.run(new SafeRunnable() {
		public void run() {
			IViewDescriptor desc = viewReg.find(viewID);
			if(desc == null) {
				result[0] = new Status(
					IStatus.ERROR,PlatformUI.PLUGIN_ID,0,
					WorkbenchMessages.format("ViewFactory.couldNotCreate", new Object[] {viewID}), //$NON-NLS-1$
					null);
				return;
			}

			// Debugging
			if (DEBUG)
				System.out.println("Create " + desc.getLabel());//$NON-NLS-1$
		
			// Create the view.
			IViewPart view = null;
			try {
				view = desc.createView();
				((ViewReference)ref).setPart(view);
			} catch (CoreException e) {
				result[0] = new Status(
					IStatus.ERROR,PlatformUI.PLUGIN_ID,0,
					WorkbenchMessages.format("ViewFactory.initException", new Object[] {desc.getID()}), //$NON-NLS-1$
					e);
				return;
			}
			
			// Create site
			ViewSite site = new ViewSite(view, page, desc);
			try {
				view.init(site,stateMem);
			} catch (PartInitException e) {
				releaseView(viewID);
				result[0] = new Status(
					IStatus.ERROR,PlatformUI.PLUGIN_ID,0,
					WorkbenchMessages.format("Perspective.exceptionRestoringView",new String[]{viewID}), //$NON-NLS-1$
					e);
				return;
			}				
			if (view.getSite() != site)  {
				releaseView(viewID);
				result[0] = new Status(
					IStatus.ERROR,PlatformUI.PLUGIN_ID,0,
					WorkbenchMessages.format("ViewFactory.siteException", new Object[] {desc.getID()}), //$NON-NLS-1$
					null);
				return;
			}
		
			ViewPane pane = new ViewPane(ref, page);
			site.setPane(pane);
			site.setActionBars(new ViewActionBars(page.getActionBars(), pane));
			result[0] =  new Status(IStatus.OK,PlatformUI.PLUGIN_ID,0,"",null);
		}
		public void handleException(Throwable e) {
			//Execption is already logged.
			result[0] = new Status(
				IStatus.ERROR,PlatformUI.PLUGIN_ID,0,
				WorkbenchMessages.format("Perspective.exceptionRestoringView",new String[]{viewID}), //$NON-NLS-1$
				e);

		}
	});
	return result[0];
}

public IStatus saveState(IMemento memento) {
	final MultiStatus result = new MultiStatus(
		PlatformUI.PLUGIN_ID,IStatus.OK,
		WorkbenchMessages.getString("ViewFactory.problemsSavingViews"),null);
	
	final IViewReference refs[] = getViews();
	for (int i = 0; i < refs.length; i++) {
		final IMemento viewMemento = memento.createChild(IWorkbenchConstants.TAG_VIEW);
		viewMemento.putString(IWorkbenchConstants.TAG_ID, refs[i].getId());
		final IViewPart view = (IViewPart)refs[i].getPart(false);
		if(view != null) {
			final int index = i;
			Platform.run(new SafeRunnable() {
				public void run() {
					view.saveState(viewMemento.createChild(IWorkbenchConstants.TAG_VIEW_STATE));
				}
				public void handleException(Throwable e) {
					result.add(new Status(
						IStatus.ERROR,PlatformUI.PLUGIN_ID,0,
						WorkbenchMessages.format("ViewFactory.couldNotSave",new String[]{refs[index].getTitle()}),
						e));
				}
			});
		} else {
			IMemento mem = (IMemento)mementoTable.get(refs[i].getId());
			if(mem != null) {
				IMemento child = viewMemento.createChild(IWorkbenchConstants.TAG_VIEW_STATE);
				child.putMemento(mem);
			}
		}
	}
	return result;
}
public IStatus restoreState(IMemento memento) {
	IMemento mem[] = memento.getChildren(IWorkbenchConstants.TAG_VIEW);
	for (int i = 0; i < mem.length; i++) {
		String id = mem[i].getString(IWorkbenchConstants.TAG_ID);
		mementoTable.put(id,mem[i].getChild(IWorkbenchConstants.TAG_VIEW_STATE));
	}
	return new Status(IStatus.OK,PlatformUI.PLUGIN_ID,0,"",null);
}
/**
 * Remove a view rec from the manager.
 *
 * The IViewPart.dispose method must be called at a higher level.
 */
private void destroyView(IViewDescriptor desc, IViewPart view) 
{
	// Debugging
	if (DEBUG)
		System.out.println("Dispose " + desc.getLabel());//$NON-NLS-1$

	// Free action bars, pane, etc.
	PartSite site = (PartSite)view.getSite();
	ViewActionBars actionBars = (ViewActionBars)site.getActionBars();
	actionBars.dispose();
	PartPane pane = site.getPane();
	pane.dispose();

	// Free the site.
	site.dispose();
}
/**
 * Returns the view with the given id, or <code>null</code> if not found.
 */
public IViewReference getView(String id) {
	IViewDescriptor desc = viewReg.find(id);
	return (IViewReference) counter.get(desc);
}
/**
 * Returns a list of views which are open.
 */
public IViewReference[] getViews() {
	List list = counter.values();
	IViewReference [] array = new IViewReference[list.size()];
	for (int i = 0; i < array.length; i++) {
		array[i] = (IViewReference)list.get(i);
	}
	return array;
}
/**
 * Returns whether a view with the given id exists.
 */
public boolean hasView(String id) {
	IViewDescriptor desc = viewReg.find(id);
	Object view = counter.get(desc);
	return (view != null);
}
/**
 * Releases an instance of a view defined by id.
 *
 * This factory does reference counting.  For more info see
 * getView.
 */
public void releaseView(String id) {
	IViewDescriptor desc = viewReg.find(id);
	IViewReference ref = (IViewReference)counter.get(desc);
	if (ref == null)
		return;
	int count = counter.removeRef(desc);
	if (count <= 0) {
		IViewPart view = (IViewPart)ref.getPart(false);
		if(view != null)
			destroyView(desc, view);
	}
}

private class ViewReference extends WorkbenchPartReference implements IViewReference {

	private IViewPart part;
	private String viewId;

	public ViewReference(String id) {
		viewId = id;
	}
	/**
	 * @see IViewReference#isFastView()
	 */
	public boolean isFastView() {
		return ((WorkbenchPage)part.getSite().getPage()).isFastView(part);
	}
	public IViewPart getView(boolean restore) {
		return (IViewPart)getPart(restore);
	}
	public void setPart(IWorkbenchPart part) {
		super.setPart(part);
		this.part = (IViewPart)part;
	}
	public String getRegisteredName() {
		if(part != null)
			return part.getSite().getRegisteredName();
			
		IViewRegistry reg = WorkbenchPlugin.getDefault().getViewRegistry();
		IViewDescriptor desc = reg.find(viewId);
		if(desc != null)
			return desc.getLabel();	
		return getTitle();
	}
	
	/**
	 * @see IWorkbenchPartReference#getPart(boolean)
	 */
	public IWorkbenchPart getPart(boolean restore) {
		if(part != null)
			return part;
		if(restore) {
			IStatus status = restoreView(this);
		/*
		 * Views are not lazy created so this code will not run for now.
		 */
			
//			if(status.getSeverity() == IStatus.ERROR) {
//				Workbench workbench = (Workbench)PlatformUI.getWorkbench();
//				if(!workbench.isStarting()) {
//					ErrorDialog.openError(
//						window.getShell(),
//						WorkbenchMessages.getString("EditorManager.unableToRestoreEditorTitle"), //$NON-NLS-1$
//						WorkbenchMessages.format("EditorManager.unableToRestoreEditorMessage",new String[]{getName()}), //$NON-NLS-1$
//						status,
//						IStatus.WARNING | IStatus.ERROR);
//				} 
//			}			
		}
		return part;
	}
	/**
	 * @see IWorkbenchPartReference#getTitle()
	 */
	public String getTitle() {
		return part.getTitle();
	}
	/**
	 * @see IWorkbenchPartReference#getTitleImage()
	 */
	public Image getTitleImage() {
		return part.getTitleImage();
	}
	/**
	 * @see IWorkbenchPartReference#getId()
	 */	
	public String getId() {
		if(part != null) {
			if(part.getSite() == null)
				return viewId;
			return part.getSite().getId();
		}
		return viewId;
	}
	public void setPane(PartPane pane) {
		((PartSite)part.getSite()).setPane(pane);
	}
	public PartPane getPane() {
		return ((PartSite)part.getSite()).getPane();
	}
	public String getTitleToolTip() {
		return part.getTitleToolTip();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8151.java