package secondaryadapters.storing

import com.fasterxml.jackson.core.type.TypeReference
import drink.*
import drink.secondaryports.ForStoringDrinkStock
import util.JsonHelper
import java.io.*

class ForStoringDrinkStockInMemory : ForStoringDrinkStock {
    val drinkStockMap: MutableMap<String, DrinkStock> = mutableMapOf()

    init {
        save(DrinkStock("A1", "coca cola 250ml", 200, 10))
        save(DrinkStock("A2", "pepsi 250ml", 150, 5))
        save(DrinkStock("A3", "agua cielo 250ml", 100, 4))
    }

    override fun findById(id: String): DrinkStock? {
        return drinkStockMap[id]
    }
    override fun getAll(): Collection<DrinkStock> {
        return drinkStockMap.values
    }
    override fun save(drinkStock: DrinkStock) {
        drinkStockMap[drinkStock.id] = drinkStock
    }
}

class ForStoringDrinkStockInFile(filePath: String = "files/drink-stock.json") : ForStoringDrinkStock {
    private val file = File(filePath)
    private val drinkStockMap: MutableMap<String, DrinkStock>
    private val typeRef = object : TypeReference<MutableMap<String, DrinkStock>>() {}

    init {
        // Create parent directories if they don't exist
        file.parentFile?.mkdirs()

        // Load existing data or initialize empty map
        drinkStockMap = if (file.exists()) {
            JsonHelper.readValue(file)
        } else {
            mutableMapOf()
        }
        DrinkStock("A1", "coca cola 250ml", 200, 10).let { drinkStockMap[it.id] = it}
        DrinkStock("A2", "pepsi 250ml", 150, 5).let { drinkStockMap[it.id] = it}
        DrinkStock("A3", "agua cielo 250ml", 100, 4).let { drinkStockMap[it.id] = it}
        persistToFile()
    }

    override fun findById(id: String): DrinkStock? {
        return drinkStockMap[id]
    }
    override fun getAll(): Collection<DrinkStock> {
        return drinkStockMap.values
    }
    override fun save(drinkStock: DrinkStock) {
        drinkStockMap[drinkStock.id] = drinkStock
        persistToFile()
    }
    private fun persistToFile() {
        JsonHelper.writerWithDefaultPrettyPrinter().writeValue(file, drinkStockMap)
    }
}
