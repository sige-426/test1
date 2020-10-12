package com.websarva.wings.android.newproductsp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WeatherNews : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_news)

        // Listを取得
        val lvCityList = findViewById<ListView>(R.id.lvCityList)
        val cityList: MutableList<MutableMap<String, String>> = mutableListOf()

//        var city = mutableMapOf("name" to "大阪", "id" to "1853908")
//        cityList.add(city)
//        city = mutableMapOf("name" to "神戸", "id" to "1859171")
//        cityList.add(city)
//        city = mutableMapOf("name" to "豊岡", "id" to "2127712")
//        cityList.add(city)
//        city = mutableMapOf("name" to "京都", "id" to "1857910")
//        cityList.add(city)
//        city = mutableMapOf("name" to "長野", "id" to "1856207")
//        cityList.add(city)

        var city = mutableMapOf("name" to "上田", "id" to "lat=36.390&lon=138.218")
        cityList.add(city)
        city = mutableMapOf("name" to "松本", "id" to "lat=36.390&lon=138.218")
        cityList.add(city)
        city = mutableMapOf("name" to "諏訪", "id" to "lat=36.390&lon=138.218")
        cityList.add(city)
        city = mutableMapOf("name" to "長野", "id" to "lat=36.648&lon=137.974")
        cityList.add(city)
        city = mutableMapOf("name" to "東御", "id" to "lat=36.360&lon=138.306")
        cityList.add(city)

        // アダプタ - - - - -
        val from = arrayOf("name")
        val to = intArrayOf(android.R.id.text1)
        // アダプタの設定
        val adapterView = SimpleAdapter(applicationContext, cityList, R.layout.support_simple_spinner_dropdown_item, from, to)
        // ListViewにSimpleAdapterをセット
        lvCityList.adapter = adapterView
        lvCityList.onItemClickListener = ListItemClickListener()
    }

    //

    /**
     *
     */
    private inner class ListItemClickListener : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            val item = parent.getItemAtPosition(position) as Map<String, String>
            val cityName = item["name"]
            val cityId = item["id"]

            // Item選択時に非同期処理を実行
            val receiver = WeatherInfoReceiver()
            receiver.execute(cityId)

            // 取得した都市名をtvCityNameに設定。
            val tvCityName = findViewById<TextView>(R.id.tvCityName)
            tvCityName.text = cityName + "の天気："
        }
    }

    /**
     * 非同期処理を実装
     */
    private inner class WeatherInfoReceiver() : AsyncTask<String, String, String>(){
        override fun doInBackground(vararg params: String): String {
            val id = params[0]
            val keyCode = "05b7ae07049977124b2730d6a5f98a51"
//            val urlBase: String = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s"
//            val urlBase = "http://api.openweathermap.org/data/2.5/weather?id=%s&appid=%s"
            val urlBase = "http://api.openweathermap.org/data/2.5/weather?%s&APPID=%s"
            val url = URL(urlBase.format(id, keyCode))
//            val url = URL(String.format(urlBase, id, keyCode))
//            val url = URL("http://api.openweathermap.org/data/2.5/weather?q=London&appid=05b7ae07049977124b2730d6a5f98a51")
            val conObj = url.openConnection() as HttpURLConnection
            conObj.requestMethod = "GET"
            conObj.connect()
            val stream = conObj.inputStream
            val jsonResult = is2string(stream)
            conObj.disconnect()
            stream.close()
            return jsonResult
        }

        /**
         *
         */
        override fun onPostExecute(result: String) {

            val rootJson = JSONObject(result)

            val weatherJSON: JSONObject = rootJson.getJSONArray("weather")[0] as JSONObject
            val main = weatherJSON.getString("main")
            val desc = weatherJSON.getString("description")

            // 詳細を取得
            // 詳細
            val tvWeatherTelop = findViewById<TextView>(R.id.tvWeatherTelop)
            // Desc?
            val tvWeatherDesc = findViewById<TextView>(R.id.tvWeatherDesc)

            tvWeatherTelop.text = main
            tvWeatherDesc.text = desc
        }

    }

    private fun is2string(stream: InputStream) : String {
        val sb = StringBuilder()
        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
        var line = reader.readLine()
        while (line != null){
            sb.append(line)
            line = reader.readLine()
        }
        reader.close()
        return sb.toString()
    }



}