error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2404.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2404.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2404.java
text:
```scala
i@@f (target != null && target.getName().equals(name) ) {

package org.argouml.ocl;

import java.util.*;

import tudresden.ocl.check.types.*;
import tudresden.ocl.check.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;

import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.MMUtil;

public class ArgoFacade implements ModelFacade {

    public MClassifier target;

    public ArgoFacade(Object target) {
	if (target instanceof MClassifier)
	    this.target = (MClassifier)target;
    }

    public Any getClassifier(String name) {
      Project p = ProjectBrowser.TheInstance.getProject();
      if (target != null && target.getName() == name ) {
	  return new ArgoAny(target);
      }
      // else we have a problem: this is not clean!
      else {
	  MClassifier classifier = p.findTypeInModel(name, p.getCurrentNamespace());
	  if (classifier == null) {
	      throw new OclTypeException("cannot find classifier: "+name);
	  }
	  return new ArgoAny(classifier);
      }
    }
}

class ArgoAny implements Any {

    MClassifier classifier;

    ArgoAny (MClassifier classifier) {
      this.classifier = classifier;
    }

    public Type navigateQualified(String name, Type[] qualifiers) {

      if (classifier == null) {
          throw new OclTypeException("attempting to access features of Void");
      }


      if (qualifiers != null) {
          throw new OclTypeException("qualified associations not supported yet!");
      }

      Type type = Basic.navigateAnyQualified(name, this, qualifiers);
      if (type != null) return type;

      MClassifier foundAssocType=null, foundAttribType=null;
      boolean isSet=false, isSequence=false; // cannot be Bag

      // first search for appropriate attributes
      java.util.Collection attributes = MMUtil.SINGLETON.getAttributesInh(classifier);
      Iterator iter = attributes.iterator();
      while (iter.hasNext() && foundAttribType == null) {
        MAttribute attr = (MAttribute)iter.next();
        if (attr.getName().equals(name)) {
          foundAttribType = attr.getType();
        }
      }

      // look for associations
      java.util.Collection associationEnds = MMUtil.SINGLETON.getAssociateEndsInh(classifier);
      Iterator asciter = associationEnds.iterator();
      while (asciter.hasNext() && foundAssocType == null) {
        MAssociationEnd ae = (MAssociationEnd)asciter.next();
        if (ae.getName()!=null && name.equals(ae.getName())) {
          foundAssocType = ae.getType();
        } else if (ae.getName()==null || ae.getName().equals("")) {
          String oppositeName = ae.getType().getName();
          if (oppositeName != null) {
            String lowerOppositeName = oppositeName.substring(0,1).toLowerCase();
            lowerOppositeName += oppositeName.substring(1);
            if (lowerOppositeName.equals(name))
              foundAssocType = ae.getType();
          }
        }
        if (foundAssocType!=null) {
          if (ae.getMultiplicity()!=null &&
              (ae.getMultiplicity().getUpper() > 1 || ae.getMultiplicity().getUpper()==-1)
             ) {
            // to do: think about the condition of this if-statement
            // ordered association end -> Sequence; otherwise -> Set
            if ( ae.getStereotype()!=null && ae.getStereotype().toString()!=null &&
                 "ordered".equals(ae.getStereotype().toString()) ) {
              isSequence=true;
            } else {
              isSet=true;
            }
          }
        }
      }

      if (foundAssocType!=null && foundAttribType!=null) {
        throw new OclTypeException(
          "cannot access feature "+name+" of classifier "+toString()+" because both "+
          "an attribute and an association end of this name where found"
        );
      }

      MClassifier foundType = (foundAssocType==null) ? foundAttribType : foundAssocType;

      if (foundType == null) {
        throw new OclTypeException("attribute "+name+" not found in classifier "+toString());
      }

      Type result = getOclRepresentation(foundType);

      if (isSet) {
        result=new tudresden.ocl.check.types.Collection(
          tudresden.ocl.check.types.Collection.SET, result
        );
      }
      if (isSequence) {
        result=new tudresden.ocl.check.types.Collection(
          tudresden.ocl.check.types.Collection.SEQUENCE, result
        );
      }

      return result;
    }

    public Type navigateParameterized(String name, Type[] params) {
      if (classifier == null) {
          throw new OclTypeException("attempting to access features of Void");
      }

      Type type = Basic.navigateAnyParameterized(name, params);
      if (type != null) return type;

      MOperation foundOp = null;
      java.util.Collection operations = MMUtil.SINGLETON.getOperations(classifier);
      Iterator iter = operations.iterator();
      while (iter.hasNext() && foundOp == null){
          MOperation op = (MOperation)iter.next();
          if ( operationMatchesCall(op, name, params) ) {
            foundOp = op;
          }
      }

      if (foundOp == null) { throw new OclTypeException("operation "+name+" not found in classifier "+toString());}

      MParameter rp = MMUtil.SINGLETON.getReturnParameter(foundOp);

      if (rp == null || rp.getType() == null) {
          System.out.println("WARNING: supposing return type void!");
          return new ArgoAny(null);
      }
      MClassifier returnType = rp.getType();

      return getOclRepresentation(returnType);
    }

    public boolean conformsTo(Type type) {
			if (type instanceof ArgoAny)
			{
				ArgoAny other = (ArgoAny) type;
				return equals(type) || MMUtil.SINGLETON.getAllSupertypes(classifier).contains(other.classifier);
			}
			else
			{
				return false;
			}
    }

    public boolean equals(Object o) {
      ArgoAny any = null;
      if (o instanceof ArgoAny) {
        any = (ArgoAny)o;
        return (any.classifier == classifier);
      }
      return false;
    }

    public int hashCode() {
      if (classifier == null) return 0;
      return classifier.hashCode();
    }

    public String toString() {
      if (classifier == null) return "Void";
      return classifier.getName();
    }

    public boolean hasState(String name) {
      System.out.println("ArgoAny.hasState() has been called, but is not implemented yet!");
      return false;
    }

    protected Type getOclRepresentation(MClassifier foundType)
    {
      Type result = null;
      
      if (foundType.getName().equals("int") || foundType.getName().equals("Integer")) {
          result = Basic.INTEGER;
      }

      if (foundType.getName().equals("float") || foundType.getName().equals("double")) {
          result = Basic.REAL;
      }

      if (foundType.getName().equals("bool") || foundType.getName().equals("Boolean") ||
          foundType.getName().equals("boolean")) {
          result = Basic.BOOLEAN;
      }

      if (foundType.getName().equals("String")){
          result = Basic.STRING;
      }

      if (result==null) {
        result=new ArgoAny(foundType);
      }
      
      return result;
    
    }
        
    /**
     *	@return true if the given MOperation names and parameters match the given name 
     *		and parameters
     */
    protected boolean operationMatchesCall(MOperation operation, String callName, Type[] callParams)
    {
      if ( ! callName.equals(operation.getName()) )
      {
        return false;
      }
      List operationParameters = operation.getParameters();
      if (
        ! (((MParameter)operationParameters.get(0)).getKind().getValue()==MParameterDirectionKind._RETURN)
      ) 
      {
        System.err.println(
	  			"ArgoFacade$ArgoAny expects the first operation parameter to be the return type; this isn't the case"
				);
      }
      if (
        ! (
				  ((MParameter)operationParameters.get(0)).getKind().getValue()==MParameterDirectionKind._RETURN
          && operationParameters.size()==(callParams.length+1)
				)
      )
      {
        return false;
      }
      Iterator paramIter = operationParameters.iterator();
      paramIter.next(); // skip first parameter == return type
      int index = 0;
      while (paramIter.hasNext())
      {
			
        MParameter nextParam = (MParameter) paramIter.next();
				MClassifier paramType = nextParam.getType();
				Type operationParam = getOclRepresentation(paramType);
				if ( ! callParams[index].conformsTo(operationParam) )
				{
				  return false;
				}
        index++;
			}
      return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2404.java