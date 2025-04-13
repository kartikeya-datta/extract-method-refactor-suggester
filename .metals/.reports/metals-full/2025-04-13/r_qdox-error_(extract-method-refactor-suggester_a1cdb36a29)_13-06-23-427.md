error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6795.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6795.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6795.java
text:
```scala
i@@f (!fieldBinding.type.isBaseType()) {

package org.eclipse.jdt.internal.eval;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
 
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.CompoundAssignment;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.IntLiteral;
import org.eclipse.jdt.internal.compiler.lookup.*;

public class CodeSnippetCodeStream extends CodeStream {
/**
 * CodeSnippetCodeStream constructor comment.
 * @param classFile org.eclipse.jdt.internal.compiler.ClassFile
 */
public CodeSnippetCodeStream(org.eclipse.jdt.internal.compiler.ClassFile classFile) {
	super(classFile);
}
protected void checkcast(int baseId) {
	countLabels = 0;
	try {
		position++;
		bCodeStream[classFileOffset++] = OPC_checkcast;
	} catch (IndexOutOfBoundsException e) {
		resizeByteArray(OPC_checkcast);
	}
	switch (baseId) {
		case T_byte :
			writeUnsignedShort(constantPool.literalIndexForJavaLangByte());
			break;
		case T_short :
			writeUnsignedShort(constantPool.literalIndexForJavaLangShort());
			break;
		case T_char :
			writeUnsignedShort(constantPool.literalIndexForJavaLangCharacter());
			break;
		case T_int :
			writeUnsignedShort(constantPool.literalIndexForJavaLangInteger());
			break;
		case T_long :
			writeUnsignedShort(constantPool.literalIndexForJavaLangLong());
			break;
		case T_float :
			writeUnsignedShort(constantPool.literalIndexForJavaLangFloat());
			break;
		case T_double :
			writeUnsignedShort(constantPool.literalIndexForJavaLangDouble());
			break;
		case T_boolean :
			writeUnsignedShort(constantPool.literalIndexForJavaLangBoolean());
	}
}
public void generateEmulatedAccessForMethod(Scope scope, MethodBinding methodBinding) {
	CodeSnippetCodeStream localCodeStream = (CodeSnippetCodeStream) this;
	localCodeStream.generateEmulationForMethod(scope, methodBinding);
	localCodeStream.invokeJavaLangReflectMethodInvoke();
}
public void generateEmulatedReadAccessForField(FieldBinding fieldBinding) {
	CodeSnippetCodeStream localCodeStream = (CodeSnippetCodeStream) this;
	localCodeStream.generateEmulationForField(fieldBinding);
	// swap  the field with the receiver
	this.swap();
	localCodeStream.invokeJavaLangReflectFieldGetter(fieldBinding.type.id);
	if (fieldBinding.type.isArrayType()) {
		this.checkcast(fieldBinding.type);
	}
}
public void generateEmulatedWriteAccessForField(FieldBinding fieldBinding) {
	CodeSnippetCodeStream localCodeStream = (CodeSnippetCodeStream) this;
	localCodeStream.invokeJavaLangReflectFieldSetter(fieldBinding.type.id);
}
public void generateEmulationForConstructor(Scope scope, MethodBinding methodBinding) {
	// leave a java.lang.reflect.Field object on the stack
	CodeSnippetCodeStream localCodeStream = (CodeSnippetCodeStream) this;
	this.ldc(String.valueOf(methodBinding.declaringClass.constantPoolName()).replace('/', '.'));
	this.invokeClassForName();
	int paramLength = methodBinding.parameters.length;
	this.generateInlinedValue(paramLength);
	this.newArray(scope, new ArrayBinding(scope.getType(TypeBinding.JAVA_LANG_CLASS), 1));
	if (paramLength > 0) {
		this.dup();
		for (int i = 0; i < paramLength; i++) {
			this.generateInlinedValue(i);	
			TypeBinding parameter = methodBinding.parameters[i];
			if (parameter.isBaseType()) {
				this.getTYPE(parameter.id);
			} else if (parameter.isArrayType()) {
				ArrayBinding array = (ArrayBinding)parameter;
				if (array.leafComponentType.isBaseType()) {
					this.getTYPE(array.leafComponentType.id);
				} else {
					this.ldc(String.valueOf(array.leafComponentType.constantPoolName()).replace('/', '.'));
					this.invokeClassForName();
				}
				int dimensions = array.dimensions;
				this.generateInlinedValue(dimensions);
				this.newarray(T_int);	
				this.invokeArrayNewInstance();
				this.invokeObjectGetClass();
			} else {
				// parameter is a reference binding
				this.ldc(String.valueOf(methodBinding.declaringClass.constantPoolName()).replace('/', '.'));
				this.invokeClassForName();
			}
			this.aastore();
			if (i < paramLength - 1) {
				this.dup();
			}
		}
	}
	localCodeStream.invokeClassGetDeclaredConstructor();
	this.dup();
	this.iconst_1();
	localCodeStream.invokeAccessibleObjectSetAccessible();
}
public void generateEmulationForField(FieldBinding fieldBinding) {
	// leave a java.lang.reflect.Field object on the stack
	CodeSnippetCodeStream localCodeStream = (CodeSnippetCodeStream) this;
	this.ldc(String.valueOf(fieldBinding.declaringClass.constantPoolName()).replace('/', '.'));
	this.invokeClassForName();
	this.ldc(String.valueOf(fieldBinding.name));
	localCodeStream.invokeClassGetDeclaredField();
	this.dup();
	this.iconst_1();
	localCodeStream.invokeAccessibleObjectSetAccessible();
}
public void generateEmulationForMethod(Scope scope, MethodBinding methodBinding) {
	// leave a java.lang.reflect.Field object on the stack
	CodeSnippetCodeStream localCodeStream = (CodeSnippetCodeStream) this;
	this.ldc(String.valueOf(methodBinding.declaringClass.constantPoolName()).replace('/', '.'));
	this.invokeClassForName();
	this.ldc(String.valueOf(methodBinding.selector));
	int paramLength = methodBinding.parameters.length;
	this.generateInlinedValue(paramLength);
	this.newArray(scope, new ArrayBinding(scope.getType(TypeBinding.JAVA_LANG_CLASS), 1));
	if (paramLength > 0) {
		this.dup();
		for (int i = 0; i < paramLength; i++) {
			this.generateInlinedValue(i);	
			TypeBinding parameter = methodBinding.parameters[i];
			if (parameter.isBaseType()) {
				this.getTYPE(parameter.id);
			} else if (parameter.isArrayType()) {
				ArrayBinding array = (ArrayBinding)parameter;
				if (array.leafComponentType.isBaseType()) {
					this.getTYPE(array.leafComponentType.id);
				} else {
					this.ldc(String.valueOf(array.leafComponentType.constantPoolName()).replace('/', '.'));
					this.invokeClassForName();
				}
				int dimensions = array.dimensions;
				this.generateInlinedValue(dimensions);
				this.newarray(T_int);	
				this.invokeArrayNewInstance();
				this.invokeObjectGetClass();
			} else {
				// parameter is a reference binding
				this.ldc(String.valueOf(methodBinding.declaringClass.constantPoolName()).replace('/', '.'));
				this.invokeClassForName();
			}
			this.aastore();
			if (i < paramLength - 1) {
				this.dup();
			}
		}
	}
	localCodeStream.invokeClassGetDeclaredMethod();
	this.dup();
	this.iconst_1();
	localCodeStream.invokeAccessibleObjectSetAccessible();
}
public void getBaseTypeValue(int baseTypeID) {
	countLabels = 0;
	try {
		position++;
		bCodeStream[classFileOffset++] = OPC_invokevirtual;
	} catch (IndexOutOfBoundsException e) {
		resizeByteArray(OPC_invokevirtual);
	}
	switch (baseTypeID) {
		case T_byte :
			// invokevirtual: byteValue()
			writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangByteByteValue());
			break;
		case T_short :
			// invokevirtual: shortValue()
			writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangShortShortValue());
			break;
		case T_char :
			// invokevirtual: charValue()
			writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangCharacterCharValue());
			break;
		case T_int :
			// invokevirtual: intValue()
			writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangIntegerIntValue());
			break;
		case T_long :
			// invokevirtual: longValue()
			stackDepth++;
			if (stackDepth > stackMax)
				stackMax = stackDepth;
			writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangLongLongValue());
			break;
		case T_float :
			// invokevirtual: floatValue()
			writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangFloatFloatValue());
			break;
		case T_double :
			// invokevirtual: doubleValue()
			stackDepth++;
			if (stackDepth > stackMax)
				stackMax = stackDepth;
			writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangDoubleDoubleValue());
			break;
		case T_boolean :
			// invokevirtual: booleanValue()
			writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangBooleanBooleanValue());
	}
}
protected void invokeAccessibleObjectSetAccessible() {
	// invokevirtual: java.lang.reflect.AccessibleObject.setAccessible(Z)V;
	countLabels = 0;
	try {
		position++;
		bCodeStream[classFileOffset++] = OPC_invokevirtual;
	} catch (IndexOutOfBoundsException e) {
		resizeByteArray(OPC_invokevirtual);
	}
	writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangReflectAccessibleObjectSetAccessible());
	stackDepth-=2;
}
protected void invokeArrayNewInstance() {
	// invokestatic: java.lang.reflect.Array.newInstance(Ljava.lang.Class;int[])Ljava.lang.reflect.Array;
	countLabels = 0;
	try {
		position++;
		bCodeStream[classFileOffset++] = OPC_invokestatic;
	} catch (IndexOutOfBoundsException e) {
		resizeByteArray(OPC_invokestatic);
	}
	writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangReflectArrayNewInstance());
	stackDepth--;
}
protected void invokeClassGetDeclaredConstructor() {
	// invokevirtual: java.lang.Class getDeclaredConstructor([Ljava.lang.Class)Ljava.lang.reflect.Constructor;
	countLabels = 0;
	try {
		position++;
		bCodeStream[classFileOffset++] = OPC_invokevirtual;
	} catch (IndexOutOfBoundsException e) {
		resizeByteArray(OPC_invokevirtual);
	}
	writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangClassGetDeclaredConstructor());
	stackDepth--;
}
protected void invokeClassGetDeclaredField() {
	// invokevirtual: java.lang.Class.getDeclaredField(Ljava.lang.String)Ljava.lang.reflect.Field;
	countLabels = 0;
	try {
		position++;
		bCodeStream[classFileOffset++] = OPC_invokevirtual;
	} catch (IndexOutOfBoundsException e) {
		resizeByteArray(OPC_invokevirtual);
	}
	writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangClassGetDeclaredField());
	stackDepth--;
}
protected void invokeClassGetDeclaredMethod() {
	// invokevirtual: java.lang.Class getDeclaredMethod(Ljava.lang.String, [Ljava.lang.Class)Ljava.lang.reflect.Method;
	countLabels = 0;
	try {
		position++;
		bCodeStream[classFileOffset++] = OPC_invokevirtual;
	} catch (IndexOutOfBoundsException e) {
		resizeByteArray(OPC_invokevirtual);
	}
	writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangClassGetDeclaredMethod());
	stackDepth-=2;
}
protected void invokeJavaLangReflectConstructorNewInstance() {
	// invokevirtual: java.lang.reflect.Constructor.newInstance([Ljava.lang.Object;)Ljava.lang.Object;
	countLabels = 0;
	try {
		position++;
		bCodeStream[classFileOffset++] = OPC_invokevirtual;
	} catch (IndexOutOfBoundsException e) {
		resizeByteArray(OPC_invokevirtual);
	}
	writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangReflectConstructorNewInstance());
	stackDepth--;
}
protected void invokeJavaLangReflectFieldGetter(int typeID) {
	countLabels = 0;
	int usedTypeID;
	if (typeID == T_null)
		usedTypeID = T_Object;
	else
		usedTypeID = typeID;
	// invokevirtual
	try {
		position++;
		bCodeStream[classFileOffset++] = OPC_invokevirtual;
	} catch (IndexOutOfBoundsException e) {
		resizeByteArray(OPC_invokevirtual);
	}
	writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexJavaLangReflectFieldGetter(typeID));
	if ((usedTypeID != T_long) && (usedTypeID != T_double)) {
		stackDepth--;
	}
}
protected void invokeJavaLangReflectFieldSetter(int typeID) {
	countLabels = 0;
	int usedTypeID;
	if (typeID == T_null)
		usedTypeID = T_Object;
	else
		usedTypeID = typeID;
	// invokevirtual
	try {
		position++;
		bCodeStream[classFileOffset++] = OPC_invokevirtual;
	} catch (IndexOutOfBoundsException e) {
		resizeByteArray(OPC_invokevirtual);
	}
	writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexJavaLangReflectFieldSetter(typeID));
	if ((usedTypeID != T_long) && (usedTypeID != T_double)) {
		stackDepth-=3;
	} else {
		stackDepth-=4;
	}
}
protected void invokeJavaLangReflectMethodInvoke() {
	// invokevirtual: java.lang.reflect.Method.invoke(Ljava.lang.Object;[Ljava.lang.Object;)Ljava.lang.Object;
	countLabels = 0;
	try {
		position++;
		bCodeStream[classFileOffset++] = OPC_invokevirtual;
	} catch (IndexOutOfBoundsException e) {
		resizeByteArray(OPC_invokevirtual);
	}
	writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangReflectMethodInvoke());
	stackDepth-=2;
}
protected void invokeObjectGetClass() {
	// invokevirtual: java.lang.Object.getClass()Ljava.lang.Class;
	countLabels = 0;
	try {
		position++;
		bCodeStream[classFileOffset++] = OPC_invokevirtual;
	} catch (IndexOutOfBoundsException e) {
		resizeByteArray(OPC_invokevirtual);
	}
	writeUnsignedShort(((CodeSnippetConstantPool) constantPool).literalIndexForJavaLangObjectGetClass());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6795.java