error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15101.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15101.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15101.java
text:
```scala
c@@txMap.put("principal", userPrincipal != null ? userPrincipal.getName() : "null");

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

import static java.security.AccessController.doPrivileged;

import java.lang.reflect.Method;
import java.security.CodeSource;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.security.acl.Group;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.Subject;

import org.jboss.as.controller.security.ServerSecurityManager;
import org.jboss.as.controller.security.SubjectUserInfo;
import org.jboss.as.domain.management.security.PasswordCredential;
import org.jboss.as.security.SecurityMessages;
import org.jboss.as.security.remoting.RemotingConnectionCredential;
import org.jboss.as.security.remoting.RemotingConnectionPrincipal;
import org.jboss.metadata.javaee.spec.SecurityRolesMetaData;
import org.jboss.remoting3.Connection;
import org.jboss.remoting3.security.UserInfo;
import org.jboss.security.AuthenticationManager;
import org.jboss.security.AuthorizationManager;
import org.jboss.security.ISecurityManagement;
import org.jboss.security.RunAs;
import org.jboss.security.RunAsIdentity;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SecurityContextFactory;
import org.jboss.security.SecurityContextUtil;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.SubjectInfo;
import org.jboss.security.audit.AuditEvent;
import org.jboss.security.audit.AuditLevel;
import org.jboss.security.audit.AuditManager;
import org.jboss.security.authorization.resources.EJBResource;
import org.jboss.security.callbacks.SecurityContextCallbackHandler;
import org.jboss.security.identity.Identity;
import org.jboss.security.identity.Role;
import org.jboss.security.identity.RoleGroup;
import org.jboss.security.identity.plugins.SimpleIdentity;
import org.jboss.security.identity.plugins.SimpleRoleGroup;
import org.jboss.security.javaee.AbstractEJBAuthorizationHelper;
import org.jboss.security.javaee.SecurityHelperFactory;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class SimpleSecurityManager implements ServerSecurityManager {
    private ThreadLocalStack<SecurityContext> contexts = new ThreadLocalStack<SecurityContext>();

    private ISecurityManagement securityManagement = null;

    private PrivilegedAction<SecurityContext> securityContext() {
        return new PrivilegedAction<SecurityContext>() {
            public SecurityContext run() {
                return SecurityContextAssociation.getSecurityContext();
            }
        };
    }

    private SecurityContext establishSecurityContext(final String securityDomain) {
        // Do not use SecurityFactory.establishSecurityContext, its static init is broken.
        try {
            final SecurityContext securityContext = SecurityContextFactory.createSecurityContext(securityDomain);
            if(securityManagement == null)
                throw SecurityMessages.MESSAGES.securityManagementNotInjected();
            securityContext.setSecurityManagement(securityManagement);
            SecurityContextAssociation.setSecurityContext(securityContext);
            return securityContext;
        } catch (Exception e) {
            throw SecurityMessages.MESSAGES.securityException(e);
        }
    }

    public void setSecurityManagement(ISecurityManagement iSecurityManagement){
        securityManagement = iSecurityManagement;
    }

    public Principal getCallerPrincipal() {
        final SecurityContext securityContext = doPrivileged(securityContext());
        if (securityContext == null) {
            return getUnauthenticatedIdentity().asPrincipal();
        }
        /*
         * final Principal principal = getPrincipal(securityContext.getUtil().getSubject());
         */
        Principal principal = securityContext.getIncomingRunAs();
        if (principal == null)
            principal = getPrincipal(securityContext.getSubjectInfo().getAuthenticatedSubject());
        if (principal == null)
            return getUnauthenticatedIdentity().asPrincipal();
        return principal;
    }

    public Subject getSubject() {
        final SecurityContext securityContext = doPrivileged(securityContext());
        if (securityContext != null) {
            return securityContext.getSubjectInfo().getAuthenticatedSubject();
        }
        return null;
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

    /**
     *
     * @param incommingMappedRoles The principal vs roles mapping (if any). Can be null.
     * @param roleLinks The role link map where the key is a alias role name and the value is the collection of
     *                  role names, that alias represents. Can be null.
     * @param roleNames The role names for which the caller is being checked for
     * @return true if the user is in <b>any</b> one of the <code>roleNames</code>. Else returns false
     */
    public boolean isCallerInRole(final Object incommingMappedRoles, final Map<String, Collection<String>> roleLinks,
                                  final String... roleNames) {
        final SecurityRolesMetaData mappedRoles = (SecurityRolesMetaData) incommingMappedRoles;
        final SecurityContext securityContext = doPrivileged(securityContext());
        if (securityContext == null) {
            return false;
        }

        RoleGroup roleGroup = null;

        RunAs runAs = securityContext.getIncomingRunAs();
        if (runAs != null && runAs instanceof RunAsIdentity) {
            RunAsIdentity runAsIdentity = (RunAsIdentity) runAs;
            roleGroup = runAsIdentity.getRunAsRolesAsRoleGroup();
        } else {
            AuthorizationManager am = securityContext.getAuthorizationManager();
            SecurityContextCallbackHandler scb = new SecurityContextCallbackHandler(securityContext);

            roleGroup = am.getSubjectRoles(securityContext.getSubjectInfo().getAuthenticatedSubject(), scb);
        }

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
        // add mapped roles
        if (mappedRoles != null) {
            Principal callerPrincipal = getCallerPrincipal();
            Set<String> mapped = mappedRoles.getSecurityRoleNamesByPrincipal(callerPrincipal.getName());
            if (mapped != null) {
                actualRoles.addAll(mapped);
            }
        }
        // if the actual roles matches any of the required roles, then return true
        if (!Collections.disjoint(requiredRoles, actualRoles)) {
            return true;
        }
        // we did not have a match between the required roles and the actual roles.
        // let's now get the alias role names (if any) for each of the actual role
        // and see if any of those aliases of the actual roles matches the required roles
        if (roleLinks != null) {
            for (final String actualRole : actualRoles) {
                // get aliases (if any) for actual role
                final Set<String> aliases = this.getRoleAliases(actualRole, roleLinks);
                // if there's a match between the required role and an alias of an actual role, then
                // return true indicating that the caller is in one of the required roles
                if (!Collections.disjoint(requiredRoles, aliases)) {
                    return true;
                }
            }
        }
        // caller is not in any of the required roles
        return false;
    }

    public boolean authorize(String ejbName, CodeSource ejbCodeSource, String ejbMethodIntf, Method ejbMethod, Set<Principal> methodRoles, String contextID) {

        final SecurityContext securityContext = doPrivileged(securityContext());
        if (securityContext == null) {
            return false;
        }

        EJBResource resource = new EJBResource(new HashMap<String, Object>());
        resource.setEjbName(ejbName);
        resource.setEjbMethod(ejbMethod);
        resource.setEjbMethodInterface(ejbMethodIntf);
        resource.setEjbMethodRoles(new SimpleRoleGroup(methodRoles));
        resource.setCodeSource(ejbCodeSource);
        resource.setPolicyContextID(contextID);
        resource.setCallerRunAsIdentity(securityContext.getIncomingRunAs());
        resource.setCallerSubject(securityContext.getUtil().getSubject());
        Principal userPrincipal = securityContext.getUtil().getUserPrincipal();
        resource.setPrincipal(userPrincipal);

        try {
            AbstractEJBAuthorizationHelper helper = SecurityHelperFactory.getEJBAuthorizationHelper(securityContext);
            return helper.authorize(resource);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Must be called from within a privileged action.
     *
     * @param securityDomain
     * @param runAs
     * @param runAsPrincipal
     * @param extraRoles
     */
    public void push(final String securityDomain) {
        // TODO - Handle a null securityDomain here? Yes I think so.
        final SecurityContext previous = SecurityContextAssociation.getSecurityContext();
        contexts.push(previous);
        SecurityContext current = establishSecurityContext(securityDomain);
        if (previous != null) {
            current.setSubjectInfo(previous.getSubjectInfo());
            current.setIncomingRunAs(previous.getOutgoingRunAs());
        }

        RunAs currentRunAs = current.getIncomingRunAs();
        boolean trusted = currentRunAs != null && currentRunAs instanceof RunAsIdentity;

        if (trusted == false) {
            /*
             * We should only be switching to a context based on an identity from the Remoting connection if we don't already
             * have a trusted identity - this allows for beans to reauthenticate as a different identity.
             */
            if (SecurityActions.remotingContextIsSet()) {
                // In this case the principal and credential will not have been set to set some random values.
                SecurityContextUtil util = current.getUtil();

                Connection connection = SecurityActions.remotingContextGetConnection();
                UserInfo userInfo = connection.getUserInfo();
                Principal p = null;
                Object credential = null;

                if (userInfo instanceof SubjectUserInfo) {
                    SubjectUserInfo sinfo = (SubjectUserInfo) userInfo;
                    Subject subject = sinfo.getSubject();

                    Set<PasswordCredential> pcSet = subject.getPrivateCredentials(PasswordCredential.class);
                    if (pcSet.size() > 0) {
                        PasswordCredential pc = pcSet.iterator().next();
                        p = new SimplePrincipal(pc.getUserName());
                        credential = new String(pc.getCredential());
                    }
                }

                if (p == null || credential == null) {
                    p = new RemotingConnectionPrincipal(connection);
                    credential = new RemotingConnectionCredential(connection);
                }
                SecurityActions.remotingContextClear();

                util.createSubjectInfo(p, credential, null);
            }
        }
    }

    public void push(final String securityDomain, String userName, char[] password, final Subject subject) {
        final SecurityContext previous = SecurityContextAssociation.getSecurityContext();
        contexts.push(previous);
        SecurityContext current = establishSecurityContext(securityDomain);
        if (previous != null) {
            current.setSubjectInfo(previous.getSubjectInfo());
            current.setIncomingRunAs(previous.getOutgoingRunAs());
        }

        RunAs currentRunAs = current.getIncomingRunAs();
        boolean trusted = currentRunAs != null && currentRunAs instanceof RunAsIdentity;

        if (trusted == false) {
            SecurityContextUtil util = current.getUtil();
            util.createSubjectInfo(new SimplePrincipal(userName), new String(password), subject);
        }
    }

    public void authenticate() {
        authenticate(null, null, null);
    }

    public void authenticate(final String runAs, final String runAsPrincipal, final Set<String> extraRoles) {
        SecurityContext context = SecurityContextAssociation.getSecurityContext();
        SecurityContextUtil util = context.getUtil();

        Object credential = util.getCredential();
        Subject subject = null;
        if (credential instanceof RemotingConnectionCredential) {
            subject = ((RemotingConnectionCredential) credential).getSubject();
        }

        if (authenticate(context, subject) == false) {
            throw SecurityMessages.MESSAGES.invalidUserException();
        }

        SecurityContext previous = contexts.peek();
        if (runAs != null) {
            RunAs runAsIdentity = new RunAsIdentity(runAs, runAsPrincipal, extraRoles);
            context.setOutgoingRunAs(runAsIdentity);
        } else if (previous != null && previous.getOutgoingRunAs() != null) {
            // Ensure the propagation continues.
            context.setOutgoingRunAs(previous.getOutgoingRunAs());
        }
    }

    private boolean authenticate(SecurityContext context, Subject subject) {
        SecurityContextUtil util = context.getUtil();
        SubjectInfo subjectInfo = context.getSubjectInfo();
        if (subject == null) {
            subject = new Subject();
        }
        Principal principal = util.getUserPrincipal();
        Principal auditPrincipal = principal;
        Object credential = util.getCredential();
        Identity unauthenticatedIdentity = null;

        boolean authenticated = false;
        if (principal == null) {
            unauthenticatedIdentity = getUnauthenticatedIdentity();
            subjectInfo.addIdentity(unauthenticatedIdentity);
            auditPrincipal = unauthenticatedIdentity.asPrincipal();
            subject.getPrincipals().add(auditPrincipal);
            authenticated = true;
        }

        if (authenticated == false) {
            AuthenticationManager authenticationManager = context.getAuthenticationManager();
            authenticated = authenticationManager.isValid(principal, credential, subject);
        }
        if (authenticated == true) {
            subjectInfo.setAuthenticatedSubject(subject);
        }

        AuditManager auditManager = context.getAuditManager();
        if (auditManager != null) {
            audit(authenticated ? AuditLevel.SUCCESS : AuditLevel.FAILURE, auditManager, auditPrincipal);
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

    /**
     * Returns the alias role names for the passed <code>roleName</code>. Returns an empty set if the passed
     * role name doesn't have any aliases
     *
     * @param roleName The role name
     * @param roleLinks The role link map where the key is a alias role name and the value is the collection of
     *                  role names, that alias represents
     * @return
     */
    private Set<String> getRoleAliases(final String roleName, final Map<String, Collection<String>> roleLinks) {
        if (roleLinks == null || roleLinks.isEmpty()) {
            return Collections.emptySet();
        }
        final Set<String> aliases = new HashSet<String>();
        for (final Map.Entry<String, Collection<String>> roleLinkEntry : roleLinks.entrySet()) {
            final String aliasRoleName = roleLinkEntry.getKey();
            final Collection<String> realRoleNames = roleLinkEntry.getValue();
            if (realRoleNames != null && realRoleNames.contains(roleName)) {
                aliases.add(aliasRoleName);
            }
        }
        return aliases;
    }

    /**
     * Sends information to the {@code AuditManager}.
     * @param level
     * @param auditManager
     * @param userPrincipal
     * @param entries
     */
    private void audit(String level, AuditManager auditManager, Principal userPrincipal) {
        AuditEvent auditEvent = new AuditEvent(AuditLevel.SUCCESS);
        Map<String, Object> ctxMap = new HashMap<String, Object>();
        ctxMap.put("principal", userPrincipal != null ? userPrincipal : "null");
        ctxMap.put("Source", getClass().getCanonicalName());
        ctxMap.put("Action", "authentication");
        auditEvent.setContextMap(ctxMap);
        auditManager.audit(auditEvent);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15101.java