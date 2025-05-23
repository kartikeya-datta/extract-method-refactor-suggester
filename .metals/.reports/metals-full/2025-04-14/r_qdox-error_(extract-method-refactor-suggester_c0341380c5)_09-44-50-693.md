error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6775.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6775.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6775.java
text:
```scala
r@@esult.append(fragments[i].replaceAll("\\W", "_"));

package org.eclipse.xtend.typesystem.uml2.profile;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.mwe.core.ConfigurationException;
import org.eclipse.internal.xtend.expression.parser.SyntaxConstants;
import org.eclipse.internal.xtend.util.Cache;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.xtend.expression.TypeSystem;
import org.eclipse.xtend.typesystem.MetaModel;
import org.eclipse.xtend.typesystem.Type;
import org.eclipse.xtend.typesystem.uml2.UML2MetaModelBase;
import org.eclipse.xtend.typesystem.uml2.UML2Util2;

public class ProfileMetaModel implements MetaModel {
	public Profile profile;

	private TypeSystem typeSystem;

	private class InternaleProfileMetaModel extends UML2MetaModelBase {

		private final Cache<String, Type> typeForNameCache = new Cache<String, Type>() {
			@Override
			protected Type createNew(final String typeName) {
				final NamedElement ele = getNamedElementRec(new NamedElement[] { profile }, typeName);
				if (ele != null) {
					final Type result = getTypeForEClassifier(ele.eClass());
					return result;
				} else {
					return null;
				}
			}
		};

		@Override
		public Type getTypeForName(final String typeName) {
			Type result = typeForNameCache.get(typeName);
			if (result == null) {
				result = super.getTypeForName(typeName);
			}
			return result;
		}

		private NamedElement getNamedElementRec(final NamedElement[] elements, final String name) {
			final String[] frags = name.split(SyntaxConstants.NS_DELIM);
			final String firstFrag = frags[0];
			for (final NamedElement ele : elements) {
				if (ele.getName() != null && ele.getName().equals(firstFrag)) {
					if (frags.length > 1) {
						final Collection<ENamedElement> children = EcoreUtil.getObjectsByType(ele.eContents(),
								UMLPackage.eINSTANCE.getNamedElement());

						return getNamedElementRec(children.toArray(new NamedElement[children.size()]), name
								.substring(name.indexOf(SyntaxConstants.NS_DELIM) + SyntaxConstants.NS_DELIM.length()));
					}
					else
						return ele;
				}
			}
			return null;
		}

	};

	private InternaleProfileMetaModel internalProfileMetaModel;

	/**
	 * Flag, if an exception should be thrown, if stereotypes, assigned to the
	 * model element, are not loaded. If set to 'false', stereotypes not loaded
	 * are skipped. Default is 'true'.
	 */
	private boolean errorIfStereotypeMissing = true;

	private final Set<String> namespaces = new TreeSet<String>();

	public void setErrorIfStereotypeMissing(final boolean errorIfStereotypeMissing) {
		this.errorIfStereotypeMissing = errorIfStereotypeMissing;
	}

	public ProfileMetaModel() {
	}

	public ProfileMetaModel(final Profile profile) {
		assert profile != null;
		this.profile = profile;
		init();
	}

	public void setProfile(final String profile) {
		assert profile != null;
		final Profile p = UML2Util2.loadProfile(profile);
		if (p == null)
			throw new ConfigurationException("Couldn't load profile from " + profile);
		this.profile = p;
		init();
	}

	private Map<String, Type> stereoTypes = null;

	/**
	 * Initializes the metamodel. All stereotypes in the profile are mapped to
	 * StereotypeType instances and all Enumerations to EnumType instances.
	 */
	private void init() {
		if (stereoTypes != null || profile == null || typeSystem == null)
			return;
		internalProfileMetaModel = new InternaleProfileMetaModel();
		internalProfileMetaModel.setTypeSystem(typeSystem);

		stereoTypes = new HashMap<String, Type>();
		final List<org.eclipse.uml2.uml.Type> sts = getAllOwnedTypes(profile);
		for (final Iterator<org.eclipse.uml2.uml.Type> iter = sts.iterator(); iter.hasNext();) {
			final Object o = iter.next();
			if (o instanceof Stereotype) {
				final Stereotype st = (Stereotype) o;
				final String typeName = getFullName(st);
				final Type t = new StereotypeType(getTypeSystem(), typeName, st);
				stereoTypes.put(typeName, t);
			}
			else if (o instanceof Enumeration) {
				final Enumeration en = (Enumeration) o;
				final String typeName = getFullName(en);
				final Type t = new EnumType(getTypeSystem(), typeName, en);
				stereoTypes.put(typeName, t);
			}
		}
		namespaces.add(normalizedName(profile.getName()));
	}

	private List<org.eclipse.uml2.uml.Type> getAllOwnedTypes(final Package pck) {
		final List<org.eclipse.uml2.uml.Type> result = new ArrayList<org.eclipse.uml2.uml.Type>();
		result.addAll(pck.getOwnedTypes());
		for (final Package nested : pck.getNestedPackages()) {
			result.addAll(getAllOwnedTypes(nested));
		}
		return result;
	}

	/**
	 * It is not allowed to have non-word characters in profile, stereotype or
	 * tagged value names. All non-word characters are replaced by underscore.
	 * 
	 * @param name
	 *            An element's name
	 * @return All non-word characters are replaced by underscores except for
	 *         occurances of the namespace delimiter '::'
	 */
	private static String normalizedName(String name) {
		String[] fragments = name.split(SyntaxConstants.NS_DELIM);
		StringBuffer result = new StringBuffer(name.length());
		result.append(fragments[0].replaceAll("\\W", "_"));
		for (int i = 1; i < fragments.length; i++) {
			result.append(SyntaxConstants.NS_DELIM);
			result.append(fragments[1].replaceAll("\\W", "_"));
		}
		return result.toString();
	}

	public String getFullName(final org.eclipse.uml2.uml.Type type) {
		return normalizedName(type.getQualifiedName());
	}

	public Type getTypeForName(final String typeName) {
		Type result = stereoTypes.get(typeName);
		if (result == null) {
			result = internalProfileMetaModel.getTypeForName(typeName);
		}
		return result;
	}

	/*
	 * getType() tries to return a type for every object according to it's
	 * stereotypes. If it fails, getType returns null and it is up to other
	 * MetaModel implementations to define a type for that object.
	 * 
	 * EnumerationLiterals are a special case. If there is no stereotype
	 * defined, getType tries to return the type of the enumeration that
	 * contains the literal. So, enumerations' literals become instances of the
	 * enumerations' type. It is important to notice that enumerations are only
	 * types if they are defined in the uml-profile and not in the uml-model.
	 * 
	 * This code should be able to handle the following two scenarios: -
	 * enumerations+literals without stereotypes defined in the uml-profile,
	 * with literals that have the enumeration as type. - enumerations+literals
	 * with stereotypes defined in the uml-model. Here it is the user's
	 * responsibility to maintain the type-compatibility of the literals.
	 * 
	 * @author Moritz@Eysholdt.de
	 */
	public Type getType(final Object obj) {
		if (obj instanceof EnumerationLiteral) {
			EnumerationLiteral el = (EnumerationLiteral) obj;
			String fqn = getFullName(el.getEnumeration());
			return getTypeSystem().getTypeForName(fqn);
		}
		else if (obj instanceof Element) {
			Element element = (Element) obj;
			List<Stereotype> stereotypes = element.getAppliedStereotypes();
			// if no stereotype is found, the stereotype is skipped or an
			// Exception is thrown
			if (stereotypes.isEmpty())
				// collection will be empty if the required profile is not
				// loaded
				if (errorIfStereotypeMissing && !stereotypes.toString().equals("[]"))
					throw new RuntimeException("Stereotype could not be loaded! Possible hint: '" + stereotypes);
				else
					return internalProfileMetaModel.getType(obj);

			List<StereotypeType> types = new ArrayList<StereotypeType>(stereotypes.size());
			// collect StereotypeTypes
			for (Iterator<Stereotype> iter = stereotypes.iterator(); iter.hasNext();) {
				Stereotype st = iter.next();
				Type theType = getTypeSystem().getTypeForName(getFullName(st));
				if (theType != null && theType instanceof StereotypeType) {
					StereotypeType stType = (StereotypeType) theType;
					types.add(stType);
				}
			}
			switch (types.size()) {
				case 0:
					return internalProfileMetaModel.getType(obj);
				case 1:
					return types.get(0);
					// when more than one stereotype is applied we return a
					// MultipleStereotypeType instance
					// containing all applied stereotypes
				default:
					return new MultipleStereotypeType(getTypeSystem(), types);
			}
		} else {
			return internalProfileMetaModel.getType(obj);
		}
	}

	public Set<Type> getKnownTypes() {
		return new HashSet<Type>(stereoTypes.values());
	}

	public TypeSystem getTypeSystem() {
		return typeSystem;
	}

	public void setTypeSystem(final TypeSystem typeSystem) {
		this.typeSystem = typeSystem;
		init();
	}

	public String getName() {
		return profile.getName();
	}

	/**
	 * @see MetaModel#getNamespaces()
	 */
	public Set<String> getNamespaces() {
		return namespaces;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6775.java