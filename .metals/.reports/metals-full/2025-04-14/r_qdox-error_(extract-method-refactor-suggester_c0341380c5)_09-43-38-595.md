error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8548.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8548.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8548.java
text:
```scala
c@@opySubject.getPrincipals().add(new org.jboss.as.controller.security.InetAddressPrincipal(((InetAddressPrincipal)principal).getInetAddress()));

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.jmx;

import java.io.IOException;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;

import javax.security.auth.Subject;

import org.jboss.as.controller.security.AccessMechanismPrincipal;
import org.jboss.as.core.security.AccessMechanism;
import org.jboss.as.core.security.SubjectUserInfo;
import org.jboss.remoting3.Channel;
import org.jboss.remoting3.security.InetAddressPrincipal;
import org.jboss.remoting3.security.UserInfo;
import org.jboss.remotingjmx.ServerMessageInterceptor;
import org.jboss.remotingjmx.ServerMessageInterceptorFactory;
import org.wildfly.security.manager.WildFlySecurityManager;

/**
 * A {@link ServerMessageInterceptorFactory} responsible for supplying a {@link ServerMessageInterceptor} for associating the
 * Subject of the remote user with the current request.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
class ServerInterceptorFactory implements ServerMessageInterceptorFactory {

    @Override
    public ServerMessageInterceptor create(Channel channel) {
        return new Interceptor(channel);
    }

    private static class Interceptor implements ServerMessageInterceptor {

        private final Channel channel;

        private Interceptor(final Channel channel) {
            this.channel = channel;
        }

        @Override
        public void handleEvent(final Event event) throws IOException {
            UserInfo userInfo = channel.getConnection().getUserInfo();
            if (userInfo instanceof SubjectUserInfo) {
                final Subject subject = ((SubjectUserInfo) userInfo).getSubject();
                Subject useSubject = subject;
                //TODO find a better place for this https://issues.jboss.org/browse/WFLY-1852
                PrivilegedAction<Subject> copyAction = new PrivilegedAction<Subject>() {
                    @Override
                    public Subject run() {
                        final Subject copySubject = new Subject();
                        copySubject.getPrincipals().addAll(subject.getPrincipals());
                        copySubject.getPrivateCredentials().addAll(subject.getPrivateCredentials());
                        copySubject.getPublicCredentials().addAll(subject.getPublicCredentials());
                        //Add the remote address and the access mechanism
                        Collection<Principal> principals = channel.getConnection().getPrincipals();
                        for (Principal principal : principals) {
                            if (principal instanceof InetAddressPrincipal) {
                                //TODO decide if we should use the remoting principal or not
                                copySubject.getPrincipals().add(new org.jboss.as.controller.security.InetAddressPrincipal((InetAddressPrincipal)principal));
                                break;
                            }
                        }
                        copySubject.getPrincipals().add(new AccessMechanismPrincipal(AccessMechanism.JMX));
                        copySubject.setReadOnly();
                        return copySubject;                            }
                };


                useSubject = WildFlySecurityManager.isChecking() ? AccessController.doPrivileged(copyAction) : copyAction.run();

                try {
                    Subject.doAs(useSubject, new PrivilegedExceptionAction<Void>() {

                        @Override
                        public Void run() throws IOException {
                            event.run();

                            return null;
                        }
                    });
                } catch (PrivilegedActionException e) {
                    Exception cause = e.getException();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    } else {
                        throw new IOException(cause);
                    }
                }

            } else {
                event.run();
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8548.java