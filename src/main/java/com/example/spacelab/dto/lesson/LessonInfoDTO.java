package com.example.spacelab.dto.lesson;

import com.example.spacelab.dto.admin.AdminAvatarDTO;
import com.example.spacelab.dto.course.CourseLinkIconDTO;
import com.example.spacelab.dto.student.StudentAvatarDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class LessonInfoDTO {

    @Schema(example = "10")
    private Long id;

    private LocalDateTime datetime;

    @Schema(example = "ACTIVE")
    private String status;

    private String link;

    private CourseLinkIconDTO course;

    private AdminAvatarDTO mentor;

    private List<StudentAvatarDTO> students;

    private List<LessonReportRowDTO> lessonReportRowList;

}
