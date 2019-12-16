package com.soulkey.applemint.ui.analyze

import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dropbox.core.v2.DbxClientV2
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.soulkey.applemint.data.ArticleRepository
import timber.log.Timber

class AnalyzeViewModel(private val dapinaClient: DbxClientV2, private val articleRepository: ArticleRepository) : ViewModel() {
    private val savePrefixPath = "/test/"
    private val validFileRegex = Regex("[\\\\/:*?\"\"<>|]")
    private val targetFbId: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val targetTitle: MutableLiveData<String> by lazy {MutableLiveData<String>()}
    val mediaContents: MutableLiveData<List<String>> = MutableLiveData(listOf())
    val externalLinks: MutableLiveData<List<String>> = MutableLiveData(listOf())

    fun callAnalyze(fb_id: String) {
        Firebase.functions.getHttpsCallable("analyze").call(hashMapOf("id" to fb_id))
            .addOnCompleteListener {task->
                if (task.isSuccessful){
                    task.result?.let {analyzeResult->
                        analyzeResult.data?.let { data->
                            val result = data as HashMap<*, *>
                            targetTitle.value = result["title"] as String?
                            targetFbId.value = result["targetFbId"] as String?
                            mediaContents.value = result["midiContents"] as List<String>
                            externalLinks.value = result["extContents"] as List<String>
                        }

                    }
                } else {
                    Timber.v("diver:/ Analyze Call is Failed..")
                }
            }
    }

    fun updateTitle(): Task<Void>{
        return articleRepository.updateArticleTitle(targetFbId.value!!, targetTitle.value!!)
    }

    private fun replaceFileName(path: String, targetName: String): String{
        path.replace("\\", "/").split("/").also { pathBlocks->
            val fileName = pathBlocks[pathBlocks.size-1]
            val extension = fileName.substring(fileName.indexOfLast {it == '.'})
            val saveTitle = targetName.replace(validFileRegex, "_")
            return saveTitle+extension
        }
    }

    fun dapina(){
        mediaContents.value?.let {mediaUrls->
            if (mediaUrls.size == 1){
                //single media
                val targetName = replaceFileName(mediaUrls[0], targetTitle.value!!)
                Timber.v("diver:/ $targetName")
                AsyncTask.execute {
                    dapinaClient.files().saveUrl(savePrefixPath+targetName, mediaUrls[0])
                }
            }else {
                //multi media
                val folderName = targetTitle.value!!.replace(validFileRegex, "_")
                mediaUrls.map { url->
                    (mediaUrls.indexOf(url)+1).toString().padStart(4, '0').also {
                        val targetPath = """$savePrefixPath$folderName/${replaceFileName(url, it)}"""
                        AsyncTask.execute {
                            dapinaClient.files().saveUrl(targetPath, url)
                        }
                    }
                }
            }
        }
    }
}