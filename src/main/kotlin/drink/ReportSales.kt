package drink

import drink.secondaryports.ForStoringSale

class ReportSales(val repo: ForStoringSale) {

    fun getAll(): Collection<DrinkSale> {
        return repo.getAll()
    }
}
