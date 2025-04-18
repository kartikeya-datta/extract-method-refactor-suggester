error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14492.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14492.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14492.java
text:
```scala
final S@@erviceController<?> service = updateContext.getServiceRegistry().getService(WebSubsystemElement.JBOSS_WEB);

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

package org.jboss.as.web;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.xml.stream.XMLStreamException;

import org.jboss.as.model.AbstractSubsystemAdd;
import org.jboss.as.model.AbstractSubsystemElement;
import org.jboss.as.model.AbstractSubsystemUpdate;
import org.jboss.as.model.UpdateContext;
import org.jboss.as.model.UpdateResultHandler;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * The jboss web subsystem configuration.
 *
 * @author Emanuel Muckenhuber
 */
public final class WebSubsystemElement extends AbstractSubsystemElement<WebSubsystemElement> {

    private static final long serialVersionUID = 4287677532426993047L;

    /** The base name for jboss.web services. */
    public static final ServiceName JBOSS_WEB = ServiceName.JBOSS.append("web");
    /** The jboss.web server name, there can only be one. */
    public static final ServiceName JBOSS_WEB_SERVER = JBOSS_WEB.append("server");
    /** The base name for jboss.web connector services. */
    public static final ServiceName JBOSS_WEB_CONNECTOR = JBOSS_WEB.append("connector");
    /** The base name for jboss.web host services. */
    public static final ServiceName JBOSS_WEB_HOST = JBOSS_WEB.append("host");

    private String defaultHost;

    /** The web container config. */
    private WebContainerConfigElement config;

    /** The web connectors. */
    private final NavigableMap<String, WebConnectorElement> connectors = new TreeMap<String, WebConnectorElement>();
    /** The virtual web servers. */
    private final NavigableMap<String, WebVirtualHostElement> hosts = new TreeMap<String, WebVirtualHostElement>();

    protected WebSubsystemElement(String defaultHost) {
        super(Namespace.CURRENT.getUriString());
        this.defaultHost = defaultHost;
    }

    public String getDefaultHost() {
        return defaultHost;
    }

    public void setDefaultHost(String defaultHost) {
        this.defaultHost = defaultHost;
    }

    /** {@inheritDoc} */
    protected void getUpdates(List<? super AbstractSubsystemUpdate<WebSubsystemElement, ?>> list) {
        for(final WebConnectorElement connector : connectors.values()) {
            list.add(WebConnectorAdd.create(connector));
        }
        for(final WebVirtualHostElement host : hosts.values()) {
            list.add(WebVirtualHostAdd.create(host));
        }
    }

    /** {@inheritDoc} */
    protected boolean isEmpty() {
        return connectors.isEmpty() && hosts.isEmpty();
    }

    /** {@inheritDoc} */
    protected AbstractSubsystemAdd<WebSubsystemElement> getAdd() {
        final WebSubsystemAdd action = new WebSubsystemAdd();
        action.setDefaultHost(defaultHost);
        action.setConfig(config);
        return action;
    }

    /** {@inheritDoc} */
    protected <P> void applyRemove(UpdateContext updateContext, UpdateResultHandler<? super Void, P> resultHandler, P param) {
        final ServiceController<?> service = updateContext.getServiceContainer().getService(WebSubsystemElement.JBOSS_WEB);
        if(service == null) {
            resultHandler.handleSuccess(null, param);
        } else {
            service.addListener(new UpdateResultHandler.ServiceRemoveListener<P>(resultHandler, param));
        }
    }

    /** {@inheritDoc} */
    protected Class<WebSubsystemElement> getElementClass() {
        return WebSubsystemElement.class;
    }

    /** {@inheritDoc} */
    public void writeContent(XMLExtendedStreamWriter streamWriter) throws XMLStreamException {
        if(defaultHost != null) streamWriter.writeAttribute(Attribute.DEFAULT_HOST.getLocalName(), defaultHost);
        if(config != null) {
            streamWriter.writeStartElement(Element.CONTAINER_CONFIG.getLocalName());
            config.writeContent(streamWriter);
        }
        if(! connectors.isEmpty()) {
            for(final WebConnectorElement connector : connectors.values()) {
                streamWriter.writeStartElement(Element.CONNECTOR.getLocalName());
                connector.writeContent(streamWriter);
            }
        }
        if(! hosts.isEmpty()) {
            for(final WebVirtualHostElement host : hosts.values()) {
                streamWriter.writeStartElement(Element.VIRTUAL_SERVER.getLocalName());
                host.writeContent(streamWriter);
            }
        }
        streamWriter.writeEndElement();
    }

    public WebContainerConfigElement getConfig() {
        return config;
    }

    public void setConfig(WebContainerConfigElement config) {
        this.config = config;
    }

    WebConnectorElement addConnector(final String name) {
       if(connectors.containsKey(name)) {
           return null;
       }
       final WebConnectorElement connector = new WebConnectorElement(name);
       connectors.put(name, connector);
       return connector;
    }

    public WebConnectorElement getConnector(final String name) {
        return connectors.get(name);
    }

    boolean removeConnector(final String name) {
        return connectors.remove(name) != null;
    }

    WebVirtualHostElement addHost(final String name) {
        if(hosts.containsKey(name)) {
            return null;
        }
        final WebVirtualHostElement host = new WebVirtualHostElement(name);
        hosts.put(name, host);
        return host;
    }

    public WebVirtualHostElement getHost(final String name) {
        return hosts.get(name);
    }

    boolean removeHost(final String name) {
        return hosts.remove(name) != null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14492.java