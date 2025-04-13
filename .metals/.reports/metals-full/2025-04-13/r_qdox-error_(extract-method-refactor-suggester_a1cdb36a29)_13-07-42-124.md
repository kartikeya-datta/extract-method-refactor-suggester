error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2039.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2039.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2039.java
text:
```scala
r@@eturn ArgumentType.ObjectArray;

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

package com.badlogic.gdx.jnigen.parsing;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class RobustJavaMethodParser implements JavaMethodParser {
	private static final String JNI_MANUAL = "MANUAL";
	private static final Map<String, ArgumentType> plainOldDataTypes;
	private static final Map<String, ArgumentType> arrayTypes;
	private static final Map<String, ArgumentType> bufferTypes;

	static {
		plainOldDataTypes = new HashMap<String, ArgumentType>();
		plainOldDataTypes.put("boolean", ArgumentType.Boolean);
		plainOldDataTypes.put("byte", ArgumentType.Byte);
		plainOldDataTypes.put("char", ArgumentType.Char);
		plainOldDataTypes.put("short", ArgumentType.Short);
		plainOldDataTypes.put("int", ArgumentType.Integer);
		plainOldDataTypes.put("long", ArgumentType.Long);
		plainOldDataTypes.put("float", ArgumentType.Float);
		plainOldDataTypes.put("double", ArgumentType.Double);

		arrayTypes = new HashMap<String, ArgumentType>();
		arrayTypes.put("boolean", ArgumentType.BooleanArray);
		arrayTypes.put("byte", ArgumentType.ByteArray);
		arrayTypes.put("char", ArgumentType.CharArray);
		arrayTypes.put("short", ArgumentType.ShortArray);
		arrayTypes.put("int", ArgumentType.IntegerArray);
		arrayTypes.put("long", ArgumentType.LongArray);
		arrayTypes.put("float", ArgumentType.FloatArray);
		arrayTypes.put("double", ArgumentType.DoubleArray);

		bufferTypes = new HashMap<String, ArgumentType>();
		bufferTypes.put("Buffer", ArgumentType.Buffer);
		bufferTypes.put("ByteBuffer", ArgumentType.ByteBuffer);
		bufferTypes.put("CharBuffer", ArgumentType.CharBuffer);
		bufferTypes.put("ShortBuffer", ArgumentType.ShortBuffer);
		bufferTypes.put("IntBuffer", ArgumentType.IntBuffer);
		bufferTypes.put("LongBuffer", ArgumentType.LongBuffer);
		bufferTypes.put("FloatBuffer", ArgumentType.FloatBuffer);
		bufferTypes.put("DoubleBuffer", ArgumentType.DoubleBuffer);
	}

	Stack<TypeDeclaration> classStack = new Stack<TypeDeclaration>();

	@Override
	public ArrayList<JavaSegment> parse (String classFile) throws Exception {
		CompilationUnit unit = JavaParser.parse(new ByteArrayInputStream(classFile.getBytes()));
		ArrayList<JavaMethod> methods = new ArrayList<JavaMethod>();
		getJavaMethods(methods, getOuterClass(unit));
		ArrayList<JniSection> methodBodies = getNativeCodeBodies(classFile);
		ArrayList<JniSection> sections = getJniSections(classFile);
		alignMethodBodies(methods, methodBodies);
		ArrayList<JavaSegment> segments = sortMethodsAndSections(methods, sections);
		return segments;
	}

	private ArrayList<JavaSegment> sortMethodsAndSections (ArrayList<JavaMethod> methods, ArrayList<JniSection> sections) {
		ArrayList<JavaSegment> segments = new ArrayList<JavaSegment>();
		segments.addAll(methods);
		segments.addAll(sections);
		Collections.sort(segments, new Comparator<JavaSegment>() {
			@Override
			public int compare (JavaSegment o1, JavaSegment o2) {
				return o1.getStartIndex() - o2.getStartIndex();
			}
		});
		return segments;
	}

	private void alignMethodBodies (ArrayList<JavaMethod> methods, ArrayList<JniSection> methodBodies) {
		for (JavaMethod method : methods) {
			for (JniSection section : methodBodies) {
				if (method.getEndIndex() == section.getStartIndex()) {
					if (section.getNativeCode().startsWith(JNI_MANUAL)) {
						section.setNativeCode(section.getNativeCode().substring(JNI_MANUAL.length()));
						method.setManual(true);
					}
					method.setNativeCode(section.getNativeCode());
					break;
				}
			}
		}
	}

	private void getJavaMethods (ArrayList<JavaMethod> methods, TypeDeclaration type) {
		classStack.push(type);
		if (type.getMembers() != null) {
			for (BodyDeclaration member : type.getMembers()) {
				if (member instanceof ClassOrInterfaceDeclaration || member instanceof EnumDeclaration) {
					getJavaMethods(methods, (TypeDeclaration)member);
				} else {
					if (member instanceof MethodDeclaration) {
						MethodDeclaration method = (MethodDeclaration)member;
						if (!ModifierSet.hasModifier(((MethodDeclaration)member).getModifiers(), ModifierSet.NATIVE)) continue;
						methods.add(createMethod(method));
					}
				}
			}
		}
		classStack.pop();
	}

	private JavaMethod createMethod (MethodDeclaration method) {
		String className = classStack.peek().getName();
		String name = method.getName();
		boolean isStatic = ModifierSet.hasModifier(method.getModifiers(), ModifierSet.STATIC);
		String returnType = method.getType().toString();
		ArrayList<Argument> arguments = new ArrayList<Argument>();

		if (method.getParameters() != null) {
			for (Parameter parameter : method.getParameters()) {
				arguments.add(new Argument(getArgumentType(parameter), parameter.getId().getName()));
			}
		}

		return new JavaMethod(className, name, isStatic, returnType, null, arguments, method.getBeginLine(), method.getEndLine());
	}

	private ArgumentType getArgumentType (Parameter parameter) {
		String[] typeTokens = parameter.getType().toString().split("\\.");
		String type = typeTokens[typeTokens.length - 1];
		int arrayDim = 0;
		for (int i = 0; i < type.length(); i++) {
			if (type.charAt(i) == '[') arrayDim++;
		}
		type = type.replace("[", "").replace("]", "");

		if (arrayDim >= 1) {
			if (arrayDim > 1) return ArgumentType.ObjectArray;
			ArgumentType arrayType = arrayTypes.get(type);
			if (arrayType == null) {
				throw new RuntimeException("Unknown array type " + type);
			}
			return arrayType;
		}

		if (plainOldDataTypes.containsKey(type)) return plainOldDataTypes.get(type);
		if (bufferTypes.containsKey(type)) return bufferTypes.get(type);
		if (type.equals("String")) return ArgumentType.String;
		return ArgumentType.Object;
	}

	private TypeDeclaration getOuterClass (CompilationUnit unit) {
		for (TypeDeclaration type : unit.getTypes()) {
			if (type instanceof ClassOrInterfaceDeclaration || type instanceof EnumDeclaration) return type;
		}
		throw new RuntimeException("Couldn't find class, is your java file empty?");
	}

	private ArrayList<JniSection> getJniSections (String classFile) {
		ArrayList<JniSection> sections = getComments(classFile);
		Iterator<JniSection> iter = sections.iterator();
		while (iter.hasNext()) {
			JniSection section = iter.next();
			if (!section.getNativeCode().startsWith("JNI")) {
				iter.remove();
			} else {
				section.setNativeCode(section.getNativeCode().substring(3));
			}
		}
		return sections;
	}

	private ArrayList<JniSection> getNativeCodeBodies (String classFile) {
		ArrayList<JniSection> sections = getComments(classFile);
		Iterator<JniSection> iter = sections.iterator();
		while (iter.hasNext()) {
			JniSection section = iter.next();
			if (section.getNativeCode().startsWith("JNI")) iter.remove();
			if (section.getNativeCode().startsWith("-{")) iter.remove();
		}
		return sections;
	}

	private ArrayList<JniSection> getComments (String classFile) {
		ArrayList<JniSection> sections = new ArrayList<JniSection>();

		boolean inComment = false;
		int start = 0;
		int startLine = 0;
		int line = 1;
		for (int i = 0; i < classFile.length() - 2; i++) {
			char c1 = classFile.charAt(i);
			char c2 = classFile.charAt(i + 1);
			char c3 = classFile.charAt(i + 2);
			if (c1 == '\n') line++;
			if (!inComment) {
				if (c1 == '/' && c2 == '*' && c3 != '*') {
					inComment = true;
					start = i;
					startLine = line;
				}
			} else {
				if (c1 == '*' && c2 == '/') {
					sections.add(new JniSection(classFile.substring(start + 2, i), startLine, line));
					inComment = false;
				}
			}
		}

		return sections;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2039.java