error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1237.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1237.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1237.java
text:
```scala
s@@ecurityContext.authenticationComplete(account, MECHANISM_NAME, false);

package org.wildfly.extension.undertow.security.jaspi;

import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.SecurityContext;
import io.undertow.security.idm.Account;
import io.undertow.security.idm.PasswordCredential;
import io.undertow.server.ExchangeCompletionListener;
import io.undertow.server.HttpServerExchange;
import io.undertow.servlet.handlers.ServletRequestContext;
import io.undertow.util.AttachmentKey;
import org.jboss.security.SecurityContextAssociation;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.callback.JBossCallbackHandler;
import org.jboss.security.auth.message.GenericMessageInfo;
import org.jboss.security.plugins.auth.JASPIServerAuthenticationManager;
import org.wildfly.extension.undertow.security.AccountImpl;

import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.callback.PasswordValidationCallback;
import javax.servlet.ServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.wildfly.extension.undertow.UndertowLogger.ROOT_LOGGER;
import static org.wildfly.extension.undertow.UndertowMessages.MESSAGES;

/**
 * <p>{@link AuthenticationMechanism} implementation that enables JASPI-based authentication.</p>
 *
 * @author Pedro Igor
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public class JASPIAuthenticationMechanism implements AuthenticationMechanism {

    private static final String JASPI_HTTP_SERVLET_LAYER = "HttpServlet";
    private static final String MECHANISM_NAME = "JASPI";

    public static final AttachmentKey<HttpServerExchange> HTTP_SERVER_EXCHANGE_ATTACHMENT_KEY = AttachmentKey.create(HttpServerExchange.class);
    public static final AttachmentKey<SecurityContext> SECURITY_CONTEXT_ATTACHMENT_KEY = AttachmentKey.create(SecurityContext.class);

    private final String securityDomain;

    public JASPIAuthenticationMechanism(final String securityDomain) {
        this.securityDomain = securityDomain;
    }

    @Override
    public AuthenticationMechanismOutcome authenticate(final HttpServerExchange exchange, final SecurityContext securityContext) {
        final ServletRequestContext requestContext = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY);
        final JASPIServerAuthenticationManager sam = createJASPIAuthenticationManager();
        final GenericMessageInfo messageInfo = createMessageInfo(exchange, securityContext);
        final String applicationIdentifier = buildApplicationIdentifier(requestContext);
        final JASPICallbackHandler cbh = new JASPICallbackHandler();

        ROOT_LOGGER.debugf("validateRequest for layer [%s] and applicationContextIdentifier [%s]", JASPI_HTTP_SERVLET_LAYER, applicationIdentifier);

        if (sam.isValid(messageInfo, new Subject(), JASPI_HTTP_SERVLET_LAYER, applicationIdentifier, cbh)) {
            Account account = securityContext.getAuthenticatedAccount();

            // if the there is no account at this time, we check if the JASPI authentication have populated the callbackhandler
            // with user information.
            if (account == null) {
                ROOT_LOGGER.debug("Creating account with credentials from JASPI callbackhandler.");
                account = createAccount(cbh);
                securityContext.authenticationComplete(account, MECHANISM_NAME);
            } else {
                ROOT_LOGGER.debug("Account already setup from JASPI modules.");
            }

            if (account == null) {
                securityContext.authenticationFailed("JASPI authentication failed.", MECHANISM_NAME);
            }
        }

        secureResponse(exchange, securityContext, sam, messageInfo, cbh);

        return securityContext.getAuthenticatedAccount() != null ? AuthenticationMechanismOutcome.AUTHENTICATED : AuthenticationMechanismOutcome.NOT_AUTHENTICATED;
    }

    @Override
    public ChallengeResult sendChallenge(final HttpServerExchange exchange, final SecurityContext securityContext) {
        return new ChallengeResult(true);
    }

    private boolean isSecureResponse(final ServletRequestContext attachment, final SecurityContext securityContext) {
        return !wasAuthExceptionThrown() && (securityContext.getAuthenticatedAccount() != null || !isMandatory(attachment));
    }

    private boolean wasAuthExceptionThrown() {
        return SecurityContextAssociation.getSecurityContext().getData().get(AuthException.class.getName()) != null;
    }

    private JASPIServerAuthenticationManager createJASPIAuthenticationManager() {
        return new JASPIServerAuthenticationManager(this.securityDomain, new JBossCallbackHandler());
    }

    private String buildApplicationIdentifier(final ServletRequestContext attachment) {
        ServletRequest servletRequest = attachment.getServletRequest();
        return servletRequest.getLocalName() + " " + servletRequest.getServletContext().getContextPath();
    }

    private GenericMessageInfo createMessageInfo(final HttpServerExchange exchange, final SecurityContext securityContext) {
        ServletRequestContext servletRequestContext = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY);

        GenericMessageInfo messageInfo = new GenericMessageInfo();

        messageInfo.setRequestMessage(servletRequestContext.getServletRequest());
        messageInfo.setResponseMessage(servletRequestContext.getServletResponse());

        messageInfo.getMap().put("javax.security.auth.message.MessagePolicy.isMandatory", isMandatory(servletRequestContext).toString());

        // additional context data, useful to provide access to Undertow resources during the modules processing
        messageInfo.getMap().put(SECURITY_CONTEXT_ATTACHMENT_KEY, securityContext);
        messageInfo.getMap().put(HTTP_SERVER_EXCHANGE_ATTACHMENT_KEY, exchange);

        return messageInfo;
    }

    private Account createAccount(final JASPICallbackHandler cbh) {
        if (cbh == null) {
            throw MESSAGES.nullParamter("JASPICallbackHandler");
        }

        CallerPrincipalCallback cpc = cbh.getCallerPrincipalCallback();

        if (cpc == null) {
            throw MESSAGES.nullParamter("CallerPrincipalCallback from JASPI CallbackHandler");
        }

        String userName;

        if (cpc.getPrincipal() != null) {
            userName = cpc.getPrincipal().getName();
        } else {
            userName = cpc.getName();
        }

        PasswordValidationCallback pcb = cbh.getPasswordValidationCallback();
        PasswordCredential credential = null;

        if (pcb != null && pcb.getPassword() != null) {
            credential = new PasswordCredential(pcb.getPassword());
        }

        GroupPrincipalCallback gpc = cbh.getGroupPrincipalCallback();
        Set<String> groups = null;

        if (gpc != null && gpc.getGroups() != null) {
            groups = new HashSet<String>(Arrays.asList(gpc.getGroups()));
        }

        return new AccountImpl(new SimplePrincipal(userName), groups, credential);
    }

    private void secureResponse(final HttpServerExchange exchange, final SecurityContext securityContext, final JASPIServerAuthenticationManager sam, final GenericMessageInfo messageInfo, final JASPICallbackHandler cbh) {
        // we add the a response listener to properly invoke the secureResponse, after processing the destination
        exchange.addExchangeCompleteListener(new ExchangeCompletionListener() {
            @Override
            public void exchangeEvent(final HttpServerExchange exchange, final NextListener nextListener) {
                ServletRequestContext requestContext = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY);
                String applicationIdentifier = buildApplicationIdentifier(requestContext);

                if (isSecureResponse(requestContext, securityContext)) {
                    ROOT_LOGGER.debugf("secureResponse for layer [%s] and applicationContextIdentifier [%s].", JASPI_HTTP_SERVLET_LAYER, applicationIdentifier);
                    sam.secureResponse(messageInfo, new Subject(), JASPI_HTTP_SERVLET_LAYER, applicationIdentifier, cbh);
                }
                nextListener.proceed();
            }
        });
    }
    /**
     * <p>The authentication is mandatory if the servlet has http constraints (eg.: {@link
     * javax.servlet.annotation.HttpConstraint}).</p>
     *
     * @param attachment
     * @return
     */
    private Boolean isMandatory(final ServletRequestContext attachment) {
        return attachment.getCurrentServlet() != null
                && attachment.getCurrentServlet().getManagedServlet() != null
                && attachment.getCurrentServlet().getManagedServlet().getServletInfo() != null
                && attachment.getCurrentServlet().getManagedServlet().getServletInfo().getServletSecurityInfo() != null
                && attachment.getCurrentServlet().getManagedServlet().getServletInfo().getServletSecurityInfo().getRolesAllowed() != null
                && !attachment.getCurrentServlet().getManagedServlet().getServletInfo().getServletSecurityInfo().getRolesAllowed().isEmpty();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1237.java