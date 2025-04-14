error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13788.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13788.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[24,1]

error in qdox parser
file content:
```java
offset: 1145
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13788.java
text:
```scala
public class SessionSynchronizationProcessor extends AbstractAnnotationEJBProcessor<StatefulComponentDescription> {

/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
p@@ackage org.jboss.as.ejb3.deployment.processors;

import org.jboss.as.ejb3.component.stateful.StatefulComponentDescription;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.annotation.CompositeIndex;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.MethodInfo;

import javax.ejb.AfterBegin;
import javax.ejb.AfterCompletion;
import javax.ejb.BeforeCompletion;
import javax.ejb.SessionSynchronization;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class SessionSynchronizationAnnotationProcessor extends AbstractAnnotationEJBProcessor<StatefulComponentDescription> {
    private static final DotName AFTER_BEGIN = DotName.createSimple(AfterBegin.class.getName());
    private static final DotName AFTER_COMPLETION = DotName.createSimple(AfterCompletion.class.getName());
    private static final DotName BEFORE_COMPLETION = DotName.createSimple(BeforeCompletion.class.getName());

    private static interface MethodProcessor {
        void process(MethodInfo method);
    }

    @Override
    protected Class<StatefulComponentDescription> getComponentDescriptionType() {
        return StatefulComponentDescription.class;
    }

    private static boolean implementsSessionSynchronization(final ClassInfo classInfo) {
        for(DotName intf : classInfo.interfaces()) {
            if (intf.toString().equals(SessionSynchronization.class.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void processAnnotations(final ClassInfo beanClass, final CompositeIndex index, final StatefulComponentDescription componentDescription) throws DeploymentUnitProcessingException {
        final DotName superName = beanClass.superName();
        if (superName != null) {
            ClassInfo superClass = index.getClassByName(superName);
            if (superClass != null)
                processAnnotations(superClass, index, componentDescription);
        }

        if (implementsSessionSynchronization(beanClass)) {
            componentDescription.setAfterBegin(null, "afterBegin");
            componentDescription.setAfterCompletion(null, "afterCompletion");
            componentDescription.setBeforeCompletion(null, "beforeCompletion");
            return;
        }

        final Map<DotName, List<AnnotationInstance>> classAnnotations = beanClass.annotations();
        if (classAnnotations == null)
            return;

        processClassAnnotations(classAnnotations, AFTER_BEGIN, new MethodProcessor() {
            @Override
            public void process(MethodInfo method) {
                componentDescription.setAfterBegin(method.declaringClass().toString(), method.name().toString());
            }
        });
        processClassAnnotations(classAnnotations, AFTER_COMPLETION, new MethodProcessor() {
            @Override
            public void process(MethodInfo method) {
                componentDescription.setAfterCompletion(method.declaringClass().toString(), method.name().toString());
            }
        });
        processClassAnnotations(classAnnotations, BEFORE_COMPLETION, new MethodProcessor() {
            @Override
            public void process(MethodInfo method) {
                componentDescription.setBeforeCompletion(method.declaringClass().toString(), method.name().toString());
            }
        });
    }

    private static void processClassAnnotations(final Map<DotName, List<AnnotationInstance>> classAnnotations, final DotName annotationName, final MethodProcessor methodProcessor) throws DeploymentUnitProcessingException {
        final List<AnnotationInstance> annotations = classAnnotations.get(annotationName);
        if (annotations == null || annotations.size() == 0)
            return;

        if (annotations.size() > 1)
            throw new DeploymentUnitProcessingException("EJB 3.1 FR 4.9.4: at most one session synchronization method is allowed for " + annotationName);

        // session synchronization annotations can only be encountered on a method, so this cast is safe.
        final MethodInfo method = (MethodInfo) annotations.get(0).target();
        methodProcessor.process(method);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13788.java