package cat.uib.secom.crypto.afc.android.store.entities;

import cat.uib.secom.crypto.afc.android.store.entities.UserAFC.UserAFCColumns;
import cat.uib.secom.crypto.afc.android.store.entities.UserAFCJourney.UserAFCJourneyColumns;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AFCClientManager {

	private static final String TAG = AFCClientManager.class.getName();
	
	private static final String DATABASE_NAME = "afc-client.db";
	private static final int DATABASE_VERSION = 2;
	
	private static final String BBS_KEYS_TABLE_NAME = "BBS_KEYS";
	private static final String USER_AFC_JORNEY_TABLE_NAME = "USER_AFC_JOURNEY";
	
	public static final String USER_KEYS = BBS_KEYS_TABLE_NAME;
	public static final String USER_AFC_JOURNEY = USER_AFC_JORNEY_TABLE_NAME;
	
	
	private static class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + BBS_KEYS_TABLE_NAME + " ("
					+ UserAFCColumns._ID + " INTEGER PRIMARY KEY,"
					+ UserAFCColumns.AI + " BLOB,"
					+ UserAFCColumns.XI + " BLOB,"
					+ UserAFCColumns.G1 + " BLOB,"
					+ UserAFCColumns.G2 + " BLOB,"
					+ UserAFCColumns.H + " BLOB,"
					+ UserAFCColumns.U + " BLOB,"
					+ UserAFCColumns.V + " BLOB,"
					+ UserAFCColumns.OMEGA + " BLOB"
					+ ");");
			
			db.execSQL("CREATE TABLE " + USER_AFC_JORNEY_TABLE_NAME + " ("
					+ UserAFCJourneyColumns._ID + " INTEGER PRIMARY KEY,"
					+ UserAFCJourneyColumns.TIN_K + " BLOB,"
					+ UserAFCJourneyColumns.TIN_R1 + " BLOB,"
					+ UserAFCJourneyColumns.TIN_SERIAL_NUMBER + " INTEGER,"
					+ UserAFCJourneyColumns.TIN_SIGNATURE + " TEXT,"
					+ UserAFCJourneyColumns.TIN_SOURCE_STATION + " INTEGER,"
					+ UserAFCJourneyColumns.TIN_TAU_1 + " BLOG,"
					+ UserAFCJourneyColumns.TIN_VALIDITY_TIME + " INTEGER,"
					+ UserAFCJourneyColumns.TIN_OMEGA_DELTAU + " BLOB,"
					+ UserAFCJourneyColumns.TIN_OMEGA_HK + " BLOB,"
					+ UserAFCJourneyColumns.TIN_OMEGA_S1 + " BLOB,"
					+ UserAFCJourneyColumns.TIN_OMEGA_SIGN_C + " BLOB,"
					+ UserAFCJourneyColumns.TIN_OMEGA_SIGN_DELTA2 + " BLOB,"
					+ UserAFCJourneyColumns.TIN_OMEGA_SIGN_SALPHA + " BLOB,"
					+ UserAFCJourneyColumns.TIN_OMEGA_SIGN_SBETA + " BLOB,"
					+ UserAFCJourneyColumns.TIN_OMEGA_SIGN_SDELTA1 + " BLOB,"
					+ UserAFCJourneyColumns.TIN_OMEGA_SIGN_SX + " BLOB,"
					+ UserAFCJourneyColumns.TIN_OMEGA_SIGN_T1 + " BLOB,"
					+ UserAFCJourneyColumns.TIN_OMEGA_SIGN_T2 + " BLOB,"
					+ UserAFCJourneyColumns.TIN_OMEGA_SIGN_T3 + " BLOB,"
					+ UserAFCJourneyColumns.TOUT_DESTINATION_STATION + " INTEGER,"
					+ UserAFCJourneyColumns.TOUT_FARE + " REAL,"
					+ UserAFCJourneyColumns.TOUT_MESSAGE + " TEXT,"
					+ UserAFCJourneyColumns.TOUT_SERIAL_NUMBER + " INTEGER,"
					+ UserAFCJourneyColumns.TOUT_SIGNATURE + " TEXT");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + BBS_KEYS_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + USER_AFC_JORNEY_TABLE_NAME);
		}
		
	}
	
	private DatabaseHelper mOpenHelper;
	
	public long insert(String table, ContentValues values) {
		Log.v(TAG, "insert values");
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			long rowId = db.insert(table, "", values);
			if (rowId > 0) {
				db.setTransactionSuccessful();
				return rowId;
			}
			throw new SQLException("Failed to insert row into " + table);
		} finally {
			db.endTransaction();
		}
	}

	public int update(String table, long rowId, ContentValues values) {
		Log.v(TAG, "update values");
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			String whereClause = UserAFCColumns._ID + "=?";
			String[] whereArgs = {Long.toString(rowId)};
			int count = db.update(table, values, whereClause, whereArgs);
			return count;
		} finally {
			db.endTransaction();
		}
	}
	
	
}
