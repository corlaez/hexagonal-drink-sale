package primaryadapters.api

import drink.ReportSales
import io.javalin.http.Context
import util.withNoCacheHeaders

/** Driving/Primary adapter (has a dependency on the UseCase) **/
class ReportOnSalesApiHandler(val reportSales: ReportSales) {

    fun getAllSales(ctx: Context) {
        val list = reportSales.getAll()
        ctx.withNoCacheHeaders()
        ctx.json(list)
    }
}
