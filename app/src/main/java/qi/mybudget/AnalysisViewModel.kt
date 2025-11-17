package qi.mybudget

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// Make sure your class extends ViewModel()
class AnalysisViewModel : ViewModel() {

    // LiveData to hold the list of all transactions for the user
    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> get() = _transactions

    // LiveData to hold summaries
    private val _totalExpenses = MutableLiveData<Double>()
    val totalExpenses: LiveData<Double> get() = _totalExpenses

    private val _totalIncome = MutableLiveData<Double>()
    val totalIncome: LiveData<Double> get() = _totalIncome

    private val _wallet = MutableLiveData<Wallet>()
    val wallet: LiveData<Wallet> get() = _wallet

    private val _monthlyExpenses = MutableLiveData<Double>()
    val monthlyExpenses: LiveData<Double> get() = _monthlyExpenses

    private val _monthlyIncome = MutableLiveData<Double>()
    val monthlyIncome: LiveData<Double> get() = _monthlyIncome

    private val _budgets = MutableLiveData<List<Budget>>()
    val budgets: LiveData<List<Budget>> get() = _budgets


    private val auth = FirebaseAuth.getInstance()
    // It's better to get the reference to the top-level node here
    private val database = FirebaseDatabase.getInstance().reference

    init {
        fetchUserTransactions()
        fetchUserWallet() // Fetch wallet info when ViewModel is created
        fetchUserBudgets()

    }

    private fun fetchUserBudgets() {
        val userId = auth.currentUser?.uid ?: return

        // Listen for changes on the user's "budgets" node
        database.child("budgets").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val budgetList = mutableListOf<Budget>()
                for (budgetSnapshot in snapshot.children) {
                    val budget = budgetSnapshot.getValue(Budget::class.java)
                    budget?.let { budgetList.add(it) }
                }
                _budgets.postValue(budgetList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Log.e("AnalysisViewModel", "Failed to read budgets.", error.toException())
            }
        })
    }
    // --- NEW: Function to create a new budget ---
    fun createBudget(categoryName: String, minLimit: Double?, maxLimit: Double?) {
        val userId = auth.currentUser?.uid ?: return

        // Use push() to generate a unique key for the budget
        val budgetNode = database.child("budgets").child(userId)
        val budgetId = budgetNode.push().key ?: return

        val newBudget = Budget(
            uid = userId,
            budgetId = budgetId,
            categoryName = categoryName,
            maxLimit = maxLimit,
            minLimit = minLimit
        )

        // Save the new budget object using its unique ID
        budgetNode.child(budgetId).setValue(newBudget)
    }
    // --- NEW FUNCTION to fetch the wallet ---
    private fun fetchUserWallet() {
        val userId = auth.currentUser?.uid ?: return

        // Listen for changes on the user's specific wallet node
        database.child("wallets").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val walletData = snapshot.getValue(Wallet::class.java) ?: Wallet()
                _wallet.postValue(walletData)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    // --- NEW FUNCTION to update the wallet ---
    fun updateWallet(cash: Double, card: Double, savings: Double) {
        val userId = auth.currentUser?.uid ?: return
        val walletUpdate = Wallet(cash, card, savings)


        // Set the value at the user's specific wallet node
        database.child("wallets").child(userId).setValue(walletUpdate)
    }
    private fun fetchUserTransactions() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _transactions.postValue(emptyList()) // Post empty list if not logged in
            return
        }

        // --- THE CRITICAL FIX IS HERE ---
        // Instead of querying the top-level "transactions" node,
        // we directly access the node for the current user.
        // This perfectly matches your saving logic.
        val userTransactionsRef = database.child("transactions").child(userId)

        userTransactionsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val transactionList = mutableListOf<Transaction>()
                for (transactionSnapshot in snapshot.children) {
                    val transaction = transactionSnapshot.getValue(Transaction::class.java)
                    transaction?.let { transactionList.add(it) }
                }

                // This will now be called every time a transaction is added, changed, or removed.
                _transactions.postValue(transactionList)

                // This function call is the key to updating all your totals.
                calculateSummaries(transactionList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Log the error for debugging
                // For example: Log.e("AnalysisViewModel", "Failed to fetch transactions", error.toException())
            }
        })
    }

    fun saveTransaction(
        transactionType: String,
        category: String,
        description: String,
        amount: Double,
        paymentSource: String // <-- The new parameter
    ) {
        val currentUser = auth.currentUser ?: return

        // --- Step 1: Save the transaction (this part is the same) ---
        val transactionsRef = database.child("transactions").child(currentUser.uid)
        val transactionId = transactionsRef.push().key ?: return

        val newTransaction = Transaction(
            uid = currentUser.uid,
            transactionId = transactionId,
            transactionType = transactionType,
            category = category,
            description = description,
            amount = amount,
            date = System.currentTimeMillis()
        )

        transactionsRef.child(transactionId).setValue(newTransaction)
            .addOnSuccessListener {
                // --- Step 2: Update the correct wallet balance ---

                // Get the current wallet's value. If it's null, start with a fresh wallet.
                val currentWallet = _wallet.value ?: Wallet()

                // Initialize new balances from the current wallet state
                var newCashBalance = currentWallet.cashBalance ?: 0.0
                var newCardBalance = currentWallet.cardBalance ?: 0.0
                var newSavingsBalance = currentWallet.savingsBalance ?: 0.0

                // Use the paymentSource to determine which balance to update
                when (paymentSource) {
                    "Cash" -> {
                        if (transactionType == "Expense") newCashBalance -= amount else newCashBalance += amount
                    }
                    "Card" -> {
                        if (transactionType == "Expense") newCardBalance -= amount else newCardBalance += amount
                    }
                    "Savings" -> {
                        if (transactionType == "Expense") newSavingsBalance -= amount else newSavingsBalance += amount
                    }
                }

                // Create a new Wallet object with the updated balances
                val updatedWallet = Wallet(
                    cashBalance = newCashBalance,
                    cardBalance = newCardBalance,
                    savingsBalance = newSavingsBalance
                )

                // Save the entire updated wallet object back to Firebase
                database.child("wallets").child(currentUser.uid).setValue(updatedWallet)
            }
            .addOnFailureListener {
                // Handle failure
            }
    }

    private fun calculateSummaries(transactions: List<Transaction>) {
        // --- Calculate ALL-TIME totals (for Analysis screen) ---
        val allTimeExpenses = transactions.filter { it.transactionType == "Expense" }.sumOf { it.amount ?: 0.0 }
        val allTimeIncome = transactions.filter { it.transactionType == "Income" }.sumOf { it.amount ?: 0.0 }

        _totalExpenses.postValue(allTimeExpenses)
        _totalIncome.postValue(allTimeIncome)

        // --- NEW: Calculate CURRENT MONTH totals (for Overview screen) ---
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        val monthlyTransactions = transactions.filter { transaction ->
            transaction.date?.let {
                calendar.timeInMillis = it
                val transactionMonth = calendar.get(Calendar.MONTH)
                val transactionYear = calendar.get(Calendar.YEAR)
                transactionMonth == currentMonth && transactionYear == currentYear
            } ?: false
        }

        val currentMonthExpenses = monthlyTransactions.filter { it.transactionType == "Expense" }.sumOf { it.amount ?: 0.0 }
        val currentMonthIncome = monthlyTransactions.filter { it.transactionType == "Income" }.sumOf { it.amount ?: 0.0 }

        _monthlyExpenses.postValue(currentMonthExpenses)
        _monthlyIncome.postValue(currentMonthIncome)
    }
}