error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7491.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7491.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,28]

error in qdox parser
file content:
```java
offset: 28
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7491.java
text:
```scala
public static final native v@@oid memmove(int dest, PtWebClient2Data_t src, int size);

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.photon;

 
import org.eclipse.swt.internal.*;

public class OS extends C {

	public static final int QNX_MAJOR;
	public static final int QNX_MINOR;
	public static final int QNX_MICRO;
	static {		
		/* Gather machine information (just like "uname -a") */
		utsname udata = new utsname();
		OS.uname(udata);
		
		/*
		* Parse out the release information into integers that
		* can be used at runtime for version checks.
		*/
		int value = 0;
		int i = 0;
		while( udata.release[i] != '.' ) {
			value = value * 10;
			value += udata.release[i] - '0';
			i++;
		}
		QNX_MAJOR = value;
		
		value = 0;
		i++;
		while( udata.release[i] != '.' ) {
			value = value * 10;
			value += udata.release[i] - '0';
			i++;
		}
		QNX_MINOR = value;
		
		value = 0;
		i++;
		while( udata.release[i] != 0 ) {
			value = value * 10;
			value += udata.release[i] - '0';
			i++;
		}
		QNX_MICRO = value;
	}

	/** Constants */
	public static final int MAX_DESC_LENGTH = 0x20;
	public static final int MAX_FONT_TAG = 0x50;
	public static final int MAX_URL_LENGTH=1024;
	public static final int NAME_MAX = 0xff;
	public static final int PATH_MAX = 0x400;
	public static final int PF_STYLE_BOLD = 0x1;
	public static final int PF_STYLE_ITALIC = 0x2;
	public static final int PHFONT_ALL_FONTS = 0x33;
	public static final int PHFONT_ALL_SYMBOLS = 0xffffffff;
	public static final int PHFONT_BITMAP = 0x2;
	public static final int PHFONT_DONT_SHOW_LEGACY = 0x40;
	public static final int PHFONT_FIXED = 0x20;
	public static final int PHFONT_INFO_BLDITC = 0x8;
	public static final int PHFONT_INFO_BOLD = 0x2;
	public static final int PHFONT_INFO_FIXED = 0x10;
	public static final int PHFONT_INFO_ITALIC = 0x4;
	public static final int PHFONT_INFO_PLAIN = 0x1;
	public static final int PHFONT_INFO_PROP = 0x20;
	public static final int PHFONT_PROP = 0x10;
	public static final int PHFONT_SCALABLE = 0x1;
	public static final int Pg_ALPHA_OP_SRC_GLOBAL = 0x40000;
	public static final int Pg_ALPHA_OP_SRC_MAP = 0x80000;
	public static final int Pg_ARC = 0x2000;
	public static final int Pg_ARC_PIE = 0x1000;
	public static final int Pg_BACK_FILL = 0x100;
	public static final int Pg_BEVEL_JOIN = 0x02;
	public static final int Pg_BLEND_DST_ONE_MINUS_SRC_ALPHA = 0x5;
	public static final int Pg_BLEND_SRC_SRC_ALPHA = 0x400;
	public static final int Pg_BUTT_CAP = 0;
	public static final int Pg_CLOSED = 0x1000;
	public static final int Pg_DRAWMODE_OPAQUE = 0x0;
	public static final int Pg_DRAWMODE_XOR = 0x1;
	public static final int Pg_DRAW_FILL = 0x20;
	public static final int Pg_DRAW_STROKE = 0x10;
	public static final int Pg_DrawModeDSx = 0x6680;
	public static final int Pg_DrawModeS = 0xcc80;
	public static final int Pg_EXTENT_BASED = 0x100;
	public static final int Pg_GRAD_HORIZONTAL = 0x3;
	public static final int Pg_GRAD_LINEAR = 0x0;
	public static final int Pg_GRAD_VERTICAL = 0x4;
	public static final int Pg_IMAGE_CLASS_DIRECT = 0x20;
	public static final int Pg_IMAGE_CLASS_MASK = 0x38;
	public static final int Pg_IMAGE_CLASS_PALETTE = 0x10;
	public static final int Pg_IMAGE_DIRECT_444 = 0x24;
	public static final int Pg_IMAGE_DIRECT_555 = 0x23;
	public static final int Pg_IMAGE_DIRECT_565 = 0x22;
	public static final int Pg_IMAGE_DIRECT_888 = 0x21;
	public static final int Pg_IMAGE_DIRECT_8888 = 0x20;
	public static final int Pg_IMAGE_PALETTE_BYTE = 0x10;
	public static final int Pg_IMAGE_PALETTE_NIBBLE = 0x11;
	public static final int Pg_INDEX_COLOR = 0x2000000;
	public static final int Pg_MITER_JOIN = 0;
	public static final int Pg_PALSET_SOFT = 0x0;
	public static final byte[] Pg_PAT_HALF = {(byte)0xAA, (byte)0x55, (byte)0xAA, (byte)0x55, (byte)0xAA, (byte)0x55, (byte)0xAA, (byte)0x55};
	public static final int Pg_ROUND_CAP = 0x01;
	public static final int Pg_ROUND_JOIN = 0x01;
	public static final int Pg_SQUARE_CAP = 2;
	public static final int Pg_TEXT_LEFT = 0x1000;
	public static final int Pg_TEXT_TOP = 0x4000;
	public static final int Pg_TRANSPARENT = 0xffffffff;
	public static final int Ph_BUTTON_ADJUST = 0x2;
	public static final int Ph_BUTTON_MENU = 0x1;
	public static final int Ph_BUTTON_SELECT = 0x4;
	public static final int Ph_CONSUMED = 0x4;
	public static final int Ph_CURSOR_BITMAP = 0xfb;
	public static final int Ph_CURSOR_CLOCK = 0xe908;
	public static final int Ph_CURSOR_CROSSHAIR = 0xe906;
	public static final int Ph_CURSOR_DEFAULT_COLOR = 0xffffe0;
	public static final int Ph_CURSOR_DONT = 0xe90a;
	public static final int Ph_CURSOR_DRAG_BL = 0xe916;
	public static final int Ph_CURSOR_DRAG_BOTTOM = 0xe910;
	public static final int Ph_CURSOR_DRAG_BR = 0xe914;
	public static final int Ph_CURSOR_DRAG_HORIZONTAL = 0xe912;
	public static final int Ph_CURSOR_DRAG_LEFT = 0xe912;
	public static final int Ph_CURSOR_DRAG_RIGHT = 0xe912;
	public static final int Ph_CURSOR_DRAG_TL = 0xe914;
	public static final int Ph_CURSOR_DRAG_TOP = 0xe910;
	public static final int Ph_CURSOR_DRAG_TR = 0xe916;
	public static final int Ph_CURSOR_DRAG_VERTICAL = 0xe910;
	public static final int Ph_CURSOR_FINGER = 0xe90c;
	public static final int Ph_CURSOR_INHERIT = 0x0;
	public static final int Ph_CURSOR_INSERT = 0xe90e;
	public static final int Ph_CURSOR_MOVE = 0xe904;
	public static final int Ph_CURSOR_NO_INHERIT = 0x1;
	public static final int Ph_CURSOR_POINTER = 0xe900;
	public static final int Ph_CURSOR_POINT_WAIT = 0xe918;
	public static final int Ph_CURSOR_QUESTION_POINT = 0xe91c;
	public static final int Ph_DEV_RID = 0x1;
	public static final int Ph_DRAG_KEY_MOTION = 0x20;
	public static final int Ph_DRAG_TRACK = 0x10;
	public static final int Ph_EVENT_DIRECT = 0x10;
	public static final int Ph_EVENT_MSG = 0x1;
	public static final int Ph_EV_BOUNDARY = 0x20;
	public static final int Ph_EV_BUT_PRESS = 0x2;
	public static final int Ph_EV_BUT_RELEASE = 0x4;
	public static final int Ph_EV_DRAG = 0x200;
	public static final int Ph_EV_DRAG_COMPLETE = 0x2;
	public static final int Ph_EV_DRAG_KEY_EVENT = 0x3;
	public static final int Ph_EV_DRAG_MOTION_EVENT = 0x4;
	public static final int Ph_EV_KEY = 0x1;
	public static final int Ph_EV_PTR_ENTER = 0x0;
	public static final int Ph_EV_PTR_ENTER_FROM_CHILD = 0x4;
	public static final int Ph_EV_PTR_LEAVE = 0x1;
	public static final int Ph_EV_PTR_LEAVE_TO_CHILD = 0x5;
	public static final int Ph_EV_PTR_MOTION = 0x18;
	public static final int Ph_EV_PTR_MOTION_BUTTON = 0x10;
	public static final int Ph_EV_PTR_MOTION_NOBUTTON = 0x8;
	public static final int Ph_EV_PTR_STEADY = 0x2;
	public static final int Ph_EV_PTR_UNSTEADY = 0x3;
	public static final int Ph_EV_RELEASE_PHANTOM = 0x1;
	public static final int Ph_EV_RELEASE_REAL = 0x0;
	public static final int Ph_EV_TIMER = 0x10000;
	public static final int Ph_FAKE_EVENT = 0x1;
	public static final int Ph_FORCE_BOUNDARY = 0x400000;
	public static final int Ph_GRAFX_REGION = 0x4;
	public static final int Ph_NOT_CUAKEY = 0x10;
	public static final int Ph_QUERY_CONSOLE = 0x0;
	public static final int Ph_QUERY_GRAPHICS = 0x4;
	public static final int Ph_QUERY_WORKSPACE = 0x2;
	public static final int Ph_RELEASE_IMAGE_ALL = 0x1f;
	public static final int Ph_RESIZE_MSG = 0x3;
	public static final int Ph_TRACK_DRAG = 0xf;
	public static final int Ph_WM_CLOSE = 0x1;
	public static final int Ph_WM_EVSTATE_FOCUS = 0x0;
	public static final int Ph_WM_EVSTATE_FOCUSLOST = 0x1;
	public static final int Ph_WM_EVSTATE_HIDE = 0x0;
	public static final int Ph_WM_EVSTATE_UNHIDE = 0x1;
	public static final int Ph_WM_FOCUS = 0x4;
	public static final int Ph_WM_HIDE = 0x40;
	public static final int Ph_WM_ICON = 0x400;
	public static final int Ph_WM_MAX = 0x800;
	public static final int Ph_WM_MOVE = 0x200;
	public static final int Ph_WM_RENDER_BORDER = 0x10;
	public static final int Ph_WM_RENDER_MAX = 0x2000;
	public static final int Ph_WM_RENDER_MENU = 0x100;
	public static final int Ph_WM_RENDER_MIN = 0x1000;
	public static final int Ph_WM_RENDER_RESIZE = 0x20;
	public static final int Ph_WM_RENDER_TITLE = 0x80;
	public static final int Ph_WM_RESIZE = 0x100;
	public static final int Ph_WM_RESTORE = 0x8000;
	public static final int Ph_WM_STATE_ISFOCUS = 0x8000;
	public static final int Ph_WM_STATE_ISFRONT = 0x100;
	public static final int Ph_WM_STATE_ISHIDDEN = 0x1;
	public static final int Ph_WM_STATE_ISICONIFIED = 0x40;
	public static final int Ph_WM_STATE_ISMAX = 0x2;
	public static final int Ph_WM_STATE_ISMAXING = 0x4000;
	public static final int Pk_Alt_L = 0xf0e9;
	public static final int Pk_Alt_R = 0xf0ea;
	public static final int Pk_BackSpace = 0xf008;
	public static final int Pk_Break = 0xF06B;
	public static final int Pk_Cancel = 0xf069;
	public static final int Pk_Caps_Lock = 0xf0e5;
	public static final int Pk_Clear = 0xf00b;
	public static final int Pk_Control_L = 0xf0e3;
	public static final int Pk_Control_R = 0xf0e4;
	public static final int Pk_Delete = 0xf0ff;
	public static final int Pk_Down = 0xf054;
	public static final int Pk_End = 0xf057;
	public static final int Pk_Escape = 0xf01b;
	public static final int Pk_F1 = 0xf0be;
	public static final int Pk_F10 = 0xf0c7;
	public static final int Pk_F11 = 0xf0c8;
	public static final int Pk_F12 = 0xf0c9;
	public static final int Pk_F13 = 0xf0cA;
	public static final int Pk_F14 = 0xf0cB;
	public static final int Pk_F15 = 0xf0cC;
	public static final int Pk_F2 = 0xf0bf;
	public static final int Pk_F3 = 0xf0c0;
	public static final int Pk_F4 = 0xf0c1;
	public static final int Pk_F5 = 0xf0c2;
	public static final int Pk_F6 = 0xf0c3;
	public static final int Pk_F7 = 0xf0c4;
	public static final int Pk_F8 = 0xf0c5;
	public static final int Pk_F9 = 0xf0c6;
	public static final int Pk_Home = 0xf050;
	public static final int Pk_Help = 0xF06a;
	public static final int Pk_Hyper_L = 0xf0ed;
	public static final int Pk_Hyper_R = 0xf0ee;
	public static final int Pk_Insert = 0xf063;
	public static final int Pk_KF_Cap_Valid = 0x80;
	public static final int Pk_KF_Key_Down = 0x1;
	public static final int Pk_KF_Key_Repeat = 0x2;
	public static final int Pk_KF_Scan_Valid = 0x20;
	public static final int Pk_KF_Sym_Valid = 0x40;
	public static final int Pk_KM_Alt = 0x4;
	public static final int Pk_KM_Ctrl = 0x2;
	public static final int Pk_KM_Num_Lock = 0x00020000;
	public static final int Pk_KM_Shift = 0x1;
	public static final int Pk_KP_Enter = 0xF08D;
	public static final int Pk_KP_Tab = 0xf089;
	public static final int Pk_KP_Equal = 0xF0BD;
	public static final int Pk_KP_Multiply = 0xF0AA;
	public static final int Pk_KP_Add = 0xF0AB;
	public static final int Pk_KP_Separator = 0xF0AC;
	public static final int Pk_KP_Subtract = 0xF0AD;
	public static final int Pk_KP_Decimal = 0xF0AE;
	public static final int Pk_KP_Divide = 0xF0AF;
	public static final int Pk_KP_0 = 0xF0B0;
	public static final int Pk_KP_1 = 0xF0B1;
	public static final int Pk_KP_2 = 0xF0B2;
	public static final int Pk_KP_3 = 0xF0B3;
	public static final int Pk_KP_4 = 0xF0B4;
	public static final int Pk_KP_5 = 0xF0B5;
	public static final int Pk_KP_6 = 0xF0B6;
	public static final int Pk_KP_7 = 0xF0B7;
	public static final int Pk_KP_8 = 0xF0B8;
	public static final int Pk_KP_9 = 0xF0B9;
	public static final int Pk_Left = 0xf051;
	public static final int Pk_Linefeed = 0xf00a;
	public static final int Pk_Menu = 0xf067;
	public static final int Pk_Next = 0xf056;
	public static final int Pk_Num_Lock = 0xf07f;
	public static final int Pk_Pause = 0xf013;
	public static final int Pk_Pg_Down = 0xf056;
	public static final int Pk_Pg_Up = 0xf055;
	public static final int Pk_Print = 0xf061;
	public static final int Pk_Prior = 0xf055;
	public static final int Pk_Return = 0xf00d;
	public static final int Pk_Right = 0xf053;
	public static final int Pk_Scroll_Lock = 0xf014;
	public static final int Pk_Shift_L = 0xf0e1;
	public static final int Pk_Shift_R = 0xf0e2;
	public static final int Pk_Tab = 0xf009;
	public static final int Pk_Up = 0xf052;
	public static final int Pt_ALL_BEVELS = 0xf00;
	public static final int Pt_ALL_BUTTONS = 0x800000;
	public static final int Pt_ALL_ETCHES = 0xf;
	public static final int Pt_ALL_INLINES = 0xf000;
	public static final int Pt_ALL_OUTLINES = 0xf0;
	public static final int Pt_ALWAYS = 0x1;
	public static final int Pt_ARG_ACCEL_KEY = 0xbca;
	public static final int Pt_ARG_ACCEL_TEXT = 0x1b58;
	public static final int Pt_ARG_ANCHOR_FLAGS = 0x2711;
	public static final int Pt_ARG_AREA = 0x3e8;
	public static final int Pt_ARG_BALLOON_COLOR = 0xbcc;
	public static final int Pt_ARG_BALLOON_FILL_COLOR = 0xbcb;
	public static final int Pt_ARG_BALLOON_POSITION = 0xbc8;
	public static final int Pt_ARG_BASIC_FLAGS = 0x7df;
	public static final int Pt_ARG_BEVEL_COLOR = 0x7e5;
	public static final int Pt_ARG_BEVEL_CONTRAST = 0x7e4;
	public static final int Pt_ARG_BEVEL_WIDTH = 0x3e9;
	public static final int Pt_ARG_BITMAP_CURSOR = 0x3f9;
	public static final int Pt_ARG_BUTTON_TYPE = 0x1b5a;
	public static final int Pt_ARG_CBOX_FLAGS = 0x7918;
	public static final int Pt_ARG_CBOX_MAX_VISIBLE_COUNT = 0x792b;
	public static final int Pt_ARG_CBOX_SELECTION_ITEM = 0x7919;
	public static final int Pt_ARG_CLIENT_NAME = 0x182BC;
	public static final int Pt_ARG_COLOR = 0x7d1;
	public static final int Pt_ARG_CONTAINER_FLAGS = 0x2715;
	public static final int Pt_ARG_CURSOR_POSITION = 0xfa3;
	public static final int Pt_ARG_CURSOR_TYPE = 0x3ea;
	public static final int Pt_ARG_DARK_BEVEL_COLOR = 0x7d6;
	public static final int Pt_ARG_FILL_COLOR = 0x7d2;
	public static final int Pt_ARG_FLAGS = 0x3ee;
	public static final int Pt_ARG_GAUGE_FLAGS = 0x5208;
	public static final int Pt_ARG_GAUGE_FONT = 0x5209;
	public static final int Pt_ARG_GAUGE_VALUE = 0x520c;
	public static final int Pt_ARG_GROUP_FLAGS = 0x2af8;
	public static final int Pt_ARG_GROUP_ORIENTATION = 0x2afa;
	public static final int Pt_ARG_HEIGHT = 0x400;
	public static final int Pt_ARG_HORIZONTAL_ALIGNMENT = 0xbb8;
	public static final int Pt_ARG_INCREMENT = 0x7149;
	public static final int Pt_ARG_INDICATOR_TYPE = 0x232a;
	public static final int Pt_ARG_ITEMS = 0x59d9;
	public static final int Pt_ARG_LABEL_IMAGE = 0xbb9;
	public static final int Pt_ARG_LABEL_TYPE = 0xbbb;
	public static final int Pt_ARG_LIGHT_BEVEL_COLOR = 0x7d0;
	public static final int Pt_ARG_LINE_SPACING = 0xbcd;
	public static final int Pt_ARG_LIST_FLAGS = 0x59da;
	public static final int Pt_ARG_LIST_FONT = 0x59db;
	public static final int Pt_ARG_LIST_ITEM_COUNT = 0x59e6;
	public static final int Pt_ARG_LIST_SEL_COUNT = 0x59e7;
	public static final int Pt_ARG_LIST_TOTAL_HEIGHT = 0x59e8;
	public static final int Pt_ARG_MARGIN_BOTTOM = 0xbbc;
	public static final int Pt_ARG_MARGIN_HEIGHT = 0x7d4;
	public static final int Pt_ARG_MARGIN_LEFT = 0xbbd;
	public static final int Pt_ARG_MARGIN_RIGHT = 0xbbe;
	public static final int Pt_ARG_MARGIN_TOP = 0xbbf;
	public static final int Pt_ARG_MARGIN_WIDTH = 0x7d5;
	public static final int Pt_ARG_MAXIMUM = 0x520b;
	public static final int Pt_ARG_MAX_LENGTH = 0xfa5;
	public static final int Pt_ARG_MENU_FLAGS = 0x4a38;
	public static final int Pt_ARG_MINIMUM = 0x520a;
	public static final int Pt_ARG_MIN_HEIGHT = 1000 * 18 + 6;
	public static final int Pt_ARG_MIN_WIDTH = 1000 * 18 + 7;
	public static final int Pt_ARG_MODIFIER_KEYS = 0x1b5c;
	public static final int Pt_ARG_MULTITEXT_LINE_SPACING = 0x138b;
	public static final int Pt_ARG_MULTITEXT_NUM_LINES = 0x138e;
	public static final int Pt_ARG_MULTITEXT_QUERY_LINE = 0x1392;
	public static final int Pt_ARG_MULTITEXT_TABS = 0x1395;
	public static final int Pt_ARG_MULTITEXT_TOP_LINE = 0x138f;
	public static final int Pt_ARG_MULTITEXT_WRAP_FLAGS = 0x1389;
	public static final int Pt_ARG_NUMERIC_FLAGS = 0xcf09;
	public static final int Pt_ARG_NUMERIC_INCREMENT = 0xcf0b;
	public static final int Pt_ARG_NUMERIC_MAX = 0xcf0e;
	public static final int Pt_ARG_NUMERIC_MIN = 0xcf0d;
	public static final int Pt_ARG_NUMERIC_SPACING = 0xcf19;
	public static final int Pt_ARG_NUMERIC_UPDOWN_WIDTH = 0xcf0a;
	public static final int Pt_ARG_NUMERIC_VALUE = 0xcf0c;
	public static final int Pt_ARG_ORIENTATION = 0x520d;
	public static final int Pt_ARG_OUTLINE_COLOR = 0x7e6;
	public static final int Pt_ARG_PAGE_INCREMENT = 0x714d;
	public static final int Pt_ARG_PG_CURRENT_INDEX = 0xfa05;
	public static final int Pt_ARG_PG_FLAGS = 64 * 1000;
	public static final int Pt_ARG_PG_PANEL_TITLES = 0xfa02;
	public static final int Pt_ARG_POS = 0x3ef;
	public static final int Pt_ARG_REGION_FLAGS = 0x3a9b;
	public static final int Pt_ARG_REGION_OPAQUE = 0x3a9d;
	public static final int Pt_ARG_REGION_SENSE = 0x3aa0;
	public static final int Pt_ARG_RESIZE_FLAGS = 0x3f0;
	public static final int Pt_ARG_SCROLLBAR_X_DISPLAY = 0x32ce;
	public static final int Pt_ARG_SCROLLBAR_Y_DISPLAY = 0x32d0;
	public static final int Pt_ARG_SELECTION_FILL_COLOR = 0x59e4;
	public static final int Pt_ARG_SELECTION_INDEXES = 0x59de;
	public static final int Pt_ARG_SELECTION_MODE = 0x59df;
	public static final int Pt_ARG_SELECTION_TEXT_COLOR = 0x59e5;
	public static final int Pt_ARG_SEP_FLAGS = 0x7530;
	public static final int Pt_ARG_SEP_TYPE = 0x7532;
	public static final int Pt_ARG_SLIDER_SIZE = 0x714f;
	public static final int Pt_ARG_TEXT_CURSOR_WIDTH = 0xbf2;
	public static final int Pt_ARG_TEXT_FLAGS = 0xfa8;
	public static final int Pt_ARG_TEXT_FONT = 0xbc2;
	public static final int Pt_ARG_TEXT_STRING = 0xbc3;
	public static final int Pt_ARG_TIMER_INITIAL = 0xa028;
	public static final int Pt_ARG_TITLE = 0x2717;
	public static final int Pt_ARG_TITLE_FONT = 0x2718;
	public static final int Pt_ARG_TOOLBAR_FLAGS = 0xee48;
	public static final int Pt_ARG_TOP_ITEM_POS = 0x59e0;
	public static final int Pt_ARG_USER_DATA = 0x3f6;
	public static final int Pt_ARG_VERTICAL_ALIGNMENT = 0xbc7;
	public static final int Pt_ARG_VISIBLE_COUNT = 0x59e1;
	public static final int Pt_ARG_WEB_DATA = 0x186B6;
	public static final int Pt_ARG_WEB_GET_URL = 0x186A0;
	public static final int Pt_ARG_WEB_NAVIGATE_PAGE = 0x186A1;
	public static final int Pt_ARG_WEB_OPTION = 0x186AC;
	public static final int Pt_ARG_WEB_RELOAD = 0x186A6;
	public static final int Pt_ARG_WEB_SERVER = 0x186A9;
	public static final int Pt_ARG_WEB_STOP = 0x186A7;
	public static final int Pt_ARG_WIDTH = 0x3ff;
	public static final int Pt_ARG_WINDOW_MANAGED_FLAGS = 0x465b;
	public static final int Pt_ARG_WINDOW_NOTIFY_FLAGS = 0x465c;
	public static final int Pt_ARG_WINDOW_RENDER_FLAGS = 0x465d;
	public static final int Pt_ARG_WINDOW_STATE = 0x465e;
	public static final int Pt_ARG_WINDOW_TITLE = 0x465f;
	public static final int Pt_BALLOON_BOTTOM = 0x3;
	public static final int Pt_BALLOON_RIGHT = 0x0;
	public static final int Pt_BLOCKED = 0x20000;
	public static final int Pt_BOTTOM_ANCHORED_BOTTOM = 0x00000080;
	public static final int Pt_BOTTOM_ANCHORED_TOP = 0x00000800;
	public static final int Pt_BOTTOM_BEVEL = 0x200;
	public static final int Pt_BOTTOM_ETCH = 0x2;
	public static final int Pt_BOTTOM_INLINE = 0x2000;
	public static final int Pt_BOTTOM_OUTLINE = 0x20;
	public static final int Pt_CALLBACKS_ACTIVE = 0x4000000;
	public static final int Pt_CB_ACTIVATE = 0x7d9;
	public static final int Pt_CB_ARM = 0x7d7;
	public static final int Pt_CB_GOT_FOCUS = 0x7da;
	public static final int Pt_CB_LOST_FOCUS = 0x7db;
	public static final int Pt_CB_MODIFY_VERIFY = 0xfa9;
	public static final int Pt_CB_NUMERIC_CHANGED = 0xcf17;
	public static final int Pt_CB_OUTBOUND = 0x3fe;
	public static final int Pt_CB_PG_PANEL_SWITCHING = 0xfa0a;
	public static final int Pt_CB_REALIZED = 0x3f4;
	public static final int Pt_CB_RESIZE = 0x2713;
	public static final int Pt_CB_SCROLL_MOVE = 0x7152;
	public static final int Pt_CB_SELECTION = 0x59e2;
	public static final int Pt_CB_SLIDER_MOVE = 0x5601;
	public static final int Pt_CB_TEXT_CHANGED = 0xfaa;
	public static final int Pt_CB_TIMER_ACTIVATE = 0xa02a;
	public static final int Pt_CB_UNREALIZED = 0x3f5;
	public static final int Pt_CB_WEB_CLOSE_WINDOW = 0x18713;
	public static final int Pt_CB_WEB_COMPLETE = 0x18704;
	public static final int Pt_CB_WEB_DATA_REQ = 0x1870F;
	public static final int Pt_CB_WEB_METADATA = 0x18705;
	public static final int Pt_CB_WEB_NEW_WINDOW = 0x18711;
	public static final int Pt_CB_WEB_START = 0x1870B;
	public static final int Pt_CB_WEB_STATUS = 0x18708;
	public static final int Pt_CB_WEB_URL = 0x18706;
	public static final int Pt_CB_WINDOW = 0x4661;
	public static final int Pt_CENTER = 0x2;
	public static final int Pt_COLORSELECT_ACCEPT = 0x8000;
	public static final int Pt_COLORSELECT_MODAL = 0x4;
	public static final int Pt_COMBOBOX_STATIC = 0x20;
	public static final int Pt_CONSUME = 0x2;
	public static final int Pt_CONTINUE = 0x0;
	public static final int Pt_DELAY_REALIZE = 0x40;
	public static final int Pt_EDITABLE = 0x2;
	public static final int Pt_EMT_CHAR = 0x2;
	public static final int Pt_EMT_NEWLINE = 0x4;
	public static final int Pt_EMT_WORD = 0x1;
	public static final int Pt_ENABLE_CUA = 0x10;
	public static final int Pt_ENABLE_CUA_ARROWS = 0x20;
	public static final int Pt_END = 0x2;
	public static final int Pt_ETCHED_IN = 0x4;
	public static final int Pt_ETCHED_OUT = 0x5;
	public static final int Pt_ETCH_TITLE_AREA = 0x200;
	public static final int Pt_FLAT_FILL = 0x10000;
	public static final int Pt_FSDIALOG_BTN2 = 0x2;
	public static final int Pt_FSR_NO_FCHECK = 0x100;
	public static final int Pt_FSR_NO_SELECT_FILES = 0x2000;
	public static final int Pt_FSR_SELECT_DIRS = 0x4000;
	public static final int Pt_GAUGE_INDETERMINATE = 0x80;
	public static final int Pt_GETS_FOCUS = 0x400;
	public static final int Pt_GHOST = 0x1000000;
	public static final int Pt_GROUP_EQUAL_SIZE = 0x2;
	public static final int Pt_GROUP_EQUAL_SIZE_VERTICAL = 0x200;
	public static final int Pt_GROUP_HORIZONTAL = 0x0;
	public static final int Pt_HIGHLIGHTED = 0x100;
	public static final int Pt_HORIZONTAL = 0x1;
	public static final int Pt_HORIZONTAL_GRADIENT = 0x40000;
	public static final int Pt_HOTKEYS_FIRST = 0x80;
	public static final int Pt_IMAGE = 0x4;
	public static final int Pt_LEFT = 0x0;
	public static final int Pt_LEFT_ANCHORED_LEFT = 0x00000100;
	public static final int Pt_LEFT_BEVEL = 0x400;
	public static final int Pt_LEFT_ETCH = 0x4;
	public static final int Pt_LEFT_INLINE = 0x4000;
	public static final int Pt_LEFT_OUTLINE = 0x40;
	public static final int Pt_LIST_SCROLLBAR_ALWAYS = 0x1;
	public static final int Pt_LIST_SCROLLBAR_AS_REQUIRED = 0x2;
	public static final int Pt_LIST_SELECTION_FINAL = 0x0;
	public static final int Pt_MENUABLE = 0x8000000;
	public static final int Pt_MENU_CHILD = 0x8;
	public static final int Pt_MENU_RIGHT = 0x2;
	public static final int Pt_MENU_TEXT = 0x1;
	public static final int Pt_MODAL = 0x84;
	public static final int Pt_NEVER = 0x0;
	public static final int Pt_NOLINE = 0x6;
	public static final int Pt_NO_PARENT = 0x1;
	public static final int Pt_N_OF_MANY = 0x0;
	public static final int Pt_NUMERIC_CHANGED = 0x1;
	public static final int Pt_NUMERIC_UPDOWN_ACTIVATE = 0x8;
	public static final int Pt_NUMERIC_UPDOWN_REPEAT = 0x4;	
	public static final int Pt_NUMERIC_WRAP= 0x04;
	public static final int Pt_ONE_OF_MANY = 0x1;
	public static final int Pt_PG_INVALID = 0xffff;
	public static final int Pt_PG_SELECTOR_ON_BOTTOM = 0x0001;
	public static final int Pt_PROCESS = 0x0;
	public static final int Pt_RESIZE_XY_BITS = 0x3f00000;
	public static final int Pt_RESIZE_X_ALWAYS = 0x800000;
	public static final int Pt_RESIZE_Y_ALWAYS = 0x100000;
	public static final int Pt_RIGHT = 0x1;
	public static final int Pt_RIGHT_ANCHORED_RIGHT = 0x00000020;
	public static final int Pt_RIGHT_BEVEL = 0x800;
	public static final int Pt_RIGHT_ETCH = 0x8;
	public static final int Pt_RIGHT_INLINE = 0x8000;
	public static final int Pt_RIGHT_OUTLINE = 0x80;
	public static final int Pt_SCROLL_DECREMENT = 0x1;
	public static final int Pt_SCROLL_DRAGGED = 0x7;
	public static final int Pt_SCROLL_INCREMENT = 0x2;
	public static final int Pt_SCROLL_PAGE_DECREMENT = 0x4;
	public static final int Pt_SCROLL_PAGE_INCREMENT = 0x3;
	public static final int Pt_SCROLL_TO_MAX = 0x5;
	public static final int Pt_SCROLL_TO_MIN = 0x6;
	public static final int Pt_SELECTABLE = 0x80;
	public static final int Pt_SELECTION_MODE_AUTO = 0x8200;
	public static final int Pt_SELECTION_MODE_MULTIPLE = 0x8000;
	public static final int Pt_SELECTION_MODE_NOCLEAR = 0x8400;
	public static final int Pt_SELECTION_MODE_NOFOCUS = 0x9000;
	public static final int Pt_SELECTION_MODE_NOMOVE = 0x8080;
	public static final int Pt_SELECTION_MODE_SINGLE = 0x8020;
	public static final int Pt_SELECTION_MODE_TOGGLE = 0x8800;
	public static final int Pt_SELECT_NOREDRAW = 0x80000;
	public static final int Pt_SEP_HORIZONTAL = 0x1;
	public static final int Pt_SEP_VERTICAL = 0x0;
	public static final int Pt_SET = 0x2;
	public static final int Pt_SET_DRAW_F = 0x170014;
	public static final int Pt_SHOW_TITLE = 0x100;
	public static final int Pt_STATIC_GRADIENT = 0x100000;
	public static final int Pt_TEXT_IMAGE = 0x8;
	public static final int Pt_TOGGLE = 0x8;
	public static final int Pt_TOOLBAR_DRAGGABLE = 0x1;
	public static final int Pt_TOOLBAR_END_SEPARATOR = 0x40;
	public static final int Pt_TOP = 0x0;
	public static final int Pt_TOP_ANCHORED_TOP = 0x00000400;
	public static final int Pt_TOP_BEVEL = 0x100;
	public static final int Pt_TOP_ETCH = 0x1;
	public static final int Pt_TOP_INLINE = 0x1000;
	public static final int Pt_TOP_OUTLINE = 0x10;
	public static final int Pt_VERTICAL = 0x0;
	public static final int Pt_WEB_ACTION_DISPLAY = 0x1;
	public static final int Pt_WEB_DATA_HEADER = 1;
	public static final int Pt_WEB_DATA_BODY = 2;
	public static final int Pt_WEB_DATA_CLOSE = 3;
	public static final int Pt_WEB_DIRECTION_BACK = 6;
	public static final int Pt_WEB_DIRECTION_CANCEL = 7;
	public static final int Pt_WEB_DIRECTION_FWD = 5;
	public static final int Pt_WEB_STATUS_CONNECT = 2;
	public static final int Pt_WEB_STATUS_DEFAULT = 6;
	public static final int Pt_WEB_STATUS_INFO = 4;
	public static final int Pt_WEB_STATUS_MOUSE = 1;
	public static final int Pt_WEB_STATUS_PROGRESS = 3;
	public static final int Pt_WEB_STATUS_PRINT = 5;
	public static final int Pt_Z_STRING = 0x1;

/** Natives */
public static final native int PfDecomposeStemToID(byte[] pkszStem);
public static final native int PfExtentText(PhRect_t extent, PhPoint_t pos, int font, int str, int len);
public static final native int PfExtentText(PhRect_t extent, PhPoint_t pos, byte[] font, byte[] str, int len);
public static final native int PfExtentWideText(PhRect_t extent, PhPoint_t pos, byte[] font, char[] str, int len);
public static final native int PfFontDescription(int ptsID);
public static final native int PfFontFlags(int ptsID);
public static final native int PfFontSize(int ptsID);
public static final native int PfFreeFont(int ptsID);
public static final native int PfGenerateFontName(byte[] pkucDescription, int kuiFlags, int kuiSize, byte[] pucBuff);
public static final native int PfLoadMetrics(byte[] font);
public static final native int PfQueryFontInfo(byte[] font, FontQueryInfo info);
public static final native int PfQueryFonts(int symbol, int flags, int list, int n);
public static final native void PgAlphaOff();
public static final native void PgAlphaOn();
public static final native int PgCreateGC(int size);
public static final native void PgDestroyGC(int GC);
public static final native int PgDrawArc(PhPoint_t center, PhPoint_t radii, int start, int end, int flags);
public static final native void PgDrawArrow(PhRect_t rect, short unknown, int color, int flags);
public static final native int PgDrawBitmap(int ptr, int flags, PhPoint_t pos, PhDim_t size, int bpl, int tag);
public static final native int PgDrawEllipse(PhPoint_t center, PhPoint_t radii, int flags);
public static final native int PgDrawGradient(PhPoint_t ul, PhPoint_t lr, int gradient_type, int transition_type, int num_color_pts, int color1, int color2, int color3, int color4, int table_size, byte[] transition_table);
public static final native int PgDrawILine(int x1, int y1, int x2, int y2);
public static final native int PgDrawIPixel(int x,int y);
public static final native int PgDrawIRect(int ulx, int uly, int lrx, int lry, int flags);
public static final native int PgDrawImage(int ptr, int type, PhPoint_t pos, PhDim_t size, int bpl, int tag);
public static final native int PgDrawMultiTextArea(byte[] text, int len, PhRect_t canvas, int text_flags, int canvas_flags, int linespacing);
public static final native int PgDrawPhImageRectmx(PhPoint_t pos, int image, PhRect_t rect, int flags);
public static final native int PgDrawPolygon(short[] ptr, int num, PhPoint_t pos, int flags);
public static final native int PgDrawRoundRect(PhRect_t rect, PhPoint_t radii, int flags);
public static final native int PgDrawTImage(int ptr, int type, PhPoint_t pos, PhDim_t size, int bpl, int tag, int TransPtr, int TransBPl);
public static final native int PgDrawText(byte[] ptr, int len, PhPoint_t pos, int flags);
public static final native int PgExtentMultiText(PhRect_t extent, PhPoint_t pos, byte[] font, byte[] str, int n, int linespacing);
public static final native int PgFlush();
public static final native int PgGetVideoMode(PgDisplaySettings_t settings);
public static final native int PgGetVideoModeInfo(short mode_number, PgVideoModeInfo_t mode_info);
public static final native int PgReadScreen(PhRect_t rect, int buffer);
public static final native int PgReadScreenSize(PhRect_t rect);
public static final native void PgSetAlpha(int alpha_op, PgMap_t src_alpha_map, int src_alpha_gradient, byte src_global_alpha, byte dst_global_alpha);
public static final native void PgSetClipping(short n, int rects);
public static final native int PgSetDrawBufferSize(int cmd_buf_len);
public static final native int PgSetDrawMode(int mode);
public static final native int PgSetFillColor(int color);
public static final native void PgSetFillTransPat(byte[] pat);
public static final native void PgSetFont(byte[] ff);
public static final native int PgSetGC(int GC);
public static final native int PgSetMultiClip(int num, int clip_list);
public static final native int PgSetPalette(int palette, int palette_id, short first_color, short num_colors, int flags, int tag);
public static final native void PgSetRegion(int rid);
public static final native int PgSetStrokeCap(int cap);
public static final native int PgSetStrokeColor(int color);
public static final native void PgSetStrokeDash(byte[] DashList, int ListLen, int DashScale);
public static final native int PgSetStrokeJoin(int join);
public static final native int PgSetStrokeWidth(int width);
public static final native int PgSetTextColor(int color);
public static final native void PgSetTextXORColor(int frgd, int bkgd);
public static final native void PgSetUserClip(PhRect_t ClipRect);
public static final native int PgShmemCreate(int size, byte[] name);
public static final native int PgShmemDestroy(int addr);
public static final native int PhAddMergeTiles(int tiles, int add_tiles, int[] added);
public static final native void PhAreaToRect(PhArea_t area, PhRect_t rect);
public static final native void PhBlit(int rid, PhRect_t rect, PhPoint_t offset);
public static final native int PhClipTilings(int tiles, int clip_tiles, int[] intersection);
public static final native int PhClipboardCopy(short ig, int n, byte[] clip);
public static final native int PhClipboardCopyString(short ig, byte[] string);
public static final native void PhClipboardPasteFinish(int cbdata);
public static final native int PhClipboardPasteStart(short ig);
public static final native int PhClipboardPasteString(short ig);
public static final native int PhClipboardPasteType(int cbdata, byte[] type);
public static final native int PhClipboardPasteTypeN(int cbdata, int n);
public static final native int PhCoalesceTiles(int tiles);
public static final native int PhCopyTiles(int tile);
public static final native int PhCreateImage(PhImage_t buffer, short width, short height, int type, int palette, int ncolors, int shmem);
public static final native int PhDCSetCurrent(int draw_context);
public static final native int PhDeTranslateTiles(int tile, int point_subtract);
public static final native int PhEventNext(int buffer, int size);
public static final native int PhEventPeek(int buffer, int size);
public static final native void PhFreeTiles(int tiles);
public static final native int PhGetData(int event);
public static final native int PhGetMsgSize(int event_buf);
public static final native int PhGetRects(int event);
public static final native int PhGetTile();
public static final native int PhInitDrag(int rid, int flags, PhRect_t rect, PhRect_t boundary, int input_group, PhDim_t min, PhDim_t max, PhDim_t step, PhPoint_t ptrpos, short[] cursor);
public static final native int PhInputGroup(int event);
public static final native int PhIntersectTilings(int tile1, int tile2, short[] num_intersect_tiles);
public static final native int PhKeyToMb(byte[] buffer,PhKeyEvent_t keyevent);
public static final native int PhMakeGhostBitmap(int image);
public static final native int PhMakeTransBitmap(int image, int trans_color);
public static final native int PhMergeTiles(int tiles);
public static final native void PhMoveCursorAbs(int input_group, int x, int y);
public static final native int PhQueryCursor(short ig, PhCursorInfo_t buf);
public static final native int PhQueryRids(int flags, int rid, int input_group, int type, int sense, int emitter, PhRect_t rect, int[] rids, int num);
public static final native int PhRectIntersect(int rect1, int rect2);
public static final native int PhRectUnion(int rect1, int rect2);
public static final native int PhRectUnion(PhRect_t rect1, PhRect_t rect2);
public static final native int PhRectsToTiles(int rects, int num_rects);
public static final native int PhRegionQuery(int rid, PhRegion_t region, PhRect_t rect, int data, int data_len);
public static final native void PhReleaseImage(int image);
public static final native int PhSortTiles(int tiles);
public static final native int PhTilesToRects(int tiles, int[] num_rects);
public static final native int PhTranslateTiles(int tile, PhPoint_t point_add);
public static final native int PhWindowQueryVisible(int flag, int rid, int input_group, PhRect_t rect);
public static final native int PiCropImage(int image, PhRect_t bounds, int flags);
public static final native int PiDuplicateImage(int image, int flags);
public static final native int PmMemCreateMC(int image, PhDim_t dim, PhPoint_t translation);
public static final native int PmMemFlush(int mc, int image);
public static final native void PmMemReleaseMC(int mc);
public static final native int PmMemStart(int mc);
public static final native int PmMemStop(int mc);
public static final native void PtAddCallback(int widget, int callback_type, int callback, int data);
public static final native void PtAddEventHandler(int widget, int event_mask, int callback, int data);
public static final native void PtAddFilterCallback(int widget, int event_mask, int callback, int data);
public static final native void PtAddHotkeyHandler(int widget, int key_sym_cap, int key_mods, short flags, int data, int callback);
public static final native int PtAlert(int parent, PhPoint_t location, byte[] title, int image, byte[] message, byte[] msgFont, int btnCount, int[] buttons, int[] btnFonts, int defBtn, int escBtn, int flags);
public static final native int PtAppAddInput(int app_context, int pid, int input_func, int data);
public static final native int PtAppAddWorkProc(int app_context, int work_func, int data);
public static final native int PtAppCreatePulse(int app, int priority);
public static final native int PtAppDeletePulse(int app, int pulse_pid);
public static final native void PtAppProcessEvent(int app_context);
public static final native int PtAppPulseTrigger(int app, int pulse);
public static final native void PtAppRemoveInput(int app_context, int input_id);
public static final native void PtAppRemoveWorkProc(int app_context, int WorkProc_id);
public static final native int PtBeep();
public static final native int PtBlit(int widget, PhRect_t source, PhPoint_t delta);
public static final native int PtBlockAllWindows(int skip, short cursor, int cursor_color);
public static final native int PtBlockWindow(int window, short cursor, int cursor_color);
public static final native int PtButton();
public static final native void PtCalcBorder(int widget, PhRect_t rect);
public static final native int PtCalcCanvas(int widget, PhRect_t canvas_rect);
public static final native int PtClippedBlit(int widget, int src, PhPoint_t delta, int clip);
public static final native int PtColorSelect(int parent, byte[] title, PtColorSelectInfo_t info);
public static final native int PtComboBox();
public static final native int PtContainer();
public static final native int PtContainerFindFocus(int family_member);
public static final native int PtContainerFocusNext(int widget, PhEvent_t event);
public static final native int PtContainerFocusPrev(int widget, PhEvent_t event);
public static final native int PtContainerGiveFocus(int widget, PhEvent_t event);
public static final native int PtContainerHold(int container_widget);
public static final native int PtContainerRelease(int container_widget);
public static final native int PtCreateAppContext();
public static final native int PtCreateWidget(int clazz, int parent, int n_args, int[] args);
public static final native int PtCreateWidgetClass(int superclass_ref, int size, int num_args, int[] arg);
public static final native int PtDamageExtent(int widget, PhRect_t extent);
public static final native int PtDamageWidget(int widget);
public static final native int PtDestroyWidget(int widget);
public static final native int PtDisjoint();
public static final native int PtEnter(int flags);
public static final native int PtEventHandler(int event);
public static final native int PtExtentWidget(int widget);
public static final native int PtExtentWidgetFamily(int widget);
public static final native int PtFileSelection(int parent, PhPoint_t pos, byte[] title, byte[] root_dir, byte[] file_spec, byte[] btn1, byte[] btn2, byte[] format, PtFileSelectionInfo_t info, int flags);
public static final native int PtFindDisjoint(int widget);
public static final native int PtFlush();
public static final native int PtFontSelection(int parent, PhPoint_t pos, byte[] title, byte[] font, int symbol, int flags, byte[] sample);
public static final native int PtForwardWindowEvent(PhWindowEvent_t event);
public static final native void PtFrameSize(int widget, int border_width, int[] left_border, int[] top_border, int[] right_border, int[] bottom_border);
public static final native void PtGetAbsPosition(int widget, short[] x, short[] y);
public static final native int PtGetResources(int widget, int n_args, int[] args);
public static final native int PtGlobalFocusNext(int widget, PhEvent_t event);
public static final native int PtGlobalFocusNextContainer(int widget, PhEvent_t event);
public static final native int PtGlobalFocusPrev(int widget, PhEvent_t event);
public static final native int PtGlobalFocusPrevContainer(int widget, PhEvent_t event);
public static final native int PtGroup();
public static final native int PtHit(int container, int n, PhRect_t rect);
public static final native int PtHold();
public static final native int PtInflateBalloon(int win, int me, int position, byte[] string, byte[] font, int fill, int text_color);
public static final native int PtInit(byte[] name);
public static final native int PtIsFocused(int widget);
public static final native int PtLabel();
public static final native int PtLeave(int flags);
public static final native int PtList();
public static final native int PtListAddItems(int widget, int[] items, int item_count, int position);
public static final native int PtListDeleteAllItems(int widget);
public static final native int PtListDeleteItemPos(int widget, int item_count, int position);
public static final native void PtListGotoPos(int widget, int pos);
public static final native int PtListItemPos(int widget, byte[] item);
public static final native int PtListReplaceItemPos(int widget, int[] new_items, int item_count, int position);
public static final native void PtListSelectPos(int widget, int pos);
public static final native void PtListUnselectPos(int widget, int pos);
public static final native void PtMainLoop();
public static final native int PtMenu();
public static final native int PtMenuBar();
public static final native int PtMenuButton();
public static final native int PtMultiText();
public static final native int PtNextTopLevelWidget(int widget);
public static final native int PtNumericInteger ();
public static final native int PtPane();
public static final native int PtPanelGroup();
public static final native void PtPositionMenu(int widget, PhEvent_t event);
public static final native int PtProgress();
public static final native int PtReParentWidget(int widget, int parent);
public static final native int PtRealizeWidget(int widget);
public static final native int PtRegion();
public static final native int PtRelease();
public static final native void PtRemoveCallback(int widget, int callback_type, int callback, int data);
public static final native void PtRemoveHotkeyHandler(int widget, int key_sym_cap, int key_mods, short flags, int data, int callback);
public static final native int PtScrollArea();
public static final native int PtScrollContainer();
public static final native int PtScrollbar();
public static final native void PtSendEventToWidget(int widget, int event);
public static final native int PtSeparator();
public static final native int PtSetAreaFromWidgetCanvas(int widget, PhRect_t canvas_rect, PhArea_t area);
public static final native int PtSetParentWidget(int widget);
public static final native int PtSetResource(int widget, int type, int value, int length);
public static final native int PtSetResources(int widget, int n_args, int[] args);
public static final native int PtSlider();
public static final native void PtSuperClassDraw(int wc_ref, int widget, int damage);
public static final native int PtSyncWidget(int widget);
public static final native int PtText();
public static final native int PtTextGetSelection(int widget, int[] start, int[] end);
public static final native int PtTextModifyText(int widget, int start, int end, int insert_pos, byte[] text, int length);
public static final native int PtTextModifyText(int widget, int start, int end, int insert_pos, int text, int length);
public static final native int PtTextSetSelection(int widget, int[] start, int[] end);
public static final native int PtTimer();
public static final native int PtToggleButton();
public static final native int PtToolbar();
public static final native void PtUnblockWindows(int bl);
public static final native int PtUnrealizeWidget(int widget);
public static final native int PtValidParent(int widget_parent, int class_ref);
public static final native int PtWebClient();
public static final native int PtWidgetArea(int widget, PhArea_t area);
public static final native int PtWidgetBrotherBehind(int widget);
public static final native int PtWidgetBrotherInFront(int widget);
public static final native int PtWidgetCanvas(int widget, int canvas_rect);
public static final native int PtWidgetCanvas(int widget, PhRect_t canvas_rect);
public static final native int PtWidgetChildBack(int widget);
public static final native int PtWidgetChildFront(int widget);
public static final native int PtWidgetClass(int widget);
public static final native int PtWidgetExtent(int widget, int extent);
public static final native int PtWidgetExtent(int widget, PhRect_t extent);
public static final native int PtWidgetFlags(int widget);
public static final native int PtWidgetInsert(int widget, int new_sibling, int behind);
public static final native int PtWidgetIsClassMember(int widget, int clazz);
public static final native boolean PtWidgetIsRealized(int widget);
public static final native int PtWidgetOffset(int widget, PhPoint_t offset);
public static final native int PtWidgetParent(int widget);
public static final native int PtWidgetPreferredSize(int widget, PhDim_t dim);
public static final native int PtWidgetRid(int widget);
public static final native int PtWidgetToBack(int widget);
public static final native int PtWidgetToFront(int widget);
public static final native int PtWindow();
public static final native int PtWindowFocus(int widget);
public static final native int PtWindowGetState(int widget);
public static final native void PtWindowToBack(int widget);
public static final native void PtWindowToFront(int widget);
public static final native void memmove(int dest, PhPoint_t src, int size);
public static final native void memmove(int dest, PhTile_t src, int size);
public static final native void memmove(PhTile_t dest, int src, int size);
public static final native void memmove(PtCallbackInfo_t dest, int src, int size);
public static final native void memmove(PhWindowEvent_t dest, int src, int size);
public static final native void memmove(int dest, PhRect_t src, int size);
public static final native void memmove(PhRect_t dest, int src, int size);
public static final native void memmove(int dest, PhImage_t src, int size);
public static final native void memmove(PhImage_t dest, int src, int size);
public static final native void memmove(FontDetails dest, int src, int size);
public static final native void memmove(int dest, PhPointerEvent_t src, int size);
public static final native void memmove(PhPointerEvent_t dest, int src, int size);
public static final native void memmove(PhEvent_t dest, int src, int size);
public static final native void memmove(int dest, PhEvent_t src, int size);
public static final native void memmove(PhKeyEvent_t dest, int src, int size);
public static final native void memmove(PtScrollbarCallback_t dest, int src, int size);
public static final native void memmove(int dest, PhArea_t src, int size);
public static final native void memmove(PgAlpha_t dest, int src, int size);
public static final native void memmove(int dest, PgAlpha_t src, int size);
public static final native void memmove(PtTextCallback_t dest, int src, int size);
public static final native void memmove(int dest, PtTextCallback_t src, int size);
public static final native void memmove(PgMap_t dest, int src, int size);
public static final native void memmove(int dest, PhCursorDef_t src, int size);
public static final native void memmove(PhClipHeader dest, int src, int size);
public static final native void memmove(byte[] dest, PhClipHeader src, int size);
public static final native void memmove(PtWebStatusCallback_t dest, int src, int size);
public static final native void memmove(PtWebDataReqCallback_t dest, int src, int size);
public static final native void memmove(PtWebWindowCallback_t dest, int src, int size);
public static final native void memmove(PtWebMetaDataCallback_t dest, int src, int size);
public static final native void memmove(int dest, PtWebClientData_t src, int size);
public static final native int strdup(int string);
public static final native int uname(utsname udata);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7491.java