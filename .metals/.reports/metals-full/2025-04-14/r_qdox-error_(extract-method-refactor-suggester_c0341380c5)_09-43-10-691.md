error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12122.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12122.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12122.java
text:
```scala
C@@riteriaQueryImpl<?> q) {

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
package org.apache.openjpa.persistence.criteria;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.apache.openjpa.kernel.exps.ExpressionFactory;
import org.apache.openjpa.persistence.meta.MetamodelImpl;

public class PredicateImpl extends ExpressionImpl<Boolean> 
implements Predicate {
    List<Expression<Boolean>> _exps;
    BooleanOperator _op;
    boolean _negated = false;

    protected PredicateImpl() {
        super(Boolean.class);
    }

    protected PredicateImpl(BooleanOperator op) {
        this();
        _op = op;
    }

    protected PredicateImpl(BooleanOperator op, Predicate...restrictions) {
        this(op);
        if (restrictions != null) {
            for (Predicate p : restrictions)
                add((PredicateImpl)p);
        }
    }

    public PredicateImpl add(Expression<Boolean> s) {
        if (_exps == null)
            _exps = new ArrayList<Expression<Boolean>>();
        _exps.add(s);
        return this;
    }

    public List<Expression<Boolean>> getExpressions() {
        return _exps;
    }

    public BooleanOperator getOperator() {
        return _op;
    }

    public boolean isNegated() {
        return _negated;
    }

    public PredicateImpl negate() {
        PredicateImpl not = clone();
        not._negated = true;
        return not;
    }

    public PredicateImpl clone() {
        PredicateImpl clone = new PredicateImpl(_op);
        if (_exps != null)
            clone._exps = new ArrayList<Expression<Boolean>>(this._exps);
        return clone;
    }

    @Override
    org.apache.openjpa.kernel.exps.Expression toKernelExpression(
        ExpressionFactory factory, MetamodelImpl model, 
        CriteriaQueryImpl q) {
        if (_exps == null || _exps.isEmpty())
            return factory.emptyExpression();
        if (_exps.size() == 1)
            return ((ExpressionImpl<?>)_exps.get(0))
            .toKernelExpression(factory, model, q);
        ExpressionImpl<?> e1 = (ExpressionImpl<?>)_exps.get(0);
        ExpressionImpl<?> e2 = (ExpressionImpl<?>)_exps.get(1);
        org.apache.openjpa.kernel.exps.Expression ke1 = 
            e1.toKernelExpression(factory, model, q);
        org.apache.openjpa.kernel.exps.Expression ke2 = 
            e2.toKernelExpression(factory, model, q);
        org.apache.openjpa.kernel.exps.Expression result = 
            _op == BooleanOperator.AND 
            ? factory.and(ke1,ke2) : factory.or(ke1, ke2);

            for (int i = 2; i < _exps.size(); i++) {
                ExpressionImpl<?> e = (ExpressionImpl<?>)_exps.get(i);
                result = _op == BooleanOperator.AND 
                ? factory.and(result, e.toKernelExpression(factory, model, q))
                : factory.or(result, e.toKernelExpression(factory,model,q));
            }
            return _negated ? factory.not(result) : result;
    }

    public static class And extends PredicateImpl {
        public And(Expression<Boolean> x, Expression<Boolean> y) {
            super(BooleanOperator.AND);
            add(x).add(y);
        }

        public And(Predicate...restrictions) {
            super(BooleanOperator.AND, restrictions);
        }
    }

    public static class Or extends PredicateImpl {
        public Or(Expression<Boolean> x, Expression<Boolean> y) {
            super(BooleanOperator.OR);
            add(x).add(y);
        }

        public Or(Predicate...restrictions) {
            super(BooleanOperator.OR, restrictions);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12122.java