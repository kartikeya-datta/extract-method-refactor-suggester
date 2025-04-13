error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7918.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7918.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7918.java
text:
```scala
public b@@oolean hasParameterizedInExpression;

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
package org.apache.openjpa.kernel.exps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.openjpa.kernel.QueryOperations;
import org.apache.openjpa.kernel.ResultShape;
import org.apache.openjpa.kernel.StoreQuery;
import org.apache.openjpa.kernel.exps.Context;
import org.apache.openjpa.lib.util.OrderedMap;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;

/**
 * Struct to hold the state of a parsed expression query.
 *
 * @author Abe White
 * @since 0.3.2
 * @nojavadoc
 */
@SuppressWarnings("serial")
public class QueryExpressions
    implements Serializable {

    public static final int DISTINCT_AUTO = 2 << 0;
    public static final int DISTINCT_TRUE = 2 << 1;
    public static final int DISTINCT_FALSE = 2 << 2;
    public static final Value[] EMPTY_VALUES = new Value[0];
    
    /**
     * Map of {@link FieldMetaData},{@link Value} for update statements.
     */
    public Map<Path, Value> updates = Collections.emptyMap();
    public int distinct = DISTINCT_AUTO;
    public String alias = null;
    public Value[] projections = EMPTY_VALUES;
    public String[] projectionClauses = StoreQuery.EMPTY_STRINGS;
    public String[] projectionAliases = StoreQuery.EMPTY_STRINGS;
    public Class<?> resultClass = null;
    public Expression filter = null;
    public Value[] grouping = EMPTY_VALUES;
    public String[] groupingClauses = StoreQuery.EMPTY_STRINGS;
    public Expression having = null;
    public Value[] ordering = EMPTY_VALUES;
    public boolean[] ascending = StoreQuery.EMPTY_BOOLEANS;
    public String[] orderingClauses = StoreQuery.EMPTY_STRINGS;
    public String[] orderingAliases = StoreQuery.EMPTY_STRINGS;
    public OrderedMap<Object,Class<?>> parameterTypes = StoreQuery.EMPTY_ORDERED_PARAMS;
    public int operation = QueryOperations.OP_SELECT;
    public ClassMetaData[] accessPath = StoreQuery.EMPTY_METAS;
    public String[] fetchPaths = StoreQuery.EMPTY_STRINGS;
    public String[] fetchInnerPaths = StoreQuery.EMPTY_STRINGS;
    public Value[] range = EMPTY_VALUES;
    private Boolean _aggregate = null;
    private Stack<Context> _contexts = null;
    public Object state;
    public ResultShape<?> shape;
    public boolean hasInExpression;
    
    /**
     * Set reference to the JPQL query contexts.
     * @param contexts
     */
    public void setContexts(Stack<Context> contexts) {
        _contexts = contexts;
    }

    /**
     * Returns the current JPQL query context.
     * @return
     */
    public Context ctx() {
        return _contexts.peek();
    }

    /**
     * Whether this is an aggregate results.
     */
    public boolean isAggregate() {
        if (projections.length == 0)
            return false; 
        if (_aggregate == null)
            _aggregate = (AggregateExpressionVisitor.isAggregate(projections))
                ? Boolean.TRUE : Boolean.FALSE;
        return _aggregate.booleanValue();    
    }
    
    public boolean isDistinct() {
        return distinct != DISTINCT_FALSE;
    }
    
    /**
     * Gets the fields that are bound to parameters.
     * 
     * @return empty if the query has no filtering condition or no parameters.
     */
    public List<FieldMetaData> getParameterizedFields() {
        return ParameterExpressionVisitor.collectParameterizedFields(filter);
    }

    /**
     * Add an update.
     */
    public void putUpdate(Path path, Value val) {
        if (updates == Collections.EMPTY_MAP)
            updates = new LinkedHashMap<Path, Value>();
        updates.put(path, val);
    }

    /**
     * Visitor to determine whether our projections are aggregates.
     */
    private static class AggregateExpressionVisitor
        extends AbstractExpressionVisitor {
        
        private Value _sub = null;
        private boolean _agg = false;

        /**
         * Return whether the given values include projections.
         */
        public static boolean isAggregate(Value[] vals) {
            if (vals.length == 0)
                return false;
            AggregateExpressionVisitor v = new AggregateExpressionVisitor();
            for (int i = 0; i < vals.length && !v._agg; i++)
                vals[i].acceptVisit(v);
            return v._agg;
        }

        public void enter(Value val) {
            if (_agg)
                return;
            if (_sub == null) {
                if (val.isAggregate())
                    _agg = true;
            } else if (val instanceof Subquery)
                _sub = val;
        }

        public void exit(Value val) {
            if (val == _sub)
                _sub = null;
        }
    }
    
    /**
     * Visits the expression tree to find the parameter nodes.
     * @author Pinaki Poddar
     *
     */
    private static class ParameterExpressionVisitor extends AbstractExpressionVisitor {
        private FieldMetaData _parameterized;
        private List<FieldMetaData> _collected = new ArrayList<FieldMetaData>();
        /**
         * Enters the current node.
         */
        public void enter(Value val) {
            if (val instanceof Parameter) {
                if (_parameterized != null) {
                    _collected.add(_parameterized);
                } 
            } else if (val instanceof Path) {
                _parameterized = ((Path)val).last();
            } else {
                _parameterized = null;
            }
        }
        
        public static List<FieldMetaData> collectParameterizedFields(Expression e) {
            if (e == null) {
                return Collections.emptyList();
            }
            ParameterExpressionVisitor visitor = new ParameterExpressionVisitor();
            e.acceptVisit(visitor);
            return visitor._collected;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7918.java