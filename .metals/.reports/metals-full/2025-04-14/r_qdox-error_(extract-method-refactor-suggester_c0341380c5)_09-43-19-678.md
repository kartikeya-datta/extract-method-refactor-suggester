error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7857.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7857.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7857.java
text:
```scala
S@@tring id = manager.createIdentifier();

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

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.wildfly.clustering.web.Batch;
import org.wildfly.clustering.web.session.Session;
import org.wildfly.clustering.web.session.SessionManager;

import io.undertow.security.api.AuthenticatedSessionManager.AuthenticatedSession;
import io.undertow.security.idm.Account;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.session.SessionConfig;
import io.undertow.server.session.SessionListener.SessionDestroyedReason;
import io.undertow.servlet.handlers.security.CachedAuthenticatedSessionHandler;

/**
 * Adapts a distributable {@link Session} to an Undertow {@link io.undertow.server.session.Session}.
 * @author Paul Ferraro
 */
public class DistributableSession extends AbstractDistributableSession<Session<LocalSessionContext>> {
    // Undertow stores the authenticated session in the HttpSession using a special attribute with the following name
    private static final String AUTHENTICATED_SESSION_ATTRIBUTE_NAME = CachedAuthenticatedSessionHandler.class.getName() + ".AuthenticatedSession";

    private final UndertowSessionManager manager;
    private volatile Map.Entry<Session<LocalSessionContext>, SessionConfig> entry;
    private final Batch batch;

    public DistributableSession(UndertowSessionManager manager, Session<LocalSessionContext> session, SessionConfig config, Batch batch) {
        this.manager = manager;
        this.entry = new SimpleImmutableEntry<>(session, config);
        this.batch = batch;
    }

    @Override
    public io.undertow.server.session.SessionManager getSessionManager() {
        return this.manager;
    }

    @Override
    protected Session<LocalSessionContext> getSession() {
        return this.entry.getKey();
    }

    @Override
    public void requestDone(HttpServerExchange exchange) {
        this.getSession().close();
        this.batch.close();
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.getSession().getMetaData().setMaxInactiveInterval(interval, TimeUnit.SECONDS);
    }

    @Override
    public Object getAttribute(String name) {
        if (AUTHENTICATED_SESSION_ATTRIBUTE_NAME.equals(name)) {
            Account account = (Account) super.getAttribute(name);
            return (account != null) ? new AuthenticatedSession(account, HttpServletRequest.FORM_AUTH) : this.getSession().getLocalContext().getAuthenticatedSession();
        }
        return super.getAttribute(name);
    }

    @Override
    public Object setAttribute(String name, Object value) {
        if (value == null) {
            return this.removeAttribute(name);
        }
        if (AUTHENTICATED_SESSION_ATTRIBUTE_NAME.equals(name)) {
            AuthenticatedSession session = (AuthenticatedSession) value;
            // If using FORM authentication, we store the corresponding Account in a session attribute
            if (session.getMechanism().equals(HttpServletRequest.FORM_AUTH)) {
                Account account = (Account) this.getSession().getAttributes().setAttribute(name, session.getAccount());
                return (account != null) ? new AuthenticatedSession(account, HttpServletRequest.FORM_AUTH) : null;
            }
            // Otherwise we store the whole AuthenticatedSession in the local context
            LocalSessionContext context = this.getSession().getLocalContext();
            AuthenticatedSession old = context.getAuthenticatedSession();
            context.setAuthenticatedSession(session);
            return old;
        }
        Object old = this.getSession().getAttributes().setAttribute(name, value);
        if (old == null) {
            this.manager.getSessionListeners().attributeAdded(this, name, value);
        } else if (old != value) {
            this.manager.getSessionListeners().attributeUpdated(this, name, value, old);
        }
        return old;
    }

    @Override
    public Object removeAttribute(String name) {
        if (AUTHENTICATED_SESSION_ATTRIBUTE_NAME.equals(name)) {
            Account account = (Account) this.getSession().getAttributes().removeAttribute(name);
            if (account != null) {
                return new AuthenticatedSession(account, HttpServletRequest.FORM_AUTH);
            }
            LocalSessionContext context = this.getSession().getLocalContext();
            AuthenticatedSession old = context.getAuthenticatedSession();
            context.setAuthenticatedSession(null);
            return old;
        }
        Object old = this.getSession().getAttributes().removeAttribute(name);
        if (old != null) {
            this.manager.getSessionListeners().attributeRemoved(this, name, old);
        }
        return old;
    }

    @Override
    public void invalidate(HttpServerExchange exchange) {
        Map.Entry<Session<LocalSessionContext>, SessionConfig> entry = this.entry;
        Session<LocalSessionContext> session = entry.getKey();
        this.manager.getSessionListeners().sessionDestroyed(this, exchange, SessionDestroyedReason.INVALIDATED);
        session.invalidate();
        if (exchange != null) {
            String id = session.getId();
            entry.getValue().clearSession(exchange, id);
        }
        this.batch.close();
    }

    @Override
    public String changeSessionId(HttpServerExchange exchange, SessionConfig config) {
        Session<LocalSessionContext> oldSession = this.getSession();
        SessionManager<LocalSessionContext> manager = this.manager.getSessionManager();
        String id = manager.createSessionId();
        Session<LocalSessionContext> newSession = manager.createSession(id);
        for (String name: oldSession.getAttributes().getAttributeNames()) {
            newSession.getAttributes().setAttribute(name, oldSession.getAttributes().getAttribute(name));
        }
        newSession.getMetaData().setMaxInactiveInterval(oldSession.getMetaData().getMaxInactiveInterval(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
        newSession.getMetaData().setLastAccessedTime(oldSession.getMetaData().getLastAccessedTime());
        newSession.getLocalContext().setAuthenticatedSession(oldSession.getLocalContext().getAuthenticatedSession());
        config.setSessionId(exchange, id);
        this.entry = new SimpleImmutableEntry<>(newSession, config);
        oldSession.invalidate();
        return id;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7857.java