error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6331.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6331.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6331.java
text:
```scala
final B@@atchBuilder builder = updateContext.getServiceTarget();

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

import java.util.Set;

import org.jboss.as.model.AbstractSubsystemUpdate;
import org.jboss.as.model.UpdateContext;
import org.jboss.as.model.UpdateFailedException;
import org.jboss.as.model.UpdateResultHandler;
import org.jboss.as.services.path.AbstractPathService;
import org.jboss.as.services.path.RelativePathService;
import org.jboss.msc.service.BatchBuilder;
import org.jboss.msc.service.ServiceBuilder;

/**
 * @author Emanuel Muckenhuber
 */
public class WebVirtualHostAdd extends AbstractWebSubsystemUpdate<Void> {

    private static final long serialVersionUID = 727085265030986640L;
    private static final String TEMP_DIR = "jboss.server.temp.dir";

    private final String name;
    private Set<String> aliases;
    private WebHostAccessLogElement accessLog;
    private WebHostRewriteElement rewrite;

    public WebVirtualHostAdd(String name) {
        if(name == null) {
            throw new IllegalArgumentException("null host name");
        }
        this.name = name;
    }

    /** {@inheritDoc} */
    protected void applyUpdate(WebSubsystemElement element) throws UpdateFailedException {
        final WebVirtualHostElement host = element.addHost(name);
        if(host == null) {
            throw new IllegalStateException("duplicate host " + name);
        }
        host.setAliases(aliases);
    }

    /** {@inheritDoc} */
    protected <P> void applyUpdate(UpdateContext updateContext, UpdateResultHandler<? super Void, P> resultHandler, P param) {
        final BatchBuilder builder = updateContext.getBatchBuilder();
        final WebVirtualHostService service = new WebVirtualHostService(name, aliases());
        final ServiceBuilder<?> serviceBuilder =  builder.addService(WebSubsystemElement.JBOSS_WEB_HOST.append(name), service)
            .addDependency(AbstractPathService.pathNameOf(TEMP_DIR), String.class, service.getTempPathInjector())
            .addDependency(WebSubsystemElement.JBOSS_WEB, WebServer.class, service.getWebServer());
        if(accessLog != null) {
            service.setAccessLog(accessLog);
            // Create the access log service
            accessLogService(name, accessLog.getDirectory(), builder);
            serviceBuilder.addDependency(WebSubsystemElement.JBOSS_WEB_HOST.append(name, "access-log"), String.class, service.getAccessLogPathInjector());
        }
        if(rewrite != null) {
            service.setRewrite(rewrite);
        }
        serviceBuilder.install();
    }

    /** {@inheritDoc} */
    public AbstractSubsystemUpdate<WebSubsystemElement, ?> getCompensatingUpdate(WebSubsystemElement original) {
        return new WebVirtualHostRemove(name);
    }

    public String getName() {
        return name;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }

    public WebHostAccessLogElement getAccessLog() {
        return accessLog;
    }

    public void setAccessLog(WebHostAccessLogElement accessLog) {
        this.accessLog = accessLog;
    }

    public WebHostRewriteElement getRewrite() {
        return rewrite;
    }

    public void setRewrite(WebHostRewriteElement rewrite) {
        this.rewrite = rewrite;
    }

    private String[] aliases() {
        if(aliases != null && ! aliases.isEmpty()) {
            return aliases.toArray(new String[aliases.size()]);
        }
        return new String[0];
    }

    static WebVirtualHostAdd create(final WebVirtualHostElement host) {
        final WebVirtualHostAdd action = new WebVirtualHostAdd(host.getName());
        action.setAliases(host.getAliases());
        action.setAccessLog(host.getAccessLog());
        action.setRewrite(host.getRewrite());
        return action;
    }

    static final String DEFAULT_RELATIVE_TO = "jboss.server.log.dir";

    static void accessLogService(final String hostName, final WebHostAccessLogElement.LogDirectory directory, final BatchBuilder batchBuilder) {
        if(directory == null) {
            RelativePathService.addService(WebSubsystemElement.JBOSS_WEB_HOST.append(hostName, "access-log"),
                    hostName, DEFAULT_RELATIVE_TO, batchBuilder);
        } else {
            final String relativeTo = directory.getRelativeTo() != null ? directory.getRelativeTo() : DEFAULT_RELATIVE_TO;
            final String path = directory.getPath() != null ? directory.getPath() : hostName;
            RelativePathService.addService(WebSubsystemElement.JBOSS_WEB_HOST.append(hostName, "access-log"),
                    path, relativeTo, batchBuilder);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6331.java