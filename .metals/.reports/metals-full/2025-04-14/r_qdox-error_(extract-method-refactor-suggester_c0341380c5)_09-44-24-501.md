error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5400.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5400.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5400.java
text:
```scala
a@@ssertTrue("Windows line separator", reg.matches("end of text\r\n"));

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.tools.ant.util.regexp;

import java.io.*;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for all implementations of the RegexpMatcher interface.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 */
public abstract class RegexpMatcherTest extends TestCase {

    public final static String UNIX_LINE = "\n";

    private RegexpMatcher reg;

    public abstract RegexpMatcher getImplementation();

    protected final RegexpMatcher getReg() {return reg;}

    public RegexpMatcherTest(String name) {
        super(name);
    }

    public void setUp() {
        reg = getImplementation();
    }

    public void testMatches() {
        reg.setPattern("aaaa");
        assertTrue("aaaa should match itself", reg.matches("aaaa"));
        assertTrue("aaaa should match xaaaa", reg.matches("xaaaa"));
        assertTrue("aaaa shouldn\'t match xaaa", !reg.matches("xaaa"));
        reg.setPattern("^aaaa");
        assertTrue("^aaaa shouldn\'t match xaaaa", !reg.matches("xaaaa"));
        assertTrue("^aaaa should match aaaax", reg.matches("aaaax"));
        reg.setPattern("aaaa$");
        assertTrue("aaaa$ shouldn\'t match aaaax", !reg.matches("aaaax"));
        assertTrue("aaaa$ should match xaaaa", reg.matches("xaaaa"));
        reg.setPattern("[0-9]+");
        assertTrue("[0-9]+ should match 123", reg.matches("123"));
        assertTrue("[0-9]+ should match 1", reg.matches("1"));
        assertTrue("[0-9]+ shouldn\'t match \'\'", !reg.matches(""));
        assertTrue("[0-9]+ shouldn\'t match a", !reg.matches("a"));
        reg.setPattern("[0-9]*");
        assertTrue("[0-9]* should match 123", reg.matches("123"));
        assertTrue("[0-9]* should match 1", reg.matches("1"));
        assertTrue("[0-9]* should match \'\'", reg.matches(""));
        assertTrue("[0-9]* should match a", reg.matches("a"));
        reg.setPattern("([0-9]+)=\\1");
        assertTrue("([0-9]+)=\\1 should match 1=1", reg.matches("1=1"));
        assertTrue("([0-9]+)=\\1 shouldn\'t match 1=2", !reg.matches("1=2"));
    }

    public void testGroups() {
        reg.setPattern("aaaa");
        Vector v = reg.getGroups("xaaaa");
        assertEquals("No parens -> no extra groups", 1, v.size());
        assertEquals("Trivial match with no parens", "aaaa", 
                     (String) v.elementAt(0));

        reg.setPattern("(aaaa)");
        v = reg.getGroups("xaaaa");
        assertEquals("Trivial match with single paren", 2, v.size());
        assertEquals("Trivial match with single paren, full match", "aaaa", 
                     (String) v.elementAt(0));
        assertEquals("Trivial match with single paren, matched paren", "aaaa", 
                     (String) v.elementAt(0));

        reg.setPattern("(a+)b(b+)");
        v = reg.getGroups("xaabb");
        assertEquals(3, v.size());
        assertEquals("aabb", (String) v.elementAt(0));
        assertEquals("aa", (String) v.elementAt(1));
        assertEquals("b", (String) v.elementAt(2));
    }

    public void testCaseInsensitiveMatch() {
        reg.setPattern("aaaa");
        assertTrue("aaaa doesn't match AAaa", !reg.matches("AAaa"));
        assertTrue("aaaa matches AAaa ignoring case", 
                   reg.matches("AAaa", RegexpMatcher.MATCH_CASE_INSENSITIVE));
    }


// make sure there are no issues concerning line separator interpretation
// a line separator for regex (perl) is always a unix line (ie \n)

    public void testParagraphCharacter() throws IOException {
        reg.setPattern("end of text$");
        assertTrue("paragraph character", !reg.matches("end of text\u2029"));
    }

    public void testLineSeparatorCharacter() throws IOException {
        reg.setPattern("end of text$");
        assertTrue("line-separator character", !reg.matches("end of text\u2028"));
    }

    public void testNextLineCharacter() throws IOException {
        reg.setPattern("end of text$");
        assertTrue("next-line character", !reg.matches("end of text\u0085"));
    }

    public void testStandaloneCR() throws IOException {
        reg.setPattern("end of text$");
        assertTrue("standalone CR", !reg.matches("end of text\r"));
    }

    public void testWindowsLineSeparator() throws IOException {
        reg.setPattern("end of text$");
        assertTrue("Windows line separator", !reg.matches("end of text\r\n"));
    }

    public void testWindowsLineSeparator2() throws IOException {
        reg.setPattern("end of text\r$");
        //        assertTrue("Windows line separator", reg.matches("end of text\r\n"));
    }

    public void testUnixLineSeparator() throws IOException {
        reg.setPattern("end of text$");
        assertTrue("Unix line separator", reg.matches("end of text\n"));
    }


    public void testMultiVersusSingleLine() throws IOException {
        StringBuffer buf = new StringBuffer();
        buf.append("Line1").append(UNIX_LINE);
        buf.append("starttest Line2").append(UNIX_LINE);
        buf.append("Line3 endtest").append(UNIX_LINE);
        buf.append("Line4").append(UNIX_LINE);
        String text = buf.toString();
        
        doStartTest1(text);
        doStartTest2(text);
        doEndTest1(text);
        doEndTest2(text);
    }

    protected void doStartTest1(String text) {
        reg.setPattern("^starttest");
        assertTrue("^starttest in default mode", !reg.matches(text));
        assertTrue("^starttest in single line mode", 
               !reg.matches(text, RegexpMatcher.MATCH_SINGLELINE));
        assertTrue("^starttest in multi line mode", 
               reg.matches(text, RegexpMatcher.MATCH_MULTILINE));
    }

    protected void doStartTest2(String text) {
        reg.setPattern("^Line1");
        assertTrue("^Line1 in default mode", reg.matches(text));
        assertTrue("^Line1 in single line mode", 
               reg.matches(text, RegexpMatcher.MATCH_SINGLELINE));
        assertTrue("^Line1 in multi line mode", 
               reg.matches(text, RegexpMatcher.MATCH_MULTILINE));
    }

    protected void doEndTest1(String text) {
        reg.setPattern("endtest$");
        assertTrue("endtest$ in default mode", !reg.matches(text));
        assertTrue("endtest$ in single line mode", 
               !reg.matches(text, RegexpMatcher.MATCH_SINGLELINE));
        assertTrue("endtest$ in multi line mode", 
               reg.matches(text, RegexpMatcher.MATCH_MULTILINE));
    }

    protected void doEndTest2(String text) {
        reg.setPattern("Line4$");
        assertTrue("Line4$ in default mode", reg.matches(text));
        assertTrue("Line4$ in single line mode", 
               reg.matches(text, RegexpMatcher.MATCH_SINGLELINE));
        assertTrue("Line4$ in multi line mode", 
               reg.matches(text, RegexpMatcher.MATCH_MULTILINE));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5400.java