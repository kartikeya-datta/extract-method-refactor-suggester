error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9577.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9577.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9577.java
text:
```scala
W@@orkbenchPlugin.log(exception);

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.decorators;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.ui.internal.ActionExpression;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.RegistryReader;

/**
 * The DecoratorDefinition is the class that holds onto
 * the label decorator, the name and the name of the
 * class a decorator definition applies to,
 */

public abstract class DecoratorDefinition {
	
    private static final String ATT_LABEL = "label"; //$NON-NLS-1$
    
    private static final String ATT_OBJECT_CLASS = "objectClass"; //$NON-NLS-1$
    
    static final String CHILD_ENABLEMENT = "enablement"; //$NON-NLS-1$
    
    private static final String ATT_ADAPTABLE = "adaptable"; //$NON-NLS-1$
    
    private static final String ATT_ENABLED = "state"; //$NON-NLS-1$

    private ActionExpression enablement;

    protected boolean enabled;

    private boolean defaultEnabled;

    private String id;

    protected IConfigurationElement definingElement;

    //A flag that is set if there is an error creating the decorator
    protected boolean labelProviderCreationFailed = false;

	private boolean hasReadEnablement;

	static final String ATT_CLASS = "class";//$NON-NLS-1$

    /**
     * Create a new instance of the receiver with the
     * supplied values.
     */

    DecoratorDefinition(String identifier, IConfigurationElement element) {

        this.id = identifier;  
        this.definingElement = element;
        
        this.enabled = this.defaultEnabled = Boolean.valueOf(element.getAttribute(ATT_ENABLED)).booleanValue();
    }

    /**
     * Gets the name.
     * @return Returns a String
     */
    public String getName() {
        return definingElement.getAttribute(ATT_LABEL);
    }

    /**
     * Returns the description.
     * @return String
     */
    public String getDescription() {
        return RegistryReader.getDescription(definingElement);
    }

    /**
     * Gets the enabled.
     * @return Returns a boolean
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled flag and adds or removes the decorator
     * manager as a listener as appropriate.
     * @param newState The enabled to set
     */
    public void setEnabled(boolean newState) {

        //Only refresh if there has been a change
        if (this.enabled != newState) {
            this.enabled = newState;
            try {
                refreshDecorator();
            } catch (CoreException exception) {
                handleCoreException(exception);
            }

        }
    }

    /**
     * Refresh the current decorator based on our enable
     * state.
     */
    protected abstract void refreshDecorator() throws CoreException;

    /**
     * Dispose the decorator instance and remove listeners
     * as appropirate.
     * @param disposedDecorator
     */
    protected void disposeCachedDecorator(IBaseLabelProvider disposedDecorator) {
        disposedDecorator.removeListener(WorkbenchPlugin.getDefault()
                .getDecoratorManager());
        disposedDecorator.dispose();

    }

    /**
     * Return whether or not this decorator should be 
     * applied to adapted types.
     * 
     * @return whether or not this decorator should be 
     * applied to adapted types
     */
    public boolean isAdaptable() {
    	return Boolean.valueOf(definingElement.getAttribute(ATT_ADAPTABLE)).booleanValue();
    }

    /**
     * Gets the id.
     * @return Returns a String
     */
    public String getId() {
        return id;
    }

    /**
     * Return the default value for this type - this value
     * is the value read from the element description.
     * 
     * @return the default value for this type - this value
     * is the value read from the element description
     */
    public boolean getDefaultValue() {
        return defaultEnabled;
    }

    /**
     * Returns the enablement.
     * @return ActionExpression
     */
    protected ActionExpression getEnablement() {
    	if (!hasReadEnablement) {
    		hasReadEnablement = true;
    		initializeEnablement();
    	}
        return enablement;
    }

    /**
     * Initialize the enablement expression for this decorator
     */
    protected void initializeEnablement() {
        IConfigurationElement[] elements = definingElement.getChildren(CHILD_ENABLEMENT);
        if (elements.length == 0) {
            String className = definingElement.getAttribute(ATT_OBJECT_CLASS);
            if (className != null) {
				enablement = new ActionExpression(ATT_OBJECT_CLASS,
                        className);
			}
        } else {
			enablement = new ActionExpression(elements[0]);
		}
    }

    /**
     * Add a listener for the decorator.If there is an exception
     * then inform the user and disable the receiver.
     * This method should not be called unless a check for
     * isEnabled() has been done first.
     */
    void addListener(ILabelProviderListener listener) {
        try {
            //Internal decorator might be null so be prepared
            IBaseLabelProvider currentDecorator = internalGetLabelProvider();
            if (currentDecorator != null) {
				currentDecorator.addListener(listener);
			}
        } catch (CoreException exception) {
            handleCoreException(exception);
        }
    }

    /**
     * Return whether or not the decorator registered for element
     * has a label property called property name. If there is an 
     * exception disable the receiver and return false.
     * This method should not be called unless a check for
     * isEnabled() has been done first.
     */
    boolean isLabelProperty(Object element, String property) {
        try { //Internal decorator might be null so be prepared
            IBaseLabelProvider currentDecorator = internalGetLabelProvider();
            if (currentDecorator != null) {
				return currentDecorator.isLabelProperty(element, property);
			}
        } catch (CoreException exception) {
            handleCoreException(exception);
            return false;
        }
        return false;
    }

    /**
     * Gets the label provider and creates it if it does not exist yet. 
     * Throws a CoreException if there is a problem
     * creating the labelProvider.
     * This method should not be called unless a check for
     * enabled to be true is done first.
     * @return Returns a ILabelDecorator
     */
    protected abstract IBaseLabelProvider internalGetLabelProvider()
            throws CoreException;

    /** 
     * A CoreException has occured. Inform the user and disable
     * the receiver.
     */

    protected void handleCoreException(CoreException exception) {

        //If there is an error then reset the enabling to false
        WorkbenchPlugin.getDefault().getLog().log(exception.getStatus());
        crashDisable();
    }

    /**
     * A crash has occured. Disable the receiver without notification.
     */
    public void crashDisable() {
        this.enabled = false;
    }

    /**
     * Return whether or not this is a full or lightweight definition.
     * @return <code>true</code> if this is not a lightweight decorator.
     */
    public abstract boolean isFull();

	/**
	 * Return the configuration element.
	 * 
	 * @return the configuration element
	 * @since 3.1
	 */
	public IConfigurationElement getConfigurationElement() {
		return definingElement;
	}

    /**
     * Return whether the decorator is applicable to the given element
     * @param element the element to be decorated
     * @return whether the decorator w=should be applied to the element
     */
    public boolean isEnabledFor(Object element) {
    	if(isEnabled()){
    		ActionExpression expression =  getEnablement();
    		if(expression != null) {
				return expression.isEnabledFor(element);
			}
    		return true;//Always on if no expression
    	}
    	return false;
       
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9577.java