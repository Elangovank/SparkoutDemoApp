package com.elango.demoapp.model

import androidx.room.*


@Entity(tableName = "map")
data class MapModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "auto_id")
    var autoId: Int = 0,
    @ColumnInfo(name = "mobile")
    val mobile: String,
    @ColumnInfo(name = "lat")
    val lat: String,
    @ColumnInfo(name = "lng")
    val lng: String,
    @ColumnInfo(name = "address")
    val address: String
)

@Dao
interface MapDataDAO {
    @Query("SELECT * FROM map")
    fun getMapDetails(): List<MapModel>

    @Insert
    fun insertAll(mData: ArrayList<MapModel>)

    @Insert
    fun insert(mData: MapModel)

    @Delete
    fun delete(data: MapModel)
}