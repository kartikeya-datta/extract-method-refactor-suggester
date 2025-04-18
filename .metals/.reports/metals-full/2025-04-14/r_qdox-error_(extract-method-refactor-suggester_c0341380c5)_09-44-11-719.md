error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14571.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14571.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14571.java
text:
```scala
D@@ouble expectedValue = certifiedValues.get(name);

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

package org.apache.commons.math.stat.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.math.TestUtils;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;

/**
 * @version $Revision$ $Date$
 */
public abstract class CertifiedDataAbstractTest extends TestCase {
    
    private DescriptiveStatistics descriptives;
    
    private SummaryStatistics summaries;
    
    private Map<String, Double> certifiedValues;

    @Override
    protected void setUp() throws Exception {
        descriptives = new DescriptiveStatistics();
        summaries = new SummaryStatistics();
        certifiedValues = new HashMap<String, Double>();
        
        loadData();
    }

    private void loadData() throws IOException {
        BufferedReader in = null;

        try {
            URL resourceURL = getClass().getClassLoader().getResource(getResourceName());
            in = new BufferedReader(new InputStreamReader(resourceURL.openStream()));
            
            String line = in.readLine();
            while (line != null) {
                
                /* this call to StringUtils did little for the 
                 * following conditional structure 
                 */
                line = line.trim();

                // not empty line or comment
                if (!("".equals(line) || line.startsWith("#"))) {
                    int n = line.indexOf('=');
                    if (n == -1) {
                        // data value
                        double value = Double.parseDouble(line);
                        descriptives.addValue(value);
                        summaries.addValue(value);
                    } else {
                        // certified value
                        String name = line.substring(0, n).trim();
                        String valueString = line.substring(n + 1).trim();
                        Double value = Double.valueOf(valueString);
                        certifiedValues.put(name, value);
                    }
                }
                line = in.readLine();
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    protected abstract String getResourceName();

    protected double getMaximumAbsoluteError() {
        return 1.0e-5;
    }

    @Override
    protected void tearDown() throws Exception {
        descriptives.clear();
        descriptives = null;
        
        summaries.clear();
        summaries = null;
        
        certifiedValues.clear();
        certifiedValues = null;
    }
    
    public void testCertifiedValues() {
        for (String name : certifiedValues.keySet()) {
            Double expectedValue = (Double)certifiedValues.get(name);

            Double summariesValue = getProperty(summaries, name);
            if (summariesValue != null) {
                TestUtils.assertEquals("summary value for " + name + " is incorrect.",
                                       summariesValue.doubleValue(), expectedValue.doubleValue(),
                                       getMaximumAbsoluteError());
            }

            Double descriptivesValue = getProperty(descriptives, name);
            if (descriptivesValue != null) {
                TestUtils.assertEquals("descriptive value for " + name + " is incorrect.",
                                       descriptivesValue.doubleValue(), expectedValue.doubleValue(),
                                       getMaximumAbsoluteError());
            }
        }
    }
    
    
    protected Double getProperty(Object bean, String name) {
        try {
            // Get the value of prop
            String prop = "get" + name.substring(0,1).toUpperCase() + name.substring(1); 
            Method meth = bean.getClass().getMethod(prop, new Class[0]);
            Object property = meth.invoke(bean, new Object[0]);
            if (meth.getReturnType().equals(Double.TYPE)) {
                return (Double) property;
            } else if (meth.getReturnType().equals(Long.TYPE)) {
                return Double.valueOf(((Long) property).doubleValue());
            } else {
                fail("wrong type: " + meth.getReturnType().getName());
            }
        } catch (NoSuchMethodException nsme) {
            // ignored
        } catch (InvocationTargetException ite) {
            fail(ite.getMessage());
        } catch (IllegalAccessException iae) {
            fail(iae.getMessage());
        }
        return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14571.java