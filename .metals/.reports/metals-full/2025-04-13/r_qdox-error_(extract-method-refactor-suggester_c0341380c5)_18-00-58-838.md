error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9855.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9855.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9855.java
text:
```scala
R@@ule r = factory.getRule(token, stack);

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

package org.apache.log4j.rule;

import org.apache.log4j.spi.LoggingEvent;

import java.util.Stack;
import java.util.StringTokenizer;


/**
 * A Rule class supporting both infix and postfix expressions, accepting any rule which
 * is supported by the <code>RuleFactory</code>.
 *
 * NOTE: parsing is supported through the use of <code>StringTokenizer</code>, which
 * implies two limitations:
 * 1: all tokens in the expression must be separated by spaces, including parenthese
 * 2: operands which contain spaces MUST be wrapped in single quotes. 
 *    For example, the expression:
 *      msg == 'some msg'
 *    is a valid expression.
 * 3: To group expressions, use parentheses.
 *    For example, the expression:
 *      level >= INFO || ( msg == 'some msg' || logger == 'test' ) 
 *    is a valid expression.
 * See org.apache.log4j.rule.InFixToPostFix for a description of supported operators.
 * See org.apache.log4j.spi.LoggingEventFieldResolver for field keywords.
 *
 * @author Scott Deboy <sdeboy@apache.org>
 */
public class ExpressionRule extends AbstractRule {
  private static final InFixToPostFix convertor = new InFixToPostFix();
  private static final PostFixExpressionCompiler compiler = new PostFixExpressionCompiler();
  private final Rule rule;

  private ExpressionRule(Rule rule) {
    this.rule = rule;
  }

  public static Rule getRule(String expression) {
      return getRule(expression, false);
  }
  
  public static Rule getRule(String expression, boolean isPostFix) {
    if (!isPostFix) {
      expression = convertor.convert(expression);
    }

    return new ExpressionRule(compiler.compileExpression(expression));
  }

  public boolean evaluate(LoggingEvent event) {
    return rule.evaluate(event);
  }
  
  public String toString() {
      return rule.toString();
  }

  /**
   * Evaluate a boolean postfix expression.
   *
   */
  static class PostFixExpressionCompiler {
    Rule compileExpression(String expression) {
      RuleFactory factory = RuleFactory.getInstance();

      Stack stack = new Stack();
      StringTokenizer tokenizer = new StringTokenizer(expression);

      while (tokenizer.hasMoreTokens()) {
        //examine each token
        String token = tokenizer.nextToken();
        if ((token.startsWith("'")) && (token.endsWith("'") && (token.length() > 2))) {
            token = token.substring(1, token.length() - 1);
        }
        if ((token.startsWith("'")) && (token.endsWith("'") && (token.length() == 2))) {
            token = "";
        }

        boolean inText = token.startsWith("'");
        if (inText) {
            token=token.substring(1);
            while (inText && tokenizer.hasMoreTokens()) {
              token = token + " " + tokenizer.nextToken();
              inText = !(token.endsWith("'"));
          }
          token = token.substring(0, token.length() - 1);
        }

        //if a symbol is found, pop 2 off the stack, evaluate and push the result 
        if (factory.isRule(token)) {
          Rule r = (Rule) factory.getRule(token, stack);
          stack.push(r);
        } else {
          //variables or constants are pushed onto the stack
          stack.push(token);
        }
      }

      if ((stack.size() == 0) || (!(stack.peek() instanceof Rule))) {
        throw new IllegalArgumentException("invalid expression: " + expression);
      } else {
        return (Rule) stack.pop();
      }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9855.java