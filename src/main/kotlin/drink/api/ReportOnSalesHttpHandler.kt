package drink.api

import drink.ReportOnSales
import io.javalin.http.Context
import util.withNoCacheHeaders

/** Driving/Primary adapter (has a dependency on the UseCase) **/
class ReportOnSalesApiHandler(val reportOnSales: ReportOnSales) {

    fun getAllSales(ctx: Context) {
        val list = reportOnSales.getAll()
        ctx.withNoCacheHeaders()
        ctx.json(list)
    }
}
