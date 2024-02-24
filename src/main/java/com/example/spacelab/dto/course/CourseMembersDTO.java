package com.example.spacelab.dto.course;

import com.example.spacelab.dto.admin.AdminAvatarDTO;
import com.example.spacelab.dto.student.StudentAvatarDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseMembersDTO {

    private AdminAvatarDTO mentor;
    private AdminAvatarDTO manager;
    private List<StudentAvatarDTO> students;

}
