error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7481.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7481.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7481.java
text:
```scala
public static C@@SVStrategy EXCEL_STRATEGY   = new CSVStrategy(',', '"', COMMENTS_DISABLED, false, false, false);

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

import java.io.Serializable;

/**
 * CSVStrategy
 * 
 * Represents the strategy for a CSV.
 */
public class CSVStrategy implements Cloneable, Serializable {

    private char delimiter;
    private char encapsulator;
    private char commentStart;
    private boolean ignoreLeadingWhitespaces;
    private boolean interpretUnicodeEscapes;
    private boolean ignoreEmptyLines;

    public static char COMMENTS_DISABLED       = (char) 0;

    public static CSVStrategy DEFAULT_STRATEGY = new CSVStrategy(',', '"', COMMENTS_DISABLED, true,  false, true);
    public static CSVStrategy EXCEL_STRATEGY   = new CSVStrategy(';', '"', COMMENTS_DISABLED, false, false, false);
    public static CSVStrategy TDF_STRATEGY     = new CSVStrategy('	', '"', COMMENTS_DISABLED, true,  false, true);


    public CSVStrategy(char delimiter, char encapsulator, char commentStart) {
        this(delimiter, encapsulator, commentStart, true, false, true);
    }
  
    /**
     * Customized CSV strategy setter.
     * 
     * @param delimiter a Char used for value separation
     * @param encapsulator a Char used as value encapsulation marker
     * @param commentStart a Char used for comment identification
     * @param ignoreLeadingWhitespace TRUE when leading whitespaces should be
     *                                ignored
     * @param interpretUnicodeEscapes TRUE when unicode escapes should be 
     *                                interpreted
     * @param ignoreEmptyLines TRUE when the parser should skip emtpy lines
     */
    public CSVStrategy(
        char delimiter, 
        char encapsulator, 
        char commentStart, 
        boolean ignoreLeadingWhitespace, 
        boolean interpretUnicodeEscapes,
        boolean ignoreEmptyLines) 
    {
        setDelimiter(delimiter);
        setEncapsulator(encapsulator);
        setCommentStart(commentStart);
        setIgnoreLeadingWhitespaces(ignoreLeadingWhitespace);
        setUnicodeEscapeInterpretation(interpretUnicodeEscapes);
        setIgnoreEmptyLines(ignoreEmptyLines);
    }

    public void setDelimiter(char delimiter) { this.delimiter = delimiter; }
    public char getDelimiter() { return this.delimiter; }

    public void setEncapsulator(char encapsulator) { this.encapsulator = encapsulator; }
    public char getEncapsulator() { return this.encapsulator; }

    public void setCommentStart(char commentStart) { this.commentStart = commentStart; }
    public char getCommentStart() { return this.commentStart; }
    public boolean isCommentingDisabled() { return this.commentStart == COMMENTS_DISABLED; }

    public void setIgnoreLeadingWhitespaces(boolean ignoreLeadingWhitespaces) { this.ignoreLeadingWhitespaces = ignoreLeadingWhitespaces; }
    public boolean getIgnoreLeadingWhitespaces() { return this.ignoreLeadingWhitespaces; }

    public void setUnicodeEscapeInterpretation(boolean interpretUnicodeEscapes) { this.interpretUnicodeEscapes = interpretUnicodeEscapes; }
    public boolean getUnicodeEscapeInterpretation() { return this.interpretUnicodeEscapes; }

    public void setIgnoreEmptyLines(boolean ignoreEmptyLines) { this.ignoreEmptyLines = ignoreEmptyLines; }
    public boolean getIgnoreEmptyLines() { return this.ignoreEmptyLines; }

    public Object clone() {
      try {
        return super.clone();
      } catch (CloneNotSupportedException e) {
        throw new RuntimeException(e);  // impossible
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7481.java