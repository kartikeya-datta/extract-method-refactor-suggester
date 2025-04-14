error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10909.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10909.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10909.java
text:
```scala
t@@hrow new RuntimeException("Could not find classes with class finder", e);

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

/*
 * Created on May 24, 2004
 *
 */
package org.apache.jmeter.protocol.http.sampler;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.List;

import org.apache.jmeter.protocol.http.util.accesslog.Filter;
import org.apache.jmeter.protocol.http.util.accesslog.LogParser;
import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.FileEditor;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.reflect.ClassFinder;
import org.apache.log.Logger;

public class AccessLogSamplerBeanInfo extends BeanInfoSupport {
    private static final Logger log = LoggingManager.getLoggerForClass();

    public AccessLogSamplerBeanInfo() {
        super(AccessLogSampler.class);
        log.debug("Entered access log sampler bean info");
        try {
            createPropertyGroup("defaults",  // $NON-NLS-1$
                    new String[] { "domain", "portString", "imageParsing" });// $NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$

            createPropertyGroup("plugins",  // $NON-NLS-1$
                    new String[] { "parserClassName", "filterClassName" }); // $NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$

            createPropertyGroup("accesslogfile",  // $NON-NLS-1$
                    new String[] { "logFile" }); // $NON-NLS-1$

            PropertyDescriptor p;

            p = property("parserClassName");
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, AccessLogSampler.DEFAULT_CLASS);
            p.setValue(NOT_OTHER, Boolean.TRUE);
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            final List<String> logParserClasses = ClassFinder.findClassesThatExtend(JMeterUtils.getSearchPaths(), new Class[] { LogParser.class });
            if (log.isDebugEnabled()) {
                log.debug("found parsers: " + logParserClasses);
            }
            p.setValue(TAGS, logParserClasses.toArray(new String[logParserClasses.size()]));

            p = property("filterClassName"); // $NON-NLS-1$
            p.setValue(NOT_UNDEFINED, Boolean.FALSE);
            p.setValue(DEFAULT, ""); // $NON-NLS-1$
            p.setValue(NOT_EXPRESSION, Boolean.TRUE);
            List<String> classes = ClassFinder.findClassesThatExtend(JMeterUtils.getSearchPaths(),
                    new Class[] { Filter.class }, false);
            p.setValue(TAGS, classes.toArray(new String[classes.size()]));

            p = property("logFile"); // $NON-NLS-1$
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");
            p.setPropertyEditorClass(FileEditor.class);

            p = property("domain"); // $NON-NLS-1$
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, "");

            p = property("portString"); // $NON-NLS-1$
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, ""); // $NON-NLS-1$

            p = property("imageParsing"); // $NON-NLS-1$
            p.setValue(NOT_UNDEFINED, Boolean.TRUE);
            p.setValue(DEFAULT, Boolean.FALSE);
            p.setValue(NOT_OTHER, Boolean.TRUE);
        } catch (IOException e) {
            log.warn("couldn't find classes and set up properties", e);
            throw new RuntimeException("Could not find classes with class finder");
        }
        log.debug("Got to end of access log samper bean info init");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10909.java