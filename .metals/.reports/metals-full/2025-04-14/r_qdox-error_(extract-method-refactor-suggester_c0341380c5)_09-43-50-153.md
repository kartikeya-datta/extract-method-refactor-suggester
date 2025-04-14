error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3129.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3129.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3129.java
text:
```scala
e@@xtList = DiskIO.getResourceStream( "org/columba/mail/composer/ext2mime.xml" );

// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

package org.columba.mail.composer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.columba.core.io.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class MimeTypeLookup
{

	private Hashtable extTable;
	

	public MimeTypeLookup ()
    {
		extTable = new Hashtable();
		loadExtensionList();
	}

	private boolean loadExtensionList ()
    {
		InputStream extList;

		// Get InputStream of ext2mime
	    try	{
	    extList = DiskIO.getResourceStream( "org/columba/modules/mail/composer/ext2mime.xml" );
		}
		catch (IOException e)
			{
			System.err.println( e );
			return false;
			}

		// Parse InputStream

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document;

        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();

            builder.setErrorHandler
                (
                    new org.xml.sax.ErrorHandler()
                        {

                            public void fatalError(SAXParseException exception)
                                throws SAXException
                            {
                            }


                            public void error (SAXParseException e)
                                throws SAXParseException
                            {
                                throw e;
                            }


                            public void warning (SAXParseException err)
                                throws SAXParseException
                            {
                                System.out.println ("** Warning"
                                                    + ", line " + err.getLineNumber ()
                                                    + ", uri " + err.getSystemId ());
                                System.out.println("   " + err.getMessage ());
                            }
                        }
                    );

            document = builder.parse( extList );

            // Write Extensions in Hashtable for faster lookup

			Node mimeNode, extNode;
			NodeList rootNodes = document.getFirstChild().getChildNodes();
			NodeList mimeNodes;
			String mimeTypeName;
			String ext;

			for( int i=0; i<rootNodes.getLength(); i++ ) {
				mimeTypeName = null;
				mimeNode = rootNodes.item(i);

				if( mimeNode.getNodeName().equals("mime-type") ) {
					mimeNodes = mimeNode.getChildNodes();

					// Get MimeType name first

					for( int j=0; j<mimeNodes.getLength(); j++ ) {
						if( mimeNodes.item(j).getNodeName().equals("name") ) {
							mimeTypeName = mimeNodes.item(j).getFirstChild().toString();
							break;
						}
					}

					if( mimeTypeName == null ) break;

					// Put extensions in extTable

					for( int j=0; j<mimeNodes.getLength(); j++ ) {
						if( mimeNodes.item(j).getNodeName().equals("ext") ) {
							ext = mimeNodes.item(j).getFirstChild().toString();
							extTable.put( ext, mimeTypeName );
						}
					}
				}
			}

        }

	catch (SAXParseException spe)
        {
            System.out.println ("\n** Parsing error"
                                + ", line " + spe.getLineNumber ()
                                + ", uri " + spe.getSystemId ());
            System.out.println("   " + spe.getMessage() );

            Exception  x = spe;
            if (spe.getException() != null)
                x = spe.getException();
            x.printStackTrace();

        } catch (SAXException sxe)
        {
            Exception  x = sxe;
            if (sxe.getException() != null)
		    x = sxe.getException();
            x.printStackTrace();

        } catch (ParserConfigurationException pce)
        {
                pce.printStackTrace();
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }


		return true;
	}

	public String getMimeType( String ext )
    {
    	if ( ext == null ) return "application/octet-stream";
    	
	   if( extTable.containsKey( ext ) )
           return (String) extTable.get( ext );

	   else return new String( "application/octet-stream" );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3129.java