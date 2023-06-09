import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.NumberFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.nowusee.festo.R
import com.nowusee.festo.data.res.BoothMenuListRes
import java.util.Locale

class MenuAdapter(val context: Context, val MenuList: List<BoothMenuListRes>) : BaseAdapter() {
    var total = 0 // 합계를 저장할 변수
    lateinit var totalTextView: TextView // 합계를 보여줄 TextView

    override fun getCount(): Int {
        return MenuList.size
    }

    override fun getItem(position: Int): Any {
        return MenuList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_menu, null)
        var image = view.findViewById<ImageView>(R.id.menuImage)
        var name = view.findViewById<TextView>(R.id.menuName)
        var price = view.findViewById<TextView>(R.id.menuPrice)
        var check = view.findViewById<TextView>(R.id.menuCheck)
        var cnt = view.findViewById<TextView>(R.id.menuCnt)

        var menu = MenuList[position]

        name.text = menu.name
        val formatter: NumberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
        val formattedString = formatter.format(menu.price)
        price.text = "${formattedString}원"
        cnt.text = menu.cnt.toString()
        Glide.with(view.getContext())
            .load(menu.imageUrl)
            .into(image)

        if (totalTextView == null) {
            totalTextView = parent!!.findViewById(R.id.totalTextView)
        }

        val addBtn = view.findViewById<ImageView>(R.id.menuAdd)
        addBtn.setOnClickListener{
            menu.cnt += 1
            menu.check = true
            total += menu.price
            val formatter: NumberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
            val formattedString = formatter.format(total)
            totalTextView.text = "${formattedString}원"
            notifyDataSetChanged()
        }

        val minusBtn = view.findViewById<ImageView>(R.id.menuMinus)
        minusBtn.setOnClickListener{
            if (menu.cnt > 1) {
                menu.cnt -= 1
                if (menu.check) {
                    total -= menu.price
                    totalTextView.text = total.toString()
                }
                notifyDataSetChanged()
            } else if (menu.cnt == 1) {
                menu.check = false
                menu.cnt -= 1
                total -= menu.price
                totalTextView.text = total.toString()
                notifyDataSetChanged()
            }
        }

        val checkBtn = view.findViewById<CheckBox>(R.id.menuCheck)
        checkBtn.isChecked = menu.check // 체크박스의 초기 상태 설정
        checkBtn.setOnCheckedChangeListener{_, isChecked ->
            menu.check = isChecked
            if (isChecked) { // 체크된 경우
                total += menu.price * menu.cnt // 합계에 더하기
            } else { // 체크 해제된 경우
                total -= menu.price * menu.cnt // 합계에서 빼기
            }
            totalTextView.text = total.toString() // 합계 TextView 갱신
        }

        return view
    }
}
