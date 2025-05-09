error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7784.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7784.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7784.java
text:
```scala
i@@f (bundle == null && sBundlePath != null) {

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.
/*
	columba: a Java open source email client
	http://columba.sourceforge.net/

	Filename: GlobalResourceLoader.java
	Author: Hrk (Luca Santarelli) <hrk@users.sourceforge.net>
	Comments: this is the core class to handle i18n in columba, loading, handling and returning localized strings.
	It should not be used directly, use MailResourceLoader or AddressbookResourceLoader (or *ResourceLoader) instead.
*/

package org.columba.core.util;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

import org.columba.core.config.Config;
import org.columba.core.config.ConfigPath;
import org.columba.core.logging.ColumbaLogger;
import org.columba.core.main.MainInterface;
import org.columba.core.xml.XmlElement;

/*
	Behaviour.
	When a resource is needed, getString() or getMnemonics() are called. They look for a resource with that name (in the current locale bundles).
	If it is not found, they look for the resource in the global resource bundle (for the current locale). If this is not found, "FIXME" is returned.

	Example of usage: We need to get the text for "my_cool_button" located into "org/columba/modules/mail/i18n/action/something_else_than_action"
	sPath: org/columba/modules/mail/i18n/action/ => The complete package path.
	sName: something_else_than_action => the name of the _it_IT.properties file.
	sID: my_cool_button => the name to be looked for inside sName file.
	We can call:
	a) MailResourceLoader.getString("action", "something_else_than_action", "my_cool_button");
	b) ResourceLoader.getString("org/columba/modules/mail/i18n/action", "something_else_than_action", "my_cool_button");
	They'll both work.

	We need to gets its mnemonic: 
	a) MailResourceLoader.getMnemonic("action", "something_else_than_action", "my_cool_button");
	b) ResourceLoader.getMnemonic("org/columba/modules/mail/i18n/action", "something_else_than_action", "my_cool_button");
*/
public class GlobalResourceLoader {

	protected static ClassLoader classLoader;
	protected static Hashtable htBundles = new Hashtable(80);
	protected static ResourceBundle globalBundle;
	protected static final String FIX_ME = "FIX ME!";
	private static final String GLOBAL_BUNDLE_PATH =
		"org.columba.core.i18n.global.global";

	static {
		XmlElement locale = Config.get("options").getElement("/options/locale");

		// no configuration available, create default config
		if (locale == null) {
			// create new locale xml treenode
			locale = new XmlElement("locale");
			locale.addAttribute("language", "en");
			Config.get("options").getElement("/options").addElement(locale);

		}

		String language = locale.getAttribute("language");
		String country = locale.getAttribute("country", "");
		String variant = locale.getAttribute("variant", "");
		Locale.setDefault(new Locale(language, country, variant));
		initClassLoader();
		try {
			// use ResourceBundle's internal classloader
			if (classLoader == null)
				globalBundle =
					ResourceBundle.getBundle(
						GLOBAL_BUNDLE_PATH,
						Locale.getDefault());
			else
				globalBundle =
					ResourceBundle.getBundle(
						GLOBAL_BUNDLE_PATH,
						Locale.getDefault(),
						classLoader);
		} catch (MissingResourceException mre) {

			throw new RuntimeException("Global resource bundle not found, Columba cannot start.");

		}
	}

	public static Locale[] getAvailableLocales() {
		Set locales = new HashSet();
		locales.add(new Locale("en", ""));
		FileFilter langpackFileFilter = new LangPackFileFilter();
		File[] langpacks =
			ConfigPath.getConfigDirectory().listFiles(langpackFileFilter);
		for (int i = 0; i < langpacks.length; i++) {
			locales.add(extractLocaleFromFilename(langpacks[i].getName()));
		}
		langpacks = new File(".").listFiles(langpackFileFilter);
		for (int i = 0; i < langpacks.length; i++) {
			locales.add(extractLocaleFromFilename(langpacks[i].getName()));
		}
		return (Locale[]) locales.toArray(new Locale[0]);
	}

	private static Locale extractLocaleFromFilename(String name) {
		String language = "";
		String country = "";
		String variant = "";
		name = name.substring(9, name.length() - 4);
		StringTokenizer tokenizer = new StringTokenizer(name, "_");
		if (tokenizer.hasMoreElements()) {
			language = tokenizer.nextToken();
			if (tokenizer.hasMoreElements()) {
				country = tokenizer.nextToken();
				if (tokenizer.hasMoreElements()) {
					variant = tokenizer.nextToken();
				}
			}
		}
		return new Locale(language, country, variant);
	}

	protected static void initClassLoader() {
		String name = "langpack_" + Locale.getDefault().toString() + ".jar";
		File langpack = new File(ConfigPath.getConfigDirectory(), name);
		if (!langpack.exists() || !langpack.isFile()) {
			langpack = new File(".", name);
		}
		if (langpack.exists() && langpack.isFile()) {
			if (MainInterface.DEBUG) {
				ColumbaLogger.log.info(
					"Creating new i18n class loader for " + langpack.getPath());
			}
			try {
				classLoader = new URLClassLoader(new URL[] { langpack.toURL()});
			} catch (MalformedURLException mue) {
			} //does not occur
		} else {
			if (MainInterface.DEBUG) {
				ColumbaLogger.log.error(
					"No language pack found for "
						+ Locale.getDefault().toString());
			}

			// we can't use SystemClassLoader here, because that
			// wouldn't work with java webstart,
			// ResourceBundle uses its own internal classloader
			// if no classloader is given
			//  -> set classloader = null

			/*
			classLoader = ClassLoader.getSystemClassLoader();
			*/

			classLoader = null;

		}
	}

	protected static String generateBundlePath(String sPath, String sName) {
		return sPath + "." + sName;
	}

	/*
		This method returns the translation for the given string identifier. If no translation is found, the default english item is used.
		Should this fail too, a "Fix me!" (private final static String FIXME) string will be returned.
	
		Example usage call: getString("org/columba/modules/mail/i18n/", "dialog", "close")
		We'll look for "close" in "org/columba/modules/mail/i18n/dialog/dialog_locale_LOCALE.properties"
		Thus:
		sPath: "org/columba/modules/mail/i18n/dialog"
		sName: "dialog"
		sID: "close"
		The bundle name will be: "org/columba/modules/mail/i18n/dialog/dialog"
	
		Hypotetically this method should not be available to classes different from *ResourceLoader (example: MailResourceLoader, AddressbookResourceLoader); this means that *ResourceLoader classes *do know* how to call this method.
	*/
	public static String getString(String sPath, String sName, String sID) {
		if (sID == null || sID.equals(""))
			return null;
		ResourceBundle bundle = null;
                String sBundlePath = null;
                if (sPath != null && !sPath.equals("")) {
                        //Find out if we already loaded the needed ResourceBundle
                        //object in the hashtable.
                        sBundlePath = generateBundlePath(sPath, sName);
                        bundle = (ResourceBundle) htBundles.get(sBundlePath);
                }
		if (bundle == null) {
			try {
				// use ResourceBundle's internal classloader
				if (classLoader == null)
					bundle =
						ResourceBundle.getBundle(
							sBundlePath,
							Locale.getDefault());
				else
					bundle =
						ResourceBundle.getBundle(
							sBundlePath,
							Locale.getDefault(),
							classLoader);
				htBundles.put(sBundlePath, bundle);
			} catch (MissingResourceException mre) {
			}
		}
		if (bundle != null) {
			try {
				return bundle.getString(sID);
			} catch (MissingResourceException mre) {
			}
		}
		try {
			return globalBundle.getString(sID);
		} catch (MissingResourceException mre) {
			if (MainInterface.DEBUG) {
				ColumbaLogger.log.error(
					"'"
						+ sID
						+ "' in '"
						+ sBundlePath
						+ "' could not be found.");
			}
			
			/*
			 * Replaced FIX_ME string with resource ID here, this makes it
			 * easier to track misspelled sID.
			 * 
			 * Also its makes the action plugins extending the menu work
			 * without using a translation infrastructure.
			 * 
			 * This infrastructure will be added in the next few weeks,
			 * then this isn't needed anymore.
			 * 
			 */
			 
			 return sID;
			//return FIX_ME;
		}
	}

	public static char getMnemonic(String sPath, String sName, String sID) {
		/*
			Example: MailResourceLoader.getMnemonic("dialog", "filter", "chose_folder");
			MailResourceLoader changes path to "org/columba/modules/mail/i18n/dialog", then submits the search.
		*/
		String sResult = getString(sPath, sName, sID + "_mnemonic");
		if (sResult != null && sResult != FIX_ME) { //String was found.
			return sResult.charAt(0);
		} else {
			return 0;
		}
	}

	public static void reload() {
		initClassLoader();
		if (MainInterface.DEBUG) {
			ColumbaLogger.log.info(
				"Reloading cached resource bundles for locale "
					+ Locale.getDefault().toString());
		}
		try {
			// use ResourceBundle's internal classloader
			if (classLoader == null)
				globalBundle =
					ResourceBundle.getBundle(
						GLOBAL_BUNDLE_PATH,
						Locale.getDefault());
			else
				globalBundle =
					ResourceBundle.getBundle(
						GLOBAL_BUNDLE_PATH,
						Locale.getDefault(),
						classLoader);
		} catch (MissingResourceException mre) {
		} //should not occur, otherwise the static initializer should have thrown a RuntimeException

		String bundlePath;
		ResourceBundle bundle;
		for (Enumeration entries = htBundles.keys();
			entries.hasMoreElements();
			) {
			try {
				bundlePath = (String) entries.nextElement();

				//retrieve new bundle

				// use ResourceBundle's internal classloader
				if (classLoader == null)
					bundle =
						ResourceBundle.getBundle(
							bundlePath,
							Locale.getDefault());
				else
					bundle =
						ResourceBundle.getBundle(
							bundlePath,
							Locale.getDefault(),
							classLoader);

				//overwrite old bundle
				htBundles.put(bundlePath, bundle);
			} catch (MissingResourceException mre) {
			} //should not occur, otherwise the bundlePath would not be in the hashtable
		}
	}

	public static class LangPackFileFilter implements FileFilter {
		public boolean accept(File file) {
			String name = file.getName().toLowerCase();
			return file.isFile()
				&& name.startsWith("langpack_")
				&& name.endsWith(".jar");
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7784.java