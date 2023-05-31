package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Item이 유일한 클래스. 테이블 스키마를 변경할때마다 버전이 올라간다. 스키마 버전 기록 백업을 유지하지 않도록 exportSchema false.
@Database(entities = [Item::class], version = 2, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    //데이터베이스를 만들거나 가져오는 메서드에 대한 액세스를 허용하고 클래스 이름을 한정자로 사용
    companion object {
        @Volatile
        private var Instance: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            //instance가 null일때 synchronized 블록내에 초기화. 있다면 기존 데이터베이스 반환
            return Instance?: synchronized(this) {
                //데이터베이스 가져오기
                //차례대로 애플리케이션 컨텍스트, 데이터베이스 클래스, 데이터베이스 이름
                Room.databaseBuilder(context, InventoryDatabase::class.java, "item_database")
                        //유형 불일치 오류
                    .fallbackToDestructiveMigration()
                    .build()
                        //db 인스턴스에 대한 참조 유지
                    .also { Instance = it }
            }
        }
    }

}