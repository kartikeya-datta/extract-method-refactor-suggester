error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15098.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15098.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15098.java
text:
```scala
public i@@nt getCode() {

/*
* JBoss, Home of Professional Open Source.
* Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.domain.controller;

import static org.jboss.as.domain.controller.DomainControllerMessages.MESSAGES;

import org.jboss.as.controller.RunningMode;

/**
 * Used to propogate error codes as part of the error message when an error occurs registering the a slave host controller.
 * I do not want to modify the protocol at the moment to support error codes natively.
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class SlaveRegistrationException extends Exception {

    private static final String SEPARATOR = "-$-";

    private final ErrorCode errorCode;
    private final String errorMessage;

    public SlaveRegistrationException(ErrorCode errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static SlaveRegistrationException parse(String raw) {
        int index = raw.indexOf("-$-");
        if (index == -1) {
            return new SlaveRegistrationException(ErrorCode.NONE, raw);
        }

        ErrorCode code = ErrorCode.parseCode(Integer.valueOf(raw.substring(0, index)));
        String msg = raw.substring(index + SEPARATOR.length());
        return new SlaveRegistrationException(code, msg);
    }

    public static SlaveRegistrationException forUnknownError(String msg) {
        return new SlaveRegistrationException(ErrorCode.NONE, msg);
    }

    public static SlaveRegistrationException forHostAlreadyExists(String slaveName) {
        return new SlaveRegistrationException(ErrorCode.HOST_ALREADY_EXISTS, DomainControllerMessages.MESSAGES.slaveAlreadyRegistered(slaveName));
    }

    public static SlaveRegistrationException forMasterInAdminOnlyMode(RunningMode runningMode) {
        return new SlaveRegistrationException(ErrorCode.MASTER_IS_ADMIN_ONLY, DomainControllerMessages.MESSAGES.adminOnlyModeCannotAcceptSlaves(runningMode));
    }

    public static SlaveRegistrationException forHostIsNotMaster() {
        return new SlaveRegistrationException(ErrorCode.HOST_ALREADY_EXISTS, DomainControllerMessages.MESSAGES.slaveControllerCannotAcceptOtherSlaves());
    }

    public String marshal() {
        return errorCode.getCode() + SEPARATOR + errorMessage;
    }

    public String toString() {
        return errorCode.getCode() + SEPARATOR + errorMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public enum ErrorCode {
        NONE(0),
        HOST_ALREADY_EXISTS(1),
        MASTER_IS_ADMIN_ONLY(2),
        HOST_IS_NOT_MASTER(3);

        private final int code;

        ErrorCode(int code){
            this.code = code;
        }

        int getCode() {
            return code;
        }

        static ErrorCode parseCode(int code) {
            if (code == NONE.getCode()) {
                return NONE;
            } else if (code == HOST_ALREADY_EXISTS.getCode()) {
                return HOST_ALREADY_EXISTS;
            } else if (code == MASTER_IS_ADMIN_ONLY.getCode()) {
                return MASTER_IS_ADMIN_ONLY;
            } else if (code == HOST_IS_NOT_MASTER.getCode()) {
                return HOST_IS_NOT_MASTER;
            }
            throw MESSAGES.invalidCode(code);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15098.java