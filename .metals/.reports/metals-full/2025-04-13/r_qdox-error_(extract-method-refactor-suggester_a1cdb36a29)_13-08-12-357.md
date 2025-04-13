error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4425.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4425.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4425.java
text:
```scala
public S@@ampleResult runTest(JavaSamplerContext p_context)

// $Header$
/*
 * Copyright 2002-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
*/

package org.apache.jmeter.protocol.java.sampler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * A sampler for executing custom Java code in each sample.  See
 * {@link JavaSamplerClient} and {@link AbstractJavaSamplerClient} for
 * information on writing Java code to be executed by this sampler.
 * 
 * @author <a href="mailto:jeremy_a@bigfoot.com">Jeremy Arnold</a>
 * @version $Revision$
 */
public class JavaSampler extends AbstractSampler implements TestListener
{
    /**
     * Property key representing the classname of the JavaSamplerClient to
     * user.
     */
    public static final String CLASSNAME = "classname";

    /**
     * Property key representing the arguments for the JavaSamplerClient.
     */
    public static final String ARGUMENTS = "arguments";

    /**
     * The JavaSamplerClient instance used by this sampler to actually perform
     * the sample.
     */
    private transient JavaSamplerClient javaClient = null;

    /**
     * The JavaSamplerContext instance used by this sampler to hold
     * information related to the test run, such as the parameters
     * specified for the sampler client.
     */
    private transient JavaSamplerContext context = null;

    /**
     * Logging
     */
    private static transient Logger log = LoggingManager.getLoggerForClass();

    /**
     * Set used to register all active JavaSamplers.  This is used so that the
     * samplers can be notified when the test ends.
     */
    private static Set allSamplers = new HashSet();

    /**
     * Create a JavaSampler.
     */
    public JavaSampler()
    {
        setArguments(new Arguments());
        synchronized (allSamplers)
        {
            allSamplers.add(this);
        }
    }

    /**
     * Set the arguments (parameters) for the JavaSamplerClient to be executed
     * with.
     *
     * @param args  the new arguments.  These replace any existing arguments.
     */
    public void setArguments(Arguments args)
    {
        setProperty(new TestElementProperty(ARGUMENTS, args));
    }

    /**
     * Get the arguments (parameters) for the JavaSamplerClient to be executed
     * with.
     *
     * @return the arguments
     */
    public Arguments getArguments()
    {
        return (Arguments) getProperty(ARGUMENTS).getObjectValue();
    }

    /**
     * Releases Java Client.
     */
    private void releaseJavaClient ()
    {
        if (javaClient != null)
        {
            javaClient.teardownTest(context);
        }
        javaClient = null;
        context = null;
    }

    /**
     * Sets the Classname attribute of the JavaConfig object
     *
     * @param  classname  the new Classname value
     */
    public void setClassname(String classname)
    {
        setProperty(CLASSNAME, classname);
    }

    /**
     * Gets the Classname attribute of the JavaConfig object
     *
     * @return  the Classname value
     */
    public String getClassname()
    {
        return getPropertyAsString(CLASSNAME);
    }

    /**
     * Performs a test sample.
     * 
     * The <code>sample()</code> method retrieves the reference to the
     * Java client and calls its <code>runTest()</code> method.
     *
     * @see JavaSamplerClient#runTest(JavaSamplerContext)
     * 
     * @param entry  the Entry for this sample
     * @return       test SampleResult
     */
    public SampleResult sample(Entry entry)
    {
        context = new JavaSamplerContext(getArguments());
        if (javaClient == null)
        {
            log.debug(whoAmI() + "Creating Java Client");
            createJavaClient();
            javaClient.setupTest(context);
        }

        return createJavaClient().runTest(context);
    }

    /**
     * Returns reference to <code>JavaSamplerClient</code>.
     * 
     * The <code>createJavaClient()</code> method uses reflection
     * to create an instance of the specified Java protocol client.
     * If the class can not be found, the method returns a reference
     * to <code>this</code> object.
     * 
     * @return JavaSamplerClient reference.
     */
    private JavaSamplerClient createJavaClient()
    {
        if (javaClient == null)
        {
            try
            {
                Class javaClass = Class.forName(getClassname().trim(),
                        false,
                        Thread.currentThread().getContextClassLoader());
                javaClient = (JavaSamplerClient) javaClass.newInstance();
                context = new JavaSamplerContext(getArguments());

                if (log.isDebugEnabled())
                {
                    log.debug(
                        whoAmI()
                            + "\tCreated:\t"
                            + getClassname()
                            + "@"
                            + Integer.toHexString(javaClient.hashCode()));
                }
            }
            catch (Exception e)
            {
                log.error(
                    whoAmI() + "\tException creating: " + getClassname(),
                    e);
                javaClient = new ErrorSamplerClient();
            }
        }
        return javaClient;
    }

    /**
     * Retrieves reference to JavaSamplerClient.
     * 
     * Convience method used to check for null reference without
     * actually creating a JavaSamplerClient
     * 
     * @return reference to JavaSamplerClient
     * NOTUSED
    private JavaSamplerClient retrieveJavaClient()
    {
        return javaClient;
    }
    */

    /**
     * Generate a String identifier of this instance for debugging
     * purposes.
     * 
     * @return  a String identifier for this sampler instance
     */
    private String whoAmI()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(Thread.currentThread().getName());
        sb.append("@");
        sb.append(Integer.toHexString(hashCode()));
        return sb.toString();
    }

    //  TestListener implementation
    /* Implements TestListener.testStarted() */
    public void testStarted()
    {
        log.debug(whoAmI() + "\ttestStarted");
    }

    /* Implements TestListener.testStarted(String) */
    public void testStarted(String host)
    {
        log.debug(whoAmI() + "\ttestStarted(" + host + ")");
    }

    /**
     * Method called at the end of the test.  This is called only on one
     * instance of JavaSampler.  This method will loop through all of the
     * other JavaSamplers which have been registered (automatically in the
     * constructor) and notify them that the test has ended, allowing the
     * JavaSamplerClients to cleanup.
     */
    public void testEnded()
    {
        log.debug(whoAmI() + "\ttestEnded");
        synchronized (allSamplers)
        {
            Iterator i = allSamplers.iterator();
            while (i.hasNext())
            {
                JavaSampler sampler = (JavaSampler) i.next();
                sampler.releaseJavaClient();
                i.remove();
            }
        }
    }

    /* Implements TestListener.testEnded(String) */
    public void testEnded(String host)
    {
        testEnded();
    }

    /* Implements TestListener.testIterationStart(LoopIterationEvent) */
    public void testIterationStart(LoopIterationEvent event)
    {
    }

    /**
     * A {@link JavaSamplerClient} implementation used for error handling.  If
     * an error occurs while creating the real JavaSamplerClient object, it is
     * replaced with an instance of this class.  Each time a sample occurs with
     * this class, the result is marked as a failure so the user can see that
     * the test failed.
     */
    class ErrorSamplerClient extends AbstractJavaSamplerClient
    {
        /**
         * Return SampleResult with data on error.
         * @see JavaSamplerClient#runTest()
         */
        public SampleResult runTest(JavaSamplerContext context)
        {
            log.debug(whoAmI() + "\trunTest");
            Thread.yield();
            SampleResult results = new SampleResult();
            results.setSuccessful(false);
            results.setResponseData(
                ("Class not found: " + getClassname()).getBytes());
            results.setSampleLabel("ERROR: " + getClassname());
            return results;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4425.java