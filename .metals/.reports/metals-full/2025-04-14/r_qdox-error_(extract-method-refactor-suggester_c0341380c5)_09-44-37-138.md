error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15895.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15895.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15895.java
text:
```scala
i@@f (null != tag.getAttributes().put(key, value))

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
package org.apache.wicket.markup.parser;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import org.apache.wicket.markup.parser.XmlTag.TagType;
import org.apache.wicket.markup.parser.XmlTag.TextSegment;
import org.apache.wicket.util.io.FullyBufferedReader;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.io.XmlReader;
import org.apache.wicket.util.parse.metapattern.parsers.TagNameParser;
import org.apache.wicket.util.parse.metapattern.parsers.VariableAssignmentParser;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 * A fairly shallow markup pull parser which parses a markup string of a given type of markup (for
 * example, html, xml, vxml or wml) into ComponentTag and RawMarkup tokens.
 * 
 * @author Jonathan Locke
 * @author Juergen Donnerstag
 */
public final class XmlPullParser implements IXmlPullParser
{
	/** */
	public static final String STYLE = "style";

	/** */
	public static final String SCRIPT = "script";

	/**
	 * Reads the xml data from an input stream and converts the chars according to its encoding
	 * (<?xml ... encoding="..." ?>)
	 */
	private XmlReader xmlReader;

	/**
	 * A XML independent reader which loads the whole source data into memory and which provides
	 * convenience methods to access the data.
	 */
	private FullyBufferedReader input;

	/** temporary variable which will hold the name of the closing tag. */
	private String skipUntilText;

	/** The last substring selected from the input */
	private CharSequence lastText;

	/** Everything in between &lt;!DOCTYPE ... &gt; */
	private CharSequence doctype;

	/** The type of what is in lastText */
	private HttpTagType lastType = HttpTagType.NOT_INITIALIZED;

	/** The last tag found */
	private XmlTag lastTag;

	/**
	 * Construct.
	 */
	public XmlPullParser()
	{
	}

	public final String getEncoding()
	{
		return xmlReader.getEncoding();
	}

	public final CharSequence getXmlDeclaration()
	{
		return xmlReader.getXmlDeclaration();
	}

	public final CharSequence getDoctype()
	{
		return doctype;
	}

	public final CharSequence getInputFromPositionMarker(final int toPos)
	{
		return input.getSubstring(toPos);
	}

	public final CharSequence getInput(final int fromPos, final int toPos)
	{
		return input.getSubstring(fromPos, toPos);
	}

	/**
	 * Whatever will be in between the current index and the closing tag, will be ignored (and thus
	 * treated as raw markup (text). This is useful for tags like 'script'.
	 * 
	 * @throws ParseException
	 */
	private final void skipUntil() throws ParseException
	{
		// this is a tag with non-XHTML text as body - skip this until the
		// skipUntilText is found.
		final int startIndex = input.getPosition();
		final int tagNameLen = skipUntilText.length();

		int pos = input.getPosition() - 1;
		String endTagText = null;
		int lastPos = 0;
		while (!skipUntilText.equalsIgnoreCase(endTagText))
		{
			pos = input.find("</", pos + 1);
			if ((pos == -1) || ((pos + (tagNameLen + 2)) >= input.size()))
			{
				throw new ParseException(
					skipUntilText + " tag not closed" + getLineAndColumnText(), startIndex);
			}

			lastPos = pos + 2;
			endTagText = input.getSubstring(lastPos, lastPos + tagNameLen).toString();
		}

		input.setPosition(pos);
		lastText = input.getSubstring(startIndex, pos);
		lastType = HttpTagType.BODY;

		// Check that the tag is properly closed
		lastPos = input.find('>', lastPos + tagNameLen);
		if (lastPos == -1)
		{
			throw new ParseException(skipUntilText + " tag not closed" + getLineAndColumnText(),
				startIndex);
		}

		// Reset the state variable
		skipUntilText = null;
	}

	/**
	 * 
	 * @return line and column number
	 */
	private String getLineAndColumnText()
	{
		return " (line " + input.getLineNumber() + ", column " + input.getColumnNumber() + ")";
	}

	/**
	 * @return XXX
	 * @throws ParseException
	 */
	public final HttpTagType next() throws ParseException
	{
		// Reached end of markup file?
		if (input.getPosition() >= input.size())
		{
			return HttpTagType.NOT_INITIALIZED;
		}

		if (skipUntilText != null)
		{
			skipUntil();
			return lastType;
		}

		// Any more tags in the markup?
		final int openBracketIndex = input.find('<');

		// Tag or Body?
		if (input.charAt(input.getPosition()) != '<')
		{
			// It's a BODY
			if (openBracketIndex == -1)
			{
				// There is no next matching tag.
				lastText = input.getSubstring(-1);
				input.setPosition(input.size());
				lastType = HttpTagType.BODY;
				return lastType;
			}

			lastText = input.getSubstring(openBracketIndex);
			input.setPosition(openBracketIndex);
			lastType = HttpTagType.BODY;
			return lastType;
		}

		// Determine the line number
		input.countLinesTo(openBracketIndex);

		// Get index of closing tag and advance past the tag
		int closeBracketIndex = input.find('>', openBracketIndex + 1);
		if (closeBracketIndex == -1)
		{
			throw new ParseException("No matching close bracket at" + getLineAndColumnText(),
				input.getPosition());
		}

		// Get the complete tag text
		lastText = input.getSubstring(openBracketIndex, closeBracketIndex + 1);

		// Get the tagtext between open and close brackets
		String tagText = lastText.subSequence(1, lastText.length() - 1).toString();
		if (tagText.length() == 0)
		{
			throw new ParseException("Found empty tag: '<>' at" + getLineAndColumnText(),
				input.getPosition());
		}

		// Type of the tag, to be determined next
		final TagType type;

		// If the tag ends in '/', it's a "simple" tag like <foo/>
		if (tagText.endsWith("/"))
		{
			type = TagType.OPEN_CLOSE;
			tagText = tagText.substring(0, tagText.length() - 1);
		}
		else if (tagText.startsWith("/"))
		{
			// The tag text starts with a '/', it's a simple close tag
			type = TagType.CLOSE;
			tagText = tagText.substring(1);
		}
		else
		{
			// It must be an open tag
			type = TagType.OPEN;

			// If open tag and starts with "s" like "script" or "style", than ...
			if ((tagText.length() > STYLE.length()) &&
				((tagText.charAt(0) == 's') || (tagText.charAt(0) == 'S')))
			{
				final String lowerCase = tagText.substring(0, 6).toLowerCase();
				if (lowerCase.startsWith(SCRIPT))
				{
					// prepare to skip everything between the open and close tag
					skipUntilText = SCRIPT;
				}
				else if (lowerCase.startsWith(STYLE))
				{
					// prepare to skip everything between the open and close tag
					skipUntilText = STYLE;
				}
			}
		}

		// Handle special tags like <!-- and <![CDATA ...
		final char firstChar = tagText.charAt(0);
		if ((firstChar == '!') || (firstChar == '?'))
		{
			specialTagHandling(tagText, openBracketIndex, closeBracketIndex);

			input.countLinesTo(openBracketIndex);
			TextSegment text = new TextSegment(lastText, openBracketIndex, input.getLineNumber(),
				input.getColumnNumber());
			lastTag = new XmlTag(text, type);

			return lastType;
		}

		TextSegment text = new TextSegment(lastText, openBracketIndex, input.getLineNumber(),
			input.getColumnNumber());
		XmlTag tag = new XmlTag(text, type);
		lastTag = tag;

		// Parse the tag text and populate tag attributes
		if (parseTagText(tag, tagText))
		{
			// Move to position after the tag
			input.setPosition(closeBracketIndex + 1);
			lastType = HttpTagType.TAG;
			return lastType;
		}
		else
		{
			throw new ParseException("Malformed tag" + getLineAndColumnText(), openBracketIndex);
		}
	}

	/**
	 * Handle special tags like <!-- --> or <![CDATA[..]]> or <?xml>
	 * 
	 * @param tagText
	 * @param openBracketIndex
	 * @param closeBracketIndex
	 * @throws ParseException
	 */
	protected void specialTagHandling(String tagText, final int openBracketIndex,
		int closeBracketIndex) throws ParseException
	{
		// Handle comments
		if (tagText.startsWith("!--"))
		{
			// downlevel-revealed conditional comments e.g.: <!--[if (gt IE9)|!(IE)]><!-->
			if (tagText.contains("![endif]--"))
			{
				lastType = HttpTagType.CONDITIONAL_COMMENT_ENDIF;

				// Move to position after the tag
				input.setPosition(closeBracketIndex + 1);
				return;
			}

			// Conditional comment? E.g.
			// "<!--[if IE]><a href='test.html'>my link</a><![endif]-->"
			if (tagText.startsWith("!--[if ") && tagText.endsWith("]"))
			{
				int pos = input.find("]-->", openBracketIndex + 1);
				if (pos == -1)
				{
					throw new ParseException("Unclosed conditional comment beginning at" +
						getLineAndColumnText(), openBracketIndex);
				}

				pos += 4;
				lastText = input.getSubstring(openBracketIndex, pos);

				// Actually it is no longer a comment. It is now
				// up to the browser to select the section appropriate.
				input.setPosition(closeBracketIndex + 1);
				lastType = HttpTagType.CONDITIONAL_COMMENT;
			}
			else
			{
				// Normal comment section.
				// Skip ahead to "-->". Note that you can not simply test for
				// tagText.endsWith("--") as the comment might contain a '>'
				// inside.
				int pos = input.find("-->", openBracketIndex + 1);
				if (pos == -1)
				{
					throw new ParseException("Unclosed comment beginning at" +
						getLineAndColumnText(), openBracketIndex);
				}

				pos += 3;
				lastText = input.getSubstring(openBracketIndex, pos);
				lastType = HttpTagType.COMMENT;
				input.setPosition(pos);
			}
			return;
		}

		// The closing tag of a conditional comment, e.g.
		// "<!--[if IE]><a href='test.html'>my link</a><![endif]-->
		// and also <!--<![endif]-->"
		if (tagText.equals("![endif]--"))
		{
			lastType = HttpTagType.CONDITIONAL_COMMENT;
			input.setPosition(closeBracketIndex + 1);
			return;
		}

		// CDATA sections might contain "<" which is not part of an XML tag.
		// Make sure escaped "<" are treated right
		if (tagText.startsWith("!["))
		{
			final String startText = (tagText.length() <= 8 ? tagText : tagText.substring(0, 8));
			if (startText.toUpperCase().equals("![CDATA["))
			{
				int pos1 = openBracketIndex;
				do
				{
					// Get index of closing tag and advance past the tag
					closeBracketIndex = findChar('>', pos1);

					if (closeBracketIndex == -1)
					{
						throw new ParseException("No matching close bracket at" +
							getLineAndColumnText(), input.getPosition());
					}

					// Get the tagtext between open and close brackets
					tagText = input.getSubstring(openBracketIndex + 1, closeBracketIndex)
						.toString();

					pos1 = closeBracketIndex + 1;
				}
				while (tagText.endsWith("]]") == false);

				// Move to position after the tag
				input.setPosition(closeBracketIndex + 1);

				lastText = tagText;
				lastType = HttpTagType.CDATA;
				return;
			}
		}

		if (tagText.charAt(0) == '?')
		{
			lastType = HttpTagType.PROCESSING_INSTRUCTION;

			// Move to position after the tag
			input.setPosition(closeBracketIndex + 1);
			return;
		}

		if (tagText.startsWith("!DOCTYPE"))
		{
			lastType = HttpTagType.DOCTYPE;

			// Get the tagtext between open and close brackets
			doctype = input.getSubstring(openBracketIndex + 1, closeBracketIndex);

			// Move to position after the tag
			input.setPosition(closeBracketIndex + 1);
			return;
		}

		// Move to position after the tag
		lastType = HttpTagType.SPECIAL_TAG;
		input.setPosition(closeBracketIndex + 1);
	}

	/**
	 * @return MarkupElement
	 */
	public final XmlTag getElement()
	{
		return lastTag;
	}

	/**
	 * @return The xml string from the last element
	 */
	public final CharSequence getString()
	{
		return lastText;
	}

	/**
	 * @return The next XML tag
	 * @throws ParseException
	 */
	public final XmlTag nextTag() throws ParseException
	{
		while (next() != HttpTagType.NOT_INITIALIZED)
		{
			switch (lastType)
			{
				case TAG :
					return lastTag;

				case BODY :
					break;

				case COMMENT :
					break;

				case CONDITIONAL_COMMENT :
					break;

				case CDATA :
					break;

				case PROCESSING_INSTRUCTION :
					break;

				case SPECIAL_TAG :
					break;
			}
		}

		return null;
	}

	/**
	 * Find the char but ignore any text within ".." and '..'
	 * 
	 * @param ch
	 *            The character to search
	 * @param startIndex
	 *            Start index
	 * @return -1 if not found, else the index
	 */
	private int findChar(final char ch, int startIndex)
	{
		char quote = 0;

		for (; startIndex < input.size(); startIndex++)
		{
			final char charAt = input.charAt(startIndex);
			if (quote != 0)
			{
				if (quote == charAt)
				{
					quote = 0;
				}
			}
			else if ((charAt == '"') || (charAt == '\''))
			{
				quote = charAt;
			}
			else if (charAt == ch)
			{
				return startIndex;
			}
		}

		return -1;
	}

	/**
	 * Parse the given string.
	 * <p>
	 * Note: xml character encoding is NOT applied. It is assumed the input provided does have the
	 * correct encoding already.
	 * 
	 * @param string
	 *            The input string
	 * @throws IOException
	 *             Error while reading the resource
	 * @throws ResourceStreamNotFoundException
	 *             Resource not found
	 */
	public void parse(final CharSequence string) throws IOException,
		ResourceStreamNotFoundException
	{
		parse(new ByteArrayInputStream(string.toString().getBytes()), null);
	}

	/**
	 * Reads and parses markup from an input stream, using UTF-8 encoding by default when not
	 * specified in XML declaration.
	 * 
	 * @param in
	 *            The input stream to read and parse
	 * @throws IOException
	 * @throws ResourceStreamNotFoundException
	 */
	public void parse(final InputStream in) throws IOException, ResourceStreamNotFoundException
	{
		// When XML declaration does not specify encoding, it defaults to UTF-8
		parse(in, "UTF-8");
	}

	/**
	 * Reads and parses markup from an input stream
	 * 
	 * @param inputStream
	 *            The input stream to read and parse
	 * @param encoding
	 *            The default character encoding of the input
	 * @throws IOException
	 * @throws ResourceStreamNotFoundException
	 */
	public void parse(final InputStream inputStream, final String encoding) throws IOException,
		ResourceStreamNotFoundException
	{
		try
		{
			xmlReader = new XmlReader(new BufferedInputStream(inputStream, 4000), encoding);
			input = new FullyBufferedReader(xmlReader);
		}
		finally
		{
			try
			{
				inputStream.close();
			}
			finally
			{
				IOUtils.close(xmlReader);
			}
		}
	}

	public final void setPositionMarker()
	{
		input.setPositionMarker(input.getPosition());
	}

	public final void setPositionMarker(final int pos)
	{
		input.setPositionMarker(pos);
	}

	@Override
	public String toString()
	{
		return input.toString();
	}

	/**
	 * Parses the text between tags. For example, "a href=foo.html".
	 * 
	 * @param tag
	 * @param tagText
	 *            The text between tags
	 * @return false in case of an error
	 * @throws ParseException
	 */
	private boolean parseTagText(final XmlTag tag, final String tagText) throws ParseException
	{
		// Get the length of the tagtext
		final int tagTextLength = tagText.length();

		// If we match tagname pattern
		final TagNameParser tagnameParser = new TagNameParser(tagText);
		if (tagnameParser.matcher().lookingAt())
		{
			// Extract the tag from the pattern matcher
			tag.name = tagnameParser.getName();
			tag.namespace = tagnameParser.getNamespace();

			// Are we at the end? Then there are no attributes, so we just
			// return the tag
			int pos = tagnameParser.matcher().end(0);
			if (pos == tagTextLength)
			{
				return true;
			}

			// Extract attributes
			final VariableAssignmentParser attributeParser = new VariableAssignmentParser(tagText);
			while (attributeParser.matcher().find(pos))
			{
				// Get key and value using attribute pattern
				String value = attributeParser.getValue();

				// In case like <html xmlns:wicket> will the value be null
				if (value == null)
				{
					value = "";
				}

				// Set new position to end of attribute
				pos = attributeParser.matcher().end(0);

				// Chop off double quotes or single quotes
				if (value.startsWith("\"") || value.startsWith("\'"))
				{
					value = value.substring(1, value.length() - 1);
				}

				// Trim trailing whitespace
				value = value.trim();

				// Get key
				final String key = attributeParser.getKey();

				// Put the attribute in the attributes hash
				if (null != ((TagAttributes)tag.getAttributes()).putInternal(key, value))
				{
					throw new ParseException("Same attribute found twice: " + key +
						getLineAndColumnText(), input.getPosition());
				}

				// The input has to match exactly (no left over junk after
				// attributes)
				if (pos == tagTextLength)
				{
					return true;
				}
			}

			return true;
		}

		return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15895.java