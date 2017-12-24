package id.asmith.someapp.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import id.asmith.someapp.R
import id.asmith.someapp.data.local.SQLHandler
import id.asmith.someapp.ui.activities.AuthActivity
import kotlinx.android.synthetic.main.fragment_auth_lock_user.*
import org.jetbrains.anko.*

class AuthLockFragment : Fragment(), PopupMenu.OnMenuItemClickListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth_lock_user, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val userDetail = SQLHandler(activity).getUser()
        val name = userDetail["name"]
        val email = userDetail["email"]

        text_lock_name.text = name
        text_lock_email.text = email



        button_lock_go.setOnClickListener {

            activity!!.longToast("awww")
        }

        button_lock_menu.setOnClickListener { view ->
            showMenu(view)
        }

    }

    private fun showMenu(view: View) {
        val popup = PopupMenu(activity, view)
        popup.setOnMenuItemClickListener(this@AuthLockFragment)
        popup.inflate(R.menu.menu_lock)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        when (item?.itemId) {

            R.id.action_delete ->
                activity?.alert("Are you sure? ",
                        "Remove account") {
                    yesButton {
                        SQLHandler(activity).deleteUserData()
                        activity?.finish()
                        activity?.startActivity<AuthActivity>()
                        activity?.toast("Whoops")
                    }
                }?.show()?.setCancelable(false)


        }

        return true
    }



}
