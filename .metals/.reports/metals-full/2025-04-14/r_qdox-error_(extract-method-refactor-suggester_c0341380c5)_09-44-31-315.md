error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3861.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3861.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3861.java
text:
```scala
final S@@tring line = in.readLine();

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

/**
 *
 *
 * @version $Id$
 */
final class CSVLexer extends Lexer {

    /** INTERNAL API. ctor needs to be public so can be called dynamically by PerformanceTest class */
    CSVLexer(final CSVFormat format, final ExtendedBufferedReader in) {
        super(format, in);
    }

    /**
     * Returns the next token.
     * <p/>
     * A token corresponds to a term, a record change or an end-of-file indicator.
     *
     * @param token
     *            an existing Token object to reuse. The caller is responsible to initialize the Token.
     * @return the next token found
     * @throws java.io.IOException
     *             on stream access error
     */
    @Override
    Token nextToken(final Token token) throws IOException {

        // get the last read char (required for empty line detection)
        int lastChar = in.getLastChar();

        // read the next char and set eol
        int c = in.read();
        /*
         * Note: The following call will swallow LF if c == CR. But we don't need to know if the last char was CR or LF
         * - they are equivalent here.
         */
        boolean eol = readEndOfLine(c);

        // empty line detection: eol AND (last char was EOL or beginning)
        if (ignoreEmptyLines) {
            while (eol && isStartOfLine(lastChar)) {
                // go on char ahead ...
                lastChar = c;
                c = in.read();
                eol = readEndOfLine(c);
                // reached end of file without any content (empty line at the end)
                if (isEndOfFile(c)) {
                    token.type = EOF;
                    // don't set token.isReady here because no content
                    return token;
                }
            }
        }

        // did we reach eof during the last iteration already ? EOF
        if (isEndOfFile(lastChar) || (!isDelimiter(lastChar) && isEndOfFile(c))) {
            token.type = EOF;
            // don't set token.isReady here because no content
            return token;
        }

        if (isStartOfLine(lastChar) && isCommentStart(c)) {
            String line = in.readLine();
            if (line == null) {
                token.type = EOF;
                // don't set token.isReady here because no content
                return token;                
            }
            final String comment = line.trim();
            token.content.append(comment);
            token.type = COMMENT;
            return token;
        }

        // important: make sure a new char gets consumed in each iteration
        while (token.type == INVALID) {
            // ignore whitespaces at beginning of a token
            if (ignoreSurroundingSpaces) {
                while (isWhitespace(c) && !eol) {
                    c = in.read();
                    eol = readEndOfLine(c);
                }
            }

            // ok, start of token reached: encapsulated, or token
            if (isDelimiter(c)) {
                // empty token return TOKEN("")
                token.type = TOKEN;
            } else if (eol) {
                // empty token return EORECORD("")
                // noop: token.content.append("");
                token.type = EORECORD;
            } else if (isQuoteChar(c)) {
                // consume encapsulated token
                parseEncapsulatedToken(token);
            } else if (isEndOfFile(c)) {
                // end of file return EOF()
                // noop: token.content.append("");
                token.type = EOF;
                token.isReady = true; // there is data at EOF
            } else {
                // next token must be a simple token
                // add removed blanks when not ignoring whitespace chars...
                parseSimpleToken(token, c);
            }
        }
        return token;
    }

    /**
     * Parses a simple token.
     * <p/>
     * Simple token are tokens which are not surrounded by encapsulators. A simple token might contain escaped
     * delimiters (as \, or \;). The token is finished when one of the following conditions become true:
     * <ul>
     * <li>end of line has been reached (EORECORD)</li>
     * <li>end of stream has been reached (EOF)</li>
     * <li>an unescaped delimiter has been reached (TOKEN)</li>
     * </ul>
     *
     * @param token
     *            the current token
     * @param ch
     *            the current character
     * @return the filled token
     * @throws IOException
     *             on stream access error
     */
    private Token parseSimpleToken(final Token token, int ch) throws IOException {
        // Faster to use while(true)+break than while(token.type == INVALID)
        while (true) {
            if (readEndOfLine(ch)) {
                token.type = EORECORD;
                break;
            } else if (isEndOfFile(ch)) {
                token.type = EOF;
                token.isReady = true; // There is data at EOF
                break;
            } else if (isDelimiter(ch)) {
                token.type = TOKEN;
                break;
            } else if (isEscape(ch)) {
                final int unescaped = readEscape();
                if (unescaped == Constants.END_OF_STREAM) { // unexpected char after escape
                    token.content.append((char) ch).append((char) in.getLastChar());
                } else {
                    token.content.append((char) unescaped);
                }
                ch = in.read(); // continue
            } else {
                token.content.append((char) ch);
                ch = in.read(); // continue
            }
        }

        if (ignoreSurroundingSpaces) {
            trimTrailingSpaces(token.content);
        }

        return token;
    }

    /**
     * Parses an encapsulated token.
     * <p/>
     * Encapsulated tokens are surrounded by the given encapsulating-string. The encapsulator itself might be included
     * in the token using a doubling syntax (as "", '') or using escaping (as in \", \'). Whitespaces before and after
     * an encapsulated token are ignored. The token is finished when one of the following conditions become true:
     * <ul>
     * <li>an unescaped encapsulator has been reached, and is followed by optional whitespace then:</li>
     * <ul>
     * <li>delimiter (TOKEN)</li>
     * <li>end of line (EORECORD)</li>
     * </ul>
     * <li>end of stream has been reached (EOF)</li> </ul>
     *
     * @param token
     *            the current token
     * @return a valid token object
     * @throws IOException
     *             on invalid state: EOF before closing encapsulator or invalid character before delimiter or EOL
     */
    private Token parseEncapsulatedToken(final Token token) throws IOException {
        // save current line number in case needed for IOE
        final long startLineNumber = getCurrentLineNumber();
        int c;
        while (true) {
            c = in.read();

            if (isEscape(c)) {
                final int unescaped = readEscape();
                if (unescaped == Constants.END_OF_STREAM) { // unexpected char after escape
                    token.content.append((char) c).append((char) in.getLastChar());
                } else {
                    token.content.append((char) unescaped);
                }
            } else if (isQuoteChar(c)) {
                if (isQuoteChar(in.lookAhead())) {
                    // double or escaped encapsulator -> add single encapsulator to token
                    c = in.read();
                    token.content.append((char) c);
                } else {
                    // token finish mark (encapsulator) reached: ignore whitespace till delimiter
                    while (true) {
                        c = in.read();
                        if (isDelimiter(c)) {
                            token.type = TOKEN;
                            return token;
                        } else if (isEndOfFile(c)) {
                            token.type = EOF;
                            token.isReady = true; // There is data at EOF
                            return token;
                        } else if (readEndOfLine(c)) {
                            token.type = EORECORD;
                            return token;
                        } else if (!isWhitespace(c)) {
                            // error invalid char between token and next delimiter
                            throw new IOException("(line " + getCurrentLineNumber() +
                                    ") invalid char between encapsulated token and delimiter");
                        }
                    }
                }
            } else if (isEndOfFile(c)) {
                // error condition (end of file before end of token)
                throw new IOException("(startline " + startLineNumber +
                        ") EOF reached before encapsulated token finished");
            } else {
                // consume character
                token.content.append((char) c);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3861.java