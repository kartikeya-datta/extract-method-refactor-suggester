error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10665.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10665.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10665.java
text:
```scala
t@@his.putAttachment(WSAttachmentKeys.JAXWS_ENDPOINTS_KEY, jaxwsDeployment);

package org.jboss.as.webservices.publish;

import java.util.Map;

import org.jboss.as.controller.PathElement;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.SimpleAttachable;
import org.jboss.as.webservices.metadata.DeploymentJaxws;
import org.jboss.as.webservices.metadata.DeploymentJaxwsImpl;
import org.jboss.as.webservices.metadata.EndpointJaxwsPojoImpl;
import org.jboss.as.webservices.util.WSAttachmentKeys;
import org.jboss.as.webservices.util.WSServices;
import org.jboss.dmr.ModelNode;
import org.jboss.metadata.web.jboss.JBossWebMetaData;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;

public class WSEndpointDeploymentUnit extends SimpleAttachable implements DeploymentUnit {

    private String deploymentName;

    public WSEndpointDeploymentUnit(ClassLoader loader, String context, Map<String,String> urlPatternToClassName) {
        this.deploymentName = context + ".deployment";

        JBossWebMetaData jbossWebMetaData = new JBossWebMetaData();
        DeploymentJaxws jaxwsDeployment = new DeploymentJaxwsImpl();
        jbossWebMetaData.setContextRoot(context);
        for (String urlPattern : urlPatternToClassName.keySet()) {
            addEndpoint(jbossWebMetaData, jaxwsDeployment, urlPatternToClassName.get(urlPattern), urlPattern);
        }
        this.putAttachment(WSAttachmentKeys.JBOSSWEB_METADATA_KEY, jbossWebMetaData);
        this.putAttachment(WSAttachmentKeys.WS_ENDPOINTS_KEY, jaxwsDeployment);
        this.putAttachment(WSAttachmentKeys.CLASSLOADER_KEY, loader);
    }

    private void addEndpoint(JBossWebMetaData jbossWebMetaData, DeploymentJaxws jaxwsDeployment, String className, String urlPattern) {
        if (urlPattern == null) {
            urlPattern = "/*";
        } else {
            urlPattern = urlPattern.trim();
            if (!urlPattern.startsWith("/")) {
                urlPattern = "/" + urlPattern;
            }
        }
        jaxwsDeployment.addEndpoint(new EndpointJaxwsPojoImpl(className, urlPattern));
    }

    @Override
    public ServiceName getServiceName() {
        return ServiceName.JBOSS.append("ws-endpoint-deployment").append(deploymentName);
    }

    @Override
    public DeploymentUnit getParent() {
        return null;
    }

    @Override
    public String getName() {
        return deploymentName;
    }

    @Override
    public ServiceRegistry getServiceRegistry() {
        return WSServices.getContainerRegistry();
    }

    @Override
    public ModelNode getDeploymentSubsystemModel(String subsystemName) {
        throw new RuntimeException("Can't get the deployment submodel from a " + WSEndpointDeploymentUnit.class + " instance");
    }

    @Override
    public ModelNode createDeploymentSubModel(String subsystemName, PathElement address) {
        throw new RuntimeException("Can't create a deployment submodel from a " + WSEndpointDeploymentUnit.class + " instance");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10665.java