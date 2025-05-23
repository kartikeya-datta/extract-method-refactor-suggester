error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/795.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/795.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/795.java
text:
```scala
r@@eturn null;

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Dan Rubel (dan_rubel@instantiations.com) - accessor to get context menu ids
 *******************************************************************************/
package org.eclipse.ui.internal;


import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.SubActionBars;
import org.eclipse.ui.commands.IActionService;
import org.eclipse.ui.contexts.IContextActivationService;
import org.eclipse.ui.internal.commands.ActionService;
import org.eclipse.ui.internal.contexts.ContextActivationService;

/**
 * <code>PartSite</code> is the general implementation for an
 * <code>IWorkbenchPartSite</code>.  A site maintains the context for a part,
 * including the part, its pane, active contributions, selection provider, etc.
 * Together, these components make up the complete behavior for a
 * part as if it was implemented by one person.  
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
public class PartSite implements IWorkbenchPartSite {

	private IWorkbenchPart part;
	private IWorkbenchPage page;
	private PartPane pane;
	private IConfigurationElement configElement;
	private String extensionID;
	private String pluginID;
	private String extensionName;
	private ISelectionProvider selectionProvider;
	private SubActionBars actionBars;
	private KeyBindingService keyBindingService;
	private ArrayList menuExtenders;
	
	private IActionService handlerService;
		
	/**
	 * EditorContainer constructor comment.
	 */
	public PartSite(IWorkbenchPart part, IWorkbenchPage page) {
		this.part = part;
		this.page = page;
		extensionID = "org.eclipse.ui.UnknownID"; //$NON-NLS-1$
		extensionName = "Unknown Name"; //$NON-NLS-1$
	}

	private IActionService actionService;

	public IActionService getActionService() {
		if (actionService == null) 
			actionService = new ActionService();
		
		return actionService;
	}
	
	private IContextActivationService contextActivationService;	
	
	public IContextActivationService getContextActivationService() {
		if (contextActivationService == null) 
			contextActivationService = new ContextActivationService();
		
		return contextActivationService;
	}
	
	/**
	 * Dispose the contributions.
	 */
	public void dispose() {
		if (menuExtenders != null) {
			for (int i = 0; i < menuExtenders.size(); i++) {
				((PopupMenuExtender)menuExtenders.get(i)).dispose();
			}
			menuExtenders = null;
		}
	}
	/**
	 * Returns the action bars for the part.
	 * If this part is a view then it has exclusive use of the action bars.
	 * If this part is an editor then the action bars are shared among this editor and other editors of
	 * the same type.
	 */
	public IActionBars getActionBars() {
		return actionBars;
	}
	/**
	 * Returns the configuration element for a part.
	 */
	public IConfigurationElement getConfigurationElement() {
		return configElement;
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
		return pane;
	}
	/**
	 * Returns the part.
	 */
	public IWorkbenchPart getPart() {
		return part;
	}
	/**
	 * Returns the part registry plugin ID.  It cannot be <code>null</code>.
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
		return page.getWorkbenchWindow().getShell();
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
	public void registerContextMenu(String menuID, MenuManager menuMgr, ISelectionProvider selProvider) {
		if (menuExtenders == null) {
			menuExtenders = new ArrayList(1);
		}
		menuExtenders.add(new PopupMenuExtender(menuID, menuMgr, selProvider, part));
	}
	/**
	 * Register a popup menu with the default id for extension.
	 */
	public void registerContextMenu(MenuManager menuMgr, ISelectionProvider selProvider) {
		registerContextMenu(getId(), menuMgr, selProvider);
	}
	// getContextMenuIds() added by Dan Rubel (dan_rubel@instantiations.com)
	/**
	 * Get the registered popup menu identifiers
	 */
	public String[] getContextMenuIds() {
		if (menuExtenders == null)
			return new String[0];
		String[] menuIds = new String[menuExtenders.size()];
		int index = 0;
		for (Iterator iter = menuExtenders.iterator(); iter.hasNext();)
			menuIds[index++] = ((PopupMenuExtender) iter.next()).getMenuId();
		return menuIds;
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
		// Save for external use.
		this.configElement = configElement;

		// Get extension ID.
		extensionID = configElement.getAttribute("id"); //$NON-NLS-1$

		// Get plugin ID.
		IPluginDescriptor pd = configElement.getDeclaringExtension().getDeclaringPluginDescriptor();
		pluginID = pd.getUniqueIdentifier();

		// Get extension name.
		String name = configElement.getAttribute("name"); //$NON-NLS-1$
		if (name != null)
			extensionName = name;
	}
	/**
	 * Sets the part pane.
	 */
	public void setPane(PartPane pane) {
		this.pane = pane;
	}
	/**
	 * Sets the part.
	 */
	public void setPart(IWorkbenchPart newPart) {
		part = newPart;
	}
	/**
	 * Set the selection provider for a part.
	 */
	public void setSelectionProvider(ISelectionProvider provider) {
		selectionProvider = provider;
	}

	/* (non-Javadoc)
	 * Method declared on IEditorSite.
	 */
	public IKeyBindingService getKeyBindingService() {
		if (keyBindingService == null) {
			keyBindingService = new KeyBindingService(getActionService(), getContextActivationService());
			
			if (this instanceof EditorSite) {
				EditorActionBuilder.ExternalContributor contributor = (EditorActionBuilder.ExternalContributor) ((EditorSite) this).getExtensionActionBarContributor();
			
				if (contributor != null) {
					ActionDescriptor[] actionDescriptors = contributor.getExtendedActions();
			
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
			}			
			
			// keyBindingService.setScopes(new String[] { getInitialScopeId() }); //$NON-NLS-1$
		}

		return keyBindingService;
	}

	protected String getInitialScopeId() {
		return IWorkbenchConstants.DEFAULT_ACCELERATOR_SCOPE_ID;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/795.java