package com.bf.calc

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : Activity() {
    val list_a = ArrayList<String>()
    val list_b = ArrayList<String>()
    val list_c = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }


    fun initView() {
        btn_A.setOnClickListener(View.OnClickListener {
            val url_a = Environment.getExternalStorageDirectory().toString() + File.separator + "calc" + File.separator + "a"
            readFile(url_a)
        })

        btn_B.setOnClickListener(View.OnClickListener {
            val url_b = Environment.getExternalStorageDirectory().toString() + File.separator + "calc" + File.separator + "b"
            readFile(url_b)
        })

        btn_C.setOnClickListener(View.OnClickListener {
            if (list_a.size == 0) {
                Toast.makeText(this@MainActivity, "请先加载A组数据", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (list_b.size == 0) {
                Toast.makeText(this@MainActivity, "请先加载B组数据", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            val url_c = Environment.getExternalStorageDirectory().toString() + File.separator + "calc" + File.separator + "c"
            var file = File(url_c)
            if (file.exists()) {
                file.delete()
            }
            calc(url_c)
        })
    }

    fun calc(path: String) {
        for (i in list_a) {
            for (j in list_b) {
                initData(i, j)
            }
        }
        writeFile(path, list_c)
    }

    fun writeFile(path: String, list: ArrayList<String>) {
        var sb = StringBuffer()
        for (i in list) {
            sb.append(i).append(" ")
        }
        var file = File(path)
        if (!file.exists()) {
            file.createNewFile()
        }
        var writer = FileWriter(path, true)
        var buffWriter = BufferedWriter(writer)
        try {
            buffWriter.write(sb.toString())
        } catch (e: IOException) {

        } finally {
            buffWriter.close()
            Toast.makeText(this@MainActivity, "C组数据生成完成", Toast.LENGTH_SHORT).show()
        }
    }


    fun initData(ss: String, sa: String) {
        if (ss.substring(2).equals(sa.subSequence(0, 1))) {
            var target = ss + (sa.substring(1))
            list_c.add(target)
            Log.e("target", "msg=" + target)
        }
    }

    fun readFile(path: String) {
        var file = File(path)
        if (null != file && file.exists()) {
            var reader = FileInputStream(file)
            var buffer = StringBuffer()
            var byte = ByteArray(1024)
            try {
                var by = 0
                //注意这种语法
                while (reader.read(byte).apply { by = this } != -1) {
                    //Log.e("by", "=" + String(byte, 0, by))
                    buffer.append(String(byte, 0, by))
                }

                while (reader.read(byte).apply { by=this }!=-1){

                }

                Log.e("buffer", "buffer= " + buffer.toString())
                getArray(buffer.toString(), path)
            } catch (e: IOException) {

            } finally {
                reader.close()
            }
        }

    }


    fun getArray(tr: String, path: String) {
        var sr = tr.split(" ")
        for (i in sr) {
            Log.e("sr", "sr=" + i)
            if ((File.separator + "a") in path) {
                if (i.contains("\n")) {
                    list_a.add(i.replace("\n", ""))
                } else {
                    list_a.add(i)
                }
            } else {
                if (i.contains("\n")) {
                    list_b.add(i.replace("\n", ""))
                } else {
                    list_b.add(i)
                }
            }
        }


        if ((File.separator + "a") in path) {
            Log.e("list_a", "msg" + (list_a == null))
            Toast.makeText(this@MainActivity, "A组数据加载完成", Toast.LENGTH_SHORT).show()
        } else {
            Log.e("list_a", "msg" + (list_b == null))
            Toast.makeText(this@MainActivity, "B组数据加载完成", Toast.LENGTH_SHORT).show()
        }
    }


}
