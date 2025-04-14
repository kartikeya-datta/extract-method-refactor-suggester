error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3776.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3776.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3776.java
text:
```scala
r@@eturn ((Id) val).getId();

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
import org.apache.openjpa.jdbc.meta.Joinable;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.sql.Result;
import org.apache.openjpa.jdbc.sql.SQLBuffer;
import org.apache.openjpa.jdbc.sql.Select;
import org.apache.openjpa.kernel.Filters;
import org.apache.openjpa.kernel.exps.ExpressionVisitor;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.util.ApplicationIds;
import org.apache.openjpa.util.Id;
import org.apache.openjpa.util.OpenJPAId;
import org.apache.openjpa.util.UserException;
import serp.util.Numbers;

/**
 * Select the oid value of an object; typically used in projections.
 *
 * @author Abe White
 */
class GetObjectId
    extends AbstractVal {

    private static final Localizer _loc = Localizer.forPackage
        (GetObjectId.class);

    private final PCPath _path;
    private ClassMetaData _meta = null;

    /**
     * Constructor. Provide the value whose oid to extract.
     */
    public GetObjectId(PCPath path) {
        _path = path;
    }

    /**
     * Return the oid columns.
     */
    public Column[] getColumns(ExpState state) {
        return _path.getClassMapping(state).getPrimaryKeyColumns();
    }

    public ClassMetaData getMetaData() {
        return _meta;
    }

    public void setMetaData(ClassMetaData meta) {
        _meta = meta;
    }

    public Class getType() {
        return Object.class;
    }

    public void setImplicitType(Class type) {
    }

    public ExpState initialize(Select sel, ExpContext ctx, int flags) {
        ExpState state = _path.initialize(sel, ctx, JOIN_REL);

        // it's difficult to get calls on non-pc fields to always return null
        // without screwing up the SQL, to just don't let users call it on
        // non-pc fields at all
        ClassMapping cls = _path.getClassMapping(state);
        if (cls == null || cls.getEmbeddingMapping() != null)
            throw new UserException(_loc.get("bad-getobjectid",
                _path.getFieldMapping(state)));
        return state;
    }

    public Object toDataStoreValue(Select sel, ExpContext ctx, ExpState state, 
        Object val) {
        // if datastore identity, try to convert to a long value
        ClassMapping mapping = _path.getClassMapping(state);
        if (mapping.getIdentityType() == mapping.ID_DATASTORE) {
            if (val instanceof Id)
                return Numbers.valueOf(((Id) val).getId());
            return Filters.convert(val, long.class);
        }

        // if unknown identity, can't do much
        if (mapping.getIdentityType() == mapping.ID_UNKNOWN)
            return (val instanceof OpenJPAId) ?
                ((OpenJPAId) val).getIdObject() : val;

        // application identity; convert to pk values in the same order as
        // the mapping's primary key columns will be returned
        Object[] pks = ApplicationIds.toPKValues(val, mapping);
        if (pks.length == 1)
            return pks[0];
        if (val == null)
            return pks;
        while (!mapping.isPrimaryKeyObjectId(false))
            mapping = mapping.getJoinablePCSuperclassMapping();

        Column[] cols = mapping.getPrimaryKeyColumns();
        Object[] vals = new Object[cols.length];
        Joinable join;
        for (int i = 0; i < cols.length; i++) {
            join = mapping.assertJoinable(cols[i]);
            vals[i] = pks[mapping.getField(join.getFieldIndex()).
                getPrimaryKeyIndex()];
            vals[i] = join.getJoinValue(vals[i], cols[i], ctx.store);
        }
        return vals;
    }

    public void select(Select sel, ExpContext ctx, ExpState state, 
        boolean pks) {
        selectColumns(sel, ctx, state, true);
    }

    public void selectColumns(Select sel, ExpContext ctx, ExpState state, 
        boolean pks) {
        _path.selectColumns(sel, ctx, state, true);
    }

    public void groupBy(Select sel, ExpContext ctx, ExpState state) {
        _path.groupBy(sel, ctx, state);
    }

    public void orderBy(Select sel, ExpContext ctx, ExpState state, 
        boolean asc) {
        _path.orderBy(sel, ctx, state, asc);
    }

    public Object load(ExpContext ctx, ExpState state, Result res)
        throws SQLException {
        return _path.load(ctx, state, res, true);
    }

    public void calculateValue(Select sel, ExpContext ctx, ExpState state, 
        Val other, ExpState otherState) {
        _path.calculateValue(sel, ctx, state, null, null);
    }

    public int length(Select sel, ExpContext ctx, ExpState state) {
        return _path.length(sel, ctx, state);
    }

    public void appendTo(Select sel, ExpContext ctx, ExpState state, 
        SQLBuffer sql, int index) {
        _path.appendTo(sel, ctx, state, sql, index);
    }

    public void acceptVisit(ExpressionVisitor visitor) {
        visitor.enter(this);
        _path.acceptVisit(visitor);
        visitor.exit(this);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3776.java