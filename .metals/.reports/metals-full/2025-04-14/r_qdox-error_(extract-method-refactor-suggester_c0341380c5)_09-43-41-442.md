error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8584.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8584.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8584.java
text:
```scala
j@@ar.addAsManifestResource(EJBSecurityTestCase.class.getPackage(), "ejb-jar.xml", "ejb-jar.xml");

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

package org.jboss.as.test.integration.ejb.security;

import org.jboss.as.test.categories.CommonCriteria;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.experimental.categories.Category;

import javax.ejb.EJBAccessException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import static org.junit.Assert.assertEquals;

/**
 * User: jpai
 */
@RunWith(Arquillian.class)
@Category(CommonCriteria.class)
public class EJBSecurityTestCase {
    private static Context ctx;

    @AfterClass
    public static void afterClass() throws NamingException {
        if (ctx != null)
            ctx.close();
    }

    @BeforeClass
    public static void beforeClass() throws NamingException {
        ctx = new InitialContext();
    }

    @Deployment
    public static JavaArchive createDeployment() {
        final JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "ejb-security-test.jar");
        jar.addPackage(AnnotatedSLSB.class.getPackage());
        jar.addAsManifestResource("ejb/security/ejb-jar.xml", "ejb-jar.xml");
        jar.addAsManifestResource(EJBSecurityTestCase.class.getPackage(), "jboss-ejb3.xml", "jboss-ejb3.xml");
        jar.addPackage(CommonCriteria.class.getPackage());
        return jar;
    }

    private static <T> T lookup(final Class<?> beanClass, final Class<T> viewClass) throws NamingException {
        if (ctx == null)
            ctx = new InitialContext(); // to circumvent an Arquillian issue
        return viewClass.cast(ctx.lookup("java:module/" + beanClass.getSimpleName() + "!" + viewClass.getName()));
    }

    @Test
    public void testDenyAllAnnotation() throws Exception {
        final Context ctx = new InitialContext();
        final Restriction restrictedBean = (Restriction) ctx.lookup("java:module/" + AnnotatedSLSB.class.getSimpleName() + "!" + Restriction.class.getName());
        try {
            restrictedBean.restrictedMethod();
            Assert.fail("Call to restrictedMethod() method was expected to fail");
        } catch (EJBAccessException ejbae) {
            // expected
        }

        final FullAccess fullAccessBean = (FullAccess) ctx.lookup("java:module/" + AnnotatedSLSB.class.getSimpleName() + "!" + FullAccess.class.getName());
        fullAccessBean.doAnything();

        final AnnotatedSLSB annotatedBean = (AnnotatedSLSB) ctx.lookup("java:module/" + AnnotatedSLSB.class.getSimpleName() + "!" + AnnotatedSLSB.class.getName());
        try {
            annotatedBean.restrictedMethod();
            Assert.fail("Call to restrictedMethod() method was expected to fail");
        } catch (EJBAccessException ejbae) {
            //expected
        }
        // full access, should work
        annotatedBean.doAnything();
        try {
            annotatedBean.restrictedBaseClassMethod();
            Assert.fail("Call to restrictedBaseClassMethod() method was expected to fail");
        } catch (EJBAccessException ejbae) {
            //expected
        }

        // should be accessible, since the overridden method isn't annotated with @DenyAll
        annotatedBean.overriddenMethod();

        final FullyRestrictedBean fullyRestrictedBean = (FullyRestrictedBean) ctx.lookup("java:module/" + FullyRestrictedBean.class.getSimpleName() + "!" + FullyRestrictedBean.class.getName());
        try {
            fullyRestrictedBean.overriddenMethod();
            Assert.fail("Call to overriddenMethod() method was expected to fail");

        } catch (EJBAccessException ejae) {
            // expected
        }

    }

    @Test
    public void testEJB2() throws Exception {
        // AS7-2809: if it deploys we're good
        final HelloRemote bean = lookup(HelloBean.class, HelloHome.class).create();
        final String result = bean.sayHello("EJB2");
        assertEquals("Hello EJB2", result);
    }

    @Test
    public void testExcludeList() throws Exception {
        final Context ctx = new InitialContext();
        final FullAccess fullAccessDDBean = (FullAccess) ctx.lookup("java:module/" + DDBasedSLSB.class.getSimpleName() + "!" + FullAccess.class.getName());
        fullAccessDDBean.doAnything();

        final DDBasedSLSB ddBasedSLSB = (DDBasedSLSB) ctx.lookup("java:module/" + DDBasedSLSB.class.getSimpleName() + "!" + DDBasedSLSB.class.getName());
        try {
            ddBasedSLSB.accessDenied();
            Assert.fail("Call to accessDenied() method was expected to fail");
        } catch (EJBAccessException ejbae) {
            // expected
        }
        try {
            ddBasedSLSB.onlyTestRoleCanAccess();
            Assert.fail("Call to onlyTestRoleCanAccess() method was expected to fail");
        } catch (EJBAccessException ejbae) {
            // expected since only TestRole can call that method
        }
    }

    /**
     * Tests that a bean which doesn't explicitly have a security domain configured, but still has EJB security related
     * annotations on it, is still considered secured and the security annotations are honoured
     *
     * @throws Exception
     */
    @Test
    public void testSecurityOnBeanInAbsenceOfExplicitSecurityDomain() throws Exception {
        final Context ctx = new InitialContext();
        // lookup the bean which doesn't explicitly have any security domain configured
        final Restriction restrictedBean = (Restriction) ctx.lookup("java:module/" + BeanWithoutExplicitSecurityDomain.class.getSimpleName() + "!" + Restriction.class.getName());
        try {
            // try invoking a method annotated @DenyAll (expected to fail)
            restrictedBean.restrictedMethod();
            Assert.fail("Call to restrictedMethod() method was expected to fail");
        } catch (EJBAccessException ejbae) {
            // expected
        }

        // lookup the bean which doesn't explicitly have any security domain configured
        final FullAccess fullAccessBean = (FullAccess) ctx.lookup("java:module/" + BeanWithoutExplicitSecurityDomain.class.getSimpleName() + "!" + FullAccess.class.getName());
        // invoke a @PermitAll method
        fullAccessBean.doAnything();

        // lookup the bean which doesn't explicitly have any security domain configured
        final BeanWithoutExplicitSecurityDomain specificRoleAccessBean = (BeanWithoutExplicitSecurityDomain) ctx.lookup("java:module/" + BeanWithoutExplicitSecurityDomain.class.getSimpleName() + "!" + BeanWithoutExplicitSecurityDomain.class.getName());
        try {
            // invoke a method which only a specific role can access.
            // this is expected to fail since we haven't logged in as any user
            specificRoleAccessBean.allowOnlyRoleTwoToAccess();
            Assert.fail("Invocation was expected to fail since only a specific role was expected to be allowed to access the bean method");
        } catch (EJBAccessException ejbae) {
            // expected
        }

    }

    /**
     * Tests that if a method of a EJB is annotated with a {@link javax.annotation.security.RolesAllowed} with empty value for the annotation
     * <code>@RolesAllowed({})</code> then access to that method by any user MUST throw an EJBAccessException. i.e. it should
     * behave like a @DenyAll
     *
     * @throws Exception
     */
    @Test
    public void testEmptyRolesAllowedAnnotationValue() throws Exception {
        final Context ctx = new InitialContext();

        final AnnotatedSLSB annotatedBean = (AnnotatedSLSB) ctx.lookup("java:module/" + AnnotatedSLSB.class.getSimpleName() + "!" + AnnotatedSLSB.class.getName());
        try {
            annotatedBean.methodWithEmptyRolesAllowedAnnotation();
            Assert.fail("Call to methodWithEmptyRolesAllowedAnnotation() method was expected to fail");
        } catch (EJBAccessException ejbae) {
            //expected
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8584.java