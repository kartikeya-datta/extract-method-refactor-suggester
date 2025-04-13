error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11334.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11334.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11334.java
text:
```scala
public static final L@@evel[] LEVELS = {

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

package org.jboss.as.logging.validators;

import static org.jboss.as.logging.LoggingMessages.MESSAGES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;

import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.operations.validation.AllowedValuesValidator;
import org.jboss.as.controller.operations.validation.ModelTypeValidator;
import org.jboss.as.logging.util.ModelParser;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * Checks the value to see if it's a valid {@link Level}.
 * <p/>
 * Date: 13.07.2011
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
public final class LogLevelValidator extends ModelTypeValidator implements AllowedValuesValidator {
    private static final Level[] LEVELS = {
            org.jboss.logmanager.Level.ALL,
            org.jboss.logmanager.Level.CONFIG,
            org.jboss.logmanager.Level.DEBUG,
            org.jboss.logmanager.Level.ERROR,
            org.jboss.logmanager.Level.FATAL,
            org.jboss.logmanager.Level.FINE,
            org.jboss.logmanager.Level.FINER,
            org.jboss.logmanager.Level.FINEST,
            org.jboss.logmanager.Level.INFO,
            org.jboss.logmanager.Level.OFF,
            org.jboss.logmanager.Level.TRACE,
            org.jboss.logmanager.Level.WARN,
            org.jboss.logmanager.Level.WARNING
    };

    private final List<Level> allowedValues;
    private final List<ModelNode> nodeValues;

    public LogLevelValidator(final boolean nullable) {
        this(nullable, false);
    }

    public LogLevelValidator(final boolean nullable, final Level... levels) {
        this(nullable, false, levels);
    }

    public LogLevelValidator(final boolean nullable, final boolean allowExpressions) {
        this(nullable, allowExpressions, LEVELS);
    }

    public LogLevelValidator(final boolean nullable, final boolean allowExpressions, final Level... levels) {
        super(ModelType.STRING, nullable, allowExpressions);
        allowedValues = Arrays.asList(levels);
        Collections.sort(allowedValues, LevelComparator.INSTANCE);
        nodeValues = new ArrayList<ModelNode>(allowedValues.size());
        for (Level level : allowedValues) {
            nodeValues.add(new ModelNode().set(level.getName()));
        }
    }

    @Override
    public void validateParameter(final String parameterName, final ModelNode value) throws OperationFailedException {
        super.validateParameter(parameterName, value);
        if (value.isDefined()) {
            final String levelString = value.asString();
            try {
                final Level level = ModelParser.parseLevel(value);
                if (!allowedValues.contains(level)) {
                    throw new OperationFailedException(new ModelNode().set(MESSAGES.invalidLogLevel(levelString)));
                }
            } catch (IllegalArgumentException e) {
                throw new OperationFailedException(new ModelNode().set(MESSAGES.invalidLogLevel(levelString)));
            }
        }
    }

    @Override
    public List<ModelNode> getAllowedValues() {
        return nodeValues;
    }

    private static class LevelComparator implements Comparator<Level> {

        static final int EQUAL = 0;
        static final int LESS = -1;
        static final int GREATER = 1;

        static final LevelComparator INSTANCE = new LevelComparator();

        @Override
        public int compare(final Level o1, final Level o2) {
            int result = EQUAL;
            final int left = o1.intValue();
            final int right = o2.intValue();
            if (left < right) {
                result = LESS;
            } else if (left > right) {
                result = GREATER;
            }
            return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11334.java