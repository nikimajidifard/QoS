package com.example.etta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.coroutines.*
import okhttp3.*
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaTypeOrNull


class FirstActivity : AppCompatActivity() {

    val weightPing = 0.4
    val weightUploadRate = 0.2
    val weightDownloadRate = 0.2
    val weightPacketLoss = 0.6


    private var startTime: Long = 0
    private var endTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        // Coroutine scope for ping calculation
        val pingCoroutineScope = CoroutineScope(Dispatchers.Default)
        val pingTimeDeferred = pingCoroutineScope.async { pingcalc().toDouble() }

        // Coroutine scope for download rate calculation
        val downloadCoroutineScope = CoroutineScope(Dispatchers.Default)
        val downloadRateDeferred = downloadCoroutineScope.async { downloadtest() }
        val qoeTextView = findViewById<TextView>(R.id.textviewQoe)
        val qosTextView = findViewById<TextView>(R.id.textviewQos)

        val newThread = Thread {
            try {
                runBlocking {
                    // Wait for the ping time value
                    val pingTime = pingTimeDeferred.await()
                    println("Ping: $pingTime ms")
                    // Add any further actions here after using the ping time value
                    println("Timer finished.")
                    // Wait for the download rate value
                    val downloadRate = downloadRateDeferred.await()

                    // Use the pingTime and downloadRate values as needed
                    val packetLoss = packetloss()
                    val qos = calculateOverallQoSScore(pingTime/100, downloadRate/10, downloadRate, packetLoss)
                    val qoe = calculateOverallQoEScore(pingTime/100, downloadRate/10, downloadRate, packetLoss)
                    Log.d("Qoe_value" , " qoe : $qoe")
                    Log.d("Qos_value " , " qos : $qos")
                    qoeTextView.text = qoe
                    qosTextView.text = qos.toString()
                    runOnUiThread {
                        qoeTextView.text = qoe
                        qosTextView.text = qos.toString()
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        newThread.start()
        val viewButton = findViewById<Button>(R.id.viewParameters)
        viewButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    //pig with system call , never used
    fun pingg(domain: String): Long {
        val runtime = Runtime.getRuntime()
        var timeofping: Long = 0
        try {
            val a = System.currentTimeMillis() % 100000
            val ipProcess = runtime.exec("/system/bin/ping -c 1 $domain")
            ipProcess.waitFor()
            val b = System.currentTimeMillis() % 100000
            if (b <= a) {
                timeofping = (100000 - a) + b
            } else {
                timeofping = b - a
            }
        } catch (e: Exception) {
            Log.d("pingtime" , "exception accured with massage  ${e.message}")
        }
        return timeofping
    }


    suspend fun pingcalc(): Long {
        val client = HttpClient()

        try {
            startTime = System.currentTimeMillis()
            val response: HttpResponse =
                client.get("https://eitaayar.ir/api/bot187104:9d7e5b78-2354-4524-a432-2eab6e96b6f0/getMe/")
            endTime = System.currentTimeMillis()
            // Accessing the response status code
            val statusCode = response.status.value
            println("Response Status Code: $statusCode")
            Log.d("responseping", "Response Status Code: $statusCode")
            val pingTime = endTime - startTime
            println("Ping Time: $pingTime ms")
            Log.d("responseping", "Ping Time: $pingTime ms")
            return pingTime
        } catch (e: Exception) {
            println("An exception occurred: ${e.message}")
            Log.d("responseping", "An exception occurred: ${e.message}")
            return -1L // Return a default value in case of an exception
        } finally {
            client.close()
        }
    }

    fun packetloss() : Double {

        val totalPings = 200 // Number of pings to perform

        var sentPackets = 0
        var receivedPackets = 0
        var pingTime = -1L
        for (i in 1..totalPings) {
            runBlocking {
                launch {
                    pingTime = pingcalc()
                } }
            sentPackets++
            Log.d("packetloss", "sent :  $sentPackets %")
            if (pingTime > 0) {
                receivedPackets++
                Log.d("packetloss", "recieved :  $receivedPackets %")
                println("Ping $i - Time: $pingTime ms")
            } else {
                println("Ping $i - Failed")
                Log.d("packetloss", "failed")
            }
        }
        val packetLoss = (sentPackets - receivedPackets) / sentPackets.toDouble() * 100
        Log.d("packetloss", "paket loss :  $packetLoss %")
        return packetLoss
    }

    private fun showError() {
        Toast.makeText(this, "Error retrieving ping", Toast.LENGTH_SHORT).show()
    }


    suspend fun getFileSize(url: String): Long = withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection()
            connection.setRequestProperty("Range", "bytes=0-0")
            connection.connect()
            val contentLength = connection.contentLength
            connection.getInputStream().close()
            contentLength.toLong()
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    suspend fun downloadtest() : Double {
        val url = "https://web.eitaa.com/assets/img/bg.jpeg"
        val startTime = System.currentTimeMillis()
        try {
            val fileSize = getFileSize(url)
            val connectionTime = System.currentTimeMillis() - startTime
            val downloadRate = fileSize.toDouble() / connectionTime * 1000 // Convert to bytes per second

            println("File Size: $fileSize bytes")
            println("Download Rate: $downloadRate bytes/second")
            Log.d("Downloadrate" , "Download Rate: $downloadRate bytes/second")

            return downloadRate
        } catch (e: Exception) {
            return 0.0
            // Handle the exception here, if needed
        }
    }
    fun calculatePingScore(ping: Double): Double {
        // Add your logic here to calculate the score based on ping value
        // Return a score between 0 and 1
        return when {
            ping <= 50 -> 1.0 // Excellent ping
            ping <= 100 -> 0.8 // Good ping
            ping <= 200 -> 0.6 // Average ping
            ping <= 300 -> 0.4 // Below average ping
            else -> 0.2 // Poor ping
        }
    }

    fun calculateUploadRateScore(uploadRate: Double): Double {
        // Add your logic here to calculate the score based on upload rate value
        // Return a score between 0 and 1
        return when {
            uploadRate >= 100 -> 1.0 // Excellent upload rate
            uploadRate >= 50 -> 0.8 // Good upload rate
            uploadRate >= 20 -> 0.6 // Average upload rate
            uploadRate >= 10 -> 0.4 // Below average upload rate
            else -> 0.2 // Poor upload rate
        }
    }

    fun calculateDownloadRateScore(downloadRate: Double): Double {
        // Add your logic here to calculate the score based on download rate value
        // Return a score between 0 and 1
        return when {
            downloadRate >= 100 -> 1.0 // Excellent download rate
            downloadRate >= 50 -> 0.8 // Good download rate
            downloadRate >= 20 -> 0.6 // Average download rate
            downloadRate >= 10 -> 0.4 // Below average download rate
            else -> 0.2 // Poor download rate
        }
    }

    fun calculatePacketLossScore(packetLoss: Double): Double {
        // Add your logic here to calculate the score based on packet loss value
        // Return a score between 0 and 1
        return when {
            packetLoss <= 0.1 -> 1.0 // Excellent packet loss
            packetLoss <= 0.5 -> 0.8 // Good packet loss
            packetLoss <= 1.0 -> 0.6 // Average packet loss
            packetLoss <= 2.0 -> 0.4 // Below average packet loss
            else -> 0.2 // Poor packet loss
        }
    }
    fun calculateOverallQoSScore(
        ping: Double,
        uploadRate: Double,
        downloadRate: Double,
        packetLoss: Double
    ): Double {
        var overallScore = 0.0
        val pingScore = calculatePingScore(ping)
        val uploadRateScore = calculateUploadRateScore(uploadRate)
        val downloadRateScore = calculateDownloadRateScore(downloadRate)
        val packetLossScore = calculatePacketLossScore(packetLoss)

        // Calculate the weighted average of scores
        overallScore = (pingScore * weightPing + uploadRateScore * weightUploadRate + downloadRateScore * weightDownloadRate + packetLossScore * weightPacketLoss)

        return overallScore
    }
    fun calculateOverallQoEScore(
        ping: Double,
        uploadRate: Double,
        downloadRate: Double,
        packetLoss: Double
    ): String {
        val qosScore = calculateOverallQoSScore(ping, uploadRate, downloadRate, packetLoss)

        // Group QoS score into QoE categories
        val qoeCategory =
            when (qosScore) {
                in 0.0..1.0 -> "Very Poor"
                in 1.0..2.0 -> "Poor"
                in 2.0..3.0 -> "Fair"
                in 3.0..4.0 -> "Good"
                else -> "Excellent"
            }

        return qoeCategory
    }
    fun calculateUploadRate(apiEndpoint: String, fileBytes: ByteArray) {
        val client = OkHttpClient()
        val requestBody = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), fileBytes)
        val request = Request.Builder()
            .url(apiEndpoint)
            .post(requestBody)
            .build()

        var startTime: Long = 0
        var endTime: Long = 0
        val call = client.newCall(request)

        try {
            startTime = System.currentTimeMillis()
            val response = call.execute()
            endTime = System.currentTimeMillis()

            // Calculate upload rate
            val dataSize = fileBytes.size // Size of the uploaded data in bytes
            val elapsedTime = endTime - startTime // Elapsed time in milliseconds

            val uploadRate = dataSize.toDouble() / (elapsedTime / 1000.0) // Upload rate in bytes per second

            println("Upload rate: ${uploadRate} B/s")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}