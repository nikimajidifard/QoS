package com.example.etta
import android.content.Intent
import android.widget.AdapterView.OnItemClickListener
import com.example.etta.databinding.ActivityMainBinding
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    val domain = "google.com"
    val packetLoss = packetLoss(domain)
    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter: ListAdapter
    private lateinit var listData: ListData
    var dataArrayList = ArrayList<ListData?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // setContentView(R.layout.activity_main)
        //fetchPing()
        processPing2()
        processlatency()
        if (packetLoss != null) {
            println("Packet Loss for $domain: $packetLoss%")
        } else {
            println("Failed to calculate packet loss for $domain")
        }
        val imageList = intArrayOf(
            R.drawable.wifi,
            R.drawable.packet,
            R.drawable.download,
            R.drawable.upload,
            R.drawable.ping,
            )
        val ingredientList = intArrayOf(
            R.string.pastaIngredients,
            R.string.maggiIngredients,
            R.string.cakeIngredients,
            R.string.pancakeIngredients,
            R.string.pizzaIngredients,
        )
        val descList = intArrayOf(
            R.string.pastaDesc,
            R.string.maggieDesc,
            R.string.cakeDesc,
            R.string.pancakeDesc,
            R.string.pizzaDesc,
        )
        val nameList = arrayOf("Latency", "Packet Loss", "Download", "Upload", "Ping")
        for (i in imageList.indices) {
            listData = ListData(
                nameList[i],
               ingredientList[i], descList[i], imageList[i]
            )
            dataArrayList.add(listData)
        }
        listAdapter = ListAdapter(this@MainActivity, dataArrayList, this::onButtonClick)
        binding.listview.adapter = listAdapter
        binding.listview.isClickable = true
        binding.listview.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this@MainActivity, DetailedActivity::class.java)
            intent.putExtra("name", nameList[i])
            Log.v("fatimeName","MainActivity: " + nameList[1])
            intent.putExtra("ingredients", ingredientList[i])
            intent.putExtra("desc", descList[i])
            intent.putExtra("image", imageList[i])
            startActivity(intent)
        }
    }
    data class PingData(val time: Long, val value: String, val description: String)

    fun pingg(domain: String): PingData {
        val runtime = Runtime.getRuntime()
        var timeofping: Long = 0
        try {
            val a = System.currentTimeMillis() % 100000
            val ipProcess = runtime.exec("/system/bin/ping -c 1 $domain")
            ipProcess.waitFor()
            Log.d("pingggggggggg", "Ping: $ipProcess ms")
            val b = System.currentTimeMillis() % 100000
            if (b <= a) {
                timeofping = (100000 - a) + b
            } else {
                timeofping = b - a
            }
        } catch (e: Exception) {
            // Handle the exception appropriately
        }

        // Determine the value and description based on the timeofping
        val value: String
        val description: String

        when {
            timeofping in 0..10 -> {
                value = "bad"
                description = "Very annoying"
            }
            timeofping in 10..20 -> {
                value = "poor"
                description = "Annoying"
            }
            timeofping in 20..30 -> {
                value = "fair"
                description = "Slightly annoying"
            }
            timeofping in 30..40 -> {
                value = "good"
                description = "Not annoying"
            }
            else -> {
                value = "excellent"
                description = "Imperceptible"
            }
        }

        return PingData(timeofping, value, description)
    }


    private fun onButtonClick(listData: ListData) {
        val pingData = pingg("web.eitaa.com") // Call the pingg function with the desired domain

        val intent = Intent(this@MainActivity, DetailedActivity::class.java)
        intent.putExtra("name", pingData.time)
        Log.v("fatimeName","MainActivity: " + pingData.time)
        intent.putExtra("ingredients", pingData.description)
        intent.putExtra("desc", pingData.value)
        intent.putExtra("image", listData.image)
        startActivity(intent)
    }


//    fun ping(domain: String): Long {
//        var timeOfPing: Long = 0
//
//        try {
//            val runtime = Runtime.getRuntime()
//            val process = runtime.exec("ping -c 1 $domain")
//            process.waitFor()
//            Log.d("minp", "Ping: $process ms")
//
//            val reader = BufferedReader(InputStreamReader(process.inputStream))
//            val output = StringBuilder()
//            var line: String?
//
//            while (reader.readLine().also { line = it } != null) {
//                output.append(line)
//            }
//
//            val pingRegex = """.*time=(\d+).*""".toRegex()
//            val matchResult = pingRegex.find(output.toString())
//
//            if (matchResult != null) {
//                val pingTime = matchResult.groups[1]?.value?.toLongOrNull()
//                if (pingTime != null) {
//                    timeOfPing = pingTime
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return timeOfPing
//    }
    fun packetLoss(domain: String): Float? {
        try {
            val runtime = Runtime.getRuntime()
            val ipProcess = runtime.exec("/system/bin/ping -c 4 $domain")
            ipProcess.waitFor()

            val reader = BufferedReader(InputStreamReader(ipProcess.inputStream))
            var transmitted = 0
            var receivedCount = 0
            var lostCount = 0

            var outputLine: String? = reader.readLine()

            while (outputLine != null) {
                if (outputLine.contains(" packets transmitted")) {
                    val fields = outputLine.split(",")
                    val transmittedStr = fields[0].trim().split(" ")[0]
                    val receivedStr = fields[1].trim().split(" ")[0]
                    val lostStr = fields[2].trim().split(" ")[0]

                    transmitted = transmittedStr.toIntOrNull() ?: 0
                    val received = receivedStr.toIntOrNull()
                    val lost = lostStr.toIntOrNull()

                    if (received != null && lost != null) {
                        receivedCount = received
                        lostCount = lost
                        break
                    }
                }
                outputLine = reader.readLine()
            }

            if (transmitted > 0) {
                val packetLoss = (lostCount.toFloat() / transmitted.toFloat()) * 100
                return packetLoss
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    fun calculateLatency(serverAddress: String): Float? {
        try {
            val process = Runtime.getRuntime().exec("/system/bin/ping -c 4 $serverAddress")

            Log.d("process2", "Ping: $process ms")

            try {
                process.waitFor()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            Log.d("reader", "Ping: $reader ms")

            var outputLine: String? = reader.readLine()
            Log.d("outputLine", "Ping: $outputLine ms")

            var avgTime: Float? = null

            while (outputLine != null) {
                Log.d("outnullornot", "Ping: $outputLine ms")

                if (outputLine.contains("rtt")) {
                    Log.d("rtt", "Ping: $outputLine ms")

                    val fields = outputLine.split("/")
                    avgTime = fields[4].toFloatOrNull()
                    break
                }
                outputLine = reader.readLine()
            }
            Log.d("avgTime", "Ping: $avgTime ms")

            return avgTime

        } catch (e: Exception) {
            return null
        }
    }
    private fun processPing2() {
        val myping = pingg("web.eitaa.com")


        val textView = findViewById<TextView>(R.id.textView)
        textView.text = "Ping: $myping ms"

        println("Ping: $myping ms")
        Log.d("MainActivity", "Ping: $myping ms")
    }
    private fun processlatency() {
        // Display ping value to the user
        val latency = calculateLatency("web.eitaa.com")
//        val textView = findViewById<TextView>(R.id.textView2)
//        textView.text = "latency: $latency ms"
        println("latency A: $latency ms")
        Log.d("latency", "latency: $latency ms")
    }
}