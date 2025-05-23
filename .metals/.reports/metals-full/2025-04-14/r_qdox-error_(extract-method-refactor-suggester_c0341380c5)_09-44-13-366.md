error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5580.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5580.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5580.java
text:
```scala
i@@f (this.parser.javadocParser.checkDocComment) {

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
package org.eclipse.jdt.internal.core.search.matching;

import java.io.IOException;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.compiler.*;
import org.eclipse.jdt.core.search.*;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException;
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.impl.ITypeRequestor;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.parser.*;
import org.eclipse.jdt.internal.compiler.problem.*;
import org.eclipse.jdt.internal.compiler.util.HashtableOfIntValues;
import org.eclipse.jdt.internal.compiler.util.SuffixConstants;
import org.eclipse.jdt.internal.core.*;
import org.eclipse.jdt.internal.core.hierarchy.HierarchyResolver;
import org.eclipse.jdt.internal.core.search.HierarchyScope;
import org.eclipse.jdt.internal.core.search.JavaSearchParticipant;
import org.eclipse.jdt.internal.core.util.HandleFactory;
import org.eclipse.jdt.internal.core.util.SimpleSet;
import org.eclipse.jdt.internal.core.util.Util;

public class MatchLocator implements ITypeRequestor {

public static final int MAX_AT_ONCE = 500;

// permanent state
public SearchPattern pattern;
public PatternLocator patternLocator;
public int matchContainer;
public SearchRequestor requestor;
public IJavaSearchScope scope;
public IProgressMonitor progressMonitor;

public org.eclipse.jdt.core.ICompilationUnit[] workingCopies;
public HandleFactory handleFactory;

// cache of all super type names if scope is hierarchy scope
public char[][][] allSuperTypeNames;

// the following is valid for the current project
public MatchLocatorParser parser;
private Parser basicParser;
public INameEnvironment nameEnvironment;
public NameLookup nameLookup;
public LookupEnvironment lookupEnvironment;
public HierarchyResolver hierarchyResolver;

public CompilerOptions options;

// management of PossibleMatch to be processed
public int numberOfMatches; // (numberOfMatches - 1) is the last unit in matchesToProcess
public PossibleMatch[] matchesToProcess;
public PossibleMatch currentPossibleMatch;

/*
 * Time spent in the IJavaSearchResultCollector
 */
public long resultCollectorTime = 0;

/**
 * An ast visitor that visits local type declarations.
 */
public class LocalDeclarationVisitor extends ASTVisitor {
	IJavaElement enclosingElement;
	MatchingNodeSet nodeSet;
	HashtableOfIntValues occurrencesCounts = new HashtableOfIntValues(); // key = class name (char[]), value = occurrenceCount (int)
	public LocalDeclarationVisitor(IJavaElement enclosingElement, MatchingNodeSet nodeSet) {
		this.enclosingElement = enclosingElement;
		this.nodeSet = nodeSet;
	}
	public boolean visit(TypeDeclaration typeDeclaration, BlockScope unused) {
		try {
			char[] simpleName;
			if ((typeDeclaration.bits & ASTNode.IsAnonymousTypeMASK) != 0) {				
				simpleName = CharOperation.NO_CHAR;
			} else {
				simpleName = typeDeclaration.name;
			}
			int occurrenceCount = occurrencesCounts.get(simpleName);
			if (occurrenceCount == HashtableOfIntValues.NO_VALUE) {
				occurrenceCount = 1;
			} else {
				occurrenceCount = occurrenceCount + 1;
			}
			occurrencesCounts.put(simpleName, occurrenceCount);
			if ((typeDeclaration.bits & ASTNode.IsAnonymousTypeMASK) != 0) {				
				reportMatching(typeDeclaration, enclosingElement, -1, nodeSet, occurrenceCount);
			} else {
				Integer level = (Integer) nodeSet.matchingNodes.removeKey(typeDeclaration);
				reportMatching(typeDeclaration, enclosingElement, level != null ? level.intValue() : -1, nodeSet, occurrenceCount);
			}
			return false; // don't visit members as this was done during reportMatching(...)
		} catch (CoreException e) {
			throw new WrappedCoreException(e);
		}
	}
}

public class WrappedCoreException extends RuntimeException {
	public CoreException coreException;
	public WrappedCoreException(CoreException coreException) {
		this.coreException = coreException;
	}
}

public static ClassFileReader classFileReader(IType type) {
	IClassFile classFile = type.getClassFile(); 
	JavaModelManager manager = JavaModelManager.getJavaModelManager();
	if (classFile.isOpen())
		return (ClassFileReader) manager.getInfo(type);

	IPackageFragment pkg = type.getPackageFragment();
	IPackageFragmentRoot root = (IPackageFragmentRoot) pkg.getParent();
	try {
		if (!root.isArchive())
			return ClassFileReader.read(type.getPath().toOSString());

		IPath zipPath = root.isExternal() ? root.getPath() : root.getResource().getLocation();
		if (zipPath == null) return null; // location is null
		ZipFile zipFile = null;
		try {
			if (JavaModelManager.ZIP_ACCESS_VERBOSE)
				System.out.println("(" + Thread.currentThread() + ") [MatchLocator.classFileReader()] Creating ZipFile on " + zipPath); //$NON-NLS-1$	//$NON-NLS-2$
			zipFile = manager.getZipFile(zipPath);
			char[] pkgPath = pkg.getElementName().toCharArray();
			CharOperation.replace(pkgPath, '.', '/');
			char[] classFileName = classFile.getElementName().toCharArray();
			char[] path = pkgPath.length == 0 ? classFileName : CharOperation.concat(pkgPath, classFileName, '/');
			return ClassFileReader.read(zipFile, new String(path));
		} finally {
			manager.closeZipFile(zipFile);
		}
	} catch (ClassFormatException e) {
		// invalid class file: return null
	} catch (CoreException e) {
		// cannot read class file: return null
	} catch (IOException e) {
		// cannot read class file: return null
	}
	return null;
}

public MatchLocator(
	SearchPattern pattern,
	SearchRequestor requestor,
	IJavaSearchScope scope,
	org.eclipse.jdt.core.ICompilationUnit[] workingCopies,
	IProgressMonitor progressMonitor) {
		
	this.pattern = pattern;
	this.patternLocator = PatternLocator.patternLocator(this.pattern);
	this.matchContainer = this.patternLocator.matchContainer();
	this.requestor = requestor;
	this.scope = scope;
	this.workingCopies = workingCopies;
	this.progressMonitor = progressMonitor;
}
/**
 * Add an additional binary type
 */
public void accept(IBinaryType binaryType, PackageBinding packageBinding) {
	this.lookupEnvironment.createBinaryTypeFrom(binaryType, packageBinding);
}
/**
 * Add an additional compilation unit into the loop
 *  ->  build compilation unit declarations, their bindings and record their results.
 */
public void accept(ICompilationUnit sourceUnit) {
	// Switch the current policy and compilation result for this unit to the requested one.
	CompilationResult unitResult = new CompilationResult(sourceUnit, 1, 1, this.options.maxProblemsPerUnit);
	try {
		CompilationUnitDeclaration parsedUnit = basicParser().dietParse(sourceUnit, unitResult);
		lookupEnvironment.buildTypeBindings(parsedUnit);
		lookupEnvironment.completeTypeBindings(parsedUnit, true);
	} catch (AbortCompilationUnit e) {
		// at this point, currentCompilationUnitResult may not be sourceUnit, but some other
		// one requested further along to resolve sourceUnit.
		if (unitResult.compilationUnit == sourceUnit) { // only report once
			//requestor.acceptResult(unitResult.tagAsAccepted());
		} else {
			throw e; // want to abort enclosing request to compile
		}
	}
}
/**
 * Add additional source types
 */
public void accept(ISourceType[] sourceTypes, PackageBinding packageBinding) {
	// case of SearchableEnvironment of an IJavaProject is used
	ISourceType sourceType = sourceTypes[0];
	while (sourceType.getEnclosingType() != null)
		sourceType = sourceType.getEnclosingType();
	if (sourceType instanceof SourceTypeElementInfo) {
		// get source
		SourceTypeElementInfo elementInfo = (SourceTypeElementInfo) sourceType;
		IType type = elementInfo.getHandle();
		ICompilationUnit sourceUnit = (ICompilationUnit) type.getCompilationUnit();
		accept(sourceUnit);
	} else {
		CompilationResult result = new CompilationResult(sourceType.getFileName(), 1, 1, 0);
		CompilationUnitDeclaration unit =
			SourceTypeConverter.buildCompilationUnit(
				sourceTypes,
				SourceTypeConverter.FIELD_AND_METHOD // need field and methods
 SourceTypeConverter.MEMBER_TYPE, // need member types
				// no need for field initialization
				lookupEnvironment.problemReporter,
				result);
		this.lookupEnvironment.buildTypeBindings(unit);
		this.lookupEnvironment.completeTypeBindings(unit, true);
	}
}	
protected Parser basicParser() {
	if (this.basicParser == null) {
		ProblemReporter problemReporter =
			new ProblemReporter(
				DefaultErrorHandlingPolicies.proceedWithAllProblems(),
				this.options,
				new DefaultProblemFactory());
		this.basicParser = new Parser(problemReporter, false);
	}
	return this.basicParser;
}
/**
 * Add the possibleMatch to the loop
 *  ->  build compilation unit declarations, their bindings and record their results.
 */
protected void buildBindings(PossibleMatch possibleMatch) {
	if (this.progressMonitor != null && this.progressMonitor.isCanceled())
		throw new OperationCanceledException();

	try {
		if (SearchEngine.VERBOSE)
			System.out.println("Parsing " + possibleMatch.openable.toStringWithAncestors()); //$NON-NLS-1$

		this.parser.nodeSet = possibleMatch.nodeSet;
		CompilationResult unitResult = new CompilationResult(possibleMatch, 1, 1, this.options.maxProblemsPerUnit);
		CompilationUnitDeclaration parsedUnit = this.parser.dietParse(possibleMatch, unitResult);
		if (parsedUnit != null) {
			if (!parsedUnit.isEmpty())
				this.lookupEnvironment.buildTypeBindings(parsedUnit);

			// add the possibleMatch with its parsedUnit to matchesToProcess
			possibleMatch.parsedUnit = parsedUnit;
			int size = this.matchesToProcess.length;
			if (this.numberOfMatches == size)
				System.arraycopy(this.matchesToProcess, 0, this.matchesToProcess = new PossibleMatch[size == 0 ? 1 : size * 2], 0, this.numberOfMatches);
			this.matchesToProcess[this.numberOfMatches++] = possibleMatch;

			if (this.progressMonitor != null)
				this.progressMonitor.worked(4);
		}
	} finally {
		this.parser.nodeSet = null;
	}
}
/*
 * Caches the given binary type in the lookup environment and returns it.
 * Returns the existing one if already cached.
 * Returns null if source type binding was cached.
 */
protected BinaryTypeBinding cacheBinaryType(IType type) throws JavaModelException {
	IType enclosingType = type.getDeclaringType();
	if (enclosingType != null)
		cacheBinaryType(enclosingType); // cache enclosing types first, so that binary type can be found in lookup enviroment
	IBinaryType binaryType = (IBinaryType) ((BinaryType) type).getElementInfo();
	BinaryTypeBinding binding = this.lookupEnvironment.cacheBinaryType(binaryType);
	if (binding == null) { // it was already cached as a result of a previous query
		char[][] compoundName = CharOperation.splitOn('.', type.getFullyQualifiedName().toCharArray());
		ReferenceBinding referenceBinding = this.lookupEnvironment.getCachedType(compoundName);
		if (referenceBinding != null && (referenceBinding instanceof BinaryTypeBinding))
			binding = (BinaryTypeBinding) referenceBinding; // if the binding could be found and if it comes from a binary type
	}
	return binding;
}
/*
 * Computes the super type names of the focus type if any.
 */
protected char[][][] computeSuperTypeNames(IType focusType) {
	String fullyQualifiedName = focusType.getFullyQualifiedName();
	int lastDot = fullyQualifiedName.lastIndexOf('.');
	char[] qualification = lastDot == -1 ? CharOperation.NO_CHAR : fullyQualifiedName.substring(0, lastDot).toCharArray();
	char[] simpleName = focusType.getElementName().toCharArray();

	SuperTypeNamesCollector superTypeNamesCollector = 
		new SuperTypeNamesCollector(
			this.pattern, 
			simpleName,
			qualification,
			new MatchLocator(this.pattern, this.requestor, this.scope, this.workingCopies, this.progressMonitor), // clone MatchLocator so that it has no side effect
			focusType, 
			this.progressMonitor);
	try {
		this.allSuperTypeNames = superTypeNamesCollector.collect();
	} catch (JavaModelException e) {
		// problem collecting super type names: leave it null
	}
	return this.allSuperTypeNames;
}
/**
 * Creates an IMethod from the given method declaration and type. 
 */
protected IJavaElement createHandle(AbstractMethodDeclaration method, IJavaElement parent) {
	if (!(parent instanceof IType)) return parent;

	IType type = (IType) parent;
	Argument[] arguments = method.arguments;
	int argCount = arguments == null ? 0 : arguments.length;
	if (type.isBinary()) {
		// don't cache the methods of the binary type
		// fall thru if its a constructor with a synthetic argument... find it the slower way
		ClassFileReader reader = classFileReader(type);
		if (reader != null) {
			IBinaryMethod[] methods = reader.getMethods();
			if (methods != null) {
				boolean firstIsSynthetic = false;
				try {
					if (type.isMember() && method.isConstructor() && !Flags.isStatic(type.getFlags())) { // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=48261
						firstIsSynthetic = true;
						argCount++;
					}
				} catch (JavaModelException e) {
					// ignored
				}
				nextMethod : for (int i = 0, methodsLength = methods.length; i < methodsLength; i++) {
					IBinaryMethod binaryMethod = methods[i];
					char[] selector = binaryMethod.isConstructor() ? type.getElementName().toCharArray() : binaryMethod.getSelector();
					if (CharOperation.equals(selector, method.selector)) {
						char[][] parameterTypes = Signature.getParameterTypes(binaryMethod.getMethodDescriptor());
						if (argCount != parameterTypes.length) continue nextMethod;
						for (int j = 0; j < argCount; j++) {
							char[] typeName;
							if (j == 0 && firstIsSynthetic) {
								typeName = type.getDeclaringType().getFullyQualifiedName().toCharArray();
							} else {
								TypeReference typeRef = arguments[firstIsSynthetic ? j - 1 : j].type;
								typeName = CharOperation.concatWith(typeRef.getTypeName(), '.');
								for (int k = 0, dim = typeRef.dimensions(); k < dim; k++)
									typeName = CharOperation.concat(typeName, new char[] {'[', ']'});
							}
							char[] parameterTypeName = ClassFileMatchLocator.convertClassFileFormat(parameterTypes[j]);
							if (!CharOperation.endsWith(Signature.toCharArray(parameterTypeName), typeName))
								continue nextMethod;
							parameterTypes[j] = parameterTypeName;
						}
						return type.getMethod(new String(selector), CharOperation.toStrings(parameterTypes));
					}
				}
			}
		}
		return null;
	}

	String[] parameterTypeSignatures = new String[argCount];
	for (int i = 0; i < argCount; i++) {
		TypeReference typeRef = arguments[i].type;
		char[] typeName = CharOperation.concatWith(typeRef.getTypeName(), '.');
		for (int j = 0, dim = typeRef.dimensions(); j < dim; j++)
			typeName = CharOperation.concat(typeName, new char[] {'[', ']'});
		parameterTypeSignatures[i] = Signature.createTypeSignature(typeName, false);
	}
	return type.getMethod(new String(method.selector), parameterTypeSignatures);
}
/**
 * Creates an IField from the given field declaration and type. 
 */
protected IJavaElement createHandle(FieldDeclaration fieldDeclaration, TypeDeclaration typeDeclaration, IJavaElement parent) {
	if (!(parent instanceof IType)) return parent;

	if (fieldDeclaration.isField())
		return ((IType) parent).getField(new String(fieldDeclaration.name));

	// find occurence count of the given initializer in its type declaration
	int occurrenceCount = 0;
	FieldDeclaration[] fields = typeDeclaration.fields;
	for (int i = 0, length = fields.length; i < length; i++) {
		if (!fields[i].isField()) {
			occurrenceCount++;
			if (fields[i].equals(fieldDeclaration)) break;
		}
	}
	return ((IType) parent).getInitializer(occurrenceCount);
}
/*
 * Creates hierarchy resolver if needed. 
 * Returns whether focus is visible.
 */
protected boolean createHierarchyResolver(IType focusType, PossibleMatch[] possibleMatches) {
	// cache focus type if not a possible match
	char[][] compoundName = CharOperation.splitOn('.', focusType.getFullyQualifiedName().toCharArray());
	boolean isPossibleMatch = false;
	for (int i = 0, length = possibleMatches.length; i < length; i++) {
		if (CharOperation.equals(possibleMatches[i].compoundName, compoundName)) {
			isPossibleMatch = true;
			break;
		}
	}
	if (!isPossibleMatch) {
		if (focusType.isBinary()) {
			try {
				cacheBinaryType(focusType);
			} catch (JavaModelException e) {
				return false;
			}
		} else {
			// cache all types in the focus' compilation unit (even secondary types)
			accept((ICompilationUnit) focusType.getCompilationUnit());
		}
	}

	// resolve focus type
	this.hierarchyResolver = new HierarchyResolver(this.lookupEnvironment, null/*hierarchy is not going to be computed*/);
	ReferenceBinding binding = this.hierarchyResolver.setFocusType(compoundName);
	return binding != null && binding.isValidBinding() && (binding.tagBits & TagBits.HierarchyHasProblems) == 0;
}
/**
 * Creates an IImportDeclaration from the given import statement
 */
protected IJavaElement createImportHandle(ImportReference importRef) {
	char[] importName = CharOperation.concatWith(importRef.getImportName(), '.');
	if (importRef.onDemand)
		importName = CharOperation.concat(importName, ".*" .toCharArray()); //$NON-NLS-1$
	Openable openable = this.currentPossibleMatch.openable;
	if (openable instanceof CompilationUnit)
		return ((CompilationUnit) openable).getImport(new String(importName));

	// binary types do not contain import statements so just answer the top-level type as the element
	IType binaryType = ((ClassFile) openable).getType();
	String typeName = binaryType.getElementName();
	int lastDollar = typeName.lastIndexOf('$');
	if (lastDollar == -1) return binaryType;
	return createTypeHandle(typeName.substring(0, lastDollar));
}
/**
 * Creates an IType from the given simple top level type name. 
 */
protected IType createTypeHandle(String simpleTypeName) {
	Openable openable = this.currentPossibleMatch.openable;
	if (openable instanceof CompilationUnit)
		return ((CompilationUnit) openable).getType(simpleTypeName);

	IType binaryType = ((ClassFile) openable).getType();
	if (simpleTypeName.equals(binaryType.getTypeQualifiedName()))
		return binaryType; // answer only top-level types, sometimes the classFile is for a member/local type

	try {
		IClassFile classFile = binaryType.getPackageFragment().getClassFile(simpleTypeName + SuffixConstants.SUFFIX_STRING_class);
		return classFile.getType();
	} catch (JavaModelException e) {
		// ignore as implementation of getType() cannot throw this exception
	}
	return null;
}
protected IBinaryType getBinaryInfo(ClassFile classFile, IResource resource) throws CoreException {
	BinaryType binaryType = (BinaryType) classFile.getType();
	if (classFile.isOpen())
		return (IBinaryType) binaryType.getElementInfo(); // reuse the info from the java model cache

	// create a temporary info
	IBinaryType info;
	try {
		IJavaElement pkg = classFile.getParent();
		PackageFragmentRoot root = (PackageFragmentRoot) pkg.getParent();
		if (root.isArchive()) {
			// class file in a jar
			String pkgPath = pkg.getElementName().replace('.', '/');
			String classFilePath = pkgPath.length() > 0
				? pkgPath + "/" + classFile.getElementName() //$NON-NLS-1$
				: classFile.getElementName();
			ZipFile zipFile = null;
			try {
				zipFile = ((JarPackageFragmentRoot) root).getJar();
				info = ClassFileReader.read(zipFile, classFilePath);
			} finally {
				JavaModelManager.getJavaModelManager().closeZipFile(zipFile);
			}
		} else {
			// class file in a directory
			String osPath = resource.getLocation().toOSString();
			info = ClassFileReader.read(osPath);
		}
		return info;
	} catch (ClassFormatException e) {
		//e.printStackTrace();
		return null;
	} catch (java.io.IOException e) {
		throw new JavaModelException(e, IJavaModelStatusConstants.IO_EXCEPTION);
	}
}
protected IType getFocusType() {
	return this.scope instanceof HierarchyScope ? ((HierarchyScope) this.scope).focusType : null;
}
protected void getMethodBodies(CompilationUnitDeclaration unit) {
	if (unit.ignoreMethodBodies) {
		unit.ignoreFurtherInvestigation = true;
		return; // if initial diet parse did not work, no need to dig into method bodies.
	}

	// save existing values to restore them at the end of the parsing process
	// see bug 47079 for more details
	int[] oldLineEnds = this.parser.scanner.lineEnds;
	int oldLinePtr = this.parser.scanner.linePtr;
	
	try {
		char[] contents = unit.compilationResult.compilationUnit.getContents();
		this.parser.scanner.setSource(contents);

		// inline old setLineEnds
		final int[] lineSeparatorPositions = unit.compilationResult.lineSeparatorPositions;
		this.parser.scanner.lineEnds = lineSeparatorPositions;
		this.parser.scanner.linePtr = lineSeparatorPositions.length - 1;

		if (this.parser.javadocParser.checkJavadoc) {
			this.parser.javadocParser.scanner.setSource(contents);
		}
		this.parser.nodeSet = this.currentPossibleMatch.nodeSet;
		this.parser.parseBodies(unit);
	} finally {
		this.parser.nodeSet = null;
		// this is done to prevent any side effects on the compilation unit result
		// line separator positions array.
		this.parser.scanner.lineEnds = oldLineEnds;
		this.parser.scanner.linePtr = oldLinePtr;
	}
}
protected boolean hasAlreadyDefinedType(CompilationUnitDeclaration parsedUnit) {
	CompilationResult result = parsedUnit.compilationResult;
	if (result == null) return false;
	for (int i = 0; i < result.problemCount; i++)
		if (result.problems[i].getID() == IProblem.DuplicateTypes)
			return true;
	return false;
}	
/**
 * Create a new parser for the given project, as well as a lookup environment.
 */
public void initialize(JavaProject project, int possibleMatchSize) throws JavaModelException {
	if (this.nameEnvironment != null)
		this.nameEnvironment.cleanup();

	SearchableEnvironment searchableEnvironment = (SearchableEnvironment) project.newSearchableNameEnvironment(this.workingCopies);
	
	// if only one possible match, a file name environment costs too much,
	// so use the existing searchable  environment which will populate the java model
	// only for this possible match and its required types.
	this.nameEnvironment = possibleMatchSize == 1
		? (INameEnvironment) searchableEnvironment
		: (INameEnvironment) new JavaSearchNameEnvironment(project);

	// create lookup environment
	this.options = new CompilerOptions(project.getOptions(true));
	ProblemReporter problemReporter =
		new ProblemReporter(
			DefaultErrorHandlingPolicies.proceedWithAllProblems(),
			this.options,
			new DefaultProblemFactory());
	this.lookupEnvironment = new LookupEnvironment(this, this.options, problemReporter, this.nameEnvironment);

	this.parser = MatchLocatorParser.createParser(problemReporter, this);

	// remember project's name lookup
	this.nameLookup = searchableEnvironment.nameLookup;

	// initialize queue of units
	this.numberOfMatches = 0;
	this.matchesToProcess = new PossibleMatch[possibleMatchSize];
}
protected void locateMatches(JavaProject javaProject, PossibleMatch[] possibleMatches, int start, int length) throws JavaModelException {
	initialize(javaProject, length);

	// create and resolve binding (equivalent to beginCompilation() in Compiler)
	boolean bindingsWereCreated = true;
	try {
		for (int i = start, maxUnits = start + length; i < maxUnits; i++)
			buildBindings(possibleMatches[i]);
		lookupEnvironment.completeTypeBindings();

		// create hierarchy resolver if needed
		IType focusType = getFocusType();
		if (focusType == null) {
			this.hierarchyResolver = null;
		} else if (!createHierarchyResolver(focusType, possibleMatches)) {
			// focus type is not visible, use the super type names instead of the bindings
			if (computeSuperTypeNames(focusType) == null) return;
		}
	} catch (AbortCompilation e) {
		bindingsWereCreated = false;
	}

	// possible match resolution
	for (int i = 0; i < this.numberOfMatches; i++) {
		if (this.progressMonitor != null && this.progressMonitor.isCanceled())
			throw new OperationCanceledException();
		PossibleMatch possibleMatch = this.matchesToProcess[i];
		this.matchesToProcess[i] = null; // release reference to processed possible match
		try {
			process(possibleMatch, bindingsWereCreated);
		} catch (AbortCompilation e) {
			// problem with class path: it could not find base classes
			// continue and try next matching openable reporting innacurate matches (since bindings will be null)
			bindingsWereCreated = false;
		} catch (JavaModelException e) {
			// problem with class path: it could not find base classes
			// continue and try next matching openable reporting innacurate matches (since bindings will be null)
			bindingsWereCreated = false;
		} catch (CoreException e) {
			// core exception thrown by client's code: let it through
			throw new JavaModelException(e);
		} finally {
			if (this.options.verbose)
				System.out.println(Util.bind("compilation.done", //$NON-NLS-1$
					new String[] {
						String.valueOf(i + 1),
						String.valueOf(numberOfMatches),
						new String(possibleMatch.parsedUnit.getFileName())}));
			// cleanup compilation unit result
			possibleMatch.parsedUnit.cleanUp();
			possibleMatch.parsedUnit = null;
		}
		if (this.progressMonitor != null)
			this.progressMonitor.worked(5);
	}
}
/**
 * Locate the matches amongst the possible matches.
 */
protected void locateMatches(JavaProject javaProject, PossibleMatchSet matchSet) throws JavaModelException {
	PossibleMatch[] possibleMatches = matchSet.getPossibleMatches(javaProject.getPackageFragmentRoots());
	for (int index = 0, length = possibleMatches.length; index < length;) {
		int max = Math.min(MAX_AT_ONCE, length - index);
		locateMatches(javaProject, possibleMatches, index, max);
		index += max;
	}
}
/**
 * Locate the matches in the given files and report them using the search requestor. 
 */
public void locateMatches(SearchDocument[] searchDocuments) throws JavaModelException {
	if (SearchEngine.VERBOSE) {
		System.out.println("Locating matches in documents ["); //$NON-NLS-1$
		for (int i = 0, length = searchDocuments.length; i < length; i++)
			System.out.println("\t" + searchDocuments[i]); //$NON-NLS-1$
		System.out.println("]"); //$NON-NLS-1$
	}

	JavaModelManager manager = JavaModelManager.getJavaModelManager();
	try {
		// optimize access to zip files during search operation
		manager.cacheZipFiles();

		// initialize handle factory (used as a cache of handles so as to optimize space)
		if (this.handleFactory == null)
			this.handleFactory = new HandleFactory();

		if (this.progressMonitor != null) {
			// 1 for file path, 4 for parsing and binding creation, 5 for binding resolution? //$NON-NLS-1$
			this.progressMonitor.beginTask("", searchDocuments.length * (this.pattern.mustResolve ? 10 : 5)); //$NON-NLS-1$
		}

		// initialize pattern for polymorphic search (ie. method reference pattern)
		this.patternLocator.initializePolymorphicSearch(this);

		JavaProject previousJavaProject = null;
		PossibleMatchSet matchSet = new PossibleMatchSet();
		Util.sort(searchDocuments, new Util.Comparer() {
			public int compare(Object a, Object b) {
				return ((SearchDocument)a).getPath().compareTo(((SearchDocument)b).getPath());
			}
		}); 
		for (int i = 0, l = searchDocuments.length; i < l; i++) {
			if (this.progressMonitor != null && this.progressMonitor.isCanceled())
				throw new OperationCanceledException();

			// skip duplicate paths
			SearchDocument searchDocument = searchDocuments[i];
			String pathString = searchDocument.getPath();
			if (i > 0 && pathString.equals(searchDocuments[i - 1].getPath())) continue;

			Openable openable;
			org.eclipse.jdt.core.ICompilationUnit workingCopy = null;
			if (searchDocument instanceof JavaSearchParticipant.WorkingCopyDocument) {
				workingCopy = ((JavaSearchParticipant.WorkingCopyDocument)searchDocument).workingCopy;
				openable = (Openable) workingCopy;
			} else {
				openable = this.handleFactory.createOpenable(pathString, this.scope);
				if (openable == null) continue; // match is outside classpath
			}

			// create new parser and lookup environment if this is a new project
			IResource resource = null;
			try {
				JavaProject javaProject = (JavaProject) openable.getJavaProject();
				resource = workingCopy != null ? workingCopy.getResource() : openable.getResource();
				if (resource == null)
					resource = javaProject.getProject(); // case of a file in an external jar
				if (!javaProject.equals(previousJavaProject)) {
					// locate matches in previous project
					if (previousJavaProject != null) {
						try {
							locateMatches(previousJavaProject, matchSet);
						} catch (JavaModelException e) {
							if (e.getException() instanceof CoreException) throw e;
							// problem with classpath in this project -> skip it
						}
						matchSet.reset();
					}
					previousJavaProject = javaProject;
				}
			} catch (JavaModelException e) {
				// file doesn't exist -> skip it
				continue;
			}
			matchSet.add(new PossibleMatch(this, resource, openable, searchDocument));

			if (this.progressMonitor != null)
				this.progressMonitor.worked(1);
		}

		// last project
		if (previousJavaProject != null) {
			try {
				locateMatches(previousJavaProject, matchSet);
			} catch (JavaModelException e) {
				if (e.getException() instanceof CoreException) throw e;
				// problem with classpath in last project -> skip it
			}
		} 

		if (this.progressMonitor != null)
			this.progressMonitor.done();
	} finally {
		if (this.nameEnvironment != null)
			this.nameEnvironment.cleanup();
		manager.flushZipFiles();
	}	
}
/**
 * Locates the package declarations corresponding to this locator's pattern. 
 */
public void locatePackageDeclarations(SearchParticipant participant) throws JavaModelException {
	locatePackageDeclarations(this.pattern, participant);
}
/**
 * Locates the package declarations corresponding to the search pattern. 
 */
protected void locatePackageDeclarations(SearchPattern searchPattern, SearchParticipant participant) throws JavaModelException {
	if (searchPattern instanceof OrPattern) {
		SearchPattern[] patterns = ((OrPattern) searchPattern).patterns;
		for (int i = 0, length = patterns.length; i < length; i++)
			locatePackageDeclarations(patterns[i], participant);
	} else if (searchPattern instanceof PackageDeclarationPattern) {
		if (searchPattern.focus != null) {
			IResource resource = searchPattern.focus.getResource();
			SearchDocument document = participant.getDocument(resource.getFullPath().toString());
			this.currentPossibleMatch = new PossibleMatch(this, resource, null, document);
			try {
				this.report(-1, -2, searchPattern.focus, IJavaSearchResultCollector.EXACT_MATCH);
			} catch (CoreException e) {
				if (e instanceof JavaModelException) {
					throw (JavaModelException) e;
				} else {
					throw new JavaModelException(e);
				}
			}					
			return;
		}
		PackageDeclarationPattern pkgPattern = (PackageDeclarationPattern) searchPattern;
		IJavaProject[] projects = JavaModelManager.getJavaModelManager().getJavaModel().getJavaProjects();
		for (int i = 0, length = projects.length; i < length; i++) {
			IJavaProject javaProject = projects[i];
			IPackageFragmentRoot[] roots = javaProject.getPackageFragmentRoots();
			for (int j = 0, rootsLength = roots.length; j < rootsLength; j++) {
				IJavaElement[] pkgs = roots[j].getChildren();
				for (int k = 0, pksLength = pkgs.length; k < pksLength; k++) {
					IPackageFragment pkg = (IPackageFragment) pkgs[k];
					if (pkg.getChildren().length > 0 
							&& pkgPattern.matchesName(pkgPattern.pkgName, pkg.getElementName().toCharArray())) {
						IResource resource = pkg.getResource();
						if (resource == null) // case of a file in an external jar
							resource = javaProject.getProject();
						SearchDocument document = participant.getDocument(resource.getFullPath().toString());
						this.currentPossibleMatch = new PossibleMatch(this, resource, null, document);
						try {
							report(-1, -2, pkg, IJavaSearchResultCollector.EXACT_MATCH);
						} catch (JavaModelException e) {
							throw e;
						} catch (CoreException e) {
							throw new JavaModelException(e);
						}
					}
				}
			}
		}
	}
}
protected IType lookupType(ReferenceBinding typeBinding) {
	if (typeBinding == null) return null;

	char[] packageName = typeBinding.qualifiedPackageName();
	IPackageFragment[] pkgs = this.nameLookup.findPackageFragments(
		(packageName == null || packageName.length == 0)
			? IPackageFragment.DEFAULT_PACKAGE_NAME
			: new String(packageName),
		false);

	// iterate type lookup in each package fragment
	char[] sourceName = typeBinding.qualifiedSourceName();
	String typeName = new String(sourceName);
	for (int i = 0, length = pkgs == null ? 0 : pkgs.length; i < length; i++) {
		IType type = this.nameLookup.findType(
			typeName,
			pkgs[i], 
			false, 
			typeBinding.isClass() ? NameLookup.ACCEPT_CLASSES : NameLookup.ACCEPT_INTERFACES);
		if (type != null) return type;
	}

	// search inside enclosing element
	char[][] qualifiedName = CharOperation.splitOn('.', sourceName);
	int length = qualifiedName.length;
	if (length == 0) return null;

	IType type = createTypeHandle(new String(qualifiedName[0])); // find the top-level type
	if (type == null) return null;

	for (int i = 1; i < length; i++) {
		type = type.getType(new String(qualifiedName[i]));
		if (type == null) return null;
	}
	if (type.exists()) return type;
	return null;
}
/*
 * Process a compilation unit already parsed and build.
 */
protected void process(PossibleMatch possibleMatch, boolean bindingsWereCreated) throws CoreException {
	this.currentPossibleMatch = possibleMatch;
	CompilationUnitDeclaration unit = possibleMatch.parsedUnit;
	try {
		if (unit.isEmpty()) {
			if (this.currentPossibleMatch.openable instanceof ClassFile) {
				ClassFile classFile = (ClassFile) this.currentPossibleMatch.openable;
				IBinaryType info = getBinaryInfo(classFile, this.currentPossibleMatch.resource);
				if (info != null)
					new ClassFileMatchLocator().locateMatches(this, classFile, info);
			}
			return;
		}
		if (hasAlreadyDefinedType(unit)) return; // skip type has it is hidden so not visible

		getMethodBodies(unit);

		if (bindingsWereCreated && this.pattern.mustResolve && unit.types != null) {
			if (SearchEngine.VERBOSE)
				System.out.println("Resolving " + this.currentPossibleMatch.openable.toStringWithAncestors()); //$NON-NLS-1$

			reduceParseTree(unit);

			if (unit.scope != null)
				unit.scope.faultInTypes(); // fault in fields & methods
			unit.resolve();

			reportMatching(unit, true);
		} else {
			reportMatching(unit, this.pattern.mustResolve);
		}
	} catch (AbortCompilation e) {
		// could not resolve: report innacurate matches
		reportMatching(unit, true); // was partially resolved
		if (!(e instanceof AbortCompilationUnit)) {
			// problem with class path
			throw e;
		}
	} finally {
		this.currentPossibleMatch.cleanUp();
		this.currentPossibleMatch = null;
	}
}
protected void purgeMethodStatements(TypeDeclaration type, boolean checkEachMethod) {
	checkEachMethod = checkEachMethod
		&& this.currentPossibleMatch.nodeSet.hasPossibleNodes(type.declarationSourceStart, type.declarationSourceEnd);
	AbstractMethodDeclaration[] methods = type.methods;
	if (methods != null) {
		if (checkEachMethod) {
			for (int j = 0, length = methods.length; j < length; j++) {
				AbstractMethodDeclaration method = methods[j];
				if (!this.currentPossibleMatch.nodeSet.hasPossibleNodes(method.declarationSourceStart, method.declarationSourceEnd)) {
					method.statements = null;
					method.javadoc = null;
				}
			}
		} else {
			for (int j = 0, length = methods.length; j < length; j++) {
				methods[j].statements = null;
				methods[j].javadoc = null;
			}
		}
	}

	TypeDeclaration[] memberTypes = type.memberTypes;
	if (memberTypes != null)
		for (int i = 0, l = memberTypes.length; i < l; i++)
			purgeMethodStatements(memberTypes[i], checkEachMethod);
}
/**
 * Called prior to the unit being resolved. Reduce the parse tree where possible.
 */
protected void reduceParseTree(CompilationUnitDeclaration unit) {
	// remove statements from methods that have no possible matching nodes
	TypeDeclaration[] types = unit.types;
	for (int i = 0, l = types.length; i < l; i++)
		purgeMethodStatements(types[i], true); 
}
protected void report(int sourceStart, int sourceEnd, IJavaElement element, int accuracy) throws CoreException {
	if (element != null && this.scope.encloses(element)) {
		if (SearchEngine.VERBOSE) {
			IResource res = this.currentPossibleMatch.resource;
			System.out.println("Reporting match"); //$NON-NLS-1$
			System.out.println("\tResource: " + (res == null ? " <unknown> " : res.getFullPath().toString())); //$NON-NLS-2$//$NON-NLS-1$
			System.out.println("\tPositions: [" + sourceStart + ", " + sourceEnd + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			System.out.println("\tJava element: " + ((JavaElement)element).toStringWithAncestors()); //$NON-NLS-1$
			System.out.println(accuracy == IJavaSearchResultCollector.EXACT_MATCH
				? "\tAccuracy: EXACT_MATCH" //$NON-NLS-1$
				: "\tAccuracy: POTENTIAL_MATCH"); //$NON-NLS-1$
		}
		report(
			this.currentPossibleMatch.resource, 
			sourceStart, 
			sourceEnd, 
			element, 
			accuracy, 
			getParticipant());
	}
}
public SearchParticipant getParticipant() {
	return this.currentPossibleMatch.document.getParticipant();
}

protected void report(IResource resource, int sourceStart, int sourceEnd, IJavaElement element, int accuracy, SearchParticipant participant) throws CoreException {
	long start = -1;
	if (SearchEngine.VERBOSE)
		start = System.currentTimeMillis();
	String documentPath = element.getPath().toString();
	SearchMatch match = new JavaSearchMatch(resource, element, documentPath, accuracy, participant, sourceStart, sourceEnd+1, -1);
	this.requestor.acceptSearchMatch(match);
	if (SearchEngine.VERBOSE)
		this.resultCollectorTime += System.currentTimeMillis()-start;
}
protected void report(long start, long end, IJavaElement element, int accuracy) throws CoreException {
	report((int) (start >>> 32), (int) end, element, accuracy); // extract the start and end from the encoded long positions
}
/**
 * Finds the accurate positions of the sequence of tokens given by qualifiedName
 * in the source and reports a reference to this this qualified name
 * to the search requestor.
 */
protected void reportAccurateReference(int sourceStart, int sourceEnd, char[] name, IJavaElement element, int accuracy) throws CoreException {
	if (accuracy == -1) return;

	// compute source positions of the qualified reference 
	Scanner scanner = this.parser.scanner;
	scanner.setSource(this.currentPossibleMatch.getContents());
	scanner.resetTo(sourceStart, sourceEnd);

	int token = -1;
	int currentPosition;
	do {
		currentPosition = scanner.currentPosition;
		try {
			token = scanner.getNextToken();
		} catch (InvalidInputException e) {
			// ignore
		}
		if (token == TerminalTokens.TokenNameIdentifier && this.pattern.matchesName(name, scanner.getCurrentTokenSource())) {
			report(currentPosition, scanner.currentPosition - 1, element, accuracy);
			return;
		}
	} while (token != TerminalTokens.TokenNameEOF);
	report(sourceStart, sourceEnd, element, accuracy);
}
/**
 * Finds the accurate positions of each valid token in the source and
 * reports a reference to this token to the search requestor.
 * A token is valid if it has an accuracy which is not -1.
 */
protected void reportAccurateReference(int sourceStart, int sourceEnd, char[][] tokens, IJavaElement element, int[] accuracies) throws CoreException {
	// compute source positions of the qualified reference 
	Scanner scanner = this.parser.scanner;
	scanner.setSource(this.currentPossibleMatch.getContents());
	scanner.resetTo(sourceStart, sourceEnd);

	int refSourceStart = -1, refSourceEnd = -1;
	int length = tokens.length;
	int token = -1;
	int previousValid = -1;
	int i = 0;
	int accuracyIndex = 0;
	do {
		int currentPosition = scanner.currentPosition;
		// read token
		try {
			token = scanner.getNextToken();
		} catch (InvalidInputException e) {
			//ignore
		}
		if (token != TerminalTokens.TokenNameEOF) {
			char[] currentTokenSource = scanner.getCurrentTokenSource();
			boolean equals = false;
			while (i < length && !(equals = this.pattern.matchesName(tokens[i++], currentTokenSource)));
			if (equals && (previousValid == -1 || previousValid == i - 2)) {
				previousValid = i - 1;
				if (refSourceStart == -1)
					refSourceStart = currentPosition;
				refSourceEnd = scanner.currentPosition - 1;
			} else {
				i = 0;
				refSourceStart = -1;
				previousValid = -1;
			}
			// read '.'
			try {
				token = scanner.getNextToken();
			} catch (InvalidInputException e) {
				// ignore
			}
		}
		if (accuracies[accuracyIndex] != -1) {
			// accept reference
			if (refSourceStart != -1) {
				report(refSourceStart, refSourceEnd, element, accuracies[accuracyIndex]);
			} else {
				report(sourceStart, sourceEnd, element, accuracies[accuracyIndex]);
			}
			i = 0;
		}
		refSourceStart = -1;
		previousValid = -1;
		if (accuracyIndex < accuracies.length - 1)
			accuracyIndex++;
	} while (token != TerminalTokens.TokenNameEOF);

}
protected void reportBinaryMatch(IResource resource, IMember binaryMember, IBinaryType info, int accuracy) throws CoreException {
	ISourceRange range = binaryMember.getNameRange();
	if (range.getOffset() == -1) {
		ClassFile classFile = (ClassFile) binaryMember.getClassFile();
		SourceMapper mapper = classFile.getSourceMapper();
		if (mapper != null) {
			IType type = classFile.getType();
			String sourceFileName = mapper.findSourceFileName(type, info);
			if (sourceFileName != null) {
				char[] contents = mapper.findSource(type, sourceFileName);
				if (contents != null)
					range = mapper.mapSource(type, contents, binaryMember);
			}
		}
	}
	int startIndex = range.getOffset();
	int endIndex = startIndex + range.getLength() - 1;
	if (resource == null)
		report(startIndex, endIndex, binaryMember, accuracy);
	else
		report(resource, startIndex, endIndex, binaryMember, accuracy, getParticipant());
}
/**
 * Visit the given method declaration and report the nodes that match exactly the
 * search pattern (ie. the ones in the matching nodes set)
 * Note that the method declaration has already been checked.
 */
protected void reportMatching(AbstractMethodDeclaration method, IJavaElement parent, int accuracy, boolean typeInHierarchy, MatchingNodeSet nodeSet) throws CoreException {
	IJavaElement enclosingElement = null;
	if (accuracy > -1) {
		enclosingElement = createHandle(method, parent);
		if (enclosingElement != null) { // skip if unable to find method
			// compute source positions of the selector 
			Scanner scanner = parser.scanner;
			int nameSourceStart = method.sourceStart;
			scanner.setSource(this.currentPossibleMatch.getContents());
			scanner.resetTo(nameSourceStart, method.sourceEnd);
			try {
				scanner.getNextToken();
			} catch (InvalidInputException e) {
				// ignore
			}
			int nameSourceEnd = scanner.currentPosition - 1;

			report(nameSourceStart, nameSourceEnd, enclosingElement, accuracy);
		}
	}

	// handle nodes for the local type first
	if ((method.bits & ASTNode.HasLocalTypeMASK) != 0) {
		if (enclosingElement == null)
			enclosingElement = createHandle(method, parent);
		LocalDeclarationVisitor localDeclarationVisitor = new LocalDeclarationVisitor(enclosingElement, nodeSet);
		try {
			method.traverse(localDeclarationVisitor, (ClassScope) null);
		} catch (WrappedCoreException e) {
			throw e.coreException;
		}
	}

	// references in this method
	if (typeInHierarchy) {
		ASTNode[] nodes = nodeSet.matchingNodes(method.declarationSourceStart, method.declarationSourceEnd);
		if (nodes != null) {
			if ((this.matchContainer & PatternLocator.METHOD_CONTAINER) != 0) {
				if (enclosingElement == null)
					enclosingElement = createHandle(method, parent);
				if (enclosingElement != null) { // skip if unable to find method
					for (int i = 0, l = nodes.length; i < l; i++) {
						ASTNode node = nodes[i];
						Integer level = (Integer) nodeSet.matchingNodes.removeKey(node);
						this.patternLocator.matchReportReference(node, enclosingElement, level.intValue(), this);
					}
					return;
				}
			}
			for (int i = 0, l = nodes.length; i < l; i++)
				nodeSet.matchingNodes.removeKey(nodes[i]);
		}
	}
}
/**
 * Visit the given resolved parse tree and report the nodes that match the search pattern.
 */
protected void reportMatching(CompilationUnitDeclaration unit, boolean mustResolve) throws CoreException {
	MatchingNodeSet nodeSet = this.currentPossibleMatch.nodeSet;
	if (mustResolve) {
		// move the possible matching nodes that exactly match the search pattern to the matching nodes set
		Object[] nodes = nodeSet.possibleMatchingNodesSet.values;
		for (int i = 0, l = nodes.length; i < l; i++) {
			ASTNode node = (ASTNode) nodes[i];
			if (node == null) continue;
			if (node instanceof ImportReference) {
				// special case for import refs: they don't know their binding
				// import ref cannot be in the hirarchy of a type
				if (this.hierarchyResolver != null) continue;

				ImportReference importRef = (ImportReference) node;
				Binding binding = importRef.onDemand
					? unit.scope.getTypeOrPackage(CharOperation.subarray(importRef.tokens, 0, importRef.tokens.length))
					: unit.scope.getTypeOrPackage(importRef.tokens);
				this.patternLocator.matchLevelAndReportImportRef(importRef, binding, this);
			} else {
				nodeSet.addMatch(node, this.patternLocator.resolveLevel(node));
			}
		}
		nodeSet.possibleMatchingNodesSet = new SimpleSet();
	}

	if (nodeSet.matchingNodes.elementSize == 0) return; // no matching nodes were found

	boolean matchedUnitContainer = (this.matchContainer & PatternLocator.COMPILATION_UNIT_CONTAINER) != 0;
	if (matchedUnitContainer) {
// Currently a no-op
//	ImportReference pkg = unit.currentPackage;
//	if (pkg != null && nodeSet.matchingNodes.removeKey(pkg) != null)
//		reportPackageDeclaration(pkg);

		ImportReference[] imports = unit.imports;
		if (imports != null) {
			for (int i = 0, l = imports.length; i < l; i++) {
				ImportReference importRef = imports[i];
				Integer level = (Integer) nodeSet.matchingNodes.removeKey(importRef);
				if (level != null)
					this.patternLocator.matchReportImportRef(importRef, null, createImportHandle(importRef), level.intValue(), this);
			}
		}
	}

	TypeDeclaration[] types = unit.types;
	if (types != null) {
		for (int i = 0, l = types.length; i < l; i++) {
			if (nodeSet.matchingNodes.elementSize == 0) return; // reported all the matching nodes
			TypeDeclaration type = types[i];
			Integer level = (Integer) nodeSet.matchingNodes.removeKey(type);
			int accuracy = (level != null && matchedUnitContainer) ? level.intValue() : -1;
			reportMatching(type, null, accuracy, nodeSet, 1);
		}
	}
}
/**
 * Visit the given field declaration and report the nodes that match exactly the
 * search pattern (ie. the ones in the matching nodes set)
 */
protected void reportMatching(FieldDeclaration field, TypeDeclaration type, IJavaElement parent, int accuracy, boolean typeInHierarchy, MatchingNodeSet nodeSet) throws CoreException {
	IJavaElement enclosingElement = null;
	if (accuracy > -1) {
		enclosingElement = createHandle(field, type, parent);
		report(field.sourceStart, field.sourceEnd, enclosingElement, accuracy);
	}

	// handle the nodes for the local type first
	if ((field.bits & ASTNode.HasLocalTypeMASK) != 0) {
		if (enclosingElement == null)
			enclosingElement = createHandle(field, type, parent);
		LocalDeclarationVisitor localDeclarationVisitor = new LocalDeclarationVisitor(enclosingElement, nodeSet);
		try {
			field.traverse(localDeclarationVisitor, null);
		} catch (WrappedCoreException e) {
			throw e.coreException;
		}
	}

	if (typeInHierarchy) {
		ASTNode[] nodes = nodeSet.matchingNodes(field.declarationSourceStart, field.declarationSourceEnd);
		if (nodes != null) {
			if ((this.matchContainer & PatternLocator.FIELD_CONTAINER) == 0) {
				for (int i = 0, l = nodes.length; i < l; i++)
					nodeSet.matchingNodes.removeKey(nodes[i]);
			} else {
				if (enclosingElement == null)
					enclosingElement = createHandle(field, type, parent);
				for (int i = 0, l = nodes.length; i < l; i++) {
					ASTNode node = nodes[i];
					Integer level = (Integer) nodeSet.matchingNodes.removeKey(node);
					this.patternLocator.matchReportReference(node, enclosingElement, level.intValue(), this);
				}
			}
		}
	}
}
/**
 * Visit the given type declaration and report the nodes that match exactly the
 * search pattern (ie. the ones in the matching nodes set)
 */
protected void reportMatching(TypeDeclaration type, IJavaElement parent, int accuracy, MatchingNodeSet nodeSet, int occurrenceCount) throws CoreException {
	// create type handle
	IJavaElement enclosingElement = parent;
	if (enclosingElement == null) {
		enclosingElement = createTypeHandle(new String(type.name));
	} else if (enclosingElement instanceof IType) {
		enclosingElement = ((IType) parent).getType(new String(type.name));
	} else if (enclosingElement instanceof IMember) {
	    IMember member = (IMember) parent;
	    if (member.isBinary()) 
	        enclosingElement = parent;
	    else
			enclosingElement = member.getType(new String(type.name), occurrenceCount);
	}
	if (enclosingElement == null) return;

	// report the type declaration
	if (accuracy > -1)
		report(type.sourceStart, type.sourceEnd, enclosingElement, accuracy);

	boolean matchedClassContainer = (this.matchContainer & PatternLocator.CLASS_CONTAINER) != 0;

	// javadoc
	if (type.javadoc != null) {
		ASTNode[] nodes = nodeSet.matchingNodes(type.declarationSourceStart, type.sourceStart);
		if (nodes != null) {
			if (!matchedClassContainer) {
				for (int i = 0, l = nodes.length; i < l; i++)
					nodeSet.matchingNodes.removeKey(nodes[i]);
			} else {
				for (int i = 0, l = nodes.length; i < l; i++) {
					ASTNode node = nodes[i];
					Integer level = (Integer) nodeSet.matchingNodes.removeKey(node);
					this.patternLocator.matchReportReference(node, enclosingElement, level.intValue(), this);
				}
			}
		}
	}
	
	// super types
	if ((type.bits & ASTNode.IsAnonymousTypeMASK) != 0) {
		TypeReference superType =type.allocation.type;
		if (superType != null) {
			Integer level = (Integer) nodeSet.matchingNodes.removeKey(superType);
			if (level != null && matchedClassContainer)
				this.patternLocator.matchReportReference(superType, enclosingElement, level.intValue(), this);
		}
	} else {
		TypeReference superClass = type.superclass;
		if (superClass != null) {
			Integer level = (Integer) nodeSet.matchingNodes.removeKey(superClass);
			if (level != null && matchedClassContainer)
				this.patternLocator.matchReportReference(superClass, enclosingElement, level.intValue(), this);
		}
		TypeReference[] superInterfaces = type.superInterfaces;
		if (superInterfaces != null) {
			for (int i = 0, l = superInterfaces.length; i < l; i++) {
				TypeReference superInterface = superInterfaces[i];
				Integer level = (Integer) nodeSet.matchingNodes.removeKey(superInterface);
				if (level != null && matchedClassContainer)
					this.patternLocator.matchReportReference(superInterface, enclosingElement, level.intValue(), this);
			}
		}
	}

	// filter out element not in hierarchy scope
	boolean typeInHierarchy = type.binding == null || typeInHierarchy(type.binding);
	matchedClassContainer = matchedClassContainer && typeInHierarchy; 

	FieldDeclaration[] fields = type.fields;
	if (fields != null) {
		if (nodeSet.matchingNodes.elementSize == 0) return; // reported all the matching nodes
		for (int i = 0, l = fields.length; i < l; i++) {
			FieldDeclaration field = fields[i];
			Integer level = (Integer) nodeSet.matchingNodes.removeKey(field);
			int value = (level != null && matchedClassContainer) ? level.intValue() : -1;
			reportMatching(field, type, enclosingElement, value, typeInHierarchy, nodeSet);
		}
	}

	AbstractMethodDeclaration[] methods = type.methods;
	if (methods != null) {
		if (nodeSet.matchingNodes.elementSize == 0) return; // reported all the matching nodes
		for (int i = 0, l = methods.length; i < l; i++) {
			AbstractMethodDeclaration method = methods[i];
			Integer level = (Integer) nodeSet.matchingNodes.removeKey(method);
			int value = (level != null && matchedClassContainer) ? level.intValue() : -1;
			reportMatching(method, enclosingElement, value, typeInHierarchy, nodeSet);
		}
	}

	TypeDeclaration[] memberTypes = type.memberTypes;
	if (memberTypes != null) {
		for (int i = 0, l = memberTypes.length; i < l; i++) {
			if (nodeSet.matchingNodes.elementSize == 0) return; // reported all the matching nodes
			TypeDeclaration memberType = memberTypes[i];
			Integer level = (Integer) nodeSet.matchingNodes.removeKey(memberType);
			int value = (level != null && matchedClassContainer) ? level.intValue() : -1;
			reportMatching(memberType, enclosingElement, value, nodeSet, 1);
		}
	}
}
protected boolean typeInHierarchy(ReferenceBinding binding) {
	if (this.hierarchyResolver == null) return true; // not a hierarchy scope
	if (this.hierarchyResolver.subOrSuperOfFocus(binding)) return true;

	if (this.allSuperTypeNames != null) {
		char[][] compoundName = binding.compoundName;
		for (int i = 0, length = this.allSuperTypeNames.length; i < length; i++)
			if (CharOperation.equals(compoundName, this.allSuperTypeNames[i]))
				return true;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5580.java