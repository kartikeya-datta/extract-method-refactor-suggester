error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17325.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17325.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17325.java
text:
```scala
C@@lass<?> m2Cls = model.getRepository().getMetaModel(mCls, true);

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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.IdentifiableType;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.Bindable.BindableType;
import javax.persistence.metamodel.PluralAttribute.CollectionType;
import javax.persistence.metamodel.Type.PersistenceType;

import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.persistence.enhance.identity.Book;
import org.apache.openjpa.persistence.enhance.identity.BookId;
import org.apache.openjpa.persistence.enhance.identity.Library;
import org.apache.openjpa.persistence.enhance.identity.Page;
import org.apache.openjpa.persistence.relations.OneOneChild;
import org.apache.openjpa.persistence.relations.OneOneParent;
import org.apache.openjpa.persistence.test.SingleEMFTestCase;

/**
 * Tests JPA 2.0 Metamodel.
 * 
 * @author Pinaki Poddar
 *
 */
public class TestMetamodel extends SingleEMFTestCase {
    private static MetamodelImpl model;
    
    public void setUp() {
        if (model == null) {
    	super.setUp(
    	        "openjpa.RuntimeUnenhancedClasses", "unsupported",
    	        "openjpa.DynamicEnhancementAgent", "false",
    			ImplicitFieldAccessMappedSuperclass.class,
    	        ImplicitFieldAccessBase.class, 
    	        ImplicitFieldAccessSubclass.class,
    	        ExplicitFieldAccess.class, 
    	        ExplicitPropertyAccess.class,
    	        Embed0.class, 
    	        Embed1.class,
    	        OneOneParent.class,
    	        OneOneChild.class,
    	        Book.class,
    	        Library.class,
    	        Page.class);
    	emf.createEntityManager();
        model = (MetamodelImpl)emf.getMetamodel();
        }
    }
    
    public void testMetaModelForDomainClassesExist() {
        assertFalse(model.getEntities().isEmpty());
        assertFalse(model.getEmbeddables().isEmpty());
        assertFalse(model.getManagedTypes().isEmpty());
    }
    
    public void testMetaClassFieldsArePopulated() {
        EntityType<ImplicitFieldAccessSubclass> m =  model.entity(ImplicitFieldAccessSubclass.class);
        assertNotNull(m);
        Class<?> mCls = m.getJavaType();
        assertSame(ImplicitFieldAccessSubclass.class, mCls);
        
        Class<?> m2Cls = model.repos.getMetaModel(mCls, true);
        assertNotNull(m2Cls);
        try {
            Field f2 = getStaticField(m2Cls, "base");
            assertNotNull(f2);
            Object value = f2.get(null);
            assertNotNull(value);
            assertTrue(Attribute.class.isAssignableFrom(value.getClass()));
        } catch (Throwable t) {
            t.printStackTrace();
            fail();
        }
    }
    
    public void testDomainClassCategorizedInPersistentCategory() {
    	assertCategory(PersistenceType.MAPPED_SUPERCLASS, ImplicitFieldAccessMappedSuperclass.class);
    	assertCategory(PersistenceType.ENTITY, ImplicitFieldAccessBase.class);
    	assertCategory(PersistenceType.ENTITY, ImplicitFieldAccessSubclass.class);
    	assertCategory(PersistenceType.EMBEDDABLE, Embed0.class);
    	assertCategory(PersistenceType.EMBEDDABLE, Embed1.class);
    	
        assertNotNull(model.entity(ImplicitFieldAccessBase.class));
        assertNotNull(model.entity(ImplicitFieldAccessSubclass.class));      
        assertNotNull(model.embeddable(Embed0.class));
        assertNotNull(model.embeddable(Embed1.class));
        
        java.util.Set<ManagedType<?>> managedTypes = model.getManagedTypes();
        managedTypes.removeAll(model.getEmbeddables());
        managedTypes.removeAll(model.getEntities());
        assertNotNull(model.type(ImplicitFieldAccessMappedSuperclass.class));
        assertTrue(managedTypes.contains(model.type(ImplicitFieldAccessMappedSuperclass.class)));
    }
    
    public void testGetAttributeByNameAndTypeFromMetaClass() {
        ManagedType<ImplicitFieldAccessBase> e0 = model.entity(ImplicitFieldAccessBase.class);
        assertNotNull(e0.getAttribute("f0"));
        assertNotNull(e0.getSingularAttribute("f0", String.class));
        assertSame(e0.getAttribute("f0"), e0.getSingularAttribute("f0", String.class));
        try {
            e0.getSingularAttribute("f0", ExplicitFieldAccess.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        ManagedType<ImplicitFieldAccessSubclass> e1 = model.entity(ImplicitFieldAccessSubclass.class);
        assertNotNull(e1.getAttribute("f0"));
    }
    
    public void testAttributeByDeclaration() {
        ManagedType<ImplicitFieldAccessBase> e0 = model.entity(ImplicitFieldAccessBase.class);
        ManagedType<ImplicitFieldAccessSubclass> e1 = model.entity(ImplicitFieldAccessSubclass.class);
        assertNotNull(e0.getAttribute("f0"));
        assertNotNull(e1.getAttribute("f0"));
        System.err.println(e0.getAttribute("f0"));
        assertNotNull(e0.getSingularAttribute("f0", String.class));
        assertNotNull(e1.getSingularAttribute("f0", String.class));
        assertSame(e0.getAttribute("f0"), e0.getSingularAttribute("f0", String.class));
        assertSame(e1.getAttribute("f0"), e1.getSingularAttribute("f0", String.class));
        assertNotSame(e0.getAttribute("f0"), e1.getAttribute("f0"));
        assertNotSame(e0.getSingularAttribute("f0", String.class), e1.getSingularAttribute("f0", String.class));
        assertNotNull(e0.getDeclaredAttribute("f0"));
        try {
            e1.getDeclaredAttribute("f0");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    
    public void testPCCollection() {
        ManagedType<ImplicitFieldAccessBase> e0 = model.entity(ImplicitFieldAccessBase.class);
        ManagedType<ExplicitFieldAccess> r1 = model.entity(ExplicitFieldAccess.class);
        CollectionAttribute<?,?> relColl = e0.getCollection("collectionRelation", ExplicitFieldAccess.class);
        assertEquals(javax.persistence.metamodel.PluralAttribute.CollectionType.COLLECTION, 
                relColl.getCollectionType());
        assertEquals(e0, relColl.getDeclaringType());
        assertEquals(r1, relColl.getElementType());
        assertEquals(ExplicitFieldAccess.class, relColl.getBindableJavaType());
        assertEquals(BindableType.PLURAL_ATTRIBUTE, relColl.getBindableType());
        assertEquals(Attribute.PersistentAttributeType.ONE_TO_MANY, relColl.getPersistentAttributeType());
    }
    
    public void testPCList() {
        ManagedType<ImplicitFieldAccessBase> e0 = model.entity(ImplicitFieldAccessBase.class);
        ManagedType<ExplicitFieldAccess> r1 = model.entity(ExplicitFieldAccess.class);
        ListAttribute<?, ?> relList = e0.getList("listRelation", ExplicitFieldAccess.class);
        
        assertEquals(CollectionType.LIST, relList.getCollectionType());
        assertEquals(e0, relList.getDeclaringType());
        assertEquals(r1, relList.getElementType());
        assertEquals(ExplicitFieldAccess.class, relList.getBindableJavaType());
        assertEquals(BindableType.PLURAL_ATTRIBUTE, relList.getBindableType());
        assertEquals(PersistentAttributeType.ONE_TO_MANY, relList.getPersistentAttributeType());
    }
    
    public void testPCSet() {
        ManagedType<ImplicitFieldAccessBase> e0 = model.entity(ImplicitFieldAccessBase.class);
        ManagedType<ExplicitFieldAccess> r1 = model.entity(ExplicitFieldAccess.class);
        SetAttribute<?, ?> relSet = e0.getSet("setRelation", ExplicitFieldAccess.class);
        assertEquals(javax.persistence.metamodel.PluralAttribute.CollectionType.SET, relSet.getCollectionType());
        assertEquals(e0, relSet.getDeclaringType());
        assertEquals(r1, relSet.getElementType());
        assertEquals(ExplicitFieldAccess.class, relSet.getBindableJavaType());
        assertEquals(BindableType.PLURAL_ATTRIBUTE, relSet.getBindableType());
        assertEquals(PersistentAttributeType.ONE_TO_MANY, relSet.getPersistentAttributeType());
    }
    
    public void testDeclaredFields() {
        ManagedType<ImplicitFieldAccessSubclass> e1 = model.entity(ImplicitFieldAccessSubclass.class);
        java.util.Set<?> all = e1.getAttributes();
        java.util.Set<?> decl = e1.getDeclaredAttributes();
        assertTrue("All fields " + all + "\r\nDeclared fields " + decl + "\r\n"+
         "expecetd not all fields as declared", all.size() > decl.size());
    }
    
    public void testNonExistentField() {
        ManagedType<ImplicitFieldAccessBase> e0 = model.entity(ImplicitFieldAccessBase.class);
        ManagedType<ImplicitFieldAccessSubclass> e1 = model.entity(ImplicitFieldAccessSubclass.class);
        assertFails(e0, "xyz", false);
        assertFails(e1, "f0", true);
    }
    
    /**
     * Test all attribute getters of ManagedType for valid inputs.
     */
    public void testAttributeGettersForValidInput() {
        IdentifiableType<ImplicitFieldAccessSubclass> subClass = model.entity(ImplicitFieldAccessSubclass.class);
        IdentifiableType<ImplicitFieldAccessBase> superClass = model.entity(ImplicitFieldAccessBase.class);
        
        assertEquals(superClass, subClass.getSupertype());
        
        assertNotNull(subClass.getAttribute("f0"));
        assertNotNull(superClass.getAttribute("f0"));
        assertNotNull(subClass.getAttribute("mapRelationKeyEmbedded"));
        
        assertNotNull(subClass.getAttributes());
        assertNotNull(superClass.getAttributes());
        assertEquals(14, subClass.getAttributes().size());
        assertEquals(12, superClass.getAttributes().size());
        
        assertNotNull(superClass.getCollection("collectionRelation"));
        assertNotNull(superClass.getCollection("collectionRelation", ExplicitFieldAccess.class));
        
        assertNotNull(subClass.getPluralAttributes());
        assertNotNull(superClass.getPluralAttributes());
        assertEquals(6, subClass.getPluralAttributes().size());
        assertEquals(5, superClass.getPluralAttributes().size());
        
        assertNotNull(subClass.getDeclaredAttribute("mapRelationKeyEmbedded"));
        
        assertNotNull(subClass.getDeclaredAttributes());
        assertNotNull(superClass.getDeclaredAttributes());
        assertEquals(2, subClass.getDeclaredAttributes().size());
        assertEquals(9, superClass.getDeclaredAttributes().size());
        
        assertNotNull(superClass.getDeclaredCollection("collectionRelation"));
        
        assertNotNull(subClass.getDeclaredPluralAttributes());
        assertNotNull(superClass.getDeclaredPluralAttributes());
        assertEquals(1, subClass.getDeclaredPluralAttributes().size());
        assertEquals(5, superClass.getDeclaredPluralAttributes().size());
        
        assertNotNull(superClass.getDeclaredList("listRelation"));
        assertNotNull(superClass.getDeclaredList("listRelation", ExplicitFieldAccess.class));
        assertNotNull(superClass.getDeclaredMap("mapRelationKeyBasic"));
        assertNotNull(superClass.getDeclaredMap("mapRelationKeyBasic", Integer.class, ExplicitFieldAccess.class));
        assertNotNull(superClass.getDeclaredSet("setRelation"));
        assertNotNull(superClass.getDeclaredSet("setRelation", ExplicitFieldAccess.class));
        assertNotNull(superClass.getDeclaredSingularAttribute("one2oneRelation"));
        assertNotNull(superClass.getDeclaredSingularAttribute("one2oneRelation", ExplicitFieldAccess.class));
        assertNotNull(superClass.getDeclaredSingularAttributes());
        
        
        assertNotNull(subClass.getList("listRelation"));
        assertNotNull(subClass.getList("listRelation", ExplicitFieldAccess.class));
        assertNotNull(subClass.getMap("mapRelationKeyBasic"));
        assertNotNull(subClass.getMap("mapRelationKeyBasic", Integer.class, ExplicitFieldAccess.class));
        assertNotNull(subClass.getPersistenceType());
        assertNotNull(subClass.getSet("setRelation"));
        assertNotNull(subClass.getSet("setRelation", ExplicitFieldAccess.class));
        assertNotNull(subClass.getSingularAttribute("one2oneRelation"));
        assertNotNull(subClass.getSingularAttribute("one2oneRelation", ExplicitFieldAccess.class));
        assertNotNull(subClass.getSingularAttributes());
    }
    
    public void testSimpleIdAttributes() {
        IdentifiableType<OneOneParent> entity = model.entity(OneOneParent.class);
        for (Attribute<? super OneOneParent,?> a : entity.getAttributes()) {
            System.err.println(a.getName());
        }
        assertNotNull(entity.getId(long.class));
        assertNotNull(entity.getDeclaredId(long.class));
        assertTrue(entity.hasSingleIdAttribute());
        assertEquals(long.class, entity.getIdType().getJavaType());
    }
    
    public void testVersionAttributes() {
        IdentifiableType<OneOneParent> entity = model.entity(OneOneParent.class);
        for (Attribute<? super OneOneParent,?> a : entity.getAttributes()) {
            System.err.println(a.getName());
        }
        assertNotNull(entity.getVersion(Integer.class));
        assertNotNull(entity.getDeclaredVersion(Integer.class));
        assertTrue(entity.hasVersionAttribute());
    }
    
    public void testIdClassAttributes() {
        IdentifiableType<Book> entity = model.entity(Book.class);
        
        assertEquals(2, entity.getIdClassAttributes().size());
        assertNotNull(entity.getId(String.class));
        assertNotNull(entity.getId(Library.class));
        assertNotNull(entity.getDeclaredId(String.class));
        assertNotNull(entity.getDeclaredId(Library.class));
        assertEquals(BookId.class, entity.getIdType().getJavaType());
        assertFalse(entity.hasSingleIdAttribute());
    }

    
    void assertFails(ManagedType<?> type, String name, boolean dec) {
        try {
            Attribute<?,?> a = dec ? type.getDeclaredAttribute(name) 
                : type.getAttribute(name);
            fail("Expected to fail " + name + " on " + type);
        } catch (IllegalArgumentException e) {
            System.err.println("Expeceted:" + e);
        }
    }
    
    PersistenceType categorize(Class<?> c) {
        AbstractManagedType<?> type = (AbstractManagedType<?>)model.getType(c);
        ClassMetaData meta = type.meta;
        return MetamodelImpl.getPersistenceType(meta);
    }
    
    void assertCategory(PersistenceType category, Class<?> cls) {
    	assertEquals(cls.toString(), category, categorize(cls));
    }
    
    Field getStaticField(Class<?> cls, String name) {
        try {
            Field[] fds = cls.getDeclaredFields();
            for (Field f : fds) {
                int mods = f.getModifiers();
                if (f.getName().equals(name) && Modifier.isStatic(mods))
                    return f;
            }
        } catch (Exception e) {
        }
        return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17325.java