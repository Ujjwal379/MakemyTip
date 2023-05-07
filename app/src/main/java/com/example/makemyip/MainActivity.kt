package com.example.makemyip

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat


private const val TAG = "MainActivity"
private const val INITIAL_TIP = 0
class MainActivity : AppCompatActivity() {
    private lateinit var etbaseamt: EditText
    private lateinit var etseekbar: SeekBar
    private lateinit var tv_tiplevel: TextView
    private lateinit var tvtotal_amt: TextView
    private lateinit var tv_tipamt: TextView
    private lateinit var tvtipfeedback: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etbaseamt = findViewById(R.id.etbaseamt)
        etseekbar = findViewById(R.id.etseekbar)
        tv_tiplevel = findViewById(R.id.tv_tiplevel)
        tvtotal_amt = findViewById(R.id.tvtotal_amt)
        tv_tipamt = findViewById(R.id.tv_tipamt)
        tvtipfeedback = findViewById(R.id.tvtipfeedback)



        etseekbar.progress = INITIAL_TIP
        tv_tipamt.text = "$INITIAL_TIP%"
        updatetipfeedback(INITIAL_TIP)
        etseekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tv_tiplevel.text = "$progress%"
                computeTipandTotal()
                updatetipfeedback(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        etbaseamt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipandTotal()

            }

        })

    }

    private fun updatetipfeedback(tippercent: Int) {
        val TipFeedback = when(tippercent) {
            in 0..9 -> "Poor"
            in 9..14 -> "Acceptable"
            in 14..18 -> "Good"
            in 18..24 -> "Great"
            else -> "Amazing"
        }
        tvtipfeedback.text = TipFeedback
        // update color based on tippercent
        val color = ArgbEvaluator().evaluate(
            tippercent.toFloat() / etseekbar.max,
            ContextCompat.getColor(this,R.color.color_worst_tip),
            ContextCompat.getColor(this,R.color.color_best_tip)
        ) as Int
        tvtipfeedback.setTextColor(color)
    }

    private fun computeTipandTotal() {
        if(etbaseamt.text.isEmpty()){
            tv_tipamt.text=""
            tvtotal_amt.text=""
            return
        }
        //Get the value of tip percent and base value
        val baseamount = etbaseamt.text.toString().toDouble()
        val tippercent = etseekbar.progress
        //compute tip and total
        val tipamt = baseamount * tippercent / 100
        val totalamt = baseamount + tipamt
        tv_tipamt.text = "%.2f".format(tipamt)
        tvtotal_amt.text = "%.2f".format(totalamt)
    }
}