package com.example.pertemuansharedpreference

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotifReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        when (intent.action) {
            "ACTION_LOGOUT" -> {
                // Hapus data sesi pengguna di SharedPreferences
                val prefManager = PrefManager.getInstance(context)
                prefManager.clearUsername()

                // Tampilkan pesan Toast
                Toast.makeText(context, "Logout berhasil", Toast.LENGTH_SHORT).show()

                // Kembali ke halaman login
                val loginIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(loginIntent)
            }
            else -> {
                // Tampilkan pesan jika ACTION_LOGOUT tidak diterima
                val msg = intent.getStringExtra("MESSAGE")
                if (msg != null) {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
