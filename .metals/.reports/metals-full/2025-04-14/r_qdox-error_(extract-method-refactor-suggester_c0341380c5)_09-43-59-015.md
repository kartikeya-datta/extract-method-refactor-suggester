error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3570.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3570.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3570.java
text:
```scala
b@@oolean is64Bit = System.getProperty("os.arch").equals("amd64") || System.getProperty("os.arch").equals("x86_64");

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.jnigen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/** Loads shared libraries from a natives jar file (desktop) or arm folders (Android). For desktop projects, have the natives jar
 * in the classpath, for Android projects put the shared libraries in the libs/armeabi and libs/armeabi-v7a folders.
 * 
 * See {@link AntScriptGenerator}.
 * 
 * @author mzechner */
public class JniGenSharedLibraryLoader {
	private static Set<String> loadedLibraries = new HashSet<String>();
	private String nativesJar;
	private SharedLibraryFinder libraryFinder;

	private ZipFile nativesZip = null;

	public JniGenSharedLibraryLoader () {
	}

	/** Fetches the natives from the given natives jar file. Used for testing a shared lib on the fly, see MyJniClass.
	 * @param nativesJar */
	public JniGenSharedLibraryLoader (String nativesJar) {
		this.nativesJar = nativesJar;
	}

	/** Fetches the natives from the given natives jar file. Used for testing a shared lib on the fly, see MyJniClass.
	 * @param nativesJar
	 * @param libraryFinder A custom libraryfinder, which enables the use of different dynamic libs naming. */
	public JniGenSharedLibraryLoader (String nativesJar, SharedLibraryFinder libraryFinder) {
		this.nativesJar = nativesJar;
		this.libraryFinder = libraryFinder;
		if (nativesJar != null) {
			try {
				nativesZip = new ZipFile(nativesJar);
			} catch (IOException e) {
				nativesZip = null;
			}
		}
	}

	/** Setting a SharedLibraryFinder enables you to load libraries according to a nondefault natives jar layout or library names.
	 * @param libraryFinder */
	public void setSharedLibraryFinder (SharedLibraryFinder libraryFinder) {
		this.libraryFinder = libraryFinder;
		if (nativesJar != null) {
			try {
				nativesZip = new ZipFile(nativesJar);
			} catch (IOException e) {
				nativesZip = null;
			}
		}
	}

	/** Returns a CRC of the remaining bytes in the stream. */
	public String crc (InputStream input) {
		if (input == null) return "" + System.nanoTime(); // fallback
		CRC32 crc = new CRC32();
		byte[] buffer = new byte[4096];
		try {
			while (true) {
				int length = input.read(buffer);
				if (length == -1) break;
				crc.update(buffer, 0, length);
			}
		} catch (Exception ex) {
			try {
				input.close();
			} catch (Exception ignored) {
			}
		}
		return Long.toString(crc.getValue());
	}

	private boolean loadLibrary (String sharedLibName) {
		if (sharedLibName == null) return false;

		String path = extractLibrary(sharedLibName);
		if (path != null) System.load(path);
		return path != null;
	}

	private String extractLibrary (String sharedLibName) {
		String srcCrc = crc(JniGenSharedLibraryLoader.class.getResourceAsStream("/" + sharedLibName));
		File nativesDir = new File(System.getProperty("java.io.tmpdir") + "/jnigen/" + srcCrc);
		File nativeFile = new File(nativesDir, sharedLibName);

		String extractedCrc = null;
		if (nativeFile.exists()) {
			try {
				extractedCrc = crc(new FileInputStream(nativeFile));
			} catch (FileNotFoundException ignored) {
			}
		}

		if (extractedCrc == null || !extractedCrc.equals(srcCrc)) {
			try {
				// Extract native from classpath to temp dir.
				InputStream input = null;
				if (nativesJar == null)
					input = JniGenSharedLibraryLoader.class.getResourceAsStream("/" + sharedLibName);
				else
					input = getFromJar(nativesJar, sharedLibName);
				if (input == null) return null;
				nativeFile.getParentFile().mkdirs();
				FileOutputStream output = new FileOutputStream(nativeFile);
				byte[] buffer = new byte[4096];
				while (true) {
					int length = input.read(buffer);
					if (length == -1) break;
					output.write(buffer, 0, length);
				}
				input.close();
				output.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
		return nativeFile.exists() ? nativeFile.getAbsolutePath() : null;
	}

	private InputStream getFromJar (String jarFile, String sharedLibrary) throws IOException {
		ZipFile file = new ZipFile(nativesJar);
		ZipEntry entry = file.getEntry(sharedLibrary);
		return file.getInputStream(entry);
	}

	/** Loads a shared library with the given name for the platform the application is running on. The name should not contain a
	 * prefix (e.g. 'lib') or suffix (e.g. '.dll).
	 * @param sharedLibName */
	public synchronized void load (String sharedLibName) {
		if (loadedLibraries.contains(sharedLibName)) return;

		boolean isWindows = System.getProperty("os.name").contains("Windows");
		boolean isLinux = System.getProperty("os.name").contains("Linux");
		boolean isMac = System.getProperty("os.name").contains("Mac");
		boolean isAndroid = false;
		boolean is64Bit = System.getProperty("os.arch").equals("amd64");
		String vm = System.getProperty("java.vm.name");
		if (vm != null && vm.contains("Dalvik")) {
			isAndroid = true;
			isWindows = false;
			isLinux = false;
			isMac = false;
			is64Bit = false;
		}

		boolean loaded = false;
		if (isWindows) {
			if (libraryFinder != null)
				loaded = loadLibrary(libraryFinder.getSharedLibraryNameWindows(sharedLibName, is64Bit, nativesZip));
			else if (!is64Bit)
				loaded = loadLibrary(sharedLibName + ".dll");
			else
				loaded = loadLibrary(sharedLibName + "64.dll");
		}
		if (isLinux) {
			if (libraryFinder != null)
				loaded = loadLibrary(libraryFinder.getSharedLibraryNameLinux(sharedLibName, is64Bit, nativesZip));
			else if (!is64Bit)
				loaded = loadLibrary("lib" + sharedLibName + ".so");
			else
				loaded = loadLibrary("lib" + sharedLibName + "64.so");
		}
		if (isMac) {
			if (libraryFinder != null)
				loaded = loadLibrary(libraryFinder.getSharedLibraryNameMac(sharedLibName, is64Bit, nativesZip));
			else if(!is64Bit)
				loaded = loadLibrary("lib" + sharedLibName + ".dylib");
			else
				loaded = loadLibrary("lib" + sharedLibName + "64.dylib");
		}
		if (isAndroid) {
			if (libraryFinder != null)
				System.loadLibrary(libraryFinder.getSharedLibraryNameAndroid(sharedLibName, nativesZip));
			else
				System.loadLibrary(sharedLibName);
			loaded = true;
		}
		if (loaded) loadedLibraries.add(sharedLibName);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3570.java