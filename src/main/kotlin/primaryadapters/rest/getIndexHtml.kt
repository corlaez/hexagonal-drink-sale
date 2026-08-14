package primaryadapters.rest

import drink.SellDrink
import util.head

enum class SellDrinkMessage {
    dispensed, insufficient, unknownid, nostock, invalidamount
}

fun getIndexHtml(sellDrink: SellDrink, selectedId: String? = null, amount: String? = null, msg: String? = null, change: String? = null): String {
    val title = "Drink Sale"
    val msgEnum = runCatching { SellDrinkMessage.valueOf(msg ?: "") }.getOrNull()
    val messageHtml = when(msgEnum) {
        SellDrinkMessage.dispensed -> "<p style='color: green;'>✅ Dispensed Drink! Change: ${change ?: "0"} cents</p>"
        SellDrinkMessage.insufficient -> "<p style='color: orange;'>⚠️ Insufficient amount. Please add more money.</p>"
        SellDrinkMessage.unknownid -> "<p style='color: red;'>❌ Unknown drink ID</p>"
        SellDrinkMessage.nostock -> "<p style='color: red;'>❌ No stock available for this drink</p>"
        SellDrinkMessage.invalidamount -> "<p style='color: red;'>❌ Invalid amount. Must be a positive integer</p>"
        null -> ""
    }
    val availableDrinksTable = sellDrink.getAllDrinkStock().joinToString("") { """
        <tr>
          <td><strong>${it.id}</strong></td>
          <td>${it.name}</td>
          <td>$${it.price / 100.0}</td>
          <td>${it.stock}</td>
        </tr>"""
    }
    val drinkOptions = sellDrink.getAllDrinkStock().joinToString("") {
        val selected = if (it.id == selectedId) "selected" else ""
        """<option value="${it.id}" $selected>${it.id} - ${it.name} ($${it.price / 100.0}) [Stock: ${it.stock}]</option>"""
    }
    val moneyButtons = """
          <div style="display: flex; gap: 0.5rem; margin-bottom: 1rem;">
            <button type="button" onclick="const input = document.getElementById('amount-input'); input.value = parseInt(input.value || 0) + 50; input.dispatchEvent(new Event('input'))">+50¢</button>
            <button type="button" onclick="const input = document.getElementById('amount-input'); input.value = parseInt(input.value || 0) + 100; input.dispatchEvent(new Event('input'))">+100¢ ($1)</button>
            <button type="button" onclick="const input = document.getElementById('amount-input'); input.value = parseInt(input.value || 0) + 200; input.dispatchEvent(new Event('input'))">+200¢ ($2)</button>
            <button type="button" onclick="const input = document.getElementById('amount-input'); input.value = parseInt(input.value || 0) + 500; input.dispatchEvent(new Event('input'))">+500¢ ($5)</button>
            <button type="button" onclick="const input = document.getElementById('amount-input'); input.value = 0; input.dispatchEvent(new Event('input'))">Reset</button>
          </div>
    """.trimIndent()
    val idName = "id"
    val amountName = "amount"
    return """
                ${head(title)}
                <body><main>
                <section>
                  <h1>$title</h1>
                  <nav style="margin-bottom: 1rem;">
                    <a href="${Routes.salesReportUI}">View Sales History</a>
                  </nav>
                  <div class="grid">
                    <article>
                      <h2>Available Drinks</h2>
                      <small><a href="${Routes.drinkStockJson}" target="_blank">View as JSON</a></small>
                      <br/>
                      <br/>
                      <table>
                        <thead>
                          <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Price</th>
                            <th>Stock</th>
                          </tr>
                        </thead>
                        <tbody>
                          $availableDrinksTable
                        </tbody>
                      </table>
                    </article>
                    <article>
                      <h2>Buy a Drink</h2>
                      <form hx-post="/sell-drink" hx-target="#result" hx-swap="innerHTML">
                      <br/><br/>
                      <label for="drink-select">Select Drink:</label>
                      <select
                        id="drink-select"
                        name="$idName"
                        aria-label="drink ID"
                        onchange="const amount = document.getElementById('amount-input').value; history.pushState({}, '', this.value ? '/?selected=' + this.value + (amount ? '&amount=' + amount : '') : '/')"
                        required
                      >
                        <option value="">-- Choose a drink --</option>
                        $drinkOptions
                      </select>

                      <label for="amount-input">Amount (cents):</label>
                      <input
                        id="amount-input"
                        type="number"
                        min=1
                        name="$amountName"
                        value="${amount.let{ if (it.isNullOrEmpty()) "0" else amount }}"
                        aria-label="Amount in cents"
                        oninput="const selected = document.getElementById('drink-select').value; history.pushState({}, '', selected ? '/?selected=' + selected + '&amount=' + this.value : '/?amount=' + this.value)"
                        required
                      >
                      $moneyButtons
                      <button type="submit">Sell Drink</button>
                      </form>
                      <div id="result">$messageHtml</div>
                    </article>
                  </div>
                </section>
                </main>
                <script>
                  // Consume message parameter after displaying it
                  if (window.location.search.includes('msg=')) {
                    const url = new URL(window.location);
                    url.searchParams.delete('msg');
                    url.searchParams.delete('change');
                    history.replaceState({}, '', url.toString());
                  }
                </script>
                </body>
            """.trimIndent()
}
