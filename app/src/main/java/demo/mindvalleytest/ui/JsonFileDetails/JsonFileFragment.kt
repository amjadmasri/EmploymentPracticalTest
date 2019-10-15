package demo.mindvalleytest.ui.JsonFileDetails

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.classic.mvloader.MVLoader

import demo.mindvalleytest.R
import demo.mindvalleytest.ui.base.BaseFragment
import demo.mindvalleytest.viewModels.ViewModelProviderFactory
import kotlinx.android.synthetic.main.json_file_fragment.*
import java.util.*
import javax.inject.Inject

class JsonFileFragment : BaseFragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    override fun getLayoutRes(): Int =R.layout.json_file_fragment

    override fun attachFragmentInteractionListener(context: Context) {
    }

    companion object {
        fun newInstance() = JsonFileFragment()
    }

    private lateinit var viewModel: JsonFileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.json_file_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this,viewModelProviderFactory).get(JsonFileViewModel::class.java)

        val jsonObserver = Observer<String> { data ->
            context?.let { MVLoader.getInstance(it).loadInto(data,{ bytes ->
                jsonText.text= String(bytes)
            },{ message ->
                Toast.makeText(context,message,Toast.LENGTH_LONG).show()
            }) }
        }

        viewModel.getJsonFileUrl()
            .observe(this,jsonObserver)
    }
}
