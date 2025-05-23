error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9482.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9482.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9482.java
text:
```scala
b@@uilder.append("org.jboss.as.arquillian.api,org.jboss.as.arquillian.container,org.jboss.as.osgi,");

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.arquillian.protocol.jmx;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.container.test.spi.TestDeployment;
import org.jboss.arquillian.container.test.spi.client.deployment.DeploymentPackager;
import org.jboss.arquillian.container.test.spi.client.deployment.ProtocolArchiveProcessor;
import org.jboss.arquillian.protocol.jmx.AbstractJMXProtocol;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.Authentication;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.arquillian.container.NetworkUtils;
import org.jboss.as.arquillian.protocol.jmx.JMXProtocolAS7.ServiceArchiveHolder;
import org.jboss.as.arquillian.service.ArquillianService;
import org.jboss.as.arquillian.service.InContainerManagementClientExtension;
import org.jboss.as.arquillian.service.JMXProtocolEndpointExtension;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.osgi.spi.ManifestBuilder;
import org.jboss.osgi.spi.OSGiManifestBuilder;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.UrlAsset;
import org.jboss.shrinkwrap.api.container.ManifestContainer;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.osgi.framework.Constants;

/**
 * A {@link DeploymentPackager} for the JMXProtocol.
 *
 * It dynamically generates the arquillian-archive, which is deployed to the
 * target container before the test run.
 *
 * @see ArquillianServiceDeployer
 *
 * @author Thomas.Diesler@jboss.com
 * @since 01-Jul-2011
 */
public class JMXProtocolPackager implements DeploymentPackager {

    private static final List<String> defaultDependencies = new ArrayList<String>();

    static {
        defaultDependencies.add("deployment.arquillian-service");
        defaultDependencies.add("org.jboss.modules");
        defaultDependencies.add("org.jboss.msc");
        defaultDependencies.add("org.osgi.core");
    }

    private static final Logger log = Logger.getLogger(JMXProtocolPackager.class);

    private ServiceArchiveHolder archiveHolder;

    JMXProtocolPackager(ServiceArchiveHolder archiveHolder) {
        this.archiveHolder = archiveHolder;
    }

    @Override
    public Archive<?> generateDeployment(TestDeployment testDeployment, Collection<ProtocolArchiveProcessor> protocolProcessors) {
        final Archive<?> appArchive = testDeployment.getApplicationArchive();
        if (archiveHolder.getArchive() == null) {
            final Collection<Archive<?>> auxArchives = testDeployment.getAuxiliaryArchives();
            JavaArchive archive = generateArquillianServiceArchive(auxArchives);
            archiveHolder.setArchive(archive);
        }
        addModulesManifestDependencies(appArchive);
        archiveHolder.addPreparedDeployment(testDeployment.getDeploymentName());
        return appArchive;
    }

    private JavaArchive generateArquillianServiceArchive(Collection<Archive<?>> auxArchives) {

        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "arquillian-service");
        log.debugf("Generating: %s", archive.getName());

        archive.addPackage(ArquillianService.class.getPackage());
        archive.addPackage(AbstractJMXProtocol.class.getPackage());
        //add the classes required for server setup
        archive.addClasses(ServerSetup.class, ServerSetupTask.class, ManagementClient.class, Authentication.class, NetworkUtils.class);

        // Merge the auxiliary archives and collect the loadable extensions
        final Set<String> loadableExtensions = new HashSet<String>();
        final String loadableExtensionsPath = "META-INF/services/" + RemoteLoadableExtension.class.getName();
        for (Archive<?> aux : auxArchives) {
            Node node = aux.get(loadableExtensionsPath);
            if (node != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(node.getAsset().openStream()));
                try {
                    String line = br.readLine();
                    while (line != null) {
                        loadableExtensions.add(line);
                        line = br.readLine();
                    }
                } catch (IOException ex) {
                    // ignore
                }
            }
            log.debugf("Merging archive: %s", aux);
            archive.merge(aux);
        }
        loadableExtensions.add(JMXProtocolEndpointExtension.class.getName());
        loadableExtensions.add(InContainerManagementClientExtension.class.getName());

        // Generate the manifest with it's dependencies
        archive.setManifest(new Asset() {
            public InputStream openStream() {
                ManifestBuilder builder = ManifestBuilder.newInstance();
                StringBuffer dependencies = new StringBuffer();
                dependencies.append("org.jboss.as.jmx,");
                dependencies.append("org.jboss.as.server,");
                dependencies.append("org.jboss.as.controller-client,");
                dependencies.append("org.jboss.as.osgi,");
                dependencies.append("org.jboss.jandex,");
                dependencies.append("org.jboss.logging,");
                dependencies.append("org.jboss.modules,");
                dependencies.append("org.jboss.dmr,");
                dependencies.append("org.jboss.msc,");
                dependencies.append("org.jboss.osgi.framework,");
                dependencies.append("org.osgi.core");
                builder.addManifestHeader("Dependencies", dependencies.toString());
                return builder.openStream();
            }
        });

        // Add the ServiceActivator
        String serviceActivatorPath = "META-INF/services/" + ServiceActivator.class.getName();
        final URL serviceActivatorURL = this.getClass().getClassLoader().getResource("arquillian-service/" + serviceActivatorPath);
        if (serviceActivatorURL == null) {
            throw new RuntimeException("No arquillian-service/" + serviceActivatorPath + " found by classloader: " + this.getClass().getClassLoader());
        }
        archive.addAsResource(new UrlAsset(serviceActivatorURL), serviceActivatorPath);

        // Add resource capabilities for registration with the Environment
        archive.addAsResource(new Asset() {
            public InputStream openStream() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    Properties props = new Properties();
                    props.setProperty(Constants.BUNDLE_SYMBOLICNAME, "arquillian-service");
                    StringBuilder builder = new StringBuilder();
                    builder.append("org.jboss.arquillian.container.test.api,org.jboss.arquillian.junit,");
                    builder.append("org.jboss.arquillian.osgi,org.jboss.arquillian.test.api,");
                    builder.append("org.jboss.as.osgi,");
                    builder.append("org.jboss.shrinkwrap.api,org.jboss.shrinkwrap.api.asset,org.jboss.shrinkwrap.api.spec,");
                    builder.append("org.junit,org.junit.runner");
                    props.setProperty(Constants.EXPORT_PACKAGE, builder.toString());
                    props.store(baos, null);
                } catch (IOException ex) {
                    throw new IllegalStateException("Cannot write osgi metadata", ex);
                }
                return new ByteArrayInputStream(baos.toByteArray());
            }

        }, "META-INF/jbosgi-xservice.properties");

        // Replace the loadable extensions with the collected set
        archive.delete(ArchivePaths.create(loadableExtensionsPath));
        archive.addAsResource(new Asset() {
            @Override
            public InputStream openStream() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(baos));
                for (String line : loadableExtensions) {
                    pw.println(line);
                }
                pw.close();
                return new ByteArrayInputStream(baos.toByteArray());
            }
        }, loadableExtensionsPath);

        log.debugf("Loadable extensions: %s", loadableExtensions);
        log.tracef("Archive content: %s\n%s", archive, archive.toString(true));
        return archive;
    }

    /**
     * Adds the Manifest Attribute "Dependencies" with the required dependencies for JBoss Modules to depend on the Arquillian Service.
     *
     * @param appArchive The Archive to deploy
     */
    private void addModulesManifestDependencies(Archive<?> appArchive) {
        if (appArchive instanceof ManifestContainer<?> == false)
            throw new IllegalArgumentException("ManifestContainer expected " + appArchive);

        final Manifest manifest = ManifestUtils.getOrCreateManifest(appArchive);

        // Don't enrich with Modules Dependencies if this is a OSGi bundle
        if (OSGiManifestBuilder.isValidBundleManifest(manifest)) {
            return;
        }
        Attributes attributes = manifest.getMainAttributes();
        if (attributes.getValue(Attributes.Name.MANIFEST_VERSION.toString()) == null) {
            attributes.putValue(Attributes.Name.MANIFEST_VERSION.toString(), "1.0");
        }
        String value = attributes.getValue("Dependencies");
        StringBuffer moduleDeps = new StringBuffer(value != null && value.trim().length() > 0 ? value : "org.jboss.modules");
        for (String dep : defaultDependencies) {
            if (moduleDeps.indexOf(dep) < 0)
                moduleDeps.append("," + dep);
        }

        log.debugf("Add dependencies: %s", moduleDeps);
        attributes.putValue("Dependencies", moduleDeps.toString());

        // Add the manifest to the archive
        ArchivePath manifestPath = ArchivePaths.create(JarFile.MANIFEST_NAME);
        appArchive.delete(manifestPath);
        appArchive.add(new Asset() {
            public InputStream openStream() {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    manifest.write(baos);
                    return new ByteArrayInputStream(baos.toByteArray());
                } catch (IOException ex) {
                    throw new IllegalStateException("Cannot write manifest", ex);
                }
            }
        }, manifestPath);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9482.java