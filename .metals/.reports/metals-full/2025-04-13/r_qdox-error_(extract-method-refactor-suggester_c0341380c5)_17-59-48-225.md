error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13582.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13582.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13582.java
text:
```scala
b@@uilder = new CSVFormatBuilder('+', Character.valueOf('!'), null, Character.valueOf('#'), Character.valueOf('!'), true, true, CRLF, Constants.EMPTY, null);

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

package org.apache.commons.csv;

import static org.apache.commons.csv.CSVFormat.RFC4180;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.LF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.csv.CSVFormat.CSVFormatBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * 
 * @version $Id$
 */
public class CSVFormatBuilderTest {

    private CSVFormatBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new CSVFormatBuilder('+', Character.valueOf('!'), null, Character.valueOf('#'), Character.valueOf('!'), true, true, CRLF, null);
    }

    @Test
    public void testCommentStart() {
        assertEquals('?', builder.withCommentStart('?').build().getCommentStart().charValue());
    }
    
    @Test
    public void testCopiedFormatIsEqualToOriginal() {
        final CSVFormat copyOfRCF4180 = CSVFormat.newBuilder(RFC4180).build();
        assertEquals(RFC4180, copyOfRCF4180);
        final CSVFormat copy2OfRCF4180 = RFC4180.toBuilder().build();
        assertEquals(RFC4180, copy2OfRCF4180);
    }

    @Test
    public void testCopiedFormatWithChanges() {
        final CSVFormat newFormat = CSVFormat.newBuilder(RFC4180).withDelimiter('!').build();
        assertTrue(newFormat.getDelimiter() != RFC4180.getDelimiter());
        final CSVFormat newFormat2 = RFC4180.toBuilder().withDelimiter('!').build();
        assertTrue(newFormat2.getDelimiter() != RFC4180.getDelimiter());
    }
    
    @Test
    public void testDelimiter() {
        assertEquals('?', builder.withDelimiter('?').build().getDelimiter());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDelimiterSameAsCommentStartThrowsException() {
        builder.withDelimiter('!').withCommentStart('!').build();
    }

    @Test(expected = IllegalStateException.class)
    public void testDelimiterSameAsEscapeThrowsException() {
        builder.withDelimiter('!').withEscape('!').build();
    }

    @Test
    public void testEscape() {
        assertEquals('?', builder.withEscape('?').build().getEscape().charValue());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testEscapeSameAsCommentStartThrowsException() {
        builder.withEscape('!').withCommentStart('!').build();
    }

    @Test(expected = IllegalStateException.class)
    public void testEscapeSameAsCommentStartThrowsExceptionForWrapperType() {
        // Cannot assume that callers won't use different Character objects
        builder.withEscape(new Character('!')).withCommentStart(new Character('!')).build();
    }

    @Test
    public void testHeaderReferenceCannotEscape() {
        final String[] header = new String[]{"one", "tow", "three"};
        builder.withHeader(header);
        
        final CSVFormat firstFormat = builder.build();
        final CSVFormat secondFormat = builder.build();
        assertNotSame(header, firstFormat.getHeader());
        assertNotSame(firstFormat, secondFormat.getHeader());
    }

    @Test
    public void testIgnoreEmptyLines() {
        assertFalse(builder.withIgnoreEmptyLines(false).build().getIgnoreEmptyLines());
    }

    @Test
    public void testIgnoreSurroundingSpaces() {
        assertFalse(builder.withIgnoreSurroundingSpaces(false).build().getIgnoreSurroundingSpaces());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNewFormatCRThrowsException() {
        CSVFormat.newBuilder(CR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewFormatLFThrowsException() {
        CSVFormat.newBuilder(LF);
    }
    
    @Test
    public void testQuoteChar() {
        assertEquals('?', builder.withQuoteChar('?').build().getQuoteChar().charValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testQuoteCharSameAsCommentStartThrowsException() {
        builder.withQuoteChar('!').withCommentStart('!').build();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testQuoteCharSameAsCommentStartThrowsExceptionForWrapperType() {
        // Cannot assume that callers won't use different Character objects
        builder.withQuoteChar(new Character('!')).withCommentStart('!').build();
    }

    @Test(expected = IllegalStateException.class)
    public void testQuoteCharSameAsDelimiterThrowsException() {
        builder.withQuoteChar('!').withDelimiter('!').build();
    }

    @Test
    public void testQuotePolicy() {
        assertEquals(Quote.ALL, builder.withQuotePolicy(Quote.ALL).build().getQuotePolicy());
    }

    @Test(expected = IllegalStateException.class)
    public void testQuotePolicyNoneWithoutEscapeThrowsException() {
        CSVFormat.newBuilder('!').withQuotePolicy(Quote.NONE).build();
    }

    @Test
    public void testRecoardSeparator() {
        assertEquals("?", builder.withRecordSeparator("?").build().getRecordSeparator());
    }
    
    @Test
    public void testRFC4180() {
        assertEquals(null, RFC4180.getCommentStart());
        assertEquals(',', RFC4180.getDelimiter());
        assertEquals(null, RFC4180.getEscape());
        assertFalse(RFC4180.getIgnoreEmptyLines());
        assertEquals(Character.valueOf('"'), RFC4180.getQuoteChar());
        assertEquals(null, RFC4180.getQuotePolicy());
        assertEquals("\r\n", RFC4180.getRecordSeparator());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithCommentStartCRThrowsException() {
        builder.withCommentStart(CR).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithDelimiterLFThrowsException() {
        builder.withDelimiter(LF).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEscapeCRThrowsExceptions() {
        builder.withEscape(CR).build();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithQuoteLFThrowsException() {
        builder.withQuoteChar(LF).build();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13582.java