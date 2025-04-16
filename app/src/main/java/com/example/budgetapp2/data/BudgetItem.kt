package com.example.budgetapp2.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat
import java.util.Locale

@Entity(tableName = "expenses")
data class BudgetItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    //If expense, 0. If income, 1.
    val expenseOrIncome: Int,

    val name: String,

    //How much money ONE of this item costs,
    //or how much money you get from this income source
    //per income period.
    val value: Double,

    //Amount of the item you buy per frequency period.
    //Not applicable for incomes.
    val quantity: Double?,

    val category: String?,
    val subcategory: String?,

    //You buy [amount] of this item every [frequency] days,
    //or you get this income amount every [frequency] days.
    val frequency: Double,

    //val date: String,

    //Amount actually paid each week. Not applicable for incomes (just change frequency)
    val valuePerDay: Double? = quantity!! * value / frequency,

    //https://api.stlouisfed.org/fred/series/observations?series_id=GNPCA&api_key=abcdefghijklmnopqrstuvwxyz123456
    val seriesId: String? = null
)

public fun valueToCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(value)
}