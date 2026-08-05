import drink.storing.ForStoringDrinkStockInMemory
import drink.SellDrink
import drink.console.ForSellingDrinkOnConsole
import drink.storing.ForObtainingUUIDGenerator
import drink.storing.ForStoringSaleInMemory
import drink.time.ForProvidingClock
import org.jline.reader.EndOfFileException
import org.jline.reader.UserInterruptException
import util.FancyConsole
import util.enAzul
import kotlin.system.exitProcess

fun initConsoleApp() {
    val repo = ForStoringDrinkStockInMemory() // Repository Secondary/Drink Adapter
    val saleRepo = ForStoringSaleInMemory()
    val uuidGenSupplier = ForObtainingUUIDGenerator.Predictable()
    val clockSupplier = ForProvidingClock.FixedClockProvider()

    val sellDrink = SellDrink(repo, saleRepo, uuidGenSupplier, clockSupplier)// USE CASE (VERBs)
    val sellDrinkOnConsole = ForSellingDrinkOnConsole(sellDrink)// Primary/Driber Adapter

    try {
        while (true) {
            FancyConsole.clearScreen()
            val s = FancyConsole.readln("Presione 1 para venta de bebida: ".enAzul())
            try {
                if (s == "1") sellDrinkOnConsole.run()
            } catch (_: UserInterruptException) { }
        }
    } catch (_: UserInterruptException) {
        exitProcess(0)
    } catch (_: EndOfFileException) {
        exitProcess(0)
    }
}
