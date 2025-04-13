error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5784.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5784.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5784.java
text:
```scala
i@@f (name.startsWith(pkgZipPath) && name.indexOf('/', pkgZipPath.length() + 1) == -1 && name.endsWith(".class")) {

/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.*;
import java.util.Arrays;

import org.eclipse.swt.SWT;

public class JNIGeneratorApp {

	Class mainClass;
	ProgressMonitor progress;
	String mainClassName, outputDir, classpath;
	MetaData metaData;

public JNIGeneratorApp() {
}

public String getClasspath() {
	return classpath;
}

public Class getMainClass() {
	return mainClass;
}

public String getMainClassName() {
	return mainClassName;
}

public MetaData getMetaData() {
	return metaData;
}

String getMetaDataDir() {
	return "./JNI Generation/org/eclipse/swt/tools/internal/";
}

public String getOutputDir() {
	return outputDir;
}

public void generateAll() {
	String mainClasses = new MetaData(getDefaultMainClass()).getMetaData("swt_main_classes", null);
	if (mainClasses != null) {
		String[] list = ItemData.split(mainClasses, ",");
		for (int i = 0; i < list.length; i += 2) {
			String className = list[i].trim();
			try {
				Class.forName(className, false, getClass().getClassLoader());
			} catch (Throwable e) {
				continue;
			}
			System.out.println("Generating \"" + className + "\"");
			setMainClassName(className);
			generate();
		}
	}
}

void generateSTATS_C(Class[] classes) {
	try {
		StatsGenerator gen = new StatsGenerator(false);
		gen.setMainClass(mainClass);
		gen.setClasses(classes);
		gen.setMetaData(metaData);
		gen.setProgressMonitor(progress);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		gen.setOutput(new PrintStream(out));
		gen.generate();
		if (out.size() > 0) output(out.toByteArray(), outputDir + gen.getFileName());
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

void generateSTATS_H(Class[] classes) {
	try {
		StatsGenerator gen = new StatsGenerator(true);
		gen.setMainClass(mainClass);
		gen.setClasses(classes);
		gen.setMetaData(metaData);
		gen.setProgressMonitor(progress);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		gen.setOutput(new PrintStream(out));
		gen.generate();
		if (out.size() > 0) output(out.toByteArray(), outputDir + gen.getFileName());
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

void generateSTRUCTS_H(Class[] classes) {
	try {
		StructsGenerator gen = new StructsGenerator(true);
		gen.setMainClass(mainClass);
		gen.setClasses(classes);
		gen.setMetaData(metaData);
		gen.setProgressMonitor(progress);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		gen.setOutput(new PrintStream(out));
		gen.generate();
		if (out.size() > 0) output(out.toByteArray(), outputDir + gen.getFileName());
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}

}

void generateSTRUCTS_C(Class[] classes) {
	try {
		StructsGenerator gen = new StructsGenerator(false);
		gen.setMainClass(mainClass);
		gen.setClasses(classes);
		gen.setMetaData(metaData);
		gen.setProgressMonitor(progress);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		gen.setOutput(new PrintStream(out));
		gen.generate();
		if (out.size() > 0) output(out.toByteArray(), outputDir + gen.getFileName());
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}

}

void generateSWT_C(Class[] classes) {
	try {
		NativesGenerator gen = new NativesGenerator();
		gen.setMainClass(mainClass);
		gen.setClasses(classes);
		gen.setMetaData(metaData);
		gen.setProgressMonitor(progress);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		gen.setOutput(new PrintStream(out));
		gen.generate();
		if (out.size() > 0) output(out.toByteArray(), outputDir + gen.getFileName());
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}


void generateMetaData(Class[] classes) {
	try {
		MetaDataGenerator gen = new MetaDataGenerator();
		gen.setMainClass(mainClass);
		gen.setClasses(classes);
		gen.setMetaData(metaData);
		gen.setProgressMonitor(progress);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		gen.setOutput(new PrintStream(out));
		gen.generate();
		if (!new File(getMetaDataDir()).exists()) {
			System.out.println("Warning: Meta data output dir does not exist");
			return;
		}
		if (out.size() > 0) output(out.toByteArray(), getMetaDataDir() + gen.getFileName());
	} catch (Exception e) {
		System.out.println("Problem");
		e.printStackTrace(System.out);
	}
}

public void generate() {
	generate(null);
}

public void generate(ProgressMonitor progress) {
	if (mainClassName == null) return;
	if (progress != null) progress.setMessage("Initializing...");
	Class[] classes = getClasses();
	Class[] natives = getNativesClasses();
	Class[] structs = getStructureClasses();
	this.progress = progress;
	if (progress != null) {
		int nativeCount = 0;
		for (int i = 0; i < natives.length; i++) {
			Class clazz = natives[i];
			Method[] methods = clazz.getDeclaredMethods();
			for (int j = 0; j < methods.length; j++) {
				Method method = methods[j];
				if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
				nativeCount++;
			}
		}
		int total = nativeCount * 4;
		total += classes.length;
		total += natives.length * 3;
		total += structs.length * 2;
		progress.setTotal(total);
		progress.setMessage("Generating structs.h ...");
	}
	generateSTRUCTS_H(structs);
	if (progress != null) progress.setMessage("Generating structs.c ...");
	generateSTRUCTS_C(structs);
	if (progress != null) progress.setMessage("Generating natives ...");
	generateSWT_C(natives);
	if (progress != null) progress.setMessage("Generating stats.h ...");
	generateSTATS_H(natives);
	if (progress != null) progress.setMessage("Generating stats.c ...");
	generateSTATS_C(natives);
	if (progress != null) progress.setMessage("Generating meta data ...");
	generateMetaData(classes);
	if (progress != null) progress.setMessage("Done.");
	this.progress = null;
}

boolean compare(InputStream is1, InputStream is2) throws IOException {
	while (true) {
		int c1 = is1.read();
		int c2 = is2.read();
		if (c1 != c2) return false;
		if (c1 == -1) break;
	}
	return true;
}

void output(byte[] bytes, String fileName) throws IOException {
	FileInputStream is = null;
	try {
		is = new FileInputStream(fileName);
		if (compare(new ByteArrayInputStream(bytes), new BufferedInputStream(is))) return;
	} catch (FileNotFoundException e) {
	} finally {
		try {
			if (is != null) is.close();
		} catch (IOException e) {}
	}
	FileOutputStream out = new FileOutputStream(fileName);
	out.write(bytes);
	out.close();
}

String getPackageName(String className) {
	int dot = mainClassName.lastIndexOf('.');
	if (dot == -1) return "";
	return mainClassName.substring(0, dot);
}

String[] getClassNames(String mainClassName) {
	String pkgName = getPackageName(mainClassName);
	String classpath = getClasspath();
	if (classpath == null) classpath = System.getProperty("java.class.path");
	String pkgPath = pkgName.replace('.', File.separatorChar);
	String pkgZipPath = pkgName.replace('.', '/');
	ArrayList classes = new ArrayList();	
	int start = 0;
	int index = 0;
	while (index < classpath.length()) {
		index = classpath.indexOf(File.pathSeparatorChar, start);
		if (index == -1) index = classpath.length();
		String path = classpath.substring(start, index);
		if (path.toLowerCase().endsWith(".jar")) {
			ZipFile zipFile = null;
			try {
				zipFile = new ZipFile(path);
				Enumeration entries = zipFile.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = (ZipEntry)entries.nextElement();
					String name = entry.getName();
					if (name.startsWith(pkgZipPath) && name.endsWith(".class")) {
						String className = name.substring(pkgZipPath.length() + 1, name.length() - 6);
						className.replace('/', '.');
						classes.add(className);
					}
				}
			} catch (IOException e) {
			} finally {
				try {
					if (zipFile != null) zipFile.close();
				} catch (IOException ex) {}
			}
		} else {
			File file = new File(path + File.separator + pkgPath);
			if (file.exists()) {
				String[] entries = file.list();
				for (int i = 0; i < entries.length; i++) {
					String entry = entries[i];
					File f = new File(file, entry);
					if (!f.isDirectory()) {
						if (f.getAbsolutePath().endsWith(".class")) {
							String className = entry.substring(0, entry.length() - 6);
							classes.add(className);
						}
					}					
				}
			}
		}
		start = index + 1;
	}
	return (String[])classes.toArray(new String[classes.size()]);
}

public Class[] getClasses() {
	if (mainClassName == null) return new Class[0];
	String[] classNames = getClassNames(mainClassName);
	Arrays.sort(classNames);
	String packageName = getPackageName(mainClassName);
	Class[] classes = new Class[classNames.length];
	for (int i = 0; i < classNames.length; i++) {
		String className = classNames[i];
		try {
			classes[i] = Class.forName(packageName + "." + className, false, getClass().getClassLoader());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	return classes;
}

public Class[] getNativesClasses() {
	if (mainClassName == null) return new Class[0];
	ArrayList result = new ArrayList();
	Class[] classes = getClasses();
	for (int i = 0; i < classes.length; i++) {
		Class clazz = classes[i];
		Method[] methods = clazz.getDeclaredMethods();
		for (int j = 0; j < methods.length; j++) {
			Method method = methods[j];
			int mods = method.getModifiers();
			if ((mods & Modifier.NATIVE) != 0) {
				result.add(clazz);
				break;
			}
		}
	}
	return (Class[])result.toArray(new Class[result.size()]);
}

public Class[] getStructureClasses() {
	if (mainClassName == null) return new Class[0];
	ArrayList result = new ArrayList();
	Class[] classes = getClasses();
	outer:
	for (int i = 0; i < classes.length; i++) {
		Class clazz = classes[i];
		Method[] methods = clazz.getDeclaredMethods();
		for (int j = 0; j < methods.length; j++) {
			Method method = methods[j];
			int mods = method.getModifiers();
			if ((mods & Modifier.NATIVE) != 0) continue outer;
		}
		Field[] fields = clazz.getFields();
		boolean hasPublicFields = false;
		for (int j = 0; j < fields.length; j++) {
			Field field = fields[j];
			int mods = field.getModifiers();
			if ((mods & Modifier.PUBLIC) != 0 && (mods & Modifier.STATIC) == 0) {
				hasPublicFields = true;
				break;
			}
		}
		if (!hasPublicFields) continue;
		result.add(clazz);
	}
	return (Class[])result.toArray(new Class[result.size()]);
}

public void setClasspath(String classpath) {
	this.classpath = classpath;
}

public void setMainClassName(String str) {
	mainClassName = str;
	metaData = new MetaData(mainClassName);
	String mainClasses = getMetaData().getMetaData("swt_main_classes", null);
	if (mainClasses != null) {
		String[] list = ItemData.split(mainClasses, ",");
		for (int i = 0; i < list.length; i += 2) {
			if (mainClassName.equals(list[i].trim())) {
				setOutputDir(list[i + 1].trim());
			}
		}
	}
	if (mainClassName != null) {
		try {
			mainClass = Class.forName(mainClassName, false, getClass().getClassLoader());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

public void setOutputDir(String str) {
	if (str != null) {
		if (!str.endsWith("\\") && !str.endsWith("/") ) {
			str += File.separator;
		}
	}
	outputDir = str;
}

public static String getDefaultMainClass() {
	return "org.eclipse.swt.internal." + getDefaultPlatform() + ".OS";
}

public static String getDefaultPlatform() {
	return SWT.getPlatform();
}

public static void main(String[] args) {
	JNIGeneratorApp gen = new JNIGeneratorApp ();
	if (args.length == 1 && (args[0].equals("*") || args[0].equals("all"))) {
		gen.generateAll();
		return;
	}
	if (args.length > 0) {
		gen.setMainClassName(args[0]);
		if (args.length > 1) gen.setOutputDir(args[1]);
		if (args.length > 2) gen.setClasspath(args[2]);
	} else {
		gen.setMainClassName(getDefaultMainClass());
	}
	gen.generate();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5784.java