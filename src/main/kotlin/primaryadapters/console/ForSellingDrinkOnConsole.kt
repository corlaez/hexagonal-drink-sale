package primaryadapters.console

import drink.SellDrinkResult
import drink.SellDrink
import util.FancyConsole
import util.enAzul
import util.enRojo
import util.enVerde

private val availableDrinksMessage = "Available Drinks: ".enAzul()
private val enterDrinkIdMessage = "Enter drink ID: ".enAzul()
private val enterAmountMessage = "Enter amount (as an integer of cents): ".enAzul()

/** Driving/Primary adapter (has a dependency on the UseCase) **/
class ForSellingDrinkOnConsole(val sellDrink: SellDrink) {

    fun run() {
        FancyConsole.println(availableDrinksMessage)
        FancyConsole.println(sellDrink.getAllDrinkStock().joinToString(System.lineSeparator()).enVerde())
        val id = FancyConsole.retryReadln(enterDrinkIdMessage) { inputDeUsuarioId ->
            val errorResult = sellDrink.validateReadyForSale(inputDeUsuarioId)
            if (errorResult != null) {
                FancyConsole.println((errorResult as SellDrinkResult).toStyledConsoleMessage())
                return@retryReadln null
            } else {
                return@retryReadln inputDeUsuarioId
            }
        }
        val amount = FancyConsole.retryReadln(enterAmountMessage) {
            try { it.toInt() } catch (_: NumberFormatException) { null }
        }
        val resultado = sellDrink.sell(id, amount)
        resultado.printlnDevolverAndResult()
    }
}

private fun SellDrinkResult.printlnDevolverAndResult() {
    when (this) {
        is SellDrinkResult.DispenseDrink -> {
            if(this.returnedAmount > 0)
                FancyConsole.println(this.toDevolverStyledConsoleMessage())
            FancyConsole.println(this.toStyledConsoleMessage())
        }
        is SellDrinkResult.InsufficientAmount -> {
            if(this.returnedAmount > 0)
                FancyConsole.println(this.toDevolverStyledConsoleMessage())
            FancyConsole.println(this.toStyledConsoleMessage())
        }
        else -> {
            FancyConsole.println(this.toStyledConsoleMessage())
        }
    }
}

private fun SellDrinkResult.InsufficientAmount.toDevolverStyledConsoleMessage() = "Return: $returnedAmount".enAzul()
private fun SellDrinkResult.DispenseDrink.toDevolverStyledConsoleMessage() = "Return: $returnedAmount".enAzul()

private fun SellDrinkResult.toStyledConsoleMessage() = when(this) {
    SellDrinkResult.UnknownID -> "ERROR: $this".enRojo()
    is SellDrinkResult.NoStock -> "ERROR: $this".enRojo()
    is SellDrinkResult.InsufficientAmount -> "ERROR: $this".enRojo()
    is SellDrinkResult.DispenseDrink -> "SUCCESS: $this".enVerde()
    is SellDrinkResult.InvalidAmount -> "ERROR: $this".enRojo()
}
