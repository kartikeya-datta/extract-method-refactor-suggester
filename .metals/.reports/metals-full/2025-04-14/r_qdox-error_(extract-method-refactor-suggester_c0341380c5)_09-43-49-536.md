error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9564.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9564.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9564.java
text:
```scala
final M@@ethod executeM = taskClass.getMethod("execute", (Class[])null);

/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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

package org.apache.tools.ant;

import java.lang.reflect.Method;
import org.apache.tools.ant.dispatch.Dispatchable;
import org.apache.tools.ant.dispatch.DispatchUtils;

/**
 * Uses introspection to "adapt" an arbitrary Bean which doesn't
 * itself extend Task, but still contains an execute method and optionally
 * a setProject method.
 *
 */
public class TaskAdapter extends Task implements TypeAdapter {

    /** Object to act as a proxy for. */
    private Object proxy;

    /**
     * Checks whether or not a class is suitable to be adapted by TaskAdapter.
     * If the class is of type Dispatchable, the check is not performed because
     * the method that will be executed will be determined only at runtime of
     * the actual task and not during parse time.
     *
     * This only checks conditions which are additionally required for
     * tasks adapted by TaskAdapter. Thus, this method should be called by
     * Project.checkTaskClass.
     *
     * Throws a BuildException and logs as Project.MSG_ERR for
     * conditions that will cause the task execution to fail.
     * Logs other suspicious conditions with Project.MSG_WARN.
     *
     * @param taskClass Class to test for suitability.
     *                  Must not be <code>null</code>.
     * @param project   Project to log warnings/errors to.
     *                  Must not be <code>null</code>.
     *
     * @see Project#checkTaskClass(Class)
     */
    public static void checkTaskClass(final Class taskClass,
                                      final Project project) {
        if (!Dispatchable.class.isAssignableFrom(taskClass)) {
            // don't have to check for interface, since then
            // taskClass would be abstract too.
            try {
                final Method executeM = taskClass.getMethod("execute", null);
                // don't have to check for public, since
                // getMethod finds public method only.
                // don't have to check for abstract, since then
                // taskClass would be abstract too.
                if (!Void.TYPE.equals(executeM.getReturnType())) {
                    final String message = "return type of execute() should be "
                        + "void but was \"" + executeM.getReturnType() + "\" in "
                        + taskClass;
                    project.log(message, Project.MSG_WARN);
                }
            } catch (NoSuchMethodException e) {
                final String message = "No public execute() in " + taskClass;
                project.log(message, Project.MSG_ERR);
                throw new BuildException(message);
            } catch (LinkageError e) {
                String message = "Could not load " + taskClass + ": " + e;
                project.log(message, Project.MSG_ERR);
                throw new BuildException(message, e);
            }
        }
    }

    /**
     * check if the proxy class is a valid class to use
     * with this adapter.
     * the class must have a public no-arg "execute()" method.
     * @param proxyClass the class to check
     */
    public void checkProxyClass(Class proxyClass) {
        checkTaskClass(proxyClass, getProject());
    }

    /**
     * Executes the proxied task.
     *
     * @exception BuildException if the project could not be set
     * or the method could not be executed.
     */
    public void execute() throws BuildException {
        Method setProjectM = null;
        try {
            Class c = proxy.getClass();
            setProjectM =
                c.getMethod("setProject", new Class[] {Project.class});
            if (setProjectM != null) {
                setProjectM.invoke(proxy, new Object[] {getProject()});
            }
        } catch (NoSuchMethodException e) {
            // ignore this if the class being used as a task does not have
            // a set project method.
        } catch (Exception ex) {
            log("Error setting project in " + proxy.getClass(),
                Project.MSG_ERR);
            throw new BuildException(ex);
        }


        Method executeM = null;
        try {
            Class c = proxy.getClass();
            DispatchUtils.execute(proxy);
            return;
        } catch (Exception ex) {
            log("Error in " + proxy.getClass(), Project.MSG_VERBOSE);
            throw new BuildException(ex);
        }

    }

    /**
     * Sets the target object to proxy for.
     *
     * @param o The target object. Must not be <code>null</code>.
     */
    public void setProxy(Object o) {
        this.proxy = o;
    }

    /**
     * Returns the target object being proxied.
     *
     * @return the target proxy object
     */
    public Object getProxy() {
        return proxy;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9564.java