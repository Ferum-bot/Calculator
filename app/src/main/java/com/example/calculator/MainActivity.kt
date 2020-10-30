package com.example.calculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import javax.xml.xpath.XPathExpression

class MainActivity : AppCompatActivity() {

    private lateinit var currentValue: String

    private lateinit var buttonDigitOne: Button
    private lateinit var buttonDigitTwo: Button
    private lateinit var buttonDigitThree: Button
    private lateinit var buttonDigitFour: Button
    private lateinit var buttonDigitFive: Button
    private lateinit var buttonDigitSix: Button
    private lateinit var buttonDigitSeven: Button
    private lateinit var buttonDigitEight: Button
    private lateinit var buttonDigitNine: Button

    private lateinit var buttonExecute: Button
    private lateinit var buttonPlus: Button
    private lateinit var buttonMinus: Button
    private lateinit var buttonDivision: Button
    private lateinit var buttonMultiply: Button

    private lateinit var clearExpression: Button

    private lateinit var mainTextView: TextView

    private lateinit var clickListener: View.OnClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        currentValue = ""
    }

    override fun onStart() {
        super.onStart()
    }


    private fun setAllButtons(): List<Button> {
        buttonDigitOne = findViewById(R.id.digitOne)
        buttonDigitTwo = findViewById(R.id.digitTwo)
        buttonDigitThree = findViewById(R.id.digitThree)
        buttonDigitFour = findViewById(R.id.digitFour)
        buttonDigitFive = findViewById(R.id.digitFive)
        buttonDigitSix = findViewById(R.id.digitSix)
        buttonDigitSeven = findViewById(R.id.digitSeven)
        buttonDigitEight = findViewById(R.id.digitEight)
        buttonDigitNine = findViewById(R.id.digitNine)

        buttonExecute = findViewById(R.id.execute)
        buttonDivision = findViewById(R.id.division)
        buttonMinus = findViewById(R.id.subtraction)
        buttonPlus = findViewById(R.id.plus)
        buttonMultiply = findViewById(R.id.multiply)

        clearExpression = findViewById(R.id.clearCurrentExpression)

        mainTextView = findViewById(R.id.mainShowingView)

        return listOf<Button>(buttonDigitOne, buttonDigitTwo, buttonMultiply, buttonPlus, buttonMinus, buttonDivision, buttonExecute, buttonDigitEight,
        buttonDigitSeven, buttonDigitFive, buttonDigitFour, buttonDigitNine, buttonDigitSix, buttonDigitThree, clearExpression)
    }

    private fun addSignToValue(sign: Char) {
        currentValue += sign
        mainTextView.text = currentValue
    }

    private fun isSign(char: Char) = char !in '0'..'9'
    private fun isSign(string: String?): Boolean {
        if (string!!.length != 1) {
            return false
        }
        return isSign(string[0]!!)
    }

    private fun checkForCorrectValue() : Boolean {
        val n = currentValue.length
        for (i in 1 until n) {
            if (isSign(currentValue[i]) && isSign(currentValue[i - 1])) {
                return false
            }
        }
        if (isSign(currentValue[n - 1])) {
            return false
        }
        return true
    }

    private fun getNumberOfSign(): Int {
        var result: Int = 0
        for (el in currentValue) {
            if (el in '0'..'9') {
                result++
            }
        }
        return result
    }


    private fun getLeftOperand(array: MutableList<String?>, pos: Int): Int {
        for (i in pos - 1 downTo 0) {
            if (array[i] != null) {
                return array[i]!!.toInt()
            }
        }
        return 0
    }

    private fun getRightOperand(array: MutableList<String?>, pos: Int): Int {
        for (i in pos + 1 until array.size) {
            if (array[i] != null) {
                return array[i]!!.toInt()
            }
        }
        return 0
    }

    private fun setLeftOperandToNull(array: MutableList<String?>, pos: Int): Unit {
        for (i in pos - 1 downTo 0) {
            if (array[i] != null) {
                array[i] = null
                break
            }
        }
    }

    private fun setRightOperandToNull(array: MutableList<String?>, pos: Int): Unit {
        for (i in pos + 1 until  array.size) {
            if (array[i] != null) {
                array[i] = null
                break
            }
        }
    }

    private fun solveTheValue() {
        if (currentValue.isEmpty()) {
            mainTextView.text = "0"
            return
        }
        if (!checkForCorrectValue()) {
            mainTextView.text = "ERROR"
            currentValue = ""
            val errorToast = Toast.makeText(this, "INVALID EXPRESSION", Toast.LENGTH_SHORT)
            errorToast.show()
            return
        }
        val currentArray: MutableList<String?> = mutableListOf("0")
        val posOfSignOfFirstPriority: MutableList<Int> = mutableListOf()
        val posOfSignOfSecondPriority: MutableList<Int> = mutableListOf()
        if (!isSign(currentValue[0])) {
            currentArray.add("+")
            posOfSignOfSecondPriority.add(1)
        }
        for (el in currentValue) {
            if (isSign(el)) {
                currentArray.add("$el")
                if (el == '*' || el == '/' || el == '%') {
                    posOfSignOfFirstPriority.add(currentArray.size - 1)
                }
                else {
                    posOfSignOfSecondPriority.add(currentArray.size - 1)
                }
            }
            else {
                if (isSign(currentArray[currentArray.size - 1])) {
                    currentArray.add("$el")
                    continue
                }
                currentArray[currentArray.size - 1] = currentArray[currentArray.size - 1] + el
            }
        }
        for (pos in posOfSignOfFirstPriority) {
            val leftOperand = getLeftOperand(currentArray, pos);
            val rightOperand = getRightOperand(currentArray, pos)
            when(currentArray[pos]) {
                "*" -> currentArray[pos] = (leftOperand * rightOperand).toString()
                "/" -> currentArray[pos] = (leftOperand / rightOperand).toString()
                "%" -> currentArray[pos] = (leftOperand % rightOperand).toString()
            }
            setLeftOperandToNull(currentArray, pos)
            setRightOperandToNull(currentArray, pos)
        }
        for (pos in posOfSignOfSecondPriority) {
            val leftOperand = getLeftOperand(currentArray, pos);
            val rightOperand = getRightOperand(currentArray, pos)
            when(currentArray[pos]) {
                "+" -> currentArray[pos] = (leftOperand + rightOperand).toString()
                "-" -> currentArray[pos] = (leftOperand - rightOperand).toString()
            }
            setLeftOperandToNull(currentArray, pos)
            setRightOperandToNull(currentArray, pos)
        }
        for (el in currentArray) {
            if (el != null) {
                currentValue = el
                mainTextView.text = currentValue
                break
            }
        }
    }

    private fun doSomeAction(currentButton: View) {
        when(currentButton.id) {
            R.id.digitOne -> addSignToValue('1')
            R.id.digitTwo -> addSignToValue('2')
            R.id.digitThree -> addSignToValue('3')
            R.id.digitFour -> addSignToValue('4')
            R.id.digitFive -> addSignToValue('5')
            R.id.digitSix -> addSignToValue('6')
            R.id.digitSeven -> addSignToValue('7')
            R.id.digitEight -> addSignToValue('8')
            R.id.digitNine -> addSignToValue('9')
            R.id.plus -> addSignToValue('+')
            R.id.division -> addSignToValue('/')
            R.id.subtraction -> addSignToValue('-')
            R.id.multiply -> addSignToValue('*')
            R.id.clearCurrentExpression -> {
                currentValue = ""
                mainTextView.text = "0"
            }
            else -> solveTheValue()
        }
    }


    override fun onResume() {
        super.onResume()
        val currentButtons = setAllButtons()
        clickListener = View.OnClickListener { doSomeAction(it) }
        for (button in currentButtons) {
            button.setOnClickListener(clickListener)
        }
    }
}