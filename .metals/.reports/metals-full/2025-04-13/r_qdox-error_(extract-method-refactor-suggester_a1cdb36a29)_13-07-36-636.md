error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2932.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2932.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2932.java
text:
```scala
public I@@ConfigurationElement getConfigurationElement() {

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
package org.eclipse.ui.internal.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.IWorkingSetElementAdapter;
import org.eclipse.ui.IWorkingSetUpdater;
import org.eclipse.ui.dialogs.IWorkingSetPage;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.misc.StatusUtil;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.statushandling.StatusManager;

/**
 * A working set descriptor stores the plugin registry data for 
 * a working set page extension.
 * 
 * @since 2.0
 */
public class WorkingSetDescriptor implements IPluginContribution {
    private String id;

    private String name;

    private String icon;

    private String pageClassName;
    
    private String updaterClassName;

    private IConfigurationElement configElement;
    
    private String[] classTypes;

	private String[] adapterTypes;

    private static final String ATT_ID = "id"; //$NON-NLS-1$

    private static final String ATT_NAME = "name"; //$NON-NLS-1$

    private static final String ATT_ICON = "icon"; //$NON-NLS-1$	

    private static final String ATT_PAGE_CLASS = "pageClass"; //$NON-NLS-1$
    
    private static final String ATT_UPDATER_CLASS = "updaterClass";  //$NON-NLS-1$
    
    private static final String ATT_ELEMENT_ADAPTER_CLASS = "elementAdapterClass";  //$NON-NLS-1$

    private static final String TAG_APPLICABLE_TYPE = "applicableType"; //$NON-NLS-1$
    
    /**
     * Creates a descriptor from a configuration element.
     * 
     * @param configElement configuration element to create a descriptor from
     */
    public WorkingSetDescriptor(IConfigurationElement configElement)
            throws CoreException {
        super();
        this.configElement = configElement;
        id = configElement.getAttribute(ATT_ID);
        name = configElement.getAttribute(ATT_NAME);
        icon = configElement.getAttribute(ATT_ICON);
        pageClassName = configElement.getAttribute(ATT_PAGE_CLASS);
        updaterClassName = configElement.getAttribute(ATT_UPDATER_CLASS);

        if (name == null) {
            throw new CoreException(new Status(IStatus.ERROR,
                    WorkbenchPlugin.PI_WORKBENCH, 0,
                    "Invalid extension (missing class name): " + id, //$NON-NLS-1$
                    null));
        }
        
        IConfigurationElement[] containsChildren = configElement
				.getChildren(TAG_APPLICABLE_TYPE);
		if (containsChildren.length > 0) {
			List byClassList = new ArrayList(containsChildren.length);
			List byAdapterList = new ArrayList(containsChildren.length);
			for (int i = 0; i < containsChildren.length; i++) {
				IConfigurationElement child = containsChildren[i];
				String className = child
						.getAttribute(IWorkbenchRegistryConstants.ATT_CLASS);
				if (className != null)
					byClassList.add(className);
				if ("true".equals(child.getAttribute(IWorkbenchRegistryConstants.ATT_ADAPTABLE)))  //$NON-NLS-1$
					byAdapterList.add(className);
			}
			if (!byClassList.isEmpty()) {
				classTypes = (String[]) byClassList.toArray(new String[byClassList
						.size()]);
				Arrays.sort(classTypes);
			}
			
			if (!byAdapterList.isEmpty()) {
				adapterTypes = (String[]) byAdapterList.toArray(new String[byAdapterList
						.size()]);
				Arrays.sort(adapterTypes);
			}
		}
    }
    
    /**
     * Returns the name space that declares this working set.
     * 
     * @return the name space declaring this working set
     */
    public String getDeclaringNamespace() {
    	return configElement.getNamespace();
    }
    
    /**
	 * Return the namespace that contains the class referenced by the
	 * updaterClass. May be the bundle that declared this extension or another
	 * bundle that contains the referenced class.
	 * 
	 * @return the namespace
	 * @since 3.3
	 */
	public String getUpdaterNamespace() {
		return WorkbenchPlugin.getBundleForExecutableExtension(configElement,
				ATT_UPDATER_CLASS).getSymbolicName();
	}
    
    /**
	 * Return the namespace that contains the class referenced by the
	 * elementAdapterClass. May be the bundle that declared this extension or
	 * another bundle that contains the referenced class.
	 * 
	 * @return the namespace
	 * @since 3.3
	 */
	public String getElementAdapterNamespace() {
		return WorkbenchPlugin.getBundleForExecutableExtension(configElement,
				ATT_UPDATER_CLASS).getSymbolicName();
	}

    /**
	 * Creates a working set page from this extension descriptor.
	 * 
	 * @return a working set page created from this extension descriptor.
	 */
    public IWorkingSetPage createWorkingSetPage() {
        Object page = null;

        if (pageClassName != null) {
            try {
                page = WorkbenchPlugin.createExtension(configElement,
                        ATT_PAGE_CLASS);
            } catch (CoreException exception) {
                IStatus status = StatusUtil.newStatus(exception.getStatus(),
						"Unable to create working set page: " + //$NON-NLS-1$
								pageClassName);
				StatusManager.getManager().handle(status);
            }
        }
        return (IWorkingSetPage) page;
    }

    /**
     * Returns the page's icon
     * 
     * @return the page's icon
     */
    public ImageDescriptor getIcon() {
        if (icon == null) {
			return null;
		}

        IExtension extension = configElement.getDeclaringExtension();
        String extendingPluginId = extension.getNamespace();
        return AbstractUIPlugin.imageDescriptorFromPlugin(extendingPluginId,
                icon);
    }

    /**
     * Returns the working set page id.
     * 
     * @return the working set page id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the working set page class name
     * 
     * @return the working set page class name or <code>null</code> if
     *  no page class name has been provided by the extension
     */
    public String getPageClassName() {
        return pageClassName;
    }

    /**
     * Returns the name of the working set element type the 
     * page works with.
     * 
     * @return the working set element type name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the working set updater class name
     * 
     * @return the working set updater class name or <code>null</code> if
     *  no updater class name has been provided by the extension
     */
    public String getUpdaterClassName() {
    	return updaterClassName;
    }
    
    /**
	 * Creates a working set element adapter.
	 * 
	 * @return the element adapter or <code>null</code> if no adapter has been
	 *         declared
	 */
	public IWorkingSetElementAdapter createWorkingSetElementAdapter() {
		if (!WorkbenchPlugin.hasExecutableExtension(configElement, ATT_ELEMENT_ADAPTER_CLASS))
			return null;
		IWorkingSetElementAdapter result = null;
		try {
			result = (IWorkingSetElementAdapter) WorkbenchPlugin
					.createExtension(configElement, ATT_ELEMENT_ADAPTER_CLASS);
		} catch (CoreException exception) {
			IStatus status = StatusUtil.newStatus(exception.getStatus(),
					"Unable to create working set element adapter: " + //$NON-NLS-1$		
							result);
			StatusManager.getManager().handle(status);
		}
		return result;
	}
    
    /**
	 * Creates a working set updater.
	 * 
	 * @return the working set updater or <code>null</code> if no updater has
	 *         been declared
	 */
    public IWorkingSetUpdater createWorkingSetUpdater() {
    	if (updaterClassName == null) {
			return null;
		}
    	IWorkingSetUpdater result = null;
        try {
            result = (IWorkingSetUpdater)WorkbenchPlugin.createExtension(configElement, ATT_UPDATER_CLASS);
        } catch (CoreException exception) {
            IStatus status = StatusUtil.newStatus(exception.getStatus(),
					"Unable to create working set updater: " + //$NON-NLS-1$
							updaterClassName);
			StatusManager.getManager().handle(status);
        }
        return result;   	
    }
    
    public boolean isUpdaterClassLoaded() {
    	return WorkbenchPlugin.isBundleLoadedForExecutableExtension(configElement, ATT_UPDATER_CLASS);
    }
    
    public boolean isElementAdapterClassLoaded() {
    	return WorkbenchPlugin.isBundleLoadedForExecutableExtension(configElement, ATT_ELEMENT_ADAPTER_CLASS);
    }
    
    /**
     * Returns whether working sets based on this descriptor are editable.
     * 
     * @return <code>true</code> if working sets based on this descriptor are editable; otherwise
     *  <code>false</code>
     * 
     * @since 3.1
     */
    public boolean isEditable() {
        return getPageClassName() != null;
    }

	public String getLocalId() {
		return getId();
	}

	public String getPluginId() {
		return getDeclaringNamespace();
	}
	
	/**
	 * Return the config element for this descriptor.
	 * 
	 * @return the config element
	 * @since 3.3
	 */
	IConfigurationElement getConfigurationElement() {
		return configElement;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2932.java