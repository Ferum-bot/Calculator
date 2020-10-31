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

    private lateinit var buttonDigitZero: Button
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
    private lateinit var buttonOst: Button

    private lateinit var clearExpression: Button
    private lateinit var deleteLastDigit: Button
    private lateinit var addPoint: Button

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

    override fun onResume() {
        super.onResume()
        val currentButtons = setAllButtons()
        clickListener = View.OnClickListener { doSomeAction(it) }
        for (button in currentButtons) {
            button.setOnClickListener(clickListener)
        }
    }

    private fun setAllButtons(): List<Button> {
        // set all digits
        buttonDigitZero = findViewById(R.id.digitZero)
        buttonDigitOne = findViewById(R.id.digitOne)
        buttonDigitTwo = findViewById(R.id.digitTwo)
        buttonDigitThree = findViewById(R.id.digitThree)
        buttonDigitFour = findViewById(R.id.digitFour)
        buttonDigitFive = findViewById(R.id.digitFive)
        buttonDigitSix = findViewById(R.id.digitSix)
        buttonDigitSeven = findViewById(R.id.digitSeven)
        buttonDigitEight = findViewById(R.id.digitEight)
        buttonDigitNine = findViewById(R.id.digitNine)

        //set all executeButtons
        buttonExecute = findViewById(R.id.execute)
        buttonDivision = findViewById(R.id.division)
        buttonMinus = findViewById(R.id.subtraction)
        buttonPlus = findViewById(R.id.plus)
        buttonMultiply = findViewById(R.id.multiply)
        buttonOst = findViewById(R.id.remainder)

        clearExpression = findViewById(R.id.clearCurrentExpression)
        deleteLastDigit = findViewById(R.id.delete)
        addPoint = findViewById(R.id.point)

        //set mainView
        mainTextView = findViewById(R.id.mainShowingView)

        return listOf<Button>(buttonDigitOne, buttonDigitTwo, buttonMultiply, buttonPlus, buttonMinus, buttonDivision, buttonExecute, buttonDigitEight,
        buttonDigitSeven, buttonDigitFive, buttonDigitFour, buttonDigitNine, buttonDigitSix, buttonDigitThree, clearExpression, deleteLastDigit, buttonDigitZero,
        buttonOst, addPoint)
    }

    private fun doSomeAction(currentButton: View) {
        when(currentButton.id) {
            R.id.digitZero -> addSignToValue('0')
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
            R.id.remainder -> addSignToValue('%')
            R.id.point -> addSignToValue('.')
            R.id.delete -> deleteLastSign()
            R.id.clearCurrentExpression -> clearMainTextView()
            else -> solveTheValue()
        }
    }

    private fun deleteLastSign() {
        if (currentValue.isEmpty()) {
            return
        }
        if (currentValue.length == 1) {
            currentValue = ""
            mainTextView.text = "0"
            return
        }
        currentValue = currentValue.substring(0, currentValue.length - 2)
        mainTextView.text = currentValue
    }

    private fun clearMainTextView(): Unit {
        currentValue = ""
        mainTextView.text = "0"
    }

    private fun addSignToValue(sign: Char): Unit {
        if (currentValue == "0") {
            currentValue = "$sign"
            mainTextView.text = currentValue
            return
        }
        currentValue += sign
        mainTextView.text = currentValue
    }

    private fun solveTheValue() {
        if (!checkForCorrect()) {
            Toast.makeText(this, "INVALID EXPRESSION", Toast.LENGTH_SHORT).show()
            mainTextView.text = "Error"
            currentValue = ""
            return
        }
    }

    private fun checkForCorrect(): Boolean {
        if (currentValue.isEmpty()) {
            return true
        }
        if (currentValue.length == 1 && (isSign(currentValue[0]) || isPoint(currentValue[0]))) {
            return false
        }
        for ((index, char) in currentValue.withIndex()) {
            when(index) {
                0 -> if (!checkForCorrectCharOnTheBorder(char)) return false
                currentValue.length - 1 -> if (!checkForCorrectCharOnTheBorder(char) || char == '+' || char == '-') return false
                else -> if (!checkForCorrectCharInTheMiddle(currentValue[index - 1], char, currentValue[index + 1])) return false
            }
        }
        return true
    }

    private fun checkForCorrectCharOnTheBorder(char: Char): Boolean {
        if (isPoint(char)) {
            return false
        }
        if (char == '*' || char == '/' || char == '%') {
            return false
        }
        return true
    }

    private fun checkForCorrectCharInTheMiddle(leftChar: Char, middleChar: Char, rightChar: Char): Boolean {
        if (isPoint(middleChar) && !isDigit(leftChar) && !isDigit(rightChar)) {
            return false
        }
        if (isSign(middleChar) && !isDigit(leftChar) && isDigit(rightChar)) {
            return false
        }
        return true
    }

    private fun isDigit(char: Char): Boolean = char in '0'..'9'
    private fun isPoint(char: Char): Boolean = char == '.'
    private fun isSign(char: Char): Boolean = !isDigit(char) && !isPoint(char)

}