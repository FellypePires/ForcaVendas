package com.faculdade.forcavendas.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.faculdade.forcavendas.data.entity.Cliente
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {

    @Insert
    suspend fun inserir(cliente: Cliente): Long

    @Query("SELECT * FROM cliente ORDER BY codigo DESC")
    fun listarTodos(): Flow<List<Cliente>>

    @Query("SELECT * FROM cliente WHERE codigo = :codigo")
    suspend fun buscarPorCodigo(codigo: Long): Cliente?
}
