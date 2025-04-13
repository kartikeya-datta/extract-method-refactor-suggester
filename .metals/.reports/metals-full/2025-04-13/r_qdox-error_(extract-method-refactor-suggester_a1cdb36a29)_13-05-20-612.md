error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2998.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2998.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2998.java
text:
```scala
h@@eaderDirs.append("\t\t\t<arg value=\"-I" + headerDir + "\"/>\n");

package com.badlogic.gdx.jnigen;

import java.util.ArrayList;

import com.badlogic.gdx.jnigen.BuildTarget.TargetOs;
import com.badlogic.gdx.jnigen.FileDescriptor.FileType;

public class AntScriptGenerator {
	public void generate(BuildConfig config, BuildTarget ... targets) {
		// create all the directories for outputing object files, shared libs and natives jar as well as build scripts.
		if(!config.libsDir.exists()) {
			if(!config.libsDir.mkdirs()) throw new RuntimeException("Couldn't create directory for shared library files in '" + config.libsDir + "'");
		}
		if(!config.jniDir.exists()) {
			if(!config.jniDir.mkdirs()) throw new RuntimeException("Couldn't create native code directory '" + config.jniDir + "'");
		}
		
		ArrayList<String> buildFiles = new ArrayList<String>();
		ArrayList<String> libsDirs = new ArrayList<String>();
		ArrayList<String> sharedLibFiles = new ArrayList<String>();
		
		// generate an Ant build script for each target
		for(BuildTarget target: targets) {
			String buildFile = generateBuildTargetTemplate(config, target);
			FileDescriptor libsDir = new FileDescriptor(getLibsDirectory(config, target));
			
			if(!libsDir.exists()) {
				if(!libsDir.mkdirs()) throw new RuntimeException("Couldn't create libs directory '" + libsDir + "'");
			}
			
			String buildFileName = "build-" + target.os.toString().toLowerCase() + (target.is64Bit?"64":"32") + ".xml";
			if(target.buildFileName != null) buildFileName = target.buildFileName;
			config.jniDir.child(buildFileName).writeString(buildFile, false);
			System.out.println("Wrote target '" + target.os + (target.is64Bit?"64":"") + "' build script '" + config.jniDir.child(buildFileName) + "'");
			
			if(!target.excludeFromMasterBuildFile) {
				buildFiles.add(buildFileName);
				sharedLibFiles.add(getSharedLibFilename(target.os, target.is64Bit, config.sharedLibName));
				libsDirs.add("../" + libsDir.path().replace('\\', '/'));
			}
		}
		
		// generate the master build script
		String template = new FileDescriptor("com/badlogic/gdx/jnigen/resources/scripts/build.xml.template", FileType.Classpath).readString();
		StringBuffer clean = new StringBuffer();
		StringBuffer compile = new StringBuffer();
		StringBuffer pack = new StringBuffer();
		
		for(int i = 0; i < buildFiles.size(); i++) {
			clean.append("\t\t<ant antfile=\"" + buildFiles.get(i) + "\" target=\"clean\"/>\n");
			compile.append("\t\t<ant antfile=\"" + buildFiles.get(i) + "\"/>\n");
			pack.append("\t\t\t<fileset dir=\"" + libsDirs.get(i) + "\" includes=\"" + sharedLibFiles.get(i) + "\"/>");
		}
		
		template = template.replace("%projectName%", config.sharedLibName + "-natives");
		template = template.replace("<clean/>", clean.toString());
		template = template.replace("<compile/>", compile.toString());
		template = template.replace("%packFile%", "../" + config.libsDir.path().replace('\\', '/') + "/" + config.sharedLibName + "-natives.jar");
		template = template.replace("<pack/>", pack);
		
		config.jniDir.child("build.xml").writeString(template, false);
		System.out.println("Wrote master build script '" + config.jniDir.child("build.xml") + "'");
	}
	
	private String getSharedLibFilename(TargetOs os, boolean is64Bit, String sharedLibName) {
		// generate shared lib prefix and suffix, determine jni platform headers directory
		String libPrefix = "";
		String libSuffix = "";
		if(os == TargetOs.Windows) {
			libSuffix = (is64Bit?"64":"") + ".dll";
		}
		if(os == TargetOs.Linux || os == TargetOs.Android) {
			libPrefix = "lib";
			libSuffix = (is64Bit?"64":"") + ".so";
		}
		if(os == TargetOs.MacOsX) {
			libPrefix = "lib";
			libSuffix = ".dylib";
		}
		return libPrefix + sharedLibName + libSuffix;
	}
	
	private String getJniPlatform(TargetOs os) {
		if(os == TargetOs.Windows) return "win32";
		if(os == TargetOs.Linux) return "linux";
		if(os == TargetOs.MacOsX) return "mac";
		return "";
	}
	
	private String getLibsDirectory(BuildConfig config, BuildTarget target) {
		return config.libsDir.child(target.os.toString().toLowerCase() + (target.is64Bit?"64":"32")).path().replace('\\', '/');
	}
	
	private String generateBuildTargetTemplate(BuildConfig config, BuildTarget target) {
		// special case for android
		if(target.os == TargetOs.Android) {
			new AndroidNdkScriptGenerator().generate(config, target);
			return new FileDescriptor("com/badlogic/gdx/jnigen/resources/scripts/build-android.xml.template", FileType.Classpath).readString();
		}
		
		// read template file from resources
		String template = new FileDescriptor("com/badlogic/gdx/jnigen/resources/scripts/build-target.xml.template", FileType.Classpath).readString();
		
		// generate shared lib filename and jni platform headers directory name
		String libName = getSharedLibFilename(target.os, target.is64Bit, config.sharedLibName);
		String jniPlatform = getJniPlatform(target.os);
		
		// generate include and exclude fileset Ant description for C/C++
		StringBuffer cIncludes = new StringBuffer();
		for(String cInclude: target.cIncludes) {
			cIncludes.append("\t\t<include name=\"" + cInclude + "\"/>\n");
		}
		StringBuffer cppIncludes = new StringBuffer();
		for(String cppInclude: target.cppIncludes) {
			cppIncludes.append("\t\t<include name=\"" + cppInclude + "\"/>\n");
		}
		StringBuffer cExcludes = new StringBuffer();
		for(String cExclude: target.cExcludes) {
			cExcludes.append("\t\t<exclude name=\"" + cExclude + "\"/>\n");
		}
		StringBuffer cppExcludes = new StringBuffer();
		for(String cppExclude: target.cppExcludes) {
			cppExcludes.append("\t\t<exclude name=\"" + cppExclude + "\"/>\n");
		}
			
		// generate C/C++ header directories
		StringBuffer headerDirs = new StringBuffer();
		for(String headerDir: target.headerDirs) {
			headerDirs.append("\t\t\t<arg value=\"" + headerDir + "\"/>\n");
		}
		
		// replace template vars with proper values
		template = template.replace("%projectName%", config.sharedLibName + "-" + target.os + "-" + (target.is64Bit?"64":"32"));
		template = template.replace("%buildDir%", config.buildDir.child(target.os.toString().toLowerCase() + (target.is64Bit?"64":"32")).path().replace('\\', '/'));
		template = template.replace("%libsDir%", "../" + getLibsDirectory(config, target));
		template = template.replace("%libName%", libName);
		template = template.replace("%jniPlatform%", jniPlatform);
		template = template.replace("%compilerPrefix%", target.compilerPrefix);
		template = template.replace("%cFlags%", target.cFlags);
		template = template.replace("%cppFlags%", target.cppFlags);
		template = template.replace("%linkerFlags%", target.linkerFlags);
		template = template.replace("%cIncludes%", cIncludes);
		template = template.replace("%cExcludes%", cExcludes);
		template = template.replace("%cppIncludes%", cppIncludes);
		template = template.replace("%cppExcludes%", cppExcludes);
		template = template.replace("%headerDirs%", headerDirs);
		
		return template;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2998.java