package drink.console

import drink.SellDrinkResult
import drink.SellDrink
import util.FancyConsole
import util.enAzul
import util.enRojo
import util.enVerde

/** Driving/Primary adapter (has a dependency on the UseCase) **/
class ForSellingDrinkOnConsole(val sellDrink: SellDrink) {

    fun run() {
        FancyConsole.println("Bebidas disponibles: ".enAzul())
        FancyConsole.println(sellDrink.getAllDrinkStock().joinToString("\n").enAzul())
        while (true) {
            val id = FancyConsole.retryReadln("Ingrese ID de bebida: ".enAzul()) { inputDeUsuarioId ->
                val errorResult = sellDrink.validateReadyForSale(inputDeUsuarioId)
                if (errorResult != null) {
                    FancyConsole.println((errorResult as SellDrinkResult).toStyledConsoleMessage())
                    return@retryReadln null
                } else {
                    return@retryReadln inputDeUsuarioId
                }
            }
            val amount = FancyConsole.retryReadln("Ingrese monto (como numero entero en centavos): ".enAzul()) {
                try { it.toInt() } catch (_: NumberFormatException) { null }
            }
            val resultado = sellDrink.sell(id, amount)
            resultado.printlnDevolverAndResult()
        }
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

private fun SellDrinkResult.InsufficientAmount.toDevolverStyledConsoleMessage() = "Devolver: $returnedAmount".enAzul()
private fun SellDrinkResult.DispenseDrink.toDevolverStyledConsoleMessage() = "Devolver: $returnedAmount".enAzul()

private fun SellDrinkResult.toStyledConsoleMessage() = when(this) {
    SellDrinkResult.UnknownID -> "ERROR: $this".enRojo()
    is SellDrinkResult.NoStock -> "ERROR: $this".enRojo()
    is SellDrinkResult.InsufficientAmount -> "ERROR: $this".enRojo()
    is SellDrinkResult.DispenseDrink -> "SUCCESS: $this".enVerde()
    is SellDrinkResult.InvalidAmount -> "ERROR: $this".enRojo()
}
