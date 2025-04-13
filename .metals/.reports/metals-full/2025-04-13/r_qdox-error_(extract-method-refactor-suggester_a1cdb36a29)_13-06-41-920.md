error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8445.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8445.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8445.java
text:
```scala
protected O@@bject readResolve() {

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

import java.io.Serializable;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.util.BeanShellInterpreter;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterException;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

public abstract class BeanShellTestElement extends AbstractTestElement
    implements Serializable, Cloneable, ThreadListener, TestListener
{
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final long serialVersionUID = 4;

    //++ For TestBean implementations only
    private String parameters; // passed to file or script

    private String filename; // file to source (overrides script)

    private String script; // script (if file not provided)

    private boolean resetInterpreter = false;
    //-- For TestBean implementations only


    private transient BeanShellInterpreter bshInterpreter = null;

    private transient boolean hasInitFile = false;

    public BeanShellTestElement() {
        super();
        init();
    }

    protected abstract String getInitFileProperty();

    protected BeanShellInterpreter getBeanShellInterpreter() {
        if (isResetInterpreter()) {
            try {
                bshInterpreter.reset();
            } catch (ClassNotFoundException e) {
                log.error("Cannot reset BeanShell: "+e.toString());
            }
        }

        try {
            bshInterpreter.set("props", JMeterUtils.getJMeterProperties());
        } catch (JMeterException e) {
            log.error("Cannot set 'props' object: "+e.toString());
        }
        return bshInterpreter;
    }

    private void init() {
        parameters=""; // ensure variables are not null
        filename="";
        script="";
        try {
            String initFileName = JMeterUtils.getProperty(getInitFileProperty());
            hasInitFile = initFileName != null;
            bshInterpreter = new BeanShellInterpreter(initFileName, log);
        } catch (ClassNotFoundException e) {
            log.error("Cannot find BeanShell: "+e.toString());
        }
    }

    private Object readResolve() {
        init();
        return this;
    }

    public Object clone() {
        BeanShellTestElement o = (BeanShellTestElement) super.clone();
        o.init();
       return o;
    }

    protected Object processFileOrScript(BeanShellInterpreter bsh) throws JMeterException{
        String fileName = getFilename();

        bsh.set("FileName", getFilename());//$NON-NLS-1$
        // Set params as a single line
        bsh.set("Parameters", getParameters()); // $NON-NLS-1$
        // and set as an array
        bsh.set("bsh.args",//$NON-NLS-1$
                JOrphanUtils.split(getParameters(), " "));//$NON-NLS-1$

        if (fileName.length() == 0) {
            return bsh.eval(getScript());
        }
        return bsh.source(fileName);
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

    public void threadStarted() {
        if (bshInterpreter == null || !hasInitFile) {
            return;
        }
        try {
            bshInterpreter.evalNoLog("threadStarted()"); // $NON-NLS-1$
        } catch (JMeterException ignored) {
            log.debug(getClass().getName() + " : " + ignored.getLocalizedMessage()); // $NON-NLS-1$
        }
    }

    public void threadFinished() {
        if (bshInterpreter == null || !hasInitFile) {
            return;
        }
        try {
            bshInterpreter.evalNoLog("threadFinished()"); // $NON-NLS-1$
        } catch (JMeterException ignored) {
            log.debug(getClass().getName() + " : " + ignored.getLocalizedMessage()); // $NON-NLS-1$
        }
    }

    public void testEnded() {
        if (bshInterpreter == null || !hasInitFile) {
            return;
        }
        try {
            bshInterpreter.evalNoLog("testEnded()"); // $NON-NLS-1$
        } catch (JMeterException ignored) {
            log.debug(getClass().getName() + " : " + ignored.getLocalizedMessage()); // $NON-NLS-1$
        }
    }

    public void testEnded(String host) {
        if (bshInterpreter == null || !hasInitFile) {
            return;
        }
        try {
            bshInterpreter.eval((new StringBuffer("testEnded(")) // $NON-NLS-1$
                    .append(host)
                    .append(")") // $NON-NLS-1$
                    .toString()); // $NON-NLS-1$
        } catch (JMeterException ignored) {
            log.debug(getClass().getName() + " : " + ignored.getLocalizedMessage()); // $NON-NLS-1$
        }
    }

    public void testIterationStart(LoopIterationEvent event) {
        // Not implemented
    }

    public void testStarted() {
        if (bshInterpreter == null || !hasInitFile) {
            return;
        }
        try {
            bshInterpreter.evalNoLog("testStarted()"); // $NON-NLS-1$
        } catch (JMeterException ignored) {
            log.debug(getClass().getName() + " : " + ignored.getLocalizedMessage()); // $NON-NLS-1$
        }
    }

    public void testStarted(String host) {
        if (bshInterpreter == null || !hasInitFile) {
            return;
        }
        try {
            bshInterpreter.eval((new StringBuffer("testStarted(")) // $NON-NLS-1$
                    .append(host)
                    .append(")") // $NON-NLS-1$
                    .toString()); // $NON-NLS-1$
        } catch (JMeterException ignored) {
            log.debug(getClass().getName() + " : " + ignored.getLocalizedMessage()); // $NON-NLS-1$
        }
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

    public boolean isResetInterpreter() {
        return resetInterpreter;
    }

    public void setResetInterpreter(boolean b) {
        resetInterpreter = b;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8445.java