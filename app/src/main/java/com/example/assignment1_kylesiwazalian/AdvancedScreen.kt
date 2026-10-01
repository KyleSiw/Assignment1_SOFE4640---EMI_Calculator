package com.example.assignment1_kylesiwazalian

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AdvancedScreen : AppCompatActivity() {

    private lateinit var expensesContainer: LinearLayout
    private lateinit var incomeContainer: LinearLayout

    private var flipFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_advanced_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val simpleBundle : Bundle = intent.extras ?: Bundle()
        val switchMode = findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.navSwitch)
        expensesContainer = findViewById<LinearLayout>(R.id.expensesContainer)
        incomeContainer = findViewById<LinearLayout>(R.id.incomeContainer)
        val emiValue = findViewById<EditText>(R.id.emiAmount)
        val btnCalc = findViewById<Button>(R.id.calc)
        val checkboxColor = findViewById<CheckBox>(R.id.colorCheckbox)
        val outputHeaderView = findViewById<TextView>(R.id.outputHeader)
        val outputAmountView = findViewById<TextView>(R.id.outputAmount)

        addNewIncomeRow()

        addNewExpenseRow()

        switchMode.isChecked = true

        val emi = simpleBundle.getString("emiText")
        if (!emi.isNullOrEmpty()){
            emiValue.setText(emi)
        }

        btnCalc.setOnClickListener {
            val output = calculateOutput()


            if (output < 0){
                if(flipFlag){
                    outputHeaderView.setTextColor(android.graphics.Color.GREEN)
                    outputAmountView.setTextColor(android.graphics.Color.GREEN)

                    outputHeaderView.setText("DEFICIT:")
                    outputAmountView.text = "\$ %.2f".format(output)
                }
                else{
                    outputHeaderView.setTextColor(android.graphics.Color.RED)
                    outputAmountView.setTextColor(android.graphics.Color.RED)

                    outputHeaderView.setText("DEFICIT:")
                    outputAmountView.text = "\$ %.2f".format(output)
                }
            }
            else{
                if (flipFlag){
                    outputHeaderView.setTextColor(android.graphics.Color.RED)
                    outputAmountView.setTextColor(android.graphics.Color.RED)

                    outputHeaderView.setText("SAVINGS:")
                    outputAmountView.text = "\$ %.2f".format(output)
                }
                else{
                    outputHeaderView.setTextColor(android.graphics.Color.GREEN)
                    outputAmountView.setTextColor(android.graphics.Color.GREEN)

                    outputHeaderView.setText("SAVINGS:")
                    outputAmountView.text = "\$ %.2f".format(output)
                }
            }

        }



        checkboxColor.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){
                flipFlag = true
            }
            else{
                flipFlag = false
            }
        }


        switchMode.setOnCheckedChangeListener { _, isChecked ->
            if(!isChecked && switchMode.isPressed){
                val intent = Intent(this, MainActivity::class.java).apply{
                    putExtras(simpleBundle)
                }
                startActivity(intent)

                @Suppress("DEPRECIATION")
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                finish()
            }
        }

    }

    private fun addNewExpenseRow(){
        val rowView = layoutInflater.inflate(R.layout.expense_row, expensesContainer, false)
        val expenseNameInput = rowView.findViewById<EditText>(R.id.emiTextHeader)
        val expenseAmountInput = rowView.findViewById<EditText>(R.id.emiAmount)

        var hasSpawnedNextRow = false

        val autoExpandWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(!hasSpawnedNextRow && (expenseNameInput.text.isNotEmpty() || expenseAmountInput.text.isNotEmpty())){
                    hasSpawnedNextRow = true
                    addNewExpenseRow()
            }

                }

            }
        expenseNameInput.addTextChangedListener(autoExpandWatcher)
        expenseAmountInput.addTextChangedListener(autoExpandWatcher)

        expensesContainer.addView(rowView)
        }

    private fun addNewIncomeRow(){
        val rowView = layoutInflater.inflate(R.layout.income_row, incomeContainer, false)
        val incomeNameInput = rowView.findViewById<EditText>(R.id.emiTextHeader)
        val incomeAmountInput = rowView.findViewById<EditText>(R.id.emiAmount)

        var hasSpawnedNextRow = false

        val autoExpandWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if(!hasSpawnedNextRow && (incomeNameInput.text.isNotEmpty() || incomeAmountInput.text.isNotEmpty())){
                    hasSpawnedNextRow = true
                    addNewIncomeRow()
                }

            }

        }
        incomeNameInput.addTextChangedListener(autoExpandWatcher)
        incomeAmountInput.addTextChangedListener(autoExpandWatcher)

        incomeContainer.addView(rowView)
    }

    private fun calculateOutput(): Double{
        var incomeTotal = 0.0
        var expensesTotal = 0.0
        for (i in 0 until incomeContainer.childCount){
            val rowView = incomeContainer.getChildAt(i)
            val amountView = rowView.findViewById<EditText>(R.id.emiAmount)

            val amountText = amountView?.text?.toString() ?: ""

            incomeTotal += amountText.toDoubleOrNull() ?: 0.0
        }

        for (i in 0 until expensesContainer.childCount){
            val rowView = expensesContainer.getChildAt(i)
            val amountView = rowView.findViewById<EditText>(R.id.emiAmount)

            val amountText = amountView?.text?.toString() ?: ""

            expensesTotal += amountText.toDoubleOrNull() ?: 0.0
        }

        val output = incomeTotal - expensesTotal

        return output
    }

    }
