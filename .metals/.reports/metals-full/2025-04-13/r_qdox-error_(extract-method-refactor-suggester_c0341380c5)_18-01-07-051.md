error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5781.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5781.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,3]

error in qdox parser
file content:
```java
offset: 3
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5781.java
text:
```scala
+ (@@origDef!=null?origDef.getName():"") + "'", origDef));

/*******************************************************************************
 * Copyright (c) 2005, 2009 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

package org.eclipse.internal.xpand2.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.internal.xpand2.model.XpandAdvice;
import org.eclipse.internal.xpand2.model.XpandDefinition;
import org.eclipse.internal.xpand2.model.XpandResource;
import org.eclipse.internal.xtend.expression.ast.SyntaxElement;
import org.eclipse.xpand2.XpandExecutionContext;
import org.eclipse.xpand2.XpandUtil;
import org.eclipse.xtend.expression.AnalysationIssue;

/**
 * *
 * 
 * @author Sven Efftinge (http://www.efftinge.de) *
 */
public class Template extends SyntaxElement implements XpandResource {
    private ImportDeclaration[] imports;

    private Definition[] definitions;

    private String fullyQualifiedName;

    private ImportDeclaration[] extensions;

    private Advice[] advices;

    public ImportDeclaration[] getExtensions() {
        return extensions;
    }
    
	public List<String> getImportedExtensionsAsList() {
        final List<String> result = new ArrayList<String>();
		for (ImportDeclaration ext : extensions) {
            result.add(ext.getImportString().getValue());
        }
        return result;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public void setFullyQualifiedName(final String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

	public Template(final ImportDeclaration[] imports, final ImportDeclaration[] extensions,
			final Definition[] definitions, final Advice[] advices) {
        this.imports = imports;
        this.extensions = extensions;
        for (int i = 0; i < definitions.length; i++) {
            definitions[i].setOwner(this);
        }
        this.definitions = definitions;
        for (int i = 0; i < advices.length; i++) {
            advices[i].setOwner(this);
        }
        this.advices = advices;
    }

    public XpandDefinition[] getDefinitions() {
        return definitions;
    }
    
    public List<XpandDefinition> getDefinitionsAsList() {
        return Arrays.asList((XpandDefinition[]) definitions);
    }
    
    public AbstractDefinition[] getAllDefinitions() {
        List<AbstractDefinition> l = new ArrayList<AbstractDefinition>();
        l.addAll(Arrays.asList(definitions));
        l.addAll(Arrays.asList(advices));
        
		Collections.sort(l, new Comparator<SyntaxElement>() {

            public int compare(SyntaxElement o1, SyntaxElement o2) {
                return new Integer(o1.getStart()).compareTo(o2.getStart());
			}
		});
        return l.toArray(new AbstractDefinition[l.size()]);
    }

    public ImportDeclaration[] getImports() {
        return imports;
    }

    public List<ImportDeclaration> getImportsAsList() {
        return Arrays.asList(imports);
    }
    
    private String[] commonPrefixes = null;

    public void analyze(XpandExecutionContext ctx, final Set<AnalysationIssue> issues) {
    	try {
	        ctx = (XpandExecutionContext) ctx.cloneWithResource(this);
			if (ctx.getCallback() != null) {
				if(!ctx.getCallback().pre(this, ctx)) {
					return;
				}
			}

			checkDuplicateDefinitions(issues);
	        for (int i = 0; i < definitions.length; i++) {
	            definitions[i].analyze(ctx, issues);
	        }

	        for (int i = 0; i < advices.length; i++) {
	            advices[i].analyze(ctx, issues);
	        }
    	}
		catch (RuntimeException ex) {
			issues.add(new AnalysationIssue(AnalysationIssue.INTERNAL_ERROR, ex.getMessage(), this));
    }
		finally {
			if (ctx.getCallback() != null) {
				ctx.getCallback().post(this, ctx, null);
			}
		}
	}

    public XpandDefinition[] getDefinitionsByName(final String aName) {
        final List<Definition> defs = new ArrayList<Definition>();
        for (int i = 0; i < definitions.length; i++) {
            final Definition def = definitions[i];
            if (def.getName().equals(aName)) {
                defs.add(def);
            }
        }
        return defs.toArray(new XpandDefinition[defs.size()]);
    }

    public String[] getImportedNamespaces() {
        if (commonPrefixes == null) {
            final List<String> l = new ArrayList<String>();
            final String thisNs = XpandUtil.withoutLastSegment(getFullyQualifiedName());

            for (int i = 0; i < getImports().length; i++) {
                final ImportDeclaration anImport = getImports()[i];
                l.add(anImport.getImportString().getValue());
            }

			if (thisNs != null) {
				l.add(thisNs);
			}

            commonPrefixes = l.toArray(new String[l.size()]);
        }
        return commonPrefixes;
    }

    public List<String> getImportedNamespacesAsList() {
        return Arrays.asList(getImportedNamespaces());
    }
    
    String[] importedExtensions = null;

    public String[] getImportedExtensions() {
        if (importedExtensions == null) {
            final List<String> l = new ArrayList<String>();
            for (int i = 0; i < getExtensions().length; i++) {
                final ImportDeclaration anImport = getExtensions()[i];
                l.add(anImport.getImportString().getValue());
            }
            importedExtensions = l.toArray(new String[l.size()]);
        }
        return importedExtensions;
    }

    public XpandAdvice[] getAdvices() {
        return advices;
    }

	private void checkDuplicateDefinitions(Set<AnalysationIssue> issues) {
		Set<Definition> definitionSet = new HashSet<Definition>();
		for (Definition def : definitions) {
			if (!definitionSet.contains(def)) {
				definitionSet.add(def);
			}
			else {
				Definition origDef = null;
				for (Iterator<Definition> it = definitionSet.iterator(); it.hasNext();) {
					Definition d = it.next();
					if (d.equals(def)) {
						origDef = d;
						break;
					}
				}
				issues.add(new AnalysationIssue(AnalysationIssue.INTERNAL_ERROR, "Duplicate definition '"
						+ def.getName() + "'", def));
				issues.add(new AnalysationIssue(AnalysationIssue.INTERNAL_ERROR, "Duplicate definition '"
						+ origDef.getName() + "'", origDef));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5781.java