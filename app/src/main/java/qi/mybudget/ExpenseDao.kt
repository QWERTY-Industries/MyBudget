package qi.mybudget

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    //Q Commented code is database not creating table and throw compile error
//    @Query("SELECT * FROM expense WHERE uid LIKE :userId")
//    fun findExpensesByUserId(userId: String): List<Expense>
//
//    @Insert
//    fun createExpense(vararg expense: Expense)
}