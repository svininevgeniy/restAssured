package pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateStudentRequest {

    @Setter @Getter
    private Integer id;
    @Setter @Getter
    private String name;
    @Setter @Getter
    private ArrayList<Integer> marks;

    public CreateStudentRequest(int id, ArrayList<Integer> integers) {
    }

    public CreateStudentRequest(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
