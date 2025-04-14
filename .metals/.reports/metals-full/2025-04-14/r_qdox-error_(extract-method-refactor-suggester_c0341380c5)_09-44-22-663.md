error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1863.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1863.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1863.java
text:
```scala
r@@eturn (Class<? extends ListResourceBundle>) b.loadClass(bundleName);

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
package org.apache.aries.util.nls;

import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * This is a helper class for loading messages for logging and exception messages. It supports translating the message into the
 * default Locale. It works out the calling Bundle and uses it to load any resources. If the resource bundle is of type
 * java.properties then it tries to find the bundle first via the bundle getResources method, then via the getEntry method. If it
 * is of type java.class then it'll use the bundle to load the class.
 */
public final class MessageUtil
{
  /** The resource bundle used to translate messages */
  private ResourceBundle messages;
  private static final StackFinder finder;

  /** 
   * One of the static methods needs to obtain the caller, so we cheat and use a SecurityManager to get the 
   * classes on the stack.
   */
  private static class StackFinder extends SecurityManager
  {
    @Override
    public Class<?>[] getClassContext()
    {
      return super.getClassContext();
    }
  }
  
  static 
  {
    finder = AccessController.doPrivileged(new PrivilegedAction<StackFinder>() {
      public StackFinder run()
      {
        return new StackFinder();
      }
    });
  }

  private MessageUtil(ResourceBundle b)
  {
    messages = b;
  }

  /**
   * This method translates the message and puts the inserts into the message before returning it.
   * If the message key does not exist then the key itself is returned.
   * 
   * @param key     the message key.
   * @param inserts the inserts into the resolved message.
   * @return the message in the correct language, or the key if the message does not exist.
   */
  public String getMessage(String key, Object ... inserts)
  {
    String message;

    try {
      message = messages.getString(key);

      if (inserts != null && inserts.length > 0) {
        message = MessageFormat.format(message, inserts);
      }
    } catch (MissingResourceException e) {
      message = key;
    }

    return message;
  }

  /**
   * Loads the MessageUtil using the given context. It resolves the Class to an OSGi bundle.
   * 
   * @param context  the bundle this class is in will be used to resolve the base name.
   * @param baseName the resource bundle base name
   * @return the message util instance.
   * @throws MissingResourceException If the resource bundle cannot be located
   */
  public static MessageUtil createMessageUtil(Class<?> context, String baseName)
  {
    return createMessageUtil(FrameworkUtil.getBundle(context), baseName);
  }

  /**
   * This method uses the Bundle associated with the caller of this method.
   * 
   * @param baseName the resource bundle base name
   * @return the message util instance.
   * @throws MissingResourceException If the resource bundle cannot be located
   */
  public static MessageUtil createMessageUtil(String baseName)
  {
    Class<?>[] stack = finder.getClassContext();

    for (Class<?> clazz : stack) {
      if (clazz != MessageUtil.class) {
        return createMessageUtil(clazz, baseName);
      }
    }

    throw new MissingResourceException(org.apache.aries.util.internal.MessageUtil.getMessage("UTIL0014E", baseName), baseName, null);
  }

  /**
   * This method loads the resource bundle backing the MessageUtil from the provided Bundle.
   * 
   * @param b        the bundle to load the resource bundle from
   * @param baseName the resource bundle base name
   * @return the message util instance.
   * @throws MissingResourceException If the resource bundle cannot be located
   */
  public static MessageUtil createMessageUtil(final Bundle b, String baseName)
  {
    ResourceBundle rb;
    
    if (b == null) {
      // if the bundle is null we are probably outside of OSGi, so just use non-OSGi resolve rules.
      rb = ResourceBundle.getBundle(baseName);
    } else {
      rb = ResourceBundle.getBundle(baseName, new ResourceBundle.Control() {
  
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format,
            ClassLoader loader, boolean reload) throws IllegalAccessException,
            InstantiationException, IOException
        {
          ResourceBundle result;
  
          final String bundleName = toBundleName(baseName, locale);
  
          if (FORMAT_PROPERTIES.contains(format)) {
            final String resourceName = toResourceName(bundleName, "properties");
            
            URL url = AccessController.doPrivileged(new PrivilegedAction<URL>() {
              public URL run()
              {
                URL url = b.getResource(resourceName);
                
                if (url == null) {
                  url = b.getEntry(resourceName);
                }
                return url;
              }
            });
            
            if (url != null) {
              result = new PropertyResourceBundle(url.openStream());
            } else {
              result = null;
            }
          } else if (FORMAT_CLASS.contains(format)) {
            @SuppressWarnings("unchecked")
            Class<? extends ListResourceBundle> clazz = AccessController.doPrivileged(new PrivilegedAction<Class<? extends ListResourceBundle>>() {
              public Class<? extends ListResourceBundle> run()
              {
                try {
                  return b.loadClass(bundleName);
                } catch (ClassNotFoundException e) {
                  return null;
                }
              }
            });
            
            if (clazz != null) {
              result = clazz.newInstance();
            } else {
              result = null;
            }
          } else {
            throw new IllegalArgumentException(org.apache.aries.util.internal.MessageUtil.getMessage("UTIL0013E", format));
          }
  
          return result;
        }
    
      });
    }
    
    return new MessageUtil(rb);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1863.java