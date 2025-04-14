error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7006.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7006.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7006.java
text:
```scala
S@@erviceRegistration<ObjectFactory> reg = context.registerService(ObjectFactory.class, of, props);

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.as.test.integration.osgi.jndi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.ObjectFactory;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.test.osgi.FrameworkUtils;
import org.jboss.osgi.metadata.OSGiManifestBuilder;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

/**
 * A test that deployes a bundle that exercises the {@link InitialContext}
 *
 * @author thomas.diesler@jboss.com
 * @author David Bosschaert
 * @since 05-May-2009
 */
@RunWith(Arquillian.class)
public class JNDITestCase {

    @ArquillianResource
    Bundle bundle;

    @Deployment
    public static JavaArchive createdeployment() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "example-jndi");
        archive.addClasses(FrameworkUtils.class);
        archive.setManifest(new Asset() {
            public InputStream openStream() {
                OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
                builder.addBundleSymbolicName(archive.getName());
                builder.addBundleManifestVersion(2);
                builder.addImportPackages(InitialContext.class);
                builder.addImportPackages(InitialContextFactoryBuilder.class);
                builder.addImportPackages(ServiceTracker.class);
                return builder.openStream();
            }
        });
        return archive;
    }

    @Test
    public void testJNDIAccess() throws Exception {
        bundle.start();
        InitialContext iniCtx = new InitialContext();
        Object lookup = iniCtx.lookup("java:jboss");
        assertNotNull("Lookup not null", lookup);

        final String value = "Bar";
        iniCtx.createSubcontext("test").bind("Foo", value);
        assertEquals(value, iniCtx.lookup("test/Foo"));
    }

    @Test
    public void testInitialContextFactoryBuilderService() throws Exception {
        bundle.start();
        BundleContext context = bundle.getBundleContext();
        InitialContextFactoryBuilder builder = FrameworkUtils.waitForService(context, InitialContextFactoryBuilder.class);

        InitialContextFactory factory = builder.createInitialContextFactory(null);
        Context iniCtx = factory.getInitialContext(null);

        Object lookup = iniCtx.lookup("java:jboss");
        assertNotNull("Lookup not null", lookup);
    }

    @Test
    public void testObjectFactoryOSGiService() throws Exception {
        InitialContext ictx = new InitialContext();

        try {
            ictx.lookup("testscheme:testing/123");
        } catch (NameNotFoundException nnfe) {
            // good
        }

        ObjectFactory of = new TestObjectFactory();

        BundleContext context = bundle.getBundleContext();
        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put("osgi.jndi.url.scheme", new String [] {"testscheme"});
        ServiceRegistration reg = context.registerService(ObjectFactory.class.getName(), of, props);

        boolean found = false;
        int i=0;
        while(i < 20) {
            try {
                assertEquals("Gotcha!", ictx.lookup("testscheme:testing/123"));
                found = true;
                break;
            } catch (NameNotFoundException nnfe) {
                // try again, functionality is enabled asynchronously so it might arrive in a bit
            }
            i++;
            Thread.sleep(100);
        }
        assertTrue(found);

        // Unregister the service, this should get rid of the URL handler in JNDI
        reg.unregister();

        boolean gone = false;
        int j=0;
        while (j < 20) {
            try {
                ictx.lookup("testscheme:testing/123");
                // functionality is removed asynchronously, try again
            } catch (NameNotFoundException nnfe) {
                gone = true;
                break;
            }

            j++;
            Thread.sleep(100);
        }
        assertTrue(gone);
    }

    public class TestObjectFactory implements ObjectFactory {
        @Override
        public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
            return new InitialContext(new Hashtable<String, Object>()) {
                @Override
                public Object lookup(Name name) throws NamingException {
                    return "Gotcha!";
                }
            };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7006.java