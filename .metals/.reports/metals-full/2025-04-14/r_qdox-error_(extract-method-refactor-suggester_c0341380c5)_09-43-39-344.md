error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/396.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/396.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/396.java
text:
```scala
public v@@oid setTaskType(String type) {

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
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

package org.apache.tools.ant;

import java.util.Enumeration;

/**
 * Base class for all tasks.
 *
 * Use Project.createTask to create a new task instance rather than
 * using this class directly for construction.
 *
 * @see Project#createTask
 */
public abstract class Task extends ProjectComponent {
    /**
     * Target this task belongs to, if any.
     * @deprecated You should not be accessing this variable directly.
     *   Please use the {@link #getOwningTarget()} method.
     */
    protected Target target;

    /**
     * Description of this task, if any.
     * @deprecated You should not be accessing this variable directly.
     */
    protected String description;

    /**
     * Location within the build file of this task definition.
     * @deprecated You should not be accessing this variable directly.
     *   Please use the {@link #getLocation()} method.
     */
    protected Location location = Location.UNKNOWN_LOCATION;

    /**
     * Name of this task to be used for logging purposes.
     * This defaults to the same as the type, but may be
     * overridden by the user. For instance, the name "java"
     * isn't terribly descriptive for a task used within
     * another task - the outer task code can probably
     * provide a better one.
     * @deprecated You should not be accessing this variable directly.
     *   Please use the {@link #getTaskName()} method.
     */
    protected String taskName;

    /**
     * Type of this task.
     *
     * @deprecated You should not be accessing this variable directly.
     *   Please use the {@link #getTaskType()} method.
     */
    protected String taskType;

    /**
     * Wrapper for this object, used to configure it at runtime.
     *
     * @deprecated You should not be accessing this variable directly.
     *   Please use the {@link #getWrapper()} method.
     */
    protected RuntimeConfigurable wrapper;

    /**
     * Whether or not this task is invalid. A task becomes invalid
     * if a conflicting class is specified as the implementation for
     * its type.
     */
    private boolean invalid;

    /** Sole constructor. */
    public Task() {
    }

    /**
     * Sets the target container of this task.
     *
     * @param target Target in whose scope this task belongs.
     *               May be <code>null</code>, indicating a top-level task.
     */
    public void setOwningTarget(Target target) {
        this.target = target;
    }

    /**
     * Returns the container target of this task.
     *
     * @return The target containing this task, or <code>null</code> if
     *         this task is a top-level task.
     */
    public Target getOwningTarget() {
        return target;
    }

    /**
     * Sets the name to use in logging messages.
     *
     * @param name The name to use in logging messages.
     *             Should not be <code>null</code>.
     */
    public void setTaskName(String name) {
        this.taskName = name;
    }

    /**
     * Returns the name to use in logging messages.
     *
     * @return the name to use in logging messages.
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Sets the name with which the task has been invoked.
     *
     * @param type The name the task has been invoked as.
     *             Should not be <code>null</code>.
     */
    void setTaskType(String type) {
        this.taskType = type;
    }

    /**
     * Sets a description of the current action. This may be used for logging
     * purposes.
     *
     * @param desc Description of the current action.
     *             May be <code>null</code>, indicating that no description is
     *             available.
     *
     */
    public void setDescription(String desc) {
        description = desc;
    }

    /**
     * Returns the description of the current action.
     *
     * @return the description of the current action, or <code>null</code> if
     *         no description is available.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Called by the project to let the task initialize properly.
     * The default implementation is a no-op.
     *
     * @exception BuildException if someting goes wrong with the build
     */
    public void init() throws BuildException {}

    /**
     * Called by the project to let the task do its work. This method may be
     * called more than once, if the task is invoked more than once.
     * For example,
     * if target1 and target2 both depend on target3, then running
     * "ant target1 target2" will run all tasks in target3 twice.
     *
     * @exception BuildException if something goes wrong with the build
     */
    public void execute() throws BuildException {}

    /**
     * Returns the file/location where this task was defined.
     *
     * @return the file/location where this task was defined.
     *         Should not return <code>null</code>. Location.UNKNOWN_LOCATION
     *         is used for unknown locations.
     *
     * @see Location#UNKNOWN_LOCATION
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the file/location where this task was defined.
     *
     * @param location The file/location where this task was defined.
     *                 Should not be <code>null</code> - use
     *                 Location.UNKNOWN_LOCATION if the location isn't known.
     *
     * @see Location#UNKNOWN_LOCATION
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Returns the wrapper used for runtime configuration.
     *
     * @return the wrapper used for runtime configuration. This
     *         method will generate a new wrapper (and cache it)
     *         if one isn't set already.
     */
    public RuntimeConfigurable getRuntimeConfigurableWrapper() {
        if (wrapper == null) {
            wrapper = new RuntimeConfigurable(this, getTaskName());
        }
        return wrapper;
    }

    /**
     * Sets the wrapper to be used for runtime configuration.
     *
     * This method should be used only by the ProjectHelper and ant internals.
     * It is public to allow helper plugins to operate on tasks, normal tasks
     * should never use it.
     *
     * @param wrapper The wrapper to be used for runtime configuration.
     *                May be <code>null</code>, in which case the next call
     *                to getRuntimeConfigurableWrapper will generate a new
     *                wrapper.
     */
    public void setRuntimeConfigurableWrapper(RuntimeConfigurable wrapper) {
        this.wrapper = wrapper;
    }

    // XXX: (Jon Skeet) The comment "if it hasn't been done already" may
    // not be strictly true. wrapper.maybeConfigure() won't configure the same
    // attributes/text more than once, but it may well add the children again,
    // unless I've missed something.
    /**
     * Configures this task - if it hasn't been done already.
     * If the task has been invalidated, it is replaced with an
     * UnknownElement task which uses the new definition in the project.
     *
     * @exception BuildException if the task cannot be configured.
     */
    public void maybeConfigure() throws BuildException {
        if (!invalid) {
            if (wrapper != null) {
                wrapper.maybeConfigure(getProject());
            }
        } else {
            getReplacement();
        }
    }

    /**
     * Handles a line of output by logging it with the INFO priority.
     *
     * @param line The line of output to log. Should not be <code>null</code>.
     */
    protected void handleOutput(String line) {
        log(line, Project.MSG_INFO);
    }

    /**
     * Handles an error line by logging it with the INFO priority.
     *
     * @param line The error line to log. Should not be <code>null</code>.
     */
    protected void handleErrorOutput(String line) {
        log(line, Project.MSG_ERR);
    }

    /**
     * Logs a message with the default (INFO) priority.
     *
     * @param msg The message to be logged. Should not be <code>null</code>.
     */
    public void log(String msg) {
        log(msg, Project.MSG_INFO);
    }

    /**
     * Logs a mesage with the given priority. This delegates
     * the actual logging to the project.
     *
     * @param msg The message to be logged. Should not be <code>null</code>.
     * @param msgLevel The message priority at which this message is to
     *                 be logged.
     */
    public void log(String msg, int msgLevel) {
        getProject().log(this, msg, msgLevel);
    }

    /**
     * Performs this task if it's still valid, or gets a replacement
     * version and performs that otherwise.
     *
     * Performing a task consists of firing a task started event,
     * configuring the task, executing it, and then firing task finished
     * event. If a runtime exception is thrown, the task finished event
     * is still fired, but with the exception as the cause.
     */
    public final void perform() {
        if (!invalid) {
            try {
                getProject().fireTaskStarted(this);
                maybeConfigure();
                execute();
                getProject().fireTaskFinished(this, null);
            } catch (RuntimeException exc) {
                if (exc instanceof BuildException) {
                    BuildException be = (BuildException) exc;
                    if (be.getLocation() == Location.UNKNOWN_LOCATION) {
                        be.setLocation(getLocation());
                    }
                }
                getProject().fireTaskFinished(this, exc);
                throw exc;
            }
        } else {
            UnknownElement ue = getReplacement();
            Task task = ue.getTask();
            task.perform();
        }
    }

    /**
     * Marks this task as invalid. Any further use of this task
     * will go through a replacement with the updated definition.
     */
    final void markInvalid() {
        invalid = true;
    }

    /**
     * Has this task been marked invalid?
     *
     * @since Ant 1.5
     */
    protected final boolean isInvalid() {
        return invalid;
    }

    /**
     * Replacement element used if this task is invalidated.
     */
    private UnknownElement replacement;

    /**
     * Creates an UnknownElement that can be used to replace this task.
     * Once this has been created once, it is cached and returned by
     * future calls.
     *
     * @return the UnknownElement instance for the new definition of this task.
     */
    private UnknownElement getReplacement() {
        if (replacement == null) {
            replacement = new UnknownElement(taskType);
            replacement.setProject(getProject());
            replacement.setTaskType(taskType);
            replacement.setTaskName(taskName);
            replacement.setLocation(location);
            replacement.setOwningTarget(target);
            replacement.setRuntimeConfigurableWrapper(wrapper);
            wrapper.setProxy(replacement);
            replaceChildren(wrapper, replacement);
            target.replaceChild(this, replacement);
            replacement.maybeConfigure();
        }
        return replacement;
    }

    /**
     * Recursively adds an UnknownElement instance for each child
     * element of replacement.
     *
     * @since Ant 1.5.1
     */
    private void replaceChildren(RuntimeConfigurable wrapper,
                                 UnknownElement parentElement) {
        Enumeration enum = wrapper.getChildren();
        while (enum.hasMoreElements()) {
            RuntimeConfigurable childWrapper =
                (RuntimeConfigurable) enum.nextElement();
            UnknownElement childElement =
                new UnknownElement(childWrapper.getElementTag());
            parentElement.addChild(childElement);
            childElement.setProject(getProject());
            childElement.setRuntimeConfigurableWrapper(childWrapper);
            childWrapper.setProxy(childElement);
            replaceChildren(childWrapper, childElement);
        }
    }

    protected String getTaskType() {
        return taskType;
    }

    protected RuntimeConfigurable getWrapper() {
        return wrapper;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/396.java