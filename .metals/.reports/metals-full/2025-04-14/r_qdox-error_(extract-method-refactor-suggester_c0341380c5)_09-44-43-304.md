error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9658.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9658.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9658.java
text:
```scala
t@@his.canReauthenticate = HttpServletRequest.BASIC_AUTH.equals(authType) || HttpServletRequest.FORM_AUTH.equals(authType) || (username != null && password != null);

/*
 * Copyright 1999-2001,2004 The Apache Software Foundation.
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
package org.jboss.as.web.sso;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.Session;
import org.apache.catalina.authenticator.SingleSignOn;

/**
 * A class that represents entries in the cache of authenticated users.
 *
 * @author Brian E. Stansberry, based on work by Craig R. McClanahan
 * @see SingleSignOn
 */
public class SingleSignOnEntry extends org.apache.catalina.authenticator.SingleSignOnEntry {

    public SingleSignOnEntry(Principal principal, String authType, String username, String password) {
        super(principal, authType, username, password);
    }

    /**
     * Adds a <code>Session</code> to the list of those associated with this SSO.
     *
     * @param sso The <code>SingleSignOn</code> valve that is managing the SSO session.
     * @param session The <code>Session</code> being associated with the SSO.
     * @return <code>true</code> if the given Session was a new addition (i.e. was not previously associated with this entry);
     *         <code>false</code> otherwise.
     */
    synchronized boolean addSession2(SingleSignOn sso, Session session) {
        for (int i = 0; i < sessions.length; i++) {
            if (session == sessions[i]) {
                return false;
            }
        }
        Session[] results = new Session[sessions.length + 1];
        System.arraycopy(sessions, 0, results, 0, sessions.length);
        results[sessions.length] = session;
        sessions = results;
        session.addSessionListener(sso);
        return true;
    }

    /**
     * Removes the given <code>Session</code> from the list of those associated with this SSO.
     *
     * @param session the <code>Session</code> to remove.
     * @return <code>true</code> if the given Session needed to be removed (i.e. was in fact previously associated with this
     *         entry); <code>false</code> otherwise.
     */
    synchronized boolean removeSession2(Session session) {
        if (sessions.length == 0) {
            return false;
        }
        boolean removed = false;
        Session[] nsessions = new Session[sessions.length - 1];
        for (int i = 0, j = 0; i < sessions.length; i++) {
            if (session == sessions[i]) {
                removed = true;
                continue;
            } else if (!removed && i == nsessions.length) {
                // We have tested all our sessions, and have not had to
                // remove any; break loop now so we don't cause an
                // ArrayIndexOutOfBounds on nsessions
                break;
            }
            nsessions[j++] = sessions[i];
        }

        // Only if we removed a session, do we replace our session list
        if (removed) {
            sessions = nsessions;
        }
        return removed;
    }

    /**
     * Sets the <code>Principal</code> that has been authenticated by the SSO.
     */
    void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    /**
     * Returns the number of sessions associated with this SSO, either locally or remotely.
     */
    int getSessionCount() {
        return sessions.length;
    }

    @Override
    public void updateCredentials(Principal principal, String authType, String username, String password) {
        updateCredentials2(principal, authType, username, password);
    }

    /**
     * Updates the SingleSignOnEntry to reflect the latest security information associated with the caller.
     *
     * @param principal the <code>Principal</code> returned by the latest call to <code>Realm.authenticate</code>.
     * @param authType the type of authenticator used (BASIC, CLIENT-CERT, DIGEST or FORM)
     * @param username the username (if any) used for the authentication
     * @param password the password (if any) used for the authentication
     */
    public synchronized boolean updateCredentials2(Principal principal, String authType, String username, String password) {

        boolean changed = (safeEquals(this.principal, principal) || safeEquals(this.authType, authType) || safeEquals(this.username, username) || safeEquals(this.password, password));

        this.principal = principal;
        this.authType = authType;
        this.username = username;
        this.password = password;
        this.canReauthenticate = HttpServletRequest.BASIC_AUTH.equals(authType) || HttpServletRequest.FORM_AUTH.equals(authType);
        return changed;
    }

    private boolean safeEquals(Object a, Object b) {
        return ((a == b) || (a != null && a.equals(b)) || (b != null && b.equals(a)));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9658.java