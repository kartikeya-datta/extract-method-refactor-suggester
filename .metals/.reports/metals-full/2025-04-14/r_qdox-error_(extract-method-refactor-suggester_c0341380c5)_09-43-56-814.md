error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8787.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8787.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8787.java
text:
```scala
r@@eturn "Flow context"/*nonNLS*/;

package org.eclipse.jdt.internal.compiler.flow;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */
import java.util.*;

import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.ast.*;
import org.eclipse.jdt.internal.compiler.codegen.*;
import org.eclipse.jdt.internal.compiler.lookup.*;
import org.eclipse.jdt.internal.compiler.problem.*;
import org.eclipse.jdt.internal.compiler.util.*;

/**
 * Reflects the context of code analysis, keeping track of enclosing
 *	try statements, exception handlers, etc...
 */
public class FlowContext implements TypeConstants {
	public AstNode associatedNode;
	public FlowContext parent;

	public final static FlowContext NotContinuableContext = new FlowContext(null,null);
public FlowContext(FlowContext parent, AstNode associatedNode) {
	this.parent = parent;
	this.associatedNode = associatedNode;
}
public Label breakLabel() {
	return null;
}
public void checkExceptionHandlers(TypeBinding[] raisedExceptions, AstNode location, FlowInfo flowInfo, BlockScope scope) {

	// check that all the argument exception types are handled

	// JDK Compatible implementation - when an exception type is thrown, 
	// all related catch blocks are marked as reachable... instead of those only
	// until the point where it is safely handled (Smarter - see comment at the end)

	int remainingCount; // counting the number of remaining unhandled exceptions
	int raisedCount; // total number of exceptions raised
	if ((raisedExceptions == null) || ((raisedCount = raisedExceptions.length) == 0))
		return;
	remainingCount = raisedCount;

	// duplicate the array of raised exceptions since it will be updated
	// (null replaces any handled exception)
	System.arraycopy(raisedExceptions, 0, (raisedExceptions = new TypeBinding[raisedCount]), 0, raisedCount);
	FlowContext traversedContext = this;
	while (traversedContext != null) {
		AstNode sub;
		if (((sub = traversedContext.subRoutine()) != null) && sub.cannotReturn()) {
			// traversing a non-returning subroutine means that all unhandled 
			// exceptions will actually never get sent...
			return;
		}

		// filter exceptions that are locally caught from the most enclosing 
		// try statement to the outer ones.
		if (traversedContext instanceof ExceptionHandlingFlowContext) {
			ExceptionHandlingFlowContext exceptionContext = (ExceptionHandlingFlowContext) traversedContext;
			ReferenceBinding[] caughtExceptions;
			if ((caughtExceptions = exceptionContext.handledExceptions) != NoExceptions) {
				int caughtCount = caughtExceptions.length;
				boolean[] locallyCaught = new boolean[raisedCount]; // at most

				for (int caughtIndex = 0; caughtIndex < caughtCount; caughtIndex++) {
					ReferenceBinding caughtException = caughtExceptions[caughtIndex];
					for (int raisedIndex = 0; raisedIndex < raisedCount; raisedIndex++) {
						TypeBinding raisedException;
						if ((raisedException = raisedExceptions[raisedIndex]) != null) {
							switch (scope.compareTypes(raisedException, caughtException)) {
								case EqualOrMoreSpecific :
									exceptionContext.recordHandlingException(caughtException, flowInfo.unconditionalInits(), raisedException, location, locallyCaught[raisedIndex]); // was already definitely caught ?
									if (!locallyCaught[raisedIndex]){
										locallyCaught[raisedIndex] = true; // remember that this exception has been definitely caught
										remainingCount--;
									}
									break;
								case MoreGeneric :
									exceptionContext.recordHandlingException(caughtException, flowInfo.unconditionalInits(), raisedException, location, false); // was not caught already per construction
							}
						}
					}
				}
				// remove locally caught exceptions from the remaining ones
				for (int i = 0; i < raisedCount; i++) {
					if (locallyCaught[i]) {
						raisedExceptions[i] = null; // removed from the remaining ones.
					}
				}
			}
			// method treatment for unchecked exceptions
			if (exceptionContext.isMethodContext){
				for (int i = 0; i < raisedCount; i++) {
					TypeBinding raisedException;
					if ((raisedException = raisedExceptions[i]) != null) {
						if (scope.areTypesCompatible(raisedException, scope.getJavaLangRuntimeException())
 scope.areTypesCompatible(raisedException, scope.getJavaLangError())){
							remainingCount --;
							raisedExceptions[i] = null;
						}
					}			
				}
			}
		}
		if (remainingCount == 0)
			return;
		traversedContext = traversedContext.parent;
	}

	// if reaches this point, then there are some remaining unhandled exception types.	
	for (int i = 0; i < raisedCount; i++) {
		TypeBinding exception;
		if ((exception = raisedExceptions[i]) != null) {
			scope.problemReporter().unhandledException(exception, location, scope);
		}
	}
}
/* 
"	- SMARTER VERSION -
 unhandledExceptionTypes nameEnv traversedContext |

someExceptionTypes isEmpty ifTrue: [^self].

unhandledExceptionTypes := someExceptionTypes asOrderedCollection.
nameEnv := scope enclosingMethod nameEnvironment.

traversedContext := self.
[traversedContext isNil] whileFalse: [| caughtExceptions sub |

((sub := traversedContext subRoutine) notNil and: [sub cannotReturn])
ifTrue: [
" "Traversing a non-returning subroutine means that all unhandled exceptions will actually
never get sent..." "
^self].
" "Filter exceptions that are locally caught from the most enclosing try statement to the outer ones." "
(caughtExceptions := traversedContext handledExceptions) isNil
ifFalse: [
caughtExceptions do: [:handledExceptionAssoc | | handledException |
handledException := handledExceptionAssoc key.
unhandledExceptionTypes copy do: [:raisedException | | safe |
" "Any exception recognized as being caught is removed from the exceptions list" "
((safe := raisedException isCompatibleWith: handledException in: nameEnv)
or: [handledException isCompatibleWith: raisedException in: nameEnv])
ifTrue: [
traversedContext
recordInitializationInfo: initInfo
onException: handledException.
handledExceptionAssoc value: true.
safe ifTrue: [unhandledExceptionTypes remove: raisedException]]]]].
unhandledExceptionTypes isEmpty ifTrue: [^self].
traversedContext := traversedContext parent].

scope enclosingMethod errorInterface
unexpectedExceptionsError: unhandledExceptionTypes
from: invocationSite

*/
public void checkExceptionHandlers(TypeBinding raisedException, AstNode location, FlowInfo flowInfo, BlockScope scope) {

	// LIGHT-VERSION OF THE EQUIVALENT WITH AN ARRAY OF EXCEPTIONS

	// check that all the argument exception types are handled
	// JDK Compatible implementation - when an exception type is thrown, 
	// all related catch blocks are marked as reachable... instead of those only
	// until the point where it is safely handled (Smarter - see comment at the end)


	FlowContext traversedContext = this;
	while (traversedContext != null) {
		AstNode sub;
		if (((sub = traversedContext.subRoutine()) != null) && sub.cannotReturn()) {
			// traversing a non-returning subroutine means that all unhandled 
			// exceptions will actually never get sent...
			return;
		}

		// filter exceptions that are locally caught from the most enclosing 
		// try statement to the outer ones.
		if (traversedContext instanceof ExceptionHandlingFlowContext) {
			ExceptionHandlingFlowContext exceptionContext = (ExceptionHandlingFlowContext) traversedContext;
			ReferenceBinding[] caughtExceptions;
			if ((caughtExceptions = exceptionContext.handledExceptions) != NoExceptions) {
				boolean definitelyCaught = false;
				for (int caughtIndex = 0, caughtCount = caughtExceptions.length; caughtIndex < caughtCount; caughtIndex++) {
					ReferenceBinding caughtException = caughtExceptions[caughtIndex];
					switch (scope.compareTypes(raisedException, caughtException)) {
						case EqualOrMoreSpecific :
							exceptionContext.recordHandlingException(
								caughtException, 
								flowInfo.unconditionalInits(), 
								raisedException, 
								location, 
								definitelyCaught); // was it already definitely caught ?
							definitelyCaught = true;
							break;
						case MoreGeneric :
							exceptionContext.recordHandlingException(
								caughtException, 
								flowInfo.unconditionalInits(), 
								raisedException, 
								location, 
								false); // was not caught already per construction
					}
				}
				if (definitelyCaught) return;
			}
			// method treatment for unchecked exceptions
			if (exceptionContext.isMethodContext){
				if (scope.areTypesCompatible(raisedException, scope.getJavaLangRuntimeException())
 scope.areTypesCompatible(raisedException, scope.getJavaLangError()))
					return;
				break; // not handled anywhere, thus jump to error handling
			}
		}
		traversedContext = traversedContext.parent;
	}

	// if reaches this point, then there are some remaining unhandled exception types.
	scope.problemReporter().unhandledException(raisedException, location, scope);
}
public Label continueLabel() {
	return null;
}
public FlowContext getTargetContextForBreakLabel(char[] labelName) {

	// lookup through break labels

	FlowContext current = this, lastNonReturningSubRoutine = null;
	while (current != null) {
		if (current.isNonReturningContext()) {
			lastNonReturningSubRoutine = current;
		}
		char[] currentLabelName;
		if (((currentLabelName = current.labelName()) != null) 
			&& CharOperation.equals(currentLabelName, labelName)) {
			if (lastNonReturningSubRoutine == null) {
				return current;
			} else {
				return lastNonReturningSubRoutine;
			}
		}
		current = current.parent;
	}

	// not found
	return null;
}
public FlowContext getTargetContextForContinueLabel(char[] labelName) {

	// lookup through continue labels

	FlowContext current = this, lastContinuable = null, lastNonReturningSubRoutine = null;
	while (current != null) {
		if (current.isNonReturningContext()) {
			lastNonReturningSubRoutine = current;
		} else {
			if (current.isContinuable()) {
				lastContinuable = current;
			}
		}
		char[] currentLabelName;		
		if (((currentLabelName = current.labelName()) != null) 
			&& CharOperation.equals(currentLabelName, labelName)) {
			if ((lastContinuable != null) && (current.associatedNode.concreteStatement() == lastContinuable.associatedNode)) {
				if (lastNonReturningSubRoutine == null) {
					return lastContinuable;
				} else {
					return lastNonReturningSubRoutine;
				}
			} else {
				// label is found, but not a continuable location
				return NotContinuableContext;
			}
		}
		current = current.parent;
	}

	// not found
	return null;
}
public FlowContext getTargetContextForDefaultBreak() {

	// lookup through break labels

	FlowContext current = this, lastNonReturningSubRoutine = null;
	while (current != null) {
		if (current.isNonReturningContext()) {
			lastNonReturningSubRoutine = current;
		}
		if (current.isBreakable()) {
			if (lastNonReturningSubRoutine == null) {
				return current;
			} else {
				return lastNonReturningSubRoutine;
			}
		}
		current = current.parent;
	}

	// not found
	return null;
}
public FlowContext getTargetContextForDefaultContinue() {

	// lookup through continue labels


	FlowContext current = this, lastNonReturningSubRoutine = null;
	while (current != null) {
		if (current.isNonReturningContext()) {
			lastNonReturningSubRoutine = current;
		}
		if (current.isContinuable()) {
			if (lastNonReturningSubRoutine == null) {
				return current;
			} else {
				return lastNonReturningSubRoutine;
			}
		}
		current = current.parent;
	}

	// not found
	return null;
}
public String individualToString(){
	return "Flow context";
}
public FlowInfo initsOnBreak() {
	return FlowInfo.DeadEnd;
}
public boolean isBreakable() {
	return false;
}
public boolean isContinuable() {
	return false;
}
public boolean isNonReturningContext() {
	return false;
}
public boolean isSubRoutine() {
	return false;
}
public char[] labelName() {
	return null;
}
public void recordBreakFrom(FlowInfo flowInfo) {
}
public void recordContinueFrom(FlowInfo flowInfo) {
}
boolean recordFinalAssignment(VariableBinding variable, Reference finalReference) {
	return true; // keep going
}
public void recordReturnFrom(UnconditionalFlowInfo flowInfo) {
}
public void recordSettingFinal(VariableBinding variable, Reference finalReference) {

	// for initialization inside looping statement that effectively loops

	FlowContext context = this;
	while (context != null) {
		if (!context.recordFinalAssignment(variable, finalReference)){
			break; // no need to keep going
		}
		context = context.parent;
	}
}
void removeFinalAssignmentIfAny(Reference reference) {
}
public AstNode subRoutine() {
	return null;
}
public String toString(){
	StringBuffer buffer = new StringBuffer();
	FlowContext current = this;

	int parentsCount = 0;
	while ((current = current.parent) != null){ parentsCount++; }
	
	FlowContext[] parents = new FlowContext[parentsCount+1];
	current = this;
	int index = parentsCount;
	while (index >= 0) { 
		parents[index--] = current;
		current = current.parent; 
	}

	for(int i = 0; i < parentsCount; i++){
		for(int j = 0; j < i; j++) buffer.append('\t');
		buffer.append(parents[i].individualToString()).append('\n');
	}
	buffer.append('*');
	for(int j = 0; j < parentsCount+1; j++) buffer.append('\t');
	buffer.append(individualToString()).append('\n');
	return buffer.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8787.java