error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9088.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9088.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9088.java
text:
```scala
d@@ebug("notifyOthersDeactivated(" + id + ")");

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

package org.eclipse.ecf.provider.generic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.provider.Trace;
import org.eclipse.ecf.provider.generic.gmm.GMMImpl;
import org.eclipse.ecf.provider.generic.gmm.Member;
import org.eclipse.ecf.provider.generic.gmm.MemberChanged;

class SOContainerGMM implements Observer {
    static Trace debug = Trace.create("gmm");
    SOContainer container;
    Member localMember;
    GMMImpl groupManager;
    // Maximum number of members. Default is -1 (no maximum).
    int maxMembers = -1;
    TreeMap loading, active;

    SOContainerGMM(SOContainer cont, Member local) {
        container = cont;
        groupManager = new GMMImpl();
        groupManager.addObserver(this);
        loading = new TreeMap();
        active = new TreeMap();
        localMember = local;
        addMember(local);
        debug("<init>");
    }

    protected void debug(String msg) {
        if (Trace.ON && debug != null) {
            debug.msg(msg + ":" + container.getID());
        }
    }

    protected void dumpStack(String msg, Throwable e) {
        if (Trace.ON && debug != null) {
            debug.dumpStack(e, msg + ":" + container.getID());
        }
    }

    ID[] getSharedObjectIDs() {
        return getActiveKeys();
    }

    synchronized boolean addMember(Member m) {
        debug("addMember(" + m.getID() + ")");
        if (maxMembers > 0 && getSize() > maxMembers) {
            return false;
        } else {
            return groupManager.addMember(m);
        }
    }

    synchronized int setMaxMembers(int max) {
        debug("setMaxMembers(" + max + ")");
        int old = maxMembers;
        maxMembers = max;
        return old;
    }

    synchronized int getMaxMembers() {
        return maxMembers;
    }

    synchronized boolean removeMember(Member m) {
        debug("removeMember(" + m.getID() + ")");
        boolean res = groupManager.removeMember(m);
        if (res) {
            removeSharedObjects(m);
        }
        return res;
    }

    synchronized boolean removeMember(ID id) {
        debug("removeMember(" + id + ")");
        Member m = getMemberForID(id);
        if (m == null)
            return false;
        return removeMember(m);
    }

    void removeAllMembers() {
        removeAllMembers(null);
    }

    void removeNonLocalMembers() {
        removeAllMembers(localMember);
    }

    synchronized void removeAllMembers(Member exception) {
        if (exception == null) {
            debug("removeAllMembers()");
        } else {
            debug("removeAllMembers(" + exception.getID() + ")");
        }
        Object m[] = getMembers();
        for (int i = 0; i < m.length; i++) {
            Member mem = (Member) m[i];
            if (exception == null || !exception.equals(mem))
                removeMember(mem);
        }
    }

    synchronized Object[] getMembers() {
        return groupManager.getMembers();
    }

    synchronized ID[] getOtherMemberIDs() {
        return groupManager.getMemberIDs(localMember.getID());
    }

    synchronized ID[] getMemberIDs() {
        return groupManager.getMemberIDs(null);
    }

    synchronized Member getMemberForID(ID id) {
        debug("getMemberForID(" + id + ")");
        Member newMem = new Member(id);
        for (Iterator i = iterator(); i.hasNext();) {
            Member oldMem = (Member) i.next();
            if (newMem.equals(oldMem))
                return oldMem;
        }
        return null;
    }

    synchronized int getSize() {
        return groupManager.getSize();
    }

    synchronized boolean containsMember(Member m) {
        if (m != null) {
            debug("containsMember(" + m.getID() + ")");
        }
        return groupManager.containsMember(m);
    }

    synchronized Iterator iterator() {
        return groupManager.iterator();
    }

    // End group membership change methods
    synchronized boolean addSharedObject(SOWrapper ro) {
        if (ro != null)
            debug("addSharedObject(" + ro.getObjID() + ")");
        if (getFromAny(ro.getObjID()) != null)
            return false;
        addSharedObjectToActive(ro);
        return true;
    }

    synchronized boolean addLoadingSharedObject(
            SOContainer.LoadingSharedObject lso) {
        if (lso != null)
            debug("addLoadingSharedObject(" + lso.getID() + ")");
        if (getFromAny(lso.getID()) != null)
            return false;
        loading.put(lso.getID(), new SOWrapper(lso, container));
        // And start the thing
        lso.start();
        return true;
    }

    synchronized void moveSharedObjectFromLoadingToActive(SOWrapper ro) {
        if (ro != null)
            debug("moveSharedObjectFromLoadingToActive(" + ro.getObjID() + ")");
        if (removeSharedObjectFromLoading(ro.getObjID()))
            addSharedObjectToActive(ro);
    }

    boolean removeSharedObjectFromLoading(ID id) {
        debug("removeSharedObjectFromLoading(" + id + ")");
        if (loading.remove(id) != null) {
            return true;
        } else
            return false;
    }

    synchronized ID[] getActiveKeys() {
        return (ID[]) active.keySet().toArray(new ID[0]);
    }

    void addSharedObjectToActive(SOWrapper so) {
        if (so != null)
            debug("addSharedObjectToActive(" + so.getObjID() + ")");
        ID[] ids = getActiveKeys();
        active.put(so.getObjID(), so);
        so.activated(ids);
    }

    synchronized void notifyOthersActivated(ID id) {
        debug("notifyOthersActivated(" + id + ")");
        notifyOtherChanged(id, active, true);
    }

    synchronized void notifyOthersDeactivated(ID id) {
        debug("notifyOthersActivated(" + id + ")");
        notifyOtherChanged(id, active, false);
    }

    void notifyOtherChanged(ID id, TreeMap aMap, boolean activated) {
        for (Iterator i = aMap.values().iterator(); i.hasNext();) {
            SOWrapper other = (SOWrapper) i.next();
            if (!id.equals(other.getObjID())) {
                other.otherChanged(id, activated);
            }
        }
    }

    synchronized boolean removeSharedObject(ID id) {
        debug("removeSharedObject(" + id + ")");
        SOWrapper ro = removeFromMap(id, active);
        if (ro == null)
            return false;
        ro.deactivated();
        return true;
    }

    synchronized SOWrapper getFromMap(ID objID, TreeMap aMap) {
        return (SOWrapper) aMap.get(objID);
    }

    synchronized SOWrapper removeFromMap(ID objID, TreeMap aMap) {
        return (SOWrapper) aMap.remove(objID);
    }

    SOWrapper getFromLoading(ID objID) {
        return getFromMap(objID, loading);
    }

    SOWrapper getFromActive(ID objID) {
        return getFromMap(objID, active);
    }

    synchronized SOWrapper getFromAny(ID objID) {
        SOWrapper ro = getFromMap(objID, active);
        if (ro != null)
            return ro;
        ro = getFromMap(objID, loading);
        return ro;
    }

    // Notification methods
    void notifyAllOfMemberChange(Member m, TreeMap map, boolean add) {
        for (Iterator i = map.values().iterator(); i.hasNext();) {
            SOWrapper ro = (SOWrapper) i.next();
            ro.memberChanged(m, add);
        }
    }

    public void update(Observable o, Object arg) {
        MemberChanged mc = (MemberChanged) arg;
        notifyAllOfMemberChange(mc.getMember(), active, mc.getAdded());
    }

    synchronized void removeSharedObjects(Member m) {
        removeSharedObjects(m, true);
    }

    synchronized void clear() {
        debug("clear()");
        removeSharedObjects(null, true);
    }

    void removeSharedObjects(Member m, boolean match) {
        HashSet set = getRemoveIDs(m.getID(), match);
        Iterator i = set.iterator();
        while (i.hasNext()) {
            ID removeID = (ID) i.next();
            if (isLoading(removeID)) {
                removeSharedObjectFromLoading(removeID);
            } else {
                container.destroySharedObject(removeID);
            }
        }
    }

    HashSet getRemoveIDs(ID homeID, boolean match) {
        HashSet aSet = new HashSet();
        for (Iterator i = new DestroyIterator(loading, homeID, match); i
                .hasNext();) {
            aSet.add(i.next());
        }
        for (Iterator i = new DestroyIterator(active, homeID, match); i
                .hasNext();) {
            aSet.add(i.next());
        }
        return aSet;
    }

    synchronized boolean isActive(ID id) {
        return active.containsKey(id);
    }

    synchronized boolean isLoading(ID id) {
        return loading.containsKey(id);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SOContainerGMM[");
        sb.append(groupManager);
        sb.append(";load:").append(loading);
        sb.append(";active:").append(active).append("]");
        return sb.toString();
    }
}

class DestroyIterator implements Iterator {
    ID next;
    ID homeID;
    Iterator i;
    boolean match;

    public DestroyIterator(TreeMap map, ID hID, boolean m) {
        i = map.values().iterator();
        homeID = hID;
        next = null;
        match = m;
    }

    public boolean hasNext() {
        if (next == null)
            next = getNext();
        return (next != null);
    }

    public Object next() {
        if (hasNext()) {
            ID value = next;
            next = null;
            return value;
        } else {
            throw new java.util.NoSuchElementException();
        }
    }

    ID getNext() {
        while (i.hasNext()) {
            SOWrapper ro = (SOWrapper) i.next();
            if (homeID == null || (match ^ !ro.getHomeID().equals(homeID))) {
                return ro.getObjID();
            }
        }
        return null;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
 No newline at end of file
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9088.java