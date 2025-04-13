error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12237.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12237.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12237.java
text:
```scala
private volatile S@@ession session;

package org.jboss.as.mail.extension;


import org.jboss.logging.Logger;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;

import javax.mail.Session;
import java.util.Properties;

/**
 * @author Tomaz Cerar
 * @created 27.7.11 0:14
 */
public class MailSessionService implements Service<Session> {
    private static final Logger log = Logger.getLogger(MailSessionService.class);
    private Session session;
    private final MailSessionConfig sessionConfig;

    public MailSessionService(MailSessionConfig sessionConfig) {
        log.trace("service constructed with config: " + sessionConfig);
        this.sessionConfig = sessionConfig;
    }

    @Override
    public void start(StartContext startContext) throws StartException {
        log.trace("start...");
        session = Session.getDefaultInstance(getProperties());
        log.trace("session is: " + session);
    }

    @Override
    public void stop(StopContext stopContext) {
        log.trace("stop...");
    }

    /*
    Name                    Type        Description
    mail.debug              boolean     The initial debug mode. Default is false.
    mail.from               String      The return email address of the current user, used by the InternetAddress method getLocalAddress.
    mail.mime.address.strict boolean    The MimeMessage class uses the InternetAddress method parseHeader to parse headers in messages. This property controls the strict flag passed to the parseHeader method. The default is true.
    mail.host               String      The default host name of the mail server for both Stores and Transports. Used if the mail.protocol.host property isn't set.
    mail.store.protocol     String      Specifies the default message access protocol. The Session method getStore() returns a Store object that implements this protocol. By default the first Store provider in the configuration files is returned.
    mail.transport.protocol String      Specifies the default message transport protocol. The Session method getTransport() returns a Transport object that implements this protocol. By default the first Transport provider in the configuration files is returned.
    mail.user               String      The default user name to use when connecting to the mail server. Used if the mail.protocol.user property isn't set.
    mail.protocol.class     String      Specifies the fully qualified class name of the provider for the specified protocol. Used in cases where more than one provider for a given protocol exists; this property can be used to specify which provider to use by default. The provider must still be listed in a configuration file.
    mail.protocol.host      String      The host name of the mail server for the specified protocol. Overrides the mail.host property.
    mail.protocol.port      int         The port number of the mail server for the specified protocol. If not specified the protocol's default port number is used.
    mail.protocol.user      String      The user name to use when connecting to mail servers using the specified protocol. Overrides the mail.user property.
     */
    private Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        if (sessionConfig.getSmtpServer() != null) {
            props.put("mail.smtp.host", sessionConfig.getSmtpServer().getAddress());
            props.put("mail.smtp.port", sessionConfig.getSmtpServer().getPort());
        }
        if (sessionConfig.getImapServer() != null) {
            props.put("mail.imap.host", sessionConfig.getImapServer().getAddress());
            props.put("mail.imap.port", sessionConfig.getImapServer().getPort());
        }
        if (sessionConfig.getPop3Server() != null) {
            props.put("mail.pop3.host", sessionConfig.getPop3Server().getAddress());
            props.put("mail.pop3.port", sessionConfig.getPop3Server().getPort());
        }

        props.put("mail.user", sessionConfig.getUsername());
        props.put("mail.debug", sessionConfig.isDebug());
        //todo maybe add mail.from

        log.trace("props: " + props);
        return props;
    }


    @Override
    public Session getValue() throws IllegalStateException, IllegalArgumentException {
        return session;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12237.java