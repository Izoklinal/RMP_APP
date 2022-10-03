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
import com.example.a11pac.data.models.Cost
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
        val db: MoneyDataBase = Room.databaseBuilder(this, MoneyDataBase::class.java, DATABASE_NAME).build()
        val executor = Executors.newSingleThreadExecutor()
        val moneyDAO = db.moneyDAO()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val name = findViewById<EditText>(R.id.editText_name)
        val cost = findViewById<EditText>(R.id.editText_cost)
        binding.addButton.setOnClickListener {
            listOf(if (name.text.toString() != ""
                && cost.text.toString() != "" &&
                binding.editTextDesc.text.toString() != "") {
                if (change == 0) {
                    executor.execute {
                        moneyDAO.addType(CostType(0, binding.editTextName.text.toString()))
                        moneyDAO.addCost(
                            Cost(
                                0,
                                1,
                                cost.text.toString().toFloat(),
                                binding.editTextDesc.text.toString()
                            )
                        )
                    }
                    Toast.makeText(this, "Успешно добавлено", Toast.LENGTH_SHORT).show()
                } else {
                    executor.execute {
                        moneyDAO.saveType(
                            CostType(
                                position + 1,
                                binding.editTextName.text.toString()
                            )
                        )
                        moneyDAO.saveCost(
                            Cost(
                                position + 1,
                                0,
                                cost.text.toString().toFloat(),
                                binding.editTextDesc.text.toString()
                            )
                        )
                    }
                }
            } else {
                Toast.makeText(this, "Нельзя добавить пустую строку", Toast.LENGTH_SHORT).show()
            })
        }
        binding.showButton.setOnClickListener {
            val intent = Intent(this, ShowActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        val db: MoneyDataBase = Room.databaseBuilder(this, MoneyDataBase::class.java, DATABASE_NAME).build()
        val moneyDAO = db.moneyDAO()
        position = intent.getIntExtra("pos", -1)
        Log.d("work", "position - $position")
        val tempCost = moneyDAO.getAllCosts()
        val tempType = moneyDAO.getAllTypes()
        if (position!=-1)
        {
            tempCost.observe(this, androidx.lifecycle.Observer {
                binding.editTextCost.setText(it[position+1].cost.toString())
                binding.editTextDesc.setText(it[position+1].description)
            })
            tempType.observe(this, androidx.lifecycle.Observer {
                binding.editTextName.setText(it[position+1].title)
            })
            change = 1
            binding.addButton.text = "Изменить"
        } else {
            change = 0
            binding.addButton.text = "Добавить"
        }
    }
}
