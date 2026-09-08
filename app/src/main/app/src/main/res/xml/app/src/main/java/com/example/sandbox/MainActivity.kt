package com.example.sandbox

import android.app.Activity
import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

class SandboxDeviceAdminReceiver : DeviceAdminReceiver()

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 50, 50, 50)
        }

        val btnProvision = Button(this).apply {
            text = "1. Create Temporary Sandbox Profile"
            setOnClickListener { setupManagedProfile() }
        }

        val btnPurge = Button(this).apply {
            text = "2. Purge & Self-Destruct Sandbox"
            setOnClickListener { destroySandbox() }
        }

        layout.addView(btnProvision)
        layout.addView(btnPurge)
        setContentView(layout)
    }

    private fun setupManagedProfile() {
        val intent = Intent(DevicePolicyManager.ACTION_PROVISION_MANAGED_PROFILE).apply {
            putExtra(
                DevicePolicyManager.EXTRA_PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME,
                ComponentName(this@MainActivity, SandboxDeviceAdminReceiver::class.java)
            )
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, 100)
        } else {
            Toast.makeText(this, "Managed profiles not supported on this device.", Toast.LENGTH_LONG).show()
        }
    }

    private fun destroySandbox() {
        val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        try {
            dpm.wipeData(0)
            Toast.makeText(this, "Sandbox purged successfully.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to purge: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
