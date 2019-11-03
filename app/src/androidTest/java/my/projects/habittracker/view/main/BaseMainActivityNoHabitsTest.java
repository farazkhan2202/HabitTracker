package my.projects.habittracker.view.main;

import org.junit.Rule;

import java.util.concurrent.CountDownLatch;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import my.projects.habittracker.model.repository.db.HabitsDatabase;
import my.projects.habittracker.view.util.AppExecutors;

public class BaseMainActivityNoHabitsTest {

    @Rule
    public IntentsTestRule<MainActivity> activityTestRule = new IntentsTestRule<MainActivity>(MainActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            try {
                cleanDB();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    };

    private HabitsDatabase db;

    private void cleanDB() throws InterruptedException {
        db = HabitsDatabase.getInstance(InstrumentationRegistry.getInstrumentation().getTargetContext());
        final CountDownLatch latch = new CountDownLatch(1);
        AppExecutors.diskIO.execute(new Runnable() {
            @Override
            public void run() {
                db.clearAllTables();
                latch.countDown();
            }
        });
        latch.await();
    }
}
