error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3317.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3317.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3317.java
text:
```scala
private static final b@@yte[] XML_PFX = {'<','?','x','m','l'};// "<?xml "

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jorphan.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.text.StrBuilder;

/**
 * This class contains frequently-used static utility methods.
 *
 */

// @see TestJorphanUtils for unit tests

public final class JOrphanUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private JOrphanUtils() {
    }

    /**
     * This is _almost_ equivalent to the String.split method in JDK 1.4. It is
     * here to enable us to support earlier JDKs.
     *
     * Note that unlike JDK1.4 split(), it optionally ignores leading split Characters,
     * and the splitChar parameter is not a Regular expression
     *
     * <P>
     * This piece of code used to be part of JMeterUtils, but was moved here
     * because some JOrphan classes use it too.
     *
     * @param splittee
     *            String to be split
     * @param splitChar
     *            Character(s) to split the string on, these are treated as a single unit
     * @param truncate
     *            Should adjacent and leading/trailing splitChars be removed?
     *
     * @return Array of all the tokens.
     *
     * @see #split(String, String, String)
     *
     */
    public static String[] split(String splittee, String splitChar,boolean truncate) {
        if (splittee == null || splitChar == null) {
            return new String[0];
        }
        final String EMPTY_ELEMENT = "";
        int spot;
        final int splitLength = splitChar.length();
        final String adjacentSplit = splitChar + splitChar;
        final int adjacentSplitLength = adjacentSplit.length();
        if(truncate) {
            while ((spot = splittee.indexOf(adjacentSplit)) != -1) {
                splittee = splittee.substring(0, spot + splitLength)
                        + splittee.substring(spot + adjacentSplitLength, splittee.length());
            }
            if(splittee.startsWith(splitChar)) {
                splittee = splittee.substring(splitLength);
            }
            if(splittee.endsWith(splitChar)) { // Remove trailing splitter
                splittee = splittee.substring(0,splittee.length()-splitLength);
            }
        }
        Vector<String> returns = new Vector<String>();
        final int length = splittee.length(); // This is the new length
        int start = 0;
        spot = 0;
        while (start < length && (spot = splittee.indexOf(splitChar, start)) > -1) {
            if (spot > 0) {
                returns.addElement(splittee.substring(start, spot));
            }
            else
            {
                returns.addElement(EMPTY_ELEMENT);
            }
            start = spot + splitLength;
        }
        if (start < length) {
            returns.add(splittee.substring(start));
        } else if (spot == length - splitLength){// Found splitChar at end of line
            returns.addElement(EMPTY_ELEMENT);
        }
        String[] values = new String[returns.size()];
        returns.copyInto(values);
        return values;
    }

    public static String[] split(String splittee,String splitChar)
    {
        return split(splittee,splitChar,true);
    }

    /**
     * Takes a String and a tokenizer character string, and returns a new array of
     * strings of the string split by the tokenizer character(s).
     *
     * Trailing delimiters are significant (unless the default = null)
     *
     * @param splittee
     *            String to be split.
     * @param delims
     *            Delimiter character(s) to split the string on
     * @param def
     *            Default value to place between two split chars that have
     *            nothing between them. If null, then ignore omitted elements.
     *
     * @return Array of all the tokens.
     *
     * @throws NullPointerException if splittee or delims are null
     *
     * @see #split(String, String, boolean)
     * @see #split(String, String)
     *
     * This is a rewritten version of JMeterUtils.split()
     */
    public static String[] split(String splittee, String delims, String def) {
        StringTokenizer tokens = new StringTokenizer(splittee,delims,def!=null);
        boolean lastWasDelim=false;
        List<String> strList=new ArrayList<String>();
        while (tokens.hasMoreTokens()) {
            String tok=tokens.nextToken();
            if (   tok.length()==1 // we have a single character; could be a token
                && delims.indexOf(tok)!=-1) // it is a token
            {
                if (lastWasDelim) {// we saw a delimiter last time
                    strList.add(def);// so add the default
                }
                lastWasDelim=true;
            } else {
                lastWasDelim=false;
                strList.add(tok);
            }
        }
        if (lastWasDelim) {
            strList.add(def);
        }
        return strList.toArray(new String[0]);
    }


    private static final String SPACES = "                                 ";

    private static final int SPACES_LEN = SPACES.length();

    /**
     * Right aligns some text in a StringBuilder N.B. modifies the input buffer
     *
     * @param in
     *            StringBuilder containing some text
     * @param len
     *            output length desired
     * @return input StringBuilder, with leading spaces
     */
    public static StringBuilder rightAlign(StringBuilder in, int len) {
        int pfx = len - in.length();
        if (pfx <= 0) {
            return in;
        }
        if (pfx > SPACES_LEN) {
            pfx = SPACES_LEN;
        }
        in.insert(0, SPACES.substring(0, pfx));
        return in;
    }

    /**
     * Left aligns some text in a StringBuilder N.B. modifies the input buffer
     *
     * @param in
     *            StringBuilder containing some text
     * @param len
     *            output length desired
     * @return input StringBuilder, with trailing spaces
     */
    public static StringBuilder leftAlign(StringBuilder in, int len) {
        int sfx = len - in.length();
        if (sfx <= 0) {
            return in;
        }
        if (sfx > SPACES_LEN) {
            sfx = SPACES_LEN;
        }
        in.append(SPACES.substring(0, sfx));
        return in;
    }

    /**
     * Convert a boolean to its upper case string representation.
     * Equivalent to Boolean.valueOf(boolean).toString().toUpperCase().
     *
     * @param value
     *            boolean to convert
     * @return "TRUE" or "FALSE"
     */
    public static String booleanToSTRING(boolean value) {
        return value ? "TRUE" : "FALSE";
    }

    /**
     * Simple-minded String.replace() for JDK1.3 Should probably be recoded...
     *
     * @param source
     *            input string
     * @param search
     *            string to look for (no regular expressions)
     * @param replace
     *            string to replace the search string
     * @return the output string
     */
    public static String replaceFirst(String source, String search, String replace) {
        int start = source.indexOf(search);
        int len = search.length();
        if (start == -1) {
            return source;
        }
        if (start == 0) {
            return replace + source.substring(len);
        }
        return source.substring(0, start) + replace + source.substring(start + len);
    }

    /**
     * Version of String.replaceAll() for JDK1.3
     * See below for another version which replaces strings rather than chars
     *
     * @param source
     *            input string
     * @param search
     *            char to look for (no regular expressions)
     * @param replace
     *            string to replace the search string
     * @return the output string
     */
    public static String replaceAllChars(String source, char search, String replace) {
        char[] chars = source.toCharArray();
        StringBuilder sb = new StringBuilder(source.length()+20);
        for(int i = 0; i < chars.length; i++){
            char c = chars[i];
            if (c == search){
                sb.append(replace);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Replace all patterns in a String
     *
     * @see String#replaceAll(String regex,String replacement) - JDK1.4 only
     *
     * @param input - string to be transformed
     * @param pattern - pattern to replace
     * @param sub - replacement
     * @return the updated string
     */
    public static String substitute(final String input, final String pattern, final String sub) {
        StringBuilder ret = new StringBuilder(input.length());
        int start = 0;
        int index = -1;
        final int length = pattern.length();
        while ((index = input.indexOf(pattern, start)) >= start) {
            ret.append(input.substring(start, index));
            ret.append(sub);
            start = index + length;
        }
        ret.append(input.substring(start));
        return ret.toString();
    }

    /**
     * Trim a string by the tokens provided.
     *
     * @param input string to trim
     * @param delims list of delimiters
     * @return input trimmed at the first delimiter
     */
    public static String trim(final String input, final String delims){
        StringTokenizer tokens = new StringTokenizer(input,delims);
        return tokens.hasMoreTokens() ? tokens.nextToken() : "";
    }

    /**
     * Returns a slice of a byte array.
     *
     * TODO - add bounds checking?
     *
     * @param array -
     *            input array
     * @param begin -
     *            start of slice
     * @param end -
     *            end of slice
     * @return slice from the input array
     */
    public static byte[] getByteArraySlice(byte[] array, int begin, int end) {
        byte[] slice = new byte[(end - begin + 1)];
        int count = 0;
        for (int i = begin; i <= end; i++) {
            slice[count] = array[i];
            count++;
        }

        return slice;
    }

    // N.B. Commons IO IOUtils has equivalent methods; these were added before IO was included
    // TODO - perhaps deprecate these in favour of Commons IO?
    /**
     * close a stream with no error thrown
     * @param is - InputStream (may be null)
     */
    public static void closeQuietly(InputStream is){
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * close a stream with no error thrown
     * @param os - OutputStream (may be null)
     */
    public static void closeQuietly(OutputStream os){
        try {
            if (os != null) {
                os.close();
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * close a Writer with no error thrown
     * @param wr - Writer (may be null)
     */
    public static void closeQuietly(Writer wr){
        try {
            if (wr != null) {
                wr.close();
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * close a Reader with no error thrown
     * @param rd - Reader (may be null)
     */
    public static void closeQuietly(Reader rd){
        try {
            if (rd != null) {
                rd.close();
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * close a Socket with no error thrown
     * @param sock - Socket (may be null)
     */
    public static void closeQuietly(Socket sock){
        try {
            if (sock!= null) {
                sock.close();
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * close a Socket with no error thrown
     * @param sock - ServerSocket (may be null)
     */
    public static void closeQuietly(ServerSocket sock){
        try {
            if (sock!= null) {
                sock.close();
            }
        } catch (IOException ignored) {
        }
    }

    /**
     * Check if a byte array starts with the given byte array.
     *
     * @see String#startsWith(String, int)
     *
     * @param target array to scan
     * @param search array to search for
     * @param offset starting offset (>=0)
     * @return true if the search array matches the target at the current offset
     */
    public static boolean startsWith(byte [] target, byte [] search, int offset){
        final int targetLength = target.length;
        final int searchLength = search.length;
        if (offset < 0 || searchLength > targetLength+offset){
            return false;
        }
        for(int i=0;i < searchLength; i++){
            if (target[i+offset] != search[i]){
                return false;
            }
        }
        return true;
    }

    private static final byte[] XML_PFX = "<?xml ".getBytes(); // $NON-NLS-1$

    /**
     * Detects if some content starts with the standard XML prefix.
     *
     * @param target the content to check
     * @return true if the document starts with the standard XML prefix.
     */
    public static boolean isXML(byte [] target){
        return startsWith(target, XML_PFX,0);
    }

    /**
     * Convert binary byte array to hex string.
     *
     * @param ba input binary byte array
     * @return hex representation of binary input
     */
    public static String baToHexString(byte ba[]) {
        StrBuilder sb = new StrBuilder(ba.length*2);
        for (int i = 0; i < ba.length; i++) {
            int j = ba[i] & 0xff;
            if (j < 16) {
                sb.append("0"); // $NON-NLS-1$ add zero padding
            }
            sb.append(Integer.toHexString(j));
        }
        return sb.toString();
    }

    /**
     * Convert binary byte array to hex string.
     *
     * @param ba input binary byte array
     * @return hex representation of binary input
     */
    public static byte[] baToHexBytes(byte ba[]) {
        byte[] hb = new byte[ba.length*2];
        for (int i = 0; i < ba.length; i++) {
            byte upper = (byte) ((ba[i] & 0xf0) >> 4);
            byte lower = (byte) (ba[i] & 0x0f);
            hb[2*i]=toHexChar(upper);
            hb[2*i+1]=toHexChar(lower);
        }
        return hb;
    }

    private static byte toHexChar(byte in){
        if (in < 10) return (byte) (in+'0');
        return (byte) ((in-10)+'a');
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3317.java