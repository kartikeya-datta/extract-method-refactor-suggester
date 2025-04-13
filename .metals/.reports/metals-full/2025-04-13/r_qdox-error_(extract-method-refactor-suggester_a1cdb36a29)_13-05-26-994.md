error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3022.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3022.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3022.java
text:
```scala
static private final H@@ashSet<String> loadedLibraries = new HashSet();

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

package com.badlogic.gdx.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/** Loads shared libraries from a natives jar file (desktop) or arm folders (Android). For desktop projects, have the natives jar
 * in the classpath, for Android projects put the shared libraries in the libs/armeabi and libs/armeabi-v7a folders.
 * 
 * @author mzechner
 * @author Nathan Sweet */
public class SharedLibraryLoader {
	static public boolean isWindows = System.getProperty("os.name").contains("Windows");
	static public boolean isLinux = System.getProperty("os.name").contains("Linux");
	static public boolean isMac = System.getProperty("os.name").contains("Mac");
	static public boolean isIos = false;
	static public boolean isAndroid = false;
	static public boolean is64Bit = System.getProperty("os.arch").equals("amd64");
	static {
		String vm = System.getProperty("java.vm.name");
		if (vm != null && vm.contains("Dalvik")) {
			isAndroid = true;
			isWindows = false;
			isLinux = false;
			isMac = false;
			is64Bit = false;
		}
		if (!isAndroid && !isWindows && !isLinux && !isMac) {
			isIos = true;
			is64Bit = false;
		}
	}

	static private HashSet<String> loadedLibraries = new HashSet();

	private String nativesJar;

	public SharedLibraryLoader () {
	}

	/** Fetches the natives from the given natives jar file. Used for testing a shared lib on the fly.
	 * @param nativesJar */
	public SharedLibraryLoader (String nativesJar) {
		this.nativesJar = nativesJar;
	}

	/** Returns a CRC of the remaining bytes in the stream. */
	public String crc (InputStream input) {
		if (input == null) throw new IllegalArgumentException("input cannot be null.");
		CRC32 crc = new CRC32();
		byte[] buffer = new byte[4096];
		try {
			while (true) {
				int length = input.read(buffer);
				if (length == -1) break;
				crc.update(buffer, 0, length);
			}
		} catch (Exception ex) {
			StreamUtils.closeQuietly(input);
		}
		return Long.toString(crc.getValue(), 16);
	}

	/** Maps a platform independent library name to a platform dependent name. */
	public String mapLibraryName (String libraryName) {
		if (isWindows) return libraryName + (is64Bit ? "64.dll" : ".dll");
		if (isLinux) return "lib" + libraryName + (is64Bit ? "64.so" : ".so");
		if (isMac) return "lib" + libraryName + ".dylib";
		return libraryName;
	}

	/** Loads a shared library for the platform the application is running on.
	 * @param libraryName The platform independent library name. If not contain a prefix (eg lib) or suffix (eg .dll). */
	public synchronized void load (String libraryName) {
		// in case of iOS, things have been linked statically to the executable, bail out.
		if (isIos) return;

		libraryName = mapLibraryName(libraryName);
		if (loadedLibraries.contains(libraryName)) return;

		try {
			if (isAndroid)
				System.loadLibrary(libraryName);
			else
				loadFile(libraryName);
		} catch (Throwable ex) {
			throw new GdxRuntimeException("Couldn't load shared library '" + libraryName + "' for target: "
				+ System.getProperty("os.name") + (is64Bit ? ", 64-bit" : ", 32-bit"), ex);
		}
		loadedLibraries.add(libraryName);
	}

	private InputStream readFile (String path) {
		if (nativesJar == null) {
			InputStream input = SharedLibraryLoader.class.getResourceAsStream("/" + path);
			if (input == null) throw new GdxRuntimeException("Unable to read file for extraction: " + path);
			return input;
		}

		// Read from JAR.
		try {
			ZipFile file = new ZipFile(nativesJar);
			ZipEntry entry = file.getEntry(path);
			if (entry == null) throw new GdxRuntimeException("Couldn't find '" + path + "' in JAR: " + nativesJar);
			return file.getInputStream(entry);
		} catch (IOException ex) {
			throw new GdxRuntimeException("Error reading '" + path + "' in JAR: " + nativesJar, ex);
		}
	}

	/** Extracts the specified file into the temp directory if it does not already exist or the CRC does not match. If file
	 * extraction fails and the file exists at java.library.path, that file is returned.
	 * @param sourcePath The file to extract from the classpath or JAR.
	 * @param dirName The name of the subdirectory where the file will be extracted. If null, the file's CRC will be used.
	 * @return The extracted file. */
	public File extractFile (String sourcePath, String dirName) throws IOException {
		try {
			String sourceCrc = crc(readFile(sourcePath));
			if (dirName == null) dirName = sourceCrc;

			File extractedFile = getExtractedFile(dirName, new File(sourcePath).getName());
			return extractFile(sourcePath, sourceCrc, extractedFile);
		} catch (RuntimeException ex) {
			// Fallback to file at java.library.path location, eg for applets.
			File file = new File(System.getProperty("java.library.path"), sourcePath);
			if (file.exists()) return file;
			throw ex;
		}
	}

	/** Returns a path to a file that can be written. Tries multiple locations and verifies writing succeeds. */
	private File getExtractedFile (String dirName, String fileName) {
		// Temp directory with username in path.
		File idealFile = new File(System.getProperty("java.io.tmpdir") + "/libgdx" + System.getProperty("user.name") + "/"
			+ dirName, fileName);
		if (canWrite(idealFile)) return idealFile;

		// System provided temp directory.
		try {
			File file = File.createTempFile(dirName, null);
			if (file.delete()) {
				file = new File(file, fileName);
				if (canWrite(file)) return file;
			}
		} catch (IOException ignored) {
		}

		// User home.
		File file = new File(System.getProperty("user.home") + "/.libgdx/" + dirName, fileName);
		if (canWrite(file)) return file;

		// Relative directory.
		file = new File(".temp/" + dirName, fileName);
		if (canWrite(file)) return file;

		return idealFile; // Will likely fail, but we did our best.
	}

	/** Returns true if the parent directories of the file can be created and the file can be written. */
	private boolean canWrite (File file) {
		if (file.canWrite()) return true; // File exists and is writable.
		File parent = file.getParentFile();
		parent.mkdirs();
		if (!parent.isDirectory()) return false;
		try {
			new FileOutputStream(file).close();
			file.delete();
			return true;
		} catch (Throwable ex) {
			return false;
		}
	}

	private File extractFile (String sourcePath, String sourceCrc, File extractedFile) throws IOException {
		String extractedCrc = null;
		if (extractedFile.exists()) {
			try {
				extractedCrc = crc(new FileInputStream(extractedFile));
			} catch (FileNotFoundException ignored) {
			}
		}

		// If file doesn't exist or the CRC doesn't match, extract it to the temp dir.
		if (extractedCrc == null || !extractedCrc.equals(sourceCrc)) {
			try {
				InputStream input = readFile(sourcePath);
				extractedFile.getParentFile().mkdirs();
				FileOutputStream output = new FileOutputStream(extractedFile);
				byte[] buffer = new byte[4096];
				while (true) {
					int length = input.read(buffer);
					if (length == -1) break;
					output.write(buffer, 0, length);
				}
				input.close();
				output.close();
			} catch (IOException ex) {
				throw new GdxRuntimeException("Error extracting file: " + sourcePath + "\nTo: " + extractedFile.getAbsolutePath(), ex);
			}
		}

		return extractedFile;
	}

	/** Extracts the source file and calls System.load. Attemps to extract and load from multiple locations. Throws runtime
	 * exception if all fail. */
	private void loadFile (String sourcePath) {
		String sourceCrc = crc(readFile(sourcePath));

		String fileName = new File(sourcePath).getName();

		// Temp directory with username in path.
		File file = new File(System.getProperty("java.io.tmpdir") + "/libgdx" + System.getProperty("user.name") + "/" + sourceCrc,
			fileName);
		Throwable ex = loadFile(sourcePath, sourceCrc, file);
		if (ex == null) return;

		// System provided temp directory.
		try {
			file = File.createTempFile(sourceCrc, null);
			if (file.delete() && loadFile(sourcePath, sourceCrc, file) == null) return;
		} catch (Throwable ignored) {
		}

		// User home.
		file = new File(System.getProperty("user.home") + "/.libgdx/" + sourceCrc, fileName);
		if (loadFile(sourcePath, sourceCrc, file) == null) return;

		// Relative directory.
		file = new File(".temp/" + sourceCrc, fileName);
		if (loadFile(sourcePath, sourceCrc, file) == null) return;

		// Fallback to java.library.path location, eg for applets.
		file = new File(System.getProperty("java.library.path"), sourcePath);
		if (file.exists()) {
			System.load(file.getAbsolutePath());
			return;
		}

		throw new GdxRuntimeException(ex);
	}

	/** @return null if the file was extracted and loaded. */
	private Throwable loadFile (String sourcePath, String sourceCrc, File extractedFile) {
		try {
			System.load(extractFile(sourcePath, sourceCrc, extractedFile).getAbsolutePath());
			return null;
		} catch (Throwable ex) {
			ex.printStackTrace();
			return ex;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/3022.java