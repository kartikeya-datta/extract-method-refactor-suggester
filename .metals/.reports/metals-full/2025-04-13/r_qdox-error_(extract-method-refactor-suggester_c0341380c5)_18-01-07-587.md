error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3493.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3493.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3493.java
text:
```scala
S@@tringBuffer contents = new StringBuffer(100);

/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.result;

import com.google.zxing.client.android.R;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ParsedResult;

import android.app.Activity;
import android.telephony.PhoneNumberUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles address book entries.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class AddressBookResultHandler extends ResultHandler {

  private static final DateFormat[] DATE_FORMATS = {
    new SimpleDateFormat("yyyyMMdd"),
    new SimpleDateFormat("yyyyMMdd'T'HHmmss"),
    new SimpleDateFormat("yyyy-MM-dd"),
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),
  };

  private final boolean[] fields;
  private int buttonCount;

  // This takes all the work out of figuring out which buttons/actions should be in which
  // positions, based on which fields are present in this barcode.
  private int mapIndexToAction(int index) {
    if (index < buttonCount) {
      int count = -1;
      for (int x = 0; x < MAX_BUTTON_COUNT; x++) {
        if (fields[x]) {
          count++;
        }
        if (count == index) {
          return x;
        }
      }
    }
    return -1;
  }

  public AddressBookResultHandler(Activity activity, ParsedResult result) {
    super(activity, result);
    AddressBookParsedResult addressResult = (AddressBookParsedResult) result;
    String[] addresses = addressResult.getAddresses();
    boolean hasAddress = addresses != null && addresses.length > 0 && addresses[0].length() > 0;
    String[] phoneNumbers = addressResult.getPhoneNumbers();
    boolean hasPhoneNumber = phoneNumbers != null && phoneNumbers.length > 0;
    String[] emails = addressResult.getEmails();
    boolean hasEmailAddress = emails != null && emails.length > 0;

    fields = new boolean[MAX_BUTTON_COUNT];
    fields[0] = true; // Add contact is always available
    fields[1] = hasAddress;
    fields[2] = hasPhoneNumber;
    fields[3] = hasEmailAddress;

    buttonCount = 0;
    for (int x = 0; x < MAX_BUTTON_COUNT; x++) {
      if (fields[x]) {
        buttonCount++;
      }
    }
  }

  @Override
  public int getButtonCount() {
    return buttonCount;
  }

  @Override
  public int getButtonText(int index) {
    int action = mapIndexToAction(index);
    switch (action) {
      case 0:
        return R.string.button_add_contact;
      case 1:
        return R.string.button_show_map;
      case 2:
        return R.string.button_dial;
      case 3:
        return R.string.button_email;
      default:
        throw new ArrayIndexOutOfBoundsException();
    }
  }

  @Override
  public void handleButtonPress(int index) {
    AddressBookParsedResult addressResult = (AddressBookParsedResult) getResult();
    String[] addresses = addressResult.getAddresses();
    String address1 = addresses == null || addresses.length < 1 ? null : addresses[0];
    int action = mapIndexToAction(index);
    switch (action) {
      case 0:
        addContact(addressResult.getNames(), addressResult.getPhoneNumbers(),
            addressResult.getEmails(), addressResult.getNote(),
            address1, addressResult.getOrg(),
            addressResult.getTitle());
        break;
      case 1:
        String[] names = addressResult.getNames();
        String title = names != null ? names[0] : null;
        searchMap(address1, title);
        break;
      case 2:
        dialPhone(addressResult.getPhoneNumbers()[0]);
        break;
      case 3:
        sendEmail(addressResult.getEmails()[0], null, null);
        break;
      default:
        break;
    }
  }

  private static Date parseDate(String s) {
    for (DateFormat currentFomat : DATE_FORMATS) {
      synchronized (currentFomat) {
        currentFomat.setLenient(false);
        Date result = currentFomat.parse(s, new ParsePosition(0));
        if (result != null) {
          return result;
        }
      }
    }
    return null;
  }

  // Overriden so we can hyphenate phone numbers, format birthdays, and bold the name.
  @Override
  public CharSequence getDisplayContents() {
    AddressBookParsedResult result = (AddressBookParsedResult) getResult();
    StringBuffer contents = new StringBuffer();
    ParsedResult.maybeAppend(result.getNames(), contents);
    int namesLength = contents.length();

    String pronunciation = result.getPronunciation();
    if (pronunciation != null && pronunciation.length() > 0) {
      contents.append("\n(");
      contents.append(pronunciation);
      contents.append(')');
    }

    ParsedResult.maybeAppend(result.getTitle(), contents);
    ParsedResult.maybeAppend(result.getOrg(), contents);
    ParsedResult.maybeAppend(result.getAddresses(), contents);
    String[] numbers = result.getPhoneNumbers();
    if (numbers != null) {
      for (String number : numbers) {
        ParsedResult.maybeAppend(PhoneNumberUtils.formatNumber(number), contents);
      }
    }
    ParsedResult.maybeAppend(result.getEmails(), contents);
    ParsedResult.maybeAppend(result.getURL(), contents);

    String birthday = result.getBirthday();
    if (birthday != null && birthday.length() > 0) {
      Date date = parseDate(birthday);
      if (date != null) {
        ParsedResult.maybeAppend(DateFormat.getDateInstance().format(date.getTime()), contents);
      }
    }
    ParsedResult.maybeAppend(result.getNote(), contents);

    if (namesLength > 0) {
      // Bold the full name to make it stand out a bit.
      Spannable styled = new SpannableString(contents.toString());
      styled.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, namesLength, 0);
      return styled;
    } else {
      return contents.toString();
    }
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_address_book;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3493.java