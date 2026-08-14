package secondaryadapters.random

import com.fasterxml.uuid.Generators
import drink.secondaryports.ForObtainingUUIDGenerator
import drink.secondaryports.UuidGenerator
import java.util.UUID

class RandomUUIDGeneratorSupplier: ForObtainingUUIDGenerator {
    override fun get(): UuidGenerator {
        return Random()
    }

    private class Random: UuidGenerator {
        override fun generate(): UUID {
            return UUID.randomUUID()
        }
    }
}

class PredictableUUIDGeneratorSupplier: ForObtainingUUIDGenerator {
    override fun get(): UuidGenerator {
        return Predictable()
    }

    private class Predictable: UuidGenerator {
        private val generator = Generators.randomBasedGenerator(java.util.Random(0))

        override fun generate(): UUID {
            return generator.generate()
        }
    }
}
