error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12763.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12763.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12763.java
text:
```scala
S@@tringTokenizer st = new StringTokenizer(handle, ProgramElement.ID_DELIM);

/* *******************************************************************
 * Copyright (c) 2003 Contributors.
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Mik Kersten     initial implementation 
 * ******************************************************************/


package org.aspectj.asm.internal;

import java.io.*;
import java.util.*;

import org.aspectj.asm.*;
import org.aspectj.asm.IProgramElement.Kind;
import org.aspectj.bridge.*;

/**
 * @author Mik Kersten
 */
public class AspectJElementHierarchy implements IHierarchy {
	
    protected  IProgramElement root = null;
    protected String configFile = null;

    private Map fileMap = null;
    
	public IProgramElement getElement(String handle) {
		throw new RuntimeException("unimplemented");
	}

    public IProgramElement getRoot() {
        return root;
    }

    public void setRoot(IProgramElement root) {
        this.root = root;
    }

	public void addToFileMap( Object key, Object value ){
		fileMap.put( key, value );
	}

	public void setFileMap(HashMap fileMap) {
		  this.fileMap = fileMap;
	  }

	public Object findInFileMap( Object key ) {
		return fileMap.get(key);
	}


	public Set getFileMapEntrySet() {
		return fileMap.entrySet();
	}
	
	public boolean isValid() {
        return root != null && fileMap != null;
    }
 
	/** 
	 * Returns the first match
	 * 
	 * @param parent
	 * @param kind		not null
	 * @return null if not found
	 */
	public IProgramElement findElementForSignature(IProgramElement parent, IProgramElement.Kind kind, String signature) {
		for (Iterator it = parent.getChildren().iterator(); it.hasNext(); ) {
			IProgramElement node = (IProgramElement)it.next();
			if (node.getKind() == kind && signature.equals(node.toSignatureString())) {
				return node;
			} else {
				IProgramElement childSearch = findElementForSignature(node, kind, signature);
				if (childSearch != null) return childSearch;
			}
		}
		return null;
	}
	
	public IProgramElement findElementForLabel(
		IProgramElement parent,
		IProgramElement.Kind kind,
		String label) {
			
		for (Iterator it = parent.getChildren().iterator(); it.hasNext(); ) {
			IProgramElement node = (IProgramElement)it.next();
			if (node.getKind() == kind && label.equals(node.toLabelString())) {
				return node;
			} else {
				IProgramElement childSearch = findElementForSignature(node, kind, label);
				if (childSearch != null) return childSearch;
			}
		}
		return null;			
	}
	
	/**
	 * @param packageName	if null default package is searched
	 * @param className 	can't be null
	 */ 
	public IProgramElement findElementForType(String packageName, String typeName) {
		IProgramElement packageNode = null;
		if (packageName == null) {
			packageNode = root;
		} else {
			for (Iterator it = root.getChildren().iterator(); it.hasNext(); ) {
				IProgramElement node = (IProgramElement)it.next();
				if (packageName.equals(node.getName())) {
					packageNode = node;
				} 
			}
			if (packageNode == null) return null;
		}
		
		// this searches each file for a class
		for (Iterator it = packageNode.getChildren().iterator(); it.hasNext(); ) {
			IProgramElement fileNode = (IProgramElement)it.next();
			IProgramElement ret = findClassInNodes(fileNode.getChildren(), typeName);
			if (ret != null) return ret;
		}
		
		return null;
	}
	
	private IProgramElement findClassInNodes(Collection nodes, String name) {
		String baseName;
		String innerName;
		int dollar = name.indexOf('$');
		if (dollar == -1) {
			baseName = name;
			innerName = null;
		} else {
			baseName = name.substring(0, dollar);
			innerName = name.substring(dollar+1);
		}
		
		for (Iterator j = nodes.iterator(); j.hasNext(); ) {
			IProgramElement classNode = (IProgramElement)j.next();
			if (baseName.equals(classNode.getName())) {
				if (innerName == null) return classNode;
				else return findClassInNodes(classNode.getChildren(), innerName);
			} else if (name.equals(classNode.getName())) {
				return classNode;
			}
		}
		return null;
	}


	/**
	 * @param		sourceFilePath	modified to '/' delimited path for consistency
	 * @return		a new structure node for the file if it was not found in the model
	 */
	public IProgramElement findElementForSourceFile(String sourceFile) {
       	try {
	       	if (!isValid() || sourceFile == null) {   
	            return IHierarchy.NO_STRUCTURE;
	        } else {
	            String correctedPath = new File(sourceFile).getCanonicalPath();//.replace('\\', '/');
	            //StructureNode node = (StructureNode)getFileMap().get(correctedPath);//findFileNode(filePath, model);
				IProgramElement node = (IProgramElement)findInFileMap(correctedPath);//findFileNode(filePath, model);
	            if (node != null) {
	                return node;
	            } else {
	                return createFileStructureNode(correctedPath);
	            }
	        }
		} catch (Exception e) {
			return IHierarchy.NO_STRUCTURE;
		}
    }

	/**
	 * TODO: discriminate columns
	 */
	public IProgramElement findElementForSourceLine(ISourceLocation location) {
		try {
			return findElementForSourceLine(location.getSourceFile().getCanonicalPath(), location.getLine());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Never returns null 
	 * 
	 * @param		sourceFilePath	canonicalized path for consistency
	 * @param 		lineNumber		if 0 or 1 the corresponding file node will be returned
	 * @return		a new structure node for the file if it was not found in the model
	 */
	public IProgramElement findElementForSourceLine(String sourceFilePath, int lineNumber) {
		IProgramElement node = findNodeForSourceLineHelper(root, sourceFilePath, lineNumber);
		if (node != null) {
			return node;	
		} else {
			return createFileStructureNode(sourceFilePath);
		}
	}

	private IProgramElement createFileStructureNode(String sourceFilePath) {
		String fileName = new File(sourceFilePath).getName();
		IProgramElement fileNode = new ProgramElement(fileName, IProgramElement.Kind.FILE_JAVA, null);
		fileNode.setSourceLocation(new SourceLocation(new File(sourceFilePath), 1, 1));
		fileNode.addChild(NO_STRUCTURE);
		return fileNode;
	}


	private IProgramElement findNodeForSourceLineHelper(IProgramElement node, String sourceFilePath, int lineNumber) {
		if (matches(node, sourceFilePath, lineNumber) 
			&& !hasMoreSpecificChild(node, sourceFilePath, lineNumber)) {
			return node;	
		} 
		
		if (node != null && node.getChildren() != null) {
			for (Iterator it = node.getChildren().iterator(); it.hasNext(); ) {
				IProgramElement foundNode = findNodeForSourceLineHelper(
					(IProgramElement)it.next(), 
					sourceFilePath, 
					lineNumber); 		
				if (foundNode != null) return foundNode;
			}
		}
		
		return null;		
	}

	private boolean matches(IProgramElement node, String sourceFilePath, int lineNumber) {
		try {			
//			if (node != null && node.getSourceLocation() != null)
//				System.err.println("====\n1: " + 
//					sourceFilePath + "\n2: " +
//					node.getSourceLocation().getSourceFile().getCanonicalPath().equals(sourceFilePath)
//				);				
			return node != null 
				&& node.getSourceLocation() != null
				&& node.getSourceLocation().getSourceFile().getCanonicalPath().equals(sourceFilePath)
				&& ((node.getSourceLocation().getLine() <= lineNumber
					&& node.getSourceLocation().getEndLine() >= lineNumber)

					(lineNumber <= 1
					 && node.getKind().isSourceFileKind())
				);
		} catch (IOException ioe) { 
			return false;
		} 
	}
	
	private boolean hasMoreSpecificChild(IProgramElement node, String sourceFilePath, int lineNumber) {
		for (Iterator it = node.getChildren().iterator(); it.hasNext(); ) {
			IProgramElement child = (IProgramElement)it.next();
			if (matches(child, sourceFilePath, lineNumber)) return true;
		}
		return false;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	
	// TODO: optimize this lookup
	public IProgramElement findElementForHandle(String handle) {
		StringTokenizer st = new StringTokenizer(handle, IProgramElement.ID_DELIM);
		String file = st.nextToken();
		int line = new Integer(st.nextToken()).intValue();
		int col = new Integer(st.nextToken()).intValue();
		// TODO: use column number when available
		return findElementForSourceLine(file, line);
		
//		IProgramElement parent = findElementForType(packageName, typeName);
//		if (parent == null) return null;
//		if (kind == IProgramElement.Kind.CLASS ||
//			kind == IProgramElement.Kind.ASPECT) {
//				return parent;
//		} else {
//			return findElementForSignature(parent, kind, name);	
//		}	
	}
//	
//	private IProgramElement findElementForBytecodeInfo(
//		IProgramElement node, 
//		String parentName,
//		String name, 
//		String signature) {
//		for (Iterator it = node.getChildren().iterator(); it.hasNext(); ) {
//			IProgramElement curr = (IProgramElement)it.next();
//			if (parentName.equals(curr.getParent().getBytecodeName())
//				&& name.equals(curr.getBytecodeName()) 
//				&& signature.equals(curr.getBytecodeSignature())) {
//				return node;
//			} else {
//				IProgramElement childSearch = findElementForBytecodeInfo(curr, parentName, name, signature);
//				if (childSearch != null) return childSearch;
//			}
//		}
//		return null;
//	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12763.java