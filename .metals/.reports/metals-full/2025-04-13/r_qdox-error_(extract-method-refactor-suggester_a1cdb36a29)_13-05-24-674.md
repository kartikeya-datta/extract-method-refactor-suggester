error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4178.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4178.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4178.java
text:
```scala
r@@eturn "";  //$NON-NLS-1$

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
package org.eclipse.jst.ws.internal.jaxws.core.annotations.initialization;

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.HEADER;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.OPERATION_NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.PART_NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.RESPONSE;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.RETURN;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.TARGET_NAMESPACE;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.initialization.AnnotationAttributeInitializer;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;
import org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;

public class WebResultAttributeInitializer extends AnnotationAttributeInitializer {
    
    @Override
    public List<MemberValuePair> getMemberValuePairs(IJavaElement javaElement, AST ast,
            Class<? extends Annotation> annotationClass) {
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

        if (javaElement.getElementType() == IJavaElement.METHOD) {
            IMethod method = (IMethod) javaElement;

            MemberValuePair nameValuePair = AnnotationsCore.createStringMemberValuePair(ast, NAME, getName(method));
        
            memberValuePairs.add(nameValuePair);
        }
        return memberValuePairs;
    }
    
    public List<ICompletionProposal> getCompletionProposalsForMemberValuePair(IJavaElement javaElement,
            MemberValuePair memberValuePair) {

        List<ICompletionProposal> completionProposals = new ArrayList<ICompletionProposal>();

        if (javaElement.getElementType() == IJavaElement.METHOD) {
            IMethod method = (IMethod) javaElement;

            String memberValuePairName = memberValuePair.getName().getIdentifier();

            if (memberValuePairName.equals(NAME)) {
                completionProposals.add(AnnotationUtils.createCompletionProposal(getName(method), memberValuePair
                    .getValue()));
            }
            
            if (memberValuePairName.equals(PART_NAME)) {
                completionProposals.add(AnnotationUtils.createCompletionProposal(getPartName(method),
                        memberValuePair.getValue()));
            }
            
            if (memberValuePairName.equals(TARGET_NAMESPACE)) {
                completionProposals.add(AnnotationUtils.createCompletionProposal(getTargetNamespace(method),
                        memberValuePair.getValue()));
            }

        }
        return completionProposals;
    }
    
    private String getName(IMethod method) {
        if (hasDocumentBareSOAPBinding(method)) {
            return getOperationName(method) + RESPONSE;
        }
        return RETURN;
    }

    private String getOperationName(IMethod method) {
        try {
            IAnnotation annotation = AnnotationUtils.getAnnotation(method, WebMethod.class);
            if (annotation != null) {
                String operationName = AnnotationUtils.getStringValue(annotation, OPERATION_NAME);
                if (operationName != null) {
                    return operationName;
                }
            }
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return method.getElementName();
    }

    private boolean hasDocumentBareSOAPBinding(IAnnotatable annotatable) {
        try {
            IAnnotation annotation = AnnotationUtils.getAnnotation(annotatable, SOAPBinding.class);
            if (annotation != null) {
                return JAXWSUtils.isDocumentBare(annotation);
            }
            if (annotatable instanceof IMethod) {
                IMethod method = (IMethod) annotatable;
                return hasDocumentBareSOAPBinding((IType) method.getParent());
            }
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return false;
    }
  
    private String getPartName(IMethod method) {
        try {
            IAnnotation annotation = AnnotationUtils.getAnnotation(method, WebResult.class);
            if (annotation != null) {
                String name = AnnotationUtils.getStringValue(annotation, NAME);
                if (name != null) {
                    return name;
                }
            }
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return getName(method);
    }

    private String getTargetNamespace(IMethod method) {
        if (hasDocumentWrappedSOAPBinding(method) && !isHeader(method)) {
            return ""; 
        }
        return getDefaultTargetNamespace(method);
    }

    private boolean hasDocumentWrappedSOAPBinding(IAnnotatable annotatable) {
        try {
            IAnnotation annotation = AnnotationUtils.getAnnotation(annotatable, SOAPBinding.class);
            if (annotation != null) {
                return JAXWSUtils.isDocumentWrapped(annotation);
            }
            if (annotatable instanceof IMethod) {
                IMethod method = (IMethod) annotatable;
                return hasDocumentWrappedSOAPBinding((IType) method.getParent());
            }
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return true;
    }
    
    private boolean isHeader(IMethod method) {
        try {
            IAnnotation annotation = AnnotationUtils.getAnnotation(method, WebResult.class);
            if (annotation != null) {
                Boolean header = AnnotationUtils.getBooleanValue(annotation, HEADER);
                if (header != null) {
                    return header.booleanValue();
                }
            }
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return false;
    }

    private String getDefaultTargetNamespace(IMethod method) {
        IType type = method.getDeclaringType();
        try {
            IAnnotation annotation = AnnotationUtils.getAnnotation(type, WebService.class);
            if (annotation != null) {
                String targetNamespace = AnnotationUtils.getStringValue(annotation, TARGET_NAMESPACE);
                if (targetNamespace != null) {
                    return targetNamespace;
                }
            }
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }

        return JDTUtils.getTargetNamespaceFromPackageName(getPackageName(type));
    }

    private String getPackageName(IType type) {
        IPackageFragment packageFragment = type.getPackageFragment();
        return packageFragment.getElementName();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4178.java