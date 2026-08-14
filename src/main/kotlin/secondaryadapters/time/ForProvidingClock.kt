package secondaryadapters.time

import drink.secondaryports.ForProvidingClock
import kotlin.time.Clock
import kotlin.time.Instant

class SystemClockProvider: ForProvidingClock {// ADAPTER
override fun get() = Clock.System
}

class FixedClockProvider(// ADAPTER (FOR TEST)
    private val instant: Instant = Instant.parse("2020-08-30T18:43:00Z")
): ForProvidingClock {
    override fun get() = FixedClock(instant)
}

class FixedClock(private val instant: Instant) : Clock {
    override fun now() = instant
}

// Also possible: a clock that ticks 1 second every time now() is called