error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6017.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6017.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6017.java
text:
```scala
r@@eturn ((Workbench) PlatformUI.getWorkbench()).getActiveAcceleratorConfiguration().getLabel().getId();

package org.eclipse.ui.internal;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.util.HashMap;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.events.*;
import org.eclipse.ui.*;
import org.eclipse.ui.internal.misc.Assert;

/** 
 * Implementation of an IKeyBindingService.
 * Notes:
 * <ul>
 * <li>One instance is created for each editor site</li>
 * <li>Each editor has to register all its actions by calling registerAction()</li>
 * <li>The editor should call setActiveAcceleratorScopeId() once</li>
 * </ul>
 */
public class KeyBindingService implements IKeyBindingService {
	
	private IPartListener partListener;
	private ShellListener shellListener;
	
	/* Maps action definition id to action. */
	private HashMap defIdToAction = new HashMap();
	
	/* Maps action definition id to action. Includes the actions 
	 * registered in this service and its parent so that only one 
	 * lookup is needed.
	 */
	private HashMap allDefIdToAction = new HashMap();

	/* The active accelerator scope which is set by the editor */
	private String[] scopeIds = new String[] { IWorkbenchConstants.DEFAULT_ACCELERATOR_SCOPE_ID };
	
	/* The Workbench window key binding service which manages the 
	 * global actions and the action sets 
	 */
	private WWinKeyBindingService parent;
	
	/* A number increased by the parent whenever a new action 
	 * is registered so that this instance can update its mapping
	 * when it is out of sync.
	 */
	private long parentUpdateNumber;
	
	/**
	 * Create an instance of KeyBindingService and initializes 
	 * it with its parent.
	 */		
	public KeyBindingService(WWinKeyBindingService service,PartSite site) {
		partListener = new IPartListener() {
			public void partActivated(IWorkbenchPart part) {}
			public void partBroughtToTop(IWorkbenchPart part) {}
			public void partClosed(IWorkbenchPart part) {}
			public void partDeactivated(IWorkbenchPart part) {
				parent.clear();
			}
			public void partOpened(IWorkbenchPart part) {}
		};
		
		shellListener = new ShellAdapter() {
			public void shellDeactivated(ShellEvent e) {
				parent.clear();
			}
		};
		
		parent = service;
		parentUpdateNumber = parent.getUpdateNumber() - 1;
		service.getWindow().getPartService().addPartListener(partListener);
		service.getWindow().getShell().addShellListener(shellListener);
		ActionDescriptor actionDescriptors[] = null;
		
		if(site instanceof EditorSite) {
			EditorActionBuilder.ExternalContributor contributor = (EditorActionBuilder.ExternalContributor)((EditorSite)site).getExtensionActionBarContributor();
			if(contributor != null)
				registerExtendedActions(contributor.getExtendedActions());
		}
	}
	
	public void registerExtendedActions(ActionDescriptor[] actionDescriptors) {
		for (int i = 0; i < actionDescriptors.length; i++) {
			IAction action = actionDescriptors[i].getAction();
			
			if (action.getActionDefinitionId() != null)
				registerAction(action);
		}		
	}

	/*
	 * @see IKeyBindingService#getScopeIds()
	 */
	public String[] getScopeIds() {
    	return (String[]) scopeIds.clone();
    }

	/*
	 * @see IKeyBindingService#setScopeIds(String[] scopeIds)
	 */
	public void setScopeIds(String[] scopeIds)
		throws IllegalArgumentException {
		if (scopeIds == null || scopeIds.length < 1)
			throw new IllegalArgumentException();
			
    	this.scopeIds = (String[]) scopeIds.clone();
    	
    	for (int i = 0; i < scopeIds.length; i++)
			if (scopeIds[i] == null)
				throw new IllegalArgumentException();    	
    }

	/*
	 * @see IKeyBindingService#registerAction(IAction)
	 */
	public void registerAction(IAction action) {
    	if(parentUpdateNumber != parent.getUpdateNumber())
    		initializeMapping();
    	String defId = action.getActionDefinitionId();
    	Assert.isNotNull(defId,"All registered action must have a definition id"); //$NON-NLS-1$
		defIdToAction.put(defId,action);
		allDefIdToAction.put(defId,action);
    }
    
   	/*
	 * @see IKeyBindingService#unregisterAction(IAction)
	 */
	public void unregisterAction(IAction action) {   		
    	String defId = action.getActionDefinitionId();
    	Assert.isNotNull(defId,"All registered action must have a definition id"); //$NON-NLS-1$
		defIdToAction.remove(defId);
		allDefIdToAction.remove(defId);
    }
	
	/*
	 * Merge the actions from its parents with its registered actions
	 * in one HashMap
	 */
	private void initializeMapping() {
		parentUpdateNumber = parent.getUpdateNumber();
		allDefIdToAction = parent.getMapping();
		allDefIdToAction.putAll(defIdToAction);
	}
	
	/** 
	 * Remove the part listener when the editor site is disposed.
	 */
	public void dispose() {
		parent.getWindow().getPartService().removePartListener(partListener);
		parent.getWindow().getShell().removeShellListener(shellListener);
	}
	
    /**
     * Returns the action mapped with the specified <code>definitionId</code>
     */
    public IAction getAction(String definitionId) {
    	//Chech if parent has changed. E.g. added action sets.
    	if (parentUpdateNumber != parent.getUpdateNumber())
    		initializeMapping();
    		
    	return (IAction) allDefIdToAction.get(definitionId);
    }

	/*
	 * @see IKeyBindingService#getActiveAcceleratorConfigurationId()
	 */
    public String getActiveAcceleratorConfigurationId() {
    	return ((Workbench) PlatformUI.getWorkbench()).getActiveAcceleratorConfiguration().getId();
    }

	/*
	 * @see IKeyBindingService#getActiveAcceleratorScopeId()
	 */
	public String getActiveAcceleratorScopeId() {
   		return getScopeIds()[0];
    }

	/*
	 * @see IKeyBindingService#setActiveAcceleratorScopeId(String)
	 */ 
    public void setActiveAcceleratorScopeId(String scopeId)
    	throws IllegalArgumentException {
   		setScopeIds(new String[] { scopeId });
    }
    
   	/*
	 * @see IKeyBindingService#processKey(Event)
	 */
	public boolean processKey(KeyEvent event) {
		return false;
    }

    /*
	 * @see IKeyBindingService#registerAction(IAction)
	 */
	public void enable(boolean enable) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6017.java