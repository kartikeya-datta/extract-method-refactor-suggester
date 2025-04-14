error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4246.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4246.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4246.java
text:
```scala
i@@f ( ( args == null ) || (args.length == 0) ) {

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.
package org.columba.core.loader;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

import org.columba.core.logging.ColumbaLogger;

/**
 * @author freddy
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ExternalClassLoader extends URLClassLoader {

	/**
	 * Constructor for ExternalClassLoader.
	 * @param urls
	 * @param parent
	 */
	public ExternalClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	/**
	 * Constructor for ExternalClassLoader.
	 * @param urls
	 */
	public ExternalClassLoader(URL[] urls) {
		super(urls);
	}

	/**
	 * Constructor for ExternalClassLoader.
	 * @param urls
	 * @param parent
	 * @param factory
	 */
	public ExternalClassLoader(
		URL[] urls,
		ClassLoader parent,
		URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	public void addURL(URL url) {
		super.addURL(url);
	}

	public Class findClass(String className) throws ClassNotFoundException {
		Class temp = super.findClass(className);
		return temp;
	}

	public Object instanciate(String className) throws Exception {
		Class actClass = findClass(className);

		return actClass.newInstance();
	}

	public Object instanciate(String className, Object[] args)
		throws Exception {
		
		/*
		//ColumbaLogger.log.debug("class="+className);

		Class actClass = findClass(className);

		Constructor[] constructors = actClass.getConstructors();
		Constructor constructor = constructors[0];

		return constructor.newInstance(args);
		*/
		
		Class actClass = findClass(className);

		Constructor constructor = null;

		// FIXME
		//
		// we can't just load the first constructor 
		//  -> go find the correct constructor based
		//  -> based on the arguments
		//
		//    old solution and wrong:
		//Constructor constructor = actClass.getConstructors()[0];//argClazz);
		//
		if (args.length == 0) {
			constructor = actClass.getConstructors()[0];
			
			return constructor.newInstance(args);
		}

		Constructor[] list = actClass.getConstructors();

		Class[] classes = new Class[args.length];

		for (int i = 0; i < list.length; i++) {
			Constructor c = list[i];

			Class[] parameterTypes = c.getParameterTypes();

			// this constructor has the correct number 
			// of arguments
			if (parameterTypes.length == args.length) {
				boolean success = true;
				for (int j = 0; j < parameterTypes.length; j++) {
					Class parameter = parameterTypes[j];

					if (args[j] == null)
						success = true;

					else if (!parameter.isAssignableFrom(args[j].getClass()))
						success = false;
				}

				// ok, we found a matching constructor
				// -> create correct list of arguments
				if (success) {
					constructor = actClass.getConstructor(parameterTypes);
				}

			}
		}

		

		// couldn't find correct constructor
		if (constructor == null)
			return null;

		return constructor.newInstance(args);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4246.java