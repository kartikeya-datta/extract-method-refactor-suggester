error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4030.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4030.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4030.java
text:
```scala
public i@@nt getRemoteDomainControllerPort() {

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

package org.jboss.as.host.controller.operations;

import org.jboss.as.controller.ControlledProcessState;
import org.jboss.as.domain.controller.LocalHostControllerInfo;
import org.jboss.as.host.controller.HostControllerEnvironment;

/**
 * Default implementation of {@link LocalHostControllerInfo}.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class LocalHostControllerInfoImpl implements LocalHostControllerInfo {

    private final ControlledProcessState processState;
    private final HostControllerEnvironment hostEnvironment;

    private final String localHostName;
    private boolean master;
    private String nativeManagementInterface;
    private int nativeManagementPort;

    private String remoteDcHost;
    private int remoteDcPort;
    private String remoteSecurityRealm;
    private String httpManagementInterface;
    private int httpManagementPort;
    private int httpManagementSecurePort;
    private String nativeManagementSecurityRealm;
    private String httpManagementSecurityRealm;

    /** Constructor solely for test cases */
    public LocalHostControllerInfoImpl(final ControlledProcessState processState, final String localHostName) {
        this.processState = processState;
        this.hostEnvironment = null;
        this.localHostName = localHostName;
    }

    public LocalHostControllerInfoImpl(final ControlledProcessState processState, final HostControllerEnvironment hostEnvironment) {
        this.processState = processState;
        this.hostEnvironment = hostEnvironment;
        this.localHostName = null;
    }

    public String getLocalHostName() {
        return hostEnvironment == null ? localHostName : hostEnvironment.getHostControllerName();
    }

    @Override
    public ControlledProcessState.State getProcessState() {
        return processState.getState();
    }

    public boolean isMasterDomainController() {
        return master;
    }

    @Override
    public String getNativeManagementInterface() {
        return nativeManagementInterface;
    }

    @Override
    public int getNativeManagementPort() {
        return nativeManagementPort;
    }

    @Override
    public String getNativeManagementSecurityRealm() {
        return nativeManagementSecurityRealm;
    }

    @Override
    public String getHttpManagementInterface() {
        return httpManagementInterface;
    }

    @Override
    public int getHttpManagementPort() {
        return httpManagementPort;
    }

    @Override
    public int getHttpManagementSecurePort() {
        return httpManagementSecurePort;
    }

    @Override
    public String getHttpManagementSecurityRealm() {
        return httpManagementSecurityRealm;
    }

    public String getRemoteDomainControllerHost() {
        return remoteDcHost;
    }

    public int getRemoteDomainControllertPort() {
        return remoteDcPort;
    }

    public String getRemoteDomainControllerSecurityRealm() {
        return remoteSecurityRealm;
    }

    void setMasterDomainController(boolean master) {
        this.master = master;
    }

    void setNativeManagementInterface(String nativeManagementInterface) {
        this.nativeManagementInterface = nativeManagementInterface;
    }

    void setNativeManagementPort(int nativeManagementPort) {
        this.nativeManagementPort = nativeManagementPort;
    }

    void setNativeManagementSecurityRealm(String nativeManagementSecurityRealm) {
        this.nativeManagementSecurityRealm = nativeManagementSecurityRealm;
    }

    void setHttpManagementInterface(String httpManagementInterface) {
        this.httpManagementInterface = httpManagementInterface;
    }

    void setHttpManagementPort(int httpManagementPort) {
        this.httpManagementPort = httpManagementPort;
    }

    void setHttpManagementSecurePort(int httpManagementSecurePort) {
        this.httpManagementSecurePort = httpManagementSecurePort;
    }

    void setHttpManagementSecurityRealm(String httpManagementSecurityRealm) {
        this.httpManagementSecurityRealm = httpManagementSecurityRealm;
    }

    void setRemoteDomainControllerHost(String host) {
        remoteDcHost = host;
    }

    void setRemoteDomainControllerPort(int port) {
        remoteDcPort = port;
    }

    public void setRemoteDomainControllerSecurityRealm(String remoteSecurityRealm) {
        this.remoteSecurityRealm = remoteSecurityRealm;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4030.java