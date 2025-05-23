error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7284.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7284.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7284.java
text:
```scala
r@@eturn new Expressions.Type<Class<? extends X>>(this);

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

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.Bindable;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;
import javax.persistence.metamodel.Type.PersistenceType;

import org.apache.openjpa.kernel.exps.ExpressionFactory;
import org.apache.openjpa.kernel.exps.Value;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.persistence.meta.Members;
import org.apache.openjpa.persistence.meta.MetamodelImpl;

/**
 * Represents a simple or compound attribute path from a 
 * bound type or collection, and is a "primitive" expression.
 * @param <X>  Type referenced by the path
 */
/**
 * Path is an expression often representing a persistent attribute traversed from another (parent) path.
 * The type of the path is the type of the persistent attribute.
 * If the persistent attribute is bindable, then further path can be travesered from this path. 
 * 
 * @author Pinaki Poddar
 * @author Fay Wang
 * 
 * @param <Z> the type of the parent path 
 * @param <X> the type of this path
 */
public class PathImpl<Z,X> extends ExpressionImpl<X> implements Path<X> {
    protected final PathImpl<?,Z> _parent;
    protected final Members.Member<? super Z,?> _member;
    private boolean isEmbedded = false;
    protected PathImpl<?,?> _correlatedPath;
    
    /**
     * Protected constructor use by root path which neither represent a member nor has a parent. 
     */
    protected PathImpl(Class<X> cls) {
        super(cls);
        _parent = null;
        _member = null;
    }
    
    /**
     * Create a path from the given non-null parent representing the given non-null member. The given class denotes 
     * the type expressed by this path.
     */
    public PathImpl(PathImpl<?,Z> parent, Members.Member<? super Z, ?> member, Class<X> cls) {
        super(cls);
        _parent = parent;
        if (_parent.isEmbedded) {
            FieldMetaData fmd = getEmbeddedFieldMetaData(member.fmd);
            _member = new Members.SingularAttributeImpl(member.owner, fmd);
        } else {
            _member = member;
        }
        isEmbedded = _member.fmd.isElementCollection() ? _member.fmd.getElement().isEmbedded() : 
            _member.fmd.isEmbedded();
    }

    /** 
     * Returns the bindable object that corresponds to the path expression.
     *  
     */
    public Bindable<X> getModel() { 
        if (_member instanceof Bindable<?> == false) {
            throw new IllegalArgumentException(this + " represents a basic path and not a bindable");
        }
        return (Bindable<X>)_member;
    }
    
    /**
     *  Return the parent "node" in the path or null if no parent.
     */
    public Path<Z> getParentPath() {
        return _parent;
    }
    
    /**
     * Gets the path that originates this traversal. Can be itself if this itself is the origin.
     */
    public PathImpl<?,?> getInnermostParentPath() {
        return (_parent == null) ? this : _parent.getInnermostParentPath();
    }

    protected FieldMetaData getEmbeddedFieldMetaData(FieldMetaData fmd) {
        Members.Member<?,?> member = getInnermostMember(_parent,_member);
        ClassMetaData embeddedMeta = member.fmd.isElementCollection() ? 
                member.fmd.getElement().getEmbeddedMetaData() :
                member.fmd.getEmbeddedMetaData();
        if (embeddedMeta != null)
            return embeddedMeta.getField(fmd.getName());
        else
            return fmd;
    }
    
    protected Members.Member<?,?> getInnermostMember(PathImpl<?,?> parent, Members.Member<?,?> member) {
        return member != null ? member : getInnermostMember(parent._parent,  parent._member); 
    }
    
    public void setCorrelatedPath(PathImpl<?,?> correlatedPath) {
        _correlatedPath = correlatedPath;
    }
    
    public PathImpl<?,?> getCorrelatedPath() {
        return _correlatedPath;
    }
    
    /**
     * Convert this path to a kernel path.
     */
    @Override
    public Value toValue(ExpressionFactory factory, MetamodelImpl model,  CriteriaQueryImpl<?> q) {
        if (q.isRegistered(this))
            return q.getValue(this);
        org.apache.openjpa.kernel.exps.Path path = null;
        SubqueryImpl<?> subquery = q.getDelegator();
        boolean allowNull = _parent == null ? false : _parent instanceof Join 
            && ((Join<?,?>)_parent).getJoinType() != JoinType.INNER;
        PathImpl<?,?> corrJoin = getCorrelatedJoin(this);
        PathImpl<?,?> corrRoot = getCorrelatedRoot(subquery);
        if (_parent != null && q.isRegistered(_parent)) {
            path = factory.newPath(q.getRegisteredVariable(_parent));
            path.setSchemaAlias(q.getAlias(_parent));
            path.get(_member.fmd, allowNull);
        } else if (corrJoin != null || corrRoot != null) {
            org.apache.openjpa.kernel.exps.Subquery subQ = subquery.getSubQ();
            path = factory.newPath(subQ);
            path.setMetaData(subQ.getMetaData());
            path.setSchemaAlias(q.getAlias(_parent));
            traversePath(_parent, path, _member.fmd);
        } else if (_parent != null) {
            path = (org.apache.openjpa.kernel.exps.Path)_parent.toValue(factory, model, q);
            path.get(_member.fmd, allowNull);
        } else if (_parent == null) {
            path = factory.newPath();
            path.setMetaData(model.repos.getCachedMetaData(getJavaType()));
        }
        if (_member != null && !_member.isCollection()) {
            path.setImplicitType(getJavaType());
        }
        path.setAlias(q.getAlias(this));
        return path;
    }
    
    public PathImpl<?,?> getCorrelatedRoot(SubqueryImpl<?> subquery) {
        if (subquery == null)
            return null;
        PathImpl<?,?> root = getInnermostParentPath();
        if (subquery.getRoots() != null && subquery.getRoots().contains(this))
            return root;
        return null;
    }
    
    
    public PathImpl<?,?> getCorrelatedJoin(PathImpl<?,?> path) {
        if (path._correlatedPath != null)
            return path._correlatedPath;
        if (path._parent == null)
            return null;
        return getCorrelatedJoin(path._parent);
    }
    
    /**
     * Affirms if this receiver occurs in the roots of the given subquery.
     */
    public boolean inSubquery(SubqueryImpl<?> subquery) {
        return subquery != null && (subquery.getRoots() == null ? false : subquery.getRoots().contains(this));
    }
    
    protected void traversePath(PathImpl<?,?> parent,  org.apache.openjpa.kernel.exps.Path path, FieldMetaData fmd) {
        boolean allowNull = parent == null ? false : parent instanceof Join 
            && ((Join<?,?>)parent).getJoinType() != JoinType.INNER;
        FieldMetaData fmd1 = parent._member == null ? null : parent._member.fmd;
        PathImpl<?,?> parent1 = parent._parent;
        if (parent1 == null || parent1.getCorrelatedPath() != null) {
            if (fmd != null) 
                path.get(fmd, allowNull);
            return;
        }
        traversePath(parent1, path, fmd1);
        if (fmd != null) 
            path.get(fmd, allowNull);
    }
    
    /**
     *  Gets a new path that represents the given single-valued attribute from this path.
     */
    public <Y> Path<Y> get(SingularAttribute<? super X, Y> attr) {
        return new PathImpl<X,Y>(this, (Members.SingularAttributeImpl<? super X, Y>)attr, attr.getJavaType());
    }
    
    /**
     *  Gets a new path that represents the given multi-valued attribute from this path.
     */
    public <E, C extends java.util.Collection<E>> Expression<C>  get(PluralAttribute<X, C, E> coll) {
        return new PathImpl<X,C>(this, (Members.PluralAttributeImpl<? super X, C, E>)coll, coll.getJavaType());
    }

    /**
     *  Gets a new path that represents the given map-valued attribute from this path.
     */
    public <K, V, M extends java.util.Map<K, V>> Expression<M> get(MapAttribute<X, K, V> map) {
        return new PathImpl<X,M>(this, (Members.MapAttributeImpl<? super X,K,V>)map, (Class<M>)map.getJavaType());
    }
    
    /**
     * Gets a new path that represents the attribute of the given name from this path.
     * 
     * @exception IllegalArgumentException if this path represents a basic attribute that is can not be traversed 
     * further.
     */
    public <Y> Path<Y> get(String attName) {
        Type<?> type = this.getType();
        if (type.getPersistenceType() == PersistenceType.BASIC) {
            throw new IllegalArgumentException(this + " is a basic path and can not be navigated to " + attName);
        }
        
        Members.Member<? super X, Y> next = (Members.Member<? super X, Y>) 
           ((ManagedType<? super X>)type).getAttribute(attName);
        return new PathImpl<X,Y>(this, next, next.getJavaType());
    }
    
    public Type<?> getType() {
        return _member.getType();
    }
    
    /**
     * Get the type() expression corresponding to this path. 
     */
    public Expression<Class<? extends X>> type() {
        return new Expressions.Type<X>(this);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7284.java