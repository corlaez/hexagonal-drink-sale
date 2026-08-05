package drink.time

import kotlin.time.Clock
import kotlin.time.Instant

interface ForProvidingClock {// PORT
    fun get(): Clock

    class SystemClockProvider: ForProvidingClock {// ADAPTER
    override fun get() = Clock.System
    }

    class FixedClockProvider(// ADAPTER (FOR TEST)
        private val instant: Instant = Instant.parse("2026-12-12")
    ): ForProvidingClock {
        override fun get() = FixedClock(instant)
    }
}


class FixedClock(private val instant: Instant) : Clock {
    override fun now() = instant
}

// Also possible: a clock that ticks 1 second every time now() is called