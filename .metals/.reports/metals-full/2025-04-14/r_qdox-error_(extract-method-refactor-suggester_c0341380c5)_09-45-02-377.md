error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/493.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/493.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/493.java
text:
```scala
p@@roject.files.add(new ProjectFile("android/res/values/strings.xml", false));

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

package com.badlogic.gdx.setup;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Command line tool to generate libgdx projects
 * @author badlogic
 *
 */
public class GdxSetup {
	public void build (String outputDir, String appName, String packageName, String mainClass) {
		Project project = new Project();
		
		String packageDir = packageName.replace('.', '/');

		// root dir/gradle files
		project.files.add(new ProjectFile("build.gradle", true));
		project.files.add(new ProjectFile("settings.gradle"));
		project.files.add(new ProjectFile("gradlew", false));
		project.files.add(new ProjectFile("gradlew.bat", false));
		project.files.add(new ProjectFile("gradle/wrapper/gradle-wrapper.jar", false));
		project.files.add(new ProjectFile("gradle/wrapper/gradle-wrapper.properties", false));
		
		// core project
		project.files.add(new ProjectFile("core/build.gradle"));
		project.files.add(new ProjectFile("core/src/MainClass", "core/src/" + packageDir + "/" + mainClass + ".java", true));
        //core but gwt required
        project.files.add(new ProjectFile("core/CoreGdxDefinition", "core/src/" + packageDir + "/" + mainClass + ".gwt.xml", true));
		
		// desktop project
		project.files.add(new ProjectFile("desktop/build.gradle"));
		project.files.add(new ProjectFile("desktop/src/DesktopLauncher", "desktop/src/" + packageDir + "/desktop/DesktopLauncher.java", true));

		// android project
		project.files.add(new ProjectFile("android/assets/badlogic.jpg", false));
		project.files.add(new ProjectFile("android/res/values/strings.xml"));
		project.files.add(new ProjectFile("android/res/values/styles.xml", false));
		project.files.add(new ProjectFile("android/res/drawable-hdpi/ic_launcher.png", false));
		project.files.add(new ProjectFile("android/res/drawable-mdpi/ic_launcher.png", false));
		project.files.add(new ProjectFile("android/res/drawable-xhdpi/ic_launcher.png", false));
		project.files.add(new ProjectFile("android/res/drawable-xxhdpi/ic_launcher.png", false));
		project.files.add(new ProjectFile("android/src/AndroidLauncher", "android/src/" + packageDir + "/android/AndroidLauncher.java", true));
		project.files.add(new ProjectFile("android/AndroidManifest.xml"));
		project.files.add(new ProjectFile("android/build.gradle"));
		project.files.add(new ProjectFile("android/ic_launcher-web.png", false));
		project.files.add(new ProjectFile("android/proguard-project.txt", false));
		project.files.add(new ProjectFile("android/project.properties", false));

        //gwt project
        project.files.add(new ProjectFile("gwt/build.gradle"));
        project.files.add(new ProjectFile("gwt/src/GwtLauncher", "gwt/src/" + packageDir + "/client/GwtLauncher.java", true));
        project.files.add(new ProjectFile("gwt/GdxDefinition", "gwt/src/" + packageDir + "/GdxDefinition.gwt.xml", true));
        project.files.add(new ProjectFile("gwt/war/index", "gwt/webapp/" + "index.html", true));
        project.files.add(new ProjectFile("gwt/war/WEB-INF/web.xml", "gwt/webapp/WEB-INF/web.xml", true));

        //ios robovm
        project.files.add(new ProjectFile("ios/src/IOSLauncher", "ios/src/" + packageDir + "/IOSLauncher.java", true));
        project.files.add(new ProjectFile("ios/build.gradle"));


		Map<String, String> values = new HashMap<String, String>();
		values.put("%APP_NAME%", appName);
		values.put("%PACKAGE%", packageName);
		values.put("%MAIN_CLASS%", mainClass);
		
		copyAndReplace(outputDir, project, values);
		
		// HACK executable flag isn't preserved for whatever reason...
		new File(outputDir, "gradlew").setExecutable(true);
	}

	private void copyAndReplace (String outputDir, Project project, Map<String, String> values) {
		File out = new File(outputDir);
		if(!out.exists() && !out.mkdirs()) {
			throw new RuntimeException("Couldn't create output directory '" + out.getAbsolutePath() + "'");
		}
		
		for(ProjectFile file: project.files) {
			copyFile(file, out, values);
		}
	}
	
	private byte[] readResource(String resource) {
		InputStream in = null;
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024*10];
			in = GdxSetup.class.getResourceAsStream("/com/badlogic/gdx/setup/resources/" + resource);
			if(in == null) throw new RuntimeException("Couldn't read resource '" + resource + "'");
			int read = 0;
			while((read = in.read(buffer)) > 0) {
				bytes.write(buffer, 0, read);
			}
			return bytes.toByteArray();
		} catch(IOException e) {
			throw new RuntimeException("Couldn't read resource '" + resource + "'", e);
		} finally {
			if(in != null) try { in.close(); } catch(IOException e) {}
		}
	}
	
	private String readResourceAsString(String resource) {
		try {
			return new String(readResource(resource), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void writeFile (File outFile, byte[] bytes) {
		OutputStream out = null;
		
		try {
			out = new BufferedOutputStream(new FileOutputStream(outFile));
			out.write(bytes);
		} catch(IOException e) {
			throw new RuntimeException("Couldn't write file '" + outFile.getAbsolutePath() + "'", e);
		} finally {
			if(out != null) try { out.close(); } catch(IOException e) {}
		}
	}
	
	private void writeFile(File outFile, String text) {
		try {
			writeFile(outFile, text.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private void copyFile(ProjectFile file, File out, Map<String, String> values) {
		File outFile = new File(out, file.outputName);
		if(!outFile.getParentFile().exists() && !outFile.getParentFile().mkdirs()) {
			throw new RuntimeException("Couldn't create dir '" + outFile.getAbsolutePath() + "'");
		}
		
		if(file.isTemplate) {
			String txt = readResourceAsString(file.resourceName);
			txt = replace(txt, values);
			writeFile(outFile, txt);
		} else {
			writeFile(outFile, readResource(file.resourceName));
		}
	}

	private String replace (String txt, Map<String, String> values) {
		for(String key: values.keySet()) {
			String value = values.get(key);
			txt = txt.replace(key, value);
		}
		return txt;
	}
	
	private static void printHelp() {
		System.out.println("Usage: GdxSetup --dir <dir-name> --name <app-name> --package <package> --mainClass <mainClass>");
	}
	
	private static Map<String, String> parseArgs(String[] args) {
		if(args.length % 2 != 0) {
			printHelp();
			System.exit(-1);
		}
		
		Map<String, String> params = new HashMap<String, String>();
		for(int i = 0; i < args.length; i+=2) {
			String param = args[i].replace("--", "");
			String value = args[i+1];
			params.put(param, value);
		}
		return params;
	}
	
	public static void main (String[] args) {
		Map<String, String> params = parseArgs(args);
		if(!params.containsKey("dir") || !params.containsKey("name") || !params.containsKey("package") || !params.containsKey("mainClass")) {
			printHelp();
			System.exit(-1);
		}
		
		new GdxSetup().build(params.get("dir"), params.get("name"), params.get("package"), params.get("mainClass"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/493.java