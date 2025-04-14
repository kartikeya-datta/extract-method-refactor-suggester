error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4266.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4266.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[20,1]

error in qdox parser
file content:
```java
offset: 889
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4266.java
text:
```scala
public final class LSInputListImpl extends AbstractList implements LSInputList {

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

p@@ackage org.apache.xerces.impl.xs.util;

import java.util.AbstractList;

import org.apache.xerces.xs.LSInputList;
import org.w3c.dom.ls.LSInput;

/**
 * Contains a list of LSInputs.
 *
 * @xerces.internal 
 *
 * @author Michael Glavassevich, IBM
 * 
 * @version $Id$
 */
public class LSInputListImpl extends AbstractList implements LSInputList {
    
    /**
     * An immutable empty list.
     */
    public static final LSInputListImpl EMPTY_LIST = new LSInputListImpl(new LSInput[0], 0);
    
    // The array to hold all data
    private final LSInput[] fArray;
    // Number of elements in this list
    private final int fLength;
    
    /**
     * Construct an LSInputList implementation
     * 
     * @param array     the data array
     * @param length    the number of elements
     */
    public LSInputListImpl(LSInput[] array, int length) {
        fArray = array;
        fLength = length;
    }
    
    /**
     * The number of <code>LSInput</code>s in the list. The range of valid 
     * child object indices is 0 to <code>length-1</code> inclusive. 
     */
    public int getLength() {
        return fLength;
    }

    /**
     * Returns the <code>index</code>th item in the collection or 
     * <code>null</code> if <code>index</code> is greater than or equal to 
     * the number of objects in the list. The index starts at 0. 
     * @param index  index into the collection. 
     * @return  The <code>LSInput</code> at the <code>index</code>th 
     *   position in the <code>LSInputList</code>, or <code>null</code> if 
     *   the index specified is not valid. 
     */
    public LSInput item(int index) {
        if (index < 0 || index >= fLength) {
            return null;
        }
        return fArray[index];
    }
    
    /*
     * List methods
     */

    public Object get(int index) {
        if (index >= 0 && index < fLength) {
            return fArray[index];
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    public int size() {
        return getLength();
    }
    
} // LSInputListImpl
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4266.java