error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3743.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3743.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3743.java
text:
```scala
c@@ontext.proxyFields(true, false);

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.kernel;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.lib.util.Localizer;

/**
 * Lifecycle state.
 * Represents a persistent instance that is not transactional, but that
 * allows access to persistent data. This state is reachable only if the
 * RetainState property is set.
 *
 * @author Abe White
 */
@SuppressWarnings("serial")
class PNonTransState
    extends PCState {

    private static final Localizer _loc = Localizer.forPackage
        (PNonTransState.class);

    void initialize(StateManagerImpl context) {
        context.setDirty(false);
        context.clearSavedFields();

        // spec says all proxies to second class objects should be reset
        context.proxyFields(true, true);
    }

    PCState delete(StateManagerImpl context) {
        context.preDelete();
        if (!context.getBroker().isActive())
            return PNONTRANSDELETED;
        return PDELETED;
    }

    PCState transactional(StateManagerImpl context) {
        // state is discarded when entering the transaction
        if (!context.getBroker().getOptimistic()
 context.getBroker().getAutoClear() == AutoClear.CLEAR_ALL)
            context.clearFields();
        return PCLEAN;
    }

    PCState release(StateManagerImpl context) {
        return TRANSIENT;
    }

    PCState evict(StateManagerImpl context) {
        return HOLLOW;
    }

    PCState beforeRead(StateManagerImpl context, int field) {
        // state is discarded when entering the transaction
        context.clearFields();
        return PCLEAN;
    }

    PCState beforeWrite(StateManagerImpl context, int field, boolean mutate) {
        return beforeWrite(context, field, mutate, false);
    }

    PCState beforeOptimisticWrite(StateManagerImpl context, int field,
        boolean mutate) {
        if (context.getBroker().getAutoClear() == AutoClear.CLEAR_ALL)
            return beforeWrite(context, field, mutate, true);
        return PDIRTY;
    }

    private PCState beforeWrite(StateManagerImpl context, int field,
        boolean mutate, boolean optimistic) {
        // if this is a direct mutation on an SCO field, we can't clear our
        // fields because that would also null the SCO; depending on whether
        // the user was directly manipulating the field or was using a method,
        // that will result in either an NPE or having the SCO be detached
        // from its owning object, making the user's change have no affect

        if (mutate && !optimistic) {
            Log log = context.getBroker().getConfiguration().getLog
                (OpenJPAConfiguration.LOG_RUNTIME);
            if (log.isWarnEnabled()) {
                log.warn(_loc.get("pessimistic-mutate",
                    context.getMetaData().getField(field),
                    context.getManagedInstance()));
            }
        } else if (!mutate) {
            // state is stored for rollback and fields are reloaded
            if (context.getDirty().length() > 0)
                context.saveFields(true);
            context.clearFields();
            context.load(null, context.LOAD_FGS, null, null, true);
        }
        return PDIRTY;
    }

    PCState beforeNontransactionalWrite(StateManagerImpl context, int field,
        boolean mutate) {
        return PNONTRANSDIRTY;
    }

    boolean isPersistent() {
        return true;
    }
    
    public String toString() {
        return "Persistent-Notransactional";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3743.java