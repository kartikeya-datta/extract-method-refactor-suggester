error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6160.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6160.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6160.java
text:
```scala
s@@el.orderBy(newSQLBuffer(sel, ctx, state), asc, false, getSelectAs());

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

import java.lang.Math;
import java.sql.SQLException;

import org.apache.openjpa.jdbc.meta.JavaSQLTypes;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.jdbc.sql.Joins;
import org.apache.openjpa.jdbc.sql.Result;
import org.apache.openjpa.jdbc.sql.SQLBuffer;
import org.apache.openjpa.jdbc.sql.Select;
import org.apache.openjpa.kernel.Filters;
import org.apache.openjpa.kernel.exps.ExpressionVisitor;
import org.apache.openjpa.meta.ClassMetaData;

/**
 * Returns the number of characters in a string.
 *
 * @author Marc Prud'hommeaux
 */
public class Trim
    extends AbstractVal {

    private final Val _val;
    private final Val _trimChar;
    private final Boolean _where;
    private ClassMetaData _meta = null;

    /**
     * Constructor. Provide the string to operate on.
     */
    public Trim(Val val, Val trimChar, Boolean where) {
        _val = val;
        _trimChar = trimChar;
        _where = where;
    }

    public Val getVal() {
        return _val;
    }

    public Val getTrimChar() {
        return _trimChar;
    }

    public Boolean getWhere(){
        return _where;
    }

    public ClassMetaData getMetaData() {
        return _meta;
    }

    public void setMetaData(ClassMetaData meta) {
        _meta = meta;
    }

    public Class getType() {
        return String.class;
    }

    public void setImplicitType(Class type) {
    }

    public ExpState initialize(Select sel, ExpContext ctx, int flags) {
        ExpState valueState =  _val.initialize(sel, ctx, 0);
        ExpState charState = _trimChar.initialize(sel, ctx, 0);
        return new TrimExpState(sel.and(valueState.joins, charState.joins), 
            valueState, charState);
    }

    /**
     * Expression state.
     */
    private static class TrimExpState
        extends ExpState {

        public final ExpState valueState;
        public final ExpState charState;

        public TrimExpState(Joins joins, ExpState valueState, 
            ExpState charState) {
            super(joins);
            this.valueState = valueState;
            this.charState = charState;
        }
    }

    public void select(Select sel, ExpContext ctx, ExpState state, 
        boolean pks) {
        sel.select(newSQLBuffer(sel, ctx, state), this);
    }

    public void selectColumns(Select sel, ExpContext ctx, ExpState state, 
        boolean pks) {
        TrimExpState tstate = (TrimExpState) state;
        _val.selectColumns(sel, ctx, tstate.valueState, true);
        _trimChar.selectColumns(sel, ctx, tstate.charState, true);
    }

    public void groupBy(Select sel, ExpContext ctx, ExpState state) {
        sel.groupBy(newSQLBuffer(sel, ctx, state));
    }

    public void orderBy(Select sel, ExpContext ctx, ExpState state, 
        boolean asc) {
        sel.orderBy(newSQLBuffer(sel, ctx, state), asc, false);
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
        TrimExpState tstate = (TrimExpState) state;
        _val.calculateValue(sel, ctx, tstate.valueState, null, null);
        _trimChar.calculateValue(sel, ctx, tstate.charState, null, null);
    }

    public int length(Select sel, ExpContext ctx, ExpState state) {
        return 1;
    }

    public void appendTo(Select sel, ExpContext ctx, ExpState state, 
        SQLBuffer sql, int index) {
        DBDictionary dict = ctx.store.getDBDictionary();
        String func;
        if (_where == null) {
            func = dict.trimBothFunction;
            dict.assertSupport(func != null, "TrimBothFunction");
        } else if (_where.booleanValue()) {
            func = dict.trimLeadingFunction;
            dict.assertSupport(func != null, "TrimLeadingFunction");
        } else {
            func = dict.trimTrailingFunction;
            dict.assertSupport(func != null, "TrimTrailingFunction");
        }        
        func = dict.getCastFunction(_val, func);
        
        int fromPart = func.indexOf("{0}");
        int charPart = func.indexOf("{1}");
        if (charPart == -1)
            charPart = func.length();
        String part1 = func.substring(0, Math.min(fromPart, charPart));
        String part2 = func.substring(Math.min(fromPart, charPart) + 3,
            Math.max(fromPart, charPart));
        String part3 = null;
        if (charPart != func.length())
            part3 = func.substring(Math.max(fromPart, charPart) + 3);

        TrimExpState tstate = (TrimExpState) state;
        sql.append(part1);
        if (fromPart < charPart)
            _val.appendTo(sel, ctx, tstate.valueState, sql, 0);
        else 
            _trimChar.appendTo(sel, ctx, tstate.charState, sql, 0);
        sql.append(part2);

        if (charPart != func.length()) {
            if (fromPart > charPart)
                _val.appendTo(sel, ctx, tstate.valueState, sql, 0);
            else
                _trimChar.appendTo(sel, ctx, tstate.charState, sql, 0);
            sql.append(part3);
        } else {
            // since the trim statement did not specify the token for
            // where to specify the trim char (denoted by "{1}"),
            // we do not have the ability to trim off non-whitespace
            // characters; throw an exception when we attempt to do so
            if (!(_trimChar instanceof Const) || String.valueOf(((Const) 
                _trimChar).getValue(ctx,tstate.charState)).trim().length() != 0)
                dict.assertSupport(false, "TrimNonWhitespaceCharacters");
        }
    }

    public void acceptVisit(ExpressionVisitor visitor) {
        visitor.enter(this);
        _val.acceptVisit(visitor);
        _trimChar.acceptVisit(visitor);
        visitor.exit(this);
    }

    public int getId() {
        return Val.TRIM_VAL;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6160.java