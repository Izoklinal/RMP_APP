package com.example.a11pac

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.a11pac.data.DATABASE_NAME
import com.example.a11pac.data.MoneyDataBase
import com.example.a11pac.data.models.Cost
import com.example.a11pac.data.models.CostType
import com.example.a11pac.databinding.ActivityMainBinding
import java.util.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var change: Int = 0
    private var position: Int = -1
    private lateinit var binding: ActivityMainBinding
    private val bookExpenses: MutableList<Cost> = mutableListOf()
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

        /*buttonDelte.setOnClickListener {
            executor.execute{
                knigaDAO.killBook(KnigaTypes(index+10, DeleteBook[index].name, DeleteBook[index].author, DeleteBook[index].numPages))
                knigaDAO.killZhanr(KnigaZhanr(index+10,0, DeleteBookZhanr[index].zhanr, Date()))
            }
            toast.show()
        }*/

        db.moneyDAO().getAllCosts().observe(this, androidx.lifecycle.Observer {
            bookExpenses.addAll(it)
        })

        binding.delete.setOnClickListener {
            executor.execute{
                moneyDAO.killCost(Cost(position, bookExpenses[position].typeId,
                bookExpenses[position].cost, bookExpenses[position].description,
                bookExpenses[position].buyDate))
            }
            Toast.makeText(this, "yep", Toast.LENGTH_SHORT).show()
        }

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
                                name.text.toString(),
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
                                position,
                                name.text.toString(),
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
        binding.delete.visibility = View.INVISIBLE
        val db: MoneyDataBase = Room.databaseBuilder(this, MoneyDataBase::class.java, DATABASE_NAME).build()
        val moneyDAO = db.moneyDAO()
        position = intent.getIntExtra("pos", -1)
        Log.d("working", "$position")
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
            binding.delete.visibility = View.VISIBLE
        } else {
            change = 0
            binding.addButton.text = "Добавить"
        }
    }
}
