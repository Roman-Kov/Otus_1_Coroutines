package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(viewData: CatsViewData) {
        findViewById<TextView>(R.id.fact_textView).text = viewData.fact ?: "No facts"
        findViewById<AppCompatImageView>(R.id.cat_imageView)?.let { imageView ->
            Picasso.get()
                .load(viewData.imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView)
        }
    }

    override fun showServerError(isServerError: Boolean, errorMessage: String) {
        val errorText = if (isServerError) context.getText(R.string.server_error) else errorMessage
        Toast.makeText(context, errorText, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(viewData: CatsViewData)
    fun showServerError(isServerError: Boolean, errorMessage: String)
}