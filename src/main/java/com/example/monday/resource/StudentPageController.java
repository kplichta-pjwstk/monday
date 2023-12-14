package com.example.monday.resource;

import com.example.monday.data.StudentRepository;
import com.example.monday.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller //Użycie tej adnotacji definiuje Springowego beana, od @RestController, którego używaliśmy do tej pory różni się tym,
//że dedytkowany jest do udostępniania stron w formacie html, a nie w postacji json jak to ma miejsce w przypadku restowego kontrolera
//jest to też element Controller z wzorca MVC (model-view-controller)
@RequestMapping("/students-page")
@RequiredArgsConstructor
public class StudentPageController {

    private final StudentRepository studentRepository;
    private final StudentService studentService;

    @GetMapping//korzystamy z adnotacji znanych nam z restowych kontrollerów, żeby określić metodę (opcjonalnie też ścieżkę do metody)
    public String returnStudentsPage(Model model, String name) { // jeśli chcemy operować danymi na widoku to w parametrze zawsze musimy pobrać Model - jest to model ze wzorca MVC
        model.addAttribute("name", name); //dodanie do modelu atrybutu o konkretnej nazwie pozwoli nam następnie na widoku pobrać jego wartość po nadanej tu nazwie
        var students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "index"; //z metod zawsze zwracamy Stringa i jako wartość wstawiamy nazwę szablonu thymeleafowego (pliku html)
    }

    @GetMapping("/add") // ustawiamy ścieżkę, do zwrócenia stron zawsze używamy metody GET, jest to standardowa metoda
    //jakiej używają przeglądarki do pobrania strony
    public String displayAddStudentPage(Model model) {
        model.addAttribute("student", new CreateStudent()); // dodajemy pusty obiekt do formularza tak, aby użytkownik mógł do niego wprowadzić wartości
        //jeśli byśmy tego nie zrobili otrzymalibyśmy błąd, ponieważ nie możemy korzystać w javie (a co za tym idzie w thymeleaf) z niezainicjalizowanych obiektów
        //jeśli obiekt ten miałby ustawioną jakąś wartość byłaby ona od razu wprowadzona na wyświetlonym formularzu
        return "addStudent";
    }

    @PostMapping// tu pojawia się post, gdyż jest to akcja wywoływana z jednego z szablonów,
    //a nie na podstawie adresu z przeglądarki
    public String saveStudent(@ModelAttribute CreateStudent createStudent) {
        studentService.saveStudent(createStudent);
        return "redirect:/students-page";//jako, że nie wchodzimy to bezpośrednio na stronę, a akcja dzieje się po kliknięciu przycisku
        //do nazwy szablonu musimy dodać 'redirect:{ścieżka do strony}' aby zostać przeniesionym po kliknięciu przycisku na inną stronę
        //robimy to tylko w przypadku gdy jest to przeniesienie na podstawie akcji użytkownika, jeśli udostępniamy stronę na podstawie adresu w przeglądarce podajemy tylko nazwę szablonu
    }
}
