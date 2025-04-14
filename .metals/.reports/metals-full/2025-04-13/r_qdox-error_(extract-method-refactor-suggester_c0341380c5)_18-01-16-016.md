error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/144.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/144.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/144.java
text:
```scala
p@@ackageName = "org.apache.aries.blueprint.proxy." + packageName;

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

import static java.lang.reflect.Modifier.isFinal;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxySubclassGenerator
{

  private final static Logger LOGGER = LoggerFactory.getLogger(ProxySubclassGenerator.class);

  // This map holds references to the names of classes created by this Class
  // It is a weak map (so when a ClassLoader is garbage collected we remove
  // the map of
  // Class names to sub-Class names)
  private static final Map<ClassLoader, ConcurrentMap<String, String>> proxyClassesByClassLoader;

  static {
    // Ensure that this is a synchronized map as we may use it from multiple
    // threads concurrently
    //
    proxyClassesByClassLoader = Collections
        .synchronizedMap(new WeakHashMap<ClassLoader, ConcurrentMap<String, String>>());
  }

  private static final char FINAL_MODIFIER = '!';
  private static final char UNABLE_TO_PROXY = '#';

  public static Class<?> getProxySubclass(Class<?> aClass) throws UnableToProxyException
  {
    LOGGER.debug(AsmInterceptorWrapper.LOG_ENTRY, "getProxySubclass", new Object[] { aClass });

    ClassLoader loader = aClass.getClassLoader();
    // in the special case where the loader is null we use the thread
    // ContextClassLoader
    // this is for subclassing java.* or javax.* packages
    if (loader == null) loader = Thread.currentThread().getContextClassLoader();

    ConcurrentMap<String, String> proxyMap;
    synchronized (loader) {
      proxyMap = proxyClassesByClassLoader.get(loader);
      if (proxyMap == null) {
        proxyMap = new ConcurrentHashMap<String, String>();
        proxyClassesByClassLoader.put(loader, proxyMap);
      }
    }

    // check the map to see if we have already generated a subclass for this
    // class
    // if we have return the mapped class object
    // if we haven't generate the subclass and return it
    Class<?> classToReturn = null;
    synchronized (aClass) {
      String key = aClass.getName();
      String className = proxyMap.get(key);
      if (className != null) {

        LOGGER.debug("Found proxy subclass with key {} and name {}.", key, className);

        if (className.charAt(0) == FINAL_MODIFIER) {
          String[] exceptionParts = className.substring(1).split(":");
          if (exceptionParts.length == 1) {
            throw new FinalModifierException(aClass);
          } else {
            throw new FinalModifierException(aClass, exceptionParts[1]);
          }
        } else if (className.charAt(0) == UNABLE_TO_PROXY) {
          throw new UnableToProxyException(aClass);
        }

        try {
          classToReturn = loader.loadClass(className);
        } catch (ClassNotFoundException cnfe) {
          LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, cnfe);
          throw new UnableToLoadProxyException(className, cnfe);
        }
      } else {

        LOGGER.debug("Need to generate subclass. Using key {}.", key);
        try {
          scanForFinalModifiers(aClass);

          classToReturn = generateAndLoadSubclass(aClass, loader);

          if (classToReturn != null) {
            proxyMap.put(key, classToReturn.getName());
          } else {
            proxyMap.put(key, UNABLE_TO_PROXY + aClass.getName());
            throw new UnableToProxyException(aClass);
          }
        } catch (FinalModifierException e) {
          if (e.isFinalClass()) {
            proxyMap.put(key, FINAL_MODIFIER + e.getClassName());
            throw e;
          } else {
            proxyMap.put(key, FINAL_MODIFIER + e.getClassName() + ':' + e.getFinalMethods());
            throw e;
          }
        }

      }
    }

    LOGGER.debug(AsmInterceptorWrapper.LOG_EXIT, "getProxySubclass", classToReturn);

    return classToReturn;
  }

  public static Object newProxySubclassInstance(Class<?> classToProxy, InvocationHandler ih)
      throws UnableToProxyException
  {

    LOGGER.debug(AsmInterceptorWrapper.LOG_ENTRY, "newProxySubclassInstance", new Object[] {
        classToProxy, ih });

    Object proxySubclassInstance = null;
    try {
      Class<?> generatedProxySubclass = getProxySubclass(classToProxy);
      LOGGER.debug("Getting the proxy subclass constructor");
      Constructor<?> subclassConstructor = generatedProxySubclass
          .getConstructor(new Class[] { InvocationHandler.class });
      LOGGER.debug("Invoking the proxy subclass constructor");
      proxySubclassInstance = subclassConstructor.newInstance(ih);
      LOGGER.debug("Invoked proxy subclass constructor");
    } catch (NoSuchMethodException nsme) {
      LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, nsme);
      throw new ProxyClassInstantiationException(classToProxy, nsme);
    } catch (InvocationTargetException ite) {
      LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, ite);
      throw new ProxyClassInstantiationException(classToProxy, ite);
    } catch (InstantiationException ie) {
      LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, ie);
      throw new ProxyClassInstantiationException(classToProxy, ie);
    } catch (IllegalAccessException iae) {
      LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, iae);
      throw new ProxyClassInstantiationException(classToProxy, iae);
    }

    LOGGER.debug(AsmInterceptorWrapper.LOG_EXIT, "newProxySubclassInstance", proxySubclassInstance);

    return proxySubclassInstance;
  }

  private static Class<?> generateAndLoadSubclass(Class<?> aClass, ClassLoader loader)
      throws UnableToProxyException
  {
    LOGGER.debug(AsmInterceptorWrapper.LOG_ENTRY, "generateAndLoadSubclass", new Object[] { aClass,
        loader });

    // set the newClassName
    String newClassName = "$" + aClass.getSimpleName() + aClass.hashCode();
    String packageName = aClass.getPackage().getName();
    if (packageName.startsWith("java.") || packageName.startsWith("javax.")) {
      packageName = "com.ibm.osgi.blueprint.proxy." + packageName;
    }
    String fullNewClassName = (packageName + "." + newClassName).replaceAll("\\.", "/");

    LOGGER.debug("New class name: {}", newClassName);
    LOGGER.debug("Full new class name: {}", fullNewClassName);

    Class<?> clazz = null;
    try {
      ClassReader cReader = new ClassReader(loader.getResourceAsStream(aClass.getName().replaceAll(
          "\\.", "/")
          + ".class"));
      ClassWriter cWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
      ClassVisitor dynamicSubclassAdapter = new ProxySubclassAdapter(cWriter, fullNewClassName,
          loader);
      byte[] byteClassData = processClass(cReader, cWriter, dynamicSubclassAdapter);
      clazz = loadClassFromBytes(loader, getBinaryName(fullNewClassName), byteClassData, aClass
          .getName());
    } catch (IOException ioe) {
      LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, ioe);
      throw new ProxyClassBytecodeGenerationException(aClass.getName(), ioe);
    } catch (TypeNotPresentException tnpe) {
      LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, tnpe);
      throw new ProxyClassBytecodeGenerationException(tnpe.typeName(), tnpe.getCause());
    }

    LOGGER.debug(AsmInterceptorWrapper.LOG_EXIT, "generateAndLoadSubclass", clazz);

    return clazz;
  }

  private static byte[] processClass(ClassReader cReader, ClassWriter cWriter, ClassVisitor cVisitor)
  {
    LOGGER.debug(AsmInterceptorWrapper.LOG_ENTRY, "processClass", new Object[] { cReader, cWriter,
        cVisitor });

    cReader.accept(cVisitor, ClassReader.SKIP_DEBUG);
    byte[] byteClassData = cWriter.toByteArray();

    LOGGER.debug(AsmInterceptorWrapper.LOG_EXIT, "processClass", byteClassData);

    return byteClassData;
  }

  private static String getBinaryName(String name)
  {
    LOGGER.debug(AsmInterceptorWrapper.LOG_ENTRY, "getBinaryName", name);

    String binaryName = name.replaceAll("/", "\\.");

    LOGGER.debug(AsmInterceptorWrapper.LOG_EXIT, "getBinaryName", binaryName);

    return binaryName;
  }

  private static Class<?> loadClassFromBytes(ClassLoader loader, String name, byte[] classData,
      String classToProxyName) throws UnableToProxyException
  {
    LOGGER.debug(AsmInterceptorWrapper.LOG_ENTRY, "loadClassFromBytes", new Object[] { loader, name,
        classData });

    Class<?> clazz = null;
    try {
      Method defineClassMethod = Class.forName("java.lang.ClassLoader").getDeclaredMethod(
          "defineClass", String.class, byte[].class, int.class, int.class, ProtectionDomain.class);
      defineClassMethod.setAccessible(true);
      // define the class in the same classloader where aClass is loaded,
      // but use the protection domain of our code
      clazz = (Class<?>) defineClassMethod.invoke(loader, name, classData, 0, classData.length,
          ProxySubclassGenerator.class.getProtectionDomain());
      defineClassMethod.setAccessible(false);
    } catch (ClassNotFoundException cnfe) {
      LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, cnfe);
      throw new ProxyClassDefinitionException(classToProxyName, cnfe);
    } catch (NoSuchMethodException nsme) {
      LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, nsme);
      throw new ProxyClassDefinitionException(classToProxyName, nsme);
    } catch (InvocationTargetException ite) {
      LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, ite);
      throw new ProxyClassDefinitionException(classToProxyName, ite);
    } catch (IllegalAccessException iae) {
      LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, iae);
      throw new ProxyClassDefinitionException(classToProxyName, iae);
    }

    LOGGER.debug(AsmInterceptorWrapper.LOG_EXIT, "loadClassFromBytes", clazz);

    return clazz;
  }

  public static boolean isProxySubclass(Class<?> aClass)
  {
    LOGGER.debug(AsmInterceptorWrapper.LOG_ENTRY, "isProxySubclass", new Object[] { aClass });

    // We will always have a proxy map for the class loader of any proxy
    // class, so if
    // this is null we know to return false
    Map<String, String> proxies = proxyClassesByClassLoader.get(aClass.getClassLoader());

    boolean isProxySubclass = (proxies != null && proxies.containsValue(aClass.getName()));

    LOGGER.debug(AsmInterceptorWrapper.LOG_EXIT, "isProxySubclass", isProxySubclass);

    return isProxySubclass;
  }

  private static void scanForFinalModifiers(Class<?> clazz) throws FinalModifierException
  {
    LOGGER.debug(AsmInterceptorWrapper.LOG_ENTRY, "scanForFinalModifiers", new Object[] { clazz });

    if (isFinal(clazz.getModifiers())) {
      throw new FinalModifierException(clazz);
    }

    List<String> finalMethods = new ArrayList<String>();

    // we don't want to check for final methods on java.* or javax.* Class
    // also, clazz can never be null here (we will always hit
    // java.lang.Object first)
    while (!clazz.getName().startsWith("java.") && !clazz.getName().startsWith("javax.")) {
      for (Method m : clazz.getDeclaredMethods()) {
        if (isFinal(m.getModifiers())) {
          finalMethods.add(m.toGenericString());
        }
      }
      clazz = clazz.getSuperclass();
    }

    if (!finalMethods.isEmpty()) {

      String methodList = finalMethods.toString();
      methodList = methodList.substring(1, methodList.length() - 1);
      throw new FinalModifierException(clazz, methodList);
    }

    LOGGER.debug(AsmInterceptorWrapper.LOG_EXIT, "scanForFinalModifiers");

  }

  public static InvocationHandler getInvocationHandler(Object o)
  {
    LOGGER.debug(AsmInterceptorWrapper.LOG_ENTRY, "getInvoationHandler", new Object[] { o });

    InvocationHandler ih = null;
    if (isProxySubclass(o.getClass())) {
      // we have to catch exceptions here, but we just log them
      // the reason for this is that it should be impossible to get these
      // exceptions
      // since the Object we are dealing with is a class we generated on
      // the fly
      try {
        ih = (InvocationHandler) o.getClass().getDeclaredMethod("getInvocationHandler",
            new Class[] {}).invoke(o, new Object[] {});
      } catch (IllegalArgumentException e) {
        LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, e);
      } catch (SecurityException e) {
        LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, e);
      } catch (IllegalAccessException e) {
        LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, e);
      } catch (InvocationTargetException e) {
        LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, e);
      } catch (NoSuchMethodException e) {
        LOGGER.debug(AsmInterceptorWrapper.LOG_EXCEPTION, e);
      }
    }
    LOGGER.debug(AsmInterceptorWrapper.LOG_EXIT, "getInvoationHandler", ih);
    return ih;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/144.java