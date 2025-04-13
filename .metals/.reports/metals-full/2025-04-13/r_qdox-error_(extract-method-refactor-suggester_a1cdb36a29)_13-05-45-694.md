error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3311.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3311.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3311.java
text:
```scala
r@@esult = ServiceHelper.getService(parentName, name, false, env);

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
package org.apache.aries.jndi.url;

import java.util.Map;
import java.util.NoSuchElementException;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.apache.aries.jndi.services.ServiceHelper;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

public class ServiceRegistryListContext extends AbstractServiceRegistryContext implements Context
{
  /** The osgi lookup name **/
  private OsgiName parentName;
  
  private interface ThingManager<T>
  {
    public T get(BundleContext ctx, ServiceReference ref);
    public void release(BundleContext ctx, ServiceReference ref);
  }
  
  private static class ServiceNamingEnumeration<T> implements NamingEnumeration<T>
  {
    private BundleContext ctx;
    private ServiceReference[] refs;
    private int position = 0;
    private ThingManager<T> mgr;
    private T last;
    
    private ServiceNamingEnumeration(BundleContext context, ServiceReference[] theRefs, ThingManager<T> manager)
    {
      ctx = context;
      refs = theRefs;
      mgr = manager;
    }
    
    public void close() throws NamingException
    {
      mgr.release(ctx, refs[position - 1]);
      last = null;
    }

    public boolean hasMore() throws NamingException
    {
      return hasMoreElements();
    }

    public T next() throws NamingException
    {
      return nextElement();
    }

    public boolean hasMoreElements()
    {
      return position < refs.length;
    }

    public T nextElement()
    {
      if (!!!hasMoreElements()) throw new NoSuchElementException();
      
      if (position > 0) mgr.release(ctx, refs[position - 1]);
      
      last = mgr.get(ctx, refs[position++]);
      
      return last;
    }
    
  }
  
  public ServiceRegistryListContext(Map<String, Object> env, OsgiName validName)
  {
    super(env);
    parentName = validName;
  }

  public NamingEnumeration<NameClassPair> list(Name name) throws NamingException
  {
    return list(name.toString());
  }

  public NamingEnumeration<NameClassPair> list(String name) throws NamingException
  {
    if (!!!"".equals(name)) throw new NameNotFoundException(name);
    
    final BundleContext ctx = ServiceHelper.getBundleContext(env);
    final ServiceReference[] refs = ServiceHelper.getServiceReferences(parentName.getInterface(), parentName.getFilter(), parentName.getServiceName(), env);
    
    return new ServiceNamingEnumeration<NameClassPair>(ctx, refs, new ThingManager<NameClassPair>() {
      public NameClassPair get(BundleContext ctx, ServiceReference ref)
      {
        String serviceId = String.valueOf(ref.getProperty(Constants.SERVICE_ID));
        String className = null;
        Object service = ctx.getService(ref);
        if (service != null) {
          className = service.getClass().getName();
        }

        ctx.ungetService(ref);
        
        return new NameClassPair(serviceId, className, true);
      }

      public void release(BundleContext ctx, ServiceReference ref)
      {
      }
    });
  }

  public NamingEnumeration<Binding> listBindings(Name name) throws NamingException
  {
    return listBindings(name.toString());
  }

  public NamingEnumeration<Binding> listBindings(String name) throws NamingException
  {
    if (!!!"".equals(name)) throw new NameNotFoundException(name);
    
    final BundleContext ctx = ServiceHelper.getBundleContext(env);
    final ServiceReference[] refs = ServiceHelper.getServiceReferences(parentName.getInterface(), parentName.getFilter(), parentName.getServiceName(), env);

    return new ServiceNamingEnumeration<Binding>(ctx, refs, new ThingManager<Binding>() {
      public Binding get(BundleContext ctx, ServiceReference ref)
      {
        String serviceId = String.valueOf(ref.getProperty(Constants.SERVICE_ID));
        
        Object service = ServiceHelper.getService(ctx, ref);

        return new Binding(serviceId, service, true);
      }

      public void release(BundleContext ctx, ServiceReference ref)
      {
        ctx.ungetService(ref);
      }
    });
  }

  public Object lookup(Name name) throws NamingException
  {
    return lookup(name.toString());
  }

  public Object lookup(String name) throws NamingException
  {
    Object result = null;
    
    result = ServiceHelper.getService(parentName.getInterface(), parentName.getFilter(), parentName.getServiceName(), name, false, env);
    
    if (result == null) {
      throw new NameNotFoundException(name.toString());
    }
    
    return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3311.java