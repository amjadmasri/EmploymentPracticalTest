package demo.mindvalleytest.ui

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import demo.mindvalleytest.ViewModels.ImagesListViewModel
import demo.mindvalleytest.R


class ImagesListFragment : BaseFragment() {
    override fun getLayoutRes(): Int = R.layout.fragment_images_list


    override fun attachFragmentInteractionListener(context: Context) {

    }

    companion object {
        fun newInstance() = ImagesListFragment()
    }

    private lateinit var viewModel: ImagesListViewModel


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ImagesListViewModel::class.java)

    }

}
