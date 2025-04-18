error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2909.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2909.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2909.java
text:
```scala
S@@impleAttributeDefinition SUFFIX = SimpleAttributeDefinitionBuilder.create("suffix", ModelType.STRING).build();

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

package org.jboss.as.logging;

import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.operations.validation.IntRangeValidator;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.logging.handlers.console.Target;
import org.jboss.as.logging.validators.FileValidator;
import org.jboss.as.logging.validators.LogLevelValidator;
import org.jboss.as.logging.validators.OverflowActionValidator;
import org.jboss.as.logging.validators.SizeValidator;
import org.jboss.as.logging.validators.TargetValidator;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.logmanager.handlers.AsyncHandler.OverflowAction;


/**
 * @author Emanuel Muckenhuber
 */
public interface CommonAttributes {

    SimpleAttributeDefinition ACCEPT = SimpleAttributeDefinitionBuilder.create("accept", ModelType.BOOLEAN, true).
            setDefaultValue(new ModelNode().set(true)).
            build();

    SimpleAttributeDefinition APPEND = SimpleAttributeDefinitionBuilder.create("append", ModelType.BOOLEAN, true).
            setDefaultValue(new ModelNode().set(true)).
            setFlags(AttributeAccess.Flag.RESTART_RESOURCE_SERVICES).
            build();

    String ASYNC_HANDLER = "async-handler";

    SimpleAttributeDefinition AUTOFLUSH = SimpleAttributeDefinitionBuilder.create("autoflush", ModelType.BOOLEAN, true).
            setDefaultValue(new ModelNode().set(true)).
            build();

    SimpleAttributeDefinition CATEGORY = SimpleAttributeDefinitionBuilder.create("category", ModelType.STRING).build();

    SimpleAttributeDefinition CHANGE_LEVEL = SimpleAttributeDefinitionBuilder.create("change-level", ModelType.STRING, true).build();

    SimpleAttributeDefinition CLASS = SimpleAttributeDefinitionBuilder.create("class", ModelType.STRING).build();

    String CONSOLE_HANDLER = "console-handler";

    String CUSTOM_HANDLER = "custom-handler";

    SimpleAttributeDefinition DENY = SimpleAttributeDefinitionBuilder.create("deny", ModelType.BOOLEAN, true).
            setDefaultValue(new ModelNode().set(true)).
            build();

    SimpleAttributeDefinition ENCODING = SimpleAttributeDefinitionBuilder.create("encoding", ModelType.STRING, true).build();

    SimpleAttributeDefinition FILE = SimpleAttributeDefinitionBuilder.create("file", ModelType.OBJECT, true).
            setCorrector(FileCorrector.INSTANCE).
            setValidator(new FileValidator()).
            build();

    String FILE_HANDLER = "file-handler";

    SimpleAttributeDefinition FILE_NAME = SimpleAttributeDefinitionBuilder.create("file-name", ModelType.STRING).build();

    SimpleAttributeDefinition FORMATTER = SimpleAttributeDefinitionBuilder.create("formatter", ModelType.STRING, true).
            setDefaultValue(new ModelNode().set("%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%E%n")).
            build();

    SimpleAttributeDefinition HANDLER = SimpleAttributeDefinitionBuilder.create("handler", ModelType.STRING).build();

    LogHandlerListAttributeDefinition HANDLERS = LogHandlerListAttributeDefinition.Builder.of("handlers", HANDLER).
            setAllowNull(true).
            build();

    SimpleAttributeDefinition LEVEL = SimpleAttributeDefinitionBuilder.create("level", ModelType.STRING, true).
            setValidator(new LogLevelValidator(true)).
            build();

    String LOGGER = "logger";

    SimpleAttributeDefinition MATCH = SimpleAttributeDefinitionBuilder.create("match", ModelType.STRING, true).build();

    SimpleAttributeDefinition MAX_BACKUP_INDEX = SimpleAttributeDefinitionBuilder.create("max-backup-index", ModelType.INT).
            setDefaultValue(new ModelNode().set(1)).
            setValidator(new IntRangeValidator(1, true)).
            build();

    SimpleAttributeDefinition MAX_INCLUSIVE = SimpleAttributeDefinitionBuilder.create("max-inclusive", ModelType.BOOLEAN).
            setDefaultValue(new ModelNode().set(true)).
            build();

    SimpleAttributeDefinition MAX_LEVEL = SimpleAttributeDefinitionBuilder.create("max-level", ModelType.STRING).build();

    SimpleAttributeDefinition MIN_INCLUSIVE = SimpleAttributeDefinitionBuilder.create("min-inclusive", ModelType.BOOLEAN).
            setDefaultValue(new ModelNode().set(true)).
            build();

    SimpleAttributeDefinition MIN_LEVEL = SimpleAttributeDefinitionBuilder.create("min-level", ModelType.STRING).build();

    SimpleAttributeDefinition MODULE = SimpleAttributeDefinitionBuilder.create("module", ModelType.STRING).build();

    SimpleAttributeDefinition NAME = SimpleAttributeDefinitionBuilder.create("name", ModelType.STRING).build();

    SimpleAttributeDefinition VALUE = SimpleAttributeDefinitionBuilder.create("value", ModelType.STRING).build();

    SimpleAttributeDefinition NEW_LEVEL = SimpleAttributeDefinitionBuilder.create("new-level", ModelType.STRING).build();

    SimpleAttributeDefinition OVERFLOW_ACTION = SimpleAttributeDefinitionBuilder.create("overflow-action", ModelType.STRING).
            setDefaultValue(new ModelNode().set(OverflowAction.BLOCK.name())).
            setValidator(new OverflowActionValidator(false)).
            build();

    SimpleAttributeDefinition PATH = SimpleAttributeDefinitionBuilder.create("path", ModelType.STRING).build();

    SimpleAttributeDefinition PATTERN = SimpleAttributeDefinitionBuilder.create("pattern", ModelType.STRING).build();

    String PATTERN_FORMATTER = "pattern-formatter";

    String PERIODIC_ROTATING_FILE_HANDLER = "periodic-rotating-file-handler";

    SimpleAttributeDefinition PROPERTY = SimpleAttributeDefinitionBuilder.create("property", ModelType.PROPERTY).build();

    String PROPERTIES = "properties";

    SimpleAttributeDefinition QUEUE_LENGTH = SimpleAttributeDefinitionBuilder.create("queue-length", ModelType.INT).
            setFlags(AttributeAccess.Flag.RESTART_RESOURCE_SERVICES).
            setValidator(new IntRangeValidator(1, false)).
            build();

    SimpleAttributeDefinition RELATIVE_TO = SimpleAttributeDefinitionBuilder.create("relative-to", ModelType.STRING, true).build();

    SimpleAttributeDefinition REPLACEMENT = SimpleAttributeDefinitionBuilder.create("replacement", ModelType.STRING).build();

    SimpleAttributeDefinition REPLACE_ALL = SimpleAttributeDefinitionBuilder.create("replace-all", ModelType.BOOLEAN).
            setDefaultValue(new ModelNode().set(true)).
            build();

    String ROOT_LOGGER = "root-logger";

    String ROOT_LOGGER_NAME = "ROOT";

    SimpleAttributeDefinition ROTATE_SIZE = SimpleAttributeDefinitionBuilder.create("rotate-size", ModelType.STRING).
            setDefaultValue(new ModelNode().set("2m")).
            setValidator(new SizeValidator()).
            build();

    String SIZE_ROTATING_FILE_HANDLER = "size-rotating-file-handler";

    LogHandlerListAttributeDefinition SUBHANDLERS = LogHandlerListAttributeDefinition.Builder.of("subhandlers", HANDLER).
            setAllowNull(true).
            build();

    SimpleAttributeDefinition SUFFIX = SimpleAttributeDefinitionBuilder.create("suffix", ModelType.STRING, true).build();

    SimpleAttributeDefinition TARGET = SimpleAttributeDefinitionBuilder.create("target", ModelType.STRING, true).
            setDefaultValue(new ModelNode().set(Target.SYSTEM_OUT.toString())).
            setValidator(new TargetValidator(false)).
            build();

    SimpleAttributeDefinition USE_PARENT_HANDLERS = SimpleAttributeDefinitionBuilder.create("use-parent-handlers", ModelType.BOOLEAN, true).
            setDefaultValue(new ModelNode().set(true)).
            build();

    // Global object types
    ObjectTypeAttributeDefinition LEVEL_RANGE = ObjectTypeAttributeDefinition.Builder.of("level-range", MIN_LEVEL, MIN_INCLUSIVE, MAX_LEVEL, MAX_INCLUSIVE).
            setSuffix("filter.level-range").build();

    ObjectTypeAttributeDefinition REPLACE = ObjectTypeAttributeDefinition.Builder.of("replace", PATTERN, REPLACEMENT, REPLACE_ALL).
            setSuffix("filter.replace").build();

    ObjectTypeAttributeDefinition NOT = ObjectTypeAttributeDefinition.Builder.of("not", ACCEPT, CHANGE_LEVEL, DENY, LEVEL, LEVEL_RANGE, MATCH, REPLACE).
            setSuffix("filter").build();

    ObjectTypeAttributeDefinition ALL = ObjectTypeAttributeDefinition.Builder.of("all", ACCEPT, CHANGE_LEVEL, DENY, LEVEL, LEVEL_RANGE, MATCH, NOT, REPLACE).
            setSuffix("filter").build();

    ObjectTypeAttributeDefinition ANY = ObjectTypeAttributeDefinition.Builder.of("any", ACCEPT, CHANGE_LEVEL, DENY, LEVEL, LEVEL_RANGE, MATCH, NOT, REPLACE).
            setSuffix("filter").build();

    ObjectTypeAttributeDefinition FILTER = ObjectTypeAttributeDefinition.Builder.of("filter", ALL, ANY, ACCEPT, CHANGE_LEVEL, DENY, LEVEL, LEVEL_RANGE, MATCH, NOT, REPLACE).
            setSuffix("filter").build();

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/2909.java