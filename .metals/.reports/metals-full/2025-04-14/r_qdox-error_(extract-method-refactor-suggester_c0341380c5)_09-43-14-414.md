error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9863.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9863.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9863.java
text:
```scala
i@@f (sourceFile.exists() && !sourceFile.delete()) {

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
package org.apache.tools.ant.taskdefs.optional.junit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import junit.framework.AssertionFailedError;
import junit.framework.Test;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.util.FileUtils;

/**
 * <p>Collects all failing test <i>cases</i> and creates a new JUnit test class containing
 * a suite() method which calls these failed tests.</p>
 * <p>Having classes <i>A</i> ... <i>D</i> with each several testcases you could earn a new
 * test class like
 * <pre>
 * // generated on: 2007.08.06 09:42:34,555
 * import junit.framework.*;
 * public class FailedTests extends TestCase {
 *     public FailedTests(String testname) {
 *         super(testname);
 *     }
 *     public static Test suite() {
 *         TestSuite suite = new TestSuite();
 *         suite.addTest( new B("test04") );
 *         suite.addTest( new org.D("test10") );
 *         return suite;
 *     }
 * }
 * </pre>
 *
 * Because each running test case gets its own formatter, we collect
 * the failing test cases in a static list. Because we dont have a finalizer
 * method in the formatters "lifecycle", we register this formatter as
 * BuildListener and generate the new java source on taskFinished event.
 *
 * @since Ant 1.8.0
 */
public class FailureRecorder extends ProjectComponent implements JUnitResultFormatter, BuildListener {

    /**
     * This is the name of a magic System property ({@value}). The value of this
     * <b>System</b> property should point to the location where to store the
     * generated class (without suffix).
     * Default location and name is defined in DEFAULT_CLASS_LOCATION.
     * @see #DEFAULT_CLASS_LOCATION
     */
    public static final String MAGIC_PROPERTY_CLASS_LOCATION
        = "ant.junit.failureCollector";

    /** Default location and name for the generated JUnit class file,
     *  in the temp directory + FailedTests */
    public static final String DEFAULT_CLASS_LOCATION
        = System.getProperty("java.io.tmpdir") + "FailedTests";

    /** Prefix for logging. {@value} */
    private static final String LOG_PREFIX = "    [junit]";

    /** Class names of failed tests without duplicates. */
    private static SortedSet/*<TestInfos>*/ failedTests = new TreeSet();

    /** A writer for writing the generated source to. */
    private BufferedWriter writer;

    /**
     * Location and name of the generated JUnit class.
     * Lazy instantiated via getLocationName().
     */
    private static String locationName;

    /**
     * Returns the (lazy evaluated) location for the collector class.
     * Order for evaluation: System property > Ant property > default value
     * @return location for the collector class
     * @see #MAGIC_PROPERTY_CLASS_LOCATION
     * @see #DEFAULT_CLASS_LOCATION
     */
    private String getLocationName() {
        if (locationName == null) {
            String syspropValue = System.getProperty(MAGIC_PROPERTY_CLASS_LOCATION);
            String antpropValue = getProject().getProperty(MAGIC_PROPERTY_CLASS_LOCATION);

            if (syspropValue != null) {
                locationName = syspropValue;
                verbose("System property '" + MAGIC_PROPERTY_CLASS_LOCATION + "' set, so use "
                        + "its value '" + syspropValue + "' as location for collector class.");
            } else if (antpropValue != null) {
                locationName = antpropValue;
                verbose("Ant property '" + MAGIC_PROPERTY_CLASS_LOCATION + "' set, so use "
                        + "its value '" + antpropValue + "' as location for collector class.");
            } else {
                locationName = DEFAULT_CLASS_LOCATION;
                verbose("System property '" + MAGIC_PROPERTY_CLASS_LOCATION + "' not set, so use "
                        + "value as location for collector class: '"
                        + DEFAULT_CLASS_LOCATION + "'");
            }

            File locationFile = new File(locationName);
            if (!locationFile.isAbsolute()) {
                File f = new File(getProject().getBaseDir(), locationName);
                locationName = f.getAbsolutePath();
                verbose("Location file is relative (" + locationFile + ")"
                        + " use absolute path instead (" + locationName + ")");
            }
        }

        return locationName;
    }

    /**
     * This method is called by the Ant runtime by reflection. We use the project reference for
     * registration of this class as BuildListener.
     *
     * @param project
     *            project reference
     */
    public void setProject(Project project) {
        // store project reference for logging
        super.setProject(project);
        // check if already registered
        boolean alreadyRegistered = false;
        Vector allListeners = project.getBuildListeners();
        for (int i = 0; i < allListeners.size(); i++) {
            Object listener = allListeners.get(i);
            if (listener instanceof FailureRecorder) {
                alreadyRegistered = true;
                continue;
            }
        }
        // register if needed
        if (!alreadyRegistered) {
            verbose("Register FailureRecorder (@" + this.hashCode() + ") as BuildListener");
            project.addBuildListener(this);
        }
    }

    // ===== JUnitResultFormatter =====

    /**
     * Not used
     * {@inheritDoc}
     */
    public void endTestSuite(JUnitTest suite) throws BuildException {
    }

    /**
     * Add the failed test to the list.
     * @param test the test that errored.
     * @param throwable the reason it errored.
     * @see junit.framework.TestListener#addError(junit.framework.Test, java.lang.Throwable)
     */
    public void addError(Test test, Throwable throwable) {
        failedTests.add(new TestInfos(test));
    }

    // CheckStyle:LineLengthCheck OFF - @see is long
    /**
     * Add the failed test to the list.
     * @param test the test that failed.
     * @param error the assertion that failed.
     * @see junit.framework.TestListener#addFailure(junit.framework.Test, junit.framework.AssertionFailedError)
     */
    // CheckStyle:LineLengthCheck ON
    public void addFailure(Test test, AssertionFailedError error) {
        failedTests.add(new TestInfos(test));
    }

    /**
     * Not used
     * {@inheritDoc}
     */
    public void setOutput(OutputStream out) {
        // unused, close output file so it can be deleted before the VM exits
        if (out != System.out) {
            FileUtils.close(out);
        }
    }

    /**
     * Not used
     * {@inheritDoc}
     */
    public void setSystemError(String err) {
    }

    /**
     * Not used
     * {@inheritDoc}
     */
    public void setSystemOutput(String out) {
    }

    /**
     * Not used
     * {@inheritDoc}
     */
    public void startTestSuite(JUnitTest suite) throws BuildException {
    }

    /**
     * Not used
     * {@inheritDoc}
     */
    public void endTest(Test test) {
    }

    /**
     * Not used
     * {@inheritDoc}
     */
    public void startTest(Test test) {
    }

    // ===== "Templates" for generating the JUnit class =====

    private void writeJavaClass() {
        try {
            File sourceFile = new File((getLocationName() + ".java"));
            verbose("Write collector class to '" + sourceFile.getAbsolutePath() + "'");

            if (!sourceFile.delete()) {
                throw new IOException("could not delete " + sourceFile);
            }
            writer = new BufferedWriter(new FileWriter(sourceFile));

            createClassHeader();
            createSuiteMethod();
            createClassFooter();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtils.close(writer);
        }
    }

    private void createClassHeader() throws IOException {
        String className = getLocationName().replace('\\', '/');
        if (className.indexOf('/') > -1) {
            className = className.substring(className.lastIndexOf('/') + 1);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss,SSS");
        writer.write("// generated on: ");
        writer.write(sdf.format(new Date()));
        writer.newLine();
        writer.write("import junit.framework.*;");
        writer.newLine();
        writer.write("public class ");
        writer.write(className);
        // If this class does not extend TC, Ant doesnt run these
        writer.write(" extends TestCase {");
        writer.newLine();
        // standard String-constructor
        writer.write("    public ");
        writer.write(className);
        writer.write("(String testname) {");
        writer.newLine();
        writer.write("        super(testname);");
        writer.newLine();
        writer.write("    }");
        writer.newLine();
    }

    private void createSuiteMethod() throws IOException {
        writer.write("    public static Test suite() {");
        writer.newLine();
        writer.write("        TestSuite suite = new TestSuite();");
        writer.newLine();
        for (Iterator iter = failedTests.iterator(); iter.hasNext();) {
            TestInfos testInfos = (TestInfos) iter.next();
            writer.write("        suite.addTest(");
            writer.write(String.valueOf(testInfos));
            writer.write(");");
            writer.newLine();
        }
        writer.write("        return suite;");
        writer.newLine();
        writer.write("    }");
        writer.newLine();
    }

    private void createClassFooter() throws IOException {
        writer.write("}");
        writer.newLine();
    }

    // ===== Helper classes and methods =====

    /**
     * Logging facade in INFO-mode.
     * @param message Log-message
     */
    public void log(String message) {
        getProject().log(LOG_PREFIX + " " + message, Project.MSG_INFO);
    }

    /**
     * Logging facade in VERBOSE-mode.
     * @param message Log-message
     */
    public void verbose(String message) {
        getProject().log(LOG_PREFIX + " " + message, Project.MSG_VERBOSE);
    }

    /**
     * TestInfos holds information about a given test for later use.
     */
    public static class TestInfos implements Comparable {

        /** The class name of the test. */
        private final String className;

        /** The method name of the testcase. */
        private final String methodName;

        /**
         * This constructor extracts the needed information from the given test.
         * @param test Test to analyze
         */
        public TestInfos(Test test) {
            className = test.getClass().getName();
            String _methodName = test.toString();
            methodName = _methodName.substring(0, _methodName.indexOf('('));
        }

        /**
         * This String-Representation can directly be used for instantiation of
         * the JUnit testcase.
         * @return the string representation.
         * @see java.lang.Object#toString()
         * @see FailureRecorder#createSuiteMethod()
         */
        public String toString() {
            return "new " + className + "(\"" + methodName + "\")";
        }

        /**
         * The SortedMap needs comparable elements.
         * @param other the object to compare to.
         * @return the result of the comparison.
         * @see java.lang.Comparable#compareTo
         * @see SortedSet#comparator()
         */
        public int compareTo(Object other) {
            if (other instanceof TestInfos) {
                TestInfos otherInfos = (TestInfos) other;
                return toString().compareTo(otherInfos.toString());
            } else {
                return -1;
            }
        }
        public boolean equals(Object obj) {
            return obj instanceof TestInfos && toString().equals(obj.toString());
        }
        public int hashCode() {
            return toString().hashCode();
        }
    }

    // ===== BuildListener =====

    /**
     * Not used
     * {@inheritDoc}
     */
    public void buildFinished(BuildEvent event) {
    }

    /**
     * Not used
     * {@inheritDoc}
     */
    public void buildStarted(BuildEvent event) {
    }

    /**
     * Not used
     * {@inheritDoc}
     */
    public void messageLogged(BuildEvent event) {
    }

    /**
     * Not used
     * {@inheritDoc}
     */
    public void targetFinished(BuildEvent event) {
    }

    /**
     * Not used
     * {@inheritDoc}
     */
    public void targetStarted(BuildEvent event) {
    }

    /**
     * The task outside of this JUnitResultFormatter is the <junit> task. So all tests passed
     * and we could create the new java class.
     * @param event  not used
     * @see org.apache.tools.ant.BuildListener#taskFinished(org.apache.tools.ant.BuildEvent)
     */
    public void taskFinished(BuildEvent event) {
        if (!failedTests.isEmpty()) {
            writeJavaClass();
        }
    }

    /**
     * Not used
     * {@inheritDoc}
     */
    public void taskStarted(BuildEvent event) {
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9863.java