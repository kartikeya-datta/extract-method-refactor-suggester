error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11162.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11162.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11162.java
text:
```scala
s@@uper(existing, globalRollback);

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

package org.jboss.as.controller.client.helpers.domain.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.jboss.as.controller.client.helpers.domain.AddDeploymentPlanBuilder;
import org.jboss.as.controller.client.helpers.domain.DeployDeploymentPlanBuilder;
import org.jboss.as.controller.client.helpers.domain.DeploymentPlanBuilder;
import org.jboss.as.controller.client.helpers.domain.DuplicateDeploymentNameException;
import org.jboss.as.controller.client.helpers.domain.RemoveDeploymentPlanBuilder;
import org.jboss.as.controller.client.helpers.domain.ReplaceDeploymentPlanBuilder;
import org.jboss.as.controller.client.helpers.domain.ServerGroupDeploymentPlan;
import org.jboss.as.controller.client.helpers.domain.ServerGroupDeploymentPlanBuilder;
import org.jboss.as.controller.client.helpers.domain.UndeployDeploymentPlanBuilder;


/**
 * Builder capable of creating a {@link DeploymentPlanImpl}.
 *
 * @author Brian Stansberry
 */
class DeploymentPlanBuilderImpl extends AbstractDeploymentPlanBuilder implements DeploymentPlanBuilder  {

    private final DeploymentContentDistributor deploymentDistributor;

    DeploymentPlanBuilderImpl(DeploymentContentDistributor deploymentDistributor) {
        super();
        if (deploymentDistributor == null)
            throw new IllegalArgumentException("deploymentDistributor is null");
        this.deploymentDistributor = deploymentDistributor;
    }

    DeploymentPlanBuilderImpl(DeploymentPlanBuilderImpl existing, boolean globalRollback) {
        super(existing);
        this.deploymentDistributor = existing.deploymentDistributor;
    }

    DeploymentPlanBuilderImpl(DeploymentPlanBuilderImpl existing, DeploymentSetPlanImpl setPlan) {
        super(existing, setPlan);
        this.deploymentDistributor = existing.deploymentDistributor;
    }

    @Override
    public AddDeploymentPlanBuilder add(File file) throws IOException, DuplicateDeploymentNameException {
        String name = file.getName();
        return add(name, name, file.toURI().toURL());
    }

    @Override
    public AddDeploymentPlanBuilder add(URL url) throws IOException, DuplicateDeploymentNameException {
        String name = getName(url);
        return add(name, name, url);
    }

    @Override
    public AddDeploymentPlanBuilder add(String name, File file) throws IOException, DuplicateDeploymentNameException {
        return add(name, file.getName(), file.toURI().toURL());
    }

    @Override
    public AddDeploymentPlanBuilder add(String name, URL url) throws IOException, DuplicateDeploymentNameException {
        String commonName = getName(url);
        return add(name, commonName, url);
    }

    @Override
    public AddDeploymentPlanBuilder add(String name, InputStream stream) throws IOException, DuplicateDeploymentNameException {
        return add(name, name, stream);
    }

    @Override
    public AddDeploymentPlanBuilder add(String name, String commonName,
            InputStream stream) throws IOException, DuplicateDeploymentNameException {
        DeploymentSetPlanImpl currentSet = getCurrentDeploymentSetPlan();
        if (currentSet.hasServerGroupPlans()) {
            throw new IllegalStateException("Cannot add deployment actions after starting creation of a rollout plan");
        }
        byte[] hash = deploymentDistributor.distributeDeploymentContent(name, commonName, stream);
        DeploymentActionImpl mod = DeploymentActionImpl.getAddAction(name, commonName, hash);
        DeploymentSetPlanImpl newSet = currentSet.addAction(mod);
        return new AddDeploymentPlanBuilderImpl(this, newSet);
    }

    @Override
    public AddDeploymentPlanBuilder add(String name) throws IOException {
        DeploymentSetPlanImpl currentSet = getCurrentDeploymentSetPlan();
        if (currentSet.hasServerGroupPlans()) {
            throw new IllegalStateException("Cannot add deployment actions after starting creation of a rollout plan");
        }
        DeploymentActionImpl mod = DeploymentActionImpl.getAddAction(name, null, null);

        DeploymentSetPlanImpl newSet = currentSet.addAction(mod);
        return new AddDeploymentPlanBuilderImpl(this, newSet);
    }

    @Override
    public DeployDeploymentPlanBuilder deploy(String key) {
        DeploymentSetPlanImpl currentSet = getCurrentDeploymentSetPlan();
        if (currentSet.hasServerGroupPlans()) {
            throw new IllegalStateException("Cannot add deployment actions after starting creation of a rollout plan");
        }
        DeploymentActionImpl mod = DeploymentActionImpl.getDeployAction(key);
        DeploymentSetPlanImpl newSet = currentSet.addAction(mod);
        return new DeployDeploymentPlanBuilderImpl(this, newSet);
    }

    @Override
    public UndeployDeploymentPlanBuilder undeploy(String key) {
        DeploymentSetPlanImpl currentSet = getCurrentDeploymentSetPlan();
        if (currentSet.hasServerGroupPlans()) {
            throw new IllegalStateException("Cannot add deployment actions after starting creation of a rollout plan");
        }
        DeploymentActionImpl mod = DeploymentActionImpl.getUndeployAction(key);
        DeploymentSetPlanImpl newSet = currentSet.addAction(mod);
        return new UndeployDeploymentPlanBuilderImpl(this, newSet);
    }

    @Override
    public DeploymentPlanBuilder redeploy(String deploymentName) {
        DeploymentActionImpl mod = DeploymentActionImpl.getRedeployAction(deploymentName);
        return getNewBuilder(mod);
    }

    @Override
    public ReplaceDeploymentPlanBuilder replace(String replacement, String toReplace) {
        DeploymentSetPlanImpl currentSet = getCurrentDeploymentSetPlan();
        if (currentSet.hasServerGroupPlans()) {
            throw new IllegalStateException("Cannot add deployment actions after starting creation of a rollout plan");
        }
        DeploymentActionImpl mod = DeploymentActionImpl.getReplaceAction(replacement, toReplace);
        DeploymentSetPlanImpl newSet = currentSet.addAction(mod);
        return new ReplaceDeploymentPlanBuilderImpl(this, newSet);
    }

    @Override
    public RemoveDeploymentPlanBuilder replace(File file) throws IOException {
        String name = file.getName();
        return replace(name, name, file.toURI().toURL());
    }

    @Override
    public RemoveDeploymentPlanBuilder replace(URL url) throws IOException {
        String name = getName(url);
        return replace(name, name, url);
    }

    @Override
    public RemoveDeploymentPlanBuilder replace(String name, File file) throws IOException {
        return replace(name, name, file.toURI().toURL());
    }

    @Override
    public RemoveDeploymentPlanBuilder replace(String name, URL url) throws IOException {
        return replace(name, name, url);
    }

    @Override
    public RemoveDeploymentPlanBuilder replace(String name, InputStream stream) throws IOException {
        return replace(name, name, stream);
    }

    @Override
    public RemoveDeploymentPlanBuilder replace(String name, String commonName, InputStream stream) throws IOException {
        DeploymentSetPlanImpl currentSet = getCurrentDeploymentSetPlan();
        if (currentSet.hasServerGroupPlans()) {
            throw new IllegalStateException("Cannot add deployment actions after starting creation of a rollout plan");
        }
        byte[] hash = deploymentDistributor.distributeReplacementDeploymentContent(name, commonName, stream);
        DeploymentActionImpl mod = DeploymentActionImpl.getFullReplaceAction(name, commonName, hash);
        DeploymentSetPlanImpl newSet = currentSet.addAction(mod);
        return new RemoveDeploymentPlanBuilderImpl(this, newSet);
    }

    @Override
    public RemoveDeploymentPlanBuilder remove(String key) {
        DeploymentSetPlanImpl currentSet = getCurrentDeploymentSetPlan();
        if (currentSet.hasServerGroupPlans()) {
            throw new IllegalStateException("Cannot add deployment actions after starting creation of a rollout plan");
        }
        DeploymentActionImpl mod = DeploymentActionImpl.getRemoveAction(key);
        DeploymentSetPlanImpl newSet = currentSet.addAction(mod);
        return new RemoveDeploymentPlanBuilderImpl(this, newSet);
    }

    ServerGroupDeploymentPlanBuilder toServerGroup(final String serverGroupName) {
        DeploymentSetPlanImpl currentSet = getCurrentDeploymentSetPlan();
        DeploymentSetPlanImpl newSet = currentSet.storeServerGroup(new ServerGroupDeploymentPlan(serverGroupName));
        return new ServerGroupDeploymentPlanBuilderImpl(this, newSet);
    }

    private AddDeploymentPlanBuilder add(String name, String commonName, URL url) throws IOException, DuplicateDeploymentNameException {
        URLConnection conn = url.openConnection();
        conn.connect();
        InputStream stream = conn.getInputStream();
        try {
            return add(name, commonName, stream);
        }
        finally {
            try { stream.close(); } catch (Exception ignored) {}
        }
    }

    private RemoveDeploymentPlanBuilder replace(String name, String commonName, URL url) throws IOException {
        URLConnection conn = url.openConnection();
        conn.connect();
        InputStream stream = conn.getInputStream();
        try {
            return replace(name, commonName, stream);
        }
        finally {
            try { stream.close(); } catch (Exception ignored) {}
        }
    }

    DeploymentPlanBuilderImpl getNewBuilder(DeploymentActionImpl mod) {
        DeploymentSetPlanImpl currentSet = getCurrentDeploymentSetPlan();
        if (currentSet.hasServerGroupPlans()) {
            throw new IllegalStateException("Cannot add deployment actions after starting creation of a rollout plan");
        }
        DeploymentSetPlanImpl newSet = currentSet.addAction(mod);
        return new DeploymentPlanBuilderImpl(this, newSet);
    }

    private static String getName(URL url) {
        if ("file".equals(url.getProtocol())) {
            try {
                File f = new File(url.toURI());
                return f.getName();
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(url + " is not a valid URI", e);
            }
        }

        String path = url.getPath();
        int idx = path.lastIndexOf('/');
        while (idx == path.length() - 1) {
            path = path.substring(0, idx);
            idx = path.lastIndexOf('/');
        }
        if (idx == -1) {
            throw new IllegalArgumentException("Cannot derive a deployment name from " +
                    url + " -- use an overloaded method variant that takes a 'name' parameter");
        }

        return path.substring(idx + 1);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11162.java