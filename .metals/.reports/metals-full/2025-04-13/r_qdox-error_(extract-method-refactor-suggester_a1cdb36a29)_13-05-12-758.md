error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6672.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6672.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6672.java
text:
```scala
.@@compile("context\\s+([\\[\\]:\\w\\]]+)(#|\\s+)[^;]*\\z");

/*******************************************************************************
 * Copyright (c) 2005, 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

package org.eclipse.internal.xtend.check.codeassist;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.internal.xtend.expression.codeassist.LazyVar;
import org.eclipse.internal.xtend.xtend.codeassist.FastAnalyzer;
import org.eclipse.internal.xtend.xtend.codeassist.Partition;
import org.eclipse.xtend.expression.AnalysationIssue;
import org.eclipse.xtend.expression.ExecutionContext;
import org.eclipse.xtend.expression.ExpressionFacade;
import org.eclipse.xtend.expression.Resource;
import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtend.typesystem.ParameterizedType;
import org.eclipse.xtend.typesystem.Type;

public class CheckFastAnalyzer {

    private final static Pattern VALIDATE_PATTERN = Pattern
            .compile("context\\s+([\\[\\]:\\w\\]]+)\\s+[^;]*\\z");

    private final static Pattern TYPEDECL_PATTERN = Pattern.compile("context\\s+[\\[\\]:\\w\\]]*\\z");

    private CheckFastAnalyzer() {
    }

    protected static boolean isTypeDeclaration(final String s) {
        final Matcher m = TYPEDECL_PATTERN.matcher(s);
        return m.find();
    }

    public final static Stack<Set<LazyVar>> computeStack(final String toAnalyze) {
        final Matcher m = VALIDATE_PATTERN.matcher(toAnalyze);
        final Stack<Set<LazyVar>> stack = new Stack<Set<LazyVar>>();
        if (!m.find())
            return stack;
        final Set<LazyVar> vars = new HashSet<LazyVar>();
        stack.push(vars);
        final LazyVar v = new LazyVar();
        v.typeName = m.group(1);
        v.name = ExecutionContext.IMPLICIT_VARIABLE;
        vars.add(v);
        return stack;
    }

    public final static Partition computePartition(final String str) {
        if (FastAnalyzer.isInsideImport(str))
            return Partition.NAMESPACE_IMPORT;

        if (FastAnalyzer.isInsideExtensionImport(str))
            return Partition.EXTENSION_IMPORT;

        if (isTypeDeclaration(str))
            return Partition.TYPE_DECLARATION;

        final Stack<Set<LazyVar>> s = computeStack(str);
        if (!s.isEmpty())
            return Partition.EXPRESSION;

        return Partition.DEFAULT;
    }

    public final static ExecutionContext computeExecutionContext(final String str, ExecutionContext ctx) {
        final Partition p = computePartition(str);
        if (p == Partition.EXPRESSION || p == Partition.TYPE_DECLARATION) {

            final List<String> imports = FastAnalyzer.findImports(str);
            final List<String> extensionImports = FastAnalyzer.findExtensions(str);
            final Resource res = new Resource() {

                private String fqn;

                public String getFullyQualifiedName() {
                    return fqn;
                }

                public void setFullyQualifiedName(String fqn) {
                    this.fqn = fqn;
                }

                public String[] getImportedNamespaces() {
                    return imports.toArray(new String[imports.size()]);
                }

                public String[] getImportedExtensions() {
                    return extensionImports.toArray(new String[extensionImports.size()]);
                }

            };

            ctx = ctx.cloneWithResource(res);

            for (final Iterator<Set<LazyVar>> iter = computeStack(str).iterator(); iter.hasNext();) {
                final Set<LazyVar> vars = iter.next();
                for (final Iterator<LazyVar> iterator = vars.iterator(); iterator.hasNext();) {
                    final LazyVar v = iterator.next();
                    Type vType = null;
                    if (v.typeName != null) {
                        vType = ctx.getTypeForName(v.typeName);
                    } else {
                        vType = new ExpressionFacade(ctx).analyze(v.expression, new HashSet<AnalysationIssue>());
                        if (v.forEach) {
                            if (vType instanceof ParameterizedType) {
                                vType = ((ParameterizedType) vType).getInnerType();
                            } else {
                                vType = null;
                            }
                        }
                    }
                    ctx = ctx.cloneWithVariable(new Variable(v.name, vType));
                }
            }
        }
        return ctx;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6672.java