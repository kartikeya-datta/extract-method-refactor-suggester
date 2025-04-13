error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4184.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4184.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4184.java
text:
```scala
public static final S@@tring ACTION = "action"; //$NON-NLS-1$

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
package org.eclipse.jst.ws.internal.jaxws.core.utils;

import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;

import com.sun.mirror.declaration.AnnotationMirror;

public final class JAXWSUtils {

    public static final String ACTION = "action";
    public static final String ARG = "arg"; //$NON-NLS-1$
    public static final String CLASS_NAME = "className"; //$NON-NLS-1$
    public static final String DOT_CHARACTER = "."; //$NON-NLS-1$
    public static final String ENDPOINT_INTERFACE = "endpointInterface"; //$NON-NLS-1$
    public static final String EXCLUDE = "exclude"; //$NON-NLS-1$
    public static final String FAULT_BEAN = "faultBean"; //$NON-NLS-1$
    public static final String FINALIZE = "finalize"; //$NON-NLS-1$
    public static final String HEADER = "header"; //$NON-NLS-1$
    public static final String JAXWS_SUBPACKAGE = "jaxws"; //$NON-NLS-1$
    public static final String LOCAL_NAME = "localName"; //$NON-NLS-1$
    public static final String MODE = "mode"; //$NON-NLS-1$
    public static final String NAME = "name"; //$NON-NLS-1$    
    public static final String OPERATION_NAME = "operationName"; //$NON-NLS-1$
    public static final String PARAMETER_STYLE = "parameterStyle"; //$NON-NLS-1$
    public static final String PART_NAME = "partName"; //$NON-NLS-1$
    public static final String PORT_NAME = "portName"; //$NON-NLS-1$
    public static final String PORT_SUFFIX = "Port"; //$NON-NLS-1$
    public static final String RESPONSE = "Response"; //$NON-NLS-1$
    public static final String RESPONSE_SUFFIX = "Response"; //$NON-NLS-1$
    public static final String RETURN = "return"; //$NON-NLS-1$
    public static final String SERVICE_NAME = "serviceName"; //$NON-NLS-1$
    public static final String SERVICE_SUFFIX = "Service"; //$NON-NLS-1$
    public static final String STYLE = "style"; //$NON-NLS-1$
    public static final String TARGET_NAMESPACE = "targetNamespace"; //$NON-NLS-1$
    public static final String TRUE = "true"; //$NON-NLS-1$
    public static final String TYPE = "type"; //$NON-NLS-1$
    public static final String USE = "use"; //$NON-NLS-1$
    public static final String WSDL_LOCATION = "wsdlLocation"; //$NON-NLS-1$
    public static final String VALUE = "value"; //$NON-NLS-1$

    private JAXWSUtils() {
    }
    
    public static boolean isDocumentBare(AnnotationMirror mirror) {
        String style = AnnotationUtils.getStringValue(mirror, STYLE);
        String use = AnnotationUtils.getStringValue(mirror, USE);
        String parameterStyle = AnnotationUtils.getStringValue(mirror, PARAMETER_STYLE);

        return JAXWSUtils.isDocumentBare(style, use, parameterStyle);
    }

    public static boolean isDocumentBare(IAnnotation annotation) {
        try {
            String style = AnnotationUtils.getEnumValue(annotation, STYLE);
            String use = AnnotationUtils.getEnumValue(annotation, USE);
            String parameterStyle = AnnotationUtils.getEnumValue(annotation, PARAMETER_STYLE);
            return JAXWSUtils.isDocumentBare(style, use, parameterStyle);
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return false;
    }
    
    public static boolean isDocumentBare(org.eclipse.jdt.core.dom.Annotation annotation) {
        String style = AnnotationUtils.getEnumValue(annotation, STYLE);
        String use = AnnotationUtils.getEnumValue(annotation, USE);
        String parameterStyle = AnnotationUtils.getEnumValue(annotation, PARAMETER_STYLE);

        return JAXWSUtils.isDocumentBare(style, use, parameterStyle);
    }

    public static boolean isDocumentBare(String style, String use, String parameterStyle) {
        return (style == null || style.equals(Style.DOCUMENT.name()))
                && (use == null || use.equals(Use.LITERAL.name()))
                && (parameterStyle != null && parameterStyle.equals(ParameterStyle.BARE.name()));
    }

    public static boolean isDocumentWrapped(AnnotationMirror mirror) {
        String style = AnnotationUtils.getStringValue(mirror, STYLE);
        String use = AnnotationUtils.getStringValue(mirror, USE);
        String parameterStyle = AnnotationUtils.getStringValue(mirror, PARAMETER_STYLE);
        
        return JAXWSUtils.isDocumentWrapped(style, use, parameterStyle);
    }
    
    public static boolean isDocumentWrapped(IAnnotation annotation) {
        try {
            String style = AnnotationUtils.getEnumValue(annotation, STYLE);
            String use = AnnotationUtils.getEnumValue(annotation, USE);
            String parameterStyle = AnnotationUtils.getEnumValue(annotation, PARAMETER_STYLE);
            return JAXWSUtils.isDocumentWrapped(style, use, parameterStyle);
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return true;
    }
    
    public static boolean isDocumentWrapped(org.eclipse.jdt.core.dom.Annotation annotation) {
        String style = AnnotationUtils.getEnumValue(annotation, STYLE);
        String use = AnnotationUtils.getEnumValue(annotation, USE);
        String parameterStyle = AnnotationUtils.getEnumValue(annotation, PARAMETER_STYLE);

        return JAXWSUtils.isDocumentWrapped(style, use, parameterStyle);
    }

    
    public static boolean isDocumentWrapped(String style, String use, String parameterStyle) {
        return (style == null || style.equals(Style.DOCUMENT.name()))
                && (use == null || use.equals(Use.LITERAL.name()))
                && (parameterStyle == null || parameterStyle.equals(ParameterStyle.WRAPPED.name()));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4184.java