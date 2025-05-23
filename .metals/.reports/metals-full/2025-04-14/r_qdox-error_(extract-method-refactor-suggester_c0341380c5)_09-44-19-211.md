error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8694.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8694.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8694.java
text:
```scala
public b@@oolean _isCallerInRole(final String... roleNames) {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.as.security.service;

import org.jboss.security.AuthenticationManager;
import org.jboss.security.AuthorizationManager;
import org.jboss.security.RunAs;
import org.jboss.security.RunAsIdentity;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SecurityContextUtil;
import org.jboss.security.SubjectInfo;
import org.jboss.security.callbacks.SecurityContextCallbackHandler;
import org.jboss.security.identity.Identity;
import org.jboss.security.identity.Role;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.plugins.SimpleIdentity;
import org.picketbox.factories.SecurityFactory;

import javax.security.auth.Subject;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.security.AccessController.doPrivileged;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class SimpleSecurityManager {
    private ThreadLocalStack<SecurityContext> contexts = new ThreadLocalStack<SecurityContext>();

    private static PrivilegedAction<SecurityContext> securityContext() {
        return new PrivilegedAction<SecurityContext>() {
            public SecurityContext run() {
                return SecurityContextAssociation.getSecurityContext();
            }
        };
    }

    public Principal getCallerPrincipal() {
        final SecurityContext securityContext = doPrivileged(securityContext());
        if (securityContext == null)
            throw new IllegalStateException("No security context established");
        /*
        final Principal principal = getPrincipal(securityContext.getUtil().getSubject());
        */
        Principal principal = securityContext.getIncomingRunAs();
        if (principal == null)
            principal = getPrincipal(securityContext.getSubjectInfo().getAuthenticatedSubject());
        if (principal == null)
            throw new IllegalStateException("No principal available");
        return principal;
    }

    /**
     * Get the Principal given the authenticated Subject. Currently the first principal that is not of type {@code Group} is
     * considered or the single principal inside the CallerPrincipal group.
     *
     * @param subject
     * @return the authenticated principal
     */
    private Principal getPrincipal(Subject subject) {
        Principal principal = null;
        Principal callerPrincipal = null;
        if (subject != null) {
            Set<Principal> principals = subject.getPrincipals();
            if (principals != null && !principals.isEmpty()) {
                for (Principal p : principals) {
                    if (!(p instanceof Group) && principal == null) {
                        principal = p;
                    }
                    if (p instanceof Group) {
                        Group g = Group.class.cast(p);
                        if (g.getName().equals("CallerPrincipal") && callerPrincipal == null) {
                            Enumeration<? extends Principal> e = g.members();
                            if (e.hasMoreElements())
                                callerPrincipal = e.nextElement();
                        }
                    }
                }
            }
        }
        return callerPrincipal == null ? principal : callerPrincipal;
    }

    public boolean isCallerInRole(final String roleName) {
        return _isCallerInRole(roleName);
    }

    /**
     * TODO - Need so separate out the role check for isCallerInRole and actual declarative role checks as context slightly different.
     *
     * @param roleNames
     * @return true if the user is in any one of the roles listed
     */
    private boolean _isCallerInRole(final String... roleNames) {
        final SecurityContext securityContext = doPrivileged(securityContext());
        if (securityContext == null)
            throw new IllegalStateException("No security context established");

        AuthorizationManager am = securityContext.getAuthorizationManager();
        SecurityContextCallbackHandler scb = new SecurityContextCallbackHandler(securityContext);

        RoleGroup roleGroup = am.getSubjectRoles(securityContext.getSubjectInfo().getAuthenticatedSubject(), scb);
        List<Role> roles = roleGroup.getRoles();
        // TODO - Review most performant way.
        Set<String> requiredRoles = new HashSet<String>();
        for (String current : roleNames) {
            requiredRoles.add(current);
        }
        Set<String> actualRoles = new HashSet<String>();
        for (Role current : roles) {
            actualRoles.add(current.getRoleName());
        }

        boolean userNotInRole = Collections.disjoint(requiredRoles, actualRoles);

        return userNotInRole == false;
    }

    /**
     * Must be called from within a privileged action.
     *
     * @param securityDomain
     * @param runAs
     * @param runAsPrincipal
     */
    public void push(final String securityDomain, final String runAs, final String runAsPrincipal) {
        // TODO - Handle a null securityDomain here?  Yes I think so.
        final SecurityContext previous = SecurityContextAssociation.getSecurityContext();
        contexts.push(previous);
        SecurityContext current = SecurityFactory.establishSecurityContext(securityDomain);
        if (previous != null) {
            current.setSubjectInfo(previous.getSubjectInfo());
            current.setIncomingRunAs(previous.getOutgoingRunAs());
        }

        // TODO - Set unauthenticated identity if no auth to occur
        boolean authenticated = authenticate(current);
        if (authenticated == false) {
            // TODO - Better type needed.
            throw new SecurityException("Invalid User");
        }

        if (runAs != null) {
            RunAs runAsIdentity = new RunAsIdentity(runAs, null);
            current.setOutgoingRunAs(runAsIdentity);
        }
    }

    private boolean authenticate(SecurityContext context) {
        SecurityContextUtil util = context.getUtil();
        SubjectInfo subjectInfo = context.getSubjectInfo();
        Subject subject = new Subject();
        Principal principal = util.getUserPrincipal();
        Object credential = util.getCredential();

        boolean authenticated = false;
        if (principal == null) {
            Identity unauthenticatedIdentity = getUnauthenticatedIdentity();
            subjectInfo.addIdentity(unauthenticatedIdentity);
            subject.getPrincipals().add(unauthenticatedIdentity.asPrincipal());
            authenticated = true;
        }

        if (authenticated == false) {
            AuthenticationManager authenticationManager = context.getAuthenticationManager();
            authenticated = authenticationManager.isValid(principal, credential, subject);
        }
        if (authenticated == true) {
            subjectInfo.setAuthenticatedSubject(subject);
        }

        return authenticated;
    }

    // TODO - Base on configuration.
    // Also the spec requires a container representation of an unauthenticated identity so providing
    // at least a default is not optional.
    private Identity getUnauthenticatedIdentity() {
      return new SimpleIdentity("anonymous");
    }

    /**
     * Must be called from within a privileged action.
     */
    public void pop() {
        final SecurityContext sc = contexts.pop();
        SecurityContextAssociation.setSecurityContext(sc);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8694.java