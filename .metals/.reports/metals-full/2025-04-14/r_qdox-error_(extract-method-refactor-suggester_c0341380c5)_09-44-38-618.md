error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1419.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1419.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1419.java
text:
```scala
b@@indOperand(fromList, subqueryList,

/*

   Derby - Class org.apache.derby.impl.sql.compile.UnaryArithmeticOperatorNode

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package	org.apache.derby.impl.sql.compile;

import org.apache.derby.iapi.sql.dictionary.DataDictionary;

import org.apache.derby.iapi.types.StringDataValue;
import org.apache.derby.iapi.types.TypeId;
import org.apache.derby.iapi.types.DataTypeDescriptor;
import org.apache.derby.iapi.reference.SQLState;
import org.apache.derby.iapi.error.StandardException;

import org.apache.derby.iapi.services.compiler.MethodBuilder;
import org.apache.derby.iapi.services.sanity.SanityManager;
import org.apache.derby.iapi.sql.compile.C_NodeTypes;
import org.apache.derby.impl.sql.compile.ExpressionClassBuilder;

import java.sql.Types;
import java.util.Vector;

/**
 * This node represents a unary arithmetic operator
 *
 */

public class UnaryArithmeticOperatorNode extends UnaryOperatorNode
{
	private final static int UNARY_PLUS	= 0;
	private final static int UNARY_MINUS	= 1;
	private final static int SQRT = 2;
	private final static int ABSOLUTE = 3;
	private final static String[] UNARY_OPERATORS = {"+","-","SQRT", "ABS/ABSVAL"};
	private final static String[] UNARY_METHODS = {"plus","minus","sqrt", "absolute"};

	private int operatorType;

	//when the bindExpression method is called during the normal binding phase,
	//unary minus and unary plus dynamic parameters are not ready for
	//binding because the type of these dynamic parameters is not yet set.
	//For eg, consider sql select * from t1 where c1 = -?
	//bindExpression on -? gets called from BinaryComparisonOperatorNode's
	//bindExpression but the parameter type has not been set yet for -?
	//Later on, in BinaryComparisonOperatorNode's bindExpression, the type
	//of the -? gets set to the type of c1 by the setType call. 
	//Now, at this point, we are ready to finish binding phase for -? 
	//(This class's setType method calls the bindExpression to finish binding)
	//In order to accomplish binding later on, we need to save the following 
	//3 objects during first call to bindExpression and then later this 
	//gets used in setType method when it calls the bindExpression method.
	FromList localCopyFromList;
	SubqueryList localCopySubqueryList;
	Vector localAggregateVector;
  
	/**
	 * Initializer for a UnaryArithmeticOperatorNode
	 *
	 * @param operand		The operand of the node
	 */
	public void init(Object operand)
	{
		switch(getNodeType())
		{
			case C_NodeTypes.UNARY_PLUS_OPERATOR_NODE:
				operatorType = UNARY_PLUS;
				break;
			case C_NodeTypes.UNARY_MINUS_OPERATOR_NODE:
				operatorType = UNARY_MINUS;
				break;
			case C_NodeTypes.SQRT_OPERATOR_NODE:
				operatorType = SQRT;
				break;
			case C_NodeTypes.ABSOLUTE_OPERATOR_NODE:
				operatorType = ABSOLUTE;
				break;
			default:
				if (SanityManager.DEBUG)
				{
					SanityManager.THROWASSERT("init for UnaryArithmeticOperator called with wrong nodeType = " + getNodeType());
				}
			    break;
		}
		init(operand, UNARY_OPERATORS[this.operatorType], 
				UNARY_METHODS[this.operatorType]);
	}

	/**
	 * By default unary operators don't accept ? parameters as operands.
	 * This can be over-ridden for particular unary operators.
	 *
	 *	We throw an exception if the parameter doesn't have a datatype
	 *	assigned to it yet.
	 *
	 * @exception StandardException		Thrown if ?  parameter doesn't
	 *									have a type bound to it yet.
	 *									? parameter where it isn't allowed.
	 */

	void bindParameter() throws StandardException
	{
		if (operatorType == SQRT || operatorType == ABSOLUTE)
		{
			operand.setType(
				new DataTypeDescriptor(TypeId.getBuiltInTypeId(Types.DOUBLE), true));
		}
		//Derby-582 add support for dynamic parameter for unary plus and minus
		else if (operatorType == UNARY_MINUS || operatorType == UNARY_PLUS) 
			return;
		else if (operand.getTypeServices() == null)
		{
			throw StandardException.newException(SQLState.LANG_UNARY_OPERAND_PARM, operator);
		}
	}
	/**
	 * Bind this operator
	 *
	 * @param fromList			The query's FROM list
	 * @param subqueryList		The subquery list being built as we find SubqueryNodes
	 * @param aggregateVector	The aggregate vector being built as we find AggregateNodes
	 *
	 * @return	The new top of the expression tree.
	 *
	 * @exception StandardException		Thrown on error
	 */

	public ValueNode bindExpression(
		FromList	fromList, SubqueryList subqueryList,
		Vector	aggregateVector)
			throws StandardException
	{
		localCopyFromList = fromList;
		localCopySubqueryList = subqueryList;
		localAggregateVector = aggregateVector;
		//Return with no binding, if the type of unary minus/plus parameter is not set yet.
		if (operand.requiresTypeFromContext() && ((operatorType == UNARY_PLUS || operatorType == UNARY_MINUS))
				&& operand.getTypeServices() == null)
				return this;

		super.bindExpression(fromList, subqueryList,
				aggregateVector);

		if (operatorType == SQRT || operatorType == ABSOLUTE)
		{
			bindSQRTABS();
		}
		else if (operatorType == UNARY_PLUS || operatorType == UNARY_MINUS)
		{
			TypeId operandType = operand.getTypeId();

			if ( ! operandType.isNumericTypeId())
			{
			
				throw StandardException.newException(SQLState.LANG_UNARY_ARITHMETIC_BAD_TYPE, 
					(operatorType == UNARY_PLUS) ? "+" : "-", 
					operandType.getSQLTypeName());
			}
		}
		/*
		** The result type of a +, -, SQRT, ABS is the same as its operand.
		*/
		super.setType(operand.getTypeServices());
		return this;
	}

	/**
	 * Do code generation for this unary plus operator
	 *
	 * @param acb	The ExpressionClassBuilder for the class we're generating
	 * @param mb	The method the expression will go into
	 *
	 * @exception StandardException		Thrown on error
	 */

	public void generateExpression(ExpressionClassBuilder acb,
											MethodBuilder mb)
									throws StandardException
	{
		/* Unary + doesn't do anything.  Just return the operand */
		if (operatorType == UNARY_PLUS)
			operand.generateExpression(acb, mb);
		else
			super.generateExpression(acb, mb);
	}
	/**
	 * Bind SQRT or ABS
	 *
	 * @exception StandardException		Thrown on error
	 */
	private void bindSQRTABS()
			throws StandardException
	{
		TypeId	operandType;
		int 	jdbcType;

		/*
		** Check the type of the operand 
		*/
		operandType = operand.getTypeId();

		/*
	 	 * If the operand is not a build-in type, generate a bound conversion
		 * tree to build-in types.
		 */
		if (operandType.userType() )
		{
			operand = operand.genSQLJavaSQLTree();
		}
		/* DB2 doesn't cast string types to numeric types for numeric functions  */

		jdbcType = operandType.getJDBCTypeId();

		/* Both SQRT and ABS are only allowed on numeric types */
		if (!operandType.isNumericTypeId())
			throw StandardException.newException(
						SQLState.LANG_UNARY_FUNCTION_BAD_TYPE, 
						getOperatorString(), operandType.getSQLTypeName());

		/* For SQRT, if operand is not a DOUBLE, convert it to DOUBLE */
		if (operatorType == SQRT && jdbcType != Types.DOUBLE)
		{
			operand = (ValueNode) getNodeFactory().getNode(
					C_NodeTypes.CAST_NODE,
					operand,
					new DataTypeDescriptor(TypeId.getBuiltInTypeId(Types.DOUBLE), true),
					getContextManager());
			((CastNode) operand).bindCastNodeOnly();
		}
	}

	/** @see ValueNode#setType */
	/* We are overwriting this method here because for -?/+?, we now know
	the type of these dynamic parameters and hence we can do the parameter
	binding. The setType method will call the binding code after setting
	the type of the parameter*/
	public void setType(DataTypeDescriptor descriptor) throws StandardException
	{
		operand.setType(descriptor);
		super.setType(descriptor);
		//Derby-582 add support for dynamic parameters for unary plus and minus
		//Now that we know the type of this parameter node, we can do the
		//binding.
		bindExpression(localCopyFromList, localCopySubqueryList, localAggregateVector);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/1419.java