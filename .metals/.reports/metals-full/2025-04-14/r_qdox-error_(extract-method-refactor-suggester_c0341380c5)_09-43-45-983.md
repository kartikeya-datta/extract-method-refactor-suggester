error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6111.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6111.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6111.java
text:
```scala
t@@arget.addService(ServiceName.JBOSS.append("jdbc-driver", driverName.replaceAll(".", "_")), driverService)

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

package org.jboss.as.connector.subsystems.datasources;

import static org.jboss.as.connector.subsystems.datasources.Constants.DRIVER_CLASS_NAME;
import static org.jboss.as.connector.subsystems.datasources.Constants.DRIVER_MAJOR_VERSION;
import static org.jboss.as.connector.subsystems.datasources.Constants.DRIVER_MINOR_VERSION;
import static org.jboss.as.connector.subsystems.datasources.Constants.DRIVER_MODULE_NAME;
import static org.jboss.as.connector.subsystems.datasources.Constants.DRIVER_NAME;
import static org.jboss.as.connector.subsystems.datasources.Constants.DRIVER_XA_DATASOURCE_CLASS_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

import java.lang.reflect.Constructor;
import java.sql.Driver;
import java.util.ServiceLoader;

import org.jboss.as.connector.ConnectorServices;
import org.jboss.as.connector.registry.DriverRegistry;
import org.jboss.as.connector.registry.DriverService;
import org.jboss.as.connector.registry.InstalledDriver;
import org.jboss.as.controller.BasicOperationResult;
import org.jboss.as.controller.ModelAddOperationHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationResult;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ResultHandler;
import org.jboss.as.controller.RuntimeTask;
import org.jboss.as.controller.RuntimeTaskContext;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;

/**
 * Operation handler responsible for adding a jdbc driver.
 * @author John Bailey
 */
public class JdbcDriverAdd implements ModelAddOperationHandler {
    static final JdbcDriverAdd INSTANCE = new JdbcDriverAdd();

    public static final Logger log = Logger.getLogger("org.jboss.as.connector.subsystems.datasources");

    @Override
    public OperationResult execute(final OperationContext context, final ModelNode operation, final ResultHandler resultHandler)
            throws OperationFailedException {
        final ModelNode address = operation.require(OP_ADDR);
        final PathAddress pathAddress = PathAddress.pathAddress(address);

        final String driverName = operation.require(DRIVER_NAME).asString();
        final String moduleName = operation.require(DRIVER_MODULE_NAME).asString();

        final Integer majorVersion = operation.hasDefined(DRIVER_MAJOR_VERSION) ? operation.get(DRIVER_MAJOR_VERSION).asInt()
                : null;
        final Integer minorVersion = operation.hasDefined(DRIVER_MINOR_VERSION) ? operation.get(DRIVER_MINOR_VERSION).asInt()
                : null;
        final String driverClassName = operation.hasDefined(DRIVER_CLASS_NAME) ? operation.get(DRIVER_CLASS_NAME).asString()
                : null;
        final String xaDataSourceClassName = operation.hasDefined(DRIVER_XA_DATASOURCE_CLASS_NAME) ? operation.get(
                DRIVER_XA_DATASOURCE_CLASS_NAME).asString() : null;

        // Apply to the model
        final ModelNode model = context.getSubModel();
        model.get(DRIVER_NAME).set(driverName);
        model.get(DRIVER_MODULE_NAME).set(moduleName);
        if (majorVersion != null)
            model.get(DRIVER_MAJOR_VERSION).set(majorVersion);
        if (minorVersion != null)
            model.get(DRIVER_MINOR_VERSION).set(minorVersion);
        if (driverClassName != null)
            model.get(DRIVER_CLASS_NAME).set(driverClassName);
        if (xaDataSourceClassName != null)
            model.get(DRIVER_XA_DATASOURCE_CLASS_NAME).set(xaDataSourceClassName);

        // Compensating is remove
        final ModelNode compensating = Util.getResourceRemoveOperation(address);

        if (context.getRuntimeContext() != null) {
            context.getRuntimeContext().setRuntimeTask(new RuntimeTask() {
                @Override
                public void execute(RuntimeTaskContext context) throws OperationFailedException {
                    final ServiceTarget target = context.getServiceTarget();

                    final ModuleIdentifier moduleId;
                    final Module module;
                    try {
                        moduleId = ModuleIdentifier.create(moduleName);
                        module = Module.getCallerModuleLoader().loadModule(moduleId);
                    } catch (ModuleLoadException e) {
                        throw new OperationFailedException(e, new ModelNode().set("Failed to load module for driver ["
                                + moduleName + "]"));
                    }

                    if (driverClassName == null) {
                        final ServiceLoader<Driver> serviceLoader = module.loadService(Driver.class);
                        if (serviceLoader != null)
                            for (Driver driver : serviceLoader) {

                                startDriverServices(target, moduleId, driver);

                            }
                    } else {
                        try {
                            final Class<? extends Driver> driverClass = module.getClassLoader().loadClass(driverClassName)
                                    .asSubclass(Driver.class);
                            final Constructor<? extends Driver> constructor = driverClass.getConstructor();
                            final Driver driver = constructor.newInstance();
                            startDriverServices(target, moduleId, driver);
                        } catch (Exception e) {
                            log.warnf("Unable to instantiate driver class \"%s\": %s", driverClassName, e);
                        }
                    }
                    resultHandler.handleResultComplete();
                }

                private void startDriverServices(final ServiceTarget target, final ModuleIdentifier moduleId, Driver driver)
                        throws IllegalStateException {
                    final int majorVer = driver.getMajorVersion();
                    final int minorVer = driver.getMinorVersion();
                    if ((majorVersion != null && majorVersion.intValue() != majorVer)
 (minorVersion != null && minorVersion.intValue() != minorVer)) {
                        throw new IllegalStateException("Specified driver version doesn't match with actual driver version");
                    }

                    final boolean compliant = driver.jdbcCompliant();
                    if (compliant) {
                        log.infof("Deploying JDBC-compliant driver %s (version %d.%d)", driver.getClass(),
                                Integer.valueOf(majorVer), Integer.valueOf(minorVer));
                    } else {
                        log.infof("Deploying non-JDBC-compliant driver %s (version %d.%d)", driver.getClass(),
                                Integer.valueOf(majorVer), Integer.valueOf(minorVer));
                    }
                    InstalledDriver driverMetadata = new InstalledDriver(driverName, moduleId, driver.getClass().getName(),
                            xaDataSourceClassName, majorVer, minorVer, compliant);
                    DriverService driverService = new DriverService(driverMetadata, driver);
                    target.addService(ServiceName.JBOSS.append("jdbc-driver", driverName), driverService)
                            .addDependency(ConnectorServices.JDBC_DRIVER_REGISTRY_SERVICE, DriverRegistry.class,
                                    driverService.getDriverRegistryServiceInjector())
                            .setInitialMode(ServiceController.Mode.ACTIVE).install();
                }
            });
        } else {
            resultHandler.handleResultComplete();
        }
        return new BasicOperationResult(compensating);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6111.java