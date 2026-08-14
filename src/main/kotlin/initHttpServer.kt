import drink.ReportSales
import drink.SellDrink
import primaryadapters.api.ReportOnSalesApiHandler
import primaryadapters.api.SellDrinkApiHandler
import primaryadapters.rest.ReportOnSalesRestHandler
import primaryadapters.rest.SellDrinkRestHandler
import drink.secondaryports.ForStoringDrinkStock
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import secondaryadapters.random.RandomUUIDGeneratorSupplier
import secondaryadapters.storing.ForStoringDrinkStockInFile
import secondaryadapters.storing.ForStoringSaleInFile
import secondaryadapters.time.SystemClockProvider

private object RoutesDef {
    val rootUI = "/"
    val sellDrinkUIHandler = "/sell-drink"
    val salesReportUI = "/sales-report"

    val data = "/data"
    val drinkStockJson = "/drink-stock.json"
    val salesReportJson = "/sales-report.json"
}
object Routes {
    val root = RoutesDef.rootUI
    val salesReportUI = RoutesDef.salesReportUI
    val sellDrinkUIHandler = RoutesDef.sellDrinkUIHandler
    val salesReportJson = RoutesDef.data + RoutesDef.salesReportJson
    val drinkStockJson = RoutesDef.data + RoutesDef.drinkStockJson
}
val getRouteExamples = listOf(Routes.root, Routes.salesReportUI, Routes.salesReportJson, Routes.drinkStockJson)
val postRouteExamples = listOf(Routes.sellDrinkUIHandler + "?id=A3&amount=400")

fun initHttpServer() {// Configurator
    /** Init all secondary/driven adapters */
    val repo: ForStoringDrinkStock = ForStoringDrinkStockInFile()
    val saleRepo = ForStoringSaleInFile()
    val uuidGenSupplier = RandomUUIDGeneratorSupplier()
    val clockSupplier = SystemClockProvider()

    /** Init all use cases*/
    val sellDrink = SellDrink(repo, saleRepo, uuidGenSupplier, clockSupplier)// USE CASE depends on the driven adapters
    val reportSales = ReportSales(saleRepo)// USE CASE depends on the driven adapters

    /** Init all primary/driver adapters */
    val sellDrinkRestHandler = SellDrinkRestHandler(sellDrink)
    val reportOnSalesRestHandler = ReportOnSalesRestHandler(reportSales)
    val sellDrinkApiHandler = SellDrinkApiHandler(sellDrink)
    val reportOnSalesApiHandler = ReportOnSalesApiHandler(reportSales)

    /** Compose all the primary/driver adapters and start server*/
    Javalin.create { config ->
        config.startup.showJavalinBanner = false
        config.routes.apiBuilder {
            get(RoutesDef.rootUI) { ctx -> sellDrinkRestHandler.getIndexHtml(ctx) }
            post(RoutesDef.sellDrinkUIHandler) { ctx -> sellDrinkRestHandler.sellDrink(ctx)  }
            get(RoutesDef.salesReportUI) { ctx -> reportOnSalesRestHandler.getDrinkSalesHtml(ctx) }
            path(RoutesDef.data) {
                get(RoutesDef.drinkStockJson) { ctx -> sellDrinkApiHandler.getAllDrinkStock(ctx) }
                get(RoutesDef.salesReportJson) { ctx -> reportOnSalesApiHandler.getAllSales(ctx) }
            }
        }
    }.start(8080)
    System.err.println("Try these commands on bash:")
    for (routeExample in getRouteExamples) {
        System.err.println("curl -X GET --location \"http://localhost:8080" + routeExample + "\"")
    }
    for (routeExample in postRouteExamples) {
        System.err.println("curl -X POST --location \"http://localhost:8080" + routeExample + "\"")
    }
}
