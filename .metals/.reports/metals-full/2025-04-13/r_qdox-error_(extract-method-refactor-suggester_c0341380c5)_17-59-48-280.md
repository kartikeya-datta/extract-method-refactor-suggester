error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1559.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1559.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1559.java
text:
```scala
public J@@vmElement getJvm(String name) {

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

package org.jboss.as.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.jboss.as.model.socket.ServerInterfaceElement;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class HostModel extends AbstractModel<HostModel> {

    private static final long serialVersionUID = 7667892965813702351L;

    public static final String DEFAULT_NAME;
    static {
        try {
            DEFAULT_NAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private final Set<String> extensions = new HashSet<String>();
    private final NavigableMap<String, ServerInterfaceElement> interfaces = new TreeMap<String, ServerInterfaceElement>();
    private final NavigableMap<String, ServerElement> servers = new TreeMap<String, ServerElement>();
    private final NavigableMap<String, JvmElement> jvms = new TreeMap<String, JvmElement>();
    private String name;
    private LocalDomainControllerElement localDomainController;
    private RemoteDomainControllerElement remoteDomainController;
    private ManagementElement managementElement;

    private PropertiesElement systemProperties;

    private static final QName ELEMENT_NAME = new QName(Namespace.CURRENT.getUriString(), Element.HOST.getLocalName());

    /**
     * Construct a new instance.
     */
    public HostModel() {
        super(ELEMENT_NAME);
    }

    /**
     * Gets the host level configuration for the jvm with the given <code>name</name>.
     * This configuration can extend or override any configuration for a jvm
     * with the same name at the {@link ServerGroupElement#getJvm() server group level}.
     * In turn, the details of the configuration of this jvm can be overridden at the
     * {@link ServerElement#getJvm() server level}.
     *
     * @param name the name of the jvm
     * @return the jvm configuration, or <code>null</code> if there is none with
     *         the given <code>name</name>
     */
    JvmElement getJvm(String name) {
        return jvms.get(name);
    }

    /**
     * Gets the host level configuration for the interface with the given <code>name</name>.
     * This configuration can override any configuration for an interface
     * with the same name at the {@link DomainModel#getInterface(String) domain level}.
     * In turn, the details of the configuration of this interface can be overridden at the
     * {@link ServerElement#getInterfaces() server level}.
     *
     * @param name the name of the interface
     * @return the interface configuration, or <code>null</code> if there is none with
     *         the given <code>name</name>
     */
    public ServerInterfaceElement getInterface(String name) {
        synchronized (interfaces) {
            return interfaces.get(name);
        }
    }

    /**
     * Gets the named interfaces configured at the host level.
     *
     * @return the interfaces. May be empty but will not be <code>null</code>
     */
    public Set<ServerInterfaceElement> getInterfaces() {
        synchronized (interfaces) {
            return new HashSet<ServerInterfaceElement>(interfaces.values());
        }
    }

    /**
     * Gets the server-specific configurations for the servers associated with this host.
     *
     * @return the servers. May be empty but will not be <code>null</code>
     */
    public Set<ServerElement> getServers() {
        synchronized (servers) {
            return new HashSet<ServerElement>(servers.values());
        }
    }

    /**
     * Gets the server configuration for the server with the given
     * <code>name</code>.
     *
     * @param name the name of the server
     * @return the server configuration, or <code>null</code> if no server
     *         named <code>name</code> is configured
     */
    public ServerElement getServer(String name) {
        synchronized (servers) {
            return servers.get(name);
        }
    }

    /**
     * Gets any system properties defined at the host level. These properties
     * can extend and override any properties declared at the
     * {@link DomainModel#getSystemProperties() domain level} or the
     * {@link ServerGroupElement server group level} and may in turn be extended
     * or overridden by any properties declared at the
     * {@link ServerElement#getSystemProperties() server level}.
     *
     * @return the system properties, or <code>null</code> if there are none
     */
    public PropertiesElement getSystemProperties() {
        return systemProperties;
    }

    public LocalDomainControllerElement getLocalDomainControllerElement() {
        return localDomainController;
    }

    public RemoteDomainControllerElement getRemoteDomainControllerElement() {
        return remoteDomainController;
    }

    public ManagementElement getManagementElement() {
        return managementElement;
    }

    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    protected Class<HostModel> getElementClass() {
        return HostModel.class;
    }

    /** {@inheritDoc} */
    @Override
    public void writeContent(final XMLExtendedStreamWriter streamWriter) throws XMLStreamException {

        writeNamespaces(streamWriter);

        if (systemProperties != null) {
            streamWriter.writeStartElement(Element.SYSTEM_PROPERTIES.getLocalName());
            systemProperties.writeContent(streamWriter);
            streamWriter.writeEndElement();
        }

        streamWriter.writeStartElement(Element.DOMAIN_CONTROLLER.getLocalName());
        if (localDomainController != null) {
            streamWriter.writeStartElement(Element.LOCAL.getLocalName());
            localDomainController.writeContent(streamWriter);
        }
        else if (remoteDomainController != null) {
            streamWriter.writeStartElement(Element.REMOTE.getLocalName());
            remoteDomainController.writeContent(streamWriter);
        }
        streamWriter.writeEndElement();

        if (!interfaces.isEmpty()) {
            streamWriter.writeStartElement(Element.INTERFACES.getLocalName());
            for (ServerInterfaceElement element : interfaces.values()) {
                streamWriter.writeStartElement(Element.INTERFACE.getLocalName());
                element.writeContent(streamWriter);
            }
            streamWriter.writeEndElement();
        }

        if (!jvms.isEmpty()) {
            streamWriter.writeStartElement(Element.JVMS.getLocalName());
            for (JvmElement element : jvms.values()) {
                streamWriter.writeStartElement(Element.JVM.getLocalName());
                element.writeContent(streamWriter);
            }
            streamWriter.writeEndElement();
        }

        if (!servers.isEmpty()) {
            streamWriter.writeStartElement(Element.SERVERS.getLocalName());
            for (ServerElement server : servers.values()) {
                streamWriter.writeStartElement(Element.SERVER.getLocalName());
                server.writeContent(streamWriter);
                streamWriter.writeEndElement();
            }
            streamWriter.writeEndElement();
        }

        streamWriter.writeEndElement();
    }

    boolean addExtension(final String name) {
        return extensions.add(name);
    }

    boolean removeExtension(final String name) {
        return extensions.remove(name);
    }

    Set<String> getExtensions() {
        return extensions;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1559.java