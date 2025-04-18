error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7333.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7333.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7333.java
text:
```scala
r@@eturn BundleUtility.isActive(bundleId);

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IActionDelegateWithEvent;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.SelectionEnabler;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.internal.misc.StatusUtil;
import org.eclipse.ui.internal.util.BundleUtility;

/**
 * A PluginAction is a proxy for an action extension.
 *
 * At startup we read the registry and create a PluginAction for each action extension.
 * This plugin action looks like the real action ( label, icon, etc ) and acts as
 * a proxy for the action until invoked.  At that point the proxy will instantiate 
 * the real action and delegate the run method to the real action.
 * This makes it possible to load the action extension lazily.
 *
 * Occasionally the class will ask if it is OK to 
 * load the delegate (on selection changes).  If the plugin containing
 * the action extension has been loaded then the action extension itself
 * will be instantiated.
 */

public abstract class PluginAction extends Action implements
        ISelectionListener, ISelectionChangedListener, INullSelectionListener,
        IPluginContribution {
    private IActionDelegate delegate;

    private SelectionEnabler enabler;

    private ISelection selection;

    private IConfigurationElement configElement;

    private String pluginId;

    private String runAttribute = ActionDescriptor.ATT_CLASS;

    private static int actionCount = 0;

    //a boolean that returns whether or not this action
    //is Adaptable - i.e. is defined on a resource type
    boolean isAdaptableAction = false;

    boolean adaptableNotChecked = true;

    /**
     * PluginAction constructor.
     */
    public PluginAction(IConfigurationElement actionElement, String id,
            int style) {
        super(null, style);

        this.configElement = actionElement;

        if (id != null) {
            setId(id);
        } else {
            // Create unique action id.
            setId("PluginAction." + Integer.toString(actionCount)); //$NON-NLS-1$
            ++actionCount;
        }

        String defId = actionElement
                .getAttribute(ActionDescriptor.ATT_DEFINITION_ID);
        setActionDefinitionId(defId);

        pluginId = configElement.getDeclaringExtension().getNamespace();

        // Read enablement declaration.
        if (configElement.getAttribute(PluginActionBuilder.ATT_ENABLES_FOR) != null) {
            enabler = new SelectionEnabler(configElement);
        } else {
            IConfigurationElement[] kids = configElement
                    .getChildren(PluginActionBuilder.TAG_ENABLEMENT);
            if (kids.length > 0)
                enabler = new SelectionEnabler(configElement);
        }

        // Give enabler or delegate a chance to adjust enable state
        selectionChanged(new StructuredSelection());
    }

    /**
     * Creates the delegate and refreshes its enablement.
     */
    protected final void createDelegate() {
        // The runAttribute is null if delegate creation failed previously...
        if (delegate == null && runAttribute != null) {
            try {
                Object obj = WorkbenchPlugin.createExtension(configElement,
                        runAttribute);
                delegate = validateDelegate(obj);
                initDelegate();
                refreshEnablement();
            } catch (Throwable e) {
                runAttribute = null;
                IStatus status = null;
                if (e instanceof CoreException) {
                    status = ((CoreException) e).getStatus();
                } else {
                    status = StatusUtil
                            .newStatus(
                                    IStatus.ERROR,
                                    "Internal plug-in action delegate error on creation.", e); //$NON-NLS-1$
                }
                String id = configElement.getAttribute(ActionDescriptor.ATT_ID);
                WorkbenchPlugin
                        .log(
                                "Could not create action delegate for id: " + id, status); //$NON-NLS-1$
                return;
            }
        }
    }

    /**
     * Validates the object is a delegate of the expected type. Subclasses can
     * override to check for specific delegate types.
     * <p>
     * <b>Note:</b> Calls to the object are not allowed during this method.
     * </p>
     *
     * @param obj a possible action delegate implementation
     * @return the <code>IActionDelegate</code> implementation for the object
     * @throws a <code>WorkbenchException</code> if not expect delegate type
     */
    protected IActionDelegate validateDelegate(Object obj)
            throws WorkbenchException {
        if (obj instanceof IActionDelegate)
            return (IActionDelegate) obj;
        else
            throw new WorkbenchException(
                    "Action must implement IActionDelegate"); //$NON-NLS-1$
    }

    /** 
     * Initialize the action delegate by calling its lifecycle method.
     * Subclasses may override but must call this implementation first.
     */
    protected void initDelegate() {
        if (delegate instanceof IActionDelegate2)
            ((IActionDelegate2) delegate).init(this);
    }

    /**
     * Returns the action delegate if created. Can be <code>null</code>
     * if the delegate is not created yet or if previous delegate
     * creation failed.
     */
    protected IActionDelegate getDelegate() {
        return delegate;
    }

    /**
     * Returns true if the declaring plugin has been loaded
     * and there is no need to delay creating the delegate
     * any more.
     */
    protected boolean isOkToCreateDelegate() {
        // test if the plugin has loaded
        String bundleId = configElement.getDeclaringExtension().getNamespace();
        return BundleUtility.isActivated(bundleId);
    }

    /**
     * Return whether or not this action could have been registered
     * due to an adaptable - i.e. it is a resource type.
     */
    private boolean hasAdaptableType() {
        if (adaptableNotChecked) {
            Object parentConfig = configElement.getParent();
            String typeName = null;
            if (parentConfig != null
                    && parentConfig instanceof IConfigurationElement)
                typeName = ((IConfigurationElement) parentConfig)
                        .getAttribute("objectClass"); //$NON-NLS-1$

            //See if this is typed at all first
            if (typeName == null) {
                adaptableNotChecked = false;
                return false;
            }
            Class resourceClass = LegacyResourceSupport.getResourceClass();
            if (resourceClass == null) {
                // resources plug-in not even present
                isAdaptableAction = false;
                adaptableNotChecked = false;
                return false;
            }

            if (typeName.equals(resourceClass.getName())) {
                isAdaptableAction = true;
                adaptableNotChecked = false;
                return isAdaptableAction;
            }
            Class[] children = resourceClass.getDeclaredClasses();
            for (int i = 0; i < children.length; i++) {
                if (children[i].getName().equals(typeName)) {
                    isAdaptableAction = true;
                    adaptableNotChecked = false;
                    return isAdaptableAction;
                }
            }
            adaptableNotChecked = false;
        }
        return isAdaptableAction;
    }

    /**
     * Refresh the action enablement.
     */
    protected void refreshEnablement() {
        if (enabler != null) {
            setEnabled(enabler.isEnabledForSelection(selection));
        }
        if (delegate != null) {
            delegate.selectionChanged(this, selection);
        }
    }

    /* (non-Javadoc)
     * Method declared on IAction.
     */
    public void run() {
        runWithEvent(null);
    }

    /* (non-Javadoc)
     * Method declared on IAction.
     */
    public void runWithEvent(Event event) {
        // this message dialog is problematic.
        if (delegate == null) {
            createDelegate();
            if (delegate == null) {
                MessageDialog
                        .openInformation(
                                Display.getDefault().getActiveShell(),
                                WorkbenchMessages.getString("Information"), //$NON-NLS-1$
                                WorkbenchMessages
                                        .getString("PluginAction.operationNotAvailableMessage")); //$NON-NLS-1$
                return;
            }
            if (!isEnabled()) {
                MessageDialog.openInformation(Display.getDefault()
                        .getActiveShell(), WorkbenchMessages
                        .getString("Information"), //$NON-NLS-1$
                        WorkbenchMessages
                                .getString("PluginAction.disabledMessage")); //$NON-NLS-1$
                return;
            }
        }

        if (event != null) {
            if (delegate instanceof IActionDelegate2) {
                ((IActionDelegate2) delegate).runWithEvent(this, event);
                return;
            }
            // Keep for backward compatibility with R2.0
            if (delegate instanceof IActionDelegateWithEvent) {
                ((IActionDelegateWithEvent) delegate).runWithEvent(this, event);
                return;
            }
        }

        delegate.run(this);
    }

    /**
     * Handles selection change. If rule-based enabled is
     * defined, it will be first to call it. If the delegate
     * is loaded, it will also be given a chance.
     */
    public void selectionChanged(ISelection newSelection) {
        // Update selection.
        selection = newSelection;
        if (selection == null)
            selection = StructuredSelection.EMPTY;
        if (hasAdaptableType())
            selection = getResourceAdapters(selection);

        // If the delegate can be loaded, do so.
        // Otherwise, just update the enablement.
        if (delegate == null && isOkToCreateDelegate())
            createDelegate();
        else
            refreshEnablement();
    }

    /**
     * The <code>SelectionChangedEventAction</code> implementation of this 
     * <code>ISelectionChangedListener</code> method calls 
     * <code>selectionChanged(IStructuredSelection)</code> when the selection is
     * a structured one.
     */
    public void selectionChanged(SelectionChangedEvent event) {
        ISelection sel = event.getSelection();
        selectionChanged(sel);
    }

    /**
     * The <code>SelectionChangedEventAction</code> implementation of this 
     * <code>ISelectionListener</code> method calls 
     * <code>selectionChanged(IStructuredSelection)</code> when the selection is
     * a structured one. Subclasses may extend this method to react to the change.
     */
    public void selectionChanged(IWorkbenchPart part, ISelection sel) {
        selectionChanged(sel);
    }

    /**
     * Get a new selection with the resource adaptable version 
     * of this selection
     */
    private ISelection getResourceAdapters(ISelection sel) {
        if (sel instanceof IStructuredSelection) {
            List adaptables = new ArrayList();
            Object[] elements = ((IStructuredSelection) sel).toArray();
            for (int i = 0; i < elements.length; i++) {
                Object originalValue = elements[i];
                if (originalValue instanceof IAdaptable) {
                    Class resourceClass = LegacyResourceSupport
                            .getResourceClass();
                    if (resourceClass != null) {
                        Object adaptedValue = ((IAdaptable) originalValue)
                                .getAdapter(resourceClass);
                        if (adaptedValue != null) {
                            adaptables.add(adaptedValue);
                        }
                    }
                }
            }
            return new StructuredSelection(adaptables);
        } else {
            return sel;
        }
    }

    /**
     * Returns the action identifier this action overrides.
     * Default implementation returns <code>null</code>.
     * 
     * @return the action identifier to override or <code>null</code>
     */
    public String getOverrideActionId() {
        return null;
    }

    /**
     * @return the IConfigurationElement used to create this PluginAction.
     * 
     * @since 3.0
     */
    protected IConfigurationElement getConfigElement() {
        return configElement;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPluginContribution#getLocalId()
     */
    public String getLocalId() {
        return getId();
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPluginContribution#getPluginId()
     */
    public String getPluginId() {
        return pluginId;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7333.java