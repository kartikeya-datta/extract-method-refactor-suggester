error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9722.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9722.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9722.java
text:
```scala
t@@hrow new ProgramNotFoundException("invalid path");

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.pgp;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.columba.core.main.MainInterface;
import org.columba.core.plugin.PluginHandlerNotFoundException;
import org.columba.core.pluginhandler.ExternalToolsPluginHandler;
import org.columba.mail.config.PGPItem;
import org.columba.mail.main.MailInterface;
import org.waffel.jscf.JSCFConnection;
import org.waffel.jscf.JSCFDriverManager;
import org.waffel.jscf.JSCFException;
import org.waffel.jscf.gpg.GPGDriver;

/**
 * The <code>JSCFController</code> controls JSCFDrivers and Connections. It
 * chaches for each account the connection to JSCFDrivers. The
 * <code>JSCFController</code> uses the "singleton pattern", which mean, that
 * you should access it, using the <code>getInstcane</code> method.
 * 
 * @author waffel
 */
public class JSCFController
{

  /** JDK 1.4+ logging framework logger, used for logging. */
  private static final Logger LOG = Logger.getLogger("org.columba.mail.pgp");

  private static JSCFController myInstance = null;

  private static Map connectionMap;

  /**
   * Gives a instance of the <code>JSCFController</code> back. If no instance
   * was created before, a new instance will be created.
   * 
   * @return A Instance of <code>JSCFController</code>
   */
  public static JSCFController getInstance ()
  {
    if (myInstance == null)
    {
      myInstance = new JSCFController();
      registerDrivers();
      connectionMap = new Hashtable();
    }

    return myInstance;
  }

  private static void registerDrivers ()
  {
    try
    {
      // at the moment we are only supporting gpg. So let us code hard
      // here the gpg driver
      JSCFDriverManager.registerJSCFDriver(new GPGDriver());
    }
    catch (JSCFException e)
    {
      
      e.printStackTrace();
    }
  }

  /**
   * Creates a new Connection to the gpg driver, if the connection for the given
   * <code>userID</code> are not exists. Properties for the connection are
   * created by using the PGPItem from the <code>AccountItem</code>.
   * Properties like PATH and the GPG USERID are stored for the connection, if
   * the connection are not exists.
   * 
   * @param userID
   *          UserID from which the connection should give back
   * @return a alrady etablished connection for this user or a newly created
   *         connection for this userID, if no connection exists for the userID
   * @throws JSCFException
   *           If there are several Driver problems
   */
  public JSCFConnection getConnection (String userID) throws JSCFException
  {
    PGPItem pgpItem = MailInterface.config.getAccountList().getDefaultAccount()
        .getPGPItem();
    JSCFConnection con = (JSCFConnection) connectionMap.get(userID);

    if (con == null)
    {
      LOG.fine("no connection for userID (" + userID
          + ") found. Creating a new Connection.");
      // let us hard coding the gpg for each connection. Later we should
      // support also other variants (like smime)
      con = JSCFDriverManager.getConnection("jscf:gpg:");

      // getting the path to gpg

      ExternalToolsPluginHandler handler = null;
      String path = null;
      try
      {
        LOG.fine("try to get the handler");
        handler = (ExternalToolsPluginHandler) MainInterface.pluginManager
            .getHandler("org.columba.core.externaltools");
        LOG.fine("recived Handler ... getting path from it");
        path = handler.getLocationOfExternalTool("gpg").getPath();
        LOG.fine("setting path: " + path);
      }
      catch (PluginHandlerNotFoundException e)
      {
        LOG.fine("PluginHandler not found" + e);
        if (MainInterface.DEBUG)
        {
          e.printStackTrace();
        }
      }

      /*
       * if (path == null) { throw new ProgramNotFoundException("invalid path"); }
       */
      Properties props = con.getProperties();
      if (path == null) {
        path = pgpItem.get("PATH");
      }
      props.put("PATH", path);
      if (handler != null) {
        LOG.fine("gpg userId: " + handler.getAttribute("gpg", "id"));
      }
      LOG.info("gpg path: " + props.get("PATH"));
      props.put("USERID", pgpItem.get("id"));
      LOG.info("current gpg userID: " + props.get("USERID"));
      con.setProperties(props);
      connectionMap.put(userID, con);
    }

    return con;
  }

  /**
   * Creates a new JSCFConnection for the current used Account. The current used
   * Account is determind from the AccountItem. This method calls only
   * {@link #getConnection(String)}with the <code>id</code> from the PGPItem.
   * 
   * @return a alrady etablished connection for the current account or a newly
   *         created connection for the current account, if no connection exists
   *         for the current account
   * @throws JSCFException
   *           If there are several Driver problems
   */
  public JSCFConnection getConnection () throws JSCFException
  {
    PGPItem pgpItem = MailInterface.config.getAccountList().getDefaultAccount()
        .getPGPItem();

    return getConnection(pgpItem.get("id"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9722.java