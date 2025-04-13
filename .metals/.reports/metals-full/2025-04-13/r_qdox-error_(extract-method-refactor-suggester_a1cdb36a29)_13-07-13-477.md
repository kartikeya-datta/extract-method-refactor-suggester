error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4171.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4171.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4171.java
text:
```scala
o@@utputln(fixDelimiter(getMetaData().getCopyright()));

/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

import java.lang.reflect.Modifier;

public class StatsGenerator extends JNIGenerator {

	boolean header;
	
public StatsGenerator(boolean header) {
	this.header = header;
}

public void generateCopyright() {
	generateMetaData("swt_copyright");
}

public void generateIncludes() {
	if (!header) {
		outputln("#include \"swt.h\"");
		output("#include \"");
		output(getOutputName());
		outputln("_stats.h\"");
		outputln();
	}
}

public void generate(JNIClass clazz) {
	if (header) {
		generateHeaderFile(clazz);
	} else {
		generateSourceFile(clazz);
	}
}

public String getExtension() {
	return header ? ".h" : super.getExtension();
}

public String getSuffix() {
	return "_stats";
}

void generateHeaderFile(JNIClass clazz){
	generateNATIVEMacros(clazz);
	JNIMethod[] methods = clazz.getDeclaredMethods();
	sort(methods);
	generateFunctionEnum(methods);	
}

void generateNATIVEMacros(JNIClass clazz) {
	String className = clazz.getSimpleName();
	outputln("#ifdef NATIVE_STATS");
	output("extern int ");
	output(className);
	outputln("_nativeFunctionCount;");
	output("extern int ");
	output(className);
	outputln("_nativeFunctionCallCount[];");
	output("extern char* ");
	output(className);
	outputln("_nativeFunctionNames[];");
	output("#define ");
	output(className);
	output("_NATIVE_ENTER(env, that, func) ");
	output(className);
	outputln("_nativeFunctionCallCount[func]++;");
	output("#define ");
	output(className);
	outputln("_NATIVE_EXIT(env, that, func) ");
	outputln("#else");
	output("#ifndef ");
	output(className);
	outputln("_NATIVE_ENTER");
	output("#define ");
	output(className);
	outputln("_NATIVE_ENTER(env, that, func) ");
	outputln("#endif");
	output("#ifndef ");
	output(className);
	outputln("_NATIVE_EXIT");
	output("#define ");
	output(className);
	outputln("_NATIVE_EXIT(env, that, func) ");
	outputln("#endif");
	outputln("#endif");
	outputln();	
}

void generateSourceFile(JNIClass clazz) {
	outputln("#ifdef NATIVE_STATS");
	outputln();
	JNIMethod[] methods = clazz.getDeclaredMethods();
	int methodCount = 0;
	for (int i = 0; i < methods.length; i++) {
		JNIMethod method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		methodCount++;
	}
	String className = clazz.getSimpleName();
	output("int ");
	output(className);
	output("_nativeFunctionCount = ");
	output(String.valueOf(methodCount));
	outputln(";");
	output("int ");
	output(className);
	output("_nativeFunctionCallCount[");
	output(String.valueOf(methodCount));
	outputln("];");
	output("char * ");
	output(className);
	outputln("_nativeFunctionNames[] = {");
	sort(methods);
	for (int i = 0; i < methods.length; i++) {
		JNIMethod method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		String function = getFunctionName(method), function64 = getFunctionName(method, method.getParameterTypes64());
		if (!function.equals(function64)) {
			output("#ifndef ");
			output(JNI64);
			outputln();
		}
		output("\t\"");
		output(function);
		outputln("\",");
		if (!function.equals(function64)) {
			outputln("#else");
			output("\t\"");
			output(function64);
			outputln("\",");
			outputln("#endif");
		}
		if (progress != null) progress.step();
	}
	outputln("};");
	outputln();
	generateStatsNatives(className);
	outputln();
	outputln("#endif");
}

void generateStatsNatives(String className) {
	outputln("#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func");
	outputln();

	output("JNIEXPORT jint JNICALL STATS_NATIVE(");
	output(toC(className + "_GetFunctionCount"));
	outputln(")");
	outputln("\t(JNIEnv *env, jclass that)");
	outputln("{");
	output("\treturn ");
	output(className);
	outputln("_nativeFunctionCount;");
	outputln("}");
	outputln();

	output("JNIEXPORT jstring JNICALL STATS_NATIVE(");
	output(toC(className + "_GetFunctionName"));
	outputln(")");
	outputln("\t(JNIEnv *env, jclass that, jint index)");
	outputln("{");
	output("\treturn ");
	if (isCPP) {
		output("env->NewStringUTF(");
	} else {
		output("(*env)->NewStringUTF(env, ");
	}
	output(className);
	outputln("_nativeFunctionNames[index]);");
	outputln("}");
	outputln();

	output("JNIEXPORT jint JNICALL STATS_NATIVE(");
	output(toC(className + "_GetFunctionCallCount"));
	outputln(")");
	outputln("\t(JNIEnv *env, jclass that, jint index)");
	outputln("{");
	output("\treturn ");
	output(className);
	outputln("_nativeFunctionCallCount[index];");
	outputln("}");
}

void generateFunctionEnum(JNIMethod[] methods) {
	if (methods.length == 0) return;
	outputln("typedef enum {");
	for (int i = 0; i < methods.length; i++) {
		JNIMethod method = methods[i];
		if ((method.getModifiers() & Modifier.NATIVE) == 0) continue;
		String function = getFunctionName(method), function64 = getFunctionName(method, method.getParameterTypes64());
		if (!function.equals(function64)) {
			output("#ifndef ");
			output(JNI64);
			outputln();
		}
		output("\t");
		output(function);
		outputln("_FUNC,");
		if (!function.equals(function64)) {
			outputln("#else");
			output("\t");
			output(function64);
			outputln("_FUNC,");
			outputln("#endif");
		}
		if (progress != null) progress.step();
	}
	JNIClass clazz = methods[0].getDeclaringClass();
	output("} ");
	output(clazz.getSimpleName());
	outputln("_FUNCS;");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4171.java