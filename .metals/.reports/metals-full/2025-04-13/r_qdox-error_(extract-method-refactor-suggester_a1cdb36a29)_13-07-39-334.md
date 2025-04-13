error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4037.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4037.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4037.java
text:
```scala
t@@hrow MailMessages.MESSAGES.unknownOutboundSocketBindingDestination(uhe, ref);

package org.jboss.as.mail.extension;


import org.jboss.as.network.OutboundSocketBinding;
import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.inject.MapInjector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;

import javax.mail.Session;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Service that provides a javax.mail.Session.
 *
 * @author Tomaz Cerar
 * @created 27.7.11 0:14
 */
public class MailSessionService implements Service<Session> {
    private static final Logger log = Logger.getLogger(MailSessionService.class);
    private volatile Properties props;
    private final MailSessionConfig config;
    private Map<String, OutboundSocketBinding> socketBindings = new HashMap<String, OutboundSocketBinding>();

    public MailSessionService(MailSessionConfig config) {
        log.tracef("service constructed with config: %s", config);
        this.config = config;
    }


    public void start(StartContext startContext) throws StartException {
        log.trace("start...");
        props = getProperties();
    }


    public void stop(StopContext stopContext) {
        log.trace("stop...");
    }

    Injector<OutboundSocketBinding> getSocketBindingInjector(String name) {
        return new MapInjector<String, OutboundSocketBinding>(socketBindings, name);
    }

    /**
     * Configures mail session properties
     *
     * @return Properties for session
     * @throws StartException if socket binding could not be found
     * @see {http://javamail.kenai.com/nonav/javadocs/com/sun/mail/smtp/package-summary.html}
     * @see {http://javamail.kenai.com/nonav/javadocs/com/sun/mail/pop3/package-summary.html}
     * @see {http://javamail.kenai.com/nonav/javadocs/com/sun/mail/imap/package-summary.html}
     */
    private Properties getProperties() throws StartException {
        Properties props = new Properties();

        if (config.getSmtpServer() != null) {
            props.setProperty("mail.transport.protocol", "smtp");
            setServerProps(props, config.getSmtpServer(), "smtp");
        }
        if (config.getImapServer() != null) {
            setServerProps(props, config.getImapServer(), "imap");
        }
        if (config.getPop3Server() != null) {
            setServerProps(props, config.getPop3Server(), "pop3");
        }
        if (config.getFrom() != null) {
            props.setProperty("mail.from", config.getFrom());
        }
        props.setProperty("mail.debug", String.valueOf(config.isDebug()));
        log.tracef("props: %s", props);
        return props;
    }

    private void setServerProps(Properties props, final MailSessionServer server, final String protocol) throws StartException {
        InetSocketAddress socketAddress = getServerSocketAddress(server);
        props.setProperty(getHostKey(protocol), socketAddress.getAddress().getHostName());
        props.setProperty(getPortKey(protocol), String.valueOf(socketAddress.getPort()));
        if (server.isSslEnabled()) {
            props.setProperty(getPropKey(protocol, "ssl.enable"), "true");
            props.setProperty(getPropKey(protocol, "starttls.enable"), "true");
        }
        if (server.getCredentials() != null) {
            props.setProperty(getPropKey(protocol, "auth"), "true");
            props.setProperty(getPropKey(protocol, "user"), server.getCredentials().getUsername());
        }
        props.setProperty(getPropKey(protocol, "debug"), String.valueOf(config.isDebug()));
    }

    private InetSocketAddress getServerSocketAddress(MailSessionServer server) throws StartException {
        final String ref = server.getOutgoingSocketBinding();
        final OutboundSocketBinding binding = socketBindings.get(ref);
        if (ref == null) {
            throw MailMessages.MESSAGES.outboundSocketBindingNotAvailable(ref);
        }
        final InetAddress destinationAddress;
        try {
            destinationAddress = binding.getDestinationAddress();
        } catch (UnknownHostException uhe) {
            throw MailMessages.MESSAGES.unknownOutboundSocketBindingDesintation(uhe, ref);
        }
        return new InetSocketAddress(destinationAddress, binding.getDestinationPort());
    }


    public Session getValue() throws IllegalStateException, IllegalArgumentException {
        final Session session = Session.getInstance(props, new PasswordAuthentication());
        return session;
    }


    private static String getHostKey(final String protocol) {
        return new StringBuilder("mail.").append(protocol).append(".host").toString();
    }

    private static String getPortKey(final String protocol) {
        return new StringBuilder("mail.").append(protocol).append(".port").toString();
    }

    private static String getPropKey(final String protocol, final String name) {
        return new StringBuilder("mail.").append(protocol).append(".").append(name).toString();
    }

    /*
     * for testing purposes only!
     * @param session
     */
    /*private static void sendMail(Session session) {
        Message msg = new MimeMessage(session);
        try {
            InternetAddress addressFrom = new InternetAddress("tomaz@cerar.net");
            msg.setFrom(addressFrom);
            msg.setRecipients(Message.RecipientType.TO, new Address[]{new InternetAddress("tomaz.cerar@gmail.com")});
            msg.setSubject("Test Jboss AS7 mail subsystem");
            msg.setContent("Testing mail subsystem, loerm ipsum", "text/plain");
            Transport.send(msg);
        } catch (Exception e) {
            log.error("could not send mail", e);
        }
    }*/

    /**
     * @author Tomaz Cerar
     * @created 5.12.11 16:22
     */
    public class PasswordAuthentication extends javax.mail.Authenticator {
        @Override
        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
            String protocol = getRequestingProtocol();
            Credentials c = null;
            if (MailSubsystemModel.SMTP.equals(protocol)) {
                c = config.getSmtpServer().getCredentials();
            } else if (MailSubsystemModel.POP3.equals(protocol)) {
                c = config.getPop3Server().getCredentials();
            } else if (MailSubsystemModel.IMAP.equals(protocol)) {
                c = config.getImapServer().getCredentials();
            }

            if (c != null) {
                return new javax.mail.PasswordAuthentication(c.getUsername(), c.getPassword());
            }
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4037.java