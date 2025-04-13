error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17141.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17141.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17141.java
text:
```scala
e@@xpectBuildException("testIllegalNameInSection", "Manifest attribute names must not start with '-' at the begin.");

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
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

package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.Project;

/**
 * Testcase for the Manifest class used in the jar task.
 *
 */
public class ManifestTest extends BuildFileTest {

    public static final String EXPANDED_MANIFEST
        = "src/etc/testcases/taskdefs/manifests/META-INF/MANIFEST.MF";

    public static final String LONG_LINE
        = "AReallyLongLineToTestLineBreakingInManifests-ACapabilityWhich" +
          "IsSureToLeadToHundredsOfQuestionsAboutWhyAntMungesManifests" +
          "OfCourseTheAnswerIsThatIsWhatTheSpecRequiresAndIfAnythingHas" +
          "AProblemWithThatItIsNotABugInAnt";

    public static final String LONG_70_NAME 
        = "ThisNameIsJustSeventyCharactersWhichIsAllowedAccordingToTheSpecsFiller";
    public static final String LONG_68_NAME 
        = "ThisNameIsJustSixtyEightCharactersWhichIsAllowedAccordingToTheSpecsX";
    public static final String NOT_LONG_NAME 
        = "NameIsJustUnderSeventyCharactersWhichIsAllowedAccordingTheSpec";

    public static final String VALUE = "NOT_LONG";

    public ManifestTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/etc/testcases/taskdefs/manifest.xml");
    }

    public void tearDown() {
        executeTarget("clean");
    }

    /**
     * Empty manifest - is OK
     */
    public void test1() throws ManifestException, IOException {
        executeTarget("test1");
        Manifest manifest = getManifest(EXPANDED_MANIFEST);
        String version = manifest.getManifestVersion();
        assertEquals("Manifest was not created with correct version - ", "1.0", version);
    }

    /**
     * Simple Manifest with version 2.0
     */
    public void test2() throws ManifestException, IOException {
        executeTarget("test2");
        Manifest manifest = getManifest(EXPANDED_MANIFEST);
        String version = manifest.getManifestVersion();
        assertEquals("Manifest was not created with correct version - ", "2.0", version);
    }

    /**
     * Malformed manifest - no : on the line
     */
    public void test3() {
        expectBuildExceptionContaining("test3", "Manifest is invalid - no colon on header line",
                                       "Invalid Manifest");
    }

    /**
     * Malformed manifest - starts with continuation line
     */
    public void test4() {
        expectBuildExceptionContaining("test4", "Manifest is invalid - section starts with continuation line",
                                       "Invalid Manifest");
   }

    /**
     * Malformed manifest - Name attribute in main section
     */
    public void test5() {
        executeTarget("test5");
        String output = getLog();
        boolean hasWarning = output.indexOf("Manifest warning: \"Name\" attributes should not occur in the main section") != -1;
        assertTrue("Expected warning about Name in main section", hasWarning);
    }

    /**
     * New Section not starting with Name attribute.
     */
    public void test6() {
        expectBuildExceptionContaining("test6", "Manifest is invalid - section starts with incorrect attribute",
                                       "Invalid Manifest");
        String output = getLog();
        boolean hasWarning = output.indexOf("Manifest sections should start with a \"Name\" attribute") != -1;
        assertTrue("Expected warning about section not starting with Name: attribute", hasWarning);
    }

    /**
     * From attribute is illegal
     */
    public void test7() {
        executeTarget("test7");

        boolean hasWarning = getLog().indexOf(Manifest.ERROR_FROM_FORBIDDEN) != -1;
        assertTrue("Expected warning about From: attribute", hasWarning);
    }

    /**
     * Inline manifest - OK
     */
    public void test8() throws IOException, ManifestException {
        executeTarget("test8");
        Manifest manifest = getManifest(EXPANDED_MANIFEST);
        Manifest.Section mainSection = manifest.getMainSection();
        String classpath = mainSection.getAttributeValue("class-path");
        assertEquals("Class-Path attribute was not set correctly - ", "fubar", classpath);

        Manifest.Section testSection = manifest.getSection("Test");
        String testAttr = testSection.getAttributeValue("TestAttr");
        assertEquals("TestAttr attribute was not set correctly - ", "Test", testAttr);
    }

    /**
     * Inline manifest - Invalid since has a Name attribute in the section element
     */
    public void test9() {
        expectBuildExceptionContaining("test9", "Construction is invalid - Name attribute should not be used",
                                       "Specify the section name using the \"name\" attribute of the <section> element");
    }

    /**
     * Inline manifest - Invalid attribute without name
     */
    public void test10() {
        expectBuildExceptionContaining("test10", "Attribute has no name",
                                       "Attributes must have name and value");
    }

    /**
     * Inline manifest - Invalid attribute without value
     */
    public void test11() {
        expectBuildExceptionContaining("test11", "Attribute has no value",
                                       "Attributes must have name and value");
    }

    /**
     * Inline manifest - Invalid attribute without value
     */
    public void test12() {
        expectBuildExceptionContaining("test12", "Section with no name",
                                       "Sections must have a name");
    }

    /**
     * Inline manifest - Duplicate attribute
     */
    public void test13() {
        expectBuildExceptionContaining("test13", "Duplicate Attribute",
                                       "The attribute \"Test\" may not occur more than once in the same section");
    }

    /**
     * Inline manifest - OK since classpath entries can be duplicated.
     */
    public void test14() throws IOException, ManifestException {
        executeTarget("test14");
        Manifest manifest = getManifest(EXPANDED_MANIFEST);
        Manifest.Section mainSection = manifest.getMainSection();
        String classpath = mainSection.getAttributeValue("class-path");
        assertEquals("Class-Path attribute was not set correctly - ",
            "Test1 Test2 Test3 Test4", classpath);
    }

    /**
     * Tets long line wrapping
     */
    public void testLongLine() throws IOException, ManifestException {
        Project p = getProject();
        p.setUserProperty("test.longline", LONG_LINE);
        p.setUserProperty("test.long68name" , LONG_68_NAME);
        p.setUserProperty("test.long70name" , LONG_70_NAME);
        p.setUserProperty("test.notlongname" , NOT_LONG_NAME);
        p.setUserProperty("test.value", VALUE);
        executeTarget("testLongLine");

        Manifest manifest = getManifest(EXPANDED_MANIFEST);
        Manifest.Section mainSection = manifest.getMainSection();
        String classpath = mainSection.getAttributeValue("class-path");
        assertEquals("Class-Path attribute was not set correctly - ",
            LONG_LINE, classpath);
        
        String value = mainSection.getAttributeValue(LONG_68_NAME);
        assertEquals("LONG_68_NAME_VALUE_MISMATCH", VALUE, value);
        value = mainSection.getAttributeValue(LONG_70_NAME);
        assertEquals("LONG_70_NAME_VALUE_MISMATCH", VALUE, value);
        value = mainSection.getAttributeValue(NOT_LONG_NAME);
        assertEquals("NOT_LONG_NAME_VALUE_MISMATCH", VALUE, value);
        
        BufferedReader in = new BufferedReader(new FileReader(EXPANDED_MANIFEST));
        
        Set set = new HashSet();
        String read = in.readLine();
        while (read != null)
        {
            set.add(read);
            read = in.readLine();
        }
        
        assertTrue("Manifest file should have contained string ", set
                .remove(" NOT_LONG"));
        assertTrue("Manifest file should have contained string ", set
                .remove(" NG"));
        assertTrue("Manifest file should have contained string ", set
                .remove(LONG_70_NAME + ": "));
        assertTrue("Manifest file should have contained string ", set
                .remove(NOT_LONG_NAME + ": NOT_LO"));
    }

    /**
     * Tests ordering of sections
     */
    public void testOrder1() throws IOException, ManifestException {
        executeTarget("testOrder1");

        Manifest manifest = getManifest(EXPANDED_MANIFEST);
        Enumeration e = manifest.getSectionNames();
        String section1 = (String)e.nextElement();
        String section2 = (String)e.nextElement();
        assertEquals("First section name unexpected", "Test1", section1);
        assertEquals("Second section name unexpected", "Test2", section2);

        Manifest.Section section = manifest.getSection("Test1");
        e = section.getAttributeKeys();
        String attr1Key = (String)e.nextElement();
        String attr2Key = (String)e.nextElement();
        String attr1 = section.getAttribute(attr1Key).getName();
        String attr2 = section.getAttribute(attr2Key).getName();
        assertEquals("First attribute name unexpected", "TestAttr1", attr1);
        assertEquals("Second attribute name unexpected", "TestAttr2", attr2);
    }

    /**
     * Tests ordering of sections
     */
    public void testOrder2() throws IOException, ManifestException {
        executeTarget("testOrder2");

        Manifest manifest = getManifest(EXPANDED_MANIFEST);
        Enumeration e = manifest.getSectionNames();
        String section1 = (String)e.nextElement();
        String section2 = (String)e.nextElement();
        assertEquals("First section name unexpected", "Test2", section1);
        assertEquals("Second section name unexpected", "Test1", section2);

        Manifest.Section section = manifest.getSection("Test1");
        e = section.getAttributeKeys();
        String attr1Key = (String)e.nextElement();
        String attr2Key = (String)e.nextElement();
        String attr1 = section.getAttribute(attr1Key).getName();
        String attr2 = section.getAttribute(attr2Key).getName();
        assertEquals("First attribute name unexpected", "TestAttr2", attr1);
        assertEquals("Second attribute name unexpected", "TestAttr1", attr2);
    }

    /**
     * file attribute for manifest task is required.
     */
    public void testNoFile() {
        expectBuildException("testNoFile", "file is required");
    }

    /**
     * replace changes Manifest-Version from 2.0 to 1.0
     */
    public void testReplace() throws IOException, ManifestException {
        executeTarget("testReplace");
        Manifest mf = getManifest("src/etc/testcases/taskdefs/mftest.mf");
        assertNotNull(mf);
        assertEquals(Manifest.getDefaultManifest(), mf);
    }

    /**
     * update keeps the Manifest-Version and adds a new attribute Foo
     */
    public void testUpdate() throws IOException, ManifestException {
        executeTarget("testUpdate");
        Manifest mf = getManifest("src/etc/testcases/taskdefs/mftest.mf");
        assertNotNull(mf);
        assertTrue(!Manifest.getDefaultManifest().equals(mf));
        String mfAsString = mf.toString();
        assertNotNull(mfAsString);
        assertTrue(mfAsString.startsWith("Manifest-Version: 2.0"));
        assertTrue(mfAsString.indexOf("Foo: Bar") > -1);

        mf = getManifest("src/etc/testcases/taskdefs/mftest2.mf");
        assertNotNull(mf);
        mfAsString = mf.toString();
        assertNotNull(mfAsString);
        assertEquals(-1, mfAsString.indexOf("Foo: Bar"));
        assertTrue(mfAsString.indexOf("Foo: Baz") > -1);
    }

    public void testFrom() {
        expectLogContaining("testFrom", Manifest.ERROR_FROM_FORBIDDEN);
    }

    public void testIllegalName() {
        expectBuildException("testIllegalName", "Manifest attribute names must not contain ' '");
    }

    public void testIllegalNameInSection() {
        expectBuildException("testIllegalNameInSection", "Manifest attribute names must not contain ' '");
    }

    public void testIllegalNameBegin() {
        expectBuildException("testIllegalNameInSection", "Manifest attribute names must not contain '-' at the begin.");
    }

    public void testIllegalName2() {
        expectBuildException("testIllegalName", "Manifest attribute names must not contain '.'");
    }

    public void testIllegalName3() {
        expectBuildException("testIllegalName", "Manifest attribute names must not contain '*'");
    }

    /**
     * Reads mftest.mf.
     */
    private Manifest getManifest(String filename) throws IOException, ManifestException {
        FileReader r = new FileReader(new File(System.getProperty("root"), filename));
        try {
            return new Manifest(r);
        } finally {
            r.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17141.java