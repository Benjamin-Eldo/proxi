package fr.iut.proxi

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView

class SettingActivity : AppCompatActivity() {

    private lateinit var seekBar:SeekBar
    private lateinit var tvSeek : TextView
    private lateinit var geolocSwitch : Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        seekBar = findViewById(R.id.nbEventSettingBar)
        tvSeek = findViewById(R.id.tvSeekBarDisplay)
        geolocSwitch = findViewById(R.id.geolocationSwitch)
        val sharedPreferences = this?.getSharedPreferences("proxi", Context.MODE_PRIVATE)
        seekBar.progress = sharedPreferences.getInt("Number of event displayed",10)-10
        tvSeek.text = (sharedPreferences.getInt("Number of event displayed",10)).toString()
        geolocSwitch.isChecked = sharedPreferences.getBoolean("Geolocation", false)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seek: SeekBar?, progress: Int, fromUser: Boolean) {
                tvSeek.text = (progress+10).toString();
            }

            override fun onStartTrackingTouch(seek: SeekBar?) {

            }

            override fun onStopTrackingTouch(seek: SeekBar?) {
                with(sharedPreferences.edit()){
                    putInt("Number of event displayed", (seek?.progress as Int)+10)
                    apply()
                }
            }

        })

        geolocSwitch.setOnCheckedChangeListener(
            object : CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(button: CompoundButton?, isChecked: Boolean) {
                    with(sharedPreferences.edit()){
                        putBoolean("Geolocation",isChecked)
                        apply()
                    }
                }

            }
        )


    }

}