package pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
public class GetTopStudentResponse {
    @Getter @Setter
    private ArrayList<CreateStudentRequest> listOfStudents;

}
