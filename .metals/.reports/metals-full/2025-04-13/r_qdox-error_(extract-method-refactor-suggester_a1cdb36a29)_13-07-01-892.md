error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13991.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13991.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13991.java
text:
```scala
i@@nt newcapacity = ((capacity*3)>>1) + 1;

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
package org.apache.commons.csv;

/**
 * A simple StringBuffer replacement that aims to 
 * reduce copying as much as possible. The buffer
 * grows as necessary.
 * This class is not thread safe.
 * 
 * @author Ortwin Glück
 */
public class CharBuffer {
    private char[] c;
    /**
     * Actually used number of characters in the array. 
     * It is also the index at which
     * a new character will be inserted into <code>c</code>. 
     */ 
    private int length;
    
    /**
     * Creates a new CharBuffer with an initial capacity of 32 characters.
     */
    public CharBuffer() {
        this(32);
    }
    
    /**
     * Creates a new CharBuffer with an initial capacity 
     * of <code>length</code> characters.
     */
    public CharBuffer(final int length) {
        if (length == 0) throw new IllegalArgumentException("Can't create an empty CharBuffer");
        this.c = new char[length];
    }
    
    /**
     * Empties the buffer. The capacity still remains the same, so no memory is freed.
     */
    public void clear() {
        length = 0;
    }
    
    /**
     * Returns the number of characters in the buffer.
     * @return the number of characters
     */
    public int length() {
        return length;
    }
    
    /**
     * Returns the current capacity of the buffer.
     * @return the maximum number of characters that can be stored in this buffer without
     * resizing it.
     */
    public int capacity() {
        return c.length;
    }
    
    /**
     * Appends the contents of <code>cb</code> to the end of this CharBuffer.
     * @param cb the CharBuffer to append or null
     */
    public void append(final CharBuffer cb) {
        if (cb == null) return;
        provideCapacity(length + cb.length);
        System.arraycopy(cb.c, 0, c, length, cb.length);
        length += cb.length;
    }
    
    /**
     * Appends <code>s</code> to the end of this CharBuffer.
     * This method involves copying the new data once!
     * @param s the String to append or null
     */
    public void append(final String s) {
        if (s == null) return;
        append(s.toCharArray());
    }
    
    /**
     * Appends <code>sb</code> to the end of this CharBuffer.
     * This method involves copying the new data once!
     * @param sb the StringBuffer to append or null
     */
    public void append(final StringBuffer sb) {
        if (sb == null) return;
        provideCapacity(length + sb.length());
        sb.getChars(0, sb.length(), c, length);
        length += sb.length();
    }
    
    /**
     * Appends <code>data</code> to the end of this CharBuffer.
     * This method involves copying the new data once!
     * @param data the char[] to append or null
     */
    public void append(final char[] data) {
        if (data == null) return;
        provideCapacity(length + data.length);
        System.arraycopy(data, 0, c, length, data.length);
        length += data.length;
    }
    
    /**
     * Appends a single character to the end of this CharBuffer.
     * This method involves copying the new data once!
     * @param data the char to append
     */
    public void append(final char data) {
        provideCapacity(length + 1);
        c[length] = data;
        length++;
    }
    
    /**
     * Shrinks the capacity of the buffer to the current length if necessary.
     * This method involves copying the data once!
     */
    public void shrink() {
        if (c.length == length) return;
        char[] newc = new char[length];
        System.arraycopy(c, 0, newc, 0, length);
        c = newc;
    }

    /**
     * Returns the contents of the buffer as a char[]. The returned array may
     * be the internal array of the buffer, so the caller must take care when
     * modifying it.
     * This method allows to avoid copying if the caller knows the exact capacity
     * before.
     * @return
     */
    public char[] getCharacters() {
        if (c.length == length) return c;
        char[] chars = new char[length];
        System.arraycopy(c, 0, chars, 0, length);
        return chars;
    }
    
    /**
     * Converts the contents of the buffer into a StringBuffer.
     * This method involves copying the new data once!
     * @return
     */
    public StringBuffer toStringBuffer() {
        StringBuffer sb = new StringBuffer(length);
        sb.append(c, 0, length);
        return sb;
    }
    
    /**
     * Converts the contents of the buffer into a StringBuffer.
     * This method involves copying the new data once!
     * @return
     */
    public String toString() {
        return new String(c, 0, length);
    }
    
    /**
     * Copies the data into a new array of at least <code>capacity</code> size.
     * @param capacity
     */
    public void provideCapacity(final int capacity) {
        if (c.length >= capacity) return;
        int newcapacity = capacity;
        char[] newc = new char[newcapacity];
        System.arraycopy(c, 0, newc, 0, length);
        c = newc;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13991.java