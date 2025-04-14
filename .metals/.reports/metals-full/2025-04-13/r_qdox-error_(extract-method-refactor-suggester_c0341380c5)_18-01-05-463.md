error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7836.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7836.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7836.java
text:
```scala
A@@ssert.assertEquals(MethodInterceptor.MESSAGE + "Hello", message);

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.as.test.integration.ejb.interceptor.defaultinterceptor;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests that default interceptors are correctly applied
 *
 * @author Stuart Douglas
 */
@RunWith(Arquillian.class)
public class DefaultInterceptorsTestCase {

    @Deployment
    public static Archive<?> deploy() {
        JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "testdefaultinterceptors.jar");
        archive.addPackage(DefaultInterceptorsTestCase.class.getPackage());
        archive.addAsManifestResource(new StringAsset("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<ejb-jar xmlns=\"http://java.sun.com/xml/ns/javaee\"\n" +
                "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/ejb-jar_3_0.xsd\"\n" +
                "         version=\"3.0\">\n" +
                "    <interceptors>\n" +
                "      <interceptor>\n" +
                "         <interceptor-class>" + DefaultInterceptor.class.getName() + "</interceptor-class>\n" +
                "      </interceptor>\n" +
                "      <interceptor>\n" +
                "         <interceptor-class>" + ClassInterceptor.class.getName() + "</interceptor-class>\n" +
                "      </interceptor>\n" +
                "      <interceptor>\n" +
                "         <interceptor-class>" + MethodInterceptor.class.getName() + "</interceptor-class>\n" +
                "      </interceptor>\n" +
                "    </interceptors>\n" +
                "   <assembly-descriptor>\n" +
                "      <interceptor-binding>\n" +
                "         <ejb-name>*</ejb-name>\n" +
                "         <interceptor-class>" + DefaultInterceptor.class.getName() + "</interceptor-class>\n" +
                "      </interceptor-binding>\n" +
                "      <interceptor-binding>\n" +
                "         <ejb-name>NoDefaultInterceptorsSLSB</ejb-name>\n" +
                "         <interceptor-class>" + ClassInterceptor.class.getName() + "</interceptor-class>\n" +
                "      </interceptor-binding>\n" +
                "      <interceptor-binding>\n" +
                "         <ejb-name>NoDefaultInterceptorsSLSB</ejb-name>\n" +
                "         <interceptor-class>" + MethodInterceptor.class.getName() + "</interceptor-class>\n" +
                "         <method><method-name>noClassLevel</method-name></method>" +
                "      </interceptor-binding>\n" +
                "   </assembly-descriptor>\n" +
                "\n" +
                "</ejb-jar>"), "ejb-jar.xml");
        return archive;
    }


    @Test
    public void testDefaultInterceptorApplied() throws NamingException {
        InitialContext ctx = new InitialContext();
        DefaultInterceptedSLSB bean = (DefaultInterceptedSLSB) ctx.lookup("java:module/" + DefaultInterceptedSLSB.class.getSimpleName());
        final String message = bean.message();
        Assert.assertEquals(DefaultInterceptor.MESSAGE + "Hello", message);
        Assert.assertTrue(bean.isPostConstructCalled());
    }

    /**
     * AS7-1436
     * <p/>
     * Test interceptor is applied twice, if it is both class level and a default interceptor
     *
     * @throws NamingException
     */
    @Test
    public void testDefaultInterceptorAppliedTwice() throws NamingException {
        InitialContext ctx = new InitialContext();
        RepeatedDefaultInterceptedSLSB bean = (RepeatedDefaultInterceptedSLSB) ctx.lookup("java:module/" + RepeatedDefaultInterceptedSLSB.class.getSimpleName());
        final String message = bean.message();
        Assert.assertEquals(DefaultInterceptor.MESSAGE + DefaultInterceptor.MESSAGE + DefaultInterceptor.MESSAGE + "Hello", message);
        Assert.assertTrue(bean.isPostConstructCalled());
    }


    @Test
    public void testClassLevelExcludeDefaultInterceptors() throws NamingException {
        InitialContext ctx = new InitialContext();
        NoDefaultInterceptorsSLSB bean = (NoDefaultInterceptorsSLSB) ctx.lookup("java:module/" + NoDefaultInterceptorsSLSB.class.getSimpleName());
        final String message = bean.message();
        Assert.assertEquals(ClassInterceptor.MESSAGE + "Hello", message);
        Assert.assertTrue(!bean.isPostConstructCalled());
    }

    @Test
    public void testClassLevelExcludeDefaultMethodLevelExcludeClassInterceptors() throws NamingException {
        InitialContext ctx = new InitialContext();
        NoDefaultInterceptorsSLSB bean = (NoDefaultInterceptorsSLSB) ctx.lookup("java:module/" + NoDefaultInterceptorsSLSB.class.getSimpleName());
        final String message = bean.noClassLevel();
        Assert.assertEquals("Hello", MethodInterceptor.MESSAGE + message);
        Assert.assertTrue(!bean.isPostConstructCalled());
    }

    @Test
    public void testMethodLevelExcludeDefaultInterceptors() throws NamingException {
        InitialContext ctx = new InitialContext();
        RepeatedDefaultInterceptedSLSB bean = (RepeatedDefaultInterceptedSLSB) ctx.lookup("java:module/" + RepeatedDefaultInterceptedSLSB.class.getSimpleName());
        final String message = bean.noClassLevel();
        Assert.assertEquals(DefaultInterceptor.MESSAGE + "Hello", message);
        Assert.assertTrue(bean.isPostConstructCalled());
    }

    @Test
    public void testMethodLevelExcludeDefaultAndClassInterceptors() throws NamingException {
        InitialContext ctx = new InitialContext();
        RepeatedDefaultInterceptedSLSB bean = (RepeatedDefaultInterceptedSLSB) ctx.lookup("java:module/" + RepeatedDefaultInterceptedSLSB.class.getSimpleName());
        final String message = bean.noClassLevelOrDefault();
        Assert.assertEquals("Hello", message);
        Assert.assertTrue(bean.isPostConstructCalled());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7836.java