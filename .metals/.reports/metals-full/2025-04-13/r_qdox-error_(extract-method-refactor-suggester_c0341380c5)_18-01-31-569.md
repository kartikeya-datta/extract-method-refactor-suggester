error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6417.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6417.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6417.java
text:
```scala
c@@aller = Caller.createCaller(null);

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

package org.jboss.as.controller.access.permission;

import static org.junit.Assert.assertEquals;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.EnumSet;

import org.jboss.as.controller.ControlledProcessState;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.access.Action;
import org.jboss.as.controller.access.AuthorizationResult;
import org.jboss.as.controller.access.Caller;
import org.jboss.as.controller.access.Environment;
import org.jboss.as.controller.access.TargetAttribute;
import org.jboss.as.controller.access.TargetResource;
import org.jboss.as.controller.access.constraint.ScopingConstraint;
import org.jboss.dmr.ModelNode;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ladislav Thon <lthon@redhat.com>
 */
public class ManagementPermissionAuthorizerTestCase {
    private Caller caller;
    private Environment environment;
    private ManagementPermissionAuthorizer authorizer;

    @Before
    public void setUp() {
        //caller = new Caller();
        ControlledProcessState processState = new ControlledProcessState(false);
        processState.setRunning();
        environment = new Environment(processState, ProcessType.EMBEDDED_SERVER);
        authorizer = new ManagementPermissionAuthorizer(new TestPermissionFactory());
    }

    @Test
    public void testAuthorizerResourcePermit() {
        Action action = new Action(null, null, EnumSet.of(Action.ActionEffect.ADDRESS,
                Action.ActionEffect.READ_CONFIG));
        TargetResource targetResource = TargetResource.forStandalone(null, null);
        AuthorizationResult result = authorizer.authorize(caller, environment, action, targetResource);

        assertEquals(AuthorizationResult.Decision.PERMIT, result.getDecision());
    }

    @Test
    public void testAuthorizerResourceDeny() {
        Action action = new Action(null, null, EnumSet.of(Action.ActionEffect.ADDRESS,
                Action.ActionEffect.READ_CONFIG, Action.ActionEffect.WRITE_CONFIG));
        TargetResource targetResource = TargetResource.forStandalone(null, null);
        AuthorizationResult result = authorizer.authorize(caller, environment, action, targetResource);

        assertEquals(AuthorizationResult.Decision.DENY, result.getDecision());
    }

    @Test
    public void testAuthorizerAttributePermit() {
        Action action = new Action(null, null, EnumSet.of(Action.ActionEffect.ADDRESS,
                Action.ActionEffect.READ_CONFIG));
        TargetResource targetResource = TargetResource.forStandalone(null, null);
        TargetAttribute targetAttribute = new TargetAttribute(null, new ModelNode(), targetResource);
        AuthorizationResult result = authorizer.authorize(caller, environment, action, targetAttribute);

        assertEquals(AuthorizationResult.Decision.PERMIT, result.getDecision());
    }

    @Test
    public void testAuthorizerAttributeDeny() {
        Action action = new Action(null, null, EnumSet.of(Action.ActionEffect.ADDRESS,
                Action.ActionEffect.READ_CONFIG, Action.ActionEffect.WRITE_CONFIG));
        TargetResource targetResource = TargetResource.forStandalone(null, null);
        TargetAttribute targetAttribute = new TargetAttribute(null, new ModelNode(), targetResource);
        AuthorizationResult result = authorizer.authorize(caller, environment, action, targetAttribute);

        assertEquals(AuthorizationResult.Decision.DENY, result.getDecision());
    }

    // ---

    private static final class TestPermissionFactory implements PermissionFactory {
        private PermissionCollection getUserPermissions() {
            ManagementPermissionCollection mpc = new ManagementPermissionCollection(TestManagementPermission.class);
            mpc.add(new TestManagementPermission(Action.ActionEffect.ADDRESS));
            mpc.add(new TestManagementPermission(Action.ActionEffect.READ_CONFIG));
            mpc.add(new TestManagementPermission(Action.ActionEffect.READ_RUNTIME));
            return mpc;
        }

        private PermissionCollection getRequiredPermissions(Action action) {
            ManagementPermissionCollection mpc = new ManagementPermissionCollection(TestManagementPermission.class);
            for (Action.ActionEffect actionEffect : action.getActionEffects()) {
                mpc.add(new TestManagementPermission(actionEffect));
            }
            return mpc;
        }

        @Override
        public PermissionCollection getUserPermissions(Caller caller, Environment callEnvironment, Action action, TargetAttribute target) {
            return getUserPermissions();
        }

        @Override
        public PermissionCollection getUserPermissions(Caller caller, Environment callEnvironment, Action action, TargetResource target) {
            return getUserPermissions();
        }

        @Override
        public PermissionCollection getRequiredPermissions(Action action, TargetAttribute target) {
            return getRequiredPermissions(action);
        }

        @Override
        public PermissionCollection getRequiredPermissions(Action action, TargetResource target) {
            return getRequiredPermissions(action);
        }
    }

    private static final class TestManagementPermission extends ManagementPermission {
        private TestManagementPermission(Action.ActionEffect actionEffect) {
            super("test", actionEffect);
        }

        @Override
        public ManagementPermission createScopedPermission(ScopingConstraint constraint) {
            return null;
        }

        @Override
        public boolean implies(Permission permission) {
            return equals(permission);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6417.java