error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6226.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6226.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6226.java
text:
```scala
public v@@oid logout() throws IOException, POP3Exception {

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.pop3;

import org.columba.core.command.CommandCancelledException;
import org.columba.core.command.StatusObservable;
import org.columba.core.command.StatusObservableImpl;
import org.columba.core.gui.util.NotifyDialog;
import org.columba.core.logging.ColumbaLogger;
import org.columba.core.main.MainInterface;
import org.columba.core.plugin.PluginHandlerNotFoundException;
import org.columba.core.xml.XmlElement;

import org.columba.mail.config.PopItem;
import org.columba.mail.gui.util.PasswordDialog;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.ColumbaMessage;
import org.columba.mail.plugin.POP3PreProcessingFilterPluginHandler;
import org.columba.mail.pop3.plugins.AbstractPOP3PreProcessingFilter;
import org.columba.mail.util.MailResourceLoader;

import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.io.Source;
import org.columba.ristretto.parser.HeaderParser;
import org.columba.ristretto.pop3.protocol.POP3Exception;
import org.columba.ristretto.pop3.protocol.POP3Protocol;
import org.columba.ristretto.pop3.protocol.ScanListEntry;
import org.columba.ristretto.pop3.protocol.UidListEntry;

import java.io.IOException;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;


/**
 * @author freddy
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates. To enable and disable the creation of
 * type comments go to Window>Preferences>Java>Code Generation.
 */
public class POP3Store {
    public static final int STATE_NONAUTHENTICATE = 0;
    public static final int STATE_AUTHENTICATE = 1;
    private static final int USER = 0;
    private static final int APOP = 1;
    private static final int AUTH = 2;
    private int state = STATE_NONAUTHENTICATE;
    private POP3Protocol protocol;
    private PopItem popItem;
    private POP3PreProcessingFilterPluginHandler handler;
    private Hashtable filterCache;
    private StatusObservableImpl observable;
    private UidListEntry[] uidMap;
    private ScanListEntry[] sizes;
    private String[] capas;
    private boolean usingSSL;

    /**
     * Constructor for POP3Store.
     */
    public POP3Store(PopItem popItem) {
        super();
        this.popItem = popItem;

        protocol = new POP3Protocol(popItem.get("host"),
                popItem.getInteger("port"));

        // add status information observable
        observable = new StatusObservableImpl();
        protocol.registerInterest(observable);

        try {
            handler = (POP3PreProcessingFilterPluginHandler) MainInterface.pluginManager.getHandler(
                    "org.columba.mail.pop3preprocessingfilter");
        } catch (PluginHandlerNotFoundException ex) {
            NotifyDialog d = new NotifyDialog();
            d.showDialog(ex);
        }

        filterCache = new Hashtable();

        usingSSL = false;
    }

    /**
     * Returns the state.
     *
     * @return int
     */
    public int getState() {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state
     *            The state to set
     */
    public void setState(int state) {
        this.state = state;
    }

    public List getUIDList() throws Exception {
        isLogin();

        uidMap = protocol.uidl();

        //Delete the old sizes
        sizes = null;

        LinkedList list = new LinkedList();

        for (int i = 0; i < uidMap.length; i++) {
            list.add(uidMap[i].getUid());
        }

        return list;
    }

    public int getSize(Object uid) throws Exception {
        int index = getIndex(uid);

        fetchMessageSizeList();

        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i].getIndex() == index) {
                return sizes[i].getSize();
            }
        }

        return protocol.list(index).getSize();
    }

    protected void fetchMessageSizeList() throws Exception {
        if (sizes == null) {
            isLogin();
            sizes = protocol.list();
        }
    }

    public int getMessageCount() throws Exception {
        isLogin();

        int[] stat = protocol.stat();

        return stat[0];
    }

    public boolean deleteMessage(Object uid) throws Exception {
        isLogin();

        return protocol.dele(getIndex(uid));
    }

    /**
     *
     * load the preprocessing filter pipe on message source
     *
     * @param rawString
     *            messagesource
     * @return modified messagesource
     */
    protected String modifyMessage(String rawString) {
        // pre-processing filter-pipe
        // configuration example (/accountlist/<my-example-account>/popserver):
        //
        //		<pop3preprocessingfilterlist>
        //		  <pop3preprocessingfilter name="myFilter"
        // class="myPackage.MyFilter"/>
        //        <pop3preprocessingfilter name="mySecondFilter"
        // class="myPackage.MySecondFilter"/>
        //		</pop3preprocessingfilterlist>
        //
        XmlElement listElement = popItem.getElement(
                "pop3preprocessingfilterlist");

        if (listElement == null) {
            return rawString;
        }

        // go through all filters and apply them to the
        // rawString variable
        for (int i = 0; i < listElement.count(); i++) {
            XmlElement rootElement = listElement.getElement(i);
            String type = rootElement.getAttribute("name");

            Object[] args = { rootElement };

            AbstractPOP3PreProcessingFilter filter = null;

            try {
                //		try to re-use already instanciated class
                if (filterCache.containsKey(type) == true) {
                    ColumbaLogger.log.info("re-using cached instanciation =" +
                        type);
                    filter = (AbstractPOP3PreProcessingFilter) filterCache.get(type);
                } else {
                    filter = (AbstractPOP3PreProcessingFilter) handler.getPlugin(type,
                            args);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (filter != null) {
                // Filter was loaded correctly
                //  -> apply filter --> modify messagesource
                ColumbaLogger.log.info("applying pop3 filter..");

                if (filter != null) {
                    filterCache.put(type, filter);
                }

                rawString = filter.modify(rawString);

                ColumbaLogger.log.info("rawString=" + rawString);
            }
        }

        return rawString;
    }

    protected int getIndex(Object uid)
        throws IOException, POP3Exception, CommandCancelledException {
        for (int i = 0; i < uidMap.length; i++) {
            if (uidMap[i].getUid().equals(uid)) {
                return uidMap[i].getIndex();
            }
        }

        throw new POP3Exception("No message with uid " + uid + " on server.");
    }

    public ColumbaMessage fetchMessage(Object uid) throws Exception {
        isLogin();

        Source source = protocol.retr(getIndex(uid));

        // pipe through preprocessing filter
        //if (popItem.getBoolean("enable_pop3preprocessingfilter", false))
        //	rawString = modifyMessage(rawString);
        //TODO: Activate PreProcessor again with Source instead of String
        Header header = HeaderParser.parse(source);

        ColumbaMessage m = new ColumbaMessage(header);
        ColumbaHeader h = (ColumbaHeader) m.getHeader();

        m.setSource(source);
        h.getAttributes().put("columba.pop3uid", uid);
        h.getAttributes().put("columba.size",
            new Integer(source.length() / 1024));

        //h.set("columba.pop3uid", (String) uids.get(number - 1));
        return m;
    }

    public void logout() throws Exception {
        protocol.quit();

        uidMap = null;
        sizes = null;

        state = STATE_NONAUTHENTICATE;
    }

    protected void fetchCapas() throws IOException {
        try {
            capas = protocol.capa();
        } catch (POP3Exception e) {
            // CAPA not supported
            capas = new String[] {};
        }
    }

    protected boolean isSupported(String command) {
        for (int i = 0; i < capas.length; i++) {
            if (capas[i].startsWith(command)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Try to login a user to a given pop3 server. While the login is not
     * succeed, the connection to the server is opened, then a dialog box is
     * coming up, then the user should fill in his password. The username and
     * password is sended to the pop3 server. Then we recive a answer. Is the
     * answer is false, we try to logout from the server and closing the
     * connection. The we begin again with open connection, showing dialog and
     * so on.
     *
     * @param worker
     *            used for cancel button
     * @throws Exception
     *
     * Bug number 619290 fixed.
     */
    public void login()
        throws IOException, POP3Exception, CommandCancelledException {
        PasswordDialog dialog;
        boolean login = false;

        String password = null;
        String user = new String("");
        String method = new String("");
        boolean save = false;

        // open a port to the server
        protocol.openPort();

        // fetch the capabilities of this server
        fetchCapas();

        // shall we switch to SSL?
        if (popItem.getBoolean("enable_ssl")) {
        	// if CAPA was not support just give it a try...
            if (isSupported("STLS") || capas.length == 0) {
                try {
                    protocol.switchToSSL();

                    usingSSL = true;
                    ColumbaLogger.log.info("Switched to SSL");
                } catch (IOException e) {
                    Object[] options = new String[] {
                            MailResourceLoader.getString("", "global", "ok").replaceAll("&",""),
                            MailResourceLoader.getString("", "global", "cancel").replaceAll("&","")
                        };

                    int result = JOptionPane.showOptionDialog(null,
                            MailResourceLoader.getString("dialog", "error",
                                "ssl_handshake_error") + ": " +
                            e.getLocalizedMessage() + "\n" +
                            MailResourceLoader.getString("dialog", "error",
                                "ssl_turn_off"), "Warning",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE, null, options,
                            options[0]);

                    if (result == 1) {
                        throw new CommandCancelledException();
                    }

                    // turn off SSL for the future
                    popItem.set("enable_ssl", false);

                    // reopen the port
                    protocol.openPort();
                } catch (POP3Exception e ) {
					Object[] options = new String[] {
							MailResourceLoader.getString("", "global", "ok").replaceAll("&",""),
							MailResourceLoader.getString("", "global", "cancel").replaceAll("&","")
						};
					int result = JOptionPane.showOptionDialog(null,
							MailResourceLoader.getString("dialog", "error",
								"ssl_not_supported") + "\n" +
								MailResourceLoader.getString("dialog", "error",
									"ssl_turn_off"), "Warning",
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE, null, options, options[0]);

					if (result == 1) {
						throw new CommandCancelledException();
					}

					// turn off SSL for the future
					popItem.set("enable_ssl", false);                	
                }
            } else {
				Object[] options = new String[] {
						MailResourceLoader.getString("", "global", "ok").replaceAll("&",""),
						MailResourceLoader.getString("", "global", "cancel").replaceAll("&","")
					};
                int result = JOptionPane.showOptionDialog(null,
                        MailResourceLoader.getString("dialog", "error",
                            "ssl_not_supported") + "\n" +
                            MailResourceLoader.getString("dialog", "error",
                                "ssl_turn_off"), "Warning",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE, null, options, options[0]);

                if (result == 1) {
                    throw new CommandCancelledException();
                }

                // turn off SSL for the future
                popItem.set("enable_ssl", false);
            }
        }

        int loginMethod = getLoginMethod();

        while (!login) {
            if ((password = popItem.get("password")).length() == 0) {
                dialog = new PasswordDialog();

                dialog.showDialog(popItem.get("user"), popItem.get("host"),
                    popItem.get("password"), popItem.getBoolean("save_password"));

                if (dialog.success()) {
                    // ok pressed
                    password = new String(dialog.getPassword());

                    save = dialog.getSave();
                } else {
                    // cancel pressed
                    protocol.cancelCurrent();

                    throw new CommandCancelledException();
                }
            } else {
                save = popItem.getBoolean("save_password");
            }

            try {
                switch (loginMethod) {
                case USER: {
                    login = protocol.userPass(popItem.get("user"), password);

                    break;
                }

                case APOP: {
                    login = protocol.apop(popItem.get("user"), password);

                    break;
                }
                }
            } catch (POP3Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Authorization failed!", JOptionPane.ERROR_MESSAGE);

                popItem.set("password", "");
                state = STATE_NONAUTHENTICATE;
            }

            ColumbaLogger.log.info("login=" + login);
        }

        popItem.set("save_password", save);

        state = STATE_AUTHENTICATE;

        if (save) {
            // save plain text password in config file
            // this is a security risk !!!
            popItem.set("password", password);
        }
    }

    /**
     * Gets the selected Authentication method or else the most secure.
     * @return the authentication method
     */
    private int getLoginMethod() {
        String loginMethod = popItem.get("login_method");

        if (loginMethod.equals("USER")) {
            return USER;
        }

        if (loginMethod.equals("APOP")) {
            return APOP;
        }

        // else find the most secure method
		// NOTE if SSL is possible we just need the plain login
		// since SSL does the encryption for us.        
        if (!usingSSL) {
        	//TODO add AUTH support
        	
            if (isSupported("APOP")) {
                return APOP;
            }
        }

        return USER;
    }

    public boolean isLogin()
        throws IOException, POP3Exception, CommandCancelledException {
        if (state == STATE_AUTHENTICATE) {
            return true;
        } else {
            login();

            return false;
        }
    }

    public StatusObservable getObservable() {
        return observable;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6226.java