package pl.pycotech

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private const val PENDING_OPERATION_CONTENT = "PendingOperationContent"
private const val OPERAND1_CONTENT = "Operand1Content"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"
private const val DISPLAY_OPERATION_CONTENT = "DisplayOperationContent"

class MainActivity : AppCompatActivity() {
//    private lateinit var result: EditText //private var result: EditText? = null - lateinit -> we will give value but later in the code
//   //private val result by lazy { findViewById<EditText>(R.id.result) }
//    private lateinit var newNumber: EditText
//    private val displayOperation by lazy (LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }

    //Variables to hold the operand and type of calculation
    private var operand1: Double? = null
    private var pendingOperation: String = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

/*        result = findViewById(R.id.result)
        newNumber = findViewById(R.id.newNumber)

        //Data input buttons
        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9 = findViewById<Button>(R.id.button9)

        //Operation buttons
        val buttonDivide = findViewById<Button>(R.id.buttonDivide)
        val buttonMultiply = findViewById<Button>(R.id.buttonMultiply)
        val buttonMinus = findViewById<Button>(R.id.buttonMinus)
        val buttonPlus = findViewById<Button>(R.id.buttonPlus)
        val buttonDot = findViewById<Button>(R.id.buttonDot)
        val buttonEquals = findViewById<Button>(R.id.buttonEquals)*/

        val listener = View.OnClickListener { v ->
            newNumber.append((v as Button).text)
        }

        val numberButtonsArray = listOf<Button>(button0, button1, button2, button3, button4, button5,
            button6, button7, button8, button9, buttonDot)

        for (b in numberButtonsArray) {
            b.setOnClickListener(listener)
        }

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch(e: NumberFormatException) {
                newNumber.setText("")
            }

            pendingOperation = op
            operation.text = pendingOperation

        }

        val operatorButtonsArray = listOf<Button>(buttonDivide, buttonMultiply, buttonMinus,
            buttonPlus, buttonEquals)

        for (b in operatorButtonsArray) {
            b.setOnClickListener(opListener)
        }

        buttonNeg.setOnClickListener {
            val value = newNumber.text.toString()
            if (value.isEmpty()) {
                newNumber.setText("-")
            } else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())

                } catch (e: java.lang.NumberFormatException){
                    //newNumber was "-" or ".", so clear it
                    newNumber.setText("")
                }
            }
        }

        //TODO add clear operation button
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand1 == null) {
            operand1 = value
        } else {

            if (pendingOperation == "=") {
                pendingOperation = operation
            }

            when (pendingOperation) {
                "=" -> operand1 = value
                "/" -> operand1 = if (value == 0.0) {
                    Double.NaN //handle attempt do divide by zero
                } else {
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PENDING_OPERATION_CONTENT, pendingOperation)
        outState.putString(DISPLAY_OPERATION_CONTENT, operation.text.toString())
        if (operand1 != null) {
            outState.putDouble(OPERAND1_CONTENT, operand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        pendingOperation = savedInstanceState.getString(PENDING_OPERATION_CONTENT, "")
        operation.text = savedInstanceState.getString(DISPLAY_OPERATION_CONTENT)

        operand1 = if(savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false)) {
            savedInstanceState.getDouble(OPERAND1_CONTENT)
        } else {
            null
        }
    }
}
