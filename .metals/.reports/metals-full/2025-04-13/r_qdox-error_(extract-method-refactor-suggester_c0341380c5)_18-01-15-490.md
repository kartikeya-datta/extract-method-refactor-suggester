error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5142.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5142.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5142.java
text:
```scala
t@@hrow new ComponentException(NLS.bind(WorkbenchMessages.NestedContext_0, new String[] {

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.part.multiplexer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.components.framework.ComponentException;
import org.eclipse.ui.internal.components.framework.ComponentHandle;
import org.eclipse.ui.internal.components.framework.IServiceProvider;
import org.eclipse.ui.internal.components.framework.ServiceFactory;
import org.eclipse.ui.internal.components.util.ServiceMap;
import org.eclipse.ui.internal.part.Part;

/**
 * Contains a factory for services that can delegate to a shared implementation.
 * Many <code>NestedContext</code> instances can share the same <code>ISharedComponents</code>
 * instance, however only one of them will be active at a time. A <code>NestedContext</code>
 * remembers everything it has created. Calling activate 
 * 
 * When a <code>NestedContext</code>
 * is activated, it activate
 * 
 * @since 3.1
 */
public class NestedContext extends ServiceFactory {

	//private List componentList = new ArrayList();
	private IServiceProvider sharedComponents;
    private ServiceFactory nestedFactories;
    private Map componentMap = new HashMap();
    
    private ISharedContext sharedContext = new ISharedContext() {
    /* (non-Javadoc)
     * @see org.eclipse.core.components.nesting.ISharedContext#getSharedComponents()
     */
    public IServiceProvider getSharedComponents() {
        return sharedComponents;
    }  
    };
    
    /**
     * Creates a new NestedContext 
     * 
     * @param sharedComponents
     * @param nestedFactories
     */
	public NestedContext(IServiceProvider sharedComponents, ServiceFactory nestedFactories) {
		this.sharedComponents = sharedComponents;
        this.nestedFactories = nestedFactories;
	}
	
	public ComponentHandle createHandle(Object componentKey, IServiceProvider container)
			throws ComponentException {
        
    	ComponentHandle handle = nestedFactories.createHandle(componentKey, new ServiceMap(container)
                .map(ISharedContext.class, sharedContext));
        
        if (handle == null) {
            return null;
        }
        
        Object component = handle.getInstance();
        
        if (!(component instanceof INestedComponent)) {
        	throw new ComponentException(NLS.bind(WorkbenchMessages.NestedContext_0, new String[] { //$NON-NLS-1$
        			INestedComponent.class.getName(), component.getClass().getName()}
        	), null);
        }
        
        componentMap.put(componentKey, component);
        
        return handle;     
	}
    
    /* (non-Javadoc)
     * @see org.eclipse.core.components.IComponentContext#hasKey(java.lang.Object)
     */
    public boolean hasService(Object componentKey) {
        return nestedFactories.hasService(componentKey);
    }
    
	/**
	 * Activates all the components created by this context. The components
	 * will copy their current state to the shared container and start
	 * delegating to the shared implementation.
	 */
	public void activate(Part partBeingActivated) {
        Collection componentList = componentMap.values();
        
		for (Iterator iter = componentList.iterator(); iter.hasNext();) {
			INestedComponent next = (INestedComponent) iter.next();
			
			next.activate(partBeingActivated);
		}
	}
	
	/**
	 * Deactivates all the components created by this context. The components
	 * will stop delegating to the shared implementation.
     * 
     * @param newActive context that is about to be activated (or null if none)
	 */
	public void deactivate(NestedContext newActive) {
        Set entries = componentMap.entrySet();
        for (Iterator iter = entries.iterator(); iter.hasNext();) {
            Map.Entry next = (Map.Entry) iter.next();
            INestedComponent component = (INestedComponent)next.getValue();
            
            component.deactivate(newActive.componentMap.get(next.getKey()));
        }
   	}
    
    /* (non-Javadoc)
     * @see org.eclipse.core.components.IComponentContext#getMissingDependencies()
     */
    public Collection getMissingDependencies() {
        Collection result = nestedFactories.getMissingDependencies();
        result.remove(ISharedContext.class);
        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5142.java