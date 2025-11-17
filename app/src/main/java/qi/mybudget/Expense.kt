package qi.mybudget

data class Expense(
    val eid: Int = 0,
    val uid: Int = 0,
    val cid: Int = 0,
    val expenseName: String = "",
    val expenseAmount: Double = 0.0
)