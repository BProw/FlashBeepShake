package com.android.flashbeepshake

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.transition.Visibility
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isInvisible
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.util.*

/**
 *                          APP DESCRIPTION **************************************************************************
 * @author Brian LeProwse
 * @version 1 (10.24.19)
 *
 * Images used for ImageButton references:
 * Flashlight icon made by Smashicons from www.flaticon.com
 * Vibrate icon made by Those Icons from www.flaticon.com
 * R2D2 icon made by Those Icons from www.flaticon.com
 */
class MainActivity : AppCompatActivity() {

    // Flashlight activation boolean.
    private var flashLightBtnClicked = true

    // Condensed messages.
    private val vibrateMsg = "Shaking..."
    private val beepMsg = "Beeping..."
    private val flashOnMsg = "Flash on..."
    private val flashOffMsg = "Flash off..."

    /**
     *
     */
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Image btn for camera flash.
        val flashLight = findViewById<ImageButton>(R.id.flashLight)
        // Image btn for beep sound.
        val beep = findViewById<ImageButton>(R.id.beep)
        // Image btn for device vibrate
        val vibrate = findViewById<ImageButton>(R.id.vibrate)
        // Main TextView
        val flashBeepShake = findViewById<TextView>(R.id.flashBeepShake)

        // Generate sound
        val beepSound = ToneGenerator(AudioManager.STREAM_MUSIC, Int.MAX_VALUE)

        // Vibrator :)
        val vibrateDevice: Vibrator =
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Access camera functions.
        val camera =
            getSystemService(Context.CAMERA_SERVICE) as CameraManager

        // Camera of device - accesses flash.
        val cameraID = camera.cameraIdList[0]

        // Determine action when flashlight image is clicked method.
        flashLight.setOnClickListener {

            when (flashLightBtnClicked) {
                true -> turnOnFlash(cameraID, camera)
                false -> turnOffFlash(cameraID, camera)
            }
        }

        // Sound when R2D2 image clicked function.
        beep.setOnClickListener {
            makeBeepNoise(beepSound)
        }

        // Vibrate device when image clicked function.
        vibrate.setOnClickListener {
            vibrateDevice(vibrateDevice)
        }

    }



    /**
     * Vibrate device on shake phone image click.
     * @param vibrateDevice Vibrator system service
     * @RequiresAPI Build.VERSION_Codes.0 - needed to
     * Vibrate System Service, AndroidStudio suggestion.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibrateDevice(vibrateDevice: Vibrator) {
        vibrateDevice.vibrate(
            VibrationEffect.createOneShot(1800, VibrationEffect.DEFAULT_AMPLITUDE)
        )

        val toast = Toast.makeText(this, vibrateMsg, Toast.LENGTH_SHORT)

        toast.setGravity(Gravity.RIGHT, 25, 800)
        toast.show()
    }

    /**
     * Activate beep noise.
     * @param beepSound Tone generated on button click.
     */
    private fun makeBeepNoise(beepSound: ToneGenerator) {

        // Duration of tone.
        var toneDuration = 2500

        beepSound.startTone(ToneGenerator.TONE_CDMA_NETWORK_BUSY, toneDuration)

        val toast = Toast.makeText(this, beepMsg, Toast.LENGTH_SHORT)

        toast.setGravity(Gravity.LEFT, 25, 800)
        toast.show()

    }

    /**
     * Activate camera flash.
     *
     * @param cameraID ID of camera flash
     * @param camera Utility to access camera function
     *
     * @TargetAPI Build_VERSION_CODES.M - setTorchMode
     *            requires API level 23 (current min is 14).
     */
    @TargetApi(Build.VERSION_CODES.M)
    // @RequiresApi(Build.VERSION_CODES.M)
    private fun turnOnFlash(cameraID: String, camera: CameraManager): Boolean {

        // Turn on flash.
        camera.setTorchMode(cameraID, flashLightBtnClicked)
        Toast.makeText(this, flashOnMsg, Toast.LENGTH_SHORT).show()
        // Reset flashlight button to off.
        flashLightBtnClicked = false

        return flashLightBtnClicked
    }

    /**
     * Deactivate camera flash.
     *
     * @param cameraID ID of camera flash
     * @param camera Utility to access camera function
     *
     * @TargetAPI Build_VERSION_CODES.M - setTorchMode
     *            requires API level 23 (current min is 14).
     *
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun turnOffFlash(cameraID: String, camera: CameraManager): Boolean {

        // Turn off flash.
        camera.setTorchMode(cameraID, flashLightBtnClicked)
        Toast.makeText(this, flashOffMsg, Toast.LENGTH_SHORT).show()
        // Reset flashlight button to on.
        flashLightBtnClicked = true

        return flashLightBtnClicked
    }


}
