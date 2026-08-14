package primaryadapters.api

import drink.SellDrink
import io.javalin.http.Context
import util.withNoCacheHeaders

/** Driving/Primary adapter (has a dependency on the UseCase) **/
class SellDrinkApiHandler(private val sellDrink: SellDrink) {

    fun getAllDrinkStock(ctx: Context) {
        ctx.withNoCacheHeaders()
        ctx.json(sellDrink.getAllDrinkStock())
    }
}
