package com.example.a11pac

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.a11pac.data.DATABASE_NAME
import com.example.a11pac.data.MoneyDataBase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.concurrent.Executors

class ShowActivity : AppCompatActivity() {
    private var bookList: MutableList<Expenses> = mutableListOf()
    private lateinit var rv: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        val db: MoneyDataBase = Room.databaseBuilder(this, MoneyDataBase::class.java, DATABASE_NAME).build()
        val moneyDAO = db.moneyDAO()
        val costs = moneyDAO.getAllCosts()
        val types = moneyDAO.getAllTypes()
        costs.observe(this, androidx.lifecycle.Observer {
            it.forEach {
                //Log.d("table", "${it.id} | ${it.typeId} | ${it.cost} | ${it.description} | ${it.buyDate}")
                val temp = Expenses(it.typeId.toString(), it.cost, it.description, it.buyDate)
                bookList.add(temp)
            }
            /*bookList.forEach{
                Log.d("temp", "${it.name} | ${it.cost} | ${it.desc} | ${it.date}")
            }*/
            val adapter = ExpensesRVAdapter(this, bookList)
            val rvListener = object : ExpensesRVAdapter.ItemClickListener{
                override fun onItemClick(view: View?, position: Int) {
                    val intent = Intent(this@ShowActivity, MainActivity::class.java)
                    intent.putExtra("pos", position)
                    startActivity(intent)
                    Toast.makeText(this@ShowActivity, "position: $position", Toast.LENGTH_SHORT).show()
                }
            }
            adapter.setClickListener(rvListener)
            rv = findViewById(R.id.rv)
            rv.adapter = adapter
            rv.layoutManager = LinearLayoutManager(this)
        })
    }
}