error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4294.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4294.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4294.java
text:
```scala
private static T@@race debug = Trace.create("containerfactory");

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/

package org.eclipse.ecf.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.ecf.core.provider.IContainerInstantiator;
import org.eclipse.ecf.core.util.AbstractFactory;
import org.eclipse.ecf.internal.core.Trace;

/**
 * Factory for creating {@link IContainer} instances. This
 * class provides ECF clients an entry point to constructing {@link IContainer}
 * instances.  
 * <br>
 * <br>
 * Here is an example use of the ContainerFactory to construct an instance
 * of the 'standalone' container (has no connection to other containers):
 * <br><br>
 * <code>
 * 	    IContainer container = <br>
 * 			ContainerFactory.getDefault().makeContainer('standalone');
 *      <br><br>
 *      ...further use of container variable here...
 * </code>
 * 
 */
public class ContainerFactory implements IContainerFactory {

    private static Trace debug = Trace.create("simplecontainerfactory");
    
    private static Hashtable containerdescriptions = new Hashtable();
    protected static IContainerFactory instance = null;
    
    static {
    	instance = new ContainerFactory();
    }
    protected ContainerFactory() {
    }
    public static IContainerFactory getDefault() {
    	return instance;
    }
    private static void trace(String msg) {
        if (Trace.ON && debug != null) {
            debug.msg(msg);
        }
    }

    private static void dumpStack(String msg, Throwable e) {
        if (Trace.ON && debug != null) {
            debug.dumpStack(e, msg);
        }
    }
    /* (non-Javadoc)
	 * @see org.eclipse.ecf.core.IContainerFactory#addDescription(org.eclipse.ecf.core.ContainerDescription)
	 */
    public ContainerDescription addDescription(
            ContainerDescription scd) {
        trace("addDescription("+scd+")");
        return addDescription0(scd);
    }
    /* (non-Javadoc)
	 * @see org.eclipse.ecf.core.IContainerFactory#getDescriptions()
	 */
    public List getDescriptions() {
        return getDescriptions0();
    }
    protected List getDescriptions0() {
        return new ArrayList(containerdescriptions.values());
    }
    protected ContainerDescription addDescription0(
            ContainerDescription n) {
        if (n == null)
            return null;
        return (ContainerDescription) containerdescriptions.put(n
                .getName(), n);
    }
    /* (non-Javadoc)
	 * @see org.eclipse.ecf.core.IContainerFactory#containsDescription(org.eclipse.ecf.core.ContainerDescription)
	 */
    public boolean containsDescription(
            ContainerDescription scd) {
        return containsDescription0(scd);
    }
    protected boolean containsDescription0(
            ContainerDescription scd) {
        if (scd == null)
            return false;
        return containerdescriptions.containsKey(scd.getName());
    }
    protected ContainerDescription getDescription0(
            ContainerDescription scd) {
        if (scd == null)
            return null;
        return (ContainerDescription) containerdescriptions.get(scd
                .getName());
    }
    protected ContainerDescription getDescription0(
            String name) {
        if (name == null)
            return null;
        return (ContainerDescription) containerdescriptions.get(name);
    }
    /* (non-Javadoc)
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#getDescriptionByName(java.lang.String)
	 */
    public ContainerDescription getDescriptionByName(
            String name) throws ContainerInstantiationException {
        trace("getDescriptionByName("+name+")");
        ContainerDescription res = getDescription0(name);
        if (res == null) {
            throw new ContainerInstantiationException(
                    "ContainerDescription named '" + name
                            + "' not found");
        }
        return res;
    }
    /* (non-Javadoc)
	 * @see org.eclipse.ecf.core.IContainerFactory#makeContainer(org.eclipse.ecf.core.ContainerDescription, java.lang.String[], java.lang.Object[])
	 */
    public IContainer makeContainer(
            ContainerDescription desc, String[] argTypes,
            Object[] args) throws ContainerInstantiationException {
        trace("makeContainer("+desc+","+Trace.convertStringAToString(argTypes)+","+Trace.convertObjectAToString(args)+")");
        if (desc == null)
            throw new ContainerInstantiationException(
                    "ContainerDescription cannot be null");
        ContainerDescription cd = getDescription0(desc);
        if (cd == null)
            throw new ContainerInstantiationException(
                    "ContainerDescription named '" + desc.getName()
                            + "' not found");
        Class clazzes[] = null;
        IContainerInstantiator instantiator = null;
        try {
            instantiator = (IContainerInstantiator) cd
            .getInstantiator();
            clazzes = AbstractFactory.getClassesForTypes(argTypes, args, cd.getClassLoader());
        } catch (Exception e) {
            ContainerInstantiationException newexcept = new ContainerInstantiationException(
                    "makeContainer exception with description: "+desc+": "+e.getClass().getName()+": "+e.getMessage());
            newexcept.setStackTrace(e.getStackTrace());
            dumpStack("Exception in makeContainer",newexcept);
            throw newexcept;
        }
        if (instantiator == null)
            throw new ContainerInstantiationException(
                    "Instantiator for ContainerDescription "
                            + cd.getName() + " is null");
        // Ask instantiator to actually create instance
        return (IContainer) instantiator
                .makeInstance(desc,clazzes, args);
    }
    /* (non-Javadoc)
	 * @see org.eclipse.ecf.core.IContainerFactory#makeContainer(java.lang.String)
	 */
    public IContainer makeContainer(
            String descriptionName)
            throws ContainerInstantiationException {
        return makeContainer(
                getDescriptionByName(descriptionName), null, null);
    }
    /* (non-Javadoc)
	 * @see org.eclipse.ecf.core.IContainerFactory#makeContainer(java.lang.String, java.lang.Object[])
	 */
    public IContainer makeContainer(
            String descriptionName, Object[] args)
            throws ContainerInstantiationException {
        return makeContainer(
                getDescriptionByName(descriptionName), null, args);
    }
    /* (non-Javadoc)
	 * @see org.eclipse.ecf.core.IContainerFactory#makeContainer(java.lang.String, java.lang.String[], java.lang.Object[])
	 */
    public IContainer makeContainer(
            String descriptionName, String[] argsTypes, Object[] args)
            throws ContainerInstantiationException {
        return makeContainer(
                getDescriptionByName(descriptionName), argsTypes, args);
    }
    /* (non-Javadoc)
	 * @see org.eclipse.ecf.core.IContainerFactory#removeDescription(org.eclipse.ecf.core.ContainerDescription)
	 */
    public ContainerDescription removeDescription(
            ContainerDescription scd) {
        trace("removeDescription("+scd+")");
        return removeDescription0(scd);
    }
    protected ContainerDescription removeDescription0(
            ContainerDescription n) {
        if (n == null)
            return null;
        return (ContainerDescription) containerdescriptions.remove(n
                .getName());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4294.java