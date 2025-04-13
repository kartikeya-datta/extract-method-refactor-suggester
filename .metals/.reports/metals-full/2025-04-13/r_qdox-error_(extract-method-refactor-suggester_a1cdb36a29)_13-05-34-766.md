error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9911.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9911.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9911.java
text:
```scala
i@@f (annotationKey.equals(CXFModelUtils.WEB_PARAM)) {

/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.creation.ui.viewers;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.internal.core.SourceMethod;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.core.utils.CXFModelUtils;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.cxf.creation.ui.CXFCreationUIPlugin;
import org.eclipse.swt.graphics.Image;

/**
 * @author sclarke
 */
@SuppressWarnings("restriction")
public class AnnotationColumnLabelProvider extends ColumnLabelProvider {
    private Image addAnnotationImage;
    private Image removeAnnotationImage;
    private Image disabled;

    private Map<IMethod, Map<String, Boolean>> methodMap;
    private String annotationKey;
    private IType type;

    public AnnotationColumnLabelProvider(Java2WSDataModel model, String annotationKey, IType type) {
        this.methodMap = model.getMethodMap();
        this.annotationKey = annotationKey;
        this.type = type;
        addAnnotationImage = CXFCreationUIPlugin.imageDescriptorFromPlugin(CXFCreationUIPlugin.PLUGIN_ID,
                "icons/obj16/true.gif").createImage(); //$NON-NLS-1$
        removeAnnotationImage = CXFCreationUIPlugin.imageDescriptorFromPlugin(CXFCreationUIPlugin.PLUGIN_ID,
                "icons/obj16/false.gif").createImage(); //$NON-NLS-1$
        disabled = CXFCreationUIPlugin.imageDescriptorFromPlugin(CXFCreationUIPlugin.PLUGIN_ID,
                "icons/obj16/disabled.gif").createImage(); //$NON-NLS-1$
    }

    public String getText(Object element) {
        return ""; //$NON-NLS-1$
    }

    @Override
    public Image getImage(Object element) {
        if (element instanceof SourceMethod) {
            if (methodMap == null || annotationKey == null || type == null) {
                return null;
            }

            IMethod method = (SourceMethod) element;

            if (annotationKey == CXFModelUtils.WEB_PARAM) {
                List<SingleVariableDeclaration> parameters = AnnotationUtils.getMethodParameters(type,
                        method);

                if (parameters.size() == 0) {
                    return disabled;
                }
                for (SingleVariableDeclaration parameter : parameters) {
                    if (AnnotationUtils.isAnnotationPresent(parameter, annotationKey)) {
                        return disabled;
                    }
                }
            }
            
            if (AnnotationUtils.isAnnotationPresent(method, annotationKey)) {
                return disabled;
            }
            
            if (methodMap.get(method) != null) {
                Boolean addAnnotation =  methodMap.get(method).get(annotationKey);
                if (addAnnotation) {
                    return addAnnotationImage;
                } else {
                    return removeAnnotationImage;
                }                
            }
        }
        return null;
    }

    @Override
    public void dispose() {
        super.dispose();
        addAnnotationImage.dispose();
        removeAnnotationImage.dispose();
        disabled.dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9911.java