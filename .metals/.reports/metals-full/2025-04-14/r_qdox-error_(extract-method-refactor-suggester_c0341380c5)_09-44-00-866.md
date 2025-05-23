error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10242.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10242.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10242.java
text:
```scala
l@@og.error("Cannot find BeanShell: "+e.toString());

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

package org.apache.jmeter.extractor;

import java.io.Serializable;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.processor.PostProcessor;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.BeanShellInterpreter;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterException;
import org.apache.log.Logger;

public class BeanShellPostProcessor extends AbstractTestElement 
    implements PostProcessor, Serializable, TestBean, ThreadListener, TestListener
{
    private static final Logger log = LoggingManager.getLoggerForClass();
    
    private static final long serialVersionUID = 3;

    private String script;
    
    transient private BeanShellInterpreter bshInterpreter = null;

    // can be specified in jmeter.properties
    private static final String INIT_FILE = "beanshell.postprocessor.init"; //$NON-NLS-1$

    public BeanShellPostProcessor() {
        super();
        init();
    }

	private void init() {
		try {
			bshInterpreter = new BeanShellInterpreter(JMeterUtils.getProperty(INIT_FILE),log);
		} catch (ClassNotFoundException e) {
			log.error("Cannot find BeanShell: "+e.getLocalizedMessage());
		}
	}

    private Object readResolve() {
    	init();
    	return this;
    }
    
     public void process() {
        JMeterContext jmctx = JMeterContextService.getContext();

        SampleResult prev = jmctx.getPreviousResult();
		if (prev == null || bshInterpreter == null) {
			return;
		}

        JMeterVariables vars = jmctx.getVariables();
        try {
            // Add variables for access to context and variables
            bshInterpreter.set("ctx", jmctx);//$NON-NLS-1$
            bshInterpreter.set("vars", vars);//$NON-NLS-1$
            bshInterpreter.set("prev", prev);//$NON-NLS-1$
            bshInterpreter.set("data", prev.getResponseData());//$NON-NLS-1$
            bshInterpreter.eval(script);
        } catch (JMeterException e) {
            log.warn("Problem in BeanShell script "+e);
        }
	}

	public Object clone() {
        BeanShellPostProcessor o = (BeanShellPostProcessor) super.clone();
        o.script = script;
		return o;
	}
    
    public String getScript(){
        return script;
    }

    public void setScript(String s){
        script=s;
    }
	public void threadStarted() {
		if (bshInterpreter == null) return;
		try {
			bshInterpreter.evalNoLog("threadStarted()"); // $NON-NLS-1$
		} catch (JMeterException ignored) {
			log.debug(ignored.getLocalizedMessage());
		}
	}

	public void threadFinished() {
		if (bshInterpreter == null) return;
		try {
			bshInterpreter.evalNoLog("threadFinished()"); // $NON-NLS-1$
		} catch (JMeterException ignored) {
			log.debug(ignored.getLocalizedMessage());
		}		
	}

	public void testEnded() {
		if (bshInterpreter == null) return;
		try {
			bshInterpreter.evalNoLog("testEnded()"); // $NON-NLS-1$
		} catch (JMeterException ignored) {
			log.debug(ignored.getLocalizedMessage());
		}		
	}

	public void testEnded(String host) {
		if (bshInterpreter == null) return;
		try {
			bshInterpreter.eval((new StringBuffer("testEnded(")) // $NON-NLS-1$
					.append(host)
					.append(")") // $NON-NLS-1$
					.toString()); // $NON-NLS-1$
		} catch (JMeterException ignored) {
			log.debug(ignored.getLocalizedMessage());
		}		
	}

	public void testIterationStart(LoopIterationEvent event) {
		// Not implemented
	}

	public void testStarted() {
		if (bshInterpreter == null) return;
		try {
			bshInterpreter.evalNoLog("testStarted()"); // $NON-NLS-1$
		} catch (JMeterException ignored) {
			log.debug(ignored.getLocalizedMessage());
		}		
	}

	public void testStarted(String host) {
		if (bshInterpreter == null) return;
		try {
			bshInterpreter.eval((new StringBuffer("testStarted(")) // $NON-NLS-1$
					.append(host)
					.append(")") // $NON-NLS-1$
					.toString()); // $NON-NLS-1$
		} catch (JMeterException ignored) {
			log.debug(ignored.getLocalizedMessage());
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10242.java