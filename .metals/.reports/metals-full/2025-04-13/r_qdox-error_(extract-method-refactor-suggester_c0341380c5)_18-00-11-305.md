error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9125.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9125.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9125.java
text:
```scala
r@@eturn owner.model.managedType(fmd.getDeclaringType());

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

import java.util.Set;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;

import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.meta.JavaTypes;

/**
 * Persistent attribute according to JPA 2.0 metamodel.
 * 
 * Implemented as a thin adapter to OpenJPA FieldMetadata. Mostly immutable.
 * 
 * @author Pinaki Poddar
 * 
 * @since 2.0.0
 *
 */
public class Members {
    /**
     * An attribute of a Java type. A persistent attribute is realized as a field and getter/setter
     * method of a Java class. This implementation adapts kernel's {@link FieldMetaData} construct 
     * to meet the JPA API contract.
	 *
	 *
     * @param <X> The type that contains this attribute
     * @param <Y> The type of this attribute
	 */
    public static abstract class Member<X, Y> implements Attribute<X, Y>, Comparable<Member<X,Y>> {
        public final AbstractManagedType<X> owner;
        public final FieldMetaData fmd;

        /**
         * Supply immutable parts.
         * 
         * @param owner the persistent type that contains this attribute
         * @param fmd the kernel's concrete representation of this attribute
         */
        protected Member(AbstractManagedType<X> owner, FieldMetaData fmd) {
            this.owner = owner;
            this.fmd = fmd;
        }

        /**
         *  Returns the managed type which declared this attribute.
         */
        public final ManagedType<X> getDeclaringType() {
            return owner.model.type(fmd.getDeclaringType());
        }
        
        /**
         *  Returns the java.lang.reflect.Member for this attribute. 
         */
        public final java.lang.reflect.Member getJavaMember() {
            return fmd.getBackingMember();
        }
        
        /**
         *  Gets the Java type of this attribute.
         */
        public final Class<Y> getJavaType() {
            return (Class<Y>)fmd.getDeclaredType();
        }
        
        /**
         * Gets the name of this attribute.
         */
        public final String getName() {
            return fmd.getName();
        }

        /**
         * Returns the type that represents the type of this attribute.
         */
        public final Type<Y> getType() {
            return owner.model.getType(isCollection() ?
                   fmd.getElement().getDeclaredType() : fmd.getDeclaredType());
        }
        
        /**
         * Affirms if this attribute is an association.
         */
        public final boolean isAssociation() {
            return fmd.isDeclaredTypePC();
        }

        /**
         * Affirms if this attribute is a collection.
         */
        public final boolean isCollection() {
            int typeCode = fmd.getDeclaredTypeCode();
            return  typeCode == JavaTypes.COLLECTION
 typeCode == JavaTypes.MAP
 typeCode == JavaTypes.ARRAY;
        }
        
        /**
         *  Returns the persistent category for the attribute.
         */
        public PersistentAttributeType getPersistentAttributeType() {
            if (fmd.isEmbeddedPC())
                return PersistentAttributeType.EMBEDDED;
            if (fmd.isElementCollection())
                return PersistentAttributeType.ELEMENT_COLLECTION;
            return PersistentAttributeType.BASIC;
        }

        public int compareTo(Member<X, Y> o) {
            return fmd.getName().compareTo(o.fmd.getName());
        }
    }
    
    
    /**
     * Represents single-valued persistent attributes.
     *
     * @param <X> The type containing the represented attribute
     * @param <T> The type of the represented attribute
     */
    public static final class SingularAttributeImpl<X, T> extends Member<X, T> 
        implements SingularAttribute<X, T> {

        public SingularAttributeImpl(AbstractManagedType<X> owner, FieldMetaData fmd) {
            super(owner, fmd);
        }

        /**
         *  Affirms if this attribute is an id attribute.
         */
        public boolean isId() {
            return fmd.isPrimaryKey();
        }

        /**
         *  Affirms if this attribute represents a version attribute.
         */
        public boolean isVersion() {
            return fmd.isVersion();
        }

        /** 
         *  Affirms if this attribute can be null.
         */
        public boolean isOptional() {
            return fmd.getNullValue() != FieldMetaData.NULL_EXCEPTION;
        }

        /**
         *  Categorizes bindable type represented by this attribute.
         */ 
        public final BindableType getBindableType() {
            return fmd.isDeclaredTypePC() 
                ? BindableType.ENTITY_TYPE
                : BindableType.SINGULAR_ATTRIBUTE;
        }
       
        /**
         * Returns the bindable Java type of this attribute.
         * 
         * If the bindable category of this attribute is PLURAL_ATTRIBUTE, the Java element type 
         * is returned. If the bindable type is SINGULAR_ATTRIBUTE or ENTITY_TYPE, the Java type 
         * of the represented entity or attribute is returned.
         */
        public final Class<T> getBindableJavaType() {
            return fmd.getElement().getDeclaredType();
        }
        
        /**
         * Categorizes the attribute.
         */
        public final PersistentAttributeType getPersistentAttributeType() {
            if (!fmd.isDeclaredTypePC())
                return super.getPersistentAttributeType();
            return fmd.getMappedByMetaData() == null 
                 ? PersistentAttributeType.ONE_TO_ONE
                 : PersistentAttributeType.ONE_TO_MANY;
        }
    }

    /**
     * Root of multi-cardinality attribute.
     *
	 * @param <X> the type that owns this member
	 * @param <C> the container type that holds this member (e.g. java.util.Set&lt;Employee&gt;)
     * @param <E> the type of the element held by this member (e.g. Employee). 
     */
    public static abstract class PluralAttributeImpl<X, C, E> extends Member<X, C>
        implements PluralAttribute<X, C, E> {
        
        public PluralAttributeImpl(AbstractManagedType<X> owner, FieldMetaData fmd) {
            super(owner, fmd);
        }

        /**
         * Returns the type representing the element type of the collection.
         */
        public final Type<E> getElementType() {
            return owner.model.getType(getBindableJavaType());
        }

        /**
         *  Returns the bindable category of this attribute.
         */ 
        public final BindableType getBindableType() {
            return BindableType.PLURAL_ATTRIBUTE;
        }
        
        /**
         * Returns the bindable Java type of this attribute.
         * 
         * For PLURAL_ATTRIBUTE, the Java element type is returned. 
         */
        public Class<E> getBindableJavaType() {
            return fmd.getElement().getDeclaredType();
        }
        
        
        public PersistentAttributeType getPersistentAttributeType() {
            return PersistentAttributeType.ONE_TO_MANY;
        }
    }

    /**
     * Represents attributes declared as java.util.Collection&lt;E&gt;.
     */
    public static class CollectionAttributeImpl<X, E> 
        extends PluralAttributeImpl<X, java.util.Collection<E>, E> 
        implements CollectionAttribute<X, E> {

        public CollectionAttributeImpl(AbstractManagedType<X> owner, FieldMetaData fmd) {
            super(owner, fmd);
        }

        public CollectionType getCollectionType() {
            return CollectionType.COLLECTION;
        }
    }

    /**
     * Represents attributes declared as java.util.List&lt;E&gt;.
     */
    public static class ListAttributeImpl<X, E> 
        extends PluralAttributeImpl<X, java.util.List<E>, E> 
        implements ListAttribute<X, E> {

        public ListAttributeImpl(AbstractManagedType<X> owner, FieldMetaData fmd) {
            super(owner, fmd);
        }

        public CollectionType getCollectionType() {
            return CollectionType.LIST;
        }
    }

    /**
     * Represents attributes declared as java.util.Set&lt;E&gt;.
     */
    public static class SetAttributeImpl<X, E> 
        extends PluralAttributeImpl<X, java.util.Set<E>, E> 
        implements SetAttribute<X, E> {

        public SetAttributeImpl(AbstractManagedType<X> owner, FieldMetaData fmd) {
            super(owner, fmd);
        }

        public CollectionType getCollectionType() {
            return CollectionType.SET;
        }
    }
    
    /**
     * Represents the keys of java.util.Map&lt;K,V&gt; in managed type &lt;X&gt; as a pseudo-attribute of type 
     * java.util.Set&lt;K&gt;.
     *
     * @param <X> the declaring type of the original java.util.Map&lt;K,V&gt; attribute 
     * @param <K> the type of the key of the original java.util.Map&lt;K,V&gt; attribute
     */
    public static class KeyAttributeImpl<X,K> extends SetAttributeImpl<X, K> {
        public KeyAttributeImpl(AbstractManagedType<X> owner, FieldMetaData fmd){
            super(owner, fmd);
        }

        public Class<K> getBindableJavaType() {
            return (Class<K>)fmd.getKey().getDeclaredType();
        }
    }

    /**
     * Represents attributes declared as java.util.Map&lt;K,V&gt; in managed type &lt;X&gt;.
     */
    public static class MapAttributeImpl<X, K, V> 
        extends PluralAttributeImpl<X, java.util.Map<K, V>, V> 
        implements MapAttribute<X, K, V> {

        public MapAttributeImpl(AbstractManagedType<X> owner, FieldMetaData fmd) {
            super(owner, fmd);
        }

        public CollectionType getCollectionType() {
            return CollectionType.MAP;
        }
        
        public Class<K> getKeyJavaType() {
            return fmd.getKey().getDeclaredType();
        }

        public Type<K> getKeyType() {
            return owner.model.getType(getKeyJavaType());
        }
        
        public PersistentAttributeType getPersistentAttributeType() {
            return PersistentAttributeType.MANY_TO_MANY;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9125.java