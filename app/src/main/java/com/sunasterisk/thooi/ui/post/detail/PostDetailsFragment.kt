package com.sunasterisk.thooi.ui.post.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.sunasterisk.thooi.R
import com.sunasterisk.thooi.base.BaseFragment
import com.sunasterisk.thooi.databinding.FragmentDetailPostBinding
import com.sunasterisk.thooi.ui.post.detail.model.PostDetailsAction.*
import com.sunasterisk.thooi.util.MarginItemDecoration
import com.sunasterisk.thooi.util.observeEvent
import com.sunasterisk.thooi.util.toast
import org.koin.android.ext.android.get

class PostDetailsFragment : BaseFragment<FragmentDetailPostBinding>() {

    private val args: PostDetailsFragmentArgs by navArgs()

    private val viewModel by viewModels<PostDetailsVM> {
        PostDetailsVM.Factory(get(), this)
    }

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean,
    ) = FragmentDetailPostBinding.inflate(inflater, container, attachToRoot).also {
        it.postDetailsVM = viewModel
        it.lifecycleOwner = viewLifecycleOwner
    }

    override fun setupView() {
        setupSliderView()
        setupAppliedFixersRecyclerView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadPost(args.postId)
    }

    override fun onObserveLiveData() = with(viewModel) {
        toastStringRes.observeEvent(viewLifecycleOwner) {
            context?.toast(getString(it))
        }
    }

    private fun setupSliderView() = with(binding.imageSliderJobThumbnails) {
        setSliderAdapter(ImageSliderAdapter())
        setIndicatorAnimation(IndicatorAnimationType.WORM)
        setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        isAutoCycle = true
        startAutoCycle()
    }

    private fun setupAppliedFixersRecyclerView() =
        with(binding.scrollViewJobDetails) {
            adapter = PostDetailsAdapter { action ->
                when (action) {
                    is SelectFixer -> viewModel.selectFixer(action.summaryUser)
                    AssignFixer -> viewModel.assignFixer()
                    ReassignFixer -> viewModel.reassignFixer()
                    CancelFixing -> viewModel.cancelFixing()
                    FinishFixing -> viewModel.finishFixing()
                    ClosePost -> viewModel.closePost()
                }
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(MarginItemDecoration(resources, R.dimen.dp_8, R.dimen.dp_8))
        }
}
