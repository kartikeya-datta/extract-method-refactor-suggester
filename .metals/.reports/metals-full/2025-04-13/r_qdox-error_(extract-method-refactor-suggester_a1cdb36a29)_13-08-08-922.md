error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13054.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13054.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13054.java
text:
```scala
public O@@bject evaluateScript(String execName)

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
package org.apache.tools.ant.util.optional;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.bsf.BSFEngine;


import java.util.Iterator;
import java.util.Hashtable;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import org.apache.tools.ant.util.ReflectUtil;
import org.apache.tools.ant.util.ScriptRunnerBase;

/**
 * This class is used to run BSF scripts
 *
 */
public class ScriptRunner extends ScriptRunnerBase {
    // Register Groovy ourselves, since BSF did not
    // natively support it in versions previous to 1.2.4.
    static {
        BSFManager.registerScriptingEngine(
            "groovy",
            "org.codehaus.groovy.bsf.GroovyEngine",
            new String[] {"groovy", "gy"});
    }

    private BSFEngine  engine;
    private BSFManager manager;

    /**
     * Get the name of the manager prefix.
     * @return "bsf"
     */
    public String getManagerName() {
        return "bsf";
    }

    /**
     * Check if bsf supports the language.
     * @return true if bsf can create an engine for this language.
     */
    public boolean supportsLanguage() {
        Hashtable table = (Hashtable) ReflectUtil.getField(
            new BSFManager(), "registeredEngines");
        String engineClassName = (String) table.get(getLanguage());
        if (engineClassName == null) {
            getProject().log(
                "This is no BSF engine class for language '"
                + getLanguage() + "'",
                Project.MSG_VERBOSE);
            return false;
        }
        try {
            getScriptClassLoader().loadClass(engineClassName);
            return true;
        } catch (Throwable ex) {
            getProject().log(
                "unable to create BSF engine class for language '"
                + getLanguage() + "'",
                ex,
                Project.MSG_VERBOSE);
            return false;
        }
    }

    /**
     * Do the work.
     *
     * @param execName the name that will be passed to BSF for this script
     *        execution.
     *
     * @exception BuildException if someting goes wrong exectuing the script.
     */
    public void executeScript(String execName) throws BuildException {
        checkLanguage();
        ClassLoader origLoader = replaceContextLoader();
        try {
            BSFManager m = createManager();
            declareBeans(m);
            // execute the script
            if (engine == null) {
                m.exec(getLanguage(), execName, 0, 0, getScript());
            } else {
                engine.exec(execName, 0, 0, getScript());
            }
        } catch (BSFException be) {
            throwBuildException(be);
        } finally {
            restoreContextLoader(origLoader);
        }
    }

    /**
     * Do the work.
     *
     * @param execName the name that will be passed to BSF for this script
     *        execution.
     * @return the result of the evalulation
     * @exception BuildException if someting goes wrong exectuing the script.
     */
    public Object evalulateScript(String execName)
        throws BuildException {
        checkLanguage();
        ClassLoader origLoader = replaceContextLoader();
        try {
            BSFManager m = createManager();
            declareBeans(m);
            // execute the script
            if (engine == null) {
                return m.eval(getLanguage(), execName, 0, 0, getScript());
            } else {
                return engine.eval(execName, 0, 0, getScript());
            }
        } catch (BSFException be) {
            throwBuildException(be);
            // NotReached
            return null;
        } finally {
            restoreContextLoader(origLoader);
        }
    }

    /**
     * Throw a buildException in place of a BSFException.
     * @param be BSFException to convert.
     * @throws BuildException the conveted exception.
     */
    private void throwBuildException(BSFException be) {
        Throwable t = be;
        Throwable te = be.getTargetException();
        if (te != null) {
            if  (te instanceof BuildException) {
                throw (BuildException) te;
            } else {
                t = te;
            }
        }
        throw new BuildException(t);
    }

    private void declareBeans(BSFManager m) throws BSFException {
        for (Iterator i = getBeans().keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            Object value = getBeans().get(key);
            if (value != null) {
                m.declareBean(key, value, value.getClass());
            } else {
                // BSF uses a hashtable to store values
                // so cannot declareBean with a null value
                // So need to remove any bean of this name as
                // that bean should not be visible
                m.undeclareBean(key);
            }
        }
    }

    private BSFManager createManager() throws BSFException {
        if (manager != null) {
            return manager;
        }
        BSFManager m = new BSFManager();
        m.setClassLoader(getScriptClassLoader());
        if (getKeepEngine()) {
            BSFEngine e = manager.loadScriptingEngine(getLanguage());
            this.manager = m;
            this.engine  = e;
        }
        return m;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13054.java