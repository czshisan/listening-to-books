package service;

import dao.StoryDao;
import model.Story;

import javax.servlet.http.Part;
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


    public void save(int aid, String name, InputStream is) throws SQLException {
        storyDao.insert(aid, name, is);
    }
}
