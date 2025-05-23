error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12705.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12705.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12705.java
text:
```scala
r@@eturn org.eclipse.xtend.backend.types.emf.internal.EObjectType.INSTANCE;

/*
Copyright (c) 2008 Arno Haase.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html

Contributors:
    Arno Haase - initial API and implementation
 */
package org.eclipse.xtend.middleend.old;

import java.lang.reflect.Field;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.internal.xtend.expression.parser.SyntaxConstants;
import org.eclipse.internal.xtend.type.baseimpl.types.BooleanTypeImpl;
import org.eclipse.internal.xtend.type.baseimpl.types.CollectionTypeImpl;
import org.eclipse.internal.xtend.type.baseimpl.types.IntegerTypeImpl;
import org.eclipse.internal.xtend.type.baseimpl.types.ListTypeImpl;
import org.eclipse.internal.xtend.type.baseimpl.types.ObjectTypeImpl;
import org.eclipse.internal.xtend.type.baseimpl.types.OperationTypeImpl;
import org.eclipse.internal.xtend.type.baseimpl.types.PropertyTypeImpl;
import org.eclipse.internal.xtend.type.baseimpl.types.RealTypeImpl;
import org.eclipse.internal.xtend.type.baseimpl.types.SetTypeImpl;
import org.eclipse.internal.xtend.type.baseimpl.types.StaticPropertyTypeImpl;
import org.eclipse.internal.xtend.type.baseimpl.types.StringTypeImpl;
import org.eclipse.internal.xtend.type.baseimpl.types.TypeTypeImpl;
import org.eclipse.internal.xtend.type.impl.java.JavaTypeImpl;
import org.eclipse.xtend.backend.common.BackendType;
import org.eclipse.xtend.backend.common.BackendTypesystem;
import org.eclipse.xtend.backend.types.CompositeTypesystem;
import org.eclipse.xtend.backend.types.builtin.BooleanType;
import org.eclipse.xtend.backend.types.builtin.CollectionType;
import org.eclipse.xtend.backend.types.builtin.DoubleType;
import org.eclipse.xtend.backend.types.builtin.FunctionType;
import org.eclipse.xtend.backend.types.builtin.ListType;
import org.eclipse.xtend.backend.types.builtin.LongType;
import org.eclipse.xtend.backend.types.builtin.ObjectType;
import org.eclipse.xtend.backend.types.builtin.PropertyType;
import org.eclipse.xtend.backend.types.builtin.SetType;
import org.eclipse.xtend.backend.types.builtin.StaticPropertyType;
import org.eclipse.xtend.backend.types.builtin.StringType;
import org.eclipse.xtend.backend.types.builtin.TypeType;
import org.eclipse.xtend.backend.types.builtin.VoidType;
import org.eclipse.xtend.backend.types.emf.EmfTypesystem;
import org.eclipse.xtend.expression.ExecutionContext;
import org.eclipse.xtend.typesystem.Type;
import org.eclipse.xtend.typesystem.emf.EClassType;
import org.eclipse.xtend.typesystem.emf.EDataTypeType;
import org.eclipse.xtend.typesystem.emf.EEnumType;
import org.eclipse.xtend.typesystem.emf.EObjectType;


/**
 * 
 * @author Arno Haase (http://www.haase-consulting.com)
 */
final class TypeToBackendType {
    private final BackendTypesystem _backendTypes;
    private final EmfTypesystem _emfTypes;
    private final ExecutionContext _ctx;
    
    public TypeToBackendType (BackendTypesystem backendTypes, ExecutionContext ctx) {
        _backendTypes = backendTypes;
        _ctx = ctx;
        
        EmfTypesystem ets = null;
        for (BackendTypesystem bts: ((CompositeTypesystem) _backendTypes).getInner()) {
            if (bts instanceof EmfTypesystem)
                ets = (EmfTypesystem) bts;
        }
        
        _emfTypes = ets;
    }
    
    public BackendType convertToBackendType (List<String> segments) {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        
        for (String s: segments) {
            if (!first) {
                sb.append (SyntaxConstants.NS_DELIM);
            }
            first = false;
            
            sb.append (s);
        }
        
        final Type t = _ctx.getTypeForName (sb.toString());
        return convertToBackendType(t);
    }

    public BackendType convertToBackendType (Class<?> cls) {
        return _backendTypes.findType (cls);
    }
    
    public BackendType convertToBackendType (Type t) {
        if (t instanceof EClassType)
            return convertEClassType (t);
        if (t instanceof EDataTypeType)
            return convertEDataTypeType (t);
        if (t instanceof EEnumType)
            return convertEEnumType (t);
        if (t instanceof EObjectType)
            return org.eclipse.xtend.backend.types.emf.EObjectType.INSTANCE;
        
        if (t instanceof JavaTypeImpl)
            return convertJavaType (t);
        
        if (t instanceof BooleanTypeImpl)
            return BooleanType.INSTANCE;
        if (t instanceof ListTypeImpl)
            return ListType.INSTANCE;
        if (t instanceof SetTypeImpl)
            return SetType.INSTANCE;
        if (t instanceof CollectionTypeImpl)
            return CollectionType.INSTANCE;
        if (t instanceof IntegerTypeImpl)
            return LongType.INSTANCE;
        if (t instanceof ObjectTypeImpl)
            return ObjectType.INSTANCE;
        if (t instanceof OperationTypeImpl)
            return FunctionType.INSTANCE;
        if (t instanceof PropertyTypeImpl)
            return PropertyType.INSTANCE;
        if (t instanceof RealTypeImpl)
            return DoubleType.INSTANCE;
        if (t instanceof StaticPropertyTypeImpl)
            return StaticPropertyType.INSTANCE;
        if (t instanceof StringTypeImpl)
            return StringType.INSTANCE;
        if (t instanceof TypeTypeImpl)
            return TypeType.INSTANCE;
        if (t instanceof org.eclipse.internal.xtend.type.baseimpl.types.VoidType)
            return VoidType.INSTANCE;
        
        if (t != null)
            throw new IllegalArgumentException ("unable to convert type " + t.getClass().getName());
        else
            throw new IllegalArgumentException ("unable to convert type 'null'");
    }
    
    private BackendType convertJavaType (Type t) {
        final Class<?> cls = (Class<?>) getField (t, "clazz");
        return _backendTypes.findType(cls);
    }
    
    private BackendType convertEClassType (Type t) {
        final EClass eClass = (EClass) getField(t, "eClass");
        return _emfTypes.getTypeForEClassifier(eClass);
    }
    
    private BackendType convertEDataTypeType (Type t) {
        final EDataType eClass = (EDataType) getField(t, "dataType");
        return _emfTypes.getTypeForEClassifier(eClass);
    }
    
    private BackendType convertEEnumType (Type t) {
        final EEnum eClass = (EEnum) getField(t, "eEnum");
        return _emfTypes.getTypeForEClassifier(eClass);
    }
    
    private Object getField (Object o, String fieldName) {
        try {
            final Class<?> cls = o.getClass();
            final Field f = cls.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(o);
        } catch (Exception e) {
            throw new RuntimeException (e);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12705.java