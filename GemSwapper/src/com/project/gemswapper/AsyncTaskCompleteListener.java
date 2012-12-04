package com.project.gemswapper;

// onTaskComplete is used as a callback, and must always be implemented by the classed
// who implement this listener. The asyncTask will call this callback in it's onPostExecute method

public interface AsyncTaskCompleteListener<T> {
	public void onTaskComplete(T result);
}
