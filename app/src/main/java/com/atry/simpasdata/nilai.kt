package com.atry.simpasdata
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.atry.simpasdata.R
import com.atry.simpasdata.model.JuniorData

class nilai(private val juniorDataList: List<JuniorData>) :
    RecyclerView.Adapter<nilai.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_nilai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val juniorData = juniorDataList[position]
        holder.bind(juniorData)
    }

    override fun getItemCount(): Int {
        return juniorDataList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nisnTextView: TextView = itemView.findViewById(R.id.nisnTextView)
        private val namaTextView: TextView = itemView.findViewById(R.id.namaTextView)
        private val kelasTextView: TextView = itemView.findViewById(R.id.kelasTextView)
        private val rataSikapTextView: TextView = itemView.findViewById(R.id.rataSikapTextView)
        private val rataPolaPikirTextView: TextView = itemView.findViewById(R.id.rataPolaPikirTextView)
        private val rataKeaktifanTextView: TextView = itemView.findViewById(R.id.rataKeaktifanTextView)
        private val rataPBBTextView: TextView = itemView.findViewById(R.id.rataPBBTextView)
        private val rataKeseluruhanTextView: TextView = itemView.findViewById(R.id.rataKeseluruhanTextView)
        private val rataPelanggaranTextView: TextView = itemView.findViewById(R.id.rataPelanggaranTextView)
        private val nilaiAlfabetTextView: TextView = itemView.findViewById(R.id.nilaiAlfabetTextView)

        fun bind(juniorData: JuniorData) {
            nisnTextView.text = "NISN: ${juniorData.nisn}"
            namaTextView.text = "Nama: ${juniorData.nama}"
            kelasTextView.text = "Kelas: ${juniorData.kelas}"
            rataSikapTextView.text = "Rata Sikap: ${juniorData.rataSikap}"
            rataPolaPikirTextView.text = "Rata Pola Pikir: ${juniorData.rataPolaPikir}"
            rataKeaktifanTextView.text = "Rata Keaktifan: ${juniorData.rataKeaktifan}"
            rataPBBTextView.text = "Rata PBB: ${juniorData.rataPBB}"
            rataKeseluruhanTextView.text = "Rata Keseluruhan: ${juniorData.rataKeseluruhan}"
            rataPelanggaranTextView.text = "Rata Pelanggaran: ${juniorData.rataPelanggaran}"
            nilaiAlfabetTextView.text = "Nilai Alfabet: ${juniorData.nilaiAlfabet}"
        }
    }
}
