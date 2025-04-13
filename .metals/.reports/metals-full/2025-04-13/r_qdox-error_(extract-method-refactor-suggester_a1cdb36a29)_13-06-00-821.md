error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14736.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14736.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14736.java
text:
```scala
d@@ata.length > 1 ? (String[]) data[1] : null);

/*******************************************************************************
 * Copyright (c) 2004 Peter Nehrer and Composent, Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Peter Nehrer - initial API and implementation
 *******************************************************************************/
package org.eclipse.ecf.example.sdo.editor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.ecf.core.ISharedObject;
import org.eclipse.ecf.core.ISharedObjectConfig;
import org.eclipse.ecf.core.SharedObjectInitException;
import org.eclipse.ecf.core.events.ISharedObjectActivatedEvent;
import org.eclipse.ecf.core.events.ISharedObjectContainerDepartedEvent;
import org.eclipse.ecf.core.events.ISharedObjectContainerJoinedEvent;
import org.eclipse.ecf.core.events.ISharedObjectDeactivatedEvent;
import org.eclipse.ecf.core.events.ISharedObjectMessageEvent;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.util.ECFException;
import org.eclipse.ecf.core.util.Event;

/**
 * @author pnehrer
 */
class PublishedGraphTracker extends PlatformObject implements ISharedObject {

    private static final String[] NO_PATHS = {};

    private static final int JOIN = 0;

    private static final int LEAVE = 1;

    private static final int ADD = 2;

    private static final int REMOVE = 3;

    private class Table {

        private final Hashtable paths = new Hashtable();

        private final Hashtable containers = new Hashtable();

        public synchronized void add(ID containerID, String[] path) {
            HashSet list = (HashSet) paths.get(containerID);
            if (list == null) {
                list = new HashSet();
                paths.put(containerID, list);
            }

            list.addAll(Arrays.asList(path));
            for (int i = 0; i < path.length; ++i) {
                list = (HashSet) containers.get(path[i]);
                if (list == null) {
                    list = new HashSet();
                    containers.put(path[i], list);
                }

                list.add(containerID);
            }
        }

        public synchronized void remove(ID containerID, String path) {
            HashSet list = (HashSet) paths.get(containerID);
            if (list != null) {
                list.remove(path);
                if (list.isEmpty())
                    paths.remove(containerID);
            }

            list = (HashSet) containers.get(path);
            if (list != null) {
                list.remove(containerID);
                if (list.isEmpty())
                    containers.remove(path);
            }
        }

        public synchronized void remove(ID containerID) {
            HashSet list = (HashSet) paths.get(containerID);
            if (list != null) {
                for (Iterator i = list.iterator(); i.hasNext();) {
                    String path = (String) i.next();
                    list = (HashSet) containers.get(path);
                    if (list != null) {
                        list.remove(containerID);
                        if (list.isEmpty())
                            containers.remove(path);
                    }
                }
            }
        }

        public synchronized boolean contains(String path) {
            return containers.contains(path);
        }

        public synchronized String[] getPaths(ID containerID) {
            HashSet list = (HashSet) paths.get(containerID);
            return list == null ? NO_PATHS : (String[]) list
                    .toArray(new String[list.size()]);
        }
    }

    private final Table table = new Table();

    private ISharedObjectConfig config;

    public synchronized void add(String path) throws ECFException {
        if (config == null)
            throw new ECFException("Not connected.");

        String[] paths = new String[] { path };
        try {
            config.getContext().sendMessage(null,
                    new Object[] { new Integer(ADD), paths });
        } catch (IOException e) {
            throw new ECFException(e);
        }

        handleAdd(config.getContext().getLocalContainerID(), paths);
    }

    public synchronized void remove(String path) throws ECFException {
        if (config == null)
            throw new ECFException("Not connected.");

        try {
            config.getContext().sendMessage(null,
                    new Object[] { new Integer(REMOVE), path });
        } catch (IOException e) {
            throw new ECFException(e);
        }

        handleRemove(config.getContext().getLocalContainerID(), path);
    }

    public synchronized boolean isPublished(String path) {
        return table.contains(path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObject#init(org.eclipse.ecf.core.ISharedObjectConfig)
     */
    public synchronized void init(ISharedObjectConfig initData)
            throws SharedObjectInitException {
        if (config == null)
            config = initData;
        else
            throw new SharedObjectInitException("Already initialized.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObject#handleEvent(org.eclipse.ecf.core.util.Event)
     */
    public void handleEvent(Event event) {
        if (event instanceof ISharedObjectMessageEvent) {
            ISharedObjectMessageEvent e = (ISharedObjectMessageEvent) event;
            Object[] data = (Object[]) e.getData();
            Integer type = (Integer) data[0];
            switch (type.intValue()) {
            case JOIN:
                handleJoin(e.getRemoteContainerID(),
                        data.length > 2 ? (String[]) data[1] : null);
                break;

            case LEAVE:
                handleLeave(e.getRemoteContainerID());
                break;

            case ADD:
                handleAdd(e.getRemoteContainerID(), (String[]) data[1]);
                break;

            case REMOVE:
                handleRemove(e.getRemoteContainerID(), (String) data[1]);
            }
        } else if (event instanceof ISharedObjectContainerJoinedEvent) {
            if (((ISharedObjectContainerJoinedEvent) event)
                    .getJoinedContainerID().equals(
                            config.getContext().getLocalContainerID()))
                handleJoined();
        } else if (event instanceof ISharedObjectContainerDepartedEvent) {
            ISharedObjectContainerDepartedEvent e = (ISharedObjectContainerDepartedEvent) event;
            if (!e.getDepartedContainerID().equals(
                    config.getContext().getLocalContainerID()))
                handleLeave(e.getDepartedContainerID());
        } else if (event instanceof ISharedObjectActivatedEvent) {
            if (((ISharedObjectActivatedEvent) event).getActivatedID().equals(
                    config.getSharedObjectID()))
                handleJoined();
        } else if (event instanceof ISharedObjectDeactivatedEvent) {
            if (((ISharedObjectDeactivatedEvent) event).getDeactivatedID()
                    .equals(config.getSharedObjectID()))
                handleDeactivated();
        }
    }

    private void handleJoin(ID containerID, String[] paths) {
        if (paths != null)
            table.add(containerID, paths);

        paths = table.getPaths(config.getContext().getLocalContainerID());
        if (paths.length > 0)
            try {
                config.getContext().sendMessage(containerID,
                        new Object[] { new Integer(ADD), paths });
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    private void handleLeave(ID containerID) {
        table.remove(containerID);
    }

    private void handleAdd(ID containerID, String[] paths) {
        table.add(containerID, paths);
    }

    private void handleRemove(ID containerID, String path) {
        table.remove(containerID, path);
    }

    private void handleJoined() {
        String[] paths = table.getPaths(config.getContext()
                .getLocalContainerID());
        Object[] data = paths.length == 0 ? new Object[] { new Integer(JOIN) }
                : new Object[] { new Integer(JOIN), paths };
        try {
            config.getContext().sendMessage(null, data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void handleDeactivated() {
        try {
            config.getContext().sendMessage(null,
                    new Object[] { new Integer(LEAVE) });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObject#handleEvents(org.eclipse.ecf.core.util.Event[])
     */
    public void handleEvents(Event[] events) {
        for (int i = 0; i < events.length; ++i)
            handleEvent(events[i]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ecf.core.ISharedObject#dispose(org.eclipse.ecf.core.identity.ID)
     */
    public synchronized void dispose(ID containerID) {
        if (config != null
                && config.getContext().getLocalContainerID()
                        .equals(containerID))
            config = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14736.java