package forApi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter @Setter
@AllArgsConstructor
public class UserInfo {

    private String email;

    private String name;

    public UserInfo() { }
}
