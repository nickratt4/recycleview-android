package com.example.list;

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.list.databinding.ActivityMainBinding
import com.example.list.databinding.ListBinding
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Variabel untuk menyimpan total harga produk
    private var subtotal = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menyimpan data ke format JSON
        val data = JSONArray().apply {
            put(JSONObject().apply {
                put("produk", "Produk 1")
                put("harga", 10000)
                put("stok", 5)
            })

            put(JSONObject().apply {
                put("produk", "Produk 2")
                put("harga", 20000)
                put("stok", 3)
            })

            put(JSONObject().apply {
                put("produk", "Produk 3")
                put("harga", 30000)
                put("stok", 10)
            })
        }

        // ViewHolder untuk menghandle tiap item di RecyclerView
        class ViewHolder(val binding: ListBinding) : RecyclerView.ViewHolder(binding.root)

        // Membuat adapter untuk RecyclerView
        val adapter = object : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val binding = ListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }

            override fun getItemCount(): Int = data.length()

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val item = data.getJSONObject(position)
                val harga = item.getInt("harga")
                holder.binding.produk.text = item.getString("produk")
                holder.binding.harga.text = "Rp ${harga}"
                holder.binding.stok.text = "Stok: " + item.getInt("stok").toString()

                var jumlah = 0
                holder.binding.angka.text = jumlah.toString()

                holder.binding.plus.setOnClickListener {
                    if (jumlah < item.getInt("stok")) {
                        jumlah++
                        holder.binding.angka.text = jumlah.toString()
                        subtotal += harga
                        updateTotal()
                    }
                }

                holder.binding.minus.setOnClickListener {
                    if (jumlah > 0) {
                        jumlah--
                        holder.binding.angka.text = jumlah.toString()
                        subtotal -= harga
                        updateTotal()
                    }
                }
            }
        }

        // Set adapter ke RecyclerView
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun updateTotal() {
        val ppn = (subtotal * 0.11).toInt() // PPN 11%
        val total = subtotal + ppn


        binding.subtotalTextView.text = "Subtotal Produk: Rp ${subtotal}"
        binding.ppnTextView.text = "PPN 11%: Rp ${ppn}"
        binding.totalTextView.text = "Total: Rp ${total}"
    }
}
