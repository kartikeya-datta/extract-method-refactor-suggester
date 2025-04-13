error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3547.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3547.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3547.java
text:
```scala
i@@f (method.getName().equals("unwrapObject")

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.blueprint.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Stack;

import org.apache.aries.blueprint.Interceptor;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A collaborator which ensures preInvoke and postInvoke occur before and after
 * method invocation
 */
class Collaborator implements InvocationHandler, Serializable {

    /** Serial version UID for this class */
    private static final long serialVersionUID = -58189302118314469L;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(Collaborator.class);

    /** The invocation handler to call */
    final InvocationHandler delegate;
    final Object object;

    private transient List<Interceptor> interceptors = null;
    private transient ComponentMetadata cm = null;
    private transient boolean sorted = false;

    Collaborator(ComponentMetadata cm, List<Interceptor> interceptors,
            final Object delegateObj) {
        this.cm = cm;
        this.object = delegateObj;
        this.delegate = new InvocationHandler() {
            private void onUnexpectedException(Throwable cause) {
                throw new Error("Unreachable catch statement reached", cause);
            }

            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                Object result;
                try {
                    result = method.invoke(object, args);
                } catch (InvocationTargetException ite) {
                    // We are invisible, so unwrap and throw the cause as
                    // though we called the method directly.
                    throw ite.getCause();
                } catch (IllegalAccessException e) {
                    onUnexpectedException(e);
                    return null;
                } catch (IllegalArgumentException e) {
                    onUnexpectedException(e);
                    return null;
                } catch (SecurityException e) {
                    onUnexpectedException(e);
                    return null;
                }

                return result;
            }
        };
        this.interceptors = interceptors;
    }

    /**
     * Invoke the preCall method on the interceptor
     * 
     * @param cm
     *            : component Metadata
     * @param m
     *            : method
     * @param parameters
     *            : method paramters
     * @throws Throwable
     */
    private void preCallInterceptor(List<Interceptor> interceptorList,
            ComponentMetadata cm, Method m, Object[] parameters,
            Stack<Collaborator.StackElement> calledInterceptors)
            throws Throwable {
        if ((interceptors != null) && !(interceptors.isEmpty())) {
            for (Interceptor im : interceptorList) {
                Collaborator.StackElement se = new StackElement(im);

                // should we do this before or after the preCall ?
                calledInterceptors.push(se);

                // allow exceptions to propagate
                se.setPreCallToken(im.preCall(cm, m, parameters));
            }
        }
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Object toReturn = null;

        // Added method to unwrap from the collaborator.
        if (method.getName().equals("getWrappedObject")
                && method.getDeclaringClass() == WrapperedObject.class) {
            toReturn = object;
        } else
        // Unwrap calls for equals 
        if (method.getName().equals("equals")
                && method.getDeclaringClass() == Object.class) {
            if (args[0] instanceof WrapperedObject) {
                //replace the wrapper with the unwrapped object, to 
                //enable object identity etc to function.
                args[0] = ((WrapperedObject) args[0]).unwrapObject();
            }
            toReturn = delegate.invoke(proxy, method, args);
        } else 
        // Proxy the call through to the delegate, wrapping call in 
        // interceptor invocations.
        {
            Stack<Collaborator.StackElement> calledInterceptors = new Stack<Collaborator.StackElement>();
            boolean inInvoke = false;
            try {
                preCallInterceptor(interceptors, cm, method, args,
                        calledInterceptors);
                inInvoke = true;
                toReturn = delegate.invoke(proxy, method, args);
                inInvoke = false;
                postCallInterceptorWithReturn(cm, method, toReturn,
                        calledInterceptors);

            } catch (Exception e) {
                // log the exception e
                LOGGER.error("invoke", e);

                // if we catch an exception we decide carefully which one to
                // throw onwards
                Exception exceptionToRethrow = null;
                // if the exception came from a precall or postcall interceptor
                // we will rethrow it
                // after we cycle through the rest of the interceptors using
                // postCallInterceptorWithException
                if (!inInvoke) {
                    exceptionToRethrow = e;
                }
                // if the exception didn't come from precall or postcall then it
                // came from invoke
                // we will rethrow this exception if it is not a runtime
                // exception
                else {
                    if (!(e instanceof RuntimeException)) {
                        exceptionToRethrow = e;
                    }
                }
                try {
                    postCallInterceptorWithException(cm, method, e,
                            calledInterceptors);
                } catch (Exception f) {
                    // we caught an exception from
                    // postCallInterceptorWithException
                    // logger.catching("invoke", f);
                    // if we haven't already chosen an exception to rethrow then
                    // we will throw this exception
                    if (exceptionToRethrow == null) {
                        exceptionToRethrow = f;
                    }
                }
                // if we made it this far without choosing an exception we
                // should throw e
                if (exceptionToRethrow == null) {
                    exceptionToRethrow = e;
                }
                throw exceptionToRethrow;
            }
        }
        return toReturn;
    }

    /**
     * Called when the method is called and returned normally
     * 
     * @param cm
     *            : component metadata
     * @param method
     *            : method
     * @param returnType
     *            : return type
     * @throws Throwable
     */
    private void postCallInterceptorWithReturn(ComponentMetadata cm,
            Method method, Object returnType,
            Stack<Collaborator.StackElement> calledInterceptors)
            throws Throwable {

        while (!calledInterceptors.isEmpty()) {
            Collaborator.StackElement se = calledInterceptors.pop();
            try {
                se.interceptor.postCallWithReturn(cm, method, returnType, se
                        .getPreCallToken());
            } catch (Throwable t) {
                LOGGER.error("postCallInterceptorWithReturn", t);
                // propagate this to invoke ... further interceptors will be
                // called via the postCallInterceptorWithException method
                throw t;
            }
        } // end while
    }

    /**
     * Called when the method is called and returned with an exception
     * 
     * @param cm
     *            : component metadata
     * @param method
     *            : method
     * @param exception
     *            : exception throwed
     */
    private void postCallInterceptorWithException(ComponentMetadata cm,
            Method method, Exception exception,
            Stack<Collaborator.StackElement> calledInterceptors)
            throws Throwable {
        Throwable tobeRethrown = null;
        while (!calledInterceptors.isEmpty()) {
            Collaborator.StackElement se = calledInterceptors.pop();

            try {
                se.interceptor.postCallWithException(cm, method, exception, se
                        .getPreCallToken());
            } catch (Throwable t) {
                // log the exception
                LOGGER.error("postCallInterceptorWithException", t);
                if (tobeRethrown == null)
                    tobeRethrown = t;
            }

        } // end while

        if (tobeRethrown != null)
            throw tobeRethrown;
    }

    // info to store on interceptor stack during invoke
    private static class StackElement {
        private Interceptor interceptor;
        private Object preCallToken;

        private StackElement(Interceptor i) {
            interceptor = i;
        }

        private void setPreCallToken(Object preCallToken) {
            this.preCallToken = preCallToken;
        }

        private Object getPreCallToken() {
            return preCallToken;
        }

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3547.java