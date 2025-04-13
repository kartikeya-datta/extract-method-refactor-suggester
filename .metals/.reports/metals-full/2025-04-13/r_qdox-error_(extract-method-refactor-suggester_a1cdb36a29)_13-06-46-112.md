error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2280.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2280.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2280.java
text:
```scala
i@@f (n == null) throw new IDInstantiationException("Namespace "+namespacename+" not found");

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

import org.eclipse.ecf.core.identity.provider.IDInstantiator;
import org.eclipse.ecf.core.util.AbstractFactory;
import org.eclipse.ecf.internal.core.Trace;

/**
 * A factory class for creating ID instances. This is the factory for plugins to
 * manufacture ID instances.
 * 
 */
public class IDFactory {

    private static final Trace debug = Trace.create("idfactory");
    
    public static final String SECURITY_PROPERTY = IDFactory.class.getName()
            + ".security";

    private static Hashtable namespaces = new Hashtable();
    private static boolean securityEnabled = false;

    static {
        addNamespace0(new Namespace(IDFactory.class.getClassLoader(),
                StringID.STRINGID_NAME, StringID.STRINGID_INSTANTIATOR_CLASS,
                null));
        addNamespace0(new Namespace(IDFactory.class.getClassLoader(),
                GUID.GUID_NAME, GUID.GUID_INSTANTIATOR_CLASS, null));
        addNamespace0(new Namespace(IDFactory.class.getClassLoader(),
                LongID.LONGID_NAME, LongID.LONGID_INSTANTIATOR_CLASS, null));
        try {
            securityEnabled = (System.getProperty(SECURITY_PROPERTY) != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add the given Namespace to our table of available Namespaces
     * 
     * @param n
     *            the Namespace to add
     * @return Namespace the namespace already in table (null if Namespace not
     *         previously in table)
     * @exception SecurityException
     *                thrown if caller does not have appropriate
     *                NamespacePermission for given namespace
     */
    public final static Namespace addNamespace(Namespace n)
            throws SecurityException {
        if (n == null)
            return null;
        checkPermission(new NamespacePermission(n.getPermissionName(),
                NamespacePermission.ADD_NAMESPACE));
        return addNamespace0(n);
    }

    protected final static Namespace addNamespace0(Namespace n) {
        if (n == null)
            return null;
        return (Namespace) namespaces.put(n.getName(), n);
    }

    protected final static void checkPermission(NamespacePermission p)
            throws SecurityException {
        if (securityEnabled)
            AccessController.checkPermission(p);
    }
    /**
     * Check whether table contains given Namespace instance
     * 
     * @param n
     *            the Namespace to look for
     * @return true if table does contain given Namespace, false otherwise
     * @exception SecurityException
     *                thrown if caller does not have appropriate
     *                NamespacePermission for given namespace
     */
    public final static boolean containsNamespace(Namespace n)
            throws SecurityException {
        debug("containsNamespace("+n+")");
        if (n == null)
            return false;
        checkPermission(new NamespacePermission(n.getPermissionName(),
                NamespacePermission.CONTAINS_NAMESPACE));
        return containsNamespace0(n);
    }
    public final static List getNamespaces() {
        debug("getNamespaces()");
        return new ArrayList(namespaces.values());
    }
    private static void debug(String msg) {
        if (Trace.ON && debug != null) {
            debug.msg(msg);
        }
    }

    private static void dumpStack(String msg, Throwable e) {
        if (Trace.ON && debug != null) {
            debug.dumpStack(e, msg);
        }
    }
    protected final static boolean containsNamespace0(Namespace n) {
        if (n == null)
            return false;
        return namespaces.containsKey(n.getName());
    }
    /**
     * Get the given Namespace instance from table
     * 
     * @param n
     *            the Namespace to look for
     * @return Namespace
     * @exception SecurityException
     *                thrown if caller does not have appropriate
     *                NamespacePermission for given namespace
     */
    public final static Namespace getNamespace(Namespace n)
            throws SecurityException {
        debug("getNamespace("+n+")");
        if (n == null)
            return null;
        checkPermission(new NamespacePermission(n.getPermissionName(),
                NamespacePermission.GET_NAMESPACE));
        return getNamespace0(n);
    }

    public final static Namespace getNamespaceByName(String name)
            throws SecurityException {
        debug("getNamespaceByName("+name+")");
        Namespace ns = new Namespace(null, name, name, null);
        return getNamespace(ns);
    }

    protected final static Namespace getNamespace0(Namespace n) {
        if (n == null)
            return null;
        return (Namespace) namespaces.get(n.getName());
    }

    public static final ID makeGUID() throws IDInstantiationException {
        return makeGUID(GUID.DEFAULT_BYTE_LENGTH);
    }

    public static final ID makeGUID(int length) throws IDInstantiationException {
        debug("makeGUID("+length+")");
        Namespace n = new Namespace(GUID.class.getClassLoader(),
                GUID.GUID_NAME, GUID.GUID_INSTANTIATOR_CLASS, null);
        return makeID(n, new String[] { Namespace.class.getName(),
                Integer.class.getName() }, new Object[] { n,
                new Integer(length) });
    }

    protected static void log(String s) {
        debug(s);
    }

    protected static void logException(String s, Throwable t) {
        dumpStack(s,t);
    }

    protected static void logAndThrow(String s, Throwable t)
            throws IDInstantiationException {
    	IDInstantiationException e = null;
    	if (t != null) {
    		e = new IDInstantiationException(s+": "+t.getClass().getName()+": "+t.getMessage());
    		e.setStackTrace(t.getStackTrace());
    	} else {
    		e = new IDInstantiationException(s);
    	}
        logException(s, t);
        throw e;
    }
    protected static void logAndThrow(String s) throws IDInstantiationException {
        IDInstantiationException e = new IDInstantiationException(s);
        logException(s, null);
        throw e;
    }
    /**
     * Make a new identity. Given a classloader, Namespace, constructor argument
     * types, and an array of arguments, return a new instance of an ID
     * belonging to the given Namespace
     * 
     * @param n
     *            the Namespace to which the ID will belong
     * @param argTypes
     *            a String [] of the arg types for the ID instance constructor
     * @param args
     *            an Object [] of the args for the ID instance constructor
     * @exception Exception
     *                thrown if class for instantiator or instance can't be
     *                loaded, if something goes wrong during instance
     *                construction or if instance cannot be created
     */
    public static final ID makeID(Namespace n, String[] argTypes, Object[] args)
            throws IDInstantiationException {
        debug("makeID("+n+","+Trace.convertStringAToString(argTypes)+","+Trace.convertObjectAToString(args)+")");
        // Verify namespace is non-null
        if (n == null)
            logAndThrow("Namespace cannot be null");
        // Make sure that namespace is in table of known namespace. If not,
        // throw...we don't create any instances that we don't know about!
        Namespace ns = getNamespace0(n);
        if (ns == null)
            logAndThrow("Namespace '" + n.getName() + "' not found");
        // We're OK, go ahead and setup array of classes for call to
        // instantiator
        Class clazzes[] = null;
        ClassLoader cl = ns.getClass().getClassLoader();
        try {
            clazzes = AbstractFactory.getClassesForTypes(argTypes, args, cl);
        } catch (ClassNotFoundException e) {
            logAndThrow("Exception in getClassesForTypes", e);
        }
        // Get actual instantiator from namespace
        IDInstantiator instantiator = null;
        try {
            instantiator = (IDInstantiator) ns.getInstantiator();
        } catch (Exception e) {
            logAndThrow("Exception in getInstantiator", e);
        }
        debug("makeID:got instantiator:"+instantiator);
        if (instantiator == null)
            throw new IDInstantiationException("Instantiator for namespace '"
                    + n.getName() + "' is null");
        // Ask instantiator to actually create instance
        return instantiator.makeInstance(ns, clazzes, args);
    }
    public static final ID makeID(String namespacename, String[] argTypes,
            Object[] args) throws IDInstantiationException {
    	Namespace n = getNamespaceByName(namespacename);
    	if (n == null) throw new IDInstantiationException("Namespace named "+namespacename+" not found");
        return makeID(n, argTypes, args);
    }
    /**
     * Make a new identity. Given a Namespace, and an array of instance
     * constructor arguments, return a new instance of an ID belonging to the
     * given Namespace
     * 
     * @param n
     *            the Namespace to which the ID will belong
     * @param args
     *            an Object [] of the args for the ID instance constructor
     * @exception Exception
     *                thrown if class for instantiator or instance can't be
     *                loaded, if something goes wrong during instance
     *                construction or if instance cannot be created
     */
    public static final ID makeID(Namespace n, Object[] args)
            throws IDInstantiationException {
        return makeID(n, null, args);
    }
    public static final ID makeID(String namespacename, Object[] args)
            throws IDInstantiationException {
    	Namespace n = getNamespaceByName(namespacename);
    	if (n == null) throw new IDInstantiationException("Namespace named "+namespacename+" not found");
        return makeID(n, args);
    }

    public static final ID makeStringID(String idstring)
            throws IDInstantiationException {
        if (idstring == null) throw new IDInstantiationException("String cannot be null");
        Namespace n = new Namespace(StringID.class.getClassLoader(),
                StringID.STRINGID_NAME, StringID.STRINGID_INSTANTIATOR_CLASS,
                null);
        return makeID(n, new String[] { String.class.getName() },
                new Object[] { idstring });
    }
    public static final ID makeLongID(Long l) throws IDInstantiationException {
        if (l == null) throw new IDInstantiationException("Long cannot be null");
        Namespace n = new Namespace(LongID.class.getClassLoader(),
                LongID.LONGID_NAME, LongID.LONGID_INSTANTIATOR_CLASS, null);
        return makeID(n, new String[] { String.class.getName() },
                new Object[] { l });
    }
    public static final ID makeLongID(long l) throws IDInstantiationException {
        Namespace n = new Namespace(LongID.class.getClassLoader(),
                LongID.LONGID_NAME, LongID.LONGID_INSTANTIATOR_CLASS, null);
        return makeID(n, new String[] { String.class.getName() },
                new Object[] { new Long(l) });
    }
    /**
     * Remove the given Namespace from our table of available Namespaces
     * 
     * @param n
     *            the Namespace to remove
     * @return Namespace the namespace already in table (null if Namespace not
     *         previously in table)
     * @exception SecurityException
     *                thrown if caller does not have appropriate
     *                NamespacePermission for given namespace
     */
    public final static Namespace removeNamespace(Namespace n)
            throws SecurityException {
        debug("removeNamespace("+n+")");
        if (n == null)
            return null;
        checkPermission(new NamespacePermission(n.getPermissionName(),
                NamespacePermission.REMOVE_NAMESPACE));
        return removeNamespace0(n);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2280.java