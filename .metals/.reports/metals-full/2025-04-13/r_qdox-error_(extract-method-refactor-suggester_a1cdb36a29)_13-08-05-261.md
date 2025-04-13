error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3730.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3730.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3730.java
text:
```scala
i@@f (provider == null) context.ungetService(ref); // we didn't get something back, so this was no good.

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
package org.apache.aries.jndi;

import java.security.PrivilegedExceptionAction;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NoInitialContextException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.ObjectFactory;

import org.apache.aries.jndi.startup.Activator;
import org.apache.aries.jndi.tracker.ServiceTrackerCustomizers;
import org.apache.aries.jndi.urls.URLObjectFactoryFinder;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Provides helper methods for the DelegateContext. This provides the methods so
 * there can be many DelegateContexts, but few service trackers.
 */
public final class ContextHelper {

    /** Ensure no one constructs us */
    private ContextHelper() {
        throw new RuntimeException();
    }

    public static ContextProvider createURLContext(final BundleContext context,
                                           final String urlScheme, 
                                           final Hashtable<?, ?> env)
        throws NamingException {
        return Utils.doPrivilegedNaming(new PrivilegedExceptionAction<ContextProvider>() {
            public ContextProvider run() throws Exception {
                return doCreateURLContext(context, urlScheme, env);
            }
        });
    }
    
    /**
     * This method is used to create a URL Context. It does this by looking for
     * the URL context's ObjectFactory in the service registry.
     * 
     * @param urlScheme
     * @param env
     * @return a Context
     * @throws NamingException
     */
    private static ContextProvider doCreateURLContext(BundleContext context, String urlScheme, Hashtable<?, ?> env)
        throws NamingException {
      
        ServicePair<ObjectFactory> urlObjectFactory = getURLObjectFactory(context, urlScheme, env);
        
        if (urlObjectFactory != null) {
            ObjectFactory factory = urlObjectFactory.get();
            
            if (factory != null) {
                try {
                    Context ctx = (Context) factory.getObjectInstance(null, null, null, env);
                    
                    return new ContextProvider(context, urlObjectFactory.getReference(), ctx);
                } catch (Exception e) {
                    urlObjectFactory.unget();
                    NamingException e2 = new NamingException();
                    e2.initCause(e);
                    throw e2;
                }
            }
        }

        // if we got here then we couldn't find a URL context factory so return null.
        return null;
    }
    
    public static final ServicePair<ObjectFactory> getURLObjectFactory(BundleContext ctx, String urlScheme, Hashtable<?, ?> environment)
      throws NamingException
    {
      ServicePair<ObjectFactory> result = null;
      
      ServiceReference ref = ServiceTrackerCustomizers.URL_FACTORY_CACHE.find(urlScheme);
      
      if (ref == null) {
        ServiceReference[] refs = Activator.getURLObectFactoryFinderServices();
        
        if (refs != null) {
          for (ServiceReference finderRef : refs) {
            URLObjectFactoryFinder finder = (URLObjectFactoryFinder) ctx.getService(finderRef);
            if (finder != null) {
              ObjectFactory f = finder.findFactory(urlScheme, environment);
              
              if (f != null) {
                result = new ServicePair<ObjectFactory>(ctx, finderRef, f);
                break;
              } else {
                ctx.ungetService(finderRef);
              }
            }
          }
        }
      } else {
        result = new ServicePair<ObjectFactory>(ctx, ref);
      }
      
      return result;
    }
        
    public static Context getInitialContext(BundleContext context, Hashtable<?, ?> environment)
        throws NamingException {
        ContextProvider provider = getContextProvider(context, environment);
        
        if (provider != null) {
          return new DelegateContext(context, provider);
        } else {
          String contextFactoryClass = (String) environment.get(Context.INITIAL_CONTEXT_FACTORY);
          if (contextFactoryClass == null) {
            return new DelegateContext(context, environment);
          } else {
            throw new NoInitialContextException("We could not find a provider for the InitialContextFactory " + contextFactoryClass);
          }
        }
    }

    public static ContextProvider getContextProvider(final BundleContext context,
                                                     final Hashtable<?, ?> environment)
        throws NamingException {
        return Utils.doPrivilegedNaming(new PrivilegedExceptionAction<ContextProvider>() {
            public ContextProvider run() throws Exception {
                return doGetContextProvider(context, environment);
            }
        });
    }
    
    private static ContextProvider doGetContextProvider(BundleContext context,
                                                        Hashtable<?, ?> environment)
        throws NamingException {
        ContextProvider provider = null;
        String contextFactoryClass = (String) environment.get(Context.INITIAL_CONTEXT_FACTORY);
        if (contextFactoryClass == null) {
            // 1. get ContextFactory using builder
            provider = getInitialContextUsingBuilder(context, environment);

            // 2. lookup all ContextFactory services
            if (provider == null) {
                ServiceReference[] references = Activator.getInitialContextFactoryServices();
                if (references != null) {
                    Context initialContext = null;
                    for (ServiceReference reference : references) {
                        InitialContextFactory factory = (InitialContextFactory) context.getService(reference);
                        try {
                            initialContext = factory.getInitialContext(environment);
                            if (initialContext != null) {
                              provider = new ContextProvider(context, reference, initialContext);
                              break;
                          }
                        } finally {
                            if (provider == null) context.ungetService(reference);
                        }
                    }
                }
            }
        } else {
            ServiceReference ref = ServiceTrackerCustomizers.ICF_CACHE.find(contextFactoryClass);
            
            if (ref != null) {
              Context initialContext = null;
              InitialContextFactory factory = (InitialContextFactory) context.getService(ref);
              if (factory != null) {
                try {
                    initialContext = factory.getInitialContext(environment);
                    provider = new ContextProvider(context, ref, initialContext);
                } finally {
                    if (provider == null) context.ungetService(ref);
                }
              }
            }
            
            // 2. get ContextFactory using builder
            if (provider == null) {
                provider = getInitialContextUsingBuilder(context, environment);
            }
        }
        
        return provider;
    }

    private static ContextProvider getInitialContextUsingBuilder(BundleContext context,
                                                                 Hashtable<?, ?> environment)
            throws NamingException {
        ContextProvider provider = null;
        ServiceReference[] refs = Activator.getInitialContextFactoryBuilderServices();
        if (refs != null) {
            InitialContextFactory factory = null;
            for (ServiceReference ref : refs) {                    
                InitialContextFactoryBuilder builder = (InitialContextFactoryBuilder) context.getService(ref);
                try {
                  factory = builder.createInitialContextFactory(environment);
                } catch (NamingException ne) {
                  // TODO: log
                  // ignore this, if the builder fails we want to move onto the next one
                }
                
                if (factory != null) {
                  try {
                    provider = new ContextProvider(context, ref, factory.getInitialContext(environment));
                  } finally {
                    context.ungetService(ref); // we didn't get something back, so this was no good.
                  }
                  break;
                } else {
                  context.ungetService(ref); // we didn't get something back, so this was no good.
                }
            }
        }
        return provider;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3730.java