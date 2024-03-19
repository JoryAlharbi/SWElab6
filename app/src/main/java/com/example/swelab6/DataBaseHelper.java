package com.example.swelab6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String STUDENT_TABLE = "Student_Table";
    public static final String COLUMN_STUDENT_NAME = "STUDENT_NAME";
    public static final String COLUMN_STUDENT_AGE = "STUDENT_AGE";
    public static final String COLUMN_ID = "ID";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "student.db", null, 1);
    }

    // when creating the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + STUDENT_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STUDENT_NAME + " TEXT, " + COLUMN_STUDENT_AGE + " INT )";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    // when upgrading
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean addOne(StudentMod studentMod) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STUDENT_NAME, studentMod.getName());
        cv.put(COLUMN_STUDENT_AGE, studentMod.getAge());
        long insert = db.insert(STUDENT_TABLE, null, cv);
        db.close(); // Close the database connection
        return insert != -1;
    }

    public boolean deleteOne(StudentMod studentMod) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_ID + "=?";
        String[] whereArgs = {String.valueOf(studentMod.getId())};
        int deletedRows = db.delete(STUDENT_TABLE, whereClause, whereArgs);
        db.close(); // Close the database connection
        return deletedRows > 0;
    }

    public List<StudentMod> getEveryone() {
        List<StudentMod> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + STUDENT_TABLE, null);

        if (cursor != null) {
            int columnIndexID = cursor.getColumnIndex(COLUMN_ID);
            int columnIndexName = cursor.getColumnIndex(COLUMN_STUDENT_NAME);
            int columnIndexAge = cursor.getColumnIndex(COLUMN_STUDENT_AGE);

            while (cursor.moveToNext()) {
                int SID = -1;
                String SName = null;
                int SAge = -1;

                if (columnIndexID != -1) {
                    SID = cursor.getInt(columnIndexID);
                }
                if (columnIndexName != -1) {
                    SName = cursor.getString(columnIndexName);
                }
                if (columnIndexAge != -1) {
                    SAge = cursor.getInt(columnIndexAge);
                }

                // Check if all columns are valid before adding the student to the list
                if (SID != -1 && SName != null && SAge != -1) {
                    StudentMod newStudent = new StudentMod(SID, SName, SAge);
                    returnList.add(newStudent);
                }
            }

            cursor.close();
        }

        db.close(); // Close the database connection
        return returnList;
    }

}
