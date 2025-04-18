error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13323.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13323.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[14,1]

error in qdox parser
file content:
```java
offset: 636
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13323.java
text:
```scala
public class StringTypeImpl extends BuiltinBaseType {

/*******************************************************************************
 * Copyright (c) 2005, 2007 committers of openArchitectureWare and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     committers of openArchitectureWare - initial API and implementation
 *******************************************************************************/

p@@ackage org.eclipse.internal.xtend.type.baseimpl.types;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.internal.xtend.type.baseimpl.OperationImpl;
import org.eclipse.internal.xtend.type.baseimpl.PropertyImpl;
import org.eclipse.internal.xtend.util.StringHelper;
import org.eclipse.xtend.expression.TypeSystem;
import org.eclipse.xtend.typesystem.Feature;
import org.eclipse.xtend.typesystem.Type;

/**
 * @author Sven Efftinge (http://www.efftinge.de)
 * @author Arno Haase
 */
@SuppressWarnings("unchecked")
public class StringTypeImpl extends BuiltinBaseType implements Type {
	final Log log = LogFactory.getLog(getClass());

	public StringTypeImpl(final TypeSystem ts, final String name) {
		super(ts, name);
	}

	public boolean isInstance(final Object o) {
		return o instanceof String || o instanceof StringBuffer || o instanceof Character;
	}

	public Object newInstance() {
		return "";
	}

	@Override
	public Feature[] getContributedFeatures() {
		return new Feature[] {
				new PropertyImpl(this, "length", getTypeSystem().getIntegerType()) {

					@Override
					public String getDocumentation() {
						return "the length of this string";
					}

					public Object get(final Object target) {
						return new Long(target.toString().length());
					}
				},

				new OperationImpl(this, "+", getTypeSystem().getStringType(), new Type[] { getTypeSystem()
						.getObjectType() }) {

					@Override
					public String getDocumentation() {
						return "concatenates two strings";
					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						return target.toString() + params[0];
					}
				},

				new OperationImpl(this, "startsWith", getTypeSystem().getBooleanType(), new Type[] { getTypeSystem()
						.getStringType() }) {

					@Override
					public String getDocumentation() {
						return "Tests if this string starts with the specified prefix.";
					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						final String token = (String) params[0];
						return new Boolean(target.toString().startsWith(token));
					}
				},
				new OperationImpl(this, "contains", getTypeSystem().getBooleanType(), new Type[] { getTypeSystem()
						.getStringType() }) {

					@Override
					public String getDocumentation() {
						return "Tests if this string contains substring.";
					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						final String token = (String) params[0];
						return new Boolean(target.toString().indexOf(token) >= 0);
					}
				},
				new OperationImpl(this, "endsWith", getTypeSystem().getBooleanType(), new Type[] { getTypeSystem()
						.getStringType() }) {

					@Override
					public String getDocumentation() {
						return "Tests if this string ends with the specified prefix.";
					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						final String token = (String) params[0];
						return new Boolean(target.toString().endsWith(token));
					}
				},
				new OperationImpl(this, "subString", getTypeSystem().getStringType(), new Type[] {
						getTypeSystem().getIntegerType(), getTypeSystem().getIntegerType() }) {

					@Override
					public String getDocumentation() {
						return "Returns a new string that is a substring of this string.";
					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						final Number from = (Number) params[0];
						final Number to = (Number) params[1];
						return target.toString().substring(from.intValue(), to.intValue());
					}
				},

				new OperationImpl(this, "toUpperCase", getTypeSystem().getStringType(), new Type[] {}) {

					@Override
					public String getDocumentation() {
						return "Converts all of the characters in this String to upper"
								+ " case using the rules of the default locale (from Java)";
					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						return target.toString().toUpperCase();
					}
				},

				new OperationImpl(this, "toLowerCase", getTypeSystem().getStringType(), new Type[] {}) {
					@Override
					public String getDocumentation() {
						return "Converts all of the characters in this String to lower"
								+ " case using the rules of the default locale (from Java)";
					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						return target.toString().toLowerCase();
					}
				},

				new OperationImpl(this, "toFirstUpper", getTypeSystem().getStringType(), new Type[] {}) {
					@Override
					public String getDocumentation() {
						return "Converts the first character in this String to upper"
								+ " case using the rules of the default locale (from Java)";
					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						return StringHelper.firstUpper(target.toString());
					}
				},

				new OperationImpl(this, "toFirstLower", getTypeSystem().getStringType(), new Type[] {}) {
					@Override
					public String getDocumentation() {
						return "Converts the first character in this String to lower"
								+ " case using the rules of the default locale (from Java)";
					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						return StringHelper.firstLower(target.toString());
					}
				},

				new OperationImpl(this, "toCharList", getTypeSystem().getListType(getTypeSystem().getStringType()),
						new Type[] {}) {

					@Override
					public String getDocumentation() {
						return "splits this String into a List[String] containing Strings of length 1";
					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						final String txt = target.toString();
						final List<String> result = new ArrayList<String>();
						final char[] chars = txt.toCharArray();
						for (int i = 0; i < chars.length; i++) {
							result.add(String.valueOf(chars[i]));
						}
						return result;
					}
				},

				new OperationImpl(this, "replaceAll", getTypeSystem().getStringType(), new Type[] {
						getTypeSystem().getStringType(), getTypeSystem().getStringType() }) {

					@Override
					public String getDocumentation() {
						return "Replaces each substring of this string that matches the given "
								+ "regular expression with the given replacement.";
					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						return target.toString().replaceAll(params[0].toString(), params[1].toString());
					}
				},

				new OperationImpl(this, "replaceFirst", getTypeSystem().getStringType(), new Type[] {
						getTypeSystem().getStringType(), getTypeSystem().getStringType() }) {

					@Override
					public String getDocumentation() {
						return "Replaces the first substring of this string that matches the given"
								+ " regular expression with the given replacement.";
					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						return target.toString().replaceFirst(params[0].toString(), params[1].toString());
					}
				},

				new OperationImpl(this, "split", getTypeSystem().getListType(getTypeSystem().getStringType()),
						new Type[] { getTypeSystem().getStringType() }) {

					@Override
					public String getDocumentation() {
						return "Splits this string around matches of the given regular expression (from Java 1.4)";

					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						return new ArrayList<String>(Arrays.asList(target.toString().split(params[0].toString())));
					}
				},

				new OperationImpl(this, "matches", getTypeSystem().getBooleanType(), new Type[] { getTypeSystem()
						.getStringType() }) {

					@Override
					public String getDocumentation() {
						return "Tells whether or not this string matches the given regular expression. (from Java 1.4)";

					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						return Boolean.valueOf(((String) target).matches((String) params[0]));
					}
				},

				new OperationImpl(this, "trim", getTypeSystem().getStringType(), new Type[] {}) {

					@Override
					public String getDocumentation() {
						return "Returns a copy of the string, with leading and trailing whitespace omitted. (from Java 1.4)";

					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						return ((String) target).trim();
					}
				},

				new OperationImpl(this, "asInteger", getTypeSystem().getIntegerType(), new Type[] {}) {

					@Override
					public String getDocumentation() {
						return "Returns an Integer object holding the value of the specified String (from Java 1.5)";

					}

					@Override
					public Object evaluateInternal(final Object target, final Object[] params) {
						try {
							return new BigInteger((String) target);
						}
						catch (NumberFormatException nfe) {
							log.error("'asInteger' on '" + target + "' returned null!");
							return null;
						}
					}
				}

		};
	}

	@Override
	public Set<Type> getSuperTypes() {
		return Collections.singleton(getTypeSystem().getObjectType());
	}

	private String toString(final Object o) {
		if (o == null)
			return null;
		if (isInstance(o))
			return o.toString();
		throw new IllegalArgumentException(o.getClass().getName() + " not supported");
	}

	@Override
	public Object convert(final Object src, final Class targetType) {
		final String s = toString(src);
		if (targetType.isAssignableFrom(String.class))
			return s;
		else if (targetType.isAssignableFrom(Character.class) || targetType.isAssignableFrom(Character.TYPE)) {
			if (s.length() == 1)
				return new Character(s.charAt(0));
		}
		else if (targetType.isAssignableFrom(StringBuffer.class))
			return new StringBuffer(s);
		return super.convert(src, targetType);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13323.java