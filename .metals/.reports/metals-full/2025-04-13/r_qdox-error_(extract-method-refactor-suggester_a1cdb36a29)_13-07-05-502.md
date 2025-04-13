error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10003.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10003.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,25]

error in qdox parser
file content:
```java
offset: 25
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10003.java
text:
```scala
private static volatile A@@ctionRouter router;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.gui.action;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.apache.jmeter.exceptions.IllegalUserActionException;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.reflect.ClassFinder;
import org.apache.jorphan.util.JMeterError;
import org.apache.log.Logger;

public final class ActionRouter implements ActionListener {
    private static final Logger log = LoggingManager.getLoggerForClass();

	private static final Object LOCK = new Object();

    private static ActionRouter router;

    private Map<String, Set<Command>> commands = new HashMap<String, Set<Command>>();

    private final Map<String, HashSet<ActionListener>> preActionListeners =
        new HashMap<String, HashSet<ActionListener>>();

    private final Map<String, HashSet<ActionListener>> postActionListeners =
        new HashMap<String, HashSet<ActionListener>>();

    private ActionRouter() {
    }

    public void actionPerformed(final ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                performAction(e);
            }

        });
    }

    private void performAction(final ActionEvent e) {
        String actionCommand = e.getActionCommand();
        try {
            try {
                GuiPackage.getInstance().updateCurrentGui();
            } catch (Exception err){
                log.error("performAction(" + actionCommand + ") updateCurrentGui() on" + e.toString() + " caused", err);
                JMeterUtils.reportErrorToUser("Problem updating GUI - see log file for details");
            }
            for (Command c : commands.get(actionCommand)) {
                try {
                    preActionPerformed(c.getClass(), e);
                    c.doAction(e);
                    postActionPerformed(c.getClass(), e);
                } catch (IllegalUserActionException err) {
                    JMeterUtils.reportErrorToUser(err.toString());
                } catch (Exception err) {
                    log.error("", err);
                }
            }
        } catch (NullPointerException er) {
            log.error("performAction(" + actionCommand + ") " + e.toString() + " caused", er);
            JMeterUtils.reportErrorToUser("Sorry, this feature (" + actionCommand + ") not yet implemented");
        }
    }

    /**
     * To execute an action immediately in the current thread.
     *
     * @param e
     *            the action to execute
     */
    public void doActionNow(ActionEvent e) {
        performAction(e);
    }

    public Set<Command> getAction(String actionName) {
        Set<Command> set = new HashSet<Command>();
        for (Command c : commands.get(actionName)) {
            try {
                set.add(c);
            } catch (Exception err) {
                log.error("Could not add Command", err);
            }
        }
        return set;
    }

    public Command getAction(String actionName, Class<?> actionClass) {
        for (Command com : commands.get(actionName)) {
            if (com.getClass().equals(actionClass)) {
                return com;
            }
        }
        return null;
    }

    public Command getAction(String actionName, String className) {
        for (Command com : commands.get(actionName)) {
            if (com.getClass().getName().equals(className)) {
                return com;
            }
        }
        return null;
    }

    /**
     * Allows an ActionListener to receive notification of a command being
     * executed prior to the actual execution of the command.
     *
     * @param action
     *            the Class of the command for which the listener will
     *            notifications for. Class must extend
     *            org.apache.jmeter.gui.action.Command.
     * @param listener
     *            the ActionListener to receive the notifications
     */
    public void addPreActionListener(Class<?> action, ActionListener listener) {
        if (action != null) {
            HashSet<ActionListener> set = preActionListeners.get(action.getName());
            if (set == null) {
                set = new HashSet<ActionListener>();
            }
            set.add(listener);
            preActionListeners.put(action.getName(), set);
        }
    }

    /**
     * Allows an ActionListener to be removed from receiving notifications of a
     * command being executed prior to the actual execution of the command.
     *
     * @param action
     *            the Class of the command for which the listener will
     *            notifications for. Class must extend
     *            org.apache.jmeter.gui.action.Command.
     * @param listener
     *            the ActionListener to receive the notifications
     */
    public void removePreActionListener(Class<?> action, ActionListener listener) {
        if (action != null) {
            HashSet<ActionListener> set = preActionListeners.get(action.getName());
            if (set != null) {
                set.remove(listener);
                preActionListeners.put(action.getName(), set);
            }
        }
    }

    /**
     * Allows an ActionListener to receive notification of a command being
     * executed after the command has executed.
     *
     * @param action
     *            the Class of the command for which the listener will
     *            notifications for. Class must extend
     *            org.apache.jmeter.gui.action.Command.
     * @param listener
     */
    public void addPostActionListener(Class<?> action, ActionListener listener) {
        if (action != null) {
            HashSet<ActionListener> set = postActionListeners.get(action.getName());
            if (set == null) {
                set = new HashSet<ActionListener>();
            }
            set.add(listener);
            postActionListeners.put(action.getName(), set);
        }
    }

    /**
     * Allows an ActionListener to be removed from receiving notifications of a
     * command being executed after the command has executed.
     *
     * @param action
     *            the Class of the command for which the listener will
     *            notifications for. Class must extend
     *            org.apache.jmeter.gui.action.Command.
     * @param listener
     */
    public void removePostActionListener(Class<?> action, ActionListener listener) {
        if (action != null) {
            HashSet<ActionListener> set = postActionListeners.get(action.getName());
            if (set != null) {
                set.remove(listener);
                postActionListeners.put(action.getName(), set);
            }
        }
    }

    protected void preActionPerformed(Class<? extends Command> action, ActionEvent e) {
        if (action != null) {
            HashSet<ActionListener> listenerSet = preActionListeners.get(action.getName());
            if (listenerSet != null && listenerSet.size() > 0) {
                ActionListener[] listeners = listenerSet.toArray(new ActionListener[listenerSet.size()]);
                for (int i = 0; i < listeners.length; i++) {
                    listeners[i].actionPerformed(e);
                }
            }
        }
    }

    protected void postActionPerformed(Class<? extends Command> action, ActionEvent e) {
        if (action != null) {
            HashSet<ActionListener> listenerSet = postActionListeners.get(action.getName());
            if (listenerSet != null && listenerSet.size() > 0) {
                ActionListener[] listeners = listenerSet.toArray(new ActionListener[listenerSet.size()]);
                for (int i = 0; i < listeners.length; i++) {
                    listeners[i].actionPerformed(e);
                }
            }
        }
    }

    private void populateCommandMap() {
        try {
            List<String> listClasses = ClassFinder.findClassesThatExtend(
                    JMeterUtils.getSearchPaths(), 
                    new Class[] {Class.forName("org.apache.jmeter.gui.action.Command") }); // $NON-NLS-1$
            commands = new HashMap<String, Set<Command>>(listClasses.size());
            if (listClasses.size() == 0) {
                log.fatalError("!!!!!Uh-oh, didn't find any action handlers!!!!!");
                throw new JMeterError("No action handlers found - check JMeterHome and libraries");
            }
            for (String strClassName : listClasses) {
                if (strClassName.startsWith("org.apache.jmeter.gui")) { // $NON-NLS-1$
                    Class<?> commandClass = Class.forName(strClassName);
                    if (!Modifier.isAbstract(commandClass.getModifiers())) {
                        Command command = (Command) commandClass.newInstance();
                        for (String commandName : command.getActionNames()) {
                            Set<Command> commandObjects = commands.get(commandName);
                            if (commandObjects == null) {
                                commandObjects = new HashSet<Command>();
                                commands.put(commandName, commandObjects);
                            }
                            commandObjects.add(command);
                        }
                    }
                }
            }
        } catch (HeadlessException e){
            log.warn(e.toString());
        } catch (JMeterError e) {
            throw e;
        } catch (Exception e) {
            log.error("exception finding action handlers", e);
        }
    }

    /**
     * Gets the Instance attribute of the ActionRouter class
     *
     * @return The Instance value
     */
    public static ActionRouter getInstance() {
        if (router == null) {
        	synchronized (LOCK) {
        		if(router == null) {
	                router = new ActionRouter();
	                router.populateCommandMap();
        		}				
			}
        }
        return router;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10003.java