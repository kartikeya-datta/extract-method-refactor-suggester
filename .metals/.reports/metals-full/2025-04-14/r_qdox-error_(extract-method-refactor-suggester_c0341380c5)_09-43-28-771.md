error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7485.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7485.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7485.java
text:
```scala
r@@eturn getKind().equals("interface") || getKind().equals("class") || getKind().equals("aspect") || getKind().equals("enum");

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
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.aspectj.asm.IProgramElement;

/**
 * @author Mik Kersten
 * @deprecated org.aspectj.asm.IProgramElement should be used instead
 */
public class Declaration implements Serializable {
    private int beginLine;
    private int endLine;
    private int beginColumn;
    private int endColumn;

    private String modifiers;
    private String fullSignature;
    private String signature;
    private String crosscutDesignator;

    private String packageName;

    private String kind;
    private String declaringType;

    private String filename;
    private String formalComment;

    private Declaration[] declarations;

    private Handle crosscutDeclarationHandle;
    private Handle[] pointedToByHandles;
    private Handle[] pointsToHandles;

    transient private Declaration crosscutDeclaration;
    transient private Declaration[] pointedToBy = null;
    transient private Declaration[] pointsTo = null;

    private Declaration parentDeclaration = null;
	private IProgramElement node;

    public Declaration(int beginLine, int endLine, int beginColumn, int endColumn,
                       String modifiers, String signature, String fullSignature,
                       String crosscutDesignator,
                       String declaringType, String kind,
                       String filename, String formalComment,
                       String packageName,
					   IProgramElement node)
    {
        this.beginLine = beginLine;
        this.endLine = endLine;
        this.beginColumn = beginColumn;
        this.endColumn = endColumn;

        this.modifiers = modifiers;
        this.signature = signature;
        this.fullSignature = fullSignature;

        this.crosscutDesignator = crosscutDesignator;

        this.declaringType = declaringType;
        this.kind = kind;

        this.filename = filename;
        this.formalComment = formalComment;

        this.packageName = packageName;

        this.pointedToByHandles = new Handle[0];
        this.pointsToHandles = new Handle[0];
        //???
        this.declarations = new Declaration[0];
        this.node = node;
    }

    public int getBeginLine() { return beginLine; }
    public int getEndLine() { return endLine; }
    public int getBeginColumn() { return beginColumn; }
    public int getEndColumn() { return endColumn; }

    public String getModifiers() { return modifiers; }
    public String getFullSignature() { return fullSignature; }
    public String getSignature() { return signature; }

    public String getPackageName() { return packageName; }

    public String getCrosscutDesignator() { return crosscutDesignator; }

    public Declaration getParentDeclaration() { return parentDeclaration; }

    public Declaration getCrosscutDeclaration() {
        if (crosscutDeclaration == null && crosscutDeclarationHandle != null) {
            crosscutDeclaration = crosscutDeclarationHandle.resolve();
        }
        return crosscutDeclaration;
    }

    public void setCrosscutDeclaration(Declaration _crosscutDeclaration) {
        crosscutDeclaration = _crosscutDeclaration;
    }

    public String getDeclaringType() { return declaringType; }
    public String getKind() {
        if (kind.startsWith("introduced-")) {
            return kind.substring(11);
        } else {
            return kind;
        }
    }

    public String getFilename() { return filename; }
    public String getFormalComment() { return formalComment; }

    public Declaration[] getDeclarations() {
        return declarations;
    }
    public void setDeclarations(Declaration[] decs) {
        declarations = decs;
        if (decs != null) {
            for (int i = 0; i < decs.length; i++) {
                decs[i].parentDeclaration = this;
            }
        }
    }
    public void setPointedToBy(Declaration[] decs) { pointedToBy = decs; }
    public void setPointsTo(Declaration[] decs) { pointsTo = decs; }


    public Declaration[] getPointedToBy() {
        if (pointedToBy == null) {
            pointedToBy = resolveHandles(pointedToByHandles);
        }
        return pointedToBy; //.elements();
    }

    public Declaration[] getPointsTo() {
        if (pointsTo == null) {
            pointsTo = resolveHandles(pointsToHandles);
        }
        return pointsTo; //.elements();
    }

    private Declaration[] filterTypes(Declaration[] a_decs) {
        List decs = new LinkedList(Arrays.asList(a_decs));
        for(Iterator i = decs.iterator(); i.hasNext(); ) {
            Declaration dec = (Declaration)i.next();
            if (!dec.isType()) i.remove();
        }
        return (Declaration[])decs.toArray(new Declaration[decs.size()]);
    }


    public Declaration[] getTargets() {
        Declaration[] pointsTo = getPointsTo();

        if (kind.equals("advice")) {
            return pointsTo;
        } else if (kind.equals("introduction")) {
            return filterTypes(pointsTo);
        } else {
            return new Declaration[0];
        }
    }

    // Handles are used to deal with dependencies between Declarations in different files
    private Handle getHandle() {
        return new Handle(filename, beginLine, beginColumn);
    }

    private Declaration[] resolveHandles(Handle[] handles) {
        Declaration[] declarations = new Declaration[handles.length];
        int missed = 0;
        for(int i=0; i<handles.length; i++) {
            //if (handles[i] == null) continue;
            declarations[i] = handles[i].resolve();
            if (declarations[i] == null) missed++;
        }
        if (missed > 0) {
            Declaration[] decs = new Declaration[declarations.length - missed];
            for (int i=0, j=0; i < declarations.length; i++) {
                if (declarations[i] != null) decs[j++] = declarations[i];
            }
            declarations = decs;
        }
        return declarations;
    }

    private Handle[] getHandles(Declaration[] declarations) {
        Handle[] handles = new Handle[declarations.length];
        for(int i=0; i<declarations.length; i++) {
            //if (declarations[i] == null) continue;
            handles[i] = declarations[i].getHandle();
        }
        return handles;
    }

    // Make sure that all decs are convertted to handles before serialization
    private void writeObject(ObjectOutputStream out) throws IOException {
        pointedToByHandles = getHandles(getPointedToBy());
        pointsToHandles = getHandles(getPointsTo());
        if (crosscutDeclaration != null) {
           crosscutDeclarationHandle = crosscutDeclaration.getHandle();
        }
        out.defaultWriteObject();
    }

    // support functions
    public Declaration[] getCrosscutDeclarations() {
        return getDeclarationsHelper("pointcut");
    }

    public Declaration[] getAdviceDeclarations() {
        return getDeclarationsHelper("advice");
    }

    public Declaration[] getIntroductionDeclarations() {
        return getDeclarationsHelper("introduction");
    }

    private Declaration[] getDeclarationsHelper(String kind) {
        Declaration[] decls  = getDeclarations();
        List result = new ArrayList();
        for ( int i = 0; i < decls.length; i++ ) {
            Declaration decl = decls[i];
            if ( decl.getKind().equals(kind) ) {
                result.add(decl);
            }
        }
        return (Declaration[])result.toArray(new Declaration[result.size()]);
    }


    public boolean isType() {
        return getKind().equals("interface") || getKind().equals("class") || getKind().equals("aspect");
    }

    public boolean hasBody() {
        String kind = getKind();
        return kind.equals("class") || kind.endsWith("constructor") ||
            (kind.endsWith("method") && getModifiers().indexOf("abstract") == -1 &&
              getModifiers().indexOf("native") == -1);
    }  

    public boolean isIntroduced() {
        return kind.startsWith("introduced-");
    }

    public boolean hasSignature() {
        String kind = getKind();
        if ( kind.equals( "class" ) ||
             kind.equals( "interface" ) ||
             kind.equals( "initializer" ) ||
             kind.equals( "field" ) ||
             kind.equals( "constructor" ) ||
             kind.equals( "method" ) ) {
            return true;
        }
        else {
            return false;
        }
    }

    private static class Handle implements Serializable {
        public String filename;
        public int line, column;

        public Handle(String filename, int line, int column) {
            this.filename = filename;
            this.line = line;
            this.column = column;
        }

        public Declaration resolve() {
            SymbolManager manager = SymbolManager.getDefault();
            return manager.getDeclarationAtPoint(filename, line, column);
        }
    }
	public IProgramElement getNode() {
		return node;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7485.java