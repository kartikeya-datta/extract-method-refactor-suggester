error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10595.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10595.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10595.java
text:
```scala
i@@mplements Serializable, TestStateListener

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
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

public abstract class JSR223TestElement extends ScriptingTestElement
    implements Serializable, Cloneable, TestStateListener
{
    /**
     * Initialization On Demand Holder pattern
     */
    private static class LazyHolder {
        public static final ScriptEngineManager INSTANCE = new ScriptEngineManager();
    }
 
    /**
     * @return ScriptEngineManager singleton
     */
    public static ScriptEngineManager getInstance() {
            return LazyHolder.INSTANCE;
    }
    
    private static final long serialVersionUID = 233L;

    private String cacheKey = ""; // If not empty then script in ScriptText will be compiled and cached

    /**
     * Cache of compiled scripts
     */
    @SuppressWarnings("unchecked") // LRUMap does not support generics (yet)
    private static final Map<String, CompiledScript> compiledScriptsCache = 
            Collections.synchronizedMap(
                    new LRUMap(JMeterUtils.getPropDefault("jsr223.compiled_scripts_cache_size", 100)));

    public JSR223TestElement() {
        super();
    }

    protected ScriptEngine getScriptEngine() throws ScriptException {
        final String lang = getScriptLanguage();

        ScriptEngine scriptEngine = getInstance().getEngineByName(lang);
        if (scriptEngine == null) {
            throw new ScriptException("Cannot find engine named: "+lang);
        }

        return scriptEngine;
    }

    /**
     * Populate variables to be passed to scripts
     * @param bindings Bindings
     */
    protected void populateBindings(Bindings bindings) {
        final String label = getName();
        final String fileName = getFilename();
        final String scriptParameters = getParameters();
        // Use actual class name for log
        final Logger logger = LoggingManager.getLoggerForShortName(getClass().getName());
        bindings.put("log", logger);
        bindings.put("Label", label);
        bindings.put("FileName", fileName);
        bindings.put("Parameters", scriptParameters);
        String [] args=JOrphanUtils.split(scriptParameters, " ");//$NON-NLS-1$
        bindings.put("args", args);
        // Add variables for access to context and variables
        JMeterContext jmctx = JMeterContextService.getContext();
        bindings.put("ctx", jmctx);
        JMeterVariables vars = jmctx.getVariables();
        bindings.put("vars", vars);
        Properties props = JMeterUtils.getJMeterProperties();
        bindings.put("props", props);
        // For use in debugging:
        bindings.put("OUT", System.out);

        // Most subclasses will need these:
        Sampler sampler = jmctx.getCurrentSampler();
        bindings.put("sampler", sampler);
        SampleResult prev = jmctx.getPreviousResult();
        bindings.put("prev", prev);
    }


    /**
     * This method will run inline script or file script with special behaviour for file script:
     * - If ScriptEngine implements Compilable script will be compiled and cached
     * - If not if will be run
     * @param scriptEngine ScriptEngine
     * @param bindings {@link Bindings} might be null
     * @return Object returned by script
     * @throws IOException
     * @throws ScriptException
     */
    protected Object processFileOrScript(ScriptEngine scriptEngine, Bindings bindings) throws IOException, ScriptException {
        if (bindings == null) {
            bindings = scriptEngine.createBindings();
        }
        populateBindings(bindings);
        File scriptFile = new File(getFilename()); 
        // Hack as in bsh-2.0b5.jar BshScriptEngine implements Compilable but throws new Error
        boolean supportsCompilable = scriptEngine instanceof Compilable 
                && !(scriptEngine.getClass().getName().equals("bsh.engine.BshScriptEngine"));
        if (!StringUtils.isEmpty(getFilename())) {
            if (scriptFile.exists() && scriptFile.canRead()) {
                BufferedReader fileReader = null;
                try {
                    if (supportsCompilable) {
                        String cacheKey = 
                                getScriptLanguage()+"#"+
                                scriptFile.getAbsolutePath()+"#"+
                                        scriptFile.lastModified();
                        CompiledScript compiledScript = 
                                compiledScriptsCache.get(cacheKey);
                        if (compiledScript==null) {
                            synchronized (compiledScriptsCache) {
                                compiledScript = 
                                        compiledScriptsCache.get(cacheKey);
                                if (compiledScript==null) {
                                    // TODO Charset ?
                                    fileReader = new BufferedReader(new FileReader(scriptFile), 
                                            (int)scriptFile.length()); 
                                    compiledScript = 
                                            ((Compilable) scriptEngine).compile(fileReader);
                                    compiledScriptsCache.put(cacheKey, compiledScript);
                                }
                            }
                        }
                        return compiledScript.eval(bindings);
                    } else {
                        // TODO Charset ?
                        fileReader = new BufferedReader(new FileReader(scriptFile), 
                                (int)scriptFile.length()); 
                        return scriptEngine.eval(fileReader, bindings);                    
                    }
                } finally {
                    IOUtils.closeQuietly(fileReader);
                }
            }  else {
                throw new ScriptException("Script file '"+scriptFile.getAbsolutePath()+"' does not exist or is unreadable for element:"+getName());
            }
        } else if (!StringUtils.isEmpty(getScript())){
            if (supportsCompilable && !StringUtils.isEmpty(cacheKey)) {
                CompiledScript compiledScript = 
                        compiledScriptsCache.get(cacheKey);
                if (compiledScript==null) {
                    synchronized (compiledScriptsCache) {
                        compiledScript = 
                                compiledScriptsCache.get(cacheKey);
                        if (compiledScript==null) {
                            compiledScript = 
                                    ((Compilable) scriptEngine).compile(getScript());
                            compiledScriptsCache.put(cacheKey, compiledScript);
                        }
                    }
                }
                return compiledScript.eval(bindings);
            } else {
                return scriptEngine.eval(getScript(), bindings);
            }
        } else {
            throw new ScriptException("Both script file and script text are empty for element:"+getName());            
        }
    }


    /**
     * @return the cacheKey
     */
    public String getCacheKey() {
        return cacheKey;
    }

    /**
     * @param cacheKey the cacheKey to set
     */
    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    /**
     * @see org.apache.jmeter.testelement.TestStateListener#testStarted()
     */
    public void testStarted() {
        // NOOP
    }

    /**
     * @see org.apache.jmeter.testelement.TestStateListener#testStarted(java.lang.String)
     */
    public void testStarted(String host) {
        // NOOP   
    }

    /**
     * @see org.apache.jmeter.testelement.TestStateListener#testEnded()
     */
    public void testEnded() {
        testEnded("");
    }

    /**
     * @see org.apache.jmeter.testelement.TestStateListener#testEnded(java.lang.String)
     */
    public void testEnded(String host) {
        compiledScriptsCache.clear();
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10595.java