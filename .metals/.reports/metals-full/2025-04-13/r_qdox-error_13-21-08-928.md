error id: <WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6383.java
<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6383.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6383.java
text:
```scala
public v@@oid testBundleDeployment() throws Exception {

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
package org.jboss.as.test.embedded.osgi;

import java.io.InputStream;

import javax.inject.Inject;

import org.jboss.arquillian.api.ArchiveProvider;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.api.DeploymentProvider;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.test.embedded.osgi.bundle.SimpleActivator;
import org.jboss.as.test.embedded.osgi.bundle.SimpleService;
import org.jboss.osgi.testing.OSGiManifestBuilder;
import org.jboss.osgi.testing.OSGiTestHelper;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Test the arquillian callback to a client provided archive
 *
 * @author thomas.diesler@jboss.com
 * @since 09-Sep-2010
 */
@RunWith(Arquillian.class)
public class SimpleArchiveProviderTestCase {

    @Inject
    public DeploymentProvider provider;

    @Inject
    public BundleContext context;

    @Deployment
    public static JavaArchive createdeployment() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "example-deployment-provider");
        archive.setManifest(new Asset() {
            public InputStream openStream() {
                OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
                builder.addBundleSymbolicName(archive.getName());
                builder.addBundleManifestVersion(2);
                return builder.openStream();
            }
        });
        return archive;
    }

    @Test
    public void testBundleInjection() throws Exception {

        InputStream input = provider.getClientDeploymentAsStream("deployment-provider-bundle");
        Bundle bundle = context.installBundle("deployment-provider-bundle", input);
        try {
            // Assert that the bundle is in state INSTALLED
            OSGiTestHelper.assertBundleState(Bundle.INSTALLED, bundle.getState());

            // Start the bundle
            bundle.start();
            OSGiTestHelper.assertBundleState(Bundle.ACTIVE, bundle.getState());

            // Stop the bundle
            bundle.stop();
            OSGiTestHelper.assertBundleState(Bundle.RESOLVED, bundle.getState());
        } finally {
            bundle.uninstall();
            OSGiTestHelper.assertBundleState(Bundle.UNINSTALLED, bundle.getState());
        }
    }

    @ArchiveProvider
    public static JavaArchive getTestArchive(String name) {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class, name);
        archive.addClasses(SimpleActivator.class, SimpleService.class);
        archive.setManifest(new Asset() {
            public InputStream openStream() {
                OSGiManifestBuilder builder = OSGiManifestBuilder.newInstance();
                builder.addBundleSymbolicName(archive.getName());
                builder.addBundleManifestVersion(2);
                builder.addBundleActivator(SimpleActivator.class);
                builder.addImportPackages(BundleActivator.class);
                return builder.openStream();
            }
        });
        return archive;
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
	scala.meta.internal.mtags.MtagsIndexer.index(MtagsIndexer.scala:21)
	scala.meta.internal.mtags.MtagsIndexer.index$(MtagsIndexer.scala:20)
	scala.meta.internal.mtags.JavaMtags.index(JavaMtags.scala:38)
	scala.meta.internal.tvp.IndexedSymbols.javaSymbols(IndexedSymbols.scala:111)
	scala.meta.internal.tvp.IndexedSymbols.workspaceSymbolsFromPath(IndexedSymbols.scala:120)
	scala.meta.internal.tvp.IndexedSymbols.$anonfun$workspaceSymbols$2(IndexedSymbols.scala:146)
	scala.collection.concurrent.TrieMap.getOrElseUpdate(TrieMap.scala:960)
	scala.meta.internal.tvp.IndexedSymbols.$anonfun$workspaceSymbols$1(IndexedSymbols.scala:146)
	scala.meta.internal.tvp.IndexedSymbols.withTimer(IndexedSymbols.scala:71)
	scala.meta.internal.tvp.IndexedSymbols.workspaceSymbols(IndexedSymbols.scala:143)
	scala.meta.internal.tvp.FolderTreeViewProvider.$anonfun$projects$9(MetalsTreeViewProvider.scala:306)
	scala.collection.Iterator$$anon$9.next(Iterator.scala:584)
	scala.collection.Iterator$$anon$10.nextCur(Iterator.scala:594)
	scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:608)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:477)
	scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:601)
	scala.collection.Iterator$$anon$8.hasNext(Iterator.scala:562)
	scala.collection.immutable.List.prependedAll(List.scala:155)
	scala.collection.immutable.List$.from(List.scala:685)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.SeqFactory$Delegate.from(Factory.scala:306)
	scala.collection.immutable.Seq$.from(Seq.scala:42)
	scala.collection.IterableOnceOps.toSeq(IterableOnce.scala:1473)
	scala.collection.IterableOnceOps.toSeq$(IterableOnce.scala:1473)
	scala.collection.AbstractIterator.toSeq(Iterator.scala:1306)
	scala.meta.internal.tvp.ClasspathTreeView.children(ClasspathTreeView.scala:62)
	scala.meta.internal.tvp.FolderTreeViewProvider.getProjectRoot(MetalsTreeViewProvider.scala:390)
	scala.meta.internal.tvp.MetalsTreeViewProvider.$anonfun$children$1(MetalsTreeViewProvider.scala:84)
	scala.collection.immutable.List.map(List.scala:247)
	scala.meta.internal.tvp.MetalsTreeViewProvider.children(MetalsTreeViewProvider.scala:84)
	scala.meta.internal.metals.WorkspaceLspService.$anonfun$treeViewChildren$1(WorkspaceLspService.scala:705)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:687)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:467)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	java.base/java.lang.Thread.run(Thread.java:840)
```
#### Short summary: 

QDox parse error in <WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6383.java