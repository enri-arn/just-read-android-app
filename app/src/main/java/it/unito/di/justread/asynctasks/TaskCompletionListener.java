package it.unito.di.justread.asynctasks;

public interface TaskCompletionListener<T> {
    void onComplete(T result);
}
