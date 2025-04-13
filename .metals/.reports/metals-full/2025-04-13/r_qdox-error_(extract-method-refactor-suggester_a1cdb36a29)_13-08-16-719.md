error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9100.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9100.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9100.java
text:
```scala
t@@hrow MailMessages.MESSAGES.outboundSocketBindingNotAvailable(ref);

/*
 *
 *  JBoss, Home of Professional Open Source.
 *  Copyright 2013, Red Hat, Inc., and individual contributors
 *  as indicated by the @author tags. See the copyright.txt file in the
 *  distribution for a full listing of individual contributors.
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this software; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * /
 */

package org.jboss.as.mail.extension;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import org.jboss.as.network.NetworkUtils;

import org.jboss.as.network.OutboundSocketBinding;
import org.jboss.metadata.javaee.spec.MailSessionMetaData;
import org.jboss.metadata.javaee.spec.PropertyMetaData;
import org.jboss.msc.service.StartException;

/**
 * @author <a href="mailto:tomaz.cerar@redhat.com">Tomaz Cerar</a> (c) 2013 Red Hat Inc.
 */
class SessionProviderFactory {
    static SessionProvider create(MailSessionConfig config, final Map<String, OutboundSocketBinding> socketBindings) throws StartException {
        return new ManagedSession(config, socketBindings);
    }

    static SessionProvider create(MailSessionMetaData mailSessionMetaData) {
        return new DirectSessionProvider(mailSessionMetaData);
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

    private static class ManagedSession implements SessionProvider {
        private final Map<String, OutboundSocketBinding> socketBindings;
        private final MailSessionConfig sessionConfig;
        private final Properties properties = new Properties();

        private ManagedSession(MailSessionConfig sessionConfig, Map<String, OutboundSocketBinding> socketBindings) throws StartException {
            this.socketBindings = socketBindings;
            this.sessionConfig = sessionConfig;
            configure();
        }

        /**
         * Configures mail session properties
         *
         * @throws org.jboss.msc.service.StartException
         *          if socket binding could not be found
         * @see {http://javamail.kenai.com/nonav/javadocs/com/sun/mail/smtp/package-summary.html}
         * @see {http://javamail.kenai.com/nonav/javadocs/com/sun/mail/pop3/package-summary.html}
         * @see {http://javamail.kenai.com/nonav/javadocs/com/sun/mail/imap/package-summary.html}
         */
        private void configure() throws StartException {
            if (sessionConfig.getSmtpServer() != null) {
                properties.setProperty("mail.transport.protocol", "smtp");
                setServerProps(properties, sessionConfig.getSmtpServer(), "smtp");
            }
            if (sessionConfig.getImapServer() != null) {
                properties.setProperty("mail.store.protocol", "imap");
                setServerProps(properties, sessionConfig.getImapServer(), "imap");
            }
            if (sessionConfig.getPop3Server() != null) {
                setServerProps(properties, sessionConfig.getPop3Server(), "pop3");
            }
            if (sessionConfig.getCustomServers() != null) {
                configureCustomServers(properties, sessionConfig.getCustomServers());
            }
            if (sessionConfig.getFrom() != null) {
                properties.setProperty("mail.from", sessionConfig.getFrom());
            }
            properties.setProperty("mail.debug", String.valueOf(sessionConfig.isDebug()));
            MailLogger.ROOT_LOGGER.tracef("props: %s", properties);

        }

        private void configureCustomServers(final Properties props, final CustomServerConfig... serverConfigs) throws StartException {
            for (CustomServerConfig serverConfig : serverConfigs) {
                setServerProps(props, serverConfig, serverConfig.getProtocol());
            }
        }

        private void setServerProps(final Properties props, final ServerConfig server, final String protocol) throws StartException {
            if (server.isSslEnabled()) {
                props.setProperty(getPropKey(protocol, "ssl.enable"), "true");
            } else if (server.isTlsEnabled()) {
                props.setProperty(getPropKey(protocol, "starttls.enable"), "true");
            }
            if (server.getCredentials() != null) {
                props.setProperty(getPropKey(protocol, "auth"), "true");
                props.setProperty(getPropKey(protocol, "user"), server.getCredentials().getUsername());
            }
            props.setProperty(getPropKey(protocol, "debug"), String.valueOf(sessionConfig.isDebug()));


            Map<String, String> customProps = server.getProperties();
            if (server.getOutgoingSocketBinding() != null) {
                InetSocketAddress socketAddress = getServerSocketAddress(server);
                props.setProperty(getHostKey(protocol), NetworkUtils.canonize(socketAddress.getAddress().getHostName()));
                props.setProperty(getPortKey(protocol), String.valueOf(socketAddress.getPort()));
            } else {
                String host = customProps.get("host");
                if (host != null && !"".equals(host.trim())) {
                    props.setProperty(getHostKey(protocol), host);
                }
                String port = customProps.get("port");
                if (port != null && !"".equals(port.trim())) {
                    props.setProperty(getPortKey(protocol), port);
                }
            }
            if (customProps != null && !customProps.isEmpty()) {
                for (Map.Entry<String, String> prop : customProps.entrySet()) {
                    if (!props.contains(prop.getKey())) {
                        if (prop.getKey().contains(".")){
                            props.put(prop.getKey(), prop.getValue());
                        }else{
                            props.put(getPropKey(protocol, prop.getKey()), prop.getValue());
                        }
                    }
                }
            }
        }

        private InetSocketAddress getServerSocketAddress(ServerConfig server) throws StartException {
            final String ref = server.getOutgoingSocketBinding();
            final OutboundSocketBinding binding = socketBindings.get(ref);
            if (binding == null) {
                throw MailLogger.ROOT_LOGGER.outboundSocketBindingNotAvailable(ref);
            }
            return new InetSocketAddress(binding.getUnresolvedDestinationAddress(), binding.getDestinationPort());
        }

        @Override
        public Session getSession() {
            final Session session = Session.getInstance(properties, new ManagedPasswordAuthenticator(sessionConfig));
            return session;
        }
    }

    protected static class ManagedPasswordAuthenticator extends Authenticator {
        private MailSessionConfig config;

        public ManagedPasswordAuthenticator(MailSessionConfig sessionConfig) {
            this.config = sessionConfig;
        }

        @Override
        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
            String protocol = getRequestingProtocol();
            Credentials c = null;
            if (MailSubsystemModel.SMTP.equals(protocol) && config.getSmtpServer() != null) {
                c = config.getSmtpServer().getCredentials();
            } else if (MailSubsystemModel.POP3.equals(protocol) && config.getPop3Server() != null) {
                c = config.getPop3Server().getCredentials();
            } else if (MailSubsystemModel.IMAP.equals(protocol) && config.getImapServer() != null) {
                c = config.getImapServer().getCredentials();
            }
            if (c == null) {
                for (CustomServerConfig ssc : config.getCustomServers()) {
                    if (ssc.getProtocol().equals(protocol)) {
                        c = ssc.getCredentials();
                        break;
                    }
                }
            }

            if (c != null) {
                return new javax.mail.PasswordAuthentication(c.getUsername(), c.getPassword());
            }
            return null;
        }
    }

    private static class DirectSessionProvider implements SessionProvider {

        private final MailSessionMetaData metaData;
        private final Properties properties = new Properties();
        private PasswordAuthentication authenticator = null;

        private DirectSessionProvider(MailSessionMetaData metaData) {
            this.metaData = metaData;
            configure();
        }

        private void configure() {
            String protocol = metaData.getTransportProtocol();
            if (protocol == null) {
                protocol = metaData.getStoreProtocol();
            }
            if (protocol == null) {
                protocol = "smtp";
            }
            properties.put(getHostKey(protocol), metaData.getHost());
            if (metaData.getFrom() != null) {
                properties.put(getPropKey(protocol, "from"), metaData.getFrom());
            }
            if (metaData.getProperties() != null) {
                for (PropertyMetaData prop : metaData.getProperties()) {
                    properties.put(prop.getKey(), prop.getValue());
                }
            }

            if (metaData.getUser() != null) {
                authenticator = new PasswordAuthentication(metaData.getUser(), metaData.getPassword());
            }
        }

        @Override
        public Session getSession() {
            return Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return authenticator;
                }
            });
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9100.java