package com.example.colorgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.colorgenerator.databinding.ActivityMainBinding
import kotlin.random.Random


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var mShakeDetector: ShakeDetector? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var colorHexList: ArrayList<kotlin.String>
    private lateinit var colorList: ArrayList<ColorStateList>
    private var textToCopy: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSensor()
        colorList = ArrayList()
        colorHexList = ArrayList()
        generateColor()
        binding.colorCard0.setOnClickListener(this)
        declareOnClick()
        binding.btnGenerateColor.setOnClickListener {
            generateColor()
        }
    }

    private fun declareOnClick(){
        binding.colorCard0.setOnClickListener(this)
        binding.colorCard1.setOnClickListener(this)
        binding.colorCard2.setOnClickListener(this)
        binding.colorCard3.setOnClickListener(this)
        binding.colorCard4.setOnClickListener(this)
        binding.colorCard5.setOnClickListener(this)
    }

    fun generateColor() {
        colorList.clear()
        colorHexList.clear()
        for (i in 0..5) {
            var r: Int = Random.nextInt(255)
            var g: Int = Random.nextInt(255)
            var b: Int = Random.nextInt(255)
            var color = Color.argb(255, r, g, b)
            colorList.add(ColorStateList.valueOf(color))
            colorHexList.add(String.format("#%02x%02x%02x", r, g, b))
        }
        var color0 = colorList.get(0)
        var color1 = colorList.get(1)
        var color2 = colorList.get(2)
        var color3 = colorList.get(3)
        var color4 = colorList.get(4)
        var color5 = colorList.get(5)
        var hex0 = colorHexList.get(0)
        var hex1 = colorHexList.get(1)
        var hex2 = colorHexList.get(2)
        var hex3 = colorHexList.get(3)
        var hex4 = colorHexList.get(4)
        var hex5 = colorHexList.get(5)
        var buttonBackground: Int =
            Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
        binding.btnGenerateColor.setBackgroundColor(buttonBackground)
        applyColors(color0, color1, color2, color3, color4, color5)
        applyColorHexNames(hex0, hex1, hex2, hex3, hex4, hex5)

    }

    fun applyColorHexNames(hex0: kotlin.String, hex1: kotlin.String, hex2: kotlin.String, hex3: kotlin.String, hex4: kotlin.String, hex5: kotlin.String) {
        binding.txtColor0.text = hex0
        binding.txtColor1.text = hex1
        binding.txtColor2.text = hex2
        binding.txtColor3.text = hex3
        binding.txtColor4.text = hex4
        binding.txtColor5.text = hex5
    }

    fun applyColors(
        color0: ColorStateList,
        color1: ColorStateList,
        color2: ColorStateList,
        color3: ColorStateList,
        color4: ColorStateList,
        color5: ColorStateList
    ) {
        binding.colorCard0.setCardBackgroundColor(color0)
        binding.colorCard1.setCardBackgroundColor(color1)
        binding.colorCard2.setCardBackgroundColor(color2)
        binding.colorCard3.setCardBackgroundColor(color3)
        binding.colorCard4.setCardBackgroundColor(color4)
        binding.colorCard5.setCardBackgroundColor(color5)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                binding.colorCard0.id -> copyTextToClipboard(binding.txtColor0.text.toString())
                binding.colorCard1.id -> copyTextToClipboard(binding.txtColor1.text.toString())
                binding.colorCard2.id -> copyTextToClipboard(binding.txtColor2.text.toString())
                binding.colorCard3.id -> copyTextToClipboard(binding.txtColor3.text.toString())
                binding.colorCard4.id -> copyTextToClipboard(binding.txtColor4.text.toString())
                binding.colorCard5.id -> copyTextToClipboard(binding.txtColor5.text.toString())
            }
        }
    }

    private fun copyTextToClipboard(colorText: String) {
        textToCopy = colorText
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this, "Color Copied! $textToCopy", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager!!.registerListener(
            mShakeDetector,
            mAccelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        mSensorManager!!.unregisterListener(mShakeDetector)
        super.onPause()
    }

    private fun initSensor() {
        // ShakeDetector initialization
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mShakeDetector = ShakeDetector()
        mShakeDetector!!.setOnShakeListener(object : ShakeDetector.OnShakeListener {
            override fun onShake(count: Int) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                generateColor()
            }
        })
    }
}