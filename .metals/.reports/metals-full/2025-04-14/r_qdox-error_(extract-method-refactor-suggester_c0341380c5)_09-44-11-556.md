error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9736.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9736.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9736.java
text:
```scala
@version $@@Revision: 1.8 $

/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.naming;

import java.io.IOException;
import javax.naming.NamingException;

/** The ExternalContext mbean interface.

@author <a href="mailto:Scott_Stark@displayscape.com">Scott Stark</a>.
@version $Revision: 1.7 $
*/
public interface ExternalContextMBean extends org.jboss.system.ServiceMBean
{
    /** Get the jndi name under which the external context is bound.
    */
    public String getJndiName();
    /** Set the jndi name under which the external context is bound.
    */
    public void setJndiName(String jndiName) throws NamingException;
    /** Get the remote access flag. If true, the external context is bound using
        Serializable object that allows the InitialContext to be recreated
        remotely.
    */
    public boolean getRemoteAccess();
    /** Set the remote access flag. If true, the external context is bound using
        Serializable object that allows the InitialContext to be recreated
        remotely.
    */
    public void setRemoteAccess(boolean remoteAccess);

    /** Get the cacheContext flag.
    */
    public boolean getCacheContext();
    /** set the cacheContext flag. When set to true, the external Context
        is only created when the mbean is started and then stored as an in
        memory object until the mbean is stopped. If cacheContext if set to
        false, the external Context is created on each lookup using the
        mbean Properties and InitialContext class. When the uncached Context
        is looked up by a client, the client should invoke close() on the
        Context to prevent resource leaks.
    */
    public void setCacheContext(boolean flag);

    /** Get the class name of the InitialContext implementation to
	use. Should be one of:
	javax.naming.InitialContext
	javax.naming.directory.InitialDirContext
	javax.naming.ldap.InitialLdapContext
    @return the classname of the InitialContext to use 
     */
    public String getInitialContext();

    /** Set the class name of the InitialContext implementation to
	use. Should be one of:
	javax.naming.InitialContext
	javax.naming.directory.InitialDirContext
	javax.naming.ldap.InitialLdapContext
	The default is javax.naming.InitialContex.
     @param contextClass, the classname of the InitialContext to use
    */
    public void setInitialContext(String contextClass) throws ClassNotFoundException;

    /** Set the jndi.properties information for the external InitialContext.
    This is either a URL string or a classpath resource name. Examples:
        file:///config/myldap.properties
        http://config.mycompany.com/myldap.properties
        /conf/myldap.properties
        myldap.properties

    @param contextPropsURL, either a URL string to a jndi.properties type of
        content or a name of a resource to locate via the current thread
        context classpath.
    @throws IOException, thrown if the url/resource cannot be loaded.
    */
    public void setProperties(String contextPropsURL) throws IOException;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9736.java