error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1893.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1893.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1893.java
text:
```scala
n@@ewCallbacks[newCallbacks.length - 1] = subjectCallBack;

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.as.domain.http.server.security;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.jboss.as.controller.security.SubjectUserInfo;
import org.jboss.as.domain.management.SecurityRealm;
import org.jboss.as.domain.management.security.DomainCallbackHandler;
import org.jboss.as.domain.management.security.RealmUser;
import org.jboss.as.domain.management.security.SubjectCallback;
import org.jboss.as.domain.management.security.SubjectSupplemental;

/**
 * The AuthenticationProvider to make available the AuthorizingCallbackHandler to the the authenticators.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class AuthenticationProvider {

    private final SecurityRealm securityRealm;

    public AuthenticationProvider(final SecurityRealm securityRealm) {
        this.securityRealm = securityRealm;
    }

    AuthorizingCallbackHandler getCallbackHandler() {
        final DomainCallbackHandler callbackHandler = securityRealm.getCallbackHandler();

        return new AuthorizingCallbackHandler() {

            Subject subject;

            @Override
            public Class<Callback>[] getSupportedCallbacks() {
                return callbackHandler.getSupportedCallbacks();
            }

            @Override
            public boolean isReady() {
                return callbackHandler.isReady();
            }

            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                if (contains(SubjectCallback.class, callbackHandler.getSupportedCallbacks())) {
                    Callback[] newCallbacks = new Callback[callbacks.length + 1];
                    System.arraycopy(callbacks, 0, newCallbacks, 0, callbacks.length);
                    SubjectCallback subjectCallBack = new SubjectCallback();
                    newCallbacks[newCallbacks.length] = subjectCallBack;
                    callbackHandler.handle(newCallbacks);
                    subject = subjectCallBack.getSubject();
                } else {
                    callbackHandler.handle(callbacks);
                }
            }

            @Override
            public SubjectUserInfo createSubjectUserInfo(Principal userPrincipal) throws IOException {
                Subject subject = this.subject == null ? new Subject() : this.subject;
                Collection<Principal> allPrincipals = subject.getPrincipals();
                allPrincipals.add(userPrincipal);
                allPrincipals.add(new RealmUser(securityRealm.getName(), userPrincipal.getName()));

                SubjectSupplemental subjectSupplemental = securityRealm.getSubjectSupplemental();
                if (subjectSupplemental != null) {
                    subjectSupplemental.supplementSubject(subject);
                }

                return new HttpSubjectUserInfo(subject);
            }
        };

    }

    private static boolean contains(Class clazz, Class<Callback>[] classes) {
        for (Class<Callback> current : classes) {
            if (current.equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    private static class HttpSubjectUserInfo implements SubjectUserInfo {

        private final String userName;
        private final Subject subject;

        private HttpSubjectUserInfo(Subject subject) {
            this.subject = subject;
            Set<RealmUser> users = subject.getPrincipals(RealmUser.class);
            userName = users.isEmpty() ? null : users.iterator().next().getName();
        }

        public String getUserName() {
            return userName;
        }

        public Collection<Principal> getPrincipals() {
            return subject.getPrincipals();
        }

        public Subject getSubject() {
            return subject;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1893.java