error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6442.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6442.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6442.java
text:
```scala
i@@f (version.onOrAfter(Version.LUCENE_4_8)) {

/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.Lucene47WordDelimiterFilter;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilter;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterIterator;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.lucene.analysis.miscellaneous.WordDelimiterFilter.*;

public class WordDelimiterTokenFilterFactory extends AbstractTokenFilterFactory {

    private final byte[] charTypeTable;
    private final int flags;
    private final CharArraySet protoWords;

    @Inject
    public WordDelimiterTokenFilterFactory(Index index, @IndexSettings Settings indexSettings, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings, name, settings);

        // Sample Format for the type table:
        // $ => DIGIT
        // % => DIGIT
        // . => DIGIT
        // \u002C => DIGIT
        // \u200D => ALPHANUM
        List<String> charTypeTableValues = Analysis.getWordList(env, settings, "type_table");
        if (charTypeTableValues == null) {
            this.charTypeTable = WordDelimiterIterator.DEFAULT_WORD_DELIM_TABLE;
        } else {
            this.charTypeTable = parseTypes(charTypeTableValues);
        }
        int flags = 0;
        // If set, causes parts of words to be generated: "PowerShot" => "Power" "Shot"
        flags |= getFlag(GENERATE_WORD_PARTS, settings, "generate_word_parts", true);
        // If set, causes number subwords to be generated: "500-42" => "500" "42"
        flags |= getFlag(GENERATE_NUMBER_PARTS, settings, "generate_number_parts", true);
        // 1, causes maximum runs of word parts to be catenated: "wi-fi" => "wifi"
        flags |= getFlag(CATENATE_WORDS, settings, "catenate_words", false);
        // If set, causes maximum runs of number parts to be catenated: "500-42" => "50042"
        flags |= getFlag(CATENATE_NUMBERS, settings, "catenate_numbers", false);
        // If set, causes all subword parts to be catenated: "wi-fi-4000" => "wifi4000"
        flags |= getFlag(CATENATE_ALL, settings, "catenate_all", false);
        // 1, causes "PowerShot" to be two tokens; ("Power-Shot" remains two parts regards)
        flags |= getFlag(SPLIT_ON_CASE_CHANGE, settings, "split_on_case_change", true);
        // If set, includes original words in subwords: "500-42" => "500" "42" "500-42"
        flags |= getFlag(PRESERVE_ORIGINAL, settings, "preserve_original", false);
        // 1, causes "j2se" to be three tokens; "j" "2" "se"
        flags |= getFlag(SPLIT_ON_NUMERICS, settings, "split_on_numerics", true);
        // If set, causes trailing "'s" to be removed for each subword: "O'Neil's" => "O", "Neil"
        flags |= getFlag(STEM_ENGLISH_POSSESSIVE, settings, "stem_english_possessive", true);
        // If not null is the set of tokens to protect from being delimited
        Set<?> protectedWords = Analysis.getWordSet(env, settings, "protected_words", version);
        this.protoWords = protectedWords == null ? null : CharArraySet.copy(Lucene.VERSION, protectedWords);
        this.flags = flags;
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
         if (version.onOrAfter(Version.LUCENE_48)) {
             return new WordDelimiterFilter(version, tokenStream,
                     charTypeTable,
                     flags,
                     protoWords);
         } else {
             return new Lucene47WordDelimiterFilter(tokenStream,
                     charTypeTable,
                     flags,
                     protoWords);
         }
    }

    public int getFlag(int flag, Settings settings, String key, boolean defaultValue) {
        if (settings.getAsBoolean(key, defaultValue)) {
            return flag;
        }
        return 0;
    }

    // source => type
    private static Pattern typePattern = Pattern.compile("(.*)\\s*=>\\s*(.*)\\s*$");

    /**
     * parses a list of MappingCharFilter style rules into a custom byte[] type table
     */
    private byte[] parseTypes(Collection<String> rules) {
        SortedMap<Character, Byte> typeMap = new TreeMap<>();
        for (String rule : rules) {
            Matcher m = typePattern.matcher(rule);
            if (!m.find())
                throw new RuntimeException("Invalid Mapping Rule : [" + rule + "]");
            String lhs = parseString(m.group(1).trim());
            Byte rhs = parseType(m.group(2).trim());
            if (lhs.length() != 1)
                throw new RuntimeException("Invalid Mapping Rule : [" + rule + "]. Only a single character is allowed.");
            if (rhs == null)
                throw new RuntimeException("Invalid Mapping Rule : [" + rule + "]. Illegal type.");
            typeMap.put(lhs.charAt(0), rhs);
        }

        // ensure the table is always at least as big as DEFAULT_WORD_DELIM_TABLE for performance
        byte types[] = new byte[Math.max(typeMap.lastKey() + 1, WordDelimiterIterator.DEFAULT_WORD_DELIM_TABLE.length)];
        for (int i = 0; i < types.length; i++)
            types[i] = WordDelimiterIterator.getType(i);
        for (Map.Entry<Character, Byte> mapping : typeMap.entrySet())
            types[mapping.getKey()] = mapping.getValue();
        return types;
    }

    private Byte parseType(String s) {
        if (s.equals("LOWER"))
            return WordDelimiterFilter.LOWER;
        else if (s.equals("UPPER"))
            return WordDelimiterFilter.UPPER;
        else if (s.equals("ALPHA"))
            return WordDelimiterFilter.ALPHA;
        else if (s.equals("DIGIT"))
            return WordDelimiterFilter.DIGIT;
        else if (s.equals("ALPHANUM"))
            return WordDelimiterFilter.ALPHANUM;
        else if (s.equals("SUBWORD_DELIM"))
            return WordDelimiterFilter.SUBWORD_DELIM;
        else
            return null;
    }

    char[] out = new char[256];

    private String parseString(String s) {
        int readPos = 0;
        int len = s.length();
        int writePos = 0;
        while (readPos < len) {
            char c = s.charAt(readPos++);
            if (c == '\\') {
                if (readPos >= len)
                    throw new RuntimeException("Invalid escaped char in [" + s + "]");
                c = s.charAt(readPos++);
                switch (c) {
                    case '\\':
                        c = '\\';
                        break;
                    case 'n':
                        c = '\n';
                        break;
                    case 't':
                        c = '\t';
                        break;
                    case 'r':
                        c = '\r';
                        break;
                    case 'b':
                        c = '\b';
                        break;
                    case 'f':
                        c = '\f';
                        break;
                    case 'u':
                        if (readPos + 3 >= len)
                            throw new RuntimeException("Invalid escaped char in [" + s + "]");
                        c = (char) Integer.parseInt(s.substring(readPos, readPos + 4), 16);
                        readPos += 4;
                        break;
                }
            }
            out[writePos++] = c;
        }
        return new String(out, 0, writePos);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/6442.java