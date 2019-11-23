package sashjakk.weather.app.ui


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

class PermissionFragment : Fragment() {

    private val code = 100

    lateinit var deferredGrant: CompletableDeferred<Boolean>
    lateinit var requiredPermission: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            code
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != code) {
            deferredGrant.complete(false)
            finish()
            return
        }

        val granted = grantResults
            .all { it == PackageManager.PERMISSION_GRANTED }

        if (!granted) {
            deferredGrant.complete(false)
            finish()
            return
        }

        deferredGrant.complete(true)
    }

    private fun finish() {
        activity?.run {
            if (isFinishing) {
                return@run
            }

            supportFragmentManager
                .beginTransaction()
                .remove(this@PermissionFragment)
                .commit()
        }
    }
}


/**
 * Permissions magic in inspired by
 *
 * https://geoffreymetais.github.io/code/runtime-permissions/#deferred-behavior
 * */
fun FragmentActivity.requestPermissionAsync(
    permission: String
): Deferred<Boolean> {
    val grant = CompletableDeferred<Boolean>()

    val permissionState = ContextCompat
        .checkSelfPermission(this, permission)

    if (permissionState == PackageManager.PERMISSION_GRANTED) {
        grant.complete(true)
        return grant
    }

    val fragment = PermissionFragment().apply {
        requiredPermission = permission
        deferredGrant = grant
    }

    supportFragmentManager
        .beginTransaction()
        .add(fragment, PermissionFragment::class.java.simpleName)
        .commit()

    return grant
}
