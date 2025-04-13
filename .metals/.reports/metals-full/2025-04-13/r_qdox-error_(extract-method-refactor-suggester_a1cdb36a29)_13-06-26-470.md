error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17690.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17690.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[272,2]

error in qdox parser
file content:
```java
offset: 7617
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17690.java
text:
```scala
{ // TODO finalize javadoc

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.protocol.http.documentvalidation;

import java.util.HashMap;
import java.util.Map;

/**
 * Lightweight document parser for HTML. This parser is only intended to process well
 * formed and simple HTML of the kind that would generally be utilised during testing.
 *
 * @author Chris Turner
 */
public class HtmlDocumentParser
{

    /** constant for unknown token. */
    public static final int UNKNOWN = -1;

    /** constant for end token. */
    public static final int END = 0;

    /** constant for comment token. */
    public static final int COMMENT = 1;

    /** constant for open tag token. */
    public static final int OPEN_TAG = 2;

    /** constant for open/close tag token. */
    public static final int OPENCLOSE_TAG = 3;

    /** constant for close tag token. */
    public static final int CLOSE_TAG = 4;

    /** constant for text token. */
    public static final int TEXT = 5;

    // Document parse elements
    private String document;

    private int pos;

    // Extracted content
    private String comment;

    private String tag;

    private Map attributes;

    private String text;

    /**
     * Create the parser for the current document.
     * @param document The document to parse
     */
    public HtmlDocumentParser(final String document)
    {
        this.document = document.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", " ");
        pos = 0;
    }

    /**
     * Get the comment.
     * @return The comment
     */
    public String getComment()
    {
        return comment;
    }

    /**
     * Get the tag name.
     * @return The tag name
     */
    public String getTag()
    {
        return tag;
    }

    /**
     * Get the attributes of the tag.
     * @return The attributes
     */
    public Map getAttributes()
    {
        return attributes;
    }

    /**
     * Get the text.
     * @return The text
     */
    public String getText()
    {
        return text;
    }

    /**
     * Iterates through the document searching for tokens. Returns the type of token that
     * was found. If an unexpected token was encountered then the parser writes this fact
     * to the console and continues
     * @return The token that was found
     */
    public int getNextToken()
    {
        while (pos < document.length())
        {
            char ch = document.charAt(pos);
            if (ch == '<')
            {
                return processDirective();
            }
            else
            {
                return processText();
            }
        }
        return END;
    }

    /**
     * Process text up to the next token.
     * @return The token code
     */
    private int processText()
    {
        StringBuffer buf = new StringBuffer();
        while (pos < document.length())
        {
            char ch = document.charAt(pos);
            if (ch == '<')
            {
                text = buf.toString();
                return TEXT;
            }
            else
            {
                buf.append(ch);
            }
            pos++;
        }
        if (buf.length() > 0)
        {
            text = buf.toString();
            return TEXT;
        }
        return END;
    }

    /**
     * Process a directive starting at the current position.
     * @return The token found
     */
    private int processDirective()
    {
        String part = document.substring(pos);
        if (part.matches("<!--.*-->.*"))
        {
            // This is a comment
            comment = part.substring(4, part.indexOf("-->")).trim();
            pos += part.indexOf("-->") + 3;
            return COMMENT;
        }
        else if (part.matches("<[^/].*>.*"))
        {
            // This is an opening tag
            if (part.matches("<[a-zA-Z]*>.*"))
            {
                // No attributes
                tag = part.substring(1, part.indexOf('>')).toLowerCase();
                attributes = new HashMap();
            }
            else
            {
                // Attributes
                tag = part.substring(1, part.indexOf(' ')).toLowerCase();
                String attributeString = part.substring(part.indexOf(' '), part.indexOf('>'));
                attributes = extractAttributes(attributeString);
            }
            pos += part.indexOf(">") + 1;
            return OPEN_TAG;
        }
        else if (part.matches("<.*/>.*"))
        {
            // This is an openclose tag
            if (part.matches("<[a-zA-Z]*/>.*"))
            {
                // No attributes
                tag = part.substring(1, part.indexOf("/>")).toLowerCase();
                attributes = new HashMap();
            }
            else
            {
                // Attributes
                tag = part.substring(1, part.indexOf(' ')).toLowerCase();
                String attributeString = part.substring(part.indexOf(' '), part.indexOf("/>"));
                attributes = extractAttributes(attributeString);
            }
            pos += part.indexOf("/>") + 2;
            return OPENCLOSE_TAG;
        }
        else if (part.matches("</.*>.*"))
        {
            // This is a closing tag
            tag = part.substring(2, part.indexOf('>')).trim().toLowerCase();
            pos += part.indexOf(">") + 1;
            return CLOSE_TAG;
        }
        else
        {
            int size = (part.length() > 30) ? 30 : part.length();
            System.err.println("Unexpected markup found: " + part.substring(0, size) + "...");
            return UNKNOWN;
        }
    }

    /**
     * Extract attributes from the given string.
     * @param attributeString The string
     * @return The map of attributes
     */
    private Map extractAttributes(String attributeString)
    {
        Map m = new HashMap();
        attributeString = attributeString.trim().replaceAll("\t", " ").replaceAll(" = ", "=");
        String[] attributeElements = attributeString.split(" ");
        for (int i = 0; i < attributeElements.length; i++)
        {
            String[] bits = attributeElements[i].split("=");
            if (bits.length == 1)
            {
                m.put(bits[0].trim().toLowerCase(), "");
            }
            else
            {
                bits[0] = bits[0].trim();
                StringBuffer value = new StringBuffer();
                for (int j = 1; j < bits.length; j++)
                {
                    value.append(bits[j]);
                    if (j < (bits.length - 1))
                        value.append('=');
                }
                bits[1] = value.toString().trim();
                if (bits[1].startsWith("\""))
                    bits[1] = bits[1].substring(1);
                if (bits[1].endsWith("\""))
                    bits[1] = bits[1].substring(0, bits[1].length() - 1);
                m.put(bits[0].toLowerCase(), bits[1]);
            }
        }
        return m;
    }
}@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17690.java