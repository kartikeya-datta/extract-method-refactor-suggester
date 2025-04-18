error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2903.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2903.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2903.java
text:
```scala
t@@his.parser = new CompletionParser(problemReporter, options.getAssertMode());

package org.eclipse.jdt.internal.codeassist;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.util.Locale;

import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.env.*;

import org.eclipse.jdt.internal.codeassist.impl.*;
import org.eclipse.jdt.internal.codeassist.complete.*;

import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.*;
import org.eclipse.jdt.internal.compiler.problem.*;
import org.eclipse.jdt.internal.compiler.util.*;
import org.eclipse.jdt.internal.compiler.impl.*;

/**
 * This class is the entry point for source completions.
 * It contains two public APIs used to call CodeAssist on a given source with
 * a given environment, assisting position and storage (and possibly options).
 */
public final class CompletionEngine extends Engine implements ISearchRequestor, TypeConstants {
	CompletionParser parser;
	ISearchableNameEnvironment nameEnvironment;
	ICompletionRequestor requestor;

	CompilationUnitScope unitScope;
	char[] source;
	boolean resolvingImports = false;
	boolean insideQualifiedReference = false;
	int startPosition, endPosition;
	HashtableOfObject knownPkgs = new HashtableOfObject(10);
/*
	static final char[][] mainDeclarations =
		new char[][] {
			"package".toCharArray(),
			"import".toCharArray(),
			"abstract".toCharArray(),
			"final".toCharArray(),
			"public".toCharArray(),
			"class".toCharArray(),
			"interface".toCharArray()};

	static final char[][] modifiers = // may want field, method, type & member type modifiers
		new char[][] {
			"abstract".toCharArray(),
			"final".toCharArray(),
			"native".toCharArray(),
			"public".toCharArray(),
			"protected".toCharArray(),
			"private".toCharArray(),
			"static".toCharArray(),
			"strictfp".toCharArray(),
			"synchronized".toCharArray(),
			"transient".toCharArray(),
			"volatile".toCharArray()};
*/
	static final char[][] baseTypes = 
		new char[][] {
			"boolean"/*nonNLS*/.toCharArray(),
			"byte"/*nonNLS*/.toCharArray(),
			"char"/*nonNLS*/.toCharArray(),
			"double"/*nonNLS*/.toCharArray(),
			"float"/*nonNLS*/.toCharArray(),
			"int"/*nonNLS*/.toCharArray(),
			"long"/*nonNLS*/.toCharArray(),
			"short"/*nonNLS*/.toCharArray(),
			"void"/*nonNLS*/.toCharArray()}; 

	static final char[] classField = "class"/*nonNLS*/.toCharArray();
	static final char[] lengthField = "length"/*nonNLS*/.toCharArray();
/**
 * The CompletionEngine is responsible for computing source completions.
 *
 * It requires a searchable name environment, which supports some
 * specific search APIs, and a requestor to feed back the results to a UI.
 *
 *  @param environment com.ibm.codeassist.java.api.ISearchableNameEnvironment
 *      used to resolve type/package references and search for types/packages
 *      based on partial names.
 *
 *  @param requestor com.ibm.codeassist.java.api.ICompletionRequestor
 *      since the engine might produce answers of various forms, the engine 
 *      is associated with a requestor able to accept all possible completions.
 *
 *  @param options com.ibm.compiler.java.api.ConfigurableOptions
 *		set of options used to configure the code assist engine.
 */

public CompletionEngine(
	ISearchableNameEnvironment nameEnvironment, ICompletionRequestor requestor, ConfigurableOption[] settings) {

	this.requestor = requestor;
	this.nameEnvironment = nameEnvironment;

	CompilerOptions options = new CompilerOptions(settings);
	ProblemReporter problemReporter =
		new ProblemReporter(
			DefaultErrorHandlingPolicies.proceedWithAllProblems(),
			options,
			new DefaultProblemFactory(Locale.getDefault())) {
				public void record(IProblem problem, CompilationResult unitResult) {
					if (problem.getID() != ProblemIrritants.UnmatchedBracket) {
						unitResult.record(problem);
						CompletionEngine.this.requestor.acceptError(problem);
					}
				}
			};

	this.parser = new CompletionParser(problemReporter);
	this.lookupEnvironment = new LookupEnvironment(this, options, problemReporter, nameEnvironment);
}
/**
 * One result of the search consists of a new class.
 *
 * NOTE - All package and type names are presented in their readable form:
 *    Package names are in the form "a.b.c".
 *    Nested type names are in the qualified form "A.M".
 *    The default package is represented by an empty array.
 */
public void acceptClass(char[] packageName, char[] className, int modifiers) {
	char[] completionName = CharOperation.concat(packageName, className, '.');
	if (resolvingImports) {
		completionName = CharOperation.concat(completionName, new char[] {';'});
	} else if (!insideQualifiedReference) {
		if (mustQualifyType(CharOperation.splitOn('.', packageName), completionName)) {
	 		if (packageName == null || packageName.length == 0)
				if (unitScope != null && unitScope.fPackage.compoundName != NoCharChar)
					return; // ignore types from the default package from outside it
		} else {
			completionName = className;
		}
	}

	requestor.acceptClass(
		packageName,
		className,
		completionName,
		modifiers,
		startPosition,
		endPosition);
}
/**
 * One result of the search consists of a new interface.
 *
 * NOTE - All package and type names are presented in their readable form:
 *    Package names are in the form "a.b.c".
 *    Nested type names are in the qualified form "A.I".
 *    The default package is represented by an empty array.
 */
public void acceptInterface(char[] packageName, char[] interfaceName, int modifiers) {
	char[] completionName = CharOperation.concat(packageName, interfaceName, '.');
	if (resolvingImports) {
		completionName = CharOperation.concat(completionName, new char[] {';'});
	} else if (!insideQualifiedReference) {
		if (mustQualifyType(CharOperation.splitOn('.', packageName), completionName)) {
	 		if (packageName == null || packageName.length == 0)
				if (unitScope != null && unitScope.fPackage.compoundName != NoCharChar)
					return; // ignore types from the default package from outside it
		} else {
			completionName = interfaceName;
		}
	}

	requestor.acceptInterface(
		packageName,
		interfaceName,
		completionName,
		modifiers,
		startPosition,
		endPosition);
}
/**
 * One result of the search consists of a new package.
 *
 * NOTE - All package names are presented in their readable form:
 *    Package names are in the form "a.b.c".
 *    The default package is represented by an empty array.
 */
public void acceptPackage(char[] packageName) {
	if (this.knownPkgs.containsKey(packageName)) return;
	this.knownPkgs.put(packageName, this);
	requestor.acceptPackage(
		packageName,
		resolvingImports ? CharOperation.concat(packageName, new char[] {'.', '*', ';'}) : packageName,
		startPosition,
		endPosition);
}
/**
 * One result of the search consists of a new type.
 *
 * NOTE - All package and type names are presented in their readable form:
 *    Package names are in the form "a.b.c".
 *    Nested type names are in the qualified form "A.M".
 *    The default package is represented by an empty array.
 */
public void acceptType(char[] packageName, char[] typeName) {
	char[] completionName = CharOperation.concat(packageName, typeName, '.');
	if (resolvingImports) {
		completionName = CharOperation.concat(completionName, new char[] {';'});
	} else if (!insideQualifiedReference) {
		if (mustQualifyType(CharOperation.splitOn('.', packageName), completionName)) {
	 		if (packageName == null || packageName.length == 0)
				if (unitScope != null && unitScope.fPackage.compoundName != NoCharChar)
					return; // ignore types from the default package from outside it
		} else {
			completionName = typeName;
		}
	}

	requestor.acceptType(
		packageName,
		typeName,
		completionName,
		startPosition,
		endPosition);
}
private void complete(AstNode astNode, Binding qualifiedBinding, Scope scope) {
	setSourceRange(astNode.sourceStart, astNode.sourceEnd); // defaults... some nodes will change these

	if (astNode instanceof CompletionOnFieldType) {
		CompletionOnSingleTypeReference type =
			(CompletionOnSingleTypeReference) ((CompletionOnFieldType) astNode).type;
		char[] token = type.token;
		setSourceRange(type.sourceStart, type.sourceEnd);
//		findKeywords(token, modifiers, scope); // could be the start of a field, method or member type
		findTypesAndPackages(token, scope);
	} else if (astNode instanceof CompletionOnSingleNameReference) {
		char[] token = ((CompletionOnSingleNameReference) astNode).token;
		findVariablesAndMethods(token, scope);
		findTypesAndPackages(token, scope); // can be the start of a qualified type name
	} else if (astNode instanceof CompletionOnSingleTypeReference) {
		char[] token = ((CompletionOnSingleTypeReference) astNode).token;
		if (qualifiedBinding == null)
			findTypesAndPackages(token, scope); // can be the start of a qualified type name
		else
			findMemberTypes(token, (ReferenceBinding) qualifiedBinding, scope);
	} else if (astNode instanceof CompletionOnQualifiedNameReference) {
		insideQualifiedReference = true;
		CompletionOnQualifiedNameReference ref = (CompletionOnQualifiedNameReference) astNode;
		char[] token = ref.completionIdentifier;
		long completionPosition = ref.sourcePositions[ref.sourcePositions.length - 1];
		if (qualifiedBinding instanceof VariableBinding) {
			setSourceRange((int) (completionPosition >>> 32), (int) completionPosition);
			TypeBinding receiverType = ((VariableBinding) qualifiedBinding).type;
			if (receiverType != null)
				findFieldsAndMethods(token, receiverType, scope);
		} else if (qualifiedBinding instanceof ReferenceBinding) {
			ReferenceBinding receiverType = (ReferenceBinding) qualifiedBinding;
			setSourceRange((int) (completionPosition >>> 32), (int) completionPosition);
			findMemberTypes(token, receiverType, scope);
			findClassField(token, (TypeBinding) qualifiedBinding);
			findFields(token, receiverType, scope, new ObjectVector(), true);
			findMethods(token, null, receiverType, scope, new ObjectVector(), true, false);
		} else if (qualifiedBinding instanceof PackageBinding) {
			setSourceRange(astNode.sourceStart, (int) completionPosition); // replace to the end of the completion identifier
			findTypesAndSubpackages(token, (PackageBinding) qualifiedBinding);
		}
	} else if (astNode instanceof CompletionOnQualifiedTypeReference) {
		insideQualifiedReference = true;
		CompletionOnQualifiedTypeReference ref = (CompletionOnQualifiedTypeReference) astNode;
		char[] token = ref.completionIdentifier;
		long completionPosition = ref.sourcePositions[ref.tokens.length]; // get the source positions of the completion identifier
		if (qualifiedBinding instanceof ReferenceBinding) {
			setSourceRange((int) (completionPosition >>> 32), (int) completionPosition);
			findMemberTypes(token, (ReferenceBinding) qualifiedBinding, scope);
		} else if (qualifiedBinding instanceof PackageBinding) {
			setSourceRange(astNode.sourceStart, (int) completionPosition); // replace to the end of the completion identifier
			findTypesAndSubpackages(token, (PackageBinding) qualifiedBinding);
		}
	} else if (astNode instanceof CompletionOnMemberAccess) {
		CompletionOnMemberAccess access = (CompletionOnMemberAccess) astNode;
		long completionPosition = access.nameSourcePosition;
		setSourceRange((int) (completionPosition >>> 32), (int) completionPosition);
		findFieldsAndMethods(access.token, (TypeBinding) qualifiedBinding, scope);
	} else if (astNode instanceof CompletionOnMessageSend) {
		CompletionOnMessageSend messageSend = (CompletionOnMessageSend) astNode;
		TypeBinding[] argTypes = computeTypes(messageSend.arguments, (BlockScope) scope);
		if (qualifiedBinding == null)
			findMessageSends(messageSend.selector, argTypes, scope);
		else
			findMethods(messageSend.selector, argTypes, (ReferenceBinding) qualifiedBinding, scope, new ObjectVector(), false, true);
	} else if (astNode instanceof CompletionOnExplicitConstructorCall) {
		CompletionOnExplicitConstructorCall constructorCall = (CompletionOnExplicitConstructorCall) astNode;
		TypeBinding[] argTypes = computeTypes(constructorCall.arguments, (BlockScope) scope);
		findConstructors((ReferenceBinding) qualifiedBinding, argTypes, scope);
	} else if (astNode instanceof CompletionOnQualifiedAllocationExpression) {
		CompletionOnQualifiedAllocationExpression allocExpression = (CompletionOnQualifiedAllocationExpression) astNode;
		TypeBinding[] argTypes = computeTypes(allocExpression.arguments, (BlockScope) scope);
		findConstructors((ReferenceBinding) qualifiedBinding, argTypes, scope);
	} else if (astNode instanceof CompletionOnClassLiteralAccess) {
		char[] token = ((CompletionOnClassLiteralAccess) astNode).completionIdentifier;
		findClassField(token, (TypeBinding) qualifiedBinding);
	}
}
/**
 * Ask the engine to compute a completion at the specified position
 * of the given compilation unit.
 *
 *  @return void
 *      completion results are answered through a requestor.
 *
 *  @param unit com.ibm.compiler.java.api.env.ICompilationUnit
 *      the source of the current compilation unit.
 *
 *  @param completionPosition int
 *      a position in the source where the completion is taking place. 
 *      This position is relative to the source provided.
 */
public void complete(ICompilationUnit sourceUnit, int completionPosition) {
	try {
		int actualCompletionPosition = completionPosition - 1; // for now until we can change the UI.
		CompilationResult result = new CompilationResult(sourceUnit, 1, 1);
		CompilationUnitDeclaration parsedUnit = parser.dietParse(sourceUnit, result, actualCompletionPosition);

//		boolean completionNodeFound = false;
		if (parsedUnit != null) {
			// scan the package & import statements first
			if (parsedUnit.currentPackage instanceof CompletionOnPackageReference) {
				findPackages((CompletionOnPackageReference) parsedUnit.currentPackage);
				return;
			}
			ImportReference[] imports = parsedUnit.imports;
			if (imports != null) {
				for (int i = 0, length = imports.length; i < length; i++) {
					ImportReference importReference = imports[i];
					if (importReference instanceof CompletionOnImportReference) {
						findImports((CompletionOnImportReference) importReference);
						return;
					}
				}
			}

			if (parsedUnit.types != null) {
				try {
					lookupEnvironment.buildTypeBindings(parsedUnit);
					if ((unitScope = parsedUnit.scope) != null) {
							source = sourceUnit.getContents();
							lookupEnvironment.completeTypeBindings(parsedUnit, true);
							parsedUnit.scope.faultInTypes();
							parseMethod(parsedUnit, actualCompletionPosition);
							parsedUnit.resolve();
					}
				} catch (CompletionNodeFound e) {
//					completionNodeFound = true;
					if (e.astNode != null) // if null then we found a problem in the completion node
						complete(e.astNode, e.qualifiedBinding, e.scope);
				}
			}
		}

/* Ignore package, import, class & interface keywords for now...
		if (!completionNodeFound) {
			if (parsedUnit == null || parsedUnit.types == null) {
				// this is not good enough... can still be trying to define a second type
				CompletionScanner scanner = (CompletionScanner) parser.scanner;
				setSourceRange(scanner.completedIdentifierStart, scanner.completedIdentifierEnd);
				findKeywords(scanner.completionIdentifier, mainDeclarations, null);
			}
			// currently have no way to know if extends/implements are possible keywords
		}
*/	} catch (IndexOutOfBoundsException e) { // work-around internal failure - 1GEMF6D
	} catch (InvalidCursorLocation e) { // may eventually report a usefull error
	} catch (AbortCompilation e) { // ignore this exception for now since it typically means we cannot find java.lang.Object
	} finally {
		reset();
	}
}
private TypeBinding[] computeTypes(Expression[] arguments, BlockScope scope) {
	if (arguments == null) return null;

	int argsLength = arguments.length;
	TypeBinding[] argTypes = new TypeBinding[argsLength];
	for (int a = argsLength; --a >= 0;)
		argTypes[a] = arguments[a].resolveType(scope);
	return argTypes;
}
private void findClassField(char[] token, TypeBinding receiverType) {
	if (token == null) return;

	if (token.length <= classField.length && CharOperation.prefixEquals(token, classField, false /* ignore case */))
		requestor.acceptField(
			NoChar,
			NoChar,
			classField,
			NoChar,
			NoChar,
			classField,
			CompilerModifiers.AccStatic | CompilerModifiers.AccPublic,
			startPosition,
			endPosition);
}
private void findConstructors(ReferenceBinding currentType, TypeBinding[] argTypes, Scope scope) {
	// No visibility checks can be performed without the scope & invocationSite
	MethodBinding[] methods = currentType.methods();
	int minArgLength = argTypes == null ? 0 : argTypes.length;
	next : for (int f = methods.length; --f >= 0;) {
		MethodBinding constructor = methods[f];
		if (constructor.isConstructor()) {
			TypeBinding[] parameters = constructor.parameters;
			int paramLength = parameters.length;
			if (minArgLength > paramLength) continue next;
			for (int a = minArgLength; --a >= 0;)
				if (argTypes[a] != null) // can be null if it could not be resolved properly
					if (!scope.areTypesCompatible(argTypes[a], constructor.parameters[a])) continue next;

			char[][] parameterPackageNames = new char[paramLength][];
			char[][] parameterTypeNames = new char[paramLength][];
			for (int i = 0; i < paramLength; i++) {
				TypeBinding type = parameters[i];
				parameterPackageNames[i] = type.qualifiedPackageName();
				parameterTypeNames[i] = type.qualifiedSourceName();
			}
			char[] completion = TypeConstants.NoChar; // nothing to insert - do not want to replace the existing selector & arguments
			if (source == null || source.length <= endPosition || source[endPosition] != ')')
				completion = new char[] {')'};
			requestor.acceptMethod(
				currentType.qualifiedPackageName(),
				currentType.qualifiedSourceName(),
				currentType.sourceName(),
				parameterPackageNames,
				parameterTypeNames,
				TypeConstants.NoChar,
				TypeConstants.NoChar,
				completion,
				constructor.modifiers,
				endPosition,
				endPosition);
		}
	}
}
// Helper method for findFields(char[], ReferenceBinding, Scope, ObjectVector, boolean)

private void findFields(
	char[] fieldName,
	FieldBinding[] fields,
	Scope scope,
	ObjectVector fieldsFound,
	boolean onlyStaticFields) {

	// Inherited fields which are hidden by subclasses are filtered out
	// No visibility checks can be performed without the scope & invocationSite

	int fieldLength = fieldName.length;
	next : for (int f = fields.length; --f >= 0;) {
		FieldBinding field = fields[f];
		if (onlyStaticFields && !field.isStatic()) continue next;
		if (fieldLength > field.name.length) continue next;
		if (!CharOperation.prefixEquals(fieldName, field.name, false /* ignore case */)) continue next;

		for (int i = fieldsFound.size; --i >= 0;) {
			FieldBinding otherField = (FieldBinding) fieldsFound.elementAt(i);
			if (field == otherField) continue next;
			if (CharOperation.equals(field.name, otherField.name, true)) {
				if (field.declaringClass.isSuperclassOf(otherField.declaringClass)) continue next;
				if (otherField.declaringClass.isInterface())
					if (field.declaringClass.implementsInterface(otherField.declaringClass, true)) continue next;
			}
		}

		fieldsFound.add(field);
		requestor.acceptField(
			field.declaringClass.qualifiedPackageName(),
			field.declaringClass.qualifiedSourceName(),
			field.name,
			field.type.qualifiedPackageName(),
			field.type.qualifiedSourceName(),
			field.name, // may include some qualification to resolve ambiguities
			field.modifiers,
			startPosition,
			endPosition);
	}
}
private void findFields(
	char[] fieldName,
	ReferenceBinding receiverType,
	Scope scope,
	ObjectVector fieldsFound,
	boolean onlyStaticFields) {

	if (fieldName == null) return;

	ReferenceBinding currentType = receiverType;
	ReferenceBinding[][] interfacesToVisit = null;
	int lastPosition = -1;
	do {
		ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
		if (itsInterfaces != NoSuperInterfaces) {
			if (interfacesToVisit == null)
				interfacesToVisit = new ReferenceBinding[5][];
			if (++lastPosition == interfacesToVisit.length)
				System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
			interfacesToVisit[lastPosition] = itsInterfaces;
		}

		findFields(fieldName, currentType.fields(), scope, fieldsFound, onlyStaticFields);
		currentType = currentType.superclass();
	} while (currentType != null);

	if (interfacesToVisit != null) {
		for (int i = 0; i <= lastPosition; i++) {
			ReferenceBinding[] interfaces = interfacesToVisit[i];
			for (int j = 0, length = interfaces.length; j < length; j++) {
				ReferenceBinding anInterface = interfaces[j];
				if ((anInterface.tagBits & TagBits.InterfaceVisited) == 0) { // if interface as not already been visited
					anInterface.tagBits |= TagBits.InterfaceVisited;

					findFields(fieldName, anInterface.fields(), scope, fieldsFound, onlyStaticFields);

					ReferenceBinding[] itsInterfaces = anInterface.superInterfaces();
					if (itsInterfaces != NoSuperInterfaces) {
						if (++lastPosition == interfacesToVisit.length)
							System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
						interfacesToVisit[lastPosition] = itsInterfaces;
					}
				}
			}
		}

		// bit reinitialization
		for (int i = 0; i <= lastPosition; i++) {
			ReferenceBinding[] interfaces = interfacesToVisit[i];
			for (int j = 0, length = interfaces.length; j < length; j++)
				interfaces[j].tagBits &= ~TagBits.InterfaceVisited;
		}
	}
}
private void findFieldsAndMethods(char[] token, TypeBinding receiverType, Scope scope) {
	if (token == null) return;

	if (receiverType.isBaseType()) return; // nothing else is possible with base types
	if (receiverType.isArrayType()) {
		if (token.length <= lengthField.length && CharOperation.prefixEquals(token, lengthField, false /* ignore case */))
			requestor.acceptField(
				NoChar,
				NoChar,
				lengthField,
				NoChar,
				NoChar,
				lengthField,
				CompilerModifiers.AccPublic,
				startPosition,
				endPosition);

		receiverType = scope.getJavaLangObject();
	}

	findFields(token, (ReferenceBinding) receiverType, scope, new ObjectVector(), false);
	findMethods(token, null, (ReferenceBinding) receiverType, scope, new ObjectVector(), false, false);
}
private void findImports(CompletionOnImportReference importReference) {
	char[] importName = CharOperation.concatWith(importReference.tokens, '.');
	if (importName.length == 0) return;
	resolvingImports = true;
	setSourceRange(importReference.sourceStart, importReference.declarationSourceEnd); // want to replace the existing .*;
	nameEnvironment.findPackages(importName, this);
	nameEnvironment.findTypes(importName, this);
}
// what about onDemand types? Ignore them since it does not happen!
// import p1.p2.A.*;
private void findKeywords(char[] keyword, char[][] choices, Scope scope) {
	int length = keyword.length;
	if (length > 0)
		for (int i = 0; i < choices.length; i++)
			if (length <= choices[i].length && CharOperation.prefixEquals(keyword, choices[i], false /* ignore case */))
				requestor.acceptKeyword(choices[i], startPosition, endPosition);
}
// Helper method for findMemberTypes(char[], ReferenceBinding, Scope)

private void findMemberTypes(char[] typeName, ReferenceBinding[] memberTypes, ObjectVector typesFound) {

	// Inherited member types which are hidden by subclasses are filtered out
	// No visibility checks can be performed without the scope & invocationSite

	int typeLength = typeName.length;
	next : for (int m = memberTypes.length; --m >= 0;) {
		ReferenceBinding memberType = memberTypes[m];
//		if (!wantClasses && memberType.isClass()) continue next;
//		if (!wantInterfaces && memberType.isInterface()) continue next;
		if (typeLength > memberType.sourceName.length) continue next;
		if (!CharOperation.prefixEquals(typeName, memberType.sourceName, false /* ignore case */)) continue next;

		for (int i = typesFound.size; --i >= 0;) {
			ReferenceBinding otherType = (ReferenceBinding) typesFound.elementAt(i);
			if (memberType == otherType) continue next;
			if (CharOperation.equals(memberType.sourceName, otherType.sourceName, true)) {
				if (memberType.enclosingType().isSuperclassOf(otherType.enclosingType())) continue next;
				if (otherType.enclosingType().isInterface())
					if (memberType.enclosingType().implementsInterface(otherType.enclosingType(), true)) continue next;
			}
		}

		typesFound.add(memberType);
		if (memberType.isClass())
			requestor.acceptClass(
				memberType.qualifiedPackageName(),
				memberType.qualifiedSourceName(),
				memberType.sourceName(),
				memberType.modifiers,
				startPosition,
				endPosition);
		else
			requestor.acceptInterface(
				memberType.qualifiedPackageName(),
				memberType.qualifiedSourceName(),
				memberType.sourceName(),
				memberType.modifiers,
				startPosition,
				endPosition);
	}
}
private void findMemberTypes(char[] typeName, ReferenceBinding currentType, Scope scope) {
	if (typeName == null) return;
	if (currentType.superInterfaces() == null) return; // we're trying to find a supertype

	ObjectVector typesFound = new ObjectVector();
	if (insideQualifiedReference || typeName.length == 0) { // do not search up the hierarchy
		findMemberTypes(typeName, currentType.memberTypes(), typesFound);
		return;
	}

	ReferenceBinding[][] interfacesToVisit = null;
	int lastPosition = -1;
	do {
		ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
		if (itsInterfaces != NoSuperInterfaces) {
			if (interfacesToVisit == null)
				interfacesToVisit = new ReferenceBinding[5][];
			if (++lastPosition == interfacesToVisit.length)
				System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
			interfacesToVisit[lastPosition] = itsInterfaces;
		}

		findMemberTypes(typeName, currentType.memberTypes(), typesFound);
		currentType = currentType.superclass();
	} while (currentType != null);

	if (interfacesToVisit != null) {
		for (int i = 0; i <= lastPosition; i++) {
			ReferenceBinding[] interfaces = interfacesToVisit[i];
			for (int j = 0, length = interfaces.length; j < length; j++) {
				ReferenceBinding anInterface = interfaces[j];
				if ((anInterface.tagBits & TagBits.InterfaceVisited) == 0) { // if interface as not already been visited
					anInterface.tagBits |= TagBits.InterfaceVisited;

					findMemberTypes(typeName, anInterface.memberTypes(), typesFound);

					ReferenceBinding[] itsInterfaces = anInterface.superInterfaces();
					if (itsInterfaces != NoSuperInterfaces) {
						if (++lastPosition == interfacesToVisit.length)
							System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
						interfacesToVisit[lastPosition] = itsInterfaces;
					}
				}
			}
		}

		// bit reinitialization
		for (int i = 0; i <= lastPosition; i++) {
			ReferenceBinding[] interfaces = interfacesToVisit[i];
			for (int j = 0, length = interfaces.length; j < length; j++)
				interfaces[j].tagBits &= ~TagBits.InterfaceVisited;
		}
	}
}
private void findMessageSends(char[] token, TypeBinding[] argTypes, Scope scope) {
	if (token == null) return;

	boolean staticsOnly = false; // need to know if we're in a static context (or inside a constructor)
	int tokenLength = token.length;
	ObjectVector methodsFound = new ObjectVector();
	done : while (true) { // done when a COMPILATION_UNIT_SCOPE is found
		switch (scope.kind) {
			case Scope.METHOD_SCOPE :
				// handle the error case inside an explicit constructor call (see MethodScope>>findField)
				MethodScope methodScope = (MethodScope) scope;
				staticsOnly |= methodScope.isStatic | methodScope.isConstructorCall;
				break;
			case Scope.CLASS_SCOPE :
				ClassScope classScope = (ClassScope) scope;
				SourceTypeBinding enclosingType = classScope.referenceContext.binding;
				findMethods(token, argTypes, enclosingType, classScope, methodsFound, staticsOnly, true);
				staticsOnly |= enclosingType.isStatic();
				break;
			case Scope.COMPILATION_UNIT_SCOPE :
				break done;
		}
		scope = scope.parent;
	}
}
// Helper method for findMethods(char[], TypeBinding[], ReferenceBinding, Scope, ObjectVector, boolean, boolean)

private void findMethods(
	char[] methodName,
	TypeBinding[] argTypes,
	MethodBinding[] methods,
	Scope scope,
	ObjectVector methodsFound,
//	boolean noVoidReturnType, how do you know?
	boolean onlyStaticMethods,
	boolean exactMatch) {

	// Inherited methods which are hidden by subclasses are filtered out
	// No visibility checks can be performed without the scope & invocationSite

	int methodLength = methodName.length;
	int minArgLength = argTypes == null ? 0 : argTypes.length;
	next : for (int f = methods.length; --f >= 0;) {
		MethodBinding method = methods[f];
		if (method.isConstructor()) continue next;
//		if (noVoidReturnType && method.returnType == BaseTypes.VoidBinding) continue next;
		if (onlyStaticMethods && !method.isStatic()) continue next;
		if (exactMatch) {
			if (!CharOperation.equals(methodName, method.selector, false /* ignore case */)) continue next;
		} else {
			if (methodLength > method.selector.length) continue next;
			if (!CharOperation.prefixEquals(methodName, method.selector, false /* ignore case */)) continue next;
		}
		if (minArgLength > method.parameters.length) continue next;
		for (int a = minArgLength; --a >= 0;)
			if (argTypes[a] != null) // can be null if it could not be resolved properly
				if (!scope.areTypesCompatible(argTypes[a], method.parameters[a])) continue next;

		for (int i = methodsFound.size; --i >= 0;) {
			MethodBinding otherMethod = (MethodBinding) methodsFound.elementAt(i);
			if (method == otherMethod) continue next;
			if (CharOperation.equals(method.selector, otherMethod.selector, true) && method.areParametersEqual(otherMethod)) {
				if (method.declaringClass.isSuperclassOf(otherMethod.declaringClass)) continue next;
				if (otherMethod.declaringClass.isInterface())
					if (method.declaringClass.implementsInterface(otherMethod.declaringClass, true)) continue next;
			}
		}

		methodsFound.add(method);
		int length = method.parameters.length;
		char[][] parameterPackageNames = new char[length][];
		char[][] parameterTypeNames = new char[length][];
		for (int i = 0; i < length; i++) {
			TypeBinding type = method.parameters[i];
			parameterPackageNames[i] = type.qualifiedPackageName();
			parameterTypeNames[i] = type.qualifiedSourceName();
		}
		char[] completion = TypeConstants.NoChar; // nothing to insert - do not want to replace the existing selector & arguments
		if (!exactMatch) {
			if (source != null && source.length > endPosition && source[endPosition] == '(')
				completion = method.selector;
			else
				completion = CharOperation.concat(method.selector, new char[] {'(', ')'});
		}
		requestor.acceptMethod(
			method.declaringClass.qualifiedPackageName(),
			method.declaringClass.qualifiedSourceName(),
			method.selector,
			parameterPackageNames,
			parameterTypeNames,
			method.returnType.qualifiedPackageName(),
			method.returnType.qualifiedSourceName(),
			completion,
			method.modifiers,
			startPosition,
			endPosition);
	}
}
private void findMethods(
	char[] selector,
	TypeBinding[] argTypes,
	ReferenceBinding receiverType,
	Scope scope,
	ObjectVector methodsFound,
	boolean onlyStaticMethods,
	boolean exactMatch) {

	if (selector == null) return;

	ReferenceBinding currentType = receiverType;
	if (currentType.isInterface()) {
		findMethods(selector, argTypes, currentType.methods(), scope, methodsFound, onlyStaticMethods, exactMatch);

		ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
		if (itsInterfaces != NoSuperInterfaces) {
			ReferenceBinding[][] interfacesToVisit = new ReferenceBinding[5][];
			int lastPosition = 0;
			interfacesToVisit[lastPosition] = itsInterfaces;
			
			for (int i = 0; i <= lastPosition; i++) {
				ReferenceBinding[] interfaces = interfacesToVisit[i];
				for (int j = 0, length = interfaces.length; j < length; j++) {
					currentType = interfaces[j];
					if ((currentType.tagBits & TagBits.InterfaceVisited) == 0) { // if interface as not already been visited
						currentType.tagBits |= TagBits.InterfaceVisited;

						findMethods(selector, argTypes, currentType.methods(), scope, methodsFound, onlyStaticMethods, exactMatch);

						itsInterfaces = currentType.superInterfaces();
						if (itsInterfaces != NoSuperInterfaces) {
							if (++lastPosition == interfacesToVisit.length)
								System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[lastPosition * 2][], 0, lastPosition);
							interfacesToVisit[lastPosition] = itsInterfaces;
						}
					}
				}
			}

			// bit reinitialization
			for (int i = 0; i <= lastPosition; i++) {
				ReferenceBinding[] interfaces = interfacesToVisit[i];
				for (int j = 0, length = interfaces.length; j < length; j++)
					interfaces[j].tagBits &= ~TagBits.InterfaceVisited;
			}
		}
		currentType = scope.getJavaLangObject();
	}

	while (currentType != null) {
		findMethods(selector, argTypes, currentType.methods(), scope, methodsFound, onlyStaticMethods, exactMatch);
		currentType = currentType.superclass();
	}
}
private void findNestedTypes(char[] typeName, ReferenceBinding currentType, Scope scope) {
	if (typeName == null) return;

	int typeLength = typeName.length;
	while (scope != null) { // done when a COMPILATION_UNIT_SCOPE is found
		switch (scope.kind) {
			case Scope.METHOD_SCOPE :
			case Scope.BLOCK_SCOPE :
				BlockScope blockScope = (BlockScope) scope;
				next : for (int i = 0, length = blockScope.scopeIndex; i < length; i++) {
					if (blockScope.subscopes[i] instanceof ClassScope) {
						SourceTypeBinding localType = ((ClassScope) blockScope.subscopes[i]).referenceContext.binding;
						if (!localType.isAnonymousType()) {
							if (typeLength > localType.sourceName.length) continue next;
							if (!CharOperation.prefixEquals(typeName, localType.sourceName, false /* ignore case */)) continue next;

							requestor.acceptClass(
								localType.qualifiedPackageName(),
								localType.sourceName,
								localType.sourceName,
								localType.modifiers,
								startPosition,
								endPosition);
						}
					}
				}
				break;
			case Scope.CLASS_SCOPE :
				findMemberTypes(typeName, scope.enclosingSourceType(), scope);
				if (typeLength == 0) return; // do not search outside the class scope if no prefix was provided
				break;
			case Scope.COMPILATION_UNIT_SCOPE :
				return;
		}
		scope = scope.parent;
	}
}
private void findPackages(CompletionOnPackageReference packageStatement) {
	char[] packageName = CharOperation.concatWith(packageStatement.tokens, '.');
	if (packageName.length == 0) return;

	setSourceRange(packageStatement.sourceStart, packageStatement.sourceEnd);
	nameEnvironment.findPackages(CharOperation.toLowerCase(packageName), this);
}
private void findTypesAndPackages(char[] token, Scope scope) {
	if (token == null) return;

	if (scope.enclosingSourceType() != null)
		findNestedTypes(token, scope.enclosingSourceType(), scope);

	if (unitScope != null) {
		int typeLength = token.length;
		SourceTypeBinding[] types = unitScope.topLevelTypes;
		for (int i = 0, length = types.length; i < length; i++) {
			SourceTypeBinding sourceType = types[i];
			if (typeLength > sourceType.sourceName.length) continue;
			if (!CharOperation.prefixEquals(token, sourceType.sourceName, false /* ignore case */)) continue;

			requestor.acceptType(
				sourceType.qualifiedPackageName(),
				sourceType.sourceName(),
				sourceType.sourceName(),
				startPosition,
				endPosition);
		}
	}

	if (token.length == 0) return;
	findKeywords(token, baseTypes, scope);
	nameEnvironment.findTypes(token, this);
	nameEnvironment.findPackages(token, this);
}
private void findTypesAndSubpackages(char[] token, PackageBinding packageBinding) {
	char[] qualifiedName = CharOperation.concatWith(packageBinding.compoundName, token, '.');
	if (token == null || token.length == 0) {
		int length = qualifiedName.length;
		System.arraycopy(qualifiedName, 0, qualifiedName = new char[length + 1], 0, length);
		qualifiedName[length] = '.';
	}
	nameEnvironment.findTypes(qualifiedName, this);
	nameEnvironment.findPackages(qualifiedName, this);
}
private void findVariablesAndMethods(char[] token, Scope scope) {
	if (token == null) return;

	// Should local variables hide fields from the receiver type or any of its enclosing types?
	// we know its an implicit field/method access... see BlockScope getBinding/getImplicitMethod

	boolean staticsOnly = false; // need to know if we're in a static context (or inside a constructor)
	char[][] found = null;
	int lastPosition = -1;
	int tokenLength = token.length;
	ObjectVector fieldsFound = new ObjectVector();
	ObjectVector methodsFound = new ObjectVector();
	done : while (true) { // done when a COMPILATION_UNIT_SCOPE is found
		switch (scope.kind) {
			case Scope.METHOD_SCOPE :
				// handle the error case inside an explicit constructor call (see MethodScope>>findField)
				MethodScope methodScope = (MethodScope) scope;
				staticsOnly |= methodScope.isStatic | methodScope.isConstructorCall;
			case Scope.BLOCK_SCOPE :
				BlockScope blockScope = (BlockScope) scope;
				next : for (int i = 0, length = blockScope.locals.length; i < length; i++) {
					LocalVariableBinding local = blockScope.locals[i];
					if (local == null) break next;
					if (tokenLength > local.name.length) continue next;
					if (!CharOperation.prefixEquals(token, local.name, false /* ignore case */)) continue next;
					if (local.isSecret()) continue next;

					if (found == null) {
						found = new char[5][];
					} else {
						for (int f = 0; f < found.length; f++) {
							char[] name = found[f];
							if (name == null) break;
							if (CharOperation.equals(name, local.name, false /* ignore case */)) continue next;
						}
					}
					if (++lastPosition == found.length)
						System.arraycopy(found, 0, found = new char[lastPosition * 2][], 0, lastPosition);
					found[lastPosition] = local.name;

					requestor.acceptLocalVariable(
						local.name,
						NoChar,
						local.type == null ? local.declaration.type.toString().toCharArray() : local.type.qualifiedSourceName(),
						local.modifiers,
						startPosition,
						endPosition);
				}
				break;
			case Scope.CLASS_SCOPE :
				ClassScope classScope = (ClassScope) scope;
				SourceTypeBinding enclosingType = classScope.referenceContext.binding;
/*				if (tokenLength == 0) { // only search inside the type itself if no prefix was provided
					findFields(token, enclosingType.fields(), classScope, fieldsFound, staticsOnly);
					findMethods(token, enclosingType.methods(), classScope, methodsFound, staticsOnly, false);
					break done;
				} else { */
					findFields(token, enclosingType, classScope, fieldsFound, staticsOnly);
					findMethods(token, null, enclosingType, classScope, methodsFound, staticsOnly, false);
					staticsOnly |= enclosingType.isStatic();
//				}
				break;
			case Scope.COMPILATION_UNIT_SCOPE :
				break done;
		}
		scope = scope.parent;
	}
}
public AssistParser getParser(){
	return parser;
}
private boolean mustQualifyType(char[][] packageName, char[] readableTypeName) {
	// If there are no types defined into the current CU yet.
	if (unitScope == null)
		return true;
	if (CharOperation.equals(unitScope.fPackage.compoundName, packageName))
		return false;

	ImportBinding[] imports = unitScope.imports;
	for (int i = 0, length = imports.length; i < length; i++) {
		if (imports[i].onDemand) {
			if (CharOperation.equals(imports[i].compoundName, packageName))
				return false; // how do you match p1.p2.A.* ?
		} else if (CharOperation.equals(imports[i].readableName(), readableTypeName)) {
			return false;
		}
	}
	return true;
}
protected void reset() {
	super.reset();
	this.knownPkgs = new HashtableOfObject(10);
}
private void setSourceRange(int start, int end) {
	this.startPosition = start;
	this.endPosition = end + 1; // Add 1 for now
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2903.java