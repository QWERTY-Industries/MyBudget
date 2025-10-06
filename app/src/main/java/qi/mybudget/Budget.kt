package qi.mybudget

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Budget(
    @PrimaryKey val bid: Int,
    @ColumnInfo(name = "uid") val uid: Int,
    @ColumnInfo(name = "budget_name") val expenseName: String,
    @ColumnInfo(name = "min_amount") val expenseAmount: Double,
    @ColumnInfo(name = "max_amount") val category: Double
)