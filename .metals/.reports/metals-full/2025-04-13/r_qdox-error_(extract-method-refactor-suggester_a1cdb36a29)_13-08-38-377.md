error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3136.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3136.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3136.java
text:
```scala
t@@hrow new IllegalStateException("Node name '" + nodeName + "' should follow a node type.");

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
package org.jboss.as.cli.operation.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.as.cli.operation.OperationRequestAddress;

/**
 * This implementation is not thread-safe.
 *
 * @author Alexey Loubyansky
 */
public class DefaultOperationRequestAddress implements OperationRequestAddress {

    private final List<NodeImpl> nodes = new ArrayList<NodeImpl>();

    public DefaultOperationRequestAddress() {
    }

    /**
     * Creates a prefix and initializes it to the value of the argument.
     * @param initial  the initial value
     */
    public DefaultOperationRequestAddress(OperationRequestAddress initial) {
        if(!initial.isEmpty()) {
            for(Node node : initial) {
                toNode(node.getType(), node.getName());
            }
        }
    }

    @Override
    public void toNodeType(String nodeType) {

        nodes.add(new NodeImpl(nodeType, null));
    }

    @Override
    public void toNode(String nodeName) {

        if(nodes.isEmpty())
            throw new IllegalStateException("The prefix should end with the node type before going to a specific node name.");

        nodes.get(nodes.size() - 1).name = nodeName;
    }

    @Override
    public void toNode(String nodeType, String nodeName) {

        if(endsOnType()) {
            throw new IllegalStateException("The prefix ends on a type. A node name must be specified before this method can be invoked.");
        }
        nodes.add(new NodeImpl(nodeType, nodeName));
    }

    @Override
    public String toNodeType() {

        if(nodes.isEmpty()) {
            return null;
        }
        String name = nodes.get(nodes.size() - 1).name;
        nodes.get(nodes.size() - 1).name = null;
        return name;
    }

    @Override
    public Node toParentNode() {

        if(nodes.isEmpty()) {
            return null;
        }
        return nodes.remove(nodes.size() - 1);
    }

    @Override
    public void reset() {
        nodes.clear();
    }

    @Override
    public boolean endsOnType() {
        if(nodes.isEmpty()) {
            return false;
        }

        NodeImpl node = nodes.get(nodes.size() - 1);
        return node.name == null;
    }

    @Override
    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    @Override
    public Iterator<Node> iterator() {

        final Node[] array = nodes.toArray(new Node[nodes.size()]);
        return new Iterator<Node>() {

            int i = 0;

            @Override
            public boolean hasNext() {
                return i < array.length;
            }

            @Override
            public Node next() {
                return array[i++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    @Override
    public String getNodeType() {

        if(nodes.isEmpty()) {
            return null;
        }
        return nodes.get(nodes.size() - 1).type;
    }

    @Override
    public String getNodeName() {

        if(nodes.isEmpty()) {
            return null;
        }
        return nodes.get(nodes.size() - 1).name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof OperationRequestAddress))
            return false;

        OperationRequestAddress other = (OperationRequestAddress) obj;

        if(isEmpty() != other.isEmpty())
            return false;

        Iterator<Node> thisIterator = iterator();
        Iterator<Node> otherIterator = other.iterator();
        boolean result = true;
        while(result) {
            if(!thisIterator.next().equals(otherIterator.next())) {
                result = false;
            } else {
                if (!thisIterator.hasNext()) {
                    if (otherIterator.hasNext()) {
                        result = false;
                    }
                    break;
                }
                if (!otherIterator.hasNext()) {
                    if (thisIterator.hasNext()) {
                        result = false;
                    }
                    break;
                }
            }
        }
        return result;
    }

    private static final class NodeImpl implements Node {

        String type;
        String name;

        NodeImpl(String type, String name) {
            this.type = type;
            this.name = name;
        }

        @Override
        public String getType() {
            return type;
        }
        @Override
        public String getName() {
            return name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((type == null) ? 0 : type.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;

            if(!(obj instanceof Node))
                return false;

            Node other = (Node) obj;
            if (name == null) {
                if (other.getName() != null)
                    return false;
            } else if (!name.equals(other.getName()))
                return false;
            if (type == null) {
                if (other.getType() != null)
                    return false;
            } else if (!type.equals(other.getType()))
                return false;
            return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3136.java