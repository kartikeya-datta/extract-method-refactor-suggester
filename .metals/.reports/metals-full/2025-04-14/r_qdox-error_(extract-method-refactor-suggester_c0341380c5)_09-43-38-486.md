error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1418.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1418.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1418.java
text:
```scala
E@@arMetaData earMetaData = handleSpecMetadata(deploymentFile, SpecDescriptorPropertyReplacement.propertyReplacer(deploymentUnit));

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

package org.jboss.as.ee.structure;

import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.jboss.as.ee.component.DeploymentDescriptorEnvironment;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.metadata.ear.jboss.JBossAppMetaData;
import org.jboss.metadata.ear.spec.EarMetaData;
import org.jboss.metadata.merge.JBossAppMetaDataMerger;
import org.jboss.metadata.parser.jboss.JBossAppMetaDataParser;
import org.jboss.metadata.parser.spec.EarMetaDataParser;
import org.jboss.metadata.parser.util.NoopXMLResolver;
import org.jboss.metadata.property.PropertyReplacer;
import org.jboss.metadata.property.PropertyReplacers;
import org.jboss.metadata.property.PropertyResolver;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;

import static org.jboss.as.ee.EeMessages.MESSAGES;

/**
 * Deployment processor responsible for parsing the application.xml file of an ear.
 *
 * @author John Bailey
 */
public class EarMetaDataParsingProcessor implements DeploymentUnitProcessor {
    private static final String APPLICATION_XML = "META-INF/application.xml";
    private static final String JBOSS_APP_XML = "META-INF/jboss-app.xml";

    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        if (!DeploymentTypeMarker.isType(DeploymentType.EAR, deploymentUnit)) {
            return;
        }

        final ResourceRoot deploymentRoot = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.DEPLOYMENT_ROOT);
        final VirtualFile deploymentFile = deploymentRoot.getRoot();

        final PropertyResolver propertyResolver = deploymentUnit.getAttachment(org.jboss.as.ee.metadata.property.Attachments.FINAL_PROPERTY_RESOLVER);
        final PropertyReplacer propertyReplacer = PropertyReplacers.resolvingReplacer(propertyResolver);

        EarMetaData earMetaData = handleSpecMetadata(deploymentFile, propertyReplacer);
        JBossAppMetaData jbossMetaData = handleJbossMetadata(deploymentFile ,propertyReplacer);
        if (earMetaData == null && jbossMetaData == null) {
            return;
        }
        // the jboss-app.xml has a distinct-name configured then attach it to the deployment unit
        if (jbossMetaData != null && jbossMetaData.getDistinctName() != null) {
            deploymentUnit.putAttachment(Attachments.DISTINCT_NAME, jbossMetaData.getDistinctName());
        }
        JBossAppMetaData merged;
        if (earMetaData != null) {
            merged = new JBossAppMetaData(earMetaData.getEarVersion());
        } else {
            merged = new JBossAppMetaData();
        }
        JBossAppMetaDataMerger.merge(merged, jbossMetaData, earMetaData);

        deploymentUnit.putAttachment(Attachments.EAR_METADATA, merged);
        if (merged.getEarEnvironmentRefsGroup() != null) {
            final DeploymentDescriptorEnvironment bindings = new DeploymentDescriptorEnvironment("java:app/env/", merged.getEarEnvironmentRefsGroup());
            deploymentUnit.putAttachment(org.jboss.as.ee.component.Attachments.MODULE_DEPLOYMENT_DESCRIPTOR_ENVIRONMENT, bindings);
        }

    }

    private EarMetaData handleSpecMetadata(VirtualFile deploymentFile, final PropertyReplacer propertyReplacer) throws DeploymentUnitProcessingException {
        final VirtualFile applicationXmlFile = deploymentFile.getChild(APPLICATION_XML);
        if (!applicationXmlFile.exists()) {
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = applicationXmlFile.openStream();
            final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setXMLResolver(NoopXMLResolver.create());
            XMLStreamReader xmlReader = inputFactory.createXMLStreamReader(inputStream);
            return EarMetaDataParser.INSTANCE.parse(xmlReader, propertyReplacer);

        } catch (Exception e) {
            throw MESSAGES.failedToParse(e, applicationXmlFile);
        } finally {
            VFSUtils.safeClose(inputStream);
        }
    }


    private JBossAppMetaData handleJbossMetadata(VirtualFile deploymentFile, final PropertyReplacer propertyReplacer) throws DeploymentUnitProcessingException {
        final VirtualFile applicationXmlFile = deploymentFile.getChild(JBOSS_APP_XML);
        if (!applicationXmlFile.exists()) {
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = applicationXmlFile.openStream();
            final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setXMLResolver(NoopXMLResolver.create());
            XMLStreamReader xmlReader = inputFactory.createXMLStreamReader(inputStream);
            return JBossAppMetaDataParser.INSTANCE.parse(xmlReader, propertyReplacer);

        } catch (Exception e) {
            throw MESSAGES.failedToParse(e, applicationXmlFile);
        } finally {
            VFSUtils.safeClose(inputStream);
        }
    }

    public void undeploy(final DeploymentUnit deploymentUnit) {
        deploymentUnit.removeAttachment(Attachments.EAR_METADATA);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1418.java