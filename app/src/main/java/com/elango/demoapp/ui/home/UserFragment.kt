package com.elango.demoapp.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elango.demoapp.R
import com.elango.demoapp.listener.CommonListener
import com.elango.demoapp.model.MapModel
import com.elango.demoapp.model.User
import com.elango.demoapp.ui.home.adapter.UserAdapter
import com.elango.demoapp.ui.home.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user.*


@AndroidEntryPoint
class UserFragment : Fragment(), CommonListener {

    var data: ArrayList<Int> = arrayListOf()
    val viewModel: UserViewModel by viewModels()
    private var progressDialog: Dialog? = null
    lateinit var userAdapter: UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        viewModel.postdata.observe(requireActivity(), {
            try {
                progressBar.visibility = View.GONE
                it?.let {
                    userAdapter.update(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        viewModel._UserDetailsLD.observe(requireActivity(),
            {
                it?.let {
                    hideProgressDialog()
                    ShowUserDetailsDialog(it)
                }

            })
    }

    fun setAdapter() {
        userRV.setHasFixedSize(true)
        userAdapter = UserAdapter(requireActivity(), data, this)
        userRV.adapter = userAdapter
    }

    override fun click(value: MapModel) {
    }

    override fun click(value: Int) {
        showProgressDialog()
        viewModel.getUserDetails(value.toString())
    }

    fun ShowUserDetailsDialog(
        data: User
    ) {
        data.apply {
            var custom_dialog: Dialog? = null
            val builder = AlertDialog.Builder(context, R.style.BottomSheetDialog)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_ok_action, null)
            val dialogTitle = view.findViewById(R.id.dialog_title) as TextView
            val dialogMessage = view.findViewById(R.id.dialog_message) as TextView
            val dialogAction = view.findViewById(R.id.dialog_action_button) as TextView
            val dialogBy = view.findViewById(R.id.dialog_by) as TextView

            builder.setCancelable(false)

            if ((title == null)) {
                dialogTitle.text = requireContext().getString(R.string.app_name)
                dialogTitle.visibility = View.GONE
            } else {
                dialogTitle.text = title
                dialogTitle.visibility = View.VISIBLE
            }
            dialogBy.text = "by $by"
            dialogMessage.text = Html.fromHtml(text)
            dialogAction.text = requireContext().getString(R.string.ok)
            dialogAction.setOnClickListener {

                custom_dialog!!.dismiss()
            }

            builder.setView(view)
            custom_dialog = builder.create()
            custom_dialog!!.show()
        }
    }

    fun showProgressDialog() {
        //  hideProgressDialog()
        progressDialog = Dialog(requireContext())
        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_progress_custom, null)
        val progressBar = view.findViewById<ProgressBar>(R.id.dialog_progress_bar)
        progressBar.indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN
        )
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog!!.setContentView(view)
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    fun hideProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog!!.dismiss()
            }
        } catch (e: Exception) {
        }
    }
}