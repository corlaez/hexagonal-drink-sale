package drink.rest;

import drink.SellDrinkResult
import drink.SellDrink
import io.javalin.http.Context
import util.withNoCacheHeaders

/** Driving/Primary adapter (depends on the UseCase) **/
class SellDrinkRestHandler(private val sellDrink: SellDrink) {

    fun getIndexHtml(ctx: Context) {
        // DECODING
        val selectedId = ctx.queryParam("selected")
        val amount = ctx.queryParam("amount")
        val msg = ctx.queryParam("msg")
        val change = ctx.queryParam("change")
        // ENCODING
        ctx.withNoCacheHeaders()
        ctx.html(getIndexHtml(sellDrink, selectedId, amount, msg, change))
    }

    fun sellDrink(ctx: Context): Context {
        // DECODING
        val id = ctx.queryParam("id") ?: ctx.formParam("id")
            ?: return ctx.html("<p style='color: red;'>❌ Required query param id</p>")
        val amountString = ctx.queryParam("amount") ?: ctx.formParam("amount")
            ?: return ctx.html("<p style='color: red;'>❌ Required query param amount</p>")
        val amount = runCatching { amountString.toInt() }.getOrElse {
            return ctx.html("<p style='color: red;'>❌ Amount should be a positive integer</p>")
        }
        // CALLING YOUR USE CASE
        val resultado = sellDrink.sell(id, amount)
        // ENCODING
        ctx.withNoCacheHeaders()
        return when(resultado) {
            is SellDrinkResult.DispenseDrink -> {
                ctx.header("HX-Redirect", "/?selected=${id}&amount=${resultado.returnedAmount}&msg=${SellDrinkMessage.dispensed}&change=${resultado.returnedAmount}")
                ctx.html("")
            }
            is SellDrinkResult.InsufficientAmount -> {
                ctx.header("HX-Redirect", "/?selected=${id}&amount=${resultado.returnedAmount}&msg=${SellDrinkMessage.insufficient}")
                ctx.html("")
            }
            SellDrinkResult.UnknownID -> {
                ctx.header("HX-Redirect", "/?selected=${id}&amount=$amount&msg=${SellDrinkMessage.unknownid}")
                ctx.html("")
            }
            SellDrinkResult.NoStock -> {
                ctx.header("HX-Redirect", "/?selected=$id&amount=$amount&msg=${SellDrinkMessage.nostock}")
                ctx.html("")
            }
            SellDrinkResult.InvalidAmount -> {
                ctx.header("HX-Redirect", "/?selected=$id&amount=0&msg=${SellDrinkMessage.invalidamount}")
                ctx.html("")
            }
        }
    }
}
