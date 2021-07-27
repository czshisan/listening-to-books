package service;

import dao.StoryDao;
import model.Story;

import java.sql.SQLException;

public class StoryService {
    private StoryDao storyDao = new StoryDao();

    public Story detail(int sid) throws SQLException {
        return storyDao.selectOneUsingSid(sid);
    }
}
