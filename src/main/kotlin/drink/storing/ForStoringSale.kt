package drink.storing

import com.fasterxml.jackson.core.type.TypeReference
import drink.DrinkSale
import util.JsonHelper
import java.io.File
import java.util.UUID

interface ForStoringSale {
    fun save(drinkSale: DrinkSale)
    fun getAll(): Collection<DrinkSale>
}

class ForStoringSaleInMemory : ForStoringSale {
    val drinkStockMap: MutableMap<UUID, DrinkSale> = mutableMapOf()

    override fun save(drinkSale: DrinkSale) {
        drinkStockMap[drinkSale.id] = drinkSale
    }
    override fun getAll(): List<DrinkSale> {
        return drinkStockMap.values.toList()
    }
}

class ForStoringSaleInFile(filePath: String = "files/drink-sales.json") : ForStoringSale {
    private val file = File(filePath)
    private val drinkSaleMap: MutableMap<UUID, DrinkSale>
    private val typeRef = object : TypeReference<MutableMap<UUID, DrinkSale>>() {}

    init {
        // Create parent directories if they don't exist
        file.parentFile?.mkdirs()

        // Load existing data or initialize empty map
        drinkSaleMap = if (file.exists()) {
            JsonHelper.readValue(file)
        } else {
            mutableMapOf()
        }
    }

    override fun getAll(): Collection<DrinkSale> {
        return drinkSaleMap.values
    }
    override fun save(drinkSale: DrinkSale) {
        drinkSaleMap[drinkSale.id] = drinkSale
        persistToFile()
    }
    private fun persistToFile() {
        JsonHelper.writerWithDefaultPrettyPrinter().writeValue(file, drinkSaleMap)
    }
}
