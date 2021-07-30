package service;

import dao.StoryDao;
import model.Story;

import java.io.InputStream;
import java.sql.SQLException;

public class StoryService {
    private StoryDao storyDao = new StoryDao();

    public Story detail(int sid) throws SQLException {
        return storyDao.selectOneUsingSid(sid);
    }

    public InputStream getAudio(int sid) throws SQLException {
        return storyDao.selectOneAudioColumnUsingSid(sid);
    }
}
