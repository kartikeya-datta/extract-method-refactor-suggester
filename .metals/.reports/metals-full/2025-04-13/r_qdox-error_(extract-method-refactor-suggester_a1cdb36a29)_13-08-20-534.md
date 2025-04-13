error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/639.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/639.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/639.java
text:
```scala
n@@ot owned by Derby code, and thus must not be modified by Derby

/*

   Derby - Class org.apache.derby.iapi.services.monitor.ModuleControl

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derby.iapi.services.monitor;

import java.util.Properties;
import org.apache.derby.iapi.error.StandardException;

/**
	ModuleControl is <B>optionally</B> implemented by a module's factory class.
*/

public interface ModuleControl {

	/**
		Boot this module with the given properties. Creates a module instance
		that can be found using the findModule() methods of Monitor.
		The module can only be found using one of these findModule() methods
		once this method has returned.
		<P>
		An implementation's boot method can throw StandardException. If it
		is thrown the module is not registered by the monitor and therefore cannot
		be found through a findModule(). In this case the module's stop() method
		is not called, thus throwing this exception must free up any
		resources.
		<P>
		When create is true the contents of the properties object
		will be written to the service.properties of the persistent
		service. Thus any code that requires an entry in service.properties
		must <B>explicitly</B> place the value in this properties set
		using the put method.
		<BR>
		Typically the properties object contains one or more default
		properties sets, which are not written out to service.properties.
		These default sets are how callers modify the create process. In a
		JDBC connection database create the first set of defaults is a properties
		object that contains the attributes that were set on the jdbc:derby: URL.
		This attributes properties set has the second default properties set as
		its default. This set (which could be null) contains the properties
		that the user set on their DriverManager.getConnection() call, and are thus
		not owned by cloudscape code, and thus must not be modified by cloudscape
		code.
		<P>
		When create is false the properties object contains all the properties
		set in the service.properties file plus a <B>limited</B> number of
		attributes from the JDBC URL attributes or connection properties set.
		This avoids properties set by the user compromising the boot process.
		An example of a property passed in from the JDBC world is the bootPassword
		for encrypted databases.

		<P>
		Code should not hold onto the passed in properties reference after boot time
		as its contents may change underneath it. At least after the complete boot
		is completed, the links to all the default sets will be removed.

		@exception StandardException Module cannot be started.

		@see Monitor
		@see ModuleFactory
		
	*/

	public void boot(boolean create, Properties properties)
		throws StandardException;

	/**
		Stop the module.

		The module may be found via a findModule() method until some time after
		this method returns. Therefore the factory must be prepared to reject requests
		to it once it has been stopped. In addition other modules may cache a reference
		to the module and make requests of it after it has been stopped, these requests
		should be rejected as well.

		@see Monitor
		@see ModuleFactory
	*/

	public void stop();


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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/639.java