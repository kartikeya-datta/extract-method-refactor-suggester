error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12717.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12717.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12717.java
text:
```scala
s@@el.groupBy(newSQLBuffer(sel, store, params, fetch));

/*
 * Copyright 2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openjpa.jdbc.kernel.exps;

import java.sql.SQLException;
import java.util.Map;

import org.apache.openjpa.jdbc.kernel.JDBCFetchConfiguration;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.JavaSQLTypes;
import org.apache.openjpa.jdbc.sql.Joins;
import org.apache.openjpa.jdbc.sql.Result;
import org.apache.openjpa.jdbc.sql.SQLBuffer;
import org.apache.openjpa.jdbc.sql.Select;
import org.apache.openjpa.kernel.Filters;
import org.apache.openjpa.meta.ClassMetaData;

/**
 * Filter listener that evaluates to a value.
 *
 * @author Abe White
 */
class Extension
    extends AbstractVal
    implements Val, Exp {

    private final JDBCFilterListener _listener;
    private final Val _target;
    private final Val _arg;
    private final ClassMapping _candidate;
    private Joins _joins = null;
    private ClassMetaData _meta = null;
    private Class _cast = null;

    /**
     * Constructor.
     */
    public Extension(JDBCFilterListener listener, Val target,
        Val arg, ClassMapping candidate) {
        _listener = listener;
        _target = target;
        _arg = arg;
        _candidate = candidate;
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
        Class targetClass = (_target == null) ? null : _target.getType();
        return _listener.getType(targetClass, getArgTypes());
    }

    private Class[] getArgTypes() {
        if (_arg == null)
            return null;
        if (_arg instanceof Args)
            return ((Args) _arg).getTypes();
        return new Class[]{ _arg.getType() };
    }

    public void setImplicitType(Class type) {
        _cast = type;
    }

    public void initialize(Select sel, JDBCStore store, boolean nullTest) {
        // note that we tell targets and args to extensions that are sql
        // paths to go ahead and join to their related object (if any),
        // because we assume that, unlike most operations, if a relation
        // field like a 1-1 is given as the target of an extension, then
        // the extension probably acts on some field or column in the
        // related object, not the 1-1 field itself
        Joins j1 = null;
        Joins j2 = null;
        if (_target != null) {
            _target.initialize(sel, store, false);
            if (_target instanceof PCPath)
                ((PCPath) _target).joinRelation();
            j1 = _target.getJoins();
        }
        if (_arg != null) {
            _arg.initialize(sel, store, false);
            if (_arg instanceof PCPath)
                ((PCPath) _arg).joinRelation();
            j2 = _arg.getJoins();
        }
        _joins = sel.and(j1, j2);
    }

    public Joins getJoins() {
        return _joins;
    }

    public Object toDataStoreValue(Object val, JDBCStore store) {
        return val;
    }

    public void select(Select sel, JDBCStore store, Object[] params,
        boolean pks, JDBCFetchConfiguration fetch) {
        sel.select(newSQLBuffer(sel, store, params, fetch), this);
    }

    public void selectColumns(Select sel, JDBCStore store,
        Object[] params, boolean pks, JDBCFetchConfiguration fetch) {
        if (_target != null)
            _target.selectColumns(sel, store, params, true, fetch);
        if (_arg != null)
            _arg.selectColumns(sel, store, params, true, fetch);
    }

    public void groupBy(Select sel, JDBCStore store, Object[] params,
        JDBCFetchConfiguration fetch) {
        sel.groupBy(newSQLBuffer(sel, store, params, fetch), false);
    }

    public void orderBy(Select sel, JDBCStore store, Object[] params,
        boolean asc, JDBCFetchConfiguration fetch) {
        sel.orderBy(newSQLBuffer(sel, store, params, fetch), asc, false);
    }

    private SQLBuffer newSQLBuffer(Select sel, JDBCStore store,
        Object[] params, JDBCFetchConfiguration fetch) {
        calculateValue(sel, store, params, null, fetch);
        SQLBuffer buf = new SQLBuffer(store.getDBDictionary());
        appendTo(buf, 0, sel, store, params, fetch);
        clearParameters();
        return buf;
    }

    public Object load(Result res, JDBCStore store,
        JDBCFetchConfiguration fetch)
        throws SQLException {
        return Filters.convert(res.getObject(this,
            JavaSQLTypes.JDBC_DEFAULT, null), getType());
    }

    public boolean hasVariable(Variable var) {
        return (_target != null && _target.hasVariable(var))
 (_arg != null && _arg.hasVariable(var));
    }

    public void calculateValue(Select sel, JDBCStore store,
        Object[] params, Val other, JDBCFetchConfiguration fetch) {
        if (_target != null)
            _target.calculateValue(sel, store, params, null, fetch);
        if (_arg != null)
            _arg.calculateValue(sel, store, params, null, fetch);
    }

    public void clearParameters() {
        if (_target != null)
            _target.clearParameters();
        if (_arg != null)
            _arg.clearParameters();
    }

    public int length() {
        return 1;
    }

    public void appendTo(SQLBuffer sql, int index, Select sel,
        JDBCStore store, Object[] params, JDBCFetchConfiguration fetch) {
        FilterValue target = (_target == null) ? null
            : new FilterValueImpl(_target, sel, store, params, fetch);
        _listener.appendTo(sql, target, getArgs(sel, store, params, fetch), 
            _candidate, store);
        sel.append(sql, _joins);
    }

    private FilterValue[] getArgs(Select sel, JDBCStore store,
        Object[] params, JDBCFetchConfiguration fetch) {
        if (_arg == null)
            return null;
        if (_arg instanceof Args) {
            Val[] vals = ((Args) _arg).getVals();
            FilterValue[] filts = new FilterValue[vals.length];
            for (int i = 0; i < vals.length; i++)
                filts[i] = new FilterValueImpl(vals[i], sel, store, params,
                    fetch);
            return filts;
        }
        return new FilterValue[]{
            new FilterValueImpl(_arg, sel, store, params, fetch)
        };
    }

    //////////////////////
    // Exp implementation
    //////////////////////

    public void initialize(Select sel, JDBCStore store,
        Object[] params, Map contains) {
        initialize(sel, store, false);
    }

    public void appendTo(SQLBuffer sql, Select sel, JDBCStore store,
        Object[] params, JDBCFetchConfiguration fetch) {
        calculateValue(sel, store, params, null, fetch);
        appendTo(sql, 0, sel, store, params, fetch);
        sel.append(sql, getJoins());
        clearParameters();
    }

    public boolean hasContainsExpression() {
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12717.java