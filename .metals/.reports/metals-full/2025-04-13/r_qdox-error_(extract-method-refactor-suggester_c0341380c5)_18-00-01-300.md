error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5108.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5108.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5108.java
text:
```scala
i@@f(!ctx.getParsedArguments().hasProperties()) {

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
package org.jboss.as.cli.handlers.jms;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.handlers.BatchModeCommandHandler;
import org.jboss.as.cli.operation.OperationFormatException;
import org.jboss.as.cli.operation.impl.DefaultOperationRequestBuilder;
import org.jboss.dmr.ModelNode;

/**
 *
 * @author Alexey Loubyansky
 */
public class CreateJmsResourceHandler extends BatchModeCommandHandler {

    public CreateJmsResourceHandler() {
        super("create-jms-resource", true);
        this.addRequiredPath("/subsystem=messaging");
    }

    @Override
    public ModelNode buildRequest(CommandContext ctx) throws OperationFormatException {

        try {
            if(!ctx.getParsedArguments().hasArguments()) {
                throw new OperationFormatException("Arguments are missing");
            }
        } catch (CommandFormatException e) {
            throw new OperationFormatException(e.getLocalizedMessage());
        }

        //String target = null;
        String restype = null;
        //String description = null;
        String propsStr = null;
        //boolean enabled = false;
        String jndiName = null;

        String[] args = ctx.getArgumentsString().split("\\s+");
        int i = 0;
        while(i < args.length) {
            String arg = args[i++];
            if(arg.equals("--restype")) {
                if(i < args.length) {
                    restype = args[i++];
                }
            } else if(arg.equals("--target")) {
//                if(i < args.length) {
//                    target = args[i++];
//                }
            } else if(arg.equals("--description")) {
//                if(i < args.length) {
//                    restype = args[i++];
//                }
            } else if(arg.equals("--property")) {
                if (i < args.length) {
                    propsStr = args[i++];
                }
            } else if(arg.equals("--enabled")) {
//                if (i < args.length) {
//                    enabled = Boolean.parseBoolean(args[i++]);
//                }
            } else {
                jndiName = arg;
            }
        }

        if(restype == null) {
            throw new OperationFormatException("Required parameter --restype is missing.");
        }

        if(jndiName == null) {
            throw new OperationFormatException("JNDI name is missing.");
        }

        String name = null;
        final Map<String, String> props;
        if(propsStr != null) {
            props = new HashMap<String, String>();
            String[] propsArr = propsStr.split(":");
            for(String prop : propsArr) {
                int equalsIndex = prop.indexOf('=');
                if(equalsIndex < 0 || equalsIndex == prop.length() - 1) {
                    throw new OperationFormatException("Failed to parse property '" + prop + "'");
                }

                String propName = prop.substring(0, equalsIndex).trim();
                String propValue = prop.substring(equalsIndex + 1).trim();
                if(propName.isEmpty()) {
                    throw new OperationFormatException("Failed to parse property '" + prop + "'");
                }

                if(propName.equals("imqDestinationName") ||propName.equalsIgnoreCase("name")) {
                    name = propValue;
                } else if("ClientId".equals(propName)) {
                    props.put("client-id", propValue);
                }
            }
        } else {
            props = Collections.emptyMap();
        }

        if(name == null) {
            name = jndiName.replace('/', '_');
        }

        if(restype.equals("javax.jms.Queue")) {

            DefaultOperationRequestBuilder builder = new DefaultOperationRequestBuilder();
            builder.addNode("subsystem", "messaging");
            builder.addNode("jms-queue", name);
            builder.setOperationName("add");
            builder.getModelNode().get("entries").add(jndiName);

            for(String prop : props.keySet()) {
                builder.addProperty(prop, props.get(prop));
            }

            return builder.buildRequest();

        } else if(restype.equals("javax.jms.Topic")) {

            DefaultOperationRequestBuilder builder = new DefaultOperationRequestBuilder();
            builder.addNode("subsystem", "messaging");
            builder.addNode("jms-topic", name);
            builder.setOperationName("add");
            builder.getModelNode().get("entries").add(jndiName);

            for(String prop : props.keySet()) {
                builder.addProperty(prop, props.get(prop));
            }

            return builder.buildRequest();

        } else if(restype.equals("javax.jms.ConnectionFactory") ||
                restype.equals("javax.jms.TopicConnectionFactory") ||
                restype.equals("javax.jms.QueueConnectionFactory")) {

            DefaultOperationRequestBuilder builder = new DefaultOperationRequestBuilder();
            builder.addNode("subsystem", "messaging");
            builder.addNode("connection-factory", name);
            builder.setOperationName("add");
            builder.getModelNode().get("entries").add(jndiName);

            for(String prop : props.keySet()) {
                builder.addProperty(prop, props.get(prop));
            }

            return builder.buildRequest();

        } else {
            throw new OperationFormatException("Resource type " + restype + " isn't supported.");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5108.java