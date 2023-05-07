package com.madm.common_libs


import androidx.room.*

@Dao
interface DAORandomEntity {
    @Insert
    fun insertAll(entities : List<RandomEntity>)

    @Insert
    fun insert(entity : RandomEntity)

    @Update
    fun update(entity : RandomEntity)

    @Delete
    fun delete(entity : RandomEntity)
}