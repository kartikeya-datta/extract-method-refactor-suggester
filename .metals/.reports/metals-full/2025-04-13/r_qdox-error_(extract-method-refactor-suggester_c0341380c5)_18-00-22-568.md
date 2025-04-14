error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8406.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8406.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8406.java
text:
```scala
S@@tringTokenizer st = new StringTokenizer(url, "://");

/*
 * @(#) DefaultCarolValues.java	1.0 02/07/15
 *
 * Copyright (C) 2002 - INRIA (www.inria.fr)
 *
 * CAROL: Common Architecture for RMI ObjectWeb Layer
 *
 * This library is developed inside the ObjectWeb Consortium,
 * http://www.objectweb.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 *
 */
package org.objectweb.carol.util.configuration;

//java import 
import java.util.Properties;
import java.util.StringTokenizer;

/*
 * Class <code>DefaultCarolValues</code> get default carol value for the properties file and
 * get carol properties with defaults from jndi Standard properties
 */
public class CarolDefaultValues {

    /**
     * Carol prefix
     */
    public static String CAROL_PREFIX="carol";

    /**
     * JNDI Prefix
     */
    public static String JNDI_PREFIX="jndi";

    /**
     * JVM Prefix
     */
    public static String JVM_PREFIX="jvm";

    /**
     * name service class prefix
     */
    public static String NS_PREFIX="NameServiceClass"; 

    /**
     * portable remote object Prefix
     */
    public static String PRO_PREFIX="PortableRemoteObjectClass";

    /**
     * jndi factory Prefix
     */
    public static String JNDI_FACTORY_PREFIX="java.naming.factory.initial";

    /**
     * jndi url  Prefix
     */
    public static String JNDI_URL_PREFIX="java.naming.provider.url";

    /**
     * jndi pkgs  Prefix
     */
    public static String JNDI_PKGS_PREFIX="java.naming.factory.url.pkgs";

    /**
     * carol url  Prefix
     */
    public static String URL_PREFIX="url";

    /**
     * carol factory Prefix
     */
    public static String FACTORY_PREFIX="context.factory";

    /**
     * start name service Prefix
     */
    public static String START_NS_PREFIX="start.ns";

    /**
     * start ns  key
     */
    public static String START_NS_KEY="carol.start.ns"; 

   /**
     * start rmi  key
     */
    public static String START_RMI_KEY="carol.start.rmi"; 
    
    /**
     * start jndi  key
     */ 
    public static String START_JNDI_KEY="carol.start.jndi";
    
    /**
     * default activation key
     */
    public static String DEFAULT_PROTOCOLS_KEY="carol.protocols.default";   

    /**
     * acativation key
     */
    public static String PROTOCOLS_KEY="carol.protocols";   

   /**
     * start ns  key
     */
    public static String MULTI_RMI_PREFIX="multi";

   /**
     * start prod  key
     */
    public static String MULTI_PROD="org.objectweb.carol.rmi.multi.MultiPRODelegate";


   /**
     * start jndi  key
     */
    public static String MULTI_JNDI="org.objectweb.carol.jndi.spi.MultiOrbInitialContextFactory";

    /**
     * interceptor prefix
     */
    public static String INTERCEPTOR_PKGS_PREFIX = "interceptor.pkgs";

    /**
     * interceptor prefix
     */
    public static String INTERCEPTOR_VALUES_PREFIX = "interceptors";
    
	/**
	 * System port property
	 */
	public static String PORT_NUMBER_PROPERTY = "rmi.server.port";

    /**
     * Hashtable mapping between default en rmi name
     */
    public static Properties mapping = new Properties();

    static {	
	mapping.setProperty("rmi","jrmp");
	mapping.setProperty("iiop","iiop");
	mapping.setProperty("corbaloc","openorb");
	mapping.setProperty("jrmi","jeremie");
	mapping.setProperty("cmi","cmi");
	mapping.setProperty("lmi","lmi");
    }
  
    /**
     * return protocol name from url
     * @return protocol name
     * @param protocol jndi url
     */
    public static String getRMIProtocol(String url) {
	if (url != null) {
	    StringTokenizer st = new StringTokenizer(url, ":");
	    String pref = st.nextToken().trim();
	    return mapping.getProperty(pref,pref);
	} else {
	    return null;
	}
    }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8406.java