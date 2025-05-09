error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13543.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13543.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13543.java
text:
```scala
N@@ewOperationBuilder operation = new NewOperationBuilder(dmr);

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

package org.jboss.as.domain.http.server;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.domain.http.server.Constants.ACCEPT;
import static org.jboss.as.domain.http.server.Constants.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.jboss.as.domain.http.server.Constants.APPLICATION_DMR_ENCODED;
import static org.jboss.as.domain.http.server.Constants.APPLICATION_JSON;
import static org.jboss.as.domain.http.server.Constants.CONTENT_DISPOSITION;
import static org.jboss.as.domain.http.server.Constants.CONTENT_TYPE;
import static org.jboss.as.domain.http.server.Constants.GET;
import static org.jboss.as.domain.http.server.Constants.INTERNAL_SERVER_ERROR;
import static org.jboss.as.domain.http.server.Constants.METHOD_NOT_ALLOWED;
import static org.jboss.as.domain.http.server.Constants.OK;
import static org.jboss.as.domain.http.server.Constants.POST;
import static org.jboss.as.domain.http.server.Constants.TEXT_HTML;
import static org.jboss.as.domain.http.server.Constants.US_ASCII;
import static org.jboss.as.domain.http.server.Constants.UTF_8;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.as.controller.client.NewModelControllerClient;
import org.jboss.as.controller.client.NewOperationBuilder;
import org.jboss.as.domain.http.server.multipart.BoundaryDelimitedInputStream;
import org.jboss.as.domain.http.server.multipart.MimeHeaderParser;
import org.jboss.as.domain.http.server.security.BasicAuthenticator;
import org.jboss.as.domain.http.server.security.DigestAuthenticator;
import org.jboss.as.domain.management.SecurityRealm;
import org.jboss.as.domain.management.security.DomainCallbackHandler;
import org.jboss.com.sun.net.httpserver.Headers;
import org.jboss.com.sun.net.httpserver.HttpContext;
import org.jboss.com.sun.net.httpserver.HttpExchange;
import org.jboss.com.sun.net.httpserver.HttpServer;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;

/**
 * An embedded web server that provides a JSON over HTTP API to the domain management model.
 *
 * @author Jason T. Greene
 */
class DomainApiHandler implements ManagementHttpHandler {

    private static final String DOMAIN_API_CONTEXT = "/management";
    private static final String UPLOAD_REQUEST = DOMAIN_API_CONTEXT + "/add-content";

    private static Pattern MULTIPART_FD_BOUNDARY =  Pattern.compile("^multipart/form-data.*;\\s*boundary=(.*)$");
    private static Pattern DISPOSITION_FILE =  Pattern.compile("^form-data.*filename=\"?([^\"]*)?\"?.*$");

    private static final Logger log = Logger.getLogger("org.jboss.as.domain.http.api");

    /**
     * Represents all possible management operations that can be executed using HTTP GET
     */
    enum GetOperation {
        RESOURCE("read-resource"), ATTRIBUTE("read-attribute"), RESOURCE_DESCRIPTION("read-resource-description"), OPERATION_DESCRIPTION(
                "read-operation-description"), OPERATION_NAMES("read-operation-names");

        private String realOperation;

        GetOperation(String realOperation) {
            this.realOperation = realOperation;
        }

        public String realOperation() {
            return realOperation;
        }
    }

    private NewModelControllerClient modelController;

    DomainApiHandler(NewModelControllerClient modelController) {
        this.modelController = modelController;
    }

    public void handle(HttpExchange http) throws IOException {
        final URI request = http.getRequestURI();
        final String requestMethod = http.getRequestMethod();

        /*
         * Detect the file upload request. If it is not present, submit the incoming request to the normal handler.
         */
        if (POST.equals(requestMethod) && UPLOAD_REQUEST.equals(request.getPath())) {
            processUploadRequest(http);
        } else {
            processRequest(http);
        }
    }

    /**
     * Handle a form POST deployment upload request.
     *
     * @param http The HttpExchange object that allows access to the request and response.
     * @throws IOException if an error occurs while attempting to extract the deployment from the multipart/form data.
     */
    private void processUploadRequest(final HttpExchange http) throws IOException {
        File tempUploadFile = null;
        ModelNode response = null;

        try {
            SeekResult result = seekToDeployment(http);

            final ModelNode dmr = new ModelNode();
            dmr.get("operation").set("upload-deployment-stream");
            dmr.get("address").setEmptyList();
            dmr.get("input-stream-index").set(0);

            NewOperationBuilder operation = NewOperationBuilder.Factory.create(dmr);
            operation.addInputStream(result.stream);
            response = modelController.execute(operation.build());
            drain(http.getRequestBody());
        } catch (Throwable t) {
            // TODO Consider draining input stream
            log.error("Unexpected error executing deployment upload request", t);
            http.sendResponseHeaders(INTERNAL_SERVER_ERROR, -1);

            return;
        }

        // TODO Determine what format the response should be in for a deployment upload request.
        writeResponse(http, false, false, response, OK, false, TEXT_HTML);
    }

    /**
     * Handles a operation request via HTTP.
     *
     * @param http The HttpExchange object that allows access to the request and response.
     * @throws IOException if an error occurs while attempting to process the request.
     */
    private void processRequest(final HttpExchange http) throws IOException {
        final URI request = http.getRequestURI();
        final String requestMethod = http.getRequestMethod();

        boolean isGet = GET.equals(requestMethod);
        if (!isGet && !POST.equals(requestMethod)) {
            http.sendResponseHeaders(METHOD_NOT_ALLOWED, -1);

            return;
        }

        ModelNode dmr = null;
        ModelNode response;
        int status = OK;

        Headers requestHeaders = http.getRequestHeaders();
        boolean encode = APPLICATION_DMR_ENCODED.equals(requestHeaders.getFirst(ACCEPT))
 APPLICATION_DMR_ENCODED.equals(requestHeaders.getFirst(CONTENT_TYPE));

        try {
            dmr = isGet ? convertGetRequest(request) : convertPostRequest(http.getRequestBody(), encode);
            response = modelController.execute(NewOperationBuilder.Factory.create(dmr).build());
        } catch (Throwable t) {
            log.error("Unexpected error executing model request", t);

            http.sendResponseHeaders(INTERNAL_SERVER_ERROR, -1);

            return;
        }

        if (response.hasDefined(OUTCOME) && FAILED.equals(response.get(OUTCOME).asString())) {
            status = INTERNAL_SERVER_ERROR;
        }

        boolean pretty = dmr.hasDefined("json.pretty") && dmr.get("json.pretty").asBoolean();
        writeResponse(http, isGet, pretty, response, status, encode);
    }

     private void writeResponse(final HttpExchange http, boolean isGet, boolean pretty, ModelNode response, int status,
            boolean encode) throws IOException {
         String contentType = encode ? APPLICATION_DMR_ENCODED : APPLICATION_JSON;
         writeResponse(http, isGet, pretty, response, status, encode, contentType);
     }

    /**
     * Writes the HTTP response to the output stream.
     *
     * @param http The HttpExchange object that allows access to the request and response.
     * @param isGet Flag indicating whether or not the request was a GET request or POST request.
     * @param pretty Flag indicating whether or not the output, if JSON, should be pretty printed or not.
     * @param response The DMR response from the operation.
     * @param status The HTTP status code to be included in the response.
     * @param encode Flag indicating whether or not to Base64 encode the response payload.
     * @throws IOException if an error occurs while attempting to generate the HTTP response.
     */
    private void writeResponse(final HttpExchange http, boolean isGet, boolean pretty, ModelNode response, int status,
            boolean encode, String contentType) throws IOException {
        final Headers responseHeaders = http.getResponseHeaders();
        responseHeaders.add(CONTENT_TYPE, contentType);
        responseHeaders.add(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        http.sendResponseHeaders(status, 0);

        final OutputStream out = http.getResponseBody();
        final PrintWriter print = new PrintWriter(out);

        // GET (read) operations will never have a compensating update, and the status is already
        // available via the http response status code, so unwrap them.
        if (isGet && status == OK)
            response = response.get("result");

        try {
            if (encode) {
                response.writeBase64(out);
            } else {
                response.writeJSONString(print, !pretty);
            }
        } finally {
            print.flush();
            out.flush();
            safeClose(print);
            safeClose(out);
        }
    }

    private static final class SeekResult {
        BoundaryDelimitedInputStream stream;
        String fileName;
    }

    /**
     * Extracts the body content contained in a POST request.
     *
     * @param http The <code>HttpExchange</code> object containing POST request data.
     * @return a result containing the stream and the file name reported by the client
     * @throws IOException if an error occurs while attempting to extract the POST request data.
     */
    private SeekResult seekToDeployment(final HttpExchange http) throws IOException {
        final String type = http.getRequestHeaders().getFirst(CONTENT_TYPE);
        if (type == null)
            throw new IllegalArgumentException("No content type provided");

        Matcher matcher = MULTIPART_FD_BOUNDARY.matcher(type);
        if (!matcher.matches())
            throw new IllegalArgumentException("Invalid content type provided: " + type);

        final String boundary = "--" + matcher.group(1);

        final BoundaryDelimitedInputStream stream = new BoundaryDelimitedInputStream(http.getRequestBody(), boundary.getBytes("US-ASCII"));

        // Eat preamble
        byte[] ignore = new byte[1024];
        while (stream.read(ignore) != -1) {}

        // From here on out a boundary is prefixed with a CRLF that should be skipped
        stream.setBoundary(("\r\n" + boundary).getBytes(US_ASCII));

        while (!stream.isOuterStreamClosed()) {
            // purposefully send the trailing CRLF to headers so that a headerless body can be detected
            MimeHeaderParser.ParseResult result = MimeHeaderParser.parseHeaders(stream);
            if (result.eof()) continue; // Skip content-less part

            Headers partHeaders = result.headers();
            String disposition = partHeaders.getFirst(CONTENT_DISPOSITION);
            if (disposition != null) {
                matcher = DISPOSITION_FILE.matcher(disposition);
                if (matcher.matches()) {
                    SeekResult seek = new SeekResult();
                    seek.fileName = matcher.group(1);
                    seek.stream = stream;

                    return seek;
                }
            }

            while (stream.read(ignore) != -1) {}
        }

        throw new IllegalArgumentException("Request did not contain a deployment");
    }

    private void drain(InputStream stream) {
        try {
            byte[] ignore = new byte[1024];
            while (stream.read(ignore) != -1) {}
        } catch (Throwable eat) {
        }
    }

    private void safeClose(Closeable close) {
        try {
            close.close();
        } catch (Throwable eat) {
        }
    }

    private ModelNode convertPostRequest(InputStream stream, boolean encode) throws IOException {
        return encode ? ModelNode.fromBase64(stream) : ModelNode.fromJSONStream(stream);
    }

    private ModelNode convertGetRequest(URI request) {
        ArrayList<String> pathSegments = decodePath(request.getRawPath());
        Map<String, String> queryParameters = decodeQuery(request.getRawQuery());

        GetOperation operation = null;
        ModelNode dmr = new ModelNode();
        for (Entry<String, String> entry : queryParameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if ("operation".equals(key)) {
                try {
                    operation = GetOperation.valueOf(value.toUpperCase().replace('-', '_'));
                    value = operation.realOperation();
                } catch (Exception e) {
                    // Unknown
                    continue;
                }
            }

            dmr.get(entry.getKey()).set(value);
        }

        if (operation == null) {
            operation = GetOperation.RESOURCE;
            dmr.get("operation").set(operation.realOperation);
        }

        if (operation == GetOperation.RESOURCE && !dmr.has("recursive"))
            dmr.get("recursive").set(false);

        ModelNode list = dmr.get("address").setEmptyList();
        for (int i = 1; i < pathSegments.size() - 1; i += 2) {
            list.add(pathSegments.get(i), pathSegments.get(i + 1));
        }
        return dmr;
    }

    private ArrayList<String> decodePath(String path) {
        if (path == null)
            throw new IllegalArgumentException();

        int i = path.charAt(0) == '/' ? 1 : 0;

        ArrayList<String> segments = new ArrayList<String>();

        do {
            int j = path.indexOf('/', i);
            if (j == -1)
                j = path.length();

            segments.add(unescape(path.substring(i, j)));
            i = j + 1;
        } while (i < path.length());

        return segments;
    }

    private String unescape(String string) {
        try {
            // URLDecoder could be way more efficient, replace it one day
            return URLDecoder.decode(string, UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    private Map<String, String> decodeQuery(String query) {
        if (query == null || query.isEmpty())
            return Collections.emptyMap();

        int i = 0;
        Map<String, String> parameters = new HashMap<String, String>();

        do {
            int j = query.indexOf('&', i);
            if (j == -1)
                j = query.length();

            String pair = query.substring(i, j);
            int k = pair.indexOf('=');

            String key;
            String value;
            if (k == -1) {
                key = unescape(pair);
                value = "true";
            } else {
                key = unescape(pair.substring(0, k));
                value = unescape(pair.substring(k + 1, pair.length()));
            }

            parameters.put(key, value);

            i = j + 1;
        } while (i < query.length());

        return parameters;
    }

    public void start(HttpServer httpServer, SecurityRealm securityRealm) {
        HttpContext context = httpServer.createContext(DOMAIN_API_CONTEXT, this);
        if (securityRealm != null) {
            DomainCallbackHandler callbackHandler = securityRealm.getCallbackHandler();
            Class[] supportedCallbacks = callbackHandler.getSupportedCallbacks();
            if (DigestAuthenticator.requiredCallbacksSupported(supportedCallbacks)) {
                context.setAuthenticator(new DigestAuthenticator(callbackHandler, securityRealm.getName()));
            } else if (BasicAuthenticator.requiredCallbacksSupported(supportedCallbacks)) {
                context.setAuthenticator(new BasicAuthenticator(callbackHandler, securityRealm.getName()));
            }
        }
    }

    public void stop(HttpServer httpServer) {
        httpServer.removeContext(DOMAIN_API_CONTEXT);
        modelController = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13543.java