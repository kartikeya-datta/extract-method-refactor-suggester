error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10837.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10837.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10837.java
text:
```scala
_@@supportedKeys.addAll(javaxHintsMap.keySet());

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
package org.apache.openjpa.persistence;

import static org.apache.openjpa.kernel.QueryHints.HINT_IGNORE_PREPARED_QUERY;
import static org.apache.openjpa.kernel.QueryHints
                    .HINT_INVALIDATE_PREPARED_QUERY;
import static org.apache.openjpa.kernel.QueryHints.HINT_RESULT_COUNT;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.enhance.Reflection;
import org.apache.openjpa.kernel.FetchConfiguration;
import org.apache.openjpa.kernel.Filters;
import org.apache.openjpa.kernel.exps.AggregateListener;
import org.apache.openjpa.kernel.exps.FilterListener;
import org.apache.openjpa.lib.conf.ProductDerivation;
import org.apache.openjpa.lib.conf.ProductDerivations;
import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.lib.util.StringDistance;

/**
 * Manages query hint keys and handles their values on behalf of a owning
 * {@link QueryImpl}. Uses specific knowledge of hint keys declared in
 * different parts of the system.
 * 
 * This receiver collects hint keys from different parts of the system. The
 * keys are implicitly or explicitly declared by several different mechanics.
 * This receiver sets the values on behalf of a owning {@link QueryImpl}
 * based on the its specific knowledge of these keys.
 * 
 * The hint keys from following sources are collected and handled: 
 * 
 * 1. {@link org.apache.openjpa.kernel.QueryHints} interface declares hint keys
 *    as public static final fields. These fields are collected by reflection.
 *    The values are handled by invoking methods on the owning {@link QueryImpl}
 *    
 * 2. Some hint keys are collected from bean-style property names of {@link 
 *    JDBCFetchPlan} by {@link Reflection#getBeanStylePropertyNames(Class) 
 *    reflection} and prefixed with <code>openjpa.FetchPlan</code>. 
 *    Their values are used to set the corresponding property of {@link 
 *    FetchPlan} via {@link #hintToSetter(FetchPlan, String, Object) reflection}
 *      
 * 3. Currently defined <code>javax.persistence.*</code> hint keys have  
 *    a equivalent counterpart to one of these FetchPlan keys. 
 *    The JPA keys are mapped to equivalent FetchPlan hint keys.
 *    
 * 4. Some keys directly invoke setters or add listeners to the owning 
 *    {@link QueryImpl}. These hint keys are statically declared in 
 *    this receiver itself. 
 *    
 * 5. ProductDerivation may introduce their own query hint keys via {@link 
 *    ProductDerivation#getSupportedQueryHints()}. Their values are set in the 
 *    {@link FetchConfiguration#setHint(String, Object)}
 *     
 *  A hint key is classified into one of the following three categories:
 *  
 *  1. Supported: A key is known to this receiver as collected from different 
 *     parts of the system. The value of a supported key is recorded and 
 *     available via {@link #getHints()} method. 
 *  2. Recognized: A key is not known to this receiver but has a prefix which
 *     is known to this receiver. The value of a recognized key is not recorded 
 *     but its value is available via {@link FetchConfiguration#getHint(String)}
 *  3. Unrecognized: A key is neither supported nor recognized. The value of a 
 *     unrecognized key is neither recorded nor set anywhere.
 *  
 *  If an incompatible value is supplied for a supported key, a non-fatal
 *  {@link ArgumentException} is raised.
 *  
 * @author Pinaki Poddar
 *
 * @since 2.0.0
 * 
 * @nojavadoc
 */
public class HintHandler extends FetchPlanHintHandler {

    private static final Localizer _loc = Localizer.forPackage(
        HintHandler.class);

    private final QueryImpl owner;
    private Map<String, Object> _hints;
    private static Set<String> _supportedKeys;
    private static Set<String> _supportedPrefixes;
    
    // These keys are directly handled in {@link QueryImpl} class.
    // Declaring a public static final String variable in this class will 
    // make it register as a supported hint key
    // if you do not want that then annotate as {@link Reflectable(false)}.
    public static final String HINT_SUBCLASSES = "openjpa.Subclasses";
    public static final String HINT_FILTER_LISTENER = "openjpa.FilterListener";
    public static final String HINT_FILTER_LISTENERS = 
        "openjpa.FilterListeners";
    public static final String HINT_AGGREGATE_LISTENER = 
        "openjpa.AggregateListener";
    public static final String HINT_AGGREGATE_LISTENERS = 
        "openjpa.AggregateListeners";
    
    HintHandler(QueryImpl impl) {
        super((FetchPlanImpl)impl.getFetchPlan());
        owner = impl;
    }
    
    /**
     * Gets all the recorded hint keys and their values.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getHints() {
        return _hints == null ? Collections.EMPTY_MAP 
            : Collections.unmodifiableMap(_hints);
    }
    
    /**
     * Record a key-value pair only only if the given key is supported.
     * 
     * @return FALSE if the key is unrecognized. 
     *         null (i.e. MAY BE) if the key is recognized, but not supported.
     *         TRUE if the key is supported.
     */
    private Boolean record(String hint, Object value) {
        if (hint == null)
            return Boolean.FALSE;
        if (isSupported(hint)) {
            if (_hints == null)
                _hints = new TreeMap<String, Object>();
            _hints.put(hint, value);
            return Boolean.TRUE;
        }
        
        Log log = owner.getDelegate().getBroker().getConfiguration()
            .getLog(OpenJPAConfiguration.LOG_RUNTIME);
        String possible = StringDistance.getClosestLevenshteinDistance(hint, 
            getSupportedHints());
        if (log.isWarnEnabled())
            log.warn(_loc.get("bad-query-hint", hint, possible));
        return (isKnownHintPrefix(hint)) ? null : Boolean.FALSE;
    }
    
    /**
     * Gets all the supported hint keys. The set of supported hint keys is
     * statically determined by collecting hint keys from the ProductDerivations
     * and reflecting upon some of the known classes.
     */
    public Set<String> getSupportedHints() {
        if (_supportedKeys == null) {
            _supportedKeys = new TreeSet<String>();
            _supportedPrefixes = new TreeSet<String>();
            
            _supportedKeys.addAll(Reflection.getFieldValues(
                org.apache.openjpa.kernel.QueryHints.class, 
                Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL, 
                String.class));

            _supportedKeys.addAll(addPrefix(PREFIX_FETCHPLAN, 
                Reflection.getBeanStylePropertyNames(
                      owner.getFetchPlan().getClass())));

            _supportedKeys.addAll(JavaxHintsMap.keySet());

            _supportedKeys.addAll(Reflection.getFieldValues(
                HintHandler.class, 
                Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL, 
                String.class));

            _supportedKeys.addAll(ProductDerivations.getSupportedQueryHints());
            
            for (String key : _supportedKeys) {
                _supportedPrefixes.add(getPrefixOf(key));
            }
        }
        return _supportedKeys;
    }
    
    /**
     * Add a hint key to the set of supported hint keys.
     */
    public void addHintKey(String key) {
        getSupportedHints().add(key);
        _supportedPrefixes.add(getPrefixOf(key));
    }
    
    public Set<String> getKnownPrefixes() {
        getSupportedHints();
        return _supportedPrefixes;
    }
    
    /**
     * Affirms the given key matches one of the supported keys.
     */
    private boolean isSupported(String key) {
        return getSupportedHints().contains(key);
    }
    
    private boolean isKnownHintPrefix(String key) {
        String prefix = getPrefixOf(key);
        return getKnownPrefixes().contains(prefix);
    }

    public void setHint(String key, Object value) {
        owner.lock();
        try {
            Boolean record = record(key, value);
            if (record == Boolean.FALSE)
                return;
            FetchPlan plan = owner.getFetchPlan();
            if (record == null) {
                plan.setHint(key, value);
                return;
            }
            // request to throw IllegalArgumentException, if needed.
            if (setHint(key, value, true))
                plan.addHint(key, value);
        } finally {
            owner.unlock();
        }
    }
    
    protected boolean setHintInternal(String key, Object value,
        boolean validateThrowException) {
        ClassLoader loader = owner.getDelegate().getBroker().getClassLoader();
        FetchPlan fPlan = owner.getFetchPlan();
        boolean objectSet = true;
        if (HINT_SUBCLASSES.equals(key)) {
            if (value instanceof String)
                value = Boolean.valueOf((String) value);
            owner.setSubclasses(((Boolean) value).booleanValue());
        } else if (HINT_FILTER_LISTENER.equals(key))
            owner.addFilterListener(Filters.hintToFilterListener(value, 
                loader));
        else if (HINT_FILTER_LISTENERS.equals(key)) {
            FilterListener[] arr = Filters.hintToFilterListeners(value, loader);
            for (int i = 0; i < arr.length; i++)
                owner.addFilterListener(arr[i]);
        } else if (HINT_AGGREGATE_LISTENER.equals(key))
            owner.addAggregateListener(Filters.hintToAggregateListener(value,
                loader));
        else if (HINT_AGGREGATE_LISTENERS.equals(key)) {
            AggregateListener[] arr = Filters.hintToAggregateListeners(value,
                loader);
            for (int i = 0; i < arr.length; i++)
                owner.addAggregateListener(arr[i]);
        } else if (HINT_RESULT_COUNT.equals(key)) {
            int v = (Integer) Filters.convert(value, Integer.class);
            if (v < 0)
                throw new ArgumentException(_loc.get("bad-query-hint-value",
                    key, value), null, null, false);
            fPlan.setHint(key, v);
            objectSet = false;
        } else if (HINT_INVALIDATE_PREPARED_QUERY.equals(key)) {
            fPlan.setHint(key, Filters.convert(value, Boolean.class));
            owner.invalidatePreparedQuery();
            objectSet = false;
        } else if (HINT_IGNORE_PREPARED_QUERY.equals(key)) {
            fPlan.setHint(key, Filters.convert(value, Boolean.class));
            owner.ignorePreparedQuery();
            objectSet = false;
        } else { // default 
            fPlan.setHint(key, value);
            objectSet = false;
        }
        return objectSet;
    }

    protected String hintToKey(String key) {
        // Let superclass performs key transformation when fPlan.setHint() 
        // is called.
        return key;
    }

    private Set<String> addPrefix(String prefix, Set<String> original) {
        Set<String> result = new TreeSet<String>();
        String join = prefix.endsWith(DOT) ? BLANK : DOT;
        for (String o : original)
            result.add(prefix + join + o);
        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10837.java