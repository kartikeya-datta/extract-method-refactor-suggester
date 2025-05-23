error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4160.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4160.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4160.java
text:
```scala
i@@nt result = this.adapter.getActiveSessions().size();

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
package org.wildfly.clustering.web.undertow.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.session.SessionConfig;
import io.undertow.server.session.SessionListener;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.wildfly.clustering.web.Batcher;
import org.wildfly.clustering.web.session.ImmutableSession;
import org.wildfly.clustering.web.session.Session;
import org.wildfly.clustering.web.session.SessionManager;

public class SessionManagerFacadeTestCase {
    private final SessionManager<Void> manager = mock(SessionManager.class);
    private final SessionListener listener = mock(SessionListener.class);

    private SessionManagerAdapter adapter = new SessionManagerAdapter(this.manager);

    @Before
    public void init() {
        this.adapter.registerSessionListener(this.listener);
    }

    @Test
    public void parse() {
        Map.Entry<String, String> result = this.adapter.parse("session1.route1");
        assertEquals("session1", result.getKey());
        assertEquals("route1", result.getValue());

        result = this.adapter.parse("session2");
        assertEquals("session2", result.getKey());
        assertNull(result.getValue());

        result = this.adapter.parse(null);
        assertNull(result.getKey());
        assertNull(result.getValue());
    }

    @Test
    public void format() {
        assertEquals("session1.route1", this.adapter.format("session1", "route1"));
        assertEquals("session2", this.adapter.format("session2", null));
        assertNull(this.adapter.format(null, null));
    }

    @Test
    public void start() {
        this.adapter.start();
        
        verify(this.manager).start();
    }

    @Test
    public void stop() {
        this.adapter.stop();
        
        verify(this.manager).stop();
    }

    @Test
    public void setDefaultSessionTimeout() {
        this.adapter.setDefaultSessionTimeout(10);
        
        verify(this.manager).setDefaultMaxInactiveInterval(10L, TimeUnit.SECONDS);
    }

    @Test
    public void createSessionNoSessionId() {
        HttpServerExchange exchange = new HttpServerExchange(null);
        Batcher batcher = mock(Batcher.class);
        SessionConfig config = mock(SessionConfig.class);
        Session<Void> session = mock(Session.class);
        String sessionId = "session";
        String route = "route";
        String routingSessionId = "session.route";
        
        when(this.manager.createSessionId()).thenReturn(sessionId);
        when(this.manager.containsSession(sessionId)).thenReturn(false);
        when(this.manager.createSession(sessionId)).thenReturn(session);
        when(this.manager.locate(sessionId)).thenReturn(route);
        when(this.manager.getBatcher()).thenReturn(batcher);
        when(batcher.startBatch()).thenReturn(true);
        when(session.getId()).thenReturn(sessionId);
        
        io.undertow.server.session.Session sessionAdapter = this.adapter.createSession(exchange, config);
        
        assertNotNull(sessionAdapter);
        
        verify(this.listener).sessionCreated(sessionAdapter, exchange);
        verify(config).setSessionId(exchange, routingSessionId);
        
        String expected = "expected";
        when(session.getId()).thenReturn(expected);
        
        String result = sessionAdapter.getId();
        assertSame(expected, result);
    }

    @Test
    public void createSessionSpecifiedSessionId() {
        HttpServerExchange exchange = new HttpServerExchange(null);
        Batcher batcher = mock(Batcher.class);
        SessionConfig config = mock(SessionConfig.class);
        Session<Void> session = mock(Session.class);
        String requestedSessionId = "session.route1";
        String sessionId = "session";
        String route = "route";
        String routingSessionId = "session.route";
        
        when(config.findSessionId(exchange)).thenReturn(requestedSessionId);
        when(this.manager.containsSession(sessionId)).thenReturn(false);
        when(this.manager.createSession(sessionId)).thenReturn(session);
        when(this.manager.locate(sessionId)).thenReturn(route);
        when(this.manager.getBatcher()).thenReturn(batcher);
        when(batcher.startBatch()).thenReturn(true);
        when(session.getId()).thenReturn(sessionId);
        
        io.undertow.server.session.Session sessionAdapter = this.adapter.createSession(exchange, config);
        
        assertNotNull(sessionAdapter);
        
        verify(this.listener).sessionCreated(sessionAdapter, exchange);
        verify(config).setSessionId(exchange, routingSessionId);
        
        String expected = "expected";
        when(session.getId()).thenReturn(expected);
        
        String result = sessionAdapter.getId();
        assertSame(expected, result);
    }

    @Test
    public void createSessionAlreadyExists() {
        HttpServerExchange exchange = new HttpServerExchange(null);
        SessionConfig config = mock(SessionConfig.class);
        String requestedSessionId = "session.route1";
        String sessionId = "session";
        
        when(config.findSessionId(exchange)).thenReturn(requestedSessionId);
        when(this.manager.containsSession(sessionId)).thenReturn(true);
        
        IllegalStateException exception = null;
        try {
            this.adapter.createSession(exchange, config);
        } catch (IllegalStateException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    @Test
    public void getSession() {
        HttpServerExchange exchange = new HttpServerExchange(null);
        Batcher batcher = mock(Batcher.class);
        SessionConfig config = mock(SessionConfig.class);
        Session<Void> session = mock(Session.class);
        String requestedSessionId = "session.route1";
        String sessionId = "session";
        String route = "route2";
        String routingSessionId = "session.route2";
        
        when(config.findSessionId(exchange)).thenReturn(requestedSessionId);
        when(this.manager.findSession(sessionId)).thenReturn(session);
        when(this.manager.locate(sessionId)).thenReturn(route);
        when(this.manager.getBatcher()).thenReturn(batcher);
        when(batcher.startBatch()).thenReturn(true);
        when(session.getId()).thenReturn(sessionId);

        io.undertow.server.session.Session sessionAdapter = this.adapter.getSession(exchange, config);
        
        assertNotNull(sessionAdapter);
        
        verify(config).setSessionId(exchange, routingSessionId);
        
        String expected = "expected";
        when(session.getId()).thenReturn(expected);
        
        String result = sessionAdapter.getId();
        assertSame(expected, result);
    }

    @Test
    public void getSessionNoSessionId() {
        HttpServerExchange exchange = new HttpServerExchange(null);
        SessionConfig config = mock(SessionConfig.class);
        
        when(config.findSessionId(exchange)).thenReturn(null);

        io.undertow.server.session.Session sessionAdapter = this.adapter.getSession(exchange, config);
        
        assertNull(sessionAdapter);
    }

    @Test
    public void getSessionNotExists() {
        HttpServerExchange exchange = new HttpServerExchange(null);
        Batcher batcher = mock(Batcher.class);
        SessionConfig config = mock(SessionConfig.class);
        String requestedSessionId = "session.route1";
        String sessionId = "session";
        
        when(config.findSessionId(exchange)).thenReturn(requestedSessionId);
        when(this.manager.findSession(sessionId)).thenReturn(null);
        when(this.manager.getBatcher()).thenReturn(batcher);
        when(batcher.startBatch()).thenReturn(true);

        io.undertow.server.session.Session sessionAdapter = this.adapter.getSession(exchange, config);
        
        assertNull(sessionAdapter);
        
        verify(batcher).endBatch(false);
    }

    @Test
    public void activeSessions() {
        when(this.manager.getActiveSessions()).thenReturn(Collections.singleton("expected"));
        
        int result = this.adapter.activeSessions();
        
        assertEquals(1, result);
    }

    @Test
    public void getTransientSessions() {
        Set<String> result = this.adapter.getTransientSessions();
        
        assertTrue(result.isEmpty());
    }

    @Test
    public void getActiveSessions() {
        String expected = "expected";
        when(this.manager.getActiveSessions()).thenReturn(Collections.singleton(expected));

        Set<String> result = this.adapter.getActiveSessions();
        
        assertEquals(1, result.size());
        assertSame(expected, result.iterator().next());
    }

    @Test
    public void getAllSessions() {
        String expected = "expected";
        when(this.manager.getLocalSessions()).thenReturn(Collections.singleton(expected));

        Set<String> result = this.adapter.getAllSessions();
        
        assertEquals(1, result.size());
        assertSame(expected, result.iterator().next());
    }

    @Test
    public void getSessionByIdentifier() {
        ImmutableSession session = mock(ImmutableSession.class);
        String id = "session";
        Batcher batcher = mock(Batcher.class);
        
        when(this.manager.getBatcher()).thenReturn(batcher);
        when(this.manager.viewSession(id)).thenReturn(session);
        when(session.getId()).thenReturn(id);
        when(batcher.startBatch()).thenReturn(true);
        
        io.undertow.server.session.Session result = this.adapter.getSession(id);
        
        assertSame(this.adapter, result.getSessionManager());
        assertSame(id, result.getId());
        
        verify(batcher).endBatch(false);
    }

    @Test
    public void getSessionByIdentifierNotExists() {
        String id = "session";
        Batcher batcher = mock(Batcher.class);
        
        when(this.manager.getBatcher()).thenReturn(batcher);
        when(this.manager.viewSession(id)).thenReturn(null);
        when(batcher.startBatch()).thenReturn(true);
        
        io.undertow.server.session.Session result = this.adapter.getSession(id);
        
        assertNull(result);

        verify(batcher).endBatch(false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4160.java