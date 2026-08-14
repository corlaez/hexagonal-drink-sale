package drink.secondaryports

import drink.*

interface ForStoringDrinkStock {
    fun findById(id: String): DrinkStock?
    fun getAll(): Collection<DrinkStock>
    fun save(drinkStock: DrinkStock)
}
