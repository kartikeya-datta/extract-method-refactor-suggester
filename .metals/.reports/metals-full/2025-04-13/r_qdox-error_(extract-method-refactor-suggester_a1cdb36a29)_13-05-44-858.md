error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10555.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10555.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10555.java
text:
```scala
T@@okenizedPattern(String pattern, String[] tokens) {

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.types.selectors;

import java.io.File;

/**
 * Provides reusable path pattern matching.  PathPattern is preferable
 * to equivalent SelectorUtils methods if you need to execute multiple
 * matching with the same pattern because here the pattern itself will
 * be parsed only once.
 * @see SelectorUtils#matchPath(String, String)
 * @see SelectorUtils#matchPath(String, String, boolean)
 * @since 1.8.0
 */
public class TokenizedPattern {

    /**
     * Instance that holds no tokens at all.
     */
    public static final TokenizedPattern EMPTY_PATTERN =
        new TokenizedPattern("", new String[0]);

    private final String pattern;
    private final String tokenizedPattern[];

    /**
    * Initialize the PathPattern by parsing it. 
    * @param pattern The pattern to match against. Must not be
    *                <code>null</code>.
    */
    public TokenizedPattern(String pattern) {
        this(pattern, SelectorUtils.tokenizePathAsArray(pattern));
    }
    
    private TokenizedPattern(String pattern, String[] tokens) {
        this.pattern = pattern;
        this.tokenizedPattern = tokens;
    }

    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param str     The path to match, as a String. Must not be
     *                <code>null</code>.
     *                
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    public boolean matchPath(String str) {
        return SelectorUtils.matchPath(tokenizedPattern, str, true);
    }
    
    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param str     The path to match, as a String. Must not be
     *                <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     *
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    public boolean matchPath(String str, boolean isCaseSensitive) {
        return SelectorUtils.matchPath(tokenizedPattern, str, isCaseSensitive);
    }
    
    /**
     * Tests whether or not a given path matches a given pattern.
     *
     * @param str     The path to match, as a String. Must not be
     *                <code>null</code>.
     * @param isCaseSensitive Whether or not matching should be performed
     *                        case sensitively.
     *
     * @return <code>true</code> if the pattern matches against the string,
     *         or <code>false</code> otherwise.
     */
    public boolean matchPath(TokenizedPath path, boolean isCaseSensitive) {
        return SelectorUtils.matchPath(tokenizedPattern, path.getTokens(),
                                       isCaseSensitive);
    }
    
    /**
     * Tests whether or not this pattern matches the start of
     * a path.
     */
    public boolean matchStartOf(TokenizedPath path,
                                boolean caseSensitive) {
        return SelectorUtils.matchPatternStart(tokenizedPattern,
                                               path.getTokens(), caseSensitive);
    }

    /**
     * @return The pattern String
     */
    public String toString() {
        return pattern;
    }
    
    public String getPattern() {
        return pattern;
    }

    /**
     * true if the original patterns are equal.
     */
    public boolean equals(Object o) {
        return o instanceof TokenizedPattern
            && pattern.equals(((TokenizedPattern) o).pattern);
    }

    public int hashCode() {
        return pattern.hashCode();
    }

    /**
     * The depth (or length) of a pattern.
     */
    public int depth() {
        return tokenizedPattern.length;
    }

    /**
     * Does the tokenized pattern contain the given string?
     */
    public boolean containsPattern(String pat) {
        for (int i = 0; i < tokenizedPattern.length; i++) {
            if (tokenizedPattern[i].equals(pat)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a new TokenizedPath where all tokens of this pattern to
     * the right containing wildcards have been removed
     * @return the leftmost part of the pattern without wildcards
     */
    public TokenizedPath rtrimWildcardTokens() {
        StringBuffer sb = new StringBuffer();
        int newLen = 0;
        for (; newLen < tokenizedPattern.length; newLen++) {
            if (SelectorUtils.hasWildcards(tokenizedPattern[newLen])) {
                break;
            }
            if (newLen > 0
                && sb.charAt(sb.length() - 1) != File.separatorChar) {
                sb.append(File.separator);
            }
            sb.append(tokenizedPattern[newLen]);
        }
        if (newLen == 0) {
            return TokenizedPath.EMPTY_PATH;
        }
        String[] newPats = new String[newLen];
        System.arraycopy(tokenizedPattern, 0, newPats, 0, newLen);
        return new TokenizedPath(sb.toString(), newPats);
    }

    /**
     * true if the last token equals the given string.
     */
    public boolean endsWith(String s) {
        return tokenizedPattern.length > 0
            && tokenizedPattern[tokenizedPattern.length - 1].equals(s);
    }

    /**
     * Returns a new pattern without the last token of this pattern.
     */
    public TokenizedPattern withoutLastToken() {
        if (tokenizedPattern.length == 0) {
            throw new IllegalStateException("cant strip a token from nothing");
        } else if (tokenizedPattern.length == 1) {
            return EMPTY_PATTERN;
        } else {
            String toStrip = tokenizedPattern[tokenizedPattern.length - 1];
            int index = pattern.lastIndexOf(toStrip);
            String[] tokens = new String[tokenizedPattern.length - 1];
            System.arraycopy(tokenizedPattern, 0, tokens, 0,
                             tokenizedPattern.length - 1);
            return new TokenizedPattern(pattern.substring(0, index), tokens);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10555.java