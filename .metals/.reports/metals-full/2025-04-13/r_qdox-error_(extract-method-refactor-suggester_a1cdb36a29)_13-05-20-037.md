error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9760.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9760.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,19]

error in qdox parser
file content:
```java
offset: 19
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9760.java
text:
```scala
private transient C@@lassLoader classLoader = null;

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/

package org.eclipse.ecf.core.identity;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.ecf.core.identity.provider.IDInstantiator;

/**
 * Namespace class. This class defines the properties associated with an
 * identity Namespace. Namespaces are defined by a unique 'name' (e.g. 'email',
 * 'icq', 'aolim'), and an 'instantiatorClass'. The instantiator class defines a
 * class that implements the org.eclipse.ecf.identity.provider.IDInstantiator
 * interface and is responsible for creating instances of the given namespace.
 * The instances created by the instantiatorClass <b>must </b> implement the
 * org.eclipse.ecf.identity.ID interface, but otherwise can provide any other
 * identity functionality desired.
 * 
 */
public class Namespace implements Serializable {

    private String name;
    private String instantiatorClass;
    private String description;

    private int hashCode = 0;
    private ClassLoader classLoader = null;

    private transient IDInstantiator instantiator = null;

    public Namespace(ClassLoader cl, String name, String instantiatorClass,
            String desc) {
        this.classLoader = cl;
        this.name = name;
        this.instantiatorClass = instantiatorClass;
        this.description = desc;
        this.hashCode = name.hashCode();
    }
    public Namespace(String name, IDInstantiator inst, String desc) {
        this.instantiator = inst;
        this.instantiatorClass = this.instantiator.getClass().getName();
        this.classLoader = this.instantiator.getClass().getClassLoader();
        this.name = name;
        this.description = desc;
        this.hashCode = name.hashCode();
    }
    /**
     * Override of Object.equals. This equals method returns true if the
     * provided Object is also a Namespace instance, and the names of the two
     * instances match.
     * 
     * @param other
     *            the Object to test for equality
     */
    public boolean equals(Object other) {
        if (!(other instanceof Namespace))
            return false;
        return ((Namespace) other).name.equals(name);
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get an ISharedObjectContainerInstantiator using the classloader used to
     * load the Namespace class
     * 
     * @return ISharedObjectContainerInstantiator associated with this Namespace
     *         instance
     * @exception Exception
     *                thrown if instantiator class cannot be loaded, or if it
     *                cannot be cast to ISharedObjectContainerInstantiator
     *                interface
     */
    protected IDInstantiator getInstantiator() throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        synchronized (this) {
            if (instantiator == null)
                initializeInstantiator(classLoader);
            return instantiator;
        }
    }

    protected boolean testIDEquals(BaseID first, BaseID second) {
        // First check that namespaces are the same and non-null
        Namespace sn = second.getNamespace();
        if (sn == null || !this.equals(sn))
            return false;
        return first.namespaceEquals(second);
    }
    protected String getNameForID(BaseID id) {
        return id.namespaceGetName();
    }
    protected URI getURIForID(BaseID id) throws URISyntaxException {
        return id.namespaceToURI();
    }
    protected int getCompareToForObject(BaseID first, BaseID second) {
        return first.namespaceCompareTo(second);
    }
    protected int getHashCodeForID(BaseID id) {
        return id.namespaceHashCode();
    }

    /**
     * @return String name of Namespace instance
     * 
     */
    public String getName() {
        return name;
    }

    protected String getPermissionName() {
        return toString();
    }

    public int hashCode() {
        return hashCode;
    }
    protected void initializeInstantiator(ClassLoader cl)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        if (cl == null)
            classLoader = this.getClass().getClassLoader();
        // Load instantiator class
        Class clazz = Class.forName(instantiatorClass, true, classLoader);
        // Make new instance
        instantiator = (IDInstantiator) clazz.newInstance();
    }

    public String toString() {
        StringBuffer b = new StringBuffer("Namespace[");
        b.append(name).append(";");
        b.append(instantiatorClass).append(";");
        b.append(description).append("]");
        return b.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9760.java