@Controller
public class ReserveViewController {

    @GetMapping("/reserve")
    public String reservePage() {
        return "reserve"; // templates/reserve.html 렌더링
    }
}
