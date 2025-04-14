error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13866.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13866.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13866.java
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

import static org.jboss.as.domain.management.RealmConfigurationConstants.VERIFY_PASSWORD_CALLBACK_SUPPORTED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADVANCED_FILTER;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.BASE_DN;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RECURSIVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.USERNAME_ATTRIBUTE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.USER_DN;
import static org.jboss.as.domain.management.DomainManagementMessages.MESSAGES;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.RealmCallback;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.jboss.as.domain.management.AuthenticationMechanism;
import org.jboss.as.domain.management.CallbackHandlerServiceRegistry;
import org.jboss.as.domain.management.connections.ConnectionManager;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.sasl.callback.VerifyPasswordCallback;

/**
 * A CallbackHandler for users within an LDAP directory.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class UserLdapCallbackHandler implements Service<CallbackHandlerService>, CallbackHandlerService, CallbackHandler {

    public static final String SERVICE_SUFFIX = "ldap";

    private static final String DEFAULT_USER_DN = "dn";

    private final InjectedValue<ConnectionManager> connectionManager = new InjectedValue<ConnectionManager>();

    private final String baseDn;
    private final String usernameAttribute;
    private final String advancedFilter;
    private final boolean recursive;
    private final String userDn;
    protected final int searchTimeLimit = 10000; // TODO - Maybe make configurable.
    private final CallbackHandlerServiceRegistry registry;

    public UserLdapCallbackHandler(ModelNode userLdap, final CallbackHandlerServiceRegistry registry) {
        baseDn = userLdap.require(BASE_DN).asString();
        if (userLdap.hasDefined(USERNAME_ATTRIBUTE)) {
            usernameAttribute = userLdap.require(USERNAME_ATTRIBUTE).asString();
            advancedFilter = null;
        } else if (userLdap.hasDefined(ADVANCED_FILTER)) {
            advancedFilter = userLdap.require(ADVANCED_FILTER).asString();
            usernameAttribute = null;
        } else {
            throw MESSAGES.oneOfRequired(USERNAME_ATTRIBUTE, ADVANCED_FILTER);
        }

        if (userLdap.hasDefined(RECURSIVE)) {
            recursive = userLdap.require(RECURSIVE).asBoolean();
        } else {
            recursive = false;
        }
        if (userLdap.hasDefined(USER_DN)) {
            userDn = userLdap.require(USER_DN).asString();
        } else {
            userDn = DEFAULT_USER_DN;
        }
        this.registry = registry;
    }



    /*
     * CallbackHandlerService Methods
     */

    public AuthenticationMechanism getPreferredMechanism() {
        return AuthenticationMechanism.PLAIN;
    }

    public Set<AuthenticationMechanism> getSupplementaryMechanisms() {
        return Collections.emptySet();
    }

    public Map<String, String> getConfigurationOptions() {
        return Collections.singletonMap(VERIFY_PASSWORD_CALLBACK_SUPPORTED, Boolean.TRUE.toString());
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

    public CallbackHandlerService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    /*
     *  Access to Injectors
     */

    public InjectedValue<ConnectionManager> getConnectionManagerInjector() {
        return connectionManager;
    }


    /*
     *  CallbackHandler Method
     */

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        if (callbacks.length == 1 && callbacks[0] instanceof AuthorizeCallback) {
            AuthorizeCallback acb = (AuthorizeCallback) callbacks[0];
            String authenticationId = acb.getAuthenticationID();
            String authorizationId = acb.getAuthorizationID();

            acb.setAuthorized(authenticationId.equals(authorizationId));

            return;
        }

        ConnectionManager connectionManager = this.connectionManager.getValue();
        String username = null;
        VerifyPasswordCallback verifyPasswordCallback = null;

        for (Callback current : callbacks) {
            if (current instanceof NameCallback) {
                username = ((NameCallback) current).getDefaultName();
            } else if (current instanceof RealmCallback) {
                // TODO - Nothing at the moment
            } else if (current instanceof VerifyPasswordCallback) {
                verifyPasswordCallback = (VerifyPasswordCallback) current;
            } else {
                throw new UnsupportedCallbackException(current);
            }
        }

        if (username == null || username.length() == 0) {
            throw MESSAGES.noUsername();
        }
        if (verifyPasswordCallback == null) {
            throw MESSAGES.noPassword();
        }

        InitialDirContext searchContext = null;
        InitialDirContext userContext = null;
        NamingEnumeration<SearchResult> searchEnumeration = null;
        try {
            // 1 - Obtain Connection to LDAP
            searchContext = (InitialDirContext) connectionManager.getConnection();
            // 2 - Search to identify the DN of the user connecting
            SearchControls searchControls = new SearchControls();
            if (recursive) {
                searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            } else {
                searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
            }
            searchControls.setReturningAttributes(new String[]{userDn});
            searchControls.setTimeLimit(searchTimeLimit);

            Object[] filterArguments = new Object[]{username};
            String filter = usernameAttribute != null ? "(" + usernameAttribute + "={0})" : advancedFilter;

            searchEnumeration = searchContext.search(baseDn, filter, filterArguments, searchControls);
            if (searchEnumeration.hasMore() == false) {
                throw MESSAGES.userNotFoundInDirectory(username);
            }

            String distinguishedUserDN = null;

            SearchResult result = searchEnumeration.next();
            Attributes attributes = result.getAttributes();
            if (attributes != null) {
                Attribute dn = attributes.get(userDn);
                if (dn != null) {
                    distinguishedUserDN = (String) dn.get();
                }
            }
            if (distinguishedUserDN == null) {
                if (result.isRelative() == true)
                    distinguishedUserDN = result.getName() + ("".equals(baseDn) ? "" : "," + baseDn);
                else
                    throw MESSAGES.nameNotFound(result.getName());
            }

            // 3 - Connect as user once their DN is identified
            userContext = (InitialDirContext) connectionManager.getConnection(distinguishedUserDN, verifyPasswordCallback.getPassword());
            if (userContext != null) {
                verifyPasswordCallback.setVerified(true);
            }

        } catch (Exception e) {
            throw MESSAGES.cannotPerformVerification(e);
        } finally {
            safeClose(searchEnumeration);
            safeClose(searchContext);
            safeClose(userContext);
        }
    }

    private void safeClose(Context context) {
        if (context != null) {
            try {
                context.close();
            } catch (Exception ignored) {
            }
        }
    }

    private void safeClose(NamingEnumeration ne) {
        if (ne != null) {
            try {
                ne.close();
            } catch (Exception ignored) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13866.java