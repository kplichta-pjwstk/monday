package com.example.monday.service;

import com.example.monday.data.Student;
import com.example.monday.data.StudentRepository;
import com.example.monday.data.StudentUnit;
import lombok.extern.java.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


//Rozszerzamy junit rozszerzeniem z mockito, aby móc w junitowych testach korzystać z funkcji biblioteki mockito
@ExtendWith(MockitoExtension.class)
class MockStudentServiceTest {

    //Mock tworzy nam proxy naszej klasy - to sprawia, że wywołania tej klasy nie wykonają rzeczywistej metody
    //i każdorazowe jej wywołanie musimy skonfigurować, możemy też tak jak w przypadku Spy śledzić jej wywołania
    @Mock
    private StudentRepository studentRepository;

    //InjectMocks pozwala nam stworzyć klasę testowaną z wykorzystaniem obiektów, które zdefiniowaliśmy
    //jako elementy mockito używając adnotacji Mock/Spy
    @InjectMocks
    private StudentService studentService;


    @Test
    void givenGdanskUnitWhenSaveStudentThenGetValidIndex() {
        //given
        var student = new Student(UUID.fromString("193c30a0-2c73-4229-989c-c257c05a9413"), "Karola", StudentUnit.GDANSK, null);
        /**poniżej przykład konfigurowania zachowania mocka przy wywołaniu konkrentej metody
         */
        when(studentRepository.getMaxIndex()).thenReturn(5L);

        //when
        var savedStudent = studentService.saveStudent(student);

        //then
        assertEquals(student.id(), savedStudent.id());
        assertEquals(student.name(), savedStudent.name());
        assertEquals(student.unit(), savedStudent.unit());
        assertEquals(25, savedStudent.index());
        verify(studentRepository, times(1)).saveStudent(any());
    }


    @Test
    void givenWarszawaUnitWhenSaveStudentThenGetValidIndex() {
        //given
        var student = new Student(UUID.fromString("193c30a0-2c73-4229-989c-c257c05a9413"), "Karola", StudentUnit.WARSZAWA, null);
        /** poniżej przykład konfigurowania zachowania mocka przy wywołaniu konkrentej metody
        zachowanie analogiczne jak powyżej, inny zapis
         */
        doReturn(7L).when(studentRepository).getMaxIndex();

        //when
        studentService.saveStudent(student);

        //then
        /** ArgumentCaptor pozwala nam odczytać wartość parametru przekazanego do metody wywołanej w ramach mocka
         */
        ArgumentCaptor<Student> argumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository, times(1)).saveStudent(argumentCaptor.capture());
        var savedStudent = argumentCaptor.getValue();
        assertEquals(student.id(), savedStudent.id());
        assertEquals(student.name(), savedStudent.name());
        assertEquals(student.unit(), savedStudent.unit());
        assertEquals(70, savedStudent.index());
    }
}