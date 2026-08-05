package drink

import drink.storing.ForStoringSale

class ReportOnSales(val repo: ForStoringSale) {

    fun getAll(): Collection<DrinkSale> {
        return repo.getAll()
    }
}