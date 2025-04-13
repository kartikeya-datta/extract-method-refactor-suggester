error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13764.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13764.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13764.java
text:
```scala
r@@eturn "[target=" + target.getName() + ",parent=" + parentID + ",type=" + moduleType+ ",id=" + moduleID + "]";

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.ee.deployment.spi;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;

/**
 * A TargetModuleID interface represents a unique identifier for a deployed application module. A deployable application module
 * can be an EAR, JAR, WAR or RAR file. A TargetModuleID can represent a root module or a child module. A root module
 * TargetModuleID has no parent. It represents a deployed EAR file or stand alone module. A child module TargetModuleID
 * represents a deployed sub module of a J2EE application. A child TargetModuleID has only one parent, the super module it was
 * bundled and deployed with. The identifier consists of the target name and the unique identifier for the deployed application
 * module.
 *
 * @author Thomas.Diesler@jboss.com
 *
 */
final class TargetModuleIDImpl implements JBossTargetModuleID {

    private final JBossTarget target;
    private final String moduleID;
    private final TargetModuleID parentModuleID;
    private final ModuleType moduleType;
    private List<TargetModuleID> childModuleIDs = new ArrayList<TargetModuleID>();
    private boolean isRunning;

    TargetModuleIDImpl(JBossTarget target, String moduleID, TargetModuleID parentModuleID, ModuleType moduleType) {
        if (target == null)
            throw new IllegalArgumentException("Null target");
        if (moduleID == null)
            throw new IllegalArgumentException("Null moduleID");
        if (moduleType == null)
            throw new IllegalArgumentException("Null moduleType");
        this.target = target;
        this.moduleID = moduleID;
        this.parentModuleID = parentModuleID;
        this.moduleType = moduleType;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public ModuleType getModuleType() {
        return moduleType;
    }

    void addChildTargetModuleID(TargetModuleID childModuleID) {
        childModuleIDs.add(childModuleID);
    }

    // TargetModuleID interface ************************************************

    @Override
    public Target getTarget() {
        return target;
    }

    @Override
    public String getModuleID() {
        return moduleID;
    }

    @Override
    public String getWebURL() {
        return null; // [todo] implement method
    }

    @Override
    public TargetModuleID getParentTargetModuleID() {
        return parentModuleID;
    }

    @Override
    public TargetModuleID[] getChildTargetModuleID() {
        TargetModuleID[] idarr = new TargetModuleID[childModuleIDs.size()];
        childModuleIDs.toArray(idarr);
        return idarr;
    }

    public int hashCode() {
        String hashStr = moduleType + moduleID;
        if (parentModuleID != null) {
            hashStr += parentModuleID.getModuleID();
        }
        return hashStr.hashCode();
    }

    /**
     * Equality is defined by moduleType, moduleID, and parentModuleID
     */
    public boolean equals(Object obj) {
        boolean equals = false;
        if (obj instanceof TargetModuleIDImpl) {
            TargetModuleIDImpl other = (TargetModuleIDImpl) obj;
            equals = moduleType.equals(other.moduleType) && moduleID.equals(other.moduleID);
            if (equals && parentModuleID != null)
                equals = equals && parentModuleID.equals(other.parentModuleID);
        }
        return equals;
    }

    public String toString() {
        String parentID = (parentModuleID != null ? parentModuleID.getModuleID() : null);
        return "[target=" + target.getName() + ",parent=" + parentID + ",id=" + moduleID + "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13764.java