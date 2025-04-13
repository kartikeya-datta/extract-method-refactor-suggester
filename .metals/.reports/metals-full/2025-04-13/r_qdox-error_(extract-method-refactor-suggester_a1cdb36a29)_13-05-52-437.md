error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9167.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9167.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9167.java
text:
```scala
final l@@ong startLineNumber = getLineNumber();

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

import static org.apache.commons.csv.Token.Type.COMMENT;
import static org.apache.commons.csv.Token.Type.EOF;
import static org.apache.commons.csv.Token.Type.EORECORD;
import static org.apache.commons.csv.Token.Type.INVALID;
import static org.apache.commons.csv.Token.Type.TOKEN;

import java.io.IOException;

class CSVLexer1306663 extends Lexer {

    // ctor needs to be public so can be called dynamically by PerformanceTest class
    public CSVLexer1306663(final CSVFormat format, final ExtendedBufferedReader in) {
        super(format, in);
    }

    /**
     * Returns the next token.
     * <p/>
     * A token corresponds to a term, a record change or an end-of-file indicator.
     *
     * @param tkn an existing Token object to reuse. The caller is responsible to initialize the Token.
     * @return the next token found
     * @throws java.io.IOException on stream access error
     */
    @Override
    Token nextToken(final Token tkn) throws IOException {

        // get the last read char (required for empty line detection)
        int lastChar = in.readAgain();

        //  read the next char and set eol
        int c = in.read();

        if (isStartOfLine(lastChar) && isCommentStart(c)) {
            in.readLine();
            tkn.type = COMMENT;
            return tkn;
        }

        /* note: unfortunately isEndOfLine may consumes a character silently.
        *       this has no effect outside of the method. so a simple workaround
        *       is to call 'readAgain' on the stream...
        */
        boolean eol = isEndOfLine(c);
        c = in.readAgain();

        //  empty line detection: eol AND (last char was EOL or beginning)
        if (ignoreEmptyLines) {
            while (eol && isStartOfLine(lastChar)) {
                // go on char ahead ...
                lastChar = c;
                c = in.read();
                eol = isEndOfLine(c);
                c = in.readAgain();
                // reached end of file without any content (empty line at the end)
                if (isEndOfFile(c)) {
                    tkn.type = EOF;
                    // don't set tkn.isReady here because no content
                    return tkn;
                }
            }
        }

        // did we reach eof during the last iteration already ? EOF
        if (isEndOfFile(lastChar) || (!isDelimiter(lastChar) && isEndOfFile(c))) {
            tkn.type = EOF;
            // don't set tkn.isReady here because no content
            return tkn;
        }

        //  important: make sure a new char gets consumed in each iteration
        while (tkn.type == INVALID) {
            // ignore whitespaces at beginning of a token
            if (ignoreSurroundingSpaces) {
                while (isWhitespace(c) && !eol) {
                    c = in.read();
                    eol = isEndOfLine(c);
                }
            }

            // ok, start of token reached: encapsulated, or token
            if (isDelimiter(c)) {
                // empty token return TOKEN("")
                tkn.type = TOKEN;
            } else if (eol) {
                // empty token return EORECORD("")
                //noop: tkn.content.append("");
                tkn.type = EORECORD;
            } else if (isEncapsulator(c)) {
                // consume encapsulated token
                encapsulatedTokenLexer(tkn);
            } else if (isEndOfFile(c)) {
                // end of file return EOF()
                //noop: tkn.content.append("");
                tkn.type = EOF;
                tkn.isReady = true; // there is data at EOF
            } else {
                // next token must be a simple token
                // add removed blanks when not ignoring whitespace chars...
                simpleTokenLexer(tkn, c);
            }
        }
        return tkn;
    }

    /**
     * A simple token lexer
     * <p/>
     * Simple token are tokens which are not surrounded by encapsulators.
     * A simple token might contain escaped delimiters (as \, or \;). The
     * token is finished when one of the following conditions become true:
     * <ul>
     *   <li>end of line has been reached (EORECORD)</li>
     *   <li>end of stream has been reached (EOF)</li>
     *   <li>an unescaped delimiter has been reached (TOKEN)</li>
     * </ul>
     *
     * @param tkn the current token
     * @param c   the current character
     * @return the filled token
     * @throws IOException on stream access error
     */
    private Token simpleTokenLexer(final Token tkn, int c) throws IOException {
        // Faster to use while(true)+break than while(tkn.type == INVALID)
        while (true) {
            if (isEndOfLine(c)) {
                tkn.type = EORECORD;
                break;
            } else if (isEndOfFile(c)) {
                tkn.type = EOF;
                tkn.isReady = true; // There is data at EOF
                break;
            } else if (isDelimiter(c)) {
                tkn.type = TOKEN;
                break;
            } else if (isEscape(c)) {
                tkn.content.append((char) readEscape());
                c = in.read(); // continue
            } else {
                tkn.content.append((char) c);
                c = in.read(); // continue
            }
        }

        if (ignoreSurroundingSpaces) {
            trimTrailingSpaces(tkn.content);
        }

        return tkn;
    }

    /**
     * An encapsulated token lexer
     * <p/>
     * Encapsulated tokens are surrounded by the given encapsulating-string.
     * The encapsulator itself might be included in the token using a
     * doubling syntax (as "", '') or using escaping (as in \", \').
     * Whitespaces before and after an encapsulated token are ignored.
     *
     * @param tkn the current token
     * @return a valid token object
     * @throws IOException on invalid state
     */
    private Token encapsulatedTokenLexer(final Token tkn) throws IOException {
        // save current line
        final int startLineNumber = getLineNumber();
        // ignore the given delimiter
        // assert c == delimiter;
        int c;
        while (true) {
            c = in.read();

            if (isEscape(c)) {
                tkn.content.append((char) readEscape());
            } else if (isEncapsulator(c)) {
                if (isEncapsulator(in.lookAhead())) {
                    // double or escaped encapsulator -> add single encapsulator to token
                    c = in.read();
                    tkn.content.append((char) c);
                } else {
                    // token finish mark (encapsulator) reached: ignore whitespace till delimiter
                    while (true) {
                        c = in.read();
                        if (isDelimiter(c)) {
                            tkn.type = TOKEN;
                            return tkn;
                        } else if (isEndOfFile(c)) {
                            tkn.type = EOF;
                            tkn.isReady = true; // There is data at EOF
                            return tkn;
                        } else if (isEndOfLine(c)) {
                            // ok eo token reached
                            tkn.type = EORECORD;
                            return tkn;
                        } else if (!isWhitespace(c)) {
                            // error invalid char between token and next delimiter
                            throw new IOException("(line " + getLineNumber() + ") invalid char between encapsulated token and delimiter");
                        }
                    }
                }
            } else if (isEndOfFile(c)) {
                // error condition (end of file before end of token)
                throw new IOException("(startline " + startLineNumber + ") EOF reached before encapsulated token finished");
            } else {
                // consume character
                tkn.content.append((char) c);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9167.java