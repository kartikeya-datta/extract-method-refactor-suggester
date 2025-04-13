error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4924.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4924.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4924.java
text:
```scala
private static final S@@tring VERSION = "2.5.1";

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

/*
 * Created on 02-Oct-2003
 *
 * This class defines the JMeter version only (moved from JMeterUtils)
 *
 * Version changes no longer change the JMeterUtils source file
 * - easier to spot when JMeterUtils really changes
 * - much smaller to download when the version changes
 *
 */
package org.apache.jmeter.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

/**
 * Utility class to define the JMeter Version string
 *
 */
public class JMeterVersion {

    /*
     *
     * The string is made private so the compiler can't propagate it into
     * JMeterUtils. (Java compilers may make copies of final variables)
     *
     * This ensures that JMeterUtils always gets the correct
     * version, even if JMeterUtils is not re-compiled during the build.
     */
    private static final String VERSION = "2.5";

    private static final String IMPLEMENTATION;
    
    static final String COPYRIGHT = "Copyright (c) 1998-2011 The Apache Software Foundation";

    static {
        String impl=null;
        final Class<?> myClass = JMeterVersion.class;
        // This assumes that the JMV treats a class file as a resource (not all do).
        URL resource = myClass.getResource("JMeterVersion.class");
        // For example:
        // jar:file:/JMeter/lib/ext/ApacheJMeter_core.jar!/org/apache/jmeter/util/JMeterVersion.class
        // or if using an IDE        
        // file:/workspaces/JMeter/build/core/org/apache/jmeter/util/JMeterVersion.class


        try {
            // Convert to URL for manifest
            String url = resource.toString().replaceFirst("!/.+", "!/META-INF/MANIFEST.MF");
            resource=new URL(url);
            InputStream inputStream = resource.openStream();
            if (inputStream != null) {
                Properties props = new Properties();
                try {
                    props.load(inputStream);
                    impl = props.getProperty("Implementation-Version");
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
            }
        } catch (IOException ioe) {
            // Ignored
        }
        if (impl == null) {
            IMPLEMENTATION = VERSION; // default to plain version
        } else {
            IMPLEMENTATION = impl;
        }
    }

    private JMeterVersion() // Not instantiable
    {
        super();
    }

    static final String getVERSION() {
        return IMPLEMENTATION;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4924.java