error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5625.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5625.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5625.java
text:
```scala
n@@ew FilterDialog(filter);

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

package org.columba.mail.gui.table.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenu;
import javax.swing.KeyStroke;

import org.columba.core.action.BasicAction;
import org.columba.core.command.DefaultCommandReference;
import org.columba.core.gui.util.ImageLoader;
import org.columba.core.main.MainInterface;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.filter.Filter;
import org.columba.mail.filter.FilterCriteria;
import org.columba.mail.filter.FilterList;
import org.columba.mail.filter.FilterRule;
import org.columba.mail.folder.Folder;
import org.columba.mail.folder.command.AddAllSendersToAddressbookCommand;
import org.columba.mail.folder.command.AddSenderToAddressbookCommand;
import org.columba.mail.folder.command.CopyMessageCommand;
import org.columba.mail.folder.command.ExpungeFolderCommand;
import org.columba.mail.folder.command.MarkMessageCommand;
import org.columba.mail.folder.command.MoveMessageCommand;
import org.columba.mail.folder.command.PrintMessageCommand;
import org.columba.mail.gui.composer.command.ForwardCommand;
import org.columba.mail.gui.composer.command.ForwardInlineCommand;
import org.columba.mail.gui.composer.command.OpenMessageWithComposerCommand;
import org.columba.mail.gui.composer.command.ReplyAsAttachmentCommand;
import org.columba.mail.gui.composer.command.ReplyCommand;
import org.columba.mail.gui.composer.command.ReplyToAllCommand;
import org.columba.mail.gui.composer.command.ReplyToMailingListCommand;
import org.columba.mail.gui.config.filter.FilterDialog;
import org.columba.mail.gui.message.command.ViewMessageSourceCommand;
import org.columba.mail.gui.table.TableController;
import org.columba.mail.gui.tree.util.SelectFolderDialog;
import org.columba.mail.message.Message;
import org.columba.mail.util.MailResourceLoader;

public class HeaderTableActionListener
	implements ActionListener {

	public BasicAction markAsReadAction;
	public BasicAction markAsFlaggedAction;
	public BasicAction markAsExpungedAction;
	public BasicAction nextAction;
	public BasicAction nextUnreadAction;
	public BasicAction prevAction;
	public BasicAction prevUnreadAction;
	public BasicAction markAllAsReadAction;
	public BasicAction replyAction;
	public BasicAction bounceAction;
	/** action to reply with an error message, means bounce */
	public BasicAction replyToAction;
	public BasicAction replyToAllAction;
	public BasicAction replyAsAttachmentAction;
	public BasicAction forwardAction;
	public BasicAction forwardInlineAction;

	public BasicAction saveAction;
	public BasicAction printAction;
	public BasicAction copyMessageAction;
	public BasicAction moveMessageAction;
	public BasicAction deleteMessageAction;
	public BasicAction addSenderAction;
	public BasicAction addAllSendersAction;
	public BasicAction viewSourceAction;

	public BasicAction filterSubjectAction;
	public BasicAction filterFromAction;
	public BasicAction filterToAction;

	public BasicAction vFolderSubjectAction;
	public BasicAction vFolderFromAction;
	public BasicAction vFolderToAction;

	public ThreadedViewAction viewThreadedAction;

	public BasicAction openMessageWithComposerAction;

	private TableController tableController;

	public HeaderTableActionListener(TableController tableController) {
		this.tableController = tableController;
		initActions();
	}

	public void initActions() {

		markAsReadAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_markasread"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_markasread_tooltip"),
				"MARK_AS_READ",
				ImageLoader.getSmallImageIcon("mail-read.png"),
				ImageLoader.getImageIcon("mail-read.png"),
				'R',
				KeyStroke.getKeyStroke("M"));

		markAsFlaggedAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_markasflagged"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_markasflagged_tooltip"),
				"MARK_AS_FLAGGED",
				ImageLoader.getSmallImageIcon("mark-as-important-16.png"),
				ImageLoader.getImageIcon("mark-as-important-16.png"),
				'F',
				null);

		markAsExpungedAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_markasexpunged"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_markasexpunged_tooltip"),
				"MARK_AS_EXPUNGED",
				ImageLoader.getSmallImageIcon("stock_delete-16.png"),
				ImageLoader.getImageIcon("stock_delete-16.png"),
				'E',
				null);

		nextAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_view_nextmessage"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_view_nextmessage_tooltip"),
				"NEXT_MESSAGE",
				null,
				null,
				'M',
				KeyStroke.getKeyStroke("F"));

		nextUnreadAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_view_nextunreadmessage"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_view_nextunreadmessage_tooltip"),
				"NEXT_UNREAD_MESSAGE",
				null,
				null,
				'U',
				KeyStroke.getKeyStroke("N"));

		prevAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_view_prevmessage"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_view_prevmessage_tooltip"),
				"PREV_MESSAGE",
				null,
				null,
				'M',
				KeyStroke.getKeyStroke("B"));

		prevUnreadAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_view_prevunreadmessage"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_view_prevunreadmessage_tooltip"),
				"PREV_UNREAD_MESSAGE",
				null,
				null,
				'U',
				KeyStroke.getKeyStroke("P"));

		markAllAsReadAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_markallasread"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_markallasread_tooltip"),
				"MARK_ALL_AS_READ",
				null,
				null,
				'A',
				KeyStroke.getKeyStroke("A"));

		replyAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_reply"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_reply_tooltip"),
				"REPLY",
				ImageLoader.getSmallImageIcon("reply_small.png"),
				ImageLoader.getImageIcon("reply.png"),
				'R',
				KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		// making a new Action -> bounce action to reply a message with an error
		bounceAction = new BasicAction(
			//reading the strings from the ressource file
	MailResourceLoader.getString("menu", "mainframe", "menu_message_bounce"),
		MailResourceLoader.getString(
			"menu",
			"mainframe",
			"menu_message_bounce_tooltip"),
		"BOUNCE",
		null,
		null,
		'B',
			// we must see if the Key not alrady used
	KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
		replyToAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_replyto"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_replyto_tooltip"),
				"REPLY_MAILINGLIST",
				null,
				null,
				'0',
				null);
		replyToAllAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_replytoall"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_replytoall_tooltip"),
				"REPLY_TO_ALL",
				ImageLoader.getSmallImageIcon("replytoall_small.png"),
				ImageLoader.getImageIcon("reply-to-all.png"),
				'0',
				null);
		replyAsAttachmentAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_replyasattachment"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_replyasattachment_tooltip"),
				"REPLY_AS_ATTACHMENT",
				null,
				null,
				'0',
				null);

		moveMessageAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_move"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_move_toolbar"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_move_tooltip"),
				"MOVE_MESSAGE",
				ImageLoader.getSmallImageIcon("movemessage_small.png"),
				ImageLoader.getImageIcon("move-message.png"),
				'M',
				null,
				false);

		copyMessageAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_copy"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_copy_toolbar"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_copy_tooltip"),
				"COPY_MESSAGE",
				ImageLoader.getSmallImageIcon("copymessage_small.png"),
				ImageLoader.getImageIcon("copy-message.png"),
				'C',
				null,
				false);

		deleteMessageAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_delete"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_delete_toolbar"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_delete_tooltip"),
				"DELETE_MESSAGE",
				ImageLoader.getSmallImageIcon("stock_delete-16.png"),
				ImageLoader.getImageIcon("stock_delete.png"),
				'0',
				null,
				false);

		/*
		replyToAllAction =
			new BasicAction(
				GlobalResourceLoader.getString("menu","mainframe", "menu_message_replytoall"),
				GlobalResourceLoader.getString(
					"menu","mainframe",
					"menu_message_replytoall_tooltip"),
				"REPLY_TO_ALL",
				null,
				null,
				'0',
				null);
		*/
		forwardAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_forward"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_forward_tooltip"),
				"FORWARD",
				ImageLoader.getSmallImageIcon("forward_small.png"),
				ImageLoader.getImageIcon("forward.png"),
				'F',
				KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		forwardInlineAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_forwardinline"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_forwardinline_tooltip"),
				"FORWARD_INLINE",
				null,
				null,
				'0',
				null);

		addSenderAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_addsender"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_addsender_tooltip"),
				"ADD_SENDER",
				null,
				null,
				'0',
				null);
		addAllSendersAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_addallsenders"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_addallsenders"),
				"ADD_ALLSENDERS",
				null,
				null,
				'0',
				null);

		printAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_print"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_print_tooltip"),
				"PRINT",
				ImageLoader.getSmallImageIcon("stock_print-16.png"),
				ImageLoader.getImageIcon("stock_print.png"),
				'0',
				KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		saveAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_save"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_save_tooltip"),
				"SAVE",
				ImageLoader.getSmallImageIcon("stock_save_as-16.png"),
				ImageLoader.getImageIcon("stock_save.png"),
				'0',
				null);
		viewSourceAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_view_source"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_view_source_tooltip"),
				"View Message Source",
				"VIEW_SOURCE",
				null,
				null,
				'0',
				KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));

		filterSubjectAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_filteronsubject"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_filteronsubject_tooltip"),
				"FILTER_ON_SUBJECT",
				null,
				null,
				'0',
				null);
		filterFromAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_filteronfrom"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_filteronfrom_tooltip"),
				"FILTER_ON_FROM",
				null,
				null,
				'0',
				null);
		filterToAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_filteronto"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_filteronto_tooltip"),
				"FILTER_ON_TO",
				null,
				null,
				'0',
				null);

		vFolderSubjectAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_vfolderonsubject"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_vfolderonsubject_tooltip"),
				"VFOLDER_ON_SUBJECT",
				null,
				null,
				'0',
				null);
		vFolderFromAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_vfolderonfrom"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_vfolderonfrom_tooltip"),
				"VFOLDER_ON_FROM",
				null,
				null,
				'0',
				null);
		vFolderToAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_vfolderonto"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_vfolderonto_tooltip"),
				"VFOLDER_ON_TO",
				null,
				null,
				'0',
				null);

		viewThreadedAction = new ThreadedViewAction( tableController.getMailFrameController() );
		tableController.addTableChangedListener(viewThreadedAction);
		
		/*
		viewThreadedAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_view_viewthreaded"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_view_viewthreaded_tooltip"),
				"VIEW_THREADED",
				null,
				null,
				'0',
				KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		viewThreadedAction.addPropertyChangeListener(this);
		*/
		

		openMessageWithComposerAction =
			new BasicAction(
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_openwithcomposer"),
				MailResourceLoader.getString(
					"menu",
					"mainframe",
					"menu_message_openwithcomposer_tooltip"),
				"OPEN_MESSAGE",
				ImageLoader.getSmallImageIcon("openmessage_small.png"),
				ImageLoader.getImageIcon("compose-message.png"),
				'0',
				null);

		viewThreadedAction.addActionListener(this);
		markAsReadAction.addActionListener(this);
		markAsReadAction.setEnabled(false);
		markAsFlaggedAction.addActionListener(this);
		markAsFlaggedAction.setEnabled(false);
		markAsExpungedAction.addActionListener(this);
		markAsExpungedAction.setEnabled(false);
		nextAction.addActionListener(this);
		nextAction.setEnabled(false);
		nextUnreadAction.addActionListener(this);
		nextUnreadAction.setEnabled(false);
		prevAction.addActionListener(this);
		prevAction.setEnabled(false);
		prevUnreadAction.addActionListener(this);
		prevUnreadAction.setEnabled(false);
		markAllAsReadAction.addActionListener(this);
		markAllAsReadAction.setEnabled(false);
		replyAction.addActionListener(this);
		replyAction.setEnabled(false);
		// same as reply
		bounceAction.addActionListener(this);
		bounceAction.setEnabled(false);
		moveMessageAction.addActionListener(this);
		moveMessageAction.setEnabled(false);
		copyMessageAction.addActionListener(this);
		copyMessageAction.setEnabled(false);
		deleteMessageAction.addActionListener(this);
		deleteMessageAction.setEnabled(false);
		replyToAllAction.addActionListener(this);
		replyToAllAction.setEnabled(false);
		replyToAction.addActionListener(this);
		replyToAction.setEnabled(false);
		replyAsAttachmentAction.addActionListener(this);
		replyAsAttachmentAction.setEnabled(false);
		forwardAction.addActionListener(this);
		forwardAction.setEnabled(false);

		addSenderAction.addActionListener(this);
		addSenderAction.setEnabled(false);
		addAllSendersAction.addActionListener(this);
		addAllSendersAction.setEnabled(false);

		printAction.addActionListener(this);
		printAction.setEnabled(false);
		saveAction.addActionListener(this);
		saveAction.setEnabled(false);
		forwardInlineAction.addActionListener(this);
		forwardInlineAction.setEnabled(false);

		viewSourceAction.addActionListener(this);
		viewSourceAction.setEnabled(false);

		filterSubjectAction.addActionListener(this);
		filterSubjectAction.setEnabled(false);
		filterFromAction.addActionListener(this);
		filterFromAction.setEnabled(false);
		filterToAction.addActionListener(this);
		filterToAction.setEnabled(false);

		vFolderSubjectAction.addActionListener(this);
		vFolderSubjectAction.setEnabled(false);
		vFolderFromAction.addActionListener(this);
		vFolderFromAction.setEnabled(false);
		vFolderToAction.addActionListener(this);
		vFolderToAction.setEnabled(false);

		openMessageWithComposerAction.setEnabled(false);
		openMessageWithComposerAction.addActionListener(this);

	}

	public void changeMessageActions() {

		//System.out.println("headertableactionlistner->changemessageaction()");

		//int row = headerTableView.getHeaderTable().getSelectedRow();

		int rowCount = tableController.getView().getSelectedRowCount();
		int max = tableController.getView().getRowCount();

		if ((rowCount == 0) || (max == 0)) {
			// disable all messages

			moveMessageAction.setEnabled(false);
			copyMessageAction.setEnabled(false);
			deleteMessageAction.setEnabled(false);

			markAsReadAction.setEnabled(false);
			markAsFlaggedAction.setEnabled(false);
			markAsExpungedAction.setEnabled(false);

			replyAction.setEnabled(false);

			bounceAction.setEnabled(false);
			replyToAllAction.setEnabled(false);
			replyToAction.setEnabled(false);
			replyAsAttachmentAction.setEnabled(false);
			forwardAction.setEnabled(false);
			forwardInlineAction.setEnabled(false);

			nextAction.setEnabled(false);
			prevAction.setEnabled(false);

			addSenderAction.setEnabled(false);
			addAllSendersAction.setEnabled(false);

			viewSourceAction.setEnabled(false);
			printAction.setEnabled(false);

			filterSubjectAction.setEnabled(false);
			filterFromAction.setEnabled(false);
			filterToAction.setEnabled(false);
			vFolderSubjectAction.setEnabled(false);
			vFolderFromAction.setEnabled(false);
			vFolderToAction.setEnabled(false);

			openMessageWithComposerAction.setEnabled(false);
		} else {
			// enable all messages
			//  -> check which actions the folder supports

			moveMessageAction.setEnabled(true);
			copyMessageAction.setEnabled(true);
			deleteMessageAction.setEnabled(true);

			markAsReadAction.setEnabled(true);
			markAsFlaggedAction.setEnabled(true);
			markAsExpungedAction.setEnabled(true);

			replyAction.setEnabled(true);

			bounceAction.setEnabled(true);
			replyToAllAction.setEnabled(true);
			replyToAction.setEnabled(true);
			replyAsAttachmentAction.setEnabled(true);
			forwardAction.setEnabled(true);
			forwardInlineAction.setEnabled(true);

			nextAction.setEnabled(true);
			prevAction.setEnabled(true);

			addSenderAction.setEnabled(true);
			addAllSendersAction.setEnabled(true);

			viewSourceAction.setEnabled(true);
			printAction.setEnabled(true);

			filterSubjectAction.setEnabled(true);
			filterFromAction.setEnabled(true);
			filterToAction.setEnabled(true);
			vFolderSubjectAction.setEnabled(true);
			vFolderFromAction.setEnabled(true);
			vFolderToAction.setEnabled(true);

			openMessageWithComposerAction.setEnabled(true);

		}
	}

	private Object[] getUids() {

		Object uids[] = tableController.getTableSelectionManager().getUids();

		return uids;

	}

	private Folder getFolder() {

		Folder folder =
			(Folder) tableController.getTableSelectionManager().getFolder();

		return folder;

	}

	public boolean hasFocus() {
		return true;
		/*
		return headerTableView.hasFocus();
		*/
	}

	/*
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getSource().equals(viewThreadedAction)) {
			Boolean isEnabled = (Boolean) e.getNewValue();

			Folder folder = (Folder) getFolder();
			folder.getFolderItem().set(
				"property",
				"enable_threaded_view",
				isEnabled.booleanValue());

			tableController
				.getHeaderTableModel()
				.getTableModelThreadedView()
				.toggleView(
			isEnabled.booleanValue());

			tableController.getView().enableThreadedView(
				isEnabled.booleanValue());

			if (isEnabled.equals(Boolean.TRUE))
				tableController.getView().enableThreadedView(true);
			else
				tableController.getView().enableThreadedView(false);

		}
	}
	*/

	public void actionPerformed(ActionEvent evt) {
		String command = evt.getActionCommand();

		if (command.equals(markAsReadAction.getActionCommand())) {

			FolderCommandReference[] r =
				(FolderCommandReference[]) tableController
					.getTableSelectionManager()
					.getSelection();
			r[0].setMarkVariant(MarkMessageCommand.MARK_AS_READ);

			MarkMessageCommand c = new MarkMessageCommand(r);

			MainInterface.processor.addOp(c);

		} else if (command.equals(markAsFlaggedAction.getActionCommand())) {
			FolderCommandReference[] r =
				(FolderCommandReference[]) tableController
					.getTableSelectionManager()
					.getSelection();
			r[0].setMarkVariant(MarkMessageCommand.MARK_AS_FLAGGED);

			MarkMessageCommand c = new MarkMessageCommand(r);

			MainInterface.processor.addOp(c);
		} else if (command.equals(markAsExpungedAction.getActionCommand())) {
			FolderCommandReference[] r =
				(FolderCommandReference[]) tableController
					.getTableSelectionManager()
					.getSelection();
			r[0].setMarkVariant(MarkMessageCommand.MARK_AS_EXPUNGED);

			MarkMessageCommand c = new MarkMessageCommand(r);

			MainInterface.processor.addOp(c);
		}
		/*
		else if ( command.equals("HEADER_CONF") )
		{
		  HeaderTableItem v = MainInterface.config.getHeaderTableItem();
		
		
		  JPopupMenu menu = new JPopupMenu();
		  for ( int i=0; i< v.count(); i++ )
		  {
		      String c = v.getName(i);
		      boolean b = v.getEnabled(i);
		        //System.out.println("name: "+c+" boolean: "+b );
		
		      JCheckBoxMenuItem item = new JCheckBoxMenuItem( c );
		      item.setEnabled( false );
		      if ( b ) item.setSelected( true );
		
		      menu.add( item );
		  }
		
		  menu.show( button, 0, 0);
		}
		*/
		/*
		else if (
			command.equals(
				MainInterface
					.globalActionCollection
					.copyAction
					.getActionCommand())) {
			
			if (hasFocus()) {
			Object uids[] = getUids();
			Folder folder = getFolder();
			ColumbaFolder clipboard = MainInterface.crossbar.getClipBoard();
			
			clipboard.removeAll();
			
			FolderOperation op =
			new FolderOperation(
				Operation.COPY,
				0,
				uids,
				folder,
				clipboard);
			MainInterface.crossbar.operate(op);
			
			}
			
		} else if (
			command.equals(
				MainInterface
					.globalActionCollection
					.cutAction
					.getActionCommand())) {
			if (hasFocus()) {
				Object uids[] = getUids();
				Folder folder = getFolder();
				
				Folder clipboard = MainInterface.crossbar.getClipBoard();
				clipboard.removeAll();
				
				FolderOperation op =
					new FolderOperation(
						Operation.MOVE,
						0,
						uids,
						folder,
						clipboard);
				MainInterface.crossbar.operate(op);
				
			}
		
		} else if (
			command.equals(
				MainInterface
					.globalActionCollection
					.pasteAction
					.getActionCommand())) {
			
			//if ( hasFocus() )
			//{
			System.out.println("pastae action");
			
			Folder clipboard = MainInterface.crossbar.getClipBoard();
			Object uids[] = clipboard.getUids();
			Folder folder = getFolder();
			
			FolderOperation op =
			new FolderOperation(Operation.COPY, 0, uids, clipboard, folder);
			MainInterface.crossbar.operate(op);
			
			
			//    }
		
		} else if (
			command.equals(
				MainInterface
					.globalActionCollection
					.deleteAction
					.getActionCommand())) {
			
			Folder folder = getFolder();
			FolderItem item = folder.getFolderItem();
			int uid = item.getUid();
			Object uids[] = getUids();
			
			FolderOperation op =
			new FolderOperation(Operation.DELETE, 0, uids, folder);
			MainInterface.crossbar.operate(op);
			
		} else if (
			command.equals(
				MainInterface
					.globalActionCollection
					.selectAllAction
					.getActionCommand())) {
			//FIXME
			
			if ( hasFocus() )
			{
			    int rowCount = getTable().getRowCount();
			    getTable().setRowSelectionInterval( 0, rowCount-1 );
			
			    changeMessageActions();
			}
			
		
		}
		*/
		else if (command.equals(replyAction.getActionCommand())) {

			int count;
			int[] selection;
			Folder folder = getFolder();
			Object uids[] = getUids();

			count = uids.length;

			for (int i = 0; i < count; i++) {
				DefaultCommandReference[] r =
					tableController.getTableSelectionManager().getSelection();
				MainInterface.processor.addOp(
					new ReplyCommand(
						tableController
							.getTableSelectionManager()
							.getSelection()));
			}

		}
		// if the command looks like an bounceAction
		else if (command.equals(bounceAction.getActionCommand())) {

			int count;
			int[] selection;
			Folder folder = getFolder();
			Object uids[] = getUids();

			count = uids.length;
		} else if (command.equals(replyToAction.getActionCommand())) {

			int count;
			int[] selection;
			Folder folder = getFolder();
			Object uids[] = getUids();

			count = uids.length;

			for (int i = 0; i < count; i++) {
				DefaultCommandReference[] r =
					tableController.getTableSelectionManager().getSelection();
				MainInterface.processor.addOp(
					new ReplyToMailingListCommand(
						tableController
							.getTableSelectionManager()
							.getSelection()));
			}

		} else if (command.equals(replyToAllAction.getActionCommand())) {

			int count;
			int[] selection;
			Folder folder = getFolder();
			Object uids[] = getUids();

			count = uids.length;

			for (int i = 0; i < count; i++) {
				DefaultCommandReference[] r =
					tableController.getTableSelectionManager().getSelection();
				MainInterface.processor.addOp(
					new ReplyToAllCommand(
						tableController
							.getTableSelectionManager()
							.getSelection()));
			}

		} else if (
			command.equals(replyAsAttachmentAction.getActionCommand())) {

			int count;
			int[] selection;
			Folder folder = getFolder();
			Object uids[] = getUids();

			count = uids.length;

			for (int i = 0; i < count; i++) {
				DefaultCommandReference[] r =
					tableController.getTableSelectionManager().getSelection();
				MainInterface.processor.addOp(
					new ReplyAsAttachmentCommand(
						tableController
							.getTableSelectionManager()
							.getSelection()));
			}
		} else if (command.equals(forwardAction.getActionCommand())) {

			int count;
			int[] selection;
			Folder folder = getFolder();
			Object uids[] = getUids();

			count = uids.length;

			for (int i = 0; i < count; i++) {
				DefaultCommandReference[] r =
					tableController.getTableSelectionManager().getSelection();
				MainInterface.processor.addOp(
					new ForwardCommand(
						tableController
							.getTableSelectionManager()
							.getSelection()));
			}
		} else if (command.equals(forwardInlineAction.getActionCommand())) {

			int count;
			int[] selection;
			Folder folder = getFolder();
			Object uids[] = getUids();

			count = uids.length;

			for (int i = 0; i < count; i++) {
				DefaultCommandReference[] r =
					tableController.getTableSelectionManager().getSelection();
				MainInterface.processor.addOp(
					new ForwardInlineCommand(
						tableController
							.getTableSelectionManager()
							.getSelection()));
			}
		} else if (command.equals(moveMessageAction.getActionCommand())) {

			SelectFolderDialog dialog =
				MainInterface.treeModel.getSelectFolderDialog();

			if (dialog.success()) {

				Folder destFolder = dialog.getSelectedFolder();

				FolderCommandReference[] result = new FolderCommandReference[2];
				FolderCommandReference[] r1 =
					(FolderCommandReference[]) tableController
						.getTableSelectionManager()
						.getSelection();
				FolderCommandReference r2 =
					new FolderCommandReference(destFolder);

				result[0] = r1[0];
				result[1] = r2;
				MoveMessageCommand c = new MoveMessageCommand(result);

				MainInterface.processor.addOp(c);

				/*
				MainInterface.processor.addOp(
					new ExpungeFolderCommand(
						tableController.getMailFrameController(),
						r1));
				*/
			}

		} else if (command.equals(copyMessageAction.getActionCommand())) {

			SelectFolderDialog dialog =
				MainInterface.treeModel.getSelectFolderDialog();

			if (dialog.success()) {

				Folder destFolder = dialog.getSelectedFolder();

				FolderCommandReference[] result = new FolderCommandReference[2];
				FolderCommandReference[] r1 =
					(FolderCommandReference[]) tableController
						.getTableSelectionManager()
						.getSelection();
				FolderCommandReference r2 =
					new FolderCommandReference(destFolder);

				result[0] = r1[0];
				result[1] = r2;
				CopyMessageCommand c = new CopyMessageCommand(result);

				MainInterface.processor.addOp(c);
			}

		} else if (command.equals(deleteMessageAction.getActionCommand())) {
			FolderCommandReference[] r =
				(FolderCommandReference[]) tableController
					.getTableSelectionManager()
					.getSelection();
			r[0].setMarkVariant(MarkMessageCommand.MARK_AS_EXPUNGED);

			MarkMessageCommand c = new MarkMessageCommand(r);

			MainInterface.processor.addOp(c);

			MainInterface.processor.addOp(new ExpungeFolderCommand(r));
		} else if (command.equals(nextAction.getActionCommand())) {

			/*
			int selection = MainInterface.headerTableView.getSelectedRow();
			
			selection+=1;
			
			headerTableView.setSelection( selection );
			
			headerTableView.showMessage();
			
			changeMessageActions();
			
			*/
		} else if (command.equals(prevAction.getActionCommand())) {
			/*
			int selection = MainInterface.headerTableView.getSelectedRow();
			
			selection-=1;
			
			headerTableView.setSelection( selection );
			headerTableView.showMessage();
			changeMessageActions();
			*/
		} else if (command.equals(addSenderAction.getActionCommand())) {

			FolderCommandReference[] r =
				(FolderCommandReference[]) tableController
					.getTableSelectionManager()
					.getSelection();
			MainInterface.processor.addOp(
				new AddSenderToAddressbookCommand(
					tableController.getMailFrameController(),
					r));

		} else if (command.equals(addAllSendersAction.getActionCommand())) {
			FolderCommandReference[] r =
				(FolderCommandReference[]) tableController
					.getTableSelectionManager()
					.getSelection();
			MainInterface.processor.addOp(
				new AddAllSendersToAddressbookCommand(
					tableController.getMailFrameController(),
					r));

		} else if (command.equals(viewSourceAction.getActionCommand())) {

			FolderCommandReference[] r =
				(FolderCommandReference[]) tableController
					.getTableSelectionManager()
					.getSelection();
			ViewMessageSourceCommand c =
				new ViewMessageSourceCommand(
					tableController.getMailFrameController(),
					r);

			MainInterface.processor.addOp(c);

		} else if (command.equals(printAction.getActionCommand())) {
			FolderCommandReference[] r =
				(FolderCommandReference[]) tableController
					.getTableSelectionManager()
					.getSelection();
			PrintMessageCommand c = new PrintMessageCommand(r);

			MainInterface.processor.addOp(c);

		}
		/*
		  else if (command.equals(filterSubjectAction.getActionCommand())) {
		
			try {
		
				Folder folder = getFolder();
				Object[] uids = getUids();
		
				for (int i = 0; i < uids.length; i++) {
		
					HeaderInterface header = folder.getMessageHeader(uids[i]);
					String pattern =
						(String) header.get("Subject");
					
					//AdapterNode parentNode = folder.getNode();
					createFilter("Subject", pattern);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		
		} else if (command.equals(filterFromAction.getActionCommand())) {
			try {
				Folder folder = getFolder();
				Object[] uids = getUids();
		
				for (int i = 0; i < uids.length; i++) {
		
					Message message = folder.getMessage(uids[i]);
					String pattern = (String) message.getHeader().get("From");
					AdapterNode parentNode = folder.getNode();
					createFilter("From", pattern);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		
		} else if (command.equals(filterToAction.getActionCommand())) {
			Folder folder = getFolder();
			Object[] uids = getUids();
		
			try {
				for (int i = 0; i < uids.length; i++) {
		
					Message message = folder.getMessage(uids[i]);
					String pattern = (String) message.getHeader().get("To");
					AdapterNode parentNode = folder.getNode();
					createFilter("To", pattern);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		
		} else if (command.equals(vFolderSubjectAction.getActionCommand())) {
			Folder folder = getFolder();
			Object uids[] = getUids();
		
			try {
				for (int i = 0; i < uids.length; i++) {
					Message message = folder.getMessage(uids[i]);
		
					createVFolder(message, "Subject");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (command.equals(vFolderFromAction.getActionCommand())) {
			Folder folder = getFolder();
			Object uids[] = getUids();
		
			try {
				for (int i = 0; i < uids.length; i++) {
					Message message = folder.getMessage(uids[i]);
		
					createVFolder(message, "From");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (command.equals(vFolderToAction.getActionCommand())) {
			Folder folder = getFolder();
			Object uids[] = getUids();
		
			try {
				for (int i = 0; i < uids.length; i++) {
					Message message = folder.getMessage(uids[i]);
		
					createVFolder(message, "To");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		
		}*/
		else if (command.equals(viewThreadedAction.getActionCommand())) {

			JMenu viewMenu =
				tableController.getMailFrameController().getMenu().getMenu(
					"VIEW");

			boolean isEnabled = false; // = viewThreadedAction.is

			/*
			boolean isEnabled =
				tableController
					.getHeaderTableModel()
					.getTableModelThreadedView()
					.isEnabled();
			*/

			/*
			tableController
				.getHeaderTableModel()
				.getTableModelThreadedView()
				.toggleView();
			*/

			/*
			Folder folder = (Folder) getFolder();
			folder.getFolderItem().set(
				"property",
				"enable_threaded_view",
				isEnabled);
			
			if (isEnabled)
				tableController.getView().enableThreadedView(true);
			else
				tableController.getView().enableThreadedView(false);
			*/

			//headerTableView.getHeaderTableModel().setFolder( headerTableView.getFolder() );
		} else if (
			command.equals(openMessageWithComposerAction.getActionCommand())) {
			Object[] uids = getUids();
			Folder folder = getFolder();

			for (int i = 0; i < uids.length; i++) {
				DefaultCommandReference[] r =
					tableController.getTableSelectionManager().getSelection();
				MainInterface.processor.addOp(
					new OpenMessageWithComposerCommand(
						tableController
							.getTableSelectionManager()
							.getSelection()));
			}

		}
	}

	protected void createFilter(String headerItem, String pattern) {
		Folder folder = getFolder();
		FilterList list = folder.getFilterList();

		Filter filter = FilterList.createEmptyFilter();
		list.add(filter);

		FilterRule rule = filter.getFilterRule();

		FilterCriteria criteria = rule.get(0);

		criteria.setType(headerItem);
		criteria.setHeaderItem(headerItem);
		criteria.setPattern(pattern);
		criteria.setCriteria("contains");

		filter.setName(headerItem + ": " + pattern);

		FilterDialog dialog =
			new FilterDialog(folder.getFilterPluginHandler(), filter);

	}

	protected void createVFolder(Message message, String headerItem) {
		/*
		String pattern = (String) message.getHeader().get(headerItem);
		
		VirtualFolder vFolder =
			(VirtualFolder) MainInterface
				.treeViewer
				.getFolderTree()
				.addVirtualFolder(
				pattern);
		
		Folder selectedFolder = getFolder();
		int uid = selectedFolder.getUid();
		Integer uidInt = new Integer(uid);
		
		Search search = vFolder.getSearchFilter();
		
		search.setUid(uidInt.intValue());
		Filter filter = search.getFilter();
		FilterRule rule = filter.getFilterRule();
		FilterCriteria criteria = rule.get(0);
		
		criteria.setType(headerItem);
		criteria.setHeaderItem(headerItem);
		criteria.setCriteria("contains");
		criteria.setPattern(pattern);
		
		SearchFrame searchDialog = new SearchFrame(vFolder);
		*/
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5625.java