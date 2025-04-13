error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16803.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16803.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16803.java
text:
```scala
public v@@oid loadXMLMetaData(Class<?> cls) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.meta;

import java.io.File;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.lib.meta.ClassArgParser;

/**
 * No-op metadata I/O to prevent attempts to load other classes.
 *
 * @author Abe White
 * @nojavadoc
 */
public class NoneMetaDataFactory
    implements MetaDataFactory, MetaDataDefaults {

    private static final NoneMetaDataFactory _instance =
        new NoneMetaDataFactory();

    public static NoneMetaDataFactory getInstance() {
        return _instance;
    }

    public void setRepository(MetaDataRepository repos) {
    }

    public void setStoreDirectory(File dir) {
    }

    public void setStoreMode(int store) {
    }

    public void setStrict(boolean strict) {
    }

    public void load(Class cls, int mode, ClassLoader envLoader) {
    }

    public boolean store(ClassMetaData[] metas, QueryMetaData[] queries,
        SequenceMetaData[] seqs, int mode, Map output) {
        return false;
    }

    public boolean drop(Class[] cls, int mode, ClassLoader envLoader) {
        return false;
    }

    public MetaDataDefaults getDefaults() {
        return this;
    }

    public Set getPersistentTypeNames(boolean classpath,
        ClassLoader envLoader) {
        return null;
    }

    public Class getQueryScope(String queryName, ClassLoader loader) {
        return null;
    }

    public Class getResultSetMappingScope(String resultSetMappingName,
        ClassLoader loader) {
        return null;
    }

    public ClassArgParser newClassArgParser() {
        return new ClassArgParser();
    }

    public void clear() {
    }

    public void addClassExtensionKeys(Collection exts) {
    }

    public void addFieldExtensionKeys(Collection exts) {
    }

    public int getDefaultAccessType() {
        return AccessCode.UNKNOWN;
    }

    public int getDefaultIdentityType() {
        return ClassMetaData.ID_UNKNOWN;
    }

    public int getCallbackMode() {
        return CALLBACK_IGNORE;
    }

    public boolean getCallbacksBeforeListeners(int type) {
        return false;
    }

    public void setIgnoreNonPersistent(boolean ignore) {
    }

    public boolean isDeclaredInterfacePersistent() {
        return false;
    }

    public boolean isDataStoreObjectIdFieldUnwrapped() {
        return false;
    }

    public void populate(ClassMetaData meta, int access) {
    }

    public void populate(ClassMetaData meta, int access, boolean ignoreTransient) {
    }

    public Member getBackingMember(FieldMetaData fmd) {
        return null;
    }

    public Member getMemberByProperty(ClassMetaData meta, String property,
    		int access, boolean scan) {
    	return null;
    }
    
    public Class<? extends Exception> getUnimplementedExceptionType() {
        return null;
    }
    
    public void loadXMLMetaData(FieldMetaData fmd) {
    }

    public String getMetaModelClassName(String managedClassName) {
        return null;
    }
    public String getManagedClassName(String metamodelClassName) {
        return null;
    }
    
    public boolean isMetaClass(Class<?> c) {
        return false;
    }

    public Class<?> getManagedClass(Class<?> c) {
        return null;
    }

    public boolean isAbstractMappingUniDirectional(OpenJPAConfiguration conf) {
        return false;
    }
    
    public boolean isNonDefaultMappingAllowed(OpenJPAConfiguration conf) {
        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16803.java