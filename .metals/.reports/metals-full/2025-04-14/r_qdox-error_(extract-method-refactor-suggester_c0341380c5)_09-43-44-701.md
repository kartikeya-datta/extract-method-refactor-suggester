error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5033.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5033.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5033.java
text:
```scala
private static final L@@ogger logger = Logger.getLogger(EjbJarParsingDeploymentUnitProcessor.class);

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

package org.jboss.as.ejb3.deployment.processors;


import org.jboss.as.ee.component.EEApplicationClasses;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.metadata.MetadataCompleteMarker;
import org.jboss.as.ejb3.deployment.EjbDeploymentAttachmentKeys;
import org.jboss.as.ejb3.deployment.EjbDeploymentMarker;
import org.jboss.as.ejb3.deployment.EjbJarDescription;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.logging.Logger;
import org.jboss.metadata.ejb.parser.spec.EjbJarMetaDataParser;
import org.jboss.metadata.ejb.spec.EjbJar31MetaData;
import org.jboss.metadata.ejb.spec.EjbJarMetaData;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.vfs.VirtualFile;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Processes a {@link DeploymentUnit} containing a ejb-jar.xml and creates {@link EjbJarMetaData}
 * for that unit.
 * <p/>
 * This {@link DeploymentUnitProcessor deployment unit processor} looks for ejb-jar.xml in META-INF of a .jar
 * and WEB-INF of a .war file. If it finds the ejb-jar.xml in these locations, it parses that file and creates
 * {@link EjbJarMetaData} out of it. The {@link EjbJarMetaData} is then attached to the {@link DeploymentUnit}
 * with {@link org.jboss.as.ejb3.deployment.EjbDeploymentAttachmentKeys#EJB_JAR_METADATA} as the key.
 * <p/>
 * <p/>
 * Author: Jaikiran Pai
 */
public class EjbJarParsingDeploymentUnitProcessor implements DeploymentUnitProcessor {

    /**
     * Logger
     */
    private static Logger logger = Logger.getLogger(EjbJarParsingDeploymentUnitProcessor.class);

    /**
     * .war file extension
     */
    private static final String WAR_FILE_EXTENSION = ".war";

    /**
     * .jar file extension
     */
    private static final String JAR_FILE_EXTENSION = ".jar";

    /**
     * Location of ejb-jar.xml packaged in a .war
     */
    private static final String EJB_JAR_XML_LOCATION_IN_WAR = "WEB-INF/ejb-jar.xml";

    /**
     * Location of ejb-jar.xml packaged in a .jar
     */
    private static final String EJB_JAR_XML_LOCATION_IN_JAR = "META-INF/ejb-jar.xml";

    /**
     * Finds a ejb-jar.xml (at WEB-INF of a .war or META-INF of a .jar) parses the file and creates
     * metadata out of it. The metadata is then attached to the deployment unit.
     *
     * @param deploymentPhase
     * @throws DeploymentUnitProcessingException
     *
     */
    @Override
    public void deploy(DeploymentPhaseContext deploymentPhase) throws DeploymentUnitProcessingException {

        // get hold of the deployment unit.
        DeploymentUnit deploymentUnit = deploymentPhase.getDeploymentUnit();

        // get the root of the deployment unit
        VirtualFile deploymentRoot = deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT).getRoot();

        final EEModuleDescription eeModuleDescription = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION);
        final EEApplicationClasses applicationClassesDescription = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_APPLICATION_CLASSES_DESCRIPTION);

        // Locate a ejb-jar.xml
        VirtualFile ejbJarXml = null;
        // EJB 3.1 FR 20.4 Enterprise Beans Packaged in a .war
        // TODO: Is there a better way to do this?
        if (deploymentRoot.getName().toLowerCase().endsWith(WAR_FILE_EXTENSION)) {
            // it's a .war file, so look for the ejb-jar.xml in WEB-INF
            ejbJarXml = deploymentRoot.getChild(EJB_JAR_XML_LOCATION_IN_WAR);
        } else if (deploymentRoot.getName().toLowerCase().endsWith(JAR_FILE_EXTENSION)) {
            ejbJarXml = deploymentRoot.getChild(EJB_JAR_XML_LOCATION_IN_JAR);
        } else {
            // neither a .jar nor a .war. Return
            return;
        }

        if (ejbJarXml == null || !ejbJarXml.exists()) {
            // no ejb-jar.xml found, nothing to do!
            return;
        }
        // Mark it as a EJB deployment
        EjbDeploymentMarker.mark(deploymentUnit);
        if (!deploymentUnit.hasAttachment(EjbDeploymentAttachmentKeys.EJB_JAR_DESCRIPTION)) {
            final EEModuleDescription moduleDescription = deploymentUnit.getAttachment(org.jboss.as.ee.component.Attachments.EE_MODULE_DESCRIPTION);
            final EjbJarDescription ejbModuleDescription = new EjbJarDescription(moduleDescription, applicationClassesDescription, deploymentUnit.getName().endsWith(".war"));
            deploymentUnit.putAttachment(EjbDeploymentAttachmentKeys.EJB_JAR_DESCRIPTION, ejbModuleDescription);
        }

        // get the XMLStreamReader and parse the ejb-jar.xml
        MetaDataElementParser.DTDInfo dtdInfo = new MetaDataElementParser.DTDInfo();
        InputStream stream = null;
        try {
            stream = ejbJarXml.openStream();

            XMLStreamReader reader = this.getXMLStreamReader(stream, ejbJarXml, dtdInfo);

            EjbJarMetaData ejbJarMetaData = EjbJarMetaDataParser.parse(reader, dtdInfo);
            // attach the EjbJarMetaData to the deployment unit
            deploymentUnit.putAttachment(EjbDeploymentAttachmentKeys.EJB_JAR_METADATA, ejbJarMetaData);

            if (ejbJarMetaData instanceof EjbJar31MetaData) {
                EjbJar31MetaData ejbJar31MetaData = (EjbJar31MetaData) ejbJarMetaData;
                if (ejbJar31MetaData.getModuleName() != null) {
                    eeModuleDescription.setModuleName(ejbJar31MetaData.getModuleName());
                }
                if (ejbJar31MetaData.isMetadataComplete()) {
                    MetadataCompleteMarker.setMetadataComplete(deploymentUnit, true);
                }
            } else if (!ejbJarMetaData.isEJB3x()) {
                //EJB spec 20.5.1, we do not process annotations for older deployments
                MetadataCompleteMarker.setMetadataComplete(deploymentUnit, true);
            }

        } catch (XMLStreamException xmlse) {
            throw new DeploymentUnitProcessingException("Exception while parsing ejb-jar.xml: " + ejbJarXml.getPathName(), xmlse);
        } catch (IOException ioe) {
            throw new DeploymentUnitProcessingException("Failed to create reader for ejb-jar.xml: " + ejbJarXml.getPathName(), ioe);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ioe) {
                logger.debug("Ignoring exception while closing the InputStream ", ioe);
            }
        }

    }

    /**
     * @param unit
     */
    @Override
    public void undeploy(DeploymentUnit unit) {

    }

    /**
     * Creates and returns a {@link XMLStreamReader} for the passed {@link VirtualFile ejb-jar.xml}
     *
     * @param stream    The input stream
     * @param ejbJarXml
     * @return
     * @throws DeploymentUnitProcessingException
     *
     */
    private XMLStreamReader getXMLStreamReader(InputStream stream, VirtualFile ejbJarXml, XMLResolver resolver) throws DeploymentUnitProcessingException {
        try {
            final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setXMLResolver(resolver);
            XMLStreamReader xmlReader = inputFactory.createXMLStreamReader(stream);
            return xmlReader;
        } catch (XMLStreamException xmlse) {
            throw new DeploymentUnitProcessingException("Failed to create reader for ejb-jar.xml: " + ejbJarXml.getPathName(), xmlse);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5033.java