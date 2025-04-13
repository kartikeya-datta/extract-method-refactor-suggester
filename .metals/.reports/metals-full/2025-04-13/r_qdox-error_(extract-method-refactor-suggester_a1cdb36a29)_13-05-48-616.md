error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8492.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8492.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8492.java
text:
```scala
public v@@oid clearXSObjectList() {

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

package org.apache.xerces.impl.xs.util;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;

/**
 * Contains a list of XSObjects.
 *
 * @xerces.internal 
 *
 * @author Sandy Gao, IBM
 *
 * @version $Id$
 */
public class XSObjectListImpl extends AbstractList implements XSObjectList {
    
    /**
     * An immutable empty list.
     */
    public static final XSObjectListImpl EMPTY_LIST = new XSObjectListImpl(new XSObject[0], 0);
    private static final ListIterator EMPTY_ITERATOR = new ListIterator() {
        public boolean hasNext() {
            return false;
        }
        public Object next() {
            throw new NoSuchElementException();
        }
        public boolean hasPrevious() {
            return false;
        }
        public Object previous() {
            throw new NoSuchElementException();
        }
        public int nextIndex() {
            return 0;
        }
        public int previousIndex() {
            return -1;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
        public void set(Object object) {
            throw new UnsupportedOperationException();
        }
        public void add(Object object) {
            throw new UnsupportedOperationException();
        }
    };
    
    private static final int DEFAULT_SIZE = 4;

    // The array to hold all data
    private XSObject[] fArray = null;
    // Number of elements in this list
    private int fLength = 0;
    
    public XSObjectListImpl() {
        fArray = new XSObject[DEFAULT_SIZE];
        fLength = 0;
    }

    /**
     * Construct an XSObjectList implementation
     * 
     * @param array     the data array
     * @param length    the number of elements
     */
    public XSObjectListImpl(XSObject[] array, int length) {
        fArray = array;
        fLength = length;
    }

    /**
     * The number of <code>XSObjects</code> in the list. The range of valid
     * child node indices is 0 to <code>length-1</code> inclusive.
     */
    public int getLength() {
        return fLength;
    }

    /**
     * Returns the <code>index</code>th item in the collection. The index
     * starts at 0. If <code>index</code> is greater than or equal to the
     * number of nodes in the list, this returns <code>null</code>.
     * @param index index into the collection.
     * @return The XSObject at the <code>index</code>th position in the
     *   <code>XSObjectList</code>, or <code>null</code> if that is not a
     *   valid index.
     */
    public XSObject item(int index) {
        if (index < 0 || index >= fLength) {
            return null;
        }
        return fArray[index];
    }

    // clear this object
    public void clear() {
        for (int i=0; i<fLength; i++) {
            fArray[i] = null;
        }
        fArray = null;
        fLength = 0;
    }
    
    public void addXSObject(XSObject object) {
       if (fLength == fArray.length) {  
           XSObject[] temp = new XSObject[fLength + 4];
           System.arraycopy(fArray, 0, temp, 0, fLength);
           fArray = temp;
       }
       fArray[fLength++] = object;
    }
    
    public void addXSObject(int index, XSObject object) {
        fArray[index] = object;
    }
    
    /*
     * List methods
     */
    
    public boolean contains(Object value) {
        return (value == null) ? containsNull() : containsObject(value);
    }

    public Object get(int index) {
        if (index >= 0 && index < fLength) {
            return fArray[index];
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    public int size() {
        return getLength();
    }
    
    public Iterator iterator() {
        return listIterator0(0);
    }
    
    public ListIterator listIterator() {
        return listIterator0(0);
    }
    
    public ListIterator listIterator(int index) {
        if (index >= 0 && index < fLength) {
            return listIterator0(index);
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }
    
    private ListIterator listIterator0(int index) {
        return fLength == 0 ? EMPTY_ITERATOR : new XSObjectListIterator(index);
    }
    
    private boolean containsObject(Object value) {
        for (int i = fLength - 1; i >= 0; --i) {
            if (value.equals(fArray[i])) {
                return true;
            }
        }
        return false;
    }
    
    private boolean containsNull() {
        for (int i = fLength - 1; i >= 0; --i) {
            if (fArray[i] == null) {
                return true;
            }
        }
        return false;
    }
    
    private final class XSObjectListIterator implements ListIterator {
        private int index;
        public XSObjectListIterator(int index) {
            this.index = index;
        }
        public boolean hasNext() {
            return (index < fLength);
        }
        public Object next() {
            if (index < fLength) {
                return fArray[index++];
            }
            throw new NoSuchElementException();
        }
        public boolean hasPrevious() {
            return (index > 0);
        }
        public Object previous() {
            if (index > 0) {
                return fArray[--index];
            }
            throw new NoSuchElementException();
        }
        public int nextIndex() {
            return index;
        }
        public int previousIndex() {
            return index - 1;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
        public void set(Object o) {
            throw new UnsupportedOperationException();
        }
        public void add(Object o) {
            throw new UnsupportedOperationException();
        }
    }

} // class XSObjectListImpl
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8492.java