error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11380.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11380.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11380.java
text:
```scala
L@@angs.put(s, loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(s)));

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.codec.language.bm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * <p>
 * Language guessing utility.
 * </p>
 * <p>
 * This class encapsulates rules used to guess the possible languages that a word originates from. This is done by reference to a whole
 * series of rules distributed in resource files.
 * </p>
 * <p>
 * Instances of this class are typically managed through the static factory method instance(). Unless you are developing your own language
 * guessing rules, you will not need to interact with this class directly.
 * </p>
 * <p>
 * This class is intended to be immutable and thread-safe.
 * </p>
 * <h2>Lang resources</h2
 * <p>
 * Language guessing rules are typically loaded from resource files. These are UTF-8 encoded text files. They are systematically named
 * following the pattern: <blockquote>org/apache/commons/codec/language/bm/lang.txt</blockquote> The format of these resources is the
 * following:
 * </p>
 * <ul>
 * <li><b>Rules:</b> whitespace separated strings. There should be 3 columns to each row, and these will be interpreted as:
 * <ol>
 * <li>pattern: a regular expression.</li>
 * <li>languages: a '+'-separated list of languages.</li>
 * <li>acceptOnMatch: 'true' or 'false' indicating if a match rules in or rules out the language.</li>
 * </ol>
 * </li>
 * <li><b>End-of-line comments:</b> Any occurance of '//' will cause all text following on that line to be discarded as a comment.</li>
 * <li><b>Multi-line comments:</b> Any line starting with '/*' will start multi-line commenting mode. This will skip all content until a
 * line ending in '*' and '/' is found.</li>
 * <li><b>Blank lines:</b> All blank lines will be skipped.</li>
 * </ul>
 * <p/>
 * Port of lang.php
 * 
 * @author Apache Software Foundation
 * @since 2.0
 */
public class Lang {

    private static class LangRule {
        private final boolean acceptOnMatch;
        private final Set<String> languages;
        private final Pattern pattern;

        private LangRule(Pattern pattern, Set<String> languages, boolean acceptOnMatch) {
            this.pattern = pattern;
            this.languages = languages;
            this.acceptOnMatch = acceptOnMatch;
        }

        public boolean matches(String txt) {
            return this.pattern.matcher(txt).find();
        }
    }

    private static final Map<NameType, Lang> Langs = new EnumMap<NameType, Lang>(NameType.class);

    private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";

    static {
        for (NameType s : NameType.values()) {
            Langs.put(s, loadFromResource(LANGUAGE_RULES_RN, Languages.instance(s)));
        }
    }

    /**
     * Gets a Lang instance for one of the supported NameTypes.
     * 
     * @param nameType
     *            the NameType to look up
     * @return a Lang encapsulating the language guessing rules for that name type
     */
    public static Lang instance(NameType nameType) {
        return Langs.get(nameType);
    }

    /**
     * <p>
     * Loads language rules from a resource.
     * </p>
     * <p>
     * In normal use, you will obtain instances of Lang through the {@link #instance(NameType)} method. You will only need to call this
     * yourself if you are developing custom language mapping rules.
     * </p>
     * 
     * @param languageRulesResourceName
     *            the fully-qualified resource name to load
     * @param languages
     *            the languages that these rules will support
     * @return a Lang encapsulating the loaded language-guessing rules.
     */
    public static Lang loadFromResource(String languageRulesResourceName, Languages languages) {
        List<LangRule> rules = new ArrayList<LangRule>();
        InputStream lRulesIS = Lang.class.getClassLoader().getResourceAsStream(languageRulesResourceName);

        if (lRulesIS == null) {
            throw new IllegalStateException("Unable to resolve required resource:" + LANGUAGE_RULES_RN);
        }

        Scanner scanner = new Scanner(lRulesIS, ResourceConstants.ENCODING);
        boolean inExtendedComment = false;
        while (scanner.hasNextLine()) {
            String rawLine = scanner.nextLine();
            String line = rawLine;

            if (inExtendedComment) {
                if (line.endsWith(ResourceConstants.EXT_CMT_END)) {
                    inExtendedComment = false;
                } else {
                    // discard doc comment line
                }
            } else {
                if (line.startsWith(ResourceConstants.EXT_CMT_START)) {
                    inExtendedComment = true;
                } else {
                    // discard comments
                    int cmtI = line.indexOf(ResourceConstants.CMT);
                    if (cmtI >= 0) {
                        // System.err.println("index of comment: " + cmtI);
                        line = line.substring(0, cmtI);
                    }

                    // trim leading-trailing whitespace
                    line = line.trim();

                    if (line.length() == 0)
                        continue; // empty lines can be safely skipped

                    // split it up
                    String[] parts = line.split("\\s+");
                    // System.err.println("part count: " + parts.length);

                    if (parts.length != 3) {
                        // fixme: we really need to log this somewhere
                        System.err.println("Warning: malformed line '" + rawLine + "'");
                        continue;
                    }

                    Pattern pattern = Pattern.compile(parts[0]);
                    String[] langs = parts[1].split("\\+");
                    boolean accept = parts[2].equals("true");

                    rules.add(new LangRule(pattern, new HashSet<String>(Arrays.asList(langs)), accept));
                }
            }
        }

        return new Lang(rules, languages);
    }

    private final Languages languages;
    private final List<LangRule> rules;

    private Lang(List<LangRule> rules, Languages languages) {
        this.rules = Collections.unmodifiableList(rules);
        this.languages = languages;
    }

    /**
     * Guesses the language of a word.
     * 
     * @param text
     *            the word
     * @return the language that the word originates from or {@link Languages#ANY} if there was no unique match
     */
    public String guessLanguage(String text) {
        Languages.LanguageSet ls = guessLanguages(text);
        return ls.isSingleton() ? ls.getAny() : Languages.ANY; 
    }

    /**
     * Guesses the languages of a word.
     * 
     * @param input
     *            the word
     * @return a Set of Strings of language names that are potential matches for the input word
     */
    public Languages.LanguageSet guessLanguages(String input) {
        String text = input.toLowerCase(); // todo: locale?
        // System.out.println("Testing text: '" + text + "'");

        Set<String> langs = new HashSet<String>(this.languages.getLanguages());
        for (LangRule rule : this.rules) {
            if (rule.matches(text)) {
                // System.out.println("Rule " + rule.pattern + " matches " + text);
                if (rule.acceptOnMatch) {
                    // System.out.println("Retaining " + rule.languages);
                    langs.retainAll(rule.languages);
                } else {
                    // System.out.println("Removing " + rule.languages);
                    langs.removeAll(rule.languages);
                }
                // System.out.println("Current languages: " + langs);
            } else {
                // System.out.println("Rule " + rule.pattern + " does not match " + text);
            }
        }

        Languages.LanguageSet ls = Languages.LanguageSet.from(langs);
        return ls.equals(Languages.NO_LANGUAGES) ? Languages.ANY_LANGUAGE : ls;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11380.java