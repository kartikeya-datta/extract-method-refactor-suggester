error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9957.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9957.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9957.java
text:
```scala
B@@uildExecutor.executeAnt("jni/build-windows32home.xml", "clean postcompile -v");

package com.badlogic.gdx.audio;

import com.badlogic.gdx.jnigen.AntScriptGenerator;
import com.badlogic.gdx.jnigen.BuildConfig;
import com.badlogic.gdx.jnigen.BuildExecutor;
import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.BuildTarget.TargetOs;
import com.badlogic.gdx.jnigen.NativeCodeGenerator;

public class GdxAudioBuild {
	public static void main(String[] args) throws Exception {
		new NativeCodeGenerator().generate("src", "bin", "jni", 
										   new String[] { "**/AudioTools.java", "**/KissFFT.java", "**/VorbisDecoder.java" }, 
										   new String[] { "**/Mpg123Decoder.java" });
		
		String[] headerDirs = new String[] { "kissfft", "vorbis", "soundtouch/include", "soundtouch/source/SoundTouch/" };
		String[] cIncludes = new String[] { 
											"kissfft/*.c", 
											"vorbis/*.c", 
		};
		String[] cppIncludes = new String[] { 
											  "**/*AudioTools.cpp", 
											  "**/*KissFFT.cpp", 
											  "**/*VorbisDecoder.cpp", 
											  "soundtouch/source/SoundTouch/*.cpp"
		};
		String[] cppExcludes = new String[] { "**/cpu_detect_x86_win.cpp" };
		String precompileTask = "<copy failonerror=\"true\" tofile=\"soundtouch\\include\\STTypes.h\" verbose=\"true\" overwrite=\"true\" file=\"STTypes.h.patched\"/>";		
		BuildConfig buildConfig = new BuildConfig("gdx-audio");
		BuildTarget win32home = BuildTarget.newDefaultTarget(TargetOs.Windows, false);
		win32home.compilerPrefix = "";
		win32home.buildFileName = "build-windows32home.xml";
		win32home.headerDirs = headerDirs;
		win32home.cIncludes = cIncludes;
		win32home.cppIncludes = cppIncludes;
		win32home.cppExcludes = cppExcludes;
		win32home.excludeFromMasterBuildFile = true;
		win32home.preCompileTask = precompileTask;
		
		BuildTarget win32 = BuildTarget.newDefaultTarget(TargetOs.Windows, false);
		win32.cFlags += " -DFIXED_POINT";
		win32.cppFlags += " -DFIXED_POINT";
		win32.headerDirs = headerDirs;
		win32.cIncludes = cIncludes;
		win32.cppIncludes = cppIncludes;
		win32.cppExcludes = cppExcludes;
		win32.preCompileTask = precompileTask;
		
		BuildTarget win64 = BuildTarget.newDefaultTarget(TargetOs.Windows, true);
		win64.cFlags += " -DFIXED_POINT";
		win64.cppFlags += " -DFIXED_POINT";
		win64.headerDirs = headerDirs;
		win64.cIncludes = cIncludes;
		win64.cppIncludes = cppIncludes;
		win64.cppExcludes = cppExcludes;
		win64.preCompileTask = precompileTask;
		
		BuildTarget lin32 = BuildTarget.newDefaultTarget(TargetOs.Linux, false);
		lin32.cFlags += " -DFIXED_POINT";
		lin32.cppFlags += " -DFIXED_POINT";
		lin32.headerDirs = headerDirs;
		lin32.cIncludes = cIncludes;
		lin32.cppIncludes = cppIncludes;
		lin32.cppExcludes = cppExcludes;
		lin32.preCompileTask = precompileTask;
		
		BuildTarget lin64 = BuildTarget.newDefaultTarget(TargetOs.Linux, true);
		lin64.cFlags += " -DFIXED_POINT";
		lin64.cppFlags += " -DFIXED_POINT";
		lin64.headerDirs = headerDirs;
		lin64.cIncludes = cIncludes;
		lin64.cppIncludes = cppIncludes;
		lin64.cppExcludes = cppExcludes;
		lin64.preCompileTask = precompileTask;
		
		BuildTarget android = BuildTarget.newDefaultTarget(TargetOs.Android, false);
		android.cFlags += " -DFIXED_POINT -D_ARM_ASSEM_ -D__ANDROID__";
		android.cppFlags += " -DFIXED_POINT -D_ARM_ASSEM_ -D__ANDROID__";
		android.headerDirs = headerDirs;
		android.cIncludes = cIncludes;
		android.cppIncludes = cppIncludes;
		android.cppExcludes = cppExcludes;
		android.preCompileTask = precompileTask;
		
		new AntScriptGenerator().generate(buildConfig, win32home, win32, win64, lin32, lin64, android);
		
		BuildExecutor.executeAnt("jni/build-windows32home.xml", "-v");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/9957.java