error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5195.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5195.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5195.java
text:
```scala
r@@esourceRegistration.registerSubModel(LdapConnectionResourceDefinition.newInstance());

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

package org.jboss.as.domain.management;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CORE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CORE_SERVICE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MANAGEMENT;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.access.management.AccessConstraintUtilizationRegistry;
import org.jboss.as.controller.access.management.DelegatingConfigurableAuthorizer;
import org.jboss.as.controller.audit.ManagedAuditLogger;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.services.path.PathManagerService;
import org.jboss.as.domain.management._private.DomainManagementResolver;
import org.jboss.as.domain.management.access.AccessAuthorizationResourceDefinition;
import org.jboss.as.domain.management.audit.AccessAuditResourceDefinition;
import org.jboss.as.domain.management.audit.EnvironmentNameReader;
import org.jboss.as.domain.management.connections.ldap.LdapConnectionResourceDefinition;
import org.jboss.as.domain.management.security.SecurityRealmResourceDefinition;

/**
 * A {@link org.jboss.as.controller.ResourceDefinition} for the the core management resource.
 *
 * The content of this resource is dependent on the process it is being used within i.e. standalone server, host controller or
 * domain server.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class CoreManagementResourceDefinition extends SimpleResourceDefinition {

    public static final PathElement PATH_ELEMENT = PathElement.pathElement(CORE_SERVICE, MANAGEMENT);

    public static void registerDomainResource(Resource parent, AccessConstraintUtilizationRegistry registry) {
        Resource coreManagement = Resource.Factory.create();
        coreManagement.registerChild(AccessAuthorizationResourceDefinition.PATH_ELEMENT,
                AccessAuthorizationResourceDefinition.createResource(registry));
        parent.registerChild(PATH_ELEMENT, coreManagement);
    }

    private final Environment environment;
    private final List<ResourceDefinition> interfaces;
    private final DelegatingConfigurableAuthorizer authorizer;
    private final ManagedAuditLogger auditLogger;
    private final PathManagerService pathManager;
    private final EnvironmentNameReader environmentReader;

    private CoreManagementResourceDefinition(final Environment environment, final DelegatingConfigurableAuthorizer authorizer,
            final ManagedAuditLogger auditLogger, final PathManagerService pathManager, final EnvironmentNameReader environmentReader,
            final List<ResourceDefinition> interfaces) {
        super(PATH_ELEMENT, DomainManagementResolver.getResolver(CORE, MANAGEMENT));
        this.environment = environment;
        this.authorizer = authorizer;
        this.interfaces = interfaces;
        this.auditLogger = auditLogger;
        this.pathManager = pathManager;
        this.environmentReader = environmentReader;
    }

    @Override
    public void registerChildren(ManagementResourceRegistration resourceRegistration) {
        if (environment != Environment.DOMAIN) {
            resourceRegistration.registerSubModel(SecurityRealmResourceDefinition.INSTANCE);
            resourceRegistration.registerSubModel(LdapConnectionResourceDefinition.INSTANCE);
        }

        for (ResourceDefinition current : interfaces) {
            resourceRegistration.registerSubModel(current);
        }

        boolean registerAuditLog = true;
        switch (environment) {
            case DOMAIN:
                resourceRegistration.registerSubModel(AccessAuthorizationResourceDefinition.forDomain(authorizer));
                registerAuditLog = false;
                break;
            case DOMAIN_SERVER:
                resourceRegistration.registerSubModel(AccessAuthorizationResourceDefinition.forDomainServer(authorizer));
                break;
            case HOST_CONTROLLER:
                resourceRegistration.registerSubModel(AccessAuthorizationResourceDefinition.forHost(authorizer));
                break;
            case STANDALONE_SERVER:
                resourceRegistration.registerSubModel(AccessAuthorizationResourceDefinition.forStandaloneServer(authorizer));
        }

        if (registerAuditLog) {
            resourceRegistration.registerSubModel(new AccessAuditResourceDefinition(auditLogger, pathManager, environmentReader));
        }
    }

    public static SimpleResourceDefinition forDomain(final DelegatingConfigurableAuthorizer authorizer) {
        List<ResourceDefinition> interfaces = Collections.emptyList();
        return new CoreManagementResourceDefinition(Environment.DOMAIN, authorizer, null, null, null, interfaces);
    }

    public static SimpleResourceDefinition forDomainServer(final DelegatingConfigurableAuthorizer authorizer,
            final ManagedAuditLogger auditLogger, final PathManagerService pathManager, final EnvironmentNameReader environmentReader) {
        List<ResourceDefinition> interfaces = Collections.emptyList();
        return new CoreManagementResourceDefinition(Environment.DOMAIN_SERVER, authorizer, auditLogger, pathManager, environmentReader, interfaces);
    }

    public static SimpleResourceDefinition forHost(final DelegatingConfigurableAuthorizer authorizer,
            final ManagedAuditLogger auditLogger, final PathManagerService pathManager, final EnvironmentNameReader environmentReader,
            final ResourceDefinition... interfaces) {
        return new CoreManagementResourceDefinition(Environment.HOST_CONTROLLER, authorizer, auditLogger, pathManager, environmentReader, Arrays.asList(interfaces));
    }

    public static SimpleResourceDefinition forStandaloneServer(final DelegatingConfigurableAuthorizer authorizer,
            final ManagedAuditLogger auditLogger, final PathManagerService pathManager, final EnvironmentNameReader environmentReader,
            final ResourceDefinition... interfaces) {
        return new CoreManagementResourceDefinition(Environment.STANDALONE_SERVER, authorizer, auditLogger, pathManager, environmentReader, Arrays.asList(interfaces));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5195.java