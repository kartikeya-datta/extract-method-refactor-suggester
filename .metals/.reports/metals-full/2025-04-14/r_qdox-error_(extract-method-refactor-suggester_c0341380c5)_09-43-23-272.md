error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7526.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7526.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7526.java
text:
```scala
r@@eturn new ErrorState(theConsole, DomainManagementLogger.ROOT_LOGGER.noPasswordExiting());

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

package org.jboss.as.domain.management.security.adduser;

import static org.jboss.as.domain.management.security.adduser.AddUser.NEW_LINE;

import java.util.Arrays;
import java.util.List;

import org.jboss.as.domain.management.logging.DomainManagementLogger;
import org.jboss.as.domain.management.security.password.PasswordRestriction;
import org.jboss.as.domain.management.security.password.RestrictionLevel;

/**
 * State to prompt the user for a password
 * <p/>
 * This state handles password validation by let the user re-enter the password in case of the password mismatch the user will
 * be present for an error and will re-enter the PromptPasswordState again
 */
public class PromptPasswordState implements State {

    private final ConsoleWrapper theConsole;
    private final StateValues stateValues;
    private final boolean rePrompt;

    public PromptPasswordState(ConsoleWrapper theConsole, StateValues stateValues, boolean rePrompt) {
        this.theConsole = theConsole;
        this.stateValues = stateValues;
        this.rePrompt = rePrompt;
    }

    @Override
    public State execute() {
        if (stateValues.isSilentOrNonInteractive() == false) {
            if (rePrompt == false) {
                // Password requirements.
                RestrictionLevel level = stateValues.getOptions().getCheckUtil().getRestrictionLevel();
                if (!RestrictionLevel.RELAX.equals(level)) {
                    final List<PasswordRestriction> passwordRestrictions = stateValues.getOptions().getCheckUtil().getPasswordRestrictions();
                    if (passwordRestrictions.size() > 0) {
                        if (level == RestrictionLevel.REJECT) {
                            theConsole.printf(DomainManagementLogger.ROOT_LOGGER.passwordRequirements());
                        } else {
                            theConsole.printf(DomainManagementLogger.ROOT_LOGGER.passwordRecommendations());
                        }
                        theConsole.printf(NEW_LINE);
                        for (PasswordRestriction passwordRestriction : passwordRestrictions) {
                            final String message = passwordRestriction.getRequirementMessage();
                            if (message != null && !message.isEmpty()) {
                                theConsole.printf(" - ");
                                theConsole.printf(message);
                                theConsole.printf(NEW_LINE);
                            }
                        }
                    }
                }
                // Prompt for password.
                theConsole.printf(DomainManagementLogger.ROOT_LOGGER.passwordPrompt());
                char[] tempChar = theConsole.readPassword(" : ");
                if (tempChar == null || tempChar.length == 0) {
                    return new ErrorState(theConsole, DomainManagementLogger.ROOT_LOGGER.noPasswordExiting(), stateValues);
                }
                stateValues.setPassword(new String(tempChar));

                return new ValidatePasswordState(theConsole, stateValues);
            } else {

                theConsole.printf(DomainManagementLogger.ROOT_LOGGER.passwordConfirmationPrompt());
                char[] secondTempChar = theConsole.readPassword(" : ");
                if (secondTempChar == null) {
                    secondTempChar = new char[0]; // If re-entry missed allow fall through to comparison.
                }

                if (Arrays.equals(stateValues.getPassword().toCharArray(), secondTempChar) == false) {
                    // Start again at the first password.
                    return new ErrorState(theConsole, DomainManagementLogger.ROOT_LOGGER.passwordMisMatch(), new PromptPasswordState(theConsole, stateValues, false));
                }

                // As long as it matches the actual value has already been validated.
                // Rather than checking if we are in management mode we need to check if we found any group property files.
                return stateValues.groupPropertiesFound() ? new PromptGroupsState(theConsole, stateValues)
                        : new PreModificationState(theConsole, stateValues);
            }
        }

        return new ValidatePasswordState(theConsole, stateValues);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7526.java