package drink.storing

import com.fasterxml.uuid.Generators
import java.util.UUID

interface ForObtainingUUIDGenerator {
    fun get(): UuidGenerator

    class Random: ForObtainingUUIDGenerator {
        override fun get(): UuidGenerator {
            return UuidGenerator.Random();
        }
    }
    class Predictable: ForObtainingUUIDGenerator {
        override fun get(): UuidGenerator {
            return UuidGenerator.Predictable();
        }
    }
}

interface UuidGenerator {
    fun generate(): UUID

    class Random: UuidGenerator {
        override fun generate(): UUID {
            return UUID.randomUUID()
        }
    }
    class Predictable: UuidGenerator {
        private val generator = Generators.randomBasedGenerator(java.util.Random(0))

        override fun generate(): UUID {
            return generator.generate();
        }
    }
}
