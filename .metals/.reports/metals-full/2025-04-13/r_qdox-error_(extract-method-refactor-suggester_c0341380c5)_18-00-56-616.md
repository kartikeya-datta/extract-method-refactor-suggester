error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3257.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3257.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3257.java
text:
```scala
i@@f (target.isNonFacadeMBeansSensitive() || target.isSuperUserOrAdminOnly()) {

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

package org.jboss.as.controller.access;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.access.AuthorizationResult.Decision;
import org.jboss.as.controller.access.constraint.ScopingConstraint;
import org.jboss.as.controller.access.permission.CombinationPolicy;
import org.jboss.as.controller.access.permission.ManagementPermissionAuthorizer;
import org.jboss.as.controller.access.rbac.DefaultPermissionFactory;
import org.jboss.as.controller.access.rbac.MockRoleMapper;
import org.jboss.as.controller.access.rbac.RoleMapper;
import org.jboss.as.controller.access.rbac.StandardRole;
import org.jboss.as.controller.operations.common.Util;

/**
 * Simple {@link ConfigurableAuthorizer} implementation that gives all permissions to any authenticated
 * user.
 * <p>Also supports the standard WildFly role-based permission scheme but does not support
 * configurable mapping of users/groups to roles. Instead the allowed roles can be specified via a
 * {@code roles} operation-header in the top level operation whose value is the name of a role or a DMR list
 * of strings each of which is the name of a role.</p>
 * <p>This operation-header based approach is only secure to the extent the clients using it are secure. To use this
 * approach the client must authenticate, and with this authorization provider any authenticated user has all privileges.
 * So, by adding the {@code roles} operation-header to the request the client can only reduce its privileges,
 * not increase them.
 * </p>
 *
 *
 * @author Brian Stansberry (c) 2013 Red Hat Inc.
 */
public class SimpleConfigurableAuthorizer implements ConfigurableAuthorizer {

    private final RoleMapper roleMapper;
    private final DefaultPermissionFactory permissionFactory;
    private final Authorizer authorizer;
    private final Set<String> addedRoles = new HashSet<String>();
    /** A fake action for JMX, just to have an operation with superuser permissions */
    private static final Action FAKE_JMX_ACTION = new Action(Util.createOperation("test", PathAddress.EMPTY_ADDRESS), null);

    public SimpleConfigurableAuthorizer() {
        this(MockRoleMapper.INSTANCE);
    }

    public SimpleConfigurableAuthorizer(final RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
        permissionFactory = new DefaultPermissionFactory(CombinationPolicy.PERMISSIVE, roleMapper);
        authorizer = new ManagementPermissionAuthorizer(permissionFactory);
    }

    @Override
    public boolean isRoleBased() {
        return true;
    }

    @Override
    public Set<String> getStandardRoles() {
        Set<String> result = new LinkedHashSet<String>();
        for (StandardRole stdRole : StandardRole.values()) {
            result.add(stdRole.toString());
        }
        return result;
    }

    @Override
    public Set<String> getAllRoles() {
        Set<String> result = getStandardRoles();
        synchronized (addedRoles) {
            result.addAll(addedRoles);
        }
        return result;
    }

    @Override
    public void addScopedRole(String roleName, String baseRole, ScopingConstraint scopingConstraint) {
        synchronized (addedRoles) {
            permissionFactory.addScopedRole(roleName, baseRole, scopingConstraint);
            addedRoles.add(roleName);
        }
    }

    @Override
    public void removeScopedRole(String roleName) {
        synchronized (addedRoles) {
            permissionFactory.removeScopedRole(roleName);
            addedRoles.remove(roleName);
        }
    }

    @Override
    public AuthorizationResult authorize(Caller caller, Environment callEnvironment, Action action, TargetAttribute target) {
        return authorizer.authorize(caller, callEnvironment, action, target);
    }

    @Override
    public AuthorizationResult authorize(Caller caller, Environment callEnvironment, Action action, TargetResource target) {
        return authorizer.authorize(caller, callEnvironment, action, target);
    }

    @Override
    public AuthorizationResult authorizeJmxOperation(Caller caller, Environment callEnvironment, JmxTarget target) {
        Set<String> roles = roleMapper.mapRoles(caller, null, FAKE_JMX_ACTION, (TargetAttribute) null);
        if (target.isNonFacadeMBeansSensitive()) {
            return authorize(roles, StandardRole.SUPERUSER, StandardRole.ADMINISTRATOR);
        } else {
            if (target.isReadOnly()) {
                //Everybody can read mbeans when not sensitive
                return AuthorizationResult.PERMITTED;
                //authorize(exception, roles, StandardRole.SUPERUSER, StandardRole.ADMINISTRATOR, StandardRole.OPERATOR, StandardRole.MAINTAINER, StandardRole.AUDITOR, StandardRole.MONITOR, StandardRole.DEPLOYER);
            } else {
                return authorize(roles, StandardRole.SUPERUSER, StandardRole.ADMINISTRATOR, StandardRole.OPERATOR, StandardRole.MAINTAINER);
            }
        }
    }

    private AuthorizationResult authorize(Set<String> callerRoles, StandardRole...roles) {
        for (StandardRole role : roles) {
            if (callerRoles.contains(role.toString())) {
                return AuthorizationResult.PERMITTED;
            }
        }
        return new AuthorizationResult(Decision.DENY);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3257.java