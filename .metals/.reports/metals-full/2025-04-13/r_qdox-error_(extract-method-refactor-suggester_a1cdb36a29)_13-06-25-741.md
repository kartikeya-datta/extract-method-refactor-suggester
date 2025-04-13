error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12638.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12638.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12638.java
text:
```scala
r@@oot.get(ATTRIBUTES, LAUNCH_TYPE, ALLOWED).add(ServerEnvironment.LaunchType.STANDALONE.toString());

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
 */package org.jboss.as.server.controller.descriptions;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.*;
import static org.jboss.as.server.controller.descriptions.ServerDescriptionConstants.LAUNCH_TYPE;
import static org.jboss.as.server.controller.descriptions.ServerDescriptionConstants.PROCESS_TYPE;
import static org.jboss.as.server.controller.descriptions.ServerDescriptionConstants.PROFILE_NAME;
import static org.jboss.as.server.controller.descriptions.ServerDescriptionConstants.SERVER_STATE;

import java.util.Locale;
import java.util.ResourceBundle;

import org.jboss.as.controller.descriptions.common.CommonDescriptions;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * Model description for the server root.
 *
 * @author Brian Stansberry
 */
public class ServerRootDescription {

    private static final String RESOURCE_NAME = ServerRootDescription.class.getPackage().getName() + ".LocalDescriptions";

    public static ModelNode getDescription(final Locale locale) {

        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode root = new ModelNode();
        root.get(DESCRIPTION).set(bundle.getString("server"));
        root.get(HEAD_COMMENT_ALLOWED).set(true);
        root.get(TAIL_COMMENT_ALLOWED).set(true);

        root.get(ATTRIBUTES, NAMESPACES).set(CommonDescriptions.getNamespacePrefixAttribute(locale));
        root.get(ATTRIBUTES, SCHEMA_LOCATIONS).set(CommonDescriptions.getSchemaLocationAttribute(locale));

        root.get(ATTRIBUTES, NAME, DESCRIPTION).set(bundle.getString("server.name"));
        root.get(ATTRIBUTES, NAME, TYPE).set(ModelType.STRING);
        root.get(ATTRIBUTES, NAME, REQUIRED).set(false);
        root.get(ATTRIBUTES, NAME, NILLABLE).set(true);
        root.get(ATTRIBUTES, NAME, MIN_LENGTH).set(1);

        root.get(ATTRIBUTES, PROFILE_NAME, DESCRIPTION).set(bundle.getString("server.profile"));
        root.get(ATTRIBUTES, PROFILE_NAME, TYPE).set(ModelType.STRING);
        root.get(ATTRIBUTES, PROFILE_NAME, REQUIRED).set(true);
        root.get(ATTRIBUTES, PROFILE_NAME, MIN_LENGTH).set(1);
        // A bit dodgy; we'll write the comment before the <profile> tag
        root.get(ATTRIBUTES, PROFILE_NAME, HEAD_COMMENT_ALLOWED).set(true);
        root.get(ATTRIBUTES, PROFILE_NAME, TAIL_COMMENT_ALLOWED).set(true);

        root.get(ATTRIBUTES, SERVER_STATE, DESCRIPTION).set(bundle.getString("server.state"));
        root.get(ATTRIBUTES, SERVER_STATE, TYPE).set(ModelType.STRING);
        root.get(ATTRIBUTES, SERVER_STATE, REQUIRED).set(true);
        root.get(ATTRIBUTES, SERVER_STATE, NILLABLE).set(false);
        root.get(ATTRIBUTES, SERVER_STATE, MIN_LENGTH).set(1);

        root.get(ATTRIBUTES, PROCESS_TYPE, DESCRIPTION).set(bundle.getString("server.process-type"));
        root.get(ATTRIBUTES, PROCESS_TYPE, TYPE).set(ModelType.STRING);
        root.get(ATTRIBUTES, PROCESS_TYPE, REQUIRED).set(true);
        root.get(ATTRIBUTES, PROCESS_TYPE, NILLABLE).set(false);
        root.get(ATTRIBUTES, PROCESS_TYPE, MIN_LENGTH).set(1);

        root.get(ATTRIBUTES, LAUNCH_TYPE, DESCRIPTION).set(bundle.getString("server.launch-type"));
        root.get(ATTRIBUTES, LAUNCH_TYPE, TYPE).set(ModelType.STRING);
        root.get(ATTRIBUTES, LAUNCH_TYPE, REQUIRED).set(true);
        root.get(ATTRIBUTES, LAUNCH_TYPE, NILLABLE).set(false);
        root.get(ATTRIBUTES, LAUNCH_TYPE, MIN_LENGTH).set(1);
        root.get(ATTRIBUTES, LAUNCH_TYPE, ALLOWED).add(ServerEnvironment.LaunchType.DOMAIN.toString());
        root.get(ATTRIBUTES, LAUNCH_TYPE, ALLOWED).add(ServerEnvironment.LaunchType.STANADALONE.toString());
        root.get(ATTRIBUTES, LAUNCH_TYPE, ALLOWED).add(ServerEnvironment.LaunchType.EMBEDDED.toString());

        root.get(OPERATIONS);

        root.get(CHILDREN, EXTENSION, DESCRIPTION).set(bundle.getString("server.extension"));
        root.get(CHILDREN, EXTENSION, MIN_OCCURS).set(0);
        root.get(CHILDREN, EXTENSION, MODEL_DESCRIPTION);

        root.get(CHILDREN, PATH, DESCRIPTION).set(bundle.getString("server.path"));
        root.get(CHILDREN, PATH, MIN_OCCURS).set(0);
        root.get(CHILDREN, PATH, MODEL_DESCRIPTION);

        root.get(CHILDREN, SYSTEM_PROPERTY, DESCRIPTION).set(bundle.getString("server.system-properties"));
        root.get(CHILDREN, SYSTEM_PROPERTY, MIN_OCCURS).set(0);
        root.get(CHILDREN, SYSTEM_PROPERTY, MODEL_DESCRIPTION);

        root.get(CHILDREN, CORE_SERVICE, DESCRIPTION).set(bundle.getString("server.core-services"));
        root.get(CHILDREN, CORE_SERVICE, MIN_OCCURS).set(0);
        root.get(CHILDREN, CORE_SERVICE, MODEL_DESCRIPTION);

        root.get(CHILDREN, INTERFACE, DESCRIPTION).set(bundle.getString("server.interface"));
        root.get(CHILDREN, INTERFACE, MIN_OCCURS).set(0);
        root.get(CHILDREN, INTERFACE, MODEL_DESCRIPTION);

        root.get(CHILDREN, SOCKET_BINDING_GROUP, DESCRIPTION).set(bundle.getString("server.socket-binding"));
        root.get(CHILDREN, SOCKET_BINDING_GROUP, MIN_OCCURS).set(0);
        root.get(CHILDREN, SOCKET_BINDING_GROUP, MODEL_DESCRIPTION);

        root.get(CHILDREN, DEPLOYMENT, DESCRIPTION).set(bundle.getString("server.deployment"));
        root.get(CHILDREN, DEPLOYMENT, MIN_OCCURS).set(0);
        root.get(CHILDREN, DEPLOYMENT, MODEL_DESCRIPTION);

        return root;
    }

    public static ModelNode getCompositeOperationDescription(Locale locale) {

        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode root = new ModelNode();
        root.get(OPERATION_NAME).set(COMPOSITE);
        root.get(DESCRIPTION).set(bundle.getString("composite"));
        root.get(REQUEST_PROPERTIES, STEPS, TYPE).set(ModelType.LIST); // TODO details of the type
        root.get(REQUEST_PROPERTIES, STEPS, DESCRIPTION).set(bundle.getString("composite.steps"));
        root.get(REQUEST_PROPERTIES, STEPS, REQUIRED).set(true);
        root.get(REPLY_PROPERTIES, TYPE).set(ModelType.LIST);
        // TODO details of the reply
        root.get(REPLY_PROPERTIES, DESCRIPTION).set(bundle.getString("composite.result"));
        return root;
    }

    /** {@inheritDoc} */
    public static ModelNode getShutdownOperationDescription(final Locale locale) {
        ResourceBundle bundle = getResourceBundle(locale);

        ModelNode node = new ModelNode();
        node.get(OPERATION_NAME).set(SHUTDOWN);
        node.get(DESCRIPTION).set(bundle.getString("shutdown"));
        node.get(REQUEST_PROPERTIES).setEmptyObject();
        node.get(REPLY_PROPERTIES).setEmptyObject();
        return node;
    }

    public static ModelNode getDumpServicesOperationDescription(final Locale locale) {
        ResourceBundle bundle = getResourceBundle(locale);

        ModelNode node = new ModelNode();
        node.get(OPERATION_NAME).set(DUMP_SERVICES);
        node.get(DESCRIPTION).set(bundle.getString("dump-services"));
        node.get(REQUEST_PROPERTIES).setEmptyObject();
        node.get(REPLY_PROPERTIES, TYPE).set(ModelType.STRING);
        return node;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12638.java