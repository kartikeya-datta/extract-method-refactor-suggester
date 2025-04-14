error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3863.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3863.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3863.java
text:
```scala
.@@log("Unable to create ActionSet: " + desc.getId(), e);//$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.SubActionBars;
import org.eclipse.ui.internal.registry.IActionSet;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;

/**
 * Manage the configurable actions for one window.
 */
public class ActionPresentation {
    private WorkbenchWindow window;

    private HashMap mapDescToRec = new HashMap(3);

    private HashMap invisibleBars = new HashMap(3);

    private class SetRec {
        public SetRec(IActionSetDescriptor desc, IActionSet set,
                SubActionBars bars) {
            this.desc = desc;
            this.set = set;
            this.bars = bars;
        }

        public IActionSetDescriptor desc;

        public IActionSet set;

        public SubActionBars bars;
    }

    /**
     * ActionPresentation constructor comment.
     */
    public ActionPresentation(WorkbenchWindow window) {
        super();
        this.window = window;
    }

    /**
     * Remove all action sets.
     */
    public void clearActionSets() {
        // Get all of the action sets -- both visible and invisible.
        final List oldList = new ArrayList();
        oldList.addAll(mapDescToRec.keySet());
        oldList.addAll(invisibleBars.keySet());

        Iterator iter = oldList.iterator();
        while (iter.hasNext()) {
            IActionSetDescriptor desc = (IActionSetDescriptor) iter.next();
            removeActionSet(desc);
        }
    }

    /**
     * Destroy an action set.
     */
    public void removeActionSet(IActionSetDescriptor desc) {
        SetRec rec = (SetRec) mapDescToRec.remove(desc);
        if (rec == null) {
            rec = (SetRec) invisibleBars.remove(desc);
        }
        if (rec != null) {
            IActionSet set = rec.set;
            SubActionBars bars = rec.bars;
            if (bars != null) {
                bars.dispose();
            }
            if (set != null) {
                set.dispose();
            }
        }
    }

    /**
     * Sets the list of visible action set.
     */
    public void setActionSets(IActionSetDescriptor[] newArray) {
        // Convert array to list.
        HashSet newList = new HashSet();
        
        for (int i = 0; i < newArray.length; i++) {
            IActionSetDescriptor descriptor = newArray[i];
            
            newList.add(descriptor);
        }
        List oldList = new ArrayList(mapDescToRec.keySet());

        // Remove obsolete actions.
        Iterator iter = oldList.iterator();
        while (iter.hasNext()) {
            IActionSetDescriptor desc = (IActionSetDescriptor) iter.next();
            if (!newList.contains(desc)) {
                SetRec rec = (SetRec) mapDescToRec.get(desc);
                if (rec != null) {
                    mapDescToRec.remove(desc);
                    IActionSet set = rec.set;
                    SubActionBars bars = rec.bars;
                    if (bars != null) {
                        SetRec invisibleRec = new SetRec(desc, set, bars);
                        invisibleBars.put(desc, invisibleRec);
                        bars.deactivate();
                    }
                }
            }
        }

        // Add new actions.
        ArrayList sets = new ArrayList();
        
        for (int i = 0; i < newArray.length; i++) {
            IActionSetDescriptor desc = newArray[i];

            if (!mapDescToRec.containsKey(desc)) {
                try {
                    SetRec rec;
                    // If the action bars and sets have already been created
                    // then
                    // reuse those action sets
                    if (invisibleBars.containsKey(desc)) {
                        rec = (SetRec) invisibleBars.get(desc);
                        if (rec.bars != null) {
                            rec.bars.activate();
                        }
                        invisibleBars.remove(desc);
                    } else {
                        IActionSet set = desc.createActionSet();
                        SubActionBars bars = new ActionSetActionBars(window
                                .getActionBars(), desc.getId());
                        rec = new SetRec(desc, set, bars);
                        set.init(window, bars);
                        sets.add(set);
                    }
                    mapDescToRec.put(desc, rec);
                } catch (CoreException e) {
                    WorkbenchPlugin
                            .log("Unable to create ActionSet: " + desc.getId());//$NON-NLS-1$
                }
            }
        }
        // We process action sets in two passes for coolbar purposes. First we
        // process base contributions
        // (i.e., actions that the action set contributes to its toolbar), then
        // we process adjunct contributions
        // (i.e., actions that the action set contributes to other toolbars).
        // This type of processing is
        // necessary in order to maintain group order within a coolitem.
        PluginActionSetBuilder.processActionSets(sets, window);

        iter = sets.iterator();
        while (iter.hasNext()) {
            PluginActionSet set = (PluginActionSet) iter.next();
            set.getBars().activate();
        }
    }

    /**
     */
    public IActionSet[] getActionSets() {
        Collection setRecCollection = mapDescToRec.values();
        IActionSet result[] = new IActionSet[setRecCollection.size()];
        int i = 0;
        for (Iterator iterator = setRecCollection.iterator(); iterator
                .hasNext(); i++)
            result[i] = ((SetRec) iterator.next()).set;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3863.java