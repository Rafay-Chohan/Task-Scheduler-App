package com.example.taskscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SchedulerDB {
    final String DATABASE_NAME = "Taskdb";
    final String TABLE_NAME = "task_table";
    final String COLUMN_ID = "task_id";
    final String COLUMN_TITLE = "task_title";
    final String COLUMN_DESCRIPTION = "task_description";
    final String COLUMN_DATE = "task_date";
    final String COLUMN_STATUS = "task_status";
    final String COLUMN_NOTIFIED="task_notified";
    final String TABLE2_NAME = "notification_table";
    final String COLUMN2_ID = "_id";
    final String COLUMN2_TITLE = "notification_message";
    final String COLUMN2_DATE = "notification_date";

    final int DATABASE_VERSION = 9;

    MyOpenHelper helper;
    SQLiteDatabase sqLiteDatabase;

    Context context;
    public SchedulerDB(Context c)
    {
        context = c;
    }

    public void open()
    {
        helper = new MyOpenHelper(context);
        sqLiteDatabase = helper.getWritableDatabase();

    }

    public long addTask(Tasks task)
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, task.getTitle());
        cv.put(COLUMN_DESCRIPTION, task.getDescription());
        cv.put(COLUMN_DATE, task.getDate());
        cv.put(COLUMN_STATUS, task.getStatus());

        return sqLiteDatabase.insert(TABLE_NAME, null, cv);
    }

    public long addNotification(Notifications notification, int taskId){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN2_TITLE, notification.getMessage());
        cv.put(COLUMN2_DATE, notification.getDate());
        cv.put(COLUMN_ID,taskId);

        return sqLiteDatabase.insert(TABLE2_NAME, null, cv);
    }

    public int updateTask(Tasks task)
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, task.getTitle());
        cv.put(COLUMN_DESCRIPTION, task.getDescription());
        cv.put(COLUMN_DATE, task.getDate());
        cv.put(COLUMN_STATUS, task.getStatus());

        return sqLiteDatabase.update(TABLE_NAME, cv, COLUMN_ID+"=?", new String[]{String.valueOf(task.getId())});
    }
    public int updateNotification(Notifications Notification)
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN2_TITLE, Notification.getMessage());
        cv.put(COLUMN2_DATE, Notification.getDate());

        return sqLiteDatabase.update(TABLE2_NAME, cv, COLUMN2_ID+"=?", new String[]{String.valueOf(Notification.getId())});
    }


    public int deleteTask(int id)
    {
        return sqLiteDatabase.delete(TABLE_NAME, COLUMN_ID+"=?", new String[]{String.valueOf(id)});
    }
    public int deleteNotification(int id)
    {
        // Query tasks due within the next hour
        String query = "SELECT * FROM " + TABLE2_NAME +
                " WHERE "+COLUMN2_ID+" = ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(id)});

        int index_id = cursor.getColumnIndex(COLUMN_ID);
        cursor.moveToNext();
        int taskId = cursor.getInt(index_id);
        UnmarkTaskAsNotified(taskId);

        return sqLiteDatabase.delete(TABLE2_NAME, COLUMN2_ID+"=?", new String[]{String.valueOf(id)});
    }

    public void close() {
        sqLiteDatabase.close();
        helper.close();
    }

    public ArrayList<Tasks> readPendingTaskData() {
        String data = "";
        String []columns = new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION,COLUMN_DATE,COLUMN_STATUS};

        Cursor c = sqLiteDatabase.query(TABLE_NAME, columns, null, null, null, null, COLUMN_DATE+" ASC");

        ArrayList<Tasks> task=new ArrayList<>();
        int index_id = c.getColumnIndex(COLUMN_ID);
        int index_title = c.getColumnIndex(COLUMN_TITLE);
        int index_description = c.getColumnIndex(COLUMN_DESCRIPTION);
        int index_date=c.getColumnIndex(COLUMN_DATE);
        int index_status=c.getColumnIndex(COLUMN_STATUS);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
        {
            if(LocalDateTime.now().compareTo(stringToLocalDateTime(c.getString(index_date)))<0)
                task.add(new Tasks(Integer.parseInt(c.getString(index_id)),c.getString(index_title),c.getString(index_description),c.getString(index_date),c.getString(index_status)));
        }
        c.close();
        return task;
    }
    public ArrayList<Tasks> readPastTaskData() {
        String data = "";
        String []columns = new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_DESCRIPTION,COLUMN_DATE,COLUMN_STATUS};

        Cursor c = sqLiteDatabase.query(TABLE_NAME, columns, null, null, null, null, COLUMN_DATE+" DESC");
        ArrayList<Tasks> task=new ArrayList<>();

        int index_id = c.getColumnIndex(COLUMN_ID);
        int index_title = c.getColumnIndex(COLUMN_TITLE);
        int index_description = c.getColumnIndex(COLUMN_DESCRIPTION);
        int index_date=c.getColumnIndex(COLUMN_DATE);
        int index_status=c.getColumnIndex(COLUMN_STATUS);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
        {
            if(LocalDateTime.now().compareTo(stringToLocalDateTime(c.getString(index_date)))>0)
                task.add(new Tasks(Integer.parseInt(c.getString(index_id)),c.getString(index_title),c.getString(index_description),c.getString(index_date),c.getString(index_status)));
        }

        c.close();

        return task;
    }

    public ArrayList<Notifications> readNotificationsData(){
        String data = "";
        String []columns = new String[]{COLUMN2_ID, COLUMN2_TITLE, COLUMN2_DATE,COLUMN_ID};

        Cursor c = sqLiteDatabase.query(TABLE2_NAME, columns, null, null, null, null, COLUMN2_DATE+" ASC");
        ArrayList<Notifications> notification=new ArrayList<>();

        int index_id = c.getColumnIndex(COLUMN2_ID);
        int index_title = c.getColumnIndex(COLUMN2_TITLE);
        int index_date=c.getColumnIndex(COLUMN2_DATE);
        int index_task_id=c.getColumnIndex(COLUMN_ID);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
        {
                notification.add(new Notifications(Integer.parseInt(c.getString(index_id)),Integer.parseInt(c.getString(index_task_id)),c.getString(index_title),c.getString(index_date)));
        }
        c.close();

        return notification;
    }

    public void checkUpcomingTasks(){
        NotificationHelper notiHelper=new NotificationHelper(context);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        String currentTime = sdf.format(calendar.getTime());

        calendar.set(Calendar.HOUR_OF_DAY, 23);  // 11 PM (23 in 24-hour format)
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        String today = sdf.format(calendar.getTime());

        // Query tasks due within the next hour
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE "+COLUMN_DATE+" > ? AND "+COLUMN_DATE+" <= ?" + " AND " + COLUMN_NOTIFIED + " = 0";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{currentTime, today});

        int index_id = cursor.getColumnIndex(COLUMN_ID);
        int index_title = cursor.getColumnIndex(COLUMN_TITLE);
        int index_date=cursor.getColumnIndex(COLUMN_DATE);

        while (cursor.moveToNext()) {
            int taskId = cursor.getInt(index_id);
            String taskName = cursor.getString(index_title);
            String taskTime = cursor.getString(index_date);

            try {
                // Calculate remaining time
                Date dueDate = sdf.parse(taskTime);
                long diffInMillis = dueDate.getTime() - System.currentTimeMillis();

                String timeLeft;
                if (diffInMillis > TimeUnit.MINUTES.toMillis(50)) {
                    timeLeft = "1 hour";
                } else {
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
                    timeLeft = minutes + " minutes";
                }

                Notifications notifications=new Notifications(0,"Task: "+taskName+" is Due in +"+timeLeft,localDateTimeToString(LocalDateTime.now()));
                addNotification(notifications,taskId);
                notiHelper.showTaskNotification(taskName,timeLeft);
                markTaskAsNotified(taskId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
    }

    private void markTaskAsNotified(int taskId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFIED, 1);
        sqLiteDatabase.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)});
    }
    private void UnmarkTaskAsNotified(int taskId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFIED, 0);
        sqLiteDatabase.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)});
    }
    class MyOpenHelper extends SQLiteOpenHelper
    {

        public MyOpenHelper(Context c)
        {
            super(c, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE "+TABLE_NAME+"("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN_TITLE+" TEXT NOT NULL, "+COLUMN_DESCRIPTION+" TEXT NOT NULL, "+COLUMN_DATE+" TEXT NOT NULL, "+COLUMN_STATUS+" TEXT NOT NULL,"+COLUMN_NOTIFIED+" INTEGER DEFAULT 0);";
            db.execSQL(query);
            String query2 = "CREATE TABLE "+TABLE2_NAME+"("+COLUMN2_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COLUMN2_TITLE+" TEXT NOT NULL, "+COLUMN2_DATE+" TEXT NOT NULL,"+COLUMN_ID+" INTEGER );";
            db.execSQL(query2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS "+TABLE2_NAME);
            onCreate(db);
        }
    }
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static LocalDateTime stringToLocalDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(dateString, formatter);
    }

    public static String localDateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return dateTime.format(formatter);
    }
}
