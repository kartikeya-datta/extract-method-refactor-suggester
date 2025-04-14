error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1002.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1002.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1002.java
text:
```scala
m@@emberValuePairs.add(AnnotationsCore.createTypeMemberValuePair(ast, name,

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
package org.eclipse.jst.ws.annotations.core.initialization;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;

/**
 * Constructs <code>MemberValuePair</code> from the defaults found in the passed in <code>Annotation</code>.
 * <p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under 
 * development and expected to change significantly before reaching stability. It is being made available at 
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses 
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 * @author sclarke
 */
public class DefaultsAnnotationAttributeInitializer extends AnnotationAttributeInitializer {
    
    public DefaultsAnnotationAttributeInitializer() {
        
    }

    @Override
    public List<MemberValuePair> getMemberValuePairs(IJavaElement javaElement, AST ast,
            Class<? extends Annotation> annotationClass) {
        return getMemberValuePairs(ast, annotationClass);
    }

    @Override
    public List<MemberValuePair> getMemberValuePairs(ASTNode astNode, AST ast,
            Class<? extends Annotation> annotationClass) {
        return getMemberValuePairs(ast, annotationClass);
    }
    
    private List<MemberValuePair> getMemberValuePairs(AST ast,
            Class<? extends Annotation> annotationClass) {
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();
        
        Method[] declaredMethods = annotationClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            String name = method.getName();
            Class<?> returnType = method.getReturnType();
            Object defaultValue = method.getDefaultValue();

            if (defaultValue != null) {
                if (returnType.equals(String.class)) {
                    memberValuePairs.add(AnnotationsCore.createStringMemberValuePair(ast,
                            name, defaultValue.toString()));
                }
                
                if (returnType.equals(Boolean.TYPE)) {
                    memberValuePairs.add(AnnotationsCore.createBooleanMemberValuePair(ast,
                            name, Boolean.parseBoolean(defaultValue.toString())));
                }
                
                if (returnType.isPrimitive() && (returnType.equals(Byte.TYPE) || returnType.equals(Short.TYPE) 
 returnType.equals(Integer.TYPE) || returnType.equals(Long.TYPE)
 returnType.equals(Float.TYPE) || returnType.equals(Double.TYPE))) {
                    memberValuePairs.add(AnnotationsCore.createNumberMemberValuePair(ast, name, defaultValue));
                }
                
                if (returnType.isArray()) {
                    memberValuePairs.add(AnnotationsCore.createArrayMemberValuePair(ast, method, 
                            (Object[])defaultValue));
                }
                
                if (returnType.isEnum()) {
                    memberValuePairs.add(AnnotationsCore.createEnumMemberValuePair(ast, 
                            method.getDeclaringClass().getCanonicalName(), name, defaultValue));
                }
                
                if (returnType.equals(Class.class)) {
                    memberValuePairs.add(AnnotationsCore.createTypeMemberVaulePair(ast, name, 
                            defaultValue));
                }
            }
        }      
        return memberValuePairs;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1002.java