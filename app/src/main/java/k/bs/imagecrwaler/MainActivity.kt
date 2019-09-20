package k.bs.imagecrwaler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import k.bs.imagecrwaler.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel = createVm()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .setVariable(BR.vm, viewModel)
    }

    private fun createVm() = MainVm(object : MainVm.Contract {
        override fun toast(content: String) {
            Toast.makeText(baseContext, content, Toast.LENGTH_LONG).show()
        }
    })

    override fun onDestroy() {
        viewModel.clearDisposable()
        super.onDestroy()
    }
}
