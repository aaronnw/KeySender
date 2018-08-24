package personal.aaron.keysender

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import personal.aaron.keysender.R

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


/**
 * Created by Girish Bhalerao on 5/4/2017.
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var mTextViewReplyFromServer: TextView? = null
    private var mEditTextSendMessage: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSend = findViewById<View>(R.id.btn_send) as Button

        mEditTextSendMessage = findViewById<View>(R.id.edt_send_message) as EditText
        mTextViewReplyFromServer = findViewById<View>(R.id.tv_reply_from_server) as TextView


        buttonSend.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.btn_send -> sendMessage(mEditTextSendMessage!!.text.toString())
        }
    }

    private fun sendMessage(msg: String) {

        val handler = Handler()
        val thread = Thread(Runnable {
            try {
                //Replace below IP with the IP of that device in which server socket open.
                //If you change port then change the port number in the server side code also.
                val s = Socket("192.168.0.102", 10000)


                println("socket connected")

                val out = s.getOutputStream()

                val output = PrintWriter(out)

                output.println(msg)
                output.flush()

                val input = BufferedReader(InputStreamReader(s.getInputStream()))
                val st = input.readLine()

                handler.post {
                    val s = mTextViewReplyFromServer!!.text.toString()
                    if (st.trim { it <= ' ' }.length != 0)
                        mTextViewReplyFromServer!!.text = "$s\nFrom Server : $st"
                }

                output.close()
                out.close()
                s.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        })

        thread.start()
    }
}