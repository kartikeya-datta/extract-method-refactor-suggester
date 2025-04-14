error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12885.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12885.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12885.java
text:
```scala
r@@eturn (Namespace) namespaces.remove(n.getName());

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.core.identity;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ecf.core.util.Trace;
import org.eclipse.ecf.internal.core.identity.Activator;
import org.eclipse.ecf.internal.core.identity.IdentityDebugOptions;

/**
 * A factory class for creating ID instances. This is the factory for plugins to
 * manufacture ID instances. 
 *  
 */
public class IDFactory implements IIDFactory {
	public static final String SECURITY_PROPERTY = IDFactory.class.getName()
			+ ".security";

	private static final int IDENTITY_CREATION_ERRORCODE = 2001;

	private static Hashtable namespaces = new Hashtable();

	private static boolean securityEnabled = false;

	protected static IIDFactory instance = null;

	static {
		instance = new IDFactory();
		addNamespace0(new StringID.StringIDNamespace());
		addNamespace0(new GUID.GUIDNamespace());
		addNamespace0(new LongID.LongNamespace());
	}

	protected IDFactory() {
	}

	public static IIDFactory getDefault() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.IIDFactory#addNamespace(org.eclipse.ecf.core.identity.Namespace)
	 */
	public Namespace addNamespace(Namespace namespace) throws SecurityException {
		if (namespace == null)
			return null;
		Trace.entering(Activator.getDefault(),
				IdentityDebugOptions.METHODS_ENTERING, IDFactory.class,
				"addNamespace", namespace);
		checkPermission(new NamespacePermission(namespace.toString(),
				NamespacePermission.ADD_NAMESPACE));
		Namespace result = addNamespace0(namespace);
		Trace.exiting(Activator.getDefault(),
				IdentityDebugOptions.METHODS_EXITING, IDFactory.class,
				"addNamespace", result);
		return result;
	}

	protected final static Namespace addNamespace0(Namespace namespace) {
		if (namespace == null)
			return null;
		return (Namespace) namespaces.put(namespace.getName(), namespace);
	}

	protected final static void checkPermission(
			NamespacePermission namespacepermission) throws SecurityException {
		if (securityEnabled)
			AccessController.checkPermission(namespacepermission);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.IIDFactory#containsNamespace(org.eclipse.ecf.core.identity.Namespace)
	 */
	public boolean containsNamespace(Namespace namespace)
			throws SecurityException {
		Trace.entering(Activator.getDefault(),
				IdentityDebugOptions.METHODS_ENTERING, IDFactory.class,
				"containsNamespace", namespace);
		if (namespace == null)
			return false;
		checkPermission(new NamespacePermission(namespace.toString(),
				NamespacePermission.CONTAINS_NAMESPACE));
		boolean result = containsNamespace0(namespace);
		Trace.exiting(Activator.getDefault(),
				IdentityDebugOptions.METHODS_EXITING, IDFactory.class,
				"containsNamespace", new Boolean(result));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.IIDFactory#getNamespaces()
	 */
	public List getNamespaces() {
		Trace.entering(Activator.getDefault(),
				IdentityDebugOptions.METHODS_ENTERING, IDFactory.class,
				"getNamespaces");
		return new ArrayList(namespaces.values());
	}

	protected final static boolean containsNamespace0(Namespace n) {
		if (n == null)
			return false;
		return namespaces.containsKey(n.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.IIDFactory#getNamespace(org.eclipse.ecf.core.identity.Namespace)
	 */
	public Namespace getNamespace(Namespace namespace) throws SecurityException {
		Trace.entering(Activator.getDefault(),
				IdentityDebugOptions.METHODS_ENTERING, IDFactory.class,
				"getNamespace", namespace);
		if (namespace == null)
			return null;
		checkPermission(new NamespacePermission(namespace.toString(),
				NamespacePermission.GET_NAMESPACE));
		Namespace result = getNamespace0(namespace);
		Trace.exiting(Activator.getDefault(),
				IdentityDebugOptions.METHODS_EXITING, IDFactory.class,
				"getNamespace", result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.IIDFactory#getNamespaceByName(java.lang.String)
	 */
	public Namespace getNamespaceByName(String name) throws SecurityException {
		Trace.entering(Activator.getDefault(),
				IdentityDebugOptions.METHODS_ENTERING, IDFactory.class,
				"getNamespaceByName", name);
		Namespace result = getNamespace0(name);
		Trace.exiting(Activator.getDefault(),
				IdentityDebugOptions.METHODS_EXITING, IDFactory.class,
				"getNamespaceByName", result);
		return result;
	}

	protected final static Namespace getNamespace0(Namespace n) {
		if (n == null)
			return null;
		return (Namespace) namespaces.get(n.getName());
	}

	protected final static Namespace getNamespace0(String name) {
		if (name == null)
			return null;
		else
			return (Namespace) namespaces.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.IIDFactory#createGUID()
	 */
	public ID createGUID() throws IDCreateException {
		return createGUID(GUID.DEFAULT_BYTE_LENGTH);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.IIDFactory#createGUID(int)
	 */
	public ID createGUID(int length) throws IDCreateException {
		Trace.entering(Activator.getDefault(),
				IdentityDebugOptions.METHODS_ENTERING, IDFactory.class,
				"createGUID", new Integer(length));
		Namespace namespace = new GUID.GUIDNamespace();
		ID result = createID(namespace, new Integer[] { new Integer(length) });
		Trace.exiting(Activator.getDefault(),
				IdentityDebugOptions.METHODS_EXITING, IDFactory.class,
				"createGUID", result);
		return result;
	}

	protected static void logAndThrow(String s, Throwable t)
			throws IDCreateException {
		IDCreateException e = null;
		if (t != null) {
			e = new IDCreateException(s + ": " + t.getClass().getName() + ": "
					+ t.getMessage(), t);
		} else {
			e = new IDCreateException(s);
		}
		Trace.throwing(Activator.getDefault(),
				IdentityDebugOptions.EXCEPTIONS_THROWING, IDFactory.class,
				"logAndThrow", e);
		Activator.getDefault().getLog().log(
				new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						IDENTITY_CREATION_ERRORCODE, s, e));
		throw e;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.IIDFactory#createID(org.eclipse.ecf.core.identity.Namespace,
	 *      java.lang.Object[])
	 */
	public ID createID(Namespace n, Object[] args) throws IDCreateException {
		Trace.entering(Activator.getDefault(),
				IdentityDebugOptions.METHODS_ENTERING, IDFactory.class,
				"createID", new Object[] { n, Trace.getArgumentsString(args) });
		// Verify namespace is non-null
		if (n == null)
			logAndThrow("Namespace cannot be null", null);
		// Make sure that namespace is in table of known namespace. If not,
		// throw...we don't create any instances that we don't know about!
		Namespace ns = getNamespace0(n);
		if (ns == null)
			logAndThrow("Namespace '" + n.getName() + "' not found", null);
		// We're OK, go ahead and setup array of classes for call to
		// instantiator
		// Ask instantiator to actually create instance
		ID result = ns.createInstance(args);
		Trace.exiting(Activator.getDefault(),
				IdentityDebugOptions.METHODS_EXITING, IDFactory.class,
				"createID", result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.IIDFactory#createID(java.lang.String,
	 *      java.lang.Object[])
	 */
	public ID createID(String namespacename, Object[] args)
			throws IDCreateException {
		Namespace n = getNamespaceByName(namespacename);
		if (n == null)
			throw new IDCreateException("Namespace named " + namespacename
					+ " not found");
		return createID(n, args);
	}

	public ID createID(Namespace namespace, String uri)
			throws IDCreateException {
		return createID(namespace, new Object[] { uri });
	}

	public ID createID(String namespace, String uri) throws IDCreateException {
		return createID(namespace, new Object[] { uri });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.IIDFactory#createStringID(java.lang.String)
	 */
	public ID createStringID(String idstring) throws IDCreateException {
		if (idstring == null)
			throw new IDCreateException("String cannot be null");
		Namespace n = new StringID.StringIDNamespace();
		return createID(n, new String[] { idstring });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.IIDFactory#createLongID(long)
	 */
	public ID createLongID(long l) throws IDCreateException {
		Namespace n = new LongID.LongNamespace();
		return createID(n, new Long[] { new Long(l) });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.IIDFactory#removeNamespace(org.eclipse.ecf.core.identity.Namespace)
	 */
	public Namespace removeNamespace(Namespace n) throws SecurityException {
		Trace.entering(Activator.getDefault(),
				IdentityDebugOptions.METHODS_ENTERING, IDFactory.class,
				"removeNamespace", n);
		if (n == null)
			return null;
		checkPermission(new NamespacePermission(n.toString(),
				NamespacePermission.REMOVE_NAMESPACE));
		Namespace result = removeNamespace0(n);
		Trace.exiting(Activator.getDefault(),
				IdentityDebugOptions.METHODS_EXITING, IDFactory.class,
				"removeNamespace", result);
		return result;
	}

	protected final static Namespace removeNamespace0(Namespace n) {
		if (n == null)
			return null;
		return (Namespace) namespaces.remove(n);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12885.java