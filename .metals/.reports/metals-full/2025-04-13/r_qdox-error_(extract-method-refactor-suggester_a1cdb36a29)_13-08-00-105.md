error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13700.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13700.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13700.java
text:
```scala
private i@@nt _jpql = JPQL_WARN;

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
package org.apache.openjpa.conf;

/**
 * Struct encompassing backwards-compatibility options.
 */
public class Compatibility {

    /**
     * If a JPQL statement is not compliant with the JPA specification,
     * fail to parse it.
     *
     * @since 1.1.0
     */
    public static final int JPQL_STRICT = 0;

    /**
     * If a JPQL statement is not compliant with the JPA specification,
     * warn the first time that statement is parsed.
     *
     * @since 1.1.0
     */
    public static final int JPQL_WARN = 1;

    /**
     * Allow non-compliant extensions of JPQL.
     * 
     * @since 1.1.0
     */
    public static final int JPQL_EXTENDED = 2;

    private boolean _strictIdValues = false;
    private boolean _hollowLookups = true;
    private boolean _checkStore = false;
    private boolean _copyIds = false;
    private boolean _closeOnCommit = true;
    private boolean _quotedNumbers = false;
    private boolean _nonOptimisticVersionCheck = false;
    private int _jpql = JPQL_STRICT;
    private boolean _storeMapCollectionInEntityAsBlob = false;
    private boolean _flushBeforeDetach = true; 

    /**
     * Whether to require exact identity value types when creating object
     * ids from a class and value. Defaults to false.
     */
    public boolean getStrictIdentityValues() {
        return _strictIdValues;
    }

    /**
     * Whether to require exact identity value types when creating object
     * ids from a class and value. Defaults to false.
     */
    public void setStrictIdentityValues(boolean strictVals) {
        _strictIdValues = strictVals;
    }

    /**
     * Whether to interpret quoted numbers in query strings as numbers.
     * OpenJPA versions 0.3.1 and prior treated them as numbers; more recent
     * versions treat them as strings.
     */
    public boolean getQuotedNumbersInQueries() {
        return _quotedNumbers;
    }

    /**
     * Whether to interpret quoted numbers in query strings as numbers.
     * OpenJPA versions 0.3.1 and prior treated them as numbers; more recent
     * versions treat them as strings.
     */
    public void setQuotedNumbersInQueries(boolean quotedNumbers) {
        _quotedNumbers = quotedNumbers;
    }

    /**
     * Whether to return hollow instances to broker lookups with a
     * <code>validate</code> parameter of false. OpenJPA versions prior to
     * 0.4.0 did not return hollow instances without special configuration
     * (the <code>ObjectLookupMode</code>). Beginning with 0.4.0, hollow
     * objects are the default.
     */
    public boolean getValidateFalseReturnsHollow() {
        return _hollowLookups;
    }

    /**
     * Whether to return hollow instances to broker lookups with a
     * <code>validate</code> parameter of false. OpenJPA versions prior to
     * 0.4.0 did not return hollow instances without special configuration
     * (the <code>ObjectLookupMode</code>). Beginning with 0.4.0, hollow
     * objects are the default.
     */
    public void setValidateFalseReturnsHollow(boolean hollow) {
        _hollowLookups = hollow;
    }

    /**
     * Whether to check the datastore for the existence of a nontransactional
     * cached object in broker lookups with a <code>validate</code> parameter
     * of true. OpenJPA versions prior to 0.4.0 checked the datastore.
     */
    public boolean getValidateTrueChecksStore() {
        return _checkStore;
    }

    /**
     * Whether to check the datastore for the existence of a nontransactional
     * cached object in broker lookups with a <code>validate</code> parameter
     * of true. OpenJPA versions prior to 0.4.0 checked the datastore.
     */
    public void setValidateTrueChecksStore(boolean check) {
        _checkStore = check;
    }

    /**
     * Whether to copy identity objects before returning them to client code.
     * Versions of OpenJPA prior to 0.3.0 always copied identity objects. Also,
     * you should configure OpenJPA to copy identity objects if you mutate them
     * after use.
     */
    public boolean getCopyObjectIds() {
        return _copyIds;
    }

    /**
     * Whether to copy identity objects before returning them to client code.
     * Versions of OpenJPA prior to 0.3.0 always copied identity objects. Also,
     * you should configure OpenJPA to copy identity objects if you mutate them
     * after use.
     */
    public void setCopyObjectIds(boolean copy) {
        _copyIds = copy;
    }

    /**
     * Whether to close the broker when the managed transaction commits.
     * Versions of OpenJPA prior to 0.3.0 did not close the broker.
     */
    public boolean getCloseOnManagedCommit() {
        return _closeOnCommit;
    }

    /**
     * Whether to close the broker when the managed transaction commits.
     * Versions of OpenJPA prior to 0.3.0 did not close the broker.
     */
    public void setCloseOnManagedCommit(boolean close) {
        _closeOnCommit = close;
	}	

    /** 
     * Whether or not to perform a version check on instances being updated
     * in a datastore transaction. Version of OpenJPA prior to 0.4.1 always
     * forced a version check.
     */
    public void setNonOptimisticVersionCheck(boolean nonOptimisticVersionCheck){
        _nonOptimisticVersionCheck = nonOptimisticVersionCheck;
    }

    /** 
     * Whether or not to perform a version check on instances being updated
     * in a datastore transaction. Version of OpenJPA prior to 0.4.1 always
     * forced a version check.
     */
    public boolean getNonOptimisticVersionCheck() {
        return _nonOptimisticVersionCheck;
    }

    /**
     * Whether or not JPQL extensions are allowed. Defaults to
     * {@link #JPQL_STRICT}.
     *
     * @since 1.1.0
     * @see #JPQL_WARN
     * @see #JPQL_STRICT
     * @see #JPQL_EXTENDED
     */
    public int getJPQL() {
        return _jpql;
    }

    /**
     * Whether or not JPQL extensions are allowed. Possible values: "warn",
     * "strict", "extended".
     *
     * @since 1.1.0
     * @see #JPQL_WARN
     * @see #JPQL_STRICT
     * @see #JPQL_EXTENDED
     */
    public void setJPQL(String jpql) {
        if ("warn".equals(jpql))
            _jpql = JPQL_WARN;
        else if ("strict".equals(jpql))
            _jpql = JPQL_STRICT;
        else if ("extended".equals(jpql))
            _jpql = JPQL_EXTENDED;
        else
            throw new IllegalArgumentException(jpql);
    }

    /**
     * Whether if map and collection in entity are stored as blob.
     * Defaults to <code>false</code>.
     *
     * @since 1.1.0 
     */

    public boolean getStoreMapCollectionInEntityAsBlob() {
        return _storeMapCollectionInEntityAsBlob;
    }

    /**
     * Whether if map and collection in entity are stored as blob.
     * Defaults to <code>false</code>.
     *
     * @since 1.1.0 
     */
    public void setStoreMapCollectionInEntityAsBlob(boolean storeAsBlob) {
        _storeMapCollectionInEntityAsBlob = storeAsBlob;
    }
    
    /**
     * Whether OpenJPA should flush changes before detaching or serializing an
     * entity. In JPA this is usually false, but other persistence frameworks
     * (ie JDO) may expect it to be true.
     * <P>Prior to version 1.0.3 and 1.2.0 changes were always flushed.
     * 
     * @since 1.0.3
     * @since 1.2.0
     * @return true if changes should be flushed, otherwise false.
     */
    public boolean getFlushBeforeDetach() {
        return _flushBeforeDetach;
    }

    /**
     * Whether OpenJPA should flush changes before detaching or serializing an
     * entity. In JPA this is usually false, but other persistence frameworks
     * (ie JDO) may expect it to be true.
     * <P>Prior to version 1.0.3 and 1.2.0 changes were always flushed.
     * 
     * @param beforeDetach if true changes will be flushed before detaching or 
     * serializing an entity.
     * 
     * @since 1.0.3
     * @since 1.2.0
     */
    public void setFlushBeforeDetach(boolean beforeDetach) {
        _flushBeforeDetach = beforeDetach;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13700.java