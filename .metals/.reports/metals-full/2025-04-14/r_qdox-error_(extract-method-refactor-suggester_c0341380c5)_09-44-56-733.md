error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11008.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11008.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11008.java
text:
```scala
w@@ar.addAsWebInfResource(BMPEntityBeanTestCase.class.getPackage(), "ejb-jar.xml", "ejb-jar.xml");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.test.integration.ejb.entity.bmp;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.RemoveException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests bean managed persistence
 */
@RunWith(Arquillian.class)
public class BMPEntityBeanTestCase {

    private static final String ARCHIVE_NAME = "SimpleLocalHomeTest.war";

    @ArquillianResource
    private InitialContext iniCtx;

    @Deployment
    public static Archive<?> deploy() {

        WebArchive war = ShrinkWrap.create(WebArchive.class, ARCHIVE_NAME);
        war.addPackage(BMPEntityBeanTestCase.class.getPackage());
        war.addAsWebInfResource("ejb/entity/bmp/ejb-jar.xml", "ejb-jar.xml");
        return war;
    }

    @Test
    public void testSimpleCreate() throws Exception {
        DataStore.DATA.clear();
        final BMPLocalHome home = getHome();
        final BMPLocalInterface ejbInstance = home.createWithValue("Hello");
        final Integer pk = (Integer) ejbInstance.getPrimaryKey();
        Assert.assertEquals("Hello", DataStore.DATA.get(pk));
    }

    @Test
    public void testFindByPrimaryKey() throws Exception {
        DataStore.DATA.clear();
        final BMPLocalHome home = getHome();
        DataStore.DATA.put(1099, "VALUE1099");
        BMPLocalInterface result = home.findByPrimaryKey(1099);
        Assert.assertEquals("VALUE1099", result.getMyField());
    }

    @Test
    public void testSingleResultFinderMethod() throws Exception {
        DataStore.DATA.clear();
        final BMPLocalHome home = getHome();
        DataStore.DATA.put(888, "VALUE888");
        BMPLocalInterface result = home.findByValue("VALUE888");
        Assert.assertEquals("VALUE888", result.getMyField());
        Assert.assertEquals(888, result.getPrimaryKey());
    }


    @Test
    public void testCollectionFinderMethod() throws Exception {
        DataStore.DATA.clear();
        final BMPLocalHome home = getHome();
        DataStore.DATA.put(1000, "Collection");
        DataStore.DATA.put(1001, "Collection");
        Collection<BMPLocalInterface> col = home.findCollection();
        for (BMPLocalInterface result : col) {
            Assert.assertEquals("Collection", result.getMyField());
        }
    }

    @Test
    public void testRemoveEntityBean() throws Exception {
        DataStore.DATA.clear();
        final BMPLocalHome home = getHome();
        DataStore.DATA.put(56, "Remove");
        BMPLocalInterface result = home.findByPrimaryKey(56);
        Assert.assertEquals("Remove", result.getMyField());
        result.remove();
        Assert.assertFalse(DataStore.DATA.containsKey(56));
        try {
            result.getMyField();
            throw new RuntimeException("Expected invocation on removed instance to fail");
        } catch (NoSuchObjectLocalException expected) {

        }
    }

    @Test
    public void testCreateRemoveCreate() throws Exception {
        DataStore.DATA.clear();
        final BMPLocalHome home = getHome();

        BMPLocalInterface result = home.createWithValueAndPk(88888, "Hello1");
        Assert.assertEquals("Hello1", result.getMyField());
        result.remove();
        Assert.assertFalse(DataStore.DATA.containsKey(88888));
        try {
            result.getMyField();
            throw new RuntimeException("Expected invocation on removed instance to fail");
        } catch (NoSuchObjectLocalException expected) {

        }
        result = home.createWithValueAndPk(88888, "Hello2");
        Assert.assertEquals("Hello2", result.getMyField());
        result.remove();
        Assert.assertFalse(DataStore.DATA.containsKey(88888));
        try {
            result.getMyField();
            throw new RuntimeException("Expected invocation on removed instance to fail");
        } catch (NoSuchObjectLocalException expected) {

        }
    }

    @Test
    public void testIsIdentical() throws Exception {
        DataStore.DATA.clear();
        final BMPLocalHome home = getHome();
        DataStore.DATA.put(40, "1");
        DataStore.DATA.put(41, "2");
        BMPLocalInterface bean1 = home.findByPrimaryKey(40);
        BMPLocalInterface bean1_2 = home.findByPrimaryKey(40);
        BMPLocalInterface bean2 = home.findByPrimaryKey(41);
        Assert.assertTrue(bean1.isIdentical(bean1_2));
        Assert.assertFalse(bean1.isIdentical(bean2));
    }

    @Test
    public void testEjbHomeMethod() throws Exception {
        final BMPLocalHome home = getHome();
        Assert.assertEquals(SimpleBMPBean.HOME_METHOD_RETURN, home.exampleHomeMethod());
    }

    @Test
    public void testGetEJBLocalHome() throws Exception {
        DataStore.DATA.clear();
        final BMPLocalHome home = getHome();
        DataStore.DATA.put(23, "23");
        BMPLocalInterface result = home.findByPrimaryKey(23);
        final BMPLocalHome home2 = (BMPLocalHome) result.getEJBLocalHome();
        Assert.assertEquals(SimpleBMPBean.HOME_METHOD_RETURN, home2.exampleHomeMethod());
    }

    @Test
    public void testHomeInterfaceEquality() throws Exception {
        final BMPLocalHome home1 = getHome();
        final BMPLocalHome home2 = getHome();
        Assert.assertEquals(home1, home2);
        Assert.assertEquals(home1.hashCode(), home2.hashCode());
        Assert.assertNotSame(home1, new BMPLocalHome() {

            @Override
            public BMPLocalInterface createEmpty() {
                return null;
            }

            @Override
            public BMPLocalInterface createWithValue(final String value) {
                return null;
            }

            @Override
            public BMPLocalInterface createWithValueAndPk(final Integer pk, final String value) {
                return null;
            }

            @Override
            public BMPLocalInterface findByPrimaryKey(final Integer primaryKey) {
                return null;
            }

            @Override
            public BMPLocalInterface findByValue(final String value) {
                return null;
            }

            @Override
            public Collection<BMPLocalInterface> findCollection() {
                return null;
            }

            @Override
            public int exampleHomeMethod() {
                return 0;
            }

            @Override
            public void remove(final Object primaryKey) throws RemoveException, EJBException {

            }
        });
    }

    private BMPLocalHome getHome() throws NamingException {
        return (BMPLocalHome) iniCtx.lookup("java:module/SimpleBMP!" + BMPLocalHome.class.getName());
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11008.java