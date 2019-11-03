package my.projects.habittracker.view.main;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.util.concurrent.CountDownLatch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import my.projects.habittracker.TestDataProvider;
import my.projects.habittracker.model.repository.HabitRepository;
import my.projects.habittracker.model.repository.db.HabitsDatabase;
import my.projects.habittracker.view.util.AppExecutors;

public class BaseMainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> activityTestRule = new IntentsTestRule<MainActivity>(MainActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            try {
                setupAndPopulateDB();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    };

    private HabitsDatabase db;

    public void setupAndPopulateDB() throws Throwable {
        db = HabitsDatabase.getInstance(InstrumentationRegistry.getInstrumentation().getTargetContext());
        clearTablesInDB();
        repopulateDatabase();
    }

    private void repopulateDatabase() throws Throwable {
        HabitRepository repository = db.repository();
        CountDownLatch popLatch = TestDataProvider.populateRepository(repository, AppExecutors.diskIO);
        popLatch.await();
    }

    private void clearTablesInDB() throws InterruptedException {
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
