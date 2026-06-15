package com.faculdade.forcavendas.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.faculdade.forcavendas.data.entity.Endereco
import kotlinx.coroutines.flow.Flow

@Dao
interface EnderecoDao {

    @Insert
    suspend fun inserir(endereco: Endereco): Long

    @Query("SELECT * FROM endereco ORDER BY codigo DESC")
    fun listarTodos(): Flow<List<Endereco>>

    @Query("SELECT * FROM endereco WHERE codigo = :codigo")
    suspend fun buscarPorCodigo(codigo: Long): Endereco?
}
