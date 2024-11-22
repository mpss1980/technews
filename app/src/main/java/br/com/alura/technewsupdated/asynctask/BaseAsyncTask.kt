package br.com.alura.technewsupdated.asynctask

import android.os.AsyncTask

class BaseAsyncTask<T>(
    private val onExecute: () -> T,
    private val onFinished: (result: T) -> Unit
): AsyncTask<Void, Void, T>() {

    override fun doInBackground(vararg params: Void?) = onExecute()

    override fun onPostExecute(result: T) {
        super.onPostExecute(result)
        onFinished(result)
    }
}