/* (C)2020 */
package com.qihuan.wanandroid.ui.project

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qihuan.wanandroid.app.BaseListViewModel
import com.qihuan.wanandroid.app.IListViewModel
import com.qihuan.wanandroid.app.applyResponseTransform
import com.qihuan.wanandroid.app.applyUIAsync
import com.qihuan.wanandroid.eRecycle.EDiffCallback
import com.qihuan.wanandroid.eRecycle.LoadEvent
import com.qihuan.wanandroid.eRecycle.LoadEventType
import com.qihuan.wanandroid.model.Project
import com.qihuan.wanandroid.network.RetrofitApi
import io.reactivex.Observable

class ProjectViewModel @ViewModelInject constructor(
    private val projectService: RetrofitApi.ProjectApi,
) : BaseListViewModel<Project>(),
    IProjectViewModel {

    private var projectListLV = MutableLiveData<LoadEvent<Project>>().apply {
        value = LoadEvent(LoadEventType.load_new_empty, emptyList())
    }

    override fun getProjectListLv(): LiveData<LoadEvent<Project>> = projectListLV

    override fun showData(loadEvent: LoadEvent<Project>) {
        projectListLV.postValue(loadEvent)
    }

    override fun doLoadData(loadNew: Boolean, skipCache: Boolean): Observable<List<Project>> {
        return projectService.listProject(page)
            .compose(applyResponseTransform())
            .compose(applyUIAsync())
            .map { it.data?.dataList ?: emptyList() }
    }

    companion object {
        val DIFF_PROJECT = EDiffCallback.diffItemCallbackHelper<Project>(
            areItemsTheSame = { oldItem, newItem ->
                return@diffItemCallbackHelper oldItem.id == newItem.id
            },
            areContentsTheSame = { oldItem, newItem ->
                if (oldItem.apkLink != newItem.apkLink) return@diffItemCallbackHelper false
                if (oldItem.audit != newItem.audit) return@diffItemCallbackHelper false
                if (oldItem.author != newItem.author) return@diffItemCallbackHelper false
                if (oldItem.canEdit != newItem.canEdit) return@diffItemCallbackHelper false
                if (oldItem.chapterId != newItem.chapterId) return@diffItemCallbackHelper false
                if (oldItem.chapterName != newItem.chapterName) return@diffItemCallbackHelper false
                if (oldItem.collect != newItem.collect) return@diffItemCallbackHelper false
                if (oldItem.courseId != newItem.courseId) return@diffItemCallbackHelper false
                if (oldItem.desc != newItem.desc) return@diffItemCallbackHelper false
                if (oldItem.descMd != newItem.descMd) return@diffItemCallbackHelper false
                if (oldItem.envelopePic != newItem.envelopePic) return@diffItemCallbackHelper false
                if (oldItem.fresh != newItem.fresh) return@diffItemCallbackHelper false
                if (oldItem.link != newItem.link) return@diffItemCallbackHelper false
                if (oldItem.niceDate != newItem.niceDate) return@diffItemCallbackHelper false
                if (oldItem.niceShareDate != newItem.niceShareDate) return@diffItemCallbackHelper false
                if (oldItem.origin != newItem.origin) return@diffItemCallbackHelper false
                if (oldItem.prefix != newItem.prefix) return@diffItemCallbackHelper false
                if (oldItem.projectLink != newItem.projectLink) return@diffItemCallbackHelper false
                if (oldItem.publishTime != newItem.publishTime) return@diffItemCallbackHelper false
                if (oldItem.realSuperChapterId != newItem.realSuperChapterId) return@diffItemCallbackHelper false
                if (oldItem.selfVisible != newItem.selfVisible) return@diffItemCallbackHelper false
                if (oldItem.shareDate != newItem.shareDate) return@diffItemCallbackHelper false
                if (oldItem.shareUser != newItem.shareUser) return@diffItemCallbackHelper false
                if (oldItem.superChapterId != newItem.superChapterId) return@diffItemCallbackHelper false
                if (oldItem.superChapterName != newItem.superChapterName) return@diffItemCallbackHelper false
                if (oldItem.tags != newItem.tags) return@diffItemCallbackHelper false
                if (oldItem.title != newItem.title) return@diffItemCallbackHelper false
                if (oldItem.type != newItem.type) return@diffItemCallbackHelper false
                if (oldItem.userId != newItem.userId) return@diffItemCallbackHelper false
                if (oldItem.visible != newItem.visible) return@diffItemCallbackHelper false
                if (oldItem.zan != newItem.zan) return@diffItemCallbackHelper false
                return@diffItemCallbackHelper true
            }
        )
    }
}

interface IProjectViewModel : IListViewModel<Project> {
    fun getProjectListLv(): LiveData<LoadEvent<Project>>
}
