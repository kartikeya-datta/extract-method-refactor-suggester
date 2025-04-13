error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12981.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12981.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12981.java
text:
```scala
i@@f (template.matches(prefix, contextTypeId)) {

/*******************************************************************************
 * Copyright (c) 2008, 2009 28msec Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gabriel Petrovay (28msec) - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xquery.internal.ui.templates;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.ui.templates.ScriptTemplateAccess;
import org.eclipse.dltk.ui.templates.ScriptTemplateCompletionProcessor;
import org.eclipse.dltk.ui.text.completion.ScriptContentAssistInvocationContext;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.wst.xquery.core.IXQDTLanguageConstants;
import org.eclipse.wst.xquery.core.codeassist.IXQDTCompletionConstants;
import org.eclipse.wst.xquery.core.text.XQDTWordDetector;
import org.eclipse.wst.xquery.core.utils.LanguageUtil;

public class XQDTTemplateCompletionProcessor extends ScriptTemplateCompletionProcessor {

    // private static final class ProposalComparator implements Comparator<TemplateProposal> {
    //
    // public int compare(TemplateProposal o1, TemplateProposal o2) {
    // return o2.getRelevance() - o1.getRelevance();
    // }
    // }
    //
    // private static final Comparator<TemplateProposal> comparator = new ProposalComparator();

    private boolean fIsNormalPrefixType;

    public XQDTTemplateCompletionProcessor(ScriptContentAssistInvocationContext context) {
        super(context);
    }

    protected String getContextTypeId() {
        IProject project = getContext().getProject().getProject();
        int mask = LanguageUtil.getLanguageLevel(project);

        if (LanguageUtil.isLanguage(mask, IXQDTLanguageConstants.LANGUAGE_XQUERY_SCRIPTING)) {
            return XQueryScriptingTemplateContentType.CONTEXT_TYPE_ID;
        } else if (LanguageUtil.isLanguage(mask, IXQDTLanguageConstants.LANGUAGE_XQUERY_UPDATE)) {
            return XQueryUpdateTemplateContentType.CONTEXT_TYPE_ID;
        }
        return XQueryTemplateContentType.CONTEXT_TYPE_ID;
    }

    protected ScriptTemplateAccess getTemplateAccess() {
        return XQDTTemplateAccess.getInstance();
    }

    @Override
    protected boolean isValidPrefix(String prefix) {
        return fIsNormalPrefixType;
    }

    protected boolean isMatchingTemplate(Template template, String prefix, TemplateContext context) {
        if (template.getName().equals("function")) {
            System.err
                    .println("Move potential functions away from normal template processing: XQDTTemplateCompletionProcessor");
            // XQDTTemplateContext tc = (XQDTTemplateContext)context;
            // try {
            // if (tc.getSourceModule().getElementAt(tc.getCompletionOffset()) != null)
            // return false;
            // } catch (ModelException e) {
            // return false;
            // }
        }
        // else
        if (!template.getName().startsWith(prefix)) {
            return false;
        }

        if (template.matches(prefix, context.getContextType().getId())) {
            return true;
        } else {
            TemplateContextType contextType = context.getContextType();
            if (contextType instanceof XQueryTemplateContentType) {
                String[] alsoMatches = ((XQueryTemplateContentType)contextType).getCompatibleContentTypes();
                for (String contextTypeId : alsoMatches) {
                    if (template.getContextTypeId().equals(contextTypeId)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    protected String extractPrefix(ITextViewer viewer, int offset) {
        int i = offset;
        IDocument document = viewer.getDocument();
        if (i > document.getLength()) {
            return "";
        }

        XQDTWordDetector wd = new XQDTWordDetector();

        try {
            char ch = 0;

            while (i > 0) {
                ch = document.getChar(i - 1);
                if (!wd.isWordPart(ch)) {
                    break;
                }
                i--;
            }

            if (ch == ':' || ch == '$') {
                fIsNormalPrefixType = false;
            } else {
                fIsNormalPrefixType = true;
            }

            return document.get(i, offset - i);
        } catch (BadLocationException e) {
            return "";
        }
    }

    @Override
    protected int getRelevance(Template template, String prefix) {
        if (template.getName().startsWith(prefix)) {
            return IXQDTCompletionConstants.RELEVANCE_TEMPLATE;
        }
        return 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12981.java