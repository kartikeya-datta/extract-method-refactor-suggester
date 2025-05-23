error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1352.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1352.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1352.java
text:
```scala
i@@ntent.putExtra(Intents.Encode.DATA, clipboard.getText().toString());

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

package com.google.zxing.client.android.share;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.provider.Contacts;
import android.provider.BaseColumns;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.Button;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.R;

/**
 * Barcode Scanner can share data like contacts and bookmarks by displaying a QR Code on screen,
 * such that another user can scan the barcode with their phone.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ShareActivity extends Activity {

  private static final int PICK_BOOKMARK = 0;
  private static final int PICK_CONTACT = 1;
  private static final int PICK_APP = 2;

  //private static final int METHODS_ID_COLUMN = 0;
  private static final int METHODS_KIND_COLUMN = 1;
  private static final int METHODS_DATA_COLUMN = 2;

  private static final String[] METHODS_PROJECTION = {
      BaseColumns._ID, // 0
      Contacts.ContactMethodsColumns.KIND, // 1
      Contacts.ContactMethodsColumns.DATA, // 2
  };

  private static final int PHONES_NUMBER_COLUMN = 1;

  private static final String[] PHONES_PROJECTION = {
      BaseColumns._ID, // 0
      Contacts.PhonesColumns.NUMBER // 1
  };

  private Button clipboardButton;

  private final Button.OnClickListener contactListener = new Button.OnClickListener() {
    public void onClick(View v) {
      Intent intent = new Intent(Intent.ACTION_PICK, Contacts.People.CONTENT_URI);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
      startActivityForResult(intent, PICK_CONTACT);
    }
  };

  private final Button.OnClickListener bookmarkListener = new Button.OnClickListener() {
    public void onClick(View v) {
      Intent intent = new Intent(Intent.ACTION_PICK);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
      intent.setClassName(ShareActivity.this, BookmarkPickerActivity.class.getName());
      startActivityForResult(intent, PICK_BOOKMARK);
    }
  };

  private final Button.OnClickListener appListener = new Button.OnClickListener() {
    public void onClick(View v) {
      Intent intent = new Intent(Intent.ACTION_PICK);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
      intent.setClassName(ShareActivity.this, AppPickerActivity.class.getName());
      startActivityForResult(intent, PICK_APP);
    }
  };

  private final Button.OnClickListener clipboardListener = new Button.OnClickListener() {
    public void onClick(View v) {
      ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
      // Should always be true, because we grey out the clipboard button in onResume() if it's empty
      if (clipboard.hasText()) {
        Intent intent = new Intent(Intents.Encode.ACTION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
        intent.putExtra(Intents.Encode.DATA, clipboard.getText());
        intent.putExtra(Intents.Encode.FORMAT, Contents.Format.QR_CODE);
        startActivity(intent);
      }
    }
  };

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.share);

    findViewById(R.id.contact_button).setOnClickListener(contactListener);
    findViewById(R.id.bookmark_button).setOnClickListener(bookmarkListener);
    findViewById(R.id.app_button).setOnClickListener(appListener);
    clipboardButton = (Button) findViewById(R.id.clipboard_button);
    clipboardButton.setOnClickListener(clipboardListener);
  }

  @Override
  protected void onResume() {
    super.onResume();

    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    if (clipboard.hasText()) {
      clipboardButton.setEnabled(true);
      clipboardButton.setText(R.string.button_share_clipboard);
    } else {
      clipboardButton.setEnabled(false);
      clipboardButton.setText(R.string.button_clipboard_empty);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case PICK_BOOKMARK:
        case PICK_APP:
          showTextAsBarcode(intent.getStringExtra(Browser.BookmarkColumns.URL));
          break;
        case PICK_CONTACT:
          // Data field is content://contacts/people/984
          showContactAsBarcode(intent.getData());
          break;
      }
    }
  }

  private void showTextAsBarcode(String text) {
    Intent intent = new Intent(Intents.Encode.ACTION);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
    intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
    intent.putExtra(Intents.Encode.DATA, text);
    intent.putExtra(Intents.Encode.FORMAT, Contents.Format.QR_CODE);
    startActivity(intent);
  }

  /**
   * Takes a contact Uri and does the necessary database lookups to retrieve that person's info,
   * then sends an Encode intent to render it as a QR Code.
   *
   * @param contactUri A Uri of the form content://contacts/people/17
   */
  private void showContactAsBarcode(Uri contactUri) {
    ContentResolver resolver = getContentResolver();
    Cursor contactCursor = resolver.query(contactUri, null, null, null, null);
    Bundle bundle = new Bundle();
    if (contactCursor != null && contactCursor.moveToFirst()) {
      int nameColumn = contactCursor.getColumnIndex(Contacts.PeopleColumns.NAME);
      String name = contactCursor.getString(nameColumn);

      // Don't require a name to be present, this contact might be just a phone number.
      if (name != null && name.length() > 0) {
        bundle.putString(Contacts.Intents.Insert.NAME, massageContactData(name));
      }
      contactCursor.close();

      Uri phonesUri = Uri.withAppendedPath(contactUri, Contacts.People.Phones.CONTENT_DIRECTORY);
      Cursor phonesCursor = resolver.query(phonesUri, PHONES_PROJECTION, null, null, null);
      if (phonesCursor != null) {
        int foundPhone = 0;
        while (phonesCursor.moveToNext()) {
          String number = phonesCursor.getString(PHONES_NUMBER_COLUMN);
          if (foundPhone < Contents.PHONE_KEYS.length) {
            bundle.putString(Contents.PHONE_KEYS[foundPhone], massageContactData(number));
            foundPhone++;
          }
        }
        phonesCursor.close();
      }

      Uri methodsUri = Uri.withAppendedPath(contactUri,
          Contacts.People.ContactMethods.CONTENT_DIRECTORY);
      Cursor methodsCursor = resolver.query(methodsUri, METHODS_PROJECTION, null, null, null);
      if (methodsCursor != null) {
        int foundEmail = 0;
        boolean foundPostal = false;
        while (methodsCursor.moveToNext()) {
          int kind = methodsCursor.getInt(METHODS_KIND_COLUMN);
          String data = methodsCursor.getString(METHODS_DATA_COLUMN);
          switch (kind) {
            case Contacts.KIND_EMAIL:
              if (foundEmail < Contents.EMAIL_KEYS.length) {
                bundle.putString(Contents.EMAIL_KEYS[foundEmail], massageContactData(data));
                foundEmail++;
              }
              break;
            case Contacts.KIND_POSTAL:
              if (!foundPostal) {
                bundle.putString(Contacts.Intents.Insert.POSTAL, massageContactData(data));
                foundPostal = true;
              }
              break;
          }
        }
        methodsCursor.close();
      }

      Intent intent = new Intent(Intents.Encode.ACTION);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);      
      intent.putExtra(Intents.Encode.TYPE, Contents.Type.CONTACT);
      intent.putExtra(Intents.Encode.DATA, bundle);
      intent.putExtra(Intents.Encode.FORMAT, Contents.Format.QR_CODE);

      startActivity(intent);
    }
  }

  private static String massageContactData(String data) {
    // For now -- make sure we don't put newlines in shared contact data. It messes up
    // any known encoding of contact data. Replace with space.
    if (data.indexOf('\n') >= 0) {
      data = data.replace("\n", " ");
    }
    if (data.indexOf('\r') >= 0) {
      data = data.replace("\r", " ");
    }
    return data;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1352.java