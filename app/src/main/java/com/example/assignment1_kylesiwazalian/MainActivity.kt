package com.example.assignment1_kylesiwazalian

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.compose.ui.Modifier
import androidx.compose.foundation.*
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.lang.Double.toString
import kotlin.apply
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnCalc = findViewById<Button>(R.id.calc)
        val loanAmtView = findViewById<EditText>(R.id.loanAmountField)
        val intRateView = findViewById<EditText>(R.id.InterestRateField)
        val loanLengthView = findViewById<EditText>(R.id.PaymentsRemainingField)
        val outputView = findViewById<TextView>(R.id.outputValue)
        val switchMode = findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.navSwitch)

        intent.extras?.let { simpleBundle ->

            window.decorView.post{
                val loanAmount = simpleBundle.getString("loanAmountText")
                val intRate = simpleBundle.getString("intRateText")
                val length = simpleBundle.getString("lengthText")
                val emi = simpleBundle.getString("emiText")

                if(!loanAmount.isNullOrEmpty()) loanAmtView.setText(loanAmount)
                if(!intRate.isNullOrEmpty()) intRateView.setText(intRate)
                if(!length.isNullOrEmpty()) loanLengthView.setText(length)
                if(!emi.isNullOrEmpty()) outputView.setText(emi)
            }
        }

    btnCalc.setOnClickListener {
        if(loanAmtView.text.isNullOrBlank() || intRateView.text.isNullOrBlank() || loanLengthView.text.isNullOrBlank()){
            Toast.makeText(this, "Please ensure all fields are filled.", Toast.LENGTH_SHORT).show()
        }
        else{
            val amountText = loanAmtView.text.toString()
            val amount = amountText.toDoubleOrNull() ?: 0.0

            val intRateText = intRateView.text.toString()
            val intRate = intRateText.toDoubleOrNull() ?: 0.0
            val intRateMonthly = intRate / 12 / 100

            val lengthText = loanLengthView.text.toString()
            val length = lengthText.toDoubleOrNull() ?: 0.0

            val EMI : Double = (amount * intRateMonthly * (1+intRateMonthly).pow(length)) / ((1+intRateMonthly).pow(length)-1)

            outputView.text = String.format("%.2f", EMI)
        }
    }

    switchMode.setOnCheckedChangeListener { _, isChecked ->
        if (isChecked && switchMode.isPressed) {
            if(outputView.text.isNullOrBlank()){
                AlertDialog.Builder(this)
                    .setTitle("EMI not calculated.")
                    .setMessage("EMI has not been calculated. The other page of this app uses the built-in EMI calculation to work seamlessly with your data. Manual input is still available. Proceed?")
                    .setPositiveButton("Proceed"){_, _ ->
                        switchMode.postDelayed({val simpleBundle = Bundle().apply{
                            putString("loanAmountText", loanAmtView.text.toString())
                            putString("intRateText", intRateView.text.toString())
                            putString("lengthText", loanLengthView.text.toString())
                            putString("emiText", outputView.text.toString())
                        }

                            val intent = Intent(this, AdvancedScreen::class.java).apply{
                                putExtras(simpleBundle)
                            }
                            startActivity(intent)

                            @Suppress("DEPRECIATION")
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }, 50)
                    }
                    .setNegativeButton("Go Back") {dialog, _ ->
                        dialog.dismiss()
                        switchMode.isChecked = false
                    }
                    .setCancelable(false)
                    .show()
            }
            else{
                switchMode.postDelayed({val simpleBundle = Bundle().apply{
                    putString("loanAmountText", loanAmtView.text.toString())
                    putString("intRateText", intRateView.text.toString())
                    putString("lengthText", loanLengthView.text.toString())
                    putString("emiText", outputView.text.toString())
                }

                    val intent = Intent(this, AdvancedScreen::class.java).apply{
                        putExtras(simpleBundle)
                    }
                    startActivity(intent)

                    @Suppress("DEPRECIATION")
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }, 50)
            }



        }
    }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }


}