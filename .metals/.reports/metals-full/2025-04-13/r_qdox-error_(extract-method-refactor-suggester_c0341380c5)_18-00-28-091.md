error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3110.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3110.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3110.java
text:
```scala
C@@lassMapping cls = (clss == null || clss.length == 0) ? null : clss[0];

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
package org.apache.openjpa.jdbc.kernel.exps;

import java.sql.SQLException;

import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.meta.JavaSQLTypes;
import org.apache.openjpa.jdbc.meta.strats.ContainerFieldStrategy;
import org.apache.openjpa.jdbc.meta.strats.LRSMapFieldStrategy;
import org.apache.openjpa.jdbc.meta.strats.RelationStrategies;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ForeignKey;
import org.apache.openjpa.jdbc.sql.Joins;
import org.apache.openjpa.jdbc.sql.Result;
import org.apache.openjpa.jdbc.sql.SQLBuffer;
import org.apache.openjpa.jdbc.sql.Select;
import org.apache.openjpa.kernel.Filters;
import org.apache.openjpa.meta.ClassMetaData;

/**
 * Returns the value of the given map's key.
 *
 * @author Marc Prud'hommeaux
 */
class GetMapValue
    extends AbstractVal {

    private final Val _map;
    private final Val _key;
    private final String _alias;
    private ClassMetaData _meta = null;
    private Class _cast = null;

    /**
     * Constructor. Provide the map and key to operate on.
     */
    public GetMapValue(Val map, Val key, String alias) {
        _map = map;
        _key = key;
        _alias = alias;
    }

    public ClassMetaData getMetaData() {
        return _meta;
    }

    public void setMetaData(ClassMetaData meta) {
        _meta = meta;
    }

    public boolean isVariable() {
        return false;
    }

    public Class getType() {
        if (_cast != null)
            return _cast;
        return _map.getType();
    }

    public void setImplicitType(Class type) {
        _cast = type;
    }

    public ExpState initialize(Select sel, ExpContext ctx, int flags) {
        ExpState mapState = _map.initialize(sel, ctx, 0);
        ExpState keyState = _key.initialize(sel, ctx, 0);
        return new GetMapValueExpState(sel.and(mapState.joins, keyState.joins),
            mapState, keyState);
    }

    /**
     * Expression state.
     */
    private static class GetMapValueExpState
        extends ExpState {

        public final ExpState mapState;
        public final ExpState keyState;

        public GetMapValueExpState(Joins joins, ExpState mapState, 
            ExpState keyState) {
            super(joins);
            this.mapState = mapState;
            this.keyState = keyState;
        }
    }

    public Object toDataStoreValue(Select sel, ExpContext ctx, ExpState state, 
        Object val) {
        GetMapValueExpState gstate = (GetMapValueExpState) state;
        return _map.toDataStoreValue(sel, ctx, gstate.mapState, val);
    }


    public void select(Select sel, ExpContext ctx, ExpState state, 
        boolean pks) {
        sel.select(newSQLBuffer(sel, ctx, state).append(" AS ").append(_alias),
            this);
    }

    public void selectColumns(Select sel, ExpContext ctx, ExpState state,
        boolean pks) {
        GetMapValueExpState gstate = (GetMapValueExpState) state;
        _map.selectColumns(sel, ctx, gstate.mapState, true);
        _key.selectColumns(sel, ctx, gstate.keyState, true);
    }

    public void groupBy(Select sel, ExpContext ctx, ExpState state) {
        sel.groupBy(newSQLBuffer(sel, ctx, state));
    }

    public void orderBy(Select sel, ExpContext ctx, ExpState state, 
        boolean asc) {
        sel.orderBy(_alias, asc, false);
    }

    private SQLBuffer newSQLBuffer(Select sel, ExpContext ctx, ExpState state) {
        calculateValue(sel, ctx, state, null, null);
        SQLBuffer buf = new SQLBuffer(ctx.store.getDBDictionary());
        appendTo(sel, ctx, state, buf, 0);
        return buf;
    }

    public Object load(ExpContext ctx, ExpState state, Result res)
        throws SQLException {
        return Filters.convert(res.getObject(this,
            JavaSQLTypes.JDBC_DEFAULT, null), getType());
    }

    public void calculateValue(Select sel, ExpContext ctx, ExpState state, 
        Val other, ExpState otherState) {
        GetMapValueExpState gstate = (GetMapValueExpState) state;
        _map.calculateValue(sel, ctx, gstate.mapState, null, null);
        _key.calculateValue(sel, ctx, gstate.keyState, null, null);
    }

    public int length(Select sel, ExpContext ctx, ExpState state) {
        return 1;
    }

    public void appendTo(Select sel, ExpContext ctx, ExpState state, 
        SQLBuffer sql, int index) {
        if (!(_map instanceof PCPath))
            throw new UnsupportedOperationException();
        if (!(_key instanceof Const))
            throw new UnsupportedOperationException();

        GetMapValueExpState gstate = (GetMapValueExpState) state;
        PCPath map = (PCPath) _map;
        Object key = ((Const) _key).getValue(ctx, gstate.keyState);
        FieldMapping field = map.getFieldMapping(gstate.mapState);
        if (!(field.getStrategy() instanceof LRSMapFieldStrategy))
            throw new UnsupportedOperationException();

        LRSMapFieldStrategy strat = (LRSMapFieldStrategy) field.getStrategy();
        ClassMapping[] clss = strat.getIndependentValueMappings(true);
        if (clss != null && clss.length > 1)
            throw RelationStrategies.unjoinable(field);

        ClassMapping cls = (clss.length == 0) ? null : clss[0];
        ForeignKey fk = strat.getJoinForeignKey(cls);

        // manually create a subselect for the Map's value
        sql.append("(SELECT ");
        Column[] values = field.getElementMapping().getColumns();
        for (int i = 0; i < values.length; i++) {
            if (i > 0)
                sql.append(", ");
            sql.append(values[i].getTable()).append(".").append(values[i]);
        }
        sql.append(" FROM ").append(values[0].getTable());
        sql.append(" WHERE ");

        // add in the joins
        ContainerFieldStrategy.appendUnaliasedJoin(sql, sel, null, 
            ctx.store.getDBDictionary(), field, fk);
        sql.append(" AND ");

        key = strat.toKeyDataStoreValue(key, ctx.store);
        Column[] cols = strat.getKeyColumns(cls);
        Object[] vals = (cols.length == 1) ? null : (Object[]) key;

        for (int i = 0; i < cols.length; i++) {
            sql.append(cols[i].getTable()).append(".").append(cols[i]);
            if (vals == null)
                sql.append((key == null) ? " IS " : " = ").
                    appendValue(key, cols[i]);
            else
                sql.append((vals[i] == null) ? " IS " : " = ").
                    appendValue(vals[i], cols[i]);
        }
        sql.append(")");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3110.java