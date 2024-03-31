package com.yan.source

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yan.source.utils.request
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.bjoernpetersen.m3u.M3uParser

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.yan.source.test", appContext.packageName)
    }

    @Test
    fun testHttp(){
        runBlocking {
            val url = "https://live.fanmingming.com/tv/m3u/ipv6.m3u"
            val m3uFile = request(url)
            val m3uEntryList = M3uParser.parse(m3uFile)
            println("$m3uFile")
            assertEquals(m3uEntryList,null)
        }

    }
}