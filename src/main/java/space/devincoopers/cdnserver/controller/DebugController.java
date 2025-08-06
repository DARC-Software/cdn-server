//package space.devincoopers.cdnserver.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/**")
//public class DebugController {
//
//    @GetMapping
//    public ResponseEntity<String> logRequest(HttpServletRequest request) {
//        String path = request.getRequestURI();
//        System.out.println("➡️ Incoming Request URI: " + path);
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body("Requested: " + path);
//    }
//}
