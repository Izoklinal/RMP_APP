package com.example.a11pac

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ShowActivity : AppCompatActivity() {
    private var bookList: MutableList<Expenses> = mutableListOf()
    private lateinit var rv: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        getExpense()
        showRV()
    }

    private fun showRV(){
        val adapter = ExpensesRVAdapter(this, bookList)
        val rvListener = object : ExpensesRVAdapter.ItemClickListener{
            override fun onItemClick(view: View?, position: Int) {
                val intent = Intent(this@ShowActivity, MainActivity::class.java)
                intent.putExtra("pos", position)
                var indexChanged = position
                startActivity(intent)
                Toast.makeText(this@ShowActivity, "position: $position", Toast.LENGTH_SHORT).show()
            }
        }
        adapter.setClickListener(rvListener)
        rv = findViewById(R.id.rv)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
    }


    override fun onResume() {
        super.onResume()
        getExpense()
        showRV()
    }
    private fun getExpense() {
        val preferences = getSharedPreferences("pref", MODE_PRIVATE)
        var json: String = ""
        if (!preferences.contains("json"))
        {
            return
        } else {
            json = preferences.getString("json", "NOT_JSON").toString()
        }
        val temp = Gson().fromJson<List<Expenses>>(json, object: TypeToken<List<Expenses>>(){}.type)
        bookList.addAll(temp)
    }
}