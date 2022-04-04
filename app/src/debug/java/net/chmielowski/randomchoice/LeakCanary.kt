package net.chmielowski.randomchoice

import leakcanary.LeakCanary

internal fun setupLeakCanary() {
    LeakCanary.config = LeakCanary.config.copy(
        retainedVisibleThreshold = 1,
    )
}
