error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13483.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13483.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13483.java
text:
```scala
r@@eturn CommonDescriptions.getSystemPropertyDescription(locale, bundle.getString("server-group.system-property"), true);

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

package org.jboss.as.domain.controller.descriptions;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CHILDREN;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HEAD_COMMENT_ALLOWED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.JVM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MAX_OCCURS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MIN_LENGTH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MIN_OCCURS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MODEL_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATIONS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PROFILE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REPLY_PROPERTIES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REQUEST_PROPERTIES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REQUIRED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOCKET_BINDING_GROUP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SOCKET_BINDING_PORT_OFFSET;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SYSTEM_PROPERTY;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.TAIL_COMMENT_ALLOWED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.TYPE;

import java.util.Locale;
import java.util.ResourceBundle;

import org.jboss.as.controller.descriptions.common.CommonDescriptions;
import org.jboss.as.server.deployment.DeploymentRemoveHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * @author Emanuel Muckenhuber
 */
public class ServerGroupDescription {

    private static final String RESOURCE_NAME = ServerGroupDescription.class.getPackage().getName() + ".LocalDescriptions";

    public static ModelNode getServerGroupDescription(final Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode root = new ModelNode();
        root.get(DESCRIPTION).set(bundle.getString("server-group"));
        root.get(HEAD_COMMENT_ALLOWED).set(true);
        root.get(TAIL_COMMENT_ALLOWED).set(true);

        root.get(ATTRIBUTES, PROFILE, DESCRIPTION).set(bundle.getString("server-group.profile"));
        root.get(ATTRIBUTES, PROFILE, TYPE).set(ModelType.STRING);
        root.get(ATTRIBUTES, PROFILE, REQUIRED).set(true);
        root.get(ATTRIBUTES, PROFILE, MIN_LENGTH).set(1);

        root.get(ATTRIBUTES, SOCKET_BINDING_GROUP, DESCRIPTION).set(bundle.getString("server-group.socket-binding-group"));
        root.get(ATTRIBUTES, SOCKET_BINDING_GROUP, TYPE).set(ModelType.STRING);
        root.get(ATTRIBUTES, SOCKET_BINDING_GROUP, REQUIRED).set(true);

        root.get(ATTRIBUTES, SOCKET_BINDING_PORT_OFFSET, DESCRIPTION).set(bundle.getString("server-group.socket-binding-port-offset"));
        root.get(ATTRIBUTES, SOCKET_BINDING_PORT_OFFSET, TYPE).set(ModelType.INT);
        root.get(ATTRIBUTES, SOCKET_BINDING_PORT_OFFSET, REQUIRED).set(false);

        root.get(OPERATIONS).setEmptyObject();

        root.get(CHILDREN, DEPLOYMENT, DESCRIPTION).set(bundle.getString("server-group.deployment"));
        root.get(CHILDREN, DEPLOYMENT, MIN_OCCURS).set(0);
        root.get(CHILDREN, DEPLOYMENT, MODEL_DESCRIPTION);

        root.get(CHILDREN, JVM, DESCRIPTION).set(bundle.getString("server-group.jvm"));
        root.get(CHILDREN, JVM, MIN_OCCURS).set(0);
        root.get(CHILDREN, JVM, MAX_OCCURS).set(Integer.MAX_VALUE);
        root.get(CHILDREN, JVM, MODEL_DESCRIPTION).setEmptyObject();

        root.get(CHILDREN, SYSTEM_PROPERTY, DESCRIPTION).set(bundle.getString("server-group.system-property"));
        root.get(CHILDREN, SYSTEM_PROPERTY, MIN_OCCURS).set(0);
        root.get(CHILDREN, SYSTEM_PROPERTY, MAX_OCCURS).set(Integer.MAX_VALUE);
        root.get(CHILDREN, SYSTEM_PROPERTY, MODEL_DESCRIPTION).setEmptyObject();

        return root;
    }

    public static ModelNode getServerGroupAdd(final Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode root = new ModelNode();

        root.get(OPERATION_NAME).set(ADD);
        root.get(DESCRIPTION).set(bundle.getString("server-group.add"));

        root.get(REQUEST_PROPERTIES, PROFILE, DESCRIPTION).set(bundle.getString("server-group.profile"));
        root.get(REQUEST_PROPERTIES, PROFILE, TYPE).set(ModelType.STRING);
        root.get(REQUEST_PROPERTIES, PROFILE, REQUIRED).set(true);
        root.get(REQUEST_PROPERTIES, PROFILE, MIN_LENGTH).set(1);

        root.get(REQUEST_PROPERTIES, SOCKET_BINDING_GROUP, DESCRIPTION).set(bundle.getString("server-group.socket-binding-group"));
        root.get(REQUEST_PROPERTIES, SOCKET_BINDING_GROUP, TYPE).set(ModelType.STRING);
        root.get(REQUEST_PROPERTIES, SOCKET_BINDING_GROUP, REQUIRED).set(true);

        root.get(REQUEST_PROPERTIES, SOCKET_BINDING_PORT_OFFSET, DESCRIPTION).set(bundle.getString("server-group.socket-binding-port-offset"));
        root.get(REQUEST_PROPERTIES, SOCKET_BINDING_PORT_OFFSET, TYPE).set(ModelType.INT);
        root.get(REQUEST_PROPERTIES, SOCKET_BINDING_PORT_OFFSET, REQUIRED).set(false);

        root.get(REQUEST_PROPERTIES, JVM, DESCRIPTION).set(bundle.getString("server-group.add.jvm"));
        root.get(REQUEST_PROPERTIES, JVM, TYPE).set(ModelType.STRING);
        root.get(REQUEST_PROPERTIES, JVM, REQUIRED).set(false);

        return root;
    }

    public static ModelNode getServerGroupRemove(final Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode root = new ModelNode();

        root.get(OPERATION_NAME).set(REMOVE);
        root.get(DESCRIPTION).set(bundle.getString("server-group.remove"));

        return root;
    }

    public static ModelNode getDeploymentRemoveOperation(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode root = new ModelNode();
        root.get(OPERATION_NAME).set(DeploymentRemoveHandler.OPERATION_NAME);
        root.get(DESCRIPTION).set(bundle.getString("server-group.deployment.remove"));
        root.get(REPLY_PROPERTIES).setEmptyObject();
        return root;
    }

    public static ModelNode getSystemPropertiesDescription(Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        return CommonDescriptions.getSystemPropertyDescription(locale, bundle.getString("server-group.system-property"), false);
    }


    private static ResourceBundle getResourceBundle(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return ResourceBundle.getBundle(RESOURCE_NAME, locale);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13483.java