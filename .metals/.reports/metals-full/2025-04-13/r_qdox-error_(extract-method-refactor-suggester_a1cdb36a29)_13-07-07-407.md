error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7514.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7514.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7514.java
text:
```scala
t@@hrow new org.apache.jorphan.util.JMeterError(e); //JDK1.4

package org.apache.jorphan.reflect;

import java.lang.reflect.Method;

/**
 * @author mstover
 */
public class Functor
{
   Object invokee;
   String methodName;
   Object[] args;
   Class[] types;
   Method methodToInvoke;

   public Functor(Object invokee, String methodName)
   {
      this(methodName);
      this.invokee = invokee;
   }

   public Functor(Object invokee, String methodName, Class[] types)
   {
      this(invokee, methodName);
      this.types = types;
   }

   public Functor(String methodName)
   {
      this.methodName = methodName;
   }

   public Functor(String methodName, Class[] types)
   {
      this(methodName);
      this.types = types;
   }

   public Functor(Object invokee, String methodName, Object[] args)
   {
      this(invokee, methodName);
      this.args = args;
   }

   public Functor(Object invokee, String methodName, Object[] args,
         Class[] types)
   {
      this(invokee, methodName, args);
      this.types = types;
   }

   public Object invoke()
   {
      try
      {
         return createMethod(types).invoke(invokee, getArgs());
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   public Object invoke(Object invokee)
   {
      this.invokee = invokee;
      return invoke();
   }

   public Object invoke(Object[] args)
   {
      this.args = args;
      return invoke();
   }

   public Object invoke(Object invokee, Object[] args)
   {
      this.args = args;
      this.invokee = invokee;
      return invoke();
   }

   private Method createMethod(Class[] types)
   {
      if (methodToInvoke == null)
      {
         try
         {
            methodToInvoke = invokee.getClass().getMethod(methodName, types);
         }
         catch (Exception e)
         {
            for (int i = 0; i < types.length; i++)
            {
               Class[] interfaces = types[i].getInterfaces();
               for (int j = 0; j < interfaces.length; j++)
               {
                  methodToInvoke = createMethod(getNewArray(i, interfaces[j], types));
                  if (methodToInvoke != null) { return methodToInvoke; }
               }
               Class parent = types[i].getSuperclass();
               methodToInvoke = createMethod(getNewArray(i, parent, types));
               if (methodToInvoke != null) { return methodToInvoke; }
            }
         }
      }
      return methodToInvoke;
   }

   protected Class[] getNewArray(int i, Class replacement, Class[] orig)
   {
      Class[] newArray = new Class[orig.length];
      for (int j = 0; j < newArray.length; j++)
      {
         newArray[j] = orig[j];
         if (j == i)
         {
            newArray[j] = replacement;
         }
      }
      return newArray;
   }

   private Class[] getTypes()
   {
      if (types == null) // do only once per functor instance. Could
      // cause errors if functor used for multiple
      // same-named-different-parametered methods.
      {
         if (args != null)
         {
            types = new Class[args.length];
            for (int i = 0; i < args.length; i++)
            {
               types[i] = args[i].getClass();
            }
         }
         else
         {
            types = new Class[0];
         }
      }
      return types;
   }

   private Object[] getArgs()
   {
      if (args == null)
      {
         args = new Object[0];
      }
      return args;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7514.java