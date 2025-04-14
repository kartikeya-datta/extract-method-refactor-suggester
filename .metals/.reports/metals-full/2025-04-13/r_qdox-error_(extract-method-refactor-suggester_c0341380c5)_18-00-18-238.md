error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8721.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8721.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[105,2]

error in qdox parser
file content:
```java
offset: 3451
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8721.java
text:
```scala
package org.eclipse.ecf.internal.example.collab.ui;

/****************************************************************************
* Copyright (c) 2004 Composent, Inc. and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Composent, Inc. - initial API and implementation
*****************************************************************************/

package org.eclipse.ecf.example.collab.ui;

import org.eclipse.ecf.example.collab.share.User;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;



class TreeDropTarget extends ChatDropTarget {
    public TreeDropTarget(LineChatClientView view, Control control, ChatComposite comp) {
        super(view,control, comp);
    }
    protected Object getData(DropTargetEvent event) {
        Object item = event.item;
        if (item != null && item instanceof Widget) {
            Widget titem = (Widget) item;
            Object obj = titem.getData();
            return obj;
        }
        return null;
    }
    private User findUserNode(Object to) {
        if (to == null)
            return null;
        if (to instanceof TreeUser) {
            TreeUser tu = (TreeUser) to;
            return tu.getUser();
        }
        if (to instanceof TreeParent) {
            return findUserNode(((TreeParent) to).getParent());
        }
        return null;
    }
    protected User isUserHit(Object item) {
        return findUserNode(item);
    }
    public void dragEnter(DropTargetEvent event) {
        if (event.detail == DND.DROP_DEFAULT) {
            if ((event.operations & DND.DROP_COPY) != 0) {
                event.detail = DND.DROP_COPY;
            } else {
                event.detail = DND.DROP_NONE;
            }
        }
        Object item = getData(event);
        if (item == null) {
            event.detail = DND.DROP_NONE;
            return;
        } else {
            User user = isUserHit(item);
            if (user == null) {
                event.detail = DND.DROP_NONE;
                return;
            }
            selectedUser = user;
        }
        // will accept text but prefer to have files dropped
        for (int i = 0; i < event.dataTypes.length; i++) {
            if (fileTransfer.isSupportedType(event.dataTypes[i])) {
                event.currentDataType = event.dataTypes[i];
                // files should only be copied
                if (event.detail != DND.DROP_COPY) {
                    event.detail = DND.DROP_NONE;
                    selectedUser = null;
                }
                break;
            }
        }
    }
    public void dragOver(DropTargetEvent event) {
        if ((event.operations & DND.DROP_COPY) != 0) {
            event.detail = DND.DROP_COPY;
        } else {
            event.detail = DND.DROP_NONE;
        }
        Object item = getData(event);
        if (item == null) {
            event.detail = DND.DROP_NONE;
            return;
        } else {
            User user = isUserHit(item);
            if (user == null) {
                event.detail = DND.DROP_NONE;
                return;
            }
            selectedUser = user;
        }
    }
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8721.java