package drink.rest

import drink.ReportOnSales
import io.javalin.http.Context
import util.head
import util.withNoCacheHeaders

/** Driving/Primary adapter (has a dependency on the UseCase) **/
class ReportOnSalesRestHandler(val reportOnSales: ReportOnSales) {
    private val title = "Drink Sale"

    fun getDrinkSalesHtml(ctx: Context) {
        // CALLING YOUR USE CASE
        val sales = reportOnSales.getAll()
        // ENCODING
        ctx.withNoCacheHeaders()
        ctx.html("""
                ${head(title)}
                <body><main>
                <section>
                  <h1>$title</h1>
                  <nav style="margin-bottom: 1rem;">
                    <a href="${Routes.root}">← Back to Vending Machine</a>
                    <a href="${Routes.salesReportJson}" target="_blank">View as JSON</a>
                  </nav>
                  <article>
                    <h2>All Sales</h2>
                      <table>
                        <thead>
                          <tr>
                            <th>UUID</th>
                            <th>Drink Code</th>
                            <th>Name</th>
                            <th>TotalPrice</th>
                            <th>Quantity</th>
                            <th>Timestamp</th>
                          </tr>
                        </thead>
                        <tbody>
                          ${sales.joinToString("") {
                            val formattedTime = it.timestamp.toString().replace('T', ' ').substringBefore('.') + " UTC"
                            """
                            <tr>
                              <td>${it.id}</td>
                              <td><strong>${it.drinkId}</strong></td>
                              <td>${it.name}</td>
                              <td>$${it.price * it.quantity / 100.0}</td>
                              <td>${it.quantity}</td>
                              <td>${formattedTime}</td>
                            </tr>"""
                          }}
                        </tbody>
                      </table>
                  </article>
                </section>
                </main>
                </body>
            """.trimIndent())
    }

}
