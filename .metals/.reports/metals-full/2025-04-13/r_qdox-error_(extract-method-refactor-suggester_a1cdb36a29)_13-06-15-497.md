error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2633.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2633.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2633.java
text:
```scala
s@@ubsystem.get(CHILDREN, ModelConstants.PROPERTY, ModelDescriptionConstants.DESCRIPTION).set(resbundle.getString("framework.property"));

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
package org.jboss.as.osgi.parser;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ATTRIBUTES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CHILDREN;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HEAD_COMMENT_ALLOWED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAMESPACE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.TAIL_COMMENT_ALLOWED;
import static org.jboss.as.osgi.parser.SubsystemState.DEFAULT_ACTIVATION;

import java.util.Locale;
import java.util.ResourceBundle;

import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.AttributeAccess.AccessType;
import org.jboss.as.controller.registry.AttributeAccess.Flag;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 * @author David Bosschaert
 * @author Thomas.Diesler@jboss.com
 */
class OSGiSubsystemProviders {
    static final String RESOURCE_NAME = OSGiSubsystemProviders.class.getPackage().getName() + ".LocalDescriptions";

    static final DescriptionProvider SUBSYSTEM = new DescriptionProvider() {
        public ModelNode getModelDescription(final Locale locale) {

            ModelNode subsystem = new ModelNode();
            ResourceBundle resbundle = getResourceBundle(locale);
            subsystem.get(DESCRIPTION).set(resbundle.getString("subsystem"));
            subsystem.get(HEAD_COMMENT_ALLOWED).set(true);
            subsystem.get(TAIL_COMMENT_ALLOWED).set(true);
            subsystem.get(NAMESPACE).set(Namespace.OSGI_1_0.getUriString());

            subsystem.get(ATTRIBUTES, ModelConstants.ACTIVATION, ModelDescriptionConstants.DESCRIPTION).set(resbundle.getString("subsystem.activation"));
            subsystem.get(ATTRIBUTES, ModelConstants.ACTIVATION, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
            subsystem.get(ATTRIBUTES, ModelConstants.ACTIVATION, ModelDescriptionConstants.DEFAULT).set(DEFAULT_ACTIVATION.toString());
            subsystem.get(ATTRIBUTES, ModelConstants.ACTIVATION, ModelDescriptionConstants.ACCESS_TYPE).set(AccessType.READ_WRITE.toString());
            subsystem.get(ATTRIBUTES, ModelConstants.ACTIVATION, ModelDescriptionConstants.RESTART_REQUIRED).set(Flag.RESTART_JVM.toString());

            subsystem.get(ATTRIBUTES, ModelConstants.STARTLEVEL, ModelDescriptionConstants.DESCRIPTION).set(resbundle.getString("subsystem.startlevel"));
            subsystem.get(ATTRIBUTES, ModelConstants.STARTLEVEL, ModelDescriptionConstants.TYPE).set(ModelType.INT);
            subsystem.get(ATTRIBUTES, ModelConstants.STARTLEVEL, ModelDescriptionConstants.ACCESS_TYPE).set(AccessType.READ_WRITE.toString());
            subsystem.get(ATTRIBUTES, ModelConstants.STARTLEVEL, ModelDescriptionConstants.RESTART_REQUIRED).set(Flag.RESTART_NONE.toString());
            subsystem.get(ATTRIBUTES, ModelConstants.STARTLEVEL, ModelDescriptionConstants.STORAGE).set(Flag.STORAGE_RUNTIME.toString());

            subsystem.get(CHILDREN, ModelConstants.CONFIGURATION, ModelDescriptionConstants.DESCRIPTION).set(resbundle.getString("configuration"));
            subsystem.get(CHILDREN, ModelConstants.FRAMEWORK_PROPERTY, ModelDescriptionConstants.DESCRIPTION).set(resbundle.getString("framework.property"));
            subsystem.get(CHILDREN, ModelConstants.CAPABILITY, ModelDescriptionConstants.DESCRIPTION).set(resbundle.getString("capability"));
            subsystem.get(CHILDREN, ModelConstants.BUNDLE, ModelDescriptionConstants.DESCRIPTION).set(resbundle.getString("bundle"));

            return subsystem;
        }
    };

    static final DescriptionProvider CONFIGURATION_DESCRIPTION = new DescriptionProvider() {
        public ModelNode getModelDescription(Locale locale) {
            final ModelNode node = new ModelNode();
            ResourceBundle resbundle = getResourceBundle(locale);
            node.get(DESCRIPTION).set(resbundle.getString("configuration"));
            node.get(ATTRIBUTES, ModelConstants.ENTRIES, ModelDescriptionConstants.DESCRIPTION).set(resbundle.getString("configuration.entries"));
            node.get(ATTRIBUTES, ModelConstants.ENTRIES, ModelDescriptionConstants.REQUIRED).set(true);
            node.get(ATTRIBUTES, ModelConstants.ENTRIES, ModelDescriptionConstants.TYPE).set(ModelType.LIST);
            node.get(ATTRIBUTES, ModelConstants.ENTRIES, ModelDescriptionConstants.VALUE_TYPE).set(ModelType.PROPERTY);
            node.get(ATTRIBUTES, ModelConstants.ENTRIES, ModelDescriptionConstants.ACCESS_TYPE).set(AccessType.READ_WRITE.toString());
            node.get(ATTRIBUTES, ModelConstants.ENTRIES, ModelDescriptionConstants.RESTART_REQUIRED).set(Flag.RESTART_ALL_SERVICES.toString());
            return node;
        }
    };

    static final DescriptionProvider FRAMEWORK_PROPERTY_DESCRIPTION = new DescriptionProvider() {
        public ModelNode getModelDescription(Locale locale) {
            final ModelNode node = new ModelNode();
            ResourceBundle resbundle = getResourceBundle(locale);
            node.get(DESCRIPTION).set(resbundle.getString("framework.property"));
            node.get(ATTRIBUTES, ModelConstants.VALUE, ModelDescriptionConstants.DESCRIPTION).set(resbundle.getString("framework.property.value"));
            node.get(ATTRIBUTES, ModelConstants.VALUE, ModelDescriptionConstants.TYPE).set(ModelType.STRING);
            node.get(ATTRIBUTES, ModelConstants.VALUE, ModelDescriptionConstants.REQUIRED).set(true);
            node.get(ATTRIBUTES, ModelConstants.VALUE, ModelDescriptionConstants.ACCESS_TYPE).set(AccessType.READ_WRITE.toString());
            node.get(ATTRIBUTES, ModelConstants.VALUE, ModelDescriptionConstants.RESTART_REQUIRED).set(Flag.RESTART_ALL_SERVICES.toString());
            return node;
        }
    };


    static final DescriptionProvider CAPABILITY_DESCRIPTION = new DescriptionProvider() {
        public ModelNode getModelDescription(Locale locale) {
            final ModelNode node = new ModelNode();
            ResourceBundle resbundle = getResourceBundle(locale);
            node.get(DESCRIPTION).set(resbundle.getString("capability"));
            node.get(ATTRIBUTES, ModelConstants.STARTLEVEL, ModelDescriptionConstants.DESCRIPTION).set(resbundle.getString("capability.startlevel"));
            node.get(ATTRIBUTES, ModelConstants.STARTLEVEL, ModelDescriptionConstants.TYPE).set(ModelType.INT);
            node.get(ATTRIBUTES, ModelConstants.STARTLEVEL, ModelDescriptionConstants.REQUIRED).set(false);
            node.get(ATTRIBUTES, ModelConstants.STARTLEVEL, ModelDescriptionConstants.ACCESS_TYPE).set(AccessType.READ_WRITE.toString());
            node.get(ATTRIBUTES, ModelConstants.STARTLEVEL, ModelDescriptionConstants.RESTART_REQUIRED).set(Flag.RESTART_ALL_SERVICES.toString());
            return node;
        }
    };

    static final DescriptionProvider BUNDLE_DESCRIPTION = new DescriptionProvider() {
        public ModelNode getModelDescription(Locale locale) {
            final ModelNode node = new ModelNode();
            ResourceBundle resbundle = getResourceBundle(locale);
            node.get(DESCRIPTION).set(resbundle.getString("bundle"));

            String storageRuntime = AttributeAccess.Storage.RUNTIME.toString();

            ModelNode idNode = new ModelNode();
            idNode.get(ModelDescriptionConstants.TYPE).set(ModelType.LONG);
            idNode.get(ModelDescriptionConstants.DESCRIPTION).set(resbundle.getString("bundle.id"));
            idNode.get(ModelDescriptionConstants.ACCESS_TYPE).set(ModelDescriptionConstants.READ_ONLY);
            idNode.get(ModelDescriptionConstants.STORAGE).set(storageRuntime);

            ModelNode startLevelNode = new ModelNode();
            startLevelNode.get(ModelDescriptionConstants.TYPE).set(ModelType.INT);
            startLevelNode.get(ModelDescriptionConstants.DESCRIPTION).set(resbundle.getString("bundle.startlevel"));
            startLevelNode.get(ModelDescriptionConstants.ACCESS_TYPE).set(ModelDescriptionConstants.READ_ONLY);
            startLevelNode.get(ModelDescriptionConstants.REQUIRED).set(false);
            startLevelNode.get(ModelDescriptionConstants.STORAGE).set(storageRuntime);

            ModelNode symbolicNameNode = new ModelNode();
            symbolicNameNode.get(ModelDescriptionConstants.TYPE).set(ModelType.STRING);
            symbolicNameNode.get(ModelDescriptionConstants.DESCRIPTION).set(resbundle.getString("bundle.symbolic-name"));
            symbolicNameNode.get(ModelDescriptionConstants.ACCESS_TYPE).set(ModelDescriptionConstants.READ_ONLY);
            symbolicNameNode.get(ModelDescriptionConstants.STORAGE).set(storageRuntime);

            ModelNode versionNode = new ModelNode();
            versionNode.get(ModelDescriptionConstants.TYPE).set(ModelType.STRING);
            versionNode.get(ModelDescriptionConstants.DESCRIPTION).set(resbundle.getString("bundle.version"));
            versionNode.get(ModelDescriptionConstants.ACCESS_TYPE).set(ModelDescriptionConstants.READ_ONLY);
            versionNode.get(ModelDescriptionConstants.STORAGE).set(storageRuntime);

            node.get(ModelDescriptionConstants.ATTRIBUTES).get(ModelConstants.ID).set(idNode);
            node.get(ModelDescriptionConstants.ATTRIBUTES).get(ModelConstants.STARTLEVEL).set(startLevelNode);
            node.get(ModelDescriptionConstants.ATTRIBUTES).get(ModelConstants.SYMBOLIC_NAME).set(symbolicNameNode);
            node.get(ModelDescriptionConstants.ATTRIBUTES).get(ModelConstants.VERSION).set(versionNode);

            return node;
        }
    };

    static ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(RESOURCE_NAME, locale != null ? locale : Locale.getDefault());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2633.java