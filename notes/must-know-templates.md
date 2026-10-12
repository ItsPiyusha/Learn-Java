Controller
```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController{
    @Autowired
    private EmployeeService employeeService;
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(
        @PathVariable Long id){
            return ResponseEntity.ok(
                employeeService.getEmployee(id));
        }
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(
        @RequestBody EmployeeDto employeeDto){
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.create(employeeDto));
        }
}
```

```java
@Service
public class EmployeeService{
    @Autowired
    private EmployeeRepository repository;

    public EmployeeDto getEmployee(Long id){
        Employee employee = repository.findById(id)
                                      .orElseThrow(()-> new ResourceNotFoundException("Employee not found"));
        return mapToDto(employee);
    }
}
```

```java
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{}
```

```java
@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(
        ResourceNotFoundException ex){
            return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);
        }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(
        Exception ex){
            return new ResponseEntity<>(
                "Something went wrong",
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
```

```java
@Component
public class JwtUtil{
    private String secretKey = "secret";
    public String generateToken(String username){
        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(new Date())
                   .setExpiration(
                        new Date(System.currentTimeMillis()
                            + 1000*60*60))
                   .signWith(SignatureAlgorithm.HS256,
                        secretKey)
                   .compact();
    }

    public String extractUsername(String token){
        return Jwts.parser()
                   .setSigningKey(secretKey)
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }
}
```

```java

```