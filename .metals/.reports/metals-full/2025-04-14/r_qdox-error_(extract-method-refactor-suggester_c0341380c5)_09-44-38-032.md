error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/641.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/641.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,12]

error in qdox parser
file content:
```java
offset: 12
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/641.java
text:
```scala
@exception S@@tandardException	Derby error.

/*

   Derby - Class org.apache.derby.iapi.services.monitor.ModuleFactory

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

import org.apache.derby.iapi.services.info.ProductVersionHolder;
import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.services.stream.InfoStreams;
import org.apache.derby.iapi.services.monitor.PersistentService;
import org.apache.derby.iapi.services.loader.InstanceGetter;

import java.util.Properties;
import java.util.Locale;
import java.io.InputStream;
import java.io.IOException;
	  
/**
The monitor provides a central registry for all modules in the system,
and manages loading, starting, and finding them.
*/

public interface ModuleFactory
{

    /**
     * Find the module in the system with the given module protocol,
	 * protocolVersion and identifier.
	    
     * @return The module instance if found, or null.
     */
    public Object findModule(Object service, String protocol, String identifier);

	/**
		Return the name of the service that the passed in module lives in.
	*/
	public String getServiceName(Object serviceModule);

	/**
		Return the locale of the service that the passed in module lives in.
		Will return null if no-locale has been defined.
	*/
	public Locale getLocale(Object serviceModule);

	/**
		Translate a string of the form ll[_CC[_variant]] to a Locale.
		This is in the Monitor because we want this translation to be
		in only one place in the code.
	 */
	public Locale getLocaleFromString(String localeDescription)
					throws StandardException;


	/**
		Set the locale for the service *outside* of boot time.

		@param userDefinedLocale	A String in the form xx_YY, where xx is the
									language code and YY is the country code.

		@return		The new Locale for the service

		@exception StandardException	Thrown on error
	 */
	public Locale setLocale(Object serviceModule, String userDefinedLocale)
						throws StandardException;

	/**
		Set the locale for the service at boot time. The passed-in
		properties must be the one passed to the boot method.

		@exception StandardException	Cloudscape error.
	 */
	public Locale setLocale(Properties serviceProperties,
							String userDefinedLocale)
						throws StandardException;

	/**
		Return the PersistentService object for a service.
		Will return null if the service does not exist.
	*/
	public PersistentService getServiceType(Object serviceModule);

    /**
     * Return the PersistentService for a subsubprotocol.
     *
     * @return the PersistentService or null if it does not exist
     *
     * @exception StandardException
     */
    public PersistentService getServiceProvider(String subSubProtocol) throws StandardException;
    
    /**
     * Return the application set of properties which correspond
     * to the set of properties in the file derby.properties.
     */
	public Properties getApplicationProperties();

	/**
		Shut down the complete system that was started by this Monitor. Will
		cause the stop() method to be called on each loaded module.
	*/
	public void shutdown();

	/**
		Shut down a service that was started by this Monitor. Will
		cause the stop() method to be called on each loaded module.
		Requires that a context stack exist.
	*/
	public void shutdown(Object service);


	/**
		Obtain a class that supports the given identifier.

		@param identifier	identifer to associate with class

		@return a reference InstanceGetter

		@exception StandardException See Monitor.classFromIdentifier
	*/
	public InstanceGetter classFromIdentifier(int identifier)
		throws StandardException;

	/**
		Obtain an new instance of a class that supports the given identifier.

		@param identifier	identifer to associate with class

		@return a reference to a newly created object

		@exception StandardException See Monitor.newInstanceFromIdentifier
	
	*/
	public Object newInstanceFromIdentifier(int identifier)
		throws StandardException;

	/**
		Return the environment object that this system was booted in.
		This is a free form object that is set by the method the
		system is booted. For example when running in a Marimba system
		it is set to the maribma application context. In most environments
		it will be set to a java.io.File object representing the system home directory.
		Code that call this method usualy have predefined knowledge of the type of the returned object, e.g.
		Marimba store code knows that this will be set to a marimba application
		context.
	*/
	public Object getEnvironment();


	/**
		Return an array of the service identifiers that are running and
		implement the passed in protocol (java interface class name).
		This list is a snapshot of the current running systesm, once
		the call returns the service may have been shutdown or
		new ones added.

		@return The list of service names, if no services exist that
		implement the protocol an array with zero elements is returned.
	*/
	public String[] getServiceList(String protocol);

	/**
		Start a persistent service.
		<BR>
		<B>Do not call directly - use Monitor.startPersistentService()</B>
		
		<P> The poperty set passed in is for boot options for the modules
		required to start the service. It does not support defining different
		or new modules implementations.
		
		@param serviceName Name of the service to be started
		@param properties Property set made available to all modules booted
		for this service, through their ModuleControl.boot method.

		@return true if the service type is handled by the monitor, false if it isn't

		@exception StandardException An attempt to start the service failed.

		@see Monitor#startPersistentService
	*/
	public boolean startPersistentService(String serviceName, Properties properties)
		throws StandardException;

	/**
		Create a persistent service.
		<BR>
		<B>Do not call directly - use Monitor.startPersistentService()</B>

		@exception StandardException An attempt to create the service failed.

		@see Monitor#createPersistentService
	*/
	public Object createPersistentService(String factoryInterface, String serviceName, Properties properties)
		throws StandardException;
    public void removePersistentService(String name)
        throws StandardException;
   
	/**
		Start a non-persistent service.
		
		<BR>
		<B>Do not call directly - use Monitor.startNonPersistentService()</B>

		@exception StandardException An attempt to start the service failed.

		@see Monitor#startNonPersistentService
	*/
	public Object startNonPersistentService(String factoryInterface, String serviceName, Properties properties)
		throws StandardException;


	/**
		Find a service.

		<BR>
		<B>Do not call directly - use Monitor.findService()</B>

		@return a refrence to a module represeting the service or null if the service does not exist.

		@see Monitor#findService
	*/
	public Object findService(String protocol, String identifier);


	/**
		Start a module.
		
		<BR>
		<B>Do not call directly - use Monitor.startSystemModule() or Monitor.bootServiceModule()</B>

		@exception StandardException An attempt to start the module failed.

		@see Monitor#startSystemModule
		@see Monitor#bootServiceModule
	*/
	public Object startModule(boolean create, Object service, String protocol,
									 String identifier, Properties properties)
									 throws StandardException;


	/**	
		Get the defined default system streams object.
	*/
	public InfoStreams getSystemStreams();


	/**
		Start all services identified by derby.service.*
		in the property set. If bootAll is true the services
		that are persistent will be booted.
	*/
	public void startServices(Properties properties, boolean bootAll);

	/**
		Return a property from the JVM's system set.
		In a Java2 environment this will be executed as a privileged block
		if and only if the property starts with 'derby.'.
		If a SecurityException occurs, null is returned.
	*/
	public String getJVMProperty(String key);

	/**
		Get a newly created background thread.
		The thread is set to be a daemon but is not started.
	*/
	public Thread getDaemonThread(Runnable task, String name, boolean setMinPriority);

	/**
		Set the priority of the current thread.
		If the current thread was not returned by getDaemonThread() then no action is taken.
	*/
	public void setThreadPriority(int priority);

	public ProductVersionHolder getEngineVersion();

	/**
	 * Get the UUID factory for the system.  The UUID factory provides
	 * methods to create and recreate database unique identifiers.
	 */
	public org.apache.derby.iapi.services.uuid.UUIDFactory getUUIDFactory();
        
	/**
	 * Get the Timer factory for the system. The Timer factory provides
     * access to Timer objects for various purposes.
     *
     * @return the system's Timer factory.
	 */
    public org.apache.derby.iapi.services.timer.TimerFactory getTimerFactory();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/641.java