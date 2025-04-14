error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17498.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17498.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17498.java
text:
```scala
p@@state.discValue[i] = pstate.disc[i] != null ?

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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.Discriminator;
import org.apache.openjpa.jdbc.sql.SQLBuffer;
import org.apache.openjpa.jdbc.sql.Select;
import org.apache.openjpa.kernel.Filters;
import org.apache.openjpa.kernel.exps.Parameter;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.util.ImplHelper;

/**
 * A collection-valued input parameter in an in-expression.
 *
 * @author Catalina Wei
 */
public class CollectionParam
    extends Const
    implements Parameter {
    private static final Localizer _loc = Localizer.forPackage(
        CollectionParam.class);

    private final Object _key;
    private Class _type = null;
    private int _idx = -1;
    private boolean _container = false;

    /**
     * Constructor. Supply parameter name and type.
     */
    public CollectionParam(Object key, Class type) {
        _key = key;
        setImplicitType(type);
    }

    public Object getParameterKey() {
        return _key;
    }

    public Class getType() {
        return _type;
    }

    public void setImplicitType(Class type) {
        _type = type;
        _container = (getMetaData() == null || !ImplHelper.isManagedType(
            getMetaData().getRepository().getConfiguration(), type))
            && (Collection.class.isAssignableFrom(type)
 Map.class.isAssignableFrom(type));
    }

    public int getIndex() {
        return _idx;
    }

    public void setIndex(int idx) {
        _idx = idx;
    }

    public Object getValue(Object[] params) {
        return Filters.convert(params[_idx], getType());
    }

    public Object getValue(ExpContext ctx, ExpState state) {
        ParamExpState pstate = (ParamExpState) state;
        if (pstate.discValue[0] != null)
            return Arrays.asList(pstate.discValue);
        else
            return getValue(ctx.params);
    }

    public Object getSQLValue(Select sel, ExpContext ctx, ExpState state) {
        return ((ParamExpState) state).sqlValue;
    }

    public ExpState initialize(Select sel, ExpContext ctx, int flags) {
        return new ParamExpState(ctx.params[_idx]);
    }

    /**
     * Expression state.
     */
    public static class ParamExpState
        extends ConstExpState {

        public int size = 0;
        public Object[] sqlValue = null;
        public int[] otherLength;
        public ClassMapping[] mapping = null;
        public Discriminator[] disc = null;
        public Object discValue[] = null;
        
        ParamExpState(Object params) {
            if (params instanceof Collection)
                size = ((Collection) params).size();
            sqlValue = new Object[size];
            otherLength = new int[size];
            mapping = new ClassMapping[size];
            disc = new Discriminator[size];
            discValue = new Object[size];
            for (int i = 0; i < size; i++) {
                sqlValue[i] = null;
                otherLength[i] = 1;
                mapping[i] = null;
                disc[i] = null;
                discValue[i] = null;
            }
        }
    } 

    public void calculateValue(Select sel, ExpContext ctx, ExpState state, 
        Val other, ExpState otherState) {
        super.calculateValue(sel, ctx, state, other, otherState);
        ParamExpState pstate = (ParamExpState) state;
        Object value = getValue(ctx.params);

        if (!(value instanceof Collection))
            throw new IllegalArgumentException(_loc.get(
                "not-collection-parm", _key).toString());

        if (((Collection) value).isEmpty())
            throw new IllegalArgumentException(_loc.get(
                "empty-collection-parm", _key).toString());

        Iterator itr = ((Collection) value).iterator();
        for (int i = 0; i < pstate.size && itr.hasNext(); i++) {
            Object val = itr.next();
            if (other != null && !_container) {
                pstate.sqlValue[i] = other.toDataStoreValue(sel, ctx,
                    otherState, val);
                pstate.otherLength[i] = other.length(sel, ctx, otherState);
                if (other instanceof Type) {
                    pstate.mapping[i] = ctx.store.getConfiguration().
                    getMappingRepositoryInstance().getMapping((Class) val,
                        ctx.store.getContext().getClassLoader(), true);
                    pstate.disc[i] = pstate.mapping[i].getDiscriminator();
                    pstate.discValue[i] = pstate.disc != null ?
                        pstate.disc[i].getValue() : null;
                }
            } else if (ImplHelper.isManageable(val)) {
                ClassMapping mapping = ctx.store.getConfiguration().
                getMappingRepositoryInstance().getMapping(val.getClass(),
                    ctx.store.getContext().getClassLoader(), true);
                pstate.sqlValue[i] = mapping.toDataStoreValue(val,
                    mapping.getPrimaryKeyColumns(), ctx.store);
                pstate.otherLength[i] = mapping.getPrimaryKeyColumns().length;
            } else
                pstate.sqlValue[i] = val;
        }
    }

    public void appendTo(Select sel, ExpContext ctx, ExpState state, 
        SQLBuffer sql, int index) {
        ParamExpState pstate = (ParamExpState) state;
        for (int i = 0; i < pstate.size; i++) {
            if (pstate.otherLength[i] > 1)
                sql.appendValue(((Object[]) pstate.sqlValue[i])[index], 
                        pstate.getColumn(index), this);
            else if (pstate.cols != null)
                sql.appendValue(pstate.sqlValue[i], pstate.getColumn(index),
                        this);
            else if (pstate.discValue[i] != null)
                sql.appendValue(pstate.discValue[i]);
            else
                sql.appendValue(pstate.sqlValue[i], pstate.getColumn(index),
                        this);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17498.java