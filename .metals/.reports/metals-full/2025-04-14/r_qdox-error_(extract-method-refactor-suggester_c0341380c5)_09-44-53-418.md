error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2096.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2096.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2096.java
text:
```scala
public static final S@@tring ANY_RANGE = "0..*";

package uci.uml.generate;

import java.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Core.Class;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;

// needs-more-work: always check for null!!!

public class GeneratorDisplay extends Generator {

  public static GeneratorDisplay SINGLETON = new GeneratorDisplay();

  public static String Generate(Object o) {
    return SINGLETON.generate(o);
  }
  
  public String generateOperation(Operation op) {
    String s = "";
    s += generateVisibility(op);
    s += generateScope(op);

    // pick out return type
    Classifier returnType = null;
    Vector params = op.getParameter();
    if (params != null) {
      java.util.Enumeration enum = params.elements();
      while (enum.hasMoreElements()) {
	Parameter p = (Parameter) enum.nextElement();
	if (p.getName() == Parameter.RETURN_NAME) {
	  returnType = p.getType();
	  break;
	}
      }
    }
    if (returnType == null) s += "void?? ";
    else s += generateClassifierRef(returnType) + " ";

    
    // name and params
    s += generateName(op.getName()) + "(";
    params = op.getParameter();
    if (params != null) {
      java.util.Enumeration enum = params.elements();
      boolean first = true;
      while (enum.hasMoreElements()) {
	Parameter p = (Parameter) enum.nextElement();
	if (p.getName() == Parameter.RETURN_NAME) continue;
	if (!first) s += ", ";
	s += generateParameter(p);
	first = false;
      }
    }
    s += ")";
    return s;
  }

  public String generateAttribute(Attribute attr) {
    String s = "";
    s += generateVisibility(attr);
    s += generateScope(attr);
    s += generateChangability(attr);
    if (attr.getMultiplicity() != Multiplicity.ONE)
      s += generateMultiplicity(attr.getMultiplicity()) + " ";

    Classifier type = attr.getType();
    if (type != null) s += generateClassifierRef(type) + " ";

    String slash = "";
    if (attr.containsStereotype(Stereotype.DERIVED)) slash = "/";
    
    s += slash + generateName(attr.getName());
    Expression init = attr.getInitialValue();
    if (init != null)
      s += " = " + generateExpression(init);

    String constraintStr = generateConstraints(attr);
    if (constraintStr.length() > 0)
      s += " " + constraintStr;
    
    return s;
  }


  public String generateParameter(Parameter param) {
    String s = "";
    //needs-more-work: qualifiers (e.g., const)
    //needs-more-work: stereotypes...
    s += generateClassifierRef(param.getType()) + " ";
    s += generateName(param.getName());
    //needs-more-work: initial value
    return s;
  }


  public String generatePackage(Package p) {
    String s = "";
    String packName = generateName(p.getName());
    s += "package " + packName + " {\n";
    Vector ownedElements = p.getOwnedElement();
    if (ownedElements != null) {
      java.util.Enumeration ownedEnum = ownedElements.elements();
      while (ownedEnum.hasMoreElements()) {
	ElementOwnership eo = (ElementOwnership) ownedEnum.nextElement();
	s += generate(eo.getModelElement());
	s += "\n\n";
      }
    }
    else {
      s += "(no elements)";
    }
    s += "\n}\n";
    return s;
  }


  public String generateClassifier(Classifier cls) {
    String generatedName = generateName(cls.getName());
    String classifierKeyword;
    if (cls instanceof Class) classifierKeyword = "class";
    else { if (cls instanceof Interface) classifierKeyword = "interface";
    else classifierKeyword = "class?"; }
    String s = "";
    s += generateVisibility(cls.getElementOwnership());
    if (cls.getIsAbstract().booleanValue()) s += "abstract ";
    if (cls.getIsLeaf().booleanValue()) s += "final ";
    s += classifierKeyword + " " + generatedName + " ";
    String baseClass = generateGeneralzation(cls.getGeneralization());
    if (!baseClass.equals("")) s += "extends " + baseClass + " ";
    String interfaces = generateRealization(cls.getRealization());
    if (!interfaces.equals("")) s += "implements " + interfaces + " ";
    s += "{\n";

    Vector strs = cls.getStructuralFeature();
    if (strs != null) {
      s += "\n";
      s += "////////////////////////////////////////////////////////////////\n";
      s += "// Attributes\n";
      java.util.Enumeration strEnum = strs.elements();
      while (strEnum.hasMoreElements())
	s += generate(strEnum.nextElement()) + ";\n";
    }

    Vector ends = cls.getAssociationEnd();
    if (ends != null) {
      s += "\n";
      s += "////////////////////////////////////////////////////////////////\n";
      s += "// Associations\n";
      java.util.Enumeration endEnum = ends.elements();
      while (endEnum.hasMoreElements()) {
	AssociationEnd ae = (AssociationEnd) endEnum.nextElement();
	IAssociation a = ae.getAssociation();
	s += generateAssociationFrom(a, ae) + ";\n";
      }
    }

    // needs-more-work: constructors
    
    Vector behs = cls.getBehavioralFeature();
    if (behs != null) {
      s += "\n";
      s += "////////////////////////////////////////////////////////////////\n";
      s += "// Operations\n";
      java.util.Enumeration behEnum = behs.elements();
      while (behEnum.hasMoreElements())
	s += generate(behEnum.nextElement()) + ";\n";
    }
    
    s += "} /* end " + classifierKeyword + " " + generatedName + " */\n";
    return s;
  }

  public String generateStereotype(Stereotype s) {
    return "<<" + generateName(s.getName()) + ">>";
  }

  public String generateTaggedValue(TaggedValue tv) {
    if (tv == null) return "";
    return generateName(tv.getTag()) + "=" +
      generateUninterpreted(tv.getValue());
  }


  public String generateAssociationFrom(IAssociation a, AssociationEnd ae) {
    // needs-more-work: does not handle n-ary associations
    Vector connections = a.getConnection();
    java.util.Enumeration connEnum = connections.elements();
    while (connEnum.hasMoreElements()) {
      AssociationEnd ae2 = (AssociationEnd) connEnum.nextElement();
      if (ae2 != ae) return generateAssociationEnd(ae2);
    }
    System.out.println("should never get here?");
    return generateAssociation(a);
  }

  public String generateAssociation(IAssociation a) {
    String s = "";
    String generatedName = generateName(a.getName());
    s += "Association " + generatedName + " {\n";

    java.util.Enumeration endEnum = a.getConnection().elements();
    while (endEnum.hasMoreElements()) {
      AssociationEnd ae = (AssociationEnd)endEnum.nextElement();
      s += generateAssociationEnd(ae);
      s += ";\n";
    }
    s += "}\n";
    return s;
  }

  public String generateAssociationEnd(AssociationEnd ae) {
    String s = "";
    Name n = ae.getName();
    if (n != null && n != Name.UNSPEC) s += generateName(n) + " ";
    if (ae.getIsNavigable()) s += "navigable ";
    if (ae.getIsOrdered()) s += "ordered ";
    Multiplicity m = ae.getMultiplicity();
    if (m != Multiplicity.ONE)
      s+= generateMultiplicity(m) + " ";
    s += generateClassifierRef(ae.getType());
    return s;
  }

  public String generateConstraints(ModelElement me) {
    Vector constr = me.getConstraint();
    if (constr == null || constr.size() == 0) return "";
    String s = "{";
    java.util.Enumeration conEnum = constr.elements();
    while (conEnum.hasMoreElements()) {
      s += generateConstraint((Constraint)conEnum.nextElement());
      if (conEnum.hasMoreElements()) s += "; ";
    }
    s += "}";
    return s;
  }


  public String generateConstraint(Constraint c) {
    return generateExpression(c.getBody());
  }
  
  ////////////////////////////////////////////////////////////////
  // internal methods?
  

  public String generateGeneralzation(Vector generalizations) {
    if (generalizations == null) return "";
    Vector classes = new Vector(generalizations.size());
    java.util.Enumeration enum = generalizations.elements();
    while (enum.hasMoreElements()) {
      Generalization g = (Generalization) enum.nextElement();
      GeneralizableElement ge = g.getSupertype();
      // assert ge != null
      if (ge != null) classes.addElement(ge);
    }
    return generateClassList(classes);
  }
  public String generateRealization(Vector classifiers) {
    // Realization is much simplier than Generalization.
    // There is no Realization class in UML metamodel
    return generateClassList(classifiers);
  }

  public String generateClassList(Vector classifiers) {
    String s = "";
    if (classifiers == null) return "";
    java.util.Enumeration clsEnum = classifiers.elements();
    while (clsEnum.hasMoreElements()) {
      s += generateClassifierRef((Classifier)clsEnum.nextElement());
      if (clsEnum.hasMoreElements()) s += ", ";
    }
    return s;
  }
       
  public String generateVisibility(ElementOwnership eo) {
    if (eo == null) return "";
    VisibilityKind vis = eo.getVisibility();
    if (vis == null) return "";
    if (vis == VisibilityKind.PUBLIC) return "public ";
    if (vis == VisibilityKind.PRIVATE) return "private ";
    if (vis == VisibilityKind.PROTECTED) return "protected ";
    return "";
  }

  public String generateVisibility(Feature f) {
    VisibilityKind vis = f.getVisibility();
    if (vis == null) return "";
    if (vis == VisibilityKind.PUBLIC) return "public ";
    if (vis == VisibilityKind.PRIVATE) return "private ";
    if (vis == VisibilityKind.PROTECTED) return "protected ";
    return "";
  }

  public String generateScope(Feature f) {
    ScopeKind scope = f.getOwnerScope();
    if (scope == null) return "";
    if (scope == ScopeKind.CLASSIFIER) return "static ";
    return "";
  }

  public String generateChangability(StructuralFeature sf) {
    ChangeableKind ck = sf.getChangeable();
    if (ck == null) return "";
    if (ck == ChangeableKind.FROZEN) return "final ";
    //if (ck == ChangeableKind.ADDONLY) return "final ";
    return "";
  }

  public String generateMultiplicity(Multiplicity m) {
    if (m == null) { System.out.println("null Multiplicity"); return ""; }
    if (m == Multiplicity.ZERO_OR_MORE) return ANY_RANGE;
    String s = "";
    Vector v = m.getRange();
    if (v == null) return s;
    java.util.Enumeration rangeEnum = v.elements();
    while (rangeEnum.hasMoreElements()) {
      MultiplicityRange mr = (MultiplicityRange) rangeEnum.nextElement();
      s += generateMultiplicityRange(mr);
      if (rangeEnum.hasMoreElements()) s += ",";
    }
    return s;
  }


  public static final String ANY_RANGE = "*..0";
  //public static final String ANY_RANGE = "*";
  // needs-more-work: user preference between "*" and "0..*"
  
  public String generateMultiplicityRange(MultiplicityRange mr) {
    
    Integer lower = mr.getLower();
    Integer upper = mr.getUpper();
    if (lower == null && upper == null) return ANY_RANGE;
    if (lower == null) return "*.."+ upper.toString();
    if (upper == null) return lower.toString() + "..*";
    if (lower.intValue() == upper.intValue()) return lower.toString();
    return mr.getLower().toString() + ".." + mr.getUpper().toString();
  } 

} /* end class GeneratorDisplay */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2096.java