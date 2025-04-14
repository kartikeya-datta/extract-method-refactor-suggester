error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/499.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/499.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/499.java
text:
```scala
private static final b@@oolean WINDOWS = File.separatorChar == '\\';

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
package org.apache.commons.io;

import java.io.File;
import java.util.Locale;

import junit.framework.TestCase;

public class FilenameUtilsWildcardTestCase extends TestCase {

    private static final boolean WINDOWS = (File.separatorChar == '\\');

    public FilenameUtilsWildcardTestCase(String name) {
        super(name);
    }

    //-----------------------------------------------------------------------
    // Testing:
    //   FilenameUtils.wildcardMatch(String,String)

    public void testMatch() {
        assertFalse(FilenameUtils.wildcardMatch(null, "Foo"));
        assertFalse(FilenameUtils.wildcardMatch("Foo", null));
        assertTrue(FilenameUtils.wildcardMatch(null, null));
        assertTrue(FilenameUtils.wildcardMatch("Foo", "Foo"));
        assertTrue(FilenameUtils.wildcardMatch("", ""));
        assertTrue(FilenameUtils.wildcardMatch("", "*"));
        assertFalse(FilenameUtils.wildcardMatch("", "?"));
        assertTrue(FilenameUtils.wildcardMatch("Foo", "Fo*"));
        assertTrue(FilenameUtils.wildcardMatch("Foo", "Fo?"));
        assertTrue(FilenameUtils.wildcardMatch("Foo Bar and Catflap", "Fo*"));
        assertTrue(FilenameUtils.wildcardMatch("New Bookmarks", "N?w ?o?k??r?s"));
        assertFalse(FilenameUtils.wildcardMatch("Foo", "Bar"));
        assertTrue(FilenameUtils.wildcardMatch("Foo Bar Foo", "F*o Bar*"));
        assertTrue(FilenameUtils.wildcardMatch("Adobe Acrobat Installer", "Ad*er"));
        assertTrue(FilenameUtils.wildcardMatch("Foo", "*Foo"));
        assertTrue(FilenameUtils.wildcardMatch("BarFoo", "*Foo"));
        assertTrue(FilenameUtils.wildcardMatch("Foo", "Foo*"));
        assertTrue(FilenameUtils.wildcardMatch("FooBar", "Foo*"));
        assertFalse(FilenameUtils.wildcardMatch("FOO", "*Foo"));
        assertFalse(FilenameUtils.wildcardMatch("BARFOO", "*Foo"));
        assertFalse(FilenameUtils.wildcardMatch("FOO", "Foo*"));
        assertFalse(FilenameUtils.wildcardMatch("FOOBAR", "Foo*"));
    }

    public void testMatchOnSystem() {
        assertFalse(FilenameUtils.wildcardMatchOnSystem(null, "Foo"));
        assertFalse(FilenameUtils.wildcardMatchOnSystem("Foo", null));
        assertTrue(FilenameUtils.wildcardMatchOnSystem(null, null));
        assertTrue(FilenameUtils.wildcardMatchOnSystem("Foo", "Foo"));
        assertTrue(FilenameUtils.wildcardMatchOnSystem("", ""));
        assertTrue(FilenameUtils.wildcardMatchOnSystem("Foo", "Fo*"));
        assertTrue(FilenameUtils.wildcardMatchOnSystem("Foo", "Fo?"));
        assertTrue(FilenameUtils.wildcardMatchOnSystem("Foo Bar and Catflap", "Fo*"));
        assertTrue(FilenameUtils.wildcardMatchOnSystem("New Bookmarks", "N?w ?o?k??r?s"));
        assertFalse(FilenameUtils.wildcardMatchOnSystem("Foo", "Bar"));
        assertTrue(FilenameUtils.wildcardMatchOnSystem("Foo Bar Foo", "F*o Bar*"));
        assertTrue(FilenameUtils.wildcardMatchOnSystem("Adobe Acrobat Installer", "Ad*er"));
        assertTrue(FilenameUtils.wildcardMatchOnSystem("Foo", "*Foo"));
        assertTrue(FilenameUtils.wildcardMatchOnSystem("BarFoo", "*Foo"));
        assertTrue(FilenameUtils.wildcardMatchOnSystem("Foo", "Foo*"));
        assertTrue(FilenameUtils.wildcardMatchOnSystem("FooBar", "Foo*"));
        assertEquals(WINDOWS, FilenameUtils.wildcardMatchOnSystem("FOO", "*Foo"));
        assertEquals(WINDOWS, FilenameUtils.wildcardMatchOnSystem("BARFOO", "*Foo"));
        assertEquals(WINDOWS, FilenameUtils.wildcardMatchOnSystem("FOO", "Foo*"));
        assertEquals(WINDOWS, FilenameUtils.wildcardMatchOnSystem("FOOBAR", "Foo*"));
    }

    public void testMatchCaseSpecified() {
        assertFalse(FilenameUtils.wildcardMatch(null, "Foo", IOCase.SENSITIVE));
        assertFalse(FilenameUtils.wildcardMatch("Foo", null, IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch(null, null, IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("Foo", "Foo", IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("", "", IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("Foo", "Fo*", IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("Foo", "Fo?", IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("Foo Bar and Catflap", "Fo*", IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("New Bookmarks", "N?w ?o?k??r?s", IOCase.SENSITIVE));
        assertFalse(FilenameUtils.wildcardMatch("Foo", "Bar", IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("Foo Bar Foo", "F*o Bar*", IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("Adobe Acrobat Installer", "Ad*er", IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("Foo", "*Foo", IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("Foo", "Foo*", IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("Foo", "*Foo", IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("BarFoo", "*Foo", IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("Foo", "Foo*", IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("FooBar", "Foo*", IOCase.SENSITIVE));
        
        assertFalse(FilenameUtils.wildcardMatch("FOO", "*Foo", IOCase.SENSITIVE));
        assertFalse(FilenameUtils.wildcardMatch("BARFOO", "*Foo", IOCase.SENSITIVE));
        assertFalse(FilenameUtils.wildcardMatch("FOO", "Foo*", IOCase.SENSITIVE));
        assertFalse(FilenameUtils.wildcardMatch("FOOBAR", "Foo*", IOCase.SENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("FOO", "*Foo", IOCase.INSENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("BARFOO", "*Foo", IOCase.INSENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("FOO", "Foo*", IOCase.INSENSITIVE));
        assertTrue(FilenameUtils.wildcardMatch("FOOBAR", "Foo*", IOCase.INSENSITIVE));
        assertEquals(WINDOWS, FilenameUtils.wildcardMatch("FOO", "*Foo", IOCase.SYSTEM));
        assertEquals(WINDOWS, FilenameUtils.wildcardMatch("BARFOO", "*Foo", IOCase.SYSTEM));
        assertEquals(WINDOWS, FilenameUtils.wildcardMatch("FOO", "Foo*", IOCase.SYSTEM));
        assertEquals(WINDOWS, FilenameUtils.wildcardMatch("FOOBAR", "Foo*", IOCase.SYSTEM));
    }

    public void testSplitOnTokens() {
        assertArrayEquals( new String[] { "Ad", "*", "er" }, FilenameUtils.splitOnTokens("Ad*er") );
        assertArrayEquals( new String[] { "Ad", "?", "er" }, FilenameUtils.splitOnTokens("Ad?er") );
        assertArrayEquals( new String[] { "Test", "*", "?", "One" }, FilenameUtils.splitOnTokens("Test*?One") );
        assertArrayEquals( new String[] { "Test", "?", "*", "One" }, FilenameUtils.splitOnTokens("Test?*One") );
        assertArrayEquals( new String[] { "*" }, FilenameUtils.splitOnTokens("****") );
        assertArrayEquals( new String[] { "*", "?", "?", "*" }, FilenameUtils.splitOnTokens("*??*") );
        assertArrayEquals( new String[] { "*", "?", "*", "?", "*" }, FilenameUtils.splitOnTokens("*?**?*") );
        assertArrayEquals( new String[] { "h", "?", "?", "*" }, FilenameUtils.splitOnTokens("h??*") );
        assertArrayEquals( new String[] { "" }, FilenameUtils.splitOnTokens("") );
    }

    private void assertArrayEquals(Object[] a1, Object[] a2) {
        assertEquals(a1.length, a2.length);
        for(int i=0; i<a1.length; i++) {
            assertEquals(a1[i], a2[i]);
        }
    }

    private void assertMatch(String text, String wildcard, boolean expected) {
        assertEquals(text + " " + wildcard, expected, FilenameUtils.wildcardMatch(text, wildcard));
    }

    // A separate set of tests, added to this batch
    public void testMatch2() {
        assertMatch("log.txt", "log.txt", true);
        assertMatch("log.txt1", "log.txt", false);

        assertMatch("log.txt", "log.txt*", true);
        assertMatch("log.txt", "log.txt*1", false);
        assertMatch("log.txt", "*log.txt*", true);

        assertMatch("log.txt", "*.txt", true);
        assertMatch("txt.log", "*.txt", false);
        assertMatch("config.ini", "*.ini", true);

        assertMatch("config.txt.bak", "con*.txt", false);

        assertMatch("log.txt9", "*.txt?", true);
        assertMatch("log.txt", "*.txt?", false);

        assertMatch("progtestcase.java~5~", "*test*.java~*~", true);
        assertMatch("progtestcase.java;5~", "*test*.java~*~", false);
        assertMatch("progtestcase.java~5", "*test*.java~*~", false);

        assertMatch("log.txt", "log.*", true);

        assertMatch("log.txt", "log?*", true);

        assertMatch("log.txt12", "log.txt??", true);
        
        assertMatch("log.log", "log**log", true);
        assertMatch("log.log", "log**", true);
        assertMatch("log.log", "log.**", true);
        assertMatch("log.log", "**.log", true);
        assertMatch("log.log", "**log", true);

        assertMatch("log.log", "log*log", true);
        assertMatch("log.log", "log*", true);
        assertMatch("log.log", "log.*", true);
        assertMatch("log.log", "*.log", true);
        assertMatch("log.log", "*log", true);
        
        assertMatch("log.log", "*log?", false);
        assertMatch("log.log", "*log?*", true);
        assertMatch("log.log.abc", "*log?abc", true);
        assertMatch("log.log.abc.log.abc", "*log?abc", true);
        assertMatch("log.log.abc.log.abc.d", "*log?abc?d", true);
    }

    /**
     * See https://issues.apache.org/jira/browse/IO-246
     */
    public void test_IO_246() {

        // Tests for "*?"
        assertMatch("aaa", "aa*?", true);
        // these ought to work as well, but "*?" does not work properly at present
//      assertMatch("aaa", "a*?", true);
//      assertMatch("aaa", "*?", true);

        // Tests for "?*"
        assertMatch("",    "?*",   false);
        assertMatch("a",   "a?*",  false);
        assertMatch("aa",  "aa?*", false);
        assertMatch("a",   "?*",   true);
        assertMatch("aa",  "?*",   true);
        assertMatch("aaa", "?*",   true);

        // Test ending on "?"
        assertMatch("",    "?", false);
        assertMatch("a",   "a?", false);
        assertMatch("aa",  "aa?", false);
        assertMatch("aab", "aa?", true);
        assertMatch("aaa", "*a", true);
    }

    public void testLocaleIndependence() {
        Locale orig = Locale.getDefault();

        Locale[] locales = Locale.getAvailableLocales();

        String[][] data = {
            { "I", "i"},
            { "i", "I"},
            { "i", "\u0130"},
            { "i", "\u0131"},
            { "\u03A3", "\u03C2"},
            { "\u03A3", "\u03C3"},
            { "\u03C2", "\u03C3"},
        };

        try {
            for (int i = 0; i < data.length; i++) {
                for (Locale locale : locales) {
                    Locale.setDefault(locale);
                    assertTrue("Test data corrupt: " + i, data[i][0].equalsIgnoreCase(data[i][1]));
                    boolean match = FilenameUtils.wildcardMatch(data[i][0], data[i][1], IOCase.INSENSITIVE);
                    assertTrue(Locale.getDefault().toString() + ": " + i, match);
                }
            }
        } finally {
            Locale.setDefault(orig);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/499.java