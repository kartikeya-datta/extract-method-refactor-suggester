error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3479.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3479.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3479.java
text:
```scala
a@@llTokens.put(r.right, allTokens.get(r.right) + StorageService.instance.getSplits(ks, cfmd.cfName, r, DatabaseDescriptor.getIndexInterval()).size());

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.dht;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.*;

import org.apache.cassandra.config.CFMetaData;
import org.apache.cassandra.config.DatabaseDescriptor;
import org.apache.cassandra.db.DecoratedKey;
import org.apache.cassandra.service.StorageService;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.cassandra.utils.FBUtilities;
import org.apache.cassandra.utils.Pair;

public class OrderPreservingPartitioner implements IPartitioner<StringToken>
{
    public static final StringToken MINIMUM = new StringToken("");

    public static final BigInteger CHAR_MASK = new BigInteger("65535");

    public DecoratedKey<StringToken> decorateKey(ByteBuffer key)
    {
        return new DecoratedKey<StringToken>(getToken(key), key);
    }
    
    public DecoratedKey<StringToken> convertFromDiskFormat(ByteBuffer key)
    {
        return new DecoratedKey<StringToken>(getToken(key), key);
    }

    public StringToken midpoint(Token ltoken, Token rtoken)
    {
        int sigchars = Math.max(((StringToken)ltoken).token.length(), ((StringToken)rtoken).token.length());
        BigInteger left = bigForString(((StringToken)ltoken).token, sigchars);
        BigInteger right = bigForString(((StringToken)rtoken).token, sigchars);

        Pair<BigInteger,Boolean> midpair = FBUtilities.midpoint(left, right, 16*sigchars);
        return new StringToken(stringForBig(midpair.left, sigchars, midpair.right));
    }

    /**
     * Copies the characters of the given string into a BigInteger.
     *
     * TODO: Does not acknowledge any codepoints above 0xFFFF... problem?
     */
    private static BigInteger bigForString(String str, int sigchars)
    {
        assert str.length() <= sigchars;

        BigInteger big = BigInteger.ZERO;
        for (int i = 0; i < str.length(); i++)
        {
            int charpos = 16 * (sigchars - (i + 1));
            BigInteger charbig = BigInteger.valueOf(str.charAt(i) & 0xFFFF);
            big = big.or(charbig.shiftLeft(charpos));
        }
        return big;
    }

    /**
     * Convert a (positive) BigInteger into a String.
     * If remainder is true, an additional char with the high order bit enabled
     * will be added to the end of the String.
     */
    private String stringForBig(BigInteger big, int sigchars, boolean remainder)
    {
        char[] chars = new char[sigchars + (remainder ? 1 : 0)];
        if (remainder)
            // remaining bit is the most significant in the last char
            chars[sigchars] |= 0x8000;
        for (int i = 0; i < sigchars; i++)
        {
            int maskpos = 16 * (sigchars - (i + 1));
            // apply bitmask and get char value
            chars[i] = (char)(big.and(CHAR_MASK.shiftLeft(maskpos)).shiftRight(maskpos).intValue() & 0xFFFF);
        }
        return new String(chars);
    }

    public StringToken getMinimumToken()
    {
        return MINIMUM;
    }

    public StringToken getRandomToken()
    {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random r = new Random();
        StringBuilder buffer = new StringBuilder();
        for (int j = 0; j < 16; j++) {
            buffer.append(chars.charAt(r.nextInt(chars.length())));
        }
        return new StringToken(buffer.toString());
    }

    private final Token.TokenFactory<String> tokenFactory = new Token.TokenFactory<String>()
    {
        public ByteBuffer toByteArray(Token<String> stringToken)
        {
            return ByteBufferUtil.bytes(stringToken.token);
        }

        public Token<String> fromByteArray(ByteBuffer bytes)
        {
            try
            {
                return new StringToken(ByteBufferUtil.string(bytes));
            }
            catch (CharacterCodingException e)
            {
                throw new RuntimeException(e);
            }
        }

        public String toString(Token<String> stringToken)
        {
            return stringToken.token;
        }

        public Token<String> fromString(String string)
        {
            return new StringToken(string);
        }
    };

    public Token.TokenFactory<String> getTokenFactory()
    {
        return tokenFactory;
    }

    public boolean preservesOrder()
    {
        return true;
    }

    public StringToken getToken(ByteBuffer key)
    {
        String skey;
        try
        {
            skey = ByteBufferUtil.string(key);
        }
        catch (CharacterCodingException e)
        {
            throw new RuntimeException("The provided key was not UTF8 encoded.", e);
        }
        return new StringToken(skey);
    }

    public Map<Token, Float> describeOwnership(List<Token> sortedTokens)
    {
        // allTokens will contain the count and be returned, sorted_ranges is shorthand for token<->token math.
        Map<Token, Float> allTokens = new HashMap<Token, Float>();
        List<Range> sortedRanges = new ArrayList<Range>();

        // this initializes the counts to 0 and calcs the ranges in order.
        Token lastToken = sortedTokens.get(sortedTokens.size() - 1);
        for (Token node : sortedTokens)
        {
            allTokens.put(node, new Float(0.0));
            sortedRanges.add(new Range(lastToken, node));
            lastToken = node;
        }

        for (String ks : DatabaseDescriptor.getTables())
        {
            for (CFMetaData cfmd : DatabaseDescriptor.getKSMetaData(ks).cfMetaData().values())
            {
                for (Range r : sortedRanges)
                {
                    // Looping over every KS:CF:Range, get the splits size and add it to the count
                    allTokens.put(r.right, allTokens.get(r.right) + StorageService.instance.getSplits(ks, cfmd.cfName, r, 1).size());
                }
            }
        }

        // Sum every count up and divide count/total for the fractional ownership.
        Float total = new Float(0.0);
        for (Float f : allTokens.values())
            total += f;
        for (Map.Entry<Token, Float> row : allTokens.entrySet())
            allTokens.put(row.getKey(), row.getValue() / total);

        return allTokens;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3479.java