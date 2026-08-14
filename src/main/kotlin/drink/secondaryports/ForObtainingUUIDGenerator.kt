package drink.secondaryports

import java.util.UUID

interface ForObtainingUUIDGenerator {
    fun get(): UuidGenerator
}

interface UuidGenerator {
    fun generate(): UUID
}
