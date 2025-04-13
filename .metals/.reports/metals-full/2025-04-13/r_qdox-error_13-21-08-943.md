error id: <WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6307.java
<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6307.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6307.java
text:
```scala
final S@@erviceTarget target = updateContext.getServiceTarget().subTarget();

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

package org.jboss.as.model.socket;

import java.net.InetAddress;

import org.jboss.as.model.AbstractSocketBindingUpdate;
import org.jboss.as.model.UpdateContext;
import org.jboss.as.model.UpdateFailedException;
import org.jboss.as.model.UpdateResultHandler;
import org.jboss.as.services.net.SocketBindingService;
import org.jboss.msc.service.ServiceTarget;

/**
 * @author Emanuel Muckenhuber
 */
public class SocketBindingAdd extends AbstractSocketBindingUpdate {

    private static final long serialVersionUID = -4940876367809265620L;
    private final String name;
    private final String interfaceName;
    private final int port;
    private boolean fixedPort;
    private int multicastPort;
    private InetAddress multicastAddress;

    public SocketBindingAdd(final String interfaceName, final String name, final int port) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (interfaceName == null) {
            throw new IllegalArgumentException("interfaceName is null");
        }
        this.interfaceName = interfaceName;
        this.name = name;
        this.port = port;
    }

    public SocketBindingAdd(SocketBindingElement original) {
        name = original.getName();
        port = original.getPort();
        fixedPort = original.isFixedPort();
        multicastPort = original.getMulticastPort();
        multicastAddress = original.getMulticastAddress();
        interfaceName = original.getInterfaceName();
    }

    /** {@inheritDoc} */
    public AbstractSocketBindingUpdate getCompensatingUpdate(SocketBindingGroupElement original) {
        return new SocketBindingRemove(name);
    }

    /** {@inheritDoc} */
    protected void applyUpdate(SocketBindingGroupElement group) throws UpdateFailedException {
        final SocketBindingElement socketBinding = new SocketBindingElement(name, group.getDefaultInterface());
        socketBinding.setConfiguredInterfaceName(interfaceName);
        socketBinding.setFixedPort(fixedPort);
        socketBinding.setPort(port);
        socketBinding.setMulticastAddress(multicastAddress);
        if(multicastAddress != null) {
            socketBinding.setMulticastPort(multicastPort);
        }
        if(group.addSocketBinding(name, socketBinding)) {
            throw new UpdateFailedException(String.format("duplicate socket-binding (%s) in binding-group (%s)", name, group.getName()));
        }
    }

    protected <P> void applyUpdate(UpdateContext updateContext, UpdateResultHandler<? super Void,P> resultHandler, P param) {
        final ServiceTarget target = updateContext.getBatchBuilder().subTarget();
        final UpdateResultHandler.ServiceStartListener<P> listener = new UpdateResultHandler.ServiceStartListener<P>(resultHandler, param);
        target.addListener(listener);
        SocketBindingService.addService(target, this);
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public boolean isFixedPort() {
        return fixedPort;
    }

    public void setFixedPort(boolean fixedPort) {
        this.fixedPort = fixedPort;
    }

    public int getMulticastPort() {
        return multicastPort;
    }

    public void setMulticastPort(int multicastPort) {
        this.multicastPort = multicastPort;
    }

    public InetAddress getMulticastAddress() {
        return multicastAddress;
    }

    public void setMulticastAddress(InetAddress multicastAddress) {
        this.multicastAddress = multicastAddress;
    }

    public String getInterfaceName() {
        return interfaceName;
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

QDox parse error in <WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6307.java