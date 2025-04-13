error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5887.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5887.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5887.java
text:
```scala
S@@tringBuilder buffer = new StringBuilder(256);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang.exception;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.SystemUtils;

/**
 * Provides context feature for exceptions.  Used by both checked and unchecked version of the contexted exceptions.
 * @see ContextedRuntimeException
 * @author D. Ashmore
 * @since 3.0
 */
public class DefaultExceptionContext implements ExceptionContext {
    
    private static final long serialVersionUID = 293747957535772807L;
    
    /*
     * This value list could really be obtained from the Map, however, some
     * callers want to control the order of the list as it appears in the 
     * Message.  The list allows that.  name/value pairs will appear in
     * the order that they're provided.   D. Ashmore
     */
    private List<String> contextKeyList = new ArrayList<String>();
    private Map<String, Serializable> contextValueMap = new HashMap<String, Serializable>();
    
    /**
     * Adds information helpful to a developer in diagnosing and correcting
     * the problem.  
     * @see ContextedException#addLabeledValue(String, Serializable)
     * @param label  a textual label associated with information
     * @param value  information needed to understand exception.  May be null.
     * @return this
     * @since 3.0
     */
    public ExceptionContext addLabeledValue(String label, Serializable value) {        
        this.contextKeyList.add(label);
        this.contextValueMap.put(label, value);
        
        return this;
    }
    
    /**
     * Retrieves the value for a given label.
     * @param label  a textual label associated with information
     * @return value  information needed to understand exception.  May be null.
     * @since 3.0
     */
    public Serializable getLabeledValue(String label) {
        return this.contextValueMap.get(label);
    }
    
    /**
     * Retrieves currently defined labels.
     * @return labelSet
     * @since 3.0
     */
    public Set<String> getLabelSet() {
        return this.contextValueMap.keySet();
    }
    
    /**
     * Centralized message logic for both checked and unchecked version of
     * context exceptions
     * @param baseMessage message retained by super class
     * @return message -- exception message
     * @since 3.0
     */
    public String getFormattedExceptionMessage(String baseMessage){
        StringBuffer buffer = new StringBuffer(256);
        if (baseMessage != null) {
            buffer.append(baseMessage);
        }
        
        if (contextKeyList.size() > 0) {
            if (buffer.length() > 0l) {
                buffer.append(SystemUtils.LINE_SEPARATOR);
            }
            buffer.append("Exception Context:");
            buffer.append(SystemUtils.LINE_SEPARATOR); 
            buffer.append("\t");  
            
            Object value;
            String valueStr;
            for (String label: this.contextKeyList) {
                buffer.append("[");
                buffer.append(label);
                buffer.append("=");
                value = this.contextValueMap.get(label);
                if (value == null) {
                    buffer.append("null");
                }
                else {
                    try {valueStr = value.toString();}
                    catch (Throwable t) {
                        valueStr = "Excepted on toString(): " + 
                            ExceptionUtils.getStackTrace(t);
                    }
                    buffer.append(valueStr);
                }
                buffer.append("]");
                buffer.append(SystemUtils.LINE_SEPARATOR);  
                buffer.append("\t");  
            }
            buffer.append("---------------------------------");
        }
        return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5887.java