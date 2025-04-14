error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6507.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6507.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6507.java
text:
```scala
C@@LEAR_TABLES, RETAIN_DATA);

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
package org.apache.openjpa.persistence.jdbc.annotations;

import java.awt.*;
import java.util.Map;

import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.meta.ClassMapping;
import org.apache.openjpa.jdbc.meta.Discriminator;
import org.apache.openjpa.jdbc.meta.FieldMapping;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.strats.ClassNameDiscriminatorStrategy;
import org.apache.openjpa.jdbc.schema.ForeignKey;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.persistence.JPAFacadeHelper;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.test.SingleEMFTestCase;

/**
 * <p>Test the parsing of non-standard mapping annotations.</p>
 *
 * @author Abe White
 */
public class TestNonstandardMappingAnnotations
    extends SingleEMFTestCase {

    private ClassMapping _mapping;
    private DBDictionary _dict;

    public void setUp() {
        setUp(NonstandardMappingEntity.class, ExtensionsEntity.class,
            NonstandardMappingMappedSuper.class, EmbedValue2.class,
            EmbedValue.class,
            CLEAR_TABLES);

        // trigger complete resolution of metadata etc.
        emf.createEntityManager().close();

        JDBCConfiguration conf = (JDBCConfiguration) emf.getConfiguration();
        _dict = conf.getDBDictionaryInstance();
        _mapping = (ClassMapping) JPAFacadeHelper.getMetaData(emf,
            NonstandardMappingEntity.class);
    }

    public void testNonpolymorphic() {
        FieldMapping fm = _mapping.getFieldMapping("superRel");
        assertEquals(ValueMapping.POLY_FALSE, fm.getPolymorphic());

        ValueMapping vm = _mapping.getFieldMapping("joinCollection").
            getElementMapping();
        assertEquals(ValueMapping.POLY_JOINABLE, vm.getPolymorphic());

        vm = _mapping.getFieldMapping("joinMap").getKeyMapping();
        assertEquals(ValueMapping.POLY_FALSE, vm.getPolymorphic());
        vm = _mapping.getFieldMapping("joinMap").getElementMapping();
        assertEquals(ValueMapping.POLY_FALSE, vm.getPolymorphic());
    }

    public void testDataStoreIdColumn() {
        assertEquals("NONSTD_ENTITY", _mapping.getTable().getName());
        assertEquals(ClassMapping.ID_DATASTORE, _mapping.getIdentityType());
        assertEquals(1, _mapping.getPrimaryKeyColumns().length);
        assertEquals("OID", _mapping.getPrimaryKeyColumns()[0].getName());
    }

    public void testDiscriminator() {
        Discriminator disc = _mapping.getDiscriminator();
        assertTrue(disc.getStrategy() instanceof
            ClassNameDiscriminatorStrategy);
        assertEquals(1, disc.getColumns().length);
        assertEquals("DISCRIM", disc.getColumns()[0].getName());
    }

    public void testSuperclassOverride() {
        FieldMapping fm = _mapping.getFieldMapping("superCollection");
        assertEquals("SUP_COLL", fm.getTable().getName());
        assertEquals(1, fm.getJoinForeignKey().getColumns().length);
        assertEquals("OWNER", fm.getJoinForeignKey().getColumns()[0].
            getName());
        assertEquals(1, fm.getElementMapping().getColumns().length);
        assertEquals("SUP_ELEM", fm.getElementMapping().getColumns()[0].
            getName());
        assertNull(fm.getElementMapping().getValueIndex());
        assertNotNull(fm.getJoinIndex());
    }

    public void testCustomField() {
        FieldMapping fm = _mapping.getFieldMapping("custom");
        assertTrue(fm.getHandler() instanceof PointHandler);
        assertEquals(2, fm.getColumns().length);
        assertEquals("X_COL", fm.getColumns()[0].getName());
        assertEquals("Y_COL", fm.getColumns()[1].getName());
        assertNotNull(fm.getValueIndex());
    }

    public void testValueCollection() {
        FieldMapping fm = _mapping.getFieldMapping("stringCollection");
        assertEquals("STRINGS_COLL", fm.getTable().getName());
        assertEquals(1, fm.getJoinForeignKey().getColumns().length);
        assertEquals("OWNER", fm.getJoinForeignKey().getColumns()[0].
            getName());
        assertEquals(1, fm.getElementMapping().getColumns().length);
        assertEquals("STR_ELEM", fm.getElementMapping().getColumns()[0].
            getName());
        assertEquals(127, fm.getElementMapping().getColumns()[0].getSize());
        assertNotNull(fm.getElementMapping().getValueIndex());
        assertNull(fm.getJoinIndex());
    }

    public void testJoinCollection() {
        FieldMapping fm = _mapping.getFieldMapping("joinCollection");
        assertEquals("JOIN_COLL", fm.getTable().getName());
        assertEquals(1, fm.getJoinForeignKey().getColumns().length);
        assertEquals("OWNER", fm.getJoinForeignKey().getColumns()[0].
            getName());
        assertEquals(1, fm.getElementMapping().getColumns().length);
        assertEquals("JOIN_ELEM", fm.getElementMapping().getColumns()[0].
            getName());
        assertForeignKey(fm.getJoinForeignKey());
        assertForeignKey(fm.getElementMapping().getForeignKey());
    }

    private void assertForeignKey(ForeignKey fk) {
        if (_dict.supportsForeignKeys)
            assertEquals(ForeignKey.ACTION_RESTRICT, fk.getDeleteAction());
    }

    public void testValueMap() {
        FieldMapping fm = _mapping.getFieldMapping("stringMap");
        assertEquals("STRINGS_MAP", fm.getTable().getName());
        assertEquals(1, fm.getJoinForeignKey().getColumns().length);
        assertEquals("OWNER", fm.getJoinForeignKey().getColumns()[0].
            getName());
        assertEquals(1, fm.getKeyMapping().getColumns().length);
        assertEquals("STR_KEY", fm.getKeyMapping().getColumns()[0].
            getName());
        assertEquals(127, fm.getKeyMapping().getColumns()[0].getSize());
        assertEquals(1, fm.getElementMapping().getColumns().length);
        assertEquals("STR_VAL", fm.getElementMapping().getColumns()[0].
            getName());
        assertEquals(127, fm.getElementMapping().getColumns()[0].getSize());
        assertNull(fm.getJoinIndex());
        assertNotNull(fm.getKeyMapping().getValueIndex());
        assertNotNull(fm.getElementMapping().getValueIndex());
    }

    public void testJoinMap() {
        FieldMapping fm = _mapping.getFieldMapping("joinMap");
        assertEquals("JOIN_MAP", fm.getTable().getName());
        assertEquals(1, fm.getJoinForeignKey().getColumns().length);
        assertEquals("OWNER", fm.getJoinForeignKey().getColumns()[0].
            getName());
        assertEquals(1, fm.getKeyMapping().getColumns().length);
        assertEquals("JOIN_KEY", fm.getKeyMapping().getColumns()[0].
            getName());
        assertEquals(1, fm.getElementMapping().getColumns().length);
        assertEquals("JOIN_VAL", fm.getElementMapping().getColumns()[0].
            getName());
        assertForeignKey(fm.getJoinForeignKey());
        assertForeignKey(fm.getKeyMapping().getForeignKey());
        assertForeignKey(fm.getElementMapping().getForeignKey());
    }

    public void testEmbeddedOverride() {
        FieldMapping fm = _mapping.getFieldMapping("embed");
        assertTrue(fm.isEmbedded());
        assertEquals(1, fm.getColumns().length);
        assertTrue("UUID_HEX".equalsIgnoreCase(fm.getColumns()[0].
            getName()));

        ClassMapping embed = fm.getEmbeddedMapping();
        fm = embed.getFieldMapping("rel");
        assertEquals(1, fm.getColumns().length);
        assertEquals("EM_REL_ID", fm.getColumns()[0].getName());
        fm = embed.getFieldMapping("eager");
        assertEquals("EM_EAGER", fm.getTable().getName());
        assertEquals(1, fm.getElementMapping().getColumns().length);
        assertEquals("ELEM_EAGER_ID", fm.getElementMapping().getColumns()[0].
            getName());
    }

    public void testEmbeddedElementOverride() {
        FieldMapping fm = _mapping.getFieldMapping("embedCollection");
        assertTrue(fm.getElement().isEmbedded());
        assertEquals("EMBED_COLL", fm.getTable().getName());
        assertEquals(0, fm.getElementMapping().getColumns().length);

        ClassMapping embed = fm.getElementMapping().getEmbeddedMapping();
        fm = embed.getFieldMapping("basic");
        assertEquals(1, fm.getColumns().length);
        assertEquals("EM_BASIC", fm.getColumns()[0].getName());
    }

    public void testInsertAndRetrieve() {
        NonstandardMappingEntity pc = new NonstandardMappingEntity();
        pc.getSuperCollection().add("super");
        pc.setCustom(new Point(1, 2));
        pc.getStringCollection().add("string");
        NonstandardMappingEntity pc2 = new NonstandardMappingEntity();
        pc.getJoinCollection().add(pc2);
        pc.getStringMap().put("stringKey", "stringValue");
        NonstandardMappingEntity pc3 = new NonstandardMappingEntity();
        pc.getJoinMap().put(pc2, pc3);
        ExtensionsEntity embed = new ExtensionsEntity();
        embed.setExternalValues('M');
        embed.setExternalizer(String.class);
        pc.setEmbed(embed);
        EmbedValue2 embed2 = new EmbedValue2();
        embed2.setBasic("basic");
        pc.getEmbedCollection().add(embed2);

        OpenJPAEntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persistAll(new Object[]{ pc, pc2, pc3 });
        em.getTransaction().commit();
        Object pcId = em.getObjectId(pc);
        Object pc2Id = em.getObjectId(pc2);
        Object pc3Id = em.getObjectId(pc3);
        em.close();

        em = emf.createEntityManager();
        pc = em.find(NonstandardMappingEntity.class, pcId);
        assertEquals(1, pc.getSuperCollection().size());
        assertEquals("super", pc.getSuperCollection().get(0));
        assertEquals(1, pc.getCustom().x);
        assertEquals(2, pc.getCustom().y);
        assertEquals(1, pc.getStringCollection().size());
        assertEquals("string", pc.getStringCollection().get(0));
        assertEquals(1, pc.getJoinCollection().size());
        assertEquals(pc2Id, em.getObjectId(pc.getJoinCollection().get(0)));
        assertEquals(1, pc.getStringMap().size());
        assertEquals("stringValue", pc.getStringMap().get("stringKey"));
        assertEquals(1, pc.getJoinMap().size());
        Map.Entry entry = pc.getJoinMap().entrySet().iterator().next();
        assertEquals(pc2Id, em.getObjectId(entry.getKey()));
        assertEquals(pc3Id, em.getObjectId(entry.getValue()));
        assertEquals('M', pc.getEmbed().getExternalValues());
        assertEquals(String.class, pc.getEmbed().getExternalizer());
        assertEquals(1, pc.getEmbedCollection().size());
        assertEquals("basic", pc.getEmbedCollection().get(0).getBasic());
        em.close();
    }

    public void testInsertAndRetrieveEmbeddedCollection() {
        NonstandardMappingEntity pc = new NonstandardMappingEntity();
        EmbedValue2 embed2 = new EmbedValue2();
        embed2.setBasic("basic");
        pc.getEmbedCollection().add(embed2);

        OpenJPAEntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(pc);
        em.getTransaction().commit();
        Object pcId = em.getObjectId(pc);
        em.close();

        em = emf.createEntityManager();
        pc = em.find(NonstandardMappingEntity.class, pcId);
        assertEquals(1, pc.getEmbedCollection().size());
        assertEquals("basic", pc.getEmbedCollection().get(0).getBasic());
        em.close();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6507.java