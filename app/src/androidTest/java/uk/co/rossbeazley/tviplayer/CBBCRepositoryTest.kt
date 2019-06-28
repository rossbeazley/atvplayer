package uk.co.rossbeazley.tviplayer

import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.assertTrue

import org.junit.Test

internal class CBBCRepositoryTest
{
    @Test
    fun smallSteps()
    {
        val repo = CBBCRepository(InstrumentationRegistry.getInstrumentation().context)
        var size = 0;
        repo.programs {
            size = it.size
        }


        Thread.sleep(5_000)

        assertTrue(size == 90)
    }
}