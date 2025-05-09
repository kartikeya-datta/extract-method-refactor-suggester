error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10737.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10737.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10737.java
text:
```scala
r@@eturn new RegexFilter(CommonAttributes.MATCH.resolveModelAttribute(context, node).asString());

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

package org.jboss.as.logging.util;

import static org.jboss.as.logging.LoggingMessages.MESSAGES;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.logging.CommonAttributes;
import org.jboss.dmr.ModelNode;
import org.jboss.logmanager.filters.AcceptAllFilter;
import org.jboss.logmanager.filters.AllFilter;
import org.jboss.logmanager.filters.AnyFilter;
import org.jboss.logmanager.filters.DenyAllFilter;
import org.jboss.logmanager.filters.InvertFilter;
import org.jboss.logmanager.filters.LevelChangingFilter;
import org.jboss.logmanager.filters.LevelFilter;
import org.jboss.logmanager.filters.LevelRangeFilter;
import org.jboss.logmanager.filters.RegexFilter;
import org.jboss.logmanager.filters.SubstituteFilter;
import org.jboss.logmanager.handlers.AsyncHandler.OverflowAction;

/**
 * Date: 10.11.2011
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public final class ModelParser {

    private static final Pattern SIZE_PATTERN = Pattern.compile("(\\d+)([kKmMgGbBtT])?");

    private ModelParser() {
    }

    /**
     * Parses the node and returns the filter.
     *
     * @param context the operation context
     * @param node    the node to parse.
     *
     * @return the filter.
     *
     * @throws OperationFailedException if the operation fails or is invalid.
     */
    public static Filter parseFilter(final OperationContext context, final ModelNode node) throws OperationFailedException {
        if (node.hasDefined(CommonAttributes.ACCEPT.getName())) {
            return AcceptAllFilter.getInstance();
        } else if (node.hasDefined(CommonAttributes.ALL.getName())) {
            final List<Filter> filters = new ArrayList<Filter>();
            for (ModelNode n : node.get(CommonAttributes.ALL.getName()).asList()) {
                filters.add(parseFilter(context, n));
            }
            return new AllFilter(filters);
        } else if (node.hasDefined(CommonAttributes.ANY.getName())) {
            final List<Filter> filters = new ArrayList<Filter>();
            for (ModelNode n : node.get(CommonAttributes.ANY.getName()).asList()) {
                filters.add(parseFilter(context, n));
            }
            return new AnyFilter(filters);
        } else if (node.hasDefined(CommonAttributes.CHANGE_LEVEL.getName())) {
            return new LevelChangingFilter(parseLevel(CommonAttributes.CHANGE_LEVEL.resolveModelAttribute(context, node)));
        } else if (node.hasDefined(CommonAttributes.DENY.getName())) {
            return DenyAllFilter.getInstance();
        } else if (node.hasDefined(CommonAttributes.LEVEL.getName())) {
            return new LevelFilter(parseLevel(CommonAttributes.LEVEL.resolveModelAttribute(context, node)));
        } else if (node.hasDefined(CommonAttributes.LEVEL_RANGE.getName())) {
            final Level min = parseLevel(CommonAttributes.MIN_LEVEL.resolveModelAttribute(context, node));
            final Level max = parseLevel(CommonAttributes.MAX_LEVEL.resolveModelAttribute(context, node));
            final boolean minInclusive = CommonAttributes.MIN_INCLUSIVE.resolveModelAttribute(context, node).asBoolean();
            final boolean maxInclusive = CommonAttributes.MAX_INCLUSIVE.resolveModelAttribute(context, node).asBoolean();
            return new LevelRangeFilter(min, minInclusive, max, maxInclusive);
        } else if (node.hasDefined(CommonAttributes.MATCH.getName())) {
            return new RegexFilter(CommonAttributes.PATTERN.resolveModelAttribute(context, node).asString());
        } else if (node.hasDefined(CommonAttributes.NOT.getName())) {
            return new InvertFilter(parseFilter(context, CommonAttributes.NOT.resolveModelAttribute(context, node)));
        } else if (node.hasDefined(CommonAttributes.REPLACE.getName())) {
            final String pattern = CommonAttributes.PATTERN.resolveModelAttribute(context, node).asString();
            final String replacement = CommonAttributes.REPLACEMENT.resolveModelAttribute(context, node).asString();
            final boolean replaceAll = CommonAttributes.REPLACE_ALL.resolveModelAttribute(context, node).asBoolean();
            return new SubstituteFilter(pattern, replacement, replaceAll);
        }
        final String name = node.hasDefined(CommonAttributes.FILTER.getName()) ? node.get(CommonAttributes.FILTER.getName()).asString() : node.asString();
        throw new OperationFailedException(new ModelNode().set(MESSAGES.invalidFilter(name)));
    }


    /**
     * Parses the string value of the size and returns the long value.
     *
     * @param node the node that contains the value.
     *
     * @return the long value.
     *
     * @throws OperationFailedException if the size is invalid.
     */
    public static long parseSize(final ModelNode node) throws OperationFailedException {
        final Matcher matcher = SIZE_PATTERN.matcher(node.asString());
        if (!matcher.matches()) {
            throw new OperationFailedException(new ModelNode().set(MESSAGES.invalidSize(node.asString())));
        }
        long qty = Long.parseLong(matcher.group(1), 10);
        final String chr = matcher.group(2);
        if (chr != null) {
            switch (chr.charAt(0)) {
                case 'b':
                case 'B':
                    break;
                case 'k':
                case 'K':
                    qty <<= 10L;
                    break;
                case 'm':
                case 'M':
                    qty <<= 20L;
                    break;
                case 'g':
                case 'G':
                    qty <<= 30L;
                    break;
                case 't':
                case 'T':
                    qty <<= 40L;
                    break;
                default:
                    throw new OperationFailedException(new ModelNode().set(MESSAGES.invalidSize(node.asString())));
            }
        }
        return qty;
    }

    /**
     * Parses the value into a logging level.
     *
     * @param node the node that contains the value.
     *
     * @return the logging level.
     *
     * @throws OperationFailedException if the size is invalid.
     */
    public static Level parseLevel(final ModelNode node) throws OperationFailedException {
        try {
            return Level.parse(node.asString());
        } catch (IllegalArgumentException e) {
            throw new OperationFailedException(new ModelNode().set(MESSAGES.invalidLogLevel(node.asString())));
        }
    }

    /**
     * Parses the string value into an {@link OverflowAction}.
     *
     * @param node the node that contains the value.
     *
     * @return the overflow action.
     *
     * @throws OperationFailedException if the size is invalid.
     */
    public static OverflowAction parseOverflowAction(final ModelNode node) throws OperationFailedException {
        try {
            return OverflowAction.valueOf(node.asString());
        } catch (IllegalArgumentException e) {
            throw new OperationFailedException(new ModelNode().set(MESSAGES.invalidOverflowAction(node.asString())));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10737.java