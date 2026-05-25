package interfaces;
import model.User;

public interface UserSearchable {
    User searchUserById(int userId);
}
