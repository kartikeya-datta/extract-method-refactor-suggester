error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3786.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3786.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3786.java
text:
```scala
I@@EditorDescriptor desc) {

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal;

import java.util.ArrayList;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

/**
 * This class reads the registry for extensions that plug into
 * 'editorActions' extension point.
 */
public class EditorActionBuilder extends PluginActionBuilder {
    private static final String TAG_CONTRIBUTION_TYPE = "editorContribution"; //$NON-NLS-1$

    /**
     * The constructor.
     */
    public EditorActionBuilder() {
    }

    /* (non-Javadoc)
     * Method declared on PluginActionBuilder.
     */
    protected ActionDescriptor createActionDescriptor(
            IConfigurationElement element) {
        return new ActionDescriptor(element, ActionDescriptor.T_EDITOR);
    }

    /* (non-Javadoc)
     * Method declared on PluginActionBuilder.
     */
    protected BasicContribution createContribution() {
        return new EditorContribution();
    }

    /**
     * Reads and apply all external contributions for this editor's ID
     * registered in 'editorActions' extension point.
     */
    public IEditorActionBarContributor readActionExtensions(
            IEditorDescriptor desc, IActionBars bars) {
        ExternalContributor ext = null;
        readContributions(desc.getId(), TAG_CONTRIBUTION_TYPE,
                IWorkbenchConstants.PL_EDITOR_ACTIONS);
        if (cache != null) {
            ext = new ExternalContributor(cache);
            cache = null;
        }
        return ext;
    }

    /**
     * Helper class to collect the menus and actions defined within a
     * contribution element.
     */
    private static class EditorContribution extends BasicContribution {
        public void dispose() {
            if (actions != null) {
                for (int i = 0; i < actions.size(); i++) {
                    PluginAction proxy = ((ActionDescriptor) actions.get(i))
                            .getAction();
                    if (proxy.getDelegate() instanceof IActionDelegate2)
                        ((IActionDelegate2) proxy.getDelegate()).dispose();
                }
            }
        }

        public void editorChanged(IEditorPart editor) {
            if (actions != null) {
                for (int i = 0; i < actions.size(); i++) {
                    ActionDescriptor ad = (ActionDescriptor) actions.get(i);
                    EditorPluginAction action = (EditorPluginAction) ad
                            .getAction();
                    action.editorChanged(editor);
                }
            }
        }
    }

    /**
     * Helper class that will populate the menu and toobar with the external
     * editor contributions.
     */
    public static class ExternalContributor implements
            IEditorActionBarContributor {
        private ArrayList cache;

        public ExternalContributor(ArrayList cache) {
            this.cache = cache;
        }

        public void dispose() {
            for (int i = 0; i < cache.size(); i++) {
                ((EditorContribution) cache.get(i)).dispose();
            }
        }

        public ActionDescriptor[] getExtendedActions() {
            ArrayList results = new ArrayList();
            for (int i = 0; i < cache.size(); i++) {
                EditorContribution ec = (EditorContribution) cache.get(i);
                if (ec.actions != null)
                    results.addAll(ec.actions);
            }
            return (ActionDescriptor[]) results
                    .toArray(new ActionDescriptor[results.size()]);
        }

        public void init(IActionBars bars, IWorkbenchPage page) {
            for (int i = 0; i < cache.size(); i++) {
                ((EditorContribution) cache.get(i)).contribute(bars
                        .getMenuManager(), false, bars.getToolBarManager(),
                        true);
            }
        }

        public void setActiveEditor(IEditorPart editor) {
            for (int i = 0; i < cache.size(); i++) {
                ((EditorContribution) cache.get(i)).editorChanged(editor);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3786.java