error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8966.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8966.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8966.java
text:
```scala
public L@@og getLog(Class clazz) {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.jboss.as.clustering.jgroups;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.jgroups.logging.CustomLogFactory;
import org.jgroups.logging.Log;

/**
 * Temporary workaround for JGRP-1475.
 * @author Paul Ferraro
 */
public class LogFactory implements CustomLogFactory {

    @Override
    public Log getLog(@SuppressWarnings("rawtypes") Class clazz) {
        return new LogAdapter(Logger.getLogger(clazz));
    }

    @Override
    public Log getLog(String category) {
        return new LogAdapter(Logger.getLogger(category));
    }

    private static class LogAdapter implements Log {
        private final Logger logger;

        LogAdapter(Logger logger) {
            this.logger = logger;
        }

        @Override
        public boolean isFatalEnabled() {
            return this.logger.isEnabled(Level.FATAL);
        }

        @Override
        public boolean isErrorEnabled() {
            return this.logger.isEnabled(Level.ERROR);
        }

        @Override
        public boolean isWarnEnabled() {
            return this.logger.isEnabled(Level.WARN);
        }

        @Override
        public boolean isInfoEnabled() {
            return this.logger.isInfoEnabled();
        }

        @Override
        public boolean isDebugEnabled() {
            return this.logger.isDebugEnabled();
        }

        @Override
        public boolean isTraceEnabled() {
            return this.logger.isTraceEnabled();
        }

        @Override
        public void fatal(String msg) {
            this.logger.fatal(msg);
        }

        @Override
        public void fatal(String msg, Object... args) {
            this.logger.fatalf(msg, args);
        }

        @Override
        public void fatal(String msg, Throwable throwable) {
            this.logger.fatal(msg, throwable);
        }

        @Override
        public void error(String msg) {
            this.logger.error(msg);
        }

        @Override
        public void error(String msg, Object... args) {
            this.logger.errorf(msg, args);
        }

        @Override
        public void error(String msg, Throwable throwable) {
            this.logger.error(msg, throwable);
        }

        @Override
        public void warn(String msg) {
            this.logger.warn(msg);
        }

        @Override
        public void warn(String msg, Object... args) {
            this.logger.warnf(msg, args);
        }

        @Override
        public void warn(String msg, Throwable throwable) {
            this.logger.warn(msg, throwable);
        }

        @Override
        public void info(String msg) {
            this.logger.info(msg);
        }

        @Override
        public void info(String msg, Object... args) {
            this.logger.infof(msg, args);
        }

        @Override
        public void debug(String msg) {
            this.logger.debug(msg);
        }

        @Override
        public void debug(String msg, Object... args) {
            this.logger.debugf(msg, args);
        }

        @Override
        public void debug(String msg, Throwable throwable) {
            this.logger.debug(msg, throwable);
        }

        @Override
        public void trace(Object msg) {
            this.logger.trace(msg);
        }

        @Override
        public void trace(String msg) {
            this.logger.trace(msg);
        }

        @Override
        public void trace(String msg, Object... args) {
            this.logger.tracef(msg, args);
        }

        @Override
        public void trace(String msg, Throwable throwable) {
            this.logger.trace(msg, throwable);
        }

        @Override
        public void setLevel(String level) {
            // Unsupported
        }

        @Override
        public String getLevel() {
            return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8966.java