error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/580.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/580.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/580.java
text:
```scala
c@@tx.error("Failed to get the AS release info: " + e.getLocalizedMessage());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.as.cli.handlers;


import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.jboss.as.cli.CommandArgument;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandHandler;
import org.jboss.as.cli.Util;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;


/**
 *
 * @author Alexey Loubyansky
 */
public class VersionHandler implements CommandHandler {

    public static final VersionHandler INSTANCE = new VersionHandler();

    /* (non-Javadoc)
     * @see org.jboss.as.cli.CommandHandler#isAvailable(org.jboss.as.cli.CommandContext)
     */
    @Override
    public boolean isAvailable(CommandContext ctx) {
        return true;
    }

    /* (non-Javadoc)
     * @see org.jboss.as.cli.CommandHandler#isBatchMode()
     */
    @Override
    public boolean isBatchMode() {
        return false;
    }

    /* (non-Javadoc)
     * @see org.jboss.as.cli.CommandHandler#handle(org.jboss.as.cli.CommandContext)
     */
    @Override
    public void handle(CommandContext ctx) {
        final StringBuilder buf = new StringBuilder();
        buf.append("JBoss Admin Command-line Interface\n");
        buf.append("JBOSS_HOME: ").append(SecurityActions.getEnvironmentVariable("JBOSS_HOME")).append('\n');
        buf.append("JBoss AS release: ");
        final ModelControllerClient client = ctx.getModelControllerClient();
        if(client == null) {
            buf.append("<connect to the controller and re-run the version command to see the release info>\n");
        } else {
            final ModelNode req = new ModelNode();
            req.get(Util.OPERATION).set(Util.READ_RESOURCE);
            req.get(Util.ADDRESS).setEmptyList();
            try {
                final ModelNode response = client.execute(req);
                if(Util.isSuccess(response)) {
                    if(response.hasDefined(Util.RESULT)) {
                        final ModelNode result = response.get(Util.RESULT);
                        byte flag = 0;
                        if(result.hasDefined("release-version")) {
                            buf.append(result.get("release-version").asString());
                            ++flag;
                        }
                        if(result.hasDefined("release-codename")) {
                            buf.append(" \"").append(result.get("release-codename").asString()).append('\"');
                            ++flag;
                        }
                        if(flag == 0) {
                            buf.append("release info was not provided by the controller");
                        }
                    } else {
                        buf.append("result was not available.");
                    }
                } else {
                    buf.append(Util.getFailureDescription(response));
                }
                buf.append('\n');
            } catch (IOException e) {
                ctx.printLine("Failed to get the AS release info: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        buf.append("JAVA_HOME: ").append(SecurityActions.getEnvironmentVariable("JAVA_HOME")).append('\n');
        buf.append("java.version: ").append(SecurityActions.getSystemProperty("java.version")).append('\n');
        buf.append("java.vm.vendor: ").append(SecurityActions.getSystemProperty("java.vm.vendor")).append('\n');
        buf.append("java.vm.version: ").append(SecurityActions.getSystemProperty("java.vm.version")).append('\n');
        buf.append("os.name: ").append(SecurityActions.getSystemProperty("os.name")).append('\n');
        buf.append("os.version: ").append(SecurityActions.getSystemProperty("os.version"));
        ctx.printLine(buf.toString());
    }

    @Override
    public boolean hasArgument(String name) {
        return false;
    }

    @Override
    public boolean hasArgument(int index) {
        return false;
    }

    @Override
    public List<CommandArgument> getArguments(CommandContext ctx) {
        return Collections.emptyList();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/580.java