error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13087.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13087.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13087.java
text:
```scala
e@@.getLocation().getColumnNumber()));

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.wildfly.extension.undertow.deployment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.as.ee.structure.DeploymentType;
import org.jboss.as.ee.structure.DeploymentTypeMarker;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.wildfly.extension.undertow.logging.UndertowLogger;
import org.jboss.as.web.common.WarMetaData;
import org.jboss.metadata.parser.jsp.TldMetaDataParser;
import org.jboss.metadata.parser.util.NoopXMLResolver;
import org.jboss.metadata.web.jboss.JBossWebMetaData;
import org.jboss.metadata.web.spec.JspConfigMetaData;
import org.jboss.metadata.web.spec.ListenerMetaData;
import org.jboss.metadata.web.spec.TaglibMetaData;
import org.jboss.metadata.web.spec.TldMetaData;
import org.jboss.vfs.VirtualFile;

/**
 * @author Remy Maucherat
 */
public class TldParsingDeploymentProcessor implements DeploymentUnitProcessor {

    private static final String TLD = ".tld";
    private static final String META_INF = "META-INF";
    private static final String WEB_INF = "WEB-INF";
    private static final String CLASSES = "classes";
    private static final String LIB = "lib";
    private static final String IMPLICIT_TLD = "implicit.tld";

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        if (!DeploymentTypeMarker.isType(DeploymentType.WAR, deploymentUnit)) {
            return; // Skip non web deployments
        }
        final WarMetaData warMetaData = deploymentUnit.getAttachment(WarMetaData.ATTACHMENT_KEY);
        if (warMetaData == null || warMetaData.getMergedJBossWebMetaData() == null) {
            return;
        }


        TldsMetaData tldsMetaData = deploymentUnit.getAttachment(TldsMetaData.ATTACHMENT_KEY);
        if (tldsMetaData == null) {
            tldsMetaData = new TldsMetaData();
            deploymentUnit.putAttachment(TldsMetaData.ATTACHMENT_KEY, tldsMetaData);
        }
        Map<String, TldMetaData> tlds = new HashMap<String, TldMetaData>();
        tldsMetaData.setTlds(tlds);
        final List<TldMetaData> uniqueTlds = new ArrayList<>();

        final VirtualFile deploymentRoot = deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT).getRoot();
        final List<VirtualFile> testRoots = new ArrayList<VirtualFile>();
        testRoots.add(deploymentRoot);
        testRoots.add(deploymentRoot.getChild(WEB_INF));
        testRoots.add(deploymentRoot.getChild(META_INF));
        for (ResourceRoot root : deploymentUnit.getAttachmentList(Attachments.RESOURCE_ROOTS)) {
            testRoots.add(root.getRoot());
            testRoots.add(root.getRoot().getChild(META_INF));
        }

        JspConfigMetaData merged = warMetaData.getMergedJBossWebMetaData().getJspConfig();
        if (merged != null && merged.getTaglibs() != null) {
            for (final TaglibMetaData tld : merged.getTaglibs()) {
                boolean found = false;
                for (final VirtualFile root : testRoots) {
                    VirtualFile child = root.getChild(tld.getTaglibLocation());
                    if (child.exists()) {
                        String pathNameRelativeToRoot;
                        try {
                            pathNameRelativeToRoot = child.getPathNameRelativeTo(deploymentRoot);
                        } catch (IllegalArgumentException e) {
                            throw new DeploymentUnitProcessingException(UndertowLogger.ROOT_LOGGER.tldFileNotContainedInRoot(child.getPathName(),
                                    deploymentRoot.getPathName()), e);
                        }
                        final TldMetaData value = parseTLD(child);
                        value.setUri(tld.getTaglibUri());
                        uniqueTlds.add(value);
                        String key = "/" + pathNameRelativeToRoot;
                        if (!tlds.containsKey(key)) {
                            tlds.put(key, value);
                        }
                        if (!tlds.containsKey(tld.getTaglibUri())) {
                            tlds.put(tld.getTaglibUri(), value);
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    //todo: internataitonalization
                    UndertowLogger.ROOT_LOGGER.errorf("Could not find tld %s", tld.getTaglibLocation());
                }

            }
        }

        // TLDs are located in WEB-INF or any subdir (except the top level "classes" and "lib")
        // and in JARs from WEB-INF/lib, in META-INF or any subdir
        List<ResourceRoot> resourceRoots = deploymentUnit.getAttachmentList(Attachments.RESOURCE_ROOTS);
        for (ResourceRoot resourceRoot : resourceRoots) {
            if (resourceRoot.getRoot().getName().toLowerCase(Locale.ENGLISH).endsWith(".jar")) {
                VirtualFile webFragment = resourceRoot.getRoot().getChild(META_INF);
                if (webFragment.exists() && webFragment.isDirectory()) {
                    processTlds(deploymentRoot, webFragment.getChildren(), tlds, uniqueTlds);
                }
            }
        }
        VirtualFile webInf = deploymentRoot.getChild(WEB_INF);
        if (webInf.exists() && webInf.isDirectory()) {
            for (VirtualFile file : webInf.getChildren()) {
                if (file.isFile() && file.getName().toLowerCase(Locale.ENGLISH).endsWith(TLD)) {
                    String pathNameRelativeToRoot;
                    try {
                        pathNameRelativeToRoot = file.getPathNameRelativeTo(deploymentRoot);
                    } catch (IllegalArgumentException e) {
                        throw new DeploymentUnitProcessingException(UndertowLogger.ROOT_LOGGER.tldFileNotContainedInRoot(file.getPathName(),
                                deploymentRoot.getPathName()), e);
                    }

                    final TldMetaData value = parseTLD(file);
                    uniqueTlds.add(value);
                    String key = "/" + pathNameRelativeToRoot;
                    if (!tlds.containsKey(key)) {
                        tlds.put(key, value);
                    }
                } else if (file.isDirectory() && !CLASSES.equals(file.getName()) && !LIB.equals(file.getName())) {
                    processTlds(deploymentRoot, file.getChildren(), tlds, uniqueTlds);
                }
            }
        }

        JBossWebMetaData mergedMd = warMetaData.getMergedJBossWebMetaData();
        if (mergedMd.getListeners() == null) {
            mergedMd.setListeners(new ArrayList<ListenerMetaData>());
        }

        final ArrayList<TldMetaData> allTlds = new ArrayList<>(uniqueTlds);
        allTlds.addAll(tldsMetaData.getSharedTlds(deploymentUnit));


        for (final TldMetaData tld : allTlds) {
            if (tld.getListeners() != null) {
                for (ListenerMetaData l : tld.getListeners()) {
                    mergedMd.getListeners().add(l);
                }
            }
        }
    }


    @Override
    public void undeploy(final DeploymentUnit context) {
    }

    private void processTlds(VirtualFile root, List<VirtualFile> files, Map<String, TldMetaData> tlds, final List<TldMetaData> uniqueTlds)
            throws DeploymentUnitProcessingException {
        for (VirtualFile file : files) {
            if (file.isFile() && file.getName().toLowerCase(Locale.ENGLISH).endsWith(TLD)) {
                String pathNameRelativeToRoot;
                try {
                    pathNameRelativeToRoot = file.getPathNameRelativeTo(root);
                } catch (IllegalArgumentException e) {
                    throw new DeploymentUnitProcessingException(UndertowLogger.ROOT_LOGGER.tldFileNotContainedInRoot(file.getPathName(),
                            root.getPathName()), e);
                }
                final TldMetaData value = parseTLD(file);
                String key = "/" + pathNameRelativeToRoot;
                uniqueTlds.add(value);
                if (!tlds.containsKey(key)) {
                    tlds.put(key, value);
                }
            } else if (file.isDirectory()) {
                processTlds(root, file.getChildren(), tlds, uniqueTlds);
            }
        }
    }

    private TldMetaData parseTLD(VirtualFile tld)
            throws DeploymentUnitProcessingException {
        if (IMPLICIT_TLD.equals(tld.getName())) {
            // Implicit TLDs are different from regular TLDs
            return new TldMetaData();
        }
        InputStream is = null;
        try {
            is = tld.openStream();
            final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setXMLResolver(NoopXMLResolver.create());
            XMLStreamReader xmlReader = inputFactory.createXMLStreamReader(is);
            return TldMetaDataParser.parse(xmlReader);
        } catch (XMLStreamException e) {
            throw new DeploymentUnitProcessingException(UndertowLogger.ROOT_LOGGER.failToParseXMLDescriptor(tld, e.getLocation().getLineNumber(),
                    e.getLocation().getColumnNumber()), e);
        } catch (IOException e) {
            throw new DeploymentUnitProcessingException(UndertowLogger.ROOT_LOGGER.failToParseXMLDescriptor(tld), e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // Ignore
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13087.java