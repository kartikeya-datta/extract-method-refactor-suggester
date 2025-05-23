error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5772.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5772.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5772.java
text:
```scala
.@@addAttributes(SingleSignOnDefinition.INSTANCE.getAttributes())

/*
 *
 *  JBoss, Home of Professional Open Source.
 *  Copyright 2013, Red Hat, Inc., and individual contributors
 *  as indicated by the @author tags. See the copyright.txt file in the
 *  distribution for a full listing of individual contributors.
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this software; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * /
 */

package org.wildfly.extension.undertow;

import static org.jboss.as.controller.PersistentResourceXMLDescription.builder;

import java.util.List;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PersistentResourceXMLDescription;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;
import org.wildfly.extension.undertow.errorhandler.ErrorPageDefinition;
import org.wildfly.extension.undertow.filters.BasicAuthHandler;
import org.wildfly.extension.undertow.filters.ConnectionLimitHandler;
import org.wildfly.extension.undertow.filters.FilterDefinitions;
import org.wildfly.extension.undertow.filters.FilterRefDefinition;
import org.wildfly.extension.undertow.filters.GzipFilter;
import org.wildfly.extension.undertow.filters.ResponseHeaderFilter;
import org.wildfly.extension.undertow.handlers.FileHandler;
import org.wildfly.extension.undertow.handlers.HandlerDefinitions;
import org.wildfly.extension.undertow.handlers.ReverseProxyHandler;
import org.wildfly.extension.undertow.handlers.ReverseProxyHandlerHost;

/**
 * @author <a href="mailto:tomaz.cerar@redhat.com">Tomaz Cerar</a> (c) 2012 Red Hat Inc.
 */
public class UndertowSubsystemParser_1_0 implements XMLStreamConstants, XMLElementReader<List<ModelNode>>, XMLElementWriter<SubsystemMarshallingContext> {
    protected static final UndertowSubsystemParser_1_0 INSTANCE = new UndertowSubsystemParser_1_0();
    private static final PersistentResourceXMLDescription xmlDescription;

    static {
        xmlDescription = builder(UndertowRootDefinition.INSTANCE)
                .addAttributes(UndertowRootDefinition.DEFAULT_VIRTUAL_HOST, UndertowRootDefinition.DEFAULT_SERVLET_CONTAINER, UndertowRootDefinition.DEFAULT_SERVER, UndertowRootDefinition.INSTANCE_ID)
                .addAttribute(UndertowRootDefinition.STATISTICS_ENABLED)
                .addChild(

                        builder(BufferCacheDefinition.INSTANCE)
                                .addAttributes(BufferCacheDefinition.BUFFER_SIZE, BufferCacheDefinition.BUFFERS_PER_REGION, BufferCacheDefinition.MAX_REGIONS)
                                .setXmlWrapperElement(Constants.BUFFER_CACHES)
                )
                .addChild(builder(ServerDefinition.INSTANCE)
                        .addAttributes(ServerDefinition.DEFAULT_HOST, ServerDefinition.SERVLET_CONTAINER)
                        .addChild(
                                builder(AjpListenerResourceDefinition.INSTANCE)
                                        .addAttributes(AjpListenerResourceDefinition.SCHEME, AjpListenerResourceDefinition.BUFFER_POOL, AjpListenerResourceDefinition.ENABLED, AjpListenerResourceDefinition.SOCKET_BINDING, AjpListenerResourceDefinition.WORKER, ListenerResourceDefinition.REDIRECT_SOCKET)
                                        .addAttributes(ListenerResourceDefinition.OPTIONS)
                        )
                        .addChild(
                                builder(HttpListenerResourceDefinition.INSTANCE)
                                        .addAttributes(HttpListenerResourceDefinition.BUFFER_POOL, HttpListenerResourceDefinition.CERTIFICATE_FORWARDING, HttpListenerResourceDefinition.ENABLED, HttpListenerResourceDefinition.SOCKET_BINDING, HttpListenerResourceDefinition.WORKER, ListenerResourceDefinition.REDIRECT_SOCKET, HttpListenerResourceDefinition.PROXY_ADDRESS_FORWARDING)
                                        .addAttributes(ListenerResourceDefinition.OPTIONS)
                        ).addChild(
                                builder(HttpsListenerResourceDefinition.INSTANCE)
                                        .addAttributes(HttpsListenerResourceDefinition.INSTANCE.getAttributes())
                        ).addChild(
                                builder(HostDefinition.INSTANCE)
                                        .addAttributes(HostDefinition.ALIAS, HostDefinition.DEFAULT_WEB_MODULE)
                                        .addChild(
                                                builder(LocationDefinition.INSTANCE)
                                                        .addAttributes(LocationDefinition.HANDLER)
                                                        .addChild(
                                                                builder(FilterRefDefinition.INSTANCE)
                                                                        .addAttributes(FilterRefDefinition.INSTANCE.getAttributes())
                                                        )
                                        ).addChild(
                                                builder(AccessLogDefinition.INSTANCE)
                                                    .addAttributes(AccessLogDefinition.PATTERN, AccessLogDefinition.DIRECTORY, AccessLogDefinition.PREFIX, AccessLogDefinition.WORKER, AccessLogDefinition.ROTATE)
                                        ).addChild(
                                                builder(FilterRefDefinition.INSTANCE)
                                                    .addAttributes(FilterRefDefinition.INSTANCE.getAttributes())
                                        ).addChild(
                                                builder(SingleSignOnDefinition.INSTANCE)
                                                    .addAttributes(SingleSignOnDefinition.DOMAIN, SingleSignOnDefinition.RE_AUTHENTICATE)
                                        )
                        )
                )
                .addChild(
                        builder(ServletContainerDefinition.INSTANCE)
                                .addAttribute(ServletContainerDefinition.ALLOW_NON_STANDARD_WRAPPERS)
                                .addAttribute(ServletContainerDefinition.DEFAULT_BUFFER_CACHE)
                                .addAttribute(ServletContainerDefinition.STACK_TRACE_ON_ERROR)
                                .addAttribute(ServletContainerDefinition.USE_LISTENER_ENCODING)
                                .addAttribute(ServletContainerDefinition.DEFAULT_ENCODING)
                                .addAttribute(ServletContainerDefinition.IGNORE_FLUSH)
                                .addChild(
                                        builder(JspDefinition.INSTANCE)
                                                .setXmlElementName(Constants.JSP_CONFIG)
                                                .addAttributes(
                                                        JspDefinition.DISABLED,
                                                        JspDefinition.DEVELOPMENT,
                                                        JspDefinition.KEEP_GENERATED,
                                                        JspDefinition.TRIM_SPACES,
                                                        JspDefinition.TAG_POOLING,
                                                        JspDefinition.MAPPED_FILE,
                                                        JspDefinition.CHECK_INTERVAL,
                                                        JspDefinition.MODIFICATION_TEST_INTERVAL,
                                                        JspDefinition.RECOMPILE_ON_FAIL,
                                                        JspDefinition.SMAP,
                                                        JspDefinition.DUMP_SMAP,
                                                        JspDefinition.GENERATE_STRINGS_AS_CHAR_ARRAYS,
                                                        JspDefinition.ERROR_ON_USE_BEAN_INVALID_CLASS_ATTRIBUTE,
                                                        JspDefinition.SCRATCH_DIR,
                                                        JspDefinition.SOURCE_VM,
                                                        JspDefinition.TARGET_VM,
                                                        JspDefinition.JAVA_ENCODING,
                                                        JspDefinition.X_POWERED_BY,
                                                        JspDefinition.DISPLAY_SOURCE_FRAGMENT)
                                )
                                .addChild(
                                        builder(SessionCookieDefinition.INSTANCE)
                                                .addAttributes(
                                                        SessionCookieDefinition.NAME,
                                                        SessionCookieDefinition.DOMAIN,
                                                        SessionCookieDefinition.COMMENT,
                                                        SessionCookieDefinition.HTTP_ONLY,
                                                        SessionCookieDefinition.SECURE,
                                                        SessionCookieDefinition.MAX_AGE
                                                )
                                )
                                .addChild(
                                        builder(PersistentSessionsDefinition.INSTANCE)
                                                .addAttributes(
                                                        PersistentSessionsDefinition.PATH,
                                                        PersistentSessionsDefinition.RELATIVE_TO
                                                )
                                )
                )
                .addChild(
                        builder(ErrorPageDefinition.INSTANCE)
                                .addAttributes(ErrorPageDefinition.CODE, ErrorPageDefinition.PATH)
                                .setXmlWrapperElement(Constants.ERROR_PAGES))
                .addChild(
                        builder(HandlerDefinitions.INSTANCE)
                                .setXmlElementName(Constants.HANDLERS)
                                .setNoAddOperation(true)
                                .addChild(
                                        builder(FileHandler.INSTANCE)
                                                .addAttributes(
                                                        FileHandler.PATH,
                                                        FileHandler.CACHE_BUFFER_SIZE,
                                                        FileHandler.CACHE_BUFFERS,
                                                        FileHandler.DIRECTORY_LISTING)
                                )
                                .addChild(
                                        builder(ReverseProxyHandler.INSTANCE)
                                                .addAttributes(
                                                        ReverseProxyHandler.CONNECTIONS_PER_THREAD,
                                                        ReverseProxyHandler.SESSION_COOKIE_NAMES,
                                                        ReverseProxyHandler.PROBLEM_SERVER_RETRY,
                                                        ReverseProxyHandler.MAX_REQUEST_TIME)
                                                .addChild(builder(ReverseProxyHandlerHost.INSTANCE)
                                                        .setXmlElementName(Constants.HOST)
                                                .addAttributes(ReverseProxyHandlerHost.INSTANCE_ID))
                                )


                )
                .addChild(
                        builder(FilterDefinitions.INSTANCE)
                                .setXmlElementName(Constants.FILTERS)
                                .setNoAddOperation(true)
                                .addChild(
                                        builder(BasicAuthHandler.INSTANCE)
                                                .addAttributes(BasicAuthHandler.SECURITY_DOMAIN)
                                )
                                .addChild(
                                        builder(ConnectionLimitHandler.INSTANCE)
                                                .addAttributes(ConnectionLimitHandler.MAX_CONCURRENT_REQUESTS, ConnectionLimitHandler.QUEUE_SIZE)
                                ).addChild(
                                        builder(ResponseHeaderFilter.INSTANCE)
                                                .addAttributes(ResponseHeaderFilter.INSTANCE.getAttributes())
                                ).addChild(
                                        builder(GzipFilter.INSTANCE)
                                                .addAttributes(GzipFilter.INSTANCE.getAttributes())
                                )

                )
                //todo why do we really need this?
                .setAdditionalOperationsGenerator(new PersistentResourceXMLDescription.AdditionalOperationsGenerator() {
                    @Override
                    public void additionalOperations(final PathAddress address, final ModelNode addOperation, final List<ModelNode> operations) {
                        operations.add(Util.createAddOperation(address.append(UndertowExtension.PATH_FILTERS)));
                        operations.add(Util.createAddOperation(address.append(UndertowExtension.PATH_HANDLERS)));
                    }
                })
                .build();
    }

    private UndertowSubsystemParser_1_0() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeContent(XMLExtendedStreamWriter writer, SubsystemMarshallingContext context) throws XMLStreamException {
        ModelNode model = new ModelNode();
        model.get(UndertowRootDefinition.INSTANCE.getPathElement().getKeyValuePair()).set(context.getModelNode());//this is bit of workaround for SPRD to work properly
        xmlDescription.persist(writer, model, Namespace.CURRENT.getUriString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readElement(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
        xmlDescription.parse(reader, PathAddress.EMPTY_ADDRESS, list);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5772.java