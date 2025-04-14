error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6932.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6932.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1084
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6932.java
text:
```scala
public class DeploymentUnitKey  implements Serializable {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

p@@ackage org.jboss.as.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * An identifier for a deployment unit suitable for use as a map key.
 * 
 * @author Brian Stansberry
 */
public class \DeploymentUnitKey  implements Serializable {
    
    private static final long serialVersionUID = 8171593872559737006L;
    private final String name;
    private final byte[] sha1Hash;
    private final int hashCode;
    
    /**
     * Creates a new DeploymentUnitKey
     * 
     * @param name the deployment's name 
     * @param sha1Hash an sha1 hash of the deployment content
     */
    public DeploymentUnitKey(String name, byte[] sha1Hash) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (sha1Hash == null) {
            throw new IllegalArgumentException("sha1Hash is null");
        }
        this.name = name;
        this.sha1Hash = sha1Hash;
        
        // We assume a hashcode will be wanted, so calculate and cache
        int result = 17;
        result += 31 * name.hashCode();
        result += 31 * Arrays.hashCode(sha1Hash);
        this.hashCode = result;
    }

    /**
     * Gets the name of the deployment.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a defensive copy of the sha1 hash of the deployment.
     * 
     * @return the hash
     */
    public byte[] getSha1Hash() {
        return sha1Hash.clone();
    }
    
    /**
     * Gets the sha1 hash of the deployment as a hex string.
     * 
     * @return the hash
     */
    public String getSha1HashAsHexString() {
        return AbstractModelElement.bytesToHexString(sha1Hash);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof DeploymentUnitKey) {
            DeploymentUnitKey other = (DeploymentUnitKey) obj;
            return name.equals(other.name) && Arrays.equals(sha1Hash, sha1Hash);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
    
    /** 
     * Computes a hash of the name and sha1 hash. Exposed as a convenience
     * since {@link #getSha1Hash()} returns a defensive copy of the byte[].
     * 
     * @return a hash of the name and the sha1 hash
     */
    long elementHash() {
        return name.hashCode() & 0xffffffffL ^ AbstractModelElement.calculateElementHashOf(sha1Hash);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6932.java