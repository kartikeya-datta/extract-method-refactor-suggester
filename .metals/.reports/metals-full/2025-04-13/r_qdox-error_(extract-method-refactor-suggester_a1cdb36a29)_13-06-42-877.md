error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2051.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2051.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2051.java
text:
```scala
E@@mptyTabFolder folder = new EmptyTabFolder(parent, true);

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

package org.eclipse.ui.presentations;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.preferences.IDynamicPropertyMap;
import org.eclipse.ui.internal.presentations.defaultpresentation.DefaultMultiTabListener;
import org.eclipse.ui.internal.presentations.defaultpresentation.DefaultSimpleTabListener;
import org.eclipse.ui.internal.presentations.defaultpresentation.DefaultTabFolder;
import org.eclipse.ui.internal.presentations.defaultpresentation.DefaultTabPositionListener;
import org.eclipse.ui.internal.presentations.defaultpresentation.DefaultThemeListener;
import org.eclipse.ui.internal.presentations.defaultpresentation.EmptyTabFolder;
import org.eclipse.ui.internal.presentations.util.PresentablePartFolder;
import org.eclipse.ui.internal.presentations.util.StandardEditorSystemMenu;
import org.eclipse.ui.internal.presentations.util.StandardViewSystemMenu;
import org.eclipse.ui.internal.presentations.util.TabbedStackPresentation;

/**
 * The default presentation factory for the Workbench.
 * 
 * @since 3.0
 */
public class WorkbenchPresentationFactory extends AbstractPresentationFactory {

    /* (non-Javadoc)
     * @see org.eclipse.ui.presentations.AbstractPresentationFactory#createEditorPresentation(org.eclipse.swt.widgets.Composite, org.eclipse.ui.presentations.IStackPresentationSite)
     */
    public StackPresentation createEditorPresentation(Composite parent,
            IStackPresentationSite site) {
        DefaultTabFolder folder = new DefaultTabFolder(parent, SWT.BORDER, 
                site.supportsState(IStackPresentationSite.STATE_MINIMIZED), 
                site.supportsState(IStackPresentationSite.STATE_MAXIMIZED));
        
        /*
         * Set the minimum characters to display, if the preference is something
         * other than the default. This is mainly intended for RCP applications
         * or for expert users (i.e., via the plug-in customization file).
         * 
         * Bug 32789.
         */
        final IPreferenceStore store = PlatformUI.getPreferenceStore();
        if (store
                .contains(IWorkbenchPreferenceConstants.EDITOR_MINIMUM_CHARACTERS)) {
            final int minimumCharacters = store
                    .getInt(IWorkbenchPreferenceConstants.EDITOR_MINIMUM_CHARACTERS);
            if (minimumCharacters >= 0) {
                folder.setMinimumCharacters(minimumCharacters);
            }
        }
        
        PresentablePartFolder partFolder = new PresentablePartFolder(folder);
        
        TabbedStackPresentation result = new TabbedStackPresentation(site, partFolder, 
                new StandardEditorSystemMenu(site));
        
        DefaultThemeListener themeListener = new DefaultThemeListener(folder, result.getTheme());
        result.getTheme().addListener(themeListener);
        
        IDynamicPropertyMap workbenchPreferences = result.getPluginPreferences(WorkbenchPlugin.getDefault()); 
        
        DefaultTabPositionListener tabListener = new DefaultTabPositionListener(
                workbenchPreferences, 
                IPreferenceConstants.EDITOR_TAB_POSITION, folder);
        
        DefaultMultiTabListener multiTabListener = new DefaultMultiTabListener(workbenchPreferences,
                IPreferenceConstants.SHOW_MULTIPLE_EDITOR_TABS, folder);

        DefaultSimpleTabListener simpleTabListener = new DefaultSimpleTabListener(
                result.getApiPreferences(),
                IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, folder);        
        
        return result;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.presentations.AbstractPresentationFactory#createViewPresentation(org.eclipse.swt.widgets.Composite, org.eclipse.ui.presentations.IStackPresentationSite)
     */
    public StackPresentation createViewPresentation(Composite parent,
            IStackPresentationSite site) {
        
        DefaultTabFolder folder = new DefaultTabFolder(parent, SWT.BORDER, 
                site.supportsState(IStackPresentationSite.STATE_MINIMIZED), 
                site.supportsState(IStackPresentationSite.STATE_MAXIMIZED));

        PresentablePartFolder partFolder = new PresentablePartFolder(folder);
        
        folder.setUnselectedCloseVisible(false);
        folder.setUnselectedImageVisible(false);
        
        TabbedStackPresentation result = new TabbedStackPresentation(site, partFolder, 
                new StandardViewSystemMenu(site));
        
        DefaultThemeListener themeListener = new DefaultThemeListener(folder, result.getTheme());
        result.getTheme().addListener(themeListener);
        
        DefaultTabPositionListener tabListener = new DefaultTabPositionListener(
                result.getPluginPreferences(WorkbenchPlugin.getDefault()), 
                IPreferenceConstants.VIEW_TAB_POSITION, folder);
        
        DefaultSimpleTabListener simpleTabListener = new DefaultSimpleTabListener(result.getApiPreferences(),
                IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, folder);

        return result;
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.presentations.AbstractPresentationFactory#createStandaloneViewPresentation(org.eclipse.swt.widgets.Composite, org.eclipse.ui.presentations.IStackPresentationSite, boolean)
     */
    public StackPresentation createStandaloneViewPresentation(Composite parent,
            IStackPresentationSite site, boolean showTitle) {
        
        if (showTitle) {
            return createViewPresentation(parent, site);
        } else {
            EmptyTabFolder folder = new EmptyTabFolder(parent);
            TabbedStackPresentation presentation = new TabbedStackPresentation(site, folder, new StandardViewSystemMenu(site));
            
            return presentation;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2051.java