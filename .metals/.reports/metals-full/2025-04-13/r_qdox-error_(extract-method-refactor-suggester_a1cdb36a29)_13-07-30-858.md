error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17189.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17189.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17189.java
text:
```scala
V@@ector<String> accept = StringUtils.split(validargs, ',');

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.taskdefs;

import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.input.DefaultInputHandler;
import org.apache.tools.ant.input.GreedyInputHandler;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.input.InputRequest;
import org.apache.tools.ant.input.MultipleChoiceInputRequest;
import org.apache.tools.ant.input.PropertyFileInputHandler;
import org.apache.tools.ant.input.SecureInputHandler;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.StringUtils;

/**
 * Reads an input line from the console.
 *
 * @since Ant 1.5
 *
 * @ant.task category="control"
 */
public class Input extends Task {

    /**
     * Represents an InputHandler.
     */
    public class Handler extends DefBase {

        private String refid = null;
        private HandlerType type = null;
        private String classname = null;

        /**
         * Specify that the handler is a reference on the project;
         * this allows the use of a custom inputhandler.
         * @param refid the String refid.
         */
        public void setRefid(String refid) {
            this.refid = refid;
        }
        /**
         * Get the refid of this Handler.
         * @return String refid.
         */
        public String getRefid() {
            return refid;
        }
        /**
         * Set the InputHandler classname.
         * @param classname the String classname.
         */
        public void setClassname(String classname) {
            this.classname = classname;
        }
        /**
         * Get the classname of the InputHandler.
         * @return String classname.
         */
        public String getClassname() {
            return classname;
        }
        /**
         * Set the handler type.
         * @param type a HandlerType.
         */
        public void setType(HandlerType type) {
            this.type = type;
        }
        /**
         * Get the handler type.
         * @return a HandlerType object.
         */
        public HandlerType getType() {
            return type;
        }
        private InputHandler getInputHandler() {
            if (type != null) {
               return type.getInputHandler();
            }
            if (refid != null) {
               try {
                   return (InputHandler) (getProject().getReference(refid));
               } catch (ClassCastException e) {
                   throw new BuildException(
                       refid + " does not denote an InputHandler", e);
               }
            }
            if (classname != null) {
               return (InputHandler) (ClasspathUtils.newInstance(classname,
                   createLoader(), InputHandler.class));
            }
            throw new BuildException(
                "Must specify refid, classname or type");
        }
    }

    /**
     * EnumeratedAttribute representing the built-in input handler types:
     * "default", "propertyfile", "greedy", "secure" (since Ant 1.8).
     */
    public static class HandlerType extends EnumeratedAttribute {
        private static final String[] VALUES = { "default", "propertyfile", "greedy", "secure" };

        private static final InputHandler[] HANDLERS
            = { new DefaultInputHandler(),
               new PropertyFileInputHandler(),
               new GreedyInputHandler(),
               new SecureInputHandler() };

        /** {@inheritDoc} */
        public String[] getValues() {
            return VALUES;
        }
        private InputHandler getInputHandler() {
            return HANDLERS[getIndex()];
        }
    }

    private String validargs = null;
    private String message = "";
    private String addproperty = null;
    private String defaultvalue = null;
    private Handler handler = null;
    private boolean messageAttribute;

    /**
     * Defines valid input parameters as comma separated strings. If set, input
     * task will reject any input not defined as accepted and requires the user
     * to reenter it. Validargs are case sensitive. If you want 'a' and 'A' to
     * be accepted you need to define both values as accepted arguments.
     *
     * @param validargs A comma separated String defining valid input args.
     */
    public void setValidargs (String validargs) {
        this.validargs = validargs;
    }

    /**
     * Defines the name of a property to be created from input. Behaviour is
     * according to property task which means that existing properties
     * cannot be overridden.
     *
     * @param addproperty Name for the property to be created from input
     */
    public void setAddproperty (String addproperty) {
        this.addproperty = addproperty;
    }

    /**
     * Sets the Message which gets displayed to the user during the build run.
     * @param message The message to be displayed.
     */
    public void setMessage (String message) {
        this.message = message;
        messageAttribute = true;
    }

    /**
     * Defines the default value of the property to be created from input.
     * Property value will be set to default if not input is received.
     *
     * @param defaultvalue Default value for the property if no input
     * is received
     */
    public void setDefaultvalue (String defaultvalue) {
        this.defaultvalue = defaultvalue;
    }

    /**
     * Set a multiline message.
     * @param msg The message to be displayed.
     */
    public void addText(String msg) {
        if (messageAttribute && "".equals(msg.trim())) {
            return;
        }
        message += getProject().replaceProperties(msg);
    }

    /**
     * No arg constructor.
     */
    public Input () {
    }

    /**
     * Actual method executed by ant.
     * @throws BuildException on error
     */
    public void execute () throws BuildException {
        if (addproperty != null
            && getProject().getProperty(addproperty) != null) {
            log("skipping " + getTaskName() + " as property " + addproperty
                + " has already been set.");
            return;
        }

        InputRequest request = null;
        if (validargs != null) {
            Vector accept = StringUtils.split(validargs, ',');
            request = new MultipleChoiceInputRequest(message, accept);
        } else {
            request = new InputRequest(message);
        }
        request.setDefaultValue(defaultvalue);

        InputHandler h = handler == null
            ? getProject().getInputHandler()
            : handler.getInputHandler();

        h.handleInput(request);

        String value = request.getInput();
        if ((value == null || value.trim().length() == 0)
            && defaultvalue != null) {
            value = defaultvalue;
        }
        if (addproperty != null && value != null) {
            getProject().setNewProperty(addproperty, value);
        }
    }

    /**
     * Create a nested handler element.
     * @return a Handler for this Input task.
     */
    public Handler createHandler() {
        if (handler != null) {
            throw new BuildException(
                "Cannot define > 1 nested input handler");
        }
        handler = new Handler();
        return handler;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17189.java