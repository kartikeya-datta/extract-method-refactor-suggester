error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14926.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14926.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14926.java
text:
```scala
r@@eturn TypedValue.NULL;

/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.expression.spel.standard;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.TypedValue;
import org.springframework.expression.common.ExpressionUtils;
import org.springframework.expression.spel.ExpressionState;
import org.springframework.expression.spel.SpelNode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.ast.SpelNodeImpl;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

/**
 * A SpelExpressions represents a parsed (valid) expression that is ready to be evaluated in a specified context. An
 * expression can be evaluated standalone or in a specified context. During expression evaluation the context may be
 * asked to resolve references to types, beans, properties, methods.
 * 
 * @author Andy Clement
 * @since 3.0
 */
public class SpelExpression implements Expression {
	
	private final String expression;

	private final SpelNodeImpl ast;

	private final SpelParserConfiguration configuration;

	// the default context is used if no override is supplied by the user
	private EvaluationContext defaultContext;


	/**
	 * Construct an expression, only used by the parser.
	 */
	public SpelExpression(String expression, SpelNodeImpl ast, SpelParserConfiguration configuration) {
		this.expression = expression;
		this.ast = ast;
		this.configuration = configuration;
	}


	// implementing Expression
	
	public Object getValue() throws EvaluationException {
		ExpressionState expressionState = new ExpressionState(getEvaluationContext(), configuration);
		return ast.getValue(expressionState);
	}

	public Object getValue(Object rootObject) throws EvaluationException {
		ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), configuration);
		return ast.getValue(expressionState);
	}

	public <T> T getValue(Class<T> expectedResultType) throws EvaluationException {
		ExpressionState expressionState = new ExpressionState(getEvaluationContext(), configuration);
		Object result = ast.getValue(expressionState);
		return ExpressionUtils.convert(expressionState.getEvaluationContext(), result, expectedResultType);
	}

	public <T> T getValue(Object rootObject, Class<T> expectedResultType) throws EvaluationException {
		ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), configuration);
		Object result = ast.getValue(expressionState);
		return ExpressionUtils.convert(expressionState.getEvaluationContext(), result, expectedResultType);
	}

	public Object getValue(EvaluationContext context) throws EvaluationException {
		Assert.notNull(context, "The EvaluationContext is required");
		return ast.getValue(new ExpressionState(context, configuration));
	}
	
	public Object getValue(EvaluationContext context, Object rootObject) throws EvaluationException {
		Assert.notNull(context, "The EvaluationContext is required");
		return ast.getValue(new ExpressionState(context, toTypedValue(rootObject), configuration));
	}

	@SuppressWarnings("unchecked")
	public <T> T getValue(EvaluationContext context, Class<T> expectedResultType) throws EvaluationException {
		Object result = ast.getValue(new ExpressionState(context, configuration));
		if (result != null && expectedResultType != null) {
			Class<?> resultType = result.getClass();
			if (!expectedResultType.isAssignableFrom(resultType)) {
				// Attempt conversion to the requested type, may throw an exception
				result = context.getTypeConverter().convertValue(result, TypeDescriptor.valueOf(expectedResultType));
			}
		}
		return (T) result;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(EvaluationContext context, Object rootObject, Class<T> expectedResultType) throws EvaluationException {
		Object result = ast.getValue(new ExpressionState(context, toTypedValue(rootObject), configuration));
		if (result != null && expectedResultType != null) {
			Class<?> resultType = result.getClass();
			if (!expectedResultType.isAssignableFrom(resultType)) {
				// Attempt conversion to the requested type, may throw an exception
				result = context.getTypeConverter().convertValue(result, TypeDescriptor.valueOf(expectedResultType));
			}
		}
		return (T) result;
	}


	public Class getValueType() throws EvaluationException {
		return ast.getValueInternal(new ExpressionState(getEvaluationContext(), configuration)).getTypeDescriptor().getType();
	}

	public Class getValueType(EvaluationContext context) throws EvaluationException {
		Assert.notNull(context, "The EvaluationContext is required");
		ExpressionState eState = new ExpressionState(context, configuration);
		TypeDescriptor typeDescriptor = ast.getValueInternal(eState).getTypeDescriptor();
		return typeDescriptor.getType();
	}

	public Class getValueType(EvaluationContext context, Object rootObject) throws EvaluationException {
		ExpressionState eState = new ExpressionState(context, toTypedValue(rootObject), configuration);
		TypeDescriptor typeDescriptor = ast.getValueInternal(eState).getTypeDescriptor();
		return typeDescriptor.getType();
	}

	public Class getValueType(Object rootObject) throws EvaluationException {
		return ast.getValueInternal(new ExpressionState(getEvaluationContext(), configuration)).getTypeDescriptor().getType();
	}

	public TypeDescriptor getValueTypeDescriptor() throws EvaluationException {
		return ast.getValueInternal(new ExpressionState(getEvaluationContext(), configuration)).getTypeDescriptor();
	}
	
	public TypeDescriptor getValueTypeDescriptor(Object rootObject) throws EvaluationException {
		ExpressionState eState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), configuration);
		return ast.getValueInternal(eState).getTypeDescriptor();
	}
	
	public TypeDescriptor getValueTypeDescriptor(EvaluationContext context) throws EvaluationException {
		Assert.notNull(context, "The EvaluationContext is required");
		ExpressionState eState = new ExpressionState(context, configuration);
		return ast.getValueInternal(eState).getTypeDescriptor();
	}
	
	public TypeDescriptor getValueTypeDescriptor(EvaluationContext context, Object rootObject) throws EvaluationException {
		Assert.notNull(context, "The EvaluationContext is required");
		ExpressionState eState = new ExpressionState(context, toTypedValue(rootObject), configuration);
		return ast.getValueInternal(eState).getTypeDescriptor();
	}
	
	public String getExpressionString() {
		return expression;
	}

	public boolean isWritable(EvaluationContext context) throws EvaluationException {
		Assert.notNull(context, "The EvaluationContext is required");		
		return ast.isWritable(new ExpressionState(context, configuration));
	}

	public boolean isWritable(Object rootObject) throws EvaluationException {
		return ast.isWritable(new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), configuration));
	}
	
	public boolean isWritable(EvaluationContext context, Object rootObject) throws EvaluationException {
		Assert.notNull(context, "The EvaluationContext is required");		
		return ast.isWritable(new ExpressionState(context, toTypedValue(rootObject), configuration));
	}

	public void setValue(EvaluationContext context, Object value) throws EvaluationException {
		Assert.notNull(context, "The EvaluationContext is required");		
		ast.setValue(new ExpressionState(context, configuration), value);
	}

	public void setValue(Object rootObject, Object value) throws EvaluationException {
		ast.setValue(new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), configuration), value);
	}
	
	public void setValue(EvaluationContext context, Object rootObject, Object value) throws EvaluationException {
		Assert.notNull(context, "The EvaluationContext is required");		
		ast.setValue(new ExpressionState(context, toTypedValue(rootObject), configuration), value);
	}

	
	// impl only

	/**
	 * @return return the Abstract Syntax Tree for the expression
	 */
	public SpelNode getAST() {
		return ast;
	}

	/**
	 * Produce a string representation of the Abstract Syntax Tree for the expression, this should ideally look like the
	 * input expression, but properly formatted since any unnecessary whitespace will have been discarded during the
	 * parse of the expression.
	 * @return the string representation of the AST
	 */
	public String toStringAST() {
		return ast.toStringAST();
	}
	
	/**
     * Return the default evaluation context that will be used if none is supplied on an evaluation call
     * @return the default evaluation context
     */
	public EvaluationContext getEvaluationContext() {
		if (defaultContext == null) {
			defaultContext = new StandardEvaluationContext();
		}
		return defaultContext;
	}
	
	/**
     * Set the evaluation context that will be used if none is specified on an evaluation call.
     * @param context an evaluation context
     */
	public void setEvaluationContext(EvaluationContext context) {
		this.defaultContext = context;
	}

	private TypedValue toTypedValue(Object object) {
		if (object == null) {
			return TypedValue.NULL_TYPED_VALUE;
		}
		else {
			return new TypedValue(object);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14926.java