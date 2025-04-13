error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4760.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4760.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4760.java
text:
```scala
p@@ublic final class ActionRouter implements ActionListener

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001,2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.jmeter.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.apache.jmeter.exceptions.IllegalUserActionException;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.reflect.ClassFinder;
import org.apache.log.Logger;

/**
 * @author     Michael Stover
 * @version    $Revision$
 */
public class ActionRouter implements ActionListener
{
    private Map commands = new HashMap();
    private static ActionRouter router;
    private static AddToTree add = new AddToTree();
    transient private static Logger log = LoggingManager.getLoggerForClass();
    private Map preActionListeners = new HashMap();
    private Map postActionListeners = new HashMap();

    private ActionRouter()
    {
    }

    public void actionPerformed(final ActionEvent e)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                performAction(e);
            }

        });
    }

    private void performAction(final ActionEvent e)
    {
        try
        {
            GuiPackage.getInstance().updateCurrentNode();
            Set commandObjects = (Set) commands.get(e.getActionCommand());
            Iterator iter = commandObjects.iterator();
            while (iter.hasNext())
            {
                try
                {
                    Command c = (Command) iter.next();
                    preActionPerformed(c.getClass(), e);
                    c.doAction(e);
                    postActionPerformed(c.getClass(), e);
                }
                catch(IllegalUserActionException err)
                {
                    JMeterUtils.reportErrorToUser(err.toString());
                }
                catch (Exception err)
                {
                    log.error("", err);
                }
            }
        }
        catch (NullPointerException er)
        {
            log.error("", er);
            JMeterUtils.reportErrorToUser(
                "Sorry, this feature ("
                    + e.getActionCommand()
                    + ") not yet implemented");
        }
    }

    /**
     * To execute an action immediately in the current thread.
     * @param e the action to execute
     */
    public void doActionNow(ActionEvent e)
    {
        performAction(e);
    }

    public Set getAction(String actionName)
    {
        Set set = new HashSet();
        Set commandObjects = (Set) commands.get(actionName);
        Iterator iter = commandObjects.iterator();
        while (iter.hasNext())
        {
            try
            {
                set.add(iter.next());
            }
            catch (Exception err)
            {
                log.error("", err);
            }
        }
        return set;
    }

    public Command getAction(String actionName, Class actionClass)
    {
        Set commandObjects = (Set) commands.get(actionName);
        Iterator iter = commandObjects.iterator();
        while (iter.hasNext())
        {
            try
            {
                Command com = (Command) iter.next();
                if (com.getClass().equals(actionClass))
                {
                    return com;
                }
            }
            catch (Exception err)
            {
                log.error("", err);
            }
        }
        return null;
    }

    public Command getAction(String actionName, String className)
    {
        Set commandObjects = (Set) commands.get(actionName);
        Iterator iter = commandObjects.iterator();
        while (iter.hasNext())
        {
            try
            {
                Command com = (Command) iter.next();
                if (com.getClass().getName().equals(className))
                {
                    return com;
                }
            }
            catch (Exception err)
            {
                log.error("", err);
            }
        }
        return null;
    }

    /**
     * Allows an ActionListener to receive notification of a command
     * being executed prior to the actual execution of the command.
     * 
     * @param action    the Class of the command for which the listener will
     *                  notifications for. Class must extend
     *                  org.apache.jmeter.gui.action.Command.
     * @param listener  the ActionListener to receive the notifications
     */
    public void addPreActionListener(Class action, ActionListener listener)
    {
        if (action != null)
        {
            HashSet set = (HashSet) preActionListeners.get(action.getName());
            if (set == null)
            {
                set = new HashSet();
            }
            set.add(listener);
            preActionListeners.put(action.getName(), set);
        }
    }

    /**
     * Allows an ActionListener to be removed from receiving 
     * notifications of a command being executed prior to the actual 
     * execution of the command.
     * 
     * @param action    the Class of the command for which the listener will
     *                  notifications for. Class must extend
     *                  org.apache.jmeter.gui.action.Command.
     * @param listener  the ActionListener to receive the notifications
     */
    public void removePreActionListener(Class action, ActionListener listener)
    {
        if (action != null)
        {
            HashSet set = (HashSet) preActionListeners.get(action.getName());
            if (set != null)
            {
                set.remove(listener);
                preActionListeners.put(action.getName(), set);
            }
        }
    }

    /**
     * Allows an ActionListener to receive notification of a command
     * being executed after the command has executed.
     * 
     * @param action    the Class of the command for which the listener will
     *                  notifications for. Class must extend
     *                  org.apache.jmeter.gui.action.Command.
     * @param listener
     */
    public void addPostActionListener(Class action, ActionListener listener)
    {
        if (action != null)
        {
            HashSet set = (HashSet) postActionListeners.get(action.getName());
            if (set == null)
            {
                set = new HashSet();
            }
            set.add(listener);
            postActionListeners.put(action.getName(), set);
        }
    }

    /**
     * Allows an ActionListener to be removed from receiving 
     * notifications of a command being executed after the command has executed.
     * 
     * @param action    the Class of the command for which the listener will
     *                  notifications for. Class must extend
     *                  org.apache.jmeter.gui.action.Command.
     * @param listener
     */
    public void removePostActionListener(Class action, ActionListener listener)
    {
        if (action != null)
        {
            HashSet set = (HashSet) postActionListeners.get(action.getName());
            if (set != null)
            {
                set.remove(listener);
                postActionListeners.put(action.getName(), set);
            }
        }
    }

    protected void preActionPerformed(Class action, ActionEvent e)
    {
        if (action != null)
        {
            HashSet listenerSet =
                (HashSet) preActionListeners.get(action.getName());
            if (listenerSet != null && listenerSet.size() > 0)
            {
                Object[] listeners = listenerSet.toArray();
                for (int i = 0; i < listeners.length; i++)
                {
                    ((ActionListener) listeners[i]).actionPerformed(e);
                }
            }
        }
    }

    protected void postActionPerformed(Class action, ActionEvent e)
    {
        if (action != null)
        {
            HashSet listenerSet =
                (HashSet) postActionListeners.get(action.getName());
            if (listenerSet != null && listenerSet.size() > 0)
            {
                Object[] listeners = listenerSet.toArray();
                for (int i = 0; i < listeners.length; i++)
                {
                    ((ActionListener) listeners[i]).actionPerformed(e);
                }
            }
        }
    }

    private void populateCommandMap()
    {
        List listClasses;
        Command command;
        Iterator iterClasses;
        Class commandClass;
        try
        {
            listClasses =
                ClassFinder.findClassesThatExtend(
                    JMeterUtils.getSearchPaths(),
                    new Class[] {
                        Class.forName(
                            "org.apache.jmeter.gui.action.Command")});
            commands = new HashMap(listClasses.size());
            if (listClasses.size() == 0)
            {
                log.warn("!!!!!Uh-oh, didn't find any action handlers!!!!!");
            }
            iterClasses = listClasses.iterator();
            while (iterClasses.hasNext())
            {
                String strClassName = (String) iterClasses.next();
                commandClass = Class.forName(strClassName);
                if (!Modifier.isAbstract(commandClass.getModifiers()))
                {
                    command = (Command) commandClass.newInstance();
                    Iterator iter = command.getActionNames().iterator();
                    while (iter.hasNext())
                    {
                        String commandName = (String) iter.next();
                        Set commandObjects = (Set) commands.get(commandName);
                        if (commandObjects == null)
                        {
                            commandObjects = new HashSet();
                            commands.put(commandName, commandObjects);
                        }
                        commandObjects.add(command);
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.error("exception finding action handlers", e);
        }
    }

    /**
     *  Gets the Instance attribute of the ActionRouter class
     *
     *@return    The Instance value
     */
    public static ActionRouter getInstance()
    {
        if (router == null)
        {
            router = new ActionRouter();
            router.populateCommandMap();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4760.java