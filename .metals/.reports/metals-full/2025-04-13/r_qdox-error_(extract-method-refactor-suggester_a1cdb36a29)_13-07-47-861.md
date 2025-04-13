error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1408.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1408.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1408.java
text:
```scala
V@@alueRestriction restriction = new ValueRestriction(new String[] { pwd });

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.domain.management.security.password.simple;

import static org.jboss.as.domain.management.DomainManagementMessages.MESSAGES;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.jboss.as.domain.management.security.password.Dictionary;
import org.jboss.as.domain.management.security.password.Keyboard;
import org.jboss.as.domain.management.security.password.LengthRestriction;
import org.jboss.as.domain.management.security.password.PasswordCheckUtil;
import org.jboss.as.domain.management.security.password.PasswordRestriction;
import org.jboss.as.domain.management.security.password.PasswordStrengthCheckResult;
import org.jboss.as.domain.management.security.password.ValueRestriction;

/**
 * @author baranowb
 */
public class SimplePasswordStrengthCheckerTestCase {

    private Keyboard keyboard = new SimpleKeyboard();
    private Dictionary dictionary = new SimpleDictionary();

    public static final PasswordCheckUtil PCU = PasswordCheckUtil.create(null);
    public static final PasswordRestriction ALPHA_RESTRICTION = PCU.createAlphaRestriction(1);
    public static final PasswordRestriction SYMBOL_RESTRICTION = PCU.createSymbolRestriction(1);
    public static final PasswordRestriction DIGIT_RESTRICTION = PCU.createDigitRestriction(1);
    public static final LengthRestriction LENGTH_RESTRICTION = new LengthRestriction(8);

    @Test
    public void testLengthRestriction() {
        List<PasswordRestriction> restrictions = new ArrayList<PasswordRestriction>();
        //one that will fail
        restrictions.add(LENGTH_RESTRICTION);
        //one that will pass
        restrictions.add(SYMBOL_RESTRICTION);
        SimplePasswordStrengthChecker checker = new SimplePasswordStrengthChecker(restrictions, this.dictionary, this.keyboard);
        String pwd = "1W2sa#4";
        PasswordStrengthCheckResult result = checker.check("", pwd, null);
        assertNotNull(result);
        assertNotNull(result.getPassedRestrictions());
        assertNotNull(result.getRestrictionFailures());

        assertEquals(1, result.getPassedRestrictions().size());
        assertEquals(1, result.getRestrictionFailures().size());

        assertNotNull(result.getStrength());

        assertEquals(MESSAGES.passwordNotLongEnough(8).getMessage(), result.getRestrictionFailures().get(0).getMessage());
        assertEquals(SYMBOL_RESTRICTION, result.getPassedRestrictions().get(0));
    }

    @Test
    public void testDigitsRestriction() {
        List<PasswordRestriction> restrictions = new ArrayList<PasswordRestriction>();
        //one that will fail
        restrictions.add(DIGIT_RESTRICTION);
        //one that will pass
        restrictions.add(ALPHA_RESTRICTION);
        SimplePasswordStrengthChecker checker = new SimplePasswordStrengthChecker(restrictions, this.dictionary, this.keyboard);
        String pwd = "DW$sa#x";
        PasswordStrengthCheckResult result = checker.check("", pwd, null);
        assertNotNull(result);
        assertNotNull(result.getPassedRestrictions());
        assertNotNull(result.getRestrictionFailures());

        assertEquals(1, result.getPassedRestrictions().size());
        assertEquals(1, result.getRestrictionFailures().size());

        assertNotNull(result.getStrength());

        assertEquals(MESSAGES.passwordMustHaveDigit(1), result.getRestrictionFailures().get(0).getMessage());
        assertEquals(ALPHA_RESTRICTION, result.getPassedRestrictions().get(0));
    }

    @Test
    public void testSymbolsRestriction() {
        List<PasswordRestriction> restrictions = new ArrayList<PasswordRestriction>();
        //one that will fail
        restrictions.add(SYMBOL_RESTRICTION);
        //one that will pass
        restrictions.add(ALPHA_RESTRICTION);
        SimplePasswordStrengthChecker checker = new SimplePasswordStrengthChecker(restrictions, this.dictionary, this.keyboard);
        String pwd = "DW5sa3x";
        PasswordStrengthCheckResult result = checker.check("", pwd, null);
        assertNotNull(result);
        assertNotNull(result.getPassedRestrictions());
        assertNotNull(result.getRestrictionFailures());

        assertEquals(1, result.getPassedRestrictions().size());
        assertEquals(1, result.getRestrictionFailures().size());

        assertNotNull(result.getStrength());

        assertEquals(MESSAGES.passwordMustHaveSymbol(1), result.getRestrictionFailures().get(0).getMessage());
        assertEquals(ALPHA_RESTRICTION, result.getPassedRestrictions().get(0));
    }

    @Test
    public void testAlphaRestriction() {
        List<PasswordRestriction> restrictions = new ArrayList<PasswordRestriction>();
        //one that will fail
        restrictions.add(ALPHA_RESTRICTION);
        //one that will pass
        restrictions.add(SYMBOL_RESTRICTION);
        SimplePasswordStrengthChecker checker = new SimplePasswordStrengthChecker(restrictions, this.dictionary, this.keyboard);
        String pwd = "!#*_33";
        PasswordStrengthCheckResult result = checker.check("", pwd, null);
        assertNotNull(result);
        assertNotNull(result.getPassedRestrictions());
        assertNotNull(result.getRestrictionFailures());

        assertEquals(1, result.getPassedRestrictions().size());
        assertEquals(1, result.getRestrictionFailures().size());

        assertNotNull(result.getStrength());

        assertEquals(MESSAGES.passwordMustHaveAlpha(1), result.getRestrictionFailures().get(0).getMessage());
        assertEquals(SYMBOL_RESTRICTION, result.getPassedRestrictions().get(0));
    }

    @Test
    public void testAdHocRestriction() {
        List<PasswordRestriction> restrictions = new ArrayList<PasswordRestriction>();
        restrictions.add(ALPHA_RESTRICTION);
        restrictions.add(SYMBOL_RESTRICTION);
        SimplePasswordStrengthChecker checker = new SimplePasswordStrengthChecker(restrictions, this.dictionary, this.keyboard);
        String pwd = "!#*_3x";
        List<PasswordRestriction> adHocRestrictions = new ArrayList<PasswordRestriction>();
        ValueRestriction restriction = new ValueRestriction(new String[] { pwd }, true);
        adHocRestrictions.add(restriction);

        PasswordStrengthCheckResult result = checker.check("", pwd, adHocRestrictions);
        assertNotNull(result);
        assertNotNull(result.getPassedRestrictions());
        assertNotNull(result.getRestrictionFailures());

        assertEquals(2, result.getPassedRestrictions().size());
        assertEquals(1, result.getRestrictionFailures().size());

        assertNotNull(result.getStrength());

        assertEquals(ALPHA_RESTRICTION, result.getPassedRestrictions().get(0));
        assertEquals(SYMBOL_RESTRICTION, result.getPassedRestrictions().get(1));
        assertEquals(MESSAGES.passwordMustNotBeEqual(pwd).getMessage(), result.getRestrictionFailures().get(0).getMessage());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1408.java