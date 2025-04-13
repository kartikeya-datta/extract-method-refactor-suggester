error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13422.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13422.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13422.java
text:
```scala
public static final S@@tring READ_RESOURCE_OPERATION = "read-resource";

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
package org.jboss.as.controller.descriptions;

/**
 * String constants frequently used in model descriptions.
 *
 * @author Brian Stansberry
 */
public class ModelDescriptionConstants {

    // KEEP THESE IN ALPHABETICAL ORDER!

    public static final String ADD = "add";
    public static final String ADD_OPERATION = "add-operation";
    public static final String ADDRESS = "address";
    public static final String ATTRIBUTES = "attributes";
    public static final String CHILD_TYPE = "child-type";
    public static final String CHILDREN = "children";
    public static final String DEFAULT = "default";
    public static final String DEPLOYMENT = "deployment";
    public static final String DESCRIPTION = "description";
    public static final String EXTENSION = "extension";
    public static final String HEAD_COMMENT_ALLOWED = "head-comment-allowed";
    public static final String HOST = "host";
    public static final String INCLUDE = "include";
    public static final String INTERFACE = "interface";
    public static final String JVM = "jvm";
    public static final String LOCALE = "locale";
    public static final String MANAGEMENT = "management";
    public static final String MAX_LENGTH = "max-length";
    public static final String MAX_OCCURS = "max-occurs";
    public static final String MIN_LENGTH = "min-length";
    public static final String MIN_OCCURS = "min-occurs";
    public static final String MODEL_DESCRIPTION = "model-description";
    public static final String NAME = "name";
    public static final String NAMESPACE = "namespace";
    public static final String NAMESPACES = "namespaces";
    public static final String NILLABLE = "nillable";
    public static final String OP = "operation";
    public static final String OP_ADDR = "address";
    public static final String OPERATION = "operation-name";
    public static final String OPERATIONS = "operations";
    public static final String OPERATION_NAME = "operation-name";
    public static final String PATH = "path";
    public static final String PORT = "port";
    public static final String PROFILE = "profile";
    public static final String READ_ATTRIBUTE_OPERATION = "read-attribute";
    public static final String READ_CHILDREN_NAMES_OPERATION = "read-children_names";
    public static final String READ_OPERATION_DESCRIPTION_OPERATION = "read-operation-description";
    public static final String READ_OPERATION_NAMES_OPERATION = "read-operation-names";
    public static final String READ_RESOURCE_DESCRIPTION_OPERATION = "read-resource-description";
    public static final String READ_SUB_MODEL_OPERATION = "read-sub-model-operation";
    public static final String REMOVE = "remove";
    public static final String REMOVE_OPERATION = "remove-operation";
    public static final String REPLY_PROPERTIES = "reply-properties";
    public static final String REQUEST_PROPERTIES = "request-properties";
    public static final String RECURSIVE = "recursive";
    public static final String REQUIRED = "required";
    public static final String SCHEMA_LOCATIONS = "schema-locations";
    public static final String SERVER = "server";
    public static final String SERVER_GROUP = "server-group";
    public static final String SOCKET_BINDING_GROUP = "socket-binding-group";
    public static final String SUBSYSTEM = "subsystem";
    public static final String SYSTEM_PROPERTY = "system-property";
    public static final String TAIL_COMMENT_ALLOWED = "tail-comment-allowed";
    public static final String TYPE = "type";
    public static final String VALUE_TYPE = "value-type";

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13422.java