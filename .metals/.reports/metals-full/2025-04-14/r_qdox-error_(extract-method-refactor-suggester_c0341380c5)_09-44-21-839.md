error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18315.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18315.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18315.java
text:
```scala
v@@ariable = ((CompoundVariable)values[1]).execute().trim();

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

package org.apache.jmeter.functions;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;

// See org.apache.jmeter.functions.TestTimeFunction for unit tests

/**
 * __time() function - returns the current time in milliseconds
 */
public class TimeFunction extends AbstractFunction implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private static final String KEY = "__time"; // $NON-NLS-1$

    private static final List desc = new LinkedList();

    private static final Map aliases = new HashMap();
    
    static {
        desc.add(JMeterUtils.getResString("time_format")); //$NON-NLS-1$
        desc.add(JMeterUtils.getResString("function_name_paropt")); //$NON-NLS-1$
        aliases.put("YMD", //$NON-NLS-1$
                JMeterUtils.getPropDefault("time.YMD", //$NON-NLS-1$
                        "yyyyMMdd")); //$NON-NLS-1$
        aliases.put("HMS", //$NON-NLS-1$
                JMeterUtils.getPropDefault("time.HMS", //$NON-NLS-1$
                        "HHmmss")); //$NON-NLS-1$
        aliases.put("YMDHMS", //$NON-NLS-1$
                JMeterUtils.getPropDefault("time.YMDHMS", //$NON-NLS-1$
                        "yyyyMMdd-HHmmss")); //$NON-NLS-1$
        aliases.put("USER1", //$NON-NLS-1$
                JMeterUtils.getPropDefault("time.USER1","")); //$NON-NLS-1$
        aliases.put("USER2", //$NON-NLS-1$
                JMeterUtils.getPropDefault("time.USER2","")); //$NON-NLS-1$
    }

    // Ensure that these are set, even if no paramters are provided
    transient private String format   = ""; //$NON-NLS-1$
    transient private String variable = ""; //$NON-NLS-1$
    
    private Object readResolve(){
        format="";
        variable="";
        return this;
    }
    
    public TimeFunction(){
        super();
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.jmeter.functions.Function#execute(SampleResult, Sampler)
     */
    public synchronized String execute(SampleResult previousResult, Sampler currentSampler) throws InvalidVariableException {
        String datetime;
        if (format.length() == 0){// Default to milliseconds
            datetime = Long.toString(System.currentTimeMillis());
        } else {
            // Resolve any aliases
            String fmt = (String) aliases.get(format);
            if (fmt == null) {
                fmt = format;// Not found
            }
            SimpleDateFormat df = new SimpleDateFormat(fmt);// Not synchronised, so can't be shared
            datetime = df.format(new Date());            
        }
        
        if (variable.length() > 0) {
            JMeterVariables vars = getVariables();
            if (vars != null){// vars will be null on TestPlan
            	vars.put(variable, datetime);
            }
        }
        return datetime;
    }

    /*
     * (non-Javadoc)
     * 
     * It appears that this is not called if no parameters are provided.
     * 
     * @see org.apache.jmeter.functions.Function#setParameters(Collection)
     */
    public synchronized void setParameters(Collection parameters) throws InvalidVariableException {

        checkParameterCount(parameters, 0, 2);
        
        Object []values = parameters.toArray();
        int count = values.length;
        
        if (count > 0) {
            format = ((CompoundVariable) values[0]).execute();
        }
        
        if (count > 1) {
            variable = ((CompoundVariable)values[1]).execute();
        }
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.jmeter.functions.Function#getReferenceKey()
     */
    public String getReferenceKey() {
        return KEY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.jmeter.functions.Function#getArgumentDesc()
     */
    public List getArgumentDesc() {
        return desc;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18315.java