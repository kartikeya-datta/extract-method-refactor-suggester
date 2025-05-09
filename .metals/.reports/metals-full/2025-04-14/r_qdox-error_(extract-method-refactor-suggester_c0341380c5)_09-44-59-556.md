error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1260.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1260.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1260.java
text:
```scala
public S@@erviceRegistryContext(Hashtable<?, ?> environment)

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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;

import org.apache.aries.jndi.services.ServiceHelper;

/**
 * A JNDI context for looking stuff up from the service registry.
 */
public class ServiceRegistryContext implements Context
{
  private static final String ARIES_SERVICES = "aries:services/";
  /** The name parser for the service registry name space */
  private NameParser parser = new ServiceRegistryNameParser();
  /** The environment for this context */
  private Map<Object, Object> env;
  
  /**
   * Why Mr Java this class does indeed take a fine copy of the provided 
   * environment. One might imagine that it is worried that the provider is
   * not to be trusted.
   * 
   * @param environment
   */
  public ServiceRegistryContext(@SuppressWarnings("unused") Hashtable<?, ?> environment)
  {
    env = new HashMap<Object, Object>();
    env.putAll(environment);
  }

  public Object addToEnvironment(String propName, Object propVal) throws NamingException
  {
    return env.put(propName, propVal);
  }

  public void bind(Name name, Object obj) throws NamingException
  {
    throw new OperationNotSupportedException();
  }

  public void bind(String name, Object obj) throws NamingException
  {
    throw new OperationNotSupportedException();
  }

  public void close() throws NamingException
  {
    env = null;
    parser = null;
  }

  public Name composeName(Name name, Name prefix) throws NamingException
  {
    String result = prefix + "/" + name;

    String ns = ARIES_SERVICES;
    
    if (result.startsWith(ns)) {
      ns = "";
    }
    
    return parser.parse(ns + result);
  }

  public String composeName(String name, String prefix) throws NamingException
  {
    String result = prefix + "/" + name;

    String ns = ARIES_SERVICES;
    
    if (result.startsWith(ns)) {
      ns = "";
    }
    
    parser.parse(ns + result);
    
    return result;
  }

  public Context createSubcontext(Name name) throws NamingException
  {
    throw new OperationNotSupportedException();
  }

  public Context createSubcontext(String name) throws NamingException
  {
    throw new OperationNotSupportedException();
  }

  public void destroySubcontext(Name name) throws NamingException
  {
    //No-op we don't support sub-contexts in our context   
  }

  public void destroySubcontext(String name) throws NamingException
  {
    //No-op we don't support sub-contexts in our context
    
  }

  public Hashtable<?, ?> getEnvironment() throws NamingException
  {
    Hashtable<Object, Object> environment = new Hashtable<Object, Object>();
    environment.putAll(env);
    return environment;
  }

  public String getNameInNamespace() throws NamingException
  {
    throw new OperationNotSupportedException();
  }

  public NameParser getNameParser(Name name) throws NamingException
  {
    return parser;
  }

  public NameParser getNameParser(String name) throws NamingException
  {
    return parser;
  }

  public NamingEnumeration<NameClassPair> list(final Name name) throws NamingException
  {
    int nameSize = validateName(name);
    
    String className = name.get(0);
    String filter = null;
    
    if (nameSize == 2) {
      filter = name.get(1);
    }
    
    try {
      final Iterator<?> it = ServiceHelper.getServices(className, filter).iterator();
      
      return new NamingEnumeration<NameClassPair>() {
        public void close() throws NamingException
        {
          // TODO return stuff to the registry, maybe???
        }

        public boolean hasMore()
        {
          return it.hasNext();
        }

        public NameClassPair next()
        {
          return nextElement();
        }

        public boolean hasMoreElements()
        {
          return it.hasNext();
        }

        public NameClassPair nextElement()
        {
          return new NameClassPair(name.toString(), it.next().getClass().getCanonicalName(), false);
        }
      };
    } catch (IllegalArgumentException e) {
      NamingException ne = new NamingException(e.getMessage());
      ne.initCause(e);
      throw ne;
    }  }

  public NamingEnumeration<NameClassPair> list(String name) throws NamingException
  {
    return list(parser.parse(name));
  }

  public NamingEnumeration<Binding> listBindings(final Name name) throws NamingException
  {
    String className = null;
    String filter = null;
    
    int nameSize = validateName(name);
    
    className = name.get(0);

    if (nameSize == 2) {
      filter = name.get(1);
    }
    
    try {
      final Iterator<?> it = ServiceHelper.getServices(className, filter).iterator();
      
      return new NamingEnumeration<Binding>() {
        public void close() throws NamingException
        {
          // TODO return stuff to the registry, maybe???
        }

        public boolean hasMore()
        {
          return it.hasNext();
        }

        public Binding next()
        {
          return nextElement();
        }

        public boolean hasMoreElements()
        {
          return it.hasNext();
        }

        public Binding nextElement()
        {
          return new Binding(name.toString(), it.next(), false);
        }
      };
    } catch (IllegalArgumentException e) {
      NamingException ne = new NamingException(e.getMessage());
      ne.initCause(e);
      throw ne;
    }
  }

  public NamingEnumeration<Binding> listBindings(String name) throws NamingException
  {
    return listBindings(parser.parse(name));
  }

  public Object lookup(Name name) throws NamingException
  {
    int nameSize = validateName(name);
    String className = name.get(0);

    String filter = null;
    
    if (nameSize == 2) {
      filter = name.get(1);
    }
    
    try {
      Object result = ServiceHelper.getService(className, filter);
      
      if (result == null) {
        throw new NameNotFoundException("We couldn't find an object in the registry matching the query " + name);
      }
      
      return result;
    } catch (IllegalArgumentException e) {
      NamingException ne = new NamingException(e.getMessage());
      ne.initCause(e);
      throw ne;
    }
  }

  public Object lookup(String name) throws NamingException
  {
    return lookup(parser.parse(name));
  }

  public Object lookupLink(Name name) throws NamingException
  {
    throw new OperationNotSupportedException();
  }

  public Object lookupLink(String name) throws NamingException
  {
    throw new OperationNotSupportedException();
  }

  public void rebind(Name name, Object obj) throws NamingException
  {
    throw new OperationNotSupportedException();
  }

  public void rebind(String name, Object obj) throws NamingException
  {
    throw new OperationNotSupportedException();
  }

  public Object removeFromEnvironment(String propName) throws NamingException
  {
    return env.remove(propName);
  }

  public void rename(Name oldName, Name newName) throws NamingException
  {
    throw new OperationNotSupportedException();
  }

  public void rename(String oldName, String newName) throws NamingException
  {
    throw new OperationNotSupportedException();
  }

  public void unbind(Name name) throws NamingException
  {
    throw new OperationNotSupportedException();
  }

  public void unbind(String name) throws NamingException
  {
    throw new OperationNotSupportedException();
  }
  
  /**
   * Check that the name conforms to the expected format
   * @param name
   * @return the size of the name
   * @throws InvalidNameException
   */
  private int validateName(final Name name) throws InvalidNameException
  {
    int nameSize = name.size();
    if (nameSize == 0) {
      throw new InvalidNameException("The provided name does not have any components" + name);
    } 
    
    if ("aries:services".equals(name.get(0))) {
      throw new InvalidNameException("The composite name should not start with aries:services");
    }
    
    if (nameSize > 2) {
      throw new InvalidNameException("This JNDI context only expects 2 components, but it found " + nameSize);
    }
    return nameSize;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1260.java