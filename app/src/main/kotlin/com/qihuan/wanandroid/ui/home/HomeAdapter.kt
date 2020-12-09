/* (C)2020 */
package com.qihuan.wanandroid.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ActivityUtils
import com.qihuan.wanandroid.R
import com.qihuan.wanandroid.eRecycle.EAdapter
import com.qihuan.wanandroid.eRecycle.EViewHolder
import com.qihuan.wanandroid.model.Article


class HomeAdapter(
    context: Context,
    data: List<Article>,
    private val homeVM: IHomeViewModel,
) : EAdapter<Article>(context, data) {

    override fun eCreateViewHolder(parent: ViewGroup, viewType: Int): EViewHolder<Article> {
        return ArticleVH(parent, homeVM)
    }

    class ArticleVH(parent: ViewGroup, vm: IHomeViewModel) :
        EViewHolder<Article>(parent, R.layout.item_article) {
        override fun setData(data: Article) {
            var displayTag: String? = when {
                data.type == 1 -> "置顶"
                data.fresh -> "新"
                else -> null
            }

            val displayAuthor = when {
                data.author.isNotBlank() -> data.author
                data.shareUser.isNotBlank() -> {
                    displayTag = if (displayTag == null) "转" else "$displayTag·转"// 标识 tag
                    data.shareUser
                }
                else -> ""
            }

            getTextView(R.id.item_article_author)?.text = displayAuthor
            getTextView(R.id.item_article_content)?.text = data.title // ?: data.desc
            getTextView(R.id.item_article_chapter)?.text = data.superChapterName
            getTextView(R.id.item_article_time)?.text =
                if (data.niceDate.isNotBlank()) data.niceDate else data.publishTime.toString()

            getTextView(R.id.item_article_refresh)?.let {
                if (displayTag == null) {
                    it.visibility = View.GONE
                } else {
                    it.visibility = View.VISIBLE
                    it.text = displayTag
                }
            }

            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.link))
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                ActivityUtils.startActivity(intent)
            }


        }
    }
}
