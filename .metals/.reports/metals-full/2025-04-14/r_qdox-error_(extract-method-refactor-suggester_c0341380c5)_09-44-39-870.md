error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2588.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2588.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2588.java
text:
```scala
r@@esponse = client.execute(new HttpGet(SessionOperationServlet.createGetURI(baseURL2, "a")));

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.as.test.clustering.cluster.web.expiration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.test.clustering.cluster.ClusterAbstractTestCase;
import org.jboss.as.test.clustering.cluster.web.ClusteredWebSimpleTestCase;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;

/**
 * Validates get/set/remove/invalidate session operations, session expiration, and their corresponding events
 *
 * @author Paul Ferraro
 */
public abstract class SessionExpirationTestCase extends ClusterAbstractTestCase {

    static WebArchive getBaseDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "expiration.war");
        war.addClasses(SessionOperationServlet.class, RecordingWebListener.class);
        // Take web.xml from the managed test.
        war.setWebXML(ClusteredWebSimpleTestCase.class.getPackage(), "web.xml");
        return war;
    }

    @Test
    public void test(@ArquillianResource(SessionOperationServlet.class) @OperateOnDeployment(DEPLOYMENT_1) URL baseURL1,
                     @ArquillianResource(SessionOperationServlet.class) @OperateOnDeployment(DEPLOYMENT_2) URL baseURL2)
                             throws IOException, URISyntaxException, InterruptedException {
        DefaultHttpClient client = org.jboss.as.test.http.util.HttpClientUtils.relaxedCookieHttpClient();
        try {
            // This should trigger session creation event, but not added attribute event
            HttpResponse response = client.execute(new HttpGet(SessionOperationServlet.createSetURI(baseURL1, "a")));
            try {
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
                Assert.assertEquals(response.getFirstHeader(SessionOperationServlet.SESSION_ID).getValue(), response.getFirstHeader(SessionOperationServlet.CREATED_SESSIONS).getValue());
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // This should trigger attribute added event and bound binding event
            response = client.execute(new HttpGet(SessionOperationServlet.createSetURI(baseURL1, "a", "1")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.SESSION_ID));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
                Assert.assertEquals("a", response.getFirstHeader(SessionOperationServlet.ADDED_ATTRIBUTES).getValue());
                Assert.assertEquals("1", response.getFirstHeader(SessionOperationServlet.BOUND_ATTRIBUTES).getValue());
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // No events should have been triggered on remote node
            response = client.execute(new HttpGet(SessionOperationServlet.createGetURI(baseURL2, "a")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.RESULT));
                Assert.assertEquals("1", response.getFirstHeader(SessionOperationServlet.RESULT).getValue());
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // Make sure remove attribute event is not fired since attribute does not exist
            response = client.execute(new HttpGet(SessionOperationServlet.createRemoveURI(baseURL2, "b")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.RESULT));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // This should trigger attribute replaced event, as well as valueBound/valueUnbound binding events
            response = client.execute(new HttpGet(SessionOperationServlet.createSetURI(baseURL1, "a", "2")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
                Assert.assertEquals("a", response.getFirstHeader(SessionOperationServlet.REPLACED_ATTRIBUTES).getValue());
                Assert.assertEquals("2", response.getFirstHeader(SessionOperationServlet.BOUND_ATTRIBUTES).getValue());
                Assert.assertEquals("1", response.getFirstHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES).getValue());
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // No events should have been triggered on remote node
            response = client.execute(new HttpGet(SessionOperationServlet.createGetURI(baseURL2, "a")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.RESULT));
                Assert.assertEquals("2", response.getFirstHeader(SessionOperationServlet.RESULT).getValue());
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // This should trigger attribute removed event and valueUnbound binding event
            response = client.execute(new HttpGet(SessionOperationServlet.createSetURI(baseURL1, "a")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
                Assert.assertEquals("a", response.getFirstHeader(SessionOperationServlet.REMOVED_ATTRIBUTES).getValue());
                Assert.assertEquals("2", response.getFirstHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES).getValue());
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // No events should have been triggered on remote node
            response = client.execute(new HttpGet(SessionOperationServlet.createGetURI(baseURL2, "a")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.RESULT));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // This should trigger attribute added event and valueBound binding event
            response = client.execute(new HttpGet(SessionOperationServlet.createSetURI(baseURL1, "a", "3", "4")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
                Assert.assertEquals("a", response.getFirstHeader(SessionOperationServlet.ADDED_ATTRIBUTES).getValue());
                Assert.assertEquals("3", response.getFirstHeader(SessionOperationServlet.BOUND_ATTRIBUTES).getValue());
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // No events should have been triggered on remote node
            response = client.execute(new HttpGet(SessionOperationServlet.createGetURI(baseURL2, "a")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.RESULT));
                Assert.assertEquals("4", response.getFirstHeader(SessionOperationServlet.RESULT).getValue());
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // This should trigger attribute removed event and valueUnbound binding event
            response = client.execute(new HttpGet(SessionOperationServlet.createRemoveURI(baseURL1, "a")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
                Assert.assertEquals("a", response.getFirstHeader(SessionOperationServlet.REMOVED_ATTRIBUTES).getValue());
                Assert.assertEquals("4", response.getFirstHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES).getValue());
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // No events should have been triggered on remote node
            response = client.execute(new HttpGet(SessionOperationServlet.createGetURI(baseURL2, "a")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.RESULT));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // This should trigger attribute added event and valueBound binding event
            response = client.execute(new HttpGet(SessionOperationServlet.createSetURI(baseURL2, "a", "5")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.SESSION_ID));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
                Assert.assertEquals("a", response.getFirstHeader(SessionOperationServlet.ADDED_ATTRIBUTES).getValue());
                Assert.assertEquals("5", response.getFirstHeader(SessionOperationServlet.BOUND_ATTRIBUTES).getValue());
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // This should trigger session destroyed event and valueUnbound binding event
            response = client.execute(new HttpGet(SessionOperationServlet.createInvalidateURI(baseURL1)));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.SESSION_ID));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
                Assert.assertEquals(response.getFirstHeader(SessionOperationServlet.SESSION_ID).getValue(), response.getFirstHeader(SessionOperationServlet.DESTROYED_SESSIONS).getValue());
                Assert.assertEquals("a",response.getFirstHeader(SessionOperationServlet.REMOVED_ATTRIBUTES).getValue());
                Assert.assertEquals("5", response.getFirstHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES).getValue());
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // This should trigger attribute added event and valueBound binding event
            response = client.execute(new HttpGet(SessionOperationServlet.createSetURI(baseURL2, "a", "6")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.SESSION_ID));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
                Assert.assertEquals(response.getFirstHeader(SessionOperationServlet.SESSION_ID).getValue(), response.getFirstHeader(SessionOperationServlet.CREATED_SESSIONS).getValue());
                Assert.assertEquals("a", response.getFirstHeader(SessionOperationServlet.ADDED_ATTRIBUTES).getValue());
                Assert.assertEquals("6", response.getFirstHeader(SessionOperationServlet.BOUND_ATTRIBUTES).getValue());
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // This should not trigger any events
            response = client.execute(new HttpGet(SessionOperationServlet.createGetURI(baseURL2, "a", "7")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.RESULT));
                Assert.assertEquals("6", response.getFirstHeader(SessionOperationServlet.RESULT).getValue());
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.SESSION_ID));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // This should not trigger any events
            response = client.execute(new HttpGet(SessionOperationServlet.createGetURI(baseURL2, "a")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.RESULT));
                Assert.assertEquals("7", response.getFirstHeader(SessionOperationServlet.RESULT).getValue());
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.SESSION_ID));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // Trigger session timeout in 1 second
            response = client.execute(new HttpGet(SessionOperationServlet.createTimeoutURI(baseURL1, 1)));
            String sessionId = null;
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.RESULT));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.SESSION_ID));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
                sessionId = response.getFirstHeader(SessionOperationServlet.SESSION_ID).getValue();
            } finally {
                HttpClientUtils.closeQuietly(response);
            }

            // Trigger timeout of sessionId
            Thread.sleep(2000);

            // Timeout should trigger session destroyed event and valueUnbound binding event
            response = client.execute(new HttpGet(SessionOperationServlet.createGetURI(baseURL1, "a")));
            try {
                Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatusLine().getStatusCode());
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.RESULT));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.SESSION_ID));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.CREATED_SESSIONS));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.DESTROYED_SESSIONS));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.ADDED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REPLACED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.REMOVED_ATTRIBUTES));
                Assert.assertFalse(response.containsHeader(SessionOperationServlet.BOUND_ATTRIBUTES));
                Assert.assertTrue(response.containsHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES));
                Assert.assertEquals(sessionId, response.getFirstHeader(SessionOperationServlet.DESTROYED_SESSIONS).getValue());
                Assert.assertEquals(response.getFirstHeader(SessionOperationServlet.SESSION_ID).getValue(), response.getFirstHeader(SessionOperationServlet.CREATED_SESSIONS).getValue());
                Assert.assertEquals("7", response.getFirstHeader(SessionOperationServlet.UNBOUND_ATTRIBUTES).getValue());
            } finally {
                HttpClientUtils.closeQuietly(response);
            }
        } finally {
            HttpClientUtils.closeQuietly(client);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2588.java