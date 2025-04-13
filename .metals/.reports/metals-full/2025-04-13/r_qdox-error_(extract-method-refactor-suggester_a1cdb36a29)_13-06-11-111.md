error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4419.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4419.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[25,1]

error in qdox parser
file content:
```java
offset: 1079
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4419.java
text:
```scala
public interface LoggingLogger extends BasicLogger {

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

p@@ackage org.jboss.as.logging;

import org.jboss.as.controller.PathAddress;
import org.jboss.logging.BasicLogger;
import org.jboss.logging.Cause;
import org.jboss.logging.LogMessage;
import org.jboss.logging.Logger;
import org.jboss.logging.Message;
import org.jboss.logging.MessageLogger;

import static org.jboss.logging.Logger.Level.ERROR;
import static org.jboss.logging.Logger.Level.INFO;
import static org.jboss.logging.Logger.Level.WARN;

/**
 * Date: 09.06.2011
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@MessageLogger(projectCode = "JBAS")
interface LoggingLogger extends BasicLogger {

    /**
     * A root logger with the category of the package name.
     */
    LoggingLogger ROOT_LOGGER = Logger.getMessageLogger(LoggingLogger.class, LoggingLogger.class.getPackage().getName());

    /**
     * Logs an error message indicating the class, represented by the {@code className} parameter, caught exception
     * attempting to revert the operation, represented by the {@code op} parameter, at the address, represented by the
     * {@code address} parameter.
     *
     * @param cause     the cause of the error.
     * @param className the name of the class that caught the error.
     * @param op        the operation.
     * @param address   the address.
     */
    @LogMessage(level = ERROR)
    @Message(id = 11500, value = "%s caught exception attempting to revert operation %s at address %s")
    void errorRevertingOperation(@Cause Throwable cause, String className, String op, PathAddress address);

    /**
     * Logs a warning message indicating an error occurred trying to set the property, represented by the
     * {@code propertyName} parameter, on the handler class, represented by the {@code className} parameter.
     *
     * @param cause        the cause of the error.
     * @param propertyName the name of the property.
     * @param className    the name of the class.
     */
    @LogMessage(level = WARN)
    @Message(id = 11501, value = "An error occurred trying to set the property '%s' on handler '%s'.")
    void errorSettingProperty(@Cause Throwable cause, String propertyName, String className);

    /**
     * Logs an informational message indicating the bootstrap log handlers are being removed.
     */
    @LogMessage(level = INFO)
    @Message(id = 11502, value = "Removing bootstrap log handlers")
    void removingBootstrapLogHandlers();

    /**
     * Logs an informational message indicating the bootstrap log handlers have been restored.
     */
    @LogMessage(level = INFO)
    @Message(id = 11503, value = "Restored bootstrap log handlers")
    void restoredBootstrapLogHandlers();

    /**
     * Logs a warning message indicating an unknown property, represented by the {@code propertyName} parameter, for
     * the class represented by the {@code className} parameter.
     *
     * @param propertyName the name of the property.
     * @param className    the name of the class.
     */
    @LogMessage(level = WARN)
    @Message(id = 11504, value = "Unknown property '%s' for '%s'.")
    void unknownProperty(String propertyName, String className);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4419.java