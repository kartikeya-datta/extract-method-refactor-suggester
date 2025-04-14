error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13865.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13865.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13865.java
text:
```scala
public C@@allbackHandler getCallbackHandler(Map<String, Object> sharedState) {

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

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PASSWORD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.USER;
import static org.jboss.as.domain.management.DomainManagementMessages.MESSAGES;
import static org.jboss.as.domain.management.RealmConfigurationConstants.DIGEST_PLAIN_TEXT;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.RealmCallback;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.as.domain.management.AuthenticationMechanism;
import org.jboss.as.domain.management.CallbackHandlerServiceRegistry;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;

/**
 * A CallbackHandler for users defined within the domain mode.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class UserDomainCallbackHandler implements Service<CallbackHandlerService>, CallbackHandlerService, CallbackHandler {

    public static final String SERVICE_SUFFIX = "users";

    private final String realm;
    private final CallbackHandlerServiceRegistry registry;

    private volatile ModelNode userDomain;

    public UserDomainCallbackHandler(String realm, ModelNode userDomain, final CallbackHandlerServiceRegistry registry) {
        this.realm = realm;
        setUserDomain(userDomain);
        this.registry = registry;
    }

    void setUserDomain(final ModelNode userDomain) {
        this.userDomain = userDomain == null || !userDomain.isDefined() ? new ModelNode().setEmptyObject() : userDomain.clone();
    }

    /*
     * CallbackHandlerService Methods
     */

    public AuthenticationMechanism getPreferredMechanism() {
        return AuthenticationMechanism.DIGEST;
    }

    public Set<AuthenticationMechanism> getSupplementaryMechanisms() {
        return Collections.singleton(AuthenticationMechanism.PLAIN);
    }

    public Map<String, String> getConfigurationOptions() {
        return Collections.singletonMap(DIGEST_PLAIN_TEXT, Boolean.TRUE.toString());
    }

    public boolean isReady() {
        return true;
    }

    public CallbackHandler getCallbackHandler() {
        return this;
    }

    /*
     *  Service Methods
     */


    public void start(StartContext context) throws StartException {
        registry.register(getPreferredMechanism(), this);
    }

    public void stop(StopContext context) {
        registry.unregister(getPreferredMechanism(), this);
    }

    public UserDomainCallbackHandler getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    /*
     *  CallbackHandler Method
     */

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        final ModelNode userMap = this.userDomain;

        List<Callback> toRespondTo = new LinkedList<Callback>();

        String userName = null;
        ModelNode user = null;

        // A single pass may be sufficient but by using a two pass approach the Callbackhandler will not
        // fail if an unexpected order is encountered.

        // First Pass - is to double check no unsupported callbacks and to retrieve
        // information from the callbacks passing in information.
        for (Callback current : callbacks) {

            if (current instanceof AuthorizeCallback) {
                toRespondTo.add(current);
            } else if (current instanceof NameCallback) {
                NameCallback nameCallback = (NameCallback) current;
                userName = nameCallback.getDefaultName();
                if (userMap.get(USER).hasDefined(userName)) {
                    user = userMap.get(USER, userName);
                }
            } else if (current instanceof PasswordCallback) {
                toRespondTo.add(current);
            } else if (current instanceof RealmCallback) {
                String realm = ((RealmCallback) current).getDefaultText();
                if (this.realm.equals(realm) == false) {
                    // TODO - Check if this needs a real error or of just an unexpected internal error.
                    throw MESSAGES.invalidRealm(realm, this.realm);
                }
            } else {
                throw new UnsupportedCallbackException(current);
            }
        }

        // Second Pass - Now iterate the Callback(s) requiring a response.
        for (Callback current : toRespondTo) {
            if (current instanceof AuthorizeCallback) {
                AuthorizeCallback authorizeCallback = (AuthorizeCallback) current;
                // Don't support impersonating another identity
                authorizeCallback.setAuthorized(authorizeCallback.getAuthenticationID().equals(authorizeCallback.getAuthorizationID()));
            } else if (current instanceof PasswordCallback) {
                if (user == null) {
                    throw new UserNotFoundException(userName);
                }
                String password = user.require(PASSWORD).asString();
                ((PasswordCallback) current).setPassword(password.toCharArray());
            }
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13865.java