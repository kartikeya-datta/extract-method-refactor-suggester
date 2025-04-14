error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6777.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6777.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6777.java
text:
```scala
S@@SLManager.getInstance().configureKeystore(Boolean.parseBoolean(preload),

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

package org.apache.jmeter.config;

import org.apache.commons.lang.StringUtils;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.TestListener;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.util.SSLManager;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JMeterStopTestException;
import org.apache.log.Logger;

/**
 * Configure Keystore
 */
public class KeystoreConfig extends ConfigTestElement implements TestBean, TestListener {
    /**
     * 
     */
    private static final long serialVersionUID = -5781402012242794890L;
    private Logger log = LoggingManager.getLoggerForClass();

    private static final String KEY_STORE_START_INDEX = "https.keyStoreStartIndex"; // $NON-NLS-1$
    private static final String KEY_STORE_END_INDEX   = "https.keyStoreEndIndex"; // $NON-NLS-1$

    private String startIndex;
    private String endIndex;
    private String preload;
    
    /**
     * 
     */
    public KeystoreConfig() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public void testEnded() {
        testEnded(null);
    }

    /**
     * {@inheritDoc}
     */
    public void testEnded(String host) {
        log.info("Destroying Keystore");         
        SSLManager.getInstance().destroyKeystore();
    }

    /**
     * {@inheritDoc}
     */
    public void testIterationStart(LoopIterationEvent event) {
        // NOOP        
    }

    /**
     * {@inheritDoc}
     */
    public void testStarted() {
        testStarted(null);
    }

    /**
     * {@inheritDoc}
     */
    public void testStarted(String host) {
        int startIndexAsInt = JMeterUtils.getPropDefault(KEY_STORE_START_INDEX, 0);
        int endIndexAsInt = JMeterUtils.getPropDefault(KEY_STORE_END_INDEX, 0);
        
        if(!StringUtils.isEmpty(this.startIndex)) {
        	try {
        		startIndexAsInt = Integer.parseInt(this.startIndex);
        	} catch(NumberFormatException e) {
        		log.warn("Failed parsing startIndex :'"+this.startIndex+"', will default to:'"+startIndexAsInt+"', error message:"+ e.getMessage(), e);
        	}
        } 
        
        if(!StringUtils.isEmpty(this.endIndex)) {
        	try {
        		endIndexAsInt = Integer.parseInt(this.endIndex);
        	} catch(NumberFormatException e) {
        		log.warn("Failed parsing endIndex :'"+this.endIndex+"', will default to:'"+endIndexAsInt+"', error message:"+ e.getMessage(), e);
        	}
        } 
        if(startIndexAsInt>endIndexAsInt) {
        	throw new JMeterStopTestException("Keystore Config error : Alias start index must be lower than Alias end index");
        }
        log.info("Configuring Keystore with (preload:"+preload+", startIndex:"+
                startIndexAsInt+", endIndex:"+endIndexAsInt+")");

        SSLManager.getInstance().configureKeystore(Boolean.valueOf(preload),
        		startIndexAsInt, 
                endIndexAsInt);
    }

    /**
     * @return the endIndex
     */
    public String getEndIndex() {
        return endIndex;
    }

    /**
     * @param endIndex the endIndex to set
     */
    public void setEndIndex(String endIndex) {
        this.endIndex = endIndex;
    }

    /**
     * @return the startIndex
     */
    public String getStartIndex() {
        return startIndex;
    }

    /**
     * @param startIndex the startIndex to set
     */
    public void setStartIndex(String startIndex) {
        this.startIndex = startIndex;
    }

    /**
     * @return the preload
     */
    public String getPreload() {
        return preload;
    }

    /**
     * @param preload the preload to set
     */
    public void setPreload(String preload) {
        this.preload = preload;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6777.java