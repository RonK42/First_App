package com.example.hm_1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textview.MaterialTextView
import dev.tomco.a25a_10357_l06.R


class MapFragment : Fragment() {


    private lateinit var map_LBL_title: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_map, container, false)
        findViews(v)
        return v
    }

    private fun findViews(v: View) {
        map_LBL_title = v.findViewById(R.id.map_LBL_title)
    }

    fun zoom(lat: Double, lon: Double) {
        map_LBL_title.text = buildString {
            append("📍\n")
            append(lat)
            append(",\n")
            append(lon)
        }
    }


}