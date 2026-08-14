import drink.ReportSales
import drink.SellDrink
import primaryadapters.console.ForSellingDrinkOnConsole
import org.jline.reader.EndOfFileException
import org.jline.reader.UserInterruptException
import primaryadapters.console.ForReportingSalesOnConsole
import secondaryadapters.random.PredictableUUIDGeneratorSupplier
import secondaryadapters.storing.ForStoringDrinkStockInMemory
import secondaryadapters.storing.ForStoringSaleInMemory
import secondaryadapters.time.FixedClockProvider
import util.FancyConsole
import util.enAzul
import kotlin.system.exitProcess

fun initConsoleApp() {// Configurator
    val repo = ForStoringDrinkStockInMemory() // Repository Secondary/Drink Adapter
    val saleRepo = ForStoringSaleInMemory()
    val uuidGenSupplier = PredictableUUIDGeneratorSupplier()
    val clockSupplier = FixedClockProvider()

    val sellDrink = SellDrink(repo, saleRepo, uuidGenSupplier, clockSupplier)// USE CASE (verbs)
    val reportSales = ReportSales(saleRepo)// USE CASE (verbs)
    val sellDrinkOnConsole = ForSellingDrinkOnConsole(sellDrink)// Primary/Driver Adapter
    val reportSalesOnConsole = ForReportingSalesOnConsole(reportSales)// Primary/Driver Adapter

    try {
        while (true) {
            FancyConsole.clearScreen()
            val s = FancyConsole.readln("Press 1 to buy a drink. Press 2 to see sales report: ".enAzul())
            try {
                if (s == "1") sellDrinkOnConsole.run()
                if (s == "2") reportSalesOnConsole.run()
            } catch (_: UserInterruptException) { }// control+C
            catch (_: EndOfFileException) { }// control+D
        }
    } catch (_: UserInterruptException) {// control+c
        exitProcess(0)
    } catch (_: EndOfFileException) {// control+d
        exitProcess(0)
    }
}
