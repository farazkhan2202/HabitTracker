package my.projects.habittracker.view.util;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    public static final Executor diskIO = Executors.newSingleThreadExecutor();

    public static final Executor mainThread = new MainThreadExecutor();

    private static class MainThreadExecutor implements Executor {

        private Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            handler.post(command);
        }
    }

}
