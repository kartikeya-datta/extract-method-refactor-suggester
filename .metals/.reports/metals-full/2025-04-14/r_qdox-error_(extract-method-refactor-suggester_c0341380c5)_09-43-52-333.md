error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7301.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7301.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7301.java
text:
```scala
r@@eturn org.eclipse.ui.internal.commands.Manager.getInstance().getKeyMachine().getConfiguration();

/************************************************************************
Copyright (c) 2003 IBM Corporation and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html

Contributors:
	IBM - Initial implementation
************************************************************************/

package org.eclipse.ui.internal;

import java.util.HashMap;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.ui.IKeyBindingService;
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
	
	/* Maps action definition id to action. */
	private HashMap defIdToAction = new HashMap();
	
	/* The active accelerator scope which is set by the editor */
	private String[] scopeIds = new String[] { IWorkbenchConstants.DEFAULT_ACCELERATOR_SCOPE_ID };
	
	/* The Workbench window key binding service which manages the 
	 * global actions and the action sets 
	 */
	private WWinKeyBindingService parent;
	
	/**
	 * Create an instance of KeyBindingService and initializes 
	 * it with its parent.
	 */		
	public KeyBindingService(WWinKeyBindingService service, PartSite site) {
		parent = service;
		
		if (site instanceof EditorSite) {
			EditorActionBuilder.ExternalContributor contributor = (EditorActionBuilder.ExternalContributor) ((EditorSite) site).getExtensionActionBarContributor();
			
			if (contributor != null)
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
    	String defId = action.getActionDefinitionId();
    	Assert.isNotNull(defId, "All registered action must have a definition id"); //$NON-NLS-1$
		defIdToAction.put(defId,action);
    }
    
   	/*
	 * @see IKeyBindingService#unregisterAction(IAction)
	 */
	public void unregisterAction(IAction action) {   		
    	String defId = action.getActionDefinitionId();
    	Assert.isNotNull(defId, "All registered action must have a definition id"); //$NON-NLS-1$
		defIdToAction.remove(defId);
    }
		
    /**
     * Returns the action mapped with the specified <code>definitionId</code>
     */
    public IAction getAction(String definitionId) {
    	IAction action = (IAction) defIdToAction.get(definitionId);
    	
    	if (action == null)
    		action = (IAction) parent.getMapping().get(definitionId);
    	    		
    	return action;
    }

	/*
	 * @see IKeyBindingService#getActiveAcceleratorConfigurationId()
	 */
    public String getActiveAcceleratorConfigurationId() {
    	return org.eclipse.ui.internal.commands.Manager.getInstance().getKeyMachine().getKeyConfiguration();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7301.java