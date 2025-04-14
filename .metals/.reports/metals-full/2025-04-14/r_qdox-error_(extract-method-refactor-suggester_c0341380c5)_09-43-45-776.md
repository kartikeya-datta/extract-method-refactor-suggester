error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2561.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2561.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2561.java
text:
```scala
J@@MeterContextService.getContext().getThread().setOnErrorStopThread(true); // No point continuing

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

package org.apache.jmeter.protocol.java.sampler;

import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.BeanShellInterpreter;
import org.apache.jmeter.util.BeanShellTestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

/**
 * A sampler which understands BeanShell
 *
 */
public class BeanShellSampler extends BeanShellTestElement implements Sampler
{
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = 3;

    public static final String FILENAME = "BeanShellSampler.filename"; //$NON-NLS-1$

    public static final String SCRIPT = "BeanShellSampler.query"; //$NON-NLS-1$

    public static final String PARAMETERS = "BeanShellSampler.parameters"; //$NON-NLS-1$

    public static final String INIT_FILE = "beanshell.sampler.init"; //$NON-NLS-1$

    public static final String RESET_INTERPRETER = "BeanShellSampler.resetInterpreter"; //$NON-NLS-1$

    protected String getInitFileProperty() {
        return INIT_FILE;
    }

    /**
     * Returns a formatted string label describing this sampler
     *
     * @return a formatted string label describing this sampler
     */

    public String getLabel() {
        return getName();
    }

    public String getScript() {
        return this.getPropertyAsString(SCRIPT);
    }

    public String getFilename() {
        return getPropertyAsString(FILENAME);
    }

    public String getParameters() {
        return getPropertyAsString(PARAMETERS);
    }

    public boolean isResetInterpreter() {
        return getPropertyAsBoolean(RESET_INTERPRETER);
    }

    public SampleResult sample(Entry e)// Entry tends to be ignored ...
    {
        // log.info(getLabel()+" "+getFilename());
        SampleResult res = new SampleResult();
        boolean isSuccessful = false;
        res.setSampleLabel(getLabel());
        res.sampleStart();
        final BeanShellInterpreter bshInterpreter = getBeanShellInterpreter();
        if (bshInterpreter == null) {
            res.sampleEnd();
            res.setResponseCode("503");//$NON-NLS-1$
            res.setResponseMessage("BeanShell Interpreter not found");
            res.setSuccessful(false);
            return res;
        }
        try {
            String request = getScript();
            String fileName = getFilename();
            if (fileName.length() == 0) {
                res.setSamplerData(request);
            } else {
                res.setSamplerData(fileName);
            }

            bshInterpreter.set("Label", getLabel()); //$NON-NLS-1$
            bshInterpreter.set("FileName", getFilename()); //$NON-NLS-1$
            bshInterpreter.set("SampleResult", res); //$NON-NLS-1$

            // Save parameters as single line and as string array
            bshInterpreter.set("Parameters", getParameters());//$NON-NLS-1$
            bshInterpreter.set("bsh.args", //$NON-NLS-1$
                    JOrphanUtils.split(getParameters(), " "));//$NON-NLS-1$

            // Set default values
            bshInterpreter.set("ResponseCode", "200"); //$NON-NLS-1$
            bshInterpreter.set("ResponseMessage", "OK");//$NON-NLS-1$
            bshInterpreter.set("IsSuccess", true);//$NON-NLS-1$

            // Add variables for access to context and variables
            JMeterContext jmctx = JMeterContextService.getContext();
            JMeterVariables vars = jmctx.getVariables();
            bshInterpreter.set("ctx", jmctx);//$NON-NLS-1$
            bshInterpreter.set("vars", vars);//$NON-NLS-1$

            res.setDataType(SampleResult.TEXT); // assume text output - script can override if necessary

            Object bshOut;

            if (fileName.length() == 0) {
                bshOut = bshInterpreter.eval(request);
            } else {
                bshOut = bshInterpreter.source(fileName);
            }

            if (bshOut != null) {// Set response data
                String out = bshOut.toString();
                res.setResponseData(out.getBytes());
            }
            // script can also use setResponseData() so long as it returns null

            res.setResponseCode(bshInterpreter.get("ResponseCode").toString());//$NON-NLS-1$
            res.setResponseMessage(bshInterpreter.get("ResponseMessage").toString());//$NON-NLS-1$
            isSuccessful = Boolean.valueOf(bshInterpreter.get("IsSuccess") //$NON-NLS-1$
                    .toString()).booleanValue();
        }
        /*
         * To avoid class loading problems when bsh,jar is missing, we don't try
         * to catch this error separately catch (bsh.EvalError ex) {
         * log.debug("",ex); res.setResponseCode("500");//$NON-NLS-1$
         * res.setResponseMessage(ex.toString()); }
         */
        // but we do trap this error to make tests work better
        catch (NoClassDefFoundError ex) {
            log.error("BeanShell Jar missing? " + ex.toString());
            res.setResponseCode("501");//$NON-NLS-1$
            res.setResponseMessage(ex.toString());
            res.setStopThread(true); // No point continuing
        } catch (Exception ex) // Mainly for bsh.EvalError
        {
            log.warn(ex.toString());
            res.setResponseCode("500");//$NON-NLS-1$
            res.setResponseMessage(ex.toString());
        }

        res.sampleEnd();

        // Set if we were successful or not
        res.setSuccessful(isSuccessful);

        return res;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2561.java