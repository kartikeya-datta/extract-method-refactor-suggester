error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1314.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1314.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1314.java
text:
```scala
r@@es.setResponseData(bsfOut.toString(), null);

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

import java.io.FileInputStream;

import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.commons.io.IOUtils;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.util.BSFTestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * A sampler which understands BSF
 *
 */
public class BSFSampler extends BSFTestElement implements Sampler {

    private static final long serialVersionUID = 240L;

    private static final Logger log = LoggingManager.getLoggerForClass();

    //+ JMX file attributes - do not change
    private static final String FILENAME = "BSFSampler.filename"; //$NON-NLS-1$

    private static final String SCRIPT = "BSFSampler.query"; //$NON-NLS-1$

    private static final String LANGUAGE = "BSFSampler.language"; //$NON-NLS-1$

    private static final String PARAMETERS = "BSFSampler.parameters"; //$NON-NLS-1$
    //- JMX file attributes

    public BSFSampler() {
        super();
    }

    @Override
    public String getFilename() {
        return getPropertyAsString(FILENAME);
    }

    @Override
    public void setFilename(String newFilename) {
        this.setProperty(FILENAME, newFilename);
    }

    @Override
    public String getScript() {
        return this.getPropertyAsString(SCRIPT);
    }

    @Override
    public void setScript(String newScript) {
        this.setProperty(SCRIPT, newScript);
    }

    @Override
    public String getParameters() {
        return this.getPropertyAsString(PARAMETERS);
    }

    @Override
    public void setParameters(String newScript) {
        this.setProperty(PARAMETERS, newScript);
    }

    @Override
    public String getScriptLanguage() {
        return this.getPropertyAsString(LANGUAGE);
    }

    @Override
    public void setScriptLanguage(String lang) {
        this.setProperty(LANGUAGE, lang);
    }

    /**
     * Returns a formatted string label describing this sampler
     *
     * @return a formatted string label describing this sampler
     */

    public String getLabel() {
        return getName();
    }

    public SampleResult sample(Entry e)// Entry tends to be ignored ...
    {
        final String label = getLabel();
        final String request = getScript();
        final String fileName = getFilename();
        log.debug(label + " " + fileName);
        SampleResult res = new SampleResult();
        res.setSampleLabel(label);
        FileInputStream is = null;
        BSFEngine bsfEngine = null;
        // There's little point saving the manager between invocations
        // as we need to reset most of the beans anyway
        BSFManager mgr = new BSFManager();

        // TODO: find out how to retrieve these from the script
        // At present the script has to use SampleResult methods to set them.
        res.setResponseCode("200"); // $NON-NLS-1$
        res.setResponseMessage("OK"); // $NON-NLS-1$
        res.setSuccessful(true);
        res.setDataType(SampleResult.TEXT); // Default (can be overridden by the script)

        res.sampleStart();
        try {
            initManager(mgr);
            mgr.declareBean("SampleResult", res, res.getClass()); // $NON-NLS-1$

            // These are not useful yet, as have not found how to get updated values back
            //mgr.declareBean("ResponseCode", "200", String.class); // $NON-NLS-1$
            //mgr.declareBean("ResponseMessage", "OK", String.class); // $NON-NLS-1$
            //mgr.declareBean("IsSuccess", Boolean.TRUE, Boolean.class); // $NON-NLS-1$

            // N.B. some engines (e.g. Javascript) cannot handle certain declareBean() calls
            // after the engine has been initialised, so create the engine last
            bsfEngine = mgr.loadScriptingEngine(getScriptLanguage());

            Object bsfOut = null;
            if (fileName.length()>0) {
                res.setSamplerData("File: "+fileName);
                is = new FileInputStream(fileName);
                bsfOut = bsfEngine.eval(fileName, 0, 0, IOUtils.toString(is));
            } else {
                res.setSamplerData(request);
                bsfOut = bsfEngine.eval("script", 0, 0, request);
            }

            if (bsfOut != null) {
                res.setResponseData(bsfOut.toString().getBytes());
            }
        } catch (BSFException ex) {
            log.warn("BSF error", ex);
            res.setSuccessful(false);
            res.setResponseCode("500"); // $NON-NLS-1$
            res.setResponseMessage(ex.toString());
        } catch (Exception ex) {// Catch evaluation errors
            log.warn("Problem evaluating the script", ex);
            res.setSuccessful(false);
            res.setResponseCode("500"); // $NON-NLS-1$
            res.setResponseMessage(ex.toString());
        } finally {
            res.sampleEnd();
            IOUtils.closeQuietly(is);
// Will be done by mgr.terminate() anyway
//          if (bsfEngine != null) {
//              bsfEngine.terminate();
//          }
            mgr.terminate();
        }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1314.java