package edu.ucsd.cse110.successorator.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;


//THIS IS ONLY USED FOR TESTING
//THIS IS ONLY USED FOR TESTING
//THIS IS ONLY USED FOR TESTING
//THIS IS ONLY USED FOR TESTING
//THIS IS ONLY USED FOR TESTING
@Database(entities = {TaskEntity.class}, version=1)
public abstract class MockDatabase extends RoomDatabase  {
    public abstract TaskDao dao();
}
