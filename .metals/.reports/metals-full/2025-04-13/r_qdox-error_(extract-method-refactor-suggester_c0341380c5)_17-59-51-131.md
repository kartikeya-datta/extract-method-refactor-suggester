error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12873.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12873.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12873.java
text:
```scala
private final M@@ap<String, SamplerCreator> samplerCreatorMap = new HashMap<String, SamplerCreator>();

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

package org.apache.jmeter.protocol.http.proxy;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.reflect.ClassFinder;
import org.apache.log.Logger;

/**
 * {@link SamplerCreator} factory
 */
public class SamplerCreatorFactory {
    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final SamplerCreator DEFAULT_SAMPLER_CREATOR = new DefaultSamplerCreator();

    private Map<String, SamplerCreator> samplerCreatorMap = new HashMap<String, SamplerCreator>();

    /**
     * 
     */
    public SamplerCreatorFactory() {
        init();
    }
    
    /**
     * Initialize factory from classpath
     */
    private void init() {
        try {
            List<String> listClasses = ClassFinder.findClassesThatExtend(
                    JMeterUtils.getSearchPaths(), 
                    new Class[] {SamplerCreator.class }); 
            for (String strClassName : listClasses) {
                try {
                    if(log.isDebugEnabled()) {
                        log.debug("Loading class: "+ strClassName);
                    }
                    Class<?> commandClass = Class.forName(strClassName);
                    if (!Modifier.isAbstract(commandClass.getModifiers())) {
                        if(log.isDebugEnabled()) {
                            log.debug("Instantiating: "+ commandClass.getName());
                        }
                            SamplerCreator creator = (SamplerCreator) commandClass.newInstance();
                            String[] contentTypes = creator.getManagedContentTypes();
                            for (String contentType : contentTypes) {
                                if(log.isDebugEnabled()) {
                                    log.debug("Registering samplerCreator "+commandClass.getName()+" for content type:"+contentType);
                                }
                                SamplerCreator oldSamplerCreator = samplerCreatorMap.put(contentType, creator);
                                if(oldSamplerCreator!=null) {
                                    log.warn("A sampler creator was already registered for:"+contentType+", class:"+oldSamplerCreator.getClass()
                                            + ", it will be replaced");
                                }
                            }                        
                    }
                } catch (Exception e) {
                    log.error("Exception registering "+SamplerCreator.class.getName() + " with implementation:"+strClassName, e);
                }
            }
        } catch (IOException e) {
            log.error("Exception finding implementations of "+SamplerCreator.class, e);
        }
    }

    /**
     * Gets {@link SamplerCreator} for content type, if none is found returns {@link DefaultSamplerCreator}
     * @param request {@link HttpRequestHdr}
     * @param pageEncodings Map<String, String> pageEncodings
     * @param formEncodings  Map<String, String> formEncodings
     * @return SamplerCreator
     */
    public SamplerCreator getSamplerCreator(HttpRequestHdr request,
            Map<String, String> pageEncodings, Map<String, String> formEncodings) {
        SamplerCreator creator = samplerCreatorMap.get(request.getContentType());
        if(creator == null) {
            return DEFAULT_SAMPLER_CREATOR;
        }
        return creator;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12873.java