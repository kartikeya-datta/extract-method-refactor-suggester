error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7528.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7528.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7528.java
text:
```scala
r@@eturn new ErrorState(theConsole, result.getMessage(), getRetryState());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.as.domain.management.security.adduser;

import static org.jboss.as.domain.management.security.adduser.AddUser.NEW_LINE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jboss.as.domain.management.logging.DomainManagementLogger;
import org.jboss.as.domain.management.security.password.PasswordCheckResult;
import org.jboss.as.domain.management.security.password.RestrictionLevel;

/**
 * State to perform validation of the supplied password.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class ValidatePasswordState extends AbstractValidationState {

    private final StateValues stateValues;
    private final ConsoleWrapper theConsole;

    public ValidatePasswordState(ConsoleWrapper theConsole, final StateValues stateValues) {
        this.theConsole = theConsole;
        this.stateValues = stateValues;
    }

    @Override
    protected Collection<State> getValidationStates() {
        final List<State> validationStates;
        final boolean relaxRestriction = RestrictionLevel.RELAX.equals(stateValues.getOptions().getCheckUtil().getRestrictionLevel());
        if (!relaxRestriction && !stateValues.getOptions().isEnableDisableMode()) {
            validationStates = new ArrayList<State>(1);
            validationStates.add(getDetailedCheckState());
        } else {
            validationStates = new ArrayList<State>(0);
        }
        return validationStates;
    }

    private State getRetryState() {
        return stateValues.isSilentOrNonInteractive() ? null : new PromptNewUserState(theConsole, stateValues);
    }

    private State getDetailedCheckState() {
        return new State() {

            @Override
            public State execute() {
                PasswordCheckResult result = stateValues.getOptions().getCheckUtil().check(false, stateValues.getUserName(), stateValues.getPassword());
                final boolean warnResult = PasswordCheckResult.Result.WARN.equals(result.getResult());
                final boolean rejectResult = PasswordCheckResult.Result.REJECT.equals(result.getResult());
                switch (stateValues.getOptions().getCheckUtil().getRestrictionLevel()) {
                    case WARN:
                        if ((warnResult || rejectResult) && !stateValues.isSilentOrNonInteractive()) {
                            return confirmWeakPassword(result);
                        }
                        break;
                    case REJECT:
                        if (warnResult && !stateValues.isSilentOrNonInteractive()) {
                            return confirmWeakPassword(result);
                        }
                        if (rejectResult) {
                            return new ErrorState(theConsole, result.getMessage(), getRetryState(), stateValues);
                        }
                        break;
                    default:
                        break;
                }
                return ValidatePasswordState.this;
            }
        };
    }

    private State confirmWeakPassword(PasswordCheckResult result) {
        if (stateValues.getOptions().isConfirmWarning()) {
            theConsole.printf(result.getMessage());
            theConsole.printf(DomainManagementLogger.ROOT_LOGGER.sureToSetPassword());
            theConsole.printf(NEW_LINE);
            return ValidatePasswordState.this;
        } else {
            String message = result.getMessage();
            String prompt = DomainManagementLogger.ROOT_LOGGER.sureToSetPassword();
            State noState = new PromptNewUserState(theConsole, stateValues);
            return new ConfirmationChoice(theConsole, message, prompt, this, noState);
        }
    }

    @Override
    protected State getSuccessState() {
        // We like the password but want the user to re-enter to confirm they know the password.
        return stateValues.isInteractive() ? new PromptPasswordState(theConsole, stateValues, true) : new PreModificationState(
                theConsole, stateValues);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7528.java