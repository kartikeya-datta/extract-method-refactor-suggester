error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10176.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10176.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10176.java
text:
```scala
J@@UnitVersionHelper.getTestCaseClassName(test));

/*
 * Copyright  2000-2005 The Apache Software Foundation
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

package org.apache.tools.ant.taskdefs.optional.junit;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Date;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.DOMElementWriter;
import org.apache.tools.ant.util.DateUtils;
import org.apache.tools.ant.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


/**
 * Prints XML output of the test to a specified Writer.
 *
 * @see FormatterElement
 */

public class XMLJUnitResultFormatter implements JUnitResultFormatter, XMLConstants {

    /** constant for unnnamed testsuites/cases */
    private static final String UNKNOWN = "unknown";

    private static DocumentBuilder getDocumentBuilder() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (Exception exc) {
            throw new ExceptionInInitializerError(exc);
        }
    }

    /**
     * The XML document.
     */
    private Document doc;
    /**
     * The wrapper for the whole testsuite.
     */
    private Element rootElement;
    /**
     * Element for the current test.
     */
    private Hashtable testElements = new Hashtable();
    /**
     * tests that failed.
     */
    private Hashtable failedTests = new Hashtable();
    /**
     * Timing helper.
     */
    private Hashtable testStarts = new Hashtable();
    /**
     * Where to write the log to.
     */
    private OutputStream out;

    public XMLJUnitResultFormatter() {
    }

    public void setOutput(OutputStream out) {
        this.out = out;
    }

    public void setSystemOutput(String out) {
        formatOutput(SYSTEM_OUT, out);
    }

    public void setSystemError(String out) {
        formatOutput(SYSTEM_ERR, out);
    }

    /**
     * The whole testsuite started.
     */
    public void startTestSuite(JUnitTest suite) {
        doc = getDocumentBuilder().newDocument();
        rootElement = doc.createElement(TESTSUITE);
        String n = suite.getName();
        rootElement.setAttribute(ATTR_NAME, n == null ? UNKNOWN : n);

        //add the timestamp
        final String timestamp = DateUtils.format(new Date(),
                DateUtils.ISO8601_DATETIME_PATTERN);
        rootElement.setAttribute(TIMESTAMP, timestamp);
        //and the hostname.
        rootElement.setAttribute(HOSTNAME, getHostname());

        // Output properties
        Element propsElement = doc.createElement(PROPERTIES);
        rootElement.appendChild(propsElement);
        Properties props = suite.getProperties();
        if (props != null) {
            Enumeration e = props.propertyNames();
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                Element propElement = doc.createElement(PROPERTY);
                propElement.setAttribute(ATTR_NAME, name);
                propElement.setAttribute(ATTR_VALUE, props.getProperty(name));
                propsElement.appendChild(propElement);
            }
        }
    }

    /**
     * get the local hostname
     * @return the name of the local host, or "localhost" if we cannot work it out
     */
    private String getHostname()  {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    /**
     * The whole testsuite ended.
     */
    public void endTestSuite(JUnitTest suite) throws BuildException {
        rootElement.setAttribute(ATTR_TESTS, "" + suite.runCount());
        rootElement.setAttribute(ATTR_FAILURES, "" + suite.failureCount());
        rootElement.setAttribute(ATTR_ERRORS, "" + suite.errorCount());
        rootElement.setAttribute(ATTR_TIME, "" + (suite.getRunTime() / 1000.0));
        if (out != null) {
            Writer wri = null;
            try {
                wri = new BufferedWriter(new OutputStreamWriter(out, "UTF8"));
                wri.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                (new DOMElementWriter()).write(rootElement, wri, 0, "  ");
                wri.flush();
            } catch (IOException exc) {
                throw new BuildException("Unable to write log file", exc);
            } finally {
                if (out != System.out && out != System.err) {
                    FileUtils.close(wri);
                }
            }
        }
    }

    /**
     * Interface TestListener.
     *
     * <p>A new Test is started.
     */
    public void startTest(Test t) {
        testStarts.put(t, new Long(System.currentTimeMillis()));
    }

    /**
     * Interface TestListener.
     *
     * <p>A Test is finished.
     */
    public void endTest(Test test) {
        // Fix for bug #5637 - if a junit.extensions.TestSetup is
        // used and throws an exception during setUp then startTest
        // would never have been called
        if (!testStarts.containsKey(test)) {
            startTest(test);
        }

        Element currentTest = null;
        if (!failedTests.containsKey(test)) {
            currentTest = doc.createElement(TESTCASE);
            String n = JUnitVersionHelper.getTestCaseName(test);
            currentTest.setAttribute(ATTR_NAME,
                                     n == null ? UNKNOWN : n);
            // a TestSuite can contain Tests from multiple classes,
            // even tests with the same name - disambiguate them.
            currentTest.setAttribute(ATTR_CLASSNAME,
                                     test.getClass().getName());
            rootElement.appendChild(currentTest);
            testElements.put(test, currentTest);
        } else {
            currentTest = (Element) testElements.get(test);
        }

        Long l = (Long) testStarts.get(test);
        currentTest.setAttribute(ATTR_TIME,
            "" + ((System.currentTimeMillis() - l.longValue()) / 1000.0));
    }

    /**
     * Interface TestListener for JUnit &lt;= 3.4.
     *
     * <p>A Test failed.
     */
    public void addFailure(Test test, Throwable t) {
        formatError(FAILURE, test, t);
    }

    /**
     * Interface TestListener for JUnit &gt; 3.4.
     *
     * <p>A Test failed.
     */
    public void addFailure(Test test, AssertionFailedError t) {
        addFailure(test, (Throwable) t);
    }

    /**
     * Interface TestListener.
     *
     * <p>An error occurred while running the test.
     */
    public void addError(Test test, Throwable t) {
        formatError(ERROR, test, t);
    }

    private void formatError(String type, Test test, Throwable t) {
        if (test != null) {
            endTest(test);
            failedTests.put(test, test);
        }

        Element nested = doc.createElement(type);
        Element currentTest = null;
        if (test != null) {
            currentTest = (Element) testElements.get(test);
        } else {
            currentTest = rootElement;
        }

        currentTest.appendChild(nested);

        String message = t.getMessage();
        if (message != null && message.length() > 0) {
            nested.setAttribute(ATTR_MESSAGE, t.getMessage());
        }
        nested.setAttribute(ATTR_TYPE, t.getClass().getName());

        String strace = JUnitTestRunner.getFilteredTrace(t);
        Text trace = doc.createTextNode(strace);
        nested.appendChild(trace);
    }

    private void formatOutput(String type, String output) {
        Element nested = doc.createElement(type);
        rootElement.appendChild(nested);
        nested.appendChild(doc.createCDATASection(output));
    }

} // XMLJUnitResultFormatter
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10176.java