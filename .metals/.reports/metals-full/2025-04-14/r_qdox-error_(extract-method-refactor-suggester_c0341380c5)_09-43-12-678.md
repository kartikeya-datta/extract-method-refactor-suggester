error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2729.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2729.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2729.java
text:
```scala
protected final S@@erviceLocator serviceLocator;

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Dan Rubel (dan_rubel@instantiations.com) - accessor to get context menu ids
 *******************************************************************************/
package org.eclipse.ui.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.expressions.Expression;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.SubActionBars;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.commands.SlaveCommandService;
import org.eclipse.ui.internal.contexts.SlaveContextService;
import org.eclipse.ui.internal.expressions.ActivePartExpression;
import org.eclipse.ui.internal.handlers.SlaveHandlerService;
import org.eclipse.ui.internal.progress.WorkbenchSiteProgressService;
import org.eclipse.ui.internal.services.ServiceLocator;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.eclipse.ui.services.IServiceLocator;

/**
 * <code>PartSite</code> is the general implementation for an
 * <code>IWorkbenchPartSite</code>. A site maintains the context for a part,
 * including the part, its pane, active contributions, selection provider, etc.
 * Together, these components make up the complete behavior for a part as if it
 * was implemented by one person.
 * 
 * The <code>PartSite</code> lifecycle is as follows ..
 * 
 * <ol>
 * <li>a site is constructed </li>
 * <li>a part is constructed and stored in the part </li>
 * <li>the site calls part.init() </li>
 * <li>a pane is constructed and stored in the site </li>
 * <li>the action bars for a part are constructed and stored in the site </li>
 * <li>the pane is added to a presentation </li>
 * <li>the SWT widgets for the pane and part are created </li>
 * <li>the site is activated, causing the actions to become visible </li>
 * </ol>
 */
public abstract class PartSite implements IWorkbenchPartSite {

	/**
	 * This is a helper method for the register context menu functionality. It
	 * is provided so that different implementations of the
	 * <code>IWorkbenchPartSite</code> interface don't have to worry about how
	 * context menus should work.
	 * 
	 * @param menuId
	 *            the menu id
	 * @param menuManager
	 *            the menu manager
	 * @param selectionProvider
	 *            the selection provider
	 * @param includeEditorInput
	 *            whether editor inputs should be included in the structured
	 *            selection when calculating contributions
	 * @param part
	 *            the part for this site
	 * @param menuExtenders
	 *            the collection of menu extenders for this site
	 * @see IWorkbenchPartSite#registerContextMenu(MenuManager,
	 *      ISelectionProvider)
	 */
	public static final void registerContextMenu(final String menuId,
			final MenuManager menuManager,
			final ISelectionProvider selectionProvider,
			final boolean includeEditorInput, final IWorkbenchPart part,
			final Collection menuExtenders) {
		/*
		 * Check to see if the same menu manager and selection provider have
		 * already been used. If they have, then we can just add another menu
		 * identifier to the existing PopupMenuExtender.
		 */
		final Iterator extenderItr = menuExtenders.iterator();
		boolean foundMatch = false;
		while (extenderItr.hasNext()) {
			final PopupMenuExtender existingExtender = (PopupMenuExtender) extenderItr
					.next();
			if (existingExtender.matches(menuManager, selectionProvider, part)) {
				existingExtender.addMenuId(menuId);
				foundMatch = true;
				break;
			}
		}

		if (!foundMatch) {
			menuExtenders.add(new PopupMenuExtender(menuId, menuManager,
					selectionProvider, part, includeEditorInput));
		}
	}

	private IWorkbenchPartReference partReference;

	private IWorkbenchPart part;

	private IWorkbenchPage page;

	private String extensionID;

	private String pluginID;

	private String extensionName;

	private ISelectionProvider selectionProvider;

	private SubActionBars actionBars;

	private KeyBindingService keyBindingService;

	protected ArrayList menuExtenders;

	private WorkbenchSiteProgressService progressService;

	private final ServiceLocator serviceLocator;

	/**
	 * Build the part site.
	 * 
	 * @param ref
	 *            the part reference
	 * @param part
	 *            the part
	 * @param page
	 *            the page it belongs to
	 */
	public PartSite(IWorkbenchPartReference ref, IWorkbenchPart part,
			IWorkbenchPage page) {
		this.partReference = ref;
		this.part = part;
		this.page = page;
		extensionID = "org.eclipse.ui.UnknownID"; //$NON-NLS-1$
		extensionName = "Unknown Name"; //$NON-NLS-1$

		// Initialize the service locator.
		final IServiceLocator parentServiceLocator = page.getWorkbenchWindow();
		this.serviceLocator = new ServiceLocator(parentServiceLocator);

		initializeDefaultServices();
	}

	/**
	 * Initialize the local services.
	 */
	private void initializeDefaultServices() {
		final IHandlerService parentService = (IHandlerService) serviceLocator
				.getService(IHandlerService.class);
		final Expression defaultExpression = new ActivePartExpression(part);
		final IHandlerService slave = new SlaveHandlerService(parentService,
				defaultExpression);
		serviceLocator.registerService(IHandlerService.class, slave);

		final IContextService parentContextService = (IContextService) serviceLocator
				.getService(IContextService.class);
		final IContextService contextService = new SlaveContextService(
				parentContextService, defaultExpression);
		serviceLocator.registerService(IContextService.class, contextService);

		final ICommandService parentCommandService = (ICommandService) serviceLocator
				.getService(ICommandService.class);
		final ICommandService commandService = new SlaveCommandService(
				parentCommandService);
		serviceLocator.registerService(ICommandService.class, commandService);
	}

	/**
	 * Dispose the contributions.
	 */
	public void dispose() {
		if (menuExtenders != null) {
			for (int i = 0; i < menuExtenders.size(); i++) {
				((PopupMenuExtender) menuExtenders.get(i)).dispose();
			}
			menuExtenders = null;
		}

		 if (keyBindingService != null) {
			keyBindingService.dispose();
		 }

		if (progressService != null) {
			progressService.dispose();
		}

		if (serviceLocator != null) {
			serviceLocator.dispose();
		}
	}

	/**
	 * Returns the action bars for the part. If this part is a view then it has
	 * exclusive use of the action bars. If this part is an editor then the
	 * action bars are shared among this editor and other editors of the same
	 * type.
	 */
	public IActionBars getActionBars() {
		return actionBars;
	}

	/**
	 * Returns the part registry extension ID.
	 * 
	 * @return the registry extension ID
	 */
	public String getId() {
		return extensionID;
	}

	/**
	 * Returns the page containing this workbench site's part.
	 * 
	 * @return the page containing this part
	 */
	public IWorkbenchPage getPage() {
		return page;
	}

	/**
	 * Gets the part pane.
	 */
	public PartPane getPane() {
		return ((WorkbenchPartReference) partReference).getPane();
	}

	/**
	 * Returns the part.
	 */
	public IWorkbenchPart getPart() {
		return part;
	}

	/**
	 * Returns the part reference.
	 */
	public IWorkbenchPartReference getPartReference() {
		return partReference;
	}

	/**
	 * Returns the part registry plugin ID. It cannot be <code>null</code>.
	 * 
	 * @return the registry plugin ID
	 */
	public String getPluginId() {
		return pluginID;
	}

	/**
	 * Returns the registered name for this part.
	 */
	public String getRegisteredName() {
		return extensionName;
	}

	/**
	 * Returns the selection provider for a part.
	 */
	public ISelectionProvider getSelectionProvider() {
		return selectionProvider;
	}

	/**
	 * Returns the shell containing this part.
	 * 
	 * @return the shell containing this part
	 */
	public Shell getShell() {
		PartPane pane = getPane();

		// Compatibility: This method should not be used outside the UI
		// thread... but since this condition
		// was not always in the JavaDoc, we still try to return our best guess
		// about the shell if it is
		// called from the wrong thread.
		Display currentDisplay = Display.getCurrent();
		if (currentDisplay == null
 currentDisplay != getWorkbenchWindow().getWorkbench()
						.getDisplay()) {
			// Uncomment this to locate places that try to access the shell from
			// a background thread
			// WorkbenchPlugin.log(new Exception("Error:
			// IWorkbenchSite.getShell() was called outside the UI thread. Fix
			// this code.")); //$NON-NLS-1$

			return getWorkbenchWindow().getShell();
		}

		if (pane == null) {
			return getWorkbenchWindow().getShell();
		}

		Shell s = pane.getShell();

		if (s == null) {
			return getWorkbenchWindow().getShell();
		}

		return s;
	}

	/**
	 * Returns the workbench window containing this part.
	 * 
	 * @return the workbench window containing this part
	 */
	public IWorkbenchWindow getWorkbenchWindow() {
		return page.getWorkbenchWindow();
	}

	/**
	 * Register a popup menu for extension.
	 */
	public void registerContextMenu(String menuID, MenuManager menuMgr,
			ISelectionProvider selProvider) {
		if (menuExtenders == null) {
			menuExtenders = new ArrayList(1);
		}

		registerContextMenu(menuID, menuMgr, selProvider, true, getPart(),
				menuExtenders);
	}

	/**
	 * Register a popup menu with the default id for extension.
	 */
	public void registerContextMenu(MenuManager menuMgr,
			ISelectionProvider selProvider) {
		registerContextMenu(getId(), menuMgr, selProvider);
	}

	// getContextMenuIds() added by Dan Rubel (dan_rubel@instantiations.com)
	/**
	 * Get the registered popup menu identifiers
	 */
	public String[] getContextMenuIds() {
		if (menuExtenders == null) {
			return new String[0];
		}
		ArrayList menuIds = new ArrayList(menuExtenders.size());
		for (Iterator iter = menuExtenders.iterator(); iter.hasNext();) {
			final PopupMenuExtender extender = (PopupMenuExtender) iter.next();
			menuIds.addAll(extender.getMenuIds());
		}
		return (String[]) menuIds.toArray(new String[menuIds.size()]);
	}

	/**
	 * Sets the action bars for the part.
	 */
	public void setActionBars(SubActionBars bars) {
		actionBars = bars;
	}

	/**
	 * Sets the configuration element for a part.
	 */
	public void setConfigurationElement(IConfigurationElement configElement) {

		// Get extension ID.
		extensionID = configElement.getAttribute("id"); //$NON-NLS-1$

		// Get plugin ID.
		pluginID = configElement.getNamespace();

		// Get extension name.
		String name = configElement.getAttribute("name"); //$NON-NLS-1$
		if (name != null) {
			extensionName = name;
		}
	}

	protected void setPluginId(String pluginId) {
		this.pluginID = pluginId;
	}

	/**
	 * Sets the part registry extension ID.
	 * 
	 * @param id
	 *            the registry extension ID
	 */
	protected void setId(String id) {
		extensionID = id;
	}

	/**
	 * Sets the part.
	 */
	public void setPart(IWorkbenchPart newPart) {
		part = newPart;
	}

	/**
	 * Sets the registered name for this part.
	 * 
	 * @param name
	 *            the registered name
	 */
	protected void setRegisteredName(String name) {
		extensionName = name;
	}

	/**
	 * Set the selection provider for a part.
	 */
	public void setSelectionProvider(ISelectionProvider provider) {
		selectionProvider = provider;
	}

	/*
	 * @see IWorkbenchPartSite#getKeyBindingService()
	 */
	public IKeyBindingService getKeyBindingService() {
		if (keyBindingService == null) {
			keyBindingService = new KeyBindingService(this);

			// TODO why is this here? and it should be using HandlerSubmissions
			// directly..
			if (this instanceof EditorSite) {
				EditorActionBuilder.ExternalContributor contributor = (EditorActionBuilder.ExternalContributor) ((EditorSite) this)
						.getExtensionActionBarContributor();

				if (contributor != null) {
					ActionDescriptor[] actionDescriptors = contributor
							.getExtendedActions();

					if (actionDescriptors != null) {
						for (int i = 0; i < actionDescriptors.length; i++) {
							ActionDescriptor actionDescriptor = actionDescriptors[i];

							if (actionDescriptor != null) {
								IAction action = actionDescriptors[i]
										.getAction();

								if (action != null
										&& action.getActionDefinitionId() != null) {
									keyBindingService.registerAction(action);
								}
							}
						}
					}
				}
			}
		}

		return keyBindingService;
	}

	protected String getInitialScopeId() {
		return null;
	}

	/**
	 * Get an adapter for this type.
	 * 
	 * @param adapter
	 * @return
	 */
	public final Object getAdapter(Class adapter) {

		if (IWorkbenchSiteProgressService.class == adapter) {
			return getSiteProgressService();
		}

		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	public void activateActionBars(boolean forceVisibility) {
		if (actionBars != null) {
			actionBars.activate(forceVisibility);
		}
	}

	public void deactivateActionBars(boolean forceHide) {
		if (actionBars != null) {
			actionBars.deactivate(forceHide);
		}
	}

	/**
	 * Get a progress service for the receiver.
	 * 
	 * @return WorkbenchSiteProgressService
	 */
	private WorkbenchSiteProgressService getSiteProgressService() {
		if (progressService == null) {
			progressService = new WorkbenchSiteProgressService(this);
		}
		return progressService;
	}

	public final Object getService(final Class key) {
		return serviceLocator.getService(key);
	}

	public final boolean hasService(final Class key) {
		return serviceLocator.hasService(key);
	}

	/**
	 * Prints out the identifier, the plug-in identifier and the registered
	 * name. This is for debugging purposes only.
	 * 
	 * @since 3.2
	 */
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("PartSite(id="); //$NON-NLS-1$
		buffer.append(getId());
		buffer.append(",pluginId="); //$NON-NLS-1$
		buffer.append(getPluginId());
		buffer.append(",registeredName="); //$NON-NLS-1$
		buffer.append(getRegisteredName());
		buffer.append(",hashCode="); //$NON-NLS-1$
		buffer.append(hashCode());
		buffer.append(')');
		return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2729.java