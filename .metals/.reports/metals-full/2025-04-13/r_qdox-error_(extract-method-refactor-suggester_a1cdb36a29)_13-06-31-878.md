error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6034.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6034.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6034.java
text:
```scala
m@@odulePath = modulePath.split(File.pathSeparator)[1];

/*
* JBoss, Home of Professional Open Source.
* Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.test.integration.extension.remove;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Assert;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.api.ContainerResource;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.Extension;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xnio.IoUtils;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CHILDREN;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.EXTENSION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MODEL_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_DESCRIPTION_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RECURSIVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;

/**
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ExtensionRemoveTestCase {

    private static final String MODULE_NAME = "extensionremovemodule";
    private static final String JAR_NAME = "extension-remove-module.jar";

    @ContainerResource
    private ManagementClient managementClient;

    private Archive<?> createArchive() {
        ArchivePath path = ArchivePaths.create("/");
        path = ArchivePaths.create(path, "services");
        path = ArchivePaths.create(path, Extension.class.getName());
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, JAR_NAME);
        jar.addClass(TestExtension.class);
        jar.addAsManifestResource(ExtensionRemoveTestCase.class.getPackage(), Extension.class.getName(), path);
        return jar;
    }

    @Test
    public void testAddAndRemoveExtension() throws Exception {
        File testModuleRoot = new File(getModulePath(), MODULE_NAME);
        deleteRecursively(testModuleRoot);
        createTestModule(testModuleRoot);
        ModelControllerClient client = managementClient.getControllerClient();
        try {
            //Check extension and subsystem is not there
            Assert.assertFalse(readResource(client).get(EXTENSION, MODULE_NAME).isDefined());
            Assert.assertFalse(readResourceDescription(client).get(SUBSYSTEM, TestExtension.SUBSYSTEM_NAME).isDefined());
            Assert.assertFalse(readResource(client).get(SUBSYSTEM, TestExtension.SUBSYSTEM_NAME).isDefined());

            //Add extension, no subsystem yet
            executeOperation(client, ADD, PathAddress.pathAddress(PathElement.pathElement(EXTENSION, MODULE_NAME)), false);
            Assert.assertTrue(readResource(client).get(EXTENSION, MODULE_NAME).isDefined());
            Assert.assertTrue(readResourceDescription(client).get(CHILDREN, SUBSYSTEM, MODEL_DESCRIPTION, TestExtension.SUBSYSTEM_NAME).isDefined());
            Assert.assertFalse(readResource(client).get(SUBSYSTEM, TestExtension.SUBSYSTEM_NAME).isDefined());

            //Add subsystem
            executeOperation(client, ADD, PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, TestExtension.SUBSYSTEM_NAME)), false);
            executeOperation(client, ADD, PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, TestExtension.SUBSYSTEM_NAME), PathElement.pathElement("child", "one")), false);
            Assert.assertTrue(readResource(client).get(EXTENSION, MODULE_NAME).isDefined());
            Assert.assertTrue(readResourceDescription(client).get(CHILDREN, SUBSYSTEM, MODEL_DESCRIPTION, TestExtension.SUBSYSTEM_NAME).isDefined());
            Assert.assertTrue(readResource(client).get(SUBSYSTEM, TestExtension.SUBSYSTEM_NAME).isDefined());

            //Should not be possible to remove extension before subsystem is removed
            executeOperation(client, REMOVE, PathAddress.pathAddress(PathElement.pathElement(EXTENSION, MODULE_NAME)), true);
            Assert.assertTrue(readResource(client).get(EXTENSION, MODULE_NAME).isDefined());
            Assert.assertTrue(readResourceDescription(client).get(CHILDREN, SUBSYSTEM, MODEL_DESCRIPTION, TestExtension.SUBSYSTEM_NAME).isDefined());
            Assert.assertTrue(readResource(client).get(SUBSYSTEM, TestExtension.SUBSYSTEM_NAME).isDefined());

            //Remove subsystem
            executeOperation(client, REMOVE, PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, TestExtension.SUBSYSTEM_NAME), PathElement.pathElement("child", "one")), false);
            executeOperation(client, REMOVE, PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, TestExtension.SUBSYSTEM_NAME)), false);
            Assert.assertTrue(readResource(client).get(EXTENSION, MODULE_NAME).isDefined());
            Assert.assertTrue(readResourceDescription(client).get(CHILDREN, SUBSYSTEM, MODEL_DESCRIPTION, TestExtension.SUBSYSTEM_NAME).isDefined());
            Assert.assertFalse(readResource(client).get(SUBSYSTEM, TestExtension.SUBSYSTEM_NAME).isDefined());

            //Remove extension
            executeOperation(client, REMOVE, PathAddress.pathAddress(PathElement.pathElement(EXTENSION, MODULE_NAME)), false);
            Assert.assertFalse(readResource(client).get(EXTENSION, MODULE_NAME).isDefined());
            Assert.assertFalse(readResourceDescription(client).get(CHILDREN, SUBSYSTEM, MODEL_DESCRIPTION, TestExtension.SUBSYSTEM_NAME).isDefined());
            Assert.assertFalse(readResource(client).get(SUBSYSTEM, TestExtension.SUBSYSTEM_NAME).isDefined());

        } finally {
            deleteRecursively(testModuleRoot);
        }
    }

    private ModelNode executeOperation(ModelControllerClient client, String name, PathAddress address, boolean fail) throws IOException {
        ModelNode op = new ModelNode();
        op.get(OP).set(name);
        op.get(OP_ADDR).set(address.toModelNode());

        ModelNode result = client.execute(op);
        if (!fail) {
            Assert.assertFalse(result.get(FAILURE_DESCRIPTION).toString(), result.get(FAILURE_DESCRIPTION).isDefined());
        } else {
            Assert.assertTrue(result.get(FAILURE_DESCRIPTION).isDefined());
        }

        return result.get(RESULT);
    }

    private ModelNode readResourceDescription(ModelControllerClient client) throws IOException {
        ModelNode op = new ModelNode();
        op.get(OP).set(READ_RESOURCE_DESCRIPTION_OPERATION);
        op.get(RECURSIVE).set(true);

        ModelNode result = client.execute(op);
        Assert.assertFalse(result.hasDefined(FAILURE_DESCRIPTION));
        return result.get(RESULT);
    }

    private ModelNode readResource(ModelControllerClient client) throws IOException {
        ModelNode op = new ModelNode();
        op.get(OP).set(READ_RESOURCE_OPERATION);
        op.get(RECURSIVE).set(true);

        ModelNode result = client.execute(op);
        Assert.assertFalse(result.hasDefined(FAILURE_DESCRIPTION));
        return result.get(RESULT);
    }

    private void deleteRecursively(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (String name : file.list()) {
                    deleteRecursively(new File(file, name));
                }
            }
            file.delete();
        }
    }

    private void createTestModule(File testModuleRoot) throws IOException {
        if (testModuleRoot.exists()) {
            throw new IllegalArgumentException(testModuleRoot + " already exists");
        }
        File file = new File(testModuleRoot, "main");
        if (!file.mkdirs()) {
            throw new IllegalArgumentException("Could not create " + file);
        }
        final InputStream is = createArchive().as(ZipExporter.class).exportAsInputStream();
        try {
            copyFile(new File(file, JAR_NAME), is);
        } finally {
            IoUtils.safeClose(is);
        }

        URL url = this.getClass().getResource("module.xml");
        if (url == null) {
            throw new IllegalStateException("Could not find module.xml");
        }
        copyFile(new File(file, "module.xml"), url.openStream());
    }

    private void copyFile(File target, InputStream src) throws IOException {
        final BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(target));
        try {
            int i = src.read();
            while (i != -1) {
                out.write(i);
                i = src.read();
            }
        } finally {
            IoUtils.safeClose(out);
        }
    }

    private File getModulePath() {
        String modulePath = System.getProperty("module.path", null);
        if (modulePath == null) {
            String jbossHome = System.getProperty("jboss.home", null);
            if (jbossHome == null) {
                throw new IllegalStateException("Neither -Dmodule.path nor -Djboss.home were set");
            }
            modulePath = jbossHome + File.separatorChar + "modules";
        }else{
           modulePath = modulePath.split(File.pathSeparator)[0];
        }
        File moduleDir = new File(modulePath);
        if (!moduleDir.exists()) {
            throw new IllegalStateException("Determined module path does not exist");
        }
        if (!moduleDir.isDirectory()) {
            throw new IllegalStateException("Determined module path is not a dir");
        }
        return moduleDir;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6034.java