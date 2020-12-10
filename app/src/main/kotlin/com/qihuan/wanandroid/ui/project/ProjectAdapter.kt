/* (C)2020 */
package com.qihuan.wanandroid.ui.project

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ActivityUtils
import com.qihuan.wanandroid.R
import com.qihuan.wanandroid.eRecycle.EAdapter
import com.qihuan.wanandroid.eRecycle.EViewHolder
import com.qihuan.wanandroid.model.Project

class ProjectAdapter(
    context: Context,
    data: List<Project>,
    private val projectVM: IProjectViewModel,
) : EAdapter<Project>(context, data) {

    override fun eCreateViewHolder(parent: ViewGroup, viewType: Int): EViewHolder<Project> {
        return ProjectVH(parent, projectVM)
    }

    class ProjectVH(parent: ViewGroup, homeVM: IProjectViewModel) :
        EViewHolder<Project>(parent, R.layout.item_article) {
        override fun setData(data: Project) {
            // TODO 当前和articleVH相同
            var displayTag: String? = when {
                data.type == 1 -> "置顶"
                data.fresh -> "新"
                else -> null
            }

            val displayAuthor = when {
                data.author.isNotBlank() -> data.author
                data.shareUser.isNotBlank() -> {
                    displayTag = if (displayTag == null) "转" else "$displayTag·转" // 标识 tag
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
