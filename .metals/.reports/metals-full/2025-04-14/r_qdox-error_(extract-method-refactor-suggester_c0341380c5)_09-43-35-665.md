error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/713.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/713.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/713.java
text:
```scala
n@@estedTypeList.add(sig);

/* *******************************************************************
 * Copyright (c) 2005 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *   Adrian Colyer			Initial implementation
 * ******************************************************************/
package org.aspectj.apache.bcel.classfile;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.apache.bcel.classfile.Signature.ArrayTypeSignature;
import org.aspectj.apache.bcel.classfile.Signature.ClassTypeSignature;
import org.aspectj.apache.bcel.classfile.Signature.FieldTypeSignature;
import org.aspectj.apache.bcel.classfile.Signature.FormalTypeParameter;
import org.aspectj.apache.bcel.classfile.Signature.MethodTypeSignature;
import org.aspectj.apache.bcel.classfile.Signature.SimpleClassTypeSignature;
import org.aspectj.apache.bcel.classfile.Signature.TypeArgument;
import org.aspectj.apache.bcel.classfile.Signature.TypeSignature;
import org.aspectj.apache.bcel.classfile.Signature.TypeVariableSignature;
import org.aspectj.apache.bcel.classfile.Signature.BaseTypeSignature;

/**
 * Parses the generic signature attribute as defined in the JVM spec.
 */
public class GenericSignatureParser {

	  private String[] tokenStream;  // for parse in flight
	  private int tokenIndex = 0;

	/**
	   * AMC.
	   * Parse the signature string interpreting it as a ClassSignature according to
	   * the grammar defined in Section 4.4.4 of the JVM specification.
	   */
	  public Signature.ClassSignature parseAsClassSignature(String sig) {
		  tokenStream = tokenize(sig);
		  tokenIndex = 0;
		  Signature.ClassSignature classSig = new Signature.ClassSignature();
		  // FormalTypeParameters-opt
		  if (maybeEat("<")) {
			List formalTypeParametersList = new ArrayList();
			do {
				formalTypeParametersList.add(parseFormalTypeParameter());
			} while (!maybeEat(">"));
			classSig.formalTypeParameters = new FormalTypeParameter[formalTypeParametersList.size()];
			formalTypeParametersList.toArray(classSig.formalTypeParameters);
		  }
		  classSig.superclassSignature = parseClassTypeSignature();
		  List superIntSigs = new ArrayList();
		  while (tokenIndex < tokenStream.length) {
			  superIntSigs.add(parseClassTypeSignature());
		  }
		  classSig.superInterfaceSignatures = new ClassTypeSignature[superIntSigs.size()];
		  superIntSigs.toArray(classSig.superInterfaceSignatures);
		  return classSig;
	  }
	  
	  /**
	   * AMC.
	   * Parse the signature string interpreting it as a MethodTypeSignature according to
	   * the grammar defined in Section 4.4.4 of the JVM specification.
	   */
	  public MethodTypeSignature parseAsMethodSignature(String sig) {
		  tokenStream = tokenize(sig);
		  tokenIndex = 0;
		  FormalTypeParameter[] formals = new FormalTypeParameter[0];
		  TypeSignature[] params = new TypeSignature[0];
		  TypeSignature returnType = null;
		  FieldTypeSignature[] throwsSigs = new FieldTypeSignature[0];
		  // FormalTypeParameters-opt
		  if (maybeEat("<")) {
			List formalTypeParametersList = new ArrayList();
			do {
				formalTypeParametersList.add(parseFormalTypeParameter());
			} while (!maybeEat(">"));
			formals = new FormalTypeParameter[formalTypeParametersList.size()];
			formalTypeParametersList.toArray(formals);
		  }
		  // Parameters
		  eat("(");
		  List paramList = new ArrayList();
		  while(!maybeEat(")")) {
			  FieldTypeSignature fsig = parseFieldTypeSignature(true);
			  if (fsig != null) {
				  paramList.add(fsig);
			  } else {
				  paramList.add(new Signature.BaseTypeSignature(eatIdentifier()));
			  }			  
		  }
		  params = new TypeSignature[paramList.size()];
		  paramList.toArray(params);
		  // return type
		  returnType = parseFieldTypeSignature(true);
		  if (returnType == null) returnType = new Signature.BaseTypeSignature(eatIdentifier());
		  // throws
		  List throwsList = new ArrayList();
		  while (maybeEat("^")) {
			  FieldTypeSignature fsig = parseFieldTypeSignature(false);
			  throwsList.add(fsig);
		  }
		  throwsSigs = new FieldTypeSignature[throwsList.size()];
		  throwsList.toArray(throwsSigs);
		  return new Signature.MethodTypeSignature(formals,params,returnType,throwsSigs);
	  }
	  
	  /**
	   * AMC.
	   * Parse the signature string interpreting it as a FieldTypeSignature according to
	   * the grammar defined in Section 4.4.4 of the JVM specification.
	   */
	  public FieldTypeSignature parseAsFieldSignature(String sig) {
		  tokenStream = tokenize(sig);
		  tokenIndex = 0;
		  return parseFieldTypeSignature(false);
	  }

	  private FormalTypeParameter parseFormalTypeParameter() {
		  FormalTypeParameter ftp = new FormalTypeParameter();
		  // Identifier
		  ftp.identifier = eatIdentifier();
		  // ClassBound
		  eat(":");
		  ftp.classBound = parseFieldTypeSignature(true);
		  if (ftp.classBound == null) {
			  ftp.classBound = new ClassTypeSignature("Ljava/lang/Object;","Ljava/lang/Object");
		  }
		  // Optional InterfaceBounds
		  List optionalBounds = new ArrayList();
		  while (maybeEat(":")) {
			  optionalBounds.add(parseFieldTypeSignature(false));
		  }
		  ftp.interfaceBounds = new FieldTypeSignature[optionalBounds.size()];
		  optionalBounds.toArray(ftp.interfaceBounds);
		  return ftp;
	  }
	  
	  private FieldTypeSignature parseFieldTypeSignature(boolean isOptional) {
		  if (isOptional) {
			  // anything other than 'L', 'T' or '[' and we're out of here
			  if (!tokenStream[tokenIndex].startsWith("L") &&
				   !tokenStream[tokenIndex].startsWith("T") &&
				   !tokenStream[tokenIndex].startsWith("[")) {
				  return null;
			  }
		  }
		  if (maybeEat("[")) {
			  return parseArrayTypeSignature();
		  } else if (tokenStream[tokenIndex].startsWith("L")) {
			  return parseClassTypeSignature();
		  } else if (tokenStream[tokenIndex].startsWith("T")) {
			  return parseTypeVariableSignature();
		  } else {
			  throw new IllegalStateException("Expection [,L, or T, but found " +
					  tokenStream[tokenIndex]);
		  }
	  }
	  
	  private ArrayTypeSignature parseArrayTypeSignature() {
		  // opening [ already eaten
		  FieldTypeSignature fieldType = parseFieldTypeSignature(true);
		  if (fieldType != null) {
			  return new ArrayTypeSignature(fieldType);
		  } else {
			  // must be BaseType array
			  return new ArrayTypeSignature(new BaseTypeSignature(eatIdentifier()));
		  }
	  }

	  // L PackageSpecifier* SimpleClassTypeSignature ClassTypeSignature* ;
	  private ClassTypeSignature parseClassTypeSignature() {
		  SimpleClassTypeSignature outerType = null;
		  SimpleClassTypeSignature[] nestedTypes = new SimpleClassTypeSignature[0];
		  StringBuffer ret = new StringBuffer();
		  String identifier = eatIdentifier();
		  ret.append(identifier);
		  while (maybeEat("/")) {
			  ret.append("/"); // dont forget this...
			  ret.append(eatIdentifier());
		  }
		  identifier = ret.toString();
		  // now we have either a "." indicating the start of a nested type,
		  // or a "<" indication type arguments, or ";" and we are done.
		  while (!maybeEat(";")) {
			  if (maybeEat(".")) {
				  // outer type completed
				  outerType = new SimpleClassTypeSignature(identifier);
				  List nestedTypeList = new ArrayList();
				  do {
					  ret.append(".");
					  SimpleClassTypeSignature sig = parseSimpleClassTypeSignature();
					  ret.append(sig.toString());
					  nestedTypeList.add(ret);
				  } while(maybeEat("."));
				  nestedTypes = new SimpleClassTypeSignature[nestedTypeList.size()];
				  nestedTypeList.toArray(nestedTypes);
			  } else if (tokenStream[tokenIndex].equals("<")) {
				  ret.append("<");
				  TypeArgument[] tArgs = maybeParseTypeArguments();
				  for (int i=0; i < tArgs.length; i++) {
					  ret.append(tArgs[i].toString());
				  }
				  ret.append(">");
				  outerType = new SimpleClassTypeSignature(identifier,tArgs);
				  // now parse possible nesteds...
				  List nestedTypeList = new ArrayList();
				  while (maybeEat(".")) {
					  ret.append(".");
					  SimpleClassTypeSignature sig = parseSimpleClassTypeSignature();
					  ret.append(sig.toString());
					  nestedTypeList.add(ret);				  
				  }
				  nestedTypes = new SimpleClassTypeSignature[nestedTypeList.size()];
				  nestedTypeList.toArray(nestedTypes);
			  } else {
				  throw new IllegalStateException("Expecting .,<, or ;, but found " + tokenStream[tokenIndex]);
			  }
		  }
		  ret.append(";");
		  if (outerType == null) outerType = new SimpleClassTypeSignature(ret.toString());
		  return new ClassTypeSignature(ret.toString(),outerType,nestedTypes);
	  }
	  
	  private SimpleClassTypeSignature parseSimpleClassTypeSignature() {
		  String identifier = eatIdentifier();
		  TypeArgument[] tArgs = maybeParseTypeArguments();
		  if (tArgs != null) {
			  return new SimpleClassTypeSignature(identifier,tArgs);
		  } else {
			  return new SimpleClassTypeSignature(identifier);
		  }
	  }
	  
	  private TypeArgument parseTypeArgument() {
		  boolean isPlus = false;
		  boolean isMinus = false;
		  if (maybeEat("*")) {
			  return new TypeArgument();
		  } else if (maybeEat("+")) {
			  isPlus = true;
		  } else if (maybeEat("-")) {
			  isMinus = true;
		  }
		  FieldTypeSignature sig = parseFieldTypeSignature(false);
		  return new TypeArgument(isPlus,isMinus,sig);
	  }
	  
	  private TypeArgument[] maybeParseTypeArguments() {
		  if (maybeEat("<")) {
			  List typeArgs = new ArrayList();
			  do {
				  TypeArgument arg = parseTypeArgument();
				  typeArgs.add(arg);
			  } while(!maybeEat(">"));
			  TypeArgument[] tArgs = new TypeArgument[typeArgs.size()];
			  typeArgs.toArray(tArgs);
			  return tArgs;
		  } else {
			  return null;
		  }
	  }
	  
	  private TypeVariableSignature parseTypeVariableSignature() {
		  TypeVariableSignature tv = new TypeVariableSignature(eatIdentifier());
		  eat(";");
		  return tv;
	  }
	  
	  private boolean maybeEat(String token) {
		  if (tokenStream.length <= tokenIndex) return false;
		  if (tokenStream[tokenIndex].equals(token)) {
			  tokenIndex++;
			  return true;
		  }
		  return false;
	  }
	  
	  private void eat(String token) {
		  if (!tokenStream[tokenIndex].equals(token)) {
			  throw new IllegalStateException("Expecting " + token + " but found " + tokenStream[tokenIndex]);
		  }
		  tokenIndex++;
	  }
	  
	  private String eatIdentifier() {
		  return tokenStream[tokenIndex++];
	  }
	  
	/**
	 * non-private for test visibility
	 * Splits a string containing a generic signature into tokens for consumption
	 * by the parser.
	 */
	public String[] tokenize(String signatureString) {
		  char[] chars = signatureString.toCharArray();
		  int index = 0;
		  List tokens = new ArrayList();
		  StringBuffer identifier = new StringBuffer();
		  boolean inParens = false;
		  boolean couldSeePrimitive = false;
		  do {
			switch (chars[index]) {
				case '<' :
					if (identifier.length() > 0) tokens.add(identifier.toString());
					identifier = new StringBuffer();
					tokens.add("<");
					break;
				case '>' :
					if (identifier.length() > 0) tokens.add(identifier.toString());
					identifier = new StringBuffer();
					tokens.add(">");
					break;
				case ':' :
					if (identifier.length() > 0) tokens.add(identifier.toString());
					identifier = new StringBuffer();
					tokens.add(":");
					break;
				case '/' :
					if (identifier.length() > 0) tokens.add(identifier.toString());
					identifier = new StringBuffer();
					tokens.add("/");
					couldSeePrimitive = false;
					break;
				case ';' :
					if (identifier.length() > 0) tokens.add(identifier.toString());
					identifier = new StringBuffer();
					tokens.add(";");
					couldSeePrimitive = true;
					break;
				case '^':
					if (identifier.length() > 0) tokens.add(identifier.toString());
					identifier = new StringBuffer();
					tokens.add("^");
					break;
				case '+':
					tokens.add("+");
					break;
				case '-':
					tokens.add("-");
					break;
				case '*':
					tokens.add("*");
					break;
				case '.' :
					if (identifier.length() > 0) tokens.add(identifier.toString());
					identifier = new StringBuffer();
					tokens.add(".");
					break;
				case '(' :
					tokens.add("(");
					inParens = true;
					couldSeePrimitive = true;
					break;
				case ')' :
					tokens.add(")");
					inParens = false;
					break;
				case '[' :
					tokens.add("[");
					break;
				case 'B' :
				case 'C' :
				case 'D' :
				case 'F' :
				case 'I' :
				case 'J' :
				case 'S' :
				case 'V' :
				case 'Z' :
					if (inParens && couldSeePrimitive && identifier.length() == 0) {
						tokens.add(new String("" + chars[index]));
					} else {
						identifier.append(chars[index]);
					}
					break;
				default : 
					identifier.append(chars[index]);
			}
		  } while((++index) < chars.length);
		  if (identifier.length() > 0) tokens.add(identifier.toString());
		  String [] tokenArray = new String[tokens.size()];
		  tokens.toArray(tokenArray);
		  return tokenArray;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/713.java