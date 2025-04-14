error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10594.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10594.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10594.java
text:
```scala
i@@mplements Serializable

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.jmeter.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Properties;

import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.commons.io.FileUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

public abstract class BSFTestElement extends ScriptingTestElement
    implements Serializable, Cloneable
{
    private static final long serialVersionUID = 233L;

    private static final Logger log = LoggingManager.getLoggerForClass();

    static {
        BSFManager.registerScriptingEngine("jexl", //$NON-NLS-1$
                "org.apache.commons.jexl.bsf.JexlEngine", //$NON-NLS-1$
                new String[]{"jexl"}); //$NON-NLS-1$
        log.info("Registering JMeter version of JavaScript engine as work-round for BSF-22");
        BSFManager.registerScriptingEngine("javascript", //$NON-NLS-1$
                "org.apache.jmeter.util.BSFJavaScriptEngine", //$NON-NLS-1$
                new String[]{"js"}); //$NON-NLS-1$
    }

    public BSFTestElement() {
        super();
    }

    protected BSFManager getManager() throws BSFException {
        BSFManager mgr = new BSFManager();
        initManager(mgr);
        return mgr;
    }

    protected void initManager(BSFManager mgr) throws BSFException{
        final String label = getName();
        final String fileName = getFilename();
        final String scriptParameters = getParameters();
        // Use actual class name for log
        final Logger logger = LoggingManager.getLoggerForShortName(getClass().getName());
        mgr.declareBean("log", logger, Logger.class); // $NON-NLS-1$
        mgr.declareBean("Label",label, String.class); // $NON-NLS-1$
        mgr.declareBean("FileName",fileName, String.class); // $NON-NLS-1$
        mgr.declareBean("Parameters", scriptParameters, String.class); // $NON-NLS-1$
        String [] args=JOrphanUtils.split(scriptParameters, " ");//$NON-NLS-1$
        mgr.declareBean("args",args,args.getClass());//$NON-NLS-1$
        // Add variables for access to context and variables
        JMeterContext jmctx = JMeterContextService.getContext();
        JMeterVariables vars = jmctx.getVariables();
        Properties props = JMeterUtils.getJMeterProperties();

        mgr.declareBean("ctx", jmctx, jmctx.getClass()); // $NON-NLS-1$
        mgr.declareBean("vars", vars, vars.getClass()); // $NON-NLS-1$
        mgr.declareBean("props", props, props.getClass()); // $NON-NLS-1$
        // For use in debugging:
        mgr.declareBean("OUT", System.out, PrintStream.class); // $NON-NLS-1$

        // Most subclasses will need these:
        Sampler sampler = jmctx.getCurrentSampler();
        mgr.declareBean("sampler", sampler, Sampler.class);
        SampleResult prev = jmctx.getPreviousResult();
        mgr.declareBean("prev", prev, SampleResult.class);
    }

    protected void processFileOrScript(BSFManager mgr) throws BSFException{
        BSFEngine bsfEngine = mgr.loadScriptingEngine(getScriptLanguage());
        final String scriptFile = getFilename();
        if (scriptFile.length() == 0) {
            bsfEngine.exec("[script]",0,0,getScript());
        } else {// we have a file, read and process it
            try {
                String script=FileUtils.readFileToString(new File(scriptFile));
                bsfEngine.exec(scriptFile,0,0,script);
            } catch (IOException e) {
                log.warn(e.getLocalizedMessage());
                throw new BSFException(BSFException.REASON_IO_ERROR,"Problem reading script file",e);
            }
        }
    }

    protected Object evalFileOrScript(BSFManager mgr) throws BSFException{
        BSFEngine bsfEngine = mgr.loadScriptingEngine(getScriptLanguage());
        final String scriptFile = getFilename();
        if (scriptFile.length() == 0) {
            return bsfEngine.eval("[script]",0,0,getScript());
        } else {// we have a file, read and process it
            try {
                String script=FileUtils.readFileToString(new File(scriptFile));
                return bsfEngine.eval(scriptFile,0,0,script);
            } catch (IOException e) {
                log.warn(e.getLocalizedMessage());
                throw new BSFException(BSFException.REASON_IO_ERROR,"Problem reading script file",e);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10594.java