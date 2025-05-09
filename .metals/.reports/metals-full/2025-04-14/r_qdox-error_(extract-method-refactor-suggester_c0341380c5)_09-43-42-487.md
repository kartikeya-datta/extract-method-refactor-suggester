error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9016.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9016.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9016.java
text:
```scala
c@@allbackHandle = pathManagerInjector.getValue().registerCallback(accessLogRelativeTo, PathManager.ReloadServerCallback.create(), PathManager.Event.UPDATED, PathManager.Event.REMOVED);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.web;

import org.apache.catalina.Container;
import org.apache.catalina.Valve;
import org.apache.catalina.authenticator.SingleSignOn;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.catalina.valves.ExtendedAccessLogValve;
import org.jboss.as.clustering.web.sso.SSOClusterManager;
import org.jboss.as.controller.services.path.PathManager;
import org.jboss.as.web.sso.ClusteredSingleSignOn;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.web.rewrite.RewriteValve;

/**
 * Service creating and registering a virtual host.
 *
 * @author Emanuel Muckenhuber
 */
public class WebVirtualHostService implements Service<VirtualHost> {

    private final String name;
    private final String[] aliases;
    private final String tempPathName;
    private String defaultWebModule;
    private boolean hasWelcomeRoot;
    private ModelNode accessLog;
    private ModelNode rewrite;
    private ModelNode sso;

    private final InjectedValue<PathManager> pathManagerInjector = new InjectedValue<PathManager>();
    private final InjectedValue<WebServer> webServer = new InjectedValue<WebServer>();
    private final InjectedValue<SSOClusterManager> ssoManager = new InjectedValue<SSOClusterManager>();

    private VirtualHost host;

    private volatile String accessLogPath;
    private volatile String accessLogRelativeTo;
    private PathManager.Callback.Handle callbackHandle;

    public WebVirtualHostService(String name, String[] aliases, boolean hasWelcomeRoot, String tempPathName) {
        this.name = name;
        this.aliases = aliases;
        this.hasWelcomeRoot = hasWelcomeRoot;
        this.tempPathName = tempPathName;
    }

    /** {@inheritDoc} */
    public synchronized void start(StartContext context) throws StartException {
        final StandardHost host = new StandardHost();
        host.setAppBase(pathManagerInjector.getValue().getPathEntry(tempPathName).resolvePath());
        host.setName(name);
        for(final String alias : aliases) {
            host.addAlias(alias);
        }
        if(accessLog != null) {
            host.addValve(createAccessLogValve(host, pathManagerInjector.getValue().resolveRelativePathEntry(accessLogPath, accessLogRelativeTo), accessLog));
            pathManagerInjector.getValue().registerCallback(accessLogRelativeTo, PathManager.ReloadServerCallback.create(), PathManager.Event.UPDATED, PathManager.Event.REMOVED);
        }
        if(rewrite != null) {
            host.addValve(createRewriteValve(host, rewrite));
        }
        if(sso != null) {
            host.addValve(createSsoValve(host, sso));
        }
        if (defaultWebModule != null) {
            host.setDefaultWebapp(defaultWebModule);
        }
        try {
            final WebServer server = webServer.getValue();
            server.addHost(host);
        } catch(Exception e) {
            throw new StartException(e);
        }
        this.host = new VirtualHost(host, hasWelcomeRoot);

    }

    /** {@inheritDoc} */
    public synchronized void stop(StopContext context) {
        if (callbackHandle != null) {
            callbackHandle.remove();
        }
        final VirtualHost host = this.host;
        this.host = null;
        final WebServer server = webServer.getValue();
        server.removeHost(host.getHost());
    }

    /** {@inheritDoc} */
    public synchronized VirtualHost getValue() throws IllegalStateException {
        final VirtualHost host = this.host;
        if(host == null) {
            throw new IllegalStateException();
        }
        return host;
    }

    void setAccessLog(final ModelNode accessLog) {
        this.accessLog = accessLog;
    }

    void setAccessLogPaths(String accessLogPath, String accessLogRelativeTo) {
        this.accessLogPath = accessLogPath;
        this.accessLogRelativeTo = accessLogRelativeTo;
    }

    void setRewrite(ModelNode rewrite) {
        this.rewrite = rewrite;
    }

    void setSso(final ModelNode sso) {
        this.sso = sso;
    }

    protected String getDefaultWebModule() {
        return defaultWebModule;
    }

    protected void setDefaultWebModule(String defaultWebModule) {
        this.defaultWebModule = defaultWebModule;
    }

    public InjectedValue<PathManager> getPathManagerInjector() {
        return pathManagerInjector;
    }

    public InjectedValue<WebServer> getWebServer() {
        return webServer;
    }

    public InjectedValue<SSOClusterManager> getSSOClusterManager() {
        return ssoManager;
    }

    static Valve createAccessLogValve(final Container container, final String logDirectory, final ModelNode element) {
        boolean extended = false;
        if (element.hasDefined(Constants.EXTENDED)) {
            extended = element.get(Constants.EXTENDED).asBoolean();
        }
        final AccessLogValve log;
        if (extended) {
            log = new ExtendedAccessLogValve();
        } else {
            log = new AccessLogValve();
        }
        log.setDirectory(logDirectory);
        if (element.hasDefined(Constants.RESOLVE_HOSTS)) log.setResolveHosts(element.get(Constants.RESOLVE_HOSTS).asBoolean());
        if (element.hasDefined(Constants.ROTATE)) log.setRotatable(element.get(Constants.ROTATE).asBoolean());
        if (element.hasDefined(Constants.PATTERN)) {
            log.setPattern(element.get(Constants.PATTERN).asString());
        } else {
            log.setPattern("common");
        }
        if (element.hasDefined(Constants.PREFIX)) log.setPrefix(element.get(Constants.PREFIX).asString());
        return log;
    }

    static Valve createRewriteValve(final Container container, final ModelNode element) throws StartException {
        final RewriteValve rewriteValve = new RewriteValve();
        rewriteValve.setContainer(container);
        StringBuffer configuration = new StringBuffer();
        for (final ModelNode rewriteElement : element.asList()) {
            final ModelNode rewrite = rewriteElement.asProperty().getValue();
            if (rewrite.has(Constants.CONDITION)) {
                for (final ModelNode conditionElement : rewrite.get(Constants.CONDITION).asList()) {
                    final ModelNode condition = conditionElement.asProperty().getValue();
                    configuration.append("RewriteCond ")
                    .append(condition.get(Constants.TEST).asString())
                    .append(" ").append(condition.get(Constants.PATTERN).asString());
                    if (condition.hasDefined(Constants.FLAGS)) {
                        configuration.append(" [").append(condition.get(Constants.FLAGS).asString()).append("]\r\n");
                    } else {
                        configuration.append("\r\n");
                    }
                }
            }
            configuration.append("RewriteRule ")
            .append(rewrite.get(Constants.PATTERN).asString())
            .append(" ").append(rewrite.get(Constants.SUBSTITUTION).asString());
            if (rewrite.hasDefined(Constants.FLAGS)) {
                configuration.append(" [").append(rewrite.get(Constants.FLAGS).asString()).append("]\r\n");
            } else {
                configuration.append("\r\n");
            }
        }
        try {
            rewriteValve.setConfiguration(configuration.toString());
        } catch(Exception e) {
            throw new StartException(e);
        }
        return rewriteValve;
    }

    Valve createSsoValve(final Container container, final ModelNode element) throws StartException {
        final SingleSignOn ssoValve = element.hasDefined(Constants.CACHE_CONTAINER) ? new ClusteredSingleSignOn(this.ssoManager.getValue()) : new SingleSignOn();
        if (element.hasDefined(Constants.DOMAIN)) ssoValve.setCookieDomain(element.get(Constants.DOMAIN).asString());
        if (element.hasDefined(Constants.REAUTHENTICATE)) ssoValve.setRequireReauthentication(element.get(Constants.REAUTHENTICATE).asBoolean());
        return ssoValve;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9016.java