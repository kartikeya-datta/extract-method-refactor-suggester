error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1289.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1289.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1289.java
text:
```scala
i@@f (service != null) {

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

package org.jboss.as.domain.management.security;

import javax.net.ssl.SSLContext;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

import org.jboss.as.domain.management.CallbackHandlerFactory;
import org.jboss.as.domain.management.SecurityRealm;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

import static org.jboss.as.domain.management.DomainManagementLogger.ROOT_LOGGER;
import static org.jboss.as.domain.management.DomainManagementMessages.MESSAGES;

/**
 * The service representing the security realm, this service will be injected into any management interfaces
 * requiring any of the capabilities provided by the realm.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class SecurityRealmService implements Service<SecurityRealmService>, SecurityRealm {

    public static final ServiceName BASE_SERVICE_NAME = ServiceName.JBOSS.append("server", "controller", "management", "security_realm");

    private final InjectedValue<DomainCallbackHandler> callbackHandler = new InjectedValue<DomainCallbackHandler>();
    private final InjectedValue<SSLIdentityService> sslIdentity = new InjectedValue<SSLIdentityService>();
    private final InjectedValue<CallbackHandlerFactory> secretCallbackFactory = new InjectedValue<CallbackHandlerFactory>();

    private final String name;

    public SecurityRealmService(String name) {
        this.name = name;
    }

    public void start(StartContext context) throws StartException {
        ROOT_LOGGER.debugf("Starting '%s' Security Realm Service", name);
    }

    public void stop(StopContext context) {
        ROOT_LOGGER.debugf("Stopping '%s' Security Realm Service", name);
    }

    public SecurityRealmService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    public String getName() {
        return name;
    }

    public InjectedValue<DomainCallbackHandler> getCallbackHandlerInjector() {
        return callbackHandler;
    }

    public InjectedValue<SSLIdentityService> getSSLIdentityInjector() {
        return sslIdentity;
    }

    public InjectedValue<CallbackHandlerFactory> getSecretCallbackFactory() {
        return secretCallbackFactory;
    }

    /**
     * Used to obtain the callback handler for the configured 'authorizations'.
     *
     * @return The CallbackHandler to be used for verifying the identity of the caller.
     */
    public DomainCallbackHandler getCallbackHandler() {
        DomainCallbackHandler response = callbackHandler.getOptionalValue();
        if (response == null) {
            response = new DomainCallbackHandler() {
                public Class[] getSupportedCallbacks() {
                    return new Class[0];
                }

                public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                    throw MESSAGES.noAuthenticationDefined();
                }

                @Override
                public boolean isReady() {
                    return false;
                }
            };
        }

        return response;
    }

    public SSLContext getSSLContext() {
        SSLIdentityService service = sslIdentity.getOptionalValue();
        if (sslIdentity != null) {
            return service.getSSLContext();
        }

        return null;
    }

    public boolean hasTrustStore() {
        SSLIdentityService service;
        return ((service = sslIdentity.getOptionalValue()) != null && service.hasTrustStore());
    }

    public CallbackHandlerFactory getSecretCallbackHandlerFactory() {
        return secretCallbackFactory.getValue();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1289.java