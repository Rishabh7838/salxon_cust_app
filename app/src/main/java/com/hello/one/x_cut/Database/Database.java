package com.hello.one.x_cut.Database;

//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteQuery;
////import android.database.sqlite.SQLiteQueryBuilder;
//
//import com.hello.one.x_cut.Model.Bookings;
//import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
//
//import java.util.ArrayList;
//import java.util.List;

/**
 * Created by one on 3/14/2018.
 */

public class Database{
        //extends SQLiteAssetHelper{
//
//
//    private static final String DB_Name = "SalxonStyleDB.db";
//    private static final int DB_VER = 1;
//    private Context context;
//
//    public Database(Context context) {
//        super(context, DB_Name, null, DB_VER);
//        this.context=context;
//    }
//
//   public List<Bookings> getCarts()
//   {
//       SQLiteDatabase db = getReadableDatabase();
//       SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
//
//       String[] sqlSelect={"BookingId","Service_Name","TimeStamp","Price","Discount"};
//
//       String TableName = "BookingDetails";
//       qb.setTables(TableName);
//
//       Cursor cursor = qb.query(db,sqlSelect,null,null,null,null,null);
//
//       final List<Bookings> result = new ArrayList<>();
//       if(cursor.moveToFirst()){
//           do{
//
//               result.add(new Bookings(cursor.getString(cursor.getColumnIndex("BookingId")),
//                       cursor.getString(cursor.getColumnIndex("Service_Name")),
//                       cursor.getString(cursor.getColumnIndex("TimeStamp")),
//                       cursor.getString(cursor.getColumnIndex("Price")),
//                       cursor.getString(cursor.getColumnIndex("Discount"))
//               ));
//           } while (cursor.moveToNext());
//       }
//    return  result;
//
//   }
//
//   public void addCart(Bookings booking)
//   {
//       SQLiteDatabase db = getReadableDatabase();
//       String query = String.format("INSERT INTO BookingDetails(BookingId,Service_Name,TimeStamp,Price,Discount) VALUES('%s' ,'%s' ,'%s' ,'%s' ,'%s');",
//               booking.getBookingId(),
//               booking.getService_Name(),
//               booking.getTimeStamp(),
//               booking.getPrice(),
//               booking.getDisscount());
//
//       db.execSQL(query);
//   }
//
//   public void cleanCart()
//   {
//       SQLiteDatabase db = getReadableDatabase();
//       String query = String.format("DELETE FROM BookingDetails");
//       db.execSQL(query);
//   }
}
