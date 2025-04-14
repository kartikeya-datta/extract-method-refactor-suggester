error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9770.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9770.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9770.java
text:
```scala
public static b@@oolean respondsTo(Object o, String methodName)

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
package org.apache.tools.ant.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;
import java.lang.reflect.Field;

/**
 * Utility class to handle reflection on java objects.
 * The class contains static methods to call reflection
 * methods, catch any exceptions, converting them
 * to BuildExceptions.
 */

public class ReflectUtil {

    /**  private constructor */
    private ReflectUtil() {
    }

    /**
     * Call a method on the object with no parameters.
     * @param obj  the object to invoke the method on.
     * @param methodName the name of the method to call
     * @return the object returned by the method
     */
    public static Object invoke(Object obj, String methodName) {
        try {
            Method method;
            method = obj.getClass().getMethod(
                methodName, (Class[]) null);
            return method.invoke(obj, (Object[]) null);
        } catch (Exception t) {
            throwBuildException(t);
            return null; // NotReached
        }
    }

    /**
     * Call a method on the object with one argument.
     * @param obj  the object to invoke the method on.
     * @param methodName the name of the method to call
     * @param argType    the type of argument.
     * @param arg        the value of the argument.
     * @return the object returned by the method
     */
    public static Object invoke(
        Object obj, String methodName, Class argType, Object arg) {
        try {
            Method method;
            method = obj.getClass().getMethod(
                methodName, new Class[] {argType});
            return method.invoke(obj, new Object[] {arg});
        } catch (Exception t) {
            throwBuildException(t);
            return null; // NotReached
        }
    }

    /**
     * Call a method on the object with two argument.
     * @param obj  the object to invoke the method on.
     * @param methodName the name of the method to call
     * @param argType1   the type of the first argument.
     * @param arg1       the value of the first argument.
     * @param argType2   the type of the second argument.
     * @param arg2       the value of the second argument.
     * @return the object returned by the method
     */
    public static Object invoke(
        Object obj, String methodName, Class argType1, Object arg1,
        Class argType2, Object arg2) {
        try {
            Method method;
            method = obj.getClass().getMethod(
                methodName, new Class[] {argType1, argType2});
            return method.invoke(obj, new Object[] {arg1, arg2});
        } catch (Exception t) {
            throwBuildException(t);
            return null; // NotReached
        }
    }

    /**
     * Get the value of a field in an object.
     * @param obj the object to look at.
     * @param fieldName the name of the field in the object.
     * @return the value of the field.
     * @throws BuildException if there is an error.
     */
    public static Object getField(Object obj, String fieldName)
        throws BuildException {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception t) {
            throwBuildException(t);
            return null; // NotReached
        }
    }

    /**
     * A method to convert an invocationTargetException to
     * a buildexception and throw it.
     * @param t the invocation target exception.
     * @throws BuildException the converted exception.
     */
    public static void throwBuildException(Exception t)
        throws BuildException {
        if (t instanceof InvocationTargetException) {
            Throwable t2 = ((InvocationTargetException) t)
                .getTargetException();
            if (t2 instanceof BuildException) {
                throw (BuildException) t2;
            }
            throw new BuildException(t2);
        } else {
            throw new BuildException(t);
        }
    }
    
    /**
     * A method to test if an object responds to a given 
     * message (method call)
     * @param o the object
     * @param methodName the method to check for
     * @return
     * @throws BuildException
     */
    public static boolean resondsTo(Object o, String methodName) 
        throws BuildException {
        try {
            Method[] methods = o.getClass().getMethods();
            for(int i=0; i<methods.length; i++) {
                if(methods[i].getName() == methodName) {
                    return true;
                }
            }
            return false;
        } catch(Exception t) {
            throwBuildException(t);
        }
        return false;//not reached
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9770.java