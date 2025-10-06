package qi.mybudget

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expense(
    @PrimaryKey val eid: Int,
    @ColumnInfo(name = "uid") val uid: Int,
    @ColumnInfo(name = "cid") val cid: Category,
    @ColumnInfo(name = "expense_name") val expenseName: String,
    @ColumnInfo(name = "expense_amount") val expenseAmount: Double?
)