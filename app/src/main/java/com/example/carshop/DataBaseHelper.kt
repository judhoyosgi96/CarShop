package com.example.carshop

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASENAME = "CARSDB"
val TABLENAME = "Cars"
val COL_ID = "id"
val COL_MODEL = "model"
val COL_SEATS = "seats"
val COL_DATE = "date"
val COL_CAT = "category"
val COL_CON = "condition"
val COL_PRICE = "price"
val COL_AUX = "aux"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(
    context, DATABASENAME, null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLENAME ($COL_ID INT PRIMARY KEY, $COL_MODEL VARCHAR(64),$COL_SEATS INTEGER,$COL_DATE INTEGER," +
                    "$COL_CAT VARCHAR(64),$COL_CON VARCHAR(64),$COL_PRICE INTEGER,$COL_AUX VARCHAR(64))"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //onCreate(db);
    }

    fun insertData(car: CarItem) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID, car.id)
        contentValues.put(COL_MODEL, car.model)
        contentValues.put(COL_SEATS, car.seats)
        contentValues.put(COL_DATE, car.date)
        contentValues.put(COL_CAT, car.category)
        contentValues.put(COL_CON, car.condition)
        contentValues.put(COL_PRICE, car.price)
        contentValues.put(COL_SEATS, car.seats)
        contentValues.put(COL_AUX, car.aux)
        val result = database.insert(TABLENAME, null, contentValues)
    }

    fun modifyData(id:Int,car:CarItem) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID, car.id)
        contentValues.put(COL_MODEL, car.model)
        contentValues.put(COL_SEATS, car.seats)
        contentValues.put(COL_DATE, car.date)
        contentValues.put(COL_CAT, car.category)
        contentValues.put(COL_CON, car.condition)
        contentValues.put(COL_PRICE, car.price)
        contentValues.put(COL_SEATS, car.seats)
        contentValues.put(COL_AUX, car.aux)
        val result = database.update(TABLENAME, contentValues, "ID=$id",null)
    }

    fun readData(): MutableList<CarItem> {
        val list: MutableList<CarItem> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME ORDER BY $COL_ID DESC"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val car = CarItem(
                    result.getString(result.getColumnIndex(COL_ID)).toInt(),
                    result.getString(result.getColumnIndex(COL_MODEL)),
                    result.getString(result.getColumnIndex(COL_SEATS)).toInt(),
                    result.getString(result.getColumnIndex(COL_DATE)).toInt(),
                    result.getString(result.getColumnIndex(COL_CAT)),
                    result.getString(result.getColumnIndex(COL_CON)),
                    result.getString(result.getColumnIndex(COL_PRICE)).toInt(),
                    result.getString(result.getColumnIndex(COL_AUX))
                )
                list.add(car)
            } while (result.moveToNext())
        }
        return list
    }
}