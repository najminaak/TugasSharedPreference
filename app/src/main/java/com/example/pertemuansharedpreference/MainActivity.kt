package com.example.pertemuansharedpreference

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.pertemuansharedpreference.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager

    private val channelId = "TEST_NOTIF"
    private val notifId = 90

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()

        with(binding) {
            btnLogin.setOnClickListener {
                val usernameUntukLogin = "admin"
                val passwordUntukLogin = "12345"

                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()

                when {
                    username.isEmpty() || password.isEmpty() -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Username dan password harus diisi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    username == usernameUntukLogin && password == passwordUntukLogin -> {
                        prefManager.saveUsername(username)
                        checkLoginStatus()
                        Toast.makeText(this@MainActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Username atau password salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            btnLogout.setOnClickListener {
                prefManager.clearUsername()
                checkLoginStatus()
                Toast.makeText(this@MainActivity, "Logout berhasil", Toast.LENGTH_SHORT).show()
            }

            btnNotif.setOnClickListener {
                showNotification()
            }
        }
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.getUsername()
        if (isLoggedIn.isEmpty()) {
            binding.llLogin.visibility = View.VISIBLE
            binding.llLogged.visibility = View.GONE
        } else {
            binding.llLogin.visibility = View.GONE
            binding.llLogged.visibility = View.VISIBLE
        }
    }

    private fun showNotification() {
        val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }

        // untuk logout
        val logoutIntent = Intent(this, NotifReceiver::class.java).apply {
            action = "ACTION_LOGOUT"
        }
        val logoutPendingIntent = PendingIntent.getBroadcast(this, 1, logoutIntent, flag)

        // untuk baca notif
        val intent = Intent(this, NotifReceiver::class.java).putExtra("MESSAGE", "Baca selengkapnya ...")
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, flag)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notifications)
            .setContentTitle("Notifikasi PPPB")
            .setContentText("Hello World!")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(0, "Baca Notif", pendingIntent)
            .addAction(0, "Logout", logoutPendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifChannel = NotificationChannel(
                channelId,
                "Notifku",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notifManager.createNotificationChannel(notifChannel)
        }
        notifManager.notify(notifId, builder.build())
    }
}
