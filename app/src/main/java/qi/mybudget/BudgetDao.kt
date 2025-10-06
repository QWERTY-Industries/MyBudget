package qi.mybudget

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BudgetDao {
    //Q Commented code is database not creating table and throw compile error
//    @Query("SELECT * FROM budget WHERE uid LIKE :userId")
//    fun findBudgetsByUserId(userId: String): List<Budget>
//
//    @Insert
//    fun createExpense(vararg expense: Budget)
}