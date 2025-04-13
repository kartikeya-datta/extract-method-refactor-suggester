error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4750.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4750.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4750.java
text:
```scala
i@@f (FileUtil.isZipFile(file)) {

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.util;

import java.io.File;
import java.lang.reflect.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
//import java.util.StringTokenizer;

public class Reflection {
    public static final Class[] MAIN_PARM_TYPES = new Class[] {String[].class};    

	private Reflection() {
	}
	
	public static Object invokestatic(Class class_, String name) {
		return invokestaticN(class_, name, new Object[0]);
	}
	
	public static Object invokestatic(Class class_, String name, Object arg1) {
		return invokestaticN(class_, name, new Object[] { arg1 });
	}
	
	public static Object invokestatic(Class class_, String name, Object arg1, Object arg2) {
		return invokestaticN(class_, name, new Object[] { arg1, arg2 });
	}
	
	public static Object invokestatic(Class class_, String name, Object arg1, Object arg2, Object arg3) {
		return invokestaticN(class_, name, new Object[] { arg1, arg2, arg3 });
	}
	
	
	public static Object invokestaticN(Class class_, String name, Object[] args) {
		return invokeN(class_, name, null, args);
	}


	public static Object invoke(Class class_, Object target, String name, Object arg1) {
		return invokeN(class_, name, target, new Object[] { arg1 });
	}
	
	public static Object invoke(Class class_, Object target, String name, Object arg1, Object arg2) {
		return invokeN(class_, name, target, new Object[] { arg1, arg2 });
	}
	
	public static Object invoke(Class class_, Object target, String name, Object arg1, Object arg2, Object arg3) {
		return invokeN(class_, name, target, new Object[] { arg1, arg2, arg3 });
	}
	


	
	public static Object invokeN(Class class_, String name, Object target, Object[] args) {
		Method meth = getMatchingMethod(class_, name, args);
		try {
			return meth.invoke(target, args);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.toString());			
		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			if (t instanceof Error) throw (Error)t;
			if (t instanceof RuntimeException) throw (RuntimeException)t;
			t.printStackTrace();
			throw new RuntimeException(t.toString());
		}
	}


	public static Method getMatchingMethod(Class class_, String name, Object[] args) {
		Method[] meths = class_.getMethods();
		for (int i=0; i < meths.length; i++) {
			Method meth = meths[i];
			if (meth.getName().equals(name) && isCompatible(meth, args)) {
				return meth;
			}
		}
		return null;
	}

	private static boolean isCompatible(Method meth, Object[] args) {
		// ignore methods with overloading other than lengths
		return meth.getParameterTypes().length == args.length;
	}


	

	public static Object getStaticField(Class class_, String name) {
		try {
			return class_.getField(name).get(null);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("unimplemented");
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("unimplemented");
		}
	}

    public static void runMainInSameVM(
            String classpath,
            String className, 
            String[] args) 
            throws SecurityException, NoSuchMethodException, 
            IllegalArgumentException, IllegalAccessException, 
            InvocationTargetException, ClassNotFoundException {
        LangUtil.throwIaxIfNull(className, "class name");
        if (LangUtil.isEmpty(classpath)) {
            Class mainClass = Class.forName(className);
            runMainInSameVM(mainClass, args);
            return;            
        }
        ArrayList dirs = new ArrayList();
        ArrayList libs = new ArrayList();
        ArrayList urls = new ArrayList();
        String[] entries = LangUtil.splitClasspath(classpath);
        for (int i = 0; i < entries.length; i++) {
            String entry = entries[i];
            URL url = makeURL(entry);
            if (null != url) {
                urls.add(url);
            }
            File file = new File(entries[i]);
// tolerate bad entries b/c bootclasspath sometimes has them
//            if (!file.canRead()) {
//                throw new IllegalArgumentException("cannot read " + file);
//            }
            if (FileUtil.hasZipSuffix(file)) {
                libs.add(file);
            } else if (file.isDirectory()) {
                dirs.add(file);
            } else {
                // not URL, zip, or dir - unsure what to do
            }
        }
        File[] dirRa = (File[]) dirs.toArray(new File[0]);
        File[] libRa = (File[]) libs.toArray(new File[0]);
        URL[] urlRa = (URL[]) urls.toArray(new URL[0]);
        runMainInSameVM(urlRa, libRa, dirRa, className, args);
    }
        
    public static void runMainInSameVM(
            URL[] urls,
            File[] libs, 
            File[] dirs, 
            String className, 
            String[] args) 
            throws SecurityException, NoSuchMethodException, 
            IllegalArgumentException, IllegalAccessException, 
            InvocationTargetException, ClassNotFoundException {
        LangUtil.throwIaxIfNull(className, "class name");
        LangUtil.throwIaxIfNotAssignable(libs, File.class, "jars");
        LangUtil.throwIaxIfNotAssignable(dirs, File.class, "dirs");
        URL[] libUrls = FileUtil.getFileURLs(libs);
        if (!LangUtil.isEmpty(libUrls)) {
            if (!LangUtil.isEmpty(urls)) {
                URL[] temp = new URL[libUrls.length + urls.length];
                System.arraycopy(urls, 0, temp, 0, urls.length);
                System.arraycopy(urls, 0, temp, libUrls.length, urls.length);
                urls = temp;
            } else {
                urls = libUrls;
            }
        }
        UtilClassLoader loader = new UtilClassLoader(urls, dirs);
        Class targetClass = null;
        try {
            targetClass = loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            String s = "unable to load class " + className
                + " using class loader " + loader; 
            throw new ClassNotFoundException(s);
        }
        Method main = targetClass.getMethod("main", MAIN_PARM_TYPES);
        main.invoke(null, new Object[] { args });
    }
    
    public static void runMainInSameVM(Class mainClass, String[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        LangUtil.throwIaxIfNull(mainClass, "main class");
        Method main = mainClass.getMethod("main", MAIN_PARM_TYPES);
        main.invoke(null, new Object[] { args });
    }

    /** @return URL if the input is valid as such */
    private static URL makeURL(String s) {
        try {
            return new URL(s);            
        } catch (Throwable t) {
            return null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4750.java