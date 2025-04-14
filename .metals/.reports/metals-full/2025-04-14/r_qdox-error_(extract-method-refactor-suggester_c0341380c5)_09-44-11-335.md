error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13748.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13748.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13748.java
text:
```scala
F@@ileUtils.close(r);

/*
 * Copyright  2003-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.filters;

import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.util.FileUtils;

/**
 */
public class TokenFilterTest extends BuildFileTest {

    public TokenFilterTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/etc/testcases/filters/tokenfilter.xml");
        executeTarget("init");
    }

    public void tearDown() {
        executeTarget("cleanup");
    }

    /** make sure tokenfilter exists */
    public void testTokenfilter() throws IOException {
        executeTarget("tokenfilter");
    }

    public void testTrimignore() throws IOException {
        expectLogContaining("trimignore", "Hello-World");
    }

    public void testStringTokenizer() throws IOException {
        expectLogContaining(
            "stringtokenizer", "#This#is#a#number#of#words#");
    }

    public void testUnixLineOutput() throws IOException {
        expectFileContains(
            "unixlineoutput", "result/unixlineoutput",
            "\nThis\nis\na\nnumber\nof\nwords\n");
    }

    public void testDosLineOutput() throws IOException {
        expectFileContains(
            "doslineoutput", "result/doslineoutput",
            "\r\nThis\r\nis\r\na\r\nnumber\r\nof\r\nwords\r\n");
    }

    public void testFileTokenizer() throws IOException {
        String contents = getFileString(
            "filetokenizer", "result/filetokenizer");
        assertStringContains(contents, "   of words");
        assertStringNotContains(contents, " This is");
    }

    public void testReplaceString() throws IOException {
        expectFileContains(
            "replacestring", "result/replacestring",
            "this is the moon");
    }

    public void testReplaceStrings() throws IOException {
        expectLogContaining("replacestrings", "bar bar bar");
    }

    public void testContainsString() throws IOException {
        String contents = getFileString(
            "containsstring", "result/containsstring");
        assertStringContains(contents, "this is a line contains foo");
        assertStringNotContains(contents, "this line does not");
    }

    public void testReplaceRegex() throws IOException {
        if (! hasRegex("testReplaceRegex"))
            return;
        String contents = getFileString(
            "replaceregex", "result/replaceregex");
        assertStringContains(contents, "world world world world");
        assertStringContains(contents, "dog Cat dog");
        assertStringContains(contents, "moon Sun Sun");
        assertStringContains(contents, "found WhiteSpace");
        assertStringContains(contents, "Found digits [1234]");
        assertStringNotContains(contents, "This is a line with digits");
    }

    public void testFilterReplaceRegex() throws IOException {
        if (! hasRegex("testFilterReplaceRegex"))
            return;
        String contents = getFileString(
            "filterreplaceregex", "result/filterreplaceregex");
        assertStringContains(contents, "world world world world");
    }

    public void testHandleDollerMatch() throws IOException {
        if (! hasRegex("testFilterReplaceRegex"))
            return;
        executeTarget("dollermatch");
    }

    public void testTrimFile() throws IOException {
        String contents = getFileString(
            "trimfile", "result/trimfile");
        assertTrue("no ws at start", contents.startsWith("This is th"));
        assertTrue("no ws at end", contents.endsWith("second line."));
        assertStringContains(contents, "  This is the second");
    }

    public void testTrimFileByLine() throws IOException {
        String contents = getFileString(
            "trimfilebyline", "result/trimfilebyline");
        assertFalse("no ws at start", contents.startsWith("This is th"));
        assertFalse("no ws at end", contents.endsWith("second line."));
        assertStringNotContains(contents, "  This is the second");
        assertStringContains(contents, "file.\nThis is the second");
    }

    public void testFilterReplaceString() throws IOException {
        String contents = getFileString(
            "filterreplacestring", "result/filterreplacestring");
        assertStringContains(contents, "This is the moon");
    }

    public void testFilterReplaceStrings() throws IOException {
        expectLogContaining("filterreplacestrings", "bar bar bar");
    }

    public void testContainsRegex() throws IOException {
        if (! hasRegex("testContainsRegex"))
            return;
        String contents = getFileString(
            "containsregex", "result/containsregex");
        assertStringContains(contents, "hello world");
        assertStringNotContains(contents, "this is the moon");
        assertStringContains(contents, "World here");
    }

    public void testFilterContainsRegex() throws IOException {
        if (! hasRegex("testFilterContainsRegex"))
            return;
        String contents = getFileString(
            "filtercontainsregex", "result/filtercontainsregex");
        assertStringContains(contents, "hello world");
        assertStringNotContains(contents, "this is the moon");
        assertStringContains(contents, "World here");
    }

    public void testContainsRegex2() throws IOException {
        if (! hasRegex("testContainsRegex2"))
            return;
        String contents = getFileString(
            "containsregex2", "result/containsregex2");
        assertStringContains(contents, "void register_bits();");
    }

    public void testDeleteCharacters() throws IOException {
        String contents = getFileString(
            "deletecharacters", "result/deletechars");
        assertStringNotContains(contents, "#");
        assertStringNotContains(contents, "*");
        assertStringContains(contents, "This is some ");
    }

    public void testScriptFilter() throws IOException {
        if (! hasScript("testScriptFilter"))
            return;

        expectFileContains("scriptfilter", "result/scriptfilter",
                           "HELLO WORLD");
    }


    public void testScriptFilter2() throws IOException {
        if (! hasScript("testScriptFilter"))
            return;

        expectFileContains("scriptfilter2", "result/scriptfilter2",
                           "HELLO MOON");
    }

    public void testCustomTokenFilter() throws IOException {
        expectFileContains("customtokenfilter", "result/custom",
                           "Hello World");
    }

    // ------------------------------------------------------
    //   Helper methods
    // -----------------------------------------------------
    private boolean hasScript(String test) {
        try {
            executeTarget("hasscript");
        }
        catch (Throwable ex) {
            System.out.println(
                test + ": skipped - script not present ");
            return false;
        }
        return true;
    }

    private boolean hasRegex(String test) {
        try {
            executeTarget("hasregex");
            expectFileContains("result/replaceregexp", "bye world");
        }
        catch (Throwable ex) {
            System.out.println(test + ": skipped - regex not present "
                               + ex);
            return false;
        }
        return true;
    }

    private void assertStringContains(String string, String contains) {
        assertTrue("[" + string + "] does not contain [" + contains +"]",
                   string.indexOf(contains) > -1);
    }

    private void assertStringNotContains(String string, String contains) {
        assertTrue("[" + string + "] does contain [" + contains +"]",
                   string.indexOf(contains) == -1);
    }

    private String getFileString(String filename)
        throws IOException
    {
        Reader r = null;
        try {
            r = new FileReader(getProject().resolveFile(filename));
            return  FileUtils.readFully(r);
        }
        finally {
            try {r.close();} catch (Throwable ignore) {}
        }

    }

    private String getFileString(String target, String filename)
        throws IOException
    {
        executeTarget(target);
        return getFileString(filename);
    }

    private void expectFileContains(String name, String contains)
        throws IOException
    {
        String content = getFileString(name);
        assertTrue(
            "expecting file " + name + " to contain " + contains +
            " but got " + content, content.indexOf(contains) > -1);
    }

    private void expectFileContains(
        String target, String name, String contains)
        throws IOException
    {
        executeTarget(target);
        expectFileContains(name, contains);
    }

    public static class Capitalize
        implements TokenFilter.Filter
    {
        public String filter(String token) {
            if (token.length() == 0)
                return token;
            return token.substring(0, 1).toUpperCase() +
                token.substring(1);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13748.java