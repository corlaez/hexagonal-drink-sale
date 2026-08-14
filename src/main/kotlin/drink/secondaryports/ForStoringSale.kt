package drink.secondaryports

import drink.DrinkSale

interface ForStoringSale {
    fun save(drinkSale: DrinkSale)
    fun getAll(): Collection<DrinkSale>
}
