error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8417.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8417.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8417.java
text:
```scala
g@@enerate("src", "bin", "jni", null, null);

package com.badlogic.gdx.jnigen;

import java.io.InputStream;
import java.nio.Buffer;
import java.util.ArrayList;

import com.badlogic.gdx.jnigen.FileDescriptor.FileType;
import com.badlogic.gdx.jnigen.parsing.CMethodParser;
import com.badlogic.gdx.jnigen.parsing.CMethodParser.CMethod;
import com.badlogic.gdx.jnigen.parsing.CMethodParser.CMethodParserResult;
import com.badlogic.gdx.jnigen.parsing.JavaMethodParser;
import com.badlogic.gdx.jnigen.parsing.JavaMethodParser.Argument;
import com.badlogic.gdx.jnigen.parsing.JavaMethodParser.JavaMethod;
import com.badlogic.gdx.jnigen.parsing.JavaMethodParser.JavaSegment;
import com.badlogic.gdx.jnigen.parsing.JavaMethodParser.JniSection;
import com.badlogic.gdx.jnigen.parsing.JniHeaderCMethodParser;
import com.badlogic.gdx.jnigen.parsing.RobustJavaMethodParser;

/**
 * Goes through a Java source directory, checks each .java file for native methods and emits
 * C/C++ code accordingly, both .h and .cpp files.
 * 
 * <h2>Augmenting Java Files with C/C++</h2>
 * C/C++ code can be directly added to native methods in the Java file as block comments starting at the
 * same line as the method signature. Custom JNI code that is not associated with a native method can be
 * added via a special block comment as shown below.</p>
 * 
 * All arguments can be accessed by the name specified in the Java native method signature (unless you use $ in your identifier which is allowed in Java).
 * 
 * <pre>
 * package com.badlogic.jnigen;
 * 
 * public class MyJniClass {
 *   /*JNI
 *   #include &lt;math.h&gt;
 *   *<i>/</i>
 * 
 *   public native void addToArray(float[] array, int len, float value); /*
 *     for(int i = 0; i < len; i++) {
 *       array[i] = value;
 *     }
 *   *<i>/</i>
 * }
 * </pre>
 * 
 * The generated header file is automatically included in the .cpp file. Methods and custom JNI code
 * can be mixed throughout the Java file, their order is preserved in the generated .cpp file. Method
 * overloading is supported but not recommended as the overloading detection is very basic.</p>
 * 
 * If a native method has strings, one dimensional primitive arrays or direct {@link Buffer} instances as arguments,
 * JNI setup and cleanup code is automatically generated.</p>
 * 
 * The following list gives the mapping from Java
 * to C/C++ types for arguments:
 * 
 * <table border="1">
 * <tr><td>Java</td><td>C/C++</td></tr>
 * <tr><td>String</td><td>char* (UTF-8)</td></tr>
 * <tr><td>boolean[]</td><td>bool*</td></tr>
 * <tr><td>byte[]</td><td>char*</td></tr>
 * <tr><td>char[]</td><td>unsigned short*</td></tr>
 * <tr><td>short[]</td><td>short*</td></tr>
 * <tr><td>int[]</td><td>int*</td></tr>
 * <tr><td>long[]</td><td>long long*</td></tr>
 * <tr><td>float[]</td><td>float*</td></tr>
 * <tr><td>double[]</td><td>double*</td></tr>
 * <tr><td>Buffer</td><td>unsigned char*</td></tr>
 * <tr><td>ByteBuffer</td><td>char*</td></tr>
 * <tr><td>CharBuffer</td><td>unsigned short*</td></tr>
 * <tr><td>ShortBuffer</td><td>short*</td></tr>
 * <tr><td>IntBuffer</td><td>int*</td></tr>
 * <tr><td>LongBuffer</td><td>long long*</td></tr>
 * <tr><td>FloatBuffer</td><td>float*</td></tr>
 * <tr><td>DoubleBuffer</td><td>double*</td></tr>
 * <tr><td>Anything else</td><td>jobject/jobjectArray</td></tr>
 * </table>
 * 
 * <h2>.h/.cpp File Generation</h2>
 * The .h files are created via javah, which has to be on your path. The Java classes have to be compiled and
 * accessible to the javah tool. The name of the generated .h/.cpp files is the fully qualified name of the
 *  class, e.g. com.badlogic.jnigen.MyJniClass.h/.cpp. The generator takes the following parameters as input:
 *  
 * <ul>
 * <li>Java source directory, containing the .java files, e.g. src/ in an Eclipse project</li>
 * <li>Java class directory, containing the compiled .class files, e.g. bin/ in an Eclipse project</li>
 * <li>JNI output directory, where the resulting .h and .cpp files will be stored, e.g. jni/</li>
 * </ul>
 * 
 * A default invocation of the generator looks like this:
 * 
 * <pre>
 * new NativeCodeGenerator().generate("src", "bin", "jni");
 * </pre>
 * 
 * To automatically compile and load the native code, see the classes {@link AntScriptGenerator}, {@link BuildExecutor}
 * and {@link JniGenSharedLibraryLoader} classes.
 * </p>
 * 
 * @author mzechner
 *
 */
public class NativeCodeGenerator {
	private static final String JNI_METHOD_MARKER = "native";
	private static final String JNI_ARG_PREFIX = "obj_";
	private static final String JNI_RETURN_VALUE = "JNI_returnValue"; 
	private static final String JNI_WRAPPER_PREFIX = "wrapped_";
	FileDescriptor sourceDir;
	String classpath;
	FileDescriptor jniDir;
	String[] includes;
	String[] excludes;
	AntPathMatcher matcher = new AntPathMatcher();
	JavaMethodParser javaMethodParser = new RobustJavaMethodParser();
	CMethodParser cMethodParser = new JniHeaderCMethodParser();
	CMethodParserResult cResult;
	
	/**
	 * Generates .h/.cpp files from the Java files found in "src/", with their .class files
	 * being in "bin/". The generated files will be stored in "jni/". All paths are relative
	 * to the applications working directory. 
	 * @throws Exception
	 */
	public void generate() throws Exception {
		generate("src", "bin", "jni", new String[0], new String[0]);
	}
	
	/**
	 * Generates .h/.cpp fiels from the Java files found in <code>sourceDir</code>, with
	 * their .class files being in <code>classpath</code>. The generated files will be stored
	 * in <code>jniDir</code>. All paths are relative to the applications working directory.
	 * @param sourceDir the directory containing the Java files
	 * @param classpath the directory containing the .class files
	 * @param jniDir the output directory
	 * @throws Exception
	 */
	public void generate(String sourceDir, String classpath, String jniDir) throws Exception {
		generate(sourceDir, classpath, jniDir, null, null);
	}
	
	/**
	 * Generates .h/.cpp fiels from the Java files found in <code>sourceDir</code>, with
	 * their .class files being in <code>classpath</code>. The generated files will be stored
	 * in <code>jniDir</code>. The <code>includes</code> and <code>excludes</code> parameters
	 * allow to specify directories and files that should be included/excluded from the generation.
	 * These can be given in the Ant path format. All paths are relative to the applications working directory.
	 * @param sourceDir the directory containing the Java files
	 * @param classpath the directory containing the .class files
	 * @param jniDir the output directory
	 * @param includes files/directories to include, can be null (all files are used)
	 * @param excludes files/directories to exclude, can be null (no files are excluded)
	 * @throws Exception
	 */
	public void generate(String sourceDir, String classpath, String jniDir, String[] includes, String[] excludes) throws Exception {
		this.sourceDir = new FileDescriptor(sourceDir);
		this.jniDir = new FileDescriptor(jniDir);
		this.classpath = classpath;
		this.includes = includes;
		this.excludes = excludes;
		
		// check if source directory exists
		if(!this.sourceDir.exists()) {
			throw new Exception("Java source directory '" + sourceDir + "' does not exist");
		}
		
		// generate jni directory if necessary
		if(!this.jniDir.exists()) {
			if(!this.jniDir.mkdirs()) {
				throw new Exception("Couldn't create JNI directory '" + jniDir + "'");
			}
		}
		
		// process the source directory, emitting c/c++ files to jniDir
		processDirectory(this.sourceDir);
	}

	private void processDirectory(FileDescriptor dir) throws Exception {
		FileDescriptor[] files = dir.list();
		for (FileDescriptor file : files) {
			if (file.isDirectory()) {
				if(file.path().contains(".svn")) continue;
				if(excludes != null && matcher.match(file.path(), excludes)) continue;
				processDirectory(file);
			} else {
				if (file.extension().equals("java")) {
					if(file.name().contains("NativeCodeGenerator")) continue;
					if(includes != null && !matcher.match(file.path(), includes)) continue;
					if(excludes != null && matcher.match(file.path(), excludes)) continue;
					String className = getFullyQualifiedClassName(file);
					FileDescriptor hFile = new FileDescriptor(jniDir.path() + "/" + className + ".h");
					FileDescriptor cppFile = new FileDescriptor(jniDir + "/" + className + ".cpp");
					if(file.lastModified() < cppFile.lastModified()) {
						System.out.println("C/C++ for '" + file.path() + "' up to date");
						continue;
					}
					String javaContent = file.readString();
					if (javaContent.contains(JNI_METHOD_MARKER)) {
						ArrayList<JavaSegment> javaSegments = javaMethodParser.parse(javaContent);
						if(javaSegments.size() == 0) {
							System.out.println("Skipping '" + file + "', no JNI code found.");
							continue;
						}
						System.out.print("Generating C/C++ for '" + file + "'...");
						generateHFile(file);
						generateCppFile(javaSegments, hFile, cppFile);
						System.out.println("done");
					}
				}
			}
		}
	}

	private String getFullyQualifiedClassName(FileDescriptor file) {
		String className = file.path().replace(sourceDir.path(), "").replace('\\', '.').replace('/', '.').replace(".java", "");
		if (className.startsWith("."))
			className = className.substring(1);
		return className;
	}
	
	private void generateHFile(FileDescriptor file) throws Exception {
		String className = getFullyQualifiedClassName(file);
		String command = "javah -classpath " + classpath + " -o "
				+ jniDir.path() + "/" + className + ".h "
				+ className;
		Process process = Runtime.getRuntime().exec(command);
		process.waitFor();
		if (process.exitValue() != 0) {
			InputStream errorStream = process.getErrorStream();
			int c = 0;
			while ((c = errorStream.read()) != -1) {
				System.out.print((char) c);
			}
		}
	}

	private void generateCppFile(ArrayList<JavaSegment> javaSegments, FileDescriptor hFile, FileDescriptor cppFile) throws Exception {
		String headerFileContent = hFile.readString();
		ArrayList<CMethod> cMethods = cMethodParser.parse(headerFileContent).getMethods();
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("#include <" + hFile.name() + ">\n");
		
		for(JavaSegment segment: javaSegments) {
			if(segment instanceof JniSection) {
				emitJniSection(buffer, (JniSection)segment);
			}
			
			if(segment instanceof JavaMethod) {
				JavaMethod javaMethod = (JavaMethod)segment;
				if(javaMethod.getNativeCode() == null) {
					throw new RuntimeException("Method '" + javaMethod.getName() + "' has no body");
				}
				CMethod cMethod = findCMethod(javaMethod, cMethods);
				if(cMethod == null) throw new RuntimeException("Couldn't find C method for Java method '" + javaMethod.getClassName() + "#" + javaMethod.getName() + "'");
				emitJavaMethod(buffer, javaMethod, cMethod);
			}
		}
		cppFile.writeString(buffer.toString(), false, "UTF-8");
	}
	
	private CMethod findCMethod(JavaMethod javaMethod, ArrayList<CMethod> cMethods) {
		for(CMethod cMethod: cMethods) {
			if(cMethod.getHead().contains(javaMethod.getClassName() + "_" + javaMethod.getName())) {
				// FIXME poor man's overloaded method check...
				// FIXME float test[] won't work, needs to be float[] test.
				if(cMethod.getArgumentTypes().length - 2 == javaMethod.getArguments().size()) {
					boolean match = true;
					for(int i = 2; i < cMethod.getArgumentTypes().length; i++) {
						String cType = cMethod.getArgumentTypes()[i];
						String javaType = javaMethod.getArguments().get(i-2).getType().getJniType();
						if(!cType.equals(javaType)) {
							match = false;
							break;
						}
					}
					
					if(match) {
						return cMethod;
					}
				}
			}
		}
		return null;
	}

	private void emitLineMarker(StringBuffer buffer, int line) {
		buffer.append("\n//@line:");
		buffer.append(line);
		buffer.append("\n");
	}
	
	private void emitJniSection(StringBuffer buffer, JniSection section) {
		emitLineMarker(buffer, section.getStartIndex());
		buffer.append(section.getNativeCode().replace("\r", ""));
	}
	
	private void emitJavaMethod(StringBuffer buffer, JavaMethod javaMethod, CMethod cMethod) {
		// if we have disposable arguments (string, buffer, arry) and if there is a return
		// in the native code (conservative, not syntactically checked), emit a wrapper method.
		if(javaMethod.hasDisposableArgument() && javaMethod.getNativeCode().contains("return")) {
			// get the setup and cleanup code for arrays, buffers and strings
			StringBuffer jniSetupCode = new StringBuffer();
			StringBuffer jniCleanupCode = new StringBuffer();
			StringBuffer additionalArgs = new StringBuffer();
			StringBuffer wrapperArgs = new StringBuffer();
			emitJniSetupCode(jniSetupCode, javaMethod, additionalArgs, wrapperArgs);
			emitJniCleanupCode(jniCleanupCode, javaMethod, cMethod);

			// emit the method containing the actual code, called by the wrapper
			// method with setup pointers to arrays, buffers and strings
			String wrappedMethodName = emitMethodSignature(buffer, javaMethod, cMethod, additionalArgs.toString());
			emitMethodBody(buffer, javaMethod);
			buffer.append("}\n\n");
			
			// emit the wrapper method, the one with the declaration in the header file
			emitMethodSignature(buffer, javaMethod, cMethod, null);
			buffer.append(jniSetupCode);
			
			if(cMethod.getReturnType().equals("void")) {
				buffer.append("\t" + wrappedMethodName + "(" + wrapperArgs.toString() + ");\n\n");
				buffer.append(jniCleanupCode);
				buffer.append("\treturn;\n");
			} else {
				buffer.append("\t" + cMethod.getReturnType() + " " + JNI_RETURN_VALUE + " = " + wrappedMethodName + "(" + wrapperArgs.toString() + ");\n\n");
				buffer.append(jniCleanupCode);
				buffer.append("\treturn " + JNI_RETURN_VALUE + ";\n");
			}
			buffer.append("}\n\n");
		} else {
			// get the setup and cleanup code for arrays, buffers and strings
			StringBuffer jniSetupCode = new StringBuffer();
			StringBuffer jniCleanupCode = new StringBuffer();
			StringBuffer additionalArgs = new StringBuffer();
			StringBuffer wrapperArgs = new StringBuffer();
			emitJniSetupCode(jniSetupCode, javaMethod, additionalArgs, wrapperArgs);
			emitJniCleanupCode(jniCleanupCode, javaMethod, cMethod);
			
			emitMethodSignature(buffer, javaMethod, cMethod, null);
			buffer.append(jniSetupCode);
			emitMethodBody(buffer, javaMethod);
			buffer.append(jniCleanupCode);
			buffer.append("}\n\n");
		}
		
	}
	
	private void emitMethodBody(StringBuffer buffer, JavaMethod javaMethod) {
		// emit a line marker
		emitLineMarker(buffer, javaMethod.getEndIndex());
		
		// FIXME add tabs cleanup
		buffer.append(javaMethod.getNativeCode());
		buffer.append("\n");
	}

	private String emitMethodSignature(StringBuffer buffer, JavaMethod javaMethod, CMethod cMethod, String additionalArguments) {
		// emit head, consisting of JNIEXPORT,return type and method name
		// if this is a wrapped method, prefix the method name
		String wrappedMethodName = null;
		if(additionalArguments != null) {
			String[] tokens = cMethod.getHead().replace("\r\n", "").replace("\n", "").split(" ");
			wrappedMethodName = JNI_WRAPPER_PREFIX + tokens[3];
			buffer.append("static inline ");
			buffer.append(tokens[1]);
			buffer.append(" ");
			buffer.append(wrappedMethodName);
			buffer.append("\n");
		} else {			
			buffer.append(cMethod.getHead());
		}
		
		// construct argument list
		// Differentiate between static and instance method, then output each argument
		if(javaMethod.isStatic()) {
			buffer.append("(JNIEnv* env, jclass clazz");
		} else {
			buffer.append("(JNIEnv* env, jobject object");
		}
		if(javaMethod.getArguments().size() > 0) buffer.append(", ");
		for(int i = 0; i < javaMethod.getArguments().size(); i++) {
			// output the argument type as defined in the header
			buffer.append(cMethod.getArgumentTypes()[i+2]);
			buffer.append(" ");
			// if this is not a POD or an object, we need to add a prefix
			// as we will output JNI code to get pointers to strings, arrays
			// and direct buffers.
			Argument javaArg = javaMethod.getArguments().get(i);
			if(!javaArg.getType().isPlainOldDataType() && !javaArg.getType().isObject()) {
				buffer.append(JNI_ARG_PREFIX);
			}
			// output the name of the argument
			buffer.append(javaArg.getName());
			
			// comma, if this is not the last argument
			if(i < javaMethod.getArguments().size() - 1) buffer.append(", ");
		}
		
		// if this is a wrapper method signature, add the additional arguments
		if(additionalArguments != null) {
			buffer.append(additionalArguments);
		}
		
		// close signature, open method body
		buffer.append(") {\n");
		
		// return the wrapped method name if any
		return wrappedMethodName;
	}
	
	private void emitJniSetupCode(StringBuffer buffer, JavaMethod javaMethod, StringBuffer additionalArgs, StringBuffer wrapperArgs) {
		// add environment and class/object as the two first arguments for 
		// wrapped method.
		if(javaMethod.isStatic()) {
			wrapperArgs.append("env, clazz, ");
		} else {
			wrapperArgs.append("env, object, ");
		}
		
		// arguments for wrapper method
		for(int i = 0; i < javaMethod.getArguments().size(); i++) {
			Argument arg = javaMethod.getArguments().get(i);
			if(!arg.getType().isPlainOldDataType() && !arg.getType().isObject()) {
				wrapperArgs.append(JNI_ARG_PREFIX);
			}
			// output the name of the argument
			wrapperArgs.append(arg.getName());
			if(i < javaMethod.getArguments().size() - 1) wrapperArgs.append(", ");
		}
		
		
		// direct buffer pointers
		for(Argument arg: javaMethod.getArguments()) {
			if(arg.getType().isBuffer()) {
				String type = arg.getType().getBufferCType();
				buffer.append("\t" + type + " " + arg.getName() + " = (" + type + ")env->GetDirectBufferAddress(" + JNI_ARG_PREFIX + arg.getName() + ");\n");
				additionalArgs.append(", ");
				additionalArgs.append(type);
				additionalArgs.append(" ");
				additionalArgs.append(arg.getName());
				wrapperArgs.append(", ");
				wrapperArgs.append(arg.getName());
			}
		}
		
		// string pointers
		for(Argument arg: javaMethod.getArguments()) {
			if(arg.getType().isString()) {
				String type = "char*";
				buffer.append("\t" + type + " " + arg.getName() + " = (" + type + ")env->GetStringUTFChars(" + JNI_ARG_PREFIX + arg.getName() + ", 0);\n");
				additionalArgs.append(", ");
				additionalArgs.append(type);
				additionalArgs.append(" ");
				additionalArgs.append(arg.getName());
				wrapperArgs.append(", ");
				wrapperArgs.append(arg.getName());
			}
		}
		
		// Array pointers, we have to collect those last as GetPrimitiveArrayCritical 
		// will explode into our face if we call another JNI method after that.
		for(Argument arg: javaMethod.getArguments()) {
			if(arg.getType().isPrimitiveArray()) {
				String type = arg.getType().getArrayCType(); 
				buffer.append("\t" + type + " " + arg.getName() + " = (" + type + ")env->GetPrimitiveArrayCritical(" + JNI_ARG_PREFIX + arg.getName() + ", 0);\n");
				additionalArgs.append(", ");
				additionalArgs.append(type);
				additionalArgs.append(" ");
				additionalArgs.append(arg.getName());
				wrapperArgs.append(", ");
				wrapperArgs.append(arg.getName());
			}
		}
		
		// new line for separation
		buffer.append("\n");
	}
	
	private void emitJniCleanupCode(StringBuffer buffer, JavaMethod javaMethod, CMethod cMethod) {
		// emit cleanup code for arrays, must come first
		for(Argument arg: javaMethod.getArguments()) {
			if(arg.getType().isPrimitiveArray()) {
				buffer.append("\tenv->ReleasePrimitiveArrayCritical(" + JNI_ARG_PREFIX + arg.getName() + ", " + arg.getName() + ", 0);\n");
			}
		}
		
		// emit cleanup code for strings
		for(Argument arg: javaMethod.getArguments()) {
			if(arg.getType().isString()) {
				buffer.append("\tenv->ReleaseStringUTFChars(" + JNI_ARG_PREFIX + arg.getName() + ", " + arg.getName() + ");\n");
			}
		}
		
		// new line for separation
		buffer.append("\n");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8417.java