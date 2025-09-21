package api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCreds {

    private String email;

    private String password;

    public static UserCreds getUserCreds(User user) {
        return new UserCreds(user.getEmail(), user.getPassword());
    }
}
