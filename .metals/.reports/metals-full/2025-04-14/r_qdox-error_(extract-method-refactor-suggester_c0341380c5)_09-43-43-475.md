error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2429.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2429.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2429.java
text:
```scala
c@@heckRandomData(random(), new UAX29URLEmailAnalyzer(TEST_VERSION_CURRENT), 1000*RANDOM_MULTIPLIER);

package org.apache.lucene.analysis.core;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.standard.UAX29URLEmailAnalyzer;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.util.Arrays;

public class TestUAX29URLEmailAnalyzer extends BaseTokenStreamTestCase {

  private Analyzer a = new UAX29URLEmailAnalyzer(TEST_VERSION_CURRENT);

  public void testHugeDoc() throws IOException {
    StringBuilder sb = new StringBuilder();
    char whitespace[] = new char[4094];
    Arrays.fill(whitespace, ' ');
    sb.append(whitespace);
    sb.append("testing 1234");
    String input = sb.toString();
    BaseTokenStreamTestCase.assertAnalyzesTo(a, input, new String[]{"testing", "1234"}) ;
  }

  public void testArmenian() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "Վիքիպեդիայի 13 միլիոն հոդվածները (4,600` հայերեն վիքիպեդիայում) գրվել են կամավորների կողմից ու համարյա բոլոր հոդվածները կարող է խմբագրել ցանկաց մարդ ով կարող է բացել Վիքիպեդիայի կայքը։",
        new String[] { "վիքիպեդիայի", "13", "միլիոն", "հոդվածները", "4,600", "հայերեն", "վիքիպեդիայում", "գրվել", "են", "կամավորների", "կողմից",
        "ու", "համարյա", "բոլոր", "հոդվածները", "կարող", "է", "խմբագրել", "ցանկաց", "մարդ", "ով", "կարող", "է", "բացել", "վիքիպեդիայի", "կայքը" } );
  }
  
  public void testAmharic() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "ዊኪፔድያ የባለ ብዙ ቋንቋ የተሟላ ትክክለኛና ነጻ መዝገበ ዕውቀት (ኢንሳይክሎፒዲያ) ነው። ማንኛውም",
        new String[] { "ዊኪፔድያ", "የባለ", "ብዙ", "ቋንቋ", "የተሟላ", "ትክክለኛና", "ነጻ", "መዝገበ", "ዕውቀት", "ኢንሳይክሎፒዲያ", "ነው", "ማንኛውም" } );
  }
  
  public void testArabic() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "الفيلم الوثائقي الأول عن ويكيبيديا يسمى \"الحقيقة بالأرقام: قصة ويكيبيديا\" (بالإنجليزية: Truth in Numbers: The Wikipedia Story)، سيتم إطلاقه في 2008.",
        new String[] { "الفيلم", "الوثائقي", "الأول", "عن", "ويكيبيديا", "يسمى", "الحقيقة", "بالأرقام", "قصة", "ويكيبيديا",
        "بالإنجليزية", "truth", "numbers", "wikipedia", "story", "سيتم", "إطلاقه", "في", "2008" } );
  }
  
  public void testAramaic() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "ܘܝܩܝܦܕܝܐ (ܐܢܓܠܝܐ: Wikipedia) ܗܘ ܐܝܢܣܩܠܘܦܕܝܐ ܚܐܪܬܐ ܕܐܢܛܪܢܛ ܒܠܫܢ̈ܐ ܣܓܝܐ̈ܐ܂ ܫܡܗ ܐܬܐ ܡܢ ܡ̈ܠܬܐ ܕ\"ܘܝܩܝ\" ܘ\"ܐܝܢܣܩܠܘܦܕܝܐ\"܀",
        new String[] { "ܘܝܩܝܦܕܝܐ", "ܐܢܓܠܝܐ", "wikipedia", "ܗܘ", "ܐܝܢܣܩܠܘܦܕܝܐ", "ܚܐܪܬܐ", "ܕܐܢܛܪܢܛ", "ܒܠܫܢ̈ܐ", "ܣܓܝܐ̈ܐ", "ܫܡܗ",
        "ܐܬܐ", "ܡܢ", "ܡ̈ܠܬܐ", "ܕ", "ܘܝܩܝ", "ܘ", "ܐܝܢܣܩܠܘܦܕܝܐ"});
  }
  
  public void testBengali() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "এই বিশ্বকোষ পরিচালনা করে উইকিমিডিয়া ফাউন্ডেশন (একটি অলাভজনক সংস্থা)। উইকিপিডিয়ার শুরু ১৫ জানুয়ারি, ২০০১ সালে। এখন পর্যন্ত ২০০টিরও বেশী ভাষায় উইকিপিডিয়া রয়েছে।",
        new String[] { "এই", "বিশ্বকোষ", "পরিচালনা", "করে", "উইকিমিডিয়া", "ফাউন্ডেশন", "একটি", "অলাভজনক", "সংস্থা", "উইকিপিডিয়ার",
        "শুরু", "১৫", "জানুয়ারি", "২০০১", "সালে", "এখন", "পর্যন্ত", "২০০টিরও", "বেশী", "ভাষায়", "উইকিপিডিয়া", "রয়েছে" });
  }
  
  public void testFarsi() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "ویکی پدیای انگلیسی در تاریخ ۲۵ دی ۱۳۷۹ به صورت مکملی برای دانشنامهٔ تخصصی نوپدیا نوشته شد.",
        new String[] { "ویکی", "پدیای", "انگلیسی", "در", "تاریخ", "۲۵", "دی", "۱۳۷۹", "به", "صورت", "مکملی",
        "برای", "دانشنامهٔ", "تخصصی", "نوپدیا", "نوشته", "شد" });
  }
  
  public void testGreek() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "Γράφεται σε συνεργασία από εθελοντές με το λογισμικό wiki, κάτι που σημαίνει ότι άρθρα μπορεί να προστεθούν ή να αλλάξουν από τον καθένα.",
        new String[] { "γράφεται", "σε", "συνεργασία", "από", "εθελοντές", "με", "το", "λογισμικό", "wiki", "κάτι", "που",
        "σημαίνει", "ότι", "άρθρα", "μπορεί", "να", "προστεθούν", "ή", "να", "αλλάξουν", "από", "τον", "καθένα" });
  }

  public void testThai() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "การที่ได้ต้องแสดงว่างานดี. แล้วเธอจะไปไหน? ๑๒๓๔",
        new String[] { "การที่ได้ต้องแสดงว่างานดี", "แล้วเธอจะไปไหน", "๑๒๓๔" });
  }
  
  public void testLao() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "ສາທາລະນະລັດ ປະຊາທິປະໄຕ ປະຊາຊົນລາວ", 
        new String[] { "ສາທາລະນະລັດ", "ປະຊາທິປະໄຕ", "ປະຊາຊົນລາວ" });
  }
  
  public void testTibetan() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "སྣོན་མཛོད་དང་ལས་འདིས་བོད་ཡིག་མི་ཉམས་གོང་འཕེལ་དུ་གཏོང་བར་ཧ་ཅང་དགེ་མཚན་མཆིས་སོ། །",
                     new String[] { "སྣོན", "མཛོད", "དང", "ལས", "འདིས", "བོད", "ཡིག", 
                                    "མི", "ཉམས", "གོང", "འཕེལ", "དུ", "གཏོང", "བར", 
                                    "ཧ", "ཅང", "དགེ", "མཚན", "མཆིས", "སོ" });
  }
  
  /*
   * For chinese, tokenize as char (these can later form bigrams or whatever)
   */
  public void testChinese() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "我是中国人。 １２３４ Ｔｅｓｔｓ ",
        new String[] { "我", "是", "中", "国", "人", "１２３４", "ｔｅｓｔｓ"});
  }
  
  public void testEmpty() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "", new String[] {});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, ".", new String[] {});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, " ", new String[] {});
  }
  
  /* test various jira issues this analyzer is related to */
  
  public void testLUCENE1545() throws Exception {
    /*
     * Standard analyzer does not correctly tokenize combining character U+0364 COMBINING LATIN SMALL LETTER E.
     * The word "moͤchte" is incorrectly tokenized into "mo" "chte", the combining character is lost.
     * Expected result is only one token "moͤchte".
     */
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "moͤchte", new String[] { "moͤchte" }); 
  }
  
  /* Tests from StandardAnalyzer, just to show behavior is similar */
  public void testAlphanumericSA() throws Exception {
    // alphanumeric tokens
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "B2B", new String[]{"b2b"});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "2B", new String[]{"2b"});
  }

  public void testDelimitersSA() throws Exception {
    // other delimiters: "-", "/", ","
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "some-dashed-phrase", new String[]{"some", "dashed", "phrase"});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "dogs,chase,cats", new String[]{"dogs", "chase", "cats"});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "ac/dc", new String[]{"ac", "dc"});
  }

  public void testApostrophesSA() throws Exception {
    // internal apostrophes: O'Reilly, you're, O'Reilly's
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "O'Reilly", new String[]{"o'reilly"});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "you're", new String[]{"you're"});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "she's", new String[]{"she's"});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "Jim's", new String[]{"jim's"});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "don't", new String[]{"don't"});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "O'Reilly's", new String[]{"o'reilly's"});
  }

  public void testNumericSA() throws Exception {
    // floating point, serial, model numbers, ip addresses, etc.
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "21.35", new String[]{"21.35"});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "R2D2 C3PO", new String[]{"r2d2", "c3po"});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "216.239.63.104", new String[]{"216.239.63.104"});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "216.239.63.104", new String[]{"216.239.63.104"});
  }

  public void testTextWithNumbersSA() throws Exception {
    // numbers
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "David has 5000 bones", new String[]{"david", "has", "5000", "bones"});
  }

  public void testVariousTextSA() throws Exception {
    // various
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "C embedded developers wanted", new String[]{"c", "embedded", "developers", "wanted"});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "foo bar FOO BAR", new String[]{"foo", "bar", "foo", "bar"});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "foo      bar .  FOO <> BAR", new String[]{"foo", "bar", "foo", "bar"});
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "\"QUOTED\" word", new String[]{"quoted", "word"});
  }

  public void testKoreanSA() throws Exception {
    // Korean words
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "안녕하세요 한글입니다", new String[]{"안녕하세요", "한글입니다"});
  }
  
  public void testOffsets() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "David has 5000 bones", 
        new String[] {"david", "has", "5000", "bones"},
        new int[] {0, 6, 10, 15},
        new int[] {5, 9, 14, 20});
  }
  
  public void testTypes() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "david has 5000 bones",
        new String[] {"david", "has", "5000", "bones"},
        new String[] { "<ALPHANUM>", "<ALPHANUM>", "<NUM>", "<ALPHANUM>" });
  }
  
  public void testSupplementary() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "𩬅艱鍟䇹愯瀛", 
        new String[] {"𩬅", "艱", "鍟", "䇹", "愯", "瀛"},
        new String[] { "<IDEOGRAPHIC>", "<IDEOGRAPHIC>", "<IDEOGRAPHIC>", "<IDEOGRAPHIC>", "<IDEOGRAPHIC>", "<IDEOGRAPHIC>" });
  }
  
  public void testKorean() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "훈민정음",
        new String[] { "훈민정음" },
        new String[] { "<HANGUL>" });
  }
  
  public void testJapanese() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "仮名遣い カタカナ",
        new String[] { "仮", "名", "遣", "い", "カタカナ" },
        new String[] { "<IDEOGRAPHIC>", "<IDEOGRAPHIC>", "<IDEOGRAPHIC>", "<HIRAGANA>", "<KATAKANA>" });
  }
  
  public void testCombiningMarks() throws Exception {
    checkOneTerm(a, "ざ", "ざ"); // hiragana
    checkOneTerm(a, "ザ", "ザ"); // katakana
    checkOneTerm(a, "壹゙", "壹゙"); // ideographic
    checkOneTerm(a, "아゙",  "아゙"); // hangul
  }
  
  /** @deprecated remove this and sophisticated backwards layer in 5.0 */
  @Deprecated
  public void testCombiningMarksBackwards() throws Exception {
    Analyzer a = new UAX29URLEmailAnalyzer(Version.LUCENE_33);
    checkOneTerm(a, "ざ", "さ"); // hiragana Bug
    checkOneTerm(a, "ザ", "ザ"); // katakana Works
    checkOneTerm(a, "壹゙", "壹"); // ideographic Bug
    checkOneTerm(a, "아゙",  "아゙"); // hangul Works
  }

  public void testBasicEmails() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a,
        "one test@example.com two three [A@example.CO.UK] \"ArakaBanassaMassanaBakarA\" <info@Info.info>",
        new String[] {"one", "test@example.com", "two", "three", "a@example.co.uk", "arakabanassamassanabakara", "info@info.info",},
        new String[] { "<ALPHANUM>", "<EMAIL>", "<ALPHANUM>", "<ALPHANUM>", "<EMAIL>", "<ALPHANUM>", "<EMAIL>" });
  }

  public void testMailtoSchemeEmails () throws Exception {
    // See LUCENE-3880
    BaseTokenStreamTestCase.assertAnalyzesTo(a, "MAILTO:Test@Example.ORG",
        new String[] {"mailto", "test@example.org"},
        new String[] { "<ALPHANUM>", "<EMAIL>" });

    // TODO: Support full mailto: scheme URIs. See RFC 6068: http://tools.ietf.org/html/rfc6068
    BaseTokenStreamTestCase.assertAnalyzesTo
        (a,  "mailto:personA@example.com,personB@example.com?cc=personC@example.com"
            + "&subject=Subjectivity&body=Corpusivity%20or%20something%20like%20that",
            new String[] { "mailto",
                "persona@example.com",
                // TODO: recognize ',' address delimiter. Also, see examples of ';' delimiter use at: http://www.mailto.co.uk/
                ",personb@example.com",
                "?cc=personc@example.com", // TODO: split field keys/values
                "subject", "subjectivity",
                "body", "corpusivity", "20or", "20something","20like", "20that" }, // TODO: Hex decoding + re-tokenization
            new String[] { "<ALPHANUM>",
                "<EMAIL>",
                "<EMAIL>",
                "<EMAIL>",
                "<ALPHANUM>", "<ALPHANUM>",
                "<ALPHANUM>", "<ALPHANUM>", "<ALPHANUM>", "<ALPHANUM>", "<ALPHANUM>", "<ALPHANUM>" });
  }

  public void testBasicURLs() throws Exception {
    BaseTokenStreamTestCase.assertAnalyzesTo(a,
        "a <HTTPs://example.net/omg/isnt/that/NICE?no=its&n%30t#mntl-E>b-D ftp://www.example.com/ABC.txt file:///C:/path/to/a/FILE.txt C",
        new String[] {"https://example.net/omg/isnt/that/nice?no=its&n%30t#mntl-e", "b", "d", "ftp://www.example.com/abc.txt", "file:///c:/path/to/a/file.txt", "c" },
        new String[] { "<URL>", "<ALPHANUM>", "<ALPHANUM>", "<URL>", "<URL>", "<ALPHANUM>" });
  }

  
  /** blast some random strings through the analyzer */
  public void testRandomStrings() throws Exception {
    checkRandomData(random(), new UAX29URLEmailAnalyzer(TEST_VERSION_CURRENT), 10000*RANDOM_MULTIPLIER);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2429.java