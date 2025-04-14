error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10101.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10101.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[24,1]

error in qdox parser
file content:
```java
offset: 1062
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10101.java
text:
```scala
public final class AttributeAccess {

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
p@@ackage org.jboss.as.controller.registry;

import org.jboss.as.controller.OperationHandler;

/**
 * Information about handling an attribute in a sub-model.
 *
 * @author Brian Stansberry
 */
public class AttributeAccess {

    /**
     * The {@link AttributeAccess.AccessType}.
     *
     * <ul>
     *   <li>{@code READ_ONLY} defines a read-only attribute, which can be either {@code Storage.CONFIGURATION} or {@code Storage.RUNTIME}.</li>
     *   <li>{@code READ_WRITE} defines a read-write attribute, which can be either {@code Storage.CONFIGURATION} or {@code Storage.RUNTIME}.</li>
     *   <li>{@code METRIC} implies a read-only {@code Storage.RUNTIME} attribute.</li>
     * </ul>
     *
     */
    public static enum AccessType {
        READ_ONLY("read-only"),
        READ_WRITE("read-write"),
        METRIC("metric");

        private final String label;

        private AccessType(final String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    /**
     * The {@link AttributeAccess.Storage} indicates whether the attribute is
     * derived from the configuration or is a runtime attribute.
     */
    public static enum Storage {

        CONFIGURATION("configuration"),
        RUNTIME("runtime");

        private final String label;

        private Storage(final String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }

    }

    private final AccessType access;
    private final Storage storage;
    private final OperationHandler readHandler;
    private final OperationHandler writeHandler;

    public AttributeAccess(final AccessType access, final Storage storage, final OperationHandler readHandler, final OperationHandler writeHandler) {
        assert access != null : "access is null";
        assert storage != null : "storage is null";
        this.access = access;
        this.readHandler = readHandler;
        this.writeHandler = writeHandler;
        this.storage = storage;
        if(access == AccessType.READ_WRITE && writeHandler == null) {
            throw new IllegalArgumentException("writeHandler is null");
        }
    }

    /**
     * Get the access type.
     *
     * @return the access type
     */
    public AccessType getAccessType() {
        return access;
    }

    /**
     * Get the storage type.
     *
     * @return the storage type
     */
    public Storage getStorageType() {
        return storage;
    }

    /**
     * Get the read handler.
     *
     * @return the read handler, <code>null</code> if not undefined
     */
    public OperationHandler getReadHandler() {
        return readHandler;
    }

    /**
     * Get the write handler.
     *
     * @return the write handler, <code>null</code> if not undefined.
     */
    public OperationHandler getWriteHandler() {
        return writeHandler;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10101.java