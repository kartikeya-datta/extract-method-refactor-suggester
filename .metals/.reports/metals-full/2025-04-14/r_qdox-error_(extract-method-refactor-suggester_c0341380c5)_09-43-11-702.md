error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5060.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5060.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5060.java
text:
```scala
t@@hrow new IDCreateException("id cannot be null"); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2004, 2007 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.provider.generic;

import java.util.*;
import org.eclipse.core.runtime.*;
import org.eclipse.ecf.core.*;
import org.eclipse.ecf.core.identity.*;
import org.eclipse.ecf.core.provider.IContainerInstantiator;
import org.eclipse.ecf.core.provider.IRemoteServiceContainerInstantiator;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.internal.provider.ECFProviderDebugOptions;
import org.eclipse.ecf.internal.provider.ProviderPlugin;

public class GenericContainerInstantiator implements IContainerInstantiator, IRemoteServiceContainerInstantiator {

	/**
	 * @since 2.0
	 */
	protected static final String[] genericProviderIntents = {"passByValue", "exactlyOnce", "ordered",}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public static final String TCPCLIENT_NAME = "ecf.generic.client"; //$NON-NLS-1$

	public static final String TCPSERVER_NAME = "ecf.generic.server"; //$NON-NLS-1$

	private static final int CREATE_INSTANCE_ERROR_CODE = 4441;

	public GenericContainerInstantiator() {
		super();
	}

	protected ID getIDFromArg(Object arg) throws IDCreateException {
		if (arg == null)
			throw new IDCreateException(Messages.getString("GenericContainerInstantiator.ID_Cannot_Be_Null")); //$NON-NLS-1$
		String val = null;
		if (arg instanceof StringID)
			return (ID) arg;
		else if (arg instanceof GUID)
			val = ((GUID) arg).getName();
		else if (arg instanceof URIID)
			val = ((URIID) arg).toURI().toString();
		else if (arg instanceof LongID)
			val = ((LongID) arg).getName();
		if (arg instanceof String)
			val = (String) arg;
		if (arg instanceof Integer)
			val = IDFactory.getDefault().createGUID(((Integer) arg).intValue()).getName();
		if (val == null)
			val = IDFactory.getDefault().createGUID().getName();
		return IDFactory.getDefault().createStringID(val);
	}

	protected Integer getIntegerFromArg(Object arg) {
		if (arg == null)
			return new Integer(-1);
		if (arg instanceof Integer)
			return (Integer) arg;
		else if (arg instanceof String) {
			return new Integer((String) arg);
		} else
			return new Integer(-1);
	}

	protected class GenericContainerArgs {
		ID id;

		Integer keepAlive;

		public GenericContainerArgs(ID id, Integer keepAlive) {
			this.id = id;
			this.keepAlive = keepAlive;
		}

		public ID getID() {
			return id;
		}

		public Integer getKeepAlive() {
			return keepAlive;
		}
	}

	/**
	 * @since 3.0
	 */
	protected GenericContainerArgs getClientArgs(Object[] args) throws IDCreateException {
		ID newID = null;
		Integer ka = null;
		if (args != null && args.length > 0) {
			if (args.length > 1) {
				if (args[0] instanceof String || args[0] instanceof ID)
					newID = getIDFromArg(args[0]);
				if (args[1] instanceof String || args[1] instanceof Integer)
					ka = getIntegerFromArg(args[1]);
			} else
				newID = getIDFromArg(args[0]);
		}
		if (newID == null)
			newID = IDFactory.getDefault().createStringID(IDFactory.getDefault().createGUID().getName());
		if (ka == null)
			ka = new Integer(0);
		return new GenericContainerArgs(newID, ka);
	}

	protected boolean isClient(ContainerTypeDescription description) {
		if (description.getName().equals(TCPSERVER_NAME))
			return false;
		return true;
	}

	/**
	 * @since 3.0
	 */
	protected GenericContainerArgs getServerArgs(Object[] args) throws IDCreateException {
		ID newID = null;
		Integer ka = null;
		if (args != null && args.length > 0) {
			if (args.length > 1) {
				if (args[0] instanceof String || args[0] instanceof ID)
					newID = getIDFromArg(args[0]);
				if (args[1] instanceof String || args[1] instanceof Integer)
					ka = getIntegerFromArg(args[1]);
			} else
				newID = getIDFromArg(args[0]);
		}
		if (newID == null)
			newID = IDFactory.getDefault().createStringID(TCPServerSOContainer.DEFAULT_PROTOCOL + "://" + TCPServerSOContainer.DEFAULT_HOST + ":" + TCPServerSOContainer.DEFAULT_PORT + TCPServerSOContainer.DEFAULT_NAME); //$NON-NLS-1$ //$NON-NLS-2$
		if (ka == null)
			ka = new Integer(TCPServerSOContainer.DEFAULT_KEEPALIVE);
		return new GenericContainerArgs(newID, ka);
	}

	public IContainer createInstance(ContainerTypeDescription description, Object[] args) throws ContainerCreateException {
		boolean isClient = isClient(description);
		try {
			GenericContainerArgs gcargs = null;
			if (isClient)
				gcargs = getClientArgs(args);
			else
				gcargs = getServerArgs(args);
			// new ID must not be null
			if (isClient) {
				return new TCPClientSOContainer(new SOContainerConfig(gcargs.getID()), gcargs.getKeepAlive().intValue());
			}
			return new TCPServerSOContainer(new SOContainerConfig(gcargs.getID()), gcargs.getKeepAlive().intValue());
		} catch (Exception e) {
			Trace.catching(ProviderPlugin.PLUGIN_ID, ECFProviderDebugOptions.EXCEPTIONS_CATCHING, this.getClass(), "createInstance", e); //$NON-NLS-1$
			ProviderPlugin.getDefault().log(new Status(IStatus.ERROR, ProviderPlugin.PLUGIN_ID, CREATE_INSTANCE_ERROR_CODE, "createInstance", e)); //$NON-NLS-1$
			Trace.throwing(ProviderPlugin.PLUGIN_ID, ECFProviderDebugOptions.EXCEPTIONS_THROWING, this.getClass(), "createInstance", e); //$NON-NLS-1$
			throw new ContainerCreateException("createInstance", e); //$NON-NLS-1$
		}
	}

	protected Set getAdaptersForClass(Class clazz) {
		Set result = new HashSet();
		IAdapterManager adapterManager = ProviderPlugin.getDefault().getAdapterManager();
		if (adapterManager != null)
			result.addAll(Arrays.asList(adapterManager.computeAdapterTypes(clazz)));
		return result;
	}

	protected Set getInterfacesForClass(Set s, Class clazz) {
		if (clazz.equals(Object.class))
			return s;
		s.addAll(getInterfacesForClass(s, clazz.getSuperclass()));
		s.addAll(Arrays.asList(clazz.getInterfaces()));
		return s;
	}

	protected Set getInterfacesForClass(Class clazz) {
		Set clazzes = getInterfacesForClass(new HashSet(), clazz);
		int index = 0;
		Set result = new HashSet();
		for (Iterator i = clazzes.iterator(); i.hasNext(); index++)
			result.add(((Class) i.next()).getName());
		return result;
	}

	protected String[] getInterfacesAndAdaptersForClass(Class clazz) {
		Set result = getAdaptersForClass(clazz);
		result.addAll(getInterfacesForClass(clazz));
		return (String[]) result.toArray(new String[] {});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.provider.IContainerInstantiator#getSupportedAdapterTypes(org.eclipse.ecf.core.ContainerTypeDescription)
	 */
	public String[] getSupportedAdapterTypes(ContainerTypeDescription description) {
		if (!isClient(description))
			return getInterfacesAndAdaptersForClass(TCPServerSOContainer.class);
		return getInterfacesAndAdaptersForClass(TCPClientSOContainer.class);
	}

	/**
	 * @see org.eclipse.ecf.core.provider.IContainerInstantiator#getSupportedParameterTypes(org.eclipse.ecf.core.ContainerTypeDescription)
	 * @since 2.0
	 */
	public Class[][] getSupportedParameterTypes(ContainerTypeDescription description) {
		if (!isClient(description))
			return new Class[][] { {ID.class}, {ID.class, Integer.class}};
		return new Class[][] { {}, {ID.class}, {ID.class, Integer.class}};
	}

	public String[] getSupportedIntents(ContainerTypeDescription description) {
		return genericProviderIntents;
	}

	/**
	 * @since 3.0
	 */
	public String[] getSupportedConfigs(ContainerTypeDescription description) {
		return new String[] {description.getName()};
	}

	/**
	 * @since 3.0
	 */
	public String[] getImportedConfigs(ContainerTypeDescription description, String[] exporterSupportedConfigs) {
		if (exporterSupportedConfigs == null)
			return null;
		List results = new ArrayList();
		List supportedConfigs = Arrays.asList(exporterSupportedConfigs);
		// For a server, if exporter is a client then we can be an importer
		if (TCPSERVER_NAME.equals(description.getName())) {
			if (supportedConfigs.contains(TCPCLIENT_NAME))
				results.add(TCPSERVER_NAME);
			// For a client, if exporter is server we can import
			// or if remote is either generic server or generic client
		} else if (TCPCLIENT_NAME.equals(description.getName())) {
			if (supportedConfigs.contains(TCPSERVER_NAME) || supportedConfigs.contains(TCPCLIENT_NAME))
				results.add(TCPCLIENT_NAME);
		}
		if (results.size() == 0)
			return null;
		return (String[]) results.toArray(new String[] {});
	}

	/**
	 * @since 3.0
	 */
	public Dictionary getPropertiesForImportedConfigs(ContainerTypeDescription description, String[] importedConfigs, Dictionary exportedProperties) {
		return null;
	}
}
 No newline at end of file
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5060.java