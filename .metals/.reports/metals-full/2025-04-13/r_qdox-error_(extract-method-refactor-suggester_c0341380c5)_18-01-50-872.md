error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8245.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8245.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8245.java
text:
```scala
final L@@ist<Property> propsList = xaProps.getValueConverter().fromString(ctx, xaPropsStr).asPropertyList();

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
package org.jboss.as.cli.handlers.jca;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jboss.as.cli.ArgumentValueConverter;
import org.jboss.as.cli.CommandArgument;
import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.CommandLineException;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.handlers.OperationCommandWithDescription;
import org.jboss.as.cli.handlers.ResourceCompositeOperationHandler;
import org.jboss.as.cli.impl.ArgumentWithValue;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.dmr.Property;

/**
 * Command handler that adds an xa datasource and its xa properties in one composite operation.
 *
 * @author Alexey Loubyansky
 */
public class XADataSourceAddCompositeHandler extends ResourceCompositeOperationHandler implements OperationCommandWithDescription {

    private static final String XA_DATASOURCE_PROPERTIES = "xa-datasource-properties";

    private ArgumentWithValue xaProps;

    public XADataSourceAddCompositeHandler(CommandContext ctx, String nodeType) {
        super(ctx, "xa-data-source-add", nodeType, null, Util.ADD);

        xaProps = new ArgumentWithValue(this, null, ArgumentValueConverter.PROPERTIES, "--" + XA_DATASOURCE_PROPERTIES);
    }

    @Override
    protected Map<String, CommandArgument> loadArguments(CommandContext ctx) {
        final Map<String, CommandArgument> args = super.loadArguments(ctx);
        args.put(xaProps.getFullName(), xaProps);
        return args;
    }

    protected ModelNode buildRequestWithoutHeaders(CommandContext ctx) throws CommandFormatException {
        final ModelNode req = super.buildRequestWithoutHeaders(ctx);
        final ModelNode steps = req.get(Util.STEPS);

        final String xaPropsStr = xaProps.getValue(ctx.getParsedCommandLine());
        if(xaPropsStr != null) {
            final List<Property> propsList = xaProps.getValueConverter().fromString(xaPropsStr).asPropertyList();
            for(Property prop : propsList) {
                final ModelNode address = this.buildOperationAddress(ctx);
                address.add(XA_DATASOURCE_PROPERTIES, prop.getName());
                final ModelNode addProp = new ModelNode();
                addProp.get(Util.ADDRESS).set(address);
                addProp.get(Util.OPERATION).set(Util.ADD);
                addProp.get(Util.VALUE).set(prop.getValue());
                steps.add(addProp);
            }
        }
        return req;
    }

    @Override
    public ModelNode getOperationDescription(CommandContext ctx) throws CommandLineException {
        ModelNode request = initRequest(ctx);
        if(request == null) {
            return null;
        }
        request.get(Util.OPERATION).set(Util.READ_OPERATION_DESCRIPTION);
        request.get(Util.NAME).set(Util.ADD);
        final ModelNode response;
        try {
            response = ctx.getModelControllerClient().execute(request);
        } catch (IOException e) {
            throw new CommandFormatException("Failed to execute read-operation-description.", e);
        }
        if (!response.hasDefined(Util.RESULT)) {
            return null;
        }
        final ModelNode result = response.get(Util.RESULT);

        final ModelNode opDescr = result.get(Util.DESCRIPTION);
        final StringBuilder buf = new StringBuilder();
        buf.append(opDescr.asString());
        buf.append(" (unlike the add operation, this command accepts xa-datasource-properties).");
        opDescr.set(buf.toString());

        final ModelNode allProps = result.get(Util.REQUEST_PROPERTIES);
        final ModelNode xaProps = allProps.get(XA_DATASOURCE_PROPERTIES);

        xaProps.get(Util.DESCRIPTION).set("A comma-separated list of XA datasource properties in key=value pair format.");
        xaProps.get(Util.TYPE).set(ModelType.LIST);
        xaProps.get(Util.REQUIRED).set(false);
        xaProps.get(Util.NILLABLE).set(false);

        return result;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8245.java