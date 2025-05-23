error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/116.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/116.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/116.java
text:
```scala
private v@@oid print(char[] value, int offset, int len, boolean checkForEscape) throws IOException {

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

import java.io.IOException;
import java.io.Writer;

/**
 * Print values as a comma separated list.
 */
public class CSVPrinter {

    /** The place that the values get written. */
    private final Writer out;
    private final CSVFormat format;

    /** True if we just began a new line. */
    private boolean newLine = true;

    /** Temporary buffer */
    private char[] buf = new char[0];  

    /**
     * Create a printer that will print values to the given stream following the CSVFormat.
     * <p/>
     * Currently, only a pure encapsulation format or a pure escaping format
     * is supported. Hybrid formats (encapsulation and escaping with a different character) are not supported.
     *
     * @param out    stream to which to print.
     * @param format describes the CSV variation.
     */
    public CSVPrinter(Writer out, CSVFormat format) {
        this.out = out;
        this.format = format == null ? CSVFormat.DEFAULT : format;
    }

    // ======================================================
    //  printing implementation
    // ======================================================

    /**
     * Output a blank line
     */
    public void println() throws IOException {
        out.write(format.getLineSeparator());
        newLine = true;
    }

    /**
     * Flush the underlying stream.
     * 
     * @throws IOException
     */
    public void flush() throws IOException {
        out.flush();
    }


    /**
     * Print a single line of comma separated values.
     * The values will be quoted if needed.  Quotes and
     * newLine characters will be escaped.
     *
     * @param values values to be outputted.
     */
    public void println(String... values) throws IOException {
        for (String value : values) {
            print(value);
        }
        println();
    }


    /**
     * Put a comment on a new line among the comma separated values. Comments
     * will always begin on a new line and occupy a least one full line. The
     * character specified to start comments and a space will be inserted at
     * the beginning of each new line in the comment.
     * <p/>
     * If comments are disabled in the current CSV format this method does nothing.
     *
     * @param comment the comment to output
     */
    public void printComment(String comment) throws IOException {
        if (this.format.isCommentingDisabled()) {
            return;
        }
        if (!newLine) {
            println();
        }
        out.write(format.getCommentStart());
        out.write(' ');
        for (int i = 0; i < comment.length(); i++) {
            char c = comment.charAt(i);
            switch (c) {
                case '\r':
                    if (i + 1 < comment.length() && comment.charAt(i + 1) == '\n') {
                        i++;
                    }
                    // break intentionally excluded.
                case '\n':
                    println();
                    out.write(format.getCommentStart());
                    out.write(' ');
                    break;
                default:
                    out.write(c);
                    break;
            }
        }
        println();
    }


    public void print(char[] value, int offset, int len, boolean checkForEscape) throws IOException {
        if (!checkForEscape) {
            printSep();
            out.write(value, offset, len);
            return;
        }
        
        if (format.isEncapsulating()) {
            printAndEncapsulate(value, offset, len);
        } else if (format.isEscaping()) {
            printAndEscape(value, offset, len);
        } else {
            printSep();
            out.write(value, offset, len);
        }
    }

    void printSep() throws IOException {
        if (newLine) {
            newLine = false;
        } else {
            out.write(this.format.getDelimiter());
        }
    }

    void printAndEscape(char[] value, int offset, int len) throws IOException {
        int start = offset;
        int pos = offset;
        int end = offset + len;

        printSep();

        char delim = this.format.getDelimiter();
        char escape = this.format.getEscape();

        while (pos < end) {
            char c = value[pos];
            if (c == '\r' || c == '\n' || c == delim || c == escape) {
                // write out segment up until this char
                int l = pos - start;
                if (l > 0) {
                    out.write(value, start, l);
                }
                if (c == '\n') {
                    c = 'n';
                } else if (c == '\r') {
                    c = 'r';
                }

                out.write(escape);
                out.write(c);

                start = pos + 1; // start on the current char after this one
            }

            pos++;
        }

        // write last segment
        int l = pos - start;
        if (l > 0) {
            out.write(value, start, l);
        }
    }

    void printAndEncapsulate(char[] value, int offset, int len) throws IOException {
        boolean first = newLine;  // is this the first value on this line?
        boolean quote = false;
        int start = offset;
        int pos = offset;
        int end = offset + len;

        printSep();

        char delim = this.format.getDelimiter();
        char encapsulator = this.format.getEncapsulator();

        if (len <= 0) {
            // always quote an empty token that is the first
            // on the line, as it may be the only thing on the
            // line. If it were not quoted in that case,
            // an empty line has no tokens.
            if (first) {
                quote = true;
            }
        } else {
            char c = value[pos];

            // Hmmm, where did this rule come from?
            if (first
                    && (c < '0'
 (c > '9' && c < 'A')
 (c > 'Z' && c < 'a')
 (c > 'z'))) {
                quote = true;
                // } else if (c == ' ' || c == '\f' || c == '\t') {
            } else if (c <= '#') {
                // Some other chars at the start of a value caused the parser to fail, so for now
                // encapsulate if we start in anything less than '#'.  We are being conservative
                // by including the default comment char too.
                quote = true;
            } else {
                while (pos < end) {
                    c = value[pos];
                    if (c == '\n' || c == '\r' || c == encapsulator || c == delim) {
                        quote = true;
                        break;
                    }
                    pos++;
                }

                if (!quote) {
                    pos = end - 1;
                    c = value[pos];
                    // if (c == ' ' || c == '\f' || c == '\t') {
                    // Some other chars at the end caused the parser to fail, so for now
                    // encapsulate if we end in anything less than ' '
                    if (c <= ' ') {
                        quote = true;
                    }
                }
            }
        }

        if (!quote) {
            // no encapsulation needed - write out the original value
            out.write(value, offset, len);
            return;
        }

        // we hit something that needed encapsulation
        out.write(encapsulator);

        // Pick up where we left off: pos should be positioned on the first character that caused
        // the need for encapsulation.
        while (pos < end) {
            char c = value[pos];
            if (c == encapsulator) {
                // write out the chunk up until this point

                // add 1 to the length to write out the encapsulator also
                out.write(value, start, pos - start + 1);
                // put the next starting position on the encapsulator so we will
                // write it out again with the next string (effectively doubling it)
                start = pos;
            }
            pos++;
        }

        // write the last segment
        out.write(value, start, pos - start);
        out.write(encapsulator);
    }

    /**
     * Print the string as the next value on the line. The value
     * will be escaped or encapsulated as needed if checkForEscape==true
     *
     * @param value value to be outputted.
     */
    public void print(String value, boolean checkForEscape) throws IOException {
        if (!checkForEscape) {
            // write directly from string
            printSep();
            out.write(value);
            return;
        }

        if (buf.length < value.length()) {
            buf = new char[value.length()];
        }

        value.getChars(0, value.length(), buf, 0);
        print(buf, 0, value.length(), checkForEscape);
    }

    /**
     * Print the string as the next value on the line. The value
     * will be escaped or encapsulated as needed.
     *
     * @param value value to be outputted.
     */
    public void print(String value) throws IOException {
        print(value, true);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/116.java