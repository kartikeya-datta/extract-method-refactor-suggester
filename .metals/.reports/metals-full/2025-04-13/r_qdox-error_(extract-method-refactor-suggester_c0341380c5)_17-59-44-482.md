error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14358.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14358.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14358.java
text:
```scala
public v@@oid testAppendConstructStringBuilder() {

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
package org.apache.commons.io.output;

import java.io.IOException;
import java.io.Writer;
import junit.framework.TestCase;

/**
 * Test case for {@link StringBuilderWriter}.
 *
 * @version $Revision$ $Date$
 */
public class StringBuilderWriterTest extends TestCase {
    private static final char[] FOOBAR_CHARS = new char[] {'F', 'o', 'o', 'B', 'a', 'r'};

    /**
     * Contruct a new test case.
     * @param name The name of the test
     */
    public StringBuilderWriterTest(String name) {
        super(name);
    }

    /** Test {@link StringBuilderWriter} constructor. */
    public void testAppendConstructCapacity() throws IOException {
        Writer writer = new StringBuilderWriter(100);
        writer.append("Foo");
        assertEquals("Foo", writer.toString());
    }

    /** Test {@link StringBuilderWriter} constructor. */
    public void testAppendConstructStringBuilder() throws IOException {
        StringBuilder builder = new StringBuilder("Foo");
        StringBuilderWriter writer = new StringBuilderWriter(builder);
        writer.append("Bar");
        assertEquals("FooBar", writer.toString());
        assertTrue(builder == writer.getBuilder());
    }

    /** Test {@link StringBuilderWriter} constructor. */
    public void testAppendConstructNull() throws IOException {
        Writer writer = new StringBuilderWriter((StringBuilder)null);
        writer.append("Foo");
        assertEquals("Foo", writer.toString());
    }

    /** Test {@link Writer#append(char)}. */
    public void testAppendChar() throws IOException {
        Writer writer = new StringBuilderWriter();
        writer.append('F').append('o').append('o');
        assertEquals("Foo", writer.toString());
    }

    /** Test {@link Writer#append(CharSequence)}. */
    public void testAppendCharSequence() throws IOException {
        Writer writer = new StringBuilderWriter();
        writer.append("Foo").append("Bar");
        assertEquals("FooBar", writer.toString());
    }

    /** Test {@link Writer#append(CharSequence, int, int)}. */
    public void testAppendCharSequencePortion() throws IOException {
        Writer writer = new StringBuilderWriter();
        writer.append("FooBar", 3, 6).append(new StringBuffer("FooBar"), 0, 3);
        assertEquals("BarFoo", writer.toString());
    }

    /** Test {@link Writer#close()}. */
    public void testClose() {
        Writer writer = new StringBuilderWriter();
        try {
            writer.append("Foo");
            writer.close();
            writer.append("Bar");
        } catch (Throwable t) {
            fail("Threw: " + t);
        }
        assertEquals("FooBar", writer.toString());
    }

    /** Test {@link Writer#write(int)}. */
    public void testWriteChar() throws IOException {
        Writer writer = new StringBuilderWriter();
        writer.write('F');
        assertEquals("F", writer.toString());
        writer.write('o');
        assertEquals("Fo", writer.toString());
        writer.write('o');
        assertEquals("Foo", writer.toString());
    }

    /** Test {@link Writer#write(char[])}. */
    public void testWriteCharArray() throws IOException {
        Writer writer = new StringBuilderWriter();
        writer.write(new char[] {'F', 'o', 'o'});
        assertEquals("Foo", writer.toString());
        writer.write(new char[] {'B', 'a', 'r'});
        assertEquals("FooBar", writer.toString());
    }

    /** Test {@link Writer#write(char[], int, int)}. */
    public void testWriteCharArrayPortion() throws IOException {
        Writer writer = new StringBuilderWriter();
        writer.write(FOOBAR_CHARS, 3, 3);
        assertEquals("Bar", writer.toString());
        writer.write(FOOBAR_CHARS, 0, 3);
        assertEquals("BarFoo", writer.toString());
    }

    /** Test {@link Writer#write(String)}. */
    public void testWriteString() throws IOException {
        Writer writer = new StringBuilderWriter();
        writer.write("Foo");
        assertEquals("Foo", writer.toString());
        writer.write("Bar");
        assertEquals("FooBar", writer.toString());
    }

    /** Test {@link Writer#write(String, int, int)}. */
    public void testWriteStringPortion() throws IOException {
        Writer writer = new StringBuilderWriter();
        writer.write("FooBar", 3, 3);
        assertEquals("Bar", writer.toString());
        writer.write("FooBar", 0, 3);
        assertEquals("BarFoo", writer.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14358.java