package com.shaz.rpn.calculator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val mRPNStack: Stack<Long> = Stack()
    private val mRPNHistoryStack: Stack<List<Long>> = Stack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun userAction(view: View) {
        var input = ""
        when (view.id) {
            clear.id -> input = "cl"
            //delete.id -> input = "dl"
            undo.id -> input = "un"
            multiply.id -> input = "*"
            subtraction.id -> input = "-"
            addition.id -> input = "+"
            division.id -> input = "/"
            zero.id -> input = "0"
            one.id -> input = "1"
            two.id -> input = "2"
            three.id -> input = "3"
            four.id -> input = "4"
            five.id -> input = "5"
            six.id -> input = "6"
            seven.id -> input = "7"
            eight.id -> input = "8"
            nine.id -> input = "9"
        }
        command(input)
    }

    private fun command(input: String) {
        when (input) {
            "cl" -> {
                mRPNStack.clear()
                mRPNHistoryStack.clear()
            }
            /*"dl" -> if (!mRPNStack.isEmpty()) {
                mRPNHistoryStack.push(listOf(mRPNStack.pop()))
            }*/
            "un" -> {
                if (!mRPNStack.isEmpty())
                    mRPNStack.pop()
                if (!mRPNHistoryStack.isEmpty()) {
                    val history = mRPNHistoryStack.pop()
                    if (history.size > 1)
                        history.forEach { value -> mRPNStack.push(value) }
                }
            }
            else -> {
                if (TextUtils.isDigitsOnly(input)) {
                    val value = input.toLong()
                    mRPNStack.push(value)
                    mRPNHistoryStack.push(listOf(value))
                } else if (!mRPNStack.isEmpty() && mRPNStack.size > 1) {
                    val last = mRPNStack.pop()
                    val secondLast = mRPNStack.pop()
                    try {
                        when (input) {
                            "+" -> mRPNStack.push(secondLast + last)
                            "-" -> mRPNStack.push(secondLast - last)
                            "/" -> mRPNStack.push(secondLast / last)
                            "*" -> mRPNStack.push(secondLast * last)
                        }
                    } catch (e: ArithmeticException) {
                        mRPNStack.push(0)
                    } finally {
                        mRPNHistoryStack.push(listOf(secondLast, last))
                    }
                } else
                    return
            }
        }

        val values = StringBuilder()
        mRPNStack.forEach { data ->
            values.append(data).append(" ")
        }
        result.text = if (values.toString().isNotEmpty()) values.toString() else getString(R.string._0)
        undo.isEnabled = !mRPNHistoryStack.isEmpty()
    }
}
