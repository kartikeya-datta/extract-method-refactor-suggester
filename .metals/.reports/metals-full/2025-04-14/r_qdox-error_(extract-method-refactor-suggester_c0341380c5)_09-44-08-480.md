error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5318.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5318.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5318.java
text:
```scala
r@@eadContributions(id, IWorkbenchRegistryConstants.TAG_VIEWER_CONTRIBUTION,

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.misc.Assert;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;

/**
 * This class reads the registry for extensions that plug into
 * 'popupMenus' extension point and deals only with the 'viewerContribution'
 * elements.
 */
public class ViewerActionBuilder extends PluginActionBuilder {
    

    private ISelectionProvider provider;

    private IWorkbenchPart part;

    /**
     * Basic contstructor
     */
    public ViewerActionBuilder() {
    }

    /* (non-Javadoc)
     * Method declared on PluginActionBuilder.
     */
    protected ActionDescriptor createActionDescriptor(
            IConfigurationElement element) {
        if (part instanceof IViewPart)
			return new ActionDescriptor(element, ActionDescriptor.T_VIEW, part);
		return new ActionDescriptor(element, ActionDescriptor.T_EDITOR, part);
    }

    /* (non-Javadoc)
     * Method declared on PluginActionBuilder.
     */
    protected BasicContribution createContribution() {
        return new ViewerContribution(provider);
    }

    /**
     * Dispose of the action builder
     */
    public void dispose() {
        if (cache != null) {
            for (int i = 0; i < cache.size(); i++) {
                ((BasicContribution) cache.get(i)).dispose();
            }
            cache = null;
        }
    }

    /* (non-Javadoc)
     * Method declared on PluginActionBuilder.
     */
    protected boolean readElement(IConfigurationElement element) {
        String tag = element.getName();

        // Found visibility sub-element
        if (currentContribution != null && tag.equals(IWorkbenchRegistryConstants.TAG_VISIBILITY)) {
            ((ViewerContribution) currentContribution)
                    .setVisibilityTest(element);
            return true;
        }

        return super.readElement(element);
    }

    /**
     * Reads the contributions for a viewer menu.
     * This method is typically used in conjunction with <code>contribute</code> to read
     * and then insert actions for a particular viewer menu.
     *
     * @param id the menu id
     * @param prov the selection provider for the control containing the menu
     * @param part the part containing the menu.
     * @return <code>true</code> if 1 or more items were read.  
     */
    public boolean readViewerContributions(String id, ISelectionProvider prov,
            IWorkbenchPart part) {
		Assert.isTrue(part instanceof IViewPart || part instanceof IEditorPart);
        provider = prov;
        this.part = part;
        readContributions(id, IWorkbenchRegistryConstants.TAG_CONTRIBUTION_TYPE,
                IWorkbenchConstants.PL_POPUP_MENU);
        return (cache != null);
    }

    /**
     * Helper class to collect the menus and actions defined within a
     * contribution element.
     */
    private static class ViewerContribution extends BasicContribution implements ISelectionChangedListener {
        private ISelectionProvider selProvider;

        private ActionExpression visibilityTest;

        /**
         * Create a new ViewerContribution.
         * 
         * @param selProvider the selection provider
         */
        public ViewerContribution(ISelectionProvider selProvider) {
            super();
            this.selProvider = selProvider;
			if (selProvider != null) {
				selProvider.addSelectionChangedListener(this);
			}
        }

        /**
         * Set the visibility test.
         * 
         * @param element the element
         */
        public void setVisibilityTest(IConfigurationElement element) {
            visibilityTest = new ActionExpression(element);
        }

        /* (non-Javadoc)
         * Method declared on BasicContribution.
         */
        public void contribute(IMenuManager menu, boolean menuAppendIfMissing,
                IToolBarManager toolbar, boolean toolAppendIfMissing) {
            boolean visible = true;

            if (visibilityTest != null) {
                ISelection selection = selProvider.getSelection();
                if (selection instanceof IStructuredSelection) {
                    visible = visibilityTest
                            .isEnabledFor((IStructuredSelection) selection);
                } else {
                    visible = visibilityTest.isEnabledFor(selection);
                }
            }

            if (visible)
                super.contribute(menu, menuAppendIfMissing, toolbar,
                        toolAppendIfMissing);
        }
		
		/* (non-Javadoc)
		 * @see org.eclipse.ui.internal.PluginActionBuilder.BasicContribution#dispose()
		 */
		public void dispose() {
			if (selProvider != null) {
				selProvider.removeSelectionChangedListener(this);
			}
			disposeActions();
			super.dispose();
		}

		/**
		 * Rather than hooking up each action as a selection listener,
		 * the contribution itself is added, and propagates
		 * the selection changed notification to all actions.
		 * This simplifies cleanup, in addition to potentially reducing the number of listeners.
		 * 
		 * @see ISelectionChangedListener
		 * @since 3.1
		 */
		public void selectionChanged(SelectionChangedEvent event) {
			if (actions != null) {
				if (actions != null) {
					for (int i = 0; i < actions.size(); i++) {
						PluginAction proxy = ((ActionDescriptor) actions.get(i))
								.getAction();
						proxy.selectionChanged(event);
					}
				}
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5318.java