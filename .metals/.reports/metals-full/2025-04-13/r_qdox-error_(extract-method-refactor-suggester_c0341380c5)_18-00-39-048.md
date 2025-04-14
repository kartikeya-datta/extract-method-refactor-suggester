error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12682.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12682.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12682.java
text:
```scala
i@@f(file == null || !file.isFile()) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.compress.compressors.bzip2.BZip2Compressor;
/**
 * Compressor-Factory. 
 * Use CompressorFactory.TYPE.getInstance() for an new Compressor-Instance.
 */
public abstract class CompressorFactory {
	/* Name of this CompressorFactory*/
	private final String name;
	/* internal archiver list */
	private static List compressors;
	
	// register compressors 
	static {
		compressors = new ArrayList();
		try {
			registerCompressor(BZip2Compressor.class);
		} catch (CompressException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Registers a new archiver in the factory.
	 * The archiver must implement the archiver interface.
	 * 
	 * @param className full qualified archiver implementation
	 * @throws ClassNotFoundException if the new archiver class could not be found
	 * @throws ArchiveException if the new archiver does not implement the archiver interface
	 */
	public static void registerCompressor(String className) 
		throws ClassNotFoundException, CompressException {
		Class clazz = Class.forName(className);
		registerCompressor(clazz);
	}
	
	/**
	 * Registers a new archiver in the factory.
	 * The archiver must implement the archiver interface and must
	 * be an concrete implementation
	 * 
	 * @param clazz full qualified archiver implementation
	 * @throws ArchiveException if the new archiver does not implement the archiver interface
	 */
	public static void registerCompressor(Class clazz) 
		throws CompressException {
		// register only, if the class is assignable and not an interface
		if(Compressor.class.isAssignableFrom(clazz) && !(clazz.isInterface())) {
			try {
				compressors.add(clazz.newInstance());
			} catch (InstantiationException e) {
				throw new CompressException("Compressor could not be instantiated", e);
			} catch (IllegalAccessException e) {
				throw new CompressException("Compressor could not be instantiated", e);
			}
		} else {
			throw new CompressException("Compressor does not implement the Compressor.class interface.");
		}
	}

	/**
	 * Constructor. Takes the name of the implementation.
	 * @param name - name of the implementation
	 */
	private CompressorFactory(String name) { 
		this.name = name; 
	}

	/**
	 * Returns a compressor
	 * @return the compressor
	 */
	public abstract Compressor getInstance();

	/**
	 * Returns an empty Compressor, if an matching compressor 
	 * could be found within this factory.
	 * If two implementations with the same name are registered,
	 * the first matching implementation will be instanciated.
	 * 
	 * @return the compressor, or null, if no matching compressor could be found
	 * @throws CompressException if the compressor could not be created
	 */
	public static Compressor getInstance(String compressorName) 
		throws CompressException {
		try {
			if(compressorName == null) {
				throw new CompressException("CompressorFactory could not create instance");
			}
			Iterator it = compressors.iterator();
			while(it.hasNext()) {
				PackableObject po = (PackableObject)it.next();
				if(po.isPackableWith(compressorName, PackableObject.CHOOSE_NAME)) {
					return (Compressor)po.getClass().newInstance();
				}
			}
			return null;
		} catch (InstantiationException e) {
			throw new CompressException("CompressorFactory could not create instance", e);
		} catch (IllegalAccessException e) {
			throw new CompressException("CompressorFactory could not create instance", e);
		}
	}
	
	/**
	 * Returns an archiver, filled with an existing archive.
	 * Uses the byte header to identify the archiver. If no corresponding
	 * archiver could be found, a filename extension check will be done.
	 * @param file an existing archive
	 * @return an archiver, filled with the archive
	 */
	public static Compressor getInstance(File file) 
		throws CompressException {
		if(file == null && !file.isFile()) {
			throw new CompressException("CompressorFactory could not create instance for this file");
		}
		
		
		/* Archive result */
		PackableObject packable = null; 
		
		try {
			packable = PackableObject.identifyByHeader(file, compressors);
			
			if(packable == null) {
				return null;
			}
			Compressor compressor = (Compressor)packable.getClass().newInstance();
			return compressor;
		} catch (SecurityException e) {
			throw new CompressException("A security violation occured while reading the field ARCHIVER_NAME", e);
		} catch (IllegalArgumentException e) {
			throw new CompressException("Internal factory exception", e);
		} catch (Exception e) {
			throw new CompressException("CompressorFactory could not create instance", e);
		}
	}
	/**
	 * <code>BZIP2</code> Compressor Factory
	 */
	public static CompressorFactory BZIP2 = new CompressorFactory("BZIP2") {
		/* (non-Javadoc)
		 * @see org.apache.commons.compress.ArchiverFactory#getInstance()
		 */
		public Compressor getInstance() { 
			return new BZip2Compressor(); 
		}
	};
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() { 
		return name; 
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12682.java