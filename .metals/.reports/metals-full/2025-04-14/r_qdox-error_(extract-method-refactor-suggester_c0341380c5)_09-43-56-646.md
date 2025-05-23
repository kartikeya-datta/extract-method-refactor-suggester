error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1001.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1001.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1001.java
text:
```scala
public static M@@emberValuePair createTypeMemberValuePair(AST ast, String name, Object value) {

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
package org.eclipse.jst.ws.annotations.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeLiteral;
/**
 * 
 * <p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under 
 * development and expected to change significantly before reaching stability. It is being made available at 
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses 
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 * @author sclarke
 */
public final class AnnotationsCore {

    private AnnotationsCore() {
    }
    
    public static Annotation createAnnotation(AST ast,
            Class<? extends java.lang.annotation.Annotation> annotationClass,
            String annotationName,
            List<MemberValuePair> memberValuePairs) {

        NormalAnnotation annotation = ast.newNormalAnnotation();
        
        Name annotationTypeName = ast.newName(annotationName);
        
        annotation.setTypeName(annotationTypeName);

        if (memberValuePairs != null) {
            for (MemberValuePair memberValuePair : memberValuePairs) {
                @SuppressWarnings("unchecked")
                List<MemberValuePair> annotationValues = annotation.values();
                annotationValues.add(memberValuePair);
            }
        }
        return annotation;
    }

    public static MemberValuePair createMemberValuePair(AST ast, String name, Expression expression) {
        MemberValuePair memberValuePair = ast.newMemberValuePair();
        memberValuePair.setName(ast.newSimpleName(name));
        memberValuePair.setValue(expression);
        return memberValuePair;
    }

    public static MemberValuePair createStringMemberValuePair(AST ast, String name, Object value) {
        MemberValuePair stringMemberValuePair = AnnotationsCore.createMemberValuePair(ast, name,
                AnnotationsCore.createStringLiteral(ast, value.toString()));

        return stringMemberValuePair;
    }

    public static MemberValuePair createBooleanMemberValuePair(AST ast, String name, Object value) {
        MemberValuePair booleanValuePair = AnnotationsCore.createMemberValuePair(ast, name, AnnotationsCore
                .createBooleanLiteral(ast, ((Boolean)value).booleanValue()));

        return booleanValuePair;
    }
    
    public static MemberValuePair createNumberMemberValuePair(AST ast, String name, Object value) {
        MemberValuePair primitiveValuePair = AnnotationsCore.createMemberValuePair(ast, name, 
                AnnotationsCore.createNumberLiteral(ast, value.toString()));
        return primitiveValuePair;
    }
    
    public static MemberValuePair createEnumMemberValuePair(AST ast, String className, String name, 
            Object value) {
         return AnnotationsCore.createMemberValuePair(ast, name, createEnumLiteral(ast, className, value));        
    }
    
    public static MemberValuePair createTypeMemberVaulePair(AST ast, String name, Object value) {
        return AnnotationsCore.createMemberValuePair(ast, name,
                createTypeLiteral(ast, value));
    }
    
    public static MemberValuePair createArrayMemberValuePair(AST ast, Method method, Object[] values) {
        return AnnotationsCore.createMemberValuePair(ast, method.getName(), createArrayValueLiteral(ast, 
                method, values));
     }
    
    @SuppressWarnings("unchecked")
    public static ArrayInitializer createArrayValueLiteral(AST ast, Method method, Object[] values) {
        ArrayInitializer arrayInitializer = ast.newArrayInitializer();
        for (Object value : values) {
            if (value instanceof java.lang.annotation.Annotation) {
                //TODO Handle this situation. Arises when annotations are specified as defaults in array initializers
            }
            if (value instanceof List) {
                Class<? extends java.lang.annotation.Annotation> annotationClass = 
                 (Class<? extends java.lang.annotation.Annotation>) method.getReturnType().getComponentType();

                List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

                List<Map<String, Object>> valuesList = (List<Map<String, Object>>) value;
                Iterator<Map<String, Object>> valuesIterator = valuesList.iterator();
                while (valuesIterator.hasNext()) {
                    Map<String, Object> annotationMap = (Map<String, Object>) valuesIterator.next();
                    Set<Entry<String, Object>> entrySet = annotationMap.entrySet();
                    Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<java.lang.String, Object> entry = iterator.next();
                        String memberName = entry.getKey();
                        try {
                            Method annotationMethod = annotationClass.getMethod(memberName, new Class[0]);
                            if (annotationMethod != null) {
                                Object memberValue = entry.getValue();
                                Class<?> returnType = annotationMethod.getReturnType();
                                if (returnType.equals(String.class)) {
                                    memberValuePairs.add(createStringMemberValuePair(ast, memberName,
                                            memberValue));
                                }
                                if (returnType.equals(Boolean.TYPE)) {
                                    memberValuePairs.add(createBooleanMemberValuePair(ast, memberName,
                                            memberValue));                                    
                                }
                                if (returnType.equals(Class.class)) {
                                    String className = memberValue.toString();
                                    if (className.endsWith(".class")) {
                                        className = className.substring(0, className.lastIndexOf("."));
                                    }
                                    memberValuePairs.add(AnnotationsCore.createMemberValuePair(ast, memberName,
                                            createTypeLiteral(ast, className)));                                    
                                }
//                                if (returnType.isPrimitive()) {
//                                    memberValuePairs.add(getNumberMemberValuePair(ast, memberName, memberValue));
//                                }
                            }
                            
                        } catch (SecurityException se) {
                            AnnotationsCorePlugin.log(se);
                        } catch (NoSuchMethodException nsme) {
                            AnnotationsCorePlugin.log(nsme);
                        }
                    }
                }
                arrayInitializer.expressions().add(createAnnotation(ast, annotationClass,
                        annotationClass.getCanonicalName(), memberValuePairs));
            }
            if (value.equals(Class.class)) {
                arrayInitializer.expressions().add(createTypeLiteral(ast, value.toString()));
            }
            if (value instanceof String) {
                String stringValue = value.toString();
                if (stringValue.endsWith(".class")) {
                    arrayInitializer.expressions().add(createTypeLiteral(ast, stringValue.substring(0,
                            stringValue.lastIndexOf("."))));
                } else {
                    arrayInitializer.expressions().add(createStringLiteral(ast, stringValue));
                }
            }
        }
        return arrayInitializer;
    }
    
    public static Name createEnumLiteral(AST ast, String className, Object value) {
        QualifiedName enumName = null;
        SimpleName enumClassName = ast.newSimpleName(value.getClass().getSimpleName());
        SimpleName enumLiteral = ast.newSimpleName(value.toString());
        if (value.getClass().isMemberClass()) {
            Name enumEnclosingClassName = null;
            String enclosingClassName = value.getClass().getEnclosingClass().getCanonicalName();
            if (enclosingClassName.equals(className)) {
                enumEnclosingClassName = ast.newSimpleName(value.getClass().getEnclosingClass()
                        .getSimpleName());
            } else {
                enumEnclosingClassName = ast.newName(enclosingClassName);
            }
            QualifiedName qualifiedName = ast.newQualifiedName(enumEnclosingClassName, enumClassName);
            enumName = ast.newQualifiedName(qualifiedName, enumLiteral);
        } else {
            Name qualifiedName = ast.newName(value.getClass().getCanonicalName());
            enumName = ast.newQualifiedName(qualifiedName, enumLiteral);
        }
        return enumName;
    }

    public static TypeLiteral createTypeLiteral(AST ast, Object value) {
        TypeLiteral typeLiteral = null;
        if (value instanceof Class) {
            typeLiteral = createTypeLiteral(ast, (Class<?>) value);
        }
        if (value instanceof String) {
            typeLiteral = createTypeLiteral(ast, (String) value);
        }
        return typeLiteral;
    }

    public static TypeLiteral createTypeLiteral(AST ast, Class<?> value) {
        TypeLiteral typeLiteral = ast.newTypeLiteral();

        Class<?> aClass = (Class<?>)value;
        SimpleName className = ast.newSimpleName(aClass.getSimpleName());

        if (aClass.isMemberClass()) {
            Name enclosingClassName = ast.newName(aClass.getEnclosingClass().getCanonicalName());
            QualifiedType qualifiedType = ast.newQualifiedType(ast.newSimpleType(enclosingClassName), className);
            typeLiteral.setType(qualifiedType);
            return typeLiteral;
        }
        return createTypeLiteral(ast, value.getCanonicalName());
    }

    public static TypeLiteral createTypeLiteral(AST ast, String value) {
        TypeLiteral typeLiteral = ast.newTypeLiteral();

        if (value.indexOf(".") == -1) { //$NON-NLS-1$
            typeLiteral.setType(ast.newSimpleType(ast.newSimpleName(value)));
        } else {
            String qualifier = value.substring(0, value.lastIndexOf(".")); //$NON-NLS-1$
            String identifier = value.substring(value.lastIndexOf(".") + 1); //$NON-NLS-1$
            if (qualifier.equals("java.lang")) { //$NON-NLS-1$
                typeLiteral.setType(ast.newSimpleType(ast.newSimpleName(identifier)));
            } else {
                typeLiteral.setType(ast.newQualifiedType(ast.newSimpleType(ast.newName(qualifier)), ast
                        .newSimpleName(identifier)));
            }
        }
        return typeLiteral;
    }

    public static StringLiteral createStringLiteral(AST ast, String literalValue) {
        StringLiteral stringLiteral = ast.newStringLiteral();
        stringLiteral.setLiteralValue(literalValue);
        return stringLiteral;
    }

    public static BooleanLiteral createBooleanLiteral(AST ast, boolean value) {
        BooleanLiteral booleanLiteral = ast.newBooleanLiteral(value);
        return booleanLiteral;
    }
    
    public static NumberLiteral createNumberLiteral(AST ast, String value) {
        NumberLiteral primitiveLiteral = ast.newNumberLiteral();
        primitiveLiteral.setToken(value);
        return primitiveLiteral;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1001.java