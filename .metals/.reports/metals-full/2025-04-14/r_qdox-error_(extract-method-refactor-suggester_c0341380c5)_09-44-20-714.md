error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2774.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2774.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2774.java
text:
```scala
S@@tring s = getStringFromContext("fetchMailsSince", null);

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
package org.apache.solr.handler.dataimport;

import com.sun.mail.imap.IMAPMessage;

import org.apache.tika.Tika;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * An {@link EntityProcessor} instance which can index emails along with their attachments from POP3 or IMAP sources. Refer to
 * <a href="http://wiki.apache.org/solr/DataImportHandler">http://wiki.apache.org/solr/DataImportHandler</a> for more
 * details. <b>This API is experimental and subject to change</b>
 *
 *
 * @since solr 1.4
 */
public class MailEntityProcessor extends EntityProcessorBase {

  public static interface CustomFilter {
    public SearchTerm getCustomSearch(Folder folder);
  }

  @Override
  public void init(Context context) {
    super.init(context);
    // set attributes using  XXX getXXXFromContext(attribute, defualtValue);
    // applies variable resolver and return default if value is not found or null
    // REQUIRED : connection and folder info
    user = getStringFromContext("user", null);
    password = getStringFromContext("password", null);
    host = getStringFromContext("host", null);
    protocol = getStringFromContext("protocol", null);
    folderNames = getStringFromContext("folders", null);
    // validate
    if (host == null || protocol == null || user == null || password == null
 folderNames == null)
      throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
              "'user|password|protocol|host|folders' are required attributes");

    //OPTIONAL : have defaults and are optional
    recurse = getBoolFromContext("recurse", true);
    String excludes = getStringFromContext("exclude", "");
    if (excludes != null && !excludes.trim().equals("")) {
      exclude = Arrays.asList(excludes.split(","));
    }
    String includes = getStringFromContext("include", "");
    if (includes != null && !includes.trim().equals("")) {
      include = Arrays.asList(includes.split(","));
    }
    batchSize = getIntFromContext("batchSize", 20);
    customFilter = getStringFromContext("customFilter", "");
    String s = getStringFromContext("fetchMailsSince", "");
    if (s != null)
      try {
        fetchMailsSince = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
      } catch (ParseException e) {
        throw new DataImportHandlerException(DataImportHandlerException.SEVERE, "Invalid value for fetchMailSince: " + s, e);
      }

    fetchSize = getIntFromContext("fetchSize", 32 * 1024);
    cTimeout = getIntFromContext("connectTimeout", 30 * 1000);
    rTimeout = getIntFromContext("readTimeout", 60 * 1000);
    processAttachment = getBoolFromContext(
              getStringFromContext("processAttachment",null) == null ? "processAttachement":"processAttachment"
            , true);

    tika = new Tika();
    
    logConfig();
  }

  @Override
  public Map<String, Object> nextRow() {
    Message mail;
    Map<String, Object> row = null;
    do {
      // try till there is a valid document or folders get exhausted.
      // when mail == NULL, it means end of processing
      mail = getNextMail();
      if (mail != null)
        row = getDocumentFromMail(mail);
    } while (row == null && mail != null);    
    return row;
  }

  private Message getNextMail() {
    if (!connected) {
      if (!connectToMailBox())
        return null;
      connected = true;
    }
    if (folderIter == null) {
      createFilters();
      folderIter = new FolderIterator(mailbox);
    }
    // get next message from the folder
    // if folder is exhausted get next folder
    // loop till a valid mail or all folders exhausted.
    while (msgIter == null || !msgIter.hasNext()) {
      Folder next = folderIter.hasNext() ? folderIter.next() : null;
      if (next == null) {
        return null;
      }
      msgIter = new MessageIterator(next, batchSize);
    }
    return msgIter.next();
  }

  private Map<String, Object> getDocumentFromMail(Message mail) {
    Map<String, Object> row = new HashMap<String, Object>();
    try {
      addPartToDocument(mail, row, true);
      return row;
    } catch (Exception e) {
      return null;
    }
  }

  public void addPartToDocument(Part part, Map<String, Object> row, boolean outerMost) throws Exception {
    if (part instanceof Message) {
      addEnvelopToDocument(part, row);
    }

    String ct = part.getContentType();
    ContentType ctype = new ContentType(ct);
    if (part.isMimeType("multipart/*")) {
      Multipart mp = (Multipart) part.getContent();
      int count = mp.getCount();
      if (part.isMimeType("multipart/alternative"))
        count = 1;
      for (int i = 0; i < count; i++)
        addPartToDocument(mp.getBodyPart(i), row, false);
    } else if (part.isMimeType("message/rfc822")) {
      addPartToDocument((Part) part.getContent(), row, false);
    } else {
      String disp = part.getDisposition();
      if (!processAttachment || (disp != null && disp.equalsIgnoreCase(Part.ATTACHMENT)))        return;
      InputStream is = part.getInputStream();
      String fileName = part.getFileName();
      Metadata md = new Metadata();
      md.set(HttpHeaders.CONTENT_TYPE, ctype.getBaseType().toLowerCase(Locale.ENGLISH));
      md.set(TikaMetadataKeys.RESOURCE_NAME_KEY, fileName);
      String content = tika.parseToString(is, md);
      if (disp != null && disp.equalsIgnoreCase(Part.ATTACHMENT)) {
        if (row.get(ATTACHMENT) == null)
          row.put(ATTACHMENT, new ArrayList<String>());
        List<String> contents = (List<String>) row.get(ATTACHMENT);
        contents.add(content);
        row.put(ATTACHMENT, contents);
        if (row.get(ATTACHMENT_NAMES) == null)
          row.put(ATTACHMENT_NAMES, new ArrayList<String>());
        List<String> names = (List<String>) row.get(ATTACHMENT_NAMES);
        names.add(fileName);
        row.put(ATTACHMENT_NAMES, names);
      } else {
        if (row.get(CONTENT) == null)
          row.put(CONTENT, new ArrayList<String>());
        List<String> contents = (List<String>) row.get(CONTENT);
        contents.add(content);
        row.put(CONTENT, contents);
      }
    }
  }

  private void addEnvelopToDocument(Part part, Map<String, Object> row) throws MessagingException {
    MimeMessage mail = (MimeMessage) part;
    Address[] adresses;
    if ((adresses = mail.getFrom()) != null && adresses.length > 0)
      row.put(FROM, adresses[0].toString());

    List<String> to = new ArrayList<String>();
    if ((adresses = mail.getRecipients(Message.RecipientType.TO)) != null)
      addAddressToList(adresses, to);
    if ((adresses = mail.getRecipients(Message.RecipientType.CC)) != null)
      addAddressToList(adresses, to);
    if ((adresses = mail.getRecipients(Message.RecipientType.BCC)) != null)
      addAddressToList(adresses, to);
    if (to.size() > 0)
      row.put(TO_CC_BCC, to);

    row.put(MESSAGE_ID, mail.getMessageID());
    row.put(SUBJECT, mail.getSubject());

    Date d = mail.getSentDate();
    if (d != null) {
      row.put(SENT_DATE, d);
    }

    List<String> flags = new ArrayList<String>();
    for (Flags.Flag flag : mail.getFlags().getSystemFlags()) {
      if (flag == Flags.Flag.ANSWERED)
        flags.add(FLAG_ANSWERED);
      else if (flag == Flags.Flag.DELETED)
        flags.add(FLAG_DELETED);
      else if (flag == Flags.Flag.DRAFT)
        flags.add(FLAG_DRAFT);
      else if (flag == Flags.Flag.FLAGGED)
        flags.add(FLAG_FLAGGED);
      else if (flag == Flags.Flag.RECENT)
        flags.add(FLAG_RECENT);
      else if (flag == Flags.Flag.SEEN)
        flags.add(FLAG_SEEN);
    }
    flags.addAll(Arrays.asList(mail.getFlags().getUserFlags()));
    row.put(FLAGS, flags);

    String[] hdrs = mail.getHeader("X-Mailer");
    if (hdrs != null)
      row.put(XMAILER, hdrs[0]);
  }


  private void addAddressToList(Address[] adresses, List<String> to) throws AddressException {
    for (Address address : adresses) {
      to.add(address.toString());
      InternetAddress ia = (InternetAddress) address;
      if (ia.isGroup()) {
        InternetAddress[] group = ia.getGroup(false);
        for (InternetAddress member : group)
          to.add(member.toString());
      }
    }
  }

  private boolean connectToMailBox() {
    try {
      Properties props = new Properties();
      props.setProperty("mail.store.protocol", protocol);
      props.setProperty("mail.imap.fetchsize", "" + fetchSize);
      props.setProperty("mail.imap.timeout", "" + rTimeout);
      props.setProperty("mail.imap.connectiontimeout", "" + cTimeout);
      Session session = Session.getDefaultInstance(props, null);
      mailbox = session.getStore(protocol);
      mailbox.connect(host, user, password);
      LOG.info("Connected to mailbox");
      return true;
    } catch (MessagingException e) {
      throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
              "Connection failed", e);
    }
  }

  private void createFilters() {
    if (fetchMailsSince != null) {
      filters.add(new MailsSinceLastCheckFilter(fetchMailsSince));
    }
    if (customFilter != null && !customFilter.equals("")) {
      try {
        Class cf = Class.forName(customFilter);
        Object obj = cf.newInstance();
        if (obj instanceof CustomFilter) {
          filters.add((CustomFilter) obj);
        }
      } catch (Exception e) {
        throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                "Custom filter could not be created", e);
      }
    }
  }

  private void logConfig() {
    if (!LOG.isInfoEnabled()) return;
    StringBuffer config = new StringBuffer();
    config.append("user : ").append(user).append(System.getProperty("line.separator"));
    config.append("pwd : ").append(password).append(System.getProperty("line.separator"));
    config.append("protocol : ").append(protocol).append(System.getProperty("line.separator"));
    config.append("host : ").append(host).append(System.getProperty("line.separator"));
    config.append("folders : ").append(folderNames).append(System.getProperty("line.separator"));
    config.append("recurse : ").append(recurse).append(System.getProperty("line.separator"));
    config.append("exclude : ").append(exclude.toString()).append(System.getProperty("line.separator"));
    config.append("include : ").append(include.toString()).append(System.getProperty("line.separator"));
    config.append("batchSize : ").append(batchSize).append(System.getProperty("line.separator"));
    config.append("fetchSize : ").append(fetchSize).append(System.getProperty("line.separator"));
    config.append("read timeout : ").append(rTimeout).append(System.getProperty("line.separator"));
    config.append("conection timeout : ").append(cTimeout).append(System.getProperty("line.separator"));
    config.append("custom filter : ").append(customFilter).append(System.getProperty("line.separator"));
    config.append("fetch mail since : ").append(fetchMailsSince).append(System.getProperty("line.separator"));
    LOG.info(config.toString());
  }

  class FolderIterator implements Iterator<Folder> {
    private Store mailbox;
    private List<String> topLevelFolders;
    private List<Folder> folders = null;
    private Folder lastFolder = null;

    public FolderIterator(Store mailBox) {
      this.mailbox = mailBox;
      folders = new ArrayList<Folder>();
      getTopLevelFolders(mailBox);
    }

    public boolean hasNext() {
      return !folders.isEmpty();
    }

    public Folder next() {
      try {
        boolean hasMessages = false;
        Folder next;
        do {
          if (lastFolder != null) {
            lastFolder.close(false);
            lastFolder = null;
          }
          if (folders.isEmpty()) {
            mailbox.close();
            return null;
          }
          next = folders.remove(0);
          if (next != null) {
            String fullName = next.getFullName();
            if (!excludeFolder(fullName)) {
              hasMessages = (next.getType() & Folder.HOLDS_MESSAGES) != 0;
              next.open(Folder.READ_ONLY);
              lastFolder = next;
              LOG.info("Opened folder : " + fullName);
            }
            if (recurse && ((next.getType() & Folder.HOLDS_FOLDERS) != 0)) {
              Folder[] children = next.list();
              LOG.info("Added its children to list  : ");
              for (int i = children.length - 1; i >= 0; i--) {
                folders.add(0, children[i]);
                LOG.info("child name : " + children[i].getFullName());
              }
              if (children.length == 0)
                LOG.info("NO children : ");
            }
          }
        }
        while (!hasMessages);
        return next;
      } catch (MessagingException e) {
        //throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
        //        "Folder open failed", e);
      }
      return null;
    }

    public void remove() {
      throw new UnsupportedOperationException("Its read only mode...");
    }

    private void getTopLevelFolders(Store mailBox) {
      if (folderNames != null)
        topLevelFolders = Arrays.asList(folderNames.split(","));
      for (int i = 0; topLevelFolders != null && i < topLevelFolders.size(); i++) {
        try {
          folders.add(mailbox.getFolder(topLevelFolders.get(i)));
        } catch (MessagingException e) {
          // skip bad ones unless its the last one and still no good folder
          if (folders.size() == 0 && i == topLevelFolders.size() - 1)
            throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                    "Folder retreival failed");
        }
      }
      if (topLevelFolders == null || topLevelFolders.size() == 0) {
        try {
          folders.add(mailBox.getDefaultFolder());
        } catch (MessagingException e) {
          throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                  "Folder retreival failed");
        }
      }
    }

    private boolean excludeFolder(String name) {
      for (String s : exclude) {
        if (name.matches(s))
          return true;
      }
      for (String s : include) {
        if (name.matches(s))
          return false;
      }
      return include.size() > 0;
    }
  }

  class MessageIterator implements Iterator<Message> {
    private Folder folder;
    private Message[] messagesInCurBatch;
    private int current = 0;
    private int currentBatch = 0;
    private int batchSize = 0;
    private int totalInFolder = 0;
    private boolean doBatching = true;

    public MessageIterator(Folder folder, int batchSize) {
      try {
        this.folder = folder;
        this.batchSize = batchSize;
        SearchTerm st = getSearchTerm();
        if (st != null) {
          doBatching = false;
          messagesInCurBatch = folder.search(st);
          totalInFolder = messagesInCurBatch.length;
          folder.fetch(messagesInCurBatch, fp);
          current = 0;
          LOG.info("Total messages : " + totalInFolder);
          LOG.info("Search criteria applied. Batching disabled");
        } else {
          totalInFolder = folder.getMessageCount();
          LOG.info("Total messages : " + totalInFolder);
          getNextBatch(batchSize, folder);
        }
      } catch (MessagingException e) {
        throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                "Message retreival failed", e);
      }
    }

    private void getNextBatch(int batchSize, Folder folder) throws MessagingException {
      // after each batch invalidate cache
      if (messagesInCurBatch != null) {
        for (Message m : messagesInCurBatch) {
          if (m instanceof IMAPMessage)
            ((IMAPMessage) m).invalidateHeaders();
        }
      }
      int lastMsg = (currentBatch + 1) * batchSize;
      lastMsg = lastMsg > totalInFolder ? totalInFolder : lastMsg;
      messagesInCurBatch = folder.getMessages(currentBatch * batchSize + 1, lastMsg);
      folder.fetch(messagesInCurBatch, fp);
      current = 0;
      currentBatch++;
      LOG.info("Current Batch  : " + currentBatch);
      LOG.info("Messages in this batch  : " + messagesInCurBatch.length);
    }

    public boolean hasNext() {
      boolean hasMore = current < messagesInCurBatch.length;
      if (!hasMore && doBatching
              && currentBatch * batchSize < totalInFolder) {
        // try next batch
        try {
          getNextBatch(batchSize, folder);
          hasMore = current < messagesInCurBatch.length;
        } catch (MessagingException e) {
          throw new DataImportHandlerException(DataImportHandlerException.SEVERE,
                  "Message retreival failed", e);
        }
      }
      return hasMore;
    }

    public Message next() {
      return hasNext() ? messagesInCurBatch[current++] : null;
    }

    public void remove() {
      throw new UnsupportedOperationException("Its read only mode...");
    }

    private SearchTerm getSearchTerm() {
      if (filters.size() == 0)
        return null;
      if (filters.size() == 1)
        return filters.get(0).getCustomSearch(folder);
      SearchTerm last = filters.get(0).getCustomSearch(folder);
      for (int i = 1; i < filters.size(); i++) {
        CustomFilter filter = filters.get(i);
        SearchTerm st = filter.getCustomSearch(folder);
        if (st != null) {
          last = new AndTerm(last, st);
        }
      }
      return last;
    }
  }

  class MailsSinceLastCheckFilter implements CustomFilter {

    private Date since;

    public MailsSinceLastCheckFilter(Date date) {
      since = date;
    }

    public SearchTerm getCustomSearch(Folder folder) {
      return new ReceivedDateTerm(ComparisonTerm.GE, since);
    }
  }

  // user settings stored in member variables
  private String user;
  private String password;
  private String host;
  private String protocol;

  private String folderNames;
  private List<String> exclude = new ArrayList<String>();
  private List<String> include = new ArrayList<String>();
  private boolean recurse;

  private int batchSize;
  private int fetchSize;
  private int cTimeout;
  private int rTimeout;

  private Date fetchMailsSince;
  private String customFilter;

  private boolean processAttachment = true;

  private Tika tika;
  
  // holds the current state
  private Store mailbox;
  private boolean connected = false;
  private FolderIterator folderIter;
  private MessageIterator msgIter;
  private List<CustomFilter> filters = new ArrayList<CustomFilter>();
  private static FetchProfile fp = new FetchProfile();
  private static final Logger LOG = LoggerFactory.getLogger(DataImporter.class);

  // diagnostics
  private int rowCount = 0;

  static {
    fp.add(FetchProfile.Item.ENVELOPE);
    fp.add(FetchProfile.Item.FLAGS);
    fp.add("X-Mailer");
  }

  // Fields To Index
  // single valued
  private static final String MESSAGE_ID = "messageId";
  private static final String SUBJECT = "subject";
  private static final String FROM = "from";
  private static final String SENT_DATE = "sentDate";
  private static final String XMAILER = "xMailer";
  // multi valued
  private static final String TO_CC_BCC = "allTo";
  private static final String FLAGS = "flags";
  private static final String CONTENT = "content";
  private static final String ATTACHMENT = "attachment";
  private static final String ATTACHMENT_NAMES = "attachmentNames";
  // flag values
  private static final String FLAG_ANSWERED = "answered";
  private static final String FLAG_DELETED = "deleted";
  private static final String FLAG_DRAFT = "draft";
  private static final String FLAG_FLAGGED = "flagged";
  private static final String FLAG_RECENT = "recent";
  private static final String FLAG_SEEN = "seen";

  private int getIntFromContext(String prop, int ifNull) {
    int v = ifNull;
    try {
      String val = context.getEntityAttribute(prop);
      if (val != null) {
        val = context.replaceTokens(val);
        v = Integer.valueOf(val);
      }
    } catch (NumberFormatException e) {
      //do nothing
    }
    return v;
  }

  private boolean getBoolFromContext(String prop, boolean ifNull) {
    boolean v = ifNull;
    String val = context.getEntityAttribute(prop);
    if (val != null) {
      val = context.replaceTokens(val);
      v = Boolean.valueOf(val);
    }
    return v;
  }

  private String getStringFromContext(String prop, String ifNull) {
    String v = ifNull;
    String val = context.getEntityAttribute(prop);
    if (val != null) {
      val = context.replaceTokens(val);
      v = val;
    }
    return v;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2774.java