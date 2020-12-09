/* (C)2020 */
package com.qihuan.wanandroid.ui.home

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.qihuan.wanandroid.R
import com.qihuan.wanandroid.app.LogUtil
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
            getTextView(R.id.item_article_author)?.text = data.author
            getTextView(R.id.item_article_content)?.text = data.title // ?: data.desc
            getTextView(R.id.item_article_chapter)?.text = data.superChapterName
            getTextView(R.id.item_article_time)?.text =
                if (data.niceDate.isNotBlank()) data.niceDate else data.publishTime.toString()

            getTextView(R.id.item_article_refresh)?.visibility =
                if (data.fresh) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                LogUtil.d { data.toString() }
            }
        }
    }
}
