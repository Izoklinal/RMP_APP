package com.example.a11pac

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.room.Room
import com.example.a11pac.data.DATABASE_NAME
import com.example.a11pac.data.MoneyDataBase
import com.example.a11pac.data.models.CostType
import com.example.a11pac.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var bookList: MutableList<Expenses> = mutableListOf()
    private var change: Int = 0
    private var position: Int = -1
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var db: MoneyDataBase = Room.databaseBuilder(this, MoneyDataBase::class.java, DATABASE_NAME).build()
        val executor = Executors.newSingleThreadExecutor()
        val moneyDAO = db.moneyDAO()
        /*executor.execute {
            moneyDAO.addType(CostType(0, "Продукты"))
            moneyDAO.addType(CostType(0, "Одежда"))
            moneyDAO.addType(CostType(0, "Коммуналка"))
        }*/
        val types = moneyDAO.getAllTypes()

        /*types.observe(this, androidx.lifecycle.Observer {
            it.forEach{
                Log.d("gigi", "${{it.id}}==={${it.title}}===")
            }
        })*/
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val name = findViewById<EditText>(R.id.editText_name)
        val cost = findViewById<EditText>(R.id.editText_cost)
        getExpense()
        binding.addButton.setOnClickListener {
            if (name.text.toString() != "" && cost.text.toString() != "") {
                if (change == 0) {
                    /*Expenses(name.text.toString(), cost.text.toString().toInt())
                    addExpense(name.text.toString(), cost.text.toString().toInt())*/
                    executor.execute {
                        moneyDAO.addType(CostType(0, binding.editTextName.text.toString()))
                    }
                    Toast.makeText(this, "Успешно добавлено", Toast.LENGTH_SHORT).show()
                } else {
                    /*bookList[position] = Expenses(name.text.toString(), cost.text.toString().toInt())
                    val preferences = getSharedPreferences("pref", MODE_PRIVATE)
                    preferences.edit {
                        this.putString("json", Gson().toJson(bookList).toString())
                    }*/
                    executor.execute {
                        moneyDAO.saveType(CostType(position, binding.editTextName.text.toString()))
                    }
                }
            } else {
                Toast.makeText(this, "Нельзя добавить пустую строку", Toast.LENGTH_SHORT).show()
            }
        }
        binding.showButton.setOnClickListener {
            val intent = Intent(this, ShowActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val name = findViewById<EditText>(R.id.editText_name)
        val cost = findViewById<EditText>(R.id.editText_cost)
        position = intent.getIntExtra("pos", -1)
        if (position!=-1)
        {
            name.setText(bookList[position].name)
            cost.setText(bookList[position].cost.toString())
            change = 1
            binding.addButton.text = "Изменить"
        } else {
            change = 0
            binding.addButton.text = "Добавить"
        }
    }
    private fun getExpense(){
        val preferences = getSharedPreferences("pref", MODE_PRIVATE)
        val json: String
        if (!preferences.contains("json"))
        {
            return
        } else {
            json = preferences.getString("json", "NOT_JSON").toString()
        }
        val temp = Gson().fromJson<List<Expenses>>(json, object: TypeToken<List<Expenses>>(){}.type)
        bookList.addAll(temp)
    }

    private fun addExpense(name: String, cost: Int)
    {
        val temp = Expenses(name, cost)
        bookList.add(temp)
        val preferences = getSharedPreferences("pref", MODE_PRIVATE)
        preferences.edit {
            this.putString("json", Gson().toJson(bookList).toString())
        }
    }
}
