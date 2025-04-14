error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14661.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14661.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14661.java
text:
```scala
i@@nt typeSep = key.indexOf('$'); // $NON-NLS-1$

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.jmeter.protocol.http.sampler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

/*
 * Utility class to set up default HttpClient parameters from a file.
 * 
 * Supports both Commons HttpClient and Apache HttpClient.
 * 
 */
public class HttpClientDefaultParameters {

    private static final Logger log = LoggingManager.getLoggerForClass();

    // Non-instantiable
    private HttpClientDefaultParameters(){
    }

    // Helper class (callback) for applying parameter definitions
    private static abstract class GenericHttpParams {
        public abstract void setParameter(String name, Object value);
        public abstract void setVersion(String name, String value) throws Exception;
    }

    /**
     * Loads a property file and converts parameters as necessary.
     * 
     * @param file the file to load
     * @param params Commons HttpClient parameter instance
     */
    public static void load(String file, 
            final org.apache.commons.httpclient.params.HttpParams params){
        load(file, 
                new GenericHttpParams (){
                    @Override
                    public void setParameter(String name, Object value) {
                        params.setParameter(name, value);
                    }
                    @Override
                    public void setVersion(String name, String value) throws Exception {
                        params.setParameter(name,
                        org.apache.commons.httpclient.HttpVersion.parse("HTTP/"+value));
                    }            
                }
            );
    }

    /**
     * Loads a property file and converts parameters as necessary.
     * 
     * @param file the file to load
     * @param params Apache HttpClient parameter instance
     */
    public static void load(String file, 
            final org.apache.http.params.HttpParams params){
        load(file, 
                new GenericHttpParams (){
                    @Override
                    public void setParameter(String name, Object value) {
                        params.setParameter(name, value);
                    }

                    @Override
                    public void setVersion(String name, String value) {
                        String parts[] = value.split("\\.");
                        if (parts.length != 2){
                            throw new IllegalArgumentException("Version must have form m.n");
                        }
                        params.setParameter(name,
                                new org.apache.http.HttpVersion(
                                        Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
                    }            
                }
            );
    }

    private static void load(String file, GenericHttpParams params){
        log.info("Reading httpclient parameters from "+file);
        File f = new File(file);
        InputStream is = null;
        Properties props = new Properties();
        try {
            is = new FileInputStream(f);
            props.load(is);
            for (Map.Entry<Object, Object> me : props.entrySet()){
                String key = (String) me.getKey();
                String value = (String)me.getValue();
                int typeSep = key.indexOf("$"); // $NON-NLS-1$
                try {
                    if (typeSep > 0){
                        String type = key.substring(typeSep+1);// get past separator
                        String name=key.substring(0,typeSep);
                        log.info("Defining "+name+ " as "+value+" ("+type+")");
                        if (type.equals("Integer")){
                            params.setParameter(name, Integer.valueOf(value));
                        } else if (type.equals("Long")){
                            params.setParameter(name, Long.valueOf(value));
                        } else if (type.equals("Boolean")){
                            params.setParameter(name, Boolean.valueOf(value));
                        } else if (type.equals("HttpVersion")){ // Commons HttpClient only
                            params.setVersion(name, value);
                        } else {
                            log.warn("Unexpected type: "+type+" for name "+name);
                        }
                    } else {
                            log.info("Defining "+key+ " as "+value);
                            params.setParameter(key, value);
                    }
                } catch (Exception e) {
                    log.error("Error in property: "+key+"="+value+" "+e.toString());
                }
            }
        } catch (FileNotFoundException e) {
            log.error("Problem loading properties "+e.toString());
        } catch (IOException e) {
            log.error("Problem loading properties "+e.toString());
        } finally {
            JOrphanUtils.closeQuietly(is);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14661.java