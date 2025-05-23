error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/892.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/892.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/892.java
text:
```scala
a@@ssertFalse("Should not ignore empty lines", format.getIgnoreEmptyLines());

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

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Token.Type.COMMENT;
import static org.apache.commons.csv.Token.Type.EOF;
import static org.apache.commons.csv.Token.Type.EORECORD;
import static org.apache.commons.csv.Token.Type.TOKEN;
import static org.apache.commons.csv.TokenMatchers.hasContent;
import static org.apache.commons.csv.TokenMatchers.matches;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

/**
 *
 *
 * @version $Id$
 */
public class LexerTest {

    private CSVFormat formatWithEscaping;

    @Before
    public void setUp() {
        formatWithEscaping = CSVFormat.DEFAULT.withEscape('\\');
    }

    private Lexer getLexer(final String input, final CSVFormat format) {
        return new Lexer(format, new ExtendedBufferedReader(new StringReader(input)));
    }

    @Test
    public void testSurroundingSpacesAreDeleted() throws IOException {
        final String code = "noSpaces,  leadingSpaces,trailingSpaces  ,  surroundingSpaces  ,  ,,";
        final Lexer parser = getLexer(code, CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "noSpaces"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "leadingSpaces"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "trailingSpaces"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "surroundingSpaces"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, ""));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, ""));
        assertThat(parser.nextToken(new Token()), matches(EOF, ""));
    }

    @Test
    public void testSurroundingTabsAreDeleted() throws IOException {
        final String code = "noTabs,\tleadingTab,trailingTab\t,\tsurroundingTabs\t,\t\t,,";
        final Lexer parser = getLexer(code, CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "noTabs"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "leadingTab"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "trailingTab"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "surroundingTabs"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, ""));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, ""));
        assertThat(parser.nextToken(new Token()), matches(EOF, ""));
    }

    @Test
    public void testIgnoreEmptyLines() throws IOException {
        final String code =
                "first,line,\n"+
                "\n"+
                "\n"+
                "second,line\n"+
                "\n"+
                "\n"+
                "third line \n"+
                "\n"+
                "\n"+
                "last, line \n"+
                "\n"+
                "\n"+
                "\n";
        final CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        final Lexer parser = getLexer(code, format);

        assertThat(parser.nextToken(new Token()), matches(TOKEN, "first"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "line"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, ""));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "second"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "line"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "third line "));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "last"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, " line "));
        assertThat(parser.nextToken(new Token()), matches(EOF, ""));
        assertThat(parser.nextToken(new Token()), matches(EOF, ""));
    }

    @Test
    public void testComments() throws IOException {
        final String code =
                "first,line,\n"+
                "second,line,tokenWith#no-comment\n"+
                "# comment line \n"+
                "third,line,#no-comment\n"+
                "# penultimate comment\n"+
                "# Final comment\n";
        final CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');
        final Lexer parser = getLexer(code, format);

        assertThat(parser.nextToken(new Token()), matches(TOKEN, "first"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "line"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, ""));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "second"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "line"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "tokenWith#no-comment"));
        assertThat(parser.nextToken(new Token()), matches(COMMENT, "comment line"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "third"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "line"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "#no-comment"));
        assertThat(parser.nextToken(new Token()), matches(COMMENT, "penultimate comment"));
        assertThat(parser.nextToken(new Token()), matches(COMMENT, "Final comment"));
        assertThat(parser.nextToken(new Token()), matches(EOF, ""));
        assertThat(parser.nextToken(new Token()), matches(EOF, ""));
    }

    @Test
    public void testCommentsAndEmptyLines() throws IOException {
        final String code =
                "1,2,3,\n"+                // 1
                "\n"+                      // 1b
                "\n"+                      // 1c
                "a,b x,c#no-comment\n"+    // 2
                "#foo\n"+                  // 3
                "\n"+                      // 4
                "\n"+                      // 4b
                "d,e,#no-comment\n"+       // 5
                "\n"+                      // 5b
                "\n"+                      // 5c
                "# penultimate comment\n"+ // 6
                "\n"+                      // 6b
                "\n"+                      // 6c
                "# Final comment\n";       // 7
        final CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#').withIgnoreEmptyLines(false);
        assertFalse("Should not ignore empty lines", format.isIgnoringEmptyLines());

        final Lexer parser = getLexer(code, format);


        assertThat(parser.nextToken(new Token()), matches(TOKEN, "1"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "2"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "3"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, ""));             // 1
        assertThat(parser.nextToken(new Token()), matches(EORECORD, ""));             // 1b
        assertThat(parser.nextToken(new Token()), matches(EORECORD, ""));             // 1c
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "a"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "b x"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "c#no-comment")); // 2
        assertThat(parser.nextToken(new Token()), matches(COMMENT, "foo"));           // 3
        assertThat(parser.nextToken(new Token()), matches(EORECORD, ""));             // 4
        assertThat(parser.nextToken(new Token()), matches(EORECORD, ""));             // 4b
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "d"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "e"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "#no-comment"));  // 5
        assertThat(parser.nextToken(new Token()), matches(EORECORD, ""));             // 5b
        assertThat(parser.nextToken(new Token()), matches(EORECORD, ""));             // 5c
        assertThat(parser.nextToken(new Token()), matches(COMMENT, "penultimate comment"));              // 6
        assertThat(parser.nextToken(new Token()), matches(EORECORD, ""));             // 6b
        assertThat(parser.nextToken(new Token()), matches(EORECORD, ""));             // 6c
        assertThat(parser.nextToken(new Token()), matches(COMMENT, "Final comment"));              // 7
        assertThat(parser.nextToken(new Token()), matches(EOF, ""));
        assertThat(parser.nextToken(new Token()), matches(EOF, ""));

    }

    // simple token with escaping not enabled
    @Test
    public void testBackslashWithoutEscaping() throws IOException {
        /* file: a,\,,b
        *       \,,
        */
        final String code = "a,\\,,b\\\n\\,,";
        final CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.isEscaping());
        final Lexer parser = getLexer(code, format);

        assertThat(parser.nextToken(new Token()), matches(TOKEN, "a"));
        // an unquoted single backslash is not an escape char
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "\\"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, ""));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "b\\"));
        // an unquoted single backslash is not an escape char
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "\\"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, ""));
        assertThat(parser.nextToken(new Token()), matches(EOF, ""));
    }

    // simple token with escaping enabled
    @Test
    public void testBackslashWithEscaping() throws IOException {
        /* file: a,\,,b
        *       \,,
        */
        final String code = "a,\\,,b\\\\\n\\,,\\\nc,d\\\r\ne";
        final CSVFormat format = formatWithEscaping.withIgnoreEmptyLines(false);
        assertTrue(format.isEscaping());
        final Lexer parser = getLexer(code, format);

        assertThat(parser.nextToken(new Token()), matches(TOKEN, "a"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, ","));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "b\\"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, ","));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "\nc"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "d\r"));
        assertThat(parser.nextToken(new Token()), matches(EOF, "e"));
    }

    // encapsulator tokenizer (single line)
    @Test
    public void testNextToken4() throws IOException {
        /* file:  a,"foo",b
        *        a,   " foo",b
        *        a,"foo "   ,b     // whitespace after closing encapsulator
        *        a,  " foo " ,b
        */
        final String code = "a,\"foo\",b\na,   \" foo\",b\na,\"foo \"  ,b\na,  \" foo \"  ,b";
        final Lexer parser = getLexer(code, CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "a"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "foo"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "b"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "a"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, " foo"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "b"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "a"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "foo "));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "b"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "a"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, " foo "));
//      assertTokenEquals(EORECORD, "b", parser.nextToken(new Token()));
        assertThat(parser.nextToken(new Token()), matches(EOF, "b"));
    }

    // encapsulator tokenizer (multi line, delimiter in string)
    @Test
    public void testNextToken5() throws IOException {
        final String code = "a,\"foo\n\",b\n\"foo\n  baar ,,,\"\n\"\n\t \n\"";
        final Lexer parser = getLexer(code, CSVFormat.DEFAULT);
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "a"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "foo\n"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "b"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "foo\n  baar ,,,"));
        assertThat(parser.nextToken(new Token()), matches(EOF, "\n\t \n"));

    }

    // change delimiters, comment, encapsulater
    @Test
    public void testNextToken6() throws IOException {
        /* file: a;'b and \' more
        *       '
        *       !comment;;;;
        *       ;;
        */
        final String code = "a;'b and '' more\n'\n!comment;;;;\n;;";
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteChar('\'').withCommentMarker('!').withDelimiter(';');
        final Lexer parser = getLexer(code, format);
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "a"));
        assertThat(parser.nextToken(new Token()), matches(EORECORD, "b and ' more\n"));
    }

    // From CSV-1
    @Test
    public void testDelimiterIsWhitespace() throws IOException {
        final String code = "one\ttwo\t\tfour \t five\t six";
        final Lexer parser = getLexer(code, CSVFormat.TDF);
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "one"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "two"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, ""));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "four"));
        assertThat(parser.nextToken(new Token()), matches(TOKEN, "five"));
        assertThat(parser.nextToken(new Token()), matches(EOF, "six"));
    }

    @Test
    public void testEscapedCR() throws Exception {
        final Lexer lexer = getLexer("character\\" + CR + "Escaped", formatWithEscaping);
        assertThat(lexer.nextToken(new Token()), hasContent("character" + CR + "Escaped"));
    }

    @Test
    public void testCR() throws Exception {
        final Lexer lexer = getLexer("character" + CR + "NotEscaped", formatWithEscaping);
        assertThat(lexer.nextToken(new Token()), hasContent("character"));
        assertThat(lexer.nextToken(new Token()), hasContent("NotEscaped"));
    }

    @Test
    public void testEscapedLF() throws Exception {
        final Lexer lexer = getLexer("character\\" + LF + "Escaped", formatWithEscaping);
        assertThat(lexer.nextToken(new Token()), hasContent("character" + LF + "Escaped"));
    }

    @Test
    public void testLF() throws Exception {
        final Lexer lexer = getLexer("character" + LF + "NotEscaped", formatWithEscaping);
        assertThat(lexer.nextToken(new Token()), hasContent("character"));
        assertThat(lexer.nextToken(new Token()), hasContent("NotEscaped"));
    }

    @Test // TODO is this correct? Do we expect <esc>TAB to be unescaped?
    public void testEscapedTab() throws Exception {
        final Lexer lexer = getLexer("character\\" + TAB + "Escaped", formatWithEscaping);
        assertThat(lexer.nextToken(new Token()), hasContent("character" + TAB + "Escaped"));
    }

    @Test
    public void testTab() throws Exception {
        final Lexer lexer = getLexer("character" + TAB + "NotEscaped", formatWithEscaping);
        assertThat(lexer.nextToken(new Token()), hasContent("character" + TAB + "NotEscaped"));
    }

    @Test // TODO is this correct? Do we expect <esc>BACKSPACE to be unescaped?
    public void testEscapedBackspace() throws Exception {
        final Lexer lexer = getLexer("character\\" + BACKSPACE + "Escaped", formatWithEscaping);
        assertThat(lexer.nextToken(new Token()), hasContent("character" + BACKSPACE + "Escaped"));
    }

    @Test
    public void testBackspace() throws Exception {
        final Lexer lexer = getLexer("character" + BACKSPACE + "NotEscaped", formatWithEscaping);
        assertThat(lexer.nextToken(new Token()), hasContent("character" + BACKSPACE + "NotEscaped"));
    }

    @Test // TODO is this correct? Do we expect <esc>FF to be unescaped?
    public void testEscapedFF() throws Exception {
        final Lexer lexer = getLexer("character\\" + FF + "Escaped", formatWithEscaping);
        assertThat(lexer.nextToken(new Token()), hasContent("character" + FF + "Escaped"));
    }

    @Test
    public void testFF() throws Exception {
        final Lexer lexer = getLexer("character" + FF + "NotEscaped", formatWithEscaping);
        assertThat(lexer.nextToken(new Token()), hasContent("character" + FF + "NotEscaped"));
    }

    @Test
    public void testEscapedMySqlNullValue() throws Exception {
        // MySQL uses \N to symbolize null values. We have to restore this
        final Lexer lexer = getLexer("character\\NEscaped", formatWithEscaping);
        assertThat(lexer.nextToken(new Token()), hasContent("character\\NEscaped"));
    }

    @Test
    public void testEscapedCharacter() throws Exception {
        final Lexer lexer = getLexer("character\\aEscaped", formatWithEscaping);
        assertThat(lexer.nextToken(new Token()), hasContent("character\\aEscaped"));
    }

    @Test
    public void testEscapedControlCharacter() throws Exception {
        // we are explicitly using an escape different from \ here
        final Lexer lexer = getLexer("character!rEscaped", CSVFormat.DEFAULT.withEscape('!'));
        assertThat(lexer.nextToken(new Token()), hasContent("character" + CR + "Escaped"));
    }

    @Test
    public void testEscapedControlCharacter2() throws Exception {
        final Lexer lexer = getLexer("character\\rEscaped", CSVFormat.DEFAULT.withEscape('\\'));
        assertThat(lexer.nextToken(new Token()), hasContent("character" + CR + "Escaped"));
    }

    @Test(expected = IOException.class)
    public void testEscapingAtEOF() throws Exception {
        final String code = "escaping at EOF is evil\\";
        final Lexer lexer = getLexer(code, formatWithEscaping);

        lexer.nextToken(new Token());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/892.java