error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/876.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/876.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/876.java
text:
```scala
F@@ield[] mfields = mcls.getDeclaredFields();

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
package org.apache.openjpa.persistence.meta;

import static javax.persistence.metamodel.Type.PersistenceType.BASIC;
import static javax.persistence.metamodel.Type.PersistenceType.EMBEDDABLE;
import static javax.persistence.metamodel.Type.PersistenceType.ENTITY;
import static 
 javax.persistence.metamodel.Type.PersistenceType.MAPPED_SUPERCLASS;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.MappedSuperclassType;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.Type;
import javax.persistence.metamodel.StaticMetamodel;
import javax.persistence.metamodel.PluralAttribute.CollectionType;
import javax.persistence.metamodel.Type.PersistenceType;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.kernel.QueryContext;
import org.apache.openjpa.kernel.exps.AggregateListener;
import org.apache.openjpa.kernel.exps.FilterListener;
import org.apache.openjpa.kernel.exps.Resolver;
import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.meta.MetaDataRepository;
import org.apache.openjpa.persistence.meta.Members.Member;
import org.apache.openjpa.util.InternalException;

/**
 * Adapts JPA Metamodel to OpenJPA meta-data repository.
 * 
 * @author Pinaki Poddar
 * 
 */
public class MetamodelImpl implements Metamodel, Resolver {
    public final MetaDataRepository repos;
    private Map<Class<?>, Type<?>> _basics = new HashMap<Class<?>, Type<?>>();
    private Map<Class<?>, EntityType<?>> _entities = new HashMap<Class<?>, EntityType<?>>();
    private Map<Class<?>, EmbeddableType<?>> _embeddables 
        = new HashMap<Class<?>, EmbeddableType<?>>();
    private Map<Class<?>, MappedSuperclassType<?>> _mappedsupers 
        = new HashMap<Class<?>, MappedSuperclassType<?>>();

    private static Localizer _loc = Localizer.forPackage(MetamodelImpl.class);

    /**
     * Constructs a model with the current content of the supplied non-null repository.
     * 
     */
    public MetamodelImpl(MetaDataRepository repos) {
        this.repos = repos;
        Collection<Class<?>> classes = repos.loadPersistentTypes(true, null);
        for (Class<?> cls : classes) {
        	ClassMetaData meta = repos.getMetaData(cls, null, true);
            PersistenceType type = getPersistenceType(meta);
            switch (type) {
            case ENTITY:
                find(cls, _entities, ENTITY);
                if (meta.isEmbeddable())
                    find(cls, _embeddables, EMBEDDABLE);
                break;
            case EMBEDDABLE:
                find(cls, _embeddables, EMBEDDABLE);
                break;
            case MAPPED_SUPERCLASS:
                find(cls, _mappedsupers, MAPPED_SUPERCLASS);
                break;
            default:
            }
        }
    }

    /**
     *  Return the metamodel embeddable type representing the embeddable class.
     *  
     *  @param cls  the type of the represented embeddable class
     *  @return the metamodel embeddable type
     *  @throws IllegalArgumentException if not an embeddable class
     */
    public <X> EmbeddableType<X> embeddable(Class<X> clazz) {
        return (EmbeddableType<X>)find(clazz, _embeddables, EMBEDDABLE);
    }

    /**
     *  Return the metamodel entity type representing the entity.
     *  @param cls  the type of the represented entity
     *  @return the metamodel entity type
     *  @throws IllegalArgumentException if not an entity
     */
    public <X> EntityType<X> entity(Class<X> clazz) {
        return (EntityType<X>) find(clazz, _entities, ENTITY);
    }

    /**
     * Return the metamodel embeddable types.
     * @return the metamodel embeddable types
     */
    public Set<EmbeddableType<?>> getEmbeddables() {
        return unmodifiableSet(_embeddables.values());
    }

    /**
     * Return the metamodel entity types.
     * @return the metamodel entity types
     */
    public Set<EntityType<?>> getEntities() {
        return unmodifiableSet(_entities.values());
    }

    /**
     *  Return the metamodel managed types.
     *  @return the metamodel managed types
     */
    public Set<ManagedType<?>> getManagedTypes() {
        Set<ManagedType<?>> result = new HashSet<ManagedType<?>>();
        result.addAll(_entities.values());
        result.addAll(_embeddables.values());
        result.addAll(_mappedsupers.values());
        return result;
    }
    
    /**
     *  Return the metamodel managed type representing the 
     *  entity, mapped superclass, or embeddable class.
     *  @param cls  the type of the represented managed class
     *  @return the metamodel managed type
     *  @throws IllegalArgumentException if not a managed class
     */
    public <X> ManagedType<X> type(Class<X> clazz) {
        if (_entities.containsKey(clazz))
            return (EntityType<X>) _entities.get(clazz);
        if (_embeddables.containsKey(clazz))
            return (EmbeddableType<X>) _embeddables.get(clazz);
        if (_mappedsupers.containsKey(clazz))
            return (MappedSuperclassType<X>) _mappedsupers.get(clazz);
        throw new IllegalArgumentException(_loc.get("type-not-managed", clazz)
            .getMessage());
    }

    /**
     *  Return the type representing the basic, 
     *  entity, mapped superclass, or embeddable class.
     *  @param cls  the type of the represented managed class
     *  @return the metamodel managed type
     *  @throws IllegalArgumentException if not a managed class
     */
    public <X> Type<X> getType(Class<X> cls) {
        try {
            return type(cls);
        } catch (IllegalArgumentException ex) {
            if (_basics.containsKey(cls))
                return (Type<X>)_basics.get(cls);
            Type<X> basic = new Types.Basic<X>(cls);
            _basics.put(cls, basic);
            return basic;
        }
    }

    public static PersistenceType getPersistenceType(ClassMetaData meta) {
        if (meta == null)
            return BASIC;
        if (meta.isAbstract())
            return MAPPED_SUPERCLASS;
        if (meta.isEmbeddable())
            return EMBEDDABLE;
        return ENTITY;
    }

    /**
     * Looks up the given container for the managed type representing the given Java class.
     * The managed type may become instantiated as a side-effect.
     */
    private <V extends ManagedType<?>> V find(Class<?> cls, Map<Class<?>,V> container,  
            PersistenceType expected) {
        if (container.containsKey(cls))
            return container.get(cls);
        ClassMetaData meta = repos.getMetaData(cls, null, false);
        if (meta != null) {
            instantiate(cls, meta, container, expected);
        }
        return container.get(cls);
    }

    /**
     * Instantiate
     * @param <X>
     * @param <V>
     * @param cls
     * @param container
     * @param expected
     */
    private <X,V extends ManagedType<?>> void instantiate(Class<X> cls, ClassMetaData meta, 
            Map<Class<?>,V> container, PersistenceType expected) {
        PersistenceType actual = getPersistenceType(meta);
        if (actual != expected) {
            if (!meta.isEmbeddable() || actual != PersistenceType.ENTITY ||
                expected != PersistenceType.EMBEDDABLE) 
                throw new IllegalArgumentException( _loc.get("type-wrong-category",
                    cls, actual, expected).getMessage());
        }
        switch (actual) {
        case EMBEDDABLE:
            Types.Embeddable<X> embedded = new Types.Embeddable<X>(meta, this);
            _embeddables.put(cls, embedded);
            populate(embedded);
            // no break : embeddables are stored as both entity and embeddable containers
        case ENTITY:
        	Types.Entity<X> entity = new Types.Entity<X>(meta, this);
            _entities.put(cls, entity);
            populate(entity);
            break;
        case MAPPED_SUPERCLASS:
            Types.MappedSuper<X> mapped = new Types.MappedSuper<X>(meta, this);
            _mappedsupers.put(cls, mapped);
            populate(mapped);
            break;
        default:
            throw new InternalException(cls.getName());
        }
    }

    public <T> Set<T> unmodifiableSet(Collection<T> coll) {
        HashSet<T> result = new HashSet<T>();
        for (T t : coll)
            result.add(t);
        return result;
    }

    public static CollectionType categorizeCollection(Class<?> cls) {
        if (Set.class.isAssignableFrom(cls))
            return CollectionType.SET;
        if (List.class.isAssignableFrom(cls))
            return CollectionType.LIST;
        if (Collection.class.isAssignableFrom(cls))
            return CollectionType.COLLECTION;
        if (Map.class.isAssignableFrom(cls))
            return CollectionType.MAP;
        
        throw new InternalException(cls.getName() + " not a collection");
    }
    
    /**
     * Populate the static fields of the canonical type.
     */
    public <X> void populate(AbstractManagedType<X> type) {
		Class<X> cls = type.getJavaType();
		Class<?> mcls = repos.getMetaModel(cls, true);
		if (mcls == null)
		    return;
        StaticMetamodel anno = mcls.getAnnotation(StaticMetamodel.class);
		if (anno == null)
            throw new IllegalArgumentException(_loc.get("meta-class-no-anno", 
               mcls.getName(), cls.getName(), StaticMetamodel.class.getName()).getMessage());
		
        if (cls != anno.value()) {
            throw new IllegalStateException(_loc.get("meta-class-mismatch",
            mcls.getName(), cls.getName(), anno.value()).getMessage());
        }
        
    	Field[] mfields = mcls.getFields();
    	for (Field mf : mfields) {
            try {
                ParameterizedType mfType = getParameterziedType(mf);
    	        Attribute<? super X, ?> f = type.getAttribute(mf.getName());
    	        Class<?> fClass = f.getJavaType();
    	       java.lang.reflect.Type[] args = mfType.getActualTypeArguments();
    	       if (args.length < 2)
    	           throw new IllegalStateException(
    	               _loc.get("meta-field-no-para", mf).getMessage());
    	       java.lang.reflect.Type ftype = args[1];
    	       if (fClass.isPrimitive() 
 Collection.class.isAssignableFrom(fClass) 
 Map.class.isAssignableFrom(fClass)) {
    	        ;
    	    } else if (ftype != args[1]) {
    	        throw new RuntimeException(_loc.get("meta-field-mismatch", 
    	            new Object[]{mf.getName(), mcls.getName(), 
    	                toTypeName(mfType), toTypeName(ftype)}).getMessage());
    	    }
            mf.set(null, f);
	} catch (Exception e) {
	    e.printStackTrace();
		throw new RuntimeException(mf.toString());
	}
        }
    }
    
    /**
     * Gets the parameterized type of the given field after validating. 
     */
    ParameterizedType getParameterziedType(Field mf) {
        java.lang.reflect.Type t = mf.getGenericType();
        if (t instanceof ParameterizedType == false) {
            throw new IllegalStateException(_loc.get("meta-field-not-param", 
            mf.getDeclaringClass(), mf.getName(), toTypeName(t)).getMessage());
        }
        ParameterizedType mfType = (ParameterizedType)t;
        java.lang.reflect.Type[] args = mfType.getActualTypeArguments();
        if (args.length < 2) {
            throw new IllegalStateException(_loc.get("meta-field-less-param", 
            mf.getDeclaringClass(), mf.getName(), toTypeName(t)).getMessage());
        }
        
        return mfType;
    }
    
    /**
     * Pretty prints a Type. 
     */
    String toTypeName(java.lang.reflect.Type type) {
        if (type instanceof GenericArrayType) {
            return toTypeName(((GenericArrayType)type).
                getGenericComponentType())+"[]";
        }
        if (type instanceof ParameterizedType == false) {
            Class<?> cls = (Class<?>)type;
            return cls.getName();
        }
        ParameterizedType pType = (ParameterizedType)type;
        java.lang.reflect.Type[] args = pType.getActualTypeArguments();
        StringBuffer tmp = new StringBuffer(pType.getRawType().toString());
        for (int i = 0; i < args.length; i++) {
            tmp.append((i == 0) ? "<" : ",");
            tmp.append(toTypeName(args[i]));
            tmp.append((i == args.length-1) ? ">" : "");
        }
        return tmp.toString();
    }
    
    /**
     * Validates the given field of the meta class matches the given 
     * FieldMetaData and 
     * @param <X>
     * @param <Y>
     * @param mField
     * @param member
     */
    void validate(Field metaField, FieldMetaData fmd) {
        
    }
    
    <X,Y> void validate(Field mField, Member<X, Y> member) {
        Class<?> fType = member.getJavaType();
        if (!ParameterizedType.class.isInstance(mField.getGenericType())) {
            throw new IllegalArgumentException(_loc.get("meta-bad-field", 
                mField).getMessage());
        }
        ParameterizedType mfType = (ParameterizedType)mField.getGenericType();
        java.lang.reflect.Type[] args = mfType.getActualTypeArguments();
        java.lang.reflect.Type owner = args[0];
        if (member.getDeclaringType().getJavaType() != owner)
            throw new IllegalArgumentException(_loc.get("meta-bad-field-owner", 
                    mField, owner).getMessage());
        java.lang.reflect.Type elementType = args[1];
        if (fType.isPrimitive())
            return;
    }

    public Class classForName(String name, String[] imports) {
        throw new UnsupportedOperationException();
    }

    public AggregateListener getAggregateListener(String tag) {
        throw new UnsupportedOperationException();
    }

    public OpenJPAConfiguration getConfiguration() {
        return repos.getConfiguration();
    }

    public FilterListener getFilterListener(String tag) {
        throw new UnsupportedOperationException();
    }

    public QueryContext getQueryContext() {
        throw new UnsupportedOperationException();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/876.java