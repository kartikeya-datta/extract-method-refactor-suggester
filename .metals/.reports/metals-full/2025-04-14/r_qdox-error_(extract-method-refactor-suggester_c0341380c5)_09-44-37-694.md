error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12484.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12484.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12484.java
text:
```scala
private final D@@eploymentChain deploymentChain =  new DeploymentChainImpl("test.chain");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.as.deployment;

import org.jboss.as.deployment.module.DeploymentModuleLoader;
import org.jboss.as.deployment.module.DeploymentModuleLoaderImpl;
import org.jboss.as.deployment.unit.DeploymentChain;
import org.jboss.as.deployment.unit.DeploymentChainImpl;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Test to verify the DeploymentManager correctly installs the deployment service.
 *
 * @author John E. Bailey
 */
public class DeploymentManagerTestCase {

    private ServiceContainer serviceContainer;
    private final DeploymentChain deploymentChain =  new DeploymentChainImpl();
    private final DeploymentModuleLoader deploymentModuleLoader = new DeploymentModuleLoaderImpl(null);
    private DeploymentManager deploymentManager;

    @Before
    public void setupDeploymentManager() throws Exception {
        serviceContainer = ServiceContainer.Factory.create();
        final ServiceName chainServiceName = ServiceName.JBOSS.append("deployment", "chain");
        final ServiceName moduleLoaderServiceName = ServiceName.JBOSS.append("deployment", "module", "loader");

        deploymentManager = new DeploymentManager(serviceContainer) {
            @Override
            protected ServiceName determineDeploymentChain() {
                return chainServiceName;
            }

            @Override
            protected ServiceName determineDeploymentModuleLoader() {
                return moduleLoaderServiceName;
            }
        };

        final BatchBuilder batchBuilder = serviceContainer.batchBuilder();
        batchBuilder.addService(chainServiceName, new PassThroughService(deploymentChain));
        batchBuilder.addService(moduleLoaderServiceName, new PassThroughService(deploymentModuleLoader));
        batchBuilder.install();
    }

    @After
    public void shutdown() {
        serviceContainer.shutdown();
    }

    @Test
    public void testDeployVirtualFile() throws Exception {
        final VirtualFile virtualFile = VFS.getChild(getResource("/test/deploymentOne"));

        deploymentManager.deploy(virtualFile);

        // Verify the DeploymentService is correctly setup
        final ServiceController<?> serviceController = serviceContainer.getService(DeploymentService.SERVICE_NAME.append(virtualFile.getPathName()));
        assertNotNull(serviceController);
        assertEquals(ServiceController.State.UP, serviceController.getState());
        final DeploymentService deploymentService = (DeploymentService)serviceController.getValue();
        assertNotNull(deploymentService);

        assertEquals(virtualFile.getPathName(), deploymentService.getDeploymentName());
        assertEquals(deploymentChain, getPrivateFieldValue(deploymentService, "deploymentChain", DeploymentChain.class));
        assertEquals(deploymentModuleLoader, getPrivateFieldValue(deploymentService, "deploymentModuleLoader", DeploymentModuleLoader.class));

        // Verify the mount service is setup
        ServiceController<?> mountServiceController = serviceContainer.getService(ServiceName.JBOSS.append("mounts").append(virtualFile.getPathName()));
        assertNotNull(mountServiceController);
        assertEquals(ServiceController.State.UP, mountServiceController.getState());
        assertNull(mountServiceController.getValue());
    }

    @Test
    public void testDeploymentException() throws Exception {
        final VirtualFile virtualFile = VFS.getChild("/test/bogus");

        try {
            deploymentManager.deploy(virtualFile);
            fail("Should have thrown a DeploymentException");
        } catch(DeploymentException expected){
        }
    }

    private URL getResource(final String path) {
        return DeploymentManagerTestCase.class.getResource(path);
    }

    private <T> T getPrivateFieldValue(final Object target, final String fieldName, final Class<T> fieldType) throws Exception {
        final Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T)field.get(target);
    }

    private static class PassThroughService<T> implements Service<T> {
        private final T value;

        private PassThroughService(T value) {
            this.value = value;
        }

        @Override
        public void start(StartContext context) throws StartException {
        }

        @Override
        public void stop(StopContext context) {
        }

        @Override
        public T getValue() throws IllegalStateException {
            return value;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12484.java