package qi.mybudget

data class Goal(
    val description: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val timeInWeeks: Int
)