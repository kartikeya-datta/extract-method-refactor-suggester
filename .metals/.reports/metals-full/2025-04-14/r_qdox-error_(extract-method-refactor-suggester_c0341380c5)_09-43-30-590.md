error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1317.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1317.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1317.java
text:
```scala
r@@esults.setResponseData(resultData, null);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

package org.apache.jmeter.protocol.java.test;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;

/**
 * The <code>JavaTest</code> class is a simple sampler which is intended for
 * use when developing test plans. The sampler generates results internally, so
 * does not need access to any external resources such as web, ftp or LDAP
 * servers. In addition, because the exact values of most of the SampleResult
 * can be directly set, it is possible to easily test most Assertions that use
 * the sample results.
 *
 * <p>
 * During each sample, this client will sleep for some amount of time. The
 * amount of time to sleep is determined from the two parameters Sleep_Time and
 * Sleep_Mask using the formula:
 *
 * <pre>
 * totalSleepTime = Sleep_Time + (System.currentTimeMillis() % Sleep_Mask)
 * </pre>
 *
 * Thus, the Sleep_Mask provides a way to add a random component to the sleep
 * time.
 * <p>
 * The sampler is able to define the precise values of:
 *
 * <pre>
 *
 *  - responseCode
 *  - responseMessage
 *  - Label
 *  - success/fail status
 *
 * </pre>
 *
 * The elapsed time and end-time cannot be directly controlled.
 * <p>
 * Note: this class was derived from {@link SleepTest}.
 *
 */

public class JavaTest extends AbstractJavaSamplerClient implements Serializable {
    private static final long serialVersionUID = 240L;

    /** The base number of milliseconds to sleep during each sample. */
    private long sleepTime;

    /** The default value of the SleepTime parameter, in milliseconds. */
    public static final long DEFAULT_SLEEP_TIME = 100;

    /** The name used to store the SleepTime parameter. */
    private static final String SLEEP_NAME = "Sleep_Time";

    /**
     * A mask to be applied to the current time in order to add a semi-random
     * component to the sleep time.
     */
    private long sleepMask;

    /** The default value of the SleepMask parameter. */
    public static final long DEFAULT_SLEEP_MASK = 0xff;

    /** Formatted string representation of the default SleepMask. */
    private static final String DEFAULT_MASK_STRING = "0x" + (Long.toHexString(DEFAULT_SLEEP_MASK)).toUpperCase(java.util.Locale.ENGLISH);

    /** The name used to store the SleepMask parameter. */
    private static final String MASK_NAME = "Sleep_Mask";

    /** The label to store in the sample result. */
    private String label;

    /** The default value of the Label parameter. */
    // private static final String LABEL_DEFAULT = "JavaTest";
    /** The name used to store the Label parameter. */
    private static final String LABEL_NAME = "Label";

    /** The response message to store in the sample result. */
    private String responseMessage;

    /** The default value of the ResponseMessage parameter. */
    private static final String RESPONSE_MESSAGE_DEFAULT = "";

    /** The name used to store the ResponseMessage parameter. */
    private static final String RESPONSE_MESSAGE_NAME = "ResponseMessage";

    /** The response code to be stored in the sample result. */
    private String responseCode;

    /** The default value of the ResponseCode parameter. */
    private static final String RESPONSE_CODE_DEFAULT = "";

    /** The name used to store the ResponseCode parameter. */
    private static final String RESPONSE_CODE_NAME = "ResponseCode";

    /** The sampler data (shown as Request Data in the Tree display). */
    private String samplerData;

    /** The default value of the SamplerData parameter. */
    private static final String SAMPLER_DATA_DEFAULT = "";

    /** The name used to store the SamplerData parameter. */
    private static final String SAMPLER_DATA_NAME = "SamplerData";

    /** Holds the result data (shown as Response Data in the Tree display). */
    private String resultData;

    /** The default value of the ResultData parameter. */
    private static final String RESULT_DATA_DEFAULT = "";

    /** The name used to store the ResultData parameter. */
    private static final String RESULT_DATA_NAME = "ResultData";

    /** The success status to be stored in the sample result. */
    private boolean success;

    /** The default value of the Success Status parameter. */
    private static final String SUCCESS_DEFAULT = "OK";

    /** The name used to store the Success Status parameter. */
    private static final String SUCCESS_NAME = "Status";

    /**
     * Default constructor for <code>JavaTest</code>.
     *
     * The Java Sampler uses the default constructor to instantiate an instance
     * of the client class.
     */
    public JavaTest() {
        getLogger().debug(whoAmI() + "\tConstruct");
    }

    /*
     * Utility method to set up all the values
     */
    private void setupValues(JavaSamplerContext context) {

        sleepTime = context.getLongParameter(SLEEP_NAME, DEFAULT_SLEEP_TIME);
        sleepMask = context.getLongParameter(MASK_NAME, DEFAULT_SLEEP_MASK);

        responseMessage = context.getParameter(RESPONSE_MESSAGE_NAME, RESPONSE_MESSAGE_DEFAULT);

        responseCode = context.getParameter(RESPONSE_CODE_NAME, RESPONSE_CODE_DEFAULT);

        success = context.getParameter(SUCCESS_NAME, SUCCESS_DEFAULT).equalsIgnoreCase("OK");

        label = context.getParameter(LABEL_NAME, "");
        if (label.length() == 0) {
            label = context.getParameter(TestElement.NAME); // default to name of element
        }

        samplerData = context.getParameter(SAMPLER_DATA_NAME, SAMPLER_DATA_DEFAULT);

        resultData = context.getParameter(RESULT_DATA_NAME, RESULT_DATA_DEFAULT);
    }

    /**
     * Do any initialization required by this client.
     *
     * There is none, as it is done in runTest() in order to be able to vary the
     * data for each sample.
     *
     * @param context
     *            the context to run with. This provides access to
     *            initialization parameters.
     */
    @Override
    public void setupTest(JavaSamplerContext context) {
        getLogger().debug(whoAmI() + "\tsetupTest()");
        listParameters(context);
    }

    /**
     * Provide a list of parameters which this test supports. Any parameter
     * names and associated values returned by this method will appear in the
     * GUI by default so the user doesn't have to remember the exact names. The
     * user can add other parameters which are not listed here. If this method
     * returns null then no parameters will be listed. If the value for some
     * parameter is null then that parameter will be listed in the GUI with an
     * empty value.
     *
     * @return a specification of the parameters used by this test which should
     *         be listed in the GUI, or null if no parameters should be listed.
     */
    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument(SLEEP_NAME, String.valueOf(DEFAULT_SLEEP_TIME));
        params.addArgument(MASK_NAME, DEFAULT_MASK_STRING);
        params.addArgument(LABEL_NAME, "");
        params.addArgument(RESPONSE_CODE_NAME, RESPONSE_CODE_DEFAULT);
        params.addArgument(RESPONSE_MESSAGE_NAME, RESPONSE_MESSAGE_DEFAULT);
        params.addArgument(SUCCESS_NAME, SUCCESS_DEFAULT);
        params.addArgument(SAMPLER_DATA_NAME, SAMPLER_DATA_DEFAULT);
        params.addArgument(RESULT_DATA_NAME, SAMPLER_DATA_DEFAULT);
        return params;
    }

    /**
     * Perform a single sample.<br>
     * In this case, this method will simply sleep for some amount of time.
     *
     * This method returns a <code>SampleResult</code> object.
     *
     * <pre>
     *
     *  The following fields are always set:
     *  - responseCode (default &quot;&quot;)
     *  - responseMessage (default &quot;&quot;)
     *  - label (set from LABEL_NAME parameter if it exists, else element name)
     *  - success (default true)
     *
     * </pre>
     *
     * The following fields are set from the user-defined parameters, if
     * supplied:
     *
     * <pre>
     * -samplerData - responseData
     * </pre>
     *
     * @see org.apache.jmeter.samplers.SampleResult#sampleStart()
     * @see org.apache.jmeter.samplers.SampleResult#sampleEnd()
     * @see org.apache.jmeter.samplers.SampleResult#setSuccessful(boolean)
     * @see org.apache.jmeter.samplers.SampleResult#setSampleLabel(String)
     * @see org.apache.jmeter.samplers.SampleResult#setResponseCode(String)
     * @see org.apache.jmeter.samplers.SampleResult#setResponseMessage(String)
     * @see org.apache.jmeter.samplers.SampleResult#setResponseData(byte [])
     * @see org.apache.jmeter.samplers.SampleResult#setDataType(String)
     *
     * @param context
     *            the context to run with. This provides access to
     *            initialization parameters.
     *
     * @return a SampleResult giving the results of this sample.
     */
    public SampleResult runTest(JavaSamplerContext context) {
        setupValues(context);

        SampleResult results = new SampleResult();

        results.setResponseCode(responseCode);
        results.setResponseMessage(responseMessage);
        results.setSampleLabel(label);

        if (samplerData != null && samplerData.length() > 0) {
            results.setSamplerData(samplerData);
        }

        if (resultData != null && resultData.length() > 0) {
            results.setResponseData(resultData.getBytes());
            results.setDataType(SampleResult.TEXT);
        }

        // Record sample start time.
        results.sampleStart();

        long sleep = sleepTime;
        if (sleepTime > 0 && sleepMask > 0) { // / Only do the calculation if
                                                // it is needed
            long start = System.currentTimeMillis();
            // Generate a random-ish offset value using the current time.
            sleep = sleepTime + (start % sleepMask);
        }

        try {
            // Execute the sample. In this case sleep for the
            // specified time, if any
            if (sleep > 0) {
                Thread.sleep(sleep);
            }
            results.setSuccessful(success);
        } catch (InterruptedException e) {
            getLogger().warn("JavaTest: interrupted.");
            results.setSuccessful(true);
        } catch (Exception e) {
            getLogger().error("JavaTest: error during sample", e);
            results.setSuccessful(false);
        } finally {
            // Record end time and populate the results.
            results.sampleEnd();
        }

        if (getLogger().isDebugEnabled()) {
            getLogger().debug(whoAmI() + "\trunTest()" + "\tTime:\t" + results.getTime());
            listParameters(context);
        }

        return results;
    }

    /**
     * Do any clean-up required by this test. In this case no clean-up is
     * necessary, but some messages are logged for debugging purposes.
     *
     * @param context
     *            the context to run with. This provides access to
     *            initialization parameters.
     */
    @Override
    public void teardownTest(JavaSamplerContext context) {
        getLogger().debug(whoAmI() + "\tteardownTest()");
        listParameters(context);
    }

    /**
     * Dump a list of the parameters in this context to the debug log.
     *
     * @param context
     *            the context which contains the initialization parameters.
     */
    private void listParameters(JavaSamplerContext context) {
        if (getLogger().isDebugEnabled()) {
            Iterator<String> argsIt = context.getParameterNamesIterator();
            while (argsIt.hasNext()) {
                String name = argsIt.next();
                getLogger().debug(name + "=" + context.getParameter(name));
            }
        }
    }

    /**
     * Generate a String identifier of this test for debugging purposes.
     *
     * @return a String identifier for this test instance
     */
    private String whoAmI() {
        StringBuilder sb = new StringBuilder();
        sb.append(Thread.currentThread().toString());
        sb.append("@");
        sb.append(Integer.toHexString(hashCode()));
        return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1317.java