package com.example.sakshi.imdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.id;

/**
 * Created by sakshi on 9/7/2017.
 */

public class Database extends SQLiteOpenHelper {
    Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Movie Database";
    private static final String TABLE_MOVIEDETAILS = "Movies";
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_POPULARITY = "popularity";
    private static final String COLUMN_RELEASE_DATE = "release_date";
    private static final String COLUMN_POSTER_PATH = "poster_path";
    private static final String COLUMN_VOTE_AVERAGE = "vote_average";
    private static final String COLUMN_VOTE_COUNT = "vote_count";
    private static final String COLUMN_IS_FAVORITE = "favorite";
    private static final String COLUMN_IS_WATCHLIST = "watchlist";
    private static final String DATATYPE_NUMERIC = " INTEGER";
    private static final String DATATYPE_VARCHAR = " TEXT";
    private static final String OPEN_BRACE = "(";
    private static final String COMMA = ",";

    private static final String CREATE_TABLE_MOVIEDETAILS = new StringBuilder()
            .append(CREATE_TABLE).append(TABLE_MOVIEDETAILS).append(OPEN_BRACE)
            .append(COLUMN_ID).append(DATATYPE_VARCHAR + COMMA)
            .append(COLUMN_TITLE).append(DATATYPE_VARCHAR + COMMA)
            .append(COLUMN_RELEASE_DATE).append(DATATYPE_VARCHAR + COMMA)
            .append(COLUMN_POSTER_PATH).append(DATATYPE_VARCHAR + COMMA)
            .append(COLUMN_VOTE_AVERAGE).append(DATATYPE_VARCHAR + COMMA)
            .append(COLUMN_VOTE_COUNT).append(DATATYPE_VARCHAR + COMMA)
            .append(COLUMN_IS_FAVORITE).append(DATATYPE_NUMERIC).append(" DEFAULT 0").append(COMMA)
            .append(COLUMN_IS_WATCHLIST).append(DATATYPE_NUMERIC + " DEFAULT 0" + COMMA)
            .append(COLUMN_POPULARITY).append(DATATYPE_VARCHAR + COMMA)
            .append("UNIQUE(").append(COLUMN_ID).append(") ON CONFLICT REPLACE)").toString();

    public Database(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MOVIEDETAILS);
    }

    public void addMovie(DetailsModel model) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, model.getId());
        values.put(COLUMN_TITLE, model.getTitle());
        values.put(COLUMN_RELEASE_DATE, model.getRelease_date());
        values.put(COLUMN_POSTER_PATH, model.getPoster_path());
        values.put(COLUMN_VOTE_AVERAGE, model.getVote_average());
        values.put(COLUMN_VOTE_COUNT, model.getVote_count());
        values.put(COLUMN_POPULARITY, model.getPopularity());
        db.insert(TABLE_MOVIEDETAILS, null, values);
        db.close();
    }

/*    public Model getMovie(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOVIEDETAILS, new String[] {COLUMN_TITLE, COLUMN_RELEASE_DATE,
                        COLUMN_POSTER_PATH, COLUMN_VOTE_AVERAGE, COLUMN_VOTE_COUNT, COLUMN_IS_FAVORITE, COLUMN_IS_WATCHLIST}, COLUMN_ID + "=?",
                new String[] {id}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Model model = new Model();
        try {
            model.setId(id);
            model.setTitle(cursor.getString(0));
            model.setRelease_date(cursor.getString(1));
            model.setPoster_path(cursor.getString(2));
            model.setVote_average(cursor.getString(3));
            model.setVote_count(cursor.getString(4));
            model.setIsFav(String.valueOf(cursor.getInt(5)));
            model.setIsWatchlist(String.valueOf(cursor.getInt(6)));
        }catch (Exception e){
           e.printStackTrace();
        }
        db.close();
        return model;
    }*/

    public boolean checkMovie(String id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            //added false here
            Cursor cursor = db.query(false, TABLE_MOVIEDETAILS, new String[]{COLUMN_ID}, COLUMN_ID + " = " + id, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public DetailsModel getMovie(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOVIEDETAILS, new String[]{COLUMN_TITLE, COLUMN_RELEASE_DATE,
                        COLUMN_POSTER_PATH, COLUMN_VOTE_AVERAGE, COLUMN_VOTE_COUNT, COLUMN_IS_FAVORITE, COLUMN_IS_WATCHLIST}, COLUMN_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        DetailsModel detailsModel = new DetailsModel();
        detailsModel.setId(id);
        detailsModel.setTitle(cursor.getString(1));
        detailsModel.setRelease_date(cursor.getString(2));
        detailsModel.setPoster_path(cursor.getString(3));
        detailsModel.setVote_average(cursor.getString(4));
        detailsModel.setVote_count(cursor.getString(5));
        detailsModel.setIsFav(cursor.getString(6));
        detailsModel.setIsWatchlist(cursor.getString(72));
        return detailsModel;
    }
    public boolean updateMovieW(String id, String val) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "select * from " + TABLE_MOVIEDETAILS + " where " + COLUMN_ID + " is " + id;
            Cursor cursor = db.rawQuery(query, null);
            String status = null;

            if (val.equals("1")) {
                ContentValues newValues = new ContentValues();
                newValues.put(COLUMN_IS_WATCHLIST, "1");
                db.update(TABLE_MOVIEDETAILS, newValues, COLUMN_ID + "= " + id, null);
                Toast.makeText(context, "Added to WatchList", Toast.LENGTH_SHORT).show();
                return true;
            } else if (val.equals("0")) {
                ContentValues newValues = new ContentValues();
                newValues.put(COLUMN_IS_WATCHLIST, "0");
                db.update(TABLE_MOVIEDETAILS, newValues, COLUMN_ID + "= " + id, null);
                Toast.makeText(context, "Removed from WatchList", Toast.LENGTH_SHORT).show();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateMovieF(String id, String val) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "select * from " + TABLE_MOVIEDETAILS + " where " + COLUMN_ID + " is " + id;
            Cursor cursor = db.rawQuery(query, null);
            if (val.equals("1")) {
                ContentValues newValues = new ContentValues();
                newValues.put(COLUMN_IS_FAVORITE, "1");
                db.update(TABLE_MOVIEDETAILS, newValues, COLUMN_ID + "= " + id, null);
                Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
                return true;
            } else if (val.equals("0")) {
                ContentValues newValues = new ContentValues();
                newValues.put(COLUMN_IS_FAVORITE, "0");
                db.update(TABLE_MOVIEDETAILS, newValues, COLUMN_ID + "= " + id, null);
                Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;

}
    public String checkfav(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from "+TABLE_MOVIEDETAILS+" where id = "+id;
        Cursor cursor = db.rawQuery(query,null);
       String fav=null;
        if(cursor!=null && cursor.getCount()>0 && cursor.moveToFirst()) {

            fav = cursor.getString(cursor.getColumnIndex("favorite"));
        }
        return fav;

    }
    public String checkwatch(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from "+TABLE_MOVIEDETAILS+" where id = "+id;
        Cursor cursor = db.rawQuery(query,null);
        String watch=null;
        if(cursor!=null && cursor.getCount()>0 && cursor.moveToFirst()) {

            watch = cursor.getString(cursor.getColumnIndex("watchlist"));
        }
        return watch;

    }


    /*public ArrayList<HashMap<String,String>> getFavorites() {
        ArrayList<HashMap<String,String>> allMovies = new ArrayList<HashMap<String,String>>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MOVIEDETAILS,
                new String[]{COLUMN_ID,COLUMN_TITLE,COLUMN_RELEASE_DATE,COLUMN_POSTER_PATH,
                        COLUMN_VOTE_AVERAGE,COLUMN_VOTE_COUNT,COLUMN_IS_FAVORITE}
                ,COLUMN_IS_FAVORITE+"=?",new String[]{String.valueOf(1)},null,null,null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> movieInfo = new HashMap<String,String>();
                movieInfo.put("id", cursor.getString(0));
                movieInfo.put("title", cursor.getString(1));
                movieInfo.put("release_date", cursor.getString(2));
                movieInfo.put("poster_path", cursor.getString(3));
                movieInfo.put("vote_average", cursor.getString(4));
                movieInfo.put("vote_count", cursor.getString(5));
                movieInfo.put("FAV", String.valueOf(cursor.getInt(6)));
                if (cursor.getInt(6) == 1) {
                    allMovies.add(movieInfo);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allMovies;
    }*/
    public ArrayList<Model> getFav(){
        ArrayList<Model> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from "+TABLE_MOVIEDETAILS+" where favorite = 1 ";
        Cursor cursor = db.rawQuery(query,null);
        Model data = null;
        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                data = new Model();
                data.setTitle(cursor.getString(1));
                data.setRelease_date(cursor.getString(2));
                data.setPoster_path(cursor.getString(3));
                data.setVote_average(cursor.getString(4));
                data.setVote_count(cursor.getString(5));
                data.setIsFav(cursor.getString(6));
                data.setIsWatchlist(cursor.getString(7));
                data.setPopularity(cursor.getString(8));
                data.setId(cursor.getString(0));
                list.add(data);

            }while(cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<Model> getWatchList(){
        ArrayList<Model> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select * from "+TABLE_MOVIEDETAILS+" where watchlist = 1 ";
        Cursor cursor = db.rawQuery(query,null);
        Model data = null;
        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                data = new Model();
                data.setTitle(cursor.getString(1));
                data.setRelease_date(cursor.getString(2));
                data.setPoster_path(cursor.getString(3));
                data.setVote_average(cursor.getString(4));
                data.setVote_count(cursor.getString(5));
                data.setIsFav(cursor.getString(6));
                data.setIsWatchlist(cursor.getString(7));
                data.setPopularity(cursor.getString(8));
                data.setId(cursor.getString(0));
                list.add(data);

            }while(cursor.moveToNext());
        }
        return list;
    }

//    public void deleteNonFavWatchMovie() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_MOVIEDETAILS, COLUMN_IS_FAVORITE + "=? AND "+COLUMN_IS_WATCHLIST+"=?", new String[] {String.valueOf(0),String.valueOf(0)});
//        db.close();
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIEDETAILS);
        onCreate(db);
    }
}
