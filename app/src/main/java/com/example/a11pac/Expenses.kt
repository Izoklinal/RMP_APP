package com.example.a11pac

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

data class Expenses(var name: String, var cost: Float, var desc: String, var date: Date) {

}