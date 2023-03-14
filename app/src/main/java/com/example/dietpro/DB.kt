package com.example.dietpro

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DB (context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, databaseName, factory, databaseVersion) {

    companion object{
        lateinit var database: SQLiteDatabase

        private val databaseName = "dietproApp.db"
        private val databaseVersion = 1

        const val mealTableName = "Meal"
        const val mealColTableId = 0
        const val mealColMealId = 1
        const val mealColMealName = 2
        const val mealColCalories = 3
        const val mealColDates = 4
        const val mealColImages = 5
        const val mealColAnalysis = 6
        const val mealColUserName = 7

        val mealCols: Array<String> = arrayOf<String>("tableId", "mealId", "mealName", "calories", "dates", "images", "analysis", "userName")
        const val mealNumberCols = 7

   }
private val mealCreateSchema =
    "create table Meal (tableId integer primary key autoincrement" +
        ", mealId VARCHAR(50) not null"+
        ", mealName VARCHAR(50) not null"+
        ", calories double not null"+
        ", dates VARCHAR(50) not null"+
        ", images VARCHAR(50) not null"+
        ", analysis VARCHAR(50) not null"+
        ", userName VARCHAR(50) not null"+
    ")"

    override fun onCreate(db: SQLiteDatabase) {
         db.execSQL(mealCreateSchema)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + mealCreateSchema)
        onCreate(db)
    }

    fun onDestroy() {
        database.close()
    }

    fun listMeal(): ArrayList<MealVO> {
        val res = ArrayList<MealVO>()
        database = readableDatabase
        val cursor: Cursor =
            database.query(mealTableName, mealCols, null, null, null, null, null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast()) {
			res.add(setData(cursor))
            cursor.moveToNext()
        }
        cursor.close()
        return res
    }

    fun createMeal(mealvo: MealVO) {
        database = writableDatabase
        val wr = ContentValues(mealNumberCols)
        wr.put(mealCols[mealColMealId], mealvo.getMealId())
        wr.put(mealCols[mealColMealName], mealvo.getMealName())
        wr.put(mealCols[mealColCalories], mealvo.getCalories())
        wr.put(mealCols[mealColDates], mealvo.getDates())
        wr.put(mealCols[mealColImages], mealvo.getImages())
        wr.put(mealCols[mealColAnalysis], mealvo.getAnalysis())
        wr.put(mealCols[mealColUserName], mealvo.getUserName())
        database.insert(mealTableName, mealCols[1], wr)
    }

    fun searchByMealmealId(value: String): ArrayList<MealVO> {
            val res = ArrayList<MealVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, mealId, mealName, calories, dates, images, analysis, userName from Meal where mealId = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
				res.add(setData(cursor))
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }

    fun searchByMealmealName(value: String): ArrayList<MealVO> {
            val res = ArrayList<MealVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, mealId, mealName, calories, dates, images, analysis, userName from Meal where mealName = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
				res.add(setData(cursor))
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }

    fun searchByMealcalories(value: String): ArrayList<MealVO> {
            val res = ArrayList<MealVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, mealId, mealName, calories, dates, images, analysis, userName from Meal where calories = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
				res.add(setData(cursor))
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }

    fun searchByMealdates(value: String): ArrayList<MealVO> {
            val res = ArrayList<MealVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, mealId, mealName, calories, dates, images, analysis, userName from Meal where dates = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
				res.add(setData(cursor))
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }

    fun searchByMealimages(value: String): ArrayList<MealVO> {
            val res = ArrayList<MealVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, mealId, mealName, calories, dates, images, analysis, userName from Meal where images = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
				res.add(setData(cursor))
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }

    fun searchByMealanalysis(value: String): ArrayList<MealVO> {
            val res = ArrayList<MealVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, mealId, mealName, calories, dates, images, analysis, userName from Meal where analysis = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
				res.add(setData(cursor))
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }

    fun searchByMealuserName(value: String): ArrayList<MealVO> {
            val res = ArrayList<MealVO>()
	        database = readableDatabase
	        val args = arrayOf(value)
	        val cursor: Cursor = database.rawQuery(
	            "select tableId, mealId, mealName, calories, dates, images, analysis, userName from Meal where userName = ?",
	            args
	        )
	        cursor.moveToFirst()
	        while (!cursor.isAfterLast()) {
	            res.add(setData(cursor))
	            cursor.moveToNext()
	        }
	        cursor.close()
	        return res
	    }


    fun editMeal(mealvo: MealVO) {
        database = writableDatabase
        val args = arrayOf(mealvo.getMealId())
        database.update(mealTableName, putData(mealvo), "mealId =?", args)
    }

    fun deleteMeal(value: String) {
        database = writableDatabase
        val args = arrayOf(value)
        database.delete(mealTableName, "mealId = ?", args)
    }

		val check = "mealId = ?"
    fun addUsereatsMeal(userName: String, mealId: String) {
        database = writableDatabase
        val wr = ContentValues(1)
        wr.put(mealCols.get(mealColUserName), userName)
        val args = arrayOf(mealId)
        database.update(mealTableName, wr, check, args)
    }
    fun removeUsereatsMeal(userName: String, mealId: String) {
        database = writableDatabase
        val wr = ContentValues(1)
        wr.put(mealCols.get(mealColUserName), "NULL")
        val args = arrayOf(mealId)
        database.update(mealTableName, wr, check, args)
    }

	private fun setData(cursor: Cursor): MealVO {
		val mealvo = MealVO()
		mealvo.setMealId(cursor.getString(mealColMealId))
		mealvo.setMealName(cursor.getString(mealColMealName))
		mealvo.setCalories(cursor.getDouble(mealColCalories))
		mealvo.setDates(cursor.getString(mealColDates))
		mealvo.setImages(cursor.getString(mealColImages))
		mealvo.setAnalysis(cursor.getString(mealColAnalysis))
		mealvo.setUserName(cursor.getString(mealColUserName))

		return mealvo
	}

	private fun putData(mealvo: MealVO): ContentValues {
		val wr = ContentValues(mealNumberCols)
		wr.put(mealCols[mealColMealId], mealvo.getMealId())
		wr.put(mealCols[mealColMealName], mealvo.getMealName())
		wr.put(mealCols[mealColCalories], mealvo.getCalories())
		wr.put(mealCols[mealColDates], mealvo.getDates())
		wr.put(mealCols[mealColImages], mealvo.getImages())
		wr.put(mealCols[mealColAnalysis], mealvo.getAnalysis())
		wr.put(mealCols[mealColUserName], mealvo.getUserName())
		return wr
	}
}
