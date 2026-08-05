package drink

import drink.storing.ForObtainingUUIDGenerator
import drink.storing.ForStoringDrinkStock
import drink.storing.ForStoringDrinkStockInMemory
import drink.storing.ForStoringSale
import drink.storing.ForStoringSaleInMemory
import drink.time.ForProvidingClock
import java.util.UUID

// Use Case Results (Failure as values)
interface SellDrinkFailure
sealed class SellDrinkResult {
    data class DispenseDrink(val returnedAmount: Int): SellDrinkResult()
    data class InsufficientAmount(val returnedAmount: Int): SellDrinkResult(), SellDrinkFailure
    data object NoStock: SellDrinkResult(), SellDrinkFailure
    data object UnknownID: SellDrinkResult(), SellDrinkFailure
    data object InvalidAmount: SellDrinkResult(), SellDrinkFailure
}
// Use Case (driven ports)
class SellDrink(
    val repo: ForStoringDrinkStock,
    val saleRepo: ForStoringSale,
    uuidGenSupplier: ForObtainingUUIDGenerator,
    clockSupplier: ForProvidingClock,
) {
    val uuidGenerator = uuidGenSupplier.get()
    val clock = clockSupplier.get()

    fun getAllDrinkStock(): Collection<DrinkStock> {
        return repo.getAll()
    }

    fun validateReadyForSale(idBebida: String): SellDrinkFailure? {
        val bebidaStock = repo.findById(idBebida) ?: return SellDrinkResult.UnknownID
        if(bebidaStock.stock == 0) return SellDrinkResult.NoStock
        return null
    }

    fun sell(idBebida:String, amount:Int): SellDrinkResult{
        if(amount < 0) return SellDrinkResult.InvalidAmount
        val bebidaStock = repo.findById(idBebida) ?: return SellDrinkResult.UnknownID
        if(bebidaStock.stock == 0) return SellDrinkResult.NoStock
        if(amount < bebidaStock.price) return SellDrinkResult.InsufficientAmount(amount)

        val vuelto = amount - bebidaStock.price
        repo.save(bebidaStock.copy(stock = bebidaStock.stock - 1))
        val uuid = uuidGenerator.generate()
        saleRepo.save(DrinkSale(uuid, bebidaStock.id, bebidaStock.name, bebidaStock.price, 1, clock.now()))
        return SellDrinkResult.DispenseDrink(vuelto)
    }
}

fun main() {
    val repo = ForStoringDrinkStockInMemory() // Repository Secondary/Drink Adapter
    val saleRepo = ForStoringSaleInMemory()
    val uuidGeneratorSupplier = ForObtainingUUIDGenerator.Predictable()
    val clockSupplier = ForProvidingClock.FixedClockProvider()

    val sut = SellDrink(repo, saleRepo, uuidGeneratorSupplier, clockSupplier)

    sut.sell("A1", 4000)

    val clock = ForProvidingClock.FixedClockProvider().get()
    println(saleRepo.getAll().size == 1)
    println(saleRepo.getAll()[0])
    val expected = DrinkSale(UUID.fromString("bb20b45f-d4d9-4138-bd93-cb799b3970be"), "A1", "coca cola 250ml", 200, 1, clock.now())
    println(saleRepo.getAll()[0] == expected)
}