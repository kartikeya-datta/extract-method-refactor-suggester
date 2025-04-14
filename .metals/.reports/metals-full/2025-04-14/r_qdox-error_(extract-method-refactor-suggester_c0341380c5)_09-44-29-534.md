error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10553.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10553.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10553.java
text:
```scala
f@@ormat.delimiter = delimiter;

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
 * The format specification of a CSV file.
 *
 * This class is immutable.
 */
public class CSVFormat implements Cloneable, Serializable {

    private char delimiter = ',';
    private char encapsulator = '"';
    private char commentStart = DISABLED;
    private char escape = DISABLED;
    private boolean leadingSpacesIgnored = true;
    private boolean trailingSpacesIgnored = true;
    private boolean unicodeEscapesInterpreted = false;
    private boolean emptyLinesIgnored = true;
    private String lineSeparator = "\n";


    /**
     * Constant char to be used for disabling comments, escapes and encapsulation.
     * The value -2 is used because it won't be confused with an EOF signal (-1),
     * and because the unicode value FFFE would be encoded as two chars (using surrogates)
     * and thus there should never be a collision with a real text char.
     */
    public static final char DISABLED = '\ufffe';

    /** Standard comma separated format. */
    public static final CSVFormat DEFAULT = new CSVFormat(',', '"', DISABLED, DISABLED, true, true, false, true);

    /** Excel file format (using a comma as the value delimiter). */
    public static final CSVFormat EXCEL = new CSVFormat(',', '"', DISABLED, DISABLED, false, false, false, false);

    /** Tabulation delimited format. */
    public static final CSVFormat TDF = new CSVFormat('\t', '"', DISABLED, DISABLED, true, true, false, true);


    /**
     * Creates a CSVFormat with the default parameters.
     */
    public CSVFormat() {
    }

    public CSVFormat(char delimiter, char encapsulator, char commentStart) {
        this(delimiter, encapsulator, commentStart, DISABLED, true, true, false, true);
    }

    /**
     * Customized CSV format constructor.
     *
     * @param delimiter                 a char used for value separation
     * @param encapsulator              a char used as value encapsulation marker
     * @param commentStart              a char used for comment identification
     * @param escape                    a char used to escape special characters in values
     * @param leadingSpacesIgnored      TRUE when leading whitespaces should be ignored
     * @param trailingSpacesIgnored     TRUE when trailing whitespaces should be ignored
     * @param unicodeEscapesInterpreted TRUE when unicode escapes should be interpreted
     * @param emptyLinesIgnored         TRUE when the parser should skip emtpy lines
     */
    public CSVFormat(
            char delimiter,
            char encapsulator,
            char commentStart,
            char escape,
            boolean leadingSpacesIgnored,
            boolean trailingSpacesIgnored,
            boolean unicodeEscapesInterpreted,
            boolean emptyLinesIgnored) {
        this.delimiter = delimiter;
        this.encapsulator = encapsulator;
        this.commentStart = commentStart;
        this.escape = escape;
        this.leadingSpacesIgnored = leadingSpacesIgnored;
        this.trailingSpacesIgnored = trailingSpacesIgnored;
        this.unicodeEscapesInterpreted = unicodeEscapesInterpreted;
        this.emptyLinesIgnored = emptyLinesIgnored;
    }

    public char getDelimiter() {
        return delimiter;
    }

    public CSVFormat withDelimiter(char delimiter) {
        CSVFormat format = (CSVFormat) clone();
        this.delimiter = delimiter;
        return format;
    }

    public char getEncapsulator() {
        return encapsulator;
    }

    public CSVFormat withEncapsulator(char encapsulator) {
        CSVFormat format = (CSVFormat) clone();
        format.encapsulator = encapsulator;
        return format;
    }

    boolean isEncapsulating() {
        return this.encapsulator != DISABLED;
    }

    public char getCommentStart() {
        return commentStart;
    }

    public CSVFormat withCommentStart(char commentStart) {
        CSVFormat format = (CSVFormat) clone();
        format.commentStart = commentStart;
        return format;
    }

    public boolean isCommentingDisabled() {
        return this.commentStart == DISABLED;
    }

    public char getEscape() {
        return escape;
    }

    public CSVFormat withEscape(char escape) {
        CSVFormat format = (CSVFormat) clone();
        format.escape = escape;
        return format;
    }

    boolean isEscaping() {
        return this.escape != DISABLED;
    }

    public boolean isLeadingSpacesIgnored() {
        return leadingSpacesIgnored;
    }

    public CSVFormat withLeadingSpacesIgnored(boolean leadingSpacesIgnored) {
        CSVFormat format = (CSVFormat) clone();
        format.leadingSpacesIgnored = leadingSpacesIgnored;
        return format;
    }

    public boolean isTrailingSpacesIgnored() {
        return trailingSpacesIgnored;
    }

    public CSVFormat withTrailingSpacesIgnored(boolean trailingSpacesIgnored) {
        CSVFormat format = (CSVFormat) clone();
        format.trailingSpacesIgnored = trailingSpacesIgnored;
        return format;
    }

    public boolean isUnicodeEscapesInterpreted() {
        return unicodeEscapesInterpreted;
    }

    public CSVFormat withUnicodeEscapesInterpreted(boolean unicodeEscapesInterpreted) {
        CSVFormat format = (CSVFormat) clone();
        format.unicodeEscapesInterpreted = unicodeEscapesInterpreted;
        return format;
    }

    public boolean isEmptyLinesIgnored() {
        return emptyLinesIgnored;
    }

    public CSVFormat withEmptyLinesIgnored(boolean emptyLinesIgnored) {
        CSVFormat format = (CSVFormat) clone();
        format.emptyLinesIgnored = emptyLinesIgnored;
        return format;
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    public CSVFormat withLineSeparator(String lineSeparator) {
        CSVFormat format = (CSVFormat) clone();
        format.lineSeparator = lineSeparator;
        return format;
    }

    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw (Error) new InternalError().initCause(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10553.java