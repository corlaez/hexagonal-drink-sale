package drink.secondaryports

import kotlin.time.Clock

interface ForProvidingClock {// PORT
    fun get(): Clock
}