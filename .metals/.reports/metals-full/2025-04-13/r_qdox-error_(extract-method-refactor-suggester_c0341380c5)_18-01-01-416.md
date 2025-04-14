error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8960.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8960.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8960.java
text:
```scala
r@@eturn (IdentifiableType<? super X>) model.managedType(meta

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

import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.MappedSuperclassType;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;

import org.apache.openjpa.lib.util.Localizer;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.util.OpenJPAId;

/**
 * Persistent Type according to JPA 2.0.
 * 
 * Implemented as a thin adapter to OpenJPA metadata system. Mostly immutable.
 * 
 * @author Pinaki Poddar
 * 
 * @since 2.0.0
 * 
 */
public class Types {
    protected static Localizer _loc = Localizer.forPackage(Types.class);

    /**
     * Mirrors a Java class.
     *
     * @param <X> Java class 
     */
    static abstract class BaseType<X> implements Type<X> {
        public final Class<X> cls;

        protected BaseType(Class<X> cls) {
            this.cls = cls;
        }

        public final Class<X> getJavaType() {
            return cls;
        }

        public String toString() {
            return cls.getName();
        }
    }

    public static class Basic<X> extends BaseType<X> implements Type<X> {
        public Basic(Class<X> cls) {
            super(cls);
        }

        public PersistenceType getPersistenceType() {
            return PersistenceType.BASIC;
        }
    }

    /**
     *  Instances of the type ManagedType represent entity, mapped 
     *  superclass, and embeddable types.
     *
     *  @param <X> The represented type.
     */
//    public static abstract class Managed<X> extends AbstractManagedType<X> implements
//        ManagedType<X> {
        /**
         * Construct a managed type. The supplied metadata must be resolved i.e.
         * all its fields populated. Because this receiver will populate its
         * attributes corresponding to the available fields of the metadata.
         * 
         */
//        public Managed(ClassMetaData meta, MetamodelImpl model) {
//            super(meta, model);
//        }
        
         /**
         *  Return the bindable type of the represented object.
         *  @return bindable type
         */ 
//        public BindableType getBindableType() {
//            return BindableType.ENTITY_TYPE;
//        }
        
        /**
         * Return the Java type of the represented object.
         * If the bindable type of the object is PLURAL_ATTRIBUTE,
         * the Java element type is returned. If the bindable type is
         * SINGULAR_ATTRIBUTE or ENTITY_TYPE, the Java type of the
         * represented entity or attribute is returned.
         * @return Java type
         */
//        public Class<X> getBindableJavaType() {
//            throw new AbstractMethodError();
//        }
//
//    }

    public static abstract class Identifiable<X> extends AbstractManagedType<X> 
        implements IdentifiableType<X> {

        public Identifiable(ClassMetaData meta, MetamodelImpl model) {
            super(meta, model);
        }

        /**
         *  Whether or not the identifiable type has a version attribute.
         *  @return boolean indicating whether or not the identifiable
         *          type has a version attribute
         */
        public boolean hasVersionAttribute() {
            return meta.getVersionField() != null;
        }


        /**
         *  Return the identifiable type that corresponds to the most
         *  specific mapped superclass or entity extended by the entity 
         *  or mapped superclass. 
         *  @return supertype of identifiable type or null if no such supertype
         */
        public IdentifiableType<? super X> getSupertype() {
            return (IdentifiableType<? super X>) model.type(meta
                .getPCSuperclassMetaData().getDescribedType());
        }

        public boolean hasIdAttribute() {
            return meta.getIdentityType() == ClassMetaData.ID_APPLICATION;
        }
        
        /**
         *  Whether or not the identifiable type has an id attribute.
         *  Returns true for a simple id or embedded id; returns false
         *  for an idclass.
         *  @return boolean indicating whether or not the identifiable
         *          type has a single id attribute
         */
        public boolean hasSingleIdAttribute() {
            return meta.getPrimaryKeyFields().length == 1;
        }

        /**
         *  Return the type that represents the type of the id.
         *  @return type of id
         */
        public Type<?> getIdType() {
            Class<?> idType = hasSingleIdAttribute() 
                     ? meta.getPrimaryKeyFields()[0].getDeclaredType() : meta.getObjectIdType();
            return model.getType(idType);
        }
    }

    public static class Embeddable<X> extends AbstractManagedType<X> 
        implements EmbeddableType<X> {
        public Embeddable(ClassMetaData meta, MetamodelImpl model) {
            super(meta, model);
        }
        
        public PersistenceType getPersistenceType() {
            return PersistenceType.EMBEDDABLE;
        }
    }

    public static class MappedSuper<X> extends Identifiable<X> implements
        MappedSuperclassType<X> {

        public MappedSuper(ClassMetaData meta, MetamodelImpl model) {
            super(meta, model);
        }
        
        public PersistenceType getPersistenceType() {
            return PersistenceType.MAPPED_SUPERCLASS;
        }

    }
    
    public static class Entity<X> extends Identifiable<X> 
        implements EntityType<X> {

        public Entity(ClassMetaData meta, MetamodelImpl model) {
            super(meta, model);
        }
        
        public PersistenceType getPersistenceType() {
            return PersistenceType.ENTITY;
        }
        
        public String getName() {
        	return meta.getTypeAlias();
        }
        /**
         *  Return the bindable type of the represented object.
         *  @return bindable type
         */ 
        public BindableType getBindableType() {
            return BindableType.ENTITY_TYPE;
        }
        
        /**
         * Return the Java type of the represented object.
         * If the bindable type of the object is PLURAL_ATTRIBUTE,
         * the Java element type is returned. If the bindable type is
         * SINGULAR_ATTRIBUTE or ENTITY_TYPE, the Java type of the
         * represented entity or attribute is returned.
         * @return Java type
         */
        public Class<X> getBindableJavaType() {
            return getJavaType();
        }
    }   
    
    /**
     * A pseudo managed type used to represent keys of a java.util.Map as a 
     * pseudo attribute.
    **/ 
    public static class PseudoEntity<X> extends AbstractManagedType<X> {

        protected PseudoEntity(Class<X> cls, MetamodelImpl model) {
            super(cls, model);
        }

        public javax.persistence.metamodel.Type.PersistenceType getPersistenceType() {
            return PersistenceType.ENTITY;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8960.java