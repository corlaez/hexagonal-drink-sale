package primaryadapters.console

import drink.ReportSales
import util.FancyConsole
import util.enVerde

class ForReportingSalesOnConsole(val reportSales: ReportSales) {

    fun run() {
        val listOfSales = reportSales.getAll()
        val stringOfSales = listOfSales.joinToString(System.lineSeparator())
        FancyConsole.println(stringOfSales.enVerde())
    }
}
