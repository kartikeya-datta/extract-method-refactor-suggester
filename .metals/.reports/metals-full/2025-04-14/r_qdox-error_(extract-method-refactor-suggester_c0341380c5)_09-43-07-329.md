error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6114.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6114.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6114.java
text:
```scala
i@@nt startPos= first.getStartPosition(); // no extended range for first: bug 121428

/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.core.dom.rewrite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.TextEdit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.TypeNameRequestor;

public final class ImportRewriteAnalyzer {
	
	private ICompilationUnit compilationUnit;
	private ArrayList packageEntries;
	
	private int importOnDemandThreshold;
	
	private boolean filterImplicitImports;
	private boolean findAmbiguousImports;
	
	private List importsCreated;
	private List staticImportsCreated;

	private IRegion replaceRange;

	private int flags= 0;
	
	private static final int F_NEEDS_LEADING_DELIM= 2;
	private static final int F_NEEDS_TRAILING_DELIM= 4;
	
	private static final String JAVA_LANG= "java.lang"; //$NON-NLS-1$
	
	public ImportRewriteAnalyzer(ICompilationUnit cu, CompilationUnit root, String[] preferenceOrder, int importThreshold, boolean restoreExistingImports) {
		this.compilationUnit= cu;
				
		this.importOnDemandThreshold= importThreshold;
		this.filterImplicitImports= true;
		this.findAmbiguousImports= true; //!restoreExistingImports;
		
		this.packageEntries= new ArrayList(20);
		this.importsCreated= new ArrayList();
		this.staticImportsCreated= new ArrayList();
		this.flags= 0;
		
		this.replaceRange= evaluateReplaceRange(root);
		if (restoreExistingImports) {
			addExistingImports(root);
		}

		PackageEntry[] order= new PackageEntry[preferenceOrder.length];
		for (int i= 0; i < order.length; i++) {
			String curr= preferenceOrder[i];
			if (curr.length() > 0 && curr.charAt(0) == '#') {
				curr= curr.substring(1);
				order[i]= new PackageEntry(curr, curr, true); // static import group
			} else {
				order[i]= new PackageEntry(curr, curr, false); // normal import group
			}
		}
		
		addPreferenceOrderHolders(order);
	}
	
	private void addPreferenceOrderHolders(PackageEntry[] preferenceOrder) {
		if (this.packageEntries.isEmpty()) {
			// all new: copy the elements
			for (int i= 0; i < preferenceOrder.length; i++) {
				this.packageEntries.add(preferenceOrder[i]);
			}
		} else {
			// match the preference order entries to existing imports
			// entries not found are appended after the last successfully matched entry
			
			PackageEntry[] lastAssigned= new PackageEntry[preferenceOrder.length];
			
			// find an existing package entry that matches most
			for (int k= 0; k < this.packageEntries.size(); k++) {
				PackageEntry entry= (PackageEntry) this.packageEntries.get(k);
				if (!entry.isComment()) {
					String currName= entry.getName();
					int currNameLen= currName.length();
					int bestGroupIndex= -1;
					int bestGroupLen= -1;
					for (int i= 0; i < preferenceOrder.length; i++) {
						boolean currPrevStatic= preferenceOrder[i].isStatic();
						if (currPrevStatic == entry.isStatic()) {
							String currPrefEntry= preferenceOrder[i].getName();
							int currPrefLen= currPrefEntry.length();
							if (currName.startsWith(currPrefEntry) && currPrefLen >= bestGroupLen) {
								if (currPrefLen == currNameLen || currName.charAt(currPrefLen) == '.') {
									if (bestGroupIndex == -1 || currPrefLen > bestGroupLen) {
										bestGroupLen= currPrefLen;
										bestGroupIndex= i;
									}
								}
							}
						}
					}
					if (bestGroupIndex != -1) {
						entry.setGroupID(preferenceOrder[bestGroupIndex].getName());
						lastAssigned[bestGroupIndex]= entry; // remember last entry 
					}
				}
			}
			// fill in not-assigned categories, keep partial order
			int currAppendIndex= 0;
			for (int i= 0; i < lastAssigned.length; i++) {
				PackageEntry entry= lastAssigned[i];
				if (entry == null) {
					PackageEntry newEntry= preferenceOrder[i];
					if (currAppendIndex == 0 && !newEntry.isStatic()) {
						currAppendIndex= getIndexAfterStatics();
					}
					this.packageEntries.add(currAppendIndex, newEntry);
					currAppendIndex++;
				} else {
					currAppendIndex= this.packageEntries.indexOf(entry) + 1;
				}
			}
		}
	}

	private static String getQualifier(ImportDeclaration decl) {
		String name= decl.getName().getFullyQualifiedName();
		return decl.isOnDemand() ? name : Signature.getQualifier(name);
	}

	private static String getFullName(ImportDeclaration decl) {
		String name= decl.getName().getFullyQualifiedName();
		return decl.isOnDemand() ? name + ".*": name; //$NON-NLS-1$
	}
	
	private void addExistingImports(CompilationUnit root) {
		List/*ImportDeclaration*/ decls= root.imports();
		if (decls.isEmpty()) {
			return;
		}				
		PackageEntry currPackage= null;
			
		ImportDeclaration curr= (ImportDeclaration) decls.get(0);
		int currOffset= curr.getStartPosition();
		int currLength= curr.getLength();
		int currEndLine= root.getLineNumber(currOffset + currLength);
		
		for (int i= 1; i < decls.size(); i++) {
			boolean isStatic= curr.isStatic();
			String name= getFullName(curr);
			String packName= getQualifier(curr);
			if (currPackage == null || currPackage.compareTo(packName, isStatic) != 0) {
				currPackage= new PackageEntry(packName, null, isStatic);
				this.packageEntries.add(currPackage);
			}

			ImportDeclaration next= (ImportDeclaration) decls.get(i);
			int nextOffset= next.getStartPosition();
			int nextLength= next.getLength();
			int nextOffsetLine= root.getLineNumber(nextOffset); 

			// if next import is on a different line, modify the end position to the next line begin offset
			if (currEndLine < nextOffsetLine) {
				currEndLine++;
				nextOffset= root.getPosition(currEndLine, 0);
			}
			currPackage.add(new ImportDeclEntry(name, isStatic, new Region(currOffset, nextOffset - currOffset)));
			currOffset= nextOffset;
			curr= next;
				
			// add a comment entry for spacing between imports
			if (currEndLine < nextOffsetLine) {
				nextOffset= root.getPosition(nextOffsetLine, 0);
				
				currPackage= new PackageEntry(); // create a comment package entry for this
				this.packageEntries.add(currPackage);
				currPackage.add(new ImportDeclEntry(null, false, new Region(currOffset, nextOffset - currOffset)));
					
				currOffset= nextOffset;
			}
			currEndLine= root.getLineNumber(nextOffset + nextLength);
		}

		boolean isStatic= curr.isStatic();
		String name= getFullName(curr);
		String packName= getQualifier(curr);
		if (currPackage == null || currPackage.compareTo(packName, isStatic) != 0) {
			currPackage= new PackageEntry(packName, null, isStatic);
			this.packageEntries.add(currPackage);
		}
		int length= this.replaceRange.getOffset() + this.replaceRange.getLength() - curr.getStartPosition();
		currPackage.add(new ImportDeclEntry(name, isStatic, new Region(curr.getStartPosition(), length)));
	}
			
	/**
	 * Sets that implicit imports (types in default package, CU- package and
	 * 'java.lang') should not be created. Note that this is a heuristic filter and can
	 * lead to missing imports, e.g. in cases where a type is forced to be specified
	 * due to a name conflict.
	 * By default, the filter is enabled.
	 * @param filterImplicitImports The filterImplicitImports to set
	 */
	public void setFilterImplicitImports(boolean filterImplicitImports) {
		this.filterImplicitImports= filterImplicitImports;
	}
	
	/**
	 * When set searches for imports that can not be folded into on-demand
	 * imports but must be specified explicitly
	 * @param findAmbiguousImports The new value
	 */
	public void setFindAmbiguousImports(boolean findAmbiguousImports) {
		this.findAmbiguousImports= findAmbiguousImports;
	}	
			
	private static class PackageMatcher {
		private String newName;
		private String bestName;
		private int bestMatchLen;
		
		public PackageMatcher() {
			// initialization in 'initialize'
		}
		
		public void initialize(String newImportName, String bestImportName) {
			this.newName= newImportName;
			this.bestName= bestImportName;
			this.bestMatchLen= getCommonPrefixLength(bestImportName, newImportName);
		}
		
		public boolean isBetterMatch(String currName, boolean preferCurr) {
			boolean isBetter;
			int currMatchLen= getCommonPrefixLength(currName, this.newName);
			int matchDiff= currMatchLen - this.bestMatchLen;
			if (matchDiff == 0) {
				if (currMatchLen == this.newName.length() && currMatchLen == currName.length() && currMatchLen == this.bestName.length()) {
					// duplicate entry and complete match
					isBetter= preferCurr;
				} else {
					isBetter= sameMatchLenTest(currName);
				}
			} else {
				isBetter= (matchDiff > 0); // curr has longer match
			}
			if (isBetter) {
				this.bestName= currName;
				this.bestMatchLen= currMatchLen;
			}
			return isBetter;
		}
				
		private boolean sameMatchLenTest(String currName) {
			int matchLen= this.bestMatchLen;
			// known: bestName and currName differ from newName at position 'matchLen'
			// currName and bestName don't have to differ at position 'matchLen'

			// determine the order and return true if currName is closer to newName
			char newChar= getCharAt(this.newName, matchLen);
			char currChar= getCharAt(currName, matchLen);
			char bestChar= getCharAt(this.bestName, matchLen);

			if (newChar < currChar) {
				if (bestChar < newChar) {								// b < n < c
					return (currChar - newChar) < (newChar - bestChar);	// -> (c - n) < (n - b)
				} else {												// n < b  && n < c
					if (currChar == bestChar) { // longer match between curr and best
						return false; // keep curr and best together, new should be before both
					} else {
						return currChar < bestChar; // -> (c < b)
					}
				}
			} else {
				if (bestChar > newChar) {								// c < n < b
					return (newChar - currChar) < (bestChar - newChar);	// -> (n - c) < (b - n)
				} else {												// n > b  && n > c
					if (currChar == bestChar) {  // longer match between curr and best
						return true; // keep curr and best together, new should be ahead of both
					} else {
						return currChar > bestChar; // -> (c > b)
					}
				}
			}
		}
	}

	/* package */ static int getCommonPrefixLength(String s, String t) {
		int len= Math.min(s.length(), t.length());
		for (int i= 0; i < len; i++) {
			if (s.charAt(i) != t.charAt(i)) {
				return i;
			}
		}
		return len;
	}

	/* package */ static char getCharAt(String str, int index) {
		if (str.length() > index) {
			return str.charAt(index);
		}
		return 0;
	}
	
	private PackageEntry findBestMatch(String newName, boolean isStatic) {
		if (this.packageEntries.isEmpty()) {
			return null;
		}
		String groupId= null;
		int longestPrefix= -1;
		// find the matching group
		for (int i= 0; i < this.packageEntries.size(); i++) {
			PackageEntry curr= (PackageEntry) this.packageEntries.get(i);
			if (isStatic == curr.isStatic()) {
				String currGroup= curr.getGroupID();
				if (currGroup != null && newName.startsWith(currGroup)) {
					int prefixLen= currGroup.length();
					if (prefixLen == newName.length()) {
						return curr; // perfect fit, use entry
					}
					if ((newName.charAt(prefixLen) == '.') && prefixLen > longestPrefix) {
						longestPrefix= prefixLen;
						groupId= currGroup;
					}
				}
			}
		}
		PackageEntry bestMatch= null;
		PackageMatcher matcher= new PackageMatcher();
		matcher.initialize(newName, ""); //$NON-NLS-1$
		for (int i= 0; i < this.packageEntries.size(); i++) { // find the best match with the same group
			PackageEntry curr= (PackageEntry) this.packageEntries.get(i);
			if (!curr.isComment() && curr.isStatic() == isStatic) {
				if (groupId == null || groupId.equals(curr.getGroupID())) {
					boolean preferrCurr= (bestMatch == null) || (curr.getNumberOfImports() > bestMatch.getNumberOfImports());
					if (matcher.isBetterMatch(curr.getName(), preferrCurr)) {
						bestMatch= curr;
					}
				}
			}
		}
		return bestMatch;
	}
		
	private static boolean isImplicitImport(String qualifier, ICompilationUnit cu) {
		if (JAVA_LANG.equals(qualifier)) { 
			return true;
		}
		String packageName= cu.getParent().getElementName();
		if (qualifier.equals(packageName)) {
			return true;
		}
		String mainTypeName= JavaCore.removeJavaLikeExtension(cu.getElementName());
		if (packageName.length() == 0) {
			return qualifier.equals(mainTypeName);
		}
		return qualifier.equals(packageName +'.' + mainTypeName);
	}
	
	public void addImport(String fullTypeName, boolean isStatic) {
		String typeContainerName= Signature.getQualifier(fullTypeName);
		ImportDeclEntry decl= new ImportDeclEntry(fullTypeName, isStatic, null);
		sortIn(typeContainerName, decl, isStatic);
	}
	
	public boolean removeImport(String qualifiedName, boolean isStatic) {
		String containerName= Signature.getQualifier(qualifiedName);
		
		int nPackages= this.packageEntries.size();
		for (int i= 0; i < nPackages; i++) {
			PackageEntry entry= (PackageEntry) this.packageEntries.get(i);
			if (entry.compareTo(containerName, isStatic) == 0) {
				if (entry.remove(qualifiedName, isStatic)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private int getIndexAfterStatics() {
		for (int i= 0; i < this.packageEntries.size(); i++) {
			if (!((PackageEntry) this.packageEntries.get(i)).isStatic()) {
				return i;
			}
		}
		return this.packageEntries.size();
	}
	
	
	private void sortIn(String typeContainerName, ImportDeclEntry decl, boolean isStatic) {
		PackageEntry bestMatch= findBestMatch(typeContainerName, isStatic);
		if (bestMatch == null) {
			PackageEntry packEntry= new PackageEntry(typeContainerName, null, isStatic);
			packEntry.add(decl);
			int insertPos= packEntry.isStatic() ? 0 : getIndexAfterStatics();
			this.packageEntries.add(insertPos, packEntry);
		} else {
			int cmp= typeContainerName.compareTo(bestMatch.getName());
			if (cmp == 0) {
				bestMatch.sortIn(decl);
			} else {
				// create a new package entry
				String group= bestMatch.getGroupID();
				if (group != null) {
					if (!typeContainerName.startsWith(group)) {
						group= null;
					}
				}
				PackageEntry packEntry= new PackageEntry(typeContainerName, group, isStatic);
				packEntry.add(decl);
				int index= this.packageEntries.indexOf(bestMatch);
				if (cmp < 0) { 	// insert ahead of best match
					this.packageEntries.add(index, packEntry);
				} else {		// insert after best match
					this.packageEntries.add(index + 1, packEntry);
				}
			}
		}
	}
			
	private IRegion evaluateReplaceRange(CompilationUnit root) {
		List imports= root.imports();
		if (!imports.isEmpty()) {
			ImportDeclaration first= (ImportDeclaration) imports.get(0);
			ImportDeclaration last= (ImportDeclaration) imports.get(imports.size() - 1);
			
			int startPos= root.getExtendedStartPosition(first);
			int endPos= root.getExtendedStartPosition(last) + root.getExtendedLength(last);
			int endLine= root.getLineNumber(endPos);
			if (endLine > 0) {
				int nextLinePos= root.getPosition(endLine + 1, 0);
				if (nextLinePos >= 0) {
					int firstTypePos= getFirstTypeBeginPos(root);
					if (firstTypePos != -1 && firstTypePos < nextLinePos) {
						endPos= firstTypePos;
					} else {
						endPos= nextLinePos;
					}
				}
			}
			return new Region(startPos, endPos - startPos);
		} else {
			int start= getPackageStatementEndPos(root);
			return new Region(start, 0);
		}		
	}
	
	public MultiTextEdit getResultingEdits(IProgressMonitor monitor) throws JavaModelException {
		if (monitor == null) {
			monitor= new NullProgressMonitor();
		}
		try {	
			int importsStart=  this.replaceRange.getOffset();
			int importsLen= this.replaceRange.getLength();
					
			String lineDelim= this.compilationUnit.findRecommendedLineSeparator();
			IBuffer buffer= this.compilationUnit.getBuffer();
			
			boolean useSpaceBetween= useSpaceBetweenGroups();
						
			int currPos= importsStart;
			MultiTextEdit resEdit= new MultiTextEdit();
			
			if ((this.flags & F_NEEDS_LEADING_DELIM) != 0) {
				// new import container
				resEdit.addChild(new InsertEdit(currPos, lineDelim));
			}
			
			PackageEntry lastPackage= null;
			
			Set onDemandConflicts= null;
			if (this.findAmbiguousImports) {
				onDemandConflicts= evaluateStarImportConflicts(monitor);
			}
			
			ArrayList stringsToInsert= new ArrayList();
			
			int nPackageEntries= this.packageEntries.size();
			for (int i= 0; i < nPackageEntries; i++) {
				PackageEntry pack= (PackageEntry) this.packageEntries.get(i);
				int nImports= pack.getNumberOfImports();
	
				if (this.filterImplicitImports && !pack.isStatic() && isImplicitImport(pack.getName(), this.compilationUnit)) {
					pack.removeAllNew(onDemandConflicts);
					nImports= pack.getNumberOfImports();
				}
				if (nImports == 0) {
					continue;
				}
				
				if (useSpaceBetween) {
					// add a space between two different groups by looking at the two adjacent imports
					if (lastPackage != null && !pack.isComment() && !pack.isSameGroup(lastPackage)) {
						ImportDeclEntry last= lastPackage.getImportAt(lastPackage.getNumberOfImports() - 1);
						ImportDeclEntry first= pack.getImportAt(0);
						if (!lastPackage.isComment() && (last.isNew() || first.isNew())) {
							stringsToInsert.add(lineDelim);
						}
					}
				}
				lastPackage= pack;
				
				boolean isStatic= pack.isStatic();
				
				boolean doStarImport= pack.hasStarImport(this.importOnDemandThreshold, onDemandConflicts);
				if (doStarImport && (pack.find("*") == null)) { //$NON-NLS-1$
					String starImportString= pack.getName() + ".*"; //$NON-NLS-1$
					String str= getNewImportString(starImportString, isStatic, lineDelim);
					stringsToInsert.add(str);
				}
				
				for (int k= 0; k < nImports; k++) {
					ImportDeclEntry currDecl= pack.getImportAt(k);
					IRegion region= currDecl.getSourceRange();
					
					if (region == null) { // new entry
						if (!doStarImport || currDecl.isOnDemand() || (onDemandConflicts != null && onDemandConflicts.contains(currDecl.getSimpleName()))) {
							String str= getNewImportString(currDecl.getElementName(), isStatic, lineDelim);
							stringsToInsert.add(str);
						}
					} else {
						if (!doStarImport || currDecl.isOnDemand() || onDemandConflicts == null || onDemandConflicts.contains(currDecl.getSimpleName())) {
							int offset= region.getOffset();
							removeAndInsertNew(buffer, currPos, offset, stringsToInsert, resEdit);
							stringsToInsert.clear();
							currPos= offset + region.getLength();
						}
					}
				}
			}
			
			int end= importsStart + importsLen;
			removeAndInsertNew(buffer, currPos, end, stringsToInsert, resEdit);
			
			if (importsLen == 0) {
				if (!this.importsCreated.isEmpty() || !this.staticImportsCreated.isEmpty()) { // new import container
					if ((this.flags & F_NEEDS_TRAILING_DELIM) != 0) {
						resEdit.addChild(new InsertEdit(currPos, lineDelim));
					}
				} else {
					return new MultiTextEdit(); // no changes
				}
			}
			return resEdit;
		} finally {
			monitor.done();
		}
	}

	private void removeAndInsertNew(IBuffer buffer, int contentOffset, int contentEnd, ArrayList stringsToInsert, MultiTextEdit resEdit) {
		int pos= contentOffset;
		for (int i= 0; i < stringsToInsert.size(); i++) {
			String curr= (String) stringsToInsert.get(i);
			int idx= findInBuffer(buffer, curr, pos, contentEnd);
			if (idx != -1) {
				if (idx != pos) {
					resEdit.addChild(new DeleteEdit(pos, idx - pos));
				}
				pos= idx + curr.length();
			} else {
				resEdit.addChild(new InsertEdit(pos, curr));
			}
		}
		if (pos < contentEnd) {
			resEdit.addChild(new DeleteEdit(pos, contentEnd - pos));
		}
	}

	private int findInBuffer(IBuffer buffer, String str, int start, int end) {
		int pos= start;
		int len= str.length();
		if (pos + len > end || str.length() == 0) {
			return -1;
		}
		char first= str.charAt(0);
		int step= str.indexOf(first, 1);
		if (step == -1) {
			step= len;
		}
		while (pos + len <= end) {
			if (buffer.getChar(pos) == first) {
				int k= 1;
				while (k < len && buffer.getChar(pos + k) == str.charAt(k)) {
					k++;
				}
				if (k == len) {
					return pos; // found
				}
				if (k < step) {
					pos+= k;
				} else {
					pos+= step;
				}
			} else {
				pos++;
			}
		}
		return -1;
	}
	
	
	/*
	 * @return  Probes if the formatter allows spaces between imports
	 */
	private boolean useSpaceBetweenGroups() {
		try {
			String sample= "import a.A;\n\n import b.B;\nclass C {}"; //$NON-NLS-1$
			TextEdit res= ToolFactory.createCodeFormatter(this.compilationUnit.getJavaProject().getOptions(true)).format(CodeFormatter.K_COMPILATION_UNIT, sample, 0, sample.length(), 0, String.valueOf('\n'));
			Document doc= new Document(sample);
			res.apply(doc);
			int idx1= doc.search(0, "import", true, true, false); //$NON-NLS-1$
			int line1= doc.getLineOfOffset(idx1);
			int idx2= doc.search(idx1 + 1, "import", true, true, false); //$NON-NLS-1$
			int line2= doc.getLineOfOffset(idx2);
			return line2 - line1 > 1; 
		} catch (BadLocationException e) {
			// should not happen 
		}
		return true;
	}

	private Set evaluateStarImportConflicts(IProgressMonitor monitor) throws JavaModelException {
		//long start= System.currentTimeMillis();
		
		final HashSet/*String*/ onDemandConflicts= new HashSet();
		
		IJavaSearchScope scope= SearchEngine.createJavaSearchScope(new IJavaElement[] { this.compilationUnit.getJavaProject() });

		ArrayList/*<char[][]>*/  starImportPackages= new ArrayList();
		ArrayList/*<char[][]>*/ simpleTypeNames= new ArrayList();
		int nPackageEntries= this.packageEntries.size();
		for (int i= 0; i < nPackageEntries; i++) {
			PackageEntry pack= (PackageEntry) this.packageEntries.get(i);
			if (!pack.isStatic() && pack.hasStarImport(this.importOnDemandThreshold, null)) {
				starImportPackages.add(pack.getName().toCharArray());
				for (int k= 0; k < pack.getNumberOfImports(); k++) {
					ImportDeclEntry curr= pack.getImportAt(k);
					if (!curr.isOnDemand() && !curr.isComment()) {
						simpleTypeNames.add(curr.getSimpleName().toCharArray());
					}
				}
			}
		}
		if (starImportPackages.isEmpty()) {
			return null;
		}
		
		starImportPackages.add(this.compilationUnit.getParent().getElementName().toCharArray());
		starImportPackages.add(JAVA_LANG.toCharArray());
		
		char[][] allPackages= (char[][]) starImportPackages.toArray(new char[starImportPackages.size()][]);
		char[][] allTypes= (char[][]) simpleTypeNames.toArray(new char[simpleTypeNames.size()][]);
		
		TypeNameRequestor requestor= new TypeNameRequestor() {
			HashMap foundTypes= new HashMap();
			
			private String getTypeContainerName(char[] packageName, char[][] enclosingTypeNames) {
				StringBuffer buf= new StringBuffer();
				buf.append(packageName);
				for (int i= 0; i < enclosingTypeNames.length; i++) {
					if (buf.length() > 0)
						buf.append('.');
					buf.append(enclosingTypeNames[i]);
				}
				return buf.toString();
			}
			
			public void acceptType(int modifiers, char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames, String path) {
				String name= new String(simpleTypeName);
				String containerName= getTypeContainerName(packageName, enclosingTypeNames);
				
				String oldContainer= (String) this.foundTypes.put(name, containerName);
				if (oldContainer != null && !oldContainer.equals(containerName)) {
					onDemandConflicts.add(name);
				}
			}
		};
		new SearchEngine().searchAllTypeNames(allPackages, allTypes, scope, requestor, IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, monitor);
		return onDemandConflicts;
	}
		
	private String getNewImportString(String importName, boolean isStatic, String lineDelim) {
		StringBuffer buf= new StringBuffer();
		buf.append("import "); //$NON-NLS-1$
		if (isStatic) {
			buf.append("static "); //$NON-NLS-1$
		}
		buf.append(importName);
		buf.append(';');
		buf.append(lineDelim);
		
		if (isStatic) {
			this.staticImportsCreated.add(importName);
		} else {
			this.importsCreated.add(importName);
		}
		return buf.toString();
	}
	
	private static int getFirstTypeBeginPos(CompilationUnit root) {
		List types= root.types();
		if (!types.isEmpty()) {
			return root.getExtendedStartPosition(((ASTNode) types.get(0)));
		}
		return -1;
	}
	
	private int getPackageStatementEndPos(CompilationUnit root) {
		PackageDeclaration packDecl= root.getPackage();
		if (packDecl != null) {
			int lineAfterPackage= root.getLineNumber(packDecl.getStartPosition() + packDecl.getLength()) + 1;
			int afterPackageStatementPos= root.getPosition(lineAfterPackage, 0);
			if (afterPackageStatementPos >= 0) {
				int firstTypePos= getFirstTypeBeginPos(root);
				if (firstTypePos != -1 && firstTypePos <= afterPackageStatementPos) {
					if (firstTypePos <= afterPackageStatementPos) {
						this.flags |= F_NEEDS_TRAILING_DELIM;
						if (firstTypePos == afterPackageStatementPos) {
							this.flags |= F_NEEDS_LEADING_DELIM;
						}
						return firstTypePos;
					}
				}
				this.flags |= F_NEEDS_LEADING_DELIM;
				return afterPackageStatementPos; // insert a line after after package statement
			}
		}
		this.flags |= F_NEEDS_TRAILING_DELIM;
		return 0;
	}
	
	public String toString() {
		int nPackages= this.packageEntries.size();
		StringBuffer buf= new StringBuffer("\n-----------------------\n"); //$NON-NLS-1$
		for (int i= 0; i < nPackages; i++) {
			PackageEntry entry= (PackageEntry) this.packageEntries.get(i);
			if (entry.isStatic()) {
				buf.append("static "); //$NON-NLS-1$
			}
			buf.append(entry.toString());
		}
		return buf.toString();	
	}
	
	private static final class ImportDeclEntry {
		
		private String elementName;
		private IRegion sourceRange;
		private final boolean isStatic;
		
		public ImportDeclEntry(String elementName, boolean isStatic, IRegion sourceRange) {
			this.elementName= elementName;
			this.sourceRange= sourceRange;
			this.isStatic= isStatic;
		}
				
		public String getElementName() {
			return this.elementName;
		}
		
		public int compareTo(String fullName, boolean isStaticImport) {
			int cmp= this.elementName.compareTo(fullName);
			if (cmp == 0) {
				if (this.isStatic == isStaticImport) {
					return 0;
				}
				return this.isStatic ? -1 : 1;
			}
			return cmp;
		}
		
		public String getSimpleName() {
			return Signature.getSimpleName(this.elementName);
		}		
		
		public boolean isOnDemand() {
			return this.elementName != null && this.elementName.endsWith(".*"); //$NON-NLS-1$
		}
		
		public boolean isStatic() {
			return this.isStatic;
		}
			
		public boolean isNew() {
			return this.sourceRange == null;
		}
		
		public boolean isComment() {
			return this.elementName == null;
		}
		
		public IRegion getSourceRange() {
			return this.sourceRange;
		}
				
	}
	
	/*
	 * Internal element for the import structure: A container for imports
	 * of all types from the same package
	 */
	private final static class PackageEntry {
		
		public static PackageEntry createOnPlaceholderEntry(String preferenceOrder) {
			if (preferenceOrder.length() > 0 && preferenceOrder.charAt(0) == '#') {
				String curr= preferenceOrder.substring(1);
				return new PackageEntry(curr, curr, true);
			}
			return new PackageEntry(preferenceOrder, preferenceOrder, false);
		}
		
		private String name;
		private ArrayList importEntries;
		private String group;
		private boolean isStatic;
	
		/**
		 * Comment package entry
		 */
		public PackageEntry() {
			this("!", null, false); //$NON-NLS-1$
		}
		
		/**
		 * @param name Name of the package entry. e.g. org.eclipse.jdt.ui, containing imports like
		 * org.eclipse.jdt.ui.JavaUI.
		 * @param group The index of the preference order entry assigned
		 *    different group id's will result in spacers between the entries
		 */
		public PackageEntry(String name, String group, boolean isStatic) {
			this.name= name;
			this.importEntries= new ArrayList(5);
			this.group= group;
			this.isStatic= isStatic;
		}
		
		public boolean isStatic() {
			return this.isStatic;
		}
		
		public int compareTo(String otherName, boolean isOtherStatic) {
			int cmp= this.name.compareTo(otherName);
			if (cmp == 0) {
				if (this.isStatic == isOtherStatic) {
					return 0;
				}
				return this.isStatic ? -1 : 1;
			}
			return cmp;
		}
						
		public void sortIn(ImportDeclEntry imp) {
			String fullImportName= imp.getElementName();
			int insertPosition= -1;
			int nInports= this.importEntries.size();
			for (int i= 0; i < nInports; i++) {
				ImportDeclEntry curr= getImportAt(i);
				if (!curr.isComment()) {
					int cmp= curr.compareTo(fullImportName, imp.isStatic());
					if (cmp == 0) {
						return; // exists already
					} else if (cmp > 0 && insertPosition == -1) {
						insertPosition= i;
					}
				}
			}
			if (insertPosition == -1) {
				this.importEntries.add(imp);
			} else {
				this.importEntries.add(insertPosition, imp);
			}
		}
		
		
		public void add(ImportDeclEntry imp) {
			this.importEntries.add(imp);
		}
		
		public ImportDeclEntry find(String simpleName) {
			int nInports= this.importEntries.size();
			for (int i= 0; i < nInports; i++) {
				ImportDeclEntry curr= getImportAt(i);
				if (!curr.isComment()) {
					String currName= curr.getElementName();
					if (currName.endsWith(simpleName)) {
						int dotPos= currName.length() - simpleName.length() - 1;
						if ((dotPos == -1) || (dotPos > 0 && currName.charAt(dotPos) == '.')) {
							return curr;
						}
					}						
				}
			}
			return null;
		}
		
		public boolean remove(String fullName, boolean isStaticImport) {
			int nInports= this.importEntries.size();
			for (int i= 0; i < nInports; i++) {
				ImportDeclEntry curr= getImportAt(i);
				if (!curr.isComment() && curr.compareTo(fullName, isStaticImport) == 0) {
					this.importEntries.remove(i);
					return true;
				}
			}
			return false;
		}
		
		public void removeAllNew(Set onDemandConflicts) {
			int nInports= this.importEntries.size();
			for (int i= nInports - 1; i >= 0; i--) {
				ImportDeclEntry curr= getImportAt(i);
				if (curr.isNew() /*&& (onDemandConflicts == null || onDemandConflicts.contains(curr.getSimpleName()))*/) {
					this.importEntries.remove(i);
				}
			}
		}
		
		public ImportDeclEntry getImportAt(int index) {
			return (ImportDeclEntry) this.importEntries.get(index);
		}
		
		public boolean hasStarImport(int threshold, Set explicitImports) {
			if (isComment() || isDefaultPackage()) { // can not star import default package
				return false;
			}
			int nImports= getNumberOfImports();
			int count= 0;
			boolean containsNew= false;
			for (int i= 0; i < nImports; i++) {
				ImportDeclEntry curr= getImportAt(i);
				if (curr.isOnDemand()) {
					return true;
				}
				if (!curr.isComment()) {
					count++;
					boolean isExplicit= !curr.isStatic() && (explicitImports != null) && explicitImports.contains(curr.getSimpleName());
					containsNew |= curr.isNew() && !isExplicit;
				}
			}
			return (count >= threshold) && containsNew;
		}
		
		public int getNumberOfImports() {
			return this.importEntries.size();
		}	
			
		public String getName() {
			return this.name;
		}
		
		public String getGroupID() {
			return this.group;
		}
		
		public void setGroupID(String groupID) {
			this.group= groupID;
		}
		
		public boolean isSameGroup(PackageEntry other) {
			if (this.group == null) {
				return other.getGroupID() == null;
			} else {
				return this.group.equals(other.getGroupID()) && (this.isStatic == other.isStatic());
			}
		}		
				
		public ImportDeclEntry getLast() {
			int nImports= getNumberOfImports();
			if (nImports > 0) {
				return getImportAt(nImports - 1);
			}
			return null;
		}
		
		public boolean isComment() {
			return "!".equals(this.name); //$NON-NLS-1$
		}
		
		public boolean isDefaultPackage() {
			return this.name.length() == 0;
		}
		
		public String toString() {
			StringBuffer buf= new StringBuffer();
			if (isComment()) {
				buf.append("comment\n"); //$NON-NLS-1$
			} else {
				buf.append(this.name); buf.append(", groupId: "); buf.append(this.group); buf.append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
				int nImports= getNumberOfImports();
				for (int i= 0; i < nImports; i++) {
					ImportDeclEntry curr= getImportAt(i);
					buf.append("  "); //$NON-NLS-1$
					if (curr.isStatic()) {
						buf.append("static "); //$NON-NLS-1$
					}
					buf.append(curr.getSimpleName());
					if (curr.isNew()) {
						buf.append(" (new)"); //$NON-NLS-1$
					}
					buf.append("\n"); //$NON-NLS-1$
				}
			}
			return buf.toString();
		}
	}	
	
	public String[] getCreatedImports() {
	    return (String[]) this.importsCreated.toArray(new String[this.importsCreated.size()]);
	}
	
	public String[] getCreatedStaticImports() {
	    return (String[]) this.staticImportsCreated.toArray(new String[this.staticImportsCreated.size()]);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6114.java