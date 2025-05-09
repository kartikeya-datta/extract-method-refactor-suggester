error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5375.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5375.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5375.java
text:
```scala
r@@ules.setEscapeRule(ParserRuleFactory.createEscapeRule(lastEscape));

/*
 * XModeHandler.java - XML handler for mode files
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 1999 mike dillon
 * Portions copyright (C) 2000, 2001 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.gjt.sp.jedit.syntax;

//{{{ Imports
import com.microstar.xml.*;
import gnu.regexp.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import org.gjt.sp.jedit.search.RESearchMatcher;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;
//}}}

public class XModeHandler extends HandlerBase
{
	//{{{ XModeHandler constructor
	public XModeHandler (XmlParser parser, String modeName, String path)
	{
		this.modeName = modeName;
		this.parser = parser;
		this.path = path;
		stateStack = new Stack();
	} //}}}

	//{{{ resolveEntity() method
	public Object resolveEntity(String publicId, String systemId)
	{
		if("xmode.dtd".equals(systemId))
		{
			// this will result in a slight speed up, since we
			// don't need to read the DTD anyway, as AElfred is
			// non-validating
			return new StringReader("<!-- -->");

			/* try
			{
				return new BufferedReader(new InputStreamReader(
					getClass().getResourceAsStream(
					"/org/gjt/sp/jedit/xmode.dtd")));
			}
			catch(Exception e)
			{
				error("dtd",e);
			} */
		}

		return null;
	} //}}}

	//{{{ attribute() method
	public void attribute(String aname, String value, boolean isSpecified)
	{
		String tag = peekElement();
		aname = (aname == null) ? null : aname.intern();

		if (aname == "NAME")
		{
			propName = value;
		}
		else if (aname == "VALUE")
		{
			propValue = value;
		}
		else if (aname == "TYPE")
		{
			lastTokenID = Token.stringToToken(value);
			if(lastTokenID == -1)
			{
				error("token-invalid",value);
				lastTokenIDString = "NULL";
			}
			else
				lastTokenIDString = value;
		}
		else if (aname == "AT_LINE_START")
		{
			lastAtLineStart = (isSpecified) ? (value.equals("TRUE")) :
				false;
		}
		else if (aname == "NO_LINE_BREAK")
		{
			lastNoLineBreak = (isSpecified) ? (value.equals("TRUE")) :
				false;
		}
		else if (aname == "NO_WORD_BREAK")
		{
			lastNoWordBreak = (isSpecified) ? (value.equals("TRUE")) :
				false;
		}
		else if (aname == "EXCLUDE_MATCH")
		{
			lastExcludeMatch = (isSpecified) ? (value.equals("TRUE")) :
				false;
		}
		else if (aname == "IGNORE_CASE")
		{
			lastIgnoreCase = (isSpecified) ? (value.equals("TRUE")) :
				true;
		}
		else if (aname == "HIGHLIGHT_DIGITS")
		{
			lastHighlightDigits = (isSpecified) ? (value.equals("TRUE")) :
				false;
		}
		else if (aname == "DIGIT_RE")
		{
			lastDigitRE = value;
		}
		else if (aname == "AT_CHAR")
		{
			try
			{
				if (isSpecified) termChar =
					Integer.parseInt(value);
			}
			catch (NumberFormatException e)
			{
				error("termchar-invalid",value);
				termChar = -1;
			}
		}
		else if (aname == "ESCAPE")
		{
			lastEscape = value;
		}
		else if (aname == "SET")
		{
			lastSetName = value;
		}
		else if (aname == "DELEGATE")
		{
			lastDelegateSet = value;
		}
		else if (aname == "DEFAULT")
		{
			lastDefaultID = Token.stringToToken(value);
			if(lastDefaultID == -1)
			{
				error("token-invalid",value);
				lastDefaultID = Token.NULL;
			}
		}
	} //}}}

	//{{{ doctypeDecl() method
	public void doctypeDecl(String name, String publicId,
		String systemId) throws Exception
	{
		if ("MODE".equalsIgnoreCase(name)) return;

		error("doctype-invalid",name);
	} //}}}

	//{{{ charData() method
	public void charData(char[] c, int off, int len)
	{
		String tag = peekElement();
		String text = new String(c, off, len);

		if (tag == "EOL_SPAN" ||
			tag == "MARK_PREVIOUS" ||
			tag == "MARK_FOLLOWING" ||
			tag == "SEQ" ||
			tag == "BEGIN"
		)
		{
			lastStart = text;
		}
		else if (tag == "END")
		{
			lastEnd = text;
		}
		else
		{
			lastKeyword = text;
		}
	} //}}}

	//{{{ startElement() method
	public void startElement (String tag)
	{
		tag = pushElement(tag);

		if (tag == "WHITESPACE")
		{
			Log.log(Log.WARNING,this,path + ": WHITESPACE rule "
				+ "no longer needed");
		}
		else if (tag == "MODE")
		{
			mode = jEdit.getMode(modeName);
			if (mode == null)
			{
				mode = new Mode(modeName);
				jEdit.addMode(mode);
			}
		}
		else if (tag == "KEYWORDS")
		{
			keywords = new KeywordMap(true);
		}
		else if (tag == "RULES")
		{
			rules = new ParserRuleSet(lastSetName,mode);
			rules.setIgnoreCase(lastIgnoreCase);
			rules.setHighlightDigits(lastHighlightDigits);
			if(lastDigitRE != null)
			{
				try
				{
					rules.setDigitRegexp(new RE(lastDigitRE,
						lastIgnoreCase
						? RE.REG_ICASE : 0,
						RESearchMatcher.RE_SYNTAX_JEDIT));
				}
				catch(REException e)
				{
					error("regexp",e);
				}
			}

			if(lastEscape != null)
				rules.addRule(ParserRuleFactory.createEscapeRule(lastEscape));
			rules.setDefault(lastDefaultID);
		}
	} //}}}

	//{{{ endElement() method
	public void endElement (String name)
	{
		if (name == null) return;

		String tag = popElement();

		if (name.equalsIgnoreCase(tag))
		{
			//{{{ MODE
			if (tag == "MODE")
			{
				mode.init();
				mode.setTokenMarker(marker);
			} //}}}
			//{{{ PROPERTY
			else if (tag == "PROPERTY")
			{
				props.put(propName,propValue);
			} //}}}
			//{{{ PROPS
			else if (tag == "PROPS")
			{
				if(peekElement().equals("RULES"))
					rules.setProperties(props);
				else
					mode.setProperties(props);

				props = new Hashtable();
			} //}}}
			//{{{ KEYWORDS
			else if (tag == "KEYWORDS")
			{
				keywords.setIgnoreCase(lastIgnoreCase);
				lastIgnoreCase = true;
			} //}}}
			//{{{ RULES
			else if (tag == "RULES")
			{
				rules.setKeywords(keywords);
				marker.addRuleSet(lastSetName, rules);
				keywords = null;
				lastSetName = null;
				lastEscape = null;
				lastIgnoreCase = true;
				lastHighlightDigits = false;
				lastDigitRE = null;
				lastDefaultID = Token.NULL;
				rules = null;
			} //}}}
			//{{{ TERMINATE
			else if (tag == "TERMINATE")
			{
				rules.setTerminateChar(termChar);
				termChar = -1;
			} //}}}
			//{{{ EOL_SPAN
			else if (tag == "EOL_SPAN")
			{
				if(lastStart == null)
				{
					error("empty-tag","EOL_SPAN");
					return;
				}

				rules.addRule(ParserRuleFactory.createEOLSpanRule(
					lastStart,lastTokenID,lastAtLineStart,
					lastExcludeMatch));
				lastStart = null;
				lastEnd = null;
				lastTokenID = Token.NULL;
				lastAtLineStart = false;
				lastExcludeMatch = false;
			} //}}}
			//{{{ MARK_PREVIOUS
			else if (tag == "MARK_PREVIOUS")
			{
				if(lastStart == null)
				{
					error("empty-tag","MARK_PREVIOUS");
					return;
				}

				rules.addRule(ParserRuleFactory
					.createMarkPreviousRule(lastStart,
					lastTokenID,lastAtLineStart,
					lastExcludeMatch));
				lastStart = null;
				lastEnd = null;
				lastTokenID = Token.NULL;
				lastAtLineStart = false;
				lastExcludeMatch = false;
			} //}}}
			//{{{ MARK_FOLLOWING
			else if (tag == "MARK_FOLLOWING")
			{
				if(lastStart == null)
				{
					error("empty-tag","MARK_FOLLOWING");
					return;
				}

				rules.addRule(ParserRuleFactory
					.createMarkFollowingRule(lastStart,
					lastTokenID,lastAtLineStart,
					lastExcludeMatch));
				lastStart = null;
				lastEnd = null;
				lastTokenID = Token.NULL;
				lastAtLineStart = false;
				lastExcludeMatch = false;
			} //}}}
			//{{{ SEQ
			else if (tag == "SEQ")
			{
				if(lastStart == null)
				{
					error("empty-tag","SEQ");
					return;
				}

				rules.addRule(ParserRuleFactory.createSequenceRule(
					lastStart,lastTokenID,lastAtLineStart));
				lastStart = null;
				lastEnd = null;
				lastTokenID = Token.NULL;
				lastAtLineStart = false;
			} //}}}
			//{{{ END
			else if (tag == "END")
			{
				// empty END tags should be supported; see
				// asp.xml, for example
				/* if(lastEnd == null)
				{
					error("empty-tag","END");
					return;
				} */

				if (lastDelegateSet == null)
				{
					rules.addRule(ParserRuleFactory
						.createSpanRule(lastStart,
						lastEnd,lastTokenIDString,
						lastTokenID,
						lastNoLineBreak,
						lastAtLineStart,
						lastExcludeMatch,
						lastNoWordBreak));
				}
				else
				{
					if (lastDelegateSet.indexOf("::") == -1)
					{
						lastDelegateSet = modeName + "::" + lastDelegateSet;
					}

					rules.addRule(ParserRuleFactory
						.createSpanRule(
						lastStart,lastEnd,
						lastDelegateSet,
						lastTokenID,lastNoLineBreak,
						lastAtLineStart,
						lastExcludeMatch,
						lastNoWordBreak));
				}
				lastStart = null;
				lastEnd = null;
				lastTokenID = Token.NULL;
				lastAtLineStart = false;
				lastNoLineBreak = false;
				lastExcludeMatch = false;
				lastNoWordBreak = false;
				lastDelegateSet = null;
			} //}}}
			//{{{ Keywords
			else if (tag == "NULL")
			{
				addKeyword(lastKeyword,Token.NULL);
			}
			else if (tag == "COMMENT1")
			{
				addKeyword(lastKeyword,Token.COMMENT1);
			}
			else if (tag == "COMMENT2")
			{
				addKeyword(lastKeyword,Token.COMMENT2);
			}
			else if (tag == "LITERAL1")
			{
				addKeyword(lastKeyword,Token.LITERAL1);
			}
			else if (tag == "LITERAL2")
			{
				addKeyword(lastKeyword,Token.LITERAL2);
			}
			else if (tag == "LABEL")
			{
				addKeyword(lastKeyword,Token.LABEL);
			}
			else if (tag == "KEYWORD1")
			{
				addKeyword(lastKeyword,Token.KEYWORD1);
			}
			else if (tag == "KEYWORD2")
			{
				addKeyword(lastKeyword,Token.KEYWORD2);
			}
			else if (tag == "KEYWORD3")
			{
				addKeyword(lastKeyword,Token.KEYWORD3);
			}
			else if (tag == "FUNCTION")
			{
				addKeyword(lastKeyword,Token.FUNCTION);
			}
			else if (tag == "MARKUP")
			{
				addKeyword(lastKeyword,Token.MARKUP);
			}
			else if (tag == "OPERATOR")
			{
				addKeyword(lastKeyword,Token.OPERATOR);
			}
			else if (tag == "DIGIT")
			{
				addKeyword(lastKeyword,Token.DIGIT);
			} //}}}
		}
		else
		{
			// can't happen
			throw new InternalError();
		}
	} //}}}

	//{{{ startDocument() method
	public void startDocument()
	{
		marker = new TokenMarker();
		marker.setName(modeName);
		props = new Hashtable();

		pushElement(null);
	} //}}}

	//{{{ Private members

	//{{{ Instance variables
	private XmlParser parser;
	private String modeName;
	private String path;

	private TokenMarker marker;
	private KeywordMap keywords;
	private Mode mode;
	private Stack stateStack;
	private String propName;
	private String propValue;
	private Hashtable props;
	private String lastStart;
	private String lastEnd;
	private String lastKeyword;
	private String lastSetName;
	private String lastEscape;
	private String lastDelegateSet;
	private ParserRuleSet rules;
	private byte lastDefaultID = Token.NULL;
	private byte lastTokenID;
	private String lastTokenIDString;
	private int termChar = -1;
	private boolean lastNoLineBreak;
	private boolean lastNoWordBreak;
	private boolean lastAtLineStart;
	private boolean lastExcludeMatch;
	private boolean lastIgnoreCase = true;
	private boolean lastHighlightDigits;
	private String lastDigitRE;
	//}}}

	//{{{ addKeyword() method
	private void addKeyword(String k, byte id)
	{
		if(k == null)
		{
			error("empty-keyword");
			return;
		}

		if (keywords == null) return;
		keywords.add(k,id);
	} //}}}

	//{{{ pushElement() method
	private String pushElement(String name)
	{
		name = (name == null) ? null : name.intern();

		stateStack.push(name);

		return name;
	} //}}}

	//{{{ peekElement() method
	private String peekElement()
	{
		return (String) stateStack.peek();
	} //}}}

	//{{{ popElement() method
	private String popElement()
	{
		return (String) stateStack.pop();
	} //}}}

	//{{{ error() method
	private void error(String msg)
	{
		_error(jEdit.getProperty("xmode-error." + msg));
	} //}}}

	//{{{ error() method
	private void error(String msg, String subst)
	{
		_error(jEdit.getProperty("xmode-error." + msg,new String[] { subst }));
	} //}}}

	//{{{ error() method
	private void error(String msg, Throwable t)
	{
		_error(jEdit.getProperty("xmode-error." + msg,new String[] { t.toString() }));
		Log.log(Log.ERROR,this,t);
	} //}}}

	//{{{ _error() method
	private void _error(String msg)
	{
		Object[] args = { path, new Integer(parser.getLineNumber()),
			new Integer(parser.getColumnNumber()), msg };

		GUIUtilities.error(null,"xmode-error",args);
	} //}}}

	//}}}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5375.java