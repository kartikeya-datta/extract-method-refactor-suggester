error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12938.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12938.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[24,1]

error in qdox parser
file content:
```java
offset: 1089
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12938.java
text:
```scala
public class ChainedOperationTransformer implements OperationTransformer {

/*
* JBoss, Home of Professional Open Source.
* Copyright 2012, Red Hat Middleware LLC, and individual contributors
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
p@@ackage org.jboss.as.controller.transform.chained;

import java.util.ArrayList;
import java.util.List;

import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.transform.OperationResultTransformer;
import org.jboss.as.controller.transform.OperationTransformer;
import org.jboss.as.controller.transform.TransformationContext;
import org.jboss.dmr.ModelNode;

/**
 * An experimental operation transformer allowing you to chain several transformers.
 *
 * @deprecated Experimental and likely to change
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
@Deprecated
public class LChainedOperationTransformer implements OperationTransformer {

    private final OperationTransformer[] entries;

    public ChainedOperationTransformer(OperationTransformer...entries) {
        this.entries = entries;
    }

    @Override
    public TransformedOperation transformOperation(TransformationContext context, PathAddress address, ModelNode operation)
            throws OperationFailedException {
        List<TransformedOperation> delegates = new ArrayList<TransformedOperation>();
        ModelNode currentOperation = operation;
        for (OperationTransformer entry : entries) {
            TransformedOperation transformed = entry.transformOperation(context, address, currentOperation);
            currentOperation = transformed.getTransformedOperation();
            delegates.add(transformed);
            if (currentOperation == null) {
                break;
            }
        }
        return new ChainedTransformedOperation(currentOperation, delegates.toArray(new TransformedOperation[delegates.size()]));
    }

    private static class ChainedTransformedOperation extends TransformedOperation {

        private TransformedOperation[] delegates;
        private volatile String failure;

        public ChainedTransformedOperation(ModelNode transformedOperation, TransformedOperation...delegates) {
            // FIXME ChainedTransformedOperation constructor
            super(transformedOperation, null);
            this.delegates = delegates;
        }

        @Override
        public ModelNode getTransformedOperation() {
            return super.getTransformedOperation();
        }

        @Override
        public OperationResultTransformer getResultTransformer() {
            return this;
        }

        @Override
        public boolean rejectOperation(ModelNode preparedResult) {
            for (TransformedOperation delegate : delegates) {
                if (delegate.rejectOperation(preparedResult)) {
                    failure = delegate.getFailureDescription();
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getFailureDescription() {
            return failure;
        }

        @Override
        public ModelNode transformResult(ModelNode result) {
            ModelNode currentResult = result;
            for (int i = delegates.length - 1 ; i >= 0 ; --i) {
                currentResult = delegates[i].transformResult(currentResult);
            }
            return currentResult;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12938.java