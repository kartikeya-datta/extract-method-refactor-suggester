error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6491.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6491.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6491.java
text:
```scala
protected A@@rrayList<SFSBContextHandle> initialValue() {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.jpa.container;

import org.jboss.as.jpa.spi.SFSBContextHandle;

import javax.persistence.EntityManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * For tracking of SFSB call stack on a per thread basis.
 * When a SFSB with an extended persistence context (XPC) is injected, the SFSB call stack is searched for
 * a XPC that can be inherited from.
 *
 * @author Scott Marlow
 */
public class SFSBCallStack {

    /**
     * Each thread will have its own list of SFSB invocations in progress.
     */
    private static ThreadLocal<ArrayList<SFSBContextHandle>> SFSBInvocationStack = new ThreadLocal<ArrayList<SFSBContextHandle>>() {
        protected synchronized ArrayList<SFSBContextHandle> initialValue() {
            return new ArrayList<SFSBContextHandle>();
        }
    };

    /**
     * For the current thread, look at the call stack of SFSB invocations and return the first extended
     * persistence context that is based on puName.
     *
     * @param puScopedName Scoped pu name
     * @return the found XPC that matches puName or null if not found
     */
    public static EntityManager findPersistenceContext(String puScopedName) {
        // TODO: arrange for a more optimal datastructure for this
        for (SFSBContextHandle handle : currentSFSBCallStack()) {
            List<WeakReference<EntityManager>> xpcs = SFSBXPCMap.getINSTANCE().getXPC(handle);
            if (xpcs == null)
                continue;
            for (WeakReference<EntityManager> xpc_ref : xpcs) {
                EntityManager xpc = xpc_ref.get();
                if (xpc != null && xpc.unwrap(EntityManagerMetadata.class).getScopedPuName().equals(puScopedName)) {
                    return xpc;
                }
            }
        }
        return null;
    }

    /**
     * Return the current SFSB call stack
     *
     * @return call stack (may be empty but never null)
     */
    public static ArrayList<SFSBContextHandle> currentSFSBCallStack() {
        return SFSBInvocationStack.get();
    }

    /**
     * Push the passed SFSB context handle onto the invocation call stack
     *
     * @param beanContextHandle
     */
    public static void pushCall(SFSBContextHandle beanContextHandle) {
        currentSFSBCallStack().add(beanContextHandle);
    }

    /**
     * Pops the current SFSB invocation off the invocation call stack
     *
     * @return the popped SFSB context handle
     */
    public static SFSBContextHandle popCall() {
        ArrayList<SFSBContextHandle> stack = currentSFSBCallStack();
        SFSBContextHandle result = stack.remove(stack.size() - 1);
        stack.trimToSize();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6491.java