error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1488.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1488.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1488.java
text:
```scala
S@@tring limitMsg;

/*

   Derby - Class org.apache.derby.impl.services.bytecode.BCClass

   Copyright 1997, 2004 The Apache Software Foundation or its licensors, as applicable.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derby.impl.services.bytecode;

import org.apache.derby.iapi.services.compiler.ClassBuilder;
import org.apache.derby.iapi.services.compiler.MethodBuilder;
import org.apache.derby.iapi.services.compiler.LocalField;

import org.apache.derby.iapi.services.classfile.ClassHolder;
import org.apache.derby.iapi.services.classfile.ClassMember;
import org.apache.derby.iapi.services.classfile.ClassFormatOutput;
import org.apache.derby.iapi.services.loader.ClassFactory;

import org.apache.derby.iapi.services.monitor.Monitor;

import org.apache.derby.iapi.error.StandardException;
import org.apache.derby.iapi.reference.Property;
import org.apache.derby.iapi.reference.SQLState;

import org.apache.derby.iapi.util.ByteArray;
import org.apache.derby.iapi.services.classfile.VMOpcode;

import java.lang.reflect.Modifier;

import org.apache.derby.iapi.services.sanity.SanityManager;
import org.apache.derby.iapi.services.classfile.VMDescriptor;

import org.apache.derby.impl.services.bytecode.GClass;

import java.io.IOException;

/**
 * ClassBuilder is used to construct a java class's byte array
 * representation.
 *
 * Limitations:
 *   No checking for language use violations such as invalid modifiers
 *	or duplicate field names.
 *   All classes must have a superclass; java.lang.Object must be
 *      supplied if there is no superclass.
 *
 * <p>
 * When a class is first created, it has:
 * <ul>
 * <li> a superclass
 * <li> modifiers
 * <li> a name
 * <li> a package
 * <li> no superinterfaces, methods, fields, or constructors
 * <li> an empty static initializer
 * <li> an empty initializer
 * </ul>
 * <p>
 * MethodBuilder implementations are required to supply a way for
 * Generators to give them code.  Most typically, they may have
 * a stream to which the Generator writes the code that is of
 * the type to satisfy what the Generator is writing.
 * @see Generator#giveCode
 * <p>
 * BCClass is a ClassBuilder implementation for generating java bytecode
 * directly.
 *
 */
class BCClass extends GClass {
	
	/**
	 * Simple text indicating any limits execeeded while generating
	 * the class file.
	 */
	private String limitMsg;
	
	//
	// ClassBuilder interface
	//
	/**
	 * add a field to this class. Fields cannot
	 * be initialized here, they must be initialized
	 * in the static initializer code (static fields)
	 * or in the constructors.
	 * <p>
	 * static fields also added to this list,
	 * with the modifier set appropriately.
	 */
	public LocalField addField(String javaType, String name, int modifiers) {

		Type type = factory.type(javaType);
		// put it into the class holder right away.
		ClassMember field = classHold.addMember(name, type.vmName(), modifiers);
		int cpi = classHold.addFieldReference(field);

		return new BCLocalField(type, cpi);
	}

	/**
	 * At the time the class is completed and bytecode
	 * generated, if there are no constructors then
	 * the default no-arg constructor will be defined.
	 */
	public ByteArray getClassBytecode() throws StandardException {

		// return if already done
		if (bytecode != null) return bytecode;
		
		try {

			if (SanityManager.DEBUG) {
				if (SanityManager.DEBUG_ON("ClassLineNumbers")) {

					ClassFormatOutput sout = new ClassFormatOutput(2);

					int cpiUTF = classHold.addUtf8("GC.java");

					sout.putU2(cpiUTF);

					classHold.addAttribute("SourceFile", sout);
				}
			}

			// the class is now complete, get its bytecode.
			bytecode = classHold.getFileFormat();
			
		} catch (IOException ioe) {
			throw StandardException.newException(
					SQLState.GENERATED_CLASS_LINKAGE_ERROR, ioe, getFullName());
		}

		// release resources, we have the code now.
		// name is not released, it may still be accessed.
		classHold = null;

		if (SanityManager.DEBUG) {
			if (SanityManager.DEBUG_ON("DumpClassFile")) {
				/* Dump the file in derby.system.home */
				String systemHome = System.getProperty(Property.SYSTEM_HOME_PROPERTY,".");
				writeClassFile(systemHome,false,null);
			}
		}

		if (SanityManager.DEBUG) {
		  if (SanityManager.DEBUG_ON("ByteCodeGenInstr")) {
			SanityManager.DEBUG("ByteCodeGenInstr",
				"GEN complete for class "+name);
		  }
		}
		
		if (limitMsg != null)
			throw StandardException.newException(
					SQLState.GENERATED_CLASS_LIMIT_EXCEEDED, getFullName(), limitMsg);
		return bytecode;
	}


	/**
	 * the class's unqualified name
	 */
	public String getName() {
		return name;
	}
 
	/**
	 * a method. Once it is created, thrown
	 * exceptions, statements, and local variable declarations
	 * must be added to it. It is put into its defining class
	 * when it is created.
	 * <verbatim>
	   Java: #modifiers #returnType #methodName() {}
	  		// modifiers is the | of the JVM constants for
	  		// the modifiers such as static, public, etc.
	   </verbatim>
	 * <p>
	 * This is used to start a constructor as well; pass in
	 * null for the returnType when used in that manner.
	 *
	 * @see java.lang.reflect..Modifiers
	 * @param modifiers the | of the Modifiers
	 *	constants representing the visibility and control of this
	 *	method.
	 * @param returnType the return type of the method as its
	 *	Java language type name.
	 * @param methodName the name of the method.
	 *
	 * @return the method builder.
	 */
	public MethodBuilder newMethodBuilder(int modifiers, String returnType,
		String methodName) {

		return newMethodBuilder(modifiers, returnType,
			methodName, (String[]) null);

	}


	/**
	 * a method with parameters. Once it is created, thrown
	 * exceptions, statements, and local variable declarations
	 * must be added to it. It is put into its defining class
	 * when it is created.
	 * <verbatim>
	   Java: #modifiers #returnType #methodName() {}
	  		// modifiers is the | of the JVM constants for
	  		// the modifiers such as static, public, etc.
	   </verbatim>
	 * <p>
	 * This is used to start a constructor as well; pass in
	 * null for the returnType when used in that manner.
	 *
	 * @see java.lang.reflect..Modifiers
	 * @param modifiers the | of the Modifiers
	 *	constants representing the visibility and control of this
	 *	method.
	 * @param returnType the return type of the method as its
	 *	Java language type name.
	 * @param methodName the name of the method.
	 * @param parms an array of ParameterDeclarations representing the
	 *				method's parameters
	 *
	 * @return the method builder.
	 */
	public MethodBuilder newMethodBuilder(int modifiers, String returnType,
		String methodName, String[] parms) {

		if (SanityManager.DEBUG) {
			SanityManager.ASSERT(returnType!=null);
		}

		BCMethod m = new BCMethod(this,
									returnType,
									methodName,
									modifiers,
									parms,
									factory);

		return m;
		
	}


	/**
	 * a constructor. Once it is created, thrown
	 * exceptions, statements, and local variable declarations
	 * must be added to it. It is put into its defining class
	 * when it is created.
	 * <verbatim>
	   Java: #modifiers #className() {}
	  		// modifiers is the | of the JVM constants for
	  		// the modifiers such as static, public, etc.
	  		// className is taken from definingClass.getName()
	   </verbatim>
	 * <p>
	 * This is used to start a constructor as well; pass in
	 * null for the returnType when used in that manner.
	 *
	 * @see Modifiers
	 * @param modifiers the | of the Modifiers
	 *	constants representing the visibility and control of this
	 *	method.
	 *
	 * @return the method builder for the constructor.
	 */
	public MethodBuilder newConstructorBuilder(int modifiers) {

		BCMethod m = new BCMethod(this, "void", "<init>", 
									modifiers,
									(String []) null,
									factory);

		return m;
	}
  	//
	// class interface
	//

	String getSuperClassName() {
		return superClassName;
	}

	/**
	 * Let those that need to get to the
	 * classModify tool to alter the class definition.
	 */
	ClassHolder modify() {
		return classHold;
	}

	/*
	** Method descriptor caching
	*/

	BCClass(ClassFactory cf, String packageName, int classModifiers,
			String className, String superClassName,
			BCJava factory) {

		super(cf, packageName.concat(className));

		if (SanityManager.DEBUG) {
		  if (SanityManager.DEBUG_ON("ByteCodeGenInstr")) {
			SanityManager.DEBUG("ByteCodeGenInstr",
				"GEN starting for class "+className);
		  }
		}

		// by the time the constructor is done, we have:
		//
		// package #packageName;
		// #classModifiers class #className extends #superClassName
		// { }
		//

		name = className;
		if (superClassName == null)
			superClassName = "java.lang.Object";
		this.superClassName = superClassName;

		classType = factory.type(getFullName());

		classHold = new ClassHolder(qualifiedName, factory.type(superClassName).vmNameSimple, classModifiers);

		this.factory = factory;
	}

	protected ClassHolder classHold;

	protected String superClassName;
	protected String name;

	BCJava factory;
	final Type classType;

	ClassFactory getClassFactory() {
		return cf;
	}

	public void newFieldWithAccessors(String getter, String setter,
		int methodModifers,
		boolean staticField, String type) {

		String vmType = factory.type(type).vmName();
		methodModifers |= Modifier.FINAL;


		// add a field, field has same name as get method
		int fieldModifiers = Modifier.PRIVATE;
		if (staticField)
			fieldModifiers |= Modifier.STATIC;

		ClassMember field = classHold.addMember(getter, vmType, fieldModifiers);
		int cpi = classHold.addFieldReference(field);

		/*
		** add the get method
		*/

		String sig = BCMethodDescriptor.get(BCMethodDescriptor.EMPTY, vmType, factory);

		ClassMember method = classHold.addMember(getter, sig, methodModifers);

		CodeChunk chunk = new CodeChunk();

		// load 'this' if required
		if (!staticField)
			chunk.addInstr(VMOpcode.ALOAD_0); // this
		
		// get the field value
		chunk.addInstrU2((staticField ? VMOpcode.GETSTATIC : VMOpcode.GETFIELD), cpi);

		// and return it
		short vmTypeId = BCJava.vmTypeId(vmType);

		chunk.addInstr(CodeChunk.RETURN_OPCODE[vmTypeId]);

		int typeWidth = Type.width(vmTypeId);
		chunk.complete(null, classHold, method, typeWidth, 1);

		/*
		** add the set method
		*/
		String[] pda = new String[1];
		pda[0] = vmType;
		sig = new BCMethodDescriptor(pda, VMDescriptor.VOID, factory).toString();
		method = classHold.addMember(setter, sig, methodModifers);
		chunk = new CodeChunk();

		// load 'this' if required
		if (!staticField)
			chunk.addInstr(VMOpcode.ALOAD_0); // this
		// push the only parameter
		chunk.addInstr((short) (CodeChunk.LOAD_VARIABLE_FAST[vmTypeId] + 1));
		
		// and set the field
		chunk.addInstrU2((staticField ? VMOpcode.PUTSTATIC : VMOpcode.PUTFIELD), cpi);

		chunk.addInstr(VMOpcode.RETURN);

		chunk.complete(null, classHold, method, typeWidth + (staticField ? 0 : 1), 1 + typeWidth);
	}
	
	/**
	 * Add the fact that some class limit was exceeded while generating
	 * the class. We create a set ofg them and report at the end, this
	 * allows the generated class file to still be dumped.
	 * @param mb
	 * @param limitName
	 * @param limit
	 * @param value
	 */
	void addLimitExceeded(BCMethod mb, String limitName, int limit, int value)
	{
		StringBuffer sb = new StringBuffer();
		if (limitMsg != null)
		{
			sb.append(limitMsg);
			sb.append(", ");
		}
		
		sb.append("method:");
		sb.append(mb.getName());
		sb.append(" ");
		sb.append(limitName);
		sb.append(" (");
		sb.append(value);
		sb.append(" > ");
		sb.append(limit);
		sb.append(")");
		
		limitMsg = sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1488.java