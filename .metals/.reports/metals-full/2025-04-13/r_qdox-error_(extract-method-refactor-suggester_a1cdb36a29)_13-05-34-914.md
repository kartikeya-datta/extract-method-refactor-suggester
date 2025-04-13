error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7530.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7530.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7530.java
text:
```scala
r@@eturn new ErrorState(theConsole, DomainManagementLogger.ROOT_LOGGER.invalidChoiceUpdateUserResponse(), this);

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.jboss.as.domain.management.logging.DomainManagementLogger;

/**
 * State to perform validation of the supplied username.
 *
 * Checks include: - Valid characters. Easy to guess user names. Duplicate users.
 *
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class ValidateUserState extends AbstractValidationState {

    private static final String[] BAD_USER_NAMES = {"admin", "administrator", "root"};

    private static final char[] VALID_PUNCTUATION;
    private static final String VALID_SYMBOLS;

    static {
        char[] validPunctuation = { '.', '-', ',', '@', '/', '\\', '=' };
        Arrays.sort(validPunctuation);
        VALID_PUNCTUATION = validPunctuation;

        StringBuilder sb = new StringBuilder();
        for (int i=0 ; i < VALID_PUNCTUATION.length ; i++) {
            sb.append("\"");
            sb.append(VALID_PUNCTUATION[i]);
            sb.append("\"");
            if (i < VALID_PUNCTUATION.length -1) {
                sb.append(", ");
            }
        }
        VALID_SYMBOLS = sb.toString();
    }

    private final StateValues stateValues;
    private final ConsoleWrapper theConsole;

    public ValidateUserState(ConsoleWrapper theConsole, final StateValues stateValues) {
        this.theConsole = theConsole;
        this.stateValues = stateValues;
    }

    @Override
    protected Collection<State> getValidationStates() {
        List<State> validationStates = new ArrayList<State>(3);
        validationStates.add(getValidCharactersState());
        validationStates.add(getDuplicateCheckState());
        validationStates.add(getCommonNamesCheckState());

        return validationStates;
    }

    @Override
    protected State getSuccessState() {
        return new PromptPasswordState(theConsole, stateValues, false);
    }

    private State getRetryState() {
        return stateValues.isSilentOrNonInteractive() ? null : new PromptNewUserState(theConsole, stateValues);
    }

    private State getValidCharactersState() {
        return new State() {

            @Override
            public State execute() {
                for (char currentChar : stateValues.getUserName().toCharArray()) {
                    if ((!isValidPunctuation(currentChar))
                            && (Character.isLetter(currentChar) || Character.isDigit(currentChar)) == false) {
                        return new ErrorState(theConsole, DomainManagementLogger.ROOT_LOGGER.usernameNotAlphaNumeric(VALID_SYMBOLS), getRetryState(), stateValues);
                    }
                }

                return ValidateUserState.this;
            }

            private boolean isValidPunctuation(char currentChar) {
                return (Arrays.binarySearch(VALID_PUNCTUATION, currentChar) >= 0);
            }
        };
    }



    private State getDuplicateCheckState() {
        return new State() {

            @Override
            public State execute() {
                if (stateValues.isExistingDisabledUser() || stateValues.isExistingEnabledUser()) {
                    State duplicateContinuing = stateValues.isSilentOrNonInteractive() ? null :
                            new PromptNewUserState(theConsole, stateValues);
                    stateValues.setExistingUser(true);
                    if (stateValues.isSilentOrNonInteractive()) {
                        return ValidateUserState.this;
                    } else {
                        final boolean existingDisabledUser = stateValues.isExistingDisabledUser();
                        if (existingDisabledUser) {
                            theConsole.printf(DomainManagementLogger.ROOT_LOGGER.aboutToUpdateDisabledUser(stateValues.getUserName()));
                        } else {
                            theConsole.printf(DomainManagementLogger.ROOT_LOGGER.aboutToUpdateEnabledUser(stateValues.getUserName()));
                        }
                        theConsole.printf(AddUser.NEW_LINE);
                        String response = theConsole.readLine("(a): ");
                        if (response == null) {
                            // This will return user to the command prompt so add a new line to ensure
                            // the command prompt is on the next line.
                            theConsole.printf(AddUser.NEW_LINE);
                            return null;
                        }
                        Option option = convertResponse(response, existingDisabledUser);
                        switch (option) {
                            case NEW:
                                return duplicateContinuing;
                            case UPDATE:
                                break;
                            case ENABLE:
                                stateValues.getOptions().setEnableDisableMode(true);
                                stateValues.getOptions().setDisable(false);
                                return new PreModificationState(theConsole, stateValues);
                            case DISABLE:
                                stateValues.getOptions().setEnableDisableMode(true);
                                stateValues.getOptions().setDisable(true);
                                return new PreModificationState(theConsole, stateValues);
                            default:
                                return new ErrorState(theConsole, DomainManagementLogger.ROOT_LOGGER.invalidChoiceUpdateUserResponse(), this, stateValues);
                        }
                        return ValidateUserState.this;
                    }
                } else {
                    stateValues.setExistingUser(false);
                    return ValidateUserState.this;
                }
            }
        };
    }

    private State getCommonNamesCheckState() {
        return new State() {

            @Override
            public State execute() {
                // If this is updating an existing user then the name is already accepted.
                if (stateValues.isExistingUser() == false && stateValues.isSilentOrNonInteractive() == false) {
                    for (String current : BAD_USER_NAMES) {
                        if (current.equals(stateValues.getUserName().toLowerCase(Locale.ENGLISH))) {
                            String message = DomainManagementLogger.ROOT_LOGGER.usernameEasyToGuess(stateValues.getUserName());
                            String prompt = DomainManagementLogger.ROOT_LOGGER.sureToAddUser(stateValues.getUserName());

                            return new ConfirmationChoice(theConsole, message, prompt, ValidateUserState.this, getRetryState());
                        }
                    }
                }

                return ValidateUserState.this;
            }
        };
    }

    private Option convertResponse(final String response, final boolean existingDisabledUser) {
        String responseLowerCase = response.toLowerCase(Locale.ENGLISH);
        if ("".equals(responseLowerCase) || "a".equals(responseLowerCase)) {
            return Option.UPDATE;
        } else if ("b".equals(responseLowerCase)) {
            // Opposite option...
            if (existingDisabledUser) {
                // ... when the existing user is disabled, enable!
                return Option.ENABLE;
            } else {
                // ... when the existing user is enabled, disable!
                return Option.DISABLE;
            }
        } else if ("c".equals(responseLowerCase)) {
            return Option.NEW;
        }
        return Option.INVALID;
    }

    private enum Option {
        UPDATE,
        DISABLE,
        ENABLE,
        NEW,
        INVALID
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7530.java