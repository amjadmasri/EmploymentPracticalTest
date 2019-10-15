package demo.mindvalleytest.ui.imagesList

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import demo.mindvalleytest.R
import demo.mindvalleytest.data.models.local.MvImagesLocal
import demo.mindvalleytest.ui.adapters.MVImagePagedAdapter
import demo.mindvalleytest.ui.base.BaseFragment
import demo.mindvalleytest.utilities.Resource
import demo.mindvalleytest.utilities.Status
import demo.mindvalleytest.viewModels.ViewModelProviderFactory
import kotlinx.android.synthetic.main.fragment_images_list.*
import javax.inject.Inject
import javax.inject.Provider


class ImagesListFragment : BaseFragment() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    override fun getLayoutRes(): Int = R.layout.fragment_images_list


    override fun attachFragmentInteractionListener(context: Context) {

    }

    companion object {
        fun newInstance() = ImagesListFragment()
    }

    private lateinit var viewModel: ImagesListViewModel

    @Inject
    lateinit var gridLayoutManager: Provider<GridLayoutManager>

    @Inject
    lateinit var mvImagePagedAdapter: MVImagePagedAdapter

    private lateinit var navController: NavController

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        viewModel = ViewModelProviders.of(this, viewModelProviderFactory)
            .get(ImagesListViewModel::class.java)

        image_recycler_view.layoutManager = gridLayoutManager.get()
        image_recycler_view.adapter = mvImagePagedAdapter

        val imagesObserver = Observer<Resource<PagedList<MvImagesLocal>>> { data ->
            if (data.status == Status.SUCCESS)
                mvImagePagedAdapter.submitList(data.data)
            else if (data.status.equals(Status.ERROR))
                Toast.makeText(activity, "there was an error", Toast.LENGTH_LONG).show()
        }

        viewModel.getPagedMvImagesLocalList().observe(this, imagesObserver)

        loadJsonFileButton.setOnClickListener {
            navController.navigate(R.id.action_imagesListFragment_to_jsonFileFragment)
        }
    }
}
