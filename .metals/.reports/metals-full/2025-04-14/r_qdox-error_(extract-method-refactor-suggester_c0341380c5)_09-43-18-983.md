error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12960.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12960.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12960.java
text:
```scala
i@@f (packageName != null && packageName != "") {

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 *     Mik Kersten	  port to AspectJ 1.1+ code base
 * ******************************************************************/

package org.aspectj.tools.ajdoc;

import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.aspectj.asm.AsmManager;
import org.aspectj.asm.IProgramElement;

/**
 * @author Mik Kersten
 */
class StubFileGenerator {

    static Hashtable declIDTable = null;

    static void doFiles(Hashtable table,
                        SymbolManager symbolManager,
                        File[] inputFiles,
                        File[] signatureFiles) {
        declIDTable = table;
        for (int i = 0; i < inputFiles.length; i++) {
            processFile(symbolManager, inputFiles[i], signatureFiles[i]);
        }
    }

    static void processFile(SymbolManager symbolManager, File inputFile, File signatureFile) {
        try {
        	String path = StructureUtil.translateAjPathName(signatureFile.getCanonicalPath());
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path)));
             
            String packageName = StructureUtil.getPackageDeclarationFromFile(inputFile);
            
            if (packageName != null ) {
                writer.println( "package " + packageName + ";" );
            }

           	IProgramElement fileNode = (IProgramElement)AsmManager.getDefault().getHierarchy().findElementForSourceFile(inputFile.getAbsolutePath());
        	for (Iterator it = fileNode.getChildren().iterator(); it.hasNext(); ) {
        		IProgramElement node = (IProgramElement)it.next();
        		if (node.getKind().equals(IProgramElement.Kind.IMPORT_REFERENCE)) {
        			processImportDeclaration(node, writer);
        		} else {
        			processTypeDeclaration(node, writer);
        		}
        	}
           	
            // if we got an error we don't want the contents of the file
            writer.close(); 
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } 
    }

    private static void processImportDeclaration(IProgramElement node, PrintWriter writer) throws IOException {
    	List imports = node.getChildren();
    	for (Iterator i = imports.iterator(); i.hasNext();) {
			IProgramElement importNode = (IProgramElement) i.next();
			writer.print("import ");
			writer.print(importNode.getName());
			writer.println(';');
		}  	 
    }
    
    private static void processTypeDeclaration(IProgramElement classNode, PrintWriter writer) throws IOException {
    	
    	String formalComment = addDeclID(classNode, classNode.getFormalComment());
    	writer.println(formalComment);
    	
    	String signature = genSourceSignature(classNode);// StructureUtil.genSignature(classNode);
    	
//    	System.err.println("######" + signature + ", " + classNode.getName());
    	if (!StructureUtil.isAnonymous(classNode) && !classNode.getName().equals("<undefined>")) {
	    	writer.println(signature + " {" );
	    	processMembers(classNode.getChildren(), writer, classNode.getKind().equals(IProgramElement.Kind.INTERFACE));
	    	writer.println();
			writer.println("}");
    	}
    }

	private static void processMembers(List/*IProgramElement*/ members, PrintWriter writer, boolean declaringTypeIsInterface) throws IOException {
    	for (Iterator it = members.iterator(); it.hasNext();) {
			IProgramElement member = (IProgramElement) it.next();
		
	    	if (member.getKind().isType()) { 
	    		if (!member.getParent().getKind().equals(IProgramElement.Kind.METHOD)
	    			 && !StructureUtil.isAnonymous(member)) {// don't print anonymous types
//	    			System.err.println(">>>>>>>>>>>>>" + member.getName() + "<<<<" + member.getParent());
	    			processTypeDeclaration(member, writer);
	    		}
			} else {
		    	String formalComment = addDeclID(member, member.getFormalComment());;
		    	writer.println(formalComment);
		    	
		    	String signature = ""; 
		    	if (!member.getKind().equals(IProgramElement.Kind.POINTCUT)
		    	    && !member.getKind().equals(IProgramElement.Kind.ADVICE)) {
		    		signature = member.getSourceSignature();//StructureUtil.genSignature(member);
		    	} 
		    	
		    	if (member.getKind().isDeclare()) {
		    		System.err.println("> Skipping declare (ajdoc limitation): " + member.toLabelString());
		    	} else if (signature != null &&
		    		signature != "" && 
		    		!member.getKind().isInterTypeMember() &&
					!member.getKind().equals(IProgramElement.Kind.INITIALIZER) &&
					!StructureUtil.isAnonymous(member)) {   
		    		writer.print(signature);
		    	} else {
//		    		System.err.println(">> skipping: " + member.getKind());
		    	}  
		      
		    	if (member.getKind().equals(IProgramElement.Kind.METHOD) ||
		    		member.getKind().equals(IProgramElement.Kind.CONSTRUCTOR)) {
		    		if (member.getParent().getKind().equals(IProgramElement.Kind.INTERFACE) ||
		    			signature.indexOf("abstract ") != -1) {
		    			writer.println(";");
		    		} else {
		    			writer.println(" { }");
		    		}
		    		
		    	} else if (member.getKind().equals(IProgramElement.Kind.FIELD)) {
//		    		writer.println(";");
		    	}
			}
		}
    }

    /**
     * Translates "aspect" to "class", as long as its not ".aspect"
     */
    private static String genSourceSignature(IProgramElement classNode) {
    	String signature = classNode.getSourceSignature();
    	int index = signature.indexOf("aspect");
    	if (index != -1 && signature.charAt(index-1) != '.') {
    		signature = signature.substring(0, index) +
			"class " +
			signature.substring(index + 6, signature.length());
    	}
    	return signature;
	}
	
    static int nextDeclID = 0;
    static String addDeclID(IProgramElement decl, String formalComment) {
        String declID = "" + ++nextDeclID;
        declIDTable.put(declID, decl);
        return addToFormal(formalComment, Config.DECL_ID_STRING + declID + Config.DECL_ID_TERMINATOR);
    }

    /**
     * We want to go:
     *   just before the first period
     *   just before the first @
     *   just before the end of the comment
     *
     * Adds a place holder for the period ('#') if one will need to be
     * replaced.
     */
    static String addToFormal(String formalComment, String string) {
        boolean appendPeriod = true;
        if ( (formalComment == null) || formalComment.equals("")) {
            //formalComment = "/**\n * . \n */\n";
            formalComment = "/**\n * \n */\n";
            appendPeriod = false;
        }
        formalComment = formalComment.trim();

        int atsignPos = formalComment.indexOf('@');
        int    endPos = formalComment.indexOf("*/");
        int periodPos = formalComment.indexOf("/**")+2;
        int position  = 0;
        String periodPlaceHolder = "";
        if ( periodPos != -1 ) {
            position = periodPos+1;
        }
        else if ( atsignPos != -1 ) {
            string = string + "\n * ";
            position = atsignPos;
        }
        else if ( endPos != -1 ) {
            string = "* " + string + "\n";
            position = endPos;
        }
        else {
            // !!! perhaps this error should not be silent
            throw new Error("Failed to append to formal comment for comment: " +
                formalComment );
        }

        return
            formalComment.substring(0, position) + periodPlaceHolder +
            string +
            formalComment.substring(position);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12960.java