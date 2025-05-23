error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5047.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5047.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5047.java
text:
```scala
s@@canner.setSource(source);

package org.eclipse.jdt.internal.codeassist;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.util.*;

import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.codeassist.impl.*;
import org.eclipse.jdt.internal.codeassist.select.*;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.*;
import org.eclipse.jdt.internal.compiler.problem.*;
import org.eclipse.jdt.internal.compiler.util.*;
import org.eclipse.jdt.internal.compiler.impl.*;

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
	private char[] qualifiedSelection;
	private char[] selectedIdentifier;
	
	private char[][][] acceptedClasses;
	private char[][][] acceptedInterfaces;
	int acceptedClassesCount;
	int acceptedInterfacesCount;

	/**
	 * The SelectionEngine is responsible for computing the selected object.
	 *
	 * It requires a searchable name environment, which supports some
	 * specific search APIs, and a requestor to feed back the results to a UI.
	 *
	 *  @param nameEnvironment org.eclipse.jdt.internal.codeassist.ISearchableNameEnvironment
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
		ISearchableNameEnvironment nameEnvironment,
		ISelectionRequestor requestor,
		Map settings) {
		this.requestor = requestor;
		this.nameEnvironment = nameEnvironment;

		CompilerOptions options = new CompilerOptions(settings);
		ProblemReporter problemReporter =
			new ProblemReporter(
				DefaultErrorHandlingPolicies.proceedWithAllProblems(),
				options,
				new DefaultProblemFactory(Locale.getDefault())) {
			public void record(IProblem problem, CompilationResult unitResult) {
				unitResult.record(problem);
				SelectionEngine.this.requestor.acceptError(problem);
			}
		};
		this.parser = new SelectionParser(problemReporter, options.assertMode);
		this.lookupEnvironment =
			new LookupEnvironment(this, options, problemReporter, nameEnvironment);
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
	public void acceptClass(char[] packageName, char[] className, int modifiers) {
		if (CharOperation.equals(className, selectedIdentifier)) {
			if (qualifiedSelection != null
				&& !CharOperation.equals(
					qualifiedSelection,
					CharOperation.concat(packageName, className, '.'))) {
				return;
			}
			
			if(mustQualifyType(packageName, className)) {
				char[][] acceptedClass = new char[2][];
				acceptedClass[0] = packageName;
				acceptedClass[1] = className;
				
				if(acceptedClasses == null) {
					acceptedClasses = new char[10][][];
					acceptedClassesCount = 0;
				}
				int length = acceptedClasses.length;
				if(length == acceptedClassesCount) {
					System.arraycopy(acceptedClasses, 0, acceptedClasses = new char[(length + 1)* 2][][], 0, length);
				}
				acceptedClasses[acceptedClassesCount++] = acceptedClass;
				
			} else {
				requestor.acceptClass(
					packageName,
					className,
					false);
				acceptedAnswer = true;
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
		int modifiers) {

		if (CharOperation.equals(interfaceName, selectedIdentifier)) {
			if (qualifiedSelection != null
				&& !CharOperation.equals(
					qualifiedSelection,
					CharOperation.concat(packageName, interfaceName, '.'))) {
				return;
			}
			
			if(mustQualifyType(packageName, interfaceName)) {
				char[][] acceptedInterface= new char[2][];
				acceptedInterface[0] = packageName;
				acceptedInterface[1] = interfaceName;
				
				if(acceptedInterfaces == null) {
					acceptedInterfaces = new char[10][][];
					acceptedInterfacesCount = 0;
				}
				int length = acceptedInterfaces.length;
				if(length == acceptedInterfacesCount) {
					System.arraycopy(acceptedInterfaces, 0, acceptedInterfaces = new char[(length + 1) * 2][][], 0, length);
				}
				acceptedInterfaces[acceptedInterfacesCount++] = acceptedInterface;
				
			} else {
				requestor.acceptInterface(
					packageName,
					interfaceName,
					false);
				acceptedAnswer = true;
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
	}

	private void acceptQualifiedTypes() {
		if(acceptedClasses != null){
			acceptedAnswer = true;
			for (int i = 0; i < acceptedClassesCount; i++) {
				requestor.acceptClass(
					acceptedClasses[i][0],
					acceptedClasses[i][1],
					true);
			}
			acceptedClasses = null;
			acceptedClassesCount = 0;
		}
		if(acceptedInterfaces != null){
			acceptedAnswer = true;
			for (int i = 0; i < acceptedInterfacesCount; i++) {
				requestor.acceptInterface(
					acceptedInterfaces[i][0],
					acceptedInterfaces[i][1],
					true);
			}
			acceptedInterfaces = null;
			acceptedInterfacesCount = 0;
		}
	}
	
	/**
	 * One result of the search consists of a new type.
	 * @param packageName char[]
	 * @param typeName char[]
	 * 
	 * NOTE - All package and type names are presented in their readable form:
	 *    Package names are in the form "a.b.c".
	 *    Nested type names are in the qualified form "A.M".
	 *    The default package is represented by an empty array.
	 */
	public void acceptType(char[] packageName, char[] typeName) {
		acceptClass(packageName, typeName, 0);
	}

	private boolean checkSelection(
		char[] source,
		int selectionStart,
		int selectionEnd) {

		Scanner scanner = new Scanner();
		scanner.setSourceBuffer(source);
		
		int lastIdentifierStart = -1;
		int lastIdentifierEnd = -1;
		char[] lastIdentifier = null;
		int token, identCount = 0;
		StringBuffer entireSelection = new StringBuffer(selectionEnd - selectionStart + 1);
		
		if(selectionStart > selectionEnd){
			
			// compute start position of current line
			int currentPosition = selectionStart - 1;
			int nextCharacterPosition = selectionStart;
			char currentCharacter = ' ';
			try {
				while(currentPosition > 0 || currentCharacter == '\r' || currentCharacter == '\n'){
					
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
			}
			catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
			
			// compute start and end of the last token
			scanner.resetTo(nextCharacterPosition, selectionEnd);
			do {
				try {
					token = scanner.getNextToken();
				} catch (InvalidInputException e) {
					return false;
				}
				if((token == ITerminalSymbols.TokenNamethis ||
					token == ITerminalSymbols.TokenNamesuper ||
					token == ITerminalSymbols.TokenNameIdentifier) &&
					scanner.startPosition <= selectionStart &&
					selectionStart <= scanner.currentPosition) {
					lastIdentifierStart = scanner.startPosition;
					lastIdentifierEnd = scanner.currentPosition - 1;
					lastIdentifier = scanner.getCurrentTokenSource();
				}
			} while (token != ITerminalSymbols.TokenNameEOF);
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
					case ITerminalSymbols.TokenNamethis :
					case ITerminalSymbols.TokenNamesuper :
					case ITerminalSymbols.TokenNameIdentifier :
						if (!expectingIdentifier)
							return false;
						lastIdentifier = scanner.getCurrentTokenSource();
						lastIdentifierStart = scanner.startPosition;
						lastIdentifierEnd = scanner.currentPosition - 1;
						if(lastIdentifierEnd > selectionEnd) {
							lastIdentifierEnd = selectionEnd;
							lastIdentifier = CharOperation.subarray(lastIdentifier, 0,lastIdentifierEnd - lastIdentifierStart + 1);
						}
						entireSelection.append(lastIdentifier);
							
						identCount++;
						expectingIdentifier = false;
						break;
					case ITerminalSymbols.TokenNameDOT :
						if (expectingIdentifier)
							return false;
						entireSelection.append('.');
						expectingIdentifier = true;
						break;
					case ITerminalSymbols.TokenNameEOF :
						if (expectingIdentifier)
							return false;
						break;
					default :
						return false;
				}
			} while (token != ITerminalSymbols.TokenNameEOF);
		}
		if (lastIdentifierStart > 0) {
			actualSelectionStart = lastIdentifierStart;
			actualSelectionEnd = lastIdentifierEnd;
			selectedIdentifier = lastIdentifier;
			if (identCount > 1)
				qualifiedSelection = entireSelection.toString().toCharArray();
			return true;
		}
		return false;
	}

	public AssistParser getParser() {
		return parser;
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
			acceptedAnswer = false;
			CompilationResult result = new CompilationResult(sourceUnit, 1, 1);
			CompilationUnitDeclaration parsedUnit =
				parser.dietParse(sourceUnit, result, actualSelectionStart, actualSelectionEnd);

			if (parsedUnit != null) {
				if(DEBUG) {
					System.out.println("SELECTION - Diet AST :"); //$NON-NLS-1$
					System.out.println(parsedUnit.toString());
				}
				
				// scan the package & import statements first
				if (parsedUnit.currentPackage instanceof SelectionOnPackageReference) {
					char[][] tokens =
						((SelectionOnPackageReference) parsedUnit.currentPackage).tokens;
					requestor.acceptPackage(CharOperation.concatWith(tokens, '.'));
					return;
				}
				ImportReference[] imports = parsedUnit.imports;
				if (imports != null) {
					for (int i = 0, length = imports.length; i < length; i++) {
						ImportReference importReference = imports[i];
						if (importReference instanceof SelectionOnImportReference) {
							char[][] tokens = ((SelectionOnImportReference) importReference).tokens;
							requestor.acceptPackage(CharOperation.concatWith(tokens, '.'));
							nameEnvironment.findTypes(CharOperation.concatWith(tokens, '.'), this);
							// accept qualified types only if no unqualified type was accepted
							if(!acceptedAnswer) {
								acceptQualifiedTypes();
								if (!acceptedAnswer) {
									nameEnvironment.findTypes(selectedIdentifier, this);
									// try with simple type name
									if(!acceptedAnswer) {
										acceptQualifiedTypes();
									}
								}
							}
							return;
						}
					}
				}
				if (parsedUnit.types != null) {
					lookupEnvironment.buildTypeBindings(parsedUnit);
					if ((this.unitScope = parsedUnit.scope)  != null) {
						try {
							lookupEnvironment.completeTypeBindings(parsedUnit, true);
							parsedUnit.scope.faultInTypes();
							selectDeclaration(parsedUnit);
							parseMethod(parsedUnit, selectionSourceStart);
							if(DEBUG) {
								System.out.println("SELECTION - AST :"); //$NON-NLS-1$
								System.out.println(parsedUnit.toString());
							}
							parsedUnit.resolve();
						} catch (SelectionNodeFound e) {
							if (e.binding != null) {
								if(DEBUG) {
									System.out.println("SELECTION - Selection binding:"); //$NON-NLS-1$
									System.out.println(e.binding.toString());
								}
								// if null then we found a problem in the selection node
								selectFrom(e.binding);
							}
						}
					}
				}
			}
			// only reaches here if no selection could be derived from the parsed tree
			// thus use the selected source and perform a textual type search
			if (!acceptedAnswer) {
				nameEnvironment.findTypes(selectedIdentifier, this);
				
				// accept qualified types only if no unqualified type was accepted
				if(!acceptedAnswer) {
					acceptQualifiedTypes();
				}
			}
		} catch (IndexOutOfBoundsException e) { // work-around internal failure - 1GEMF6D		
		} catch (AbortCompilation e) { // ignore this exception for now since it typically means we cannot find java.lang.Object
		} finally {
			reset();
		}
	}

	private void selectFrom(Binding binding) {
		if (binding instanceof ReferenceBinding) {
			ReferenceBinding typeBinding = (ReferenceBinding) binding;
			if (qualifiedSelection != null
				&& !CharOperation.equals(qualifiedSelection, typeBinding.readableName())) {
				return;
			}
			if (typeBinding.isInterface()) {
				requestor.acceptInterface(
					typeBinding.qualifiedPackageName(),
					typeBinding.qualifiedSourceName(),
					false);
			} else if(typeBinding instanceof ProblemReferenceBinding){
				ProblemReferenceBinding problemBinding = (ProblemReferenceBinding)typeBinding;
				if(problemBinding.original == null
 !(problemBinding.original instanceof ReferenceBinding)) {
					return;
				}
				ReferenceBinding original = (ReferenceBinding) problemBinding.original;

				requestor.acceptClass(
					original.qualifiedPackageName(),
					original.qualifiedSourceName(),
					false);
			} else {
				requestor.acceptClass(
					typeBinding.qualifiedPackageName(),
					typeBinding.qualifiedSourceName(),
					false);
			}
			acceptedAnswer = true;
		} else
			if (binding instanceof MethodBinding) {
				MethodBinding methodBinding = (MethodBinding) binding;
				TypeBinding[] parameterTypes = methodBinding.parameters;
				int length = parameterTypes.length;
				char[][] parameterPackageNames = new char[length][];
				char[][] parameterTypeNames = new char[length][];
				for (int i = 0; i < length; i++) {
					parameterPackageNames[i] = parameterTypes[i].qualifiedPackageName();
					parameterTypeNames[i] = parameterTypes[i].qualifiedSourceName();
				}
				requestor.acceptMethod(
					methodBinding.declaringClass.qualifiedPackageName(),
					methodBinding.declaringClass.qualifiedSourceName(),
					methodBinding.isConstructor()
						? methodBinding.declaringClass.sourceName()
						: methodBinding.selector,
					parameterPackageNames,
					parameterTypeNames,
					methodBinding.isConstructor());
				acceptedAnswer = true;
			} else
				if (binding instanceof FieldBinding) {
					FieldBinding fieldBinding = (FieldBinding) binding;
					if (fieldBinding.declaringClass != null) { // arraylength
						requestor.acceptField(
							fieldBinding.declaringClass.qualifiedPackageName(),
							fieldBinding.declaringClass.qualifiedSourceName(),
							fieldBinding.name);
						acceptedAnswer = true;
					}
				} else
					if (binding instanceof LocalVariableBinding) {
						selectFrom(((LocalVariableBinding) binding).type);
						// open on the type of the variable
					} else
						if (binding instanceof ArrayBinding) {
							selectFrom(((ArrayBinding) binding).leafComponentType);
							// open on the type of the array
						} else
							if (binding instanceof PackageBinding) {
								PackageBinding packageBinding = (PackageBinding) binding;
								requestor.acceptPackage(packageBinding.readableName());
								acceptedAnswer = true;
							} else
								if(binding instanceof BaseTypeBinding) {
									acceptedAnswer = true;
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
	 */
	public void selectType(ISourceType sourceType, char[] typeName) {
		try {
			acceptedAnswer = false;

			// find the outer most type
			ISourceType outerType = sourceType;
			ISourceType parent = sourceType.getEnclosingType();
			while (parent != null) {
				outerType = parent;
				parent = parent.getEnclosingType();
			}
			// compute parse tree for this most outer type
			CompilationResult result = new CompilationResult(outerType.getFileName(), 1, 1);
			CompilationUnitDeclaration parsedUnit =
				SourceTypeConverter
					.buildCompilationUnit(
						new ISourceType[] { outerType },
						false,
			// don't need field and methods
			true, // by default get member types
			this.parser.problemReporter(), result);

			if (parsedUnit != null && parsedUnit.types != null) {
				if(DEBUG) {
					System.out.println("SELECTION - Diet AST :"); //$NON-NLS-1$
					System.out.println(parsedUnit.toString());
				}
				// find the type declaration that corresponds to the original source type
				char[] packageName = sourceType.getPackageName();
				char[] sourceTypeName = sourceType.getQualifiedName();
				// the fully qualified name without the package name
				if (packageName != null) {
					// remove the package name if necessary
					sourceTypeName =
						CharOperation.subarray(
							sourceType.getQualifiedName(),
							packageName.length + 1,
							sourceTypeName.length);
				};
				TypeDeclaration typeDecl =
					parsedUnit.declarationOfType(CharOperation.splitOn('.', sourceTypeName));
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
						qualifiedSelection = typeName;
						char[][] previousIdentifiers = CharOperation.splitOn('.', typeName, 0, dot - 1);
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
					lookupEnvironment.buildTypeBindings(parsedUnit);
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
								selectFrom(e.binding);
							}
						}
					}
				}
			}
			// only reaches here if no selection could be derived from the parsed tree
			// thus use the selected source and perform a textual type search
			if (!acceptedAnswer) {
				if (this.selectedIdentifier != null) {
					nameEnvironment.findTypes(typeName, this);
					
					// accept qualified types only if no unqualified type was accepted
					if(!acceptedAnswer) {
						acceptQualifiedTypes();
					}
				}
			}
		} catch (AbortCompilation e) { // ignore this exception for now since it typically means we cannot find java.lang.Object
		} finally {
			qualifiedSelection = null;
			reset();
		}
	}

	// Check if a declaration got selected in this unit
	private void selectDeclaration(CompilationUnitDeclaration compilationUnit){

		// the selected identifier is not identical to the parser one (equals but not identical),
		// for traversing the parse tree, the parser assist identifier is necessary for identitiy checks
		char[] assistIdentifier = this.getParser().assistIdentifier();
		if (assistIdentifier == null) return;
		
		// iterate over the types
		TypeDeclaration[] types = compilationUnit.types;
		for (int i = 0, length = types == null ? 0 : types.length; i < length; i++){
			selectDeclaration(types[i], assistIdentifier);
		}
	}

	// Check if a declaration got selected in this type
	private void selectDeclaration(TypeDeclaration typeDeclaration, char[] assistIdentifier){

		if (typeDeclaration.name == assistIdentifier){
			throw new SelectionNodeFound(typeDeclaration.binding);
		}
		TypeDeclaration[] memberTypes = typeDeclaration.memberTypes;
		for (int i = 0, length = memberTypes == null ? 0 : memberTypes.length; i < length; i++){
			selectDeclaration(memberTypes[i], assistIdentifier);
		}
		FieldDeclaration[] fields = typeDeclaration.fields;
		for (int i = 0, length = fields == null ? 0 : fields.length; i < length; i++){
			if (fields[i].name == assistIdentifier){
				throw new SelectionNodeFound(fields[i].binding);
			}
		}
		AbstractMethodDeclaration[] methods = typeDeclaration.methods;
		for (int i = 0, length = methods == null ? 0 : methods.length; i < length; i++){
			AbstractMethodDeclaration method = methods[i];
			if (method.selector == assistIdentifier){
				if(method.binding != null) {
					throw new SelectionNodeFound(method.binding);
				} else {
					if(method.scope != null) {
						throw new SelectionNodeFound(new MethodBinding(method.modifiers, method.selector, null, null, null, method.scope.referenceType().binding));
					}
				}
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5047.java