error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10504.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10504.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10504.java
text:
```scala
S@@tring specVersion = AccessController.doPrivileged(

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.lib.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;

/**
 * Utilities for dealing with different Java spec versions.
 *
 * @author Abe White
 * @nojavadoc
 */
public class JavaVersions {

    /**
     * Java version; one of 2, 3, 4, 5, 6, or 7.
     */
    public static final int VERSION;

    private static final Class[] EMPTY_CLASSES = new Class[0];

    private static Class PARAM_TYPE = null;
    private static Class ENUM_TYPE = null;
    private static Class ANNO_TYPE = null;
    private static Method GET_STACK = null;
    private static Method SET_STACK = null;
    private static Method GET_CAUSE = null;
    private static Method INIT_CAUSE = null;

    static {
        String specVersion = (String) AccessController.doPrivileged(
            J2DoPrivHelper.getPropertyAction("java.specification.version")); 
        if ("1.2".equals(specVersion))
            VERSION = 2;
        else if ("1.3".equals(specVersion))
            VERSION = 3;
        else if ("1.4".equals(specVersion))
            VERSION = 4;
        else if ("1.5".equals(specVersion))
            VERSION = 5;
        else if ("1.6".equals(specVersion))
            VERSION = 6;
        else
            VERSION = 7; // maybe someday...

        if (VERSION >= 5) {
            try {
                PARAM_TYPE = Class.forName
                    ("java.lang.reflect.ParameterizedType");
                ENUM_TYPE = Class.forName("java.lang.Enum");
                ANNO_TYPE = Class.forName("java.lang.annotation.Annotation");
            } catch (Throwable t) {
            }
        }

        if (VERSION >= 4) {
            try {
                Class stack = Class.forName("[Ljava.lang.StackTraceElement;");
                GET_STACK = Throwable.class.getMethod("getStackTrace",
                    (Class[]) null);
                SET_STACK = Throwable.class.getMethod("setStackTrace",
                    new Class[]{ stack });
                GET_CAUSE = Throwable.class.getMethod("getCause",
                    (Class[]) null);
                INIT_CAUSE = Throwable.class.getMethod("initCause",
                    new Class[]{ Throwable.class });
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Returns a version-specific instance of the specified class
     *
     * @param base the base class to check
     * @return the JDK-version-specific version of the class
     * @see #getVersionSpecificClass(String)
     */
    public static Class getVersionSpecificClass(Class base) {
        try {
            return getVersionSpecificClass(base.getName());
        } catch (ClassNotFoundException e) {
            return base;
        }
    }

    /**
     * Obtains a subclass of the specific base class that is
     * specific to the current version of Java in use. The
     * heuristic for the class name to load will be that OpenJPA
     * first checks for the name of the class with the current
     * setting of the {@link #VERSION} field, then each number in
     * decreasing order, until ending in the unqualified name.
     * For example, if we are using JDK 1.5.1, and we want to load
     * "org.apache.openjpa.lib.SomeClass", we will try to load the following
     * classes in order and return the first one that is successfully
     * found and loaded:
     * <ol>
     * <li>org.apache.openjpa.lib.SomeClass5</li>
     * <li>org.apache.openjpa.lib.SomeClass4</li>
     * <li>org.apache.openjpa.lib.SomeClass3</li>
     * <li>org.apache.openjpa.lib.SomeClass2</li>
     * <li>org.apache.openjpa.lib.SomeClass1</li>
     * <li>org.apache.openjpa.lib.SomeClass</li>
     * </ol>
     *
     * @param base the base name of the class to load
     * @return the subclass appropriate for the current Java version
     */
    public static Class getVersionSpecificClass(String base)
        throws ClassNotFoundException {
        for (int i = VERSION; i >= 1; i--) {
            try {
                return Class.forName(base + i);
            } catch (Throwable e) {
                // throwables might occur with bytecode that we
                // cannot understand
            }
        }
        return Class.forName(base);
    }

    /**
     * Return true if the given type is an annotation.
     */
    public static boolean isAnnotation(Class cls) {
        return ANNO_TYPE != null && ANNO_TYPE.isAssignableFrom(cls);
    }

    /**
     * Return true if the given type is an enumeration.
     */
    public static boolean isEnumeration(Class cls) {
        return ENUM_TYPE != null && ENUM_TYPE.isAssignableFrom(cls);
    }

    /**
     * Collects the parameterized type declarations for a given field.
     */
    public static Class[] getParameterizedTypes(Field f) {
        if (f == null)
            return null;
        if (VERSION < 5)
            return EMPTY_CLASSES;

        try {
            Object type = Field.class.getMethod("getGenericType",
                (Class[]) null).invoke(f, (Object[]) null);
            return collectParameterizedTypes(type);
        } catch (Exception e) {
            return EMPTY_CLASSES;
        }
    }

    /**
     * Collects the parameterized return type declarations for a given method.
     */
    public static Class[] getParameterizedTypes(Method meth) {
        if (meth == null)
            return null;
        if (VERSION < 5)
            return EMPTY_CLASSES;

        try {
            Object type = Method.class.getMethod("getGenericReturnType",
                (Class[]) null).invoke(meth, (Object[]) null);
            return collectParameterizedTypes(type);
        } catch (Exception e) {
            return EMPTY_CLASSES;
        }
    }

    /**
     * Return all parameterized classes for the given type.
     */
    private static Class[] collectParameterizedTypes(Object type)
        throws Exception {
        if (PARAM_TYPE == null || !PARAM_TYPE.isInstance(type))
            return EMPTY_CLASSES;

        Object[] args = (Object[]) PARAM_TYPE.getMethod
            ("getActualTypeArguments", (Class[]) null).invoke(type,
            (Object[]) null);
        if (args.length == 0)
            return EMPTY_CLASSES;

        Class[] clss = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            if (!(args[i] instanceof Class))
                return EMPTY_CLASSES;
            clss[i] = (Class) args[i];
        }
        return clss;
    }

    /**
     * Transfer the stack from one throwable to another, or return
     * false if it cannot be done, possibly due to an unsupported Java version.
     */
    public static boolean transferStackTrace(Throwable from, Throwable to) {
        if (GET_STACK == null || SET_STACK == null || from == null
 to == null)
            return false;

        try {
            Object stack = GET_STACK.invoke(from, (Object[]) null);
            SET_STACK.invoke(to, new Object[]{ stack });
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Return the cause of the given throwable.
     */
    public static Throwable getCause(Throwable ex) {
        if (GET_CAUSE == null || ex == null)
            return null;

        try {
            return (Throwable) GET_CAUSE.invoke(ex, (Object[]) null);
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * Set the cause of the given throwable.
     */
    public static Throwable initCause(Throwable ex, Throwable cause) {
        if (INIT_CAUSE == null || ex == null || cause == null)
            return ex;

        try {
            return (Throwable) INIT_CAUSE.invoke(ex, new Object[]{ cause });
        } catch (Throwable t) {
            return ex;
        }
    }

    public static void main(String[] args) {
        System.out.println("Java version is: " + VERSION);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10504.java