error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9248.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9248.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9248.java
text:
```scala
f@@ileReader = new BufferedReader(new FileReader(scriptFile)); // TODO Charset ?

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

public abstract class JSR223TestElement extends AbstractTestElement
    implements Serializable, Cloneable
{
    private static final long serialVersionUID = 233L;

    //++ For TestBean implementations only
    private String parameters; // passed to file or script

    private String filename; // file to source (overrides script)

    private String script; // script (if file not provided)

    private String scriptLanguage; // JSR223 language to use
    //-- For TestBean implementations only

    public JSR223TestElement() {
        super();
        init();
    }

    private void init() {
        parameters=""; // ensure variables are not null
        filename="";
        script="";
        scriptLanguage="";
    }

    protected Object readResolve() {
        init();
        return this;
    }

    @Override
    public Object clone() {
        JSR223TestElement o = (JSR223TestElement) super.clone();
        o.init();
       return o;
    }

    protected ScriptEngineManager getManager() {
        ScriptEngineManager sem = new ScriptEngineManager();
        initManager(sem);
        return sem;
    }

    protected void initManager(ScriptEngineManager sem) {
        final String label = getName();
        final String fileName = getFilename();
        final String scriptParameters = getParameters();
        // Use actual class name for log
        final Logger logger = LoggingManager.getLoggerForShortName(getClass().getName());

        sem.put("log", logger);
        sem.put("Label", label);
        sem.put("FileName", fileName);
        sem.put("Parameters", scriptParameters);
        String [] args=JOrphanUtils.split(scriptParameters, " ");//$NON-NLS-1$
        sem.put("args", args);
        // Add variables for access to context and variables
        JMeterContext jmctx = JMeterContextService.getContext();
        sem.put("ctx", jmctx);
        JMeterVariables vars = jmctx.getVariables();
        sem.put("vars", vars);
        Properties props = JMeterUtils.getJMeterProperties();
        sem.put("props", props);
        // For use in debugging:
        sem.put("OUT", System.out);

        // Most subclasses will need these:
        Sampler sampler = jmctx.getCurrentSampler();
        sem.put("sampler", sampler);
        SampleResult prev = jmctx.getPreviousResult();
        sem.put("prev", prev);
    }


    protected Object processFileOrScript(ScriptEngineManager sem) throws IOException, ScriptException {

        final String lang = getScriptLanguage();
        ScriptEngine scriptEngine = sem.getEngineByName(lang);
        if (scriptEngine == null) {
            throw new ScriptException("Cannot find engine named: "+lang);
        }

        File scriptFile = new File(getFilename());
        if (scriptFile.exists()) {
            BufferedReader fileReader = null;
            try {
                fileReader = new BufferedReader(new FileReader(scriptFile));
                return scriptEngine.eval(fileReader);
            } finally {
                IOUtils.closeQuietly(fileReader);
            }
        } else {
            return scriptEngine.eval(getScript());
        }

    }

    /**
     * Return the script (TestBean version).
     * Must be overridden for subclasses that don't implement TestBean
     * otherwise the clone() method won't work.
     *
     * @return the script to execute
     */
    public String getScript(){
        return script;
    }

    /**
     * Set the script (TestBean version).
     * Must be overridden for subclasses that don't implement TestBean
     * otherwise the clone() method won't work.
     *
     * @param s the script to execute (may be blank)
     */
    public void setScript(String s){
        script=s;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String s) {
        parameters = s;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String s) {
        filename = s;
    }

    public String getScriptLanguage() {
        return scriptLanguage;
    }

    public void setScriptLanguage(String s) {
        scriptLanguage = s;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9248.java