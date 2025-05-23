error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8415.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8415.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8415.java
text:
```scala
C@@lassFileReader reader = (ClassFileReader) classFile.getBinaryTypeInfo((IFile) classFile.resource(), false/*don't fully initialize so as to keep constant pool (used below)*/);

/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.codeassist;

import java.util.Locale;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.internal.codeassist.impl.*;
import org.eclipse.jdt.internal.codeassist.select.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.*;
import org.eclipse.jdt.internal.compiler.problem.*;
import org.eclipse.jdt.internal.core.BinaryTypeConverter;
import org.eclipse.jdt.internal.core.ClassFile;
import org.eclipse.jdt.internal.core.SearchableEnvironment;
import org.eclipse.jdt.internal.core.SelectionRequestor;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jdt.internal.core.SourceTypeElementInfo;
import org.eclipse.jdt.internal.core.util.ASTNodeFinder;
import org.eclipse.jdt.internal.core.util.HashSetOfCharArrayArray;

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
	public static boolean PERF = false;
	
	SelectionParser parser;
	ISelectionRequestor requestor;

	boolean acceptedAnswer;

	private int actualSelectionStart;
	private int actualSelectionEnd;
	private char[] selectedIdentifier;
	
	private char[][][] acceptedClasses;
	private int[] acceptedClassesModifiers;
	private char[][][] acceptedInterfaces;
	private int[] acceptedInterfacesModifiers;
	private char[][][] acceptedEnums;
	private int[] acceptedEnumsModifiers;
	private char[][][] acceptedAnnotations;
	private int[] acceptedAnnotationsModifiers;
	int acceptedClassesCount;
	int acceptedInterfacesCount;
	int acceptedEnumsCount;
	int acceptedAnnotationsCount;
	
	boolean noProposal = true;
	CategorizedProblem problem = null;

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
					
			public CategorizedProblem createProblem(
				char[] fileName,
				int problemId,
				String[] problemArguments,
				String[] messageArguments,
				int severity,
				int problemStartPosition,
				int problemEndPosition,
				int lineNumber,
				int columnNumber) {
				CategorizedProblem pb =  super.createProblem(
					fileName,
					problemId,
					problemArguments,
					messageArguments,
					severity,
					problemStartPosition,
					problemEndPosition,
					lineNumber,
					columnNumber);
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

	public void acceptType(char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames, int modifiers, AccessRestriction accessRestriction) {
		char[] typeName = enclosingTypeNames == null ?
				simpleTypeName :
					CharOperation.concat(
						CharOperation.concatWith(enclosingTypeNames, '.'),
						simpleTypeName,
						'.');
		
		if (CharOperation.equals(simpleTypeName, this.selectedIdentifier)) {
			char[] flatEnclosingTypeNames =
				enclosingTypeNames == null || enclosingTypeNames.length == 0 ?
						null :
							CharOperation.concatWith(enclosingTypeNames, '.');
			if(mustQualifyType(packageName, simpleTypeName, flatEnclosingTypeNames, modifiers)) {
				int length = 0;
				int kind = modifiers & (ClassFileConstants.AccInterface | ClassFileConstants.AccEnum | ClassFileConstants.AccAnnotation);
				switch (kind) {
					case ClassFileConstants.AccAnnotation:
					case ClassFileConstants.AccAnnotation | ClassFileConstants.AccInterface:
						char[][] acceptedAnnotation = new char[2][];
						acceptedAnnotation[0] = packageName;
						acceptedAnnotation[1] = typeName;
						
						if(this.acceptedAnnotations == null) {
							this.acceptedAnnotations = new char[10][][];
							this.acceptedAnnotationsModifiers = new int[10];
							this.acceptedAnnotationsCount = 0;
						}
						length = this.acceptedAnnotations.length;
						if(length == this.acceptedAnnotationsCount) {
							int newLength = (length + 1)* 2;
							System.arraycopy(this.acceptedAnnotations, 0, this.acceptedAnnotations = new char[newLength][][], 0, length);
							System.arraycopy(this.acceptedAnnotationsModifiers, 0, this.acceptedAnnotationsModifiers = new int[newLength], 0, length);
						}
						this.acceptedAnnotationsModifiers[this.acceptedAnnotationsCount] = modifiers;
						this.acceptedAnnotations[this.acceptedAnnotationsCount++] = acceptedAnnotation;
						break;
					case ClassFileConstants.AccEnum:
						char[][] acceptedEnum = new char[2][];
						acceptedEnum[0] = packageName;
						acceptedEnum[1] = typeName;
						
						if(this.acceptedEnums == null) {
							this.acceptedEnums = new char[10][][];
							this.acceptedEnumsModifiers = new int[10];
							this.acceptedEnumsCount = 0;
						}
						length = this.acceptedEnums.length;
						if(length == this.acceptedEnumsCount) {
							int newLength = (length + 1)* 2;
							System.arraycopy(this.acceptedEnums, 0, this.acceptedEnums = new char[newLength][][], 0, length);
							System.arraycopy(this.acceptedEnumsModifiers, 0, this.acceptedEnumsModifiers = new int[newLength], 0, length);
						}
						this.acceptedEnumsModifiers[this.acceptedEnumsCount] = modifiers;
						this.acceptedEnums[this.acceptedEnumsCount++] = acceptedEnum;
						break;
					case ClassFileConstants.AccInterface:
						char[][] acceptedInterface= new char[2][];
						acceptedInterface[0] = packageName;
						acceptedInterface[1] = typeName;
						
						if(this.acceptedInterfaces == null) {
							this.acceptedInterfaces = new char[10][][];
							this.acceptedInterfacesModifiers = new int[10];
							this.acceptedInterfacesCount = 0;
						}
						length = this.acceptedInterfaces.length;
						if(length == this.acceptedInterfacesCount) {
							int newLength = (length + 1)* 2;
							System.arraycopy(this.acceptedInterfaces, 0, this.acceptedInterfaces = new char[newLength][][], 0, length);
							System.arraycopy(this.acceptedInterfacesModifiers, 0, this.acceptedInterfacesModifiers = new int[newLength], 0, length);
						}
						this.acceptedInterfacesModifiers[this.acceptedInterfacesCount] = modifiers;
						this.acceptedInterfaces[this.acceptedInterfacesCount++] = acceptedInterface;
						break;
					default:
						char[][] acceptedClass = new char[2][];
						acceptedClass[0] = packageName;
						acceptedClass[1] = typeName;
						
						if(this.acceptedClasses == null) {
							this.acceptedClasses = new char[10][][];
							this.acceptedClassesModifiers = new int[10];
							this.acceptedClassesCount = 0;
						}
						length = this.acceptedClasses.length;
						if(length == this.acceptedClassesCount) {
							int newLength = (length + 1)* 2;
							System.arraycopy(this.acceptedClasses, 0, this.acceptedClasses = new char[newLength][][], 0, length);
							System.arraycopy(this.acceptedClassesModifiers, 0, this.acceptedClassesModifiers = new int[newLength], 0, length);
						}
						this.acceptedClassesModifiers[this.acceptedClassesCount] = modifiers;
						this.acceptedClasses[this.acceptedClassesCount++] = acceptedClass;
						break;
				}
			} else {
				this.noProposal = false;
				this.requestor.acceptType(
					packageName,
					typeName,
					modifiers,
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
				this.requestor.acceptType(
					this.acceptedClasses[i][0],
					this.acceptedClasses[i][1],
					this.acceptedClassesModifiers[i],
					false,
					null,
					this.actualSelectionStart,
					this.actualSelectionEnd);
			}
			this.acceptedClasses = null;
			this.acceptedClassesModifiers = null;
			this.acceptedClassesCount = 0;
		}
		if(this.acceptedInterfaces != null){
			this.acceptedAnswer = true;
			for (int i = 0; i < this.acceptedInterfacesCount; i++) {
				this.noProposal = false;
				this.requestor.acceptType(
					this.acceptedInterfaces[i][0],
					this.acceptedInterfaces[i][1],
					this.acceptedInterfacesModifiers[i],
					false,
					null,
					this.actualSelectionStart,
					this.actualSelectionEnd);
			}
			this.acceptedInterfaces = null;
			this.acceptedInterfacesModifiers = null;
			this.acceptedInterfacesCount = 0;
		}
		if(this.acceptedAnnotations != null){
			this.acceptedAnswer = true;
			for (int i = 0; i < this.acceptedAnnotationsCount; i++) {
				this.noProposal = false;
				this.requestor.acceptType(
					this.acceptedAnnotations[i][0],
					this.acceptedAnnotations[i][1],
					this.acceptedAnnotationsModifiers[i],
					false,
					null,
					this.actualSelectionStart,
					this.actualSelectionEnd);
			}
			this.acceptedAnnotations = null;
			this.acceptedAnnotationsModifiers = null;
			this.acceptedAnnotationsCount = 0;
		}
		if(this.acceptedEnums != null){
			this.acceptedAnswer = true;
			for (int i = 0; i < this.acceptedEnumsCount; i++) {
				this.noProposal = false;
				this.requestor.acceptType(
					this.acceptedEnums[i][0],
					this.acceptedEnums[i][1],
					this.acceptedEnumsModifiers[i],
					false,
					null,
					this.actualSelectionStart,
					this.actualSelectionEnd);
			}
			this.acceptedEnums = null;
			this.acceptedEnumsModifiers = null;
			this.acceptedEnumsCount = 0;
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
			int end = source.length - 1;
			
			// compute start position of current line
			int currentPosition = selectionStart - 1;
			int nextCharacterPosition = selectionStart;
			char currentCharacter = ' ';
			try {
				lineLoop: while(currentPosition > 0){
					
					if(source[currentPosition] == '\\' && source[currentPosition+1] == 'u') {
						int pos = currentPosition + 2;
						int c1 = 0, c2 = 0, c3 = 0, c4 = 0;
						while (source[pos] == 'u') {
							pos++;
						}
						
						int endOfUnicode = pos + 3;
						if (end < endOfUnicode) {
							if (endOfUnicode < source.length) {
								end = endOfUnicode;
							} else {
								return false; // not enough characters to decode an unicode
							}
						}

						if ((c1 = ScannerHelper.getNumericValue(source[pos++])) > 15
 c1 < 0
 (c2 = ScannerHelper.getNumericValue(source[pos++])) > 15
 c2 < 0
 (c3 = ScannerHelper.getNumericValue(source[pos++])) > 15
 c3 < 0
 (c4 = ScannerHelper.getNumericValue(source[pos++])) > 15
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
					
					switch(currentCharacter) {
						case '\r':
						case '\n':
						case '/':
						case '"':
						case '\'':
							break lineLoop;
					}
					currentPosition--;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
			
			// compute start and end of the last token
			scanner.resetTo(nextCharacterPosition, end);
			isolateLastName: do {
				try {
					token = scanner.getNextToken();
				} catch (InvalidInputException e) {
					return false;
				}
				switch (token) {
					case TerminalTokens.TokenNamethis:
					case TerminalTokens.TokenNamesuper:
					case TerminalTokens.TokenNameIdentifier:
						if (scanner.startPosition <= selectionStart && selectionStart <= scanner.currentPosition) {
							if (scanner.currentPosition == scanner.eofPosition) {
								int temp = scanner.eofPosition;
								scanner.eofPosition = scanner.source.length;
							 	while(scanner.getNextCharAsJavaIdentifierPart()){/*empty*/}
							 	scanner.eofPosition = temp;
							}
							lastIdentifierStart = scanner.startPosition;
							lastIdentifierEnd = scanner.currentPosition - 1;
							lastIdentifier = scanner.getCurrentTokenSource();
							break isolateLastName;
						}
						break;
				}
			} while (token != TerminalTokens.TokenNameEOF);
		} else {
			scanner.resetTo(selectionStart, selectionEnd);
	
			boolean expectingIdentifier = true;
			do {
				try {
					token = scanner.getNextToken();
				} catch (InvalidInputException e) {
					return false;
				}
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
						if(!checkTypeArgument(scanner))
							return false;
						break;
					case TerminalTokens.TokenNameAT:
						if(scanner.startPosition != scanner.initialPosition)
							return false;
						break;
					default :
						return false;
				}
			} while (token != TerminalTokens.TokenNameEOF);
		}
		if (lastIdentifierStart > 0) {
			this.actualSelectionStart = lastIdentifierStart;
			this.actualSelectionEnd = lastIdentifierEnd;
			this.selectedIdentifier = lastIdentifier;
			return true;
		}
		return false;
	}
	private boolean checkTypeArgument(Scanner scanner) {
		int depth = 1;
		int token;
		StringBuffer buffer = new StringBuffer();
		do {
			try {
				token = scanner.getNextToken();
			} catch (InvalidInputException e) {
				return false;
			}
			switch(token) {
				case TerminalTokens.TokenNameLESS :
					depth++;
					buffer.append(scanner.getCurrentTokenSource());
					break;
				case TerminalTokens.TokenNameGREATER :
					depth--;
					buffer.append(scanner.getCurrentTokenSource());
					break;
				case TerminalTokens.TokenNameRIGHT_SHIFT :
					depth-=2;
					buffer.append(scanner.getCurrentTokenSource());
					break;
				case TerminalTokens.TokenNameUNSIGNED_RIGHT_SHIFT :
					depth-=3;
					buffer.append(scanner.getCurrentTokenSource());
					break;
				case TerminalTokens.TokenNameextends :
				case TerminalTokens.TokenNamesuper :
					buffer.append(' ');
					buffer.append(scanner.getCurrentTokenSource());
					buffer.append(' ');
					break;
				case TerminalTokens.TokenNameCOMMA :
					if(depth == 1) {
						int length = buffer.length();
						char[] typeRef = new char[length];
						buffer.getChars(0, length, typeRef, 0);
						try {
							Signature.createTypeSignature(typeRef, true);
							buffer = new StringBuffer();
						} catch(IllegalArgumentException e) {
							return false;
						}
					}
					break;
				default :
					buffer.append(scanner.getCurrentTokenSource());
					break;
				
			}
			if(depth < 0) {
				return false;
			}
		} while (depth != 0 && token != TerminalTokens.TokenNameEOF);
		
		if(depth == 0) {
			int length = buffer.length() - 1;
			char[] typeRef = new char[length];
			buffer.getChars(0, length, typeRef, 0);
			try {
				Signature.createTypeSignature(typeRef, true);
				return true;
			} catch(IllegalArgumentException e) {
				return false;
			}
		}
		
		return false;
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
			return isLocal(((ParameterizedTypeBinding)binding).genericType());
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
		if (!checkSelection(source, selectionSourceStart, selectionSourceEnd)) {
			return;
		}
		if (DEBUG) {
			System.out.print("SELECTION - Checked : \""); //$NON-NLS-1$
			System.out.print(new String(source, actualSelectionStart, actualSelectionEnd-actualSelectionStart+1));
			System.out.println('"');
		}
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
							this.nameEnvironment.findTypes(CharOperation.concatWith(tokens, '.'), false, false, IJavaSearchConstants.TYPE, this);
							
							this.lookupEnvironment.buildTypeBindings(parsedUnit, null /*no access restriction*/);
							if ((this.unitScope = parsedUnit.scope) != null) {
								int tokenCount = tokens.length;
								char[] lastToken = tokens[tokenCount - 1];
								char[][] qualifierTokens = CharOperation.subarray(tokens, 0, tokenCount - 1);
								
								if(qualifierTokens != null && qualifierTokens.length > 0) {
									Binding binding = this.unitScope.getTypeOrPackage(qualifierTokens);
									if(binding != null && binding instanceof ReferenceBinding) {
										ReferenceBinding ref = (ReferenceBinding) binding;
										selectMemberTypeFromImport(parsedUnit, lastToken, ref, importReference.isStatic());
										if(importReference.isStatic()) {
											selectStaticFieldFromStaticImport(parsedUnit, lastToken, ref);
											selectStaticMethodFromStaticImport(parsedUnit, lastToken, ref);
										}
									}
								}
							}
							
							// accept qualified types only if no unqualified type was accepted
							if(!this.acceptedAnswer) {
								acceptQualifiedTypes();
								if (!this.acceptedAnswer) {
									this.nameEnvironment.findTypes(this.selectedIdentifier, false, false, IJavaSearchConstants.TYPE, this);
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
				if (parsedUnit.types != null || parsedUnit.isPackageInfo()) {
					if(selectDeclaration(parsedUnit))
						return;
					this.lookupEnvironment.buildTypeBindings(parsedUnit, null /*no access restriction*/);
					if ((this.unitScope = parsedUnit.scope)  != null) {
						try {
							this.lookupEnvironment.completeTypeBindings(parsedUnit, true);
							parsedUnit.scope.faultInTypes();
							ASTNode node = null;
							if (parsedUnit.types != null)
								node = parseBlockStatements(parsedUnit, selectionSourceStart);
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
				this.nameEnvironment.findTypes(this.selectedIdentifier, false, false, IJavaSearchConstants.TYPE, this);
				
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

	private void selectMemberTypeFromImport(CompilationUnitDeclaration parsedUnit, char[] lastToken, ReferenceBinding ref, boolean staticOnly) {
		int fieldLength = lastToken.length;
		ReferenceBinding[] memberTypes = ref.memberTypes();
		next : for (int j = 0; j < memberTypes.length; j++) {
			ReferenceBinding memberType = memberTypes[j];
			
			if (fieldLength > memberType.sourceName.length)
				continue next;

			if (staticOnly && !memberType.isStatic())
				continue next;

			if (!CharOperation.equals(lastToken, memberType.sourceName, true))
				continue next;
			
			this.selectFrom(memberType, parsedUnit, false);
		}
	}
	
	private void selectStaticFieldFromStaticImport(CompilationUnitDeclaration parsedUnit, char[] lastToken, ReferenceBinding ref) {
		int fieldLength = lastToken.length;
		FieldBinding[] fields = ref.availableFields();
		next : for (int j = 0; j < fields.length; j++) {
			FieldBinding field = fields[j];
			
			if (fieldLength > field.name.length)
				continue next;
			
			if (field.isSynthetic())
				continue next;

			if (!field.isStatic())
				continue next;

			if (!CharOperation.equals(lastToken, field.name, true))
				continue next;
			
			this.selectFrom(field, parsedUnit, false);
		}
	}
	
	private void selectStaticMethodFromStaticImport(CompilationUnitDeclaration parsedUnit, char[] lastToken, ReferenceBinding ref) {
		int methodLength = lastToken.length;
		MethodBinding[] methods = ref.availableMethods();
		next : for (int j = 0; j < methods.length; j++) {
			MethodBinding method = methods[j];
			
			if (method.isSynthetic()) continue next;

			if (method.isDefaultAbstract())	continue next;

			if (method.isConstructor()) continue next;

			if (!method.isStatic()) continue next;

			if (methodLength > method.selector.length)
				continue next;

			if (!CharOperation.equals(lastToken, method.selector, true))
				continue next;
			
			this.selectFrom(method, parsedUnit, false);
		}
	}

	private void selectFrom(Binding binding, CompilationUnitDeclaration parsedUnit, boolean isDeclaration) {
		if(binding instanceof TypeVariableBinding) {
			TypeVariableBinding typeVariableBinding = (TypeVariableBinding) binding;
			Binding enclosingElement = typeVariableBinding.declaringElement;
			this.noProposal = false;
			
			if(enclosingElement instanceof SourceTypeBinding) {
				SourceTypeBinding enclosingType = (SourceTypeBinding) enclosingElement;
				if (isLocal(enclosingType) && this.requestor instanceof SelectionRequestor) {
					((SelectionRequestor)this.requestor).acceptLocalTypeParameter(typeVariableBinding);
				} else {
					this.requestor.acceptTypeParameter(
						enclosingType.qualifiedPackageName(),
						enclosingType.qualifiedSourceName(),
						typeVariableBinding.sourceName(),
						false,
						this.actualSelectionStart,
						this.actualSelectionEnd);
				}
			} else if(enclosingElement instanceof MethodBinding) {
				MethodBinding enclosingMethod = (MethodBinding) enclosingElement;
				if (isLocal(enclosingMethod.declaringClass) && this.requestor instanceof SelectionRequestor) {
					((SelectionRequestor)this.requestor).acceptLocalMethodTypeParameter(typeVariableBinding);
				} else {
					this.requestor.acceptMethodTypeParameter(
						enclosingMethod.declaringClass.qualifiedPackageName(),
						enclosingMethod.declaringClass.qualifiedSourceName(),
						enclosingMethod.isConstructor()
								? enclosingMethod.declaringClass.sourceName()
								: enclosingMethod.selector,
						enclosingMethod.sourceStart(),
						enclosingMethod.sourceEnd(),
						typeVariableBinding.sourceName(),
						false,
						this.actualSelectionStart,
						this.actualSelectionEnd);
				}
			}
			this.acceptedAnswer = true;
		} else if (binding instanceof ReferenceBinding) {
			ReferenceBinding typeBinding = (ReferenceBinding) binding;
			if(typeBinding instanceof ProblemReferenceBinding) {
				TypeBinding closestMatch = typeBinding.closestMatch();
				if (closestMatch instanceof ReferenceBinding) {
					typeBinding = (ReferenceBinding) closestMatch;
				} else {
					typeBinding = null;
				}
			}
			if (typeBinding == null) return;
			if (isLocal(typeBinding) && this.requestor instanceof SelectionRequestor) {
				this.noProposal = false;
				((SelectionRequestor)this.requestor).acceptLocalType(typeBinding);
			} else {
				this.noProposal = false;

				this.requestor.acceptType(
					typeBinding.qualifiedPackageName(),
					typeBinding.qualifiedSourceName(),
					typeBinding.modifiers,
					false,
					typeBinding.computeUniqueKey(),
					this.actualSelectionStart,
					this.actualSelectionEnd);
			}
			this.acceptedAnswer = true;
		} else
			if (binding instanceof MethodBinding) {
				MethodBinding methodBinding = (MethodBinding) binding;
				this.noProposal = false;
				
				boolean isValuesOrValueOf = false;
				if(binding instanceof SyntheticMethodBinding) {
					SyntheticMethodBinding syntheticMethodBinding = (SyntheticMethodBinding) binding;
					if(syntheticMethodBinding.purpose  == SyntheticMethodBinding.EnumValues
 syntheticMethodBinding.purpose  == SyntheticMethodBinding.EnumValueOf) {
						isValuesOrValueOf =  true;
					}
				}
						
				if(!isValuesOrValueOf && !methodBinding.isSynthetic()) {
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
					
					TypeVariableBinding[] typeVariables = methodBinding.original().typeVariables;
					length = typeVariables == null ? 0 : typeVariables.length;
					char[][] typeParameterNames = new char[length][];
					char[][][] typeParameterBoundNames = new char[length][][];
					for (int i = 0; i < length; i++) {
						TypeVariableBinding typeVariable = typeVariables[i];
						typeParameterNames[i] = typeVariable.sourceName;
						if (typeVariable.firstBound == null) {
							typeParameterBoundNames[i] = new char[0][];
						} else if (typeVariable.firstBound == typeVariable.superclass) {
							int boundCount = 1 + (typeVariable.superInterfaces == null ? 0 : typeVariable.superInterfaces.length);
							typeParameterBoundNames[i] = new char[boundCount][];
							typeParameterBoundNames[i][0] = typeVariable.superclass.sourceName;
							for (int j = 1; j < boundCount; j++) {
								typeParameterBoundNames[i][j] = typeVariables[i].superInterfaces[j - 1].sourceName;
							}
						} else {
							int boundCount = typeVariable.superInterfaces == null ? 0 : typeVariable.superInterfaces.length;
							typeParameterBoundNames[i] = new char[boundCount][];
							for (int j = 0; j < boundCount; j++) {
								typeParameterBoundNames[i][j] = typeVariables[i].superInterfaces[j].sourceName;
							}
						}
					}
					
					ReferenceBinding declaringClass = methodBinding.declaringClass;
					if (isLocal(declaringClass) && this.requestor instanceof SelectionRequestor) {
						((SelectionRequestor)this.requestor).acceptLocalMethod(methodBinding);
					} else {
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
							typeParameterNames,
							typeParameterBoundNames,
							methodBinding.isConstructor(), 
							isDeclaration,
							methodBinding.computeUniqueKey(),
							this.actualSelectionStart,
							this.actualSelectionEnd);
					}
				}
				this.acceptedAnswer = true;
			} else
				if (binding instanceof FieldBinding) {
					FieldBinding fieldBinding = (FieldBinding) binding;
					ReferenceBinding declaringClass = fieldBinding.declaringClass;
					if (declaringClass != null) { // arraylength
						this.noProposal = false;
						if (isLocal(declaringClass) && this.requestor instanceof SelectionRequestor) {
							((SelectionRequestor)this.requestor).acceptLocalField(fieldBinding);
						} else {
							this.requestor.acceptField(
								declaringClass.qualifiedPackageName(),
								declaringClass.qualifiedSourceName(),
								fieldBinding.name,
								false,
								fieldBinding.computeUniqueKey(),
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
			public boolean visit(TypeParameter typeParameter, BlockScope scope) {
				if (typeParameter.name == assistIdentifier) {
					throw new SelectionNodeFound(typeParameter.binding);
				}
				return true;
			}
			public boolean visit(TypeParameter typeParameter, ClassScope scope) {
				if (typeParameter.name == assistIdentifier) {
					throw new SelectionNodeFound(typeParameter.binding);
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
	 * from the given context
	 *
	 *  @param typeName char[]
	 *      a type name which is to be resolved in the context of a compilation unit.
	 *		NOTE: the type name is supposed to be correctly reduced (no whitespaces, no unicodes left)
	 *
	 *  @param context org.eclipse.jdt.core.IType
	 *      the context in which code assist is invoked.
	 */
	public void selectType(char[] typeName, IType context) throws JavaModelException {
		try {
			this.acceptedAnswer = false;
			
			// only the type erasure are returned by IType.resolvedType(...)
			if (CharOperation.indexOf('<', typeName) != -1) {
				char[] typeSig = Signature.createCharArrayTypeSignature(typeName, false/*not resolved*/);
				typeSig = Signature.getTypeErasure(typeSig);
				typeName = Signature.toCharArray(typeSig);
			}

			// find the outer most type
			IType outerType = context;
			IType parent = context.getDeclaringType();
			while (parent != null) {
				outerType = parent;
				parent = parent.getDeclaringType();
			}
			
			// compute parse tree for this most outer type
			CompilationUnitDeclaration parsedUnit = null;
			TypeDeclaration typeDeclaration = null;
			org.eclipse.jdt.core.ICompilationUnit cu = context.getCompilationUnit();
			if (cu != null) {
			 	IType[] topLevelTypes = cu.getTypes();
			 	int length = topLevelTypes.length;
			 	SourceTypeElementInfo[] topLevelInfos = new SourceTypeElementInfo[length];
			 	for (int i = 0; i < length; i++) {
					topLevelInfos[i] = (SourceTypeElementInfo) ((SourceType)topLevelTypes[i]).getElementInfo();
				}
				ISourceType outerTypeInfo = (ISourceType) ((SourceType) outerType).getElementInfo();
				CompilationResult result = new CompilationResult(outerTypeInfo.getFileName(), 1, 1, this.compilerOptions.maxProblemsPerUnit);
				int flags = SourceTypeConverter.FIELD_AND_METHOD | SourceTypeConverter.MEMBER_TYPE;
				if (context.isAnonymous() || context.isLocal()) 
					flags |= SourceTypeConverter.LOCAL_TYPE;
				parsedUnit =
					SourceTypeConverter.buildCompilationUnit(
							topLevelInfos,
							flags,
							this.parser.problemReporter(), 
							result);
				if (parsedUnit != null && parsedUnit.types != null) {
					if(DEBUG) {
						System.out.println("SELECTION - Diet AST :"); //$NON-NLS-1$
						System.out.println(parsedUnit.toString());
					}
					// find the type declaration that corresponds to the original source type
					typeDeclaration = new ASTNodeFinder(parsedUnit).findType(context);
				} 
			} else { // binary type
				ClassFile classFile = (ClassFile) context.getClassFile();
				ClassFileReader reader = (ClassFileReader) classFile.getBinaryTypeInfo((IFile) classFile.getResource(), false/*don't fully initialize so as to keep constant pool (used below)*/);
				CompilationResult result = new CompilationResult(reader.getFileName(), 1, 1, this.compilerOptions.maxProblemsPerUnit);
				parsedUnit = new CompilationUnitDeclaration(this.parser.problemReporter(), result, 0);
				HashSetOfCharArrayArray typeNames = new HashSetOfCharArrayArray();
				typeDeclaration = BinaryTypeConverter.buildTypeDeclaration(context, parsedUnit, result, typeNames);
				parsedUnit.imports = BinaryTypeConverter.buildImports(typeNames, reader);
			}

			if (typeDeclaration != null) {

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
				typeDeclaration.fields = new FieldDeclaration[] { field };

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
		char[] packageName = currentPackage == null ? CharOperation.NO_CHAR : CharOperation.concatWith(currentPackage.tokens, '.');
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
			char[] uniqueKey = typeDeclaration.binding != null ? typeDeclaration.binding.computeUniqueKey() : null;
			
			this.requestor.acceptType(
				packageName,
				qualifiedSourceName,
				typeDeclaration.modifiers,
				true,
				uniqueKey,
				this.actualSelectionStart,
				this.actualSelectionEnd);
			
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
				FieldDeclaration field = fields[i];
				this.requestor.acceptField(
					packageName,
					qualifiedSourceName,
					field.name,
					true,
					field.binding != null ? field.binding.computeUniqueKey() : null,
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
					null, // SelectionRequestor does not need of type parameters name for method declaration
					null, // SelectionRequestor does not need of type parameters bounds for method declaration
					method.isConstructor(),
					true,
					method.binding != null ? method.binding.computeUniqueKey() : null,
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8415.java