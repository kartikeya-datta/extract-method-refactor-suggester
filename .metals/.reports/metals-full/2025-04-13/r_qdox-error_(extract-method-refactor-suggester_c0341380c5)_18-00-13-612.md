error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1693.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1693.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1047
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1693.java
text:
```scala
interface Protocol {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

p@@ackage org.jboss.as.domain.client.impl;

/**
 * @author John Bailey
 */
public interface Protocol {

    byte[] SIGNATURE = {Byte.MAX_VALUE, Byte.MIN_VALUE, Byte.MAX_VALUE, Byte.MIN_VALUE};
    int VERSION_FIELD = 0x00; // The version field header
    int VERSION = 1; // The current protocol version

    int REQUEST_START = 0x01;
    int REQUEST_END = 0x02;
    int RESPONSE_START = 0x03;
    int RESPONSE_END = 0x04;

    int DOMAIN_CONTROLLER_REQUEST = 0x05;

    int REQUEST_OPERATION = 0x10;

    int PARAM_DOMAIN_MODEL = 0x13;
    int PARAM_DOMAIN_MODEL_UPDATE = 0x29;
    int GET_DOMAIN_REQUEST = 0x43;
    int GET_DOMAIN_RESPONSE = 0x44;
    int APPLY_UPDATES_REQUEST = 0x45;
    int PARAM_APPLY_UPDATES_RESULT_COUNT = 0x46;
    int PARAM_APPLY_UPDATE_RESULT = 0x47;
    int APPLY_UPDATE_RESULT_DOMAIN_MODEL_SUCCESS = 0x00;
    int PARAM_APPLY_UPDATE_RESULT_HOST_FAILURE_COUNT = 0x01;
    int PARAM_APPLY_UPDATE_RESULT_SERVER_FAILURE_COUNT = 0x01;
    int PARAM_APPLY_UPDATE_RESULT_SERVER_RESULT_COUNT = 0x02;
    int PARAM_HOST_NAME = 0x03;
    int PARAM_SERVER_GROUP_NAME = 0x04;
    int PARAM_SERVER_NAME = 0x05;
    int PARAM_APPLY_SERVER_MODEL_UPDATE_CANCELLED = 0x06;
    int PARAM_APPLY_SERVER_MODEL_UPDATE_TIMED_OUT = 0x07;
    int PARAM_APPLY_SERVER_MODEL_UPDATE_RESULT_RETURN = 0x48;
    int PARAM_APPLY_UPDATE_RESULT_EXCEPTION = 0x49;
    int APPLY_UPDATES_RESPONSE = 0x50;
    int EXECUTE_DEPLOYMENT_PLAN_REQUEST = 0x51;
    int PARAM_DEPLOYMENT_PLAN = 0x52;
    int PARAM_DEPLOYMENT_PLAN_RESULT = 0x53;
    int EXECUTE_DEPLOYMENT_PLAN_RESPONSE = 0x54;
    int ADD_DEPLOYMENT_CONTENT_REQUEST = 0x55;
    int PARAM_DEPLOYMENT_NAME = 0x56;
    int PARAM_DEPLOYMENT_RUNTIME_NAME = 0x57;
    int PARAM_DEPLOYMENT_CONTENT = 0x58;
    int PARAM_DEPLOYMENT_HASH_LENGTH = 0x59;
    int PARAM_DEPLOYMENT_HASH = 0x60;
    int ADD_DEPLOYMENT_CONTENT_RESPONSE = 0x61;
    int CHECK_UNIQUE_DEPLOYMENT_NAME_REQUEST = 0x62;
    int PARAM_DEPLOYMENT_NAME_UNIQUE = 0x63;
    int CHECK_UNIQUE_DEPLOYMENT_NAME_RESPONSE = 0x64;
    int APPLY_UPDATE_REQUEST = 0x65;
    int PARAM_APPLY_UPDATE_RESULT_SERVER_COUNT = 0x66;
    int APPLY_UPDATE_RESPONSE = 0x67;
    int APPLY_SERVER_MODEL_UPDATE_REQUEST = 0x68;
    int PARAM_SERVER_MODEL_UPDATE = 0x69;
    int APPLY_SERVER_MODEL_UPDATE_RESPONSE = 0x70;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1693.java