error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2819.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2819.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[52,1]

error in qdox parser
file content:
```java
offset: 2731
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2819.java
text:
```scala
public class InFixToPostFix {

/*
 * ============================================================================
 *                   The Apache Software License, Version 1.1
 * ============================================================================
 *
 *    Copyright (C) 1999 The Apache Software Foundation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modifica-
 * tion, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of  source code must  retain the above copyright  notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include  the following  acknowledgment:  "This product includes  software
 *    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
 *    Alternately, this  acknowledgment may  appear in the software itself,  if
 *    and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "log4j" and  "Apache Software Foundation"  must not be used to
 *    endorse  or promote  products derived  from this  software without  prior
 *    written permission. For written permission, please contact
 *    apache@apache.org.
 *
 * 5. Products  derived from this software may not  be called "Apache", nor may
 *    "Apache" appear  in their name,  without prior written permission  of the
 *    Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This software  consists of voluntary contributions made  by many individuals
 * on  behalf of the Apache Software  Foundation.  For more  information on the
 * Apache Software Foundation, please see <http://www.apache.org/>.
 *
 */

p@@ackage org.apache.log4j.chainsaw.rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * A helper class which converts infix expressions to postfix expressions
 * Currently grouping is supported, as well as all of the Rules supported by <code>RuleFactory</code>
 * 
 * NOTE: parsing is supported through the use of <code>StringTokenizer</code>, which means 
 * all tokens in the expression must be separated by spaces. 
 * 
 * @author Scott Deboy <sdeboy@apache.org>
 */

class InFixToPostFix {
  private final Map precedenceMap = new HashMap();
  private final List operators = new Vector();

  public InFixToPostFix() {
    //boolean operators
    operators.add("!");
    operators.add("!=");
    operators.add("==");
    operators.add("~=");
    operators.add("||");
    operators.add("&&");
    operators.add("like");
    operators.add("<");
    operators.add(">");
    operators.add("<=");
    operators.add(">=");
    
    operators.add("*");
    operators.add("+");
    operators.add("-");

    //boolean precedence
    precedenceMap.put("<", new Integer(3));
    precedenceMap.put(">", new Integer(3));
    precedenceMap.put("<=", new Integer(3));
    precedenceMap.put(">=", new Integer(3));
    
    precedenceMap.put("!", new Integer(3));
    precedenceMap.put("!=", new Integer(3));
    precedenceMap.put("==", new Integer(3));
    precedenceMap.put("~=", new Integer(3));
    precedenceMap.put("like", new Integer(3));

    precedenceMap.put("||", new Integer(2));
    precedenceMap.put("&&", new Integer(2));

    precedenceMap.put("-", new Integer(2));
    precedenceMap.put("+", new Integer(2));
    precedenceMap.put("*", new Integer(3));
  }

  public String convert(String expression) {
    return infixToPostFix(new StringTokenizer(expression));
  }

  boolean isOperand(String symbol) {
    symbol = symbol.toLowerCase();

    return (!operators.contains(symbol));
  }

  boolean precedes(String symbol1, String symbol2) {
    symbol1 = symbol1.toLowerCase();
    symbol2 = symbol2.toLowerCase();

    if (!precedenceMap.keySet().contains(symbol1)) {
      return false;
    }

    if (!precedenceMap.keySet().contains(symbol2)) {
      return false;
    }

    int index1 = ((Integer) precedenceMap.get(symbol1)).intValue();
    int index2 = ((Integer) precedenceMap.get(symbol2)).intValue();

    boolean precedesResult = (index1 < index2);

    return precedesResult;
  }

  String infixToPostFix(StringTokenizer tokenizer) {
    String SPACE = " ";
    StringBuffer postfix = new StringBuffer();

    Stack stack = new Stack();

    while (tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken();

      if ("(".equals(token)) {
        //recurse
        postfix.append(infixToPostFix(tokenizer));
        postfix.append(SPACE);
      } else if (")".equals(token)) {
        //exit recursion level
        while (stack.size() > 0) {
          postfix.append(stack.pop().toString());
          postfix.append(SPACE);
        }

        return postfix.toString();
      } else if (isOperand(token)) {
        postfix.append(token);
        postfix.append(SPACE);
      } else {
        //operator..
        //peek the stack..if the top element has a lower precedence than token
        //(peeked + has lower precedence than token *), push token onto the stack
        //otherwise, pop top element off stack and add to postfix string
        //in a loop until lower precedence or empty..then push token
        if (stack.size() > 0) {

          String peek = stack.peek().toString();

          if (precedes(peek, token)) {
            stack.push(token);
          } else {
            boolean bypass = false;

            do {
              if (
                (stack.size() > 0)
                  && !precedes(stack.peek().toString(), token)) {
                postfix.append(stack.pop().toString());
                postfix.append(SPACE);
              } else {
                bypass = true;
              }
            } while (!bypass);

            stack.push(token);
          }
        } else {
          stack.push(token);
        }
      }
    }

    while (stack.size() > 0) {
      postfix.append(stack.pop().toString());
      postfix.append(SPACE);
    }

    return postfix.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2819.java