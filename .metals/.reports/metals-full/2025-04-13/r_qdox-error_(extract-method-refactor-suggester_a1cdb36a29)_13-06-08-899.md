error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15176.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15176.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15176.java
text:
```scala
n@@ewContainer.dispose();

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/

package org.eclipse.ecf.core;

import org.eclipse.ecf.internal.core.Trace;

/**
 * Factory for creating {@link ISharedObjectContainer} instances. This
 * class provides ECF clients an entry point to constructing {@link ISharedObjectContainer}
 * instances.  
 * <br>
 * <br>
 * Here is an example use of the SharedObjectContainerFactory to construct an instance
 * of the 'standalone' container (has no connection to other containers):
 * <br><br>
 * <code>
 * 	    ISharedObjectContainer container = <br>
 * 			SharedObjectContainerFactory.getDefault().makeSharedObjectContainer('standalone');
 *      <br><br>
 *      ...further use of container variable here...
 * </code>
 * 
 */
public class SharedObjectContainerFactory implements ISharedObjectContainerFactory {

    private static Trace debug = Trace.create("sharedobjectcontainerfactory");
    
    protected static ISharedObjectContainerFactory instance = null;
    
    static {
    	instance = new SharedObjectContainerFactory();
    }
    protected SharedObjectContainerFactory() {
    }
    public static ISharedObjectContainerFactory getDefault() {
    	return instance;
    }
    private static void trace(String msg) {
        if (Trace.ON && debug != null) {
            debug.msg(msg);
        }
    }

    /* (non-Javadoc)
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#makeSharedObjectContainer(org.eclipse.ecf.core.SharedObjectContainerDescription, java.lang.String[], java.lang.Object[])
	 */
    public ISharedObjectContainer makeSharedObjectContainer(
            ContainerDescription desc, String[] argTypes,
            Object[] args) throws ContainerInstantiationException {
        trace("makeSharedObjectContainer("+desc+","+Trace.convertStringAToString(argTypes)+","+Trace.convertObjectAToString(args)+")");
        if (desc == null)
            throw new ContainerInstantiationException(
                    "ContainerDescription cannot be null");
        IContainer newContainer = ContainerFactory.getDefault().makeContainer(desc,argTypes,args);
        ISharedObjectContainer soContainer = (ISharedObjectContainer) newContainer.getAdapter(ISharedObjectContainer.class);
        if (soContainer == null) {
        	newContainer.dispose(-1L);
        	throw new ContainerInstantiationException("new container is not a shared object container");
        }
        return soContainer;
    }
    /* (non-Javadoc)
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#makeSharedObjectContainer(java.lang.String)
	 */
    public ISharedObjectContainer makeSharedObjectContainer(
            String descriptionName)
            throws ContainerInstantiationException {
    	
        return makeSharedObjectContainer(
                ContainerFactory.getDefault().getDescriptionByName(descriptionName), null, null);
    }
    /* (non-Javadoc)
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#makeSharedObjectContainer(java.lang.String, java.lang.Object[])
	 */
    public ISharedObjectContainer makeSharedObjectContainer(
            String descriptionName, Object[] args)
            throws ContainerInstantiationException {
        return makeSharedObjectContainer(
        		ContainerFactory.getDefault().getDescriptionByName(descriptionName), null, args);
    }
    /* (non-Javadoc)
	 * @see org.eclipse.ecf.core.ISharedObjectContainerFactory#makeSharedObjectContainer(java.lang.String, java.lang.String[], java.lang.Object[])
	 */
    public ISharedObjectContainer makeSharedObjectContainer(
            String descriptionName, String[] argsTypes, Object[] args)
            throws ContainerInstantiationException {
        return makeSharedObjectContainer(
        		ContainerFactory.getDefault().getDescriptionByName(descriptionName), argsTypes, args);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15176.java