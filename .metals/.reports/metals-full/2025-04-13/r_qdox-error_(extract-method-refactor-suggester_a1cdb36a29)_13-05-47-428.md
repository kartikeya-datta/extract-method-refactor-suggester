error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4595.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4595.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4595.java
text:
```scala
i@@f(this == Buffer) return "unsigned char*";

package com.badlogic.gdx.jnigen.parsing;

import java.util.ArrayList;

public interface JavaMethodParser {
	public ArrayList<JavaSegment> parse(String classFile) throws Exception;
	
	public interface JavaSegment {
		public int getStartIndex();
		
		public int getEndIndex();
	}
	
	public class JniSection implements JavaSegment {
		private String nativeCode;
		private final int startIndex;
		private final int endIndex;
		
		public JniSection(String nativeCode, int startIndex, int endIndex) {
			this.nativeCode = nativeCode;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}

		public String getNativeCode() {
			return nativeCode;
		}
		
		public void setNativeCode(String nativeCode) {
			this.nativeCode = nativeCode;
		}

		public int getStartIndex() {
			return startIndex;
		}

		public int getEndIndex() {
			return endIndex;
		}

		@Override
		public String toString() {
			return "JniSection [nativeCode=" + nativeCode + ", startIndex="
					+ startIndex + ", endIndex=" + endIndex + "]";
		}
	}
	
	public enum ArgumentType {
		Boolean, Byte, Char, Short, Integer, Long, Float, Double, 
		Buffer, ByteBuffer, CharBuffer, ShortBuffer, IntBuffer, LongBuffer, FloatBuffer, DoubleBuffer, 
		BooleanArray, ByteArray, CharArray, ShortArray, IntegerArray, LongArray, FloatArray, DoubleArray,
		String, Object;
		
		public boolean isArray() {
			return toString().endsWith("Array");
		}
		
		public boolean isBuffer() {
			return toString().endsWith("Buffer");
		}
		
		public boolean isObject() {
			return toString().equals("Object");
		}
		
		public boolean isString() {
			return toString().equals("String");
		}
		
		public boolean isPlainOldDataType() {
			return !isString() && !isArray() && !isBuffer() && !isObject();
		}
		
		public String getBufferCType() {
			if(!this.isBuffer()) throw new RuntimeException("ArgumentType " + this + " is not a Buffer!");
			if(this == Buffer) return "char*";
			if(this == ByteBuffer) return "char*";
			if(this == CharBuffer) return "unsigned short*";
			if(this == ShortBuffer) return "short*";
			if(this == IntBuffer) return "int*";
			if(this == LongBuffer) return "long long*";
			if(this == FloatBuffer) return "float*";
			if(this == DoubleBuffer) return "double*";
			throw new RuntimeException("Unknown Buffer type " + this);
		}
		
		public String getArrayCType() {
			if(!this.isArray()) throw new RuntimeException("ArgumentType " + this + " is not an Array!");
			if(this == BooleanArray) return "bool*";
			if(this == ByteArray) return "char*";
			if(this == CharArray) return "unsigned short*";
			if(this == ShortArray) return "short*";
			if(this == IntegerArray) return "int*";
			if(this == LongArray) return "long long*";
			if(this == FloatArray) return "float*";
			if(this == DoubleArray) return "double*";
			throw new RuntimeException("Unknown Array type " + this);
		}
	}

	public static class Argument {
		private final ArgumentType type;
		private final String name;

		public Argument(ArgumentType type, String name) {
			this.type = type;
			this.name = name;
		}

		public ArgumentType getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return "Argument [type=" + type + ", name=" + name + "]";
		}
	}
	
	/**
	 * @author mzechner
	 *
	 */
	public static class JavaMethod implements JavaSegment {
		private final String className;
		private final String name;
		private final boolean isStatic;
		private final String returnType;
		private String nativeCode;
		private final ArrayList<Argument> arguments;
		private final boolean hasDisposableArgument;
		private final int startIndex;
		private final int endIndex;

		public JavaMethod(String className, String name, boolean isStatic, String returnType, String nativeCode, ArrayList<Argument> arguments, int startIndex, int endIndex) {
			this.className = className;
			this.name = name;
			this.isStatic = isStatic;
			this.returnType = returnType;
			this.nativeCode = nativeCode;
			this.arguments = arguments;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			for(Argument arg: arguments) {
				if(arg.type.isArray() || arg.type.isBuffer() || arg.type.isString()) {
					hasDisposableArgument = true;
					return;
				}
			}
			hasDisposableArgument = false;
		}		
		
		public String getName() {
			return name;
		}
		
		public boolean isStatic() {
			return isStatic;
		}

		public String getReturnType() {
			return returnType;
		}

		public String getNativeCode() {
			return nativeCode;
		}
		
		public void setNativeCode(String nativeCode) {
			this.nativeCode = nativeCode;
		}

		public ArrayList<Argument> getArguments() {
			return arguments;
		}

		public boolean hasDisposableArgument() {
			return hasDisposableArgument;
		}
		
		@Override
		public int getStartIndex() {
			return startIndex;
		}

		@Override
		public int getEndIndex() {
			return endIndex;
		}

		public CharSequence getClassName() {
			return className;
		}

		@Override
		public String toString() {
			return "JavaMethod [className=" + className + ", name=" + name
					+ ", isStatic=" + isStatic + ", returnType=" + returnType
					+ ", nativeCode=" + nativeCode + ", arguments=" + arguments
					+ ", hasDisposableArgument=" + hasDisposableArgument
					+ ", startIndex=" + startIndex + ", endIndex=" + endIndex
					+ "]";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/4595.java