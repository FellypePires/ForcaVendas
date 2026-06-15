package com.faculdade.forcavendas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.faculdade.forcavendas.data.dao.ClienteDao
import com.faculdade.forcavendas.data.dao.EnderecoDao
import com.faculdade.forcavendas.data.dao.ItemDao
import com.faculdade.forcavendas.data.dao.PedidoDao
import com.faculdade.forcavendas.data.entity.Cliente
import com.faculdade.forcavendas.data.entity.Endereco
import com.faculdade.forcavendas.data.entity.Item
import com.faculdade.forcavendas.data.entity.PedidoItem
import com.faculdade.forcavendas.data.entity.PedidoVenda

@Database(
    entities = [
        Endereco::class,
        Cliente::class,
        Item::class,
        PedidoVenda::class,
        PedidoItem::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun enderecoDao(): EnderecoDao
    abstract fun clienteDao(): ClienteDao
    abstract fun itemDao(): ItemDao
    abstract fun pedidoDao(): PedidoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "forca_vendas.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
