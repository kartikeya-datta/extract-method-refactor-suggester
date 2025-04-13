error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1964.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1964.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1964.java
text:
```scala
@version $@@Revision: 1.2 $

/*
 * JBoss, the OpenSource WebOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.web.tomcat.tc4;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;

import org.w3c.dom.Element;

import org.apache.catalina.startup.Embedded;
import org.apache.catalina.startup.ContextRuleSet;

import org.jboss.logging.Logger;
import org.jboss.web.tomcat.tc4.AddEngineAction;

/** This is a step toward supporting elements of the catalina server.xml config
 as child elements of the jboss.jcml mbean/config extended configuration element.

 TOMCAT 4.1.12: To use the Digester package bundled with Tomcat 4.1.12 instead.

@author Scott.Stark@jboss.org
@author alain.coetmeur@caissedesdepots.fr
@version $Revision: 1.1 $
 */
public class ConfigHandler
{
   private Logger log;

   /** Creates new ConfigHandler */
   public ConfigHandler(Logger log)
   {
      this.log = log;
   }

   /** Handle the Connector configuration elements.
    @param config, the mbean/config jboss.jcml element
    @param root, the object to which the Connectors will be added using
    addConnector
    @param debug, a flag indicating if the XmlMapper debug level should be set
    */
   public void applyHostConfig(Element config, Embedded embedded, boolean debug)
      throws Exception
   {
      if( config == null )
         return;

      // Create an XmlMapper utility
      LoggedXmlMapper mapper = new LoggedXmlMapper(log);
      mapper.setValidating(false);

      mapper.addRule("Server/Service", new ServiceCreateAction());

      // Setup the mapping rules for Connectors
      mapper.addObjectCreate("Server/Service/Connector", "org.apache.catalina.connector.http.HttpConnector", "className");
      mapper.addSetProperties("Server/Service/Connector");
      mapper.addSetNext("Server/Service/Connector", "addConnector", "org.apache.catalina.Connector");
      mapper.addObjectCreate("Server/Service/Connector/Factory", "org.apache.catalina.net.DefaultServerSocketFactory", "className");
      mapper.addSetProperties("Server/Service/Connector/Factory");
      mapper.addSetNext("Server/Service/Connector/Factory", "setFactory", "org.apache.catalina.net.ServerSocketFactory");

      // Setup the mapping rules for Engines
      mapper.addRule("Server/Service/Engine", new EngineCreateAction("org.apache.catalina.core.StandardEngine",
         "className"));
      mapper.addSetProperties("Server/Service/Engine");
      mapper.addRule("Server/Service/Engine", new AddEngineAction());

      // Setup the mapping rules for Engine virtual hosts
      mapper.addObjectCreate("Server/Service/Engine/Host", "org.apache.catalina.core.StandardHost", "className");
      mapper.addSetProperties("Server/Service/Engine/Host");
      mapper.addSetNext("Server/Service/Engine/Host", "addChild", "org.apache.catalina.Container");
      mapper.addCallMethod("Server/Service/Engine/Host/Alias", "addAlias",0);

      // Setup the mapping rules common to both Engine and Host
      String engineOrHost[] = {"Server/Service/Engine", "Server/Service/Engine/Host"};
      for(int i=0;i<engineOrHost.length;i++)
      {
         String prefix=engineOrHost[i];
         // Logger rules
         mapper.addObjectCreate(prefix+"/Logger", "org.jboss.web.catalina.Log4jLogger", "className");
         mapper.addSetProperties(prefix+"/Logger");
         mapper.addSetTop(prefix+"/Logger", "setContainer", "org.apache.catalina.Container");
         mapper.addSetNext(prefix+"/Logger", "setLogger", "org.apache.catalina.Logger");

         // DefaultContext rules
         mapper.addObjectCreate(prefix+"/DefaultContext", "org.apache.catalina.core.StandardDefaultContext", "className");
         mapper.addSetProperties(prefix+"/DefaultContext");
         mapper.addSetTop(prefix+"/DefaultContext", "setParent", "org.apache.catalina.Container");
         mapper.addSetNext(prefix+"/DefaultContext", "addDefaultContext", "org.apache.catalina.core.StandardDefaultContext");
         // add container default context various listeners
         mapper.addCallMethod(prefix + "/DefaultContext/WrapperLifecycle", "addWrapperLifecycle", 0);
         mapper.addCallMethod(prefix + "/DefaultContext/InstanceListener", "addInstanceListener", 0);
         mapper.addCallMethod(prefix + "/DefaultContext/WrapperListener", "addWrapperListener", 0);
         // Allow the context manager to be overriden
         mapper.addObjectCreate(prefix + "/DefaultContext/Manager", "org.apache.catalina.session.StandardManager", "className");
         mapper.addSetProperties(prefix + "/DefaultContext/Manager");
         mapper.addSetNext(prefix + "/DefaultContext/Manager", "setManager", "org.apache.catalina.Manager");


         // Add support for adding valves
         mapper.addObjectCreate(prefix+"/Valve", null, "className");
         mapper.addSetProperties(prefix+"/Valve");
         mapper.addSetTop(prefix+"/Valve", "setContainer", "org.apache.catalina.Container");
         mapper.addSetNext(prefix+"/Valve", "addValve", "org.apache.catalina.Valve");
      }
      // Enable static external contexts
      mapper.addRuleSet(new ContextRuleSet("Server/Service/Engine/Host/"));
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      mapper.addRule("Server/Service/Engine/Host/Context",
                             new SetParentClassLoaderRule(loader));

      // Add the Listener rules to Engine, Engine/Host, and Connector
      String lifeCycling[] = {"Server/Service/Engine", "Server/Service/Engine/Host", "Server/Service/Connector"};
      for(int i = 0; i < lifeCycling.length; i ++)
      {
         String prefix = lifeCycling[i];
         mapper.addObjectCreate(prefix + "/Listener", null, "className");
         mapper.addSetProperties(prefix + "/Listener");
         mapper.addSetNext(prefix + "/Listener", "addLifecycleListener", "org.apache.catalina.LifecycleListener");
      }

      /* Push the Embedded onto the config object stack and transform the DOM
       Config element into a SAX event stream so that the XmlMapper parses the
       configuration
      */
      mapper.push(embedded);
      try
      {
         TransformerFactory tFactory = TransformerFactory.newInstance();
         Transformer transformer = tFactory.newTransformer();
         transformer.transform(new DOMSource(config), new SAXResult(mapper));
         mapper.clear();
      }
      catch(TransformerException e)
      {
         Throwable t = e.getException();
         throw e;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1964.java