package com.faculdade.forcavendas.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.faculdade.forcavendas.data.entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert
    suspend fun inserir(item: Item): Long

    @Query("SELECT * FROM item ORDER BY codigo DESC")
    fun listarTodos(): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE codigo = :codigo")
    suspend fun buscarPorCodigo(codigo: Long): Item?
}
