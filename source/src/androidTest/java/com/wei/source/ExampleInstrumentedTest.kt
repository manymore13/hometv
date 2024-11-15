package com.wei.source

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

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

//    fun testHttp() {
//        runBlocking {
//            val url = "https://live.fanmingming.com/tv/m3u/ipv6.m3u"
//            val m3uFile = request(url)
//            val m3uEntryList = M3uParser.parse(m3uFile)
//            println("$m3uFile")
//            val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//
//            val db = DatabaseManager.getInstance(appContext)
//            val channelDao = db.getChannelDao()
//            GlobalScope.launch {
//                val sourceId = channelDao.insertSource(Source("默认源", url))
//                System.out.println("sourceId: $sourceId")
//                val channelList  = mutableListOf<Channel>()
//                m3uEntryList.forEach {
//                    channelList.add(Channel(it.title?:"", it.metadata["tvg-logo"]?:"", sourceId,""))
//                }
//                channelDao.insertChannels(channelList)
//                assertEquals(m3uEntryList, null)
//            }
//        }
//
//    }
}