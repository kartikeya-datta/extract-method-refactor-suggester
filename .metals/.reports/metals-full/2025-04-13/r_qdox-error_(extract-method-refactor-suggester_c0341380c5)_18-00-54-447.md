error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11902.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11902.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11902.java
text:
```scala
private A@@nnotationProcessor getAnnotationProcessor(IConfigurationElement configurationElement,

/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.annotations.core.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jst.ws.annotations.core.AnnotationsCorePlugin;
import org.eclipse.jst.ws.annotations.core.AnnotationsManager;
import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessors;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

public class AnnotationsCoreProcessorFactory implements AnnotationProcessorFactory {

    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> annotationSet,
            AnnotationProcessorEnvironment processorEnvironment) {

    	if (annotationSet.size() == 0) {
    		return AnnotationProcessors.NO_OP;
    	}
        List<AnnotationProcessor> annotationProcessors = new ArrayList<AnnotationProcessor>();

        Map<String, List<IConfigurationElement>> annotationProcessorCache =
            AnnotationsManager.getAnnotationProcessorsCache();

        for (AnnotationTypeDeclaration annotationTypeDeclaration : annotationSet) {
            List<IConfigurationElement> processorElements = annotationProcessorCache.get(
                    annotationTypeDeclaration.getQualifiedName());

            for (IConfigurationElement configurationElement : processorElements) {
                if (configurationElement != null) {
                    AnnotationProcessor processor = getAnnotationProcessor(configurationElement,
                            processorEnvironment);
                    if (processor != null) {
                        annotationProcessors.add(processor);
                    }
                }
            }
        }

        return AnnotationProcessors.getCompositeAnnotationProcessor(annotationProcessors);
    }

    public AnnotationProcessor getAnnotationProcessor(IConfigurationElement configurationElement,
            AnnotationProcessorEnvironment processorEnvironment) {
          try {
              AbstractAnnotationProcessor annotationProcessor =
                  (AbstractAnnotationProcessor)configurationElement.createExecutableExtension("class");
              annotationProcessor.setAnnotationProcessorEnvironment(processorEnvironment);
              return annotationProcessor;
          } catch (CoreException ce) {
              AnnotationsCorePlugin.log(ce.getStatus());
          }
          return null;
      }

    public Collection<String> supportedAnnotationTypes() {
        return AnnotationsManager.getAnnotationProcessorsCache().keySet();
    }

    public Collection<String> supportedOptions() {
        return Collections.emptyList();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11902.java