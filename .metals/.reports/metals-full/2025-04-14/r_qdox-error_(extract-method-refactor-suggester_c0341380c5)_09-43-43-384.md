error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2889.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2889.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2889.java
text:
```scala
static i@@nt MINOR_VERSION = 504;

/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import java.io.*;

public class Library {

	/* SWT Version - Mmmm (M=major, mmm=minor) */
	
	/**
	 * SWT Major version number (must be >= 0)
	 */
    static int MAJOR_VERSION = 3;
	
	/**
	 * SWT Minor version number (must be in the range 0..999)
	 */
    static int MINOR_VERSION = 503;
	
	/**
	 * SWT revision number (must be >= 0)
	 */
	static int REVISION = 0;
	
	/**
	 * The JAVA and SWT versions
	 */
	public static final int JAVA_VERSION, SWT_VERSION;

	static final String SEPARATOR;

static {
	SEPARATOR = System.getProperty("file.separator");
	JAVA_VERSION = parseVersion(System.getProperty("java.version"));
	SWT_VERSION = SWT_VERSION(MAJOR_VERSION, MINOR_VERSION);
}

static int parseVersion(String version) {
	if (version == null) return 0;
	int major = 0, minor = 0, micro = 0;
	int length = version.length(), index = 0, start = 0;
	while (index < length && Character.isDigit(version.charAt(index))) index++;
	try {
		if (start < length) major = Integer.parseInt(version.substring(start, index));
	} catch (NumberFormatException e) {}
	start = ++index;
	while (index < length && Character.isDigit(version.charAt(index))) index++;
	try {
		if (start < length) minor = Integer.parseInt(version.substring(start, index));
	} catch (NumberFormatException e) {}
	start = ++index;
	while (index < length && Character.isDigit(version.charAt(index))) index++;
	try {
		if (start < length) micro = Integer.parseInt(version.substring(start, index));
	} catch (NumberFormatException e) {}
	return JAVA_VERSION(major, minor, micro);
}

/**
 * Returns the Java version number as an integer.
 * 
 * @param major
 * @param minor
 * @param micro
 * @return the version
 */
public static int JAVA_VERSION (int major, int minor, int micro) {
	return (major << 16) + (minor << 8) + micro;
}

/**
 * Returns the SWT version number as an integer.
 * 
 * @param major
 * @param minor
 * @return the version
 */
public static int SWT_VERSION (int major, int minor) {
	return major * 1000 + minor;
}

static boolean extract (String fileName, String mappedName) {
	FileOutputStream os = null;
	InputStream is = null;
	File file = new File(fileName);
	boolean extracted = false;
	try {
		if (!file.exists ()) {
			is = Library.class.getResourceAsStream ("/" + mappedName); //$NON-NLS-1$
			if (is != null) {
				extracted = true;
				int read;
				byte [] buffer = new byte [4096];
				os = new FileOutputStream (fileName);
				while ((read = is.read (buffer)) != -1) {
					os.write(buffer, 0, read);
				}
				os.close ();
				is.close ();
				if (!Platform.PLATFORM.equals ("win32")) { //$NON-NLS-1$
					try {
						Runtime.getRuntime ().exec (new String []{"chmod", "755", fileName}).waitFor(); //$NON-NLS-1$ //$NON-NLS-2$
					} catch (Throwable e) {}
				}
			}
		}
		if (load (fileName)) return true;
	} catch (Throwable e) {
		try {
			if (os != null) os.close ();
		} catch (IOException e1) {}
		try {
			if (is != null) is.close ();
		} catch (IOException e1) {}
		if (extracted && file.exists ()) file.delete ();
	}
	return false;
}

static boolean load (String libName) {
	try {
		if (libName.indexOf (SEPARATOR) != -1) {
			System.load (libName);
		} else {
			System.loadLibrary (libName);
		}		
		return true;
	} catch (UnsatisfiedLinkError e) {}
	return false;
}

/**
 * Loads the shared library that matches the version of the
 * Java code which is currently running.  SWT shared libraries
 * follow an encoding scheme where the major, minor and revision
 * numbers are embedded in the library name and this along with
 * <code>name</code> is used to load the library.  If this fails,
 * <code>name</code> is used in another attempt to load the library,
 * this time ignoring the SWT version encoding scheme.
 *
 * @param name the name of the library to load
 */
public static void loadLibrary (String name) {
	loadLibrary (name, true);
}

/**
 * Loads the shared library that matches the version of the
 * Java code which is currently running.  SWT shared libraries
 * follow an encoding scheme where the major, minor and revision
 * numbers are embedded in the library name and this along with
 * <code>name</code> is used to load the library.  If this fails,
 * <code>name</code> is used in another attempt to load the library,
 * this time ignoring the SWT version encoding scheme.
 *
 * @param name the name of the library to load
 * @param mapName true if the name should be mapped, false otherwise
 */
public static void loadLibrary (String name, boolean mapName) {
	String prop = System.getProperty ("sun.arch.data.model"); //$NON-NLS-1$
	if (prop == null) prop = System.getProperty ("com.ibm.vm.bitmode"); //$NON-NLS-1$
	if (prop != null) {
		if ("32".equals (prop)) { //$NON-NLS-1$
			 if (0x1FFFFFFFFL == (int /*long*/)0x1FFFFFFFFL) {
				throw new UnsatisfiedLinkError ("Cannot load 64-bit SWT libraries on 32-bit JVM"); //$NON-NLS-1$
			 }
		}
		if ("64".equals (prop)) { //$NON-NLS-1$
			if (0x1FFFFFFFFL != (int /*long*/)0x1FFFFFFFFL) {
				throw new UnsatisfiedLinkError ("Cannot load 32-bit SWT libraries on 64-bit JVM"); //$NON-NLS-1$
			}		
		}
	}
	
	/* Compute the library name and mapped name */
	String libName1, libName2, mappedName1, mappedName2;
	if (mapName) {
		String version = System.getProperty ("swt.version"); //$NON-NLS-1$
		if (version == null) {
			version = "" + MAJOR_VERSION; //$NON-NLS-1$
			/* Force 3 digits in minor version number */
			if (MINOR_VERSION < 10) {
				version += "00"; //$NON-NLS-1$
			} else {
				if (MINOR_VERSION < 100) version += "0"; //$NON-NLS-1$
			}
			version += MINOR_VERSION;		
			/* No "r" until first revision */
			if (REVISION > 0) version += "r" + REVISION; //$NON-NLS-1$
		}
		libName1 = name + "-" + Platform.PLATFORM + "-" + version;  //$NON-NLS-1$ //$NON-NLS-2$
		libName2 = name + "-" + Platform.PLATFORM;  //$NON-NLS-1$
		mappedName1 = System.mapLibraryName (libName1);
		mappedName2 = System.mapLibraryName (libName2);
	} else {
		libName1 = libName2 = mappedName1 = mappedName2 = name;
	}

	/* Try loading library from swt library path */
	String path = System.getProperty ("swt.library.path"); //$NON-NLS-1$
	if (path != null) {
		path = new File (path).getAbsolutePath ();
		if (load (path + SEPARATOR + mappedName1)) return;
		if (mapName && load (path + SEPARATOR + mappedName2)) return;
	}

	/* Try loading library from java library path */
	if (load (libName1)) return;
	if (mapName && load (libName2)) return;
	
	/* Try loading library from the tmp directory if swt library path is not specified */
	if (path == null) {
		path = System.getProperty ("java.io.tmpdir"); //$NON-NLS-1$
		path = new File (path).getAbsolutePath ();
		if (load (path + SEPARATOR + mappedName1)) return;
		if (mapName && load (path + SEPARATOR + mappedName2)) return;
	}
		
	/* Try extracting and loading library from jar */
	if (path != null) {
		if (extract (path + SEPARATOR + mappedName1, mappedName1)) return;
		if (mapName && extract (path + SEPARATOR + mappedName2, mappedName2)) return;
	}
	
	/* Failed to find the library */
	throw new UnsatisfiedLinkError ("no " + libName1 + " or " + libName2 + " in swt.library.path, java.library.path or the jar file"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2889.java