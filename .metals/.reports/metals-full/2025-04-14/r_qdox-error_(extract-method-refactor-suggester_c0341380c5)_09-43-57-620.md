error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12236.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12236.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12236.java
text:
```scala
i@@f (isQuoteChar(intch)) {

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

import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Token.Type.COMMENT;
import static org.apache.commons.csv.Token.Type.EOF;
import static org.apache.commons.csv.Token.Type.EORECORD;
import static org.apache.commons.csv.Token.Type.INVALID;
import static org.apache.commons.csv.Token.Type.TOKEN;

import java.io.IOException;

/**
 * Experimental Lexer using enums to keep track of state and character type.
 * Unfortunately it is twice as slow.
 * For reference purpose only.
 *
 * @version $Id$
 */
class CSVLexer3 extends Lexer {

    private final char escape;

    // ctor needs to be public so can be called dynamically by PerformanceTest class
    public CSVLexer3(final CSVFormat format, final ExtendedBufferedReader in) {
        super(format, in);
        this.escape = format.getEscape();
    }

    /**
     * Classify the character types
     */
    private static enum CharType {
        DELIM,
        ESCAPE,
        ENCAP,
        EOL,
        COMMENT_START,
        WHITESPACE,
        OTHER,
        EOFCHAR
    }

    private CharType classify(final int intch) {
        if (isDelimiter(intch)) {
            return CharType.DELIM;
        }
        if (isCommentStart(intch)) {
            return CharType.COMMENT_START;
        }
        if (isEncapsulator(intch)) {
            return CharType.ENCAP;
        }
        if (isEscape(intch)) {
            return CharType.ESCAPE;
        }
        if (intch == '\r' || intch == '\n') {
            return CharType.EOL;
        }
        if (isWhitespace(intch)) { // Must be after EOL check
            return CharType.WHITESPACE;
        }
        if (intch == END_OF_STREAM) {
            return CharType.EOFCHAR;
        }
        return CharType.OTHER;
    }

    /**
     * Parsing states
     */
    private static enum State {
        BEGIN, PLAIN, INQUOTE, QUOTEQUOTE, ESCAPE_PLAIN, ESCAPE_QUOTE,
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

        State state = State.BEGIN;
        int intch;
        boolean trimTrailingSpaces = false;
        while(tkn.type == INVALID) {
            intch = in.read();
            final CharType type = classify(intch);
            switch(state) {
                case BEGIN:
                    switch(type){
                        case COMMENT_START:
                            in.readLine();
                            tkn.type = COMMENT;
                            break;
                        case ENCAP:
                            state = State.INQUOTE;
                            break;
                        case DELIM:
                            tkn.type = TOKEN;
                            break;
                        case EOL:
                            tkn.type = EORECORD;
                            break;
                        case EOFCHAR:
                            tkn.type = EOF;
                            break;
                        case ESCAPE:
                            state = State.ESCAPE_PLAIN;
                            break;
                        case OTHER:
                            tkn.content.append((char) intch);
                            state = State.PLAIN;
                            break;
                        case WHITESPACE:
                            if (!ignoreSurroundingSpaces){
                                tkn.content.append((char) intch);
                                state = State.PLAIN;
                            }
                            break;
                    }
                    break;
                case PLAIN:
                    switch(type){
                        case DELIM:
                            tkn.type = TOKEN;
                            break;
                        case EOL:
                            tkn.type = EORECORD;
                            break;
                        case EOFCHAR:
                            tkn.type = EOF;
                            break;
                        case ESCAPE:
                            state = State.ESCAPE_PLAIN;
                            break;
                        default:
                            trimTrailingSpaces = ignoreSurroundingSpaces; // we have a plain token
                            tkn.content.append((char) intch);
                            break;
                    }
                    break;
                case INQUOTE: // Started a quoted string
                    switch(type){
                        case ENCAP:
                            state = State.QUOTEQUOTE;
                            break;
                        case ESCAPE:
                            state = State.ESCAPE_QUOTE;
                            break;
                        case EOFCHAR:
                            throw new IOException("(line " + getLineNumber() + ") unexpected EOF in quoted string");
                        default:
                            tkn.content.append((char) intch);
                            break;
                    }
                    break;
                case QUOTEQUOTE: // "..." seen, expecting end of token or "
                    switch(type){
                        case DELIM:
                            tkn.type = TOKEN;
                            break;
                        case EOL:
                            tkn.type = EORECORD;
                            break;
                        case EOFCHAR:
                            tkn.type = EOF;
                            break;
                        case ENCAP: // "..."" seen, append it
                            tkn.content.append((char) intch);
                            state = State.INQUOTE;
                            break;
                        case WHITESPACE: // trailing whitespace may be allowed
                            if (!ignoreSurroundingSpaces) {
                                // error invalid char between token and next delimiter
                                throw new IOException("(line " + getLineNumber() + ") invalid char between encapsulated token and delimiter");
                            }
                            break;
                        // Everything else is invalid
                        case ESCAPE:
                        case OTHER:
                        case COMMENT_START:
                            // error invalid char between token and next delimiter
                            throw new IOException("(line " + getLineNumber() + ") invalid char between encapsulated token and delimiter");
                    }
                    break;
                case ESCAPE_PLAIN:
                    switch(type){
                        case DELIM:
                        case ESCAPE:
                        case EOL:
                            tkn.content.append((char) intch);
                            state = State.PLAIN;
                            break;
                        case COMMENT_START: // TODO should comment be escaped?
                        case ENCAP: // TODO is this correct?
                        case OTHER: // TODO may need to escape further
                        case WHITESPACE:
                            tkn.content.append(escape);
                            tkn.content.append((char) intch);
                            break;
                        case EOFCHAR:
                            throw new IOException("(line " + getLineNumber() + ") unexpected EOF in escape sequence");
                    }
                    break;
                case ESCAPE_QUOTE:
                    switch(type){
                        case ESCAPE:
                        case ENCAP: // this is the only required escape
                            tkn.content.append((char) intch);
                            break;
                        case COMMENT_START:
                        case DELIM:
                        case EOL:
                        case OTHER:
                        case WHITESPACE:
                            tkn.content.append(escape);
                            tkn.content.append((char) intch);
                            break;
                        case EOFCHAR:
                            throw new IOException("(line " + getLineNumber() + ") unexpected EOF in escape sequence");
                    }
                    break;
                default:
                    break;
            }
        }
        if (trimTrailingSpaces) {
            trimTrailingSpaces(tkn.content);
        }
        return tkn;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12236.java