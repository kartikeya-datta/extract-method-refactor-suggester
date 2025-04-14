error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/196.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/196.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/196.java
text:
```scala
r@@eturn (proxy != null && getInvocationHandler(proxy) instanceof ProxyHandler);

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
package org.apache.aries.proxy.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.apache.aries.proxy.InvocationHandlerWrapper;
import org.apache.aries.proxy.ProxyManager;
import org.apache.aries.proxy.UnableToProxyException;
import org.apache.aries.util.AriesFrameworkUtil;
import org.apache.aries.util.nls.MessageUtil;
import org.osgi.framework.Bundle;

public abstract class AbstractProxyManager implements ProxyManager
{
  public final Object createProxy(Bundle clientBundle, Collection<Class<?>> classes,
      Callable<Object> dispatcher) 
    throws UnableToProxyException
  {
    return createProxy(clientBundle, classes, dispatcher, null);
  }

  public final Object createProxy(Bundle clientBundle, Collection<Class<?>> classes,
      Callable<Object> dispatcher, InvocationHandlerWrapper wrapper)
      throws UnableToProxyException 
  {
    InvocationHandler ih = new ProxyHandler(this, dispatcher, wrapper);
    Object proxyObject = duplicateProxy(classes, ih);
    
    if (proxyObject == null) {
      proxyObject = createNewProxy(clientBundle, classes, ih);
    }
    
    return proxyObject;
  }
  
  public final Callable<Object> unwrap(Object proxy) 
  {
    Callable<Object> target = null;
    
    if (isProxy(proxy)) {
      InvocationHandler ih = getInvocationHandler(proxy);
      
      if (ih instanceof ProxyHandler) {
        target = ((ProxyHandler)ih).getTarget();
      }
    }
    
    return target;
  }
  
  public final boolean isProxy(Object proxy)
  {
    return (getInvocationHandler(proxy) instanceof ProxyHandler);
  }
  
  protected abstract Object createNewProxy(Bundle clientBundle, Collection<Class<?>> classes,
      InvocationHandler ih) throws UnableToProxyException;
  protected abstract InvocationHandler getInvocationHandler(Object proxy);
  protected abstract boolean isProxyClass(Class<?> clazz);

  protected synchronized ClassLoader getClassLoader(final Bundle clientBundle, Collection<Class<?>> classes) 
  {
    if (clientBundle.getState() == Bundle.UNINSTALLED) {
      throw new IllegalStateException(NLS.MESSAGES.getMessage("bundle.uninstalled", clientBundle.getSymbolicName(), clientBundle.getVersion(), clientBundle.getBundleId()));
    }
    
    ClassLoader cl = null;
    
    if (classes.size() == 1) cl = classes.iterator().next().getClassLoader();

    if (cl == null) {
      // First of all see if the AriesFrameworkUtil can get the classloader, if it can we go with that.
      cl = AriesFrameworkUtil.getClassLoaderForced(clientBundle);
    }
    
    return cl;
  }

  private Object duplicateProxy(Collection<Class<?>> classes, InvocationHandler handler)
  {
    Object proxyObject = null;
    
    if (classes.size() == 1) {

      Class<?> classToProxy = classes.iterator().next();

      boolean isProxy = isProxyClass(classToProxy);

      if (isProxy) {
        try {
          /*
           * the class is already a proxy, we should just invoke
           * the constructor to get a new instance of the proxy
           * with a new Collaborator using the specified delegate
           */
          proxyObject = classToProxy.getConstructor(InvocationHandler.class).newInstance(handler);
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        } catch (InstantiationException e) {
        } catch (IllegalArgumentException e) {
        } catch (SecurityException e) {
        } catch (IllegalAccessException e) {
        }
      }
    }
    
    return proxyObject;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/196.java