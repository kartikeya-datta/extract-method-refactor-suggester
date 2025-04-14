error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9865.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9865.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,59]

error in qdox parser
file content:
```java
offset: 59
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9865.java
text:
```scala
"-c -Wall -O2 -mfpmath=sse -msse2 -fmessage-length=0 -m32",@@ "-Wl,--kill-at -shared -m32 -static-libgcc -static-libstdc++");

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

/** Defines the configuration for building a native shared library for a specific platform. Used with {@link AntScriptGenerator} to
 * create Ant build files that invoke the compiler toolchain to create the shared libraries. */
public class BuildTarget {
	/** The target operating system of a build target. */
	public enum TargetOs {
		Windows, Linux, MacOsX, Android
	}

	/** the target operating system **/
	public BuildTarget.TargetOs os;
	/** whether this is a 64-bit build, not used for Android **/
	public boolean is64Bit;
	/** the C files and directories to be included in the build, accepts Ant path format, must not be null **/
	public String[] cIncludes;
	/** the C files and directories to be excluded from the build, accepts Ant path format, must not be null **/
	public String[] cExcludes;
	/** the C++ files and directories to be included in the build, accepts Ant path format, must not be null **/
	public String[] cppIncludes;
	/** the C++ files and directories to be excluded from the build, accepts Ant path format, must not be null **/
	public String[] cppExcludes;
	/** the directories containing headers for the build, must not be null **/
	public String[] headerDirs;
	/** prefix for the compiler (g++, gcc), useful for cross compilation, must not be null **/
	public String compilerPrefix;
	/** the flags passed to the C compiler, must not be null **/
	public String cFlags;
	/** the flags passed to the C++ compiler, must not be null **/
	public String cppFlags;
	/** the flags passed to the linker, must not be null **/
	public String linkerFlags;
	/** the name of the generated build file for this target, defaults to "build-${target}(64)?.xml", must not be null **/
	public String buildFileName;
	/** whether to exclude this build target from the master build file, useful for debugging **/
	public boolean excludeFromMasterBuildFile = false;
	/** Ant XML executed in a target before compilation **/
	public String preCompileTask;
	/** Ant Xml executed in a target after compilation **/
	public String postCompileTask;

	/** Creates a new build target. See members of this class for a description of the parameters. */
	public BuildTarget (BuildTarget.TargetOs targetType, boolean is64Bit, String[] cIncludes, String[] cExcludes,
		String[] cppIncludes, String[] cppExcludes, String[] headerDirs, String compilerPrefix, String cFlags, String cppFlags,
		String linkerFlags) {
		if (targetType == null) throw new IllegalArgumentException("targetType must not be null");
		if (cIncludes == null) cIncludes = new String[0];
		if (cExcludes == null) cExcludes = new String[0];
		if (cppIncludes == null) cppIncludes = new String[0];
		if (cppExcludes == null) cppExcludes = new String[0];
		if (headerDirs == null) headerDirs = new String[0];
		if (compilerPrefix == null) compilerPrefix = "";
		if (cFlags == null) cFlags = "";
		if (cppFlags == null) cppFlags = "";
		if (linkerFlags == null) linkerFlags = "";

		this.os = targetType;
		this.is64Bit = is64Bit;
		this.cIncludes = cIncludes;
		this.cExcludes = cExcludes;
		this.cppIncludes = cppIncludes;
		this.cppExcludes = cppExcludes;
		this.headerDirs = headerDirs;
		this.compilerPrefix = compilerPrefix;
		this.cFlags = cFlags;
		this.cppFlags = cppFlags;
		this.linkerFlags = linkerFlags;
	}

	/** Creates a new default BuildTarget for the given OS, using common default values. */
	public static BuildTarget newDefaultTarget (BuildTarget.TargetOs type, boolean is64Bit) {
		if (type == TargetOs.Windows && !is64Bit) {
			// Windows 32-Bit
			return new BuildTarget(TargetOs.Windows, false, new String[] {"**/*.c"}, new String[0], new String[] {"**/*.cpp"},
				new String[0], new String[0], "i686-w64-mingw32-", "-c -Wall -O2 -mfpmath=sse -msse2 -fmessage-length=0 -m32",
				"-c -Wall -O2 -mfpmath=sse -msse2 -fmessage-length=0 -m32", "-Wl,--kill-at -shared -m32");
		}

		if (type == TargetOs.Windows && is64Bit) {
			// Windows 64-Bit
			return new BuildTarget(TargetOs.Windows, true, new String[] {"**/*.c"}, new String[0], new String[] {"**/*.cpp"},
				new String[0], new String[0], "x86_64-w64-mingw32-", "-c -Wall -O2 -mfpmath=sse -msse2 -fmessage-length=0 -m64",
				"-c -Wall -O2 -mfpmath=sse -msse2 -fmessage-length=0 -m64",
				"-Wl,--kill-at -shared -static-libgcc -static-libstdc++ -m64");
		}

		if (type == TargetOs.Linux && !is64Bit) {
			// Linux 32-Bit
			return new BuildTarget(TargetOs.Linux, false, new String[] {"**/*.c"}, new String[0], new String[] {"**/*.cpp"},
				new String[0], new String[0], "", "-c -Wall -O2 -mfpmath=sse -msse -fmessage-length=0 -m32 -fPIC",
				"-c -Wall -O2 -mfpmath=sse -msse -fmessage-length=0 -m32 -fPIC", "-shared -m32");
		}

		if (type == TargetOs.Linux && is64Bit) {
			// Linux 64-Bit
			return new BuildTarget(TargetOs.Linux, true, new String[] {"**/*.c"}, new String[0], new String[] {"**/*.cpp"},
				new String[0], new String[0], "", "-c -Wall -O2 -mfpmath=sse -msse -fmessage-length=0 -m64 -fPIC",
				"-c -Wall -O2 -mfpmath=sse -msse -fmessage-length=0 -m64 -fPIC", "-shared -m64");
		}

		if (type == TargetOs.MacOsX) {
			// Mac OS X x86 & x86_64
			BuildTarget mac = new BuildTarget(TargetOs.MacOsX, false, new String[] {"**/*.c"}, new String[0],
				new String[] {"**/*.cpp"}, new String[0], new String[0], "",
				"-c -Wall -O2 -arch i386 -arch x86_64 -DFIXED_POINT -fmessage-length=0 -fPIC -mmacosx-version-min=10.5",
				"-c -Wall -O2 -arch i386 -arch x86_64 -DFIXED_POINT -fmessage-length=0 -fPIC -mmacosx-version-min=10.5",
				"-shared -arch i386 -arch x86_64 -mmacosx-version-min=10.5");
			return mac;
		}

		if (type == TargetOs.Android) {
			BuildTarget android = new BuildTarget(TargetOs.Android, false, new String[] {"**/*.c"}, new String[0],
				new String[] {"**/*.cpp"}, new String[0], new String[0], "", "-O2 -Wall -D__ANDROID__", "-O2 -Wall -D__ANDROID__",
				"-lm");
			return android;
		}

		throw new RuntimeException("Unknown target type");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9865.java