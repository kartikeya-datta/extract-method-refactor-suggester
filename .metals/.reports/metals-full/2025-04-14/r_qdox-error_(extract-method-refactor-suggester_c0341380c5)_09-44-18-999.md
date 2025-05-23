error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6045.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6045.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6045.java
text:
```scala
r@@egexp = regularExpression.getRegexp(getProject());

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Ant" and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.util.Vector;
import java.util.Enumeration;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.RegularExpression;
import org.apache.tools.ant.types.Substitution;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.Tokenizer;
import org.apache.tools.ant.util.LineTokenizer;
import org.apache.tools.ant.util.regexp.Regexp;

/**
 * This splits up input into tokens and passes
 * the tokens to a sequence of filters.
 *
 * @author Peter Reilly
 * @since Ant 1.6
 * @see BaseFilterReader
 * @see ChainableReader
 * @see DynamicConfigurator
 */
public class TokenFilter extends BaseFilterReader
    implements ChainableReader {
    /**
     * string filters implement this interface
     */
    public interface Filter {
        /**
         * filter and/of modify a string
         *
         * @param string the string to filter
         * @return the modified string or null if the
         *         string did not pass the filter
         */
        String filter(String string);
    }


    /** string filters */
    private Vector    filters   = new Vector();
    /** the tokenizer to use on the input stream */
    private Tokenizer tokenizer = null;
    /** the output token termination */
    private String    delimOutput = null;
    /** the current string token from the input stream */
    private String    line      = null;
    /** the position in the current string token */
    private int       linePos   = 0;

    /**
     * Constructor for "dummy" instances.
     *
     * @see BaseFilterReader#BaseFilterReader()
     */
    public TokenFilter() {
        super();
    }

    /**
     * Creates a new filtered reader.
     *
     * @param in A Reader object providing the underlying stream.
     *           Must not be <code>null</code>.
     */
    public TokenFilter(final Reader in) {
        super(in);
    }


    /**
     * Returns the next character in the filtered stream, only including
     * lines from the original stream which match all of the specified
     * regular expressions.
     *
     * @return the next character in the resulting stream, or -1
     * if the end of the resulting stream has been reached
     *
     * @exception IOException if the underlying stream throws an IOException
     * during reading
     */

    public int read() throws IOException {
        if (tokenizer == null) {
            tokenizer = new LineTokenizer();
        }
        while (line == null || line.length() == 0) {
            line = tokenizer.getToken(in);
            if (line == null) {
                return -1;
            }
            for (Enumeration e = filters.elements(); e.hasMoreElements();) {
                Filter filter = (Filter) e.nextElement();
                line = filter.filter(line);
                if (line == null) {
                    break;
                }
            }
            linePos = 0;
            if (line != null) {
                if (tokenizer.getPostToken().length() != 0) {
                    if (delimOutput != null) {
                        line = line + delimOutput;
                    } else {
                        line = line + tokenizer.getPostToken();
                    }
                }
            }
        }
        int ch = line.charAt(linePos);
        linePos++;
        if (linePos == line.length()) {
            line = null;
        }
        return ch;
    }

    /**
     * Creates a new TokenFilter using the passed in
     * Reader for instantiation.
     *
     * @param reader A Reader object providing the underlying stream.
     *
     * @return a new filter based on this configuration
     */

    public final Reader chain(final Reader reader) {
        TokenFilter newFilter = new TokenFilter(reader);
        newFilter.filters = filters;
        newFilter.tokenizer = tokenizer;
        newFilter.delimOutput = delimOutput;
        newFilter.setProject(getProject());
        return newFilter;
    }

    /**
     * set the output delimitor.
     * @param delimOutput replaces the delim string returned by the
     *                    tokenizer, it it present.
     */

    public void setDelimOutput(String delimOutput) {
        this.delimOutput = resolveBackSlash(delimOutput);
    }

    // -----------------------------------------
    //  Predefined tokenizers
    // -----------------------------------------

    /**
     * add a line tokenizer - this is the default.
     * @param tokenizer the line tokenizer
     */

    public void addLineTokenizer(LineTokenizer tokenizer) {
        add(tokenizer);
    }

    /**
     * add a string tokenizer
     * @param tokenizer the string tokenizer
     */

    public void addStringTokenizer(StringTokenizer tokenizer) {
        add(tokenizer);
    }

    /**
     * add a file tokenizer
     * @param tokenizer the file tokenizer
     */
    public void addFileTokenizer(FileTokenizer tokenizer) {
        add(tokenizer);
    }

    /**
     * add an arbirarty tokenizer
     * @param tokenizer the tokenizer to all, only one allowed
     */

    public void add(Tokenizer tokenizer) {
        if (this.tokenizer != null) {
            throw new BuildException("Only one tokenizer allowed");
        }
        this.tokenizer = tokenizer;
    }

    // -----------------------------------------
    //  Predefined filters
    // -----------------------------------------

    /**
     * replace string filter
     * @param filter the replace string filter
     */
    public void addReplaceString(ReplaceString filter) {
        filters.addElement(filter);
    }

    /**
     * contains string filter
     * @param filter the contains string filter
     */
    public void addContainsString(ContainsString filter) {
        filters.addElement(filter);
    }

    /**
     * replace regex filter
     * @param filter the replace regex filter
     */
    public void addReplaceRegex(ReplaceRegex filter) {
        filters.addElement(filter);
    }

    /**
     * contains regex filter
     * @param filter the contains regex filter
     */
    public void addContainsRegex(ContainsRegex filter) {
        filters.addElement(filter);
    }

    /**
     * trim filter
     * @param filter the trim filter
     */
    public void addTrim(Trim filter) {
        filters.addElement(filter);
    }

    /**
     * ignore blank filter
     * @param filter the ignore blank filter
     */
    public void addIgnoreBlank(IgnoreBlank filter) {
        filters.addElement(filter);
    }

    /**
     * delete chars
     * @param filter the delete chaarcters filter
     */
    public void addDeleteCharacters(DeleteCharacters filter) {
        filters.addElement(filter);
    }

    /**
     * Add an arbitary filter
     * @param filter the filter to add
     */
    public void add(Filter filter) {
        filters.addElement(filter);
    }


    // --------------------------------------------
    //
    //      Tokenizer Classes
    //
    // --------------------------------------------

    /**
     * class to read the complete input into a string
     */
    public static class FileTokenizer extends ProjectComponent
        implements Tokenizer {
        /**
         * Get the complete input as a string
         * @param in the reader object
         * @return the complete input
         * @throws IOException if error reading
         */
        public String getToken(Reader in) throws IOException {
            return FileUtils.readFully(in);
        }

        /**
         * Return the intra-token string
         * @return an empty string always
         */
        public String getPostToken() {
            return "";
        }
    }

    /**
     * class to tokenize the input as areas seperated
     * by white space, or by a specified list of
     * delim characters. Behaves like java.util.StringTokenizer.
     * if the stream starts with delim characters, the first
     * token will be an empty string (unless the treat tokens
     * as delims flag is set).
     */
    public static class StringTokenizer extends ProjectComponent
        implements Tokenizer {
        private String intraString = "";
        private int    pushed = -2;
        private char[] delims = null;
        private boolean delimsAreTokens = false;
        private boolean suppressDelims = false;
        private boolean includeDelims = false;

        /**
         * attribute delims - the delimeter characters
         * @param delims a string containing the delimiter characters
         */
        public void setDelims(String delims) {
            this.delims = resolveBackSlash(delims).toCharArray();
        }

        /**
         * attribute delimsaretokens - treat delimiters as
         * separate tokens.
         * @param delimsAreTokens true if delimiters are to be separate
         */

        public void setDelimsAreTokens(boolean delimsAreTokens) {
            this.delimsAreTokens = delimsAreTokens;
        }
        /**
         * attribute suppressdelims - suppress delimiters.
         * default - false
         * @param suppressDelims if true do not report delimiters
         */
        public void setSuppressDelims(boolean suppressDelims) {
            this.suppressDelims = suppressDelims;
        }

        /**
         * attribute includedelims - treat delimiters as part
         * of the token.
         * default - false
         * @param includeDelims if true add deliters to the token
         */
        public void setIncludeDelims(boolean includeDelims) {
            this.includeDelims = includeDelims;
        }

        /**
         * find and return the next token
         *
         * @param in the input stream
         * @return the token
         * @exception IOException if an error occurs reading
         */
        public String getToken(Reader in) throws IOException {
            int ch = -1;
            if (pushed != -2) {
                ch = pushed;
                pushed = -2;
            } else {
                ch = in.read();
            }
            if (ch == -1) {
                return null;
            }
            boolean inToken = true;
            intraString = "";
            StringBuffer word = new StringBuffer();
            StringBuffer padding = new StringBuffer();
            while (ch != -1) {
                char c = (char) ch;
                boolean isDelim = isDelim(c);
                if (inToken) {
                    if (isDelim) {
                        if (delimsAreTokens) {
                            if (word.length() == 0) {
                                word.append(c);
                            } else {
                                pushed = ch;
                            }
                            break;
                        }
                        padding.append(c);
                        inToken = false;
                    } else {
                        word.append(c);
                    }
                } else {
                    if (isDelim) {
                        padding.append(c);
                    } else {
                        pushed = ch;
                        break;
                    }
                }
                ch = in.read();
            }
            intraString = padding.toString();
            if (includeDelims) {
                word.append(intraString);
            }
            return word.toString();
        }

        /**
         * @return the intratoken string
         */
        public String getPostToken() {
            if (suppressDelims || includeDelims) {
                return "";
            }
            return intraString;
        }

        private boolean isDelim(char ch) {
            if (delims == null) {
                return Character.isWhitespace(ch);
            }
            for (int i = 0; i < delims.length; ++i) {
                if (delims[i] == ch) {
                    return true;
                }
            }
            return false;
        }
    }

    // --------------------------------------------
    //
    //      Filter classes
    //
    // --------------------------------------------

    /**
     * Abstract class that converts derived filter classes into
     * ChainableReaderFilter's
     */
    public abstract static class ChainableReaderFilter extends ProjectComponent
        implements ChainableReader, Filter {
        private boolean byLine = true;

        /**
         * set wheter to use filetokenizer or line tokenizer
         * @param byLine if true use a linetokenizer (default) otherwise
         *               use a filetokenizer
         */
        public void setByLine(boolean byLine) {
            this.byLine = byLine;
        }

        /**
         * Chain a tokenfilter reader to a reader,
         *
         * @param reader the input reader object
         * @return the chained reader object
         */
        public Reader chain(Reader reader) {
            TokenFilter tokenFilter = new TokenFilter(reader);
            if (!byLine) {
                tokenFilter.add(new FileTokenizer());
            }
            tokenFilter.add(this);
            return tokenFilter;
        }
    }

    /**
     * Simple replace string filter.
     */
    public static class ReplaceString extends ChainableReaderFilter {
        private String from;
        private String to;

        /**
         * the from attribute
         *
         * @param from the string to replace
         */
        public void setFrom(String from) {
            this.from = from;
        }

        /**
         * the to attribute
         *
         * @param to the string to replace 'from' with
         */
        public void setTo(String to) {
            this.to = to;
        }

        /**
         * Filter a string 'line' replaceing from with to
         * (C&P from the Replace task)
         * @param line the string to be filtered
         * @return the filtered line
         */
        public String filter(String line) {
            if (from == null) {
                throw new BuildException("Missing from in stringreplace");
            }
            StringBuffer ret = new StringBuffer();
            int start = 0;
            int found = line.indexOf(from);
            while (found >= 0) {
                // write everything up to the from
                if (found > start) {
                    ret.append(line.substring(start, found));
                }

                // write the replacement to
                if (to != null) {
                    ret.append(to);
                }

                // search again
                start = found + from.length();
                found = line.indexOf(line, start);
            }

            // write the remaining characters
            if (line.length() > start) {
                ret.append(line.substring(start, line.length()));
            }

            return ret.toString();
        }
    }

    /**
     * Simple filter to filter lines contains strings
     */
    public static class ContainsString extends ProjectComponent
        implements Filter {
        private String contains;

        /**
         * the contains attribute
         * @param contains the string that the token should contain
         */
        public void setContains(String contains) {
            this.contains = contains;
        }

        /**
         * Filter strings that contain the contains attribute
         *
         * @param string the string to be filtered
         * @return null if the string does not contain "contains",
         *              string otherwise
         */
        public String filter(String string) {
            if (contains == null) {
                throw new BuildException("Missing contains in containsstring");
            }
            if (string.indexOf(contains) > -1) {
                return string;
            }
            return null;
        }
    }

    /**
     * filter to replace regex.
     */
    public static class ReplaceRegex extends ChainableReaderFilter {
        private String             from;
        private String             to;
        private RegularExpression  regularExpression;
        private Substitution       substitution;
        private boolean            initialized = false;
        private String             flags = "";
        private int                options;
        private Regexp             regexp;

        /**
         * the from attribute
         * @param from the regex string
         */
        public void setPattern(String from) {
            this.from = from;
        }
        /**
         * the to attribute
         * @param to the replacement string
         */
        public void setReplace(String to) {
            this.to = to;
        }

        /**
         * @param flags the regex flags
         */
        public void setFlags(String flags) {
            this.flags = flags;
        }

        private void initialize() {
            if (initialized) {
                return;
            }
            options = convertRegexOptions(flags);
            if (from == null) {
                throw new BuildException("Missing pattern in replaceregex");
            }
            regularExpression = new RegularExpression();
            regularExpression.setPattern(from);
            regexp = regularExpression.getRegexp(project);
            if (to == null) {
                to = "";
            }
            substitution = new Substitution();
            substitution.setExpression(to);
        }

        /**
         * @param line the string to modify
         * @return the modified string
         */
        public String filter(String line) {
            initialize();

            if (!regexp.matches(line, options)) {
                return line;
            }
            return regexp.substitute(
                line, substitution.getExpression(getProject()), options);
        }
    }

    /**
     * filter to filter tokens matching regular expressions.
     */
    public static class ContainsRegex extends ChainableReaderFilter {
        private String             from;
        private String             to;
        private Project            project;
        private RegularExpression  regularExpression;
        private Substitution       substitution;
        private boolean            initialized = false;
        private String             flags = "";
        private int                options;
        private Regexp             regexp;


        /**
         * @param from the regex pattern
         */
        public void setPattern(String from) {
            this.from = from;
        }

        /**
         * @param to the replacement string
         */
        public void setReplace(String to) {
            this.to = to;
        }

        /**
         * @param flags the regex flags
         */
        public void setFlags(String flags) {
            this.flags = flags;
        }

        private void initialize() {
            if (initialized) {
                return;
            }
            options = convertRegexOptions(flags);
            if (from == null) {
                throw new BuildException("Missing from in containsregex");
            }
            regularExpression = new RegularExpression();
            regularExpression.setPattern(from);
            regexp = regularExpression.getRegexp(project);
            if (to == null) {
                return;
            }
            substitution = new Substitution();
            substitution.setExpression(to);
        }

        /**
         * apply regex and substitution on a string
         * @param string the string to apply filter on
         * @return the filtered string
         */
        public String filter(String string) {
            initialize();
            if (!regexp.matches(string, options)) {
                return null;
            }
            if (substitution == null) {
                return string;
            }
            return regexp.substitute(
                string, substitution.getExpression(getProject()), options);
        }
    }

    /** Filter to trim white space */
    public static class Trim extends ChainableReaderFilter {
        /**
         * @param line the string to be trimed
         * @return the trimmed string
         */
        public String filter(String line) {
            return line.trim();
        }
    }



    /** Filter remove empty tokens */
    public static class IgnoreBlank extends ChainableReaderFilter {
        /**
         * @param line the line to modify
         * @return the trimed line
         */
        public String filter(String line) {
            if (line.trim().length() == 0) {
                return null;
            }
            return line;
        }
    }

    /**
     * Filter to delete characters
     */
    public static class DeleteCharacters extends ProjectComponent
        implements Filter, ChainableReader {
        // Attributes
        /** the list of characters to remove from the input */
        private String deleteChars = "";

        /**
         * Set the list of characters to delete
         * @param deleteChars the list of characters
         */
        public void setChars(String deleteChars) {
            this.deleteChars = resolveBackSlash(deleteChars);
        }

        /**
         * remove characters from a string
         * @param string the string to remove the characters from
         * @return the converted string
         */
        public String filter(String string) {
            StringBuffer output = new StringBuffer(string.length());
            for (int i = 0; i < string.length(); ++i) {
                char ch = string.charAt(i);
                if (!(isDeleteCharacter(ch))) {
                    output.append(ch);
                }
            }
            return output.toString();
        }

        /**
         * factory method to provide a reader that removes
         * the characters from a reader as part of a filter
         * chain
         * @param reader the reader object
         * @return the chained reader object
         */
        public Reader chain(Reader reader) {
            return new BaseFilterReader(reader) {
                /**
                 * @return the next non delete character
                 */
                public int read()
                    throws IOException {
                    while (true) {
                        int c = in.read();
                        if (c == -1) {
                            return c;
                        }
                        if (!(isDeleteCharacter((char) c))) {
                            return c;
                        }
                    }
                }
            };
        }

        /** check if the character c is to be deleted */
        private boolean isDeleteCharacter(char c) {
            for (int d = 0; d < deleteChars.length(); ++d) {
                if (deleteChars.charAt(d) ==  c) {
                    return true;
                }
            }
            return false;
        }
    }

    // --------------------------------------------------------
    //  static utility methods - could be placed somewhere else
    // --------------------------------------------------------

    /**
     * xml does not do "c" like interpetation of strings.
     * i.e. \n\r\t etc.
     * this methid processes \n, \r, \t, \f, \\
     * also subs \s -> " \n\r\t\f"
     * a trailing '\' will be ignored
     *
     * @param input raw string with possible embedded '\'s
     * @return converted string
     */
    public static String resolveBackSlash(String input) {
        StringBuffer b = new StringBuffer();
        boolean backSlashSeen = false;
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (!backSlashSeen) {
                if (c == '\\') {
                    backSlashSeen = true;
                } else {
                    b.append(c);
                }
            } else {
                switch (c) {
                    case '\\':
                        b.append((char) '\\');
                        break;
                    case 'n':
                        b.append((char) '\n');
                        break;
                    case 'r':
                        b.append((char) '\r');
                        break;
                    case 't':
                        b.append((char) '\t');
                        break;
                    case 'f':
                        b.append((char) '\f');
                        break;
                    case 's':
                        b.append(" \t\n\r\f");
                        break;
                    default:
                        b.append(c);
                }
                backSlashSeen = false;
            }
        }
        return b.toString();
    }

    /**
     * convert regex option flag characters to regex options
     * <dl>
     *   <li>g -  Regexp.REPLACE_ALL</li>
     *   <li>i -  Regexp.MATCH_CASE_INSENSITIVE</li>
     *   <li>m -  Regexp.MATCH_MULTILINE</li>
     *   <li>s -  Regexp.MATCH_SINGLELINE</li>
     * </dl>
     * @param flags the string containing the flags
     * @return the Regexp option bits
     */
    public static int convertRegexOptions(String flags) {
        if (flags == null) {
            return 0;
        }
        int options = 0;
        if (flags.indexOf('g') != -1) {
            options |= Regexp.REPLACE_ALL;
        }
        if (flags.indexOf('i') != -1) {
            options |= Regexp.MATCH_CASE_INSENSITIVE;
        }
        if (flags.indexOf('m') != -1) {
            options |= Regexp.MATCH_MULTILINE;
        }
        if (flags.indexOf('s') != -1) {
            options |= Regexp.MATCH_SINGLELINE;
        }
        return options;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6045.java