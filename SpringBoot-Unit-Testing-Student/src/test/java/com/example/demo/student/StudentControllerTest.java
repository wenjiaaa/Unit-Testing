package com.example.demo.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


/**
 * @author kongwenjia
 * @date 2023/9/11 22:35
 * @description Controller 测试，使用 mockMVC
 */
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @MockBean
    private StudentService studentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    @DisplayName("获取所有学生")
    void getAllStudents() throws Exception {
        // given
        Student student1 = new Student(1L, "John", "john@example.com",Gender.FEMALE);
        Student student2 = new Student(2L, "Jane", "jane@example.com",Gender.FEMALE);
        List<Student> students = Arrays.asList(student1, student2);
        Mockito.when(studentService.getAllStudents()).thenReturn(students);
        // when & then
        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jane"))
                .andExpect(jsonPath("$[1].email").value("jane@example.com"));
    }

    @Test
    @DisplayName("添加学生")
    public void testAddStudent() throws Exception {
        // given
        Student student = new Student(1L, "John", "john@example.com",Gender.FEMALE);
        // when
        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());
        // then
        // 验证执行过 studentService.addStudent(student); 即可
        Mockito.verify(studentService).addStudent(student);
    }

    @Test
    @DisplayName("删除学生")
    public void testDeleteStudent() throws Exception {
        // given
        Long studentId = 1L;
        // when
        mockMvc.perform(delete("/api/v1/students/{studentId}", studentId))
                .andExpect(status().isOk());
        //then
        // 验证执行过 studentService.deleteStudent(studentId); 即可
        Mockito.verify(studentService).deleteStudent(studentId);
    }
}