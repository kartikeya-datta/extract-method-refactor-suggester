error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2335.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2335.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2335.java
text:
```scala
L@@OGGER.trace(NLS.MESSAGES.getMessage("cannot.weave", wovenClass.getClassName()), e);

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
package org.apache.aries.proxy.impl.weaving;

import java.util.List;

import org.apache.aries.proxy.UnableToProxyException;
import org.apache.aries.proxy.impl.NLS;
import org.osgi.framework.Bundle;
import org.osgi.framework.hooks.weaving.WeavingException;
import org.osgi.framework.hooks.weaving.WeavingHook;
import org.osgi.framework.hooks.weaving.WovenClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProxyWeavingHook implements WeavingHook {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProxyWeavingHook.class);
  /** An import of the WovenProxy package */
  private static final String IMPORT_A = "org.apache.aries.proxy.weaving";
  /** 
   * An import for the InvocationListener class that we will need.
   * This should automatically wire to the right thing because of the uses clause
   * on the impl.weaving package
   */
  private static final String IMPORT_B = "org.apache.aries.proxy";
  
  public final void weave(WovenClass wovenClass) {
    
    Bundle b = wovenClass.getBundleWiring().getBundle();
    
    if(b.getBundleId() == 0 || 
        b.getSymbolicName().startsWith("org.apache.aries.proxy") ||
        b.getSymbolicName().startsWith("org.apache.aries.util")) {
      return;
    }
    
    if(wovenClass.getClassName().startsWith("org.objectweb.asm") || 
        wovenClass.getClassName().startsWith("org.slf4j") || 
        wovenClass.getClassName().startsWith("org.apache.log4j"))
      return;
    
    byte[] bytes = null;
    
    try {
      bytes = WovenProxyGenerator.getWovenProxy(wovenClass.getBytes(),
          wovenClass.getClassName(), wovenClass.getBundleWiring().getClassLoader());
      
    } catch (Exception e) {
      if(e instanceof RuntimeException && 
          e.getCause() instanceof UnableToProxyException){
        //This is a weaving failure that should be logged, but the class
        //can still be loaded
        LOGGER.info(NLS.MESSAGES.getMessage("cannot.weave", wovenClass.getClassName()), e);
      } else {
        String failureMessage = NLS.MESSAGES.getMessage("fatal.weaving.failure", wovenClass.getClassName());
        //This is a failure that should stop the class loading!
        LOGGER.error(failureMessage, e);
        throw new WeavingException(failureMessage, e);
      }
    }
    
    if(bytes != null && bytes.length != 0) {
      wovenClass.setBytes(bytes);
      List<String> imports = wovenClass.getDynamicImports();
      imports.add(IMPORT_A);
      imports.add(IMPORT_B);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2335.java