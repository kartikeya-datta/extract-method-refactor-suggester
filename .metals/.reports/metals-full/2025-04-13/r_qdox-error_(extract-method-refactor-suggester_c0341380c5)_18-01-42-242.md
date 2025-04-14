error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15759.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15759.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15759.java
text:
```scala
_@@delimitReservedWords = delimitReservedWords;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.lib.identifier;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;

/**
 * The standard identifier rule.  Rules are used for specific configuration
 * of identifier types.  For example.  A rule could be used to indicate that
 * an identifier type should not be delimited or has a max length of 255 
 * characters.
 *
 */
public class IdentifierRule {
    
    public static final Set<String> EMPTY_SET = new HashSet<String>(0);
    public static final String DEFAULT_RULE = "default";
    public static char UNDERSCORE = '_';

    private String _name;
    private int _maxLength = 128;
    private boolean _nullable = false;
    private boolean _allowTruncation = false;
    private boolean _allowCompaction = true;
    private boolean _canDelimit = true;
    private boolean _mustDelimit = false;
    private boolean _mustBeginWithLetter = true;
    private boolean _onlyLettersDigitsUnderscores = true;
    private String _specialCharacters = "";
    private Set<String> _reservedWords = null;
    private boolean _delimitReservedWords = false;
    private String _wildcard = "%";

    public void setName(String name) {
        _name = name;
    }
    
    public String getName() {
        return _name;
    }

    public void setMaxLength(int maxLength) {
        _maxLength = maxLength;
    }

    public int getMaxLength() {
        return _maxLength;
    }

    public void setAllowTruncation(boolean allowTruncation) {
        _allowTruncation = allowTruncation;
    }

    public boolean isAllowTruncation() {
        return _allowTruncation;
    }

    public void setNullable(boolean nullable) {
        _nullable = nullable;
    }

    public boolean isNullable() {
        return _nullable;
    }

    public void setAllowCompaction(boolean allowCompaction) {
        _allowCompaction = allowCompaction;
    }

    public boolean getAllowCompaction() {
        return _allowCompaction;
    }

    public void setCanDelimit(boolean canDelimit) {
        _canDelimit = canDelimit;
    }

    public boolean getCanDelimit() {
        return _canDelimit;
    }

    public void setMustDelimit(boolean mustDelimit) {
        _mustDelimit = mustDelimit;
    }

    public boolean getMustDelimit() {
        return _mustDelimit;
    }

    public void setMustBeginWithLetter(boolean mustBeginWithLetter) {
        _mustBeginWithLetter = mustBeginWithLetter;
    }

    public boolean isMustBeginWithLetter() {
        return _mustBeginWithLetter;
    }

    public void setOnlyLettersDigitsUnderscores(boolean onlyLettersDigitsUnderscores) {
        _onlyLettersDigitsUnderscores = onlyLettersDigitsUnderscores;
    }

    public boolean isOnlyLettersDigitsUnderscores() {
        return _onlyLettersDigitsUnderscores;
    }

    public void setReservedWords(Set<String> reservedWords) {
        _reservedWords = reservedWords;
    }

    public Set<String> getReservedWords() {
        if (_reservedWords == null) {
            _reservedWords = new HashSet<String>();
        }
        return _reservedWords;
    }

    public void setSpecialCharacters(String specialCharacters) {
        _specialCharacters = specialCharacters;
    }

    public String getSpecialCharacters() {
        return _specialCharacters;
    }

    public void setDelimitReservedWords(boolean delimitReservedWords) {
        delimitReservedWords = _delimitReservedWords;
    }

    public boolean getDelimitReservedWords() {
        return _delimitReservedWords;
    }

    /**
     * SQL identifier rules:
     * 1) Can be up to 128 characters long
     * 2) Must begin with a letter
     * 3) Can contain letters, digits, and underscores
     * 4) Can't contain spaces or special characters such as #, $, &, %, or 
     *    punctuation.
     * 5) Can't be reserved words
     */
    public boolean requiresDelimiters(String identifier) {

        // Do not delimit single valued wildcards or "?" or names that have method-type
        // signatures (ex. getValue()).  These are considered special values in OpenJPA
        // and should not be delimited.
        if (_wildcard.equals(identifier) || "?".equals(identifier) ||
            identifier.endsWith("()")) {
            return false;
        }
        
        if (getMustDelimit()) {
            return true;
        }
        
        // Assert identifier begins with a letter
        char[] chars = identifier.toCharArray();
        if (isMustBeginWithLetter()) {
            if (!CharUtils.isAsciiAlpha(chars[0])) {
                return true;
            }
        }

        // Iterate through chars, asserting delimiting rules 
        for (char ch : chars) {
            if (isOnlyLettersDigitsUnderscores()) {
                if (!CharUtils.isAsciiAlphanumeric(ch) && !(ch == UNDERSCORE)) {
                    return true;
                }
            }
            // Look for special characters
            if (StringUtils.contains(getSpecialCharacters(), ch)) {
                return true;
            }
        }
        // Finally, look for reserved words
        if (getDelimitReservedWords()) {
            if (isReservedWord(identifier)) {
                return true;
            }
        }
        return false;
    }

    public boolean isReservedWord(String identifier) {
        return _reservedWords.contains(identifier);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15759.java