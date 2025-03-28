package com.example.budgetapp2.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat
import java.util.Locale

@Entity(tableName = "expenses")
data class BudgetItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val cost: Double,
    val amount: Double,
    val category: String,
    val frequency: Double,
    val date: String,
    val amountPerWeek: Double = amount * cost / frequency,
    //https://api.stlouisfed.org/fred/series/observations?series_id=GNPCA&api_key=abcdefghijklmnopqrstuvwxyz123456
    val seriesId: String? = null
)

public fun amountToCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(amount)
}

/*
public var tempBudgetItems = mutableListOf(
    BudgetItem(1, "Groceries", 50.0, "Food", "2023-04-01"),
    BudgetItem(2, "Gas", 20.0, "Transportation", "2023-04-02"),
    BudgetItem(3, "Rent", 1000.0, "Housing", "2023-04-03"),
    BudgetItem(4, "Utilities", 50.0, "Utilities", "2023-04-04"),
    BudgetItem(5, "Entertainment", 30.0, "Entertainment", "2023-04-05"),
    BudgetItem(6, "Health", 20.0, "Health", "2023-04-06"),
    BudgetItem(7, "Example Expense 1", 50.0, "Examples", "2023-04-01"),
    BudgetItem(8, "Example Expense 2", 20.0, "Examples", "2023-04-02"),
    BudgetItem(9, "Example Expense 3", 1000.0, "Examples", "2023-04-03"),
    BudgetItem(10, "Example Expense 4", 50.0, "Examples", "2023-04-04"),
    BudgetItem(11, "Example Expense 5", 30.0, "Examples", "2023-04-05"),
    BudgetItem(12, "Example Expense 6", 20.0, "Examples", "2023-04-06"),
    BudgetItem(13, "Example Expense 7", 50.0, "Examples", "2023-04-01"),
    BudgetItem(14, "Example Expense 8", 20.0, "Examples", "2023-04-02"),
    BudgetItem(15, "Example Expense 9", 1000.0, "Examples", "2023-04-03"),
    BudgetItem(16, "Example Expense 10", 50.0, "Examples", "2023-04-04"),


)
 */