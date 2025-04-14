error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18337.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18337.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18337.java
text:
```scala
public v@@oid setProperties(Map props) {

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/

package org.eclipse.ecf.core;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.ecf.core.identity.ID;

/**
 * Description of an ISharedObject instance.
 * 
 * @see org.eclipse.ecf.core.ISharedObjectManager#createSharedObject(SharedObjectDescription, ISharedObjectContainerTransaction)
 */
public class SharedObjectDescription implements Serializable {

	private static final long serialVersionUID = 3257848783613146929L;

	protected static long staticID = 0;

    protected transient ClassLoader classLoader;
    protected ID id;
    protected ID homeID;
    protected String className;
    protected Map properties;
    protected long identifier;

    public static long getNextUniqueIdentifier() {
        return staticID++;
    }
    public SharedObjectDescription(ClassLoader loader, ID objectID, ID homeID,
            String className, Map dict, long ident) {
        this.classLoader = loader;
        this.id = objectID;
        this.homeID = homeID;
        this.className = className;
        if (dict != null) this.properties = dict;
        else this.properties = new Hashtable();
        this.identifier = ident;
    }

    public SharedObjectDescription(ClassLoader loader, ID id, String className, Map dict, long ident) {
        this(loader, id, null, className, dict, ident);
    }
    public SharedObjectDescription(ID id, Class clazz, Map dict, long ident) {
        this(clazz.getClassLoader(),id,clazz.getName(),dict,ident);
    }
    public SharedObjectDescription(ClassLoader loader, ID id, String className, Map dict) {
        this(loader, id, null, className, dict, getNextUniqueIdentifier());
    }
    public SharedObjectDescription(ClassLoader loader, ID id, String className) {
        this(loader, id, null, className, null, getNextUniqueIdentifier());
    }
    public SharedObjectDescription(ID id, String className, Map dict, long ident) {
        this(null, id, null, className, dict, ident);
    }
    public SharedObjectDescription(ID id, String className, Map dict) {
        this(null, id, null, className, dict, getNextUniqueIdentifier());
    }
    public SharedObjectDescription(ID id, Class clazz, Map dict) {
        this(id,clazz,dict,getNextUniqueIdentifier());
    }
    public SharedObjectDescription(ID id, String className, long ident) {
        this(null, id, null, className, null, ident);
    }
    public SharedObjectDescription(ID id, Class clazz, long ident) {
        this(id,clazz,null,ident);
    }
    public SharedObjectDescription(ID id, String className) {
        this(id, className, getNextUniqueIdentifier());
    }
    public SharedObjectDescription(ID id, Class clazz) {
        this(id,clazz,null);
    }
    public ID getID() {
        return id;
    }
    public void setID(ID theID) {
        this.id = theID;
    }
    public ID getHomeID() {
        return homeID;
    }
    public void setHomeID(ID theID) {
        this.homeID = theID;
    }
    public String getClassname() {
        return className;
    }
    public void setClassname(String theName) {
        this.className = theName;
    }
    public Map getProperties() {
        return properties;
    }
    public void getProperties(Map props) {
        this.properties = props;
    }
    public ClassLoader getClassLoader() {
        return classLoader;
    }
    public long getIdentifier() {
        return identifier;
    }
    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("SharedObjectDescription[");
        sb.append("classLoader:").append(classLoader).append(";");
        sb.append("id:").append(id).append(";");
        sb.append("homeID:").append(homeID).append(";");
        sb.append("class:").append(className).append(";");
        sb.append("props:").append(properties).append(";");
        sb.append("ident:").append(identifier).append("]");
        return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18337.java