package k.bs.imagecrwaler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import k.bs.imagecrwaler.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    private val viewModel = MainVm()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .setVariable(BR.vm, viewModel)
    }

    override fun onDestroy() {
        viewModel.clearDisposable()
        super.onDestroy()
    }
}
