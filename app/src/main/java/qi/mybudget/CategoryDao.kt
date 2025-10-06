package qi.mybudget

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {
    //Q Commented code is database not creating table and throw compile error
//    @Query("SELECT * FROM expense WHERE uid LIKE :userId")
//    fun findCategoriesByUserId(userId: Int): List<Category>
//
//    @Insert
//    fun createCategory(vararg category: Category)
}