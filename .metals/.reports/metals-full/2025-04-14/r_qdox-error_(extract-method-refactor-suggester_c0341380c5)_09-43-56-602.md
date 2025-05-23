error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8397.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8397.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8397.java
text:
```scala
public v@@oid activate() {

/*
 * Copyright 1999,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.log4j.net;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Properties;

import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;


/**
 * A simple appender that publishes events to a JMS Topic. The events
 * are serialized and transmitted as JMS message type {@link
 * javax.jms.ObjectMessage}.

 * <p>JMS {@link javax.jms.Topic topics} and {@link javax.jms.TopicConnectionFactory topic
 * connection factories} are administered objects that are retrieved
 * using JNDI messaging which in turn requires the retreival of a JNDI
 * {@link Context}.

 * <p>There are two common methods for retrieving a JNDI {@link
 * Context}. If a file resource named <em>jndi.properties</em> is
 * available to the JNDI API, it will use the information found
 * therein to retrieve an initial JNDI context. To obtain an initial
 * context, your code will simply call:

   <pre>
   InitialContext jndiContext = new InitialContext();
   </pre>

 * <p>Calling the no-argument <code>InitialContext()</code> method
 * will also work from within Enterprise Java Beans (EJBs) because it
 * is part of the EJB contract for application servers to provide each
 * bean an environment naming context (ENC).

 * <p>In the second approach, several predetermined properties are set
 * and these properties are passed to the <code>InitialContext</code>
 * contructor to connect to the naming service provider. For example,
 * to connect to JBoss naming service one would write:

<pre>
   Properties env = new Properties( );
   env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
   env.put(Context.PROVIDER_URL, "jnp://hostname:1099");
   env.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
   InitialContext jndiContext = new InitialContext(env);
</pre>

   * where <em>hostname</em> is the host where the JBoss applicaiton
   * server is running.
   *
   * <p>To connect to the the naming service of Weblogic application
   * server one would write:

<pre>
   Properties env = new Properties( );
   env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
   env.put(Context.PROVIDER_URL, "t3://localhost:7001");
   InitialContext jndiContext = new InitialContext(env);
</pre>

  * <p>Other JMS providers will obviously require different values.
  *
  * The initial JNDI context can be obtained by calling the
  * no-argument <code>InitialContext()</code> method in EJBs. Only
  * clients running in a separate JVM need to be concerned about the
  * <em>jndi.properties</em> file and calling {@link
  * InitialContext#InitialContext()} or alternatively correctly
  * setting the different properties before calling {@link
  * InitialContext#InitialContext(java.util.Hashtable)} method.


   @author Ceki G&uuml;lc&uuml; */
public class JMSAppender extends AppenderSkeleton {
  
  static int SUCCESSIVE_FAILURE_LIMIT = 3;
  
  String securityPrincipalName;
  String securityCredentials;
  String initialContextFactoryName;
  String urlPkgPrefixes;
  String providerURL;
  String topicBindingName;
  String tcfBindingName;
  String userName;
  String password;
  boolean locationInfo;
  TopicConnection topicConnection;
  TopicSession topicSession;
  TopicPublisher topicPublisher;

  boolean inOrder = false;
  int successiveFailureCount = 0;
  
  public JMSAppender() {
  }

  /**
     The <b>TopicConnectionFactoryBindingName</b> option takes a
     string value. Its value will be used to lookup the appropriate
     <code>TopicConnectionFactory</code> from the JNDI context.
   */
  public void setTopicConnectionFactoryBindingName(String tcfBindingName) {
    this.tcfBindingName = tcfBindingName;
  }

  /**
     Returns the value of the <b>TopicConnectionFactoryBindingName</b> option.
   */
  public String getTopicConnectionFactoryBindingName() {
    return tcfBindingName;
  }

  /**
     The <b>TopicBindingName</b> option takes a
     string value. Its value will be used to lookup the appropriate
     <code>Topic</code> from the JNDI context.
   */
  public void setTopicBindingName(String topicBindingName) {
    this.topicBindingName = topicBindingName;
  }

  /**
     Returns the value of the <b>TopicBindingName</b> option.
   */
  public String getTopicBindingName() {
    return topicBindingName;
  }

  /**
   * Returns value of the <b>LocationInfo</b> property which determines whether
   * caller's location info is sent to the remote subscriber.
   * */
  public boolean getLocationInfo() {
    return locationInfo;
  }

  /**
   *  Options are activated and become effective only after calling
   *  this method.*/
  public void activateOptions() {
    TopicConnectionFactory topicConnectionFactory;

    try {
      Context jndi;

      getLogger().debug("Getting initial context.");
      if (initialContextFactoryName != null) {
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactoryName);
        if (providerURL != null) {
          env.put(Context.PROVIDER_URL, providerURL);
        } else {
          getLogger().warn(
            "You have set InitialContextFactoryName option but not the "
            + "ProviderURL. This is likely to cause problems.");
        }
        if (urlPkgPrefixes != null) {
          env.put(Context.URL_PKG_PREFIXES, urlPkgPrefixes);
        }

        if (securityPrincipalName != null) {
          env.put(Context.SECURITY_PRINCIPAL, securityPrincipalName);
          if (securityCredentials != null) {
            env.put(Context.SECURITY_CREDENTIALS, securityCredentials);
          } else {
            getLogger().warn(
              "You have set SecurityPrincipalName option but not the "
              + "SecurityCredentials. This is likely to cause problems.");
          }
        }
        jndi = new InitialContext(env);
      } else {
        jndi = new InitialContext();
      }

      getLogger().debug("Looking up [{}]", tcfBindingName);
      topicConnectionFactory =
        (TopicConnectionFactory) lookup(jndi, tcfBindingName);
      getLogger().debug("About to create TopicConnection.");
      if (userName != null) {
        this.topicConnection =
          topicConnectionFactory.createTopicConnection(userName, password);
      } else {
        this.topicConnection = topicConnectionFactory.createTopicConnection();
      }

      getLogger().debug(
        "Creating TopicSession, non-transactional, "
        + "in AUTO_ACKNOWLEDGE mode.");
      this.topicSession =
        topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

      getLogger().debug("Looking up topic name [" + topicBindingName + "].");
      Topic topic = (Topic) lookup(jndi, topicBindingName);

      getLogger().debug("Creating TopicPublisher.");
      this.topicPublisher = topicSession.createPublisher(topic);

      getLogger().debug("Starting TopicConnection.");
      topicConnection.start();

      jndi.close();
    } catch (Exception e) {
      getLogger().error(
       "Error while activating options for appender named [" + name + "].", e);
    }
    
    if (this.topicConnection != null && this.topicSession != null && this.topicPublisher == null) {
      inOrder = true;
    } else {
      inOrder = false;
    }
  }

  protected Object lookup(Context ctx, String name) throws NamingException {
    try {
      return ctx.lookup(name);
    } catch (NameNotFoundException e) {
      getLogger().error("Could not find name [" + name + "].");
      throw e;
    }
  }

  /**
     Close this JMSAppender. Closing releases all resources used by the
     appender. A closed appender cannot be re-opened. */
  public synchronized void close() {
    // The synchronized modifier avoids concurrent append and close operations
    if (this.closed) {
      return;
    }

    getLogger().debug("Closing appender [" + name + "].");
    this.closed = true;

    try {
      if (topicSession != null) {
        topicSession.close();
      }
      if (topicConnection != null) {
        topicConnection.close();
      }
    } catch (Exception e) {
      getLogger().error("Error while closing JMSAppender [" + name + "].", e);
    }

    // Help garbage collection
    topicPublisher = null;
    topicSession = null;
    topicConnection = null;
  }

  /**
     This method called by {@link AppenderSkeleton#doAppend} method to
     do most of the real appending work.  */
  public void append(LoggingEvent event) {
    if (!inOrder) {
      return;
    }

    try {
      ObjectMessage msg = topicSession.createObjectMessage();
      if (locationInfo) {
        event.getLocationInformation();
      }
      msg.setObject(event);
      topicPublisher.publish(msg);
      successiveFailureCount = 0;
    } catch (Exception e) {
      successiveFailureCount++;
      if(successiveFailureCount > SUCCESSIVE_FAILURE_LIMIT) {
        inOrder = false;
      }
      getLogger().error(
        "Could not publish message in JMSAppender [" + name + "].", e);
   
      
    }
  }

  /**
   * Returns the value of the <b>InitialContextFactoryName</b> option.
   * See {@link #setInitialContextFactoryName} for more details on the
   * meaning of this option.
   * */
  public String getInitialContextFactoryName() {
    return initialContextFactoryName;
  }

  /**
   * Setting the <b>InitialContextFactoryName</b> method will cause
   * this <code>JMSAppender</code> instance to use the {@link
   * InitialContext#InitialContext(Hashtable)} method instead of the
   * no-argument constructor. If you set this option, you should also
   * at least set the <b>ProviderURL</b> option.
   *
   * <p>See also {@link #setProviderURL(String)}.
   * */
  public void setInitialContextFactoryName(String initialContextFactoryName) {
    this.initialContextFactoryName = initialContextFactoryName;
  }

  public String getProviderURL() {
    return providerURL;
  }

  public void setProviderURL(String providerURL) {
    this.providerURL = providerURL;
  }

  String getURLPkgPrefixes() {
    return urlPkgPrefixes;
  }

  public void setURLPkgPrefixes(String urlPkgPrefixes) {
    this.urlPkgPrefixes = urlPkgPrefixes;
  }

  public String getSecurityCredentials() {
    return securityCredentials;
  }

  public void setSecurityCredentials(String securityCredentials) {
    this.securityCredentials = securityCredentials;
  }

  public String getSecurityPrincipalName() {
    return securityPrincipalName;
  }

  public void setSecurityPrincipalName(String securityPrincipalName) {
    this.securityPrincipalName = securityPrincipalName;
  }

  public String getUserName() {
    return userName;
  }

  /**
   * The user name to use when {@link
   * javax.jms.TopicConnectionFactory#createTopicConnection(String, String)}
   * creating a topic session}.  If you set this option, you should
   * also set the <b>Password</b> option. See {@link
   * #setPassword(String)}.
   * */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  /**
   * The paswword to use when creating a topic session.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * If true, the information sent to the remote subscriber will include
   * caller's location information. Due to performance concerns, by default no
   * location information is sent to the subscriber.
   * */
  public void setLocationInfo(boolean locationInfo) {
    this.locationInfo = locationInfo;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8397.java