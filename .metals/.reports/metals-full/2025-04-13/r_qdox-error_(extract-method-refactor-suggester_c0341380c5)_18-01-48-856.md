error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6036.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6036.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6036.java
text:
```scala
m@@odulePath = modulePath.split(File.pathSeparator)[1];

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.as.test.integration.naming;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ALLOW_RESOURCE_SERVICE_RESTART;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.naming.subsystem.NamingSubsystemModel.BINDING;
import static org.jboss.as.naming.subsystem.NamingSubsystemModel.BINDING_TYPE;
import static org.jboss.as.naming.subsystem.NamingSubsystemModel.CLASS;
import static org.jboss.as.naming.subsystem.NamingSubsystemModel.MODULE;
import static org.jboss.as.naming.subsystem.NamingSubsystemModel.OBJECT_FACTORY;
import static org.jboss.as.naming.subsystem.NamingSubsystemModel.OBJECT_FACTORY_ENV;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.spi.ObjectFactory;

import org.junit.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.naming.subsystem.NamingExtension;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xnio.IoUtils;

/**
 * Test case for binding of {@link ObjectFactory} with environment properties (see AS7-4575). The test case deploys a module,
 * containing the object factory class, and then uses the {@link ManagementClient} to bind it. The factory, when invoked to
 * retrieve an instance, verifies that the env properties are the ones used in the binding management operation.
 *
 * @author Eduardo Martins
 */
@RunWith(Arquillian.class)
@ServerSetup(ObjectFactoryWithEnvironmentBindingTestCase.ObjectFactoryWithEnvironmentBindingTestCaseServerSetup.class)
public class ObjectFactoryWithEnvironmentBindingTestCase {

    private static Logger LOGGER = Logger.getLogger(ObjectFactoryWithEnvironmentBindingTestCase.class);

    // caution, must match module.xml
    private static final String MODULE_NAME = "objectFactoryWithEnvironmentBindingModule";
    private static final String MODULE_JAR_NAME = "objectFactoryWithEnvironmentBinding.jar";

    // the environment properties used in the binding operation
    private static final Map<String, String> ENVIRONMENT_PROPERTIES = getEnvironmentProperties();

    private static Map<String, String> getEnvironmentProperties() {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("p1", "v1");
        map.put("p2", "v2");
        return Collections.unmodifiableMap(map);
    }

    public static void validateEnvironmentProperties(Hashtable<?, ?> environment) throws IllegalArgumentException {
        for (Map.Entry<String, String> property : ENVIRONMENT_PROPERTIES.entrySet()) {
            String value = (String) environment.get(property.getKey());
            if (value == null || !value.equals(property.getValue())) {
                throw new IllegalArgumentException("Unexpected value for environment property named " + property.getKey()
                        + ": " + value);
            }
        }
    }

    static class ObjectFactoryWithEnvironmentBindingTestCaseServerSetup implements ServerSetupTask {

        @Override
        public void setup(final ManagementClient managementClient, final String containerId) throws Exception {

            deployModule();
            // bind the object factory
            final ModelNode address = createAddress();
            final ModelNode bindingAdd = new ModelNode();
            bindingAdd.get(OP).set(ADD);
            bindingAdd.get(OP_ADDR).set(address);
            bindingAdd.get(BINDING_TYPE).set(OBJECT_FACTORY);
            bindingAdd.get(MODULE).set(MODULE_NAME);
            bindingAdd.get(CLASS).set(ObjectFactoryWithEnvironmentBinding.class.getName());
            final ModelNode environment = new ModelNode();
            for (Map.Entry<String, String> property : ENVIRONMENT_PROPERTIES.entrySet()) {
                environment.add(property.getKey(), property.getValue());
            }
            bindingAdd.get(OBJECT_FACTORY_ENV).set(environment);
            final ModelNode addResult = managementClient.getControllerClient().execute(bindingAdd);
            Assert.assertFalse(addResult.get(FAILURE_DESCRIPTION).toString(), addResult.get(FAILURE_DESCRIPTION).isDefined());
            LOGGER.info("Object factory bound.");

        }

        private ModelNode createAddress() {
            final ModelNode address = new ModelNode();
            address.add(SUBSYSTEM, NamingExtension.SUBSYSTEM_NAME);
            address.add(BINDING, "java:global/b");
            return address;
        }

        @Override
        public void tearDown(final ManagementClient managementClient, final String containerId) throws Exception {
            try {
                // unbind the object factory
                final ModelNode bindingRemove = new ModelNode();
                bindingRemove.get(OP).set(REMOVE);
                bindingRemove.get(OP_ADDR).set(createAddress());
                bindingRemove.get(ALLOW_RESOURCE_SERVICE_RESTART).set(true);
                final ModelNode removeResult = managementClient.getControllerClient().execute(bindingRemove);
                Assert.assertFalse(removeResult.get(FAILURE_DESCRIPTION).toString(), removeResult.get(FAILURE_DESCRIPTION)
                        .isDefined());
                LOGGER.info("Object factory unbound.");
            } finally {
                undeployModule();
                LOGGER.info("Module undeployed.");
            }
        }
    }


    @Deployment
    public static JavaArchive deploy() {
        return ShrinkWrap.create(JavaArchive.class, "ObjectFactoryWithEnvironmentBindingTestCase.jar")
                .addClass(ObjectFactoryWithEnvironmentBindingTestCase.class);
    }

    @Test
    public void testBindingWithEnvironment() throws Exception {
        InitialContext context = new InitialContext();
        Assert.assertEquals("v1", context.lookup("java:global/b"));
    }

    private static void deployModule() throws IOException {
        File testModuleRoot = new File(getModulesHome(), MODULE_NAME);
        if (testModuleRoot.exists()) {
            throw new IllegalArgumentException(testModuleRoot + " already exists");
        }
        File testModuleMainDir = new File(testModuleRoot, "main");
        if (!testModuleMainDir.mkdirs()) {
            throw new IllegalArgumentException("Could not create " + testModuleMainDir);
        }
        Archive<?> moduleJar = ShrinkWrap.create(JavaArchive.class, MODULE_JAR_NAME)
                .addClass(ObjectFactoryWithEnvironmentBinding.class)
                .addClass(ObjectFactoryWithEnvironmentBindingTestCase.class);
        final InputStream moduleJarInputStream = moduleJar.as(ZipExporter.class).exportAsInputStream();
        try {
            copyFile(new File(testModuleMainDir, MODULE_JAR_NAME), moduleJarInputStream);
        } finally {
            IoUtils.safeClose(moduleJarInputStream);
        }

        URL moduleXmlURL = ObjectFactoryWithEnvironmentBindingTestCase.class.getResource(
                ObjectFactoryWithEnvironmentBindingTestCase.class.getSimpleName() + "-module.xml");
        if (moduleXmlURL == null) {
            throw new IllegalStateException("Could not find module.xml");
        }
        copyFile(new File(testModuleMainDir, "module.xml"), moduleXmlURL.openStream());
    }

    private static void undeployModule() {
        File testModuleRoot = new File(getModulesHome(), MODULE_NAME);
        deleteRecursively(testModuleRoot);
    }

    private static File getModulesHome() {
        String modulePath = System.getProperty("module.path", null);
        if (modulePath == null) {
            String jbossHome = System.getProperty("jboss.home", null);
            if (jbossHome == null) {
                throw new IllegalStateException("Neither -Dmodule.path nor -Djboss.home were set");
            }
            modulePath = jbossHome + File.separatorChar + "modules";
        } else {
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

    private static void copyFile(File target, InputStream src) throws IOException {
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

    private static void deleteRecursively(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (String name : file.list()) {
                    deleteRecursively(new File(file, name));
                }
            }
            file.delete();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6036.java