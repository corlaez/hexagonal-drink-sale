package drink

import java.util.UUID
import kotlin.time.Instant


data class DrinkStock(val id: String, val name: String, val price: Int, val stock: Int) {
    init {
        // INVARIANT CHECK
        // FAIL-FAST (AND HARD)
        // "Make illegal states unrepresentable." a very FP technique
        // In theory our use case should never break invariants but if we dropped the ball these will halt the system
        require(id.length == 2) { "id must have two characters" }
        require(name.length >= 2) { "name must have two or more characters" }
        require(price > 0) { "price must be positive" }
        require(stock >= 0) { "stock must be greater than or equal to zero" }
    }
}

data class DrinkSale(val id: UUID, val drinkId: String, val name: String, val price: Int, val quantity: Int, val timestamp: Instant) {
    init {
        require(drinkId.length == 2) { "id must have two characters" }
        require(name.length >= 2) { "name must have two or more characters" }
        require(price > 0) { "price must be positive" }
        require(quantity > 0) { "quantity must be greater than zero" }
    }
}
