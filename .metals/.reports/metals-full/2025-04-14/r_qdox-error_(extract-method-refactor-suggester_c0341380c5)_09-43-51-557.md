error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8424.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8424.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8424.java
text:
```scala
public v@@oid testA(){}

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights
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
package org.apache.tools.ant.taskdefs.optional.junit;

import java.io.*;
import junit.framework.*;
import org.apache.tools.ant.BuildException;

/**
 * Small testcase for the runner, tests are very very very basics.
 * They must be enhanced with time.
 *
 * @author <a href="mailto:sbailliez@imediation.com">Stephane Bailliez</a>
 */
public class JUnitTestRunnerTest extends TestCase {

    // mandatory constructor
    public JUnitTestRunnerTest(String name){
        super(name);
    }

    // check that having no suite generates no errors
    public void testNoSuite(){
        TestRunner runner = createRunner(NoSuiteTestCase.class);
        runner.run();
        assertEquals(runner.getFormatter().getError(), JUnitTestRunner.SUCCESS, runner.getRetCode());
    }

    // check that a suite generates no errors
    public void testSuite(){
        TestRunner runner = createRunner(SuiteTestCase.class);
        runner.run();
        assertEquals(runner.getFormatter().getError(), JUnitTestRunner.SUCCESS, runner.getRetCode());
    }

    // check that an invalid suite generates an error.
    public void testInvalidSuite(){
        TestRunner runner = createRunner(InvalidSuiteTestCase.class);
        runner.run();
        String error = runner.getFormatter().getError();
        assertEquals(error, JUnitTestRunner.ERRORS, runner.getRetCode());
        assertTrue(error, error.indexOf("thrown on purpose") != -1);
    }

    // check that something which is not a testcase generates no errors
    // at first even though this is incorrect.
    public void testNoTestCase(){
        TestRunner runner = createRunner(NoTestCase.class);
        runner.run();
        assertEquals(runner.getFormatter().getError(), JUnitTestRunner.FAILURES, runner.getRetCode());
    }

    // check that an exception in the constructor is noticed
    public void testInvalidTestCase(){
        TestRunner runner = createRunner(InvalidTestCase.class);
        runner.run();
        String error = runner.getFormatter().getError();
        assertEquals(error, JUnitTestRunner.FAILURES, runner.getRetCode());
        //@fixme as of now does not report the original stacktrace.
        //assertTrue(error, error.indexOf("thrown on purpose") != -1);
    }
    
    protected TestRunner createRunner(Class clazz){
        return new TestRunner(new JUnitTest(clazz.getName()), true, true);
    }

    // the test runner that wrap the dummy formatter that interests us
    private final static class TestRunner extends JUnitTestRunner {
        private ResultFormatter formatter = new ResultFormatter();
        TestRunner(JUnitTest test, boolean x, boolean y){
            super(test, x, y,  TestRunner.class.getClassLoader());
            // use the classloader that loaded this class otherwise
            // it will not be able to run inner classes if this test
            // is ran in non-forked mode.
            addFormatter(formatter);
        }
        ResultFormatter getFormatter(){
            return formatter;
        }
    }

    // dummy formatter just to catch the error
    private final static class ResultFormatter implements JUnitResultFormatter {
        private Throwable error;
        public void setSystemOutput(String output){}
        public void setSystemError(String output){}
        public void startTestSuite(JUnitTest suite) throws BuildException{}
        public void endTestSuite(JUnitTest suite) throws BuildException{}
        public void setOutput(java.io.OutputStream out){}
        public void startTest(Test t) {}
        public void endTest(Test test) {}
        public void addFailure(Test test, Throwable t) { }
        public void addFailure(Test test, AssertionFailedError t) { }
        public void addError(Test test, Throwable t) {
            error = t;
        }
        String getError(){
            if (error == null){
                return "";
            }
            StringWriter sw = new StringWriter();
            error.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
    }

    public static class NoTestCase {
    }

    public static class InvalidTestCase extends TestCase {
        public InvalidTestCase(String name){
            super(name);
            throw new NullPointerException("thrown on purpose");
        }
    }
    
    public static class NoSuiteTestCase extends TestCase {
        public NoSuiteTestCase(String name){ super(name); }
        public void testA(){};
    }

    public static class SuiteTestCase extends NoSuiteTestCase {
        public SuiteTestCase(String name){ super(name); }
        public static Test suite(){
            return new TestSuite(SuiteTestCase.class);
        }
    }

    public static class InvalidSuiteTestCase extends NoSuiteTestCase {
        public InvalidSuiteTestCase(String name){ super(name); }
        public static Test suite(){
            throw new NullPointerException("thrown on purpose");
        }
    }
    public static void main(String[] args){
        junit.textui.TestRunner.run(JUnitTestRunnerTest.class);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8424.java