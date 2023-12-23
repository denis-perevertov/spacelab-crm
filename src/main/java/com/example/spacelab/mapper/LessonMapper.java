package com.example.spacelab.mapper;

import com.example.spacelab.dto.lesson.LessonInfoDTO;
import com.example.spacelab.dto.lesson.LessonListDTO;
import com.example.spacelab.dto.lesson.LessonReportRowDTO;
import com.example.spacelab.dto.lesson.LessonSaveBeforeStartDTO;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.lesson.LessonReport;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.service.AdminService;
import com.example.spacelab.service.CourseService;
import com.example.spacelab.model.lesson.LessonStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LessonMapper {
    private final CourseService courseService;
    private final AdminService adminService;
    public LessonListDTO  fromLessonToLessonListDTO(Lesson lesson) {
        LessonListDTO dto = new LessonListDTO();
        dto.setId(lesson.getId());
        dto.setDatetime(lesson.getDatetime());

        Course course = lesson.getCourse();
        if (course != null) {
            dto.setCourseId(course.getId());
            dto.setCourseName(course.getName());

            if(course.getMentor() != null) {
                dto.setMentorId(course.getMentor().getId());
                dto.setMentorName(course.getMentor().getFullName());
            }

            if(course.getManager() != null) {
                dto.setManagerId(course.getManager().getId());
                dto.setManagerName(course.getManager().getFullName());
            }
        }

        dto.setLink(lesson.getLink());
        dto.setStatus(lesson.getStatus().toString());

        if(lesson.getLessonReport() != null) {
            dto.setPresentStudentsQuantity(
                    lesson.getLessonReport()
                            .getRows()
                            .stream()
                            .filter(LessonReportRow::getWasPresent)
                            .count()
                            +
                            " / "
                            +
                            lesson.getLessonReport().getRows().size()
            );
        }

        return dto;
    }

    public List<LessonListDTO> lessonsToLessonListDTOs(List<Lesson> lessons) {
        List<LessonListDTO> dtos = new ArrayList<>();
        for (Lesson lesson : lessons) {
            dtos.add(fromLessonToLessonListDTO(lesson));
        }
        return dtos;
    }

    public  Page<LessonListDTO> pageLessonToPageLessonListDTO(Page<Lesson> lessonPage) {
        List<Lesson> lessonList = lessonPage.getContent();
        List<LessonListDTO> lessonDTOList = lessonsToLessonListDTOs(lessonList);

        return new PageImpl<>(lessonDTOList, lessonPage.getPageable(), lessonPage.getTotalElements());
    }

    public LessonInfoDTO fromLessonToLessonInfoDTO(Lesson lesson) {
        LessonInfoDTO lessonInfoDTO = new LessonInfoDTO();
        lessonInfoDTO.setId(lesson.getId());
        lessonInfoDTO.setDatetime(lesson.getDatetime());
        lessonInfoDTO.setStatus(lesson.getStatus().toString());

        List<LessonReportRowDTO> lessonReportRowDTOList = new ArrayList<>();
        if (lesson.getLessonReport() != null && lesson.getLessonReport().getRows() != null) {
            for (LessonReportRow lessonReportRow : lesson.getLessonReport().getRows()) {
                LessonReportRowDTO lessonReportRowDTO = new LessonReportRowDTO();
                
                lessonReportRowDTO.setId(lessonReportRow.getId());
                
                lessonReportRowDTO.setStudent(lessonReportRow.getStudent().getDetails().getFirstName()+" "
                        +lessonReportRow.getStudent().getDetails().getLastName()+" "
                        +lessonReportRow.getStudent().getDetails().getFathersName());
                
                lessonReportRowDTO.setWasPresent(lessonReportRow.getWasPresent());

                List<String> taskNames = new ArrayList<>();
                for (StudentTask currentTask : lessonReportRow.getStudent().getTasks()) {
                    String taskName = currentTask.getTaskReference().getName();
                    String taskPercent = "";
                    if (currentTask.getPercentOfCompletion() != null ) {
                        taskPercent = " ("+currentTask.getPercentOfCompletion()+"%)";
                    }
                taskNames.add(taskName + taskPercent); }
                lessonReportRowDTO.setCurrentTasks(taskNames);
                
                lessonReportRowDTO.setHours(lessonReportRow.getHours());
                lessonReportRowDTO.setHoursNote(lessonReportRow.getHoursNote());
                lessonReportRowDTO.setComment(lessonReportRow.getComment());
                lessonReportRowDTO.setRating(lessonReportRow.getRating());
                lessonReportRowDTOList.add(lessonReportRowDTO);
            }
        }
        lessonInfoDTO.setLessonReportRowList(lessonReportRowDTOList);

        return lessonInfoDTO;

    }

    public Lesson BeforeStartDTOtoLesson(LessonSaveBeforeStartDTO dto) {
        Lesson lesson = new Lesson();

        if(dto.getId() != null && dto.getId()>0) lesson.setId(dto.getId());
        lesson.setDatetime(dto.getLessonStartTime());
        Course course = courseService.getCourseById(dto.getCourseID());
        lesson.setCourse(course);
        lesson.setStatus(dto.getStatus());
        lesson.setLink(dto.getLink());
        lesson.setStartsAutomatically(dto.getStartsAutomatically());

        return lesson;
    }

    public LessonSaveBeforeStartDTO fromLessonToSaveDTO(Lesson lesson) {
        LessonSaveBeforeStartDTO dto = new LessonSaveBeforeStartDTO();

        dto.setId(lesson.getId());
        dto.setLessonStartTime(lesson.getDatetime());
        dto.setCourseID(lesson.getCourse().getId());
        dto.setLink(lesson.getLink());
        dto.setStatus(lesson.getStatus());
        dto.setStartsAutomatically(lesson.getStartsAutomatically());

        return dto;
    }
}
