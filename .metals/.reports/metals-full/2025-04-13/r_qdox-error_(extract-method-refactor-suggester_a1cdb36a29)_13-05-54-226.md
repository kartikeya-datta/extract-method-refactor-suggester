error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3162.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3162.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3162.java
text:
```scala
final S@@tring deploymentRootName = deploymentRoot.getName().toLowerCase();

package org.jboss.as.messaging.deployment;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.as.messaging.MessagingLogger;
import org.jboss.as.messaging.MessagingMessages;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.staxmapper.XMLMapper;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;

/**
 * Processor that handles the messaging subsystems deployable XML
 *
 * @author Stuart Douglas
 */
public class MessagingXmlParsingDeploymentUnitProcessor implements DeploymentUnitProcessor {

    private static final XMLInputFactory INPUT_FACTORY = XMLInputFactory.newInstance();

    private static final String[] LOCATIONS = {"WEB-INF", "META-INF"};

    private static final QName ROOT_1_0 = new QName(Namespace.MESSAGING_DEPLOYMENT_1_0.getUriString(), "messaging-deployment");
    private static final QName ROOT_NO_NAMESPACE = new QName("messaging-deployment");

    private final XMLMapper mapper;

    public MessagingXmlParsingDeploymentUnitProcessor() {
        mapper = XMLMapper.Factory.create();
        mapper.registerRootElement(ROOT_1_0, MessagingDeploymentParser_1_0.INSTANCE);
        mapper.registerRootElement(ROOT_NO_NAMESPACE, MessagingDeploymentParser_1_0.INSTANCE);
    }

    @Override
    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final Set<VirtualFile> files = messageDestinations(deploymentUnit);

        for (final VirtualFile file : files) {
            InputStream xmlStream = null;

            try {
                final File f = file.getPhysicalFile();
                xmlStream = new FileInputStream(f);
                try {

                    final XMLInputFactory inputFactory = INPUT_FACTORY;
                    setIfSupported(inputFactory, XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
                    setIfSupported(inputFactory, XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
                    final XMLStreamReader streamReader = inputFactory.createXMLStreamReader(xmlStream);
                    final ParseResult result = new ParseResult();
                    try {
                        mapper.parseDocument(result, streamReader);
                        deploymentUnit.addToAttachmentList(MessagingAttachments.PARSE_RESULT, result);
                    } finally {
                        safeClose(streamReader, f.getAbsolutePath());
                    }
                } catch (XMLStreamException e) {
                    throw MessagingMessages.MESSAGES.couldNotParseDeployment(f.getPath(), e);
                }
            } catch (Exception e) {
                throw new DeploymentUnitProcessingException(e.getMessage(), e);
            } finally {
                VFSUtils.safeClose(xmlStream);
            }
        }
    }

    private void setIfSupported(final XMLInputFactory inputFactory, final String property, final Object value) {
        if (inputFactory.isPropertySupported(property)) {
            inputFactory.setProperty(property, value);
        }
    }

    @Override
    public void undeploy(final DeploymentUnit context) {

    }


    private Set<VirtualFile> messageDestinations(final DeploymentUnit deploymentUnit) {
        final VirtualFile deploymentRoot = deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT).getRoot();
        if (deploymentRoot == null || !deploymentRoot.exists()) {
            return Collections.emptySet();
        }

        final String deploymentRootName = deploymentRoot.getLowerCaseName();

        if (deploymentRootName.endsWith("-jms.xml")) {
            return Collections.singleton(deploymentRoot);
        }
        final Set<VirtualFile> ret = new HashSet<VirtualFile>();
        for (String location : LOCATIONS) {
            final VirtualFile loc = deploymentRoot.getChild(location);
            if (loc.exists()) {
                for (final VirtualFile file : loc.getChildren()) {
                    if (file.getName().endsWith("-jms.xml")) {
                        ret.add(file);
                    }
                }
            }
        }
        return ret;
    }

    private static void safeClose(final XMLStreamReader closeable, final String file) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (XMLStreamException e) {
                MessagingLogger.ROOT_LOGGER.couldNotCloseFile(file, e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3162.java