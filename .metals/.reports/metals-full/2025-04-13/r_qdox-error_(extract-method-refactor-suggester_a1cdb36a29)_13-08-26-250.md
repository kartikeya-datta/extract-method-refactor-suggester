error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2150.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2150.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2150.java
text:
```scala
c@@odeStream.updateLastRecordedEndPC(this.scope, codeStream.position);

/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.flow.*;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;

public class SwitchStatement extends Statement {

	public Expression expression;
	public Statement[] statements;
	public BlockScope scope;
	public int explicitDeclarations;
	public Label breakLabel;
	public CaseStatement[] cases;
	public CaseStatement defaultCase;
	public int blockStart;
	public int caseCount;
	int[] constants;
	
	// for local variables table attributes
	int preSwitchInitStateIndex = -1;
	int mergedInitStateIndex = -1;

	public FlowInfo analyseCode(
			BlockScope currentScope,
			FlowContext flowContext,
			FlowInfo flowInfo) {

	    try {
			flowInfo = expression.analyseCode(currentScope, flowContext, flowInfo);
			SwitchFlowContext switchContext =
				new SwitchFlowContext(flowContext, this, (breakLabel = new Label()));
	
			// analyse the block by considering specially the case/default statements (need to bind them 
			// to the entry point)
			FlowInfo caseInits = FlowInfo.DEAD_END;
			// in case of statements before the first case
			preSwitchInitStateIndex =
				currentScope.methodScope().recordInitializationStates(flowInfo);
			int caseIndex = 0;
			if (statements != null) {
				boolean didAlreadyComplain = false;
				for (int i = 0, max = statements.length; i < max; i++) {
					Statement statement = statements[i];
					if ((caseIndex < caseCount) && (statement == cases[caseIndex])) { // statement is a case
						this.scope.enclosingCase = cases[caseIndex]; // record entering in a switch case block
						caseIndex++;
						caseInits = caseInits.mergedWith(flowInfo.copy().unconditionalInits());
						didAlreadyComplain = false; // reset complaint
					} else if (statement == defaultCase) { // statement is the default case
						this.scope.enclosingCase = defaultCase; // record entering in a switch case block
						caseInits = caseInits.mergedWith(flowInfo.copy().unconditionalInits());
						didAlreadyComplain = false; // reset complaint
					}
					if (!statement.complainIfUnreachable(caseInits, scope, didAlreadyComplain)) {
						caseInits = statement.analyseCode(scope, switchContext, caseInits);
					} else {
						didAlreadyComplain = true;
					}
				}
			}
	
			// if no default case, then record it may jump over the block directly to the end
			if (defaultCase == null) {
				// only retain the potential initializations
				flowInfo.addPotentialInitializationsFrom(
					caseInits.mergedWith(switchContext.initsOnBreak));
				mergedInitStateIndex =
					currentScope.methodScope().recordInitializationStates(flowInfo);
				return flowInfo;
			}
	
			// merge all branches inits
			FlowInfo mergedInfo = caseInits.mergedWith(switchContext.initsOnBreak);
			mergedInitStateIndex =
				currentScope.methodScope().recordInitializationStates(mergedInfo);
			return mergedInfo;
	    } finally {
	        if (this.scope != null) this.scope.enclosingCase = null; // no longer inside switch case block
	    }
	}

	/**
	 * Switch code generation
	 *
	 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
	 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
	 */
	public void generateCode(BlockScope currentScope, CodeStream codeStream) {

	    try {
			if ((bits & IsReachableMASK) == 0) {
				return;
			}
			int pc = codeStream.position;
	
			// prepare the labels and constants
			this.breakLabel.initialize(codeStream);
			CaseLabel[] caseLabels = new CaseLabel[this.caseCount];
			boolean needSwitch = this.caseCount != 0;
			for (int i = 0; i < caseCount; i++) {
				cases[i].targetLabel = (caseLabels[i] = new CaseLabel(codeStream));
			}
			CaseLabel defaultLabel = new CaseLabel(codeStream);
			if (defaultCase != null) {
				defaultCase.targetLabel = defaultLabel;
			}
			// generate expression testes
			expression.generateCode(currentScope, codeStream, needSwitch);
			// generate the appropriate switch table/lookup bytecode
			if (needSwitch) {
				int[] sortedIndexes = new int[this.caseCount];
				// we sort the keys to be able to generate the code for tableswitch or lookupswitch
				for (int i = 0; i < caseCount; i++) {
					sortedIndexes[i] = i;
				}
				int[] localKeysCopy;
				System.arraycopy(this.constants, 0, (localKeysCopy = new int[this.caseCount]), 0, this.caseCount);
				CodeStream.sort(localKeysCopy, 0, this.caseCount - 1, sortedIndexes);

				// for enum constants, actually switch on constant ordinal()
				if (this.expression.resolvedType.isEnum()) {
					codeStream.invokeEnumOrdinal(this.expression.resolvedType.constantPoolName());
				}
				int max = localKeysCopy[this.caseCount - 1];
				int min = localKeysCopy[0];
				if ((long) (caseCount * 2.5) > ((long) max - (long) min)) {
					
					// work-around 1.3 VM bug, if max>0x7FFF0000, must use lookup bytecode
					// see http://dev.eclipse.org/bugs/show_bug.cgi?id=21557
					if (max > 0x7FFF0000 && currentScope.environment().options.complianceLevel < ClassFileConstants.JDK1_4) {
						codeStream.lookupswitch(defaultLabel, this.constants, sortedIndexes, caseLabels);
	
					} else {
						codeStream.tableswitch(
							defaultLabel,
							min,
							max,
							this.constants,
							sortedIndexes,
							caseLabels);
					}
				} else {
					codeStream.lookupswitch(defaultLabel, this.constants, sortedIndexes, caseLabels);
				}
				codeStream.updateLastRecordedEndPC(codeStream.position);
			}
			
			// generate the switch block statements
			int caseIndex = 0;
			if (this.statements != null) {
				for (int i = 0, maxCases = this.statements.length; i < maxCases; i++) {
					Statement statement = this.statements[i];
					if ((caseIndex < this.caseCount) && (statement == this.cases[caseIndex])) { // statements[i] is a case
						this.scope.enclosingCase = this.cases[caseIndex]; // record entering in a switch case block
						if (preSwitchInitStateIndex != -1) {
							codeStream.removeNotDefinitelyAssignedVariables(currentScope, preSwitchInitStateIndex);
						}
						caseIndex++;
					} else {
						if (statement == this.defaultCase) { // statements[i] is a case or a default case
							this.scope.enclosingCase = this.defaultCase; // record entering in a switch case block
							if (preSwitchInitStateIndex != -1) {
								codeStream.removeNotDefinitelyAssignedVariables(currentScope, preSwitchInitStateIndex);
							}
						}
					}
					statement.generateCode(scope, codeStream);
				}
			}
			// place the trailing labels (for break and default case)
			this.breakLabel.place();
			if (defaultCase == null) {
				defaultLabel.place();
			}
			// May loose some local variable initializations : affecting the local variable attributes
			if (mergedInitStateIndex != -1) {
				codeStream.removeNotDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
				codeStream.addDefinitelyAssignedVariables(currentScope, mergedInitStateIndex);
			}
			if (scope != currentScope) {
				codeStream.exitUserScope(this.scope);
			}
			codeStream.recordPositionsFrom(pc, this.sourceStart);
	    } finally {
	        if (this.scope != null) this.scope.enclosingCase = null; // no longer inside switch case block
	    }		
	}

	public StringBuffer printStatement(int indent, StringBuffer output) {

		printIndent(indent, output).append("switch ("); //$NON-NLS-1$
		expression.printExpression(0, output).append(") {"); //$NON-NLS-1$
		if (statements != null) {
			for (int i = 0; i < statements.length; i++) {
				output.append('\n');
				if (statements[i] instanceof CaseStatement) {
					statements[i].printStatement(indent, output);
				} else {
					statements[i].printStatement(indent+2, output);
				}
			}
		}
		output.append("\n"); //$NON-NLS-1$
		return printIndent(indent, output).append('}');
	}

	public void resolve(BlockScope upperScope) {
	
	    try {
			boolean isEnumSwitch = false;
			TypeBinding expressionType = expression.resolveType(upperScope);
			if (expressionType == null)
				return;
			expression.computeConversion(upperScope, expressionType, expressionType);
			checkType: {
				if (expressionType.isBaseType()) {
					if (expression.isConstantValueOfTypeAssignableToType(expressionType, IntBinding))
						break checkType;
					if (expressionType.isCompatibleWith(IntBinding))
						break checkType;
				} else if (expressionType.isEnum()) {
					isEnumSwitch = true;
					break checkType;
				} else if (upperScope.isBoxingCompatibleWith(expressionType, IntBinding)) {
					expression.computeConversion(upperScope, IntBinding, expressionType);
					break checkType;
				}
				upperScope.problemReporter().incorrectSwitchType(expression, expressionType);
				// TODO (philippe) could keep analyzing switch statements in case of error
				return;
			}
			if (statements != null) {
				scope = /*explicitDeclarations == 0 ? upperScope : */new BlockScope(upperScope);
				int length;
				// collection of cases is too big but we will only iterate until caseCount
				cases = new CaseStatement[length = statements.length];
				this.constants = new int[length];
				CaseStatement[] duplicateCaseStatements = null;
				int duplicateCaseStatementsCounter = 0;
				int counter = 0;
				for (int i = 0; i < length; i++) {
					Constant constant;
					final Statement statement = statements[i];
					if ((constant = statement.resolveCase(scope, expressionType, this)) != Constant.NotAConstant) {
						int key = constant.intValue();
						//----check for duplicate case statement------------
						for (int j = 0; j < counter; j++) {
							if (this.constants[j] == key) {
								final CaseStatement currentCaseStatement = (CaseStatement) statement;
								if (duplicateCaseStatements == null) {
									scope.problemReporter().duplicateCase(cases[j]);
									scope.problemReporter().duplicateCase(currentCaseStatement);
									duplicateCaseStatements = new CaseStatement[length];
									duplicateCaseStatements[duplicateCaseStatementsCounter++] = cases[j];
									duplicateCaseStatements[duplicateCaseStatementsCounter++] = currentCaseStatement;
								} else {
									boolean found = false;
									searchReportedDuplicate: for (int k = 2; k < duplicateCaseStatementsCounter; k++) {
										if (duplicateCaseStatements[k] == statement) {
											found = true;
											break searchReportedDuplicate;
										}
									}
									if (!found) {
										scope.problemReporter().duplicateCase(currentCaseStatement);
										duplicateCaseStatements[duplicateCaseStatementsCounter++] = currentCaseStatement;
									}
								}
							}
						}
						this.constants[counter++] = key;
					}
				}
				if (length != counter) { // resize constants array
					System.arraycopy(this.constants, 0, this.constants = new int[counter], 0, counter);
				}
			} else {
				if ((this.bits & UndocumentedEmptyBlockMASK) != 0) {
					upperScope.problemReporter().undocumentedEmptyBlock(this.blockStart, this.sourceEnd);
				}
			}
			// for enum switch, check if all constants are accounted for (if no default) 
			if (isEnumSwitch && defaultCase == null 
					&& scope.environment().options.getSeverity(CompilerOptions.IncompleteEnumSwitch) != ProblemSeverities.Ignore
					&& caseCount != ((ReferenceBinding)expressionType).enumConstantCount()) {
				FieldBinding[] enumFields = ((ReferenceBinding)expressionType.erasure()).fields();
				for (int i = 0, max = enumFields.length; i <max; i++) {
					FieldBinding enumConstant = enumFields[i];
					if ((enumConstant.modifiers & AccEnum) == 0) continue;
					findConstant : {
						for (int j = 0; j < caseCount; j++) {
							if (enumConstant.id == this.constants[j]) break findConstant;
						}
						// enum constant did not get referenced from switch
						scope.problemReporter().missingEnumConstantCase(this, enumConstant);
					}
				}
			}
	    } finally {
	        if (this.scope != null) this.scope.enclosingCase = null; // no longer inside switch case block
	    }
	}

	public void traverse(
			ASTVisitor visitor,
			BlockScope blockScope) {

		if (visitor.visit(this, blockScope)) {
			expression.traverse(visitor, scope);
			if (statements != null) {
				int statementsLength = statements.length;
				for (int i = 0; i < statementsLength; i++)
					statements[i].traverse(visitor, scope);
			}
		}
		visitor.endVisit(this, blockScope);
	}
	
	/**
	 * Dispatch the call on its last statement.
	 */
	public void branchChainTo(Label label) {
		
		// in order to improve debug attributes for stepping (11431)
		// we want to inline the jumps to #breakLabel which already got
		// generated (if any), and have them directly branch to a better
		// location (the argument label).
		// we know at this point that the breakLabel already got placed
		if (this.breakLabel.hasForwardReferences()) {
			label.appendForwardReferencesFrom(this.breakLabel);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2150.java