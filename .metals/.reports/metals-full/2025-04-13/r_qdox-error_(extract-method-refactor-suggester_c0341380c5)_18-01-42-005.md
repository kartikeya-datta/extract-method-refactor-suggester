error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12605.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12605.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12605.java
text:
```scala
m@@odulesDir = serverEnvironment.getModulesDir();

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.as.osgi.service;

import static org.jboss.as.osgi.OSGiLogger.LOGGER;
import static org.jboss.as.osgi.OSGiConstants.SERVICE_BASE_NAME;
import static org.jboss.osgi.resolver.XResourceConstants.MODULE_IDENTITY_NAMESPACE;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.server.ServerEnvironmentService;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.msc.service.AbstractService;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.osgi.framework.Constants;
import org.jboss.osgi.framework.Services;
import org.jboss.osgi.repository.ArtifactProviderPlugin;
import org.jboss.osgi.repository.RepositoryResolutionException;
import org.jboss.osgi.repository.URLBasedResourceBuilder;
import org.jboss.osgi.resolver.XResource;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.resource.Capability;
import org.osgi.resource.Requirement;

/**
 * An {@link ArtifactProviderPlugin} that resolves artifacts from the local modules/bundles location
 *
 * @author thomas.diesler@jboss.com
 * @since 20-Jan-2012
 */
final class ModuleIdentityArtifactProvider extends AbstractService<Void> implements ArtifactProviderPlugin {

    public static final ServiceName SERVICE_NAME = SERVICE_BASE_NAME.append("artifact.provider");

    private final InjectedValue<BundleContext> injectedSystemContext = new InjectedValue<BundleContext>();
    private final InjectedValue<ServerEnvironment> injectedEnvironment = new InjectedValue<ServerEnvironment>();
    private ServiceRegistration registration;
    private File modulesDir;
    private File bundlesDir;

    static ServiceController<?> addService(final ServiceTarget target) {
        ModuleIdentityArtifactProvider service = new ModuleIdentityArtifactProvider();
        ServiceBuilder<?> builder = target.addService(SERVICE_NAME, service);
        builder.addDependency(ServerEnvironmentService.SERVICE_NAME, ServerEnvironment.class, service.injectedEnvironment);
        builder.addDependency(Services.SYSTEM_CONTEXT, BundleContext.class, service.injectedSystemContext);
        builder.addDependency(Services.FRAMEWORK_CREATE);
        builder.setInitialMode(Mode.PASSIVE);
        return builder.install();
    }

    private ModuleIdentityArtifactProvider() {
    }

    @Override
    public void start(StartContext context) throws StartException {
        BundleContext syscontext = injectedSystemContext.getValue();
        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put(Constants.SERVICE_RANKING, Integer.MAX_VALUE);
        registration = syscontext.registerService(ArtifactProviderPlugin.class.getName(), this, props);
        ServerEnvironment serverEnvironment = injectedEnvironment.getValue();
        modulesDir = new File(serverEnvironment.getHomeDir() + File.separator + "modules");
        bundlesDir = serverEnvironment.getBundlesDir();
    }

    @Override
    public void stop(StopContext context) {
        if (registration != null) {
            registration.unregister();
            registration = null;
        }
    }

    @Override
    public Collection<Capability> findProviders(Requirement req) {
        String namespace = req.getNamespace();
        List<Capability> result = new ArrayList<Capability>();
        if (MODULE_IDENTITY_NAMESPACE.equals(namespace)) {
            String strval = (String) req.getAttributes().get(MODULE_IDENTITY_NAMESPACE);
            ModuleIdentifier moduleIdentifier = ModuleIdentifier.fromString(strval);
            try {
                File contentFile = getRepositoryEntry(bundlesDir, moduleIdentifier);
                if (contentFile != null) {
                    URL baseURL = bundlesDir.toURI().toURL();
                    String contentPath = contentFile.toURI().toURL().toExternalForm();
                    contentPath = contentPath.substring(baseURL.toExternalForm().length());
                    XResource resource = URLBasedResourceBuilder.createResource(baseURL, contentPath);
                    result.add(resource.getIdentityCapability());
                } else {
                    contentFile = getRepositoryEntry(modulesDir, moduleIdentifier);
                    if (contentFile != null) {
                        URL baseURL = modulesDir.toURI().toURL();
                        String contentPath = contentFile.toURI().toURL().toExternalForm();
                        contentPath = contentPath.substring(baseURL.toExternalForm().length());
                        XResource resource = URLBasedResourceBuilder.createResource(baseURL, contentPath);
                        result.add(resource.getIdentityCapability());
                    }
                }
            } catch (RepositoryResolutionException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RepositoryResolutionException(ex);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Get file for the singe jar that corresponds to the given identifier
     */
    static File getRepositoryEntry(File rootDir, ModuleIdentifier identifier) throws IOException {

        String identifierPath = identifier.getName().replace('.', '/') + "/" + identifier.getSlot();
        File entryDir = new File(rootDir + "/" + identifierPath);
        if (entryDir.isDirectory() == false) {
            LOGGER.debugf("Cannot obtain directory: %s", entryDir);
            return null;
        }

        String[] files = entryDir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });
        if (files.length == 0) {
            LOGGER.debugf("Cannot find jar in: %s", entryDir);
            return null;
        }
        if (files.length > 1) {
            LOGGER.debugf("Multiple jars in: %s", entryDir);
            return null;
        }

        File entryFile = new File(entryDir + "/" + files[0]);
        if (entryFile.exists() == false) {
            LOGGER.debugf("File does not exist: %s", entryFile);
            return null;
        }

        return entryFile;
    }

    @Override
    public String toString() {
        return ModuleIdentityArtifactProvider.class.getSimpleName();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12605.java