package com.sinosoftyingda.fastclaim.common.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.sinosoftyingda.fastclaim.R;
import com.sinosoftyingda.fastclaim.common.utils.Log;
import com.sinosoftyingda.fastclaim.more.service.MoreServlet;

/***
 * 数据库助手类
 * 
 * @author chenjianfan
 * 
 */
public class FastClaimDbHelper extends SQLiteOpenHelper {
	private static final String TAG = "FastClaimDbHelper";
	private Context context;
	// 数据库创建地址
	private static String dbPath = "";
	// 数据库名
	private static String dbName = "";
	// 数据库版�?
	private static int dbversion = 1;
	private SQLiteDatabase myDataBase = null;
	private MoreServlet moreServlet;

	/**
	 * 在SQLiteOpenHelper的子类当中，必须有该构造函�?
	 * 
	 * @param context
	 *            上下文对�?
	 * @param name
	 *            数据库名�?
	 * @param factory
	 *            一般都是null
	 * @param version
	 *            当前数据库的版本，值必须是整数并且是递增的状�?
	 */
	public FastClaimDbHelper(Context context, String name, CursorFactory factory, int version) {
		// 必须通过super调用父类当中的构造函�?
		super(context, name, null, version);
		this.context = context;
		initAddress(context);
	}

	/**
	 * 加载数据库创建地址及名�?
	 * 
	 * @param context
	 */
	private static void initAddress(Context context) {
		dbPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + context.getString(R.string.DBPath);
		dbName = context.getString(R.string.DBName);
	}

	public FastClaimDbHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public FastClaimDbHelper(Context context, String name) {
		this(context, name, dbversion);
	}

	public FastClaimDbHelper(Context context) {
		this(context, dbPath + dbName);
	}

	/***
	 * 检查数据库是否有效
	 * 
	 * @return
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		String myPath = dbPath + dbName;
		try {
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/***
	 * 创建数据�?
	 * 
	 * @throws IOException
	 */
	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			// 数据库已存在，do nothing.
			if (moreServlet == null)
				moreServlet = new MoreServlet(context);
			SharedPreferences sp = context.getSharedPreferences("DBFirst", 0);
			String DBFirst = sp.getString("DBFirst", null);
			/***
			 * 在sd中的数据库ClContent表增加一个字�?
			 */
			if ("1.1.14".equals(moreServlet.getVersion())) { 

				File dbf = new File(dbPath + dbName);
					Cursor cursor=null;
					System.out.println("新增加字�?);
					SQLiteDatabase db1 = SQLiteDatabase.openOrCreateDatabase(dbf, null);
					try {
						//1.13 增加赔付率页面显示提�?
						cursor = db1.rawQuery("SELECT COMPENSATERATE from TASKQUERY", null);	
						cursor.close();
					} catch (Exception e) {
						//1.13 增加赔付率页面显示提�?
						db1.execSQL("ALTER TABLE TASKQUERY ADD COMPENSATERATE  TEXT  DEFAULT '' ");
						if(cursor!=null){
							cursor.close();
						}
						
					}
					db1.close();
			}
		} else {
			File dir = new File(dbPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File dbf = new File(dbPath + dbName);
			if (dbf.exists()) {
				dbf.delete();
			} else {
				// 创建库文�?
				dbf.createNewFile();
				// 批量创建库文件中的表
				try {
					InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open("fastclaimdb.sql"), "GBK");
					BufferedReader bufReader = new BufferedReader(inputReader);
					SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbf, null);
					String sqlUpdate = null;
 
					while ((sqlUpdate = bufReader.readLine()) != null) {
						if (!TextUtils.isEmpty(sqlUpdate)) {
							Log.i(TAG, "批量sql语句" + sqlUpdate);
							// String[] versionS = sqlUpdate.split("=");
							// if ("version".equals(versionS[0]))
							// version = Integer.parseInt(versionS[1]);
							// if ("newversion".equals(versionS[0]))
							// newversion = Integer.parseInt(versionS[1]);
							db.execSQL(sqlUpdate);
						}
					}

					bufReader.close();
					inputReader.close();
					db.close();
				} catch (SQLException e) {
					Log.i(TAG, e.getMessage());
				} catch (IOException e) {
					Log.i(TAG, e.getMessage());
				}
			}

		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "执行了没�?);
		try {
			createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public synchronized void close() {
		if (myDataBase != null) {
			myDataBase.close();
		}
		super.close();
	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		File dbf = new File(dbPath + dbName);
		myDataBase = SQLiteDatabase.openOrCreateDatabase(dbf, null);
		// return super.getReadableDatabase();
		return myDataBase;
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		File dbf = new File(dbPath + dbName);
		// return super.getWritableDatabase();
		myDataBase = SQLiteDatabase.openOrCreateDatabase(dbf, null);
		return myDataBase;
	}
}
