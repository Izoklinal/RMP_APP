package com.example.a11pac

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.a11pac.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private var bookList: MutableList<Expenses> = mutableListOf()
    private var change: Int = 0
    private var position: Int = -1
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val name = findViewById<EditText>(R.id.editText_name)
        val cost = findViewById<EditText>(R.id.editText_cost)
        getExpense()
        binding.addButton.setOnClickListener {
            if (name.text.toString() != "" && cost.text.toString() != "") {
                if (change == 0) {
                    Expenses(name.text.toString(), cost.text.toString().toInt())
                    addExpense(name.text.toString(), cost.text.toString().toInt())
                    Toast.makeText(this, "Успешно добавлено", Toast.LENGTH_SHORT).show()
                } else {
                    bookList[position] = Expenses(name.text.toString(), cost.text.toString().toInt())
                    val preferences = getSharedPreferences("pref", MODE_PRIVATE)
                    preferences.edit {
                        this.putString("json", Gson().toJson(bookList).toString())
                    }
                    val what = bookList[position]
                    Toast.makeText(this, "$what", Toast.LENGTH_SHORT).show()
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
