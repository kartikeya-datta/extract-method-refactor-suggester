error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7173.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7173.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7173.java
text:
```scala
b@@oolean isUpload = UPLOAD_REQUEST.equals(exchange.getRequestPath());

/*
* JBoss, Home of Professional Open Source.
* Copyright 2012, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.domain.http.server;

import static org.jboss.as.domain.http.server.HttpServerLogger.ROOT_LOGGER;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.form.MultiPartHandler;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;

import org.jboss.as.controller.ControlledProcessState;
import org.jboss.as.controller.ControlledProcessStateService;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.domain.http.server.security.SubjectAssociationHandler;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
class DomainApiCheckHandler implements HttpHandler {

    static String PATH = "/management";
    private static final String UPLOAD_REQUEST = PATH + "/add-content";

    private final ControlledProcessStateService controlledProcessStateService;
    private final HttpHandler domainApiHandler;
    private final MultiPartHandler uploadHandler = new MultiPartHandler();;


    DomainApiCheckHandler(final ModelControllerClient modelController, final ControlledProcessStateService controlledProcessStateService) {
        this.controlledProcessStateService = controlledProcessStateService;
        domainApiHandler = new BlockingHandler(new SubjectAssociationHandler(new DomainApiHandler(modelController)));
        uploadHandler.setNext(new BlockingHandler(new SubjectAssociationHandler(new DomainApiUploadHandler(modelController))));
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (!commonChecks(exchange)) {
            return;
        }

        boolean isUpload = UPLOAD_REQUEST.equals(exchange.getCanonicalPath());
        if (Methods.POST.equals(exchange.getRequestMethod())) {
            if (isUpload) {
                uploadHandler.handleRequest(exchange);
                return;
            }
            if (!checkPostContentType(exchange)) {
                return;
            }

        }

        domainApiHandler.handleRequest(exchange);
    }

    private boolean checkPostContentType(HttpServerExchange exchange) throws Exception {
        HeaderMap headers = exchange.getRequestHeaders();
        String contentType = extractContentType(headers.getFirst(Headers.CONTENT_TYPE));
        if (!(Common.APPLICATION_JSON.equals(contentType) || Common.APPLICATION_DMR_ENCODED.equals(contentType))) {

            // RFC 2616: 14.11 Content-Encoding
            // If the content-coding of an entity in a request message is not
            // acceptable to the origin server, the server SHOULD respond with a
            // status code of 415 (Unsupported Media Type).
            ROOT_LOGGER.debug("Request rejected due to unsupported media type - should be one of (application/json,application/dmr-encoded).");
            Common.UNSUPPORTED_MEDIA_TYPE.handleRequest(exchange);
            return false;
        }
        return true;
    }

    private String extractContentType(final String fullContentType) {
        int pos = fullContentType.indexOf(';');
        return pos < 0 ? fullContentType : fullContentType.substring(0, pos).trim();
    }

    private boolean commonChecks(HttpServerExchange exchange) throws Exception {
     // AS7-2284 If we are starting or stopping, tell caller the service is unavailable and to try again
        // later. If "stopping" it's either a reload, in which case trying again will eventually succeed,
        // or it's a true process stop eventually the server will have stopped.
        @SuppressWarnings("deprecation")
        ControlledProcessState.State currentState = controlledProcessStateService.getCurrentState();
        if (currentState == ControlledProcessState.State.STARTING
 currentState == ControlledProcessState.State.STOPPING) {
            exchange.getResponseHeaders().add(Headers.RETRY_AFTER, "2"); //  2 secs is just a guesstimate
            Common.SERVICE_UNAVAIABLE.handleRequest(exchange);
            return false;
        }

        /*
         * Completely disallow OPTIONS - if the browser suspects this is a cross site request just reject it.
         */
        final HttpString requestMethod = exchange.getRequestMethod();
        if (!Methods.POST.equals(requestMethod) && !Methods.GET.equals(requestMethod)) {
            if (Methods.OPTIONS.equals(requestMethod)) {
                ROOT_LOGGER.debug("Request rejected due to 'OPTIONS' method which is not supported.");
            } else {
                ROOT_LOGGER.debug("Request rejected as method not one of (GET,POST).");
            }
            Common.METHOD_NOT_ALLOWED_HANDLER.handleRequest(exchange);
            return false;
        }

        /*
         *  Origin check, if it is set the Origin header should match the Host otherwise reject the request.
         *
         *  This check is for cross site scripted GET and POST requests.
         */
        final HeaderMap headers = exchange.getRequestHeaders();
        if (headers.contains(Headers.ORIGIN)) {
            String origin = headers.getFirst(Headers.ORIGIN);
            String host = headers.getFirst(Headers.HOST);
            String protocol = exchange.getRequestScheme();
            //This browser set header should not need IPv6 escaping
            String allowedOrigin = protocol + "://" + host;

            // This will reject multi-origin Origin headers due to the exact match.
            if (origin.equals(allowedOrigin) == false) {
                ROOT_LOGGER.debug("Request rejected due to HOST/ORIGIN mis-match.");
                ResponseCodeHandler.HANDLE_403.handleRequest(exchange);
                return false;
            }
        }
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7173.java