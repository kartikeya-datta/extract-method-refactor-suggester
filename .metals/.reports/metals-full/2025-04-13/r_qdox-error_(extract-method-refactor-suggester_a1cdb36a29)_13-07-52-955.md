error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/800.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/800.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/800.java
text:
```scala
l@@og.error("Couldn't resolve type " + superTypeName);

/*******************************************************************************
 * Copyright (c) 2005, 2006 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/
package org.eclipse.xtend.typesystem.uml2.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.internal.xtend.expression.parser.SyntaxConstants;
import org.eclipse.internal.xtend.type.baseimpl.PropertyImpl;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.xtend.expression.TypeSystem;
import org.eclipse.xtend.typesystem.AbstractTypeImpl;
import org.eclipse.xtend.typesystem.Feature;
import org.eclipse.xtend.typesystem.Type;

public class StereotypeType extends AbstractTypeImpl {

    private Log log = LogFactory.getLog(getClass());

    Stereotype stereoType;

    public StereotypeType(TypeSystem typeSystem, String name, Stereotype stereoType) {
        super(typeSystem, name);
        this.stereoType = stereoType;
    }

    @Override
    public Feature[] getContributedFeatures() {
        List<?> children = stereoType.getAttributes();
        List<Feature> features = new ArrayList<Feature>();
        for (Iterator<?> iter = children.iterator(); iter.hasNext();) {
            Object obj = iter.next();
            if (obj instanceof Property) {
                Property p = (Property) obj;
                String name = normalizedName(p.getName());
                if (name != null) {
                    org.eclipse.uml2.uml.Type type = p.getType();
                    if (type.eIsProxy()) {
                        InternalEObject proxy = (InternalEObject) type;
                        URI uri = proxy.eProxyURI();

                        type = (org.eclipse.uml2.uml.Type) EcoreUtil.resolve(proxy, p);

                        if (type.eIsProxy()) {
                            log.error("Couldn't resolve proxy under " + uri);
                        }
                    }
                    String fqn = getFullName(type);
                    if (fqn == null) {
                        log.error("element : " + p.getQualifiedName());
                        log.error("Type : " + type.getQualifiedName());
                    }
                    if (fqn != null) {
                        // We cannot use typeSystem.getTypeForName here, since
                        // some UML tools have there own UML profiles (e.g.
                        // MagicDraw 11.5)
                        // so this is a work around
                        Type rt = null;
                        if (fqn.toLowerCase().endsWith("::string")) {
                            rt = getTypeSystem().getStringType();
                        } else if (fqn.toLowerCase().endsWith("::boolean")) {
                            rt = getTypeSystem().getBooleanType();
                        } else if (fqn.toLowerCase().endsWith("::integer") || fqn.toLowerCase().endsWith("::int")) {
                            rt = getTypeSystem().getIntegerType();
                        } else if (fqn.toLowerCase().endsWith("::real")) {
                            rt = getTypeSystem().getRealType();
                        } else {
                            rt = getTypeSystem().getTypeForName(fqn);
                        }

                        // mapping of multivalued tagged values
                        // see Bug#168076
                        if(p.isMultivalued()){
                        	rt = getTypeSystem().getCollectionType(rt);
                        }

                        if (rt != null) {
                            PropertyImpl prop = new PropertyImpl(StereotypeType.this, name, rt) {

								@SuppressWarnings("unchecked")
								public Object get(Object target) {
									if (target instanceof Element) {
                                        Element ele = (Element) target;
                                        List<Stereotype> all = ele.getAppliedStereotypes();
                                        for (Stereotype st : all) {
                                            if (isStereoTypeAssignable(st, StereotypeType.this.stereoType)) {
												final Object value = ele.getValue(st, getName());

												// custom datatypes
												// see Bug#185033
												if (value instanceof EList) {
													final EList eList = (EList) value;
													final Collection<Object> values = new ArrayList<Object>();
													for (Iterator iterator = eList.iterator(); iterator.hasNext();) {
														final Object dynObject = iterator.next();
														final Object dynValue = getDynamicValue(dynObject);
														if(dynValue != null){
															values.add(dynValue);
														}
													}
													if(!values.isEmpty()){
														return values;
													}

												}else if (value instanceof EObject) {
													final Object dynValue = getDynamicValue(value);
													if(dynValue != null){
														return dynValue;
													}
												}

												return value;
											}
                                        }
                                    }
                                    throw new IllegalArgumentException("uml2 Element expected but was " + target.getClass().getName());
                                }

								private Object getDynamicValue(final Object value) {
									if (value instanceof EObject) {
										final EObject dynObject = (EObject) value;
										final EClass dynClass = dynObject.eClass();
										final EStructuralFeature baseClassFeature = dynClass.getEStructuralFeature("base_Class");

										if(baseClassFeature != null){
											return dynObject.eGet(baseClassFeature,true);
										}
									}
									return null;
								}


                            };
                            features.add(prop);
                        } else {
                            log.error("Couldn't find type for " + fqn);
                        }
                    }
                }
            }
        }
        return features.toArray(new Feature[features.size()]);
    }

	private boolean isStereoTypeAssignable(Stereotype st1, Stereotype st2) {
        if (st1.getQualifiedName().equals(st2.getQualifiedName())) {
            return true;
        }
        List<Generalization> gs = st1.getGeneralizations();
        for (Generalization g : gs) {
            if (g.getGeneral() instanceof Stereotype && isStereoTypeAssignable((Stereotype) g.getGeneral(), st2))
                return true;
        }
        return false;
    }

	public boolean isInstance(Object o) {
        if (o instanceof Element) {
            Element ele = (Element) o;
            List<Stereotype> all = ele.getAppliedStereotypes();
            for (Stereotype st : all) {
              if(isStereoTypeAssignable(st, StereotypeType.this.stereoType)){
                return true;
              }
            }
        }
        return false;
    }

    public Object newInstance() {
        throw new UnsupportedOperationException();
    }

    private Set<Type> superTypes = null;

    @Override
    public Set<Type> getSuperTypes() {
        if (superTypes == null) {
            superTypes = new HashSet<Type>();

            List<Classifier> all = new ArrayList<Classifier>(stereoType.getExtendedMetaclasses());
            all.addAll(stereoType.getSuperClasses());
            for (Classifier classifier : all) {
                String superTypeName = getFullName(classifier);
                Type t = getTypeSystem().getTypeForName(superTypeName);
                if (t == null) {
                    log.warn("Couldn't resolve super type " + superTypeName);
                } else {
                    superTypes.add(t);
                }
            }

        }
        return Collections.unmodifiableSet(superTypes);
    }

    public Stereotype getStereoType () {
    	return stereoType;
    }

    private String getFullName(Object object) {
        if (object instanceof NamedElement) {
            return normalizedName(((NamedElement) object).getQualifiedName());
        } else if (object instanceof EClassifier) {
            return getFullyQualifiedName((EClassifier) object);
        }
        if (log.isWarnEnabled()) {
            log.warn("Cannot resolve names for " + object.getClass().getName());
        }
        return null;
    }

	private String getFullyQualifiedName(final ENamedElement ele) {
		return getFqnRec(ele.eContainer(), ele.getName());
	}

	private String getFqnRec(final EObject ele, final String suffix) {
		if (ele == null || !(ele instanceof ENamedElement)) {
			return suffix;
		}
		else {
			return getFqnRec(ele.eContainer(), ((ENamedElement) ele).getName() + SyntaxConstants.NS_DELIM + suffix);
		}
	}

    @Override
	protected boolean internalIsAssignableFrom(Type t) {
		if (super.internalIsAssignableFrom(t)) {
			return true;
		} else if (t instanceof StereotypeType) {
			return ((StereotypeType)t).isCompatible(this);
		} else {
			return false;
		}
	}

    protected boolean isCompatible (Type t) {
    	return false;
    }

    /**
     * It is not allowed to have whitespaces in profile, stereotype or tagged value names. Therefore we need to replace them w
     * All non-word characters are replaced by underscore.
     * @param name An element's name
     * @return All non-word characters are replaced by underscores
     */
    private static String normalizedName (String name) {
    	String[] fragments = name.split(SyntaxConstants.NS_DELIM);
    	StringBuffer result = new StringBuffer(name.length());
    	result.append(fragments[0].replaceAll("\\W", "_"));
    	for (int i=1; i<fragments.length; i++) {
    		result.append(SyntaxConstants.NS_DELIM);
        	result.append(fragments[1].replaceAll("\\W", "_"));
    	}
    	return result.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/800.java