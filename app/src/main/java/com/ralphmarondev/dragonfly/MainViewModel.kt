package com.ralphmarondev.dragonfly

import android.app.Application
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.ralphmarondev.dragonfly.core.common.NotificationHelper
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dbRef = FirebaseDatabase.getInstance()
        .getReference("HELLO123")

    init {
        startListening()
    }

    private fun startListening() {
        viewModelScope.launch {
            try {
                dbRef.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        val lat = snapshot.child("latitude").getValue(Double::class.java)
                        val lng = snapshot.child("longitude").getValue(Double::class.java)

                        if (lat != null && lng != null) {
                            val longMessage = "Latitude: $lat\nLongitude: $lng"
                            Log.d("Firebase", longMessage)
                            sendNotification(longMessage)
                        } else {
                            Log.d("Firebase", "Vehicle moved with null latitude and longitude.")
                        }
                    }

                    override fun onChildChanged(
                        p0: DataSnapshot,
                        p1: String?
                    ) {

                    }

                    override fun onChildRemoved(p0: DataSnapshot) {

                    }

                    override fun onChildMoved(
                        p0: DataSnapshot,
                        p1: String?
                    ) {

                    }

                    override fun onCancelled(error: DatabaseError) {
                        error.toException().printStackTrace()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun sendNotification(
        longMessage: String
    ) {
        val shortMessage = "Your vehicle moved!"
        NotificationHelper.sendNotification(
            context = application.applicationContext,
            id = 2026,
            title = shortMessage,
            content = shortMessage,
            style = NotificationCompat.BigTextStyle().bigText(longMessage)
        )
    }
}