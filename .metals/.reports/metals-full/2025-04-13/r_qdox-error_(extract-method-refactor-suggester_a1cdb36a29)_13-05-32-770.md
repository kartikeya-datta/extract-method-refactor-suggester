error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13731.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13731.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13731.java
text:
```scala
b@@state.state2, other, otherState);

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

import org.apache.openjpa.jdbc.meta.JavaSQLTypes;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.jdbc.sql.Joins;
import org.apache.openjpa.jdbc.sql.Raw;
import org.apache.openjpa.jdbc.sql.Result;
import org.apache.openjpa.jdbc.sql.SQLBuffer;
import org.apache.openjpa.jdbc.sql.Select;
import org.apache.openjpa.kernel.Filters;
import org.apache.openjpa.kernel.exps.ExpressionVisitor;
import org.apache.openjpa.meta.ClassMetaData;

/**
 * Simple case expression.
 *
 * @author Catalina Wei
 */
public class SimpleCaseExpression
    extends AbstractVal {

    private final Val _caseOperand;
    private final Exp[] _exp;
    private final Val _val;
    private ClassMetaData _meta = null;
    private Class _cast = null;

    /**
     * Constructor.
     */
    public SimpleCaseExpression(Val caseOperand, Exp[] exp, Val val) {
        _caseOperand = caseOperand;
        _exp = exp;
        _val = val;
    }

    public Val getCaseOperand() {
        return _caseOperand;
    }

    public Exp[] getExp() {
        return _exp;
    }

    public Val getVal() {
        return _val;
    }

    public Class getType() {
        if (_cast != null)
            return _cast;
        Class type = _val.getType();
        for (int i = 0; i < _exp.length; i++)
            type = Filters.promote(type,
                ((WhenScalar) _exp[i]).getVal2().getType());
        if (type == Raw.class)
            return String.class;
        return type;
    }

    public ExpState initialize(Select sel, ExpContext ctx, int flags) {
        ExpState[] states = new ExpState[_exp.length+2];
        Joins joins = null;

        states[0] = _caseOperand.initialize(sel, ctx, 0);
        if (joins == null)
            joins = states[0].joins;
        else
            joins = sel.and(joins, states[0].joins);
        for (int i = 0; i < _exp.length; i++) {
            states[i+1] = _exp[i].initialize(sel, ctx, null);
            if (joins == null)
                joins = states[i+1].joins;
            else
                joins = sel.and(joins, states[i+1].joins);
        }
        states[_exp.length+1] = _val.initialize(sel, ctx, 0);
        if (joins == null)
            joins = states[_exp.length+1].joins;
        else
            joins = sel.and(joins, states[_exp.length+1].joins);
        return new SimpleCaseExpState(joins, states);
    }

    private static class SimpleCaseExpState
        extends ExpState {
        
        public ExpState[] states;
        
        public SimpleCaseExpState(Joins joins, ExpState[] states) {
            super(joins);
            this.states = states;
        }
    }

    public void appendTo(Select sel, ExpContext ctx, ExpState state,
        SQLBuffer buf, int index) {
        SimpleCaseExpState cstate = (SimpleCaseExpState) state;

        DBDictionary dict = ctx.store.getDBDictionary();

        buf.append(" CASE ");

        for (int i = 0; i < _exp.length; i++) {
            // if back-end does not support simple case expression,
            // pushdown sql as general case expression.

            if (!dict.supportsSimpleCaseExpression)
                buf.append(" WHEN ");

            if (i == 0 || !dict.supportsSimpleCaseExpression)
                _caseOperand.appendTo(sel, ctx, cstate.states[0], buf, 0);

            if (!dict.supportsSimpleCaseExpression)
                buf.append(" = ");
            else
                buf.append(" WHEN ");

            _exp[i].appendTo(sel, ctx, cstate.states[i+1], buf);
        }
        buf.append(" ELSE ");
        _val.appendTo(sel, ctx, cstate.states[_exp.length+1], buf, 0);

        buf.append(" END ");
    }

    public void selectColumns(Select sel, ExpContext ctx, ExpState state,
        boolean pks) {
        SimpleCaseExpState cstate = (SimpleCaseExpState) state;
        
        _caseOperand.selectColumns(sel, ctx, cstate.states[0], pks);
        for (int i = 0; i < _exp.length; i++)
            _exp[i].selectColumns(sel, ctx, cstate.states[i+1], pks);
        _val.selectColumns(sel, ctx, cstate.states[_exp.length+1], pks);
    }

    public void acceptVisit(ExpressionVisitor visitor) {
        visitor.enter(this);
        _caseOperand.acceptVisit(visitor);
        for (int i = 0; i < _exp.length; i++)
            _exp[i].acceptVisit(visitor);
        _val.acceptVisit(visitor);
        visitor.exit(this);
    }

    public int getId() {
        return Val.SIMPLECASE_VAL;
    }

    public void calculateValue(Select sel, ExpContext ctx, ExpState state,
        Val other, ExpState otherState) {
        SimpleCaseExpState cstate = (SimpleCaseExpState) state;
        _caseOperand.calculateValue(sel, ctx, cstate.states[0], other,
            otherState);
        for (int i = 0; i < _exp.length; i++) {
            BinaryOpExpState bstate = (BinaryOpExpState) cstate.states[i+1];
            ((WhenScalar) _exp[i]).getVal1().calculateValue(sel, ctx,
                bstate.state1, null, null);
            ((WhenScalar) _exp[i]).getVal2().calculateValue(sel, ctx,
                bstate.state2, null, null);
        }
        _val.calculateValue(sel, ctx, cstate.states[_exp.length+1], other,
            otherState);
    }

    public void groupBy(Select sel, ExpContext ctx, ExpState state) {
        sel.groupBy(newSQLBuffer(sel, ctx, state));
    }

    public int length(Select sel, ExpContext ctx, ExpState state) {
        return 1;
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

    public void orderBy(Select sel, ExpContext ctx, ExpState state,
        boolean asc) {
        sel.orderBy(newSQLBuffer(sel, ctx, state), asc, false, getSelectAs());
    }

    public void select(Select sel, ExpContext ctx, ExpState state, boolean pks){
        sel.select(newSQLBuffer(sel, ctx, state), this);
    }

    public ClassMetaData getMetaData() {
        return _meta;
    }

    public void setImplicitType(Class type) {
        _cast = type;        
    }

    public void setMetaData(ClassMetaData meta) {
        _meta = meta;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13731.java