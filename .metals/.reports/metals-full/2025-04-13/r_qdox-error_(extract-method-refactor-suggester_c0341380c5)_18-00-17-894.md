error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18240.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18240.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18240.java
text:
```scala
i@@f (read.indexOf("<property name=\"test.property\" value=\""+TEST_VALUE+"\" />") >= 0) {

/*
 * Copyright  2000-2004 The Apache Software Foundation
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

package org.apache.tools.ant.taskdefs.optional;

import org.apache.tools.ant.BuildFileTest;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Properties;

/**
 * Tests the EchoProperties task.
 *
 * @created   17-Jan-2002
 * @since     Ant 1.5
 */
public class EchoPropertiesTest extends BuildFileTest {

    private final static String TASKDEFS_DIR = "src/etc/testcases/taskdefs/optional/";
    private static final String GOOD_OUTFILE = "test.properties";
    private static final String GOOD_OUTFILE_XML = "test.xml";
    private static final String PREFIX_OUTFILE = "test-prefix.properties";
    private static final String TEST_VALUE = "isSet";
    private static final String BAD_OUTFILE = ".";

    public EchoPropertiesTest(String name) {
        super(name);
    }


    public void setUp() {
        configureProject(TASKDEFS_DIR + "echoproperties.xml");
        project.setProperty( "test.property", TEST_VALUE );
    }


    public void tearDown() {
        executeTarget("cleanup");
    }


    public void testEchoToLog() {
        expectLogContaining("testEchoToLog", "test.property="+TEST_VALUE);
    }


    public void testReadBadFile() {
        expectBuildExceptionContaining( "testReadBadFile",
            "srcfile is a directory", "srcfile is a directory!" );
    }


    public void testReadBadFileFail() {
        expectBuildExceptionContaining( "testReadBadFile",
            "srcfile is a directory", "srcfile is a directory!" );
    }


    public void testReadBadFileNoFail() {
        expectLog( "testReadBadFileNoFail", "srcfile is a directory!" );
    }


    public void testEchoToBadFile() {
        expectBuildExceptionContaining( "testEchoToBadFile",
            "destfile is a directory", "destfile is a directory!" );
    }


    public void testEchoToBadFileFail() {
        expectBuildExceptionContaining( "testEchoToBadFileFail",
            "destfile is a directory", "destfile is a directory!" );
    }


    public void testEchoToBadFileNoFail() {
        expectLog( "testEchoToBadFileNoFail", "destfile is a directory!");
    }


    public void testEchoToGoodFile() throws Exception {
        executeTarget( "testEchoToGoodFile" );
        assertGoodFile();
    }


    public void testEchoToGoodFileXml() throws Exception {
        executeTarget( "testEchoToGoodFileXml" );

        // read in the file
        File f = createRelativeFile( GOOD_OUTFILE_XML );
        FileReader fr = new FileReader( f );
        try {
            BufferedReader br = new BufferedReader( fr );
            String read = null;
            while ( (read = br.readLine()) != null) {
                if (read.indexOf("<property name=\"test.property\" value=\""+TEST_VALUE+"\"></property>") >= 0) {
                    // found the property we set - it's good.
                    return;
                }
            }
            fail( "did not encounter set property in generated file." );
        } finally {
            try {
                fr.close();
            } catch(IOException e) {}
        }
    }


    public void testEchoToGoodFileFail() throws Exception {
        executeTarget( "testEchoToGoodFileFail" );
        assertGoodFile();
    }


    public void testEchoToGoodFileNoFail() throws Exception {
        executeTarget( "testEchoToGoodFileNoFail" );
        assertGoodFile();
    }


    public void testEchoPrefix() throws Exception {
        testEchoPrefixVarious("testEchoPrefix");
    }

    public void testEchoPrefixAsPropertyset() throws Exception {
        testEchoPrefixVarious("testEchoPrefixAsPropertyset");
    }

    public void testEchoPrefixAsNegatedPropertyset() throws Exception {
        testEchoPrefixVarious("testEchoPrefixAsNegatedPropertyset");
    }

    public void testEchoPrefixAsDoublyNegatedPropertyset() throws Exception {
        testEchoPrefixVarious("testEchoPrefixAsDoublyNegatedPropertyset");
    }

    private void testEchoPrefixVarious(String target) throws Exception {
        executeTarget(target);
        Properties props = loadPropFile(PREFIX_OUTFILE);
        assertEquals("prefix didn't include 'a.set' property",
            "true", props.getProperty("a.set"));
        assertNull("prefix failed to filter out property 'b.set'",
            props.getProperty("b.set"));
    }

    protected Properties loadPropFile(String relativeFilename)
            throws IOException {
        File f = createRelativeFile( relativeFilename );
        Properties props=new Properties();
        InputStream in=null;
        try  {
            in=new BufferedInputStream(new FileInputStream(f));
            props.load(in);
        } finally {
            if(in!=null) {
                try { in.close(); } catch(IOException e) {}
            }
        }
        return props;
    }

    protected void assertGoodFile() throws Exception {
        File f = createRelativeFile( GOOD_OUTFILE );
        assertTrue(
            "Did not create "+f.getAbsolutePath(),
            f.exists() );
        Properties props=loadPropFile(GOOD_OUTFILE);
        props.list(System.out);
        assertEquals("test property not found ",
                     TEST_VALUE, props.getProperty("test.property"));
/*
        // read in the file
        FileReader fr = new FileReader( f );
        try {
            BufferedReader br = new BufferedReader( fr );
            String read = null;
            while ( (read = br.readLine()) != null)
            {
                if (read.indexOf("test.property" + TEST_VALUE) >= 0)
                {
                    // found the property we set - it's good.
                    return;
                }
            }
            fail( "did not encounter set property in generated file." );
        } finally {
            try { fr.close(); } catch(IOException e) {}
        }
*/
    }


    protected String toAbsolute( String filename ) {
        return createRelativeFile( filename ).getAbsolutePath();
    }


    protected File createRelativeFile( String filename ) {
        if (filename.equals( "." )) {
            return getProjectDir();
        }
        // else
        return new File( getProjectDir(), filename );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18240.java