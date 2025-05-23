error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9020.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9020.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9020.java
text:
```scala
s@@witch(binding.kind()) {

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.codeassist;

import java.util.*;

import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.internal.codeassist.impl.*;
import org.eclipse.jdt.internal.codeassist.select.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.*;
import org.eclipse.jdt.internal.compiler.parser.Scanner;
import org.eclipse.jdt.internal.compiler.problem.*;
import org.eclipse.jdt.internal.core.SearchableEnvironment;
import org.eclipse.jdt.internal.core.SelectionRequestor;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.core.SourceTypeElementInfo;
import org.eclipse.jdt.internal.core.util.ASTNodeFinder;

/**
 * The selection engine is intended to infer the nature of a selected name in some
 * source code. This name can be qualified.
 *
 * Selection is resolving context using a name environment (no need to search), assuming
 * the source where selection occurred is correct and will not perform any completion
 * attempt. If this was the desired behavior, a call to the CompletionEngine should be
 * performed instead.
 */
public final class SelectionEngine extends Engine implements ISearchRequestor {

	public static boolean DEBUG = false;
	
	SelectionParser parser;
	ISelectionRequestor requestor;

	boolean acceptedAnswer;

	private int actualSelectionStart;
	private int actualSelectionEnd;
	private char[] selectedIdentifier;
	
	private char[][][] acceptedClasses;
	private char[][][] acceptedInterfaces;
	int acceptedClassesCount;
	int acceptedInterfacesCount;
	
	boolean noProposal = true;
	IProblem problem = null;
	
	private class CheckState {
		public CheckState(int depth) {
			this.depth = depth;
		}
		public int depth = 0;
	}

	/**
	 * The SelectionEngine is responsible for computing the selected object.
	 *
	 * It requires a searchable name environment, which supports some
	 * specific search APIs, and a requestor to feed back the results to a UI.
	 *
	 *  @param nameEnvironment org.eclipse.jdt.internal.core.SearchableEnvironment
	 *      used to resolve type/package references and search for types/packages
	 *      based on partial names.
	 *
	 *  @param requestor org.eclipse.jdt.internal.codeassist.ISelectionRequestor
	 *      since the engine might produce answers of various forms, the engine 
	 *      is associated with a requestor able to accept all possible completions.
	 *
	 *  @param settings java.util.Map
	 *		set of options used to configure the code assist engine.
	 */
	public SelectionEngine(
		SearchableEnvironment nameEnvironment,
		ISelectionRequestor requestor,
		Map settings) {

		super(settings);

		this.requestor = requestor;
		this.nameEnvironment = nameEnvironment;

		ProblemReporter problemReporter =
			new ProblemReporter(
				DefaultErrorHandlingPolicies.proceedWithAllProblems(),
				this.compilerOptions,
				new DefaultProblemFactory(Locale.getDefault())) {
					
			public IProblem createProblem(
				char[] fileName,
				int problemId,
				String[] problemArguments,
				String[] messageArguments,
				int severity,
				int problemStartPosition,
				int problemEndPosition,
				int lineNumber) {
				IProblem pb =  super.createProblem(
					fileName,
					problemId,
					problemArguments,
					messageArguments,
					severity,
					problemStartPosition,
					problemEndPosition,
					lineNumber);
					if(SelectionEngine.this.problem == null && pb.isError() && (pb.getID() & IProblem.Syntax) == 0) {
						SelectionEngine.this.problem = pb;
					}

					return pb;
			}
		};
		this.lookupEnvironment =
			new LookupEnvironment(this, this.compilerOptions, problemReporter, nameEnvironment);
		this.parser = new SelectionParser(problemReporter);
	}

	/**
	 * One result of the search consists of a new class.
	 * @param packageName char[]
	 * @param className char[]
	 * @param modifiers int
	 *
	 * NOTE - All package and type names are presented in their readable form:
	 *    Package names are in the form "a.b.c".
	 *    Nested type names are in the qualified form "A.M".
	 *    The default package is represented by an empty array.
	 */
	public void acceptClass(char[] packageName, char[] className, int modifiers, AccessRestriction accessRestriction) {
		if (CharOperation.equals(className, this.selectedIdentifier)) {
			if(mustQualifyType(packageName, className)) {
				char[][] acceptedClass = new char[2][];
				acceptedClass[0] = packageName;
				acceptedClass[1] = className;
				
				if(this.acceptedClasses == null) {
					this.acceptedClasses = new char[10][][];
					this.acceptedClassesCount = 0;
				}
				int length = this.acceptedClasses.length;
				if(length == this.acceptedClassesCount) {
					System.arraycopy(this.acceptedClasses, 0, this.acceptedClasses = new char[(length + 1)* 2][][], 0, length);
				}
				this.acceptedClasses[this.acceptedClassesCount++] = acceptedClass;
				
			} else {
				this.noProposal = false;
				this.requestor.acceptClass(
					packageName,
					className,
					false,
					null,
					this.actualSelectionStart,
					this.actualSelectionEnd);
				this.acceptedAnswer = true;
			}
		}
	}

	/**
	 * One result of the search consists of a new interface.
	 *
	 * NOTE - All package and type names are presented in their readable form:
	 *    Package names are in the form "a.b.c".
	 *    Nested type names are in the qualified form "A.I".
	 *    The default package is represented by an empty array.
	 */
	public void acceptInterface(
		char[] packageName,
		char[] interfaceName,
		int modifiers,
		AccessRestriction accessRestriction) {

		if (CharOperation.equals(interfaceName, this.selectedIdentifier)) {
			if(mustQualifyType(packageName, interfaceName)) {
				char[][] acceptedInterface= new char[2][];
				acceptedInterface[0] = packageName;
				acceptedInterface[1] = interfaceName;
				
				if(this.acceptedInterfaces == null) {
					this.acceptedInterfaces = new char[10][][];
					this.acceptedInterfacesCount = 0;
				}
				int length = this.acceptedInterfaces.length;
				if(length == this.acceptedInterfacesCount) {
					System.arraycopy(this.acceptedInterfaces, 0, this.acceptedInterfaces = new char[(length + 1) * 2][][], 0, length);
				}
				this.acceptedInterfaces[this.acceptedInterfacesCount++] = acceptedInterface;
				
			} else {
				this.noProposal = false;
				this.requestor.acceptInterface(
					packageName,
					interfaceName,
					false,
					null,
					this.actualSelectionStart,
					this.actualSelectionEnd);
				this.acceptedAnswer = true;
			}
		}
	}

	/**
	 * One result of the search consists of a new package.
	 * @param packageName char[]
	 * 
	 * NOTE - All package names are presented in their readable form:
	 *    Package names are in the form "a.b.c".
	 *    The default package is represented by an empty array.
	 */
	public void acceptPackage(char[] packageName) {
		// implementation of interface method
	}

	private void acceptQualifiedTypes() {
		if(this.acceptedClasses != null){
			this.acceptedAnswer = true;
			for (int i = 0; i < this.acceptedClassesCount; i++) {
				this.noProposal = false;
				this.requestor.acceptClass(
					this.acceptedClasses[i][0],
					this.acceptedClasses[i][1],
					false,
					null,
					this.actualSelectionStart,
					this.actualSelectionEnd);
			}
			this.acceptedClasses = null;
			this.acceptedClassesCount = 0;
		}
		if(this.acceptedInterfaces != null){
			this.acceptedAnswer = true;
			for (int i = 0; i < this.acceptedInterfacesCount; i++) {
				this.noProposal = false;
				this.requestor.acceptInterface(
					this.acceptedInterfaces[i][0],
					this.acceptedInterfaces[i][1],
					false,
					null,
					this.actualSelectionStart,
					this.actualSelectionEnd);
			}
			this.acceptedInterfaces = null;
			this.acceptedInterfacesCount = 0;
		}
	}
	private boolean checkSelection(
		char[] source,
		int selectionStart,
		int selectionEnd) {

		Scanner scanner = new Scanner();
		scanner.setSource(source);
		
		int lastIdentifierStart = -1;
		int lastIdentifierEnd = -1;
		char[] lastIdentifier = null;
		int token;
		
		if(selectionStart > selectionEnd){
			
			// compute start position of current line
			int currentPosition = selectionStart - 1;
			int nextCharacterPosition = selectionStart;
			char currentCharacter = ' ';
			try {
				while(currentPosition > 0){
					
					if(source[currentPosition] == '\\' && source[currentPosition+1] == 'u') {
						int pos = currentPosition + 2;
						int c1 = 0, c2 = 0, c3 = 0, c4 = 0;
						while (source[pos] == 'u') {
							pos++;
						}
						if ((c1 = Character.getNumericValue(source[pos++])) > 15
 c1 < 0
 (c2 = Character.getNumericValue(source[pos++])) > 15
 c2 < 0
 (c3 = Character.getNumericValue(source[pos++])) > 15
 c3 < 0
 (c4 = Character.getNumericValue(source[pos++])) > 15
 c4 < 0) {
							return false;
						} else {
							currentCharacter = (char) (((c1 * 16 + c2) * 16 + c3) * 16 + c4);
							nextCharacterPosition = pos;
						}
					} else {
						currentCharacter = source[currentPosition];
						nextCharacterPosition = currentPosition+1;
					}
					
					if(currentCharacter == '\r' || currentCharacter == '\n') {
						break;
					}
					currentPosition--;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
			
			// compute start and end of the last token
			scanner.resetTo(nextCharacterPosition, selectionEnd + 1 == source.length ? selectionEnd : selectionEnd + 1);
			do {
				try {
					token = scanner.getNextToken();
				} catch (InvalidInputException e) {
					return false;
				}
				if((token == TerminalTokens.TokenNamethis ||
					token == TerminalTokens.TokenNamesuper ||
					token == TerminalTokens.TokenNameIdentifier) &&
					scanner.startPosition <= selectionStart &&
					selectionStart <= scanner.currentPosition) {
					lastIdentifierStart = scanner.startPosition;
					lastIdentifierEnd = scanner.currentPosition - 1;
					lastIdentifier = scanner.getCurrentTokenSource();
				}
			} while (token != TerminalTokens.TokenNameEOF);
		} else {
			scanner.resetTo(selectionStart, selectionEnd);
	
			boolean expectingIdentifier = true;
			try {
				do {
					token = scanner.getNextToken();

					switch (token) {
						case TerminalTokens.TokenNamethis :
						case TerminalTokens.TokenNamesuper :
						case TerminalTokens.TokenNameIdentifier :
							if (!expectingIdentifier)
								return false;
							lastIdentifier = scanner.getCurrentTokenSource();
							lastIdentifierStart = scanner.startPosition;
							lastIdentifierEnd = scanner.currentPosition - 1;
							if(lastIdentifierEnd > selectionEnd) {
								lastIdentifierEnd = selectionEnd;
								lastIdentifier = CharOperation.subarray(lastIdentifier, 0,lastIdentifierEnd - lastIdentifierStart + 1);
							}
	
							expectingIdentifier = false;
							break;
						case TerminalTokens.TokenNameDOT :
							if (expectingIdentifier)
								return false;
							expectingIdentifier = true;
							break;
						case TerminalTokens.TokenNameEOF :
							if (expectingIdentifier)
								return false;
							break;
						case TerminalTokens.TokenNameLESS :
							CheckState state = new CheckState(1);
							if(!checkTypeArgument(scanner, state))
								return false;
							
							if(state.depth != 0)
								return false;
							break;
						default :
							return false;
					}
				} while (token != TerminalTokens.TokenNameEOF);
			} catch (InvalidInputException e) {
				return false;
			}
		}
		if (lastIdentifierStart > 0) {
			this.actualSelectionStart = lastIdentifierStart;
			this.actualSelectionEnd = lastIdentifierEnd;
			this.selectedIdentifier = lastIdentifier;
			return true;
		}
		return false;
	}
	private boolean checkTypeArgument(Scanner scanner, CheckState state) throws InvalidInputException {
		boolean expectingIdentifier = true;
		
		int token;
		do {
			token = scanner.getNextToken();
	
			switch(token) {
				case TerminalTokens.TokenNameDOT :
					if (expectingIdentifier)
						return false;
					expectingIdentifier = true;
					break;
				case TerminalTokens.TokenNameIdentifier :
					if (!expectingIdentifier)
						return false;
					expectingIdentifier = false;
					break;
				case TerminalTokens.TokenNameCOMMA :
					if (expectingIdentifier)
						return false;
					expectingIdentifier = true;
					break;
				case TerminalTokens.TokenNameLESS :
					int oldDepth = state.depth;
					state.depth++;
					if(checkTypeArgument(scanner, state)) {
						if(state.depth < oldDepth) {
							return true;
						}
					} else {
						return false;
					}
					break;
				case TerminalTokens.TokenNameGREATER :
					state.depth--;
					return state.depth >= 0;
				case TerminalTokens.TokenNameRIGHT_SHIFT :
					state.depth-=2;
					return state.depth >= 0;
				case TerminalTokens.TokenNameUNSIGNED_RIGHT_SHIFT :
					state.depth-=3;
					return state.depth >= 0;
				case TerminalTokens.TokenNameQUESTION :	
					token = scanner.getNextToken();
					if(token != TerminalTokens.TokenNameextends &&
							token != TerminalTokens.TokenNamesuper) {
						return false;
					}
					
					token = scanner.getNextToken();
					if(token != TerminalTokens.TokenNameIdentifier)
						return false;
					
					expectingIdentifier = false;
					break;
				default:
					return false;
			}
		} while (token != TerminalTokens.TokenNameEOF);
		
		return false;
	}

	private void completeLocalTypes(Binding binding){
		switch(binding.bindingType()) {
			case Binding.PARAMETERIZED_TYPE :
			case Binding.RAW_TYPE :
				ParameterizedTypeBinding parameterizedTypeBinding = (ParameterizedTypeBinding) binding;
				this.completeLocalTypes(parameterizedTypeBinding.type);
				TypeBinding[] args = parameterizedTypeBinding.arguments;
				int length = args == null ? 0 : args.length;
				for (int i = 0; i < length; i++) {
					this.completeLocalTypes(args[i]);
				}
				break;
			case Binding.TYPE :
			case Binding.GENERIC_TYPE :
				if(binding instanceof LocalTypeBinding) {
					LocalTypeBinding localTypeBinding = (LocalTypeBinding) binding;
					if(localTypeBinding.constantPoolName() == null) {
						char[] constantPoolName = this.unitScope.computeConstantPoolName(localTypeBinding);
						localTypeBinding.setConstantPoolName(constantPoolName);
					}
				}
				TypeBinding typeBinding = (TypeBinding) binding;
				ReferenceBinding enclosingType = typeBinding.enclosingType();
				if(enclosingType != null) {
					this.completeLocalTypes(enclosingType);
				}
				break;
			case Binding.ARRAY_TYPE :
				ArrayBinding arrayBinding = (ArrayBinding) binding;
				this.completeLocalTypes(arrayBinding.leafComponentType);
			case Binding.WILDCARD_TYPE :
			case Binding.TYPE_PARAMETER :
				break;	
			case Binding.FIELD :
				FieldBinding fieldBinding = (FieldBinding) binding;
				this.completeLocalTypes(fieldBinding.declaringClass);
				this.completeLocalTypes(fieldBinding.type);
				break;	
			case Binding.METHOD :
				MethodBinding methodBinding = (MethodBinding) binding;
				this.completeLocalTypes(methodBinding.returnType);
				TypeBinding[] parameters = methodBinding.parameters;
				for(int i = 0, max = parameters == null ? 0 : parameters.length; i < max; i++) {
					this.completeLocalTypes(parameters[i]);
				}
				break;
		}
	}
	
	public AssistParser getParser() {
		return this.parser;
	}

	/*
	 * Returns whether the given binding is a local/anonymous reference binding, or if its declaring class is
	 * local.
	 */
	private boolean isLocal(ReferenceBinding binding) {
		if(binding instanceof ParameterizedTypeBinding) {
			return isLocal(((ParameterizedTypeBinding)binding).type);
		}
		if (!(binding instanceof SourceTypeBinding)) return false;
		if (binding instanceof LocalTypeBinding) return true;
		if (binding instanceof MemberTypeBinding) {
			return isLocal(((MemberTypeBinding)binding).enclosingType);
		}
		return false;
	}

	/**
	 * Ask the engine to compute the selection at the specified position
	 * of the given compilation unit.

	 *  @param sourceUnit org.eclipse.jdt.internal.compiler.env.ICompilationUnit
	 *      the source of the current compilation unit.
	 *
	 *  @param selectionSourceStart int
	 *  @param selectionSourceEnd int
	 *      a range in the source where the selection is.
	 */
	public void select(
		ICompilationUnit sourceUnit,
		int selectionSourceStart,
		int selectionSourceEnd) {

		char[] source = sourceUnit.getContents();
		
		if(DEBUG) {
			System.out.print("SELECTION IN "); //$NON-NLS-1$
			System.out.print(sourceUnit.getFileName());
			System.out.print(" FROM "); //$NON-NLS-1$
			System.out.print(selectionSourceStart);
			System.out.print(" TO "); //$NON-NLS-1$
			System.out.println(selectionSourceEnd);
			System.out.println("SELECTION - Source :"); //$NON-NLS-1$
			System.out.println(source);
		}
		if (!checkSelection(source, selectionSourceStart, selectionSourceEnd))
			return;
		try {
			this.acceptedAnswer = false;
			CompilationResult result = new CompilationResult(sourceUnit, 1, 1, this.compilerOptions.maxProblemsPerUnit);
			CompilationUnitDeclaration parsedUnit =
				this.parser.dietParse(sourceUnit, result, this.actualSelectionStart, this.actualSelectionEnd);

			if (parsedUnit != null) {
				if(DEBUG) {
					System.out.println("SELECTION - Diet AST :"); //$NON-NLS-1$
					System.out.println(parsedUnit.toString());
				}
				
				// scan the package & import statements first
				if (parsedUnit.currentPackage instanceof SelectionOnPackageReference) {
					char[][] tokens =
						((SelectionOnPackageReference) parsedUnit.currentPackage).tokens;
					this.noProposal = false;
					this.requestor.acceptPackage(CharOperation.concatWith(tokens, '.'));
					return;
				}
				ImportReference[] imports = parsedUnit.imports;
				if (imports != null) {
					for (int i = 0, length = imports.length; i < length; i++) {
						ImportReference importReference = imports[i];
						if (importReference instanceof SelectionOnImportReference) {
							char[][] tokens = ((SelectionOnImportReference) importReference).tokens;
							this.noProposal = false;
							this.requestor.acceptPackage(CharOperation.concatWith(tokens, '.'));
							this.nameEnvironment.findTypes(CharOperation.concatWith(tokens, '.'), this);
							// accept qualified types only if no unqualified type was accepted
							if(!this.acceptedAnswer) {
								acceptQualifiedTypes();
								if (!this.acceptedAnswer) {
									this.nameEnvironment.findTypes(this.selectedIdentifier, this);
									// try with simple type name
									if(!this.acceptedAnswer) {
										acceptQualifiedTypes();
									}
								}
							}
							if(this.noProposal && this.problem != null) {
								this.requestor.acceptError(this.problem);
							}
							return;
						}
					}
				}
				if (parsedUnit.types != null) {
					if(selectDeclaration(parsedUnit))
						return;
					this.lookupEnvironment.buildTypeBindings(parsedUnit, null /*no access restriction*/);
					if ((this.unitScope = parsedUnit.scope)  != null) {
						try {
							this.lookupEnvironment.completeTypeBindings(parsedUnit, true);
							parsedUnit.scope.faultInTypes();
							ASTNode node = parseBlockStatements(parsedUnit, selectionSourceStart);
							if(DEBUG) {
								System.out.println("SELECTION - AST :"); //$NON-NLS-1$
								System.out.println(parsedUnit.toString());
							}
							parsedUnit.resolve();
							if (node != null) {
								selectLocalDeclaration(node);
							}
						} catch (SelectionNodeFound e) {
							if (e.binding != null) {
								if(DEBUG) {
									System.out.println("SELECTION - Selection binding:"); //$NON-NLS-1$
									System.out.println(e.binding.toString());
								}
								// if null then we found a problem in the selection node
								selectFrom(e.binding, parsedUnit, e.isDeclaration);
							}
						}
					}
				}
			}
			// only reaches here if no selection could be derived from the parsed tree
			// thus use the selected source and perform a textual type search
			if (!this.acceptedAnswer) {
				this.nameEnvironment.findTypes(this.selectedIdentifier, this);
				
				// accept qualified types only if no unqualified type was accepted
				if(!this.acceptedAnswer) {
					acceptQualifiedTypes();
				}
			}
			if(this.noProposal && this.problem != null) {
				this.requestor.acceptError(this.problem);
			}
		} catch (IndexOutOfBoundsException e) { // work-around internal failure - 1GEMF6D		
			if(DEBUG) {
				System.out.println("Exception caught by SelectionEngine:"); //$NON-NLS-1$
				e.printStackTrace(System.out);
			}
		} catch (AbortCompilation e) { // ignore this exception for now since it typically means we cannot find java.lang.Object
			if(DEBUG) {
				System.out.println("Exception caught by SelectionEngine:"); //$NON-NLS-1$
				e.printStackTrace(System.out);
			}
		} finally {
			reset();
		}
	}

	private void selectFrom(Binding binding, CompilationUnitDeclaration parsedUnit, boolean isDeclaration) {
		if(binding instanceof TypeVariableBinding) {
			TypeVariableBinding typeVariableBinding = (TypeVariableBinding) binding;
			Binding enclosingElement = typeVariableBinding.declaringElement;
			this.noProposal = false;
			
			if(enclosingElement instanceof SourceTypeBinding) {
				SourceTypeBinding enclosingType = (SourceTypeBinding) enclosingElement;
				this.requestor.acceptTypeParameter(
					enclosingType.qualifiedPackageName(),
					enclosingType.qualifiedSourceName(),
					typeVariableBinding.sourceName(),
					false,
					this.actualSelectionStart,
					this.actualSelectionEnd);
			} else if(enclosingElement instanceof MethodBinding) {
				MethodBinding enclosingMethod = (MethodBinding) enclosingElement;
				
				this.requestor.acceptMethodTypeParameter(
					enclosingMethod.declaringClass.qualifiedPackageName(),
					enclosingMethod.declaringClass.qualifiedSourceName(),
					enclosingMethod.selector,
					enclosingMethod.sourceStart(),
					enclosingMethod.sourceEnd(),
					typeVariableBinding.sourceName(),
					false,
					this.actualSelectionStart,
					this.actualSelectionEnd);
			}
			this.acceptedAnswer = true;
		} else if (binding instanceof ReferenceBinding) {
			ReferenceBinding typeBinding = (ReferenceBinding) binding;
			if (typeBinding.isInterface()) {
				this.noProposal = false;
				if (isLocal(typeBinding) && this.requestor instanceof SelectionRequestor) {
					if(typeBinding.isParameterizedType() || typeBinding.isRawType()) {
						completeLocalTypes(typeBinding);
					}
					((SelectionRequestor)this.requestor).acceptLocalType(typeBinding);
				} else {
					char[] genericTypeSignature = null;
					if(typeBinding.isParameterizedType() || typeBinding.isRawType()) {
						completeLocalTypes(typeBinding);
						genericTypeSignature = typeBinding.computeUniqueKey();
					}
					this.requestor.acceptInterface(
						typeBinding.qualifiedPackageName(),
						typeBinding.qualifiedSourceName(),
						false,
						genericTypeSignature,
						this.actualSelectionStart,
						this.actualSelectionEnd);
				}
			} else if(typeBinding instanceof ProblemReferenceBinding){
				ReferenceBinding original = ((ProblemReferenceBinding) typeBinding).original;
				if(original == null) return;
				this.noProposal = false;
				if (isLocal(original) && this.requestor instanceof SelectionRequestor) {
					if(original.isParameterizedType() || typeBinding.isRawType()) {
						completeLocalTypes(original);
					}
					((SelectionRequestor)this.requestor).acceptLocalType(original);
				} else {
					char[] genericTypeSignature = null;
					if(typeBinding.isParameterizedType() || typeBinding.isRawType()) {
						completeLocalTypes(typeBinding);
						genericTypeSignature = typeBinding.computeUniqueKey();
					}
					this.requestor.acceptClass(
						original.qualifiedPackageName(),
						original.qualifiedSourceName(),
						false,
						genericTypeSignature,
						this.actualSelectionStart,
						this.actualSelectionEnd);
				}
			} else {
				this.noProposal = false;
				if (isLocal(typeBinding) && this.requestor instanceof SelectionRequestor) {
					if(typeBinding.isParameterizedType() || typeBinding.isRawType()) {
						completeLocalTypes(typeBinding);
					}
					((SelectionRequestor)this.requestor).acceptLocalType(typeBinding);
				} else {
					char[] genericTypeSignature = null;
					if(typeBinding.isParameterizedType() || typeBinding.isRawType()) {
						completeLocalTypes(typeBinding);
						genericTypeSignature = typeBinding.computeUniqueKey();
					}
					this.requestor.acceptClass(
						typeBinding.qualifiedPackageName(),
						typeBinding.qualifiedSourceName(),
						false,
						genericTypeSignature,
						this.actualSelectionStart,
						this.actualSelectionEnd);
				}
			}
			this.acceptedAnswer = true;
		} else
			if (binding instanceof MethodBinding) {
				MethodBinding methodBinding = (MethodBinding) binding;
				TypeBinding[] parameterTypes = methodBinding.original().parameters;
				int length = parameterTypes.length;
				char[][] parameterPackageNames = new char[length][];
				char[][] parameterTypeNames = new char[length][];
				String[] parameterSignatures = new String[length];
				for (int i = 0; i < length; i++) {
					parameterPackageNames[i] = parameterTypes[i].qualifiedPackageName();
					parameterTypeNames[i] = parameterTypes[i].qualifiedSourceName();
					parameterSignatures[i] = new String(getSignature(parameterTypes[i])).replace('/', '.');
				}
				this.noProposal = false;
				ReferenceBinding declaringClass = methodBinding.declaringClass;
				if (isLocal(declaringClass) && this.requestor instanceof SelectionRequestor) {
					if(methodBinding instanceof ParameterizedMethodBinding) {
						completeLocalTypes(methodBinding);
					}
					((SelectionRequestor)this.requestor).acceptLocalMethod(methodBinding);
				} else {
					char[] uniqueKey = null;
					if(methodBinding instanceof ParameterizedMethodBinding) {
						completeLocalTypes(methodBinding);
						uniqueKey = methodBinding.computeUniqueKey();
					}
					this.requestor.acceptMethod(
						declaringClass.qualifiedPackageName(),
						declaringClass.qualifiedSourceName(),
						declaringClass.enclosingType() == null ? null : new String(getSignature(declaringClass.enclosingType())),
						methodBinding.isConstructor()
							? declaringClass.sourceName()
							: methodBinding.selector,
						parameterPackageNames,
						parameterTypeNames,
						parameterSignatures,
						methodBinding.isConstructor(), 
						isDeclaration,
						uniqueKey,
						this.actualSelectionStart,
						this.actualSelectionEnd);
				}
				this.acceptedAnswer = true;
			} else
				if (binding instanceof FieldBinding) {
					FieldBinding fieldBinding = (FieldBinding) binding;
					ReferenceBinding declaringClass = fieldBinding.declaringClass;
					if (declaringClass != null) { // arraylength
						this.noProposal = false;
						if (isLocal(declaringClass) && this.requestor instanceof SelectionRequestor) {
							if(fieldBinding instanceof ParameterizedFieldBinding) {
								completeLocalTypes(fieldBinding.declaringClass);
							}
							((SelectionRequestor)this.requestor).acceptLocalField(fieldBinding);
						} else {
							char[] uniqueKey = null;
							if(fieldBinding instanceof ParameterizedFieldBinding) {
								completeLocalTypes(fieldBinding.declaringClass);
								uniqueKey = fieldBinding.computeUniqueKey();
							}
							this.requestor.acceptField(
								declaringClass.qualifiedPackageName(),
								declaringClass.qualifiedSourceName(),
								fieldBinding.name,
								false,
								uniqueKey,
								this.actualSelectionStart,
								this.actualSelectionEnd);
						}
						this.acceptedAnswer = true;
					}
				} else
					if (binding instanceof LocalVariableBinding) {
						if (this.requestor instanceof SelectionRequestor) {
							((SelectionRequestor)this.requestor).acceptLocalVariable((LocalVariableBinding)binding);
							this.acceptedAnswer = true;
						} else {
							// open on the type of the variable
							selectFrom(((LocalVariableBinding) binding).type, parsedUnit, false);
						}
					} else
						if (binding instanceof ArrayBinding) {
							selectFrom(((ArrayBinding) binding).leafComponentType, parsedUnit, false);
							// open on the type of the array
						} else
							if (binding instanceof PackageBinding) {
								PackageBinding packageBinding = (PackageBinding) binding;
								this.noProposal = false;
								this.requestor.acceptPackage(packageBinding.readableName());
								this.acceptedAnswer = true;
							} else
								if(binding instanceof BaseTypeBinding) {
									this.acceptedAnswer = true;
								}
	}
	
	/*
	 * Checks if a local declaration got selected in this method/initializer/field.
	 */
	private void selectLocalDeclaration(ASTNode node) {
		// the selected identifier is not identical to the parser one (equals but not identical),
		// for traversing the parse tree, the parser assist identifier is necessary for identitiy checks
		final char[] assistIdentifier = this.getParser().assistIdentifier();
		if (assistIdentifier == null) return;
		
		class Visitor extends ASTVisitor {
			public boolean visit(ConstructorDeclaration constructorDeclaration, ClassScope scope) {
				if (constructorDeclaration.selector == assistIdentifier){
					if (constructorDeclaration.binding != null) {
						throw new SelectionNodeFound(constructorDeclaration.binding);
					} else {
						if (constructorDeclaration.scope != null) {
							throw new SelectionNodeFound(new MethodBinding(constructorDeclaration.modifiers, constructorDeclaration.selector, null, null, null, constructorDeclaration.scope.referenceType().binding));
						}
					}
				}
				return true;
			}
			public boolean visit(FieldDeclaration fieldDeclaration, MethodScope scope) {
				if (fieldDeclaration.name == assistIdentifier){
					throw new SelectionNodeFound(fieldDeclaration.binding);
				}
				return true;
			}
			public boolean visit(TypeDeclaration localTypeDeclaration, BlockScope scope) {
				if (localTypeDeclaration.name == assistIdentifier) {
					throw new SelectionNodeFound(localTypeDeclaration.binding);
				}
				return true;
			}
			public boolean visit(TypeDeclaration memberTypeDeclaration, ClassScope scope) {
				if (memberTypeDeclaration.name == assistIdentifier) {
					throw new SelectionNodeFound(memberTypeDeclaration.binding);
				}
				return true;
			}
			public boolean visit(MethodDeclaration methodDeclaration, ClassScope scope) {
				if (methodDeclaration.selector == assistIdentifier){
					if (methodDeclaration.binding != null) {
						throw new SelectionNodeFound(methodDeclaration.binding);
					} else {
						if (methodDeclaration.scope != null) {
							throw new SelectionNodeFound(new MethodBinding(methodDeclaration.modifiers, methodDeclaration.selector, null, null, null, methodDeclaration.scope.referenceType().binding));
						}
					}
				}
				return true;
			}
			public boolean visit(TypeDeclaration typeDeclaration, CompilationUnitScope scope) {
				if (typeDeclaration.name == assistIdentifier) {
					throw new SelectionNodeFound(typeDeclaration.binding);
				}
				return true;
			}
		}
		
		if (node instanceof AbstractMethodDeclaration) {
			((AbstractMethodDeclaration)node).traverse(new Visitor(), (ClassScope)null);
		} else {
			((FieldDeclaration)node).traverse(new Visitor(), (MethodScope)null);
		}
	}

	/**
	 * Asks the engine to compute the selection of the given type
	 * from the source type.
	 *
	 *  @param sourceType org.eclipse.jdt.internal.compiler.env.ISourceType
	 *      a source form of the current type in which code assist is invoked.
	 *
	 *  @param typeName char[]
	 *      a type name which is to be resolved in the context of a compilation unit.
	 *		NOTE: the type name is supposed to be correctly reduced (no whitespaces, no unicodes left)
	 * 
	 * @param topLevelTypes SourceTypeElementInfo[]
	 *      a source form of the top level types of the compilation unit in which code assist is invoked.

	 *  @param searchInEnvironment
	 * 	if <code>true</code> and no selection could be found in context then search type in environment.
	 */
	public void selectType(ISourceType sourceType, char[] typeName, SourceTypeElementInfo[] topLevelTypes, boolean searchInEnvironment) {
		try {
			this.acceptedAnswer = false;

			// find the outer most type
			ISourceType outerType = sourceType;
			ISourceType parent = sourceType.getEnclosingType();
			while (parent != null) {
				outerType = parent;
				parent = parent.getEnclosingType();
			}
			// compute parse tree for this most outer type
			CompilationResult result = new CompilationResult(outerType.getFileName(), 1, 1, this.compilerOptions.maxProblemsPerUnit);
			if (!(sourceType instanceof SourceTypeElementInfo)) return;
			SourceType typeHandle = (SourceType) ((SourceTypeElementInfo)sourceType).getHandle();
			int flags = SourceTypeConverter.FIELD_AND_METHOD | SourceTypeConverter.MEMBER_TYPE;
			if (typeHandle.isAnonymous() || typeHandle.isLocal()) 
				flags |= SourceTypeConverter.LOCAL_TYPE;
			CompilationUnitDeclaration parsedUnit =
				SourceTypeConverter.buildCompilationUnit(
						topLevelTypes,
						flags,
						this.parser.problemReporter(), 
						result);

			if (parsedUnit != null && parsedUnit.types != null) {
				if(DEBUG) {
					System.out.println("SELECTION - Diet AST :"); //$NON-NLS-1$
					System.out.println(parsedUnit.toString());
				}
				// find the type declaration that corresponds to the original source type
				TypeDeclaration typeDecl = new ASTNodeFinder(parsedUnit).findType(typeHandle);

				if (typeDecl != null) {

					// add fake field with the type we're looking for
					// note: since we didn't ask for fields above, there is no field defined yet
					FieldDeclaration field = new FieldDeclaration();
					int dot;
					if ((dot = CharOperation.lastIndexOf('.', typeName)) == -1) {
						this.selectedIdentifier = typeName;
						field.type = new SelectionOnSingleTypeReference(typeName, -1);
						// position not used
					} else {
						char[][] previousIdentifiers = CharOperation.splitOn('.', typeName, 0, dot);
						char[] selectionIdentifier =
							CharOperation.subarray(typeName, dot + 1, typeName.length);
						this.selectedIdentifier = selectionIdentifier;
						field.type =
							new SelectionOnQualifiedTypeReference(
								previousIdentifiers,
								selectionIdentifier,
								new long[previousIdentifiers.length + 1]);
					}
					field.name = "<fakeField>".toCharArray(); //$NON-NLS-1$
					typeDecl.fields = new FieldDeclaration[] { field };

					// build bindings
					this.lookupEnvironment.buildTypeBindings(parsedUnit, null /*no access restriction*/);
					if ((this.unitScope = parsedUnit.scope) != null) {
						try {
							// build fields
							// note: this builds fields only in the parsed unit (the buildFieldsAndMethods flag is not passed along)
							this.lookupEnvironment.completeTypeBindings(parsedUnit, true);

							// resolve
							parsedUnit.scope.faultInTypes();
							parsedUnit.resolve();
						} catch (SelectionNodeFound e) {
							if (e.binding != null) {
								if(DEBUG) {
									System.out.println("SELECTION - Selection binding :"); //$NON-NLS-1$
									System.out.println(e.binding.toString());
								}
								// if null then we found a problem in the selection node
								selectFrom(e.binding, parsedUnit, e.isDeclaration);
							}
						}
					}
				}
			}
			// only reaches here if no selection could be derived from the parsed tree
			// thus use the selected source and perform a textual type search
			if (!this.acceptedAnswer && searchInEnvironment) {
				if (this.selectedIdentifier != null) {
					this.nameEnvironment.findTypes(typeName, this);
					
					// accept qualified types only if no unqualified type was accepted
					if(!this.acceptedAnswer) {
						acceptQualifiedTypes();
					}
				}
			}
			if(this.noProposal && this.problem != null) {
				this.requestor.acceptError(this.problem);
			}
		} catch (AbortCompilation e) { // ignore this exception for now since it typically means we cannot find java.lang.Object
		} finally {
			reset();
		}
	}

	// Check if a declaration got selected in this unit
	private boolean selectDeclaration(CompilationUnitDeclaration compilationUnit){

		// the selected identifier is not identical to the parser one (equals but not identical),
		// for traversing the parse tree, the parser assist identifier is necessary for identitiy checks
		char[] assistIdentifier = this.getParser().assistIdentifier();
		if (assistIdentifier == null) return false;
		
		ImportReference currentPackage = compilationUnit.currentPackage;
		char[] packageName = currentPackage == null ? new char[0] : CharOperation.concatWith(currentPackage.tokens, '.');
		// iterate over the types
		TypeDeclaration[] types = compilationUnit.types;
		for (int i = 0, length = types == null ? 0 : types.length; i < length; i++){
			if(selectDeclaration(types[i], assistIdentifier, packageName))
				return true;
		}
		return false;
	}

	// Check if a declaration got selected in this type
	private boolean selectDeclaration(TypeDeclaration typeDeclaration, char[] assistIdentifier, char[] packageName){
	
		if (typeDeclaration.name == assistIdentifier){
			char[] qualifiedSourceName = null;
			
			TypeDeclaration enclosingType = typeDeclaration;
			while(enclosingType != null) {
				qualifiedSourceName = CharOperation.concat(enclosingType.name, qualifiedSourceName, '.');
				enclosingType = enclosingType.enclosingType;
			}
			switch (typeDeclaration.getKind()) {
				case IGenericType.CLASS :
					this.requestor.acceptClass(
						packageName,
						qualifiedSourceName,
						true,
						null,
						this.actualSelectionStart,
						this.actualSelectionEnd);
					break;
				case IGenericType.INTERFACE :
					this.requestor.acceptInterface(
						packageName,
						qualifiedSourceName,
						true,
						null,
						this.actualSelectionStart,
						this.actualSelectionEnd);
					break;
				case IGenericType.ENUM :
					this.requestor.acceptClass(
						packageName,
						qualifiedSourceName,
						true,
						null,
						this.actualSelectionStart,
						this.actualSelectionEnd);
					break;
				case IGenericType.ANNOTATION_TYPE :
					this.requestor.acceptInterface(
						packageName,
						qualifiedSourceName,
						true,
						null,
						this.actualSelectionStart,
						this.actualSelectionEnd);
					break;
			}			
			this.noProposal = false;
			return true;
		}
		TypeDeclaration[] memberTypes = typeDeclaration.memberTypes;
		for (int i = 0, length = memberTypes == null ? 0 : memberTypes.length; i < length; i++){
			if(selectDeclaration(memberTypes[i], assistIdentifier, packageName))
				return true;
		}
		FieldDeclaration[] fields = typeDeclaration.fields;
		for (int i = 0, length = fields == null ? 0 : fields.length; i < length; i++){
			if (fields[i].name == assistIdentifier){
				char[] qualifiedSourceName = null;
				
				TypeDeclaration enclosingType = typeDeclaration;
				while(enclosingType != null) {
					qualifiedSourceName = CharOperation.concat(enclosingType.name, qualifiedSourceName, '.');
					enclosingType = enclosingType.enclosingType;
				}
				
				this.requestor.acceptField(
					packageName,
					qualifiedSourceName,
					fields[i].name,
					true,
					null,
					this.actualSelectionStart,
					this.actualSelectionEnd);

				this.noProposal = false;
				return true;
			}
		}
		AbstractMethodDeclaration[] methods = typeDeclaration.methods;
		for (int i = 0, length = methods == null ? 0 : methods.length; i < length; i++){
			AbstractMethodDeclaration method = methods[i];
			
			if (method.selector == assistIdentifier){
				char[] qualifiedSourceName = null;
				
				TypeDeclaration enclosingType = typeDeclaration;
				while(enclosingType != null) {
					qualifiedSourceName = CharOperation.concat(enclosingType.name, qualifiedSourceName, '.');
					enclosingType = enclosingType.enclosingType;
				}
				
				this.requestor.acceptMethod(
					packageName,
					qualifiedSourceName,
					null, // SelectionRequestor does not need of declaring type signature for method declaration
					method.selector,
					null, // SelectionRequestor does not need of parameters type for method declaration
					null, // SelectionRequestor does not need of parameters type for method declaration
					null, // SelectionRequestor does not need of parameters type for method declaration
					method.isConstructor(),
					true,
					null,
					this.actualSelectionStart,
					this.actualSelectionEnd);
				
				this.noProposal = false;
				return true;
			}
			
			TypeParameter[] methodTypeParameters = method.typeParameters();
			for (int j = 0, length2 = methodTypeParameters == null ? 0 : methodTypeParameters.length; j < length2; j++){
				TypeParameter methodTypeParameter = methodTypeParameters[j];
				
				if(methodTypeParameter.name == assistIdentifier) {
					char[] qualifiedSourceName = null;
					
					TypeDeclaration enclosingType = typeDeclaration;
					while(enclosingType != null) {
						qualifiedSourceName = CharOperation.concat(enclosingType.name, qualifiedSourceName, '.');
						enclosingType = enclosingType.enclosingType;
					}
					
					this.requestor.acceptMethodTypeParameter(
						packageName,
						qualifiedSourceName,
						method.selector,
						method.sourceStart,
						method.sourceEnd,
						methodTypeParameter.name,
						true,
						this.actualSelectionStart,
						this.actualSelectionEnd);
					
					this.noProposal = false;
					return true;
				}
			}
		}
		
		TypeParameter[] typeParameters = typeDeclaration.typeParameters;
		for (int i = 0, length = typeParameters == null ? 0 : typeParameters.length; i < length; i++){
			TypeParameter typeParameter = typeParameters[i];
			if(typeParameter.name == assistIdentifier) {
				char[] qualifiedSourceName = null;
				
				TypeDeclaration enclosingType = typeDeclaration;
				while(enclosingType != null) {
					qualifiedSourceName = CharOperation.concat(enclosingType.name, qualifiedSourceName, '.');
					enclosingType = enclosingType.enclosingType;
				}
				
				this.requestor.acceptTypeParameter(
					packageName,
					qualifiedSourceName,
					typeParameter.name,
					true,
					this.actualSelectionStart,
					this.actualSelectionEnd);
				
				this.noProposal = false;
				return true;
			}
		}
		
		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9020.java