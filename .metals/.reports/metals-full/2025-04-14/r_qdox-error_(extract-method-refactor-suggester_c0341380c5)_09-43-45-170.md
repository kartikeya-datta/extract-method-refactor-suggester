error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8060.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8060.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8060.java
text:
```scala
private final Q@@Name fElement = new QName();

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.xerces.impl.dtd.models;

import org.apache.xerces.impl.dtd.XMLContentSpec;
import org.apache.xerces.xni.QName;

/**
 * Content model leaf node.
 * 
 * @xerces.internal
 *
 * @version $Id$
 */
public class CMLeaf 
    extends CMNode {

    //
    // Data
    //

    /** This is the element that this leaf represents. */
    private QName fElement = new QName();

    /**
     * Part of the algorithm to convert a regex directly to a DFA
     * numbers each leaf sequentially. If its -1, that means its an
     * epsilon node. Zero and greater are non-epsilon positions.
     */
    private int fPosition = -1;

    //
    // Constructors
    //

    /** Constructs a content model leaf. */
    public CMLeaf(QName element, int position)  {
        super(XMLContentSpec.CONTENTSPECNODE_LEAF);

        // Store the element index and position
        fElement.setValues(element);
        fPosition = position;
    }

    /** Constructs a content model leaf. */
    public CMLeaf(QName element)  {
        super(XMLContentSpec.CONTENTSPECNODE_LEAF);

        // Store the element index and position
        fElement.setValues(element);
    }

    //
    // Package methods
    //

    final QName getElement()
    {
        return fElement;
    }

    final int getPosition()
    {
        return fPosition;
    }

    final void setPosition(int newPosition)
    {
        fPosition = newPosition;
    }

    //
    // CMNode methods
    //

    // package

    public boolean isNullable() 
    {
        // Leaf nodes are never nullable unless its an epsilon node
        return (fPosition == -1);
    }

    public String toString()
    {
        StringBuffer strRet = new StringBuffer(fElement.toString());
        strRet.append(" (");
        strRet.append(fElement.uri);
        strRet.append(',');
        strRet.append(fElement.localpart);
        strRet.append(')');
        if (fPosition >= 0) {
            strRet.append(" (Pos:")
                  .append(Integer.toString(fPosition))
                  .append(')');
        }
        return strRet.toString();
    }

    // protected

    protected void calcFirstPos(CMStateSet toSet) 
    {
        // If we are an epsilon node, then the first pos is an empty set
        if (fPosition == -1)
            toSet.zeroBits();

        // Otherwise, its just the one bit of our position
        else
            toSet.setBit(fPosition);
    }

    protected void calcLastPos(CMStateSet toSet) 
    {
        // If we are an epsilon node, then the last pos is an empty set
        if (fPosition == -1)
            toSet.zeroBits();

        // Otherwise, its just the one bit of our position
        else
            toSet.setBit(fPosition);
    }

} // class CMLeaf


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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8060.java